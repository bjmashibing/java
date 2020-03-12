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

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.codec.StringDecoder;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.http.codec.DecoderHttpMessageReader;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;

/**
 * @author Arjen Poutsma
 * @author Denys Ivano
 */
public class DefaultClientResponseTests {

	private ClientHttpResponse mockResponse;

	private ExchangeStrategies mockExchangeStrategies;

	private DefaultClientResponse defaultClientResponse;


	@Before
	public void createMocks() {
		mockResponse = mock(ClientHttpResponse.class);
		mockExchangeStrategies = mock(ExchangeStrategies.class);
		defaultClientResponse = new DefaultClientResponse(mockResponse, mockExchangeStrategies, "");
	}


	@Test
	public void statusCode() {
		HttpStatus status = HttpStatus.CONTINUE;
		when(mockResponse.getStatusCode()).thenReturn(status);

		assertEquals(status, defaultClientResponse.statusCode());
	}

	@Test
	public void rawStatusCode() {
		int status = 999;
		when(mockResponse.getRawStatusCode()).thenReturn(status);

		assertEquals(status, defaultClientResponse.rawStatusCode());
	}

	@Test
	public void header() {
		HttpHeaders httpHeaders = new HttpHeaders();
		long contentLength = 42L;
		httpHeaders.setContentLength(contentLength);
		MediaType contentType = MediaType.TEXT_PLAIN;
		httpHeaders.setContentType(contentType);
		InetSocketAddress host = InetSocketAddress.createUnresolved("localhost", 80);
		httpHeaders.setHost(host);
		List<HttpRange> range = Collections.singletonList(HttpRange.createByteRange(0, 42));
		httpHeaders.setRange(range);

		when(mockResponse.getHeaders()).thenReturn(httpHeaders);

		ClientResponse.Headers headers = defaultClientResponse.headers();
		assertEquals(OptionalLong.of(contentLength), headers.contentLength());
		assertEquals(Optional.of(contentType), headers.contentType());
		assertEquals(httpHeaders, headers.asHttpHeaders());
	}

	@Test
	public void cookies() {
		ResponseCookie cookie = ResponseCookie.from("foo", "bar").build();
		MultiValueMap<String, ResponseCookie> cookies = new LinkedMultiValueMap<>();
		cookies.add("foo", cookie);

		when(mockResponse.getCookies()).thenReturn(cookies);

		assertSame(cookies, defaultClientResponse.cookies());
	}


	@Test
	public void body() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		Mono<String> resultMono = defaultClientResponse.body(toMono(String.class));
		assertEquals("foo", resultMono.block());
	}

	@Test
	public void bodyToMono() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		Mono<String> resultMono = defaultClientResponse.bodyToMono(String.class);
		assertEquals("foo", resultMono.block());
	}

	@Test
	public void bodyToMonoTypeReference() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		Mono<String> resultMono =
				defaultClientResponse.bodyToMono(new ParameterizedTypeReference<String>() {
				});
		assertEquals("foo", resultMono.block());
	}

	@Test
	public void bodyToFlux() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		Flux<String> resultFlux = defaultClientResponse.bodyToFlux(String.class);
		Mono<List<String>> result = resultFlux.collectList();
		assertEquals(Collections.singletonList("foo"), result.block());
	}

	@Test
	public void bodyToFluxTypeReference() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		Flux<String> resultFlux =
				defaultClientResponse.bodyToFlux(new ParameterizedTypeReference<String>() {
				});
		Mono<List<String>> result = resultFlux.collectList();
		assertEquals(Collections.singletonList("foo"), result.block());
	}

	@Test
	public void toEntity() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<String> result = defaultClientResponse.toEntity(String.class).block();
		assertEquals("foo", result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}

	@Test
	public void toEntityWithUnknownStatusCode() throws Exception {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer
				= factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		when(mockResponse.getHeaders()).thenReturn(httpHeaders);
		when(mockResponse.getStatusCode()).thenThrow(new IllegalArgumentException("999"));
		when(mockResponse.getRawStatusCode()).thenReturn(999);
		when(mockResponse.getBody()).thenReturn(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<String> result = defaultClientResponse.toEntity(String.class).block();
		assertEquals("foo", result.getBody());
		try {
			result.getStatusCode();
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// do nothing
		}
		assertEquals(999, result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}

	@Test
	public void toEntityTypeReference() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<String> result = defaultClientResponse.toEntity(
				new ParameterizedTypeReference<String>() {
				}).block();
		assertEquals("foo", result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}

	@Test
	public void toEntityList() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);
		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<List<String>> result = defaultClientResponse.toEntityList(String.class).block();
		assertEquals(Collections.singletonList("foo"), result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}

	@Test
	public void toEntityListWithUnknownStatusCode() throws Exception {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		when(mockResponse.getHeaders()).thenReturn(httpHeaders);
		when(mockResponse.getStatusCode()).thenThrow(new IllegalArgumentException("999"));
		when(mockResponse.getRawStatusCode()).thenReturn(999);
		when(mockResponse.getBody()).thenReturn(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<List<String>> result = defaultClientResponse.toEntityList(String.class).block();
		assertEquals(Collections.singletonList("foo"), result.getBody());
		try {
			result.getStatusCode();
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException ex) {
			// do nothing
		}
		assertEquals(999, result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}

	@Test
	public void toEntityListTypeReference() {
		DefaultDataBufferFactory factory = new DefaultDataBufferFactory();
		DefaultDataBuffer dataBuffer =
				factory.wrap(ByteBuffer.wrap("foo".getBytes(StandardCharsets.UTF_8)));
		Flux<DataBuffer> body = Flux.just(dataBuffer);

		mockTextPlainResponse(body);

		List<HttpMessageReader<?>> messageReaders = Collections
				.singletonList(new DecoderHttpMessageReader<>(StringDecoder.allMimeTypes()));
		when(mockExchangeStrategies.messageReaders()).thenReturn(messageReaders);

		ResponseEntity<List<String>> result = defaultClientResponse.toEntityList(
				new ParameterizedTypeReference<String>() {
				}).block();
		assertEquals(Collections.singletonList("foo"), result.getBody());
		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(HttpStatus.OK.value(), result.getStatusCodeValue());
		assertEquals(MediaType.TEXT_PLAIN, result.getHeaders().getContentType());
	}


	private void mockTextPlainResponse(Flux<DataBuffer> body) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.TEXT_PLAIN);
		when(mockResponse.getHeaders()).thenReturn(httpHeaders);
		when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);
		when(mockResponse.getRawStatusCode()).thenReturn(HttpStatus.OK.value());
		when(mockResponse.getBody()).thenReturn(body);
	}

}
