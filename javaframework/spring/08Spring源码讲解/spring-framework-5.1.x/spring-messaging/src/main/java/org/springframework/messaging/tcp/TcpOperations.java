/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.messaging.tcp;

import org.springframework.util.concurrent.ListenableFuture;

/**
 * A contract for establishing TCP connections.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 * @param <P> the type of payload for in and outbound messages
 */
public interface TcpOperations<P> {

	/**
	 * Open a new connection.
	 * @param connectionHandler a handler to manage the connection
	 * @return a ListenableFuture that can be used to determine when and if the
	 * connection is successfully established
	 */
	ListenableFuture<Void> connect(TcpConnectionHandler<P> connectionHandler);

	/**
	 * Open a new connection and a strategy for reconnecting if the connection fails.
	 * @param connectionHandler a handler to manage the connection
	 * @param reconnectStrategy a strategy for reconnecting
	 * @return a ListenableFuture that can be used to determine when and if the
	 * initial connection is successfully established
	 */
	ListenableFuture<Void> connect(TcpConnectionHandler<P> connectionHandler, ReconnectStrategy reconnectStrategy);

	/**
	 * Shut down and close any open connections.
	 * @return a ListenableFuture that can be used to determine when and if the
	 * connection is successfully closed
	 */
	ListenableFuture<Void> shutdown();

}
