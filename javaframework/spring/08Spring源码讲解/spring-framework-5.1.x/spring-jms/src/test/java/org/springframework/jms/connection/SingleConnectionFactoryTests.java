/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.jms.connection;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Juergen Hoeller
 * @since 26.07.2004
 */
public class SingleConnectionFactoryTests {

	@Test
	public void testWithConnection() throws JMSException {
		Connection con = mock(Connection.class);

		SingleConnectionFactory scf = new SingleConnectionFactory(con);
		Connection con1 = scf.createConnection();
		con1.start();
		con1.stop();
		con1.close();
		Connection con2 = scf.createConnection();
		con2.start();
		con2.stop();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con, times(2)).start();
		verify(con, times(2)).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithQueueConnection() throws JMSException {
		Connection con = mock(QueueConnection.class);

		SingleConnectionFactory scf = new SingleConnectionFactory(con);
		QueueConnection con1 = scf.createQueueConnection();
		con1.start();
		con1.stop();
		con1.close();
		QueueConnection con2 = scf.createQueueConnection();
		con2.start();
		con2.stop();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con, times(2)).start();
		verify(con, times(2)).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithTopicConnection() throws JMSException {
		Connection con = mock(TopicConnection.class);

		SingleConnectionFactory scf = new SingleConnectionFactory(con);
		TopicConnection con1 = scf.createTopicConnection();
		con1.start();
		con1.stop();
		con1.close();
		TopicConnection con2 = scf.createTopicConnection();
		con2.start();
		con2.stop();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con, times(2)).start();
		verify(con, times(2)).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithConnectionFactory() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		Connection con = mock(Connection.class);

