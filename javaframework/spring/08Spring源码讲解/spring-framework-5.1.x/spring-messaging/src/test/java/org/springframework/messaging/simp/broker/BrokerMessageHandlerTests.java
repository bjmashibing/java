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

package org.springframework.messaging.simp.broker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link org.springframework.messaging.simp.broker.AbstractBrokerMessageHandler}.
 *
 * @author Rossen Stoyanchev
 */
public class BrokerMessageHandlerTests {

	private TestBrokerMessageHandler handler;


	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.handler = new TestBrokerMessageHandler();
	}


	@Test
	public void startShouldUpdateIsRunning() {
		assertFalse(this.handler.isRunning());
		this.handler.start();
		assertTrue(this.handler.isRunning());
	}

	@Test
	public void stopShouldUpdateIsRunning() {

		this.handler.start();
		assertTrue(this.handler.isRunning());

		this.handler.stop();
		assertFalse(this.handler.isRunning());
	}

	@Test
	public void startAndStopShouldNotPublishBrokerAvailabilityEvents() {
		this.handler.start();
		this.handler.stop();
		assertEquals(Collections.emptyList(), this.handler.availabilityEvents);
	}

	@Test
	public void handleMessageWhenBrokerNotRunning() {
		this.handler.handleMessage(new GenericMessage<Object>("payload"));
		assertEquals(Collections.emptyList(), this.handler.messages);
	}

	@Test
	public void publishBrokerAvailableEvent() {

		assertFalse(this.handler.isBrokerAvailable());
		assertEquals(Collections.emptyList(), this.handler.availabilityEvents);

		this.handler.publishBrokerAvailableEvent();

		assertTrue(this.handler.isBrokerAvailable());
		assertEquals(Arrays.asList(true), this.handler.availabilityEvents);
	}

	@Test
	public void publishBrokerAvailableEventWhenAlreadyAvailable() {

		this.handler.publishBrokerAvailableEvent();
		this.handler.publishBrokerAvailableEvent();

		assertEquals(Arrays.asList(true), this.handler.availabilityEvents);
	}

	@Test
	public void publishBrokerUnavailableEvent() {

		this.handler.publishBrokerAvailableEvent();
		assertTrue(this.handler.isBrokerAvailable());

		this.handler.publishBrokerUnavailableEvent();
		assertFalse(this.handler.isBrokerAvailable());

		assertEquals(Arrays.asList(true, false), this.handler.availabilityEvents);
	}

	@Test
	public void publishBrokerUnavailableEventWhenAlreadyUnavailable() {

		this.handler.publishBrokerAvailableEvent();
		this.handler.publishBrokerUnavailableEvent();
		this.handler.publishBrokerUnavailableEvent();

		assertEquals(Arrays.asList(true, false), this.handler.availabilityEvents);
	}


	private static class TestBrokerMessageHandler extends AbstractBrokerMessageHandler
			implements ApplicationEventPublisher {

		private final List<Message<?>> messages = new ArrayList<>();

		private final List<Boolean> availabilityEvents = new ArrayList<>();


		private TestBrokerMessageHandler() {
			super(mock(SubscribableChannel.class), mock(MessageChannel.class), mock(SubscribableChannel.class));
			setApplicationEventPublisher(this);
		}

		@Override
		protected void handleMessageInternal(Message<?> message) {
			this.messages.add(message);
		}

		@Override
		public void publishEvent(ApplicationEvent event) {
			publishEvent((Object) event);
		}

		@Override
		public void publishEvent(Object event) {
			if (event instanceof BrokerAvailabilityEvent) {
				this.availabilityEvents.add(((BrokerAvailabilityEvent) event).isBrokerAvailable());
			}
		}
	}

}
