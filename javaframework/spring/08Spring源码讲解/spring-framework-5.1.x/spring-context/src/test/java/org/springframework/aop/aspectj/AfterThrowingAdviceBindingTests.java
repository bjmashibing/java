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

package org.springframework.aop.aspectj;

import org.junit.Before;
import org.junit.Test;

import org.springframework.aop.aspectj.AfterThrowingAdviceBindingTestAspect.AfterThrowingAdviceBindingCollaborator;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.tests.sample.beans.ITestBean;

import static org.mockito.BDDMockito.*;

/**
 * Tests for various parameter binding scenarios with before advice.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
public class AfterThrowingAdviceBindingTests {

	private ITestBean testBean;

	private AfterThrowingAdviceBindingTestAspect afterThrowingAdviceAspect;

	private AfterThrowingAdviceBindingCollaborator mockCollaborator;


	@Before
	public void setup() {
		ClassPathXmlApplicationContext ctx =
				new ClassPathXmlApplicationContext(getClass().getSimpleName() + ".xml", getClass());

		testBean = (ITestBean) ctx.getBean("testBean");
		afterThrowingAdviceAspect = (AfterThrowingAdviceBindingTestAspect) ctx.getBean("testAspect");

		mockCollaborator = mock(AfterThrowingAdviceBindingCollaborator.class);
		afterThrowingAdviceAspect.setCollaborator(mockCollaborator);
	}


	@Test(expected = Throwable.class)
	public void testSimpleAfterThrowing() throws Throwable {
		this.testBean.exceptional(new Throwable());
		verify(mockCollaborator).noArgs();
	}

	@Test(expected = Throwable.class)
	public void testAfterThrowingWithBinding() throws Throwable {
		Throwable t = new Throwable();
		this.testBean.exceptional(t);
		verify(mockCollaborator).oneThrowable(t);
	}

	@Test(expected = Throwable.class)
	public void testAfterThrowingWithNamedTypeRestriction() throws Throwable {
		Throwable t = new Throwable();
		this.testBean.exceptional(t);
		verify(mockCollaborator).noArgs();
		verify(mockCollaborator).oneThrowable(t);
		verify(mockCollaborator).noArgsOnThrowableMatch();
	}

	@Test(expected = Throwable.class)
	public void testAfterThrowingWithRuntimeExceptionBinding() throws Throwable {
		RuntimeException ex = new RuntimeException();
		this.testBean.exceptional(ex);
		verify(mockCollaborator).oneRuntimeException(ex);
	}

	@Test(expected = Throwable.class)
	public void testAfterThrowingWithTypeSpecified() throws Throwable {
		this.testBean.exceptional(new Throwable());
		verify(mockCollaborator).noArgsOnThrowableMatch();
	}

	@Test(expected = Throwable.class)
	public void testAfterThrowingWithRuntimeTypeSpecified() throws Throwable {
		this.testBean.exceptional(new RuntimeException());
		verify(mockCollaborator).noArgsOnRuntimeExceptionMatch();
	}

}


final class AfterThrowingAdviceBindingTestAspect {

	// collaborator interface that makes it easy to test this aspect is
	// working as expected through mocking.
	public interface AfterThrowingAdviceBindingCollaborator {
		void noArgs();
		void oneThrowable(Throwable t);
		void oneRuntimeException(RuntimeException re);
		void noArgsOnThrowableMatch();
		void noArgsOnRuntimeExceptionMatch();
	}

	protected AfterThrowingAdviceBindingCollaborator collaborator = null;

	public void setCollaborator(AfterThrowingAdviceBindingCollaborator aCollaborator) {
		this.collaborator = aCollaborator;
	}

	public void noArgs() {
		this.collaborator.noArgs();
	}

	public void oneThrowable(Throwable t) {
		this.collaborator.oneThrowable(t);
	}

	public void oneRuntimeException(RuntimeException ex) {
		this.collaborator.oneRuntimeException(ex);
	}

	public void noArgsOnThrowableMatch() {
		this.collaborator.noArgsOnThrowableMatch();
	}

	public void noArgsOnRuntimeExceptionMatch() {
		this.collaborator.noArgsOnRuntimeExceptionMatch();
	}
}
