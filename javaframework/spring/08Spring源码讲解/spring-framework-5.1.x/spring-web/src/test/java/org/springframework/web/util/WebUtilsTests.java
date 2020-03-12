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

package org.springframework.web.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.test.MockFilterChain;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.ForwardedHeaderFilter;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Arjen Poutsma
 * @author Rossen Stoyanchev
 * @author Sebastien Deleuze
 */
public class WebUtilsTests {

	@Test
	public void findParameterValue() {
		Map<String, Object> params = new HashMap<>();
		params.put("myKey1", "myValue1");
		params.put("myKey2_myValue2", "xxx");
		params.put("myKey3_myValue3.x", "xxx");
		params.put("myKey4_myValue4.y", new String[] {"yyy"});

		assertNull(WebUtils.findParameterValue(params, "myKey0"));
		assertEquals("myValue1", WebUtils.findParameterValue(params, "myKey1"));
		assertEquals("myValue2", WebUtils.findParameterValue(params, "myKey2"));
		assertEquals("myValue3", WebUtils.findParameterValue(params, "myKey3"));
		assertEquals("myValue4", WebUtils.findParameterValue(params, "myKey4"));
	}

	@Test
	public void parseMatrixVariablesString() {
		MultiValueMap<String, String> variables;

		variables = WebUtils.parseMatrixVariables(null);
		assertEquals(0, variables.size());

		variables = WebUtils.parseMatrixVariables("year");
		assertEquals(1, variables.size());
		assertEquals("", variables.getFirst("year"));

		variables = WebUtils.parseMatrixVariables("year=2012");
		assertEquals(1, variables.size());
		assertEquals("2012", variables.getFirst("year"));

		variables = WebUtils.parseMatrixVariables("year=2012;colors=red,blue,green");
		assertEquals(2, variables.size());
		assertEquals(Arrays.asList("red", "blue", "green"), variables.get("colors"));
		assertEquals("2012", variables.getFirst("year"));

		variables = WebUtils.parseMatrixVariables(";year=2012;colors=red,blue,green;");
		assertEquals(2, variables.size());
		assertEquals(Arrays.asList("red", "blue", "green"), variables.get("colors"));
		assertEquals("2012", variables.getFirst("year"));

		variables = WebUtils.parseMatrixVariables("colors=red;colors=blue;colors=green");
		assertEquals(1, variables.size());
		assertEquals(Arrays.asList("red", "blue", "green"), variables.get("colors"));
	}

	@Test
	public void isValidOrigin() {
		List<String> allowed = Collections.emptyList();
		assertTrue(checkValidOrigin("mydomain1.com", -1, "http://mydomain1.com", allowed));
		assertFalse(checkValidOrigin("mydomain1.com", -1, "http://mydomain2.com", allowed));

		allowed = Collections.singletonList("*");
		assertTrue(checkValidOrigin("mydomain1.com", -1, "http://mydomain2.com", allowed));

		allowed = Collections.singletonList("http://mydomain1.com");
		assertTrue(checkValidOrigin("mydomain2.com", -1, "http://mydomain1.com", allowed));
		assertFalse(checkValidOrigin("mydomain2.com", -1, "http://mydomain3.com", allowed));
	}

