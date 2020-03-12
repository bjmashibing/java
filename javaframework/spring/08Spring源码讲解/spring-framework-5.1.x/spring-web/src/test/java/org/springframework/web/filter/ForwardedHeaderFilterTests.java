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

package org.springframework.web.filter;

import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.test.MockFilterChain;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ForwardedHeaderFilter}.
 *
 * @author Rossen Stoyanchev
 * @author Eddú Meléndez
 * @author Rob Winch
 */
public class ForwardedHeaderFilterTests {

	private static final String X_FORWARDED_PROTO = "x-forwarded-proto";  // SPR-14372 (case insensitive)
	private static final String X_FORWARDED_HOST = "x-forwarded-host";
	private static final String X_FORWARDED_PORT = "x-forwarded-port";
	private static final String X_FORWARDED_PREFIX = "x-forwarded-prefix";
	private static final String X_FORWARDED_SSL = "x-forwarded-ssl";


	private final ForwardedHeaderFilter filter = new ForwardedHeaderFilter();

	private MockHttpServletRequest request;

	private MockFilterChain filterChain;


	@Before
	@SuppressWarnings("serial")
	public void setup() throws Exception {
		this.request = new MockHttpServletRequest();
		this.request.setScheme("http");
		this.request.setServerName("localhost");
		this.request.setServerPort(80);
		this.filterChain = new MockFilterChain(new HttpServlet() {});
	}


