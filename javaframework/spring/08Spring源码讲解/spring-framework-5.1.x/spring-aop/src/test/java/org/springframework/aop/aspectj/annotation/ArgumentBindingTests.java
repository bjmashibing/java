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

package org.springframework.aop.aspectj.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.Test;

import org.springframework.aop.aspectj.AspectJAdviceParameterNameDiscoverer;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;

/**
 * @author Adrian Colyer
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class ArgumentBindingTests {

	@Test(expected = IllegalArgumentException.class)
	public void testBindingInPointcutUsedByAdvice() {
		TestBean tb = new TestBean();
		AspectJProxyFactory proxyFactory = new AspectJProxyFactory(tb);
		proxyFactory.addAspect(NamedPointcutWithArgs.class);

		ITestBean proxiedTestBean = proxyFactory.getProxy();
		proxiedTestBean.setName("Supercalifragalisticexpialidocious");
	}

	@Test(expected = IllegalStateException.class)
	public void testAnnotationArgumentNameBinding() {
		TransactionalBean tb = new TransactionalBean();
		AspectJProxyFactory proxyFactory = new AspectJProxyFactory(tb);
		proxyFactory.addAspect(PointcutWithAnnotationArgument.class);

		ITransactionalBean proxiedTestBean = proxyFactory.getProxy();
		proxiedTestBean.doInTransaction();
	}

	@Test
	public void testParameterNameDiscoverWithReferencePointcut() throws Exception {
		AspectJAdviceParameterNameDiscoverer discoverer =
				new AspectJAdviceParameterNameDiscoverer("somepc(formal) && set(* *)");
		discoverer.setRaiseExceptions(true);
		Method methodUsedForParameterTypeDiscovery =
				getClass().getMethod("methodWithOneParam", String.class);
		String[] pnames = discoverer.getParameterNames(methodUsedForParameterTypeDiscovery);
		assertEquals("one parameter name", 1, pnames.length);
		assertEquals("formal", pnames[0]);
	}


	public void methodWithOneParam(String aParam) {
	}


	public interface ITransactionalBean {

		@Transactional
		void doInTransaction();
	}


	public static class TransactionalBean implements ITransactionalBean {

		@Override
		@Transactional
		public void doInTransaction() {
		}
	}

}

/**
 * Represents Spring's Transactional annotation without actually introducing the dependency
 */
@Retention(RetentionPolicy.RUNTIME)
@interface Transactional {
}


@Aspect
class PointcutWithAnnotationArgument {

	@Around(value = "execution(* org.springframework..*.*(..)) && @annotation(transaction)")
	public Object around(ProceedingJoinPoint pjp, Transactional transaction) throws Throwable {
		System.out.println("Invoked with transaction " + transaction);
		throw new IllegalStateException();
	}

}


@Aspect
class NamedPointcutWithArgs {

	@Pointcut("execution(* *(..)) && args(s,..)")
	public void pointcutWithArgs(String s) {}

	@Around("pointcutWithArgs(aString)")
	public Object doAround(ProceedingJoinPoint pjp, String aString) throws Throwable {
		System.out.println("got '" + aString + "' at '" + pjp + "'");
		throw new IllegalArgumentException(aString);
	}

}
