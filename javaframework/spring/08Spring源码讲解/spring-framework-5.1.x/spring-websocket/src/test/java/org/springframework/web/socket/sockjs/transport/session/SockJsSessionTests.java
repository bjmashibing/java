/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.web.socket.sockjs.transport.session;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.junit.Test;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.ExceptionWebSocketHandlerDecorator;
import org.springframework.web.socket.sockjs.SockJsMessageDeliveryException;
import org.springframework.web.socket.sockjs.SockJsTransportFailureException;
import org.springframework.web.socket.sockjs.frame.SockJsFrame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.verifyNoMoreInteractions;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

/**
 * Test fixture for {@link AbstractSockJsSession}.
 *
 * @author Rossen Stoyanchev
 */
public class SockJsSessionTests extends AbstractSockJsSessionTests<TestSockJsSession> {

	@Override
	protected TestSockJsSession initSockJsSession() {
		return new TestSockJsSession("1", this.sockJsConfig, this.webSocketHandler, Collections.<String, Object>emptyMap());
	}


	@Test
	public void getTimeSinceLastActive() throws Exception {
		Thread.sleep(1);

		long time1 = this.session.getTimeSinceLastActive();
		assertTrue(time1 > 0);

		Thread.sleep(1);

		long time2 = this.session.getTimeSinceLastActive();
		assertTrue(time2 > time1);

		this.session.delegateConnectionEstablished();

		Thread.sleep(1);

		this.session.setActive(false);
		assertTrue(this.session.getTimeSinceLastActive() > 0);

		this.session.setActive(true);
		assertEquals(0, this.session.getTimeSinceLastActive());
	}

	@Test
	public void delegateConnectionEstablished() throws Exception {
		assertNew();
		this.session.delegateConnectionEstablished();
		assertOpen();
		verify(this.webSocketHandler).afterConnectionEstablished(this.session);
	}

	@Test
	public void delegateError() throws Exception {
		Exception ex = new Exception();
		this.session.delegateError(ex);
		verify(this.webSocketHandler).handleTransportError(this.session, ex);
	}

	@Test
	public void delegateMessages() throws Exception {
		String msg1 = "message 1";
		String msg2 = "message 2";
		this.session.delegateMessages(msg1, msg2);

		verify(this.webSocketHandler).handleMessage(this.session, new TextMessage(msg1));
		verify(this.webSocketHandler).handleMessage(this.session, new TextMessage(msg2));
		verifyNoMoreInteractions(this.webSocketHandler);
	}

	@Test
	public void delegateMessagesWithErrorAndConnectionClosing() throws Exception {
		WebSocketHandler wsHandler = new ExceptionWebSocketHandlerDecorator(this.webSocketHandler);
		TestSockJsSession sockJsSession = new TestSockJsSession(
				"1", this.sockJsConfig, wsHandler, Collections.<String, Object>emptyMap());

		String msg1 = "message 1";
		String msg2 = "message 2";
		String msg3 = "message 3";

		willThrow(new IOException()).given(this.webSocketHandler).handleMessage(sockJsSession, new TextMessage(msg2));

		sockJsSession.delegateConnectionEstablished();
		try {
			sockJsSession.delegateMessages(msg1, msg2, msg3);
			fail("expected exception");
		}
		catch (SockJsMessageDeliveryException ex) {
			assertEquals(Collections.singletonList(msg3), ex.getUndeliveredMessages());
			verify(this.webSocketHandler).afterConnectionEstablished(sockJsSession);
			verify(this.webSocketHandler).handleMessage(sockJsSession, new TextMessage(msg1));
			verify(this.webSocketHandler).handleMessage(sockJsSession, new TextMessage(msg2));
			verify(this.webSocketHandler).afterConnectionClosed(sockJsSession, CloseStatus.SERVER_ERROR);
			verifyNoMoreInteractions(this.webSocketHandler);
		}
	}

	@Test
	public void delegateConnectionClosed() throws Exception {
		this.session.delegateConnectionEstablished();
		this.session.delegateConnectionClosed(CloseStatus.GOING_AWAY);

		assertClosed();
		assertEquals(1, this.session.getNumberOfLastActiveTimeUpdates());
		verify(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.GOING_AWAY);
	}

