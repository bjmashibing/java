/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.http.codec.multipart;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.CodecException;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.PooledDataBuffer;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.MultiValueMap;

/**
 * {@link HttpMessageWriter} for writing a {@code MultiValueMap<String, ?>}
 * as multipart form data, i.e. {@code "multipart/form-data"}, to the body
 * of a request.
 *
 * <p>The serialization of individual parts is delegated to other writers.
 * By default only {@link String} and {@link Resource} parts are supported but
 * you can configure others through a constructor argument.
 *
 * <p>This writer can be configured with a {@link FormHttpMessageWriter} to
 * delegate to. It is the preferred way of supporting both form data and
 * multipart data (as opposed to registering each writer separately) so that
 * when the {@link MediaType} is not specified and generics are not present on
 * the target element type, we can inspect the values in the actual map and
 * decide whether to write plain form data (String values only) or otherwise.
 *
 * @author Sebastien Deleuze
 * @author Rossen Stoyanchev
 * @since 5.0
 * @see FormHttpMessageWriter
 */
public class MultipartHttpMessageWriter extends LoggingCodecSupport
		implements HttpMessageWriter<MultiValueMap<String, ?>> {

	/**
	 * THe default charset used by the writer.
	 */
	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	/** Suppress logging from individual part writers (full map logged at this level). */
	private static final Map<String, Object> DEFAULT_HINTS = Hints.from(Hints.SUPPRESS_LOGGING_HINT, true);


	private final List<HttpMessageWriter<?>> partWriters;

	@Nullable
	private final HttpMessageWriter<MultiValueMap<String, String>> formWriter;

	private Charset charset = DEFAULT_CHARSET;

	private final List<MediaType> supportedMediaTypes;


	/**
	 * Constructor with a default list of part writers (String and Resource).
	 */
	public MultipartHttpMessageWriter() {
		this(Arrays.asList(
				new EncoderHttpMessageWriter<>(CharSequenceEncoder.textPlainOnly()),
				new ResourceHttpMessageWriter()
		));
	}

	/**
	 * Constructor with explicit list of writers for serializing parts.
	 */
	public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters) {
		this(partWriters, new FormHttpMessageWriter());
	}

	/**
	 * Constructor with explicit list of writers for serializing parts and a
	 * writer for plain form data to fall back when no media type is specified
	 * and the actual map consists of String values only.
	 * @param partWriters the writers for serializing parts
	 * @param formWriter the fallback writer for form data, {@code null} by default
	 */
	public MultipartHttpMessageWriter(List<HttpMessageWriter<?>> partWriters,
			@Nullable  HttpMessageWriter<MultiValueMap<String, String>> formWriter) {

		this.partWriters = partWriters;
		this.formWriter = formWriter;
		this.supportedMediaTypes = initMediaTypes(formWriter);
	}

	private static List<MediaType> initMediaTypes(@Nullable HttpMessageWriter<?> formWriter) {
		List<MediaType> result = new ArrayList<>();
		result.add(MediaType.MULTIPART_FORM_DATA);
		if (formWriter != null) {
			result.addAll(formWriter.getWritableMediaTypes());
		}
		return Collections.unmodifiableList(result);
	}


	/**
	 * Return the configured part writers.
	 * @since 5.0.7
	 */
	public List<HttpMessageWriter<?>> getPartWriters() {
		return Collections.unmodifiableList(this.partWriters);
	}


	/**
	 * Return the configured form writer.
	 * @since 5.1.13
	 */
	@Nullable
	public HttpMessageWriter<MultiValueMap<String, String>> getFormWriter() {
		return this.formWriter;
	}

	/**
	 * Set the character set to use for part headers such as
	 * "Content-Disposition" (and its filename parameter).
	 * <p>By default this is set to "UTF-8".
	 */
	public void setCharset(Charset charset) {
		Assert.notNull(charset, "Charset must not be null");
		this.charset = charset;
	}

	/**
	 * Return the configured charset for part headers.
	 */
	public Charset getCharset() {
		return this.charset;
	}


	@Override
	public List<MediaType> getWritableMediaTypes() {
		return this.supportedMediaTypes;
	}

	@Override
	public boolean canWrite(ResolvableType elementType, @Nullable MediaType mediaType) {
		return (MultiValueMap.class.isAssignableFrom(elementType.toClass()) &&
				(mediaType == null ||
						this.supportedMediaTypes.stream().anyMatch(element -> element.isCompatibleWith(mediaType))));
	}

	@Override
	public Mono<Void> write(Publisher<? extends MultiValueMap<String, ?>> inputStream,
			ResolvableType elementType, @Nullable MediaType mediaType, ReactiveHttpOutputMessage outputMessage,
			Map<String, Object> hints) {

		return Mono.from(inputStream)
				.flatMap(map -> {
					if (this.formWriter == null || isMultipart(map, mediaType)) {
						return writeMultipart(map, outputMessage, hints);
					}
					else {
						@SuppressWarnings("unchecked")
						Mono<MultiValueMap<String, String>> input = Mono.just((MultiValueMap<String, String>) map);
						return this.formWriter.write(input, elementType, mediaType, outputMessage, hints);
					}
				});
	}

	private boolean isMultipart(MultiValueMap<String, ?> map, @Nullable MediaType contentType) {
		if (contentType != null) {
			return MediaType.MULTIPART_FORM_DATA.includes(contentType);
		}
		for (List<?> values : map.values()) {
			for (Object value : values) {
				if (value != null && !(value instanceof String)) {
					return true;
				}
			}
		}
		return false;
	}

	private Mono<Void> writeMultipart(
			MultiValueMap<String, ?> map, ReactiveHttpOutputMessage outputMessage, Map<String, Object> hints) {

		byte[] boundary = generateMultipartBoundary();

		Map<String, String> params = new HashMap<>(2);
		params.put("boundary", new String(boundary, StandardCharsets.US_ASCII));
		params.put("charset", getCharset().name());

		outputMessage.getHeaders().setContentType(new MediaType(MediaType.MULTIPART_FORM_DATA, params));

		LogFormatUtils.traceDebug(logger, traceOn -> Hints.getLogPrefix(hints) + "Encoding " +
				(isEnableLoggingRequestDetails() ?
						LogFormatUtils.formatValue(map, !traceOn) :
						"parts " + map.keySet() + " (content masked)"));

		DataBufferFactory bufferFactory = outputMessage.bufferFactory();

		Flux<DataBuffer> body = Flux.fromIterable(map.entrySet())
				.concatMap(entry -> encodePartValues(boundary, entry.getKey(), entry.getValue(), bufferFactory))
				.concatWith(generateLastLine(boundary, bufferFactory))
				.doOnDiscard(PooledDataBuffer.class, PooledDataBuffer::release);

		return outputMessage.writeWith(body);
	}

	/**
	 * Generate a multipart boundary.
	 * <p>By default delegates to {@link MimeTypeUtils#generateMultipartBoundary()}.
	 */
	protected byte[] generateMultipartBoundary() {
		return MimeTypeUtils.generateMultipartBoundary();
	}

	private Flux<DataBuffer> encodePartValues(
			byte[] boundary, String name, List<?> values, DataBufferFactory bufferFactory) {

		return Flux.fromIterable(values)
				.concatMap(value -> encodePart(boundary, name, value, bufferFactory));
	}

	@SuppressWarnings("unchecked")
	private <T> Flux<DataBuffer> encodePart(byte[] boundary, String name, T value, DataBufferFactory bufferFactory) {
		MultipartHttpOutputMessage outputMessage = new MultipartHttpOutputMessage(bufferFactory, getCharset());
		HttpHeaders outputHeaders = outputMessage.getHeaders();

		T body;
		ResolvableType resolvableType = null;
		if (value instanceof HttpEntity) {
			HttpEntity<T> httpEntity = (HttpEntity<T>) value;
			outputHeaders.putAll(httpEntity.getHeaders());
			body = httpEntity.getBody();
			Assert.state(body != null, "MultipartHttpMessageWriter only supports HttpEntity with body");
			if (httpEntity instanceof ResolvableTypeProvider) {
				resolvableType = ((ResolvableTypeProvider) httpEntity).getResolvableType();
			}
		}
		else {
			body = value;
		}
		if (resolvableType == null) {
			resolvableType = ResolvableType.forClass(body.getClass());
		}

		if (!outputHeaders.containsKey(HttpHeaders.CONTENT_DISPOSITION)) {
			if (body instanceof Resource) {
				outputHeaders.setContentDispositionFormData(name, ((Resource) body).getFilename());
			}
			else if (resolvableType.resolve() == Resource.class) {
				body = (T) Mono.from((Publisher<?>) body).doOnNext(o -> outputHeaders
						.setContentDispositionFormData(name, ((Resource) o).getFilename()));
			}
			else {
				outputHeaders.setContentDispositionFormData(name, null);
			}
		}

		MediaType contentType = outputHeaders.getContentType();

		final ResolvableType finalBodyType = resolvableType;
		Optional<HttpMessageWriter<?>> writer = this.partWriters.stream()
				.filter(partWriter -> partWriter.canWrite(finalBodyType, contentType))
				.findFirst();

		if (!writer.isPresent()) {
			return Flux.error(new CodecException("No suitable writer found for part: " + name));
		}

		Publisher<T> bodyPublisher =
				body instanceof Publisher ? (Publisher<T>) body : Mono.just(body);

		// The writer will call MultipartHttpOutputMessage#write which doesn't actually write
		// but only stores the body Flux and returns Mono.empty().

		Mono<Void> partContentReady = ((HttpMessageWriter<T>) writer.get())
				.write(bodyPublisher, resolvableType, contentType, outputMessage, DEFAULT_HINTS);

		// After partContentReady, we can access the part content from MultipartHttpOutputMessage
		// and use it for writing to the actual request body

		Flux<DataBuffer> partContent = partContentReady.thenMany(Flux.defer(outputMessage::getBody));

		return Flux.concat(
				generateBoundaryLine(boundary, bufferFactory),
				partContent,
				generateNewLine(bufferFactory));
	}


	private Mono<DataBuffer> generateBoundaryLine(byte[] boundary, DataBufferFactory bufferFactory) {
		return Mono.fromCallable(() -> {
			DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 4);
			buffer.write((byte)'-');
			buffer.write((byte)'-');
			buffer.write(boundary);
			buffer.write((byte)'\r');
			buffer.write((byte)'\n');
			return buffer;
		});
	}

	private Mono<DataBuffer> generateNewLine(DataBufferFactory bufferFactory) {
		return Mono.fromCallable(() -> {
			DataBuffer buffer = bufferFactory.allocateBuffer(2);
			buffer.write((byte)'\r');
			buffer.write((byte)'\n');
			return buffer;
		});
	}

	private Mono<DataBuffer> generateLastLine(byte[] boundary, DataBufferFactory bufferFactory) {
		return Mono.fromCallable(() -> {
			DataBuffer buffer = bufferFactory.allocateBuffer(boundary.length + 6);
			buffer.write((byte)'-');
			buffer.write((byte)'-');
			buffer.write(boundary);
			buffer.write((byte)'-');
			buffer.write((byte)'-');
			buffer.write((byte)'\r');
			buffer.write((byte)'\n');
			return buffer;
		});
	}


	private static class MultipartHttpOutputMessage implements ReactiveHttpOutputMessage {

		private final DataBufferFactory bufferFactory;

		private final Charset charset;

		private final HttpHeaders headers = new HttpHeaders();

		private final AtomicBoolean committed = new AtomicBoolean();

		@Nullable
		private Flux<DataBuffer> body;

		public MultipartHttpOutputMessage(DataBufferFactory bufferFactory, Charset charset) {
			this.bufferFactory = bufferFactory;
			this.charset = charset;
		}

		@Override
		public HttpHeaders getHeaders() {
			return (this.body != null ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
		}

		@Override
		public DataBufferFactory bufferFactory() {
			return this.bufferFactory;
		}

		@Override
		public void beforeCommit(Supplier<? extends Mono<Void>> action) {
			this.committed.set(true);
		}

		@Override
		public boolean isCommitted() {
			return this.committed.get();
		}

		@Override
		public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
			if (this.body != null) {
				return Mono.error(new IllegalStateException("Multiple calls to writeWith() not supported"));
			}
			this.body = generateHeaders().concatWith(body);

			// We don't actually want to write (just save the body Flux)
			return Mono.empty();
		}

		private Mono<DataBuffer> generateHeaders() {
			return Mono.fromCallable(() -> {
				DataBuffer buffer = this.bufferFactory.allocateBuffer();
				for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
					byte[] headerName = entry.getKey().getBytes(this.charset);
					for (String headerValueString : entry.getValue()) {
						byte[] headerValue = headerValueString.getBytes(this.charset);
						buffer.write(headerName);
						buffer.write((byte)':');
						buffer.write((byte)' ');
						buffer.write(headerValue);
						buffer.write((byte)'\r');
						buffer.write((byte)'\n');
					}
				}
				buffer.write((byte)'\r');
				buffer.write((byte)'\n');
				return buffer;
			});
		}

		@Override
		public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
			return Mono.error(new UnsupportedOperationException());
		}

		public Flux<DataBuffer> getBody() {
			return (this.body != null ? this.body :
					Flux.error(new IllegalStateException("Body has not been written yet")));
		}

		@Override
		public Mono<Void> setComplete() {
			return Mono.error(new UnsupportedOperationException());
		}
	}

}
