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

package org.springframework.http.server.reactive;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.logging.Log;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import org.springframework.core.log.LogDelegateFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * An alternative to {@link AbstractListenerWriteProcessor} but instead writing
 * a {@code Publisher<Publisher<T>>} with flush boundaries enforces after
 * the completion of each nested Publisher.
 *
 * @author Arjen Poutsma
 * @author Violeta Georgieva
 * @author Rossen Stoyanchev
 * @since 5.0
 * @param <T> the type of element signaled to the {@link Subscriber}
 */
public abstract class AbstractListenerWriteFlushProcessor<T> implements Processor<Publisher<? extends T>, Void> {

	/**
	 * Special logger for debugging Reactive Streams signals.
	 * @see LogDelegateFactory#getHiddenLog(Class)
	 * @see AbstractListenerReadPublisher#rsReadLogger
	 * @see AbstractListenerWriteProcessor#rsWriteLogger
	 * @see WriteResultPublisher#rsWriteResultLogger
	 */
	protected static final Log rsWriteFlushLogger =
			LogDelegateFactory.getHiddenLog(AbstractListenerWriteFlushProcessor.class);


	private final AtomicReference<State> state = new AtomicReference<>(State.UNSUBSCRIBED);

	@Nullable
	private Subscription subscription;

	private volatile boolean subscriberCompleted;

	private final WriteResultPublisher resultPublisher;

	private final String logPrefix;


	public AbstractListenerWriteFlushProcessor() {
		this("");
	}

	/**
	 * Create an instance with the given log prefix.
	 * @since 5.1
	 */
	public AbstractListenerWriteFlushProcessor(String logPrefix) {
		this.logPrefix = logPrefix;
		this.resultPublisher = new WriteResultPublisher(logPrefix);
	}


	/**
	 * Create an instance with the given log prefix.
	 * @since 5.1
	 */
	public String getLogPrefix() {
		return this.logPrefix;
	}


	// Subscriber methods and async I/O notification methods...

	@Override
	public final void onSubscribe(Subscription subscription) {
		this.state.get().onSubscribe(this, subscription);
	}