	@Test
	public void contextPathEmpty() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "");
		assertEquals("", filterAndGetContextPath());
	}

	@Test
	public void contextPathWithTrailingSlash() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/foo/bar/");
		assertEquals("/foo/bar", filterAndGetContextPath());
	}

	@Test
	public void contextPathWithTrailingSlashes() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/foo/bar/baz///");
		assertEquals("/foo/bar/baz", filterAndGetContextPath());
	}

	@Test
	public void contextPathWithForwardedPrefix() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix");
		this.request.setContextPath("/mvc-showcase");

		String actual = filterAndGetContextPath();
		assertEquals("/prefix", actual);
	}

	@Test
	public void contextPathWithForwardedPrefixTrailingSlash() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix/");
		this.request.setContextPath("/mvc-showcase");

		String actual = filterAndGetContextPath();
		assertEquals("/prefix", actual);
	}

	private String filterAndGetContextPath() throws ServletException, IOException {
		return filterAndGetWrappedRequest().getContextPath();
	}

	private HttpServletRequest filterAndGetWrappedRequest() throws ServletException, IOException {
		MockHttpServletResponse response = new MockHttpServletResponse();
		this.filter.doFilterInternal(this.request, response, this.filterChain);
		return (HttpServletRequest) this.filterChain.getRequest();
	}


	@Test
	public void contextPathPreserveEncoding() throws Exception {
		this.request.setContextPath("/app%20");
		this.request.setRequestURI("/app%20/path/");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("/app%20", actual.getContextPath());
		assertEquals("/app%20/path/", actual.getRequestURI());
		assertEquals("http://localhost/app%20/path/", actual.getRequestURL().toString());
	}

	@Test
	public void requestUri() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/");
		this.request.setContextPath("/app");
		this.request.setRequestURI("/app/path");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("", actual.getContextPath());
		assertEquals("/path", actual.getRequestURI());
	}

	@Test
	public void requestUriWithTrailingSlash() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/");
		this.request.setContextPath("/app");
		this.request.setRequestURI("/app/path/");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("", actual.getContextPath());
		assertEquals("/path/", actual.getRequestURI());
	}

	@Test
	public void requestUriPreserveEncoding() throws Exception {
		this.request.setContextPath("/app");
		this.request.setRequestURI("/app/path%20with%20spaces/");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("/app", actual.getContextPath());
		assertEquals("/app/path%20with%20spaces/", actual.getRequestURI());
		assertEquals("http://localhost/app/path%20with%20spaces/", actual.getRequestURL().toString());
	}

	@Test
	public void requestUriEqualsContextPath() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/");
		this.request.setContextPath("/app");
		this.request.setRequestURI("/app");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("", actual.getContextPath());
		assertEquals("/", actual.getRequestURI());
	}

	@Test
	public void requestUriRootUrl() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/");
		this.request.setContextPath("/app");
		this.request.setRequestURI("/app/");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("", actual.getContextPath());
		assertEquals("/", actual.getRequestURI());
	}

	@Test
	public void requestUriPreserveSemicolonContent() throws Exception {
		this.request.setContextPath("");
		this.request.setRequestURI("/path;a=b/with/semicolon");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("", actual.getContextPath());
		assertEquals("/path;a=b/with/semicolon", actual.getRequestURI());
		assertEquals("http://localhost/path;a=b/with/semicolon", actual.getRequestURL().toString());
	}

	@Test
	public void caseInsensitiveForwardedPrefix() throws Exception {
		this.request = new MockHttpServletRequest() {

			@Override // SPR-14372: make it case-sensitive
			public String getHeader(String header) {
				Enumeration<String> names = getHeaderNames();
				while (names.hasMoreElements()) {
					String name = names.nextElement();
					if (name.equals(header)) {
						return super.getHeader(header);
					}
				}
				return null;
			}
		};
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix");
		this.request.setRequestURI("/path");
		HttpServletRequest actual = filterAndGetWrappedRequest();

		assertEquals("/prefix/path", actual.getRequestURI());
	}

	@Test
	public void shouldFilter() {
		testShouldFilter("Forwarded");
		testShouldFilter(X_FORWARDED_HOST);
		testShouldFilter(X_FORWARDED_PORT);
		testShouldFilter(X_FORWARDED_PROTO);
		testShouldFilter(X_FORWARDED_SSL);
	}

	private void testShouldFilter(String headerName) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(headerName, "1");
		assertFalse(this.filter.shouldNotFilter(request));
	}

	@Test
	public void shouldNotFilter() {
		assertTrue(this.filter.shouldNotFilter(new MockHttpServletRequest()));
	}

	@Test
	public void forwardedRequest() throws Exception {
		this.request.setRequestURI("/mvc-showcase");
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "84.198.58.199");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.addHeader("foo", "bar");

		this.filter.doFilter(this.request, new MockHttpServletResponse(), this.filterChain);
		HttpServletRequest actual = (HttpServletRequest) this.filterChain.getRequest();

		assertEquals("https://84.198.58.199/mvc-showcase", actual.getRequestURL().toString());
		assertEquals("https", actual.getScheme());
		assertEquals("84.198.58.199", actual.getServerName());
		assertEquals(443, actual.getServerPort());
		assertTrue(actual.isSecure());

		assertNull(actual.getHeader(X_FORWARDED_PROTO));
		assertNull(actual.getHeader(X_FORWARDED_HOST));
		assertNull(actual.getHeader(X_FORWARDED_PORT));
		assertEquals("bar", actual.getHeader("foo"));
	}

	@Test
	public void forwardedRequestInRemoveOnlyMode() throws Exception {
		this.request.setRequestURI("/mvc-showcase");
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "84.198.58.199");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.addHeader(X_FORWARDED_SSL, "on");
		this.request.addHeader("foo", "bar");

		this.filter.setRemoveOnly(true);
		this.filter.doFilter(this.request, new MockHttpServletResponse(), this.filterChain);
		HttpServletRequest actual = (HttpServletRequest) this.filterChain.getRequest();

		assertEquals("http://localhost/mvc-showcase", actual.getRequestURL().toString());
		assertEquals("http", actual.getScheme());
		assertEquals("localhost", actual.getServerName());
		assertEquals(80, actual.getServerPort());
		assertFalse(actual.isSecure());

		assertNull(actual.getHeader(X_FORWARDED_PROTO));
		assertNull(actual.getHeader(X_FORWARDED_HOST));
		assertNull(actual.getHeader(X_FORWARDED_PORT));
		assertNull(actual.getHeader(X_FORWARDED_SSL));
		assertEquals("bar", actual.getHeader("foo"));
	}

	@Test
	public void forwardedRequestWithSsl() throws Exception {
		this.request.setRequestURI("/mvc-showcase");
		this.request.addHeader(X_FORWARDED_SSL, "on");
		this.request.addHeader(X_FORWARDED_HOST, "84.198.58.199");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.addHeader("foo", "bar");

		this.filter.doFilter(this.request, new MockHttpServletResponse(), this.filterChain);
		HttpServletRequest actual = (HttpServletRequest) this.filterChain.getRequest();

		assertEquals("https://84.198.58.199/mvc-showcase", actual.getRequestURL().toString());
		assertEquals("https", actual.getScheme());
		assertEquals("84.198.58.199", actual.getServerName());
		assertEquals(443, actual.getServerPort());
		assertTrue(actual.isSecure());

		assertNull(actual.getHeader(X_FORWARDED_SSL));
		assertNull(actual.getHeader(X_FORWARDED_HOST));
		assertNull(actual.getHeader(X_FORWARDED_PORT));
		assertEquals("bar", actual.getHeader("foo"));
	}

	@Test // SPR-16983
	public void forwardedRequestWithServletForward() throws Exception {
		this.request.setRequestURI("/foo");
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "www.mycompany.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		this.filter.doFilter(this.request, new MockHttpServletResponse(), this.filterChain);
		HttpServletRequest wrappedRequest = (HttpServletRequest) this.filterChain.getRequest();

		this.request.setDispatcherType(DispatcherType.FORWARD);
		this.request.setRequestURI("/bar");
		this.filterChain.reset();

		this.filter.doFilter(wrappedRequest, new MockHttpServletResponse(), this.filterChain);
		HttpServletRequest actual = (HttpServletRequest) this.filterChain.getRequest();

		assertNotNull(actual);
		assertEquals("/bar", actual.getRequestURI());
		assertEquals("https://www.mycompany.com/bar", actual.getRequestURL().toString());
	}

	@Test
	public void requestUriWithForwardedPrefix() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix");
		this.request.setRequestURI("/mvc-showcase");

		HttpServletRequest actual = filterAndGetWrappedRequest();
		assertEquals("http://localhost/prefix/mvc-showcase", actual.getRequestURL().toString());
	}

	@Test
	public void requestUriWithForwardedPrefixTrailingSlash() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix/");
		this.request.setRequestURI("/mvc-showcase");

		HttpServletRequest actual = filterAndGetWrappedRequest();
		assertEquals("http://localhost/prefix/mvc-showcase", actual.getRequestURL().toString());
	}

	@Test
	public void requestURLNewStringBuffer() throws Exception {
		this.request.addHeader(X_FORWARDED_PREFIX, "/prefix/");
		this.request.setRequestURI("/mvc-showcase");

		HttpServletRequest actual = filterAndGetWrappedRequest();
		actual.getRequestURL().append("?key=value");
		assertEquals("http://localhost/prefix/mvc-showcase", actual.getRequestURL().toString());
	}

	@Test
	public void sendRedirectWithAbsolutePath() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		String redirectedUrl = sendRedirect("/foo/bar");
		assertEquals("https://example.com/foo/bar", redirectedUrl);
	}

	@Test // SPR-16506
	public void sendRedirectWithAbsolutePathQueryParamAndFragment() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.setQueryString("oldqp=1");

		String redirectedUrl = sendRedirect("/foo/bar?newqp=2#fragment");
		assertEquals("https://example.com/foo/bar?newqp=2#fragment", redirectedUrl);
	}

	@Test
	public void sendRedirectWithContextPath() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.setContextPath("/context");

		String redirectedUrl = sendRedirect("/context/foo/bar");
		assertEquals("https://example.com/context/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithRelativePath() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.setRequestURI("/parent/");

		String redirectedUrl = sendRedirect("foo/bar");
		assertEquals("https://example.com/parent/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithFileInPathAndRelativeRedirect() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.setRequestURI("/context/a");

		String redirectedUrl = sendRedirect("foo/bar");
		assertEquals("https://example.com/context/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithRelativePathIgnoresFile() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.request.setRequestURI("/parent");

		String redirectedUrl = sendRedirect("foo/bar");
		assertEquals("https://example.com/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithLocationDotDotPath() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		String redirectedUrl = sendRedirect("parent/../foo/bar");
		assertEquals("https://example.com/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithLocationHasScheme() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		String location = "https://other.info/foo/bar";
		String redirectedUrl = sendRedirect(location);
		assertEquals(location, redirectedUrl);
	}

	@Test
	public void sendRedirectWithLocationSlashSlash() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		String location = "//other.info/foo/bar";
		String redirectedUrl = sendRedirect(location);
		assertEquals("https:" + location, redirectedUrl);
	}

	@Test
	public void sendRedirectWithLocationSlashSlashParentDotDot() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");

		String location = "//other.info/parent/../foo/bar";
		String redirectedUrl = sendRedirect(location);
		assertEquals("https:" + location, redirectedUrl);
	}

	@Test
	public void sendRedirectWithNoXForwardedAndAbsolutePath() throws Exception {
		String redirectedUrl = sendRedirect("/foo/bar");
		assertEquals("/foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWithNoXForwardedAndDotDotPath() throws Exception {
		String redirectedUrl = sendRedirect("../foo/bar");
		assertEquals("../foo/bar", redirectedUrl);
	}

	@Test
	public void sendRedirectWhenRequestOnlyAndXForwardedThenUsesRelativeRedirects() throws Exception {
		this.request.addHeader(X_FORWARDED_PROTO, "https");
		this.request.addHeader(X_FORWARDED_HOST, "example.com");
		this.request.addHeader(X_FORWARDED_PORT, "443");
		this.filter.setRelativeRedirects(true);
		String location = sendRedirect("/a");

		assertEquals("/a", location);
	}

	@Test
	public void sendRedirectWhenRequestOnlyAndNoXForwardedThenUsesRelativeRedirects() throws Exception {
		this.filter.setRelativeRedirects(true);
		String location = sendRedirect("/a");

		assertEquals("/a", location);
	}

	private String sendRedirect(final String location) throws ServletException, IOException {
		Filter filter = new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
					FilterChain chain) throws IOException {

				res.sendRedirect(location);
			}
		};

		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain filterChain = new MockFilterChain(mock(HttpServlet.class), this.filter, filter);
		filterChain.doFilter(request, response);

		return response.getRedirectedUrl();
	}

}
