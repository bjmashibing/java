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

package org.springframework.web.reactive.function;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.core.codec.ByteBufferEncoder;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.support.DataBufferTestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRange;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.FormHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.http.codec.ServerSentEventHttpMessageWriter;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.multipart.MultipartHttpMessageWriter;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.mock.http.client.reactive.test.MockClientHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static java.nio.charset.StandardCharsets.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.http.codec.json.Jackson2CodecSupport.*;

/**
 * @author Arjen Poutsma
 * @author Sebastien Deleuze
 */
public class BodyInsertersTests {

	private BodyInserter.Context context;

	private Map<String, Object> hints;


	@Before
	public void createContext() {
		final List<HttpMessageWriter<?>> messageWriters = new ArrayList<>();
		messageWriters.add(new EncoderHttpMessageWriter<>(new ByteBufferEncoder()));
		messageWriters.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.textPlainOnly()));
		messageWriters.add(new ResourceHttpMessageWriter());
		messageWriters.add(new EncoderHttpMessageWriter<>(new Jaxb2XmlEncoder()));
		Jackson2JsonEncoder jsonEncoder = new Jackson2JsonEncoder();
		messageWriters.add(new EncoderHttpMessageWriter<>(jsonEncoder));
		messageWriters.add(new ServerSentEventHttpMessageWriter(jsonEncoder));
		messageWriters.add(new FormHttpMessageWriter());
		messageWriters.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));
		messageWriters.add(new MultipartHttpMessageWriter(messageWriters));

		this.context = new BodyInserter.Context() {
			@Override
			public List<HttpMessageWriter<?>> messageWriters() {
				return messageWriters;
			}
			@Override
			public Optional<ServerHttpRequest> serverRequest() {
				return Optional.empty();
			}
			@Override
			public Map<String, Object> hints() {
				return hints;
			}
		};
		this.hints = new HashMap<>();
	}


	@Test
	public void ofString() {
		String body = "foo";
		BodyInserter<String, ReactiveHttpOutputMessage> inserter = BodyInserters.fromObject(body);

		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();
		StepVerifier.create(response.getBody())
				.consumeNextWith(buf -> {
					String actual = DataBufferTestUtils.dumpString(buf, UTF_8);
					Assert.assertEquals("foo", actual);
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void ofObject() {
		User body = new User("foo", "bar");
		BodyInserter<User, ReactiveHttpOutputMessage> inserter = BodyInserters.fromObject(body);
		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(response.getBodyAsString())
				.expectNext("{\"username\":\"foo\",\"password\":\"bar\"}")
				.expectComplete()
				.verify();
	}

	@Test
	public void ofObjectWithHints() {
		User body = new User("foo", "bar");
		BodyInserter<User, ReactiveHttpOutputMessage> inserter = BodyInserters.fromObject(body);
		this.hints.put(JSON_VIEW_HINT, SafeToSerialize.class);
		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(response.getBodyAsString())
				.expectNext("{\"username\":\"foo\"}")
				.expectComplete()
				.verify();
	}

	@Test
	public void ofPublisher() {
		Flux<String> body = Flux.just("foo");
		BodyInserter<Flux<String>, ReactiveHttpOutputMessage> inserter = BodyInserters.fromPublisher(body, String.class);

		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();
		StepVerifier.create(response.getBody())
				.consumeNextWith(buf -> {
					String actual = DataBufferTestUtils.dumpString(buf, UTF_8);
					Assert.assertEquals("foo", actual);
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void ofResource() throws IOException {
		Resource body = new ClassPathResource("response.txt", getClass());
		BodyInserter<Resource, ReactiveHttpOutputMessage> inserter = BodyInserters.fromResource(body);

		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();

		byte[] expectedBytes = Files.readAllBytes(body.getFile().toPath());

		StepVerifier.create(response.getBody())
				.consumeNextWith(dataBuffer -> {
					byte[] resultBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(resultBytes);
					DataBufferUtils.release(dataBuffer);
					assertArrayEquals(expectedBytes, resultBytes);
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void ofResourceRange() throws IOException {
		final int rangeStart = 10;
		Resource body = new ClassPathResource("response.txt", getClass());
		BodyInserter<Resource, ReactiveHttpOutputMessage> inserter = BodyInserters.fromResource(body);

		MockServerHttpRequest request = MockServerHttpRequest.get("/foo")
				.range(HttpRange.createByteRange(rangeStart))
				.build();
		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, new BodyInserter.Context() {
			@Override
			public List<HttpMessageWriter<?>> messageWriters() {
				return Collections.singletonList(new ResourceHttpMessageWriter());
			}

			@Override
			public Optional<ServerHttpRequest> serverRequest() {
				return Optional.of(request);
			}

			@Override
			public Map<String, Object> hints() {
				return hints;
			}
		});
		StepVerifier.create(result).expectComplete().verify();

		byte[] allBytes = Files.readAllBytes(body.getFile().toPath());
		byte[] expectedBytes = new byte[allBytes.length - rangeStart];
		System.arraycopy(allBytes, rangeStart, expectedBytes, 0, expectedBytes.length);

		StepVerifier.create(response.getBody())
				.consumeNextWith(dataBuffer -> {
					byte[] resultBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(resultBytes);
					DataBufferUtils.release(dataBuffer);
					assertArrayEquals(expectedBytes, resultBytes);
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void ofServerSentEventFlux() {
		ServerSentEvent<String> event = ServerSentEvent.builder("foo").build();
		Flux<ServerSentEvent<String>> body = Flux.just(event);
		BodyInserter<Flux<ServerSentEvent<String>>, ServerHttpResponse> inserter =
				BodyInserters.fromServerSentEvents(body);

		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectNextCount(0).expectComplete().verify();
	}

	@Test
	public void fromFormDataMap() {
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.set("name 1", "value 1");
		body.add("name 2", "value 2+1");
		body.add("name 2", "value 2+2");
		body.add("name 3", null);

		BodyInserter<MultiValueMap<String, String>, ClientHttpRequest>
				inserter = BodyInserters.fromFormData(body);

		MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("https://example.com"));
		Mono<Void> result = inserter.insert(request, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(request.getBody())
				.consumeNextWith(dataBuffer -> {
					byte[] resultBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(resultBytes);
					DataBufferUtils.release(dataBuffer);
					assertArrayEquals("name+1=value+1&name+2=value+2%2B1&name+2=value+2%2B2&name+3".getBytes(StandardCharsets.UTF_8),
							resultBytes);
				})
				.expectComplete()
				.verify();

	}

	@Test
	public void fromFormDataWith() {
		BodyInserter<MultiValueMap<String, String>, ClientHttpRequest>
				inserter = BodyInserters.fromFormData("name 1", "value 1")
				.with("name 2", "value 2+1")
				.with("name 2", "value 2+2")
				.with("name 3", null);

		MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("https://example.com"));
		Mono<Void> result = inserter.insert(request, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(request.getBody())
				.consumeNextWith(dataBuffer -> {
					byte[] resultBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(resultBytes);
					DataBufferUtils.release(dataBuffer);
					assertArrayEquals("name+1=value+1&name+2=value+2%2B1&name+2=value+2%2B2&name+3".getBytes(StandardCharsets.UTF_8),
							resultBytes);
				})
				.expectComplete()
				.verify();

	}

	@Test
	public void fromMultipartData() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.set("name 3", "value 3");

		BodyInserters.FormInserter<Object> inserter =
				BodyInserters.fromMultipartData("name 1", "value 1")
						.withPublisher("name 2", Flux.just("foo", "bar", "baz"), String.class)
						.with(map);

		MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("https://example.com"));
		Mono<Void> result = inserter.insert(request, this.context);
		StepVerifier.create(result).expectComplete().verify();

	}

	@Test  // SPR-16350
	public void fromMultipartDataWithMultipleValues() {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.put("name", Arrays.asList("value1", "value2"));
		BodyInserters.FormInserter<Object> inserter = BodyInserters.fromMultipartData(map);

		MockClientHttpRequest request = new MockClientHttpRequest(HttpMethod.GET, URI.create("https://example.com"));
		Mono<Void> result = inserter.insert(request, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(DataBufferUtils.join(request.getBody()))
				.consumeNextWith(dataBuffer -> {
					byte[] resultBytes = new byte[dataBuffer.readableByteCount()];
					dataBuffer.read(resultBytes);
					DataBufferUtils.release(dataBuffer);
					String content = new String(resultBytes, StandardCharsets.UTF_8);
					assertThat(content, containsString("Content-Disposition: form-data; name=\"name\"\r\n" +
							"Content-Type: text/plain;charset=UTF-8\r\n" +
							"Content-Length: 6\r\n" +
							"\r\n" +
							"value1"));
					assertThat(content, containsString("Content-Disposition: form-data; name=\"name\"\r\n" +
							"Content-Type: text/plain;charset=UTF-8\r\n" +
							"Content-Length: 6\r\n" +
							"\r\n" +
							"value2"));
				})
				.expectComplete()
				.verify();
	}

	@Test
	public void ofDataBuffers() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);

		BodyInserter<Flux<DataBuffer>, ReactiveHttpOutputMessage> inserter = BodyInserters.fromDataBuffers(body);

		MockServerHttpResponse response = new MockServerHttpResponse();
		Mono<Void> result = inserter.insert(response, this.context);
		StepVerifier.create(result).expectComplete().verify();

		StepVerifier.create(response.getBody())
				.expectNext(dataBuffer)
				.expectComplete()
				.verify();
	}


	interface SafeToSerialize {}


	@SuppressWarnings("unused")
	private static class User {

		@JsonView(SafeToSerialize.class)
		private String username;

		private String password;

		public User() {
		}

		public User(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

}
