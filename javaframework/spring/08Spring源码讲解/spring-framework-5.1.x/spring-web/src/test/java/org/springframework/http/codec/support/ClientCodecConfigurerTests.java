/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.http.codec.support;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import reactor.core.publisher.Flux;

import org.springframework.core.ResolvableType;
import org.springframework.core.codec.ByteArrayDecoder;
import org.springframework.core.codec.ByteArrayEncoder;
import org.springframework.core.codec.ByteBufferDecoder;
import org.springframework.core.codec.ByteBufferEncoder;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.codec.DataBufferDecoder;
import org.springframework.core.codec.DataBufferEncoder;
import org.springframework.core.codec.Decoder;
import org.springframework.core.codec.Encoder;
import org.springframework.core.codec.ResourceDecoder;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageReader;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.http.codec.ServerSentEventHttpMessageReader;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.json.Jackson2SmileDecoder;
import org.springframework.http.codec.json.Jackson2SmileEncoder;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.http.codec.protobuf.ProtobufDecoder;
import org.springframework.http.codec.protobuf.ProtobufHttpMessageWriter;
import org.springframework.http.codec.xml.Jaxb2XmlDecoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.util.MimeTypeUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.springframework.core.ResolvableType.forClass;

/**
 * Unit tests for {@link ClientCodecConfigurer}.
 *
 * @author Rossen Stoyanchev
 */
public class ClientCodecConfigurerTests {

	private final ClientCodecConfigurer configurer = new DefaultClientCodecConfigurer();

	private final AtomicInteger index = new AtomicInteger(0);


