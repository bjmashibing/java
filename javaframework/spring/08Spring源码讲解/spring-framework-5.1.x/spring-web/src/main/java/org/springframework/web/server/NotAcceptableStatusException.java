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

package org.springframework.web.server;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

/**
 * Exception for errors that fit response status 406 (not acceptable).
 *
 * @author Rossen Stoyanchev
 * @since 5.0
 */
@SuppressWarnings("serial")
public class NotAcceptableStatusException extends ResponseStatusException {

	private final List<MediaType> supportedMediaTypes;


	/**
	 * Constructor for when the requested Content-Type is invalid.
	 */
	public NotAcceptableStatusException(String reason) {
		super(HttpStatus.NOT_ACCEPTABLE, reason);
		this.supportedMediaTypes = Collections.emptyList();
	}

	/**
	 * Constructor for when the requested Content-Type is not supported.
	 */
	public NotAcceptableStatusException(List<MediaType> supportedMediaTypes) {
		super(HttpStatus.NOT_ACCEPTABLE, "Could not find acceptable representation");
		this.supportedMediaTypes = Collections.unmodifiableList(supportedMediaTypes);
	}


	/**
	 * Return a Map with an "Accept" header, or an empty map.
	 * @since 5.1.11
	 */
	@Override
	public Map<String, String> getHeaders() {
		return !CollectionUtils.isEmpty(this.supportedMediaTypes) ?
				Collections.singletonMap("Accept", MediaType.toString(this.supportedMediaTypes)) :
				Collections.emptyMap();
	}

	/**
	 * Return the list of supported content types in cases when the Accept
	 * header is parsed but not supported, or an empty list otherwise.
	 */
	public List<MediaType> getSupportedMediaTypes() {
		return this.supportedMediaTypes;
	}

}