	@Test
	public void isSameOrigin() {
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com"));
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com:80"));
		assertTrue(checkSameOrigin("https", "mydomain1.com", 443, "https://mydomain1.com"));
		assertTrue(checkSameOrigin("https", "mydomain1.com", 443, "https://mydomain1.com:443"));
		assertTrue(checkSameOrigin("http", "mydomain1.com", 123, "http://mydomain1.com:123"));
		assertTrue(checkSameOrigin("ws", "mydomain1.com", -1, "ws://mydomain1.com"));
		assertTrue(checkSameOrigin("wss", "mydomain1.com", 443, "wss://mydomain1.com"));

		assertFalse(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain2.com"));
		assertFalse(checkSameOrigin("http", "mydomain1.com", -1, "https://mydomain1.com"));
		assertFalse(checkSameOrigin("http", "mydomain1.com", -1, "invalid-origin"));
		assertFalse(checkSameOrigin("https", "mydomain1.com", -1, "https://mydomain1.com"));

		// Handling of invalid origins as described in SPR-13478
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com/"));
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com:80"));
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com/path"));
		assertTrue(checkSameOrigin("http", "mydomain1.com", -1, "http://mydomain1.com:80/path"));
		assertFalse(checkSameOrigin("http", "mydomain2.com", -1, "http://mydomain1.com/"));
		assertFalse(checkSameOrigin("http", "mydomain2.com", -1, "http://mydomain1.com:80/"));
		assertFalse(checkSameOrigin("http", "mydomain2.com", -1, "http://mydomain1.com/path"));
		assertFalse(checkSameOrigin("http", "mydomain2.com", -1, "http://mydomain1.com:80/path"));

		// Handling of IPv6 hosts as described in SPR-13525
		assertTrue(checkSameOrigin("http", "[::1]", -1, "http://[::1]"));
		assertTrue(checkSameOrigin("http", "[::1]", 8080, "http://[::1]:8080"));
		assertTrue(checkSameOrigin("http",
				"[2001:0db8:0000:85a3:0000:0000:ac1f:8001]", -1,
				"http://[2001:0db8:0000:85a3:0000:0000:ac1f:8001]"));
		assertTrue(checkSameOrigin("http",
				"[2001:0db8:0000:85a3:0000:0000:ac1f:8001]", 8080,
				"http://[2001:0db8:0000:85a3:0000:0000:ac1f:8001]:8080"));
		assertFalse(checkSameOrigin("http", "[::1]", -1, "http://[::1]:8080"));
		assertFalse(checkSameOrigin("http", "[::1]", 8080,
				"http://[2001:0db8:0000:85a3:0000:0000:ac1f:8001]:8080"));
	}

	@Test  // SPR-16262
	public void isSameOriginWithXForwardedHeaders() throws Exception {
		String server = "mydomain1.com";
		testWithXForwardedHeaders(server, -1, "https", null, -1, "https://mydomain1.com");
		testWithXForwardedHeaders(server, 123, "https", null, -1, "https://mydomain1.com");
		testWithXForwardedHeaders(server, -1, "https", "mydomain2.com", -1, "https://mydomain2.com");
		testWithXForwardedHeaders(server, 123, "https", "mydomain2.com", -1, "https://mydomain2.com");
		testWithXForwardedHeaders(server, -1, "https", "mydomain2.com", 456, "https://mydomain2.com:456");
		testWithXForwardedHeaders(server, 123, "https", "mydomain2.com", 456, "https://mydomain2.com:456");
	}

	@Test  // SPR-16262
	public void isSameOriginWithForwardedHeader() throws Exception {
		String server = "mydomain1.com";
		testWithForwardedHeader(server, -1, "proto=https", "https://mydomain1.com");
		testWithForwardedHeader(server, 123, "proto=https", "https://mydomain1.com");
		testWithForwardedHeader(server, -1, "proto=https; host=mydomain2.com", "https://mydomain2.com");
		testWithForwardedHeader(server, 123, "proto=https; host=mydomain2.com", "https://mydomain2.com");
		testWithForwardedHeader(server, -1, "proto=https; host=mydomain2.com:456", "https://mydomain2.com:456");
		testWithForwardedHeader(server, 123, "proto=https; host=mydomain2.com:456", "https://mydomain2.com:456");
	}


	private boolean checkValidOrigin(String serverName, int port, String originHeader, List<String> allowed) {
		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		servletRequest.setServerName(serverName);
		if (port != -1) {
			servletRequest.setServerPort(port);
		}
		servletRequest.addHeader(HttpHeaders.ORIGIN, originHeader);
		return WebUtils.isValidOrigin(request, allowed);
	}

	private boolean checkSameOrigin(String scheme, String serverName, int port, String originHeader) {
		MockHttpServletRequest servletRequest = new MockHttpServletRequest();
		ServerHttpRequest request = new ServletServerHttpRequest(servletRequest);
		servletRequest.setScheme(scheme);
		servletRequest.setServerName(serverName);
		if (port != -1) {
			servletRequest.setServerPort(port);
		}
		servletRequest.addHeader(HttpHeaders.ORIGIN, originHeader);
		return WebUtils.isSameOrigin(request);
	}

	private void testWithXForwardedHeaders(String serverName, int port, String forwardedProto,
			String forwardedHost, int forwardedPort, String originHeader) throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerName(serverName);
		if (port != -1) {
			request.setServerPort(port);
		}
		if (forwardedProto != null) {
			request.addHeader("X-Forwarded-Proto", forwardedProto);
		}
		if (forwardedHost != null) {
			request.addHeader("X-Forwarded-Host", forwardedHost);
		}
		if (forwardedPort != -1) {
			request.addHeader("X-Forwarded-Port", String.valueOf(forwardedPort));
		}
		request.addHeader(HttpHeaders.ORIGIN, originHeader);

		HttpServletRequest requestToUse = adaptFromForwardedHeaders(request);
		ServerHttpRequest httpRequest = new ServletServerHttpRequest(requestToUse);

		assertTrue(WebUtils.isSameOrigin(httpRequest));
	}

	private void testWithForwardedHeader(String serverName, int port, String forwardedHeader,
			String originHeader) throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerName(serverName);
		if (port != -1) {
			request.setServerPort(port);
		}
		request.addHeader("Forwarded", forwardedHeader);
		request.addHeader(HttpHeaders.ORIGIN, originHeader);

		HttpServletRequest requestToUse = adaptFromForwardedHeaders(request);
		ServerHttpRequest httpRequest = new ServletServerHttpRequest(requestToUse);

		assertTrue(WebUtils.isSameOrigin(httpRequest));
	}

	// SPR-16668
	private HttpServletRequest adaptFromForwardedHeaders(HttpServletRequest request) throws Exception {
		MockFilterChain chain = new MockFilterChain();
		new ForwardedHeaderFilter().doFilter(request, new MockHttpServletResponse(), chain);
		return (HttpServletRequest) chain.getRequest();
	}

}
