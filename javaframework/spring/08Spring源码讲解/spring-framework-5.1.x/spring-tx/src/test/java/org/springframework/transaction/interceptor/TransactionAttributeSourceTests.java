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

package org.springframework.transaction.interceptor;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import org.springframework.transaction.TransactionDefinition;

import static org.junit.Assert.*;

/**
 * Unit tests for the various {@link TransactionAttributeSource} implementations.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author Chris Beams
 * @since 15.10.2003
 * @see org.springframework.transaction.interceptor.TransactionProxyFactoryBean
 */
public class TransactionAttributeSourceTests {

	@Test
	public void matchAlwaysTransactionAttributeSource() throws Exception {
		MatchAlwaysTransactionAttributeSource tas = new MatchAlwaysTransactionAttributeSource();
		TransactionAttribute ta = tas.getTransactionAttribute(Object.class.getMethod("hashCode"), null);
		assertNotNull(ta);
		assertTrue(TransactionDefinition.PROPAGATION_REQUIRED == ta.getPropagationBehavior());

		tas.setTransactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_SUPPORTS));
		ta = tas.getTransactionAttribute(IOException.class.getMethod("getMessage"), IOException.class);
		assertNotNull(ta);
		assertTrue(TransactionDefinition.PROPAGATION_SUPPORTS == ta.getPropagationBehavior());
	}

	@Test
	public void nameMatchTransactionAttributeSourceWithStarAtStartOfMethodName() throws Exception {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("*ashCode", "PROPAGATION_REQUIRED");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(Object.class.getMethod("hashCode"), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
	}

	@Test
	public void nameMatchTransactionAttributeSourceWithStarAtEndOfMethodName() throws Exception {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("hashCod*", "PROPAGATION_REQUIRED");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(Object.class.getMethod("hashCode"), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_REQUIRED, ta.getPropagationBehavior());
	}

	@Test
	public void nameMatchTransactionAttributeSourceMostSpecificMethodNameIsDefinitelyMatched() throws Exception {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("*", "PROPAGATION_REQUIRED");
		attributes.put("hashCode", "PROPAGATION_MANDATORY");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(Object.class.getMethod("hashCode"), null);
		assertNotNull(ta);
		assertEquals(TransactionDefinition.PROPAGATION_MANDATORY, ta.getPropagationBehavior());
	}

	@Test
	public void nameMatchTransactionAttributeSourceWithEmptyMethodName() throws Exception {
		NameMatchTransactionAttributeSource tas = new NameMatchTransactionAttributeSource();
		Properties attributes = new Properties();
		attributes.put("", "PROPAGATION_MANDATORY");
		tas.setProperties(attributes);
		TransactionAttribute ta = tas.getTransactionAttribute(Object.class.getMethod("hashCode"), null);
		assertNull(ta);
	}

}