	@Override
	public final void onNext(Publisher<? extends T> publisher) {
		if (rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + "Received onNext publisher");
		}
		this.state.get().onNext(this, publisher);
	}

	/**
	 * Error signal from the upstream, write Publisher. This is also used by
	 * sub-classes to delegate error notifications from the container.
	 */
	@Override
	public final void onError(Throwable ex) {
		if (rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + "Received onError: " + ex);
		}
		this.state.get().onError(this, ex);
	}

	/**
	 * Completion signal from the upstream, write Publisher. This is also used
	 * by sub-classes to delegate completion notifications from the container.
	 */
	@Override
	public final void onComplete() {
		if (rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + "Received onComplete");
		}
		this.state.get().onComplete(this);
	}

	/**
	 * Invoked when flushing is possible, either in the same thread after a check
	 * via {@link #isWritePossible()}, or as a callback from the underlying
	 * container.
	 */
	protected final void onFlushPossible() {
		this.state.get().onFlushPossible(this);
	}

	/**
	 * Invoked during an error or completion callback from the underlying
	 * container to cancel the upstream subscription.
	 */
	protected void cancel() {
		if (rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + "Received request to cancel");
		}
		if (this.subscription != null) {
			this.subscription.cancel();
		}
	}


	// Publisher implementation for result notifications...

	@Override
	public final void subscribe(Subscriber<? super Void> subscriber) {
		this.resultPublisher.subscribe(subscriber);
	}


	// Write API methods to be implemented or template methods to override...

	/**
	 * Create a new processor for the current flush boundary.
	 */
	protected abstract Processor<? super T, Void> createWriteProcessor();

	/**
	 * Whether writing/flushing is possible.
	 */
	protected abstract boolean isWritePossible();

	/**
	 * Flush the output if ready, or otherwise {@link #isFlushPending()} should
	 * return true after.
	 * <p>This is primarily for the Servlet non-blocking I/O API where flush
	 * cannot be called without a readyToWrite check.
	 */
	protected abstract void flush() throws IOException;

	/**
	 * Whether flushing is pending.
	 * <p>This is primarily for the Servlet non-blocking I/O API where flush
	 * cannot be called without a readyToWrite check.
	 */
	protected abstract boolean isFlushPending();

	/**
	 * Invoked when an error happens while flushing. Sub-classes may choose
	 * to ignore this if they know the underlying API will provide an error
	 * notification in a container thread.
	 * <p>Defaults to no-op.
	 */
	protected void flushingFailed(Throwable t) {
	}


	// Private methods for use in State...

	private boolean changeState(State oldState, State newState) {
		boolean result = this.state.compareAndSet(oldState, newState);
		if (result && rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + oldState + " -> " + newState);
		}
		return result;
	}

	private void flushIfPossible() {
		boolean result = isWritePossible();
		if (rsWriteFlushLogger.isTraceEnabled()) {
			rsWriteFlushLogger.trace(getLogPrefix() + "isWritePossible[" + result + "]");
		}
		if (result) {
			onFlushPossible();
		}
	}


	/**
	 * Represents a state for the {@link Processor} to be in.
	 *
	 * <p><pre>
	 *       UNSUBSCRIBED
	 *            |
	 *            v
	 *        REQUESTED <---> RECEIVED ------+
	 *            |              |           |
	 *            |              v           |
	 *            |           FLUSHING       |
	 *            |              |           |
	 *            |              v           |
	 *            +--------> COMPLETED <-----+
	 * </pre>
	 */
	private enum State {

		UNSUBSCRIBED {
			@Override
			public <T> void onSubscribe(AbstractListenerWriteFlushProcessor<T> processor, Subscription subscription) {
				Assert.notNull(subscription, "Subscription must not be null");
				if (processor.changeState(this, REQUESTED)) {
					processor.subscription = subscription;
					subscription.request(1);
				}
				else {
					super.onSubscribe(processor, subscription);
				}
			}

			@Override
			public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				// This can happen on (very early) completion notification from container..
				if (processor.changeState(this, COMPLETED)) {
					processor.resultPublisher.publishComplete();
				}
				else {
					processor.state.get().onComplete(processor);
				}
			}
		},

		REQUESTED {
			@Override
			public <T> void onNext(AbstractListenerWriteFlushProcessor<T> processor,
					Publisher<? extends T> currentPublisher) {

				if (processor.changeState(this, RECEIVED)) {
					Processor<? super T, Void> currentProcessor = processor.createWriteProcessor();
					currentPublisher.subscribe(currentProcessor);
					currentProcessor.subscribe(new WriteResultSubscriber(processor));
				}
			}
			@Override
			public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				if (processor.changeState(this, COMPLETED)) {
					processor.resultPublisher.publishComplete();
				}
				else {
					processor.state.get().onComplete(processor);
				}
			}
		},

		RECEIVED {
			@Override
			public <T> void writeComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				try {
					processor.flush();
				}
				catch (Throwable ex) {
					processor.flushingFailed(ex);
					return;
				}
				if (processor.changeState(this, REQUESTED)) {
					if (processor.subscriberCompleted) {
						handleSubscriberCompleted(processor);
					}
					else {
						Assert.state(processor.subscription != null, "No subscription");
						processor.subscription.request(1);
					}
				}
			}
			@Override
			public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				processor.subscriberCompleted = true;
				// A competing write might have completed very quickly
				if (processor.state.get().equals(State.REQUESTED)) {
					handleSubscriberCompleted(processor);
				}
			}

			private <T> void handleSubscriberCompleted(AbstractListenerWriteFlushProcessor<T> processor) {
				if (processor.isFlushPending()) {
					// Ensure the final flush
					processor.changeState(State.REQUESTED, State.FLUSHING);
					processor.flushIfPossible();
				}
				else if (processor.changeState(State.REQUESTED, State.COMPLETED)) {
					processor.resultPublisher.publishComplete();
				}
				else {
					processor.state.get().onComplete(processor);
				}
			}
		},

		FLUSHING {
			@Override
			public <T> void onFlushPossible(AbstractListenerWriteFlushProcessor<T> processor) {
				try {
					processor.flush();
				}
				catch (Throwable ex) {
					processor.flushingFailed(ex);
					return;
				}
				if (processor.changeState(this, COMPLETED)) {
					processor.resultPublisher.publishComplete();
				}
				else {
					processor.state.get().onComplete(processor);
				}
			}
			@Override
			public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {
				// ignore
			}
			@Override
			public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				// ignore
			}
		},

		COMPLETED {
			@Override
			public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {
				// ignore
			}
			@Override
			public <T> void onError(AbstractListenerWriteFlushProcessor<T> processor, Throwable t) {
				// ignore
			}
			@Override
			public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
				// ignore
			}
		};


		public <T> void onSubscribe(AbstractListenerWriteFlushProcessor<T> proc, Subscription subscription) {
			subscription.cancel();
		}

		public <T> void onNext(AbstractListenerWriteFlushProcessor<T> proc, Publisher<? extends T> pub) {
			throw new IllegalStateException(toString());
		}

		public <T> void onError(AbstractListenerWriteFlushProcessor<T> processor, Throwable ex) {
			if (processor.changeState(this, COMPLETED)) {
				processor.resultPublisher.publishError(ex);
			}
			else {
				processor.state.get().onError(processor, ex);
			}
		}

		public <T> void onComplete(AbstractListenerWriteFlushProcessor<T> processor) {
			throw new IllegalStateException(toString());
		}

		public <T> void writeComplete(AbstractListenerWriteFlushProcessor<T> processor) {
			throw new IllegalStateException(toString());
		}

		public <T> void onFlushPossible(AbstractListenerWriteFlushProcessor<T> processor) {
			// ignore
		}


		/**
		 * Subscriber to receive and delegate completion notifications for from
		 * the current Publisher, i.e. for the current flush boundary.
		 */
		private static class WriteResultSubscriber implements Subscriber<Void> {

			private final AbstractListenerWriteFlushProcessor<?> processor;


			public WriteResultSubscriber(AbstractListenerWriteFlushProcessor<?> processor) {
				this.processor = processor;
			}

			@Override
			public void onSubscribe(Subscription subscription) {
				subscription.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Void aVoid) {
			}

			@Override
			public void onError(Throwable ex) {
				this.processor.cancel();
				this.processor.onError(ex);
			}

			@Override
			public void onComplete() {
				if (rsWriteFlushLogger.isTraceEnabled()) {
					rsWriteFlushLogger.trace(this.processor.getLogPrefix() + this.processor.state + " writeComplete");
				}
				this.processor.state.get().writeComplete(this.processor);
			}
		}
	}

}
