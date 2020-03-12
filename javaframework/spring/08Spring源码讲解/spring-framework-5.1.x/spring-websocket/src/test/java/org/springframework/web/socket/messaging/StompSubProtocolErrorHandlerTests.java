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
package org.springframework.web.socket.messaging;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

/**
 * Unit tests for {@link StompSubProtocolErrorHandler}.
 * @author Rossen Stoyanchev
 */
public class StompSubProtocolErrorHandlerTests {

	private StompSubProtocolErrorHandler handler;


	@Before
	public void setUp() throws Exception {
		this.handler = new StompSubProtocolErrorHandler();
	}


	@Test
	public void handleClientMessageProcessingError() throws Exception {

		Exception ex = new Exception("fake exception");
		Message<byte[]> actual = this.handler.handleClientMessageProcessingError(null, ex);

		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(actual, StompHeaderAccessor.class);
		assertNotNull(accessor);
		assertEquals(StompCommand.ERROR, accessor.getCommand());
		assertEquals(ex.getMessage(), accessor.getMessage());
		assertArrayEquals(new byte[0], actual.getPayload());
	}

	@Test
	public void handleClientMessageProcessingErrorWithReceipt() throws Exception {

		String receiptId = "123";
		StompHeaderAccessor clientHeaderAccessor = StompHeaderAccessor.create(StompCommand.SEND);
		clientHeaderAccessor.setReceipt(receiptId);
		MessageHeaders clientHeaders = clientHeaderAccessor.getMessageHeaders();
		Message<byte[]> clientMessage = MessageBuilder.createMessage(new byte[0], clientHeaders);
		Message<byte[]> actual = this.handler.handleClientMessageProcessingError(clientMessage, new Exception());

		StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(actual, StompHeaderAccessor.class);
		assertNotNull(accessor);
		assertEquals(receiptId, accessor.getReceiptId());
	}


}
