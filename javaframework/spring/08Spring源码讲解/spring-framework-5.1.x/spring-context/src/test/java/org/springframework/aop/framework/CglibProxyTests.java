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

package org.springframework.aop.framework;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import test.mixin.LockMixinAdvisor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.tests.aop.advice.CountingBeforeAdvice;
import org.springframework.tests.aop.interceptor.NopInterceptor;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Additional and overridden tests for CGLIB proxies.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Ramnivas Laddad
 * @author Chris Beams
 */
@SuppressWarnings("serial")
public class CglibProxyTests extends AbstractAopProxyTests implements Serializable {

	private static final String DEPENDENCY_CHECK_CONTEXT =
			CglibProxyTests.class.getSimpleName() + "-with-dependency-checking.xml";


	@Override
	protected Object createProxy(ProxyCreatorSupport as) {
		as.setProxyTargetClass(true);
		Object proxy = as.createAopProxy().getProxy();
		assertTrue(AopUtils.isCglibProxy(proxy));
		return proxy;
	}

	@Override
	protected AopProxy createAopProxy(AdvisedSupport as) {
		as.setProxyTargetClass(true);
		return new CglibAopProxy(as);
	}

	@Override
	protected boolean requiresTarget() {
		return true;
	}


	@Test(expected = IllegalArgumentException.class)
	public void testNullConfig() {
		new CglibAopProxy(null);
	}

	@Test(expected = AopConfigException.class)
	public void testNoTarget() {
		AdvisedSupport pc = new AdvisedSupport(ITestBean.class);
		pc.addAdvice(new NopInterceptor());
		AopProxy aop = createAopProxy(pc);
		aop.getProxy();
	}

	@Test
	public void testProtectedMethodInvocation() {
		ProtectedMethodTestBean bean = new ProtectedMethodTestBean();
		bean.value = "foo";
		mockTargetSource.setTarget(bean);

		AdvisedSupport as = new AdvisedSupport();
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		AopProxy aop = new CglibAopProxy(as);

		ProtectedMethodTestBean proxy = (ProtectedMethodTestBean) aop.getProxy();
		assertTrue(AopUtils.isCglibProxy(proxy));
		assertEquals(proxy.getClass().getClassLoader(), bean.getClass().getClassLoader());
		assertEquals("foo", proxy.getString());
	}

	@Test
	public void testPackageMethodInvocation() {
		PackageMethodTestBean bean = new PackageMethodTestBean();
		bean.value = "foo";
		mockTargetSource.setTarget(bean);

		AdvisedSupport as = new AdvisedSupport();
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		AopProxy aop = new CglibAopProxy(as);

		PackageMethodTestBean proxy = (PackageMethodTestBean) aop.getProxy();
		assertTrue(AopUtils.isCglibProxy(proxy));
		assertEquals(proxy.getClass().getClassLoader(), bean.getClass().getClassLoader());
		assertEquals("foo", proxy.getString());
	}

	@Test
	public void testProxyCanBeClassNotInterface() {
		TestBean raw = new TestBean();
		raw.setAge(32);
		mockTargetSource.setTarget(raw);
		AdvisedSupport pc = new AdvisedSupport();
		pc.setTargetSource(mockTargetSource);
		AopProxy aop = new CglibAopProxy(pc);

		Object proxy = aop.getProxy();
		assertTrue(AopUtils.isCglibProxy(proxy));
		assertTrue(proxy instanceof ITestBean);
		assertTrue(proxy instanceof TestBean);

		TestBean tb = (TestBean) proxy;
		assertEquals(32, tb.getAge());
	}

	@Test
	public void testMethodInvocationDuringConstructor() {
		CglibTestBean bean = new CglibTestBean();
		bean.setName("Rob Harrop");

		AdvisedSupport as = new AdvisedSupport();
		as.setTarget(bean);
		as.addAdvice(new NopInterceptor());
		AopProxy aop = new CglibAopProxy(as);

		CglibTestBean proxy = (CglibTestBean) aop.getProxy();
		assertEquals("The name property has been overwritten by the constructor", "Rob Harrop", proxy.getName());
	}

	@Test
	public void testToStringInvocation() {
		PrivateCglibTestBean bean = new PrivateCglibTestBean();
		bean.setName("Rob Harrop");

		AdvisedSupport as = new AdvisedSupport();
		as.setTarget(bean);
		as.addAdvice(new NopInterceptor());
		AopProxy aop = new CglibAopProxy(as);

		PrivateCglibTestBean proxy = (PrivateCglibTestBean) aop.getProxy();
		assertEquals("The name property has been overwritten by the constructor", "Rob Harrop", proxy.toString());
	}

