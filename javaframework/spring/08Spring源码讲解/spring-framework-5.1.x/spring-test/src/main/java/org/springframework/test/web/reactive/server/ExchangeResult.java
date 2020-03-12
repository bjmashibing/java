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

package org.springframework.test.web.reactive.server;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

/**
 * Container for request and response details for exchanges performed through
 * {@link WebTestClient}.
 *
 * <p>Note that a decoded response body is not exposed at this level since the
 * body may not have been decoded and consumed yet. Sub-types
 * {@link EntityExchangeResult} and {@link FluxExchangeResult} provide access
 * to a decoded response entity and a decoded (but not consumed) response body
 * respectively.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 * @see EntityExchangeResult
 * @see FluxExchangeResult
 */
public class ExchangeResult {

	private static final List<MediaType> PRINTABLE_MEDIA_TYPES = Arrays.asList(
			MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.parseMediaType("text/*"), MediaType.APPLICATION_FORM_URLENCODED);


	private final ClientHttpRequest request;

	private final ClientHttpResponse response;

	private final Mono<byte[]> requestBody;

	private final Mono<byte[]> responseBody;

	private final Duration timeout;

	@Nullable
	private final String uriTemplate;


	/**
	 * Create an instance with an HTTP request and response along with promises
	 * for the serialized request and response body content.
	 *
	 * @param request the HTTP request
	 * @param response the HTTP response
	 * @param requestBody capture of serialized request body content
	 * @param responseBody capture of serialized response body content
	 * @param timeout how long to wait for content to materialize
	 * @param uriTemplate the URI template used to set up the request, if any
	 */
	ExchangeResult(ClientHttpRequest request, ClientHttpResponse response,
			Mono<byte[]> requestBody, Mono<byte[]> responseBody, Duration timeout, @Nullable String uriTemplate) {

		Assert.notNull(request, "ClientHttpRequest is required");
		Assert.notNull(response, "ClientHttpResponse is required");
		Assert.notNull(requestBody, "'requestBody' is required");
		Assert.notNull(responseBody, "'responseBody' is required");

		this.request = request;
		this.response = response;
		this.requestBody = requestBody;
		this.responseBody = responseBody;
		this.timeout = timeout;
		this.uriTemplate = uriTemplate;
	}

	/**
	 * Copy constructor to use after body is decoded and/or consumed.
	 */
	ExchangeResult(ExchangeResult other) {
		this.request = other.request;
		this.response = other.response;
		this.requestBody = other.requestBody;
		this.responseBody = other.responseBody;
		this.timeout = other.timeout;
		this.uriTemplate = other.uriTemplate;
	}


	/**
	 * Return the method of the request.
	 */
	public HttpMethod getMethod() {
		return this.request.getMethod();
	}

	/**
	 * Return the URI of the request.
	 */
	public URI getUrl() {
		return this.request.getURI();
	}

	/**
	 * Return the original URI template used to prepare the request, if any.
	 */
	@Nullable
	public String getUriTemplate() {
		return this.uriTemplate;
	}

	/**
	 * Return the request headers sent to the server.
	 */
	public HttpHeaders getRequestHeaders() {
		return this.request.getHeaders();
	}

	/**
	 * Return the raw request body content written through the request.
	 * <p><strong>Note:</strong> If the request content has not been consumed
	 * for any reason yet, use of this method will trigger consumption.
	 * @throws IllegalStateException if the request body is not been fully written.
	 */
	@Nullable
	public byte[] getRequestBodyContent() {
		return this.requestBody.block(this.timeout);
	}


	/**
	 * Return the HTTP status code as an {@link HttpStatus} enum value.
	 */
	public HttpStatus getStatus() {
		return this.response.getStatusCode();
	}

	/**
	 * Return the HTTP status code (potentially non-standard and not resolvable
	 * through the {@link HttpStatus} enum) as an integer.
	 * @since 5.1.10
	 */
	public int getRawStatusCode() {
		return this.response.getRawStatusCode();
	}

	/**
	 * Return the response headers received from the server.
	 */
	public HttpHeaders getResponseHeaders() {
		return this.response.getHeaders();
	}

	/**
	 * Return response cookies received from the server.
	 */
	public MultiValueMap<String, ResponseCookie> getResponseCookies() {
		return this.response.getCookies();
	}

	/**
	 * Return the raw request body content written to the response.
	 * <p><strong>Note:</strong> If the response content has not been consumed
	 * yet, use of this method will trigger consumption.
	 * @throws IllegalStateException if the response is not been fully read.
	 */
	@Nullable
	public byte[] getResponseBodyContent() {
		return this.responseBody.block(this.timeout);
	}


	/**
	 * Execute the given Runnable, catch any {@link AssertionError}, decorate
	 * with {@code AssertionError} containing diagnostic information about the
	 * request and response, and then re-throw.
	 */
	public void assertWithDiagnostics(Runnable assertion) {
		try {
			assertion.run();
		}
		catch (AssertionError ex) {
			throw new AssertionError(ex.getMessage() + "\n" + this, ex);
		}
	}


	@Override
	public String toString() {
		return "\n" +
				"> " + getMethod() + " " + getUrl() + "\n" +
				"> " + formatHeaders(getRequestHeaders(), "\n> ") + "\n" +
				"\n" +
				formatBody(getRequestHeaders().getContentType(), this.requestBody) + "\n" +
				"\n" +
				"< " + getStatus() + " " + getStatus().getReasonPhrase() + "\n" +
				"< " + formatHeaders(getResponseHeaders(), "\n< ") + "\n" +
				"\n" +
				formatBody(getResponseHeaders().getContentType(), this.responseBody) +"\n";
	}

	private String formatHeaders(HttpHeaders headers, String delimiter) {
		return headers.entrySet().stream()
				.map(entry -> entry.getKey() + ": " + entry.getValue())
				.collect(Collectors.joining(delimiter));
	}

	@Nullable
	private String formatBody(@Nullable MediaType contentType, Mono<byte[]> body) {
		return body
				.map(bytes -> {
					if (contentType == null) {
						return bytes.length + " bytes of content (unknown content-type).";
					}
					Charset charset = contentType.getCharset();
					if (charset != null) {
						return new String(bytes, charset);
					}
					if (PRINTABLE_MEDIA_TYPES.stream().anyMatch(contentType::isCompatibleWith)) {
						return new String(bytes, StandardCharsets.UTF_8);
					}
					return bytes.length + " bytes of content.";
				})
				.defaultIfEmpty("No content")
				.onErrorResume(ex -> Mono.just("Failed to obtain content: " + ex.getMessage()))
				.block(this.timeout);
	}

}
