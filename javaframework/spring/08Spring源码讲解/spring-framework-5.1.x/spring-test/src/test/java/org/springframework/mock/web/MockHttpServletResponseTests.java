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

package org.springframework.mock.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.web.util.WebUtils;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link MockHttpServletResponse}.
 *
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author Rossen Stoyanchev
 * @author Rob Winch
 * @author Sam Brannen
 * @author Brian Clozel
 * @author Vedran Pavic
 * @since 19.02.2006
 */
public class MockHttpServletResponseTests {

	private MockHttpServletResponse response = new MockHttpServletResponse();


	@Test
	public void setContentType() {
		String contentType = "test/plain";
		response.setContentType(contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals(WebUtils.DEFAULT_CHARACTER_ENCODING, response.getCharacterEncoding());
	}

	@Test
	public void setContentTypeUTF8() {
		String contentType = "test/plain;charset=UTF-8";
		response.setContentType(contentType);
		assertEquals("UTF-8", response.getCharacterEncoding());
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
	}

	@Test
	public void contentTypeHeader() {
		String contentType = "test/plain";
		response.addHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals(WebUtils.DEFAULT_CHARACTER_ENCODING, response.getCharacterEncoding());

		response = new MockHttpServletResponse();
		response.setHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals(WebUtils.DEFAULT_CHARACTER_ENCODING, response.getCharacterEncoding());
	}

	@Test
	public void contentTypeHeaderUTF8() {
		String contentType = "test/plain;charset=UTF-8";
		response.setHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());

		response = new MockHttpServletResponse();
		response.addHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());
	}

	@Test  // SPR-12677
	public void contentTypeHeaderWithMoreComplexCharsetSyntax() {
		String contentType = "test/plain;charset=\"utf-8\";foo=\"charset=bar\";foocharset=bar;foo=bar";
		response.setHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());

		response = new MockHttpServletResponse();
		response.addHeader("Content-Type", contentType);
		assertEquals(contentType, response.getContentType());
		assertEquals(contentType, response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());
	}

	@Test
	public void setContentTypeThenCharacterEncoding() {
		response.setContentType("test/plain");
		response.setCharacterEncoding("UTF-8");
		assertEquals("test/plain", response.getContentType());
		assertEquals("test/plain;charset=UTF-8", response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());
	}

	@Test
	public void setCharacterEncodingThenContentType() {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("test/plain");
		assertEquals("test/plain", response.getContentType());
		assertEquals("test/plain;charset=UTF-8", response.getHeader("Content-Type"));
		assertEquals("UTF-8", response.getCharacterEncoding());
	}

	@Test
	public void contentLength() {
		response.setContentLength(66);
		assertEquals(66, response.getContentLength());
		assertEquals("66", response.getHeader("Content-Length"));
	}

	@Test
	public void contentLengthHeader() {
		response.addHeader("Content-Length", "66");
		assertEquals(66, response.getContentLength());
		assertEquals("66", response.getHeader("Content-Length"));
	}

	@Test
	public void contentLengthIntHeader() {
		response.addIntHeader("Content-Length", 66);
		assertEquals(66, response.getContentLength());
		assertEquals("66", response.getHeader("Content-Length"));
	}

	@Test
	public void httpHeaderNameCasingIsPreserved() throws Exception {
		final String headerName = "Header1";
		response.addHeader(headerName, "value1");
		Collection<String> responseHeaders = response.getHeaderNames();
		assertNotNull(responseHeaders);
		assertEquals(1, responseHeaders.size());
		assertEquals("HTTP header casing not being preserved", headerName, responseHeaders.iterator().next());
	}

	@Test
	public void cookies() {
		Cookie cookie = new Cookie("foo", "bar");
		cookie.setPath("/path");
		cookie.setDomain("example.com");
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);

		response.addCookie(cookie);

		assertEquals("foo=bar; Path=/path; Domain=example.com; " +
				"Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; " +
				"Secure; HttpOnly", response.getHeader(HttpHeaders.SET_COOKIE));
	}

	@Test
	public void servletOutputStreamCommittedWhenBufferSizeExceeded() throws IOException {
		assertFalse(response.isCommitted());
		response.getOutputStream().write('X');
		assertFalse(response.isCommitted());
		int size = response.getBufferSize();
		response.getOutputStream().write(new byte[size]);
		assertTrue(response.isCommitted());
		assertEquals(size + 1, response.getContentAsByteArray().length);
	}

	@Test
	public void servletOutputStreamCommittedOnFlushBuffer() throws IOException {
		assertFalse(response.isCommitted());
		response.getOutputStream().write('X');
		assertFalse(response.isCommitted());
		response.flushBuffer();
		assertTrue(response.isCommitted());
		assertEquals(1, response.getContentAsByteArray().length);
	}

	@Test
	public void servletWriterCommittedWhenBufferSizeExceeded() throws IOException {
		assertFalse(response.isCommitted());
		response.getWriter().write("X");
		assertFalse(response.isCommitted());
		int size = response.getBufferSize();
		char[] data = new char[size];
		Arrays.fill(data, 'p');
		response.getWriter().write(data);
		assertTrue(response.isCommitted());
		assertEquals(size + 1, response.getContentAsByteArray().length);
	}

	@Test
	public void servletOutputStreamCommittedOnOutputStreamFlush() throws IOException {
		assertFalse(response.isCommitted());
		response.getOutputStream().write('X');
		assertFalse(response.isCommitted());
		response.getOutputStream().flush();
		assertTrue(response.isCommitted());
		assertEquals(1, response.getContentAsByteArray().length);
	}

	@Test
	public void servletWriterCommittedOnWriterFlush() throws IOException {
		assertFalse(response.isCommitted());
		response.getWriter().write("X");
		assertFalse(response.isCommitted());
		response.getWriter().flush();
		assertTrue(response.isCommitted());
		assertEquals(1, response.getContentAsByteArray().length);
	}

	@Test // SPR-16683
	public void servletWriterCommittedOnWriterClose() throws IOException {
		assertFalse(response.isCommitted());
		response.getWriter().write("X");
		assertFalse(response.isCommitted());
		response.getWriter().close();
		assertTrue(response.isCommitted());
		assertEquals(1, response.getContentAsByteArray().length);
	}

	@Test
	public void servletWriterAutoFlushedForString() throws IOException {
		response.getWriter().write("X");
		assertEquals("X", response.getContentAsString());
	}

	@Test
	public void servletWriterAutoFlushedForChar() throws IOException {
		response.getWriter().write('X');
		assertEquals("X", response.getContentAsString());
	}

	@Test
	public void servletWriterAutoFlushedForCharArray() throws IOException {
		response.getWriter().write("XY".toCharArray());
		assertEquals("XY", response.getContentAsString());
	}

	@Test
	public void sendRedirect() throws IOException {
		String redirectUrl = "/redirect";
		response.sendRedirect(redirectUrl);
		assertEquals(HttpServletResponse.SC_MOVED_TEMPORARILY, response.getStatus());
		assertEquals(redirectUrl, response.getHeader("Location"));
		assertEquals(redirectUrl, response.getRedirectedUrl());
		assertTrue(response.isCommitted());
	}

	@Test
	public void locationHeaderUpdatesGetRedirectedUrl() {
		String redirectUrl = "/redirect";
		response.setHeader("Location", redirectUrl);
		assertEquals(redirectUrl, response.getRedirectedUrl());
	}

	@Test
	public void setDateHeader() {
		response.setDateHeader("Last-Modified", 1437472800000L);
		assertEquals("Tue, 21 Jul 2015 10:00:00 GMT", response.getHeader("Last-Modified"));
	}

	@Test
	public void addDateHeader() {
		response.addDateHeader("Last-Modified", 1437472800000L);
		response.addDateHeader("Last-Modified", 1437472801000L);
		assertEquals("Tue, 21 Jul 2015 10:00:00 GMT", response.getHeaders("Last-Modified").get(0));
		assertEquals("Tue, 21 Jul 2015 10:00:01 GMT", response.getHeaders("Last-Modified").get(1));
	}

	@Test
	public void getDateHeader() {
		long time = 1437472800000L;
		response.setDateHeader("Last-Modified", time);
		assertEquals("Tue, 21 Jul 2015 10:00:00 GMT", response.getHeader("Last-Modified"));
		assertEquals(time, response.getDateHeader("Last-Modified"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getInvalidDateHeader() {
		response.setHeader("Last-Modified", "invalid");
		assertEquals("invalid", response.getHeader("Last-Modified"));
		response.getDateHeader("Last-Modified");
	}

	@Test  // SPR-16160
	public void getNonExistentDateHeader() {
		assertNull(response.getHeader("Last-Modified"));
		assertEquals(-1, response.getDateHeader("Last-Modified"));
	}

	@Test  // SPR-10414
	public void modifyStatusAfterSendError() throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		response.setStatus(HttpServletResponse.SC_OK);
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}

	@Test  // SPR-10414
	@SuppressWarnings("deprecation")
	public void modifyStatusMessageAfterSendError() throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Server Error");
		assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}

	/**
	 * @since 5.1.10
	 */
	@Test
	public void setCookieHeader() {
		response.setHeader(HttpHeaders.SET_COOKIE, "SESSION=123; Path=/; Secure; HttpOnly; SameSite=Lax");
		assertNumCookies(1);
		assertPrimarySessionCookie("123");

		// Setting the Set-Cookie header a 2nd time should overwrite the previous value
		response.setHeader(HttpHeaders.SET_COOKIE, "SESSION=999; Path=/; Secure; HttpOnly; SameSite=Lax");
		assertNumCookies(1);
		assertPrimarySessionCookie("999");
	}

	/**
	 * @since 5.1.11
	 */
	@Test
	public void setCookieHeaderWithExpiresAttribute() {
		String cookieValue = "SESSION=123; Path=/; Max-Age=100; Expires=Tue, 8 Oct 2019 19:50:00 GMT; Secure; " +
				"HttpOnly; SameSite=Lax";
		response.setHeader(HttpHeaders.SET_COOKIE, cookieValue);
		assertNumCookies(1);
		assertEquals(cookieValue, response.getHeader(HttpHeaders.SET_COOKIE));
	}

	/**
	 * @since 5.1.12
	 */
	@Test
	public void setCookieHeaderWithZeroExpiresAttribute() {
		String cookieValue = "SESSION=123; Path=/; Max-Age=100; Expires=0";
		response.setHeader(HttpHeaders.SET_COOKIE, cookieValue);
		assertNumCookies(1);
		String header = response.getHeader(HttpHeaders.SET_COOKIE);
		assertNotEquals(cookieValue, header);
		// We don't assert the actual Expires value since it is based on the current time.
		assertTrue(header.startsWith("SESSION=123; Path=/; Max-Age=100; Expires="));
	}

	@Test
	public void addCookieHeader() {
		response.addHeader(HttpHeaders.SET_COOKIE, "SESSION=123; Path=/; Secure; HttpOnly; SameSite=Lax");
		assertNumCookies(1);
		assertPrimarySessionCookie("123");

		// Adding a 2nd cookie header should result in 2 cookies.
		response.addHeader(HttpHeaders.SET_COOKIE, "SESSION=999; Path=/; Secure; HttpOnly; SameSite=Lax");
		assertNumCookies(2);
		assertPrimarySessionCookie("123");
		assertCookieValues("123", "999");
	}

	/**
	 * @since 5.1.11
	 */
	@Test
	public void addCookieHeaderWithExpiresAttribute() {
		String cookieValue = "SESSION=123; Path=/; Max-Age=100; Expires=Tue, 8 Oct 2019 19:50:00 GMT; Secure; " +
				"HttpOnly; SameSite=Lax";
		response.addHeader(HttpHeaders.SET_COOKIE, cookieValue);
		assertEquals(cookieValue, response.getHeader(HttpHeaders.SET_COOKIE));
	}

	/**
	 * @since 5.1.12
	 */
	@Test
	public void addCookieHeaderWithZeroExpiresAttribute() {
		String cookieValue = "SESSION=123; Path=/; Max-Age=100; Expires=0";
		response.addHeader(HttpHeaders.SET_COOKIE, cookieValue);
		assertNumCookies(1);
		String header = response.getHeader(HttpHeaders.SET_COOKIE);
		assertNotEquals(cookieValue, header);
		// We don't assert the actual Expires value since it is based on the current time.
		assertTrue(header.startsWith("SESSION=123; Path=/; Max-Age=100; Expires="));
	}

	@Test
	public void addCookie() {
		MockCookie mockCookie = new MockCookie("SESSION", "123");
		mockCookie.setPath("/");
		mockCookie.setDomain("example.com");
		mockCookie.setMaxAge(0);
		mockCookie.setSecure(true);
		mockCookie.setHttpOnly(true);
		mockCookie.setSameSite("Lax");

		response.addCookie(mockCookie);

		assertNumCookies(1);
		assertEquals("SESSION=123; Path=/; Domain=example.com; Max-Age=0; " +
				"Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=Lax",
				response.getHeader(HttpHeaders.SET_COOKIE));

		// Adding a 2nd Cookie should result in 2 Cookies.
		response.addCookie(new MockCookie("SESSION", "999"));
		assertNumCookies(2);
		assertCookieValues("123", "999");
	}

	private void assertNumCookies(int expected) {
		assertEquals(expected, this.response.getCookies().length);
	}

	private void assertCookieValues(String... expected) {
		assertArrayEquals(expected, Arrays.stream(response.getCookies()).map(Cookie::getValue).toArray());
	}

	private void assertPrimarySessionCookie(String expectedValue) {
		Cookie cookie = this.response.getCookie("SESSION");
		assertNotNull(cookie);
		assertTrue(cookie instanceof MockCookie);
		assertEquals("SESSION", cookie.getName());
		assertEquals(expectedValue, cookie.getValue());
		assertEquals("/", cookie.getPath());
		assertTrue(cookie.getSecure());
		assertTrue(cookie.isHttpOnly());
		assertEquals("Lax", ((MockCookie) cookie).getSameSite());
	}

}
