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

package org.springframework.aop.support;

import org.junit.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.tests.aop.interceptor.NopInterceptor;
import org.springframework.tests.aop.interceptor.SerializableNopInterceptor;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.Person;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.SerializationTestUtils;

import static org.junit.Assert.*;
import static org.springframework.tests.TestResourceUtils.*;

/**
 * @author Rod Johnson
 * @author Chris Beams
 */
public class RegexpMethodPointcutAdvisorIntegrationTests {

	private static final Resource CONTEXT =
			qualifiedResource(RegexpMethodPointcutAdvisorIntegrationTests.class, "context.xml");


	@Test
	public void testSinglePattern() throws Throwable {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(CONTEXT);
		ITestBean advised = (ITestBean) bf.getBean("settersAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());

		int newAge = 12;
		// Not advised
		advised.exceptional(null);
		assertEquals(0, nop.getCount());
		advised.setAge(newAge);
		assertEquals(newAge, advised.getAge());
		// Only setter fired
		assertEquals(1, nop.getCount());
	}

	@Test
	public void testMultiplePatterns() throws Throwable {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(CONTEXT);
		// This is a CGLIB proxy, so we can proxy it to the target class
		TestBean advised = (TestBean) bf.getBean("settersAndAbsquatulateAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());

		int newAge = 12;
		// Not advised
		advised.exceptional(null);
		assertEquals(0, nop.getCount());

		// This is proxied
		advised.absquatulate();
		assertEquals(1, nop.getCount());
		advised.setAge(newAge);
		assertEquals(newAge, advised.getAge());
		// Only setter fired
		assertEquals(2, nop.getCount());
	}

	@Test
	public void testSerialization() throws Throwable {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(bf).loadBeanDefinitions(CONTEXT);
		// This is a CGLIB proxy, so we can proxy it to the target class
		Person p = (Person) bf.getBean("serializableSettersAdvised");
		// Interceptor behind regexp advisor
		NopInterceptor nop = (NopInterceptor) bf.getBean("nopInterceptor");
		assertEquals(0, nop.getCount());

		int newAge = 12;
		// Not advised
		assertEquals(0, p.getAge());
		assertEquals(0, nop.getCount());

		// This is proxied
		p.setAge(newAge);
		assertEquals(1, nop.getCount());
		p.setAge(newAge);
		assertEquals(newAge, p.getAge());
		// Only setter fired
		assertEquals(2, nop.getCount());

		// Serialize and continue...
		p = (Person) SerializationTestUtils.serializeAndDeserialize(p);
		assertEquals(newAge, p.getAge());
		// Remembers count, but we need to get a new reference to nop...
		nop = (SerializableNopInterceptor) ((Advised) p).getAdvisors()[0].getAdvice();
		assertEquals(2, nop.getCount());
		assertEquals("serializableSettersAdvised", p.getName());
		p.setAge(newAge + 1);
		assertEquals(3, nop.getCount());
		assertEquals(newAge + 1, p.getAge());
	}

}
