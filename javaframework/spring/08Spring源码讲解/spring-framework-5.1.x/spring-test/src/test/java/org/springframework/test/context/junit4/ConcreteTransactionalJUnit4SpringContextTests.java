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

package org.springframework.test.context.junit4;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.tests.sample.beans.Employee;
import org.springframework.tests.sample.beans.Pet;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.springframework.test.transaction.TransactionTestUtils.*;

/**
 * Combined integration test for {@link AbstractJUnit4SpringContextTests} and
 * {@link AbstractTransactionalJUnit4SpringContextTests}.
 *
 * @author Sam Brannen
 * @since 2.5
 */
@ContextConfiguration
public class ConcreteTransactionalJUnit4SpringContextTests extends AbstractTransactionalJUnit4SpringContextTests
		implements BeanNameAware, InitializingBean {

	private static final String JANE = "jane";
	private static final String SUE = "sue";
	private static final String YODA = "yoda";

	private Employee employee;

	@Autowired
	private Pet pet;

	@Autowired(required = false)
	private Long nonrequiredLong;

	@Resource
	private String foo;

	private String bar;

	private String beanName;

	private boolean beanInitialized = false;


	@Autowired
	private void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Resource
	private void setBar(String bar) {
		this.bar = bar;
	}

	@Override
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public void afterPropertiesSet() {
		this.beanInitialized = true;
	}


	@Before
	public void setUp() {
		assertEquals("Verifying the number of rows in the person table before a test method.",
				(inTransaction() ? 2 : 1), countRowsInPersonTable());
	}

	@After
	public void tearDown() {
		assertEquals("Verifying the number of rows in the person table after a test method.",
				(inTransaction() ? 4 : 1), countRowsInPersonTable());
	}

	@BeforeTransaction
	public void beforeTransaction() {
		assertEquals("Verifying the number of rows in the person table before a transactional test method.",
				1, countRowsInPersonTable());
		assertEquals("Adding yoda", 1, addPerson(YODA));
	}

	@AfterTransaction
	public void afterTransaction() {
		assertEquals("Deleting yoda", 1, deletePerson(YODA));
		assertEquals("Verifying the number of rows in the person table after a transactional test method.",
				1, countRowsInPersonTable());
	}


	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyBeanNameSet() {
		assertInTransaction(false);
		assertTrue("The bean name of this test instance should have been set to the fully qualified class name " +
				"due to BeanNameAware semantics.", this.beanName.startsWith(getClass().getName()));
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyApplicationContext() {
		assertInTransaction(false);
		assertNotNull("The application context should have been set due to ApplicationContextAware semantics.",
				super.applicationContext);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyBeanInitialized() {
		assertInTransaction(false);
		assertTrue("This test bean should have been initialized due to InitializingBean semantics.",
				this.beanInitialized);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyAnnotationAutowiredFields() {
		assertInTransaction(false);
		assertNull("The nonrequiredLong property should NOT have been autowired.", this.nonrequiredLong);
		assertNotNull("The pet field should have been autowired.", this.pet);
		assertEquals("Fido", this.pet.getName());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyAnnotationAutowiredMethods() {
		assertInTransaction(false);
		assertNotNull("The employee setter method should have been autowired.", this.employee);
		assertEquals("John Smith", this.employee.getName());
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyResourceAnnotationWiredFields() {
		assertInTransaction(false);
		assertEquals("The foo field should have been wired via @Resource.", "Foo", this.foo);
	}

	@Test
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void verifyResourceAnnotationWiredMethods() {
		assertInTransaction(false);
		assertEquals("The bar method should have been wired via @Resource.", "Bar", this.bar);
	}

	@Test
	public void modifyTestDataWithinTransaction() {
		assertInTransaction(true);
		assertEquals("Adding jane", 1, addPerson(JANE));
		assertEquals("Adding sue", 1, addPerson(SUE));
		assertEquals("Verifying the number of rows in the person table in modifyTestDataWithinTransaction().",
				4, countRowsInPersonTable());
	}


	private int addPerson(String name) {
		return super.jdbcTemplate.update("INSERT INTO person VALUES(?)", name);
	}

	private int deletePerson(String name) {
		return super.jdbcTemplate.update("DELETE FROM person WHERE name=?", name);
	}

	private int countRowsInPersonTable() {
		return countRowsInTable("person");
	}

}
