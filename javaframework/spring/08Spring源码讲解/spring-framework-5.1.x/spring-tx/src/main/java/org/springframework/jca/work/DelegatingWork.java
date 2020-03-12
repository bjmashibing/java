/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.jca.work;

import javax.resource.spi.work.Work;

import org.springframework.util.Assert;

/**
 * Simple Work adapter that delegates to a given Runnable.
 *
 * @author Juergen Hoeller
 * @since 2.0.3
 * @see javax.resource.spi.work.Work
 * @see Runnable
 */
public class DelegatingWork implements Work {

	private final Runnable delegate;


	/**
	 * Create a new DelegatingWork.
	 * @param delegate the Runnable implementation to delegate to
	 */
	public DelegatingWork(Runnable delegate) {
		Assert.notNull(delegate, "Delegate must not be null");
		this.delegate = delegate;
	}

	/**
	 * Return the wrapped Runnable implementation.
	 */
	public final Runnable getDelegate() {
		return this.delegate;
	}


	/**
	 * Delegates execution to the underlying Runnable.
	 */
	@Override
	public void run() {
		this.delegate.run();
	}

	/**
	 * This implementation is empty, since we expect the Runnable
	 * to terminate based on some specific shutdown signal.
	 */
	@Override
	public void release() {
	}

}
