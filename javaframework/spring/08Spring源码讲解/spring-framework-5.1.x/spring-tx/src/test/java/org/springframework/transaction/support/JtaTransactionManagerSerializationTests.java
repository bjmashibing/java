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

package org.springframework.transaction.support;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.junit.Test;

import org.springframework.tests.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.SerializationTestUtils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Rod Johnson
 */
public class JtaTransactionManagerSerializationTests {

	@Test
	public void serializable() throws Exception {
		UserTransaction ut1 = mock(UserTransaction.class);
		UserTransaction ut2 = mock(UserTransaction.class);
		TransactionManager tm = mock(TransactionManager.class);

		JtaTransactionManager jtam = new JtaTransactionManager();
		jtam.setUserTransaction(ut1);
		jtam.setTransactionManager(tm);
		jtam.setRollbackOnCommitFailure(true);
		jtam.afterPropertiesSet();

		SimpleNamingContextBuilder jndiEnv = SimpleNamingContextBuilder
				.emptyActivatedContextBuilder();
		jndiEnv.bind(JtaTransactionManager.DEFAULT_USER_TRANSACTION_NAME, ut2);
		JtaTransactionManager serializedJtatm = (JtaTransactionManager) SerializationTestUtils
				.serializeAndDeserialize(jtam);

		// should do client-side lookup
		assertNotNull("Logger must survive serialization",
				serializedJtatm.logger);
		assertTrue("UserTransaction looked up on client", serializedJtatm
				.getUserTransaction() == ut2);
		assertNull("TransactionManager didn't survive", serializedJtatm
				.getTransactionManager());
		assertEquals(true, serializedJtatm.isRollbackOnCommitFailure());
	}

}
