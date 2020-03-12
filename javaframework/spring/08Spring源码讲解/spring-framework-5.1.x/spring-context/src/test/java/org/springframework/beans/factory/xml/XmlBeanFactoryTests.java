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

package org.springframework.beans.factory.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.xml.sax.InputSource;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.MethodReplacer;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.tests.sample.beans.DependenciesBean;
import org.springframework.tests.sample.beans.DerivedTestBean;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.IndexedTestBean;
import org.springframework.tests.sample.beans.ResourceTestBean;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.tests.sample.beans.factory.DummyFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.SerializationTestUtils;
import org.springframework.util.StopWatch;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Miscellaneous tests for XML bean definitions.
 *
 * @author Juergen Hoeller
 * @author Rod Johnson
 * @author Rick Evans
 * @author Chris Beams
 * @author Sam Brannen
 */
public class XmlBeanFactoryTests {

	private static final Class<?> CLASS = XmlBeanFactoryTests.class;
	private static final String CLASSNAME = CLASS.getSimpleName();

	private static final ClassPathResource AUTOWIRE_CONTEXT = classPathResource("-autowire.xml");
	private static final ClassPathResource CHILD_CONTEXT = classPathResource("-child.xml");
	private static final ClassPathResource CLASS_NOT_FOUND_CONTEXT = classPathResource("-classNotFound.xml");
	private static final ClassPathResource COMPLEX_FACTORY_CIRCLE_CONTEXT = classPathResource("-complexFactoryCircle.xml");
	private static final ClassPathResource CONSTRUCTOR_ARG_CONTEXT = classPathResource("-constructorArg.xml");
	private static final ClassPathResource CONSTRUCTOR_OVERRIDES_CONTEXT = classPathResource("-constructorOverrides.xml");
	private static final ClassPathResource DELEGATION_OVERRIDES_CONTEXT = classPathResource("-delegationOverrides.xml");
	private static final ClassPathResource DEP_CARG_AUTOWIRE_CONTEXT = classPathResource("-depCargAutowire.xml");
	private static final ClassPathResource DEP_CARG_INNER_CONTEXT = classPathResource("-depCargInner.xml");
	private static final ClassPathResource DEP_CARG_CONTEXT = classPathResource("-depCarg.xml");
	private static final ClassPathResource DEP_DEPENDSON_INNER_CONTEXT = classPathResource("-depDependsOnInner.xml");
	private static final ClassPathResource DEP_DEPENDSON_CONTEXT = classPathResource("-depDependsOn.xml");
	private static final ClassPathResource DEP_PROP = classPathResource("-depProp.xml");
	private static final ClassPathResource DEP_PROP_ABN_CONTEXT = classPathResource("-depPropAutowireByName.xml");
	private static final ClassPathResource DEP_PROP_ABT_CONTEXT = classPathResource("-depPropAutowireByType.xml");
	private static final ClassPathResource DEP_PROP_MIDDLE_CONTEXT = classPathResource("-depPropInTheMiddle.xml");
	private static final ClassPathResource DEP_PROP_INNER_CONTEXT = classPathResource("-depPropInner.xml");
	private static final ClassPathResource DEP_MATERIALIZE_CONTEXT = classPathResource("-depMaterializeThis.xml");
	private static final ClassPathResource FACTORY_CIRCLE_CONTEXT = classPathResource("-factoryCircle.xml");
	private static final ClassPathResource INITIALIZERS_CONTEXT = classPathResource("-initializers.xml");
	private static final ClassPathResource INVALID_CONTEXT = classPathResource("-invalid.xml");
	private static final ClassPathResource INVALID_NO_SUCH_METHOD_CONTEXT = classPathResource("-invalidOverridesNoSuchMethod.xml");
	private static final ClassPathResource COLLECTIONS_XSD_CONTEXT = classPathResource("-localCollectionsUsingXsd.xml");
	private static final ClassPathResource MISSING_CONTEXT = classPathResource("-missing.xml");
	private static final ClassPathResource OVERRIDES_CONTEXT = classPathResource("-overrides.xml");
	private static final ClassPathResource PARENT_CONTEXT = classPathResource("-parent.xml");
	private static final ClassPathResource NO_SUCH_FACTORY_METHOD_CONTEXT = classPathResource("-noSuchFactoryMethod.xml");
	private static final ClassPathResource RECURSIVE_IMPORT_CONTEXT = classPathResource("-recursiveImport.xml");
	private static final ClassPathResource RESOURCE_CONTEXT = classPathResource("-resource.xml");
	private static final ClassPathResource TEST_WITH_DUP_NAMES_CONTEXT = classPathResource("-testWithDuplicateNames.xml");
	private static final ClassPathResource TEST_WITH_DUP_NAME_IN_ALIAS_CONTEXT = classPathResource("-testWithDuplicateNameInAlias.xml");
	private static final ClassPathResource REFTYPES_CONTEXT = classPathResource("-reftypes.xml");
	private static final ClassPathResource DEFAULT_LAZY_CONTEXT = classPathResource("-defaultLazyInit.xml");
	private static final ClassPathResource DEFAULT_AUTOWIRE_CONTEXT = classPathResource("-defaultAutowire.xml");

	private static ClassPathResource classPathResource(String suffix) {
		return new ClassPathResource(CLASSNAME + suffix, CLASS);
	}


