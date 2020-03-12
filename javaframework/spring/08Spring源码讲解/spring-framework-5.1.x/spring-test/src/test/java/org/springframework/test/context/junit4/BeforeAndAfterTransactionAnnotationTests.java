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

package org.springframework.test.context.junit4;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.transaction.TransactionTestUtils.*;

/**
 * JUnit 4 based integration test which verifies
 * {@link BeforeTransaction @BeforeTransaction} and
 * {@link AfterTransaction @AfterTransaction} behavior.
 *
 * @author Sam Brannen
 * @since 2.5
 */
@Transactional
public class BeforeAndAfterTransactionAnnotationTests extends AbstractTransactionalSpringRunnerTests {

	protected static JdbcTemplate jdbcTemplate;

	protected static int numBeforeTransactionCalls = 0;
	protected static int numAfterTransactionCalls = 0;

	protected boolean inTransaction = false;

	@Rule
	public final TestName testName = new TestName();


	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}


	@BeforeClass
	public static void beforeClass() {
		BeforeAndAfterTransactionAnnotationTests.numBeforeTransactionCalls = 0;
		BeforeAndAfterTransactionAnnotationTests.numAfterTransactionCalls = 0;
	}

	@AfterClass
	public static void afterClass() {
		assertEquals("Verifying the final number of rows in the person table after all tests.", 3,
			countRowsInPersonTable(jdbcTemplate));
		assertEquals("Verifying the total number of calls to beforeTransaction().", 2,
			BeforeAndAfterTransactionAnnotationTests.numBeforeTransactionCalls);
		assertEquals("Verifying the total number of calls to afterTransaction().", 2,
			BeforeAndAfterTransactionAnnotationTests.numAfterTransactionCalls);
	}

	@BeforeTransaction
	void beforeTransaction() {
		assertInTransaction(false);
		this.inTransaction = true;
		BeforeAndAfterTransactionAnnotationTests.numBeforeTransactionCalls++;
		clearPersonTable(jdbcTemplate);
		assertEquals("Adding yoda", 1, addPerson(jdbcTemplate, YODA));
	}

	@AfterTransaction
	void afterTransaction() {
		assertInTransaction(false);
		this.inTransaction = false;
		BeforeAndAfterTransactionAnnotationTests.numAfterTransactionCalls++;
		assertEquals("Deleting yoda", 1, deletePerson(jdbcTemplate, YODA));
		assertEquals("Verifying the number of rows in the person table after a transactional test method.", 0,
			countRowsInPersonTable(jdbcTemplate));
	}

	@Before
	public void before() {
		assertShouldBeInTransaction();
		assertEquals("Verifying the number of rows in the person table before a test method.", (this.inTransaction ? 1
				: 0), countRowsInPersonTable(jdbcTemplate));
	}

	private void assertShouldBeInTransaction() {
		boolean shouldBeInTransaction = !testName.getMethodName().equals("nonTransactionalMethod");
		assertInTransaction(shouldBeInTransaction);
	}

	@After
	public void after() {
		assertShouldBeInTransaction();
	}

	@Test
	public void transactionalMethod1() {
		assertInTransaction(true);
		assertEquals("Adding jane", 1, addPerson(jdbcTemplate, JANE));
		assertEquals("Verifying the number of rows in the person table within transactionalMethod1().", 2,
			countRowsInPersonTable(jdbcTemplate));
	}

	@Test
	public void transactionalMethod2() {
		assertInTransaction(true);
		assertEquals("Adding jane", 1, addPerson(jdbcTemplate, JANE));
		assertEquals("Adding sue", 1, addPerson(jdbcTemplate, SUE));
		assertEquals("Verifying the number of rows in the person table within transactionalMethod2().", 3,
			countRowsInPersonTable(jdbcTemplate));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void nonTransactionalMethod() {
		assertInTransaction(false);
		assertEquals("Adding luke", 1, addPerson(jdbcTemplate, LUKE));
		assertEquals("Adding leia", 1, addPerson(jdbcTemplate, LEIA));
		assertEquals("Adding yoda", 1, addPerson(jdbcTemplate, YODA));
		assertEquals("Verifying the number of rows in the person table without a transaction.", 3,
			countRowsInPersonTable(jdbcTemplate));
	}

}
