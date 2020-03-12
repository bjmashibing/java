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

package org.springframework.web.context;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.junit.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.mock.web.test.MockServletConfig;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.tests.sample.beans.LifecycleBean;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.SimpleWebApplicationContext;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Tests for {@link ContextLoader} and {@link ContextLoaderListener}.
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Chris Beams
 * @since 12.08.2003
 * @see org.springframework.web.context.support.Spr8510Tests
 */
public class ContextLoaderTests {

	@Test
	public void testContextLoaderListenerWithDefaultContext() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"/org/springframework/web/context/WEB-INF/applicationContext.xml " +
				"/org/springframework/web/context/WEB-INF/context-addition.xml");
		ServletContextListener listener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(sc);
		listener.contextInitialized(event);
		String contextAttr = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		WebApplicationContext context = (WebApplicationContext) sc.getAttribute(contextAttr);
		assertTrue("Correct WebApplicationContext exposed in ServletContext", context instanceof XmlWebApplicationContext);
		assertTrue(WebApplicationContextUtils.getRequiredWebApplicationContext(sc) instanceof XmlWebApplicationContext);
		LifecycleBean lb = (LifecycleBean) context.getBean("lifecycle");
		assertTrue("Has father", context.containsBean("father"));
		assertTrue("Has rod", context.containsBean("rod"));
		assertTrue("Has kerry", context.containsBean("kerry"));
		assertTrue("Not destroyed", !lb.isDestroyed());
		assertFalse(context.containsBean("beans1.bean1"));
		assertFalse(context.containsBean("beans1.bean2"));
		listener.contextDestroyed(event);
		assertTrue("Destroyed", lb.isDestroyed());
		assertNull(sc.getAttribute(contextAttr));
		assertNull(WebApplicationContextUtils.getWebApplicationContext(sc));
	}

	/**
	 * Addresses the issues raised in <a
	 * href="https://opensource.atlassian.com/projects/spring/browse/SPR-4008"
	 * target="_blank">SPR-4008</a>: <em>Supply an opportunity to customize
	 * context before calling refresh in ContextLoaders</em>.
	 */
	@Test
	public void testContextLoaderListenerWithCustomizedContextLoader() {
		final StringBuffer buffer = new StringBuffer();
		final String expectedContents = "customizeContext() was called";
		final MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"/org/springframework/web/context/WEB-INF/applicationContext.xml");
		ServletContextListener listener = new ContextLoaderListener() {
			@Override
			protected void customizeContext(ServletContext sc, ConfigurableWebApplicationContext wac) {
				assertNotNull("The ServletContext should not be null.", sc);
				assertEquals("Verifying that we received the expected ServletContext.", sc, sc);
				assertFalse("The ApplicationContext should not yet have been refreshed.", wac.isActive());
				buffer.append(expectedContents);
			}
		};
		listener.contextInitialized(new ServletContextEvent(sc));
		assertEquals("customizeContext() should have been called.", expectedContents, buffer.toString());
	}

	@Test
	public void testContextLoaderListenerWithLocalContextInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		sc.addInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, StringUtils.arrayToCommaDelimitedString(
				new Object[] {TestContextInitializer.class.getName(), TestWebContextInitializer.class.getName()}));
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testContextLoaderListenerWithGlobalContextInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		sc.addInitParameter(ContextLoader.GLOBAL_INITIALIZER_CLASSES_PARAM, StringUtils.arrayToCommaDelimitedString(
				new Object[] {TestContextInitializer.class.getName(), TestWebContextInitializer.class.getName()}));
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testContextLoaderListenerWithMixedContextInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		sc.addInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, TestContextInitializer.class.getName());
		sc.addInitParameter(ContextLoader.GLOBAL_INITIALIZER_CLASSES_PARAM, TestWebContextInitializer.class.getName());
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testContextLoaderListenerWithProgrammaticInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.setContextInitializers(new TestContextInitializer(), new TestWebContextInitializer());
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testContextLoaderListenerWithProgrammaticAndLocalInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		sc.addInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM, TestContextInitializer.class.getName());
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.setContextInitializers(new TestWebContextInitializer());
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testContextLoaderListenerWithProgrammaticAndGlobalInitializers() {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"org/springframework/web/context/WEB-INF/ContextLoaderTests-acc-context.xml");
		sc.addInitParameter(ContextLoader.GLOBAL_INITIALIZER_CLASSES_PARAM, TestWebContextInitializer.class.getName());
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.setContextInitializers(new TestContextInitializer());
		listener.contextInitialized(new ServletContextEvent(sc));
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		TestBean testBean = wac.getBean(TestBean.class);
		assertThat(testBean.getName(), equalTo("testName"));
		assertThat(wac.getServletContext().getAttribute("initialized"), notNullValue());
	}

	@Test
	public void testRegisteredContextInitializerCanAccessServletContextParamsViaEnvironment() {
		MockServletContext sc = new MockServletContext("");
		// config file doesn't matter - just a placeholder
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"/org/springframework/web/context/WEB-INF/empty-context.xml");

		sc.addInitParameter("someProperty", "someValue");
		sc.addInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM,
				EnvApplicationContextInitializer.class.getName());
		ContextLoaderListener listener = new ContextLoaderListener();
		listener.contextInitialized(new ServletContextEvent(sc));
	}

	@Test
	public void testContextLoaderListenerWithUnknownContextInitializer() {
		MockServletContext sc = new MockServletContext("");
		// config file doesn't matter.  just a placeholder
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM,
				"/org/springframework/web/context/WEB-INF/empty-context.xml");
		sc.addInitParameter(ContextLoader.CONTEXT_INITIALIZER_CLASSES_PARAM,
				StringUtils.arrayToCommaDelimitedString(new Object[] {UnknownContextInitializer.class.getName()}));
		ContextLoaderListener listener = new ContextLoaderListener();
		try {
			listener.contextInitialized(new ServletContextEvent(sc));
			fail("expected exception");
		}
		catch (ApplicationContextException ex) {
			assertTrue(ex.getMessage().contains("not assignable"));
		}
	}

	@Test
	public void testContextLoaderWithCustomContext() throws Exception {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
				"org.springframework.web.servlet.SimpleWebApplicationContext");
		ServletContextListener listener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(sc);
		listener.contextInitialized(event);
		String contextAttr = WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE;
		WebApplicationContext wc = (WebApplicationContext) sc.getAttribute(contextAttr);
		assertTrue("Correct WebApplicationContext exposed in ServletContext",
				wc instanceof SimpleWebApplicationContext);
	}

	@Test
	public void testContextLoaderWithInvalidLocation() throws Exception {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONFIG_LOCATION_PARAM, "/WEB-INF/myContext.xml");
		ServletContextListener listener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(sc);
		try {
			listener.contextInitialized(event);
			fail("Should have thrown BeanDefinitionStoreException");
		}
		catch (BeanDefinitionStoreException ex) {
			// expected
			assertTrue(ex.getCause() instanceof FileNotFoundException);
		}
	}

	@Test
	public void testContextLoaderWithInvalidContext() throws Exception {
		MockServletContext sc = new MockServletContext("");
		sc.addInitParameter(ContextLoader.CONTEXT_CLASS_PARAM,
				"org.springframework.web.context.support.InvalidWebApplicationContext");
		ServletContextListener listener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(sc);
		try {
			listener.contextInitialized(event);
			fail("Should have thrown ApplicationContextException");
		}
		catch (ApplicationContextException ex) {
			// expected
			assertTrue(ex.getCause() instanceof ClassNotFoundException);
		}
	}

	@Test
	public void testContextLoaderWithDefaultLocation() throws Exception {
		MockServletContext sc = new MockServletContext("");
		ServletContextListener listener = new ContextLoaderListener();
		ServletContextEvent event = new ServletContextEvent(sc);
		try {
			listener.contextInitialized(event);
			fail("Should have thrown BeanDefinitionStoreException");
		}
		catch (BeanDefinitionStoreException ex) {
			// expected
			assertTrue(ex.getCause() instanceof IOException);
			assertTrue(ex.getCause().getMessage().contains("/WEB-INF/applicationContext.xml"));
		}
	}

	@Test
	public void testFrameworkServletWithDefaultLocation() throws Exception {
		DispatcherServlet servlet = new DispatcherServlet();
		servlet.setContextClass(XmlWebApplicationContext.class);
		try {
			servlet.init(new MockServletConfig(new MockServletContext(""), "test"));
			fail("Should have thrown BeanDefinitionStoreException");
		}
		catch (BeanDefinitionStoreException ex) {
			// expected
			assertTrue(ex.getCause() instanceof IOException);
			assertTrue(ex.getCause().getMessage().contains("/WEB-INF/test-servlet.xml"));
		}
	}

	@Test
	public void testFrameworkServletWithCustomLocation() throws Exception {
		DispatcherServlet servlet = new DispatcherServlet();
		servlet.setContextConfigLocation("/org/springframework/web/context/WEB-INF/testNamespace.xml "
				+ "/org/springframework/web/context/WEB-INF/context-addition.xml");
		servlet.init(new MockServletConfig(new MockServletContext(""), "test"));
		assertTrue(servlet.getWebApplicationContext().containsBean("kerry"));
		assertTrue(servlet.getWebApplicationContext().containsBean("kerryX"));
	}

	@Test
	@SuppressWarnings("resource")
	public void testClassPathXmlApplicationContext() throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/org/springframework/web/context/WEB-INF/applicationContext.xml");
		assertTrue("Has father", context.containsBean("father"));
		assertTrue("Has rod", context.containsBean("rod"));
		assertFalse("Hasn't kerry", context.containsBean("kerry"));
		assertTrue("Doesn't have spouse", ((TestBean) context.getBean("rod")).getSpouse() == null);
		assertTrue("myinit not evaluated", "Roderick".equals(((TestBean) context.getBean("rod")).getName()));

		context = new ClassPathXmlApplicationContext(new String[] {
			"/org/springframework/web/context/WEB-INF/applicationContext.xml",
			"/org/springframework/web/context/WEB-INF/context-addition.xml" });
		assertTrue("Has father", context.containsBean("father"));
		assertTrue("Has rod", context.containsBean("rod"));
		assertTrue("Has kerry", context.containsBean("kerry"));
	}

	@Test(expected = BeanCreationException.class)
	@SuppressWarnings("resource")
	public void testSingletonDestructionOnStartupFailure() throws IOException {
		new ClassPathXmlApplicationContext(new String[] {
			"/org/springframework/web/context/WEB-INF/applicationContext.xml",
			"/org/springframework/web/context/WEB-INF/fail.xml" }) {

			@Override
			public void refresh() throws BeansException {
				try {
					super.refresh();
				}
				catch (BeanCreationException ex) {
					DefaultListableBeanFactory factory = (DefaultListableBeanFactory) getBeanFactory();
					assertEquals(0, factory.getSingletonCount());
					throw ex;
				}
			}
		};
	}


	private static class TestContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			environment.getPropertySources().addFirst(new PropertySource<Object>("testPropertySource") {
				@Override
				public Object getProperty(String key) {
					return "name".equals(key) ? "testName" : null;
				}
			});
		}
	}


	private static class TestWebContextInitializer implements
			ApplicationContextInitializer<ConfigurableWebApplicationContext> {

		@Override
		public void initialize(ConfigurableWebApplicationContext applicationContext) {
			ServletContext ctx = applicationContext.getServletContext(); // type-safe access to servlet-specific methods
			ctx.setAttribute("initialized", true);
		}
	}


	private static class EnvApplicationContextInitializer
			implements ApplicationContextInitializer<ConfigurableWebApplicationContext> {

		@Override
		public void initialize(ConfigurableWebApplicationContext applicationContext) {
			// test that ApplicationContextInitializers can access ServletContext properties
			// via the environment (SPR-8991)
			String value = applicationContext.getEnvironment().getRequiredProperty("someProperty");
			assertThat(value, is("someValue"));
		}
	}


	private static interface UnknownApplicationContext extends ConfigurableApplicationContext {

		void unheardOf();
	}


	private static class UnknownContextInitializer implements ApplicationContextInitializer<UnknownApplicationContext> {

		@Override
		public void initialize(UnknownApplicationContext applicationContext) {
			applicationContext.unheardOf();
		}
	}

}
