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

package org.springframework.http.server.reactive;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link ContextPathCompositeHandler}.
 *
 * @author Rossen Stoyanchev
 */
public class ContextPathCompositeHandlerTests {


	@Test
	public void invalidContextPath() {
		testInvalid("  ", "Context path must not be empty");
		testInvalid("path", "Context path must begin with '/'");
		testInvalid("/path/", "Context path must not end with '/'");
	}

	private void testInvalid(String contextPath, String expectedError) {
		try {
			new ContextPathCompositeHandler(Collections.singletonMap(contextPath, new TestHttpHandler()));
			fail();
		}
		catch (IllegalArgumentException ex) {
			assertEquals(expectedError, ex.getMessage());
		}
	}

	@Test
	public void match() {
		TestHttpHandler handler1 = new TestHttpHandler();
		TestHttpHandler handler2 = new TestHttpHandler();
		TestHttpHandler handler3 = new TestHttpHandler();

		Map<String, HttpHandler> map = new HashMap<>();
		map.put("/path", handler1);
		map.put("/another/path", handler2);
		map.put("/yet/another/path", handler3);

		testHandle("/another/path/and/more", map);

		assertInvoked(handler2, "/another/path");
		assertNotInvoked(handler1, handler3);
	}

	@Test
	public void matchWithContextPathEqualToPath() {
		TestHttpHandler handler1 = new TestHttpHandler();
		TestHttpHandler handler2 = new TestHttpHandler();
		TestHttpHandler handler3 = new TestHttpHandler();

		Map<String, HttpHandler> map = new HashMap<>();
		map.put("/path", handler1);
		map.put("/another/path", handler2);
		map.put("/yet/another/path", handler3);

		testHandle("/path", map);

		assertInvoked(handler1, "/path");
		assertNotInvoked(handler2, handler3);
	}

	@Test
	public void matchWithNativeContextPath() {
		MockServerHttpRequest request = MockServerHttpRequest
				.get("/yet/another/path")
				.contextPath("/yet")  // contextPath in underlying request
				.build();

		TestHttpHandler handler = new TestHttpHandler();
		Map<String, HttpHandler> map = Collections.singletonMap("/another/path", handler);

		new ContextPathCompositeHandler(map).handle(request, new MockServerHttpResponse());

		assertTrue(handler.wasInvoked());
		assertEquals("/yet/another/path", handler.getRequest().getPath().contextPath().value());
	}

	@Test
	public void notFound() {
		TestHttpHandler handler1 = new TestHttpHandler();
		TestHttpHandler handler2 = new TestHttpHandler();

		Map<String, HttpHandler> map = new HashMap<>();
		map.put("/path", handler1);
		map.put("/another/path", handler2);

		ServerHttpResponse response = testHandle("/yet/another/path", map);

		assertNotInvoked(handler1, handler2);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	@Test // SPR-17144
	public void notFoundWithCommitAction() {

		AtomicBoolean commitInvoked = new AtomicBoolean(false);

		ServerHttpRequest request = MockServerHttpRequest.get("/unknown/path").build();
		ServerHttpResponse response = new MockServerHttpResponse();
		response.beforeCommit(() -> {
			commitInvoked.set(true);
			return Mono.empty();
		});

		Map<String, HttpHandler> map = new HashMap<>();
		TestHttpHandler handler = new TestHttpHandler();
		map.put("/path", handler);
		new ContextPathCompositeHandler(map).handle(request, response).block(Duration.ofSeconds(5));

		assertNotInvoked(handler);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(commitInvoked.get());
	}


	private ServerHttpResponse testHandle(String pathToHandle, Map<String, HttpHandler> handlerMap) {
		ServerHttpRequest request = MockServerHttpRequest.get(pathToHandle).build();
		ServerHttpResponse response = new MockServerHttpResponse();
		new ContextPathCompositeHandler(handlerMap).handle(request, response).block(Duration.ofSeconds(5));
		return response;
	}

	private void assertInvoked(TestHttpHandler handler, String contextPath) {
		assertTrue(handler.wasInvoked());
		assertEquals(contextPath, handler.getRequest().getPath().contextPath().value());
	}

	private void assertNotInvoked(TestHttpHandler... handlers) {
		Arrays.stream(handlers).forEach(handler -> assertFalse(handler.wasInvoked()));
	}


	@SuppressWarnings("WeakerAccess")
	private static class TestHttpHandler implements HttpHandler {

		private ServerHttpRequest request;

		public boolean wasInvoked() {
			return (this.request != null);
		}

		public ServerHttpRequest getRequest() {
			return this.request;
		}

		@Override
		public Mono<Void> handle(ServerHttpRequest request, ServerHttpResponse response) {
			this.request = request;
			return Mono.empty();
		}
	}

}
