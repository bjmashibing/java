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

package org.springframework.http.client;

import java.io.Closeable;
import java.io.IOException;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;

/**
 * Represents a client-side HTTP response.
 * Obtained via an calling of the {@link ClientHttpRequest#execute()}.
 *
 * <p>A {@code ClientHttpResponse} must be {@linkplain #close() closed},
 * typically in a {@code finally} block.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public interface ClientHttpResponse extends HttpInputMessage, Closeable {

	/**
	 * Return the HTTP status code as an {@link HttpStatus} enum value.
	 * @return the HTTP status as an HttpStatus enum value (never {@code null})
	 * @throws IOException in case of I/O errors
	 * @throws IllegalArgumentException in case of an unknown HTTP status code
	 * @since #getRawStatusCode()
	 * @see HttpStatus#valueOf(int)
	 */
	HttpStatus getStatusCode() throws IOException;

	/**
	 * Return the HTTP status code (potentially non-standard and not
	 * resolvable through the {@link HttpStatus} enum) as an integer.
	 * @return the HTTP status as an integer value
	 * @throws IOException in case of I/O errors
	 * @since 3.1.1
	 * @see #getStatusCode()
	 * @see HttpStatus#resolve(int)
	 */
	int getRawStatusCode() throws IOException;

	/**
	 * Return the HTTP status text of the response.
	 * @return the HTTP status text
	 * @throws IOException in case of I/O errors
	 */
	String getStatusText() throws IOException;

	/**
	 * Close this response, freeing any resources created.
	 */
	@Override
	void close();

}
