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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import reactor.core.publisher.Mono;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;

/**
 * A {@code ResourceResolver} that delegates to the chain to locate a resource
 * and then attempts to find a variation with the ".gz" extension.
 *
 * <p>The resolver gets involved only if the "Accept-Encoding" request header
 * contains the value "gzip" indicating the client accepts gzipped responses.
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 * @deprecated as of 5.1, in favor of using {@link EncodedResourceResolver}
 */
@Deprecated
public class GzipResourceResolver extends AbstractResourceResolver {

	@Override
	protected Mono<Resource> resolveResourceInternal(@Nullable ServerWebExchange exchange,
			String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {

		return chain.resolveResource(exchange, requestPath, locations)
				.map(resource -> {
					if (exchange == null || isGzipAccepted(exchange)) {
						try {
							Resource gzipped = new GzippedResource(resource);
							if (gzipped.exists()) {
								resource = gzipped;
							}
						}
						catch (IOException ex) {
							String logPrefix = exchange != null ? exchange.getLogPrefix() : "";
							logger.trace(logPrefix + "No gzip resource for [" + resource.getFilename() + "]", ex);
						}
					}
					return resource;
				});
	}

	private boolean isGzipAccepted(ServerWebExchange exchange) {
		String value = exchange.getRequest().getHeaders().getFirst("Accept-Encoding");
		return (value != null && value.toLowerCase().contains("gzip"));
	}

	@Override
	protected Mono<String> resolveUrlPathInternal(String resourceUrlPath,
			List<? extends Resource> locations, ResourceResolverChain chain) {

		return chain.resolveUrlPath(resourceUrlPath, locations);
	}


	/**
	 * A gzipped {@link HttpResource}.
	 */
	static final class GzippedResource extends AbstractResource implements HttpResource {

		private final Resource original;

		private final Resource gzipped;

		public GzippedResource(Resource original) throws IOException {
			this.original = original;
			this.gzipped = original.createRelative(original.getFilename() + ".gz");
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return this.gzipped.getInputStream();
		}

		@Override
		public boolean exists() {
			return this.gzipped.exists();
		}

		@Override
		public boolean isReadable() {
			return this.gzipped.isReadable();
		}

		@Override
		public boolean isOpen() {
			return this.gzipped.isOpen();
		}

		@Override
		public boolean isFile() {
			return this.gzipped.isFile();
		}

		@Override
		public URL getURL() throws IOException {
			return this.gzipped.getURL();
		}

		@Override
		public URI getURI() throws IOException {
			return this.gzipped.getURI();
		}

		@Override
		public File getFile() throws IOException {
			return this.gzipped.getFile();
		}

		@Override
		public long contentLength() throws IOException {
			return this.gzipped.contentLength();
		}

		@Override
		public long lastModified() throws IOException {
			return this.gzipped.lastModified();
		}

		@Override
		public Resource createRelative(String relativePath) throws IOException {
			return this.gzipped.createRelative(relativePath);
		}

		@Override
		@Nullable
		public String getFilename() {
			return this.original.getFilename();
		}

		@Override
		public String getDescription() {
			return this.gzipped.getDescription();
		}

		@Override
		public HttpHeaders getResponseHeaders() {
			HttpHeaders headers = (this.original instanceof HttpResource ?
					((HttpResource) this.original).getResponseHeaders() : new HttpHeaders());
			headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
			headers.add(HttpHeaders.VARY, HttpHeaders.ACCEPT_ENCODING);
			return headers;
		}
	}

}