	@Test
	public void testUnadvisedProxyCreationWithCallDuringConstructor() {
		CglibTestBean target = new CglibTestBean();
		target.setName("Rob Harrop");

		AdvisedSupport pc = new AdvisedSupport();
		pc.setFrozen(true);
		pc.setTarget(target);

		CglibAopProxy aop = new CglibAopProxy(pc);
		CglibTestBean proxy = (CglibTestBean) aop.getProxy();
		assertNotNull("Proxy should not be null", proxy);
		assertEquals("Constructor overrode the value of name", "Rob Harrop", proxy.getName());
	}

	@Test
	public void testMultipleProxies() {
		TestBean target = new TestBean();
		target.setAge(20);
		TestBean target2 = new TestBean();
		target2.setAge(21);

		ITestBean proxy1 = getAdvisedProxy(target);
		ITestBean proxy2 = getAdvisedProxy(target2);
		assertSame(proxy1.getClass(), proxy2.getClass());
		assertEquals(target.getAge(), proxy1.getAge());
		assertEquals(target2.getAge(), proxy2.getAge());
	}

	private ITestBean getAdvisedProxy(TestBean target) {
		ProxyFactory pf = new ProxyFactory(new Class<?>[]{ITestBean.class});
		pf.setProxyTargetClass(true);

		MethodInterceptor advice = new NopInterceptor();
		Pointcut pointcut = new Pointcut() {
			@Override
			public ClassFilter getClassFilter() {
				return ClassFilter.TRUE;
			}
			@Override
			public MethodMatcher getMethodMatcher() {
				return MethodMatcher.TRUE;
			}
			@Override
			public boolean equals(Object obj) {
				return true;
			}
			@Override
			public int hashCode() {
				return 0;
			}
		};
		pf.addAdvisor(new DefaultPointcutAdvisor(pointcut, advice));

		pf.setTarget(target);
		pf.setFrozen(true);
		pf.setExposeProxy(false);

		return (ITestBean) pf.getProxy();
	}

	@Test
	public void testMultipleProxiesForIntroductionAdvisor() {
		TestBean target1 = new TestBean();
		target1.setAge(20);
		TestBean target2 = new TestBean();
		target2.setAge(21);

		ITestBean proxy1 = getIntroductionAdvisorProxy(target1);
		ITestBean proxy2 = getIntroductionAdvisorProxy(target2);
		assertSame("Incorrect duplicate creation of proxy classes", proxy1.getClass(), proxy2.getClass());
	}

	private ITestBean getIntroductionAdvisorProxy(TestBean target) {
		ProxyFactory pf = new ProxyFactory(ITestBean.class);
		pf.setProxyTargetClass(true);

		pf.addAdvisor(new LockMixinAdvisor());
		pf.setTarget(target);
		pf.setFrozen(true);
		pf.setExposeProxy(false);

		return (ITestBean) pf.getProxy();
	}

	@Test
	public void testWithNoArgConstructor() {
		NoArgCtorTestBean target = new NoArgCtorTestBean("b", 1);
		target.reset();

		mockTargetSource.setTarget(target);
		AdvisedSupport pc = new AdvisedSupport();
		pc.setTargetSource(mockTargetSource);
		CglibAopProxy aop = new CglibAopProxy(pc);
		aop.setConstructorArguments(new Object[] {"Rob Harrop", 22}, new Class<?>[] {String.class, int.class});

		NoArgCtorTestBean proxy = (NoArgCtorTestBean) aop.getProxy();
		assertNotNull(proxy);
	}

	@Test
	public void testProxyAProxy() {
		ITestBean target = new TestBean();

		mockTargetSource.setTarget(target);
		AdvisedSupport as = new AdvisedSupport();
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		CglibAopProxy cglib = new CglibAopProxy(as);

		ITestBean proxy1 = (ITestBean) cglib.getProxy();

		mockTargetSource.setTarget(proxy1);
		as = new AdvisedSupport(new Class<?>[]{});
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		cglib = new CglibAopProxy(as);

		assertThat(cglib.getProxy(), instanceOf(ITestBean.class));
	}

	@Test
	public void testProxyAProxyWithAdditionalInterface() {
		ITestBean target = new TestBean();
		mockTargetSource.setTarget(target);

		AdvisedSupport as = new AdvisedSupport();
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		as.addInterface(Serializable.class);
		CglibAopProxy cglib = new CglibAopProxy(as);

		ITestBean proxy1 = (ITestBean) cglib.getProxy();

		mockTargetSource.setTarget(proxy1);
		as = new AdvisedSupport(new Class<?>[]{});
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		cglib = new CglibAopProxy(as);

		ITestBean proxy2 = (ITestBean) cglib.getProxy();
		assertTrue(proxy2 instanceof Serializable);
	}