	@Test
	public void closeWhenNotOpen() throws Exception {
		assertNew();

		this.session.close();
		assertNull("Close not ignored for a new session", this.session.getCloseStatus());

		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.close();
		assertClosed();
		assertEquals(3000, this.session.getCloseStatus().getCode());

		this.session.close(CloseStatus.SERVER_ERROR);
		assertEquals("Close should be ignored if already closed", 3000, this.session.getCloseStatus().getCode());
	}

	@Test
	public void closeWhenNotActive() throws Exception {
		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.setActive(false);
		this.session.close();

		assertEquals(Collections.emptyList(), this.session.getSockJsFramesWritten());
	}

	@Test
	public void close() throws Exception {
		this.session.delegateConnectionEstablished();
		assertOpen();

		this.session.setActive(true);
		this.session.close();

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.closeFrameGoAway(), this.session.getSockJsFramesWritten().get(0));

		assertEquals(1, this.session.getNumberOfLastActiveTimeUpdates());
		assertTrue(this.session.didCancelHeartbeat());

		assertEquals(new CloseStatus(3000, "Go away!"), this.session.getCloseStatus());
		assertClosed();
		verify(this.webSocketHandler).afterConnectionClosed(this.session, new CloseStatus(3000, "Go away!"));
	}

	@Test
	public void closeWithWriteFrameExceptions() throws Exception {
		this.session.setExceptionOnWrite(new IOException());

		this.session.delegateConnectionEstablished();
		this.session.setActive(true);
		this.session.close();

		assertEquals(new CloseStatus(3000, "Go away!"), this.session.getCloseStatus());
		assertClosed();
	}

	@Test
	public void closeWithWebSocketHandlerExceptions() throws Exception {
		willThrow(new Exception()).given(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.NORMAL);

		this.session.delegateConnectionEstablished();
		this.session.setActive(true);
		this.session.close(CloseStatus.NORMAL);

		assertEquals(CloseStatus.NORMAL, this.session.getCloseStatus());
		assertClosed();
	}

	@Test
	public void tryCloseWithWebSocketHandlerExceptions() throws Exception {
		this.session.delegateConnectionEstablished();
		this.session.setActive(true);
		this.session.tryCloseWithSockJsTransportError(new Exception(), CloseStatus.BAD_DATA);

		assertEquals(CloseStatus.BAD_DATA, this.session.getCloseStatus());
		assertClosed();
	}

	@Test
	public void writeFrame() throws Exception {
		this.session.writeFrame(SockJsFrame.openFrame());

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.openFrame(), this.session.getSockJsFramesWritten().get(0));
	}

	@Test
	public void writeFrameIoException() throws Exception {
		this.session.setExceptionOnWrite(new IOException());
		this.session.delegateConnectionEstablished();

		try {
			this.session.writeFrame(SockJsFrame.openFrame());
			fail("expected exception");
		}
		catch (SockJsTransportFailureException ex) {
			assertEquals(CloseStatus.SERVER_ERROR, this.session.getCloseStatus());
			verify(this.webSocketHandler).afterConnectionClosed(this.session, CloseStatus.SERVER_ERROR);
		}
	}

	@Test
	public void sendHeartbeat() throws Exception {
		this.session.setActive(true);
		this.session.sendHeartbeat();

		assertEquals(1, this.session.getSockJsFramesWritten().size());
		assertEquals(SockJsFrame.heartbeatFrame(), this.session.getSockJsFramesWritten().get(0));

		verify(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));
		verifyNoMoreInteractions(this.taskScheduler);
	}

	@Test
	public void scheduleHeartbeatNotActive() throws Exception {
		this.session.setActive(false);
		this.session.scheduleHeartbeat();

		verifyNoMoreInteractions(this.taskScheduler);
	}

	@Test
	public void sendHeartbeatWhenDisabled() throws Exception {
		this.session.disableHeartbeat();
		this.session.setActive(true);
		this.session.sendHeartbeat();

		assertEquals(Collections.emptyList(), this.session.getSockJsFramesWritten());
	}

	@Test
	public void scheduleAndCancelHeartbeat() throws Exception {
		ScheduledFuture<?> task = mock(ScheduledFuture.class);
		willReturn(task).given(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));

		this.session.setActive(true);
		this.session.scheduleHeartbeat();

		verify(this.taskScheduler).schedule(any(Runnable.class), any(Date.class));
		verifyNoMoreInteractions(this.taskScheduler);

		given(task.isCancelled()).willReturn(false);
		given(task.cancel(false)).willReturn(true);

		this.session.cancelHeartbeat();

		verify(task).cancel(false);
		verifyNoMoreInteractions(task);
	}

}
