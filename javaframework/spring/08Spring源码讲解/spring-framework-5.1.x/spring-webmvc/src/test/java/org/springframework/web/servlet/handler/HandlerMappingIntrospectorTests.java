/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.web.servlet.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

/**
 * Unit tests for {@link HandlerMappingIntrospector}.
 *
 * @author Rossen Stoyanchev
 * @since 4.3.1
 */
public class HandlerMappingIntrospectorTests {

	@Test
	public void detectHandlerMappings() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.registerSingleton("hmA", SimpleUrlHandlerMapping.class);
		cxt.registerSingleton("hmB", SimpleUrlHandlerMapping.class);
		cxt.registerSingleton("hmC", SimpleUrlHandlerMapping.class);
		cxt.refresh();

		List<?> expected = Arrays.asList(cxt.getBean("hmA"), cxt.getBean("hmB"), cxt.getBean("hmC"));
		List<HandlerMapping> actual = getIntrospector(cxt).getHandlerMappings();

		assertEquals(expected, actual);
	}

	@Test
	public void detectHandlerMappingsOrdered() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		MutablePropertyValues pvs = new MutablePropertyValues(Collections.singletonMap("order", "3"));
		cxt.registerSingleton("hmA", SimpleUrlHandlerMapping.class, pvs);
		pvs = new MutablePropertyValues(Collections.singletonMap("order", "2"));
		cxt.registerSingleton("hmB", SimpleUrlHandlerMapping.class, pvs);
		pvs = new MutablePropertyValues(Collections.singletonMap("order", "1"));
		cxt.registerSingleton("hmC", SimpleUrlHandlerMapping.class, pvs);
		cxt.refresh();

		List<?> expected = Arrays.asList(cxt.getBean("hmC"), cxt.getBean("hmB"), cxt.getBean("hmA"));
		List<HandlerMapping> actual = getIntrospector(cxt).getHandlerMappings();

		assertEquals(expected, actual);
	}

	public void defaultHandlerMappings() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.refresh();

		List<HandlerMapping> actual = getIntrospector(cxt).getHandlerMappings();
		assertEquals(2, actual.size());
		assertEquals(BeanNameUrlHandlerMapping.class, actual.get(0).getClass());
		assertEquals(RequestMappingHandlerMapping.class, actual.get(1).getClass());
	}

	@Test
	public void getMatchable() throws Exception {
		MutablePropertyValues pvs = new MutablePropertyValues(
				Collections.singletonMap("urlMap", Collections.singletonMap("/path", new Object())));

		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.registerSingleton("hm", SimpleUrlHandlerMapping.class, pvs);
		cxt.refresh();

		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/path");
		MatchableHandlerMapping hm = getIntrospector(cxt).getMatchableHandlerMapping(request);

		assertEquals(cxt.getBean("hm"), hm);
		assertNull("Attributes changes not ignored", request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE));
	}

	@Test(expected = IllegalStateException.class)
	public void getMatchableWhereHandlerMappingDoesNotImplementMatchableInterface() throws Exception {
		StaticWebApplicationContext cxt = new StaticWebApplicationContext();
		cxt.registerSingleton("hm1", TestHandlerMapping.class);
		cxt.refresh();

		MockHttpServletRequest request = new MockHttpServletRequest();
		getIntrospector(cxt).getMatchableHandlerMapping(request);
	}

	@Test
	public void getCorsConfigurationPreFlight() throws Exception {
		AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
		cxt.register(TestConfig.class);
		cxt.refresh();

		// PRE-FLIGHT

		MockHttpServletRequest request = new MockHttpServletRequest("OPTIONS", "/path");
		request.addHeader("Origin", "http://localhost:9000");
		request.addHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST");
		CorsConfiguration corsConfig = getIntrospector(cxt).getCorsConfiguration(request);

		assertNotNull(corsConfig);
		assertEquals(Collections.singletonList("http://localhost:9000"), corsConfig.getAllowedOrigins());
		assertEquals(Collections.singletonList("POST"), corsConfig.getAllowedMethods());
	}

	@Test
	public void getCorsConfigurationActual() throws Exception {
		AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
		cxt.register(TestConfig.class);
		cxt.refresh();

		MockHttpServletRequest request = new MockHttpServletRequest("POST", "/path");
		request.addHeader("Origin", "http://localhost:9000");
		CorsConfiguration corsConfig = getIntrospector(cxt).getCorsConfiguration(request);

		assertNotNull(corsConfig);
		assertEquals(Collections.singletonList("http://localhost:9000"), corsConfig.getAllowedOrigins());
		assertEquals(Collections.singletonList("POST"), corsConfig.getAllowedMethods());
	}

	private HandlerMappingIntrospector getIntrospector(WebApplicationContext cxt) {
		HandlerMappingIntrospector introspector = new HandlerMappingIntrospector();
		introspector.setApplicationContext(cxt);
		introspector.afterPropertiesSet();
		return introspector;
	}


	private static class TestHandlerMapping implements HandlerMapping {

		@Override
		public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
			return new HandlerExecutionChain(new Object());
		}
	}


	@Configuration
	@SuppressWarnings({"WeakerAccess", "unused"})
	static class TestConfig {

		@Bean
		public RequestMappingHandlerMapping handlerMapping() {
			return new RequestMappingHandlerMapping();
		}

		@Bean
		public TestController testController() {
			return new TestController();
		}
	}


	@CrossOrigin("http://localhost:9000")
	@Controller
	private static class TestController {

		@PostMapping("/path")
		public void handle() {
		}
	}

}