	@Test
	public void testExceptionHandling() {
		ExceptionThrower bean = new ExceptionThrower();
		mockTargetSource.setTarget(bean);

		AdvisedSupport as = new AdvisedSupport();
		as.setTargetSource(mockTargetSource);
		as.addAdvice(new NopInterceptor());
		AopProxy aop = new CglibAopProxy(as);

		ExceptionThrower proxy = (ExceptionThrower) aop.getProxy();

		try {
			proxy.doTest();
		}
		catch (Exception ex) {
			assertTrue("Invalid exception class", ex instanceof ApplicationContextException);
		}

		assertTrue("Catch was not invoked", proxy.isCatchInvoked());
		assertTrue("Finally was not invoked", proxy.isFinallyInvoked());
	}

	@Test
	@SuppressWarnings("resource")
	public void testWithDependencyChecking() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(DEPENDENCY_CHECK_CONTEXT, getClass());
		ctx.getBean("testBean");
	}

	@Test
	public void testAddAdviceAtRuntime() {
		TestBean bean = new TestBean();
		CountingBeforeAdvice cba = new CountingBeforeAdvice();

		ProxyFactory pf = new ProxyFactory();
		pf.setTarget(bean);
		pf.setFrozen(false);
		pf.setOpaque(false);
		pf.setProxyTargetClass(true);

		TestBean proxy = (TestBean) pf.getProxy();
		assertTrue(AopUtils.isCglibProxy(proxy));

		proxy.getAge();
		assertEquals(0, cba.getCalls());

		((Advised) proxy).addAdvice(cba);
		proxy.getAge();
		assertEquals(1, cba.getCalls());
	}

	@Test
	public void testProxyProtectedMethod() {
		CountingBeforeAdvice advice = new CountingBeforeAdvice();
		ProxyFactory proxyFactory = new ProxyFactory(new MyBean());
		proxyFactory.addAdvice(advice);
		proxyFactory.setProxyTargetClass(true);

		MyBean proxy = (MyBean) proxyFactory.getProxy();
		assertEquals(4, proxy.add(1, 3));
		assertEquals(1, advice.getCalls("add"));
	}

	@Test
	public void testProxyTargetClassInCaseOfNoInterfaces() {
		ProxyFactory proxyFactory = new ProxyFactory(new MyBean());
		MyBean proxy = (MyBean) proxyFactory.getProxy();
		assertEquals(4, proxy.add(1, 3));
	}

	@Test  // SPR-13328
	public void testVarargsWithEnumArray() {
		ProxyFactory proxyFactory = new ProxyFactory(new MyBean());
		MyBean proxy = (MyBean) proxyFactory.getProxy();
		assertTrue(proxy.doWithVarargs(MyEnum.A, MyOtherEnum.C));
	}


	public static class MyBean {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		protected int add(int x, int y) {
			return x + y;
		}

		@SuppressWarnings("unchecked")
		public <V extends MyInterface> boolean doWithVarargs(V... args) {
			return true;
		}
	}


	public interface MyInterface {
	}


	public enum MyEnum implements MyInterface {

		A, B;
	}


	public enum MyOtherEnum implements MyInterface {

		C, D;
	}


	public static class ExceptionThrower {

		private boolean catchInvoked;

		private boolean finallyInvoked;

		public boolean isCatchInvoked() {
			return catchInvoked;
		}

		public boolean isFinallyInvoked() {
			return finallyInvoked;
		}

		public void doTest() throws Exception {
			try {
				throw new ApplicationContextException("foo");
			}
			catch (Exception ex) {
				catchInvoked = true;
				throw ex;
			}
			finally {
				finallyInvoked = true;
			}
		}
	}


	public static class NoArgCtorTestBean {

		private boolean called = false;

		public NoArgCtorTestBean(String x, int y) {
			called = true;
		}

		public boolean wasCalled() {
			return called;
		}

		public void reset() {
			called = false;
		}
	}


	public static class ProtectedMethodTestBean {

		public String value;

		protected String getString() {
			return this.value;
		}
	}


	public static class PackageMethodTestBean {

		public String value;

		String getString() {
			return this.value;
		}
	}


	private static class PrivateCglibTestBean {

		private String name;

		public PrivateCglibTestBean() {
			setName("Some Default");
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}
}


class CglibTestBean {

	private String name;

	public CglibTestBean() {
		setName("Some Default");
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}


class UnsupportedInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		throw new UnsupportedOperationException(mi.getMethod().getName());
	}
}
