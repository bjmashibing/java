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

package org.springframework.mail.javamail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.ObjectUtils;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 * @since 09.10.2004
 */
public class JavaMailSenderTests {

	@Rule
	public final ExpectedException thrown = ExpectedException.none();


	@Test
	public void javaMailSenderWithSimpleMessage() throws MessagingException, IOException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setPort(30);
		sender.setUsername("username");
		sender.setPassword("password");

		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setFrom("me@mail.org");
		simpleMessage.setReplyTo("reply@mail.org");
		simpleMessage.setTo("you@mail.org");
		simpleMessage.setCc("he@mail.org", "she@mail.org");
		simpleMessage.setBcc("us@mail.org", "them@mail.org");
		Date sentDate = new GregorianCalendar(2004, 1, 1).getTime();
		simpleMessage.setSentDate(sentDate);
		simpleMessage.setSubject("my subject");
		simpleMessage.setText("my text");
		sender.send(simpleMessage);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals(30, sender.transport.getConnectedPort());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());

		assertEquals(1, sender.transport.getSentMessages().size());
		MimeMessage sentMessage = sender.transport.getSentMessage(0);
		List<Address> froms = Arrays.asList(sentMessage.getFrom());
		assertEquals(1, froms.size());
		assertEquals("me@mail.org", ((InternetAddress) froms.get(0)).getAddress());
		List<Address> replyTos = Arrays.asList(sentMessage.getReplyTo());
		assertEquals("reply@mail.org", ((InternetAddress) replyTos.get(0)).getAddress());
		List<Address> tos = Arrays.asList(sentMessage.getRecipients(Message.RecipientType.TO));
		assertEquals(1, tos.size());
		assertEquals("you@mail.org", ((InternetAddress) tos.get(0)).getAddress());
		List<Address> ccs = Arrays.asList(sentMessage.getRecipients(Message.RecipientType.CC));
		assertEquals(2, ccs.size());
		assertEquals("he@mail.org", ((InternetAddress) ccs.get(0)).getAddress());
		assertEquals("she@mail.org", ((InternetAddress) ccs.get(1)).getAddress());
		List<Address> bccs = Arrays.asList(sentMessage.getRecipients(Message.RecipientType.BCC));
		assertEquals(2, bccs.size());
		assertEquals("us@mail.org", ((InternetAddress) bccs.get(0)).getAddress());
		assertEquals("them@mail.org", ((InternetAddress) bccs.get(1)).getAddress());
		assertEquals(sentDate.getTime(), sentMessage.getSentDate().getTime());
		assertEquals("my subject", sentMessage.getSubject());
		assertEquals("my text", sentMessage.getContent());
	}

	@Test
	public void javaMailSenderWithSimpleMessages() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		SimpleMailMessage simpleMessage1 = new SimpleMailMessage();
		simpleMessage1.setTo("he@mail.org");
		SimpleMailMessage simpleMessage2 = new SimpleMailMessage();
		simpleMessage2.setTo("she@mail.org");
		sender.send(simpleMessage1, simpleMessage2);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());

		assertEquals(2, sender.transport.getSentMessages().size());
		MimeMessage sentMessage1 = sender.transport.getSentMessage(0);
		List<Address> tos1 = Arrays.asList(sentMessage1.getRecipients(Message.RecipientType.TO));
		assertEquals(1, tos1.size());
		assertEquals("he@mail.org", ((InternetAddress) tos1.get(0)).getAddress());
		MimeMessage sentMessage2 = sender.transport.getSentMessage(1);
		List<Address> tos2 = Arrays.asList(sentMessage2.getRecipients(Message.RecipientType.TO));
		assertEquals(1, tos2.size());
		assertEquals("she@mail.org", ((InternetAddress) tos2.get(0)).getAddress());
	}

	@Test
	public void javaMailSenderWithMimeMessage() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessage mimeMessage = sender.createMimeMessage();
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("you@mail.org"));
		sender.send(mimeMessage);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(mimeMessage, sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailSenderWithMimeMessages() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessage mimeMessage1 = sender.createMimeMessage();
		mimeMessage1.setRecipient(Message.RecipientType.TO, new InternetAddress("he@mail.org"));
		MimeMessage mimeMessage2 = sender.createMimeMessage();
		mimeMessage2.setRecipient(Message.RecipientType.TO, new InternetAddress("she@mail.org"));
		sender.send(mimeMessage1, mimeMessage2);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(2, sender.transport.getSentMessages().size());
		assertEquals(mimeMessage1, sender.transport.getSentMessage(0));
		assertEquals(mimeMessage2, sender.transport.getSentMessage(1));
	}

	@Test
	public void javaMailSenderWithMimeMessagePreparator() {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		final List<Message> messages = new ArrayList<>();

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("you@mail.org"));
				messages.add(mimeMessage);
			}
		};
		sender.send(preparator);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(messages.get(0), sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailSenderWithMimeMessagePreparators() {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		final List<Message> messages = new ArrayList<>();

		MimeMessagePreparator preparator1 = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("he@mail.org"));
				messages.add(mimeMessage);
			}
		};
		MimeMessagePreparator preparator2 = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException {
				mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("she@mail.org"));
				messages.add(mimeMessage);
			}
		};
		sender.send(preparator1, preparator2);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(2, sender.transport.getSentMessages().size());
		assertEquals(messages.get(0), sender.transport.getSentMessage(0));
		assertEquals(messages.get(1), sender.transport.getSentMessage(1));
	}

	@Test
	public void javaMailSenderWithMimeMessageHelper() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessageHelper message = new MimeMessageHelper(sender.createMimeMessage());
		assertNull(message.getEncoding());
		assertTrue(message.getFileTypeMap() instanceof ConfigurableMimeFileTypeMap);

		message.setTo("you@mail.org");
		sender.send(message.getMimeMessage());

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(message.getMimeMessage(), sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailSenderWithMimeMessageHelperAndSpecificEncoding() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessageHelper message = new MimeMessageHelper(sender.createMimeMessage(), "UTF-8");
		assertEquals("UTF-8", message.getEncoding());
		FileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
		message.setFileTypeMap(fileTypeMap);
		assertEquals(fileTypeMap, message.getFileTypeMap());

		message.setTo("you@mail.org");
		sender.send(message.getMimeMessage());

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(message.getMimeMessage(), sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailSenderWithMimeMessageHelperAndDefaultEncoding() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");
		sender.setDefaultEncoding("UTF-8");

		FileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
		sender.setDefaultFileTypeMap(fileTypeMap);
		MimeMessageHelper message = new MimeMessageHelper(sender.createMimeMessage());
		assertEquals("UTF-8", message.getEncoding());
		assertEquals(fileTypeMap, message.getFileTypeMap());

		message.setTo("you@mail.org");
		sender.send(message.getMimeMessage());

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(message.getMimeMessage(), sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailSenderWithParseExceptionOnSimpleMessage() {
		MockJavaMailSender sender = new MockJavaMailSender();
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setFrom("");
		try {
			sender.send(simpleMessage);
		}
		catch (MailParseException ex) {
			// expected
			assertTrue(ex.getCause() instanceof AddressException);
		}
	}

	@Test
	public void javaMailSenderWithParseExceptionOnMimeMessagePreparator() {
		MockJavaMailSender sender = new MockJavaMailSender();
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws MessagingException {
				mimeMessage.setFrom(new InternetAddress(""));
			}
		};
		try {
			sender.send(preparator);
		}
		catch (MailParseException ex) {
			// expected
			assertTrue(ex.getCause() instanceof AddressException);
		}
	}

	@Test
	public void javaMailSenderWithCustomSession() throws MessagingException {
		final Session session = Session.getInstance(new Properties());
		MockJavaMailSender sender = new MockJavaMailSender() {
			@Override
			protected Transport getTransport(Session sess) throws NoSuchProviderException {
				assertEquals(session, sess);
				return super.getTransport(sess);
			}
		};
		sender.setSession(session);
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessage mimeMessage = sender.createMimeMessage();
		mimeMessage.setSubject("custom");
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("you@mail.org"));
		mimeMessage.setSentDate(new GregorianCalendar(2005, 3, 1).getTime());
		sender.send(mimeMessage);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(mimeMessage, sender.transport.getSentMessage(0));
	}

	@Test
	public void javaMailProperties() throws MessagingException {
		Properties props = new Properties();
		props.setProperty("bogusKey", "bogusValue");
		MockJavaMailSender sender = new MockJavaMailSender() {
			@Override
			protected Transport getTransport(Session sess) throws NoSuchProviderException {
				assertEquals("bogusValue", sess.getProperty("bogusKey"));
				return super.getTransport(sess);
			}
		};
		sender.setJavaMailProperties(props);
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessage mimeMessage = sender.createMimeMessage();
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("you@mail.org"));
		sender.send(mimeMessage);

		assertEquals("host", sender.transport.getConnectedHost());
		assertEquals("username", sender.transport.getConnectedUsername());
		assertEquals("password", sender.transport.getConnectedPassword());
		assertTrue(sender.transport.isCloseCalled());
		assertEquals(1, sender.transport.getSentMessages().size());
		assertEquals(mimeMessage, sender.transport.getSentMessage(0));
	}

	@Test
	public void failedMailServerConnect() {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost(null);
		sender.setUsername("username");
		sender.setPassword("password");
		SimpleMailMessage simpleMessage1 = new SimpleMailMessage();
		try {
			sender.send(simpleMessage1);
			fail("Should have thrown MailSendException");
		}
		catch (MailSendException ex) {
			// expected
			ex.printStackTrace();
			assertTrue(ex.getFailedMessages() != null);
			assertEquals(1, ex.getFailedMessages().size());
			assertSame(simpleMessage1, ex.getFailedMessages().keySet().iterator().next());
			assertSame(ex.getCause(), ex.getFailedMessages().values().iterator().next());
		}
	}

	@Test
	public void failedMailServerClose() {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("");
		sender.setUsername("username");
		sender.setPassword("password");
		SimpleMailMessage simpleMessage1 = new SimpleMailMessage();
		try {
			sender.send(simpleMessage1);
			fail("Should have thrown MailSendException");
		}
		catch (MailSendException ex) {
			// expected
			ex.printStackTrace();
			assertTrue(ex.getFailedMessages() != null);
			assertEquals(0, ex.getFailedMessages().size());
		}
	}

	@Test
	public void failedSimpleMessage() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		SimpleMailMessage simpleMessage1 = new SimpleMailMessage();
		simpleMessage1.setTo("he@mail.org");
		simpleMessage1.setSubject("fail");
		SimpleMailMessage simpleMessage2 = new SimpleMailMessage();
		simpleMessage2.setTo("she@mail.org");

		try {
			sender.send(simpleMessage1, simpleMessage2);
		}
		catch (MailSendException ex) {
			ex.printStackTrace();
			assertEquals("host", sender.transport.getConnectedHost());
			assertEquals("username", sender.transport.getConnectedUsername());
			assertEquals("password", sender.transport.getConnectedPassword());
			assertTrue(sender.transport.isCloseCalled());
			assertEquals(1, sender.transport.getSentMessages().size());
			assertEquals(new InternetAddress("she@mail.org"), sender.transport.getSentMessage(0).getAllRecipients()[0]);
			assertEquals(1, ex.getFailedMessages().size());
			assertEquals(simpleMessage1, ex.getFailedMessages().keySet().iterator().next());
			Object subEx = ex.getFailedMessages().values().iterator().next();
			assertTrue(subEx instanceof MessagingException);
			assertEquals("failed", ((MessagingException) subEx).getMessage());
		}
	}

	@Test
	public void failedMimeMessage() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.setUsername("username");
		sender.setPassword("password");

		MimeMessage mimeMessage1 = sender.createMimeMessage();
		mimeMessage1.setRecipient(Message.RecipientType.TO, new InternetAddress("he@mail.org"));
		mimeMessage1.setSubject("fail");
		MimeMessage mimeMessage2 = sender.createMimeMessage();
		mimeMessage2.setRecipient(Message.RecipientType.TO, new InternetAddress("she@mail.org"));

		try {
			sender.send(mimeMessage1, mimeMessage2);
		}
		catch (MailSendException ex) {
			ex.printStackTrace();
			assertEquals("host", sender.transport.getConnectedHost());
			assertEquals("username", sender.transport.getConnectedUsername());
			assertEquals("password", sender.transport.getConnectedPassword());
			assertTrue(sender.transport.isCloseCalled());
			assertEquals(1, sender.transport.getSentMessages().size());
			assertEquals(mimeMessage2, sender.transport.getSentMessage(0));
			assertEquals(1, ex.getFailedMessages().size());
			assertEquals(mimeMessage1, ex.getFailedMessages().keySet().iterator().next());
			Object subEx = ex.getFailedMessages().values().iterator().next();
			assertTrue(subEx instanceof MessagingException);
			assertEquals("failed", ((MessagingException) subEx).getMessage());
		}
	}

	@Test
	public void testConnection() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost("host");
		sender.testConnection();
	}

	@Test
	public void testConnectionWithFailure() throws MessagingException {
		MockJavaMailSender sender = new MockJavaMailSender();
		sender.setHost(null);

		thrown.expect(MessagingException.class);
		sender.testConnection();
	}


	private static class MockJavaMailSender extends JavaMailSenderImpl {

		private MockTransport transport;

		@Override
		protected Transport getTransport(Session session) throws NoSuchProviderException {
			this.transport = new MockTransport(session, null);
			return transport;
		}
	}


	private static class MockTransport extends Transport {

		private String connectedHost = null;
		private int connectedPort = -2;
		private String connectedUsername = null;
		private String connectedPassword = null;
		private boolean closeCalled = false;
		private List<Message> sentMessages = new ArrayList<>();

		private MockTransport(Session session, URLName urlName) {
			super(session, urlName);
		}

		public String getConnectedHost() {
			return connectedHost;
		}

		public int getConnectedPort() {
			return connectedPort;
		}

		public String getConnectedUsername() {
			return connectedUsername;
		}

		public String getConnectedPassword() {
			return connectedPassword;
		}

		public boolean isCloseCalled() {
			return closeCalled;
		}

		public List<Message> getSentMessages() {
			return sentMessages;
		}

		public MimeMessage getSentMessage(int index) {
			return (MimeMessage) this.sentMessages.get(index);
		}

		@Override
		public void connect(String host, int port, String username, String password) throws MessagingException {
			if (host == null) {
				throw new MessagingException("no host");
			}
			this.connectedHost = host;
			this.connectedPort = port;
			this.connectedUsername = username;
			this.connectedPassword = password;
			setConnected(true);
		}

		@Override
		public synchronized void close() throws MessagingException {
			if ("".equals(connectedHost)) {
				throw new MessagingException("close failure");
			}
			this.closeCalled = true;
		}

		@Override
		public void sendMessage(Message message, Address[] addresses) throws MessagingException {
			if ("fail".equals(message.getSubject())) {
				throw new MessagingException("failed");
			}
			if (addresses == null || (message.getAllRecipients() == null ? addresses.length > 0 :
					!ObjectUtils.nullSafeEquals(addresses, message.getAllRecipients()))) {
				throw new MessagingException("addresses not correct");
			}
			if (message.getSentDate() == null) {
				throw new MessagingException("No sentDate specified");
			}
			if (message.getSubject() != null && message.getSubject().contains("custom")) {
				assertEquals(new GregorianCalendar(2005, 3, 1).getTime(), message.getSentDate());
			}
			this.sentMessages.add(message);
		}
	}

}
