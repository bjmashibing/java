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

package org.springframework.web.reactive.result.method.annotation;

import java.net.URI;
import java.time.Duration;

import org.junit.Test;
import org.reactivestreams.Publisher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.adapter.ForwardedHeaderTransformer;

import static org.junit.Assert.*;

/**
 * Integration tests with {@code @RequestMapping} handler methods.
 *
 * <p>Before adding tests here consider if they are a better fit for any of the
 * other {@code RequestMapping*IntegrationTests}.
 *
 * @author Rossen Stoyanchev
 * @author Stephane Maldini
 * @since 5.0
 */
public class RequestMappingIntegrationTests extends AbstractRequestMappingIntegrationTests {

	@Override
	protected ApplicationContext initApplicationContext() {
		AnnotationConfigApplicationContext wac = new AnnotationConfigApplicationContext();
		wac.register(WebConfig.class, TestRestController.class, LocalConfig.class);
		wac.refresh();
		return wac;
	}


	@Test
	public void httpHead() {
		String url = "http://localhost:" + this.port + "/text";
		HttpHeaders headers = getRestTemplate().headForHeaders(url);
		String contentType = headers.getFirst("Content-Type");
		assertNotNull(contentType);
		assertEquals("text/html;charset=utf-8", contentType.toLowerCase());
		assertEquals(3, headers.getContentLength());
	}

	@Test
	public void forwardedHeaders() {

		// One integration test to verify triggering of Forwarded header support.
		// More fine-grained tests in ForwardedHeaderTransformerTests.

		RequestEntity<Void> request = RequestEntity
				.get(URI.create("http://localhost:" + this.port + "/uri"))
				.header("Forwarded", "host=84.198.58.199;proto=https")
				.build();
		ResponseEntity<String> entity = getRestTemplate().exchange(request, String.class);
		assertEquals("https://84.198.58.199/uri", entity.getBody());
	}

	@Test
	public void stream() throws Exception {
		String[] expected = {"0", "1", "2", "3", "4"};
		assertArrayEquals(expected, performGet("/stream", new HttpHeaders(), String[].class).getBody());
	}


	@Configuration
	@EnableWebFlux
	static class WebConfig {
	}


	@RestController
	@SuppressWarnings("unused")
	private static class TestRestController {

		@GetMapping("/text")
		public String textGet() {
			return "Foo";
		}

		// SPR-17593: explicit HEAD should not clash with implicit mapping via GET
		@RequestMapping(path = "/text", method = RequestMethod.HEAD)
		public String textHead() {
			return textGet();
		}

		@GetMapping("/uri")
		public String uri(ServerHttpRequest request) {
			return request.getURI().toString();
		}

		@GetMapping("/stream")
		public Publisher<Long> stream() {
			return testInterval(Duration.ofMillis(50), 5);
		}
	}


	@Configuration
	static class LocalConfig {

		@Bean
		public ForwardedHeaderTransformer forwardedHeaderTransformer() {
			return new ForwardedHeaderTransformer();
		}
	}

}
