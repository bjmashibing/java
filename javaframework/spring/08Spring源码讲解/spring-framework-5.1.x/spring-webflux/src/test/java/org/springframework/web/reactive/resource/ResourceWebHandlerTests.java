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

package org.springframework.web.reactive.resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.core.io.buffer.support.DataBufferTestUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.mock.http.server.reactive.test.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.test.MockServerHttpResponse;
import org.springframework.mock.web.test.server.MockServerWebExchange;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ResourceWebHandler}.
 *
 * @author Rossen Stoyanchev
 */
public class ResourceWebHandlerTests {

	private static final Duration TIMEOUT = Duration.ofSeconds(1);


	private ResourceWebHandler handler;

	private DataBufferFactory bufferFactory = new DefaultDataBufferFactory();


	@Before
	public void setup() throws Exception {
		List<Resource> locations = new ArrayList<>(2);
		locations.add(new ClassPathResource("test/", getClass()));
		locations.add(new ClassPathResource("testalternatepath/", getClass()));
		locations.add(new ClassPathResource("META-INF/resources/webjars/"));

		this.handler = new ResourceWebHandler();
		this.handler.setLocations(locations);
		this.handler.setCacheControl(CacheControl.maxAge(3600, TimeUnit.SECONDS));
		this.handler.afterPropertiesSet();
	}