	@Test
	public void defaultReaders() {
		List<HttpMessageReader<?>> readers = this.configurer.getReaders();
		assertEquals(12, readers.size());
		assertEquals(ByteArrayDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(ByteBufferDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(DataBufferDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(ResourceDecoder.class, getNextDecoder(readers).getClass());
		assertStringDecoder(getNextDecoder(readers), true);
		assertEquals(ProtobufDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(FormHttpMessageReader.class, readers.get(this.index.getAndIncrement()).getClass()); // SPR-16804
		assertEquals(Jackson2JsonDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(Jackson2SmileDecoder.class, getNextDecoder(readers).getClass());
		assertEquals(Jaxb2XmlDecoder.class, getNextDecoder(readers).getClass());
		assertSseReader(readers);
		assertStringDecoder(getNextDecoder(readers), false);
	}

	@Test
	public void defaultWriters() {
		List<HttpMessageWriter<?>> writers = this.configurer.getWriters();
		assertEquals(11, writers.size());
		assertEquals(ByteArrayEncoder.class, getNextEncoder(writers).getClass());
		assertEquals(ByteBufferEncoder.class, getNextEncoder(writers).getClass());
		assertEquals(DataBufferEncoder.class, getNextEncoder(writers).getClass());
		assertEquals(ResourceHttpMessageWriter.class, writers.get(index.getAndIncrement()).getClass());
		assertStringEncoder(getNextEncoder(writers), true);
		assertEquals(ProtobufHttpMessageWriter.class, writers.get(index.getAndIncrement()).getClass());
		assertEquals(MultipartHttpMessageWriter.class, writers.get(this.index.getAndIncrement()).getClass());
		assertEquals(Jackson2JsonEncoder.class, getNextEncoder(writers).getClass());
		assertEquals(Jackson2SmileEncoder.class, getNextEncoder(writers).getClass());
		assertEquals(Jaxb2XmlEncoder.class, getNextEncoder(writers).getClass());
		assertStringEncoder(getNextEncoder(writers), false);
	}

	@Test
	public void jackson2EncoderOverride() {
		Jackson2JsonDecoder decoder = new Jackson2JsonDecoder();
		this.configurer.defaultCodecs().jackson2JsonDecoder(decoder);

		List<HttpMessageReader<?>> readers = this.configurer.getReaders();
		assertSame(decoder, findCodec(readers, ServerSentEventHttpMessageReader.class).getDecoder());
	}

	@Test
	public void maxInMemorySize() {
		int size = 99;
		this.configurer.defaultCodecs().maxInMemorySize(size);
		List<HttpMessageReader<?>> readers = this.configurer.getReaders();
		assertEquals(12, readers.size());
		assertEquals(size, ((ByteArrayDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((ByteBufferDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((DataBufferDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((ResourceDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((StringDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((ProtobufDecoder) getNextDecoder(readers)).getMaxMessageSize());
		assertEquals(size, ((FormHttpMessageReader) nextReader(readers)).getMaxInMemorySize());

		assertEquals(size, ((Jackson2JsonDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((Jackson2SmileDecoder) getNextDecoder(readers)).getMaxInMemorySize());
		assertEquals(size, ((Jaxb2XmlDecoder) getNextDecoder(readers)).getMaxInMemorySize());

		ServerSentEventHttpMessageReader reader = (ServerSentEventHttpMessageReader) nextReader(readers);
		assertEquals(size, ((Jackson2JsonDecoder) reader.getDecoder()).getMaxInMemorySize());

		assertEquals(size, ((StringDecoder) getNextDecoder(readers)).getMaxInMemorySize());
	}

	@Test
	public void enableLoggingRequestDetails() {
		this.configurer.defaultCodecs().enableLoggingRequestDetails(true);

		List<HttpMessageWriter<?>> writers = this.configurer.getWriters();
		MultipartHttpMessageWriter multipartWriter = findCodec(writers, MultipartHttpMessageWriter.class);
		assertTrue(multipartWriter.isEnableLoggingRequestDetails());

		FormHttpMessageWriter formWriter = (FormHttpMessageWriter) multipartWriter.getFormWriter();
		assertNotNull(formWriter);
		assertTrue(formWriter.isEnableLoggingRequestDetails());
	}

	@Test
	public void clonedConfigurer() {
		ClientCodecConfigurer clone = this.configurer.clone();

		Jackson2JsonDecoder jackson2Decoder = new Jackson2JsonDecoder();
		clone.defaultCodecs().serverSentEventDecoder(jackson2Decoder);
		clone.defaultCodecs().multipartCodecs().encoder(new Jackson2SmileEncoder());
		clone.defaultCodecs().multipartCodecs().writer(new ResourceHttpMessageWriter());

		// Clone has the customizations

		Decoder<?> sseDecoder = findCodec(clone.getReaders(), ServerSentEventHttpMessageReader.class).getDecoder();
		List<HttpMessageWriter<?>> writers = findCodec(clone.getWriters(), MultipartHttpMessageWriter.class).getPartWriters();

		assertSame(jackson2Decoder, sseDecoder);
		assertEquals(2, writers.size());

		// Original does not have the customizations

		sseDecoder = findCodec(this.configurer.getReaders(), ServerSentEventHttpMessageReader.class).getDecoder();
		writers = findCodec(this.configurer.getWriters(), MultipartHttpMessageWriter.class).getPartWriters();

		assertNotSame(jackson2Decoder, sseDecoder);
		assertEquals(10, writers.size());
	}

	@Test // gh-24194
	public void cloneShouldNotDropMultipartCodecs() {

		ClientCodecConfigurer clone = this.configurer.clone();
		List<HttpMessageWriter<?>> writers =
				findCodec(clone.getWriters(), MultipartHttpMessageWriter.class).getPartWriters();

		assertEquals(10, writers.size());
	}

	@Test
	public void cloneShouldNotBeImpactedByChangesToOriginal() {

		ClientCodecConfigurer clone = this.configurer.clone();

		this.configurer.registerDefaults(false);
		this.configurer.customCodecs().register(new Jackson2JsonEncoder());

		List<HttpMessageWriter<?>> writers =
				findCodec(clone.getWriters(), MultipartHttpMessageWriter.class).getPartWriters();

		assertEquals(10, writers.size());
	}

	private Decoder<?> getNextDecoder(List<HttpMessageReader<?>> readers) {
		HttpMessageReader<?> reader = readers.get(this.index.getAndIncrement());
		assertEquals(DecoderHttpMessageReader.class, reader.getClass());
		return ((DecoderHttpMessageReader<?>) reader).getDecoder();
	}

	private HttpMessageReader<?> nextReader(List<HttpMessageReader<?>> readers) {
		return readers.get(this.index.getAndIncrement());
	}

	private Encoder<?> getNextEncoder(List<HttpMessageWriter<?>> writers) {
		HttpMessageWriter<?> writer = writers.get(this.index.getAndIncrement());
		assertEquals(EncoderHttpMessageWriter.class, writer.getClass());
		return ((EncoderHttpMessageWriter<?>) writer).getEncoder();
	}

	@SuppressWarnings("unchecked")
	private <T> T findCodec(List<?> codecs, Class<T> type) {
		return (T) codecs.stream().filter(type::isInstance).findFirst().get();
	}

	@SuppressWarnings("unchecked")
	private void assertStringDecoder(Decoder<?> decoder, boolean textOnly) {
		assertEquals(StringDecoder.class, decoder.getClass());
		assertTrue(decoder.canDecode(forClass(String.class), MimeTypeUtils.TEXT_PLAIN));
		assertEquals(!textOnly, decoder.canDecode(forClass(String.class), MediaType.TEXT_EVENT_STREAM));

		Flux<String> decoded = (Flux<String>) decoder.decode(
				Flux.just(new DefaultDataBufferFactory().wrap("line1\nline2".getBytes(StandardCharsets.UTF_8))),
				ResolvableType.forClass(String.class), MimeTypeUtils.TEXT_PLAIN, Collections.emptyMap());

		assertEquals(Arrays.asList("line1", "line2"), decoded.collectList().block(Duration.ZERO));
	}

	private void assertStringEncoder(Encoder<?> encoder, boolean textOnly) {
		assertEquals(CharSequenceEncoder.class, encoder.getClass());
		assertTrue(encoder.canEncode(forClass(String.class), MimeTypeUtils.TEXT_PLAIN));
		assertEquals(!textOnly, encoder.canEncode(forClass(String.class), MediaType.TEXT_EVENT_STREAM));
	}

	private void assertSseReader(List<HttpMessageReader<?>> readers) {
		HttpMessageReader<?> reader = readers.get(this.index.getAndIncrement());
		assertEquals(ServerSentEventHttpMessageReader.class, reader.getClass());
		Decoder<?> decoder = ((ServerSentEventHttpMessageReader) reader).getDecoder();
		assertNotNull(decoder);
		assertEquals(Jackson2JsonDecoder.class, decoder.getClass());
	}

}
