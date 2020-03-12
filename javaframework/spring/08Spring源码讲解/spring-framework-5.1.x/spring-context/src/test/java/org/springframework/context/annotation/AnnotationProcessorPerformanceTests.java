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

package org.springframework.context.annotation;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.tests.Assume;
import org.springframework.tests.TestGroup;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.StopWatch;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 2.5
 */
public class AnnotationProcessorPerformanceTests {

	private static final Log factoryLog = LogFactory.getLog(DefaultListableBeanFactory.class);

	@BeforeClass
	public static void commonAssumptions() {
		Assume.group(TestGroup.PERFORMANCE);
		Assume.notLogging(factoryLog);
	}

	@Test
	public void testPrototypeCreationWithResourcePropertiesIsFastEnough() {
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(ResourceAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithOverriddenResourcePropertiesIsFastEnough() {
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(ResourceAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		rbd.getPropertyValues().add("spouse", new RuntimeBeanReference("spouse"));
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithAutowiredPropertiesIsFastEnough() {
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(AutowiredAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 4000);
	}

	@Test
	public void testPrototypeCreationWithOverriddenAutowiredPropertiesIsFastEnough() {
		GenericApplicationContext ctx = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ctx);
		ctx.refresh();

		RootBeanDefinition rbd = new RootBeanDefinition(AutowiredAnnotatedTestBean.class);
		rbd.setScope(RootBeanDefinition.SCOPE_PROTOTYPE);
		rbd.getPropertyValues().add("spouse", new RuntimeBeanReference("spouse"));
		ctx.registerBeanDefinition("test", rbd);
		ctx.registerBeanDefinition("spouse", new RootBeanDefinition(TestBean.class));
		TestBean spouse = (TestBean) ctx.getBean("spouse");
		StopWatch sw = new StopWatch();
		sw.start("prototype");
		for (int i = 0; i < 100000; i++) {
			TestBean tb = (TestBean) ctx.getBean("test");
			assertSame(spouse, tb.getSpouse());
		}
		sw.stop();
		assertTrue("Prototype creation took too long: " + sw.getTotalTimeMillis(), sw.getTotalTimeMillis() < 6000);
	}


	private static class ResourceAnnotatedTestBean extends TestBean {

		@Override
		@Resource
		@SuppressWarnings("deprecation")
		@org.springframework.beans.factory.annotation.Required
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
		}
	}

	private static class AutowiredAnnotatedTestBean extends TestBean {

		@Override
		@Autowired
		@SuppressWarnings("deprecation")
		@org.springframework.beans.factory.annotation.Required
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
		}
	}

}