		given(cf.createConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		Connection con1 = scf.createConnection();
		Connection con2 = scf.createConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithQueueConnectionFactoryAndJms11Usage() throws JMSException {
		QueueConnectionFactory cf = mock(QueueConnectionFactory.class);
		QueueConnection con = mock(QueueConnection.class);

		given(cf.createConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		Connection con1 = scf.createConnection();
		Connection con2 = scf.createConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithQueueConnectionFactoryAndJms102Usage() throws JMSException {
		QueueConnectionFactory cf = mock(QueueConnectionFactory.class);
		QueueConnection con = mock(QueueConnection.class);

		given(cf.createQueueConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		Connection con1 = scf.createQueueConnection();
		Connection con2 = scf.createQueueConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithTopicConnectionFactoryAndJms11Usage() throws JMSException {
		TopicConnectionFactory cf = mock(TopicConnectionFactory.class);
		TopicConnection con = mock(TopicConnection.class);

		given(cf.createConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		Connection con1 = scf.createConnection();
		Connection con2 = scf.createConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithTopicConnectionFactoryAndJms102Usage() throws JMSException {
		TopicConnectionFactory cf = mock(TopicConnectionFactory.class);
		TopicConnection con = mock(TopicConnection.class);

		given(cf.createTopicConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		Connection con1 = scf.createTopicConnection();
		Connection con2 = scf.createTopicConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithConnectionAggregatedStartStop() throws JMSException {
		Connection con = mock(Connection.class);

		SingleConnectionFactory scf = new SingleConnectionFactory(con);
		Connection con1 = scf.createConnection();
		con1.start();
		verify(con).start();
		con1.stop();
		verify(con).stop();
		Connection con2 = scf.createConnection();
		con2.start();
		verify(con, times(2)).start();
		con2.stop();
		verify(con, times(2)).stop();
		con2.start();
		verify(con, times(3)).start();
		con1.start();
		con2.stop();
		con1.stop();
		verify(con, times(3)).stop();
		con1.start();
		verify(con, times(4)).start();
		con1.close();
		verify(con, times(4)).stop();
		con2.close();
		scf.destroy();
		verify(con).close();

		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithConnectionFactoryAndClientId() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		Connection con = mock(Connection.class);
		given(cf.createConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		scf.setClientId("myId");
		Connection con1 = scf.createConnection();
		Connection con2 = scf.createConnection();
		con1.start();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).setClientID("myId");
		verify(con).start();
		verify(con).stop();
		verify(con).close();
		verifyNoMoreInteractions(con);
	}

	@Test
	public void testWithConnectionFactoryAndExceptionListener() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		Connection con = mock(Connection.class);

		ExceptionListener listener = new ChainedExceptionListener();
		given(cf.createConnection()).willReturn(con);
		given(con.getExceptionListener()).willReturn(listener);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		scf.setExceptionListener(listener);
		Connection con1 = scf.createConnection();
		assertEquals(listener, con1.getExceptionListener());
		con1.start();
		con1.stop();
		con1.close();
		Connection con2 = scf.createConnection();
		con2.start();
		con2.stop();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(con).setExceptionListener(listener);
		verify(con, times(2)).start();
		verify(con, times(2)).stop();
		verify(con).close();
	}

	@Test
	public void testWithConnectionFactoryAndReconnectOnException() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		TestConnection con = new TestConnection();
		given(cf.createConnection()).willReturn(con);

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		scf.setReconnectOnException(true);
		Connection con1 = scf.createConnection();
		assertNull(con1.getExceptionListener());
		con1.start();
		con.getExceptionListener().onException(new JMSException(""));
		Connection con2 = scf.createConnection();
		con2.start();
		scf.destroy();  // should trigger actual close

		assertEquals(2, con.getStartCount());
		assertEquals(2, con.getCloseCount());
	}

	@Test
	public void testWithConnectionFactoryAndExceptionListenerAndReconnectOnException() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		TestConnection con = new TestConnection();
		given(cf.createConnection()).willReturn(con);

		TestExceptionListener listener = new TestExceptionListener();
		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		scf.setExceptionListener(listener);
		scf.setReconnectOnException(true);
		Connection con1 = scf.createConnection();
		assertSame(listener, con1.getExceptionListener());
		con1.start();
		con.getExceptionListener().onException(new JMSException(""));
		Connection con2 = scf.createConnection();
		con2.start();
		scf.destroy();  // should trigger actual close

		assertEquals(2, con.getStartCount());
		assertEquals(2, con.getCloseCount());
		assertEquals(1, listener.getCount());
	}

	@Test
	public void testWithConnectionFactoryAndLocalExceptionListenerWithCleanup() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		TestConnection con = new TestConnection();
		given(cf.createConnection()).willReturn(con);

		TestExceptionListener listener0 = new TestExceptionListener();
		TestExceptionListener listener1 = new TestExceptionListener();
		TestExceptionListener listener2 = new TestExceptionListener();

		SingleConnectionFactory scf = new SingleConnectionFactory(cf) {
			@Override
			public void onException(JMSException ex) {
				// no-op
			}
		};
		scf.setReconnectOnException(true);
		scf.setExceptionListener(listener0);
		Connection con1 = scf.createConnection();
		con1.setExceptionListener(listener1);
		assertSame(listener1, con1.getExceptionListener());
		Connection con2 = scf.createConnection();
		con2.setExceptionListener(listener2);
		assertSame(listener2, con2.getExceptionListener());
		con.getExceptionListener().onException(new JMSException(""));
		con2.close();
		con.getExceptionListener().onException(new JMSException(""));
		con1.close();
		con.getExceptionListener().onException(new JMSException(""));
		scf.destroy();  // should trigger actual close

		assertEquals(0, con.getStartCount());
		assertEquals(1, con.getCloseCount());
		assertEquals(3, listener0.getCount());
		assertEquals(2, listener1.getCount());
		assertEquals(1, listener2.getCount());
	}

	@Test
	public void testWithConnectionFactoryAndLocalExceptionListenerWithReconnect() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		TestConnection con = new TestConnection();
		given(cf.createConnection()).willReturn(con);

		TestExceptionListener listener0 = new TestExceptionListener();
		TestExceptionListener listener1 = new TestExceptionListener();
		TestExceptionListener listener2 = new TestExceptionListener();

		SingleConnectionFactory scf = new SingleConnectionFactory(cf);
		scf.setReconnectOnException(true);
		scf.setExceptionListener(listener0);
		Connection con1 = scf.createConnection();
		con1.setExceptionListener(listener1);
		assertSame(listener1, con1.getExceptionListener());
		con1.start();
		Connection con2 = scf.createConnection();
		con2.setExceptionListener(listener2);
		assertSame(listener2, con2.getExceptionListener());
		con.getExceptionListener().onException(new JMSException(""));
		con2.close();
		con1.getMetaData();
		con.getExceptionListener().onException(new JMSException(""));
		con1.close();
		scf.destroy();  // should trigger actual close

		assertEquals(2, con.getStartCount());
		assertEquals(2, con.getCloseCount());
		assertEquals(2, listener0.getCount());
		assertEquals(2, listener1.getCount());
		assertEquals(1, listener2.getCount());
	}

	@Test
	public void testCachingConnectionFactory() throws JMSException {
		ConnectionFactory cf = mock(ConnectionFactory.class);
		Connection con = mock(Connection.class);
		Session txSession = mock(Session.class);
		Session nonTxSession = mock(Session.class);

		given(cf.createConnection()).willReturn(con);
		given(con.createSession(true, Session.AUTO_ACKNOWLEDGE)).willReturn(txSession);
		given(txSession.getTransacted()).willReturn(true);
		given(con.createSession(false, Session.CLIENT_ACKNOWLEDGE)).willReturn(nonTxSession);

		CachingConnectionFactory scf = new CachingConnectionFactory(cf);
		scf.setReconnectOnException(false);
		Connection con1 = scf.createConnection();
		Session session1 = con1.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session1.getTransacted();
		session1.close();  // should lead to rollback
		session1 = con1.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		session1.close();
		con1.start();
		Connection con2 = scf.createConnection();
		Session session2 = con2.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		session2.close();
		session2 = con2.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session2.commit();
		session2.close();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(txSession).commit();
		verify(txSession).close();
		verify(nonTxSession).close();
		verify(con).start();
		verify(con).stop();
		verify(con).close();
	}

	@Test
	public void testCachingConnectionFactoryWithQueueConnectionFactoryAndJms102Usage() throws JMSException {
		QueueConnectionFactory cf = mock(QueueConnectionFactory.class);
		QueueConnection con = mock(QueueConnection.class);
		QueueSession txSession = mock(QueueSession.class);
		QueueSession nonTxSession = mock(QueueSession.class);

		given(cf.createQueueConnection()).willReturn(con);
		given(con.createQueueSession(true, Session.AUTO_ACKNOWLEDGE)).willReturn(txSession);
		given(txSession.getTransacted()).willReturn(true);
		given(con.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE)).willReturn(nonTxSession);

		CachingConnectionFactory scf = new CachingConnectionFactory(cf);
		scf.setReconnectOnException(false);
		Connection con1 = scf.createQueueConnection();
		Session session1 = con1.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session1.rollback();
		session1.close();
		session1 = con1.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		session1.close();
		con1.start();
		QueueConnection con2 = scf.createQueueConnection();
		Session session2 = con2.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
		session2.close();
		session2 = con2.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session2.getTransacted();
		session2.close();  // should lead to rollback
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(txSession).rollback();
		verify(txSession).close();
		verify(nonTxSession).close();
		verify(con).start();
		verify(con).stop();
		verify(con).close();
	}

	@Test
	public void testCachingConnectionFactoryWithTopicConnectionFactoryAndJms102Usage() throws JMSException {
		TopicConnectionFactory cf = mock(TopicConnectionFactory.class);
		TopicConnection con = mock(TopicConnection.class);
		TopicSession txSession = mock(TopicSession.class);
		TopicSession nonTxSession = mock(TopicSession.class);

		given(cf.createTopicConnection()).willReturn(con);
		given(con.createTopicSession(true, Session.AUTO_ACKNOWLEDGE)).willReturn(txSession);
		given(txSession.getTransacted()).willReturn(true);
		given(con.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE)).willReturn(nonTxSession);

		CachingConnectionFactory scf = new CachingConnectionFactory(cf);
		scf.setReconnectOnException(false);
		Connection con1 = scf.createTopicConnection();
		Session session1 = con1.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session1.getTransacted();
		session1.close();  // should lead to rollback
		session1 = con1.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		session1.close();
		con1.start();
		TopicConnection con2 = scf.createTopicConnection();
		Session session2 = con2.createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
		session2.close();
		session2 = con2.createSession(true, Session.AUTO_ACKNOWLEDGE);
		session2.getTransacted();
		session2.close();
		con2.start();
		con1.close();
		con2.close();
		scf.destroy();  // should trigger actual close

		verify(txSession).close();
		verify(nonTxSession).close();
		verify(con).start();
		verify(con).stop();
		verify(con).close();
	}

}
