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

package org.springframework.http.client.reactive;

import java.util.function.Consumer;
import java.util.function.Supplier;

import reactor.netty.http.HttpResources;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Factory to manage Reactor Netty resources, i.e. {@link LoopResources} for
 * event loop threads, and {@link ConnectionProvider} for the connection pool,
 * within the lifecycle of a Spring {@code ApplicationContext}.
 *
 * <p>This factory implements {@link InitializingBean} and {@link DisposableBean}
 * and is expected typically to be declared as a Spring-managed bean.
 *
 * @author Rossen Stoyanchev
 * @since 5.1
 */
public class ReactorResourceFactory implements InitializingBean, DisposableBean {

	private boolean useGlobalResources = true;

	@Nullable
	private Consumer<HttpResources> globalResourcesConsumer;

	private Supplier<ConnectionProvider> connectionProviderSupplier = () -> ConnectionProvider.elastic("webflux");

	private Supplier<LoopResources> loopResourcesSupplier = () -> LoopResources.create("webflux-http");

	@Nullable
	private ConnectionProvider connectionProvider;

	@Nullable
	private LoopResources loopResources;

	private boolean manageConnectionProvider = false;

	private boolean manageLoopResources = false;


	/**
	 * Whether to use global Reactor Netty resources via {@link HttpResources}.
	 * <p>Default is "true" in which case this factory initializes and stops the
	 * global Reactor Netty resources within Spring's {@code ApplicationContext}
	 * lifecycle. If set to "false" the factory manages its resources independent
	 * of the global ones.
	 * @param useGlobalResources whether to expose and manage the global resources
	 * @see #addGlobalResourcesConsumer(Consumer)
	 */
	public void setUseGlobalResources(boolean useGlobalResources) {
		this.useGlobalResources = useGlobalResources;
	}

	/**
	 * Whether this factory exposes the global
	 * {@link reactor.netty.http.HttpResources HttpResources} holder.
	 */
	public boolean isUseGlobalResources() {
		return this.useGlobalResources;
	}

	/**
	 * Add a Consumer for configuring the global Reactor Netty resources on
	 * startup. When this option is used, {@link #setUseGlobalResources} is also
	 * enabled.
	 * @param consumer the consumer to apply
	 * @see #setUseGlobalResources(boolean)
	 */
	public void addGlobalResourcesConsumer(Consumer<HttpResources> consumer) {
		this.useGlobalResources = true;
		this.globalResourcesConsumer = this.globalResourcesConsumer != null ?
				this.globalResourcesConsumer.andThen(consumer) : consumer;
	}

	/**
	 * Use this option when you don't want to participate in global resources and
	 * you want to customize the creation of the managed {@code ConnectionProvider}.
	 * <p>By default, {@code ConnectionProvider.elastic("http")} is used.
	 * <p>Note that this option is ignored if {@code userGlobalResources=false} or
	 * {@link #setConnectionProvider(ConnectionProvider)} is set.
	 * @param supplier the supplier to use
	 */
	public void setConnectionProviderSupplier(Supplier<ConnectionProvider> supplier) {
		this.connectionProviderSupplier = supplier;
	}

	/**
	 * Use this option when you don't want to participate in global resources and
	 * you want to customize the creation of the managed {@code LoopResources}.
	 * <p>By default, {@code LoopResources.create("reactor-http")} is used.
	 * <p>Note that this option is ignored if {@code userGlobalResources=false} or
	 * {@link #setLoopResources(LoopResources)} is set.
	 * @param supplier the supplier to use
	 */
	public void setLoopResourcesSupplier(Supplier<LoopResources> supplier) {
		this.loopResourcesSupplier = supplier;
	}

	/**
	 * Use this option when you want to provide an externally managed
	 * {@link ConnectionProvider} instance.
	 * @param connectionProvider the connection provider to use as is
	 */
	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	/**
	 * Return the configured {@link ConnectionProvider}.
	 */
	public ConnectionProvider getConnectionProvider() {
		Assert.state(this.connectionProvider != null, "ConnectionProvider not initialized yet");
		return this.connectionProvider;
	}

	/**
	 * Use this option when you want to provide an externally managed
	 * {@link LoopResources} instance.
	 * @param loopResources the loop resources to use as is
	 */
	public void setLoopResources(LoopResources loopResources) {
		this.loopResources = loopResources;
	}

	/**
	 * Return the configured {@link LoopResources}.
	 */
	public LoopResources getLoopResources() {
		Assert.state(this.loopResources != null, "LoopResources not initialized yet");
		return this.loopResources;
	}


	@Override
	public void afterPropertiesSet() {
		if (this.useGlobalResources) {
			Assert.isTrue(this.loopResources == null && this.connectionProvider == null,
					"'useGlobalResources' is mutually exclusive with explicitly configured resources");
			HttpResources httpResources = HttpResources.get();
			if (this.globalResourcesConsumer != null) {
				this.globalResourcesConsumer.accept(httpResources);
			}
			this.connectionProvider = httpResources;
			this.loopResources = httpResources;
		}
		else {
			if (this.loopResources == null) {
				this.manageLoopResources = true;
				this.loopResources = this.loopResourcesSupplier.get();
			}
			if (this.connectionProvider == null) {
				this.manageConnectionProvider = true;
				this.connectionProvider = this.connectionProviderSupplier.get();
			}
		}
	}

	@Override
	public void destroy() {
		if (this.useGlobalResources) {
			HttpResources.disposeLoopsAndConnectionsLater().block();
		}
		else {
			try {
				ConnectionProvider provider = this.connectionProvider;
				if (provider != null && this.manageConnectionProvider) {
					provider.disposeLater().block();
				}
			}
			catch (Throwable ex) {
				// ignore
			}

			try {
				LoopResources resources = this.loopResources;
				if (resources != null && this.manageLoopResources) {
					resources.disposeLater().block();
				}
			}
			catch (Throwable ex) {
				// ignore
			}
		}
	}

}
