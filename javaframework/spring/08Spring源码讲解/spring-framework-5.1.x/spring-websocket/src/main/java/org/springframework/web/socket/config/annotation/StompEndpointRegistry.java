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

package org.springframework.web.socket.config.annotation;

import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;
import org.springframework.web.util.UrlPathHelper;

/**
 * A contract for registering STOMP over WebSocket endpoints.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
public interface StompEndpointRegistry {

	/**
	 * Register a STOMP over WebSocket endpoint at the given mapping path.
	 */
	StompWebSocketEndpointRegistration addEndpoint(String... paths);

	/**
	 * Set the order of the {@link org.springframework.web.servlet.HandlerMapping}
	 * used for STOMP endpoints relative to other Spring MVC handler mappings.
	 * <p>By default this is set to 1.
	 */
	void setOrder(int order);

	/**
	 * Configure a customized {@link UrlPathHelper} for the STOMP endpoint
	 * {@link org.springframework.web.servlet.HandlerMapping HandlerMapping}.
	 */
	void setUrlPathHelper(UrlPathHelper urlPathHelper);

	/**
	 * Configure a handler for customizing or handling STOMP ERROR frames to clients.
	 * @param errorHandler the error handler
	 * @since 4.2
	 */
	WebMvcStompEndpointRegistry setErrorHandler(StompSubProtocolErrorHandler errorHandler);

}