	@Test  // SPR-2368
	public void testCollectionsReferredToAsRefLocals() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(factory).loadBeanDefinitions(COLLECTIONS_XSD_CONTEXT);
		factory.preInstantiateSingletons();
	}

	@Test
	public void testRefToSeparatePrototypeInstances() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);

		TestBean emma = (TestBean) xbf.getBean("emma");
		TestBean georgia = (TestBean) xbf.getBean("georgia");
		ITestBean emmasJenks = emma.getSpouse();
		ITestBean georgiasJenks = georgia.getSpouse();
		assertTrue("Emma and georgia think they have a different boyfriend", emmasJenks != georgiasJenks);
		assertTrue("Emmas jenks has right name", emmasJenks.getName().equals("Andrew"));
		assertTrue("Emmas doesn't equal new ref", emmasJenks != xbf.getBean("jenks"));
		assertTrue("Georgias jenks has right name", emmasJenks.getName().equals("Andrew"));
		assertTrue("They are object equal", emmasJenks.equals(georgiasJenks));
		assertTrue("They object equal direct ref", emmasJenks.equals(xbf.getBean("jenks")));
	}

	@Test
	public void testRefToSingleton() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(new EncodedResource(REFTYPES_CONTEXT, "ISO-8859-1"));

		TestBean jen = (TestBean) xbf.getBean("jenny");
		TestBean dave = (TestBean) xbf.getBean("david");
		TestBean jenks = (TestBean) xbf.getBean("jenks");
		ITestBean davesJen = dave.getSpouse();
		ITestBean jenksJen = jenks.getSpouse();
		assertTrue("1 jen instance", davesJen == jenksJen);
		assertTrue("1 jen instance", davesJen == jen);
	}

	@Test
	public void testInnerBeans() throws IOException {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);

		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		InputStream inputStream = getClass().getResourceAsStream(REFTYPES_CONTEXT.getPath());
		try {
			reader.loadBeanDefinitions(new InputSource(inputStream));
		}
		finally {
			inputStream.close();
		}

		// Let's create the outer bean named "innerBean",
		// to check whether it doesn't create any conflicts
		// with the actual inner beans named "innerBean".
		xbf.getBean("innerBean");

		TestBean hasInnerBeans = (TestBean) xbf.getBean("hasInnerBeans");
		assertEquals(5, hasInnerBeans.getAge());
		TestBean inner1 = (TestBean) hasInnerBeans.getSpouse();
		assertNotNull(inner1);
		assertEquals("innerBean#1", inner1.getBeanName());
		assertEquals("inner1", inner1.getName());
		assertEquals(6, inner1.getAge());

		assertNotNull(hasInnerBeans.getFriends());
		Object[] friends = hasInnerBeans.getFriends().toArray();
		assertEquals(3, friends.length);
		DerivedTestBean inner2 = (DerivedTestBean) friends[0];
		assertEquals("inner2", inner2.getName());
		assertTrue(inner2.getBeanName().startsWith(DerivedTestBean.class.getName()));
		assertFalse(xbf.containsBean("innerBean#1"));
		assertNotNull(inner2);
		assertEquals(7, inner2.getAge());
		TestBean innerFactory = (TestBean) friends[1];
		assertEquals(DummyFactory.SINGLETON_NAME, innerFactory.getName());
		TestBean inner5 = (TestBean) friends[2];
		assertEquals("innerBean#2", inner5.getBeanName());

		assertNotNull(hasInnerBeans.getSomeMap());
		assertEquals(2, hasInnerBeans.getSomeMap().size());
		TestBean inner3 = (TestBean) hasInnerBeans.getSomeMap().get("someKey");
		assertEquals("Jenny", inner3.getName());
		assertEquals(30, inner3.getAge());
		TestBean inner4 = (TestBean) hasInnerBeans.getSomeMap().get("someOtherKey");
		assertEquals("inner4", inner4.getName());
		assertEquals(9, inner4.getAge());

		TestBean hasInnerBeansForConstructor = (TestBean) xbf.getBean("hasInnerBeansForConstructor");
		TestBean innerForConstructor = (TestBean) hasInnerBeansForConstructor.getSpouse();
		assertNotNull(innerForConstructor);
		assertEquals("innerBean#3", innerForConstructor.getBeanName());
		assertEquals("inner1", innerForConstructor.getName());
		assertEquals(6, innerForConstructor.getAge());

		hasInnerBeansForConstructor = (TestBean) xbf.getBean("hasInnerBeansAsPrototype");
		innerForConstructor = (TestBean) hasInnerBeansForConstructor.getSpouse();
		assertNotNull(innerForConstructor);
		assertEquals("innerBean", innerForConstructor.getBeanName());
		assertEquals("inner1", innerForConstructor.getName());
		assertEquals(6, innerForConstructor.getAge());

		hasInnerBeansForConstructor = (TestBean) xbf.getBean("hasInnerBeansAsPrototype");
		innerForConstructor = (TestBean) hasInnerBeansForConstructor.getSpouse();
		assertNotNull(innerForConstructor);
		assertEquals("innerBean", innerForConstructor.getBeanName());
		assertEquals("inner1", innerForConstructor.getName());
		assertEquals(6, innerForConstructor.getAge());

		xbf.destroySingletons();
		assertTrue(inner1.wasDestroyed());
		assertTrue(inner2.wasDestroyed());
		assertTrue(innerFactory.getName() == null);
		assertTrue(inner5.wasDestroyed());
	}

	@Test
	public void testInnerBeansWithoutDestroy() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);

		// Let's create the outer bean named "innerBean",
		// to check whether it doesn't create any conflicts
		// with the actual inner beans named "innerBean".
		xbf.getBean("innerBean");

		TestBean hasInnerBeans = (TestBean) xbf.getBean("hasInnerBeansWithoutDestroy");
		assertEquals(5, hasInnerBeans.getAge());
		TestBean inner1 = (TestBean) hasInnerBeans.getSpouse();
		assertNotNull(inner1);
		assertTrue(inner1.getBeanName().startsWith("innerBean"));
		assertEquals("inner1", inner1.getName());
		assertEquals(6, inner1.getAge());

		assertNotNull(hasInnerBeans.getFriends());
		Object[] friends = hasInnerBeans.getFriends().toArray();
		assertEquals(3, friends.length);
		DerivedTestBean inner2 = (DerivedTestBean) friends[0];
		assertEquals("inner2", inner2.getName());
		assertTrue(inner2.getBeanName().startsWith(DerivedTestBean.class.getName()));
		assertNotNull(inner2);
		assertEquals(7, inner2.getAge());
		TestBean innerFactory = (TestBean) friends[1];
		assertEquals(DummyFactory.SINGLETON_NAME, innerFactory.getName());
		TestBean inner5 = (TestBean) friends[2];
		assertTrue(inner5.getBeanName().startsWith("innerBean"));
	}

	@Test
	public void testFailsOnInnerBean() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);

		try {
			xbf.getBean("failsOnInnerBean");
		}
		catch (BeanCreationException ex) {
			// Check whether message contains outer bean name.
			ex.printStackTrace();
			assertTrue(ex.getMessage().contains("failsOnInnerBean"));
			assertTrue(ex.getMessage().contains("someMap"));
		}

		try {
			xbf.getBean("failsOnInnerBeanForConstructor");
		}
		catch (BeanCreationException ex) {
			// Check whether message contains outer bean name.
			ex.printStackTrace();
			assertTrue(ex.getMessage().contains("failsOnInnerBeanForConstructor"));
			assertTrue(ex.getMessage().contains("constructor argument"));
		}
	}

	@Test
	public void testInheritanceFromParentFactoryPrototype() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		assertEquals(TestBean.class, child.getType("inheritsFromParentFactory"));
		TestBean inherits = (TestBean) child.getBean("inheritsFromParentFactory");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("override"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 1);
		TestBean inherits2 = (TestBean) child.getBean("inheritsFromParentFactory");
		assertFalse(inherits2 == inherits);
	}

	@Test
	public void testInheritanceWithDifferentClass() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		assertEquals(DerivedTestBean.class, child.getType("inheritsWithClass"));
		DerivedTestBean inherits = (DerivedTestBean) child.getBean("inheritsWithDifferentClass");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("override"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 1);
		assertTrue(inherits.wasInitialized());
	}

	@Test
	public void testInheritanceWithClass() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		assertEquals(DerivedTestBean.class, child.getType("inheritsWithClass"));
		DerivedTestBean inherits = (DerivedTestBean) child.getBean("inheritsWithClass");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("override"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 1);
		assertTrue(inherits.wasInitialized());
	}

	@Test
	public void testPrototypeInheritanceFromParentFactoryPrototype() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		assertEquals(TestBean.class, child.getType("prototypeInheritsFromParentFactoryPrototype"));
		TestBean inherits = (TestBean) child.getBean("prototypeInheritsFromParentFactoryPrototype");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("prototype-override"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 2);
		TestBean inherits2 = (TestBean) child.getBean("prototypeInheritsFromParentFactoryPrototype");
		assertFalse(inherits2 == inherits);
		inherits2.setAge(13);
		assertTrue(inherits2.getAge() == 13);
		// Shouldn't have changed first instance
		assertTrue(inherits.getAge() == 2);
	}

	@Test
	public void testPrototypeInheritanceFromParentFactorySingleton() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		TestBean inherits = (TestBean) child.getBean("protoypeInheritsFromParentFactorySingleton");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("prototypeOverridesInheritedSingleton"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 1);
		TestBean inherits2 = (TestBean) child.getBean("protoypeInheritsFromParentFactorySingleton");
		assertFalse(inherits2 == inherits);
		inherits2.setAge(13);
		assertTrue(inherits2.getAge() == 13);
		// Shouldn't have changed first instance
		assertTrue(inherits.getAge() == 1);
	}

	@Test
	public void testAutowireModeNotInherited() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(OVERRIDES_CONTEXT);

		TestBean david = (TestBean) xbf.getBean("magicDavid");
		// the parent bean is autowiring
		assertNotNull(david.getSpouse());

		TestBean derivedDavid = (TestBean) xbf.getBean("magicDavidDerived");
		// this fails while it inherits from the child bean
		assertNull("autowiring not propagated along child relationships", derivedDavid.getSpouse());
	}

	@Test
	public void testAbstractParentBeans() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		parent.preInstantiateSingletons();
		assertTrue(parent.isSingleton("inheritedTestBeanWithoutClass"));

		// abstract beans should not match
		Map<?, ?> tbs = parent.getBeansOfType(TestBean.class);
		assertEquals(2, tbs.size());
		assertTrue(tbs.containsKey("inheritedTestBeanPrototype"));
		assertTrue(tbs.containsKey("inheritedTestBeanSingleton"));

		// abstract bean should throw exception on creation attempt
		try {
			parent.getBean("inheritedTestBeanWithoutClass");
			fail("Should have thrown BeanIsAbstractException");
		}
		catch (BeanIsAbstractException ex) {
			// expected
		}

		// non-abstract bean should work, even if it serves as parent
		assertTrue(parent.getBean("inheritedTestBeanPrototype") instanceof TestBean);
	}

	@Test
	public void testDependenciesMaterializeThis() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(DEP_MATERIALIZE_CONTEXT);

		assertEquals(2, xbf.getBeansOfType(DummyBo.class, true, false).size());
		assertEquals(3, xbf.getBeansOfType(DummyBo.class, true, true).size());
		assertEquals(3, xbf.getBeansOfType(DummyBo.class, true, false).size());
		assertEquals(3, xbf.getBeansOfType(DummyBo.class).size());
		assertEquals(2, xbf.getBeansOfType(DummyBoImpl.class, true, true).size());
		assertEquals(1, xbf.getBeansOfType(DummyBoImpl.class, false, true).size());
		assertEquals(2, xbf.getBeansOfType(DummyBoImpl.class).size());

		DummyBoImpl bos = (DummyBoImpl) xbf.getBean("boSingleton");
		DummyBoImpl bop = (DummyBoImpl) xbf.getBean("boPrototype");
		assertNotSame(bos, bop);
		assertTrue(bos.dao == bop.dao);
	}

	@Test
	public void testChildOverridesParentBean() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		TestBean inherits = (TestBean) child.getBean("inheritedTestBean");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("overrideParentBean"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 1);
		TestBean inherits2 = (TestBean) child.getBean("inheritedTestBean");
		assertTrue(inherits2 != inherits);
	}

	/**
	 * Check that a prototype can't inherit from a bogus parent.
	 * If a singleton does this the factory will fail to load.
	 */
	@Test
	public void testBogusParentageFromParentFactory() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		try {
			child.getBean("bogusParent", TestBean.class);
			fail();
		}
		catch (BeanDefinitionStoreException ex) {
			// check exception message contains the name
			assertTrue(ex.getMessage().contains("bogusParent"));
			assertTrue(ex.getCause() instanceof NoSuchBeanDefinitionException);
		}
	}

	/**
	 * Note that prototype/singleton distinction is <b>not</b> inherited.
	 * It's possible for a subclass singleton not to return independent
	 * instances even if derived from a prototype
	 */
	@Test
	public void testSingletonInheritsFromParentFactoryPrototype() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		TestBean inherits = (TestBean) child.getBean("singletonInheritsFromParentFactoryPrototype");
		// Name property value is overridden
		assertTrue(inherits.getName().equals("prototype-override"));
		// Age property is inherited from bean in parent factory
		assertTrue(inherits.getAge() == 2);
		TestBean inherits2 = (TestBean) child.getBean("singletonInheritsFromParentFactoryPrototype");
		assertTrue(inherits2 == inherits);
	}

	@Test
	public void testSingletonFromParent() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		TestBean beanFromParent = (TestBean) parent.getBean("inheritedTestBeanSingleton");
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		TestBean beanFromChild = (TestBean) child.getBean("inheritedTestBeanSingleton");
		assertTrue("singleton from parent and child is the same", beanFromParent == beanFromChild);
	}

	@Test
	public void testNestedPropertyValue() {
		DefaultListableBeanFactory parent = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(parent).loadBeanDefinitions(PARENT_CONTEXT);
		DefaultListableBeanFactory child = new DefaultListableBeanFactory(parent);
		new XmlBeanDefinitionReader(child).loadBeanDefinitions(CHILD_CONTEXT);
		IndexedTestBean bean = (IndexedTestBean) child.getBean("indexedTestBean");
		assertEquals("name applied correctly", "myname", bean.getArray()[0].getName());
	}

	@Test
	public void testCircularReferences() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		TestBean jenny = (TestBean) xbf.getBean("jenny");
		TestBean david = (TestBean) xbf.getBean("david");
		TestBean ego = (TestBean) xbf.getBean("ego");
		TestBean complexInnerEgo = (TestBean) xbf.getBean("complexInnerEgo");
		TestBean complexEgo = (TestBean) xbf.getBean("complexEgo");
		assertTrue("Correct circular reference", jenny.getSpouse() == david);
		assertTrue("Correct circular reference", david.getSpouse() == jenny);
		assertTrue("Correct circular reference", ego.getSpouse() == ego);
		assertTrue("Correct circular reference", complexInnerEgo.getSpouse().getSpouse() == complexInnerEgo);
		assertTrue("Correct circular reference", complexEgo.getSpouse().getSpouse() == complexEgo);
	}

	@Test
	public void testCircularReferenceWithFactoryBeanFirst() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		xbf.getBean("egoBridge");
		TestBean complexEgo = (TestBean) xbf.getBean("complexEgo");
		assertTrue("Correct circular reference", complexEgo.getSpouse().getSpouse() == complexEgo);
	}

	@Test
	public void testCircularReferenceWithTwoFactoryBeans() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		TestBean ego1 = (TestBean) xbf.getBean("ego1");
		assertTrue("Correct circular reference", ego1.getSpouse().getSpouse() == ego1);
		TestBean ego3 = (TestBean) xbf.getBean("ego3");
		assertTrue("Correct circular reference", ego3.getSpouse().getSpouse() == ego3);
	}

	@Test
	public void testCircularReferencesWithNotAllowed() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		xbf.setAllowCircularReferences(false);
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		try {
			xbf.getBean("jenny");
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.contains(BeanCurrentlyInCreationException.class));
		}
	}

	@Test
	public void testCircularReferencesWithWrapping() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		xbf.addBeanPostProcessor(new WrappingPostProcessor());
		try {
			xbf.getBean("jenny");
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.contains(BeanCurrentlyInCreationException.class));
		}
	}

	@Test
	public void testCircularReferencesWithWrappingAndRawInjectionAllowed() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		xbf.setAllowRawInjectionDespiteWrapping(true);
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.setValidationMode(XmlBeanDefinitionReader.VALIDATION_NONE);
		reader.loadBeanDefinitions(REFTYPES_CONTEXT);
		xbf.addBeanPostProcessor(new WrappingPostProcessor());

		ITestBean jenny = (ITestBean) xbf.getBean("jenny");
		ITestBean david = (ITestBean) xbf.getBean("david");
		assertTrue(AopUtils.isAopProxy(jenny));
		assertTrue(AopUtils.isAopProxy(david));
		assertSame(david, jenny.getSpouse());
		assertNotSame(jenny, david.getSpouse());
		assertEquals("Jenny", david.getSpouse().getName());
		assertSame(david, david.getSpouse().getSpouse());
		assertTrue(AopUtils.isAopProxy(jenny.getSpouse()));
		assertTrue(!AopUtils.isAopProxy(david.getSpouse()));
	}

	@Test
	public void testFactoryReferenceCircle() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(FACTORY_CIRCLE_CONTEXT);
		TestBean tb = (TestBean) xbf.getBean("singletonFactory");
		DummyFactory db = (DummyFactory) xbf.getBean("&singletonFactory");
		assertTrue(tb == db.getOtherTestBean());
	}

	@Test
	public void testFactoryReferenceWithDoublePrefix() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(FACTORY_CIRCLE_CONTEXT);
		assertThat(xbf.getBean("&&singletonFactory"), instanceOf(DummyFactory.class));
	}

	@Test
	public void testComplexFactoryReferenceCircle() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(COMPLEX_FACTORY_CIRCLE_CONTEXT);
		xbf.getBean("proxy1");
		// check that unused instances from autowiring got removed
		assertEquals(4, xbf.getSingletonCount());
		// properly create the remaining two instances
		xbf.getBean("proxy2");
		assertEquals(5, xbf.getSingletonCount());
	}

	@Test(expected = BeanCreationException.class)
	public void noSuchFactoryBeanMethod() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(NO_SUCH_FACTORY_METHOD_CONTEXT);
		assertNotNull(xbf.getBean("defaultTestBean"));
	}

	@Test
	public void testInitMethodIsInvoked() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INITIALIZERS_CONTEXT);
		DoubleInitializer in = (DoubleInitializer) xbf.getBean("init-method1");
		// Initializer should have doubled value
		assertEquals(14, in.getNum());
	}

	/**
	 * Test that if a custom initializer throws an exception, it's handled correctly
	 */
	@Test
	public void testInitMethodThrowsException() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INITIALIZERS_CONTEXT);
		try {
			xbf.getBean("init-method2");
			fail();
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.getResourceDescription().contains("initializers.xml"));
			assertEquals("init-method2", ex.getBeanName());
			assertTrue(ex.getCause() instanceof IOException);
		}
	}

	@Test
	public void testNoSuchInitMethod() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INITIALIZERS_CONTEXT);
		try {
			xbf.getBean("init-method3");
			fail();
		}
		catch (FatalBeanException ex) {
			// check message is helpful
			assertTrue(ex.getMessage().contains("initializers.xml"));
			assertTrue(ex.getMessage().contains("init-method3"));
			assertTrue(ex.getMessage().contains("init"));
		}
	}

	/**
	 * Check that InitializingBean method is called first.
	 */
	@Test
	public void testInitializingBeanAndInitMethod() {
		InitAndIB.constructed = false;
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INITIALIZERS_CONTEXT);
		assertFalse(InitAndIB.constructed);
		xbf.preInstantiateSingletons();
		assertFalse(InitAndIB.constructed);
		InitAndIB iib = (InitAndIB) xbf.getBean("init-and-ib");
		assertTrue(InitAndIB.constructed);
		assertTrue(iib.afterPropertiesSetInvoked && iib.initMethodInvoked);
		assertTrue(!iib.destroyed && !iib.customDestroyed);
		xbf.destroySingletons();
		assertTrue(iib.destroyed && iib.customDestroyed);
		xbf.destroySingletons();
		assertTrue(iib.destroyed && iib.customDestroyed);
	}

	/**
	 * Check that InitializingBean method is not called twice.
	 */
	@Test
	public void testInitializingBeanAndSameInitMethod() {
		InitAndIB.constructed = false;
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INITIALIZERS_CONTEXT);
		assertFalse(InitAndIB.constructed);
		xbf.preInstantiateSingletons();
		assertFalse(InitAndIB.constructed);
		InitAndIB iib = (InitAndIB) xbf.getBean("ib-same-init");
		assertTrue(InitAndIB.constructed);
		assertTrue(iib.afterPropertiesSetInvoked && !iib.initMethodInvoked);
		assertTrue(!iib.destroyed && !iib.customDestroyed);
		xbf.destroySingletons();
		assertTrue(iib.destroyed && !iib.customDestroyed);
		xbf.destroySingletons();
		assertTrue(iib.destroyed && !iib.customDestroyed);
	}

	@Test
	public void testDefaultLazyInit() {
		InitAndIB.constructed = false;
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(DEFAULT_LAZY_CONTEXT);
		assertFalse(InitAndIB.constructed);
		xbf.preInstantiateSingletons();
		assertTrue(InitAndIB.constructed);
		try {
			xbf.getBean("lazy-and-bad");
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.getCause() instanceof IOException);
		}
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void noSuchXmlFile() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(MISSING_CONTEXT);
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void invalidXmlFile() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(INVALID_CONTEXT);
	}

	@Test
	public void testAutowire() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(AUTOWIRE_CONTEXT);
		TestBean spouse = new TestBean("kerry", 0);
		xbf.registerSingleton("spouse", spouse);
		doTestAutowire(xbf);
	}

	@Test
	public void testAutowireWithParent() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(AUTOWIRE_CONTEXT);
		DefaultListableBeanFactory lbf = new DefaultListableBeanFactory();
		MutablePropertyValues pvs = new MutablePropertyValues();
		pvs.add("name", "kerry");
		RootBeanDefinition bd = new RootBeanDefinition(TestBean.class);
		bd.setPropertyValues(pvs);
		lbf.registerBeanDefinition("spouse", bd);
		xbf.setParentBeanFactory(lbf);
		doTestAutowire(xbf);
	}

	private void doTestAutowire(DefaultListableBeanFactory xbf) {
		DependenciesBean rod1 = (DependenciesBean) xbf.getBean("rod1");
		TestBean kerry = (TestBean) xbf.getBean("spouse");
		// should have been autowired
		assertEquals(kerry, rod1.getSpouse());

		DependenciesBean rod1a = (DependenciesBean) xbf.getBean("rod1a");
		// should have been autowired
		assertEquals(kerry, rod1a.getSpouse());

		DependenciesBean rod2 = (DependenciesBean) xbf.getBean("rod2");
		// should have been autowired
		assertEquals(kerry, rod2.getSpouse());

		DependenciesBean rod2a = (DependenciesBean) xbf.getBean("rod2a");
		// should have been set explicitly
		assertEquals(kerry, rod2a.getSpouse());

		ConstructorDependenciesBean rod3 = (ConstructorDependenciesBean) xbf.getBean("rod3");
		IndexedTestBean other = (IndexedTestBean) xbf.getBean("other");
		// should have been autowired
		assertEquals(kerry, rod3.getSpouse1());
		assertEquals(kerry, rod3.getSpouse2());
		assertEquals(other, rod3.getOther());

		ConstructorDependenciesBean rod3a = (ConstructorDependenciesBean) xbf.getBean("rod3a");
		// should have been autowired
		assertEquals(kerry, rod3a.getSpouse1());
		assertEquals(kerry, rod3a.getSpouse2());
		assertEquals(other, rod3a.getOther());

		try {
			xbf.getBean("rod4", ConstructorDependenciesBean.class);
			fail("Must have thrown a FatalBeanException");
		}
		catch (FatalBeanException expected) {
			// expected
		}

		DependenciesBean rod5 = (DependenciesBean) xbf.getBean("rod5");
		// Should not have been autowired
		assertNull(rod5.getSpouse());

		BeanFactory appCtx = (BeanFactory) xbf.getBean("childAppCtx");
		assertTrue(appCtx.containsBean("rod1"));
		assertTrue(appCtx.containsBean("jenny"));
	}

	@Test
	public void testAutowireWithDefault() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(DEFAULT_AUTOWIRE_CONTEXT);

		DependenciesBean rod1 = (DependenciesBean) xbf.getBean("rod1");
		// should have been autowired
		assertNotNull(rod1.getSpouse());
		assertTrue(rod1.getSpouse().getName().equals("Kerry"));

		DependenciesBean rod2 = (DependenciesBean) xbf.getBean("rod2");
		// should have been autowired
		assertNotNull(rod2.getSpouse());
		assertTrue(rod2.getSpouse().getName().equals("Kerry"));
	}

	@Test
	public void testAutowireByConstructor() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		ConstructorDependenciesBean rod1 = (ConstructorDependenciesBean) xbf.getBean("rod1");
		TestBean kerry = (TestBean) xbf.getBean("kerry2");
		// should have been autowired
		assertEquals(kerry, rod1.getSpouse1());
		assertEquals(0, rod1.getAge());
		assertEquals(null, rod1.getName());

		ConstructorDependenciesBean rod2 = (ConstructorDependenciesBean) xbf.getBean("rod2");
		TestBean kerry1 = (TestBean) xbf.getBean("kerry1");
		TestBean kerry2 = (TestBean) xbf.getBean("kerry2");
		// should have been autowired
		assertEquals(kerry2, rod2.getSpouse1());
		assertEquals(kerry1, rod2.getSpouse2());
		assertEquals(0, rod2.getAge());
		assertEquals(null, rod2.getName());

		ConstructorDependenciesBean rod = (ConstructorDependenciesBean) xbf.getBean("rod3");
		IndexedTestBean other = (IndexedTestBean) xbf.getBean("other");
		// should have been autowired
		assertEquals(kerry, rod.getSpouse1());
		assertEquals(kerry, rod.getSpouse2());
		assertEquals(other, rod.getOther());
		assertEquals(0, rod.getAge());
		assertEquals(null, rod.getName());

		xbf.getBean("rod4", ConstructorDependenciesBean.class);
		// should have been autowired
		assertEquals(kerry, rod.getSpouse1());
		assertEquals(kerry, rod.getSpouse2());
		assertEquals(other, rod.getOther());
		assertEquals(0, rod.getAge());
		assertEquals(null, rod.getName());
	}

	@Test
	public void testAutowireByConstructorWithSimpleValues() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);

		ConstructorDependenciesBean rod5 = (ConstructorDependenciesBean) xbf.getBean("rod5");
		TestBean kerry1 = (TestBean) xbf.getBean("kerry1");
		TestBean kerry2 = (TestBean) xbf.getBean("kerry2");
		IndexedTestBean other = (IndexedTestBean) xbf.getBean("other");
		// should have been autowired
		assertEquals(kerry2, rod5.getSpouse1());
		assertEquals(kerry1, rod5.getSpouse2());
		assertEquals(other, rod5.getOther());
		assertEquals(99, rod5.getAge());
		assertEquals("myname", rod5.getName());

		DerivedConstructorDependenciesBean rod6 = (DerivedConstructorDependenciesBean) xbf.getBean("rod6");
		// should have been autowired
		assertTrue(rod6.initialized);
		assertTrue(!rod6.destroyed);
		assertEquals(kerry2, rod6.getSpouse1());
		assertEquals(kerry1, rod6.getSpouse2());
		assertEquals(other, rod6.getOther());
		assertEquals(0, rod6.getAge());
		assertEquals(null, rod6.getName());

		xbf.destroySingletons();
		assertTrue(rod6.destroyed);
	}

	@Test
	public void testRelatedCausesFromConstructorResolution() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);

		try {
			xbf.getBean("rod2Accessor");
		}
		catch (BeanCreationException ex) {
			assertTrue(ex.toString().contains("touchy"));
			ex.printStackTrace();
			assertNull(ex.getRelatedCauses());
		}
	}

	@Test
	public void testConstructorArgResolution() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		TestBean kerry1 = (TestBean) xbf.getBean("kerry1");
		TestBean kerry2 = (TestBean) xbf.getBean("kerry2");

		ConstructorDependenciesBean rod9 = (ConstructorDependenciesBean) xbf.getBean("rod9");
		assertEquals(99, rod9.getAge());
		ConstructorDependenciesBean rod9a = (ConstructorDependenciesBean) xbf.getBean("rod9", 98);
		assertEquals(98, rod9a.getAge());
		ConstructorDependenciesBean rod9b = (ConstructorDependenciesBean) xbf.getBean("rod9", "myName");
		assertEquals("myName", rod9b.getName());
		ConstructorDependenciesBean rod9c = (ConstructorDependenciesBean) xbf.getBean("rod9", 97);
		assertEquals(97, rod9c.getAge());

		ConstructorDependenciesBean rod10 = (ConstructorDependenciesBean) xbf.getBean("rod10");
		assertEquals(null, rod10.getName());

		ConstructorDependenciesBean rod11 = (ConstructorDependenciesBean) xbf.getBean("rod11");
		assertEquals(kerry2, rod11.getSpouse1());

		ConstructorDependenciesBean rod12 = (ConstructorDependenciesBean) xbf.getBean("rod12");
		assertEquals(kerry1, rod12.getSpouse1());
		assertNull(rod12.getSpouse2());

		ConstructorDependenciesBean rod13 = (ConstructorDependenciesBean) xbf.getBean("rod13");
		assertEquals(kerry1, rod13.getSpouse1());
		assertEquals(kerry2, rod13.getSpouse2());

		ConstructorDependenciesBean rod14 = (ConstructorDependenciesBean) xbf.getBean("rod14");
		assertEquals(kerry1, rod14.getSpouse1());
		assertEquals(kerry2, rod14.getSpouse2());

		ConstructorDependenciesBean rod15 = (ConstructorDependenciesBean) xbf.getBean("rod15");
		assertEquals(kerry2, rod15.getSpouse1());
		assertEquals(kerry1, rod15.getSpouse2());

		ConstructorDependenciesBean rod16 = (ConstructorDependenciesBean) xbf.getBean("rod16");
		assertEquals(kerry2, rod16.getSpouse1());
		assertEquals(kerry1, rod16.getSpouse2());
		assertEquals(29, rod16.getAge());

		ConstructorDependenciesBean rod17 = (ConstructorDependenciesBean) xbf.getBean("rod17");
		assertEquals(kerry1, rod17.getSpouse1());
		assertEquals(kerry2, rod17.getSpouse2());
		assertEquals(29, rod17.getAge());
	}

	@Test
	public void testPrototypeWithExplicitArguments() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		SimpleConstructorArgBean cd1 = (SimpleConstructorArgBean) xbf.getBean("rod18");
		assertEquals(0, cd1.getAge());
		SimpleConstructorArgBean cd2 = (SimpleConstructorArgBean) xbf.getBean("rod18", 98);
		assertEquals(98, cd2.getAge());
		SimpleConstructorArgBean cd3 = (SimpleConstructorArgBean) xbf.getBean("rod18", "myName");
		assertEquals("myName", cd3.getName());
		SimpleConstructorArgBean cd4 = (SimpleConstructorArgBean) xbf.getBean("rod18");
		assertEquals(0, cd4.getAge());
		SimpleConstructorArgBean cd5 = (SimpleConstructorArgBean) xbf.getBean("rod18", 97);
		assertEquals(97, cd5.getAge());
	}

	@Test
	public void testConstructorArgWithSingleMatch() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		File file = (File) xbf.getBean("file");
		assertEquals(File.separator + "test", file.getPath());
	}

	@Test(expected = BeanCreationException.class)
	public void throwsExceptionOnTooManyArguments() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		xbf.getBean("rod7", ConstructorDependenciesBean.class);
	}

	@Test(expected = UnsatisfiedDependencyException.class)
	public void throwsExceptionOnAmbiguousResolution() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		xbf.getBean("rod8", ConstructorDependenciesBean.class);
	}

	@Test
	public void testDependsOn() {
		doTestDependencies(DEP_DEPENDSON_CONTEXT, 1);
	}

	@Test
	public void testDependsOnInInnerBean() {
		doTestDependencies(DEP_DEPENDSON_INNER_CONTEXT, 4);
	}

	@Test
	public void testDependenciesThroughConstructorArguments() {
		doTestDependencies(DEP_CARG_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughConstructorArgumentAutowiring() {
		doTestDependencies(DEP_CARG_AUTOWIRE_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughConstructorArgumentsInInnerBean() {
		doTestDependencies(DEP_CARG_INNER_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughProperties() {
		doTestDependencies(DEP_PROP, 1);
	}

	@Test
	public void testDependenciesThroughPropertiesWithInTheMiddle() {
		doTestDependencies(DEP_PROP_MIDDLE_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughPropertyAutowiringByName() {
		doTestDependencies(DEP_PROP_ABN_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughPropertyAutowiringByType() {
		doTestDependencies(DEP_PROP_ABT_CONTEXT, 1);
	}

	@Test
	public void testDependenciesThroughPropertiesInInnerBean() {
		doTestDependencies(DEP_PROP_INNER_CONTEXT, 1);
	}

	private void doTestDependencies(ClassPathResource resource, int nrOfHoldingBeans) {
		PreparingBean1.prepared = false;
		PreparingBean1.destroyed = false;
		PreparingBean2.prepared = false;
		PreparingBean2.destroyed = false;
		DependingBean.destroyCount = 0;
		HoldingBean.destroyCount = 0;
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(resource);
		xbf.preInstantiateSingletons();
		xbf.destroySingletons();
		assertTrue(PreparingBean1.prepared);
		assertTrue(PreparingBean1.destroyed);
		assertTrue(PreparingBean2.prepared);
		assertTrue(PreparingBean2.destroyed);
		assertEquals(nrOfHoldingBeans, DependingBean.destroyCount);
		if (!xbf.getBeansOfType(HoldingBean.class, false, false).isEmpty()) {
			assertEquals(nrOfHoldingBeans, HoldingBean.destroyCount);
		}
	}

	/**
	 * When using a BeanFactory. singletons are of course not pre-instantiated.
	 * So rubbish class names in bean defs must now not be 'resolved' when the
	 * bean def is being parsed, 'cos everything on a bean def is now lazy, but
	 * must rather only be picked up when the bean is instantiated.
	 */
	@Test
	public void testClassNotFoundWithDefaultBeanClassLoader() {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(factory).loadBeanDefinitions(CLASS_NOT_FOUND_CONTEXT);
		// cool, no errors, so the rubbish class name in the bean def was not resolved
		try {
			// let's resolve the bean definition; must blow up
			factory.getBean("classNotFound");
			fail("Must have thrown a CannotLoadBeanClassException");
		}
		catch (CannotLoadBeanClassException ex) {
			assertTrue(ex.getResourceDescription().contains("classNotFound.xml"));
			assertTrue(ex.getCause() instanceof ClassNotFoundException);
		}
	}

	@Test
	public void testClassNotFoundWithNoBeanClassLoader() {
		DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
		reader.setBeanClassLoader(null);
		reader.loadBeanDefinitions(CLASS_NOT_FOUND_CONTEXT);
		assertEquals("WhatALotOfRubbish", bf.getBeanDefinition("classNotFound").getBeanClassName());
	}

	@Test
	public void testResourceAndInputStream() throws IOException {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(RESOURCE_CONTEXT);
		// comes from "resourceImport.xml"
		ResourceTestBean resource1 = (ResourceTestBean) xbf.getBean("resource1");
		// comes from "resource.xml"
		ResourceTestBean resource2 = (ResourceTestBean) xbf.getBean("resource2");

		assertTrue(resource1.getResource() instanceof ClassPathResource);
		StringWriter writer = new StringWriter();
		FileCopyUtils.copy(new InputStreamReader(resource1.getResource().getInputStream()), writer);
		assertEquals("test", writer.toString());
		writer = new StringWriter();
		FileCopyUtils.copy(new InputStreamReader(resource1.getInputStream()), writer);
		assertEquals("test", writer.toString());
		writer = new StringWriter();
		FileCopyUtils.copy(new InputStreamReader(resource2.getResource().getInputStream()), writer);
		assertEquals("test", writer.toString());
		writer = new StringWriter();
		FileCopyUtils.copy(new InputStreamReader(resource2.getInputStream()), writer);
		assertEquals("test", writer.toString());
	}

	@Test
	public void testClassPathResourceWithImport() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(RESOURCE_CONTEXT);
		// comes from "resourceImport.xml"
		xbf.getBean("resource1", ResourceTestBean.class);
		// comes from "resource.xml"
		xbf.getBean("resource2", ResourceTestBean.class);
	}

	@Test
	public void testUrlResourceWithImport() {
		URL url = getClass().getResource(RESOURCE_CONTEXT.getPath());
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(new UrlResource(url));
		// comes from "resourceImport.xml"
		xbf.getBean("resource1", ResourceTestBean.class);
		// comes from "resource.xml"
		xbf.getBean("resource2", ResourceTestBean.class);
	}

	@Test
	public void testFileSystemResourceWithImport() throws URISyntaxException {
		String file = getClass().getResource(RESOURCE_CONTEXT.getPath()).toURI().getPath();
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(new FileSystemResource(file));
		// comes from "resourceImport.xml"
		xbf.getBean("resource1", ResourceTestBean.class);
		// comes from "resource.xml"
		xbf.getBean("resource2", ResourceTestBean.class);
	}

	@Test(expected = BeanDefinitionStoreException.class)
	public void recursiveImport() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(RECURSIVE_IMPORT_CONTEXT);
	}

	/**
	 * @since 3.2.8 and 4.0.2
	 * @see <a href="https://jira.spring.io/browse/SPR-10785">SPR-10785</a> and <a
	 *      href="https://jira.spring.io/browse/SPR-11420">SPR-11420</a>
	 */
	@Test
	public void methodInjectedBeanMustBeOfSameEnhancedCglibSubclassTypeAcrossBeanFactories() {
		Class<?> firstClass = null;

		for (int i = 0; i < 10; i++) {
			DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
			new XmlBeanDefinitionReader(bf).loadBeanDefinitions(OVERRIDES_CONTEXT);

			final Class<?> currentClass = bf.getBean("overrideOneMethod").getClass();
			assertTrue("Method injected bean class [" + currentClass + "] must be a CGLIB enhanced subclass.",
				ClassUtils.isCglibProxyClass(currentClass));

			if (firstClass == null) {
				firstClass = currentClass;
			}
			else {
				assertEquals(firstClass, currentClass);
			}
		}
	}

	@Test
	public void lookupOverrideMethodsWithSetterInjection() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(OVERRIDES_CONTEXT);

		lookupOverrideMethodsWithSetterInjection(xbf, "overrideOneMethod", true);
		// Should work identically on subclass definition, in which lookup
		// methods are inherited
		lookupOverrideMethodsWithSetterInjection(xbf, "overrideInheritedMethod", true);

		// Check cost of repeated construction of beans with method overrides
		// Will pick up misuse of CGLIB
		int howMany = 100;
		StopWatch sw = new StopWatch();
		sw.start("Look up " + howMany + " prototype bean instances with method overrides");
		for (int i = 0; i < howMany; i++) {
			lookupOverrideMethodsWithSetterInjection(xbf, "overrideOnPrototype", false);
		}
		sw.stop();
		// System.out.println(sw);
		if (!LogFactory.getLog(DefaultListableBeanFactory.class).isDebugEnabled()) {
			assertTrue(sw.getTotalTimeMillis() < 2000);
		}

		// Now test distinct bean with swapped value in factory, to ensure the two are independent
		OverrideOneMethod swappedOom = (OverrideOneMethod) xbf.getBean("overrideOneMethodSwappedReturnValues");

		TestBean tb = swappedOom.getPrototypeDependency();
		assertEquals("David", tb.getName());
		tb = swappedOom.protectedOverrideSingleton();
		assertEquals("Jenny", tb.getName());
	}

	private void lookupOverrideMethodsWithSetterInjection(BeanFactory xbf,
			String beanName, boolean singleton) {
		OverrideOneMethod oom = (OverrideOneMethod) xbf.getBean(beanName);

		if (singleton) {
			assertSame(oom, xbf.getBean(beanName));
		}
		else {
			assertNotSame(oom, xbf.getBean(beanName));
		}

		TestBean jenny1 = oom.getPrototypeDependency();
		assertEquals("Jenny", jenny1.getName());
		TestBean jenny2 = oom.getPrototypeDependency();
		assertEquals("Jenny", jenny2.getName());
		assertNotSame(jenny1, jenny2);

		// Check that the bean can invoke the overridden method on itself
		// This differs from Spring's AOP support, which has a distinct notion
		// of a "target" object, meaning that the target needs explicit knowledge
		// of AOP proxying to invoke an advised method on itself.
		TestBean jenny3 = oom.invokesOverriddenMethodOnSelf();
		assertEquals("Jenny", jenny3.getName());
		assertNotSame(jenny1, jenny3);

		// Now try protected method, and singleton
		TestBean dave1 = oom.protectedOverrideSingleton();
		assertEquals("David", dave1.getName());
		TestBean dave2 = oom.protectedOverrideSingleton();
		assertEquals("David", dave2.getName());
		assertSame(dave1, dave2);
	}

	@Test
	public void testReplaceMethodOverrideWithSetterInjection() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(DELEGATION_OVERRIDES_CONTEXT);

		OverrideOneMethod oom = (OverrideOneMethod) xbf.getBean("overrideOneMethod");

		// Same contract as for overrides.xml
		TestBean jenny1 = oom.getPrototypeDependency();
		assertEquals("Jenny", jenny1.getName());
		TestBean jenny2 = oom.getPrototypeDependency();
		assertEquals("Jenny", jenny2.getName());
		assertNotSame(jenny1, jenny2);

		TestBean notJenny = oom.getPrototypeDependency("someParam");
		assertTrue(!"Jenny".equals(notJenny.getName()));

		// Now try protected method, and singleton
		TestBean dave1 = oom.protectedOverrideSingleton();
		assertEquals("David", dave1.getName());
		TestBean dave2 = oom.protectedOverrideSingleton();
		assertEquals("David", dave2.getName());
		assertSame(dave1, dave2);

		// Check unadvised behaviour
		String str = "woierowijeiowiej";
		assertEquals(str, oom.echo(str));

		// Now test replace
		String s = "this is not a palindrome";
		String reverse = new StringBuffer(s).reverse().toString();
		assertEquals("Should have overridden to reverse, not echo", reverse, oom.replaceMe(s));

		assertEquals("Should have overridden no-arg overloaded replaceMe method to return fixed value",
				FixedMethodReplacer.VALUE, oom.replaceMe());

		OverrideOneMethodSubclass ooms = (OverrideOneMethodSubclass) xbf.getBean("replaceVoidMethod");
		DoSomethingReplacer dos = (DoSomethingReplacer) xbf.getBean("doSomethingReplacer");
		assertEquals(null, dos.lastArg);
		String s1 = "";
		String s2 = "foo bar black sheep";
		ooms.doSomething(s1);
		assertEquals(s1, dos.lastArg);
		ooms.doSomething(s2);
		assertEquals(s2, dos.lastArg);
	}

	@Test
	public void lookupOverrideOneMethodWithConstructorInjection() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(CONSTRUCTOR_OVERRIDES_CONTEXT);

		ConstructorInjectedOverrides cio = (ConstructorInjectedOverrides) xbf.getBean("constructorOverrides");

		// Check that the setter was invoked...
		// We should be able to combine Constructor and
		// Setter Injection
		assertEquals("Setter string was set", "from property element", cio.getSetterString());

		// Jenny is a singleton
		TestBean jenny = (TestBean) xbf.getBean("jenny");
		assertSame(jenny, cio.getTestBean());
		assertSame(jenny, cio.getTestBean());
		FactoryMethods fm1 = cio.createFactoryMethods();
		FactoryMethods fm2 = cio.createFactoryMethods();
		assertNotSame("FactoryMethods reference is to a prototype", fm1, fm2);
		assertSame("The two prototypes hold the same singleton reference",
				fm1.getTestBean(), fm2.getTestBean());
	}

	@Test
	public void testRejectsOverrideOfBogusMethodName() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		try {
			reader.loadBeanDefinitions(INVALID_NO_SUCH_METHOD_CONTEXT);
			xbf.getBean("constructorOverrides");
			fail("Shouldn't allow override of bogus method");
		}
		catch (BeanDefinitionStoreException ex) {
			// Check that the bogus method name was included in the error message
			assertTrue("Bogus method name correctly reported", ex.getMessage().contains("bogusMethod"));
		}
	}

	@Test
	public void serializableMethodReplacerAndSuperclass() throws IOException {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(DELEGATION_OVERRIDES_CONTEXT);
		SerializableMethodReplacerCandidate s = (SerializableMethodReplacerCandidate) xbf.getBean("serializableReplacer");
		String forwards = "this is forwards";
		String backwards = new StringBuffer(forwards).reverse().toString();
		assertEquals(backwards, s.replaceMe(forwards));
		// SPR-356: lookup methods & method replacers are not serializable.
		assertFalse(
				"Lookup methods and method replacers are not meant to be serializable.",
				SerializationTestUtils.isSerializable(s));
	}

	@Test
	public void testInnerBeanInheritsScopeFromConcreteChildDefinition() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(OVERRIDES_CONTEXT);

		TestBean jenny1 = (TestBean) xbf.getBean("jennyChild");
		assertEquals(1, jenny1.getFriends().size());
		Object friend1 = jenny1.getFriends().iterator().next();
		assertTrue(friend1 instanceof TestBean);

		TestBean jenny2 = (TestBean) xbf.getBean("jennyChild");
		assertEquals(1, jenny2.getFriends().size());
		Object friend2 = jenny2.getFriends().iterator().next();
		assertTrue(friend2 instanceof TestBean);

		assertNotSame(jenny1, jenny2);
		assertNotSame(friend1, friend2);
	}

	@Test
	public void testConstructorArgWithSingleSimpleTypeMatch() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);

		SingleSimpleTypeConstructorBean bean = (SingleSimpleTypeConstructorBean) xbf.getBean("beanWithBoolean");
		assertTrue(bean.isSingleBoolean());

		SingleSimpleTypeConstructorBean bean2 = (SingleSimpleTypeConstructorBean) xbf.getBean("beanWithBoolean2");
		assertTrue(bean2.isSingleBoolean());
	}

	@Test
	public void testConstructorArgWithDoubleSimpleTypeMatch() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);

		SingleSimpleTypeConstructorBean bean = (SingleSimpleTypeConstructorBean) xbf.getBean("beanWithBooleanAndString");
		assertTrue(bean.isSecondBoolean());
		assertEquals("A String", bean.getTestString());

		SingleSimpleTypeConstructorBean bean2 = (SingleSimpleTypeConstructorBean) xbf.getBean("beanWithBooleanAndString2");
		assertTrue(bean2.isSecondBoolean());
		assertEquals("A String", bean2.getTestString());
	}

	@Test
	public void testDoubleBooleanAutowire() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		DoubleBooleanConstructorBean bean = (DoubleBooleanConstructorBean) xbf.getBean("beanWithDoubleBoolean");
		assertEquals(Boolean.TRUE, bean.boolean1);
		assertEquals(Boolean.FALSE, bean.boolean2);
	}

	@Test
	public void testDoubleBooleanAutowireWithIndex() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		DoubleBooleanConstructorBean bean = (DoubleBooleanConstructorBean) xbf.getBean("beanWithDoubleBooleanAndIndex");
		assertEquals(Boolean.FALSE, bean.boolean1);
		assertEquals(Boolean.TRUE, bean.boolean2);
	}

	@Test
	public void testLenientDependencyMatching() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		LenientDependencyTestBean bean = (LenientDependencyTestBean) xbf.getBean("lenientDependencyTestBean");
		assertTrue(bean.tb instanceof DerivedTestBean);
	}

	@Test
	public void testLenientDependencyMatchingFactoryMethod() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		LenientDependencyTestBean bean = (LenientDependencyTestBean) xbf.getBean("lenientDependencyTestBeanFactoryMethod");
		assertTrue(bean.tb instanceof DerivedTestBean);
	}

	@Test
	public void testNonLenientDependencyMatching() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AbstractBeanDefinition bd = (AbstractBeanDefinition) xbf.getBeanDefinition("lenientDependencyTestBean");
		bd.setLenientConstructorResolution(false);
		try {
			xbf.getBean("lenientDependencyTestBean");
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			// expected
			ex.printStackTrace();
			assertTrue(ex.getMostSpecificCause().getMessage().contains("Ambiguous"));
		}
	}

	@Test
	public void testNonLenientDependencyMatchingFactoryMethod() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AbstractBeanDefinition bd = (AbstractBeanDefinition) xbf.getBeanDefinition("lenientDependencyTestBeanFactoryMethod");
		bd.setLenientConstructorResolution(false);
		try {
			xbf.getBean("lenientDependencyTestBeanFactoryMethod");
			fail("Should have thrown BeanCreationException");
		}
		catch (BeanCreationException ex) {
			// expected
			ex.printStackTrace();
			assertTrue(ex.getMostSpecificCause().getMessage().contains("Ambiguous"));
		}
	}

	@Test
	public void testJavaLangStringConstructor() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AbstractBeanDefinition bd = (AbstractBeanDefinition) xbf.getBeanDefinition("string");
		bd.setLenientConstructorResolution(false);
		String str = (String) xbf.getBean("string");
		assertEquals("test", str);
	}

	@Test
	public void testCustomStringConstructor() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AbstractBeanDefinition bd = (AbstractBeanDefinition) xbf.getBeanDefinition("stringConstructor");
		bd.setLenientConstructorResolution(false);
		StringConstructorTestBean tb = (StringConstructorTestBean) xbf.getBean("stringConstructor");
		assertEquals("test", tb.name);
	}

	@Test
	public void testPrimitiveConstructorArray() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		ConstructorArrayTestBean bean = (ConstructorArrayTestBean) xbf.getBean("constructorArray");
		assertTrue(bean.array instanceof int[]);
		assertEquals(1, ((int[]) bean.array).length);
		assertEquals(1, ((int[]) bean.array)[0]);
	}

	@Test
	public void testIndexedPrimitiveConstructorArray() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		ConstructorArrayTestBean bean = (ConstructorArrayTestBean) xbf.getBean("indexedConstructorArray");
		assertTrue(bean.array instanceof int[]);
		assertEquals(1, ((int[]) bean.array).length);
		assertEquals(1, ((int[]) bean.array)[0]);
	}

	@Test
	public void testStringConstructorArrayNoType() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		ConstructorArrayTestBean bean = (ConstructorArrayTestBean) xbf.getBean("constructorArrayNoType");
		assertTrue(bean.array instanceof String[]);
		assertEquals(0, ((String[]) bean.array).length);
	}

	@Test
	public void testStringConstructorArrayNoTypeNonLenient() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AbstractBeanDefinition bd = (AbstractBeanDefinition) xbf.getBeanDefinition("constructorArrayNoType");
		bd.setLenientConstructorResolution(false);
		ConstructorArrayTestBean bean = (ConstructorArrayTestBean) xbf.getBean("constructorArrayNoType");
		assertTrue(bean.array instanceof String[]);
		assertEquals(0, ((String[]) bean.array).length);
	}

	@Test
	public void testConstructorWithUnresolvableParameterName() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(CONSTRUCTOR_ARG_CONTEXT);
		AtomicInteger bean = (AtomicInteger) xbf.getBean("constructorUnresolvableName");
		assertEquals(1, bean.get());
		bean = (AtomicInteger) xbf.getBean("constructorUnresolvableNameWithIndex");
		assertEquals(1, bean.get());
	}

	@Test
	public void testWithDuplicateName() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		try {
			new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(TEST_WITH_DUP_NAMES_CONTEXT);
			fail("Duplicate name not detected");
		}
		catch (BeansException ex) {
			assertTrue(ex.getMessage().contains("Bean name 'foo'"));
		}
	}

	@Test
	public void testWithDuplicateNameInAlias() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		try {
			new XmlBeanDefinitionReader(xbf).loadBeanDefinitions(TEST_WITH_DUP_NAME_IN_ALIAS_CONTEXT);
			fail("Duplicate name not detected");
		}
		catch (BeansException e) {
			assertTrue(e.getMessage().contains("Bean name 'foo'"));
		}
	}

	@Test
	public void testOverrideMethodByArgTypeAttribute() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(DELEGATION_OVERRIDES_CONTEXT);
		OverrideOneMethod oom = (OverrideOneMethod) xbf.getBean("overrideOneMethodByAttribute");
		assertEquals("should not replace", "replaceMe:1", oom.replaceMe(1));
		assertEquals("should replace", "cba", oom.replaceMe("abc"));
	}

	@Test
	public void testOverrideMethodByArgTypeElement() {
		DefaultListableBeanFactory xbf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(xbf);
		reader.loadBeanDefinitions(DELEGATION_OVERRIDES_CONTEXT);
		OverrideOneMethod oom = (OverrideOneMethod) xbf.getBean("overrideOneMethodByElement");
		assertEquals("should not replace", "replaceMe:1", oom.replaceMe(1));
		assertEquals("should replace", "cba", oom.replaceMe("abc"));
	}

	public static class DoSomethingReplacer implements MethodReplacer {

		public Object lastArg;

		@Override
		public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
			assertEquals(1, args.length);
			assertEquals("doSomething", method.getName());
			lastArg = args[0];
			return null;
		}
	}


	public static class BadInitializer {

		/** Init method */
		public void init2() throws IOException {
			throw new IOException();
		}
	}


	public static class DoubleInitializer {

		private int num;

		public int getNum() {
			return num;
		}

		public void setNum(int i) {
			num = i;
		}

		/** Init method */
		public void init() {
			this.num *= 2;
		}
	}


	public static class InitAndIB implements InitializingBean, DisposableBean {

		public static boolean constructed;

		public boolean afterPropertiesSetInvoked, initMethodInvoked, destroyed, customDestroyed;

		public InitAndIB() {
			constructed = true;
		}

		@Override
		public void afterPropertiesSet() {
			if (this.initMethodInvoked) {
				fail();
			}
			if (this.afterPropertiesSetInvoked) {
				throw new IllegalStateException("Already initialized");
			}
			this.afterPropertiesSetInvoked = true;
		}

		/** Init method */
		public void customInit() throws IOException {
			if (!this.afterPropertiesSetInvoked) {
				fail();
			}
			if (this.initMethodInvoked) {
				throw new IllegalStateException("Already customInitialized");
			}
			this.initMethodInvoked = true;
		}

		@Override
		public void destroy() {
			if (this.customDestroyed) {
				fail();
			}
			if (this.destroyed) {
				throw new IllegalStateException("Already destroyed");
			}
			this.destroyed = true;
		}

		public void customDestroy() {
			if (!this.destroyed) {
				fail();
			}
			if (this.customDestroyed) {
				throw new IllegalStateException("Already customDestroyed");
			}
			this.customDestroyed = true;
		}
	}


	public static class PreparingBean1 implements DisposableBean {

		public static boolean prepared = false;

		public static boolean destroyed = false;

		public PreparingBean1() {
			prepared = true;
		}

		@Override
		public void destroy() {
			destroyed = true;
		}
	}


	public static class PreparingBean2 implements DisposableBean {

		public static boolean prepared = false;

		public static boolean destroyed = false;

		public PreparingBean2() {
			prepared = true;
		}

		@Override
		public void destroy() {
			destroyed = true;
		}
	}


	public static class DependingBean implements InitializingBean, DisposableBean {

		public static int destroyCount = 0;

		public boolean destroyed = false;

		public DependingBean() {
		}

		public DependingBean(PreparingBean1 bean1, PreparingBean2 bean2) {
		}

		public void setBean1(PreparingBean1 bean1) {
		}

		public void setBean2(PreparingBean2 bean2) {
		}

		public void setInTheMiddleBean(InTheMiddleBean bean) {
		}

		@Override
		public void afterPropertiesSet() {
			if (!(PreparingBean1.prepared && PreparingBean2.prepared)) {
				throw new IllegalStateException("Need prepared PreparingBeans!");
			}
		}

		@Override
		public void destroy() {
			if (PreparingBean1.destroyed || PreparingBean2.destroyed) {
				throw new IllegalStateException("Should not be destroyed after PreparingBeans");
			}
			destroyed = true;
			destroyCount++;
		}
	}


	public static class InTheMiddleBean {

		public void setBean1(PreparingBean1 bean1) {
		}

		public void setBean2(PreparingBean2 bean2) {
		}
	}


	public static class HoldingBean implements DisposableBean {

		public static int destroyCount = 0;

		private DependingBean dependingBean;

		public boolean destroyed = false;

		public void setDependingBean(DependingBean dependingBean) {
			this.dependingBean = dependingBean;
		}

		@Override
		public void destroy() {
			if (this.dependingBean.destroyed) {
				throw new IllegalStateException("Should not be destroyed after DependingBean");
			}
			this.destroyed = true;
			destroyCount++;
		}
	}


	public static class DoubleBooleanConstructorBean {

		private Boolean boolean1;
		private Boolean boolean2;

		public DoubleBooleanConstructorBean(Boolean b1, Boolean b2) {
			this.boolean1 = b1;
			this.boolean2 = b2;
		}

		public DoubleBooleanConstructorBean(String s1, String s2) {
			throw new IllegalStateException("Don't pick this constructor");
		}

		public static DoubleBooleanConstructorBean create(Boolean b1, Boolean b2) {
			return new DoubleBooleanConstructorBean(b1, b2);
		}

		public static DoubleBooleanConstructorBean create(String s1, String s2) {
			return new DoubleBooleanConstructorBean(s1, s2);
		}
	}


	public static class LenientDependencyTestBean {

		public final ITestBean tb;

		public LenientDependencyTestBean(ITestBean tb) {
			this.tb = tb;
		}

		public LenientDependencyTestBean(TestBean tb) {
			this.tb = tb;
		}

		public LenientDependencyTestBean(DerivedTestBean tb) {
			this.tb = tb;
		}

		@SuppressWarnings("rawtypes")
		public LenientDependencyTestBean(Map[] m) {
			throw new IllegalStateException("Don't pick this constructor");
		}

		public static LenientDependencyTestBean create(ITestBean tb) {
			return new LenientDependencyTestBean(tb);
		}

		public static LenientDependencyTestBean create(TestBean tb) {
			return new LenientDependencyTestBean(tb);
		}

		public static LenientDependencyTestBean create(DerivedTestBean tb) {
			return new LenientDependencyTestBean(tb);
		}
	}


	public static class ConstructorArrayTestBean {

		public final Object array;

		public ConstructorArrayTestBean(int[] array) {
			this.array = array;
		}

		public ConstructorArrayTestBean(float[] array) {
			this.array = array;
		}

		public ConstructorArrayTestBean(short[] array) {
			this.array = array;
		}

		public ConstructorArrayTestBean(String[] array) {
			this.array = array;
		}
	}


	public static class StringConstructorTestBean {

		public final String name;

		public StringConstructorTestBean(String name) {
			this.name = name;
		}
	}


	public static class WrappingPostProcessor implements BeanPostProcessor {

		@Override
		public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
			return bean;
		}

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
			ProxyFactory pf = new ProxyFactory(bean);
			return pf.getProxy();
		}
	}

}
