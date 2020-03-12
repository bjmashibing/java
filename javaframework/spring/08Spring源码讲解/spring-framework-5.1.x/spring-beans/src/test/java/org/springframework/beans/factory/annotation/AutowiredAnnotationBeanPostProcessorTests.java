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

package org.springframework.beans.factory.annotation;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.AutowireCandidateQualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.IndexedTestBean;
import org.springframework.tests.sample.beans.NestedTestBean;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.SerializationTestUtils;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Sam Brannen
 * @author Chris Beams
 * @author Stephane Nicoll
 */
public class AutowiredAnnotationBeanPostProcessorTests {

	private DefaultListableBeanFactory bf;

	private AutowiredAnnotationBeanPostProcessor bpp;


	@Before
	public void setup() {
		bf = new DefaultListableBeanFactory();
		bf.registerResolvableDependency(BeanFactory.class, bf);
		bpp = new AutowiredAnnotationBeanPostProcessor();
		bpp.setBeanFactory(bf);
		bf.addBeanPostProcessor(bpp);
		bf.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver());
		bf.setDependencyComparator(AnnotationAwareOrderComparator.INSTANCE);
	}

	@After
	public void close() {
		bf.destroySingletons();
	}


	@Test
	public void testIncompleteBeanDefinition() {
		bf.registerBeanDefinition("testBean", new GenericBeanDefinition());
		try {
			bf.getBean("testBean");
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.getRootCause() instanceof IllegalStateException);
		}
	}

	@Test
	public void testResourceInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(ResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		ResourceInjectionBean bean = (ResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());

		bean = (ResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
	}

	@Test
	public void testExtendedResourceInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(TypedExtendedResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		TypedExtendedResourceInjectionBean bean = (TypedExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		bean = (TypedExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		String[] depBeans = bf.getDependenciesForBean("annotatedBean");
		assertEquals(2, depBeans.length);
		assertEquals("testBean", depBeans[0]);
		assertEquals("nestedTestBean", depBeans[1]);
	}

	@Test
	public void testExtendedResourceInjectionWithDestruction() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(TypedExtendedResourceInjectionBean.class));
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		TestBean tb = bf.getBean("testBean", TestBean.class);
		TypedExtendedResourceInjectionBean bean = (TypedExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		assertArrayEquals(new String[] {"testBean", "nestedTestBean"}, bf.getDependenciesForBean("annotatedBean"));
		bf.destroySingleton("testBean");
		assertFalse(bf.containsSingleton("testBean"));
		assertFalse(bf.containsSingleton("annotatedBean"));
		assertTrue(bean.destroyed);
		assertSame(0, bf.getDependenciesForBean("annotatedBean").length);
	}

	@Test
	public void testExtendedResourceInjectionWithOverriding() {
		RootBeanDefinition annotatedBd = new RootBeanDefinition(TypedExtendedResourceInjectionBean.class);
		TestBean tb2 = new TestBean();
		annotatedBd.getPropertyValues().add("testBean2", tb2);
		bf.registerBeanDefinition("annotatedBean", annotatedBd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		TypedExtendedResourceInjectionBean bean = (TypedExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb2, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());
	}

	@Test
	public void testExtendedResourceInjectionWithSkippedOverriddenMethods() {
		RootBeanDefinition annotatedBd = new RootBeanDefinition(OverriddenExtendedResourceInjectionBean.class);
		bf.registerBeanDefinition("annotatedBean", annotatedBd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		OverriddenExtendedResourceInjectionBean bean = (OverriddenExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertNull(bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertNull(bean.getBeanFactory());
		assertTrue(bean.baseInjected);
		assertTrue(bean.subInjected);
	}

	@Test
	public void testExtendedResourceInjectionWithDefaultMethod() {
		RootBeanDefinition annotatedBd = new RootBeanDefinition(DefaultMethodResourceInjectionBean.class);
		bf.registerBeanDefinition("annotatedBean", annotatedBd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		DefaultMethodResourceInjectionBean bean = (DefaultMethodResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertNull(bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertNull(bean.getBeanFactory());
		assertTrue(bean.baseInjected);
		assertTrue(bean.subInjected);
	}

	@Test
	public void testExtendedResourceInjectionWithAtRequired() {
		bf.addBeanPostProcessor(new RequiredAnnotationBeanPostProcessor());
		RootBeanDefinition bd = new RootBeanDefinition(TypedExtendedResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		TypedExtendedResourceInjectionBean bean = (TypedExtendedResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());
	}

	@Test
	public void testOptionalResourceInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(OptionalResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		NestedTestBean ntb1 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		NestedTestBean ntb2 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		OptionalResourceInjectionBean bean = (OptionalResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().length);
		assertSame(ntb1, bean.getNestedTestBeans()[0]);
		assertSame(ntb2, bean.getNestedTestBeans()[1]);
		assertEquals(2, bean.nestedTestBeansField.length);
		assertSame(ntb1, bean.nestedTestBeansField[0]);
		assertSame(ntb2, bean.nestedTestBeansField[1]);
	}

	@Test
	public void testOptionalCollectionResourceInjection() {
		RootBeanDefinition rbd = new RootBeanDefinition(OptionalCollectionResourceInjectionBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", rbd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		NestedTestBean ntb1 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		NestedTestBean ntb2 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		// Two calls to verify that caching doesn't break re-creation.
		OptionalCollectionResourceInjectionBean bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb1, bean.getNestedTestBeans().get(0));
		assertSame(ntb2, bean.getNestedTestBeans().get(1));
		assertEquals(2, bean.nestedTestBeansSetter.size());
		assertSame(ntb1, bean.nestedTestBeansSetter.get(0));
		assertSame(ntb2, bean.nestedTestBeansSetter.get(1));
		assertEquals(2, bean.nestedTestBeansField.size());
		assertSame(ntb1, bean.nestedTestBeansField.get(0));
		assertSame(ntb2, bean.nestedTestBeansField.get(1));
	}

	@Test
	public void testOptionalCollectionResourceInjectionWithSingleElement() {
		RootBeanDefinition rbd = new RootBeanDefinition(OptionalCollectionResourceInjectionBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", rbd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		NestedTestBean ntb1 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);

		// Two calls to verify that caching doesn't break re-creation.
		OptionalCollectionResourceInjectionBean bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(1, bean.getNestedTestBeans().size());
		assertSame(ntb1, bean.getNestedTestBeans().get(0));
		assertEquals(1, bean.nestedTestBeansSetter.size());
		assertSame(ntb1, bean.nestedTestBeansSetter.get(0));
		assertEquals(1, bean.nestedTestBeansField.size());
		assertSame(ntb1, bean.nestedTestBeansField.get(0));
	}

	@Test
	public void testOptionalResourceInjectionWithIncompleteDependencies() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(OptionalResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		OptionalResourceInjectionBean bean = (OptionalResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertNull(bean.getTestBean4());
		assertNull(bean.getNestedTestBeans());
	}

	@Test
	public void testOptionalResourceInjectionWithNoDependencies() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(OptionalResourceInjectionBean.class));

		OptionalResourceInjectionBean bean = (OptionalResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean4());
		assertNull(bean.getNestedTestBeans());
	}

	@Test
	public void testOrderedResourceInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(OptionalResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		OrderedNestedTestBean ntb1 = new OrderedNestedTestBean();
		ntb1.setOrder(2);
		bf.registerSingleton("nestedTestBean1", ntb1);
		OrderedNestedTestBean ntb2 = new OrderedNestedTestBean();
		ntb2.setOrder(1);
		bf.registerSingleton("nestedTestBean2", ntb2);

		OptionalResourceInjectionBean bean = (OptionalResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().length);
		assertSame(ntb2, bean.getNestedTestBeans()[0]);
		assertSame(ntb1, bean.getNestedTestBeans()[1]);
		assertEquals(2, bean.nestedTestBeansField.length);
		assertSame(ntb2, bean.nestedTestBeansField[0]);
		assertSame(ntb1, bean.nestedTestBeansField[1]);
	}

	@Test
	public void testAnnotationOrderedResourceInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(OptionalResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		OptionalResourceInjectionBean bean = (OptionalResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().length);
		assertSame(ntb2, bean.getNestedTestBeans()[0]);
		assertSame(ntb1, bean.getNestedTestBeans()[1]);
		assertEquals(2, bean.nestedTestBeansField.length);
		assertSame(ntb2, bean.nestedTestBeansField[0]);
		assertSame(ntb1, bean.nestedTestBeansField[1]);
	}

	@Test
	public void testOrderedCollectionResourceInjection() {
		RootBeanDefinition rbd = new RootBeanDefinition(OptionalCollectionResourceInjectionBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", rbd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		OrderedNestedTestBean ntb1 = new OrderedNestedTestBean();
		ntb1.setOrder(2);
		bf.registerSingleton("nestedTestBean1", ntb1);
		OrderedNestedTestBean ntb2 = new OrderedNestedTestBean();
		ntb2.setOrder(1);
		bf.registerSingleton("nestedTestBean2", ntb2);

		// Two calls to verify that caching doesn't break re-creation.
		OptionalCollectionResourceInjectionBean bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
		assertEquals(2, bean.nestedTestBeansSetter.size());
		assertSame(ntb2, bean.nestedTestBeansSetter.get(0));
		assertSame(ntb1, bean.nestedTestBeansSetter.get(1));
		assertEquals(2, bean.nestedTestBeansField.size());
		assertSame(ntb2, bean.nestedTestBeansField.get(0));
		assertSame(ntb1, bean.nestedTestBeansField.get(1));
	}

	@Test
	public void testAnnotationOrderedCollectionResourceInjection() {
		RootBeanDefinition rbd = new RootBeanDefinition(OptionalCollectionResourceInjectionBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", rbd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		IndexedTestBean itb = new IndexedTestBean();
		bf.registerSingleton("indexedTestBean", itb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		// Two calls to verify that caching doesn't break re-creation.
		OptionalCollectionResourceInjectionBean bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		bean = (OptionalCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(itb, bean.getIndexedTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
		assertEquals(2, bean.nestedTestBeansSetter.size());
		assertSame(ntb2, bean.nestedTestBeansSetter.get(0));
		assertSame(ntb1, bean.nestedTestBeansSetter.get(1));
		assertEquals(2, bean.nestedTestBeansField.size());
		assertSame(ntb2, bean.nestedTestBeansField.get(0));
		assertSame(ntb1, bean.nestedTestBeansField.get(1));
	}

	@Test
	public void testConstructorResourceInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(ConstructorResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb = new NestedTestBean();
		bf.registerSingleton("nestedTestBean", ntb);

		ConstructorResourceInjectionBean bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertSame(ntb, bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());
	}

	@Test
	public void testConstructorResourceInjectionWithNullFromFactoryBean() {
		RootBeanDefinition bd = new RootBeanDefinition(ConstructorResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		bf.registerBeanDefinition("nestedTestBean", new RootBeanDefinition(NullNestedTestBeanFactoryBean.class));
		bf.registerSingleton("nestedTestBean2", new NestedTestBean());

		ConstructorResourceInjectionBean bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertNull(bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertSame(tb, bean.getTestBean2());
		assertSame(tb, bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertNull(bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());
	}

	@Test
	public void testConstructorResourceInjectionWithNullFromFactoryMethod() {
		RootBeanDefinition bd = new RootBeanDefinition(ConstructorResourceInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		RootBeanDefinition tb = new RootBeanDefinition(NullFactoryMethods.class);
		tb.setFactoryMethodName("createTestBean");
		bf.registerBeanDefinition("testBean", tb);
		RootBeanDefinition ntb = new RootBeanDefinition(NullFactoryMethods.class);
		ntb.setFactoryMethodName("createNestedTestBean");
		bf.registerBeanDefinition("nestedTestBean", ntb);
		bf.registerSingleton("nestedTestBean2", new NestedTestBean());

		ConstructorResourceInjectionBean bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean4());
		assertNull(bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());

		bean = (ConstructorResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean4());
		assertNull(bean.getNestedTestBean());
		assertSame(bf, bean.getBeanFactory());
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleCandidates() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorsResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb1 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		NestedTestBean ntb2 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		ConstructorsResourceInjectionBean bean = (ConstructorsResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertEquals(2, bean.getNestedTestBeans().length);
		assertSame(ntb1, bean.getNestedTestBeans()[0]);
		assertSame(ntb2, bean.getNestedTestBeans()[1]);
	}

	@Test
	public void testConstructorResourceInjectionWithNoCandidatesAndNoFallback() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorWithoutFallbackBean.class));

		try {
			bf.getBean("annotatedBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(ConstructorWithoutFallbackBean.class, ex.getInjectionPoint().getMethodParameter().getDeclaringClass());
		}
	}

	@Test
	public void testConstructorResourceInjectionWithCollectionAndNullFromFactoryBean() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(
				ConstructorsCollectionResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		bf.registerBeanDefinition("nestedTestBean1", new RootBeanDefinition(NullNestedTestBeanFactoryBean.class));
		NestedTestBean ntb2 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		ConstructorsCollectionResourceInjectionBean bean = (ConstructorsCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertEquals(1, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));

		Map<String, NestedTestBean> map = bf.getBeansOfType(NestedTestBean.class);
		assertNull(map.get("nestedTestBean1"));
		assertSame(ntb2, map.get("nestedTestBean2"));
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleCandidatesAsCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(
				ConstructorsCollectionResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		NestedTestBean ntb1 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		NestedTestBean ntb2 = new NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		ConstructorsCollectionResourceInjectionBean bean = (ConstructorsCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb1, bean.getNestedTestBeans().get(0));
		assertSame(ntb2, bean.getNestedTestBeans().get(1));
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleOrderedCandidates() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorsResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		ConstructorsResourceInjectionBean bean = (ConstructorsResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertEquals(2, bean.getNestedTestBeans().length);
		assertSame(ntb2, bean.getNestedTestBeans()[0]);
		assertSame(ntb1, bean.getNestedTestBeans()[1]);
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleCandidatesAsOrderedCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorsCollectionResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		ConstructorsCollectionResourceInjectionBean bean = (ConstructorsCollectionResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertSame(tb, bean.getTestBean4());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
	}

	@Test
	public void testSingleConstructorInjectionWithMultipleCandidatesAsRequiredVararg() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorVarargBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		SingleConstructorVarargBean bean = (SingleConstructorVarargBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
	}

	@Test
	public void testSingleConstructorInjectionWithEmptyVararg() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorVarargBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		SingleConstructorVarargBean bean = (SingleConstructorVarargBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertNotNull(bean.getNestedTestBeans());
		assertTrue(bean.getNestedTestBeans().isEmpty());
	}

	@Test
	public void testSingleConstructorInjectionWithMultipleCandidatesAsRequiredCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorRequiredCollectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		SingleConstructorRequiredCollectionBean bean = (SingleConstructorRequiredCollectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
	}

	@Test
	public void testSingleConstructorInjectionWithEmptyCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorRequiredCollectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		SingleConstructorRequiredCollectionBean bean = (SingleConstructorRequiredCollectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertNotNull(bean.getNestedTestBeans());
		assertTrue(bean.getNestedTestBeans().isEmpty());
	}

	@Test
	public void testSingleConstructorInjectionWithMultipleCandidatesAsOrderedCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorOptionalCollectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);
		FixedOrder2NestedTestBean ntb1 = new FixedOrder2NestedTestBean();
		bf.registerSingleton("nestedTestBean1", ntb1);
		FixedOrder1NestedTestBean ntb2 = new FixedOrder1NestedTestBean();
		bf.registerSingleton("nestedTestBean2", ntb2);

		SingleConstructorOptionalCollectionBean bean = (SingleConstructorOptionalCollectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertEquals(2, bean.getNestedTestBeans().size());
		assertSame(ntb2, bean.getNestedTestBeans().get(0));
		assertSame(ntb1, bean.getNestedTestBeans().get(1));
	}

	@Test
	public void testSingleConstructorInjectionWithEmptyCollectionAsNull() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorOptionalCollectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		SingleConstructorOptionalCollectionBean bean = (SingleConstructorOptionalCollectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean());
		assertNull(bean.getNestedTestBeans());
	}

	@Test(expected = UnsatisfiedDependencyException.class)
	public void testSingleConstructorInjectionWithMissingDependency() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorOptionalCollectionBean.class));
		bf.getBean("annotatedBean");
	}

	@Test(expected = UnsatisfiedDependencyException.class)
	public void testSingleConstructorInjectionWithNullDependency() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SingleConstructorOptionalCollectionBean.class));
		RootBeanDefinition tb = new RootBeanDefinition(NullFactoryMethods.class);
		tb.setFactoryMethodName("createTestBean");
		bf.registerBeanDefinition("testBean", tb);

		bf.getBean("annotatedBean");
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleCandidatesAndFallback() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorsResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		ConstructorsResourceInjectionBean bean = (ConstructorsResourceInjectionBean) bf.getBean("annotatedBean");
		assertSame(tb, bean.getTestBean3());
		assertNull(bean.getTestBean4());
	}

	@Test
	public void testConstructorResourceInjectionWithMultipleCandidatesAndDefaultFallback() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ConstructorsResourceInjectionBean.class));

		ConstructorsResourceInjectionBean bean = (ConstructorsResourceInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean4());
	}

	@Test
	public void testConstructorInjectionWithMap() {
		RootBeanDefinition bd = new RootBeanDefinition(MapConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb1 = new TestBean("tb1");
		bf.registerSingleton("testBean1", tb1);
		RootBeanDefinition tb2 = new RootBeanDefinition(NullFactoryMethods.class);
		tb2.setFactoryMethodName("createTestBean");
		bf.registerBeanDefinition("testBean2", tb2);

		MapConstructorInjectionBean bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertEquals(1, bean.getTestBeanMap().size());
		assertSame(tb1, bean.getTestBeanMap().get("testBean1"));
		assertNull(bean.getTestBeanMap().get("testBean2"));

		bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertEquals(1, bean.getTestBeanMap().size());
		assertSame(tb1, bean.getTestBeanMap().get("testBean1"));
		assertNull(bean.getTestBeanMap().get("testBean2"));
	}

	@Test
	public void testFieldInjectionWithMap() {
		RootBeanDefinition bd = new RootBeanDefinition(MapFieldInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb1 = new TestBean("tb1");
		TestBean tb2 = new TestBean("tb2");
		bf.registerSingleton("testBean1", tb1);
		bf.registerSingleton("testBean2", tb2);

		MapFieldInjectionBean bean = (MapFieldInjectionBean) bf.getBean("annotatedBean");
		assertEquals(2, bean.getTestBeanMap().size());
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean1"));
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean2"));
		assertTrue(bean.getTestBeanMap().values().contains(tb1));
		assertTrue(bean.getTestBeanMap().values().contains(tb2));

		bean = (MapFieldInjectionBean) bf.getBean("annotatedBean");
		assertEquals(2, bean.getTestBeanMap().size());
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean1"));
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean2"));
		assertTrue(bean.getTestBeanMap().values().contains(tb1));
		assertTrue(bean.getTestBeanMap().values().contains(tb2));
	}

	@Test
	public void testMethodInjectionWithMap() {
		RootBeanDefinition bd = new RootBeanDefinition(MapMethodInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		MapMethodInjectionBean bean = (MapMethodInjectionBean) bf.getBean("annotatedBean");
		assertEquals(1, bean.getTestBeanMap().size());
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean"));
		assertTrue(bean.getTestBeanMap().values().contains(tb));
		assertSame(tb, bean.getTestBean());

		bean = (MapMethodInjectionBean) bf.getBean("annotatedBean");
		assertEquals(1, bean.getTestBeanMap().size());
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean"));
		assertTrue(bean.getTestBeanMap().values().contains(tb));
		assertSame(tb, bean.getTestBean());
	}

	@Test
	public void testMethodInjectionWithMapAndMultipleMatches() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(MapMethodInjectionBean.class));
		bf.registerBeanDefinition("testBean1", new RootBeanDefinition(TestBean.class));
		bf.registerBeanDefinition("testBean2", new RootBeanDefinition(TestBean.class));

		try {
			bf.getBean("annotatedBean");
			fail("should have failed, more than one bean of type");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(MapMethodInjectionBean.class, ex.getInjectionPoint().getMethodParameter().getDeclaringClass());
		}
	}

	@Test
	public void testMethodInjectionWithMapAndMultipleMatchesButOnlyOneAutowireCandidate() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(MapMethodInjectionBean.class));
		bf.registerBeanDefinition("testBean1", new RootBeanDefinition(TestBean.class));
		RootBeanDefinition rbd2 = new RootBeanDefinition(TestBean.class);
		rbd2.setAutowireCandidate(false);
		bf.registerBeanDefinition("testBean2", rbd2);

		MapMethodInjectionBean bean = (MapMethodInjectionBean) bf.getBean("annotatedBean");
		TestBean tb = (TestBean) bf.getBean("testBean1");
		assertEquals(1, bean.getTestBeanMap().size());
		assertTrue(bean.getTestBeanMap().keySet().contains("testBean1"));
		assertTrue(bean.getTestBeanMap().values().contains(tb));
		assertSame(tb, bean.getTestBean());
	}

	@Test
	public void testMethodInjectionWithMapAndNoMatches() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(MapMethodInjectionBean.class));

		MapMethodInjectionBean bean = (MapMethodInjectionBean) bf.getBean("annotatedBean");
		assertNull(bean.getTestBeanMap());
		assertNull(bean.getTestBean());
	}

	@Test
	public void testConstructorInjectionWithTypedMapAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(MapConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		MyTestBeanMap tbm = new MyTestBeanMap();
		tbm.put("testBean1", new TestBean("tb1"));
		tbm.put("testBean2", new TestBean("tb2"));
		bf.registerSingleton("testBeans", tbm);
		bf.registerSingleton("otherMap", new Properties());

		MapConstructorInjectionBean bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(tbm, bean.getTestBeanMap());
		bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(tbm, bean.getTestBeanMap());
	}

	@Test
	public void testConstructorInjectionWithPlainMapAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(MapConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		RootBeanDefinition tbm = new RootBeanDefinition(CollectionFactoryMethods.class);
		tbm.setUniqueFactoryMethodName("testBeanMap");
		bf.registerBeanDefinition("myTestBeanMap", tbm);
		bf.registerSingleton("otherMap", new HashMap<>());

		MapConstructorInjectionBean bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
		bean = (MapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
	}

	@Test
	public void testConstructorInjectionWithCustomMapAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(CustomMapConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		RootBeanDefinition tbm = new RootBeanDefinition(CustomCollectionFactoryMethods.class);
		tbm.setUniqueFactoryMethodName("testBeanMap");
		bf.registerBeanDefinition("myTestBeanMap", tbm);
		bf.registerSingleton("testBean1", new TestBean());
		bf.registerSingleton("testBean2", new TestBean());

		CustomMapConstructorInjectionBean bean = (CustomMapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
		bean = (CustomMapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
	}

	@Test
	public void testConstructorInjectionWithPlainHashMapAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(QualifiedMapConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		bf.registerBeanDefinition("myTestBeanMap", new RootBeanDefinition(HashMap.class));

		QualifiedMapConstructorInjectionBean bean = (QualifiedMapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
		bean = (QualifiedMapConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanMap"), bean.getTestBeanMap());
	}

	@Test
	public void testConstructorInjectionWithTypedSetAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(SetConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		MyTestBeanSet tbs = new MyTestBeanSet();
		tbs.add(new TestBean("tb1"));
		tbs.add(new TestBean("tb2"));
		bf.registerSingleton("testBeans", tbs);
		bf.registerSingleton("otherSet", new HashSet<>());

		SetConstructorInjectionBean bean = (SetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(tbs, bean.getTestBeanSet());
		bean = (SetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(tbs, bean.getTestBeanSet());
	}

	@Test
	public void testConstructorInjectionWithPlainSetAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(SetConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		RootBeanDefinition tbs = new RootBeanDefinition(CollectionFactoryMethods.class);
		tbs.setUniqueFactoryMethodName("testBeanSet");
		bf.registerBeanDefinition("myTestBeanSet", tbs);
		bf.registerSingleton("otherSet", new HashSet<>());

		SetConstructorInjectionBean bean = (SetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanSet"), bean.getTestBeanSet());
		bean = (SetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanSet"), bean.getTestBeanSet());
	}

	@Test
	public void testConstructorInjectionWithCustomSetAsBean() {
		RootBeanDefinition bd = new RootBeanDefinition(CustomSetConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		RootBeanDefinition tbs = new RootBeanDefinition(CustomCollectionFactoryMethods.class);
		tbs.setUniqueFactoryMethodName("testBeanSet");
		bf.registerBeanDefinition("myTestBeanSet", tbs);

		CustomSetConstructorInjectionBean bean = (CustomSetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanSet"), bean.getTestBeanSet());
		bean = (CustomSetConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("myTestBeanSet"), bean.getTestBeanSet());
	}

	@Test
	public void testSelfReference() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SelfInjectionBean.class));

		SelfInjectionBean bean = (SelfInjectionBean) bf.getBean("annotatedBean");
		assertSame(bean, bean.reference);
		assertNull(bean.referenceCollection);
	}

	@Test
	public void testSelfReferenceWithOther() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SelfInjectionBean.class));
		bf.registerBeanDefinition("annotatedBean2", new RootBeanDefinition(SelfInjectionBean.class));

		SelfInjectionBean bean = (SelfInjectionBean) bf.getBean("annotatedBean");
		SelfInjectionBean bean2 = (SelfInjectionBean) bf.getBean("annotatedBean2");
		assertSame(bean2, bean.reference);
		assertEquals(1, bean.referenceCollection.size());
		assertSame(bean2, bean.referenceCollection.get(0));
	}

	@Test
	public void testSelfReferenceCollection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SelfInjectionCollectionBean.class));

		SelfInjectionCollectionBean bean = (SelfInjectionCollectionBean) bf.getBean("annotatedBean");
		assertSame(bean, bean.reference);
		assertNull(bean.referenceCollection);
	}

	@Test
	public void testSelfReferenceCollectionWithOther() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SelfInjectionCollectionBean.class));
		bf.registerBeanDefinition("annotatedBean2", new RootBeanDefinition(SelfInjectionCollectionBean.class));

		SelfInjectionCollectionBean bean = (SelfInjectionCollectionBean) bf.getBean("annotatedBean");
		SelfInjectionCollectionBean bean2 = (SelfInjectionCollectionBean) bf.getBean("annotatedBean2");
		assertSame(bean2, bean.reference);
		assertSame(1, bean2.referenceCollection.size());
		assertSame(bean2, bean.referenceCollection.get(0));
	}

	@Test
	public void testObjectFactoryFieldInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectFactoryFieldInjectionBean.class));
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));

		ObjectFactoryFieldInjectionBean bean = (ObjectFactoryFieldInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean"), bean.getTestBean());
	}

	@Test
	public void testObjectFactoryConstructorInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectFactoryConstructorInjectionBean.class));
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));

		ObjectFactoryConstructorInjectionBean bean = (ObjectFactoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean"), bean.getTestBean());
	}

	@Test
	public void testObjectFactoryInjectionIntoPrototypeBean() {
		RootBeanDefinition annotatedBeanDefinition = new RootBeanDefinition(ObjectFactoryFieldInjectionBean.class);
		annotatedBeanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", annotatedBeanDefinition);
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));

		ObjectFactoryFieldInjectionBean bean = (ObjectFactoryFieldInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean"), bean.getTestBean());
		ObjectFactoryFieldInjectionBean anotherBean = (ObjectFactoryFieldInjectionBean) bf.getBean("annotatedBean");
		assertNotSame(anotherBean, bean);
		assertSame(bf.getBean("testBean"), anotherBean.getTestBean());
	}

	@Test
	public void testObjectFactoryQualifierInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectFactoryQualifierInjectionBean.class));
		RootBeanDefinition bd = new RootBeanDefinition(TestBean.class);
		bd.addQualifier(new AutowireCandidateQualifier(Qualifier.class, "testBean"));
		bf.registerBeanDefinition("dependencyBean", bd);
		bf.registerBeanDefinition("dependencyBean2", new RootBeanDefinition(TestBean.class));

		ObjectFactoryQualifierInjectionBean bean = (ObjectFactoryQualifierInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("dependencyBean"), bean.getTestBean());
	}

	@Test
	public void testObjectFactoryQualifierProviderInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectFactoryQualifierInjectionBean.class));
		RootBeanDefinition bd = new RootBeanDefinition(TestBean.class);
		bd.setQualifiedElement(ReflectionUtils.findMethod(getClass(), "testBeanQualifierProvider"));
		bf.registerBeanDefinition("dependencyBean", bd);
		bf.registerBeanDefinition("dependencyBean2", new RootBeanDefinition(TestBean.class));

		ObjectFactoryQualifierInjectionBean bean = (ObjectFactoryQualifierInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("dependencyBean"), bean.getTestBean());
	}

	@Test
	public void testObjectFactorySerialization() throws Exception {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectFactoryFieldInjectionBean.class));
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));
		bf.setSerializationId("test");

		ObjectFactoryFieldInjectionBean bean = (ObjectFactoryFieldInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean"), bean.getTestBean());
		bean = (ObjectFactoryFieldInjectionBean) SerializationTestUtils.serializeAndDeserialize(bean);
		assertSame(bf.getBean("testBean"), bean.getTestBean());
	}

	@Test
	public void testObjectProviderInjectionWithPrototype() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));
		RootBeanDefinition tbd = new RootBeanDefinition(TestBean.class);
		tbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("testBean", tbd);

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		assertEquals(bf.getBean("testBean"), bean.getTestBean());
		assertEquals(bf.getBean("testBean", "myName"), bean.getTestBean("myName"));
		assertEquals(bf.getBean("testBean"), bean.getOptionalTestBean());
		assertEquals(bf.getBean("testBean"), bean.getOptionalTestBeanWithDefault());
		assertEquals(bf.getBean("testBean"), bean.consumeOptionalTestBean());
		assertEquals(bf.getBean("testBean"), bean.getUniqueTestBean());
		assertEquals(bf.getBean("testBean"), bean.getUniqueTestBeanWithDefault());
		assertEquals(bf.getBean("testBean"), bean.consumeUniqueTestBean());

		List<?> testBeans = bean.iterateTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.forEachTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.streamTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.sortedTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
	}

	@Test
	public void testObjectProviderInjectionWithSingletonTarget() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));
		bf.registerBeanDefinition("testBean", new RootBeanDefinition(TestBean.class));

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean"), bean.getTestBean());
		assertSame(bf.getBean("testBean"), bean.getOptionalTestBean());
		assertSame(bf.getBean("testBean"), bean.getOptionalTestBeanWithDefault());
		assertEquals(bf.getBean("testBean"), bean.consumeOptionalTestBean());
		assertSame(bf.getBean("testBean"), bean.getUniqueTestBean());
		assertSame(bf.getBean("testBean"), bean.getUniqueTestBeanWithDefault());
		assertEquals(bf.getBean("testBean"), bean.consumeUniqueTestBean());

		List<?> testBeans = bean.iterateTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.forEachTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.streamTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
		testBeans = bean.sortedTestBeans();
		assertEquals(1, testBeans.size());
		assertTrue(testBeans.contains(bf.getBean("testBean")));
	}

	@Test
	public void testObjectProviderInjectionWithTargetNotAvailable() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		try {
			bean.getTestBean();
			fail("Should have thrown NoSuchBeanDefinitionException");
		}
		catch (NoSuchBeanDefinitionException ex) {
			// expected
		}
		assertNull(bean.getOptionalTestBean());
		assertNull(bean.consumeOptionalTestBean());
		assertEquals(new TestBean("default"), bean.getOptionalTestBeanWithDefault());
		assertEquals(new TestBean("default"), bean.getUniqueTestBeanWithDefault());
		assertNull(bean.getUniqueTestBean());
		assertNull(bean.consumeUniqueTestBean());

		List<?> testBeans = bean.iterateTestBeans();
		assertTrue(testBeans.isEmpty());
		testBeans = bean.forEachTestBeans();
		assertTrue(testBeans.isEmpty());
		testBeans = bean.streamTestBeans();
		assertTrue(testBeans.isEmpty());
		testBeans = bean.sortedTestBeans();
		assertTrue(testBeans.isEmpty());
	}

	@Test
	public void testObjectProviderInjectionWithTargetNotUnique() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));
		bf.registerBeanDefinition("testBean1", new RootBeanDefinition(TestBean.class));
		bf.registerBeanDefinition("testBean2", new RootBeanDefinition(TestBean.class));

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		try {
			bean.getTestBean();
			fail("Should have thrown NoUniqueBeanDefinitionException");
		}
		catch (NoUniqueBeanDefinitionException ex) {
			// expected
		}
		try {
			bean.getOptionalTestBean();
			fail("Should have thrown NoUniqueBeanDefinitionException");
		}
		catch (NoUniqueBeanDefinitionException ex) {
			// expected
		}
		try {
			bean.consumeOptionalTestBean();
			fail("Should have thrown NoUniqueBeanDefinitionException");
		}
		catch (NoUniqueBeanDefinitionException ex) {
			// expected
		}
		assertNull(bean.getUniqueTestBean());
		assertNull(bean.consumeUniqueTestBean());

		List<?> testBeans = bean.iterateTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.forEachTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.streamTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.sortedTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
	}

	@Test
	public void testObjectProviderInjectionWithTargetPrimary() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));
		RootBeanDefinition tb1 = new RootBeanDefinition(TestBeanFactory.class);
		tb1.setFactoryMethodName("newTestBean1");
		tb1.setPrimary(true);
		bf.registerBeanDefinition("testBean1", tb1);
		RootBeanDefinition tb2 = new RootBeanDefinition(TestBeanFactory.class);
		tb2.setFactoryMethodName("newTestBean2");
		tb2.setLazyInit(true);
		bf.registerBeanDefinition("testBean2", tb2);

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		assertSame(bf.getBean("testBean1"), bean.getTestBean());
		assertSame(bf.getBean("testBean1"), bean.getOptionalTestBean());
		assertSame(bf.getBean("testBean1"), bean.consumeOptionalTestBean());
		assertSame(bf.getBean("testBean1"), bean.getUniqueTestBean());
		assertSame(bf.getBean("testBean1"), bean.consumeUniqueTestBean());
		assertFalse(bf.containsSingleton("testBean2"));

		List<?> testBeans = bean.iterateTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.forEachTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.streamTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean1"), testBeans.get(0));
		assertSame(bf.getBean("testBean2"), testBeans.get(1));
		testBeans = bean.sortedTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean2"), testBeans.get(0));
		assertSame(bf.getBean("testBean1"), testBeans.get(1));
	}

	@Test
	public void testObjectProviderInjectionWithUnresolvedOrderedStream() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(ObjectProviderInjectionBean.class));
		RootBeanDefinition tb1 = new RootBeanDefinition(TestBeanFactory.class);
		tb1.setFactoryMethodName("newTestBean1");
		tb1.setPrimary(true);
		bf.registerBeanDefinition("testBean1", tb1);
		RootBeanDefinition tb2 = new RootBeanDefinition(TestBeanFactory.class);
		tb2.setFactoryMethodName("newTestBean2");
		tb2.setLazyInit(true);
		bf.registerBeanDefinition("testBean2", tb2);

		ObjectProviderInjectionBean bean = (ObjectProviderInjectionBean) bf.getBean("annotatedBean");
		List<?> testBeans = bean.sortedTestBeans();
		assertEquals(2, testBeans.size());
		assertSame(bf.getBean("testBean2"), testBeans.get(0));
		assertSame(bf.getBean("testBean1"), testBeans.get(1));
	}

	@Test
	public void testCustomAnnotationRequiredFieldResourceInjection() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredFieldResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		CustomAnnotationRequiredFieldResourceInjectionBean bean =
				(CustomAnnotationRequiredFieldResourceInjectionBean) bf.getBean("customBean");
		assertSame(tb, bean.getTestBean());
	}

	@Test
	public void testCustomAnnotationRequiredFieldResourceInjectionFailsWhenNoDependencyFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredFieldResourceInjectionBean.class));

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationRequiredFieldResourceInjectionBean.class,
					ex.getInjectionPoint().getField().getDeclaringClass());
		}
	}

	@Test
	public void testCustomAnnotationRequiredFieldResourceInjectionFailsWhenMultipleDependenciesFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredFieldResourceInjectionBean.class));
		TestBean tb1 = new TestBean();
		bf.registerSingleton("testBean1", tb1);
		TestBean tb2 = new TestBean();
		bf.registerSingleton("testBean2", tb2);

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationRequiredFieldResourceInjectionBean.class,
					ex.getInjectionPoint().getField().getDeclaringClass());
		}
	}

	@Test
	public void testCustomAnnotationRequiredMethodResourceInjection() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredMethodResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		CustomAnnotationRequiredMethodResourceInjectionBean bean =
				(CustomAnnotationRequiredMethodResourceInjectionBean) bf.getBean("customBean");
		assertSame(tb, bean.getTestBean());
	}

	@Test
	public void testCustomAnnotationRequiredMethodResourceInjectionFailsWhenNoDependencyFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredMethodResourceInjectionBean.class));

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationRequiredMethodResourceInjectionBean.class,
					ex.getInjectionPoint().getMethodParameter().getDeclaringClass());
		}
	}

	@Test
	public void testCustomAnnotationRequiredMethodResourceInjectionFailsWhenMultipleDependenciesFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationRequiredMethodResourceInjectionBean.class));
		TestBean tb1 = new TestBean();
		bf.registerSingleton("testBean1", tb1);
		TestBean tb2 = new TestBean();
		bf.registerSingleton("testBean2", tb2);

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationRequiredMethodResourceInjectionBean.class,
					ex.getInjectionPoint().getMethodParameter().getDeclaringClass());
		}
	}

	@Test
	public void testCustomAnnotationOptionalFieldResourceInjection() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalFieldResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		CustomAnnotationOptionalFieldResourceInjectionBean bean =
				(CustomAnnotationOptionalFieldResourceInjectionBean) bf.getBean("customBean");
		assertSame(tb, bean.getTestBean3());
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
	}

	@Test
	public void testCustomAnnotationOptionalFieldResourceInjectionWhenNoDependencyFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalFieldResourceInjectionBean.class));

		CustomAnnotationOptionalFieldResourceInjectionBean bean =
				(CustomAnnotationOptionalFieldResourceInjectionBean) bf.getBean("customBean");
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
	}

	@Test
	public void testCustomAnnotationOptionalFieldResourceInjectionWhenMultipleDependenciesFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalFieldResourceInjectionBean.class));
		TestBean tb1 = new TestBean();
		bf.registerSingleton("testBean1", tb1);
		TestBean tb2 = new TestBean();
		bf.registerSingleton("testBean2", tb2);

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationOptionalFieldResourceInjectionBean.class,
					ex.getInjectionPoint().getField().getDeclaringClass());
		}
	}

	@Test
	public void testCustomAnnotationOptionalMethodResourceInjection() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalMethodResourceInjectionBean.class));
		TestBean tb = new TestBean();
		bf.registerSingleton("testBean", tb);

		CustomAnnotationOptionalMethodResourceInjectionBean bean =
				(CustomAnnotationOptionalMethodResourceInjectionBean) bf.getBean("customBean");
		assertSame(tb, bean.getTestBean3());
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
	}

	@Test
	public void testCustomAnnotationOptionalMethodResourceInjectionWhenNoDependencyFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalMethodResourceInjectionBean.class));

		CustomAnnotationOptionalMethodResourceInjectionBean bean =
				(CustomAnnotationOptionalMethodResourceInjectionBean) bf.getBean("customBean");
		assertNull(bean.getTestBean3());
		assertNull(bean.getTestBean());
		assertNull(bean.getTestBean2());
	}

	@Test
	public void testCustomAnnotationOptionalMethodResourceInjectionWhenMultipleDependenciesFound() {
		bpp.setAutowiredAnnotationType(MyAutowired.class);
		bpp.setRequiredParameterName("optional");
		bpp.setRequiredParameterValue(false);
		bf.registerBeanDefinition("customBean", new RootBeanDefinition(
				CustomAnnotationOptionalMethodResourceInjectionBean.class));
		TestBean tb1 = new TestBean();
		bf.registerSingleton("testBean1", tb1);
		TestBean tb2 = new TestBean();
		bf.registerSingleton("testBean2", tb2);

		try {
			bf.getBean("customBean");
			fail("Should have thrown UnsatisfiedDependencyException");
		}
		catch (UnsatisfiedDependencyException ex) {
			// expected
			assertSame(CustomAnnotationOptionalMethodResourceInjectionBean.class,
					ex.getInjectionPoint().getMethodParameter().getDeclaringClass());
		}
	}

	/**
	 * Verifies that a dependency on a {@link FactoryBean} can be autowired via
	 * {@link Autowired @Autowired}, specifically addressing the JIRA issue
	 * raised in <a
	 * href="https://opensource.atlassian.com/projects/spring/browse/SPR-4040"
	 * target="_blank">SPR-4040</a>.
	 */
	@Test
	public void testBeanAutowiredWithFactoryBean() {
		bf.registerBeanDefinition("factoryBeanDependentBean", new RootBeanDefinition(FactoryBeanDependentBean.class));
		bf.registerSingleton("stringFactoryBean", new StringFactoryBean());

		final StringFactoryBean factoryBean = (StringFactoryBean) bf.getBean("&stringFactoryBean");
		final FactoryBeanDependentBean bean = (FactoryBeanDependentBean) bf.getBean("factoryBeanDependentBean");

		assertNotNull("The singleton StringFactoryBean should have been registered.", factoryBean);
		assertNotNull("The factoryBeanDependentBean should have been registered.", bean);
		assertEquals("The FactoryBeanDependentBean should have been autowired 'by type' with the StringFactoryBean.",
				factoryBean, bean.getFactoryBean());
	}

	@Test
	public void testGenericsBasedFieldInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		String sv = "X";
		bf.registerSingleton("stringValue", sv);
		Integer iv = 1;
		bf.registerSingleton("integerValue", iv);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryFieldInjectionBean bean = (RepositoryFieldInjectionBean) bf.getBean("annotatedBean");
		assertSame(sv, bean.string);
		assertSame(iv, bean.integer);
		assertSame(1, bean.stringArray.length);
		assertSame(1, bean.integerArray.length);
		assertSame(sv, bean.stringArray[0]);
		assertSame(iv, bean.integerArray[0]);
		assertSame(1, bean.stringList.size());
		assertSame(1, bean.integerList.size());
		assertSame(sv, bean.stringList.get(0));
		assertSame(iv, bean.integerList.get(0));
		assertSame(1, bean.stringMap.size());
		assertSame(1, bean.integerMap.size());
		assertSame(sv, bean.stringMap.get("stringValue"));
		assertSame(iv, bean.integerMap.get("integerValue"));
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	public void testGenericsBasedFieldInjectionWithSubstitutedVariables() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithSubstitutedVariables.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		String sv = "X";
		bf.registerSingleton("stringValue", sv);
		Integer iv = 1;
		bf.registerSingleton("integerValue", iv);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryFieldInjectionBeanWithSubstitutedVariables bean = (RepositoryFieldInjectionBeanWithSubstitutedVariables) bf.getBean("annotatedBean");
		assertSame(sv, bean.string);
		assertSame(iv, bean.integer);
		assertSame(1, bean.stringArray.length);
		assertSame(1, bean.integerArray.length);
		assertSame(sv, bean.stringArray[0]);
		assertSame(iv, bean.integerArray[0]);
		assertSame(1, bean.stringList.size());
		assertSame(1, bean.integerList.size());
		assertSame(sv, bean.stringList.get(0));
		assertSame(iv, bean.integerList.get(0));
		assertSame(1, bean.stringMap.size());
		assertSame(1, bean.integerMap.size());
		assertSame(sv, bean.stringMap.get("stringValue"));
		assertSame(iv, bean.integerMap.get("integerValue"));
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	public void testGenericsBasedFieldInjectionWithQualifiers() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithQualifiers.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryFieldInjectionBeanWithQualifiers bean = (RepositoryFieldInjectionBeanWithQualifiers) bf.getBean("annotatedBean");
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	public void testGenericsBasedFieldInjectionWithMocks() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithQualifiers.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);

		RootBeanDefinition rbd = new RootBeanDefinition(MocksControl.class);
		bf.registerBeanDefinition("mocksControl", rbd);
		rbd = new RootBeanDefinition();
		rbd.setFactoryBeanName("mocksControl");
		rbd.setFactoryMethodName("createMock");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(Repository.class);
		bf.registerBeanDefinition("stringRepo", rbd);
		rbd = new RootBeanDefinition();
		rbd.setFactoryBeanName("mocksControl");
		rbd.setFactoryMethodName("createMock");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(Repository.class);
		rbd.setQualifiedElement(ReflectionUtils.findField(getClass(), "integerRepositoryQualifierProvider"));
		bf.registerBeanDefinition("integerRepository", rbd); // Bean name not matching qualifier

		RepositoryFieldInjectionBeanWithQualifiers bean = (RepositoryFieldInjectionBeanWithQualifiers) bf.getBean("annotatedBean");
		Repository<?> sr = bf.getBean("stringRepo", Repository.class);
		Repository<?> ir = bf.getBean("integerRepository", Repository.class);
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepository"));
	}

	@Test
	public void testGenericsBasedFieldInjectionWithSimpleMatch() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithSimpleMatch.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);

		bf.registerSingleton("repo", new StringRepository());

		RepositoryFieldInjectionBeanWithSimpleMatch bean = (RepositoryFieldInjectionBeanWithSimpleMatch) bf.getBean("annotatedBean");
		Repository<?> repo = bf.getBean("repo", Repository.class);
		assertSame(repo, bean.repository);
		assertSame(repo, bean.stringRepository);
		assertSame(1, bean.repositoryArray.length);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(repo, bean.repositoryArray[0]);
		assertSame(repo, bean.stringRepositoryArray[0]);
		assertSame(1, bean.repositoryList.size());
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(repo, bean.repositoryList.get(0));
		assertSame(repo, bean.stringRepositoryList.get(0));
		assertSame(1, bean.repositoryMap.size());
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(repo, bean.repositoryMap.get("repo"));
		assertSame(repo, bean.stringRepositoryMap.get("repo"));

		assertArrayEquals(new String[] {"repo"}, bf.getBeanNamesForType(ResolvableType.forClassWithGenerics(Repository.class, String.class)));
	}

	@Test
	public void testGenericsBasedFactoryBeanInjectionWithBeanDefinition() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFactoryBeanInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		bf.registerBeanDefinition("repoFactoryBean", new RootBeanDefinition(RepositoryFactoryBean.class));

		RepositoryFactoryBeanInjectionBean bean = (RepositoryFactoryBeanInjectionBean) bf.getBean("annotatedBean");
		RepositoryFactoryBean<?> repoFactoryBean = bf.getBean("&repoFactoryBean", RepositoryFactoryBean.class);
		assertSame(repoFactoryBean, bean.repositoryFactoryBean);
	}

	@Test
	public void testGenericsBasedFactoryBeanInjectionWithSingletonBean() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFactoryBeanInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		bf.registerSingleton("repoFactoryBean", new RepositoryFactoryBean<>());

		RepositoryFactoryBeanInjectionBean bean = (RepositoryFactoryBeanInjectionBean) bf.getBean("annotatedBean");
		RepositoryFactoryBean<?> repoFactoryBean = bf.getBean("&repoFactoryBean", RepositoryFactoryBean.class);
		assertSame(repoFactoryBean, bean.repositoryFactoryBean);
	}

	@Test
	public void testGenericsBasedFieldInjectionWithSimpleMatchAndMock() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithSimpleMatch.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);

		RootBeanDefinition rbd = new RootBeanDefinition(MocksControl.class);
		bf.registerBeanDefinition("mocksControl", rbd);
		rbd = new RootBeanDefinition();
		rbd.setFactoryBeanName("mocksControl");
		rbd.setFactoryMethodName("createMock");
		rbd.getConstructorArgumentValues().addGenericArgumentValue(Repository.class);
		bf.registerBeanDefinition("repo", rbd);

		RepositoryFieldInjectionBeanWithSimpleMatch bean = (RepositoryFieldInjectionBeanWithSimpleMatch) bf.getBean("annotatedBean");
		Repository<?> repo = bf.getBean("repo", Repository.class);
		assertSame(repo, bean.repository);
		assertSame(repo, bean.stringRepository);
		assertSame(1, bean.repositoryArray.length);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(repo, bean.repositoryArray[0]);
		assertSame(repo, bean.stringRepositoryArray[0]);
		assertSame(1, bean.repositoryList.size());
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(repo, bean.repositoryList.get(0));
		assertSame(repo, bean.stringRepositoryList.get(0));
		assertSame(1, bean.repositoryMap.size());
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(repo, bean.repositoryMap.get("repo"));
		assertSame(repo, bean.stringRepositoryMap.get("repo"));
	}

	@Test
	public void testGenericsBasedFieldInjectionWithSimpleMatchAndMockito() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryFieldInjectionBeanWithSimpleMatch.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);

		RootBeanDefinition rbd = new RootBeanDefinition();
		rbd.setBeanClassName(Mockito.class.getName());
		rbd.setFactoryMethodName("mock");
		// TypedStringValue used to be equivalent to an XML-defined argument String
		rbd.getConstructorArgumentValues().addGenericArgumentValue(new TypedStringValue(Repository.class.getName()));
		bf.registerBeanDefinition("repo", rbd);

		RepositoryFieldInjectionBeanWithSimpleMatch bean = (RepositoryFieldInjectionBeanWithSimpleMatch) bf.getBean("annotatedBean");
		Repository<?> repo = bf.getBean("repo", Repository.class);
		assertSame(repo, bean.repository);
		assertSame(repo, bean.stringRepository);
		assertSame(1, bean.repositoryArray.length);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(repo, bean.repositoryArray[0]);
		assertSame(repo, bean.stringRepositoryArray[0]);
		assertSame(1, bean.repositoryList.size());
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(repo, bean.repositoryList.get(0));
		assertSame(repo, bean.stringRepositoryList.get(0));
		assertSame(1, bean.repositoryMap.size());
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(repo, bean.repositoryMap.get("repo"));
		assertSame(repo, bean.stringRepositoryMap.get("repo"));
	}

	@Test
	public void testGenericsBasedMethodInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryMethodInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		String sv = "X";
		bf.registerSingleton("stringValue", sv);
		Integer iv = 1;
		bf.registerSingleton("integerValue", iv);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryMethodInjectionBean bean = (RepositoryMethodInjectionBean) bf.getBean("annotatedBean");
		assertSame(sv, bean.string);
		assertSame(iv, bean.integer);
		assertSame(1, bean.stringArray.length);
		assertSame(1, bean.integerArray.length);
		assertSame(sv, bean.stringArray[0]);
		assertSame(iv, bean.integerArray[0]);
		assertSame(1, bean.stringList.size());
		assertSame(1, bean.integerList.size());
		assertSame(sv, bean.stringList.get(0));
		assertSame(iv, bean.integerList.get(0));
		assertSame(1, bean.stringMap.size());
		assertSame(1, bean.integerMap.size());
		assertSame(sv, bean.stringMap.get("stringValue"));
		assertSame(iv, bean.integerMap.get("integerValue"));
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	public void testGenericsBasedMethodInjectionWithSubstitutedVariables() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryMethodInjectionBeanWithSubstitutedVariables.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		String sv = "X";
		bf.registerSingleton("stringValue", sv);
		Integer iv = 1;
		bf.registerSingleton("integerValue", iv);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryMethodInjectionBeanWithSubstitutedVariables bean = (RepositoryMethodInjectionBeanWithSubstitutedVariables) bf.getBean("annotatedBean");
		assertSame(sv, bean.string);
		assertSame(iv, bean.integer);
		assertSame(1, bean.stringArray.length);
		assertSame(1, bean.integerArray.length);
		assertSame(sv, bean.stringArray[0]);
		assertSame(iv, bean.integerArray[0]);
		assertSame(1, bean.stringList.size());
		assertSame(1, bean.integerList.size());
		assertSame(sv, bean.stringList.get(0));
		assertSame(iv, bean.integerList.get(0));
		assertSame(1, bean.stringMap.size());
		assertSame(1, bean.integerMap.size());
		assertSame(sv, bean.stringMap.get("stringValue"));
		assertSame(iv, bean.integerMap.get("integerValue"));
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	public void testGenericsBasedConstructorInjection() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		IntegerRepository ir = new IntegerRepository();
		bf.registerSingleton("integerRepo", ir);

		RepositoryConstructorInjectionBean bean = (RepositoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(sr, bean.stringRepository);
		assertSame(ir, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ir, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ir, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ir, bean.integerRepositoryMap.get("integerRepo"));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedConstructorInjectionWithNonTypedTarget() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		GenericRepository gr = new GenericRepository();
		bf.registerSingleton("genericRepo", gr);

		RepositoryConstructorInjectionBean bean = (RepositoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(gr, bean.stringRepository);
		assertSame(gr, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(gr, bean.stringRepositoryArray[0]);
		assertSame(gr, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(gr, bean.stringRepositoryList.get(0));
		assertSame(gr, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(gr, bean.stringRepositoryMap.get("genericRepo"));
		assertSame(gr, bean.integerRepositoryMap.get("genericRepo"));
	}

	@Test
	public void testGenericsBasedConstructorInjectionWithNonGenericTarget() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		SimpleRepository ngr = new SimpleRepository();
		bf.registerSingleton("simpleRepo", ngr);

		RepositoryConstructorInjectionBean bean = (RepositoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(ngr, bean.stringRepository);
		assertSame(ngr, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(ngr, bean.stringRepositoryArray[0]);
		assertSame(ngr, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(ngr, bean.stringRepositoryList.get(0));
		assertSame(ngr, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(ngr, bean.stringRepositoryMap.get("simpleRepo"));
		assertSame(ngr, bean.integerRepositoryMap.get("simpleRepo"));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedConstructorInjectionWithMixedTargets() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		GenericRepository gr = new GenericRepositorySubclass();
		bf.registerSingleton("genericRepo", gr);

		RepositoryConstructorInjectionBean bean = (RepositoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(sr, bean.stringRepository);
		assertSame(gr, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(gr, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(gr, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(gr, bean.integerRepositoryMap.get("genericRepo"));
	}

	@Test
	public void testGenericsBasedConstructorInjectionWithMixedTargetsIncludingNonGeneric() {
		RootBeanDefinition bd = new RootBeanDefinition(RepositoryConstructorInjectionBean.class);
		bd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		bf.registerBeanDefinition("annotatedBean", bd);
		StringRepository sr = new StringRepository();
		bf.registerSingleton("stringRepo", sr);
		SimpleRepository ngr = new SimpleRepositorySubclass();
		bf.registerSingleton("simpleRepo", ngr);

		RepositoryConstructorInjectionBean bean = (RepositoryConstructorInjectionBean) bf.getBean("annotatedBean");
		assertSame(sr, bean.stringRepository);
		assertSame(ngr, bean.integerRepository);
		assertSame(1, bean.stringRepositoryArray.length);
		assertSame(1, bean.integerRepositoryArray.length);
		assertSame(sr, bean.stringRepositoryArray[0]);
		assertSame(ngr, bean.integerRepositoryArray[0]);
		assertSame(1, bean.stringRepositoryList.size());
		assertSame(1, bean.integerRepositoryList.size());
		assertSame(sr, bean.stringRepositoryList.get(0));
		assertSame(ngr, bean.integerRepositoryList.get(0));
		assertSame(1, bean.stringRepositoryMap.size());
		assertSame(1, bean.integerRepositoryMap.size());
		assertSame(sr, bean.stringRepositoryMap.get("stringRepo"));
		assertSame(ngr, bean.integerRepositoryMap.get("simpleRepo"));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedInjectionIntoMatchingTypeVariable() {
		RootBeanDefinition bd = new RootBeanDefinition(GenericInterface1Impl.class);
		bd.setFactoryMethodName("create");
		bf.registerBeanDefinition("bean1", bd);
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(GenericInterface2Impl.class));

		GenericInterface1Impl bean1 = (GenericInterface1Impl) bf.getBean("bean1");
		GenericInterface2Impl bean2 = (GenericInterface2Impl) bf.getBean("bean2");
		assertSame(bean2, bean1.gi2);
		assertEquals(ResolvableType.forClass(GenericInterface1Impl.class), bd.getResolvableType());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedInjectionIntoUnresolvedTypeVariable() {
		RootBeanDefinition bd = new RootBeanDefinition(GenericInterface1Impl.class);
		bd.setFactoryMethodName("createPlain");
		bf.registerBeanDefinition("bean1", bd);
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(GenericInterface2Impl.class));

		GenericInterface1Impl bean1 = (GenericInterface1Impl) bf.getBean("bean1");
		GenericInterface2Impl bean2 = (GenericInterface2Impl) bf.getBean("bean2");
		assertSame(bean2, bean1.gi2);
		assertEquals(ResolvableType.forClass(GenericInterface1Impl.class), bd.getResolvableType());
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedInjectionIntoTypeVariableSelectingBestMatch() {
		RootBeanDefinition bd = new RootBeanDefinition(GenericInterface1Impl.class);
		bd.setFactoryMethodName("create");
		bf.registerBeanDefinition("bean1", bd);
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(GenericInterface2Impl.class));
		bf.registerBeanDefinition("bean2a", new RootBeanDefinition(ReallyGenericInterface2Impl.class));
		bf.registerBeanDefinition("bean2b", new RootBeanDefinition(PlainGenericInterface2Impl.class));

		GenericInterface1Impl bean1 = (GenericInterface1Impl) bf.getBean("bean1");
		GenericInterface2Impl bean2 = (GenericInterface2Impl) bf.getBean("bean2");
		assertSame(bean2, bean1.gi2);
		assertArrayEquals(new String[] {"bean1"}, bf.getBeanNamesForType(ResolvableType.forClassWithGenerics(GenericInterface1.class, String.class)));
		assertArrayEquals(new String[] {"bean2"}, bf.getBeanNamesForType(ResolvableType.forClassWithGenerics(GenericInterface2.class, String.class)));
	}

	@Test
	@Ignore  // SPR-11521
	@SuppressWarnings("rawtypes")
	public void testGenericsBasedInjectionIntoTypeVariableSelectingBestMatchAgainstFactoryMethodSignature() {
		RootBeanDefinition bd = new RootBeanDefinition(GenericInterface1Impl.class);
		bd.setFactoryMethodName("createErased");
		bf.registerBeanDefinition("bean1", bd);
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(GenericInterface2Impl.class));
		bf.registerBeanDefinition("bean2a", new RootBeanDefinition(ReallyGenericInterface2Impl.class));
		bf.registerBeanDefinition("bean2b", new RootBeanDefinition(PlainGenericInterface2Impl.class));

		GenericInterface1Impl bean1 = (GenericInterface1Impl) bf.getBean("bean1");
		GenericInterface2Impl bean2 = (GenericInterface2Impl) bf.getBean("bean2");
		assertSame(bean2, bean1.gi2);
	}

	@Test
	public void testGenericsBasedInjectionWithBeanDefinitionTargetResolvableType() {
		RootBeanDefinition bd1 = new RootBeanDefinition(GenericInterface2Bean.class);
		bd1.setTargetType(ResolvableType.forClassWithGenerics(GenericInterface2Bean.class, String.class));
		bf.registerBeanDefinition("bean1", bd1);
		RootBeanDefinition bd2 = new RootBeanDefinition(GenericInterface2Bean.class);
		bd2.setTargetType(ResolvableType.forClassWithGenerics(GenericInterface2Bean.class, Integer.class));
		bf.registerBeanDefinition("bean2", bd2);
		bf.registerBeanDefinition("bean3", new RootBeanDefinition(MultiGenericFieldInjection.class));

		assertEquals("bean1 a bean2 123", bf.getBean("bean3").toString());
		assertEquals(ResolvableType.forClassWithGenerics(GenericInterface2Bean.class, String.class), bd1.getResolvableType());
		assertEquals(ResolvableType.forClassWithGenerics(GenericInterface2Bean.class, Integer.class), bd2.getResolvableType());
	}

	@Test
	public void testCircularTypeReference() {
		bf.registerBeanDefinition("bean1", new RootBeanDefinition(StockServiceImpl.class));
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(StockMovementDaoImpl.class));
		bf.registerBeanDefinition("bean3", new RootBeanDefinition(StockMovementImpl.class));
		bf.registerBeanDefinition("bean4", new RootBeanDefinition(StockMovementInstructionImpl.class));

		StockServiceImpl service = bf.getBean(StockServiceImpl.class);
		assertSame(bf.getBean(StockMovementDaoImpl.class), service.stockMovementDao);
	}

	@Test
	public void testBridgeMethodHandling() {
		bf.registerBeanDefinition("bean1", new RootBeanDefinition(MyCallable.class));
		bf.registerBeanDefinition("bean2", new RootBeanDefinition(SecondCallable.class));
		bf.registerBeanDefinition("bean3", new RootBeanDefinition(FooBar.class));
		assertNotNull(bf.getBean(FooBar.class));
	}

	@Test
	public void testSingleConstructorWithProvidedArgument() {
		RootBeanDefinition bd = new RootBeanDefinition(ProvidedArgumentBean.class);
		bd.getConstructorArgumentValues().addGenericArgumentValue(Collections.singletonList("value"));
		bf.registerBeanDefinition("beanWithArgs", bd);
		assertNotNull(bf.getBean(ProvidedArgumentBean.class));
	}

	@Test
	public void testAnnotatedDefaultConstructor() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(AnnotatedDefaultConstructorBean.class));

		assertNotNull(bf.getBean("annotatedBean"));
	}

	@Test  // SPR-15125
	public void testFactoryBeanSelfInjection() {
		bf.registerBeanDefinition("annotatedBean", new RootBeanDefinition(SelfInjectingFactoryBean.class));

		SelfInjectingFactoryBean bean = bf.getBean(SelfInjectingFactoryBean.class);
		assertSame(bf.getBean("annotatedBean"), bean.testBean);
	}

	@Test  // SPR-15125
	public void testFactoryBeanSelfInjectionViaFactoryMethod() {
		RootBeanDefinition bd = new RootBeanDefinition(SelfInjectingFactoryBean.class);
		bd.setFactoryMethodName("create");
		bf.registerBeanDefinition("annotatedBean", bd);

		SelfInjectingFactoryBean bean = bf.getBean(SelfInjectingFactoryBean.class);
		assertSame(bf.getBean("annotatedBean"), bean.testBean);
	}


	@Qualifier("testBean")
	private void testBeanQualifierProvider() {}

	@Qualifier("integerRepo")
	private Repository<?> integerRepositoryQualifierProvider;


	public static class ResourceInjectionBean {

		@Autowired(required = false)
		private TestBean testBean;

		private TestBean testBean2;

		@Autowired
		public void setTestBean2(TestBean testBean2) {
			if (this.testBean2 != null) {
				throw new IllegalStateException("Already called");
			}
			this.testBean2 = testBean2;
		}

		public TestBean getTestBean() {
			return this.testBean;
		}

		public TestBean getTestBean2() {
			return this.testBean2;
		}
	}


	static class NonPublicResourceInjectionBean<T> extends ResourceInjectionBean {

		@Autowired
		public final ITestBean testBean3 = null;

		private T nestedTestBean;

		private ITestBean testBean4;

		protected BeanFactory beanFactory;

		public boolean baseInjected = false;

		public NonPublicResourceInjectionBean() {
		}

		@Override
		@Autowired @Required
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		@Autowired
		private void inject(ITestBean testBean4, T nestedTestBean) {
			this.testBean4 = testBean4;
			this.nestedTestBean = nestedTestBean;
		}

		@Autowired
		private void inject(ITestBean testBean4) {
			this.baseInjected = true;
		}

		@Autowired
		protected void initBeanFactory(BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public T getNestedTestBean() {
			return this.nestedTestBean;
		}

		public BeanFactory getBeanFactory() {
			return this.beanFactory;
		}
	}


	public static class TypedExtendedResourceInjectionBean extends NonPublicResourceInjectionBean<NestedTestBean>
			implements DisposableBean {

		public boolean destroyed = false;

		@Override
		public void destroy() {
			this.destroyed = true;
		}
	}


	public static class OverriddenExtendedResourceInjectionBean extends NonPublicResourceInjectionBean<NestedTestBean> {

		public boolean subInjected = false;

		@Override
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		@Override
		protected void initBeanFactory(BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		@Autowired
		private void inject(ITestBean testBean4) {
			this.subInjected = true;
		}
	}


	public interface InterfaceWithDefaultMethod {

		@Autowired
		void setTestBean2(TestBean testBean2);

		@Autowired
		default void injectDefault(ITestBean testBean4) {
			markSubInjected();
		}

		void markSubInjected();
	}


	public static class DefaultMethodResourceInjectionBean extends NonPublicResourceInjectionBean<NestedTestBean>
			implements InterfaceWithDefaultMethod {

		public boolean subInjected = false;

		@Override
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		@Override
		protected void initBeanFactory(BeanFactory beanFactory) {
			this.beanFactory = beanFactory;
		}

		@Override
		public void markSubInjected() {
			subInjected = true;
		}
	}


	public static class OptionalResourceInjectionBean extends ResourceInjectionBean {

		@Autowired(required = false)
		protected ITestBean testBean3;

		private IndexedTestBean indexedTestBean;

		private NestedTestBean[] nestedTestBeans;

		@Autowired(required = false)
		public NestedTestBean[] nestedTestBeansField;

		private ITestBean testBean4;

		@Override
		@Autowired(required = false)
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		@Autowired(required = false)
		private void inject(ITestBean testBean4, NestedTestBean[] nestedTestBeans, IndexedTestBean indexedTestBean) {
			this.testBean4 = testBean4;
			this.indexedTestBean = indexedTestBean;
			this.nestedTestBeans = nestedTestBeans;
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public IndexedTestBean getIndexedTestBean() {
			return this.indexedTestBean;
		}

		public NestedTestBean[] getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class OptionalCollectionResourceInjectionBean extends ResourceInjectionBean {

		@Autowired(required = false)
		protected ITestBean testBean3;

		private IndexedTestBean indexedTestBean;

		private List<NestedTestBean> nestedTestBeans;

		public List<NestedTestBean> nestedTestBeansSetter;

		@Autowired(required = false)
		public List<NestedTestBean> nestedTestBeansField;

		private ITestBean testBean4;

		@Override
		@Autowired(required = false)
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		@Autowired(required = false)
		private void inject(ITestBean testBean4, List<NestedTestBean> nestedTestBeans, IndexedTestBean indexedTestBean) {
			this.testBean4 = testBean4;
			this.indexedTestBean = indexedTestBean;
			this.nestedTestBeans = nestedTestBeans;
		}

		@Autowired(required = false)
		public void setNestedTestBeans(List<NestedTestBean> nestedTestBeans) {
			this.nestedTestBeansSetter = nestedTestBeans;
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public IndexedTestBean getIndexedTestBean() {
			return this.indexedTestBean;
		}

		public List<NestedTestBean> getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class ConstructorResourceInjectionBean extends ResourceInjectionBean {

		@Autowired(required = false)
		protected ITestBean testBean3;

		private ITestBean testBean4;

		private NestedTestBean nestedTestBean;

		private ConfigurableListableBeanFactory beanFactory;

		public ConstructorResourceInjectionBean() {
			throw new UnsupportedOperationException();
		}

		public ConstructorResourceInjectionBean(ITestBean testBean3) {
			throw new UnsupportedOperationException();
		}

		@Autowired
		public ConstructorResourceInjectionBean(@Autowired(required = false) ITestBean testBean4,
				@Autowired(required = false) NestedTestBean nestedTestBean,
				ConfigurableListableBeanFactory beanFactory) {
			this.testBean4 = testBean4;
			this.nestedTestBean = nestedTestBean;
			this.beanFactory = beanFactory;
		}

		public ConstructorResourceInjectionBean(NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		public ConstructorResourceInjectionBean(ITestBean testBean3, ITestBean testBean4, NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		@Override
		@Autowired(required = false)
		public void setTestBean2(TestBean testBean2) {
			super.setTestBean2(testBean2);
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public NestedTestBean getNestedTestBean() {
			return this.nestedTestBean;
		}

		public ConfigurableListableBeanFactory getBeanFactory() {
			return this.beanFactory;
		}
	}


	public static class ConstructorsResourceInjectionBean {

		protected ITestBean testBean3;

		private ITestBean testBean4;

		private NestedTestBean[] nestedTestBeans;

		public ConstructorsResourceInjectionBean() {
		}

		@Autowired(required = false)
		public ConstructorsResourceInjectionBean(ITestBean testBean3) {
			this.testBean3 = testBean3;
		}

		@Autowired(required = false)
		public ConstructorsResourceInjectionBean(ITestBean testBean4, NestedTestBean[] nestedTestBeans) {
			this.testBean4 = testBean4;
			this.nestedTestBeans = nestedTestBeans;
		}

		public ConstructorsResourceInjectionBean(NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		public ConstructorsResourceInjectionBean(ITestBean testBean3, ITestBean testBean4, NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public NestedTestBean[] getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class ConstructorWithoutFallbackBean {

		protected ITestBean testBean3;

		@Autowired(required = false)
		public ConstructorWithoutFallbackBean(ITestBean testBean3) {
			this.testBean3 = testBean3;
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}
	}


	public static class ConstructorsCollectionResourceInjectionBean {

		protected ITestBean testBean3;

		private ITestBean testBean4;

		private List<NestedTestBean> nestedTestBeans;

		public ConstructorsCollectionResourceInjectionBean() {
		}

		@Autowired(required = false)
		public ConstructorsCollectionResourceInjectionBean(ITestBean testBean3) {
			this.testBean3 = testBean3;
		}

		@Autowired(required = false)
		public ConstructorsCollectionResourceInjectionBean(ITestBean testBean4, List<NestedTestBean> nestedTestBeans) {
			this.testBean4 = testBean4;
			this.nestedTestBeans = nestedTestBeans;
		}

		public ConstructorsCollectionResourceInjectionBean(NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		public ConstructorsCollectionResourceInjectionBean(ITestBean testBean3, ITestBean testBean4,
				NestedTestBean nestedTestBean) {
			throw new UnsupportedOperationException();
		}

		public ITestBean getTestBean3() {
			return this.testBean3;
		}

		public ITestBean getTestBean4() {
			return this.testBean4;
		}

		public List<NestedTestBean> getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class SingleConstructorVarargBean {

		private ITestBean testBean;

		private List<NestedTestBean> nestedTestBeans;

		public SingleConstructorVarargBean(ITestBean testBean, NestedTestBean... nestedTestBeans) {
			this.testBean = testBean;
			this.nestedTestBeans = Arrays.asList(nestedTestBeans);
		}

		public ITestBean getTestBean() {
			return this.testBean;
		}

		public List<NestedTestBean> getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class SingleConstructorRequiredCollectionBean {

		private ITestBean testBean;

		private List<NestedTestBean> nestedTestBeans;

		public SingleConstructorRequiredCollectionBean(ITestBean testBean, List<NestedTestBean> nestedTestBeans) {
			this.testBean = testBean;
			this.nestedTestBeans = nestedTestBeans;
		}

		public ITestBean getTestBean() {
			return this.testBean;
		}

		public List<NestedTestBean> getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	public static class SingleConstructorOptionalCollectionBean {

		private ITestBean testBean;

		private List<NestedTestBean> nestedTestBeans;

		public SingleConstructorOptionalCollectionBean(ITestBean testBean,
				@Autowired(required = false) List<NestedTestBean> nestedTestBeans) {
			this.testBean = testBean;
			this.nestedTestBeans = nestedTestBeans;
		}

		public ITestBean getTestBean() {
			return this.testBean;
		}

		public List<NestedTestBean> getNestedTestBeans() {
			return this.nestedTestBeans;
		}
	}


	@SuppressWarnings("serial")
	public static class MyTestBeanMap extends LinkedHashMap<String, TestBean> {
	}


	@SuppressWarnings("serial")
	public static class MyTestBeanSet extends LinkedHashSet<TestBean> {
	}


	public static class MapConstructorInjectionBean {

		private Map<String, TestBean> testBeanMap;

		@Autowired
		public MapConstructorInjectionBean(Map<String, TestBean> testBeanMap) {
			this.testBeanMap = testBeanMap;
		}

		public Map<String, TestBean> getTestBeanMap() {
			return this.testBeanMap;
		}
	}


	public static class QualifiedMapConstructorInjectionBean {

		private Map<String, TestBean> testBeanMap;

		@Autowired
		public QualifiedMapConstructorInjectionBean(@Qualifier("myTestBeanMap") Map<String, TestBean> testBeanMap) {
			this.testBeanMap = testBeanMap;
		}

		public Map<String, TestBean> getTestBeanMap() {
			return this.testBeanMap;
		}
	}


	public static class SetConstructorInjectionBean {

		private Set<TestBean> testBeanSet;

		@Autowired
		public SetConstructorInjectionBean(Set<TestBean> testBeanSet) {
			this.testBeanSet = testBeanSet;
		}

		public Set<TestBean> getTestBeanSet() {
			return this.testBeanSet;
		}
	}


	public static class SelfInjectionBean {

		@Autowired
		public SelfInjectionBean reference;

		@Autowired(required = false)
		public List<SelfInjectionBean> referenceCollection;
	}


	@SuppressWarnings("serial")
	public static class SelfInjectionCollectionBean extends LinkedList<SelfInjectionCollectionBean> {

		@Autowired
		public SelfInjectionCollectionBean reference;

		@Autowired(required = false)
		public List<SelfInjectionCollectionBean> referenceCollection;
	}


	public static class MapFieldInjectionBean {

		@Autowired
		private Map<String, TestBean> testBeanMap;

		public Map<String, TestBean> getTestBeanMap() {
			return this.testBeanMap;
		}
	}


	public static class MapMethodInjectionBean {

		private TestBean testBean;

		private Map<String, TestBean> testBeanMap;

		@Autowired(required = false)
		public void setTestBeanMap(TestBean testBean, Map<String, TestBean> testBeanMap) {
			this.testBean = testBean;
			this.testBeanMap = testBeanMap;
		}

		public TestBean getTestBean() {
			return this.testBean;
		}

		public Map<String, TestBean> getTestBeanMap() {
			return this.testBeanMap;
		}
	}


	@SuppressWarnings("serial")
	public static class ObjectFactoryFieldInjectionBean implements Serializable {

		@Autowired
		private ObjectFactory<TestBean> testBeanFactory;

		public TestBean getTestBean() {
			return this.testBeanFactory.getObject();
		}
	}


	@SuppressWarnings("serial")
	public static class ObjectFactoryConstructorInjectionBean implements Serializable {

		private final ObjectFactory<TestBean> testBeanFactory;

		public ObjectFactoryConstructorInjectionBean(ObjectFactory<TestBean> testBeanFactory) {
			this.testBeanFactory = testBeanFactory;
		}

		public TestBean getTestBean() {
			return this.testBeanFactory.getObject();
		}
	}


	public static class ObjectFactoryQualifierInjectionBean {

		@Autowired
		@Qualifier("testBean")
		private ObjectFactory<?> testBeanFactory;

		public TestBean getTestBean() {
			return (TestBean) this.testBeanFactory.getObject();
		}
	}


	public static class ObjectProviderInjectionBean {

		@Autowired
		private ObjectProvider<TestBean> testBeanProvider;

		private TestBean consumedTestBean;

		public TestBean getTestBean() {
			return this.testBeanProvider.getObject();
		}

		public TestBean getTestBean(String name) {
			return this.testBeanProvider.getObject(name);
		}

		public TestBean getOptionalTestBean() {
			return this.testBeanProvider.getIfAvailable();
		}

		public TestBean getOptionalTestBeanWithDefault() {
			return this.testBeanProvider.getIfAvailable(() -> new TestBean("default"));
		}

		public TestBean consumeOptionalTestBean() {
			this.testBeanProvider.ifAvailable(tb -> consumedTestBean = tb);
			return consumedTestBean;
		}

		public TestBean getUniqueTestBean() {
			return this.testBeanProvider.getIfUnique();
		}

		public TestBean getUniqueTestBeanWithDefault() {
			return this.testBeanProvider.getIfUnique(() -> new TestBean("default"));
		}

		public TestBean consumeUniqueTestBean() {
			this.testBeanProvider.ifUnique(tb -> consumedTestBean = tb);
			return consumedTestBean;
		}

		public List<TestBean> iterateTestBeans() {
			List<TestBean> resolved = new LinkedList<>();
			for (TestBean tb : this.testBeanProvider) {
				resolved.add(tb);
			}
			return resolved;
		}

		public List<TestBean> forEachTestBeans() {
			List<TestBean> resolved = new LinkedList<>();
			this.testBeanProvider.forEach(resolved::add);
			return resolved;
		}

		public List<TestBean> streamTestBeans() {
			return this.testBeanProvider.stream().collect(Collectors.toList());
		}

		public List<TestBean> sortedTestBeans() {
			return this.testBeanProvider.orderedStream().collect(Collectors.toList());
		}
	}


	public static class CustomAnnotationRequiredFieldResourceInjectionBean {

		@MyAutowired(optional = false)
		private TestBean testBean;

		public TestBean getTestBean() {
			return this.testBean;
		}
	}


	public static class CustomAnnotationRequiredMethodResourceInjectionBean {

		private TestBean testBean;

		@MyAutowired(optional = false)
		public void setTestBean(TestBean testBean) {
			this.testBean = testBean;
		}

		public TestBean getTestBean() {
			return this.testBean;
		}
	}


	public static class CustomAnnotationOptionalFieldResourceInjectionBean extends ResourceInjectionBean {

		@MyAutowired(optional = true)
		private TestBean testBean3;

		public TestBean getTestBean3() {
			return this.testBean3;
		}
	}


	public static class CustomAnnotationOptionalMethodResourceInjectionBean extends ResourceInjectionBean {

		private TestBean testBean3;

		@MyAutowired(optional = true)
		protected void setTestBean3(TestBean testBean3) {
			this.testBean3 = testBean3;
		}

		public TestBean getTestBean3() {
			return this.testBean3;
		}
	}


	@Target({ElementType.METHOD, ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MyAutowired {

		boolean optional() default false;
	}


	/**
	 * Bean with a dependency on a {@link FactoryBean}.
	 */
	private static class FactoryBeanDependentBean {

		@Autowired
		private FactoryBean<?> factoryBean;

		public final FactoryBean<?> getFactoryBean() {
			return this.factoryBean;
		}
	}


	public static class StringFactoryBean implements FactoryBean<String> {

		@Override
		public String getObject() throws Exception {
			return "";
		}

		@Override
		public Class<String> getObjectType() {
			return String.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}
	}


	public static class OrderedNestedTestBean extends NestedTestBean implements Ordered {

		private int order;

		public void setOrder(int order) {
			this.order = order;
		}

		@Override
		public int getOrder() {
			return this.order;
		}
	}


	@Order(1)
	public static class FixedOrder1NestedTestBean extends NestedTestBean {
	}

	@Order(2)
	public static class FixedOrder2NestedTestBean extends NestedTestBean {
	}


	public interface Repository<T> {
	}

	public static class StringRepository implements Repository<String> {
	}

	public static class IntegerRepository implements Repository<Integer> {
	}

	public static class GenericRepository<T> implements Repository<T> {
	}

	@SuppressWarnings("rawtypes")
	public static class GenericRepositorySubclass extends GenericRepository {
	}

	@SuppressWarnings("rawtypes")
	public static class SimpleRepository implements Repository {
	}

	public static class SimpleRepositorySubclass extends SimpleRepository {
	}


	public static class RepositoryFactoryBean<T> implements FactoryBean<T> {

		@Override
		public T getObject() {
			throw new IllegalStateException();
		}

		@Override
		public Class<?> getObjectType() {
			return Object.class;
		}

		@Override
		public boolean isSingleton() {
			return false;
		}
	}


	public static class RepositoryFieldInjectionBean {

		@Autowired
		public String string;

		@Autowired
		public Integer integer;

		@Autowired
		public String[] stringArray;

		@Autowired
		public Integer[] integerArray;

		@Autowired
		public List<String> stringList;

		@Autowired
		public List<Integer> integerList;

		@Autowired
		public Map<String, String> stringMap;

		@Autowired
		public Map<String, Integer> integerMap;

		@Autowired
		public Repository<String> stringRepository;

		@Autowired
		public Repository<Integer> integerRepository;

		@Autowired
		public Repository<String>[] stringRepositoryArray;

		@Autowired
		public Repository<Integer>[] integerRepositoryArray;

		@Autowired
		public List<Repository<String>> stringRepositoryList;

		@Autowired
		public List<Repository<Integer>> integerRepositoryList;

		@Autowired
		public Map<String, Repository<String>> stringRepositoryMap;

		@Autowired
		public Map<String, Repository<Integer>> integerRepositoryMap;
	}


	public static class RepositoryFieldInjectionBeanWithVariables<S, I> {

		@Autowired
		public S string;

		@Autowired
		public I integer;

		@Autowired
		public S[] stringArray;

		@Autowired
		public I[] integerArray;

		@Autowired
		public List<S> stringList;

		@Autowired
		public List<I> integerList;

		@Autowired
		public Map<String, S> stringMap;

		@Autowired
		public Map<String, I> integerMap;

		@Autowired
		public Repository<S> stringRepository;

		@Autowired
		public Repository<I> integerRepository;

		@Autowired
		public Repository<S>[] stringRepositoryArray;

		@Autowired
		public Repository<I>[] integerRepositoryArray;

		@Autowired
		public List<Repository<S>> stringRepositoryList;

		@Autowired
		public List<Repository<I>> integerRepositoryList;

		@Autowired
		public Map<String, Repository<S>> stringRepositoryMap;

		@Autowired
		public Map<String, Repository<I>> integerRepositoryMap;
	}


	public static class RepositoryFieldInjectionBeanWithSubstitutedVariables
			extends RepositoryFieldInjectionBeanWithVariables<String, Integer> {
	}


	public static class RepositoryFieldInjectionBeanWithQualifiers {

		@Autowired @Qualifier("stringRepo")
		public Repository<?> stringRepository;

		@Autowired @Qualifier("integerRepo")
		public Repository<?> integerRepository;

		@Autowired @Qualifier("stringRepo")
		public Repository<?>[] stringRepositoryArray;

		@Autowired @Qualifier("integerRepo")
		public Repository<?>[] integerRepositoryArray;

		@Autowired @Qualifier("stringRepo")
		public List<Repository<?>> stringRepositoryList;

		@Autowired @Qualifier("integerRepo")
		public List<Repository<?>> integerRepositoryList;

		@Autowired @Qualifier("stringRepo")
		public Map<String, Repository<?>> stringRepositoryMap;

		@Autowired @Qualifier("integerRepo")
		public Map<String, Repository<?>> integerRepositoryMap;
	}


	public static class RepositoryFieldInjectionBeanWithSimpleMatch {

		@Autowired
		public Repository<?> repository;

		@Autowired
		public Repository<String> stringRepository;

		@Autowired
		public Repository<?>[] repositoryArray;

		@Autowired
		public Repository<String>[] stringRepositoryArray;

		@Autowired
		public List<Repository<?>> repositoryList;

		@Autowired
		public List<Repository<String>> stringRepositoryList;

		@Autowired
		public Map<String, Repository<?>> repositoryMap;

		@Autowired
		public Map<String, Repository<String>> stringRepositoryMap;
	}


	public static class RepositoryFactoryBeanInjectionBean {

		@Autowired
		public RepositoryFactoryBean<?> repositoryFactoryBean;
	}


	public static class RepositoryMethodInjectionBean {

		public String string;

		public Integer integer;

		public String[] stringArray;

		public Integer[] integerArray;

		public List<String> stringList;

		public List<Integer> integerList;

		public Map<String, String> stringMap;

		public Map<String, Integer> integerMap;

		public Repository<String> stringRepository;

		public Repository<Integer> integerRepository;

		public Repository<String>[] stringRepositoryArray;

		public Repository<Integer>[] integerRepositoryArray;

		public List<Repository<String>> stringRepositoryList;

		public List<Repository<Integer>> integerRepositoryList;

		public Map<String, Repository<String>> stringRepositoryMap;

		public Map<String, Repository<Integer>> integerRepositoryMap;

		@Autowired
		public void setString(String string) {
			this.string = string;
		}

		@Autowired
		public void setInteger(Integer integer) {
			this.integer = integer;
		}

		@Autowired
		public void setStringArray(String[] stringArray) {
			this.stringArray = stringArray;
		}

		@Autowired
		public void setIntegerArray(Integer[] integerArray) {
			this.integerArray = integerArray;
		}

		@Autowired
		public void setStringList(List<String> stringList) {
			this.stringList = stringList;
		}

		@Autowired
		public void setIntegerList(List<Integer> integerList) {
			this.integerList = integerList;
		}

		@Autowired
		public void setStringMap(Map<String, String> stringMap) {
			this.stringMap = stringMap;
		}

		@Autowired
		public void setIntegerMap(Map<String, Integer> integerMap) {
			this.integerMap = integerMap;
		}

		@Autowired
		public void setStringRepository(Repository<String> stringRepository) {
			this.stringRepository = stringRepository;
		}

		@Autowired
		public void setIntegerRepository(Repository<Integer> integerRepository) {
			this.integerRepository = integerRepository;
		}

		@Autowired
		public void setStringRepositoryArray(Repository<String>[] stringRepositoryArray) {
			this.stringRepositoryArray = stringRepositoryArray;
		}

		@Autowired
		public void setIntegerRepositoryArray(Repository<Integer>[] integerRepositoryArray) {
			this.integerRepositoryArray = integerRepositoryArray;
		}

		@Autowired
		public void setStringRepositoryList(List<Repository<String>> stringRepositoryList) {
			this.stringRepositoryList = stringRepositoryList;
		}

		@Autowired
		public void setIntegerRepositoryList(List<Repository<Integer>> integerRepositoryList) {
			this.integerRepositoryList = integerRepositoryList;
		}

		@Autowired
		public void setStringRepositoryMap(Map<String, Repository<String>> stringRepositoryMap) {
			this.stringRepositoryMap = stringRepositoryMap;
		}

		@Autowired
		public void setIntegerRepositoryMap(Map<String, Repository<Integer>> integerRepositoryMap) {
			this.integerRepositoryMap = integerRepositoryMap;
		}
	}


	public static class RepositoryMethodInjectionBeanWithVariables<S, I> {

		public S string;

		public I integer;

		public S[] stringArray;

		public I[] integerArray;

		public List<S> stringList;

		public List<I> integerList;

		public Map<String, S> stringMap;

		public Map<String, I> integerMap;

		public Repository<S> stringRepository;

		public Repository<I> integerRepository;

		public Repository<S>[] stringRepositoryArray;

		public Repository<I>[] integerRepositoryArray;

		public List<Repository<S>> stringRepositoryList;

		public List<Repository<I>> integerRepositoryList;

		public Map<String, Repository<S>> stringRepositoryMap;

		public Map<String, Repository<I>> integerRepositoryMap;

		@Autowired
		public void setString(S string) {
			this.string = string;
		}

		@Autowired
		public void setInteger(I integer) {
			this.integer = integer;
		}

		@Autowired
		public void setStringArray(S[] stringArray) {
			this.stringArray = stringArray;
		}

		@Autowired
		public void setIntegerArray(I[] integerArray) {
			this.integerArray = integerArray;
		}

		@Autowired
		public void setStringList(List<S> stringList) {
			this.stringList = stringList;
		}

		@Autowired
		public void setIntegerList(List<I> integerList) {
			this.integerList = integerList;
		}

		@Autowired
		public void setStringMap(Map<String, S> stringMap) {
			this.stringMap = stringMap;
		}

		@Autowired
		public void setIntegerMap(Map<String, I> integerMap) {
			this.integerMap = integerMap;
		}

		@Autowired
		public void setStringRepository(Repository<S> stringRepository) {
			this.stringRepository = stringRepository;
		}

		@Autowired
		public void setIntegerRepository(Repository<I> integerRepository) {
			this.integerRepository = integerRepository;
		}

		@Autowired
		public void setStringRepositoryArray(Repository<S>[] stringRepositoryArray) {
			this.stringRepositoryArray = stringRepositoryArray;
		}

		@Autowired
		public void setIntegerRepositoryArray(Repository<I>[] integerRepositoryArray) {
			this.integerRepositoryArray = integerRepositoryArray;
		}

		@Autowired
		public void setStringRepositoryList(List<Repository<S>> stringRepositoryList) {
			this.stringRepositoryList = stringRepositoryList;
		}

		@Autowired
		public void setIntegerRepositoryList(List<Repository<I>> integerRepositoryList) {
			this.integerRepositoryList = integerRepositoryList;
		}

		@Autowired
		public void setStringRepositoryMap(Map<String, Repository<S>> stringRepositoryMap) {
			this.stringRepositoryMap = stringRepositoryMap;
		}

		@Autowired
		public void setIntegerRepositoryMap(Map<String, Repository<I>> integerRepositoryMap) {
			this.integerRepositoryMap = integerRepositoryMap;
		}
	}


	public static class RepositoryMethodInjectionBeanWithSubstitutedVariables
			extends RepositoryMethodInjectionBeanWithVariables<String, Integer> {
	}


	public static class RepositoryConstructorInjectionBean {

		public Repository<String> stringRepository;

		public Repository<Integer> integerRepository;

		public Repository<String>[] stringRepositoryArray;

		public Repository<Integer>[] integerRepositoryArray;

		public List<Repository<String>> stringRepositoryList;

		public List<Repository<Integer>> integerRepositoryList;

		public Map<String, Repository<String>> stringRepositoryMap;

		public Map<String, Repository<Integer>> integerRepositoryMap;

		@Autowired
		public RepositoryConstructorInjectionBean(Repository<String> stringRepository, Repository<Integer> integerRepository,
				Repository<String>[] stringRepositoryArray, Repository<Integer>[] integerRepositoryArray,
				List<Repository<String>> stringRepositoryList, List<Repository<Integer>> integerRepositoryList,
				Map<String, Repository<String>> stringRepositoryMap, Map<String, Repository<Integer>> integerRepositoryMap) {
			this.stringRepository = stringRepository;
			this.integerRepository = integerRepository;
			this.stringRepositoryArray = stringRepositoryArray;
			this.integerRepositoryArray = integerRepositoryArray;
			this.stringRepositoryList = stringRepositoryList;
			this.integerRepositoryList = integerRepositoryList;
			this.stringRepositoryMap = stringRepositoryMap;
			this.integerRepositoryMap = integerRepositoryMap;
		}
	}


	/**
	 * Pseudo-implementation of EasyMock's {@code MocksControl} class.
	 */
	public static class MocksControl {

		@SuppressWarnings("unchecked")
		public <T> T createMock(Class<T> toMock) {
			return (T) Proxy.newProxyInstance(AutowiredAnnotationBeanPostProcessorTests.class.getClassLoader(), new Class<?>[] {toMock},
					new InvocationHandler() {
						@Override
						public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
							throw new UnsupportedOperationException("mocked!");
						}
					});
		}
	}


	public interface GenericInterface1<T> {

		String doSomethingGeneric(T o);
	}


	public static class GenericInterface1Impl<T> implements GenericInterface1<T> {

		@Autowired
		private GenericInterface2<T> gi2;

		@Override
		public String doSomethingGeneric(T o) {
			return gi2.doSomethingMoreGeneric(o) + "_somethingGeneric_" + o;
		}

		public static GenericInterface1<String> create() {
			return new StringGenericInterface1Impl();
		}

		public static GenericInterface1<String> createErased() {
			return new GenericInterface1Impl<>();
		}

		@SuppressWarnings("rawtypes")
		public static GenericInterface1 createPlain() {
			return new GenericInterface1Impl();
		}
	}


	public static class StringGenericInterface1Impl extends GenericInterface1Impl<String> {
	}


	public interface GenericInterface2<K> {

		String doSomethingMoreGeneric(K o);
	}


	public static class GenericInterface2Impl implements GenericInterface2<String> {

		@Override
		public String doSomethingMoreGeneric(String o) {
			return "somethingMoreGeneric_" + o;
		}
	}


	public static class ReallyGenericInterface2Impl implements GenericInterface2<Object> {

		@Override
		public String doSomethingMoreGeneric(Object o) {
			return "somethingMoreGeneric_" + o;
		}
	}


	public static class GenericInterface2Bean<K> implements GenericInterface2<K>, BeanNameAware {

		private String name;

		@Override
		public void setBeanName(String name) {
			this.name = name;
		}

		@Override
		public String doSomethingMoreGeneric(K o) {
			return this.name + " " + o;
		}
	}


	public static class MultiGenericFieldInjection {

		@Autowired
		private GenericInterface2<String> stringBean;

		@Autowired
		private GenericInterface2<Integer> integerBean;

		@Override
		public String toString() {
			return this.stringBean.doSomethingMoreGeneric("a") + " " + this.integerBean.doSomethingMoreGeneric(123);
		}
	}


	@SuppressWarnings("rawtypes")
	public static class PlainGenericInterface2Impl implements GenericInterface2 {

		@Override
		public String doSomethingMoreGeneric(Object o) {
			return "somethingMoreGeneric_" + o;
		}
	}


	@SuppressWarnings("rawtypes")
	public interface StockMovement<P extends StockMovementInstruction> {
	}


	@SuppressWarnings("rawtypes")
	public interface StockMovementInstruction<C extends StockMovement> {
	}


	@SuppressWarnings("rawtypes")
	public interface StockMovementDao<S extends StockMovement> {
	}


	@SuppressWarnings("rawtypes")
	public static class StockMovementImpl<P extends StockMovementInstruction> implements StockMovement<P> {
	}


	@SuppressWarnings("rawtypes")
	public static class StockMovementInstructionImpl<C extends StockMovement> implements StockMovementInstruction<C> {
	}


	@SuppressWarnings("rawtypes")
	public static class StockMovementDaoImpl<E extends StockMovement> implements StockMovementDao<E> {
	}


	public static class StockServiceImpl {

		@Autowired
		@SuppressWarnings("rawtypes")
		private StockMovementDao<StockMovement> stockMovementDao;
	}


	public static class MyCallable implements Callable<Thread> {

		@Override
		public Thread call() throws Exception {
			return null;
		}
	}


	public static class SecondCallable implements Callable<Thread>{

		@Override
		public Thread call() throws Exception {
			return null;
		}
	}


	public static abstract class Foo<T extends Runnable, RT extends Callable<T>> {

		private RT obj;

		protected void setObj(RT obj) {
			if (this.obj != null) {
				throw new IllegalStateException("Already called");
			}
			this.obj = obj;
		}
	}


	public static class FooBar extends Foo<Thread, MyCallable> {

		@Override
		@Autowired
		public void setObj(MyCallable obj) {
			super.setObj(obj);
		}
	}


	public static class NullNestedTestBeanFactoryBean implements FactoryBean<NestedTestBean> {

		@Override
		public NestedTestBean getObject() {
			return null;
		}

		@Override
		public Class<?> getObjectType() {
			return NestedTestBean.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}
	}


	public static class NullFactoryMethods {

		public static TestBean createTestBean() {
			return null;
		}

		public static NestedTestBean createNestedTestBean() {
			return null;
		}
	}


	public static class ProvidedArgumentBean {

		public ProvidedArgumentBean(String[] args) {
		}
	}


	public static class CollectionFactoryMethods {

		public static Map<String, TestBean> testBeanMap() {
			Map<String, TestBean> tbm = new LinkedHashMap<>();
			tbm.put("testBean1", new TestBean("tb1"));
			tbm.put("testBean2", new TestBean("tb2"));
			return tbm;
		}

		public static Set<TestBean> testBeanSet() {
			Set<TestBean> tbs = new LinkedHashSet<>();
			tbs.add(new TestBean("tb1"));
			tbs.add(new TestBean("tb2"));
			return tbs;
		}
	}


	public static class CustomCollectionFactoryMethods {

		public static CustomMap<String, TestBean> testBeanMap() {
			CustomMap<String, TestBean> tbm = new CustomHashMap<>();
			tbm.put("testBean1", new TestBean("tb1"));
			tbm.put("testBean2", new TestBean("tb2"));
			return tbm;
		}

		public static CustomSet<TestBean> testBeanSet() {
			CustomSet<TestBean> tbs = new CustomHashSet<>();
			tbs.add(new TestBean("tb1"));
			tbs.add(new TestBean("tb2"));
			return tbs;
		}
	}


	public static class CustomMapConstructorInjectionBean {

		private CustomMap<String, TestBean> testBeanMap;

		@Autowired
		public CustomMapConstructorInjectionBean(CustomMap<String, TestBean> testBeanMap) {
			this.testBeanMap = testBeanMap;
		}

		public CustomMap<String, TestBean> getTestBeanMap() {
			return this.testBeanMap;
		}
	}


	public static class CustomSetConstructorInjectionBean {

		private CustomSet<TestBean> testBeanSet;

		@Autowired
		public CustomSetConstructorInjectionBean(CustomSet<TestBean> testBeanSet) {
			this.testBeanSet = testBeanSet;
		}

		public CustomSet<TestBean> getTestBeanSet() {
			return this.testBeanSet;
		}
	}


	public interface CustomMap<K, V> extends Map<K, V> {
	}


	@SuppressWarnings("serial")
	public static class CustomHashMap<K, V> extends LinkedHashMap<K, V> implements CustomMap<K, V> {
	}


	public interface CustomSet<E> extends Set<E> {
	}


	@SuppressWarnings("serial")
	public static class CustomHashSet<E> extends LinkedHashSet<E> implements CustomSet<E> {
	}


	public static class AnnotatedDefaultConstructorBean {

		@Autowired
		public AnnotatedDefaultConstructorBean() {
		}
	}


	public static class SelfInjectingFactoryBean implements FactoryBean<TestBean> {

		private final TestBean exposedTestBean = new TestBean();

		@Autowired
		TestBean testBean;

		@Override
		public TestBean getObject() {
			return exposedTestBean;
		}

		@Override
		public Class<?> getObjectType() {
			return TestBean.class;
		}

		@Override
		public boolean isSingleton() {
			return true;
		}

		public static SelfInjectingFactoryBean create() {
			return new SelfInjectingFactoryBean();
		}
	}


	public static class TestBeanFactory {

		@Order(1)
		public static TestBean newTestBean1() {
			return new TestBean();
		}

		@Order(0)
		public static TestBean newTestBean2() {
			return new TestBean();
		}
	}

}
