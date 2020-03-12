/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.transaction.annotation;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import javax.ejb.TransactionAttributeType;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import org.junit.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.tests.transaction.CallCountingTransactionManager;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.util.SerializationTestUtils;

import static org.junit.Assert.*;

/**
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public class AnnotationTransactionAttributeSourceTests {

	@Test
	public void serializable() throws Exception {
		TestBean1 tb = new TestBean1();
		CallCountingTransactionManager ptm = new CallCountingTransactionManager();
		AnnotationTransactionAttributeSource tas = new AnnotationTransactionAttributeSource();
		TransactionInterceptor ti = new TransactionInterceptor(ptm, tas);

		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setInterfaces(ITestBean1.class);
		proxyFactory.addAdvice(ti);
		proxyFactory.setTarget(tb);
		ITestBean1 proxy = (ITestBean1) proxyFactory.getProxy();
		proxy.getAge();
		assertEquals(1, ptm.commits);

		ITestBean1 serializedProxy = (ITestBean1) SerializationTestUtils.serializeAndDeserialize(proxy);
		serializedProxy.getAge();
		Advised advised = (Advised) serializedProxy;
		TransactionInterceptor serializedTi = (TransactionInterceptor) advised.getAdvisors()[0].getAdvice();
		CallCountingTransactionManager serializedPtm =
				(CallCountingTransactionManager) serializedTi.getTransactionManager();
		assertEquals(2, serializedPtm.commits);
	}

	@Test
	public void nullOrEmpty() throws Exception {
		Method method = Empty.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		assertNull(atas.getTransactionAttribute(method, null));

		// Try again in case of caching
		assertNull(atas.getTransactionAttribute(method, null));
	}

	/**
	 * Test the important case where the invocation is on a proxied interface method
	 * but the attribute is defined on the target class.
	 */
	@Test
	public void transactionAttributeDeclaredOnClassMethod() throws Exception {
		Method classMethod = ITestBean1.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(classMethod, TestBean1.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	/**
	 * Test the important case where the invocation is on a proxied interface method
	 * but the attribute is defined on the target class.
	 */
	@Test
	public void transactionAttributeDeclaredOnCglibClassMethod() throws Exception {
		Method classMethod = ITestBean1.class.getMethod("getAge");
		TestBean1 tb = new TestBean1();
		ProxyFactory pf = new ProxyFactory(tb);
		pf.setProxyTargetClass(true);
		Object proxy = pf.getProxy();

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(classMethod, proxy.getClass());

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	/**
	 * Test case where attribute is on the interface method.
	 */
	@Test
	public void transactionAttributeDeclaredOnInterfaceMethodOnly() throws Exception {
		Method interfaceMethod = ITestBean2.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(interfaceMethod, TestBean2.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	/**
	 * Test that when an attribute exists on both class and interface, class takes precedence.
	 */
	@Test
	public void transactionAttributeOnTargetClassMethodOverridesAttributeOnInterfaceMethod() throws Exception {
		Method interfaceMethod = ITestBean3.class.getMethod("getAge");
		Method interfaceMethod2 = ITestBean3.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(interfaceMethod, TestBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRES_NEW, actual.getPropagationBehavior());
		assertEquals(TransactionAttribute.ISOLATION_REPEATABLE_READ, actual.getIsolationLevel());
		assertEquals(5, actual.getTimeout());
		assertTrue(actual.isReadOnly());

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());

		TransactionAttribute actual2 = atas.getTransactionAttribute(interfaceMethod2, TestBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, actual2.getPropagationBehavior());
	}

	@Test
	public void rollbackRulesAreApplied() throws Exception {
		Method method = TestBean3.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean3.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute("java.lang.Exception"));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));

		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
		assertTrue(actual.rollbackOn(new Exception()));
		assertFalse(actual.rollbackOn(new IOException()));

		actual = atas.getTransactionAttribute(method, method.getDeclaringClass());

		rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute("java.lang.Exception"));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));

		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
		assertTrue(actual.rollbackOn(new Exception()));
		assertFalse(actual.rollbackOn(new IOException()));
	}

	/**
	 * Test that transaction attribute is inherited from class
	 * if not specified on method.
	 */
	@Test
	public void defaultsToClassTransactionAttribute() throws Exception {
		Method method = TestBean4.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean4.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	@Test
	public void customClassAttributeDetected() throws Exception {
		Method method = TestBean5.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean5.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	@Test
	public void customMethodAttributeDetected() throws Exception {
		Method method = TestBean6.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean6.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());
	}

	@Test
	public void customClassAttributeWithReadOnlyOverrideDetected() throws Exception {
		Method method = TestBean7.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean7.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());

		assertTrue(actual.isReadOnly());
	}

	@Test
	public void customMethodAttributeWithReadOnlyOverrideDetected() throws Exception {
		Method method = TestBean8.class.getMethod("getAge");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean8.class);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());

		assertTrue(actual.isReadOnly());
	}

	@Test
	public void customClassAttributeWithReadOnlyOverrideOnInterface() throws Exception {
		Method method = TestInterface9.class.getMethod("getAge");

		Transactional annotation = AnnotationUtils.findAnnotation(method, Transactional.class);
		assertNull("AnnotationUtils.findAnnotation should not find @Transactional for TestBean9.getAge()", annotation);
		annotation = AnnotationUtils.findAnnotation(TestBean9.class, Transactional.class);
		assertNotNull("AnnotationUtils.findAnnotation failed to find @Transactional for TestBean9", annotation);

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean9.class);
		assertNotNull("Failed to retrieve TransactionAttribute for TestBean9.getAge()", actual);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());

		assertTrue(actual.isReadOnly());
	}

	@Test
	public void customMethodAttributeWithReadOnlyOverrideOnInterface() throws Exception {
		Method method = TestInterface10.class.getMethod("getAge");

		Transactional annotation = AnnotationUtils.findAnnotation(method, Transactional.class);
		assertNotNull("AnnotationUtils.findAnnotation failed to find @Transactional for TestBean10.getAge()",
				annotation);
		annotation = AnnotationUtils.findAnnotation(TestBean10.class, Transactional.class);
		assertNull("AnnotationUtils.findAnnotation should not find @Transactional for TestBean10", annotation);

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute actual = atas.getTransactionAttribute(method, TestBean10.class);
		assertNotNull("Failed to retrieve TransactionAttribute for TestBean10.getAge()", actual);

		RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
		rbta.getRollbackRules().add(new RollbackRuleAttribute(Exception.class));
		rbta.getRollbackRules().add(new NoRollbackRuleAttribute(IOException.class));
		assertEquals(rbta.getRollbackRules(), ((RuleBasedTransactionAttribute) actual).getRollbackRules());

		assertTrue(actual.isReadOnly());
	}

	@Test
	public void transactionAttributeDeclaredOnClassMethodWithEjb3() throws Exception {
		Method getAgeMethod = ITestBean1.class.getMethod("getAge");
		Method getNameMethod = ITestBean1.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, Ejb3AnnotatedBean1.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, Ejb3AnnotatedBean1.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnClassWithEjb3() throws Exception {
		Method getAgeMethod = ITestBean1.class.getMethod("getAge");
		Method getNameMethod = ITestBean1.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, Ejb3AnnotatedBean2.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, Ejb3AnnotatedBean2.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnInterfaceWithEjb3() throws Exception {
		Method getAgeMethod = ITestEjb.class.getMethod("getAge");
		Method getNameMethod = ITestEjb.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, Ejb3AnnotatedBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, Ejb3AnnotatedBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnClassMethodWithJta() throws Exception {
		Method getAgeMethod = ITestBean1.class.getMethod("getAge");
		Method getNameMethod = ITestBean1.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, JtaAnnotatedBean1.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, JtaAnnotatedBean1.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnClassWithJta() throws Exception {
		Method getAgeMethod = ITestBean1.class.getMethod("getAge");
		Method getNameMethod = ITestBean1.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, JtaAnnotatedBean2.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, JtaAnnotatedBean2.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnInterfaceWithJta() throws Exception {
		Method getAgeMethod = ITestEjb.class.getMethod("getAge");
		Method getNameMethod = ITestEjb.class.getMethod("getName");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, JtaAnnotatedBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, JtaAnnotatedBean3.class);
		assertEquals(TransactionAttribute.PROPAGATION_SUPPORTS, getNameAttr.getPropagationBehavior());
	}

	@Test
	public void transactionAttributeDeclaredOnGroovyClass() throws Exception {
		Method getAgeMethod = ITestBean1.class.getMethod("getAge");
		Method getNameMethod = ITestBean1.class.getMethod("getName");
		Method getMetaClassMethod = GroovyObject.class.getMethod("getMetaClass");

		AnnotationTransactionAttributeSource atas = new AnnotationTransactionAttributeSource();
		TransactionAttribute getAgeAttr = atas.getTransactionAttribute(getAgeMethod, GroovyTestBean.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getAgeAttr.getPropagationBehavior());
		TransactionAttribute getNameAttr = atas.getTransactionAttribute(getNameMethod, GroovyTestBean.class);
		assertEquals(TransactionAttribute.PROPAGATION_REQUIRED, getNameAttr.getPropagationBehavior());
		assertNull(atas.getTransactionAttribute(getMetaClassMethod, GroovyTestBean.class));
	}


	interface ITestBean1 {

		int getAge();

		void setAge(int age);

		String getName();

		void setName(String name);
	}


	interface ITestBean2 {

		@Transactional
		int getAge();

		void setAge(int age);
	}


	interface ITestBean2X extends ITestBean2 {

		String getName();

		void setName(String name);
	}


	@Transactional
	interface ITestBean3 {

		int getAge();

		void setAge(int age);

		String getName();

		void setName(String name);
	}


	static class Empty implements ITestBean1 {

		private String name;

		private int age;

		public Empty() {
		}

		public Empty(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@SuppressWarnings("serial")
	static class TestBean1 implements ITestBean1, Serializable {

		private String name;

		private int age;

		public TestBean1() {
		}

		public TestBean1(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@Transactional(rollbackFor = Exception.class)
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	static class TestBean2 implements ITestBean2X {

		private String name;

		private int age;

		public TestBean2() {
		}

		public TestBean2(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	static class TestBean3 implements ITestBean3 {

		private String name;

		private int age;

		public TestBean3() {
		}

		public TestBean3(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@Transactional(propagation = Propagation.REQUIRES_NEW, isolation=Isolation.REPEATABLE_READ,
				timeout = 5, readOnly = true, rollbackFor = Exception.class, noRollbackFor = IOException.class)
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@Transactional(rollbackFor = Exception.class, noRollbackFor = IOException.class)
	static class TestBean4 implements ITestBean3 {

		private String name;

		private int age;

		public TestBean4() {
		}

		public TestBean4(String name, int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Transactional(rollbackFor = Exception.class, noRollbackFor = IOException.class)
	@interface Tx {
	}


	@Tx
	static class TestBean5 {

		public int getAge() {
			return 10;
		}
	}


	static class TestBean6 {

		@Tx
		public int getAge() {
			return 10;
		}
	}


	@Retention(RetentionPolicy.RUNTIME)
	@Transactional(rollbackFor = Exception.class, noRollbackFor = IOException.class)
	@interface TxWithAttribute {

		boolean readOnly();
	}


	@TxWithAttribute(readOnly = true)
	static class TestBean7 {

		public int getAge() {
			return 10;
		}
	}


	static class TestBean8 {

		@TxWithAttribute(readOnly = true)
		public int getAge() {
			return 10;
		}
	}


	@TxWithAttribute(readOnly = true)
	interface TestInterface9 {

		int getAge();
	}


	static class TestBean9 implements TestInterface9 {

		@Override
		public int getAge() {
			return 10;
		}
	}


	interface TestInterface10 {

		@TxWithAttribute(readOnly = true)
		int getAge();
	}


	static class TestBean10 implements TestInterface10 {

		@Override
		public int getAge() {
			return 10;
		}
	}


	static class Ejb3AnnotatedBean1 implements ITestBean1 {

		private String name;

		private int age;

		@Override
		@javax.ejb.TransactionAttribute(TransactionAttributeType.SUPPORTS)
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@javax.ejb.TransactionAttribute
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@javax.ejb.TransactionAttribute(TransactionAttributeType.SUPPORTS)
	static class Ejb3AnnotatedBean2 implements ITestBean1 {

		private String name;

		private int age;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@javax.ejb.TransactionAttribute
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@javax.ejb.TransactionAttribute(TransactionAttributeType.SUPPORTS)
	interface ITestEjb {

		@javax.ejb.TransactionAttribute
		int getAge();

		void setAge(int age);

		String getName();

		void setName(String name);
	}


	static class Ejb3AnnotatedBean3 implements ITestEjb {

		private String name;

		private int age;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	static class JtaAnnotatedBean1 implements ITestBean1 {

		private String name;

		private int age;

		@Override
		@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@javax.transaction.Transactional
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
	static class JtaAnnotatedBean2 implements ITestBean1 {

		private String name;

		private int age;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@javax.transaction.Transactional
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@javax.transaction.Transactional(javax.transaction.Transactional.TxType.SUPPORTS)
	interface ITestJta {

		@javax.transaction.Transactional
		int getAge();

		void setAge(int age);

		String getName();

		void setName(String name);
	}


	static class JtaAnnotatedBean3 implements ITestEjb {

		private String name;

		private int age;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}
	}


	@Transactional
	static class GroovyTestBean implements ITestBean1, GroovyObject {

		private String name;

		private int age;

		@Override
		public String getName() {
			return name;
		}

		@Override
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public int getAge() {
			return age;
		}

		@Override
		public void setAge(int age) {
			this.age = age;
		}

		@Override
		public Object invokeMethod(String name, Object args) {
			return null;
		}

		@Override
		public Object getProperty(String propertyName) {
			return null;
		}

		@Override
		public void setProperty(String propertyName, Object newValue) {
		}

		@Override
		public MetaClass getMetaClass() {
			return null;
		}

		@Override
		public void setMetaClass(MetaClass metaClass) {
		}
	}

}