	@Test
	public void getResource() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		HttpHeaders headers = exchange.getResponse().getHeaders();
		assertEquals(MediaType.parseMediaType("text/css"), headers.getContentType());
		assertEquals(17, headers.getContentLength());
		assertEquals("max-age=3600", headers.getCacheControl());
		assertTrue(headers.containsKey("Last-Modified"));
		assertEquals(headers.getLastModified() / 1000, resourceLastModifiedDate("test/foo.css") / 1000);
		assertEquals("bytes", headers.getFirst("Accept-Ranges"));
		assertEquals(1, headers.get("Accept-Ranges").size());
		assertResponseBody(exchange, "h1 { color:red; }");
	}

	@Test
	public void getResourceHttpHeader() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.head(""));
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		assertNull(exchange.getResponse().getStatusCode());
		HttpHeaders headers = exchange.getResponse().getHeaders();
		assertEquals(MediaType.parseMediaType("text/css"), headers.getContentType());
		assertEquals(17, headers.getContentLength());
		assertEquals("max-age=3600", headers.getCacheControl());
		assertTrue(headers.containsKey("Last-Modified"));
		assertEquals(headers.getLastModified() / 1000, resourceLastModifiedDate("test/foo.css") / 1000);
		assertEquals("bytes", headers.getFirst("Accept-Ranges"));
		assertEquals(1, headers.get("Accept-Ranges").size());

		StepVerifier.create(exchange.getResponse().getBody())
				.expectErrorMatches(ex -> ex.getMessage().startsWith("No content was written"))
				.verify();
	}

	@Test
	public void getResourceHttpOptions() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.options(""));
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		assertNull(exchange.getResponse().getStatusCode());
		assertEquals("GET,HEAD,OPTIONS", exchange.getResponse().getHeaders().getFirst("Allow"));
	}

	@Test
	public void getResourceNoCache() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.setCacheControl(CacheControl.noStore());
		this.handler.handle(exchange).block(TIMEOUT);

		MockServerHttpResponse response = exchange.getResponse();
		assertEquals("no-store", response.getHeaders().getCacheControl());
		assertTrue(response.getHeaders().containsKey("Last-Modified"));
		assertEquals(response.getHeaders().getLastModified() / 1000, resourceLastModifiedDate("test/foo.css") / 1000);
		assertEquals("bytes", response.getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, response.getHeaders().get("Accept-Ranges").size());
	}

	@Test
	public void getVersionedResource() throws Exception {
		VersionResourceResolver versionResolver = new VersionResourceResolver();
		versionResolver.addFixedVersionStrategy("versionString", "/**");
		this.handler.setResourceResolvers(Arrays.asList(versionResolver, new PathResourceResolver()));
		this.handler.afterPropertiesSet();

		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "versionString/foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals("\"versionString\"", exchange.getResponse().getHeaders().getETag());
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
	}

	@Test
	public void getResourceWithHtmlMediaType() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "foo.html");
		this.handler.handle(exchange).block(TIMEOUT);

		HttpHeaders headers = exchange.getResponse().getHeaders();
		assertEquals(MediaType.TEXT_HTML, headers.getContentType());
		assertEquals("max-age=3600", headers.getCacheControl());
		assertTrue(headers.containsKey("Last-Modified"));
		assertEquals(headers.getLastModified() / 1000, resourceLastModifiedDate("test/foo.html") / 1000);
		assertEquals("bytes", headers.getFirst("Accept-Ranges"));
		assertEquals(1, headers.get("Accept-Ranges").size());
	}

	@Test
	public void getResourceFromAlternatePath() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "baz.css");
		this.handler.handle(exchange).block(TIMEOUT);

		HttpHeaders headers = exchange.getResponse().getHeaders();
		assertEquals(MediaType.parseMediaType("text/css"), headers.getContentType());
		assertEquals(17, headers.getContentLength());
		assertEquals("max-age=3600", headers.getCacheControl());
		assertTrue(headers.containsKey("Last-Modified"));
		assertEquals(headers.getLastModified() / 1000, resourceLastModifiedDate("testalternatepath/baz.css") / 1000);
		assertEquals("bytes", headers.getFirst("Accept-Ranges"));
		assertEquals(1, headers.get("Accept-Ranges").size());
		assertResponseBody(exchange, "h1 { color:red; }");
	}

	@Test
	public void getResourceFromSubDirectory() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "js/foo.js");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(MediaType.parseMediaType("application/javascript"),
				exchange.getResponse().getHeaders().getContentType());
		assertResponseBody(exchange, "function foo() { console.log(\"hello world\"); }");
	}

	@Test
	public void getResourceFromSubDirectoryOfAlternatePath() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "js/baz.js");
		this.handler.handle(exchange).block(TIMEOUT);

		HttpHeaders headers = exchange.getResponse().getHeaders();
		assertEquals(MediaType.parseMediaType("application/javascript"), headers.getContentType());
		assertResponseBody(exchange, "function foo() { console.log(\"hello world\"); }");
	}

	@Test // SPR-14577
	public void getMediaTypeWithFavorPathExtensionOff() throws Exception {
		List<Resource> paths = Collections.singletonList(new ClassPathResource("test/", getClass()));
		ResourceWebHandler handler = new ResourceWebHandler();
		handler.setLocations(paths);
		handler.afterPropertiesSet();

		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("")
				.header("Accept", "application/json,text/plain,*/*"));
		setPathWithinHandlerMapping(exchange, "foo.html");
		handler.handle(exchange).block(TIMEOUT);

		assertEquals(MediaType.TEXT_HTML, exchange.getResponse().getHeaders().getContentType());
	}

	@Test
	public void testInvalidPath() throws Exception {

		// Use mock ResourceResolver: i.e. we're only testing upfront validations...

		Resource resource = mock(Resource.class);
		when(resource.getFilename()).thenThrow(new AssertionError("Resource should not be resolved"));
		when(resource.getInputStream()).thenThrow(new AssertionError("Resource should not be resolved"));
		ResourceResolver resolver = mock(ResourceResolver.class);
		when(resolver.resolveResource(any(), any(), any(), any())).thenReturn(Mono.just(resource));

		ResourceWebHandler handler = new ResourceWebHandler();
		handler.setLocations(Collections.singletonList(new ClassPathResource("test/", getClass())));
		handler.setResourceResolvers(Collections.singletonList(resolver));
		handler.afterPropertiesSet();

		testInvalidPath("../testsecret/secret.txt", handler);
		testInvalidPath("test/../../testsecret/secret.txt", handler);
		testInvalidPath(":/../../testsecret/secret.txt", handler);

		Resource location = new UrlResource(getClass().getResource("./test/"));
		this.handler.setLocations(Collections.singletonList(location));
		Resource secretResource = new UrlResource(getClass().getResource("testsecret/secret.txt"));
		String secretPath = secretResource.getURL().getPath();

		testInvalidPath("file:" + secretPath, handler);
		testInvalidPath("/file:" + secretPath, handler);
		testInvalidPath("url:" + secretPath, handler);
		testInvalidPath("/url:" + secretPath, handler);
		testInvalidPath("/../.." + secretPath, handler);
		testInvalidPath("/%2E%2E/testsecret/secret.txt", handler);
		testInvalidPath("/%2E%2E/testsecret/secret.txt", handler);
		testInvalidPath("%2F%2F%2E%2E%2F%2F%2E%2E" + secretPath, handler);
	}

	private void testInvalidPath(String requestPath, ResourceWebHandler handler) {
		ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, requestPath);

		StepVerifier.create(handler.handle(exchange))
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				}).verify(TIMEOUT);
	}

	@Test
	public void testResolvePathWithTraversal() throws Exception {
		for (HttpMethod method : HttpMethod.values()) {
			testResolvePathWithTraversal(method);
		}
	}

	private void testResolvePathWithTraversal(HttpMethod httpMethod) throws Exception {
		Resource location = new ClassPathResource("test/", getClass());
		this.handler.setLocations(Collections.singletonList(location));

		testResolvePathWithTraversal(httpMethod, "../testsecret/secret.txt", location);
		testResolvePathWithTraversal(httpMethod, "test/../../testsecret/secret.txt", location);
		testResolvePathWithTraversal(httpMethod, ":/../../testsecret/secret.txt", location);

		location = new UrlResource(getClass().getResource("./test/"));
		this.handler.setLocations(Collections.singletonList(location));
		Resource secretResource = new UrlResource(getClass().getResource("testsecret/secret.txt"));
		String secretPath = secretResource.getURL().getPath();

		testResolvePathWithTraversal(httpMethod, "file:" + secretPath, location);
		testResolvePathWithTraversal(httpMethod, "/file:" + secretPath, location);
		testResolvePathWithTraversal(httpMethod, "url:" + secretPath, location);
		testResolvePathWithTraversal(httpMethod, "/url:" + secretPath, location);
		testResolvePathWithTraversal(httpMethod, "////../.." + secretPath, location);
		testResolvePathWithTraversal(httpMethod, "/%2E%2E/testsecret/secret.txt", location);
		testResolvePathWithTraversal(httpMethod, "%2F%2F%2E%2E%2F%2Ftestsecret/secret.txt", location);
		testResolvePathWithTraversal(httpMethod, "url:" + secretPath, location);

		// The following tests fail with a MalformedURLException on Windows
		// testResolvePathWithTraversal(location, "/" + secretPath);
		// testResolvePathWithTraversal(location, "/  " + secretPath);
	}

	private void testResolvePathWithTraversal(HttpMethod httpMethod, String requestPath, Resource location)
			throws Exception {

		ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.method(httpMethod, ""));
		setPathWithinHandlerMapping(exchange, requestPath);
		StepVerifier.create(this.handler.handle(exchange))
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				})
				.verify(TIMEOUT);
		if (!location.createRelative(requestPath).exists() && !requestPath.contains(":")) {
			fail(requestPath + " doesn't actually exist as a relative path");
		}
	}

	@Test
	public void processPath() {
		assertSame("/foo/bar", this.handler.processPath("/foo/bar"));
		assertSame("foo/bar", this.handler.processPath("foo/bar"));

		// leading whitespace control characters (00-1F)
		assertEquals("/foo/bar", this.handler.processPath("  /foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath((char) 1 + "/foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath((char) 31 + "/foo/bar"));
		assertEquals("foo/bar", this.handler.processPath("  foo/bar"));
		assertEquals("foo/bar", this.handler.processPath((char) 31 + "foo/bar"));

		// leading control character 0x7F (DEL)
		assertEquals("/foo/bar", this.handler.processPath((char) 127 + "/foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath((char) 127 + "/foo/bar"));

		// leading control and '/' characters
		assertEquals("/foo/bar", this.handler.processPath("  /  foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath("  /  /  foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath("  // /// ////  foo/bar"));
		assertEquals("/foo/bar", this.handler.processPath((char) 1 + " / " + (char) 127 + " // foo/bar"));

		// root or empty path
		assertEquals("", this.handler.processPath("   "));
		assertEquals("/", this.handler.processPath("/"));
		assertEquals("/", this.handler.processPath("///"));
		assertEquals("/", this.handler.processPath("/ /   / "));
	}

	@Test
	public void initAllowedLocations() {
		PathResourceResolver resolver = (PathResourceResolver) this.handler.getResourceResolvers().get(0);
		Resource[] locations = resolver.getAllowedLocations();

		assertEquals(3, locations.length);
		assertEquals("test/", ((ClassPathResource) locations[0]).getPath());
		assertEquals("testalternatepath/", ((ClassPathResource) locations[1]).getPath());
		assertEquals("META-INF/resources/webjars/", ((ClassPathResource) locations[2]).getPath());
	}

	@Test
	public void initAllowedLocationsWithExplicitConfiguration() throws Exception {
		ClassPathResource location1 = new ClassPathResource("test/", getClass());
		ClassPathResource location2 = new ClassPathResource("testalternatepath/", getClass());

		PathResourceResolver pathResolver = new PathResourceResolver();
		pathResolver.setAllowedLocations(location1);

		ResourceWebHandler handler = new ResourceWebHandler();
		handler.setResourceResolvers(Collections.singletonList(pathResolver));
		handler.setLocations(Arrays.asList(location1, location2));
		handler.afterPropertiesSet();

		Resource[] locations = pathResolver.getAllowedLocations();
		assertEquals(1, locations.length);
		assertEquals("test/", ((ClassPathResource) locations[0]).getPath());
	}

	@Test
	public void notModified() throws Exception {
		MockServerWebExchange exchange = MockServerWebExchange.from(
				MockServerHttpRequest.get("").ifModifiedSince(resourceLastModified("test/foo.css")));

		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);
		assertEquals(HttpStatus.NOT_MODIFIED, exchange.getResponse().getStatusCode());
	}

	@Test
	public void modified() throws Exception {
		long timestamp = resourceLastModified("test/foo.css") / 1000 * 1000 - 1;
		MockServerHttpRequest request = MockServerHttpRequest.get("").ifModifiedSince(timestamp).build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		assertNull(exchange.getResponse().getStatusCode());
		assertResponseBody(exchange, "h1 { color:red; }");
	}

	@Test
	public void directory() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "js/");
		StepVerifier.create(this.handler.handle(exchange))
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				}).verify(TIMEOUT);
	}

	@Test
	public void directoryInJarFile() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "underscorejs/");
		StepVerifier.create(this.handler.handle(exchange))
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				}).verify(TIMEOUT);
	}

	@Test
	public void missingResourcePath() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		setPathWithinHandlerMapping(exchange, "");
		StepVerifier.create(this.handler.handle(exchange))
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				}).verify(TIMEOUT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void noPathWithinHandlerMappingAttribute() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		this.handler.handle(exchange).block(TIMEOUT);
	}

	@Test(expected = MethodNotAllowedException.class)
	public void unsupportedHttpMethod() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.post(""));
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);
	}

	@Test
	public void resourceNotFound() throws Exception {
		for (HttpMethod method : HttpMethod.values()) {
			resourceNotFound(method);
		}
	}

	private void resourceNotFound(HttpMethod httpMethod) {
		MockServerHttpRequest request = MockServerHttpRequest.method(httpMethod, "").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "not-there.css");
		Mono<Void> mono = this.handler.handle(exchange);

		StepVerifier.create(mono)
				.expectErrorSatisfies(err -> {
					assertThat(err, instanceOf(ResponseStatusException.class));
					assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) err).getStatus());
				}).verify(TIMEOUT);

		// SPR-17475
		AtomicReference<Throwable> exceptionRef = new AtomicReference<>();
		StepVerifier.create(mono).consumeErrorWith(exceptionRef::set).verify();
		StepVerifier.create(mono).consumeErrorWith(ex -> assertNotSame(exceptionRef.get(), ex)).verify();
	}

	@Test
	public void partialContentByteRange() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("Range", "bytes=0-1").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertEquals(MediaType.TEXT_PLAIN, exchange.getResponse().getHeaders().getContentType());
		assertEquals(2, exchange.getResponse().getHeaders().getContentLength());
		assertEquals("bytes 0-1/10", exchange.getResponse().getHeaders().getFirst("Content-Range"));
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
		assertResponseBody(exchange, "So");
	}

	@Test
	public void partialContentByteRangeNoEnd() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("range", "bytes=9-").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertEquals(MediaType.TEXT_PLAIN, exchange.getResponse().getHeaders().getContentType());
		assertEquals(1, exchange.getResponse().getHeaders().getContentLength());
		assertEquals("bytes 9-9/10", exchange.getResponse().getHeaders().getFirst("Content-Range"));
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
		assertResponseBody(exchange, ".");
	}

	@Test
	public void partialContentByteRangeLargeEnd() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("range", "bytes=9-10000").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertEquals(MediaType.TEXT_PLAIN, exchange.getResponse().getHeaders().getContentType());
		assertEquals(1, exchange.getResponse().getHeaders().getContentLength());
		assertEquals("bytes 9-9/10", exchange.getResponse().getHeaders().getFirst("Content-Range"));
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
		assertResponseBody(exchange, ".");
	}

	@Test
	public void partialContentSuffixRange() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("range", "bytes=-1").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertEquals(MediaType.TEXT_PLAIN, exchange.getResponse().getHeaders().getContentType());
		assertEquals(1, exchange.getResponse().getHeaders().getContentLength());
		assertEquals("bytes 9-9/10", exchange.getResponse().getHeaders().getFirst("Content-Range"));
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
		assertResponseBody(exchange, ".");
	}

	@Test
	public void partialContentSuffixRangeLargeSuffix() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("range", "bytes=-11").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertEquals(MediaType.TEXT_PLAIN, exchange.getResponse().getHeaders().getContentType());
		assertEquals(10, exchange.getResponse().getHeaders().getContentLength());
		assertEquals("bytes 0-9/10", exchange.getResponse().getHeaders().getFirst("Content-Range"));
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
		assertEquals(1, exchange.getResponse().getHeaders().get("Accept-Ranges").size());
		assertResponseBody(exchange, "Some text.");
	}

	@Test
	public void partialContentInvalidRangeHeader() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("range", "bytes=foo bar").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");

		StepVerifier.create(this.handler.handle(exchange))
				.expectNextCount(0)
				.expectComplete()
				.verify();

		assertEquals(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, exchange.getResponse().getStatusCode());
		assertEquals("bytes", exchange.getResponse().getHeaders().getFirst("Accept-Ranges"));
	}

	@Test
	public void partialContentMultipleByteRanges() {
		MockServerHttpRequest request = MockServerHttpRequest.get("").header("Range", "bytes=0-1, 4-5, 8-9").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		setPathWithinHandlerMapping(exchange, "foo.txt");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals(HttpStatus.PARTIAL_CONTENT, exchange.getResponse().getStatusCode());
		assertTrue(exchange.getResponse().getHeaders().getContentType().toString()
				.startsWith("multipart/byteranges;boundary="));

		String boundary = "--" + exchange.getResponse().getHeaders().getContentType().toString().substring(30);

		Mono<DataBuffer> reduced = Flux.from(exchange.getResponse().getBody())
				.reduce(this.bufferFactory.allocateBuffer(), (previous, current) -> {
					previous.write(current);
					DataBufferUtils.release(current);
					return previous;
				});

		StepVerifier.create(reduced)
				.consumeNextWith(buf -> {
					String content = DataBufferTestUtils.dumpString(buf, StandardCharsets.UTF_8);
					String[] ranges = StringUtils.tokenizeToStringArray(content, "\r\n", false, true);

					assertEquals(boundary, ranges[0]);
					assertEquals("Content-Type: text/plain", ranges[1]);
					assertEquals("Content-Range: bytes 0-1/10", ranges[2]);
					assertEquals("So", ranges[3]);

					assertEquals(boundary, ranges[4]);
					assertEquals("Content-Type: text/plain", ranges[5]);
					assertEquals("Content-Range: bytes 4-5/10", ranges[6]);
					assertEquals(" t", ranges[7]);

					assertEquals(boundary, ranges[8]);
					assertEquals("Content-Type: text/plain", ranges[9]);
					assertEquals("Content-Range: bytes 8-9/10", ranges[10]);
					assertEquals("t.", ranges[11]);
				})
				.expectComplete()
				.verify();
	}

	@Test  // SPR-14005
	public void doOverwriteExistingCacheControlHeaders() {
		MockServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get(""));
		exchange.getResponse().getHeaders().setCacheControl(CacheControl.noStore().getHeaderValue());
		setPathWithinHandlerMapping(exchange, "foo.css");
		this.handler.handle(exchange).block(TIMEOUT);

		assertEquals("max-age=3600", exchange.getResponse().getHeaders().getCacheControl());
	}


	private void setPathWithinHandlerMapping(ServerWebExchange exchange, String path) {
		exchange.getAttributes().put(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE,
				PathContainer.parsePath(path));
	}

	private long resourceLastModified(String resourceName) throws IOException {
		return new ClassPathResource(resourceName, getClass()).getFile().lastModified();
	}

	private long resourceLastModifiedDate(String resourceName) throws IOException {
		return new ClassPathResource(resourceName, getClass()).getFile().lastModified();
	}

	private void assertResponseBody(MockServerWebExchange exchange, String responseBody) {
		StepVerifier.create(exchange.getResponse().getBody())
				.consumeNextWith(buf -> assertEquals(responseBody,
						DataBufferTestUtils.dumpString(buf, StandardCharsets.UTF_8)))
				.expectComplete()
				.verify();
	}

}
