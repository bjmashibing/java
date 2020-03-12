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

package org.springframework.web.server.adapter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;
import org.springframework.web.filter.reactive.ForwardedHeaderFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebHandler;

import static java.time.Duration.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link WebHttpHandlerBuilder}.
 * @author Rossen Stoyanchev
 */
public class WebHttpHandlerBuilderTests {

	@Test  // SPR-15074
	public void orderedWebFilterBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(OrderedWebFilterBeanConfig.class);
		context.refresh();

		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(context).build();
		assertTrue(httpHandler instanceof HttpWebHandlerAdapter);
		assertSame(context, ((HttpWebHandlerAdapter) httpHandler).getApplicationContext());

		MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
		MockServerHttpResponse response = new MockServerHttpResponse();
		httpHandler.handle(request, response).block(ofMillis(5000));

		assertEquals("FilterB::FilterA", response.getBodyAsString().block(ofMillis(5000)));
	}

	@Test
	public void forwardedHeaderFilter() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(ForwardedHeaderFilterConfig.class);
		context.refresh();

		WebHttpHandlerBuilder builder = WebHttpHandlerBuilder.applicationContext(context);
		builder.filters(filters -> assertEquals(Collections.emptyList(), filters));
		assertTrue(builder.hasForwardedHeaderTransformer());
	}

	@Test  // SPR-15074
	public void orderedWebExceptionHandlerBeans() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(OrderedExceptionHandlerBeanConfig.class);
		context.refresh();

		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(context).build();
		MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
		MockServerHttpResponse response = new MockServerHttpResponse();
		httpHandler.handle(request, response).block(ofMillis(5000));

		assertEquals("ExceptionHandlerB", response.getBodyAsString().block(ofMillis(5000)));
	}

	@Test
	public void configWithoutFilters() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(NoFilterConfig.class);
		context.refresh();

		HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(context).build();
		MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
		MockServerHttpResponse response = new MockServerHttpResponse();
		httpHandler.handle(request, response).block(ofMillis(5000));

		assertEquals("handled", response.getBodyAsString().block(ofMillis(5000)));
	}

	@Test  // SPR-16972
	public void cloneWithApplicationContext() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.register(NoFilterConfig.class);
		context.refresh();

		WebHttpHandlerBuilder builder = WebHttpHandlerBuilder.applicationContext(context);
		assertSame(context, ((HttpWebHandlerAdapter) builder.build()).getApplicationContext());
		assertSame(context, ((HttpWebHandlerAdapter) builder.clone().build()).getApplicationContext());
	}


	private static Mono<Void> writeToResponse(ServerWebExchange exchange, String value) {
		byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = new DefaultDataBufferFactory().wrap(bytes);
		return exchange.getResponse().writeWith(Flux.just(buffer));
	}


	@Configuration
	@SuppressWarnings("unused")
	static class OrderedWebFilterBeanConfig {

		private static final String ATTRIBUTE = "attr";

		@Bean @Order(2)
		public WebFilter filterA() {
			return createFilter("FilterA");
		}

		@Bean @Order(1)
		public WebFilter filterB() {
			return createFilter("FilterB");
		}

		private WebFilter createFilter(String name) {
			return (exchange, chain) -> {
				String value = exchange.getAttribute(ATTRIBUTE);
				value = (value != null ? value + "::" + name : name);
				exchange.getAttributes().put(ATTRIBUTE, value);
				return chain.filter(exchange);
			};
		}

		@Bean
		public WebHandler webHandler() {
			return exchange -> {
				String value = exchange.getAttributeOrDefault(ATTRIBUTE, "none");
				return writeToResponse(exchange, value);
			};
		}
	}


	@Configuration
	@SuppressWarnings("unused")
	static class OrderedExceptionHandlerBeanConfig {

		@Bean
		@Order(2)
		public WebExceptionHandler exceptionHandlerA() {
			return (exchange, ex) -> writeToResponse(exchange, "ExceptionHandlerA");
		}

		@Bean
		@Order(1)
		public WebExceptionHandler exceptionHandlerB() {
			return (exchange, ex) -> writeToResponse(exchange, "ExceptionHandlerB");
		}

		@Bean
		public WebHandler webHandler() {
			return exchange -> Mono.error(new Exception());
		}
	}

	@Configuration
	@SuppressWarnings({"unused", "deprecation"})
	static class ForwardedHeaderFilterConfig {

		@Bean
		public ForwardedHeaderFilter forwardedHeaderFilter() {
			return new ForwardedHeaderFilter();
		}

		@Bean
		public WebHandler webHandler() {
			return exchange -> Mono.error(new Exception());
		}
	}

	@Configuration
	@SuppressWarnings("unused")
	static class NoFilterConfig {

		@Bean
		public WebHandler webHandler() {
			return exchange -> writeToResponse(exchange, "handled");
		}
	}

}
