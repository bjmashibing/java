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

package org.springframework.messaging.simp.stomp;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession.Receiptable;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.messaging.tcp.TcpConnection;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.concurrent.SettableListenableFuture;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.notNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultStompSession}.
 *
 * @author Rossen Stoyanchev
 */
public class DefaultStompSessionTests {

	private DefaultStompSession session;

	@Mock
	private StompSessionHandler sessionHandler;

	private StompHeaders connectHeaders;

	@Mock
	private TcpConnection<byte[]> connection;

	@Captor
	private ArgumentCaptor<Message<byte[]>> messageCaptor;

	@Rule
	public ExpectedException expected = ExpectedException.none();


	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		this.sessionHandler = mock(StompSessionHandler.class);
		this.connectHeaders = new StompHeaders();
		this.session = new DefaultStompSession(this.sessionHandler, this.connectHeaders);
		this.session.setMessageConverter(
				new CompositeMessageConverter(
						Arrays.asList(new StringMessageConverter(), new ByteArrayMessageConverter())));

		SettableListenableFuture<Void> future = new SettableListenableFuture<>();
		future.set(null);
		when(this.connection.send(this.messageCaptor.capture())).thenReturn(future);
	}


	@Test
	public void afterConnected() {
		assertFalse(this.session.isConnected());
		this.connectHeaders.setHost("my-host");
		this.connectHeaders.setHeartbeat(new long[] {11, 12});

		this.session.afterConnected(this.connection);

		assertTrue(this.session.isConnected());
		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.CONNECT, accessor.getCommand());
		assertEquals("my-host", accessor.getHost());
		assertThat(accessor.getAcceptVersion(), containsInAnyOrder("1.1", "1.2"));
		assertArrayEquals(new long[] {11, 12}, accessor.getHeartbeat());
	}

	@Test // SPR-16844
	public void afterConnectedWithSpecificVersion() {
		assertFalse(this.session.isConnected());
		this.connectHeaders.setAcceptVersion("1.1");

		this.session.afterConnected(this.connection);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.CONNECT, accessor.getCommand());
		assertThat(accessor.getAcceptVersion(), containsInAnyOrder("1.1"));
	}

	@Test
	public void afterConnectFailure() {
		IllegalStateException exception = new IllegalStateException("simulated exception");
		this.session.afterConnectFailure(exception);
		verify(this.sessionHandler).handleTransportError(this.session, exception);
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleConnectedFrame() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		this.connectHeaders.setHeartbeat(new long[] {10000, 10000});

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECTED);
		accessor.setVersion("1.2");
		accessor.setHeartbeat(10000, 10000);
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		verify(this.sessionHandler).afterConnected(this.session, stompHeaders);
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void heartbeatValues() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		this.connectHeaders.setHeartbeat(new long[] {10000, 10000});

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECTED);
		accessor.setVersion("1.2");
		accessor.setHeartbeat(20000, 20000);
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		ArgumentCaptor<Long> writeInterval = ArgumentCaptor.forClass(Long.class);
		verify(this.connection).onWriteInactivity(any(Runnable.class), writeInterval.capture());
		assertEquals(20000, (long) writeInterval.getValue());

		ArgumentCaptor<Long> readInterval = ArgumentCaptor.forClass(Long.class);
		verify(this.connection).onReadInactivity(any(Runnable.class), readInterval.capture());
		assertEquals(60000, (long) readInterval.getValue());
	}

	@Test
	public void heartbeatNotSupportedByServer() {
		this.session.afterConnected(this.connection);
		verify(this.connection).send(any());

		this.connectHeaders.setHeartbeat(new long[] {10000, 10000});

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECTED);
		accessor.setVersion("1.2");
		accessor.setHeartbeat(0, 0);
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		verifyNoMoreInteractions(this.connection);
	}

	@Test
	public void heartbeatTasks() {
		this.session.afterConnected(this.connection);
		verify(this.connection).send(any());

		this.connectHeaders.setHeartbeat(new long[] {10000, 10000});

		StompHeaderAccessor connected = StompHeaderAccessor.create(StompCommand.CONNECTED);
		connected.setVersion("1.2");
		connected.setHeartbeat(10000, 10000);
		connected.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], connected.getMessageHeaders()));

		ArgumentCaptor<Runnable> writeTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
		ArgumentCaptor<Runnable> readTaskCaptor = ArgumentCaptor.forClass(Runnable.class);
		verify(this.connection).onWriteInactivity(writeTaskCaptor.capture(), any(Long.class));
		verify(this.connection).onReadInactivity(readTaskCaptor.capture(), any(Long.class));

		Runnable writeTask = writeTaskCaptor.getValue();
		Runnable readTask = readTaskCaptor.getValue();
		assertNotNull(writeTask);
		assertNotNull(readTask);

		writeTask.run();
		StompHeaderAccessor accessor = StompHeaderAccessor.createForHeartbeat();
		Message<byte[]> message = MessageBuilder.createMessage(new byte[] {'\n'}, accessor.getMessageHeaders());
		verify(this.connection).send(eq(message));
		verifyNoMoreInteractions(this.connection);

		reset(this.sessionHandler);
		readTask.run();
		verify(this.sessionHandler).handleTransportError(same(this.session), any(IllegalStateException.class));
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleErrorFrame() {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setContentType(new MimeType("text", "plain", StandardCharsets.UTF_8));
		accessor.addNativeHeader("foo", "bar");
		accessor.setLeaveMutable(true);
		String payload = "Oops";

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		when(this.sessionHandler.getPayloadType(stompHeaders)).thenReturn(String.class);

		this.session.handleMessage(MessageBuilder.createMessage(
				payload.getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders()));

		verify(this.sessionHandler).getPayloadType(stompHeaders);
		verify(this.sessionHandler).handleFrame(stompHeaders, payload);
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleErrorFrameWithEmptyPayload() {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.addNativeHeader("foo", "bar");
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		verify(this.sessionHandler).handleFrame(stompHeaders, null);
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleErrorFrameWithConversionException() {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
		accessor.addNativeHeader("foo", "bar");
		accessor.setLeaveMutable(true);
		byte[] payload = "{'foo':'bar'}".getBytes(StandardCharsets.UTF_8);

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		when(this.sessionHandler.getPayloadType(stompHeaders)).thenReturn(Map.class);

		this.session.handleMessage(MessageBuilder.createMessage(payload, accessor.getMessageHeaders()));

		verify(this.sessionHandler).getPayloadType(stompHeaders);
		verify(this.sessionHandler).handleException(same(this.session), same(StompCommand.ERROR),
				eq(stompHeaders), same(payload), any(MessageConversionException.class));
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleMessageFrame() {
		this.session.afterConnected(this.connection);

		StompFrameHandler frameHandler = mock(StompFrameHandler.class);
		String destination = "/topic/foo";
		Subscription subscription = this.session.subscribe(destination, frameHandler);

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
		accessor.setDestination(destination);
		accessor.setSubscriptionId(subscription.getSubscriptionId());
		accessor.setContentType(MimeTypeUtils.TEXT_PLAIN);
		accessor.setMessageId("1");
		accessor.setLeaveMutable(true);
		String payload = "sample payload";

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		when(frameHandler.getPayloadType(stompHeaders)).thenReturn(String.class);

		this.session.handleMessage(MessageBuilder.createMessage(payload.getBytes(StandardCharsets.UTF_8),
				accessor.getMessageHeaders()));

		verify(frameHandler).getPayloadType(stompHeaders);
		verify(frameHandler).handleFrame(stompHeaders, payload);
		verifyNoMoreInteractions(frameHandler);
	}

	@Test
	public void handleMessageFrameWithConversionException() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		StompFrameHandler frameHandler = mock(StompFrameHandler.class);
		String destination = "/topic/foo";
		Subscription subscription = this.session.subscribe(destination, frameHandler);

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.MESSAGE);
		accessor.setDestination(destination);
		accessor.setSubscriptionId(subscription.getSubscriptionId());
		accessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
		accessor.setMessageId("1");
		accessor.setLeaveMutable(true);
		byte[] payload = "{'foo':'bar'}".getBytes(StandardCharsets.UTF_8);

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		when(frameHandler.getPayloadType(stompHeaders)).thenReturn(Map.class);

		this.session.handleMessage(MessageBuilder.createMessage(payload, accessor.getMessageHeaders()));

		verify(frameHandler).getPayloadType(stompHeaders);
		verifyNoMoreInteractions(frameHandler);

		verify(this.sessionHandler).handleException(same(this.session), same(StompCommand.MESSAGE),
				eq(stompHeaders), same(payload), any(MessageConversionException.class));
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void handleFailure() {
		IllegalStateException exception = new IllegalStateException("simulated exception");
		this.session.handleFailure(exception);

		verify(this.sessionHandler).handleTransportError(this.session, exception);
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void afterConnectionClosed() {
		this.session.afterConnectionClosed();

		verify(this.sessionHandler).handleTransportError(same(this.session), any(ConnectionLostException.class));
		verifyNoMoreInteractions(this.sessionHandler);
	}

	@Test
	public void send() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String destination = "/topic/foo";
		String payload = "sample payload";
		this.session.send(destination, payload);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.SEND, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 2, stompHeaders.size());

		assertEquals(destination, stompHeaders.getDestination());
		assertEquals(new MimeType("text", "plain", StandardCharsets.UTF_8), stompHeaders.getContentType());
		assertEquals(-1, stompHeaders.getContentLength());  // StompEncoder isn't involved
		assertEquals(payload, new String(message.getPayload(), StandardCharsets.UTF_8));
	}

	@Test
	public void sendWithReceipt() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		this.session.setTaskScheduler(mock(TaskScheduler.class));
		this.session.setAutoReceipt(true);
		this.session.send("/topic/foo", "sample payload");

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertNotNull(accessor.getReceipt());

		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.setDestination("/topic/foo");
		stompHeaders.setReceipt("my-receipt");
		this.session.send(stompHeaders, "sample payload");

		message = this.messageCaptor.getValue();
		accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals("my-receipt", accessor.getReceipt());
	}

	@Test // gh-23358
	public void sendByteArray() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String destination = "/topic/foo";
		String payload = "sample payload";
		this.session.send(destination, payload.getBytes(StandardCharsets.UTF_8));

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 2, stompHeaders.size());

		assertEquals(destination, stompHeaders.getDestination());
		assertEquals(MimeTypeUtils.APPLICATION_OCTET_STREAM, stompHeaders.getContentType());
		assertEquals(payload, new String(message.getPayload(), StandardCharsets.UTF_8));
	}

	@Test
	public void sendWithConversionException() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.setDestination("/topic/foo");
		stompHeaders.setContentType(MimeTypeUtils.APPLICATION_JSON);
		String payload = "{'foo':'bar'}";

		this.expected.expect(MessageConversionException.class);
		this.session.send(stompHeaders, payload);
		verifyNoMoreInteractions(this.connection);
	}

	@Test
	public void sendWithExecutionException() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		IllegalStateException exception = new IllegalStateException("simulated exception");
		SettableListenableFuture<Void> future = new SettableListenableFuture<>();
		future.setException(exception);

		when(this.connection.send(any())).thenReturn(future);
		this.expected.expect(MessageDeliveryException.class);
		this.expected.expectCause(Matchers.sameInstance(exception));

		this.session.send("/topic/foo", "sample payload".getBytes(StandardCharsets.UTF_8));

		verifyNoMoreInteractions(this.connection);
	}

	@Test
	public void subscribe() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String destination = "/topic/foo";
		StompFrameHandler frameHandler = mock(StompFrameHandler.class);
		Subscription subscription = this.session.subscribe(destination, frameHandler);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.SUBSCRIBE, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 2, stompHeaders.size());
		assertEquals(destination, stompHeaders.getDestination());
		assertEquals(subscription.getSubscriptionId(), stompHeaders.getId());
	}

	@Test
	public void subscribeWithHeaders() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String subscriptionId = "123";
		String destination = "/topic/foo";

		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.setId(subscriptionId);
		stompHeaders.setDestination(destination);
		StompFrameHandler frameHandler = mock(StompFrameHandler.class);

		Subscription subscription = this.session.subscribe(stompHeaders, frameHandler);
		assertEquals(subscriptionId, subscription.getSubscriptionId());

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.SUBSCRIBE, accessor.getCommand());

		stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 2, stompHeaders.size());
		assertEquals(destination, stompHeaders.getDestination());
		assertEquals(subscriptionId, stompHeaders.getId());
	}

	@Test
	public void unsubscribe() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String destination = "/topic/foo";
		StompFrameHandler frameHandler = mock(StompFrameHandler.class);
		Subscription subscription = this.session.subscribe(destination, frameHandler);
		subscription.unsubscribe();

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.UNSUBSCRIBE, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 1, stompHeaders.size());
		assertEquals(subscription.getSubscriptionId(), stompHeaders.getId());
	}

	@Test // SPR-15131
	public void unsubscribeWithCustomHeader() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String headerName = "durable-subscription-name";
		String headerValue = "123";

		StompHeaders subscribeHeaders = new StompHeaders();
		subscribeHeaders.setDestination("/topic/foo");
		subscribeHeaders.set(headerName, headerValue);
		StompFrameHandler frameHandler = mock(StompFrameHandler.class);
		Subscription subscription = this.session.subscribe(subscribeHeaders, frameHandler);

		StompHeaders unsubscribeHeaders = new StompHeaders();
		unsubscribeHeaders.set(headerName,  subscription.getSubscriptionHeaders().getFirst(headerName));
		subscription.unsubscribe(unsubscribeHeaders);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.UNSUBSCRIBE, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 2, stompHeaders.size());
		assertEquals(subscription.getSubscriptionId(), stompHeaders.getId());
		assertEquals(headerValue, stompHeaders.getFirst(headerName));
	}

	@Test
	public void ack() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String messageId = "123";
		this.session.acknowledge(messageId, true);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.ACK, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 1, stompHeaders.size());
		assertEquals(messageId, stompHeaders.getId());
	}

	@Test
	public void nack() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		String messageId = "123";
		this.session.acknowledge(messageId, false);

		Message<byte[]> message = this.messageCaptor.getValue();
		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
		assertEquals(StompCommand.NACK, accessor.getCommand());

		StompHeaders stompHeaders = StompHeaders.readOnlyStompHeaders(accessor.getNativeHeaders());
		assertEquals(stompHeaders.toString(), 1, stompHeaders.size());
		assertEquals(messageId, stompHeaders.getId());
	}

	@Test
	public void receiptReceived() {
		this.session.afterConnected(this.connection);
		this.session.setTaskScheduler(mock(TaskScheduler.class));

		AtomicReference<Boolean> received = new AtomicReference<>();

		StompHeaders headers = new StompHeaders();
		headers.setDestination("/topic/foo");
		headers.setReceipt("my-receipt");
		Subscription subscription = this.session.subscribe(headers, mock(StompFrameHandler.class));
		subscription.addReceiptTask(() -> received.set(true));

		assertNull(received.get());

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.RECEIPT);
		accessor.setReceiptId("my-receipt");
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		assertNotNull(received.get());
		assertTrue(received.get());
	}

	@Test
	public void receiptReceivedBeforeTaskAdded() {
		this.session.afterConnected(this.connection);
		this.session.setTaskScheduler(mock(TaskScheduler.class));

		AtomicReference<Boolean> received = new AtomicReference<>();

		StompHeaders headers = new StompHeaders();
		headers.setDestination("/topic/foo");
		headers.setReceipt("my-receipt");
		Subscription subscription = this.session.subscribe(headers, mock(StompFrameHandler.class));

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.RECEIPT);
		accessor.setReceiptId("my-receipt");
		accessor.setLeaveMutable(true);
		this.session.handleMessage(MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders()));

		subscription.addReceiptTask(() -> received.set(true));

		assertNotNull(received.get());
		assertTrue(received.get());
	}

	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void receiptNotReceived() {
		TaskScheduler taskScheduler = mock(TaskScheduler.class);

		this.session.afterConnected(this.connection);
		this.session.setTaskScheduler(taskScheduler);

		AtomicReference<Boolean> notReceived = new AtomicReference<>();

		ScheduledFuture future = mock(ScheduledFuture.class);
		when(taskScheduler.schedule(any(Runnable.class), any(Date.class))).thenReturn(future);

		StompHeaders headers = new StompHeaders();
		headers.setDestination("/topic/foo");
		headers.setReceipt("my-receipt");
		Receiptable receiptable = this.session.send(headers, "payload");
		receiptable.addReceiptLostTask(() -> notReceived.set(true));

		ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);
		verify(taskScheduler).schedule(taskCaptor.capture(), (Date) notNull());
		Runnable scheduledTask = taskCaptor.getValue();
		assertNotNull(scheduledTask);

		assertNull(notReceived.get());

		scheduledTask.run();
		assertTrue(notReceived.get());
		verify(future).cancel(true);
		verifyNoMoreInteractions(future);
	}

	@Test
	public void disconnect() {
		this.session.afterConnected(this.connection);
		assertTrue(this.session.isConnected());

		this.session.disconnect();
		assertFalse(this.session.isConnected());
		verifyNoMoreInteractions(this.sessionHandler);
	}

}
