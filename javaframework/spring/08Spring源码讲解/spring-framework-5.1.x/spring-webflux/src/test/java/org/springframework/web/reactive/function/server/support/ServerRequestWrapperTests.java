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

package org.springframework.web.reactive.function.server.support;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpMethod;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyExtractor;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Arjen Poutsma
 */
public class ServerRequestWrapperTests {

	private final ServerRequest mockRequest = mock(ServerRequest.class);

	private final ServerRequestWrapper wrapper = new ServerRequestWrapper(mockRequest);


	@Test
	public void request() {
		assertSame(mockRequest, wrapper.request());
	}

	@Test
	public void method() {
		HttpMethod method = HttpMethod.POST;
		when(mockRequest.method()).thenReturn(method);

		assertSame(method, wrapper.method());
	}

	@Test
	public void uri() {
		URI uri = URI.create("https://example.com");
		when(mockRequest.uri()).thenReturn(uri);

		assertSame(uri, wrapper.uri());
	}

	@Test
	public void path() {
		String path = "/foo/bar";
		when(mockRequest.path()).thenReturn(path);

		assertSame(path, wrapper.path());
	}

	@Test
	public void headers() {
		ServerRequest.Headers headers = mock(ServerRequest.Headers.class);
		when(mockRequest.headers()).thenReturn(headers);

		assertSame(headers, wrapper.headers());
	}

	@Test
	public void attribute() {
		String name = "foo";
		String value = "bar";
		when(mockRequest.attribute(name)).thenReturn(Optional.of(value));

		assertEquals(Optional.of(value), wrapper.attribute(name));
	}

	@Test
	public void queryParam() {
		String name = "foo";
		String value = "bar";
		when(mockRequest.queryParam(name)).thenReturn(Optional.of(value));

		assertEquals(Optional.of(value), wrapper.queryParam(name));
	}

	@Test
	public void queryParams() {
		MultiValueMap<String, String> value = new LinkedMultiValueMap<>();
		value.add("foo", "bar");
		when(mockRequest.queryParams()).thenReturn(value);

		assertSame(value, wrapper.queryParams());
	}

	@Test
	public void pathVariable() {
		String name = "foo";
		String value = "bar";
		when(mockRequest.pathVariable(name)).thenReturn(value);

		assertEquals(value, wrapper.pathVariable(name));
	}

	@Test
	public void pathVariables() {
		Map<String, String> pathVariables = Collections.singletonMap("foo", "bar");
		when(mockRequest.pathVariables()).thenReturn(pathVariables);

		assertSame(pathVariables, wrapper.pathVariables());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void cookies() {
		MultiValueMap<String, HttpCookie> cookies = mock(MultiValueMap.class);
		when(mockRequest.cookies()).thenReturn(cookies);

		assertSame(cookies, wrapper.cookies());
	}

	@Test
	public void bodyExtractor() {
		Mono<String> result = Mono.just("foo");
		BodyExtractor<Mono<String>, ReactiveHttpInputMessage> extractor = BodyExtractors.toMono(String.class);
		when(mockRequest.body(extractor)).thenReturn(result);

		assertSame(result, wrapper.body(extractor));
	}

	@Test
	public void bodyToMonoClass() {
		Mono<String> result = Mono.just("foo");
		when(mockRequest.bodyToMono(String.class)).thenReturn(result);

		assertSame(result, wrapper.bodyToMono(String.class));
	}

	@Test
	public void bodyToMonoParameterizedTypeReference() {
		Mono<String> result = Mono.just("foo");
		ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {};
		when(mockRequest.bodyToMono(reference)).thenReturn(result);

		assertSame(result, wrapper.bodyToMono(reference));
	}

	@Test
	public void bodyToFluxClass() {
		Flux<String> result = Flux.just("foo");
		when(mockRequest.bodyToFlux(String.class)).thenReturn(result);

		assertSame(result, wrapper.bodyToFlux(String.class));
	}

	@Test
	public void bodyToFluxParameterizedTypeReference() {
		Flux<String> result = Flux.just("foo");
		ParameterizedTypeReference<String> reference = new ParameterizedTypeReference<String>() {};
		when(mockRequest.bodyToFlux(reference)).thenReturn(result);

		assertSame(result, wrapper.bodyToFlux(reference));
	}

}
