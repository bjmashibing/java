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

package org.springframework.web.reactive.socket.client;

import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoProcessor;

import org.springframework.context.Lifecycle;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.adapter.JettyWebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.adapter.JettyWebSocketSession;

/**
 * A {@link WebSocketClient} implementation for use with Jetty
 * {@link org.eclipse.jetty.websocket.client.WebSocketClient}.
 *
 * <p><strong>Note: </strong> the Jetty {@code WebSocketClient} requires
 * lifecycle management and must be started and stopped. This is automatically
 * managed when this class is declared as a Spring bean and created with the
 * default constructor. See constructor notes for more details.
 *
 * @author Violeta Georgieva
 * @author Rossen Stoyanchev
 * @since 5.0
 */
public class JettyWebSocketClient implements WebSocketClient, Lifecycle {

	private static final Log logger = LogFactory.getLog(JettyWebSocketClient.class);


	private final org.eclipse.jetty.websocket.client.WebSocketClient jettyClient;

	private final boolean externallyManaged;

	private final DataBufferFactory bufferFactory = new DefaultDataBufferFactory();


	/**
	 * Default constructor that creates and manages an instance of a Jetty
	 * {@link org.eclipse.jetty.websocket.client.WebSocketClient WebSocketClient}.
	 * The instance can be obtained with {@link #getJettyClient()} for further
	 * configuration.
	 *
	 * <p><strong>Note: </strong> When this constructor is used {@link Lifecycle}
	 * methods of this class are delegated to the Jetty {@code WebSocketClient}.
	 */
	public JettyWebSocketClient() {
		this.jettyClient = new org.eclipse.jetty.websocket.client.WebSocketClient();
		this.externallyManaged = false;
	}

	/**
	 * Constructor that accepts an existing instance of a Jetty
	 * {@link org.eclipse.jetty.websocket.client.WebSocketClient WebSocketClient}.
	 *
	 * <p><strong>Note: </strong> Use of this constructor implies the Jetty
	 * {@code WebSocketClient} is externally managed and hence {@link Lifecycle}
	 * methods of this class are not delegated to it.
	 */
	public JettyWebSocketClient(org.eclipse.jetty.websocket.client.WebSocketClient jettyClient) {
		this.jettyClient = jettyClient;
		this.externallyManaged = true;
	}


	/**
	 * Return the underlying Jetty {@code WebSocketClient}.
	 */
	public org.eclipse.jetty.websocket.client.WebSocketClient getJettyClient() {
		return this.jettyClient;
	}


	@Override
	public void start() {
		if (!this.externallyManaged) {
			try {
				this.jettyClient.start();
			}
			catch (Exception ex) {
				throw new IllegalStateException("Failed to start Jetty WebSocketClient", ex);
			}
		}
	}

	@Override
	public void stop() {
		if (!this.externallyManaged) {
			try {
				this.jettyClient.stop();
			}
			catch (Exception ex) {
				throw new IllegalStateException("Error stopping Jetty WebSocketClient", ex);
			}
		}
	}

	@Override
	public boolean isRunning() {
		return this.jettyClient.isRunning();
	}


	@Override
	public Mono<Void> execute(URI url, WebSocketHandler handler) {
		return execute(url, new HttpHeaders(), handler);
	}

	@Override
	public Mono<Void> execute(URI url, HttpHeaders headers, WebSocketHandler handler) {
		return executeInternal(url, headers, handler);
	}

	private Mono<Void> executeInternal(URI url, HttpHeaders headers, WebSocketHandler handler) {
		MonoProcessor<Void> completionMono = MonoProcessor.create();
		return Mono.fromCallable(
				() -> {
					if (logger.isDebugEnabled()) {
						logger.debug("Connecting to " + url);
					}
					Object jettyHandler = createHandler(url, handler, completionMono);
					ClientUpgradeRequest request = new ClientUpgradeRequest();
					request.setSubProtocols(handler.getSubProtocols());
					UpgradeListener upgradeListener = new DefaultUpgradeListener(headers);
					return this.jettyClient.connect(jettyHandler, url, request, upgradeListener);
				})
				.then(completionMono);
	}

	private Object createHandler(URI url, WebSocketHandler handler, MonoProcessor<Void> completion) {
		return new JettyWebSocketHandlerAdapter(handler, session -> {
			HandshakeInfo info = createHandshakeInfo(url, session);
			return new JettyWebSocketSession(session, info, this.bufferFactory, completion);
		});
	}

	private HandshakeInfo createHandshakeInfo(URI url, Session jettySession) {
		HttpHeaders headers = new HttpHeaders();
		jettySession.getUpgradeResponse().getHeaders().forEach(headers::put);
		String protocol = headers.getFirst("Sec-WebSocket-Protocol");
		return new HandshakeInfo(url, headers, Mono.empty(), protocol);
	}


	private static class DefaultUpgradeListener implements UpgradeListener {

		private final HttpHeaders headers;


		public DefaultUpgradeListener(HttpHeaders headers) {
			this.headers = headers;
		}

		@Override
		public void onHandshakeRequest(UpgradeRequest request) {
			this.headers.forEach(request::setHeader);
		}

		@Override
		public void onHandshakeResponse(UpgradeResponse response) {
		}
	}

}
