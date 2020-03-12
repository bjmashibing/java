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

package org.springframework.http.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.util.FileCopyUtils;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 */
public class ServletServerHttpRequestTests {

	private ServletServerHttpRequest request;

	private MockHttpServletRequest mockRequest;


	@Before
	public void create() {
		mockRequest = new MockHttpServletRequest();
		request = new ServletServerHttpRequest(mockRequest);
	}


	@Test
	public void getMethod() {
		mockRequest.setMethod("POST");
		assertEquals("Invalid method", HttpMethod.POST, request.getMethod());
	}

	@Test
	public void getUriForSimplePath() throws URISyntaxException {
		URI uri = new URI("https://example.com/path");
		mockRequest.setScheme(uri.getScheme());
		mockRequest.setServerName(uri.getHost());
		mockRequest.setServerPort(uri.getPort());
		mockRequest.setRequestURI(uri.getPath());
		mockRequest.setQueryString(uri.getQuery());
		assertEquals(uri, request.getURI());
	}

	@Test
	public void getUriWithQueryString() throws URISyntaxException {
		URI uri = new URI("https://example.com/path?query");
		mockRequest.setScheme(uri.getScheme());
		mockRequest.setServerName(uri.getHost());
		mockRequest.setServerPort(uri.getPort());
		mockRequest.setRequestURI(uri.getPath());
		mockRequest.setQueryString(uri.getQuery());
		assertEquals(uri, request.getURI());
	}

	@Test  // SPR-16414
	public void getUriWithQueryParam() throws URISyntaxException {
		mockRequest.setServerName("example.com");
		mockRequest.setRequestURI("/path");
		mockRequest.setQueryString("query=foo");
		assertEquals(new URI("http://example.com/path?query=foo"), request.getURI());
	}

	@Test  // SPR-16414
	public void getUriWithMalformedQueryParam() throws URISyntaxException {
		mockRequest.setServerName("example.com");
		mockRequest.setRequestURI("/path");
		mockRequest.setQueryString("query=foo%%x");
		assertEquals(new URI("http://example.com/path"), request.getURI());
	}

	@Test  // SPR-13876
	public void getUriWithEncoding() throws URISyntaxException {
		URI uri = new URI("https://example.com/%E4%B8%AD%E6%96%87" +
				"?redirect=https%3A%2F%2Fgithub.com%2Fspring-projects%2Fspring-framework");
		mockRequest.setScheme(uri.getScheme());
		mockRequest.setServerName(uri.getHost());
		mockRequest.setServerPort(uri.getPort());
		mockRequest.setRequestURI(uri.getRawPath());
		mockRequest.setQueryString(uri.getRawQuery());
		assertEquals(uri, request.getURI());
    }

	@Test
	public void getHeaders() {
		String headerName = "MyHeader";
		String headerValue1 = "value1";
		String headerValue2 = "value2";
		mockRequest.addHeader(headerName, headerValue1);
		mockRequest.addHeader(headerName, headerValue2);
		mockRequest.setContentType("text/plain");
		mockRequest.setCharacterEncoding("UTF-8");

		HttpHeaders headers = request.getHeaders();
		assertNotNull("No HttpHeaders returned", headers);
		assertTrue("Invalid headers returned", headers.containsKey(headerName));
		List<String> headerValues = headers.get(headerName);
		assertEquals("Invalid header values returned", 2, headerValues.size());
		assertTrue("Invalid header values returned", headerValues.contains(headerValue1));
		assertTrue("Invalid header values returned", headerValues.contains(headerValue2));
		assertEquals("Invalid Content-Type", new MediaType("text", "plain", StandardCharsets.UTF_8),
				headers.getContentType());
	}

	@Test
	public void getHeadersWithEmptyContentTypeAndEncoding() {
		String headerName = "MyHeader";
		String headerValue1 = "value1";
		String headerValue2 = "value2";
		mockRequest.addHeader(headerName, headerValue1);
		mockRequest.addHeader(headerName, headerValue2);
		mockRequest.setContentType("");
		mockRequest.setCharacterEncoding("");

		HttpHeaders headers = request.getHeaders();
		assertNotNull("No HttpHeaders returned", headers);
		assertTrue("Invalid headers returned", headers.containsKey(headerName));
		List<String> headerValues = headers.get(headerName);
		assertEquals("Invalid header values returned", 2, headerValues.size());
		assertTrue("Invalid header values returned", headerValues.contains(headerValue1));
		assertTrue("Invalid header values returned", headerValues.contains(headerValue2));
		assertNull(headers.getContentType());
	}

	@Test
	public void getBody() throws IOException {
		byte[] content = "Hello World".getBytes("UTF-8");
		mockRequest.setContent(content);

		byte[] result = FileCopyUtils.copyToByteArray(request.getBody());
		assertArrayEquals("Invalid content returned", content, result);
	}

	@Test
	public void getFormBody() throws IOException {
		// Charset (SPR-8676)
		mockRequest.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
		mockRequest.setMethod("POST");
		mockRequest.addParameter("name 1", "value 1");
		mockRequest.addParameter("name 2", new String[] {"value 2+1", "value 2+2"});
		mockRequest.addParameter("name 3", (String) null);

		byte[] result = FileCopyUtils.copyToByteArray(request.getBody());
		byte[] content = "name+1=value+1&name+2=value+2%2B1&name+2=value+2%2B2&name+3".getBytes("UTF-8");
		assertArrayEquals("Invalid content returned", content, result);
	}

}
