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

package org.springframework.web.reactive.function.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.mock.http.client.reactive.test.MockClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;

import static java.nio.charset.StandardCharsets.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpMethod.*;

/**
 * @author Arjen Poutsma
 */
public class DefaultClientRequestBuilderTests {

	@Test
	public void from() throws URISyntaxException {
		ClientRequest other = ClientRequest.create(GET, URI.create("https://example.com"))
				.header("foo", "bar")
				.cookie("baz", "qux").build();
		ClientRequest result = ClientRequest.from(other)
				.headers(httpHeaders -> httpHeaders.set("foo", "baar"))
				.cookies(cookies -> cookies.set("baz", "quux"))
		.build();
		assertEquals(new URI("https://example.com"), result.url());
		assertEquals(GET, result.method());
		assertEquals(1, result.headers().size());
		assertEquals("baar", result.headers().getFirst("foo"));
		assertEquals(1, result.cookies().size());
		assertEquals("quux", result.cookies().getFirst("baz"));
	}

	@Test
	public void method() throws URISyntaxException {
		URI url = new URI("https://example.com");
		ClientRequest.Builder builder = ClientRequest.create(DELETE, url);
		assertEquals(DELETE, builder.build().method());

		builder.method(OPTIONS);
		assertEquals(OPTIONS, builder.build().method());
	}

	@Test
	public void url() throws URISyntaxException {
		URI url1 = new URI("https://example.com/foo");
		URI url2 = new URI("https://example.com/bar");
		ClientRequest.Builder builder = ClientRequest.create(DELETE, url1);
		assertEquals(url1, builder.build().url());

		builder.url(url2);
		assertEquals(url2, builder.build().url());
	}

	@Test
	public void cookie() {
		ClientRequest result = ClientRequest.create(GET, URI.create("https://example.com"))
				.cookie("foo", "bar").build();
		assertEquals("bar", result.cookies().getFirst("foo"));
	}

	@Test
	public void build() {
		ClientRequest result = ClientRequest.create(GET, URI.create("https://example.com"))
				.header("MyKey", "MyValue")
				.cookie("foo", "bar")
				.build();

		MockClientHttpRequest request = new MockClientHttpRequest(GET, "/");
		ExchangeStrategies strategies = mock(ExchangeStrategies.class);

		result.writeTo(request, strategies).block();

		assertEquals("MyValue", request.getHeaders().getFirst("MyKey"));
		assertEquals("bar", request.getCookies().getFirst("foo").getValue());
		StepVerifier.create(request.getBody()).expectComplete().verify();
	}

	@Test
	public void bodyInserter() {
		String body = "foo";
		BodyInserter<String, ClientHttpRequest> inserter =
				(response, strategies) -> {
					byte[] bodyBytes = body.getBytes(UTF_8);
					DataBuffer buffer = new DefaultDataBufferFactory().wrap(bodyBytes);

					return response.writeWith(Mono.just(buffer));
				};

		ClientRequest result = ClientRequest.create(POST, URI.create("https://example.com"))
				.body(inserter).build();

		List<HttpMessageWriter<?>> messageWriters = new ArrayList<>();
		messageWriters.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));

		ExchangeStrategies strategies = mock(ExchangeStrategies.class);
		when(strategies.messageWriters()).thenReturn(messageWriters);

		MockClientHttpRequest request = new MockClientHttpRequest(GET, "/");
		result.writeTo(request, strategies).block();
		assertNotNull(request.getBody());

		StepVerifier.create(request.getBody())
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	public void bodyClass() {
		String body = "foo";
		Publisher<String> publisher = Mono.just(body);
		ClientRequest result = ClientRequest.create(POST, URI.create("https://example.com"))
				.body(publisher, String.class).build();

		List<HttpMessageWriter<?>> messageWriters = new ArrayList<>();
		messageWriters.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));

		ExchangeStrategies strategies = mock(ExchangeStrategies.class);
		when(strategies.messageWriters()).thenReturn(messageWriters);

		MockClientHttpRequest request = new MockClientHttpRequest(GET, "/");
		result.writeTo(request, strategies).block();
		assertNotNull(request.getBody());

		StepVerifier.create(request.getBody())
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	public void bodyParameterizedTypeReference() {
		String body = "foo";
		Publisher<String> publisher = Mono.just(body);
		ParameterizedTypeReference<String> typeReference = new ParameterizedTypeReference<String>() {};
		ClientRequest result = ClientRequest.create(POST, URI.create("https://example.com"))
				.body(publisher, typeReference).build();

		List<HttpMessageWriter<?>> messageWriters = new ArrayList<>();
		messageWriters.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));

		ExchangeStrategies strategies = mock(ExchangeStrategies.class);
		when(strategies.messageWriters()).thenReturn(messageWriters);

		MockClientHttpRequest request = new MockClientHttpRequest(GET, "/");
		result.writeTo(request, strategies).block();
		assertNotNull(request.getBody());

		StepVerifier.create(request.getBody())
				.expectNextCount(1)
				.verifyComplete();
	}

}
