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

package org.springframework.web.reactive.function.client;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultWebClient}.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 */
public class DefaultWebClientTests {

	private WebClient.Builder builder;

	private ExchangeFunction exchangeFunction;

	@Captor
	private ArgumentCaptor<ClientRequest> captor;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.exchangeFunction = mock(ExchangeFunction.class);
		ClientResponse mockResponse = mock(ClientResponse.class);
		when(this.exchangeFunction.exchange(this.captor.capture())).thenReturn(Mono.just(mockResponse));
		this.builder = WebClient.builder().baseUrl("/base").exchangeFunction(this.exchangeFunction);
	}


	@Test
	public void basic() {
		this.builder.build().get().uri("/path").exchange().block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("/base/path", request.url().toString());
		assertEquals(new HttpHeaders(), request.headers());
		assertEquals(Collections.emptyMap(), request.cookies());
	}

	@Test
	public void uriBuilder() {
		this.builder.build().get()
				.uri(builder -> builder.path("/path").queryParam("q", "12").build())
				.exchange()
				.block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("/base/path?q=12", request.url().toString());
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void uriBuilderWithPathOverride() {
		this.builder.build().get()
				.uri(builder -> builder.replacePath("/path").build())
				.exchange()
				.block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("/path", request.url().toString());
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void requestHeaderAndCookie() {
		this.builder.build().get().uri("/path").accept(MediaType.APPLICATION_JSON)
				.cookies(cookies -> cookies.add("id", "123"))	// SPR-16178
				.exchange()
				.block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("application/json", request.headers().getFirst("Accept"));
		assertEquals("123", request.cookies().getFirst("id"));
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void defaultHeaderAndCookie() {
		WebClient client = this.builder
				.defaultHeader("Accept", "application/json").defaultCookie("id", "123")
				.build();

		client.get().uri("/path").exchange().block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("application/json", request.headers().getFirst("Accept"));
		assertEquals("123", request.cookies().getFirst("id"));
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void defaultHeaderAndCookieOverrides() {
		WebClient client = this.builder
				.defaultHeader("Accept", "application/json")
				.defaultCookie("id", "123")
				.build();

		client.get().uri("/path").header("Accept", "application/xml").cookie("id", "456")
				.exchange().block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("application/xml", request.headers().getFirst("Accept"));
		assertEquals("456", request.cookies().getFirst("id"));
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void defaultRequest() {
		ThreadLocal<String> context = new NamedThreadLocal<>("foo");

		Map<String, Object> actual = new HashMap<>();
		ExchangeFilterFunction filter = (request, next) -> {
			actual.putAll(request.attributes());
			return next.exchange(request);
		};

		WebClient client = this.builder
				.defaultRequest(spec -> spec.attribute("foo", context.get()))
				.filter(filter)
				.build();

		try {
			context.set("bar");
			client.get().uri("/path").attribute("foo", "bar").exchange().block(Duration.ofSeconds(10));
		}
		finally {
			context.remove();
		}

		assertEquals("bar", actual.get("foo"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void bodyObjectPublisher() {
		Mono<Void> mono = Mono.empty();
		WebClient client = this.builder.build();

		client.post().uri("https://example.com").syncBody(mono);
	}

	@Test
	public void mutateDoesCopy() {
		// First, build the clients

		WebClient.Builder builder = WebClient.builder()
				.filter((request, next) -> next.exchange(request))
				.defaultHeader("foo", "bar")
				.defaultCookie("foo", "bar");

		WebClient client1 = builder.build();

		WebClient client2 = builder.filter((request, next) -> next.exchange(request))
				.defaultHeader("baz", "qux")
				.defaultCookie("baz", "qux")
				.build();

		WebClient client1a = client1.mutate()
				.filter((request, next) -> next.exchange(request))
				.defaultHeader("baz", "qux")
				.defaultCookie("baz", "qux")
				.build();

		// Now, verify what each client has..

		WebClient.Builder builder1 = client1.mutate();
		builder1.filters(filters -> assertEquals(1, filters.size()));
		builder1.defaultHeaders(headers -> assertEquals(1, headers.size()));
		builder1.defaultCookies(cookies -> assertEquals(1, cookies.size()));

		WebClient.Builder builder2 = client2.mutate();
		builder2.filters(filters -> assertEquals(2, filters.size()));
		builder2.defaultHeaders(headers -> assertEquals(2, headers.size()));
		builder2.defaultCookies(cookies -> assertEquals(2, cookies.size()));

		WebClient.Builder builder1a = client1a.mutate();
		builder1a.filters(filters -> assertEquals(2, filters.size()));
		builder1a.defaultHeaders(headers -> assertEquals(2, headers.size()));
		builder1a.defaultCookies(cookies -> assertEquals(2, cookies.size()));
	}

	@Test
	public void withStringAttribute() {
		Map<String, Object> actual = new HashMap<>();
		ExchangeFilterFunction filter = (request, next) -> {
			actual.putAll(request.attributes());
			return next.exchange(request);
		};

		this.builder.filter(filter).build()
				.get().uri("/path")
				.attribute("foo", "bar")
				.exchange()
				.block(Duration.ofSeconds(10));

		assertEquals("bar", actual.get("foo"));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("bar", request.attribute("foo").get());
	}

	@Test
	public void withNullAttribute() {
		Map<String, Object> actual = new HashMap<>();
		ExchangeFilterFunction filter = (request, next) -> {
			actual.putAll(request.attributes());
			return next.exchange(request);
		};

		this.builder.filter(filter).build()
				.get().uri("/path")
				.attribute("foo", null)
				.exchange()
				.block(Duration.ofSeconds(10));

		assertNull(actual.get("foo"));

		ClientRequest request = verifyAndGetRequest();
		assertFalse(request.attribute("foo").isPresent());
	}

	@Test
	public void apply() {
		WebClient client = this.builder
				.apply(builder -> builder
						.defaultHeader("Accept", "application/json")
						.defaultCookie("id", "123"))
				.build();

		client.get().uri("/path").exchange().block(Duration.ofSeconds(10));

		ClientRequest request = verifyAndGetRequest();
		assertEquals("application/json", request.headers().getFirst("Accept"));
		assertEquals("123", request.cookies().getFirst("id"));
		verifyNoMoreInteractions(this.exchangeFunction);
	}

	@Test
	public void switchToErrorOnEmptyClientResponseMono() {
		ExchangeFunction exchangeFunction = mock(ExchangeFunction.class);
		when(exchangeFunction.exchange(any())).thenReturn(Mono.empty());
		WebClient.Builder builder = WebClient.builder().baseUrl("/base").exchangeFunction(exchangeFunction);
		StepVerifier.create(builder.build().get().uri("/path").exchange())
				.expectErrorMessage("The underlying HTTP client completed without emitting a response.")
				.verify(Duration.ofSeconds(5));
	}

	@Test // gh-23909
	public void shouldApplyFiltersAtSubscription() {
		WebClient client = this.builder
				.filter((request, next) ->
						next.exchange(ClientRequest
								.from(request)
								.header("Custom", "value")
								.build()))
				.build();
		Mono<ClientResponse> exchange = client.get().uri("/path").exchange();
		verifyZeroInteractions(this.exchangeFunction);
		exchange.block(Duration.ofSeconds(10));
		ClientRequest request = verifyAndGetRequest();
		assertEquals("value", request.headers().getFirst("Custom"));
	}


	private ClientRequest verifyAndGetRequest() {
		ClientRequest request = this.captor.getValue();
		Mockito.verify(this.exchangeFunction).exchange(request);
		verifyNoMoreInteractions(this.exchangeFunction);
		return request;
	}

}
