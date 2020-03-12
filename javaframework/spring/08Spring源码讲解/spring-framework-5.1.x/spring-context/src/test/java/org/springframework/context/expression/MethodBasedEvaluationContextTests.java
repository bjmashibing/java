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

package org.springframework.context.expression;

import java.lang.reflect.Method;

import org.junit.Test;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link MethodBasedEvaluationContext}.
 *
 * @author Stephane Nicoll
 * @author Juergen Hoeller
 * @author Sergey Podgurskiy
 */
public class MethodBasedEvaluationContextTests {

	private final ParameterNameDiscoverer paramDiscover = new DefaultParameterNameDiscoverer();


	@Test
	public void simpleArguments() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", String.class, Boolean.class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, "test", true);

		assertEquals("test", context.lookupVariable("a0"));
		assertEquals("test", context.lookupVariable("p0"));
		assertEquals("test", context.lookupVariable("foo"));

		assertEquals(true, context.lookupVariable("a1"));
		assertEquals(true, context.lookupVariable("p1"));
		assertEquals(true, context.lookupVariable("flag"));

		assertNull(context.lookupVariable("a2"));
		assertNull(context.lookupVariable("p2"));
	}

	@Test
	public void nullArgument() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", String.class, Boolean.class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, null, null);

		assertNull(context.lookupVariable("a0"));
		assertNull(context.lookupVariable("p0"));
		assertNull(context.lookupVariable("foo"));

		assertNull(context.lookupVariable("a1"));
		assertNull(context.lookupVariable("p1"));
		assertNull(context.lookupVariable("flag"));
	}

	@Test
	public void varArgEmpty() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", Boolean.class, String[].class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, new Object[] {null});

		assertNull(context.lookupVariable("a0"));
		assertNull(context.lookupVariable("p0"));
		assertNull(context.lookupVariable("flag"));

		assertNull(context.lookupVariable("a1"));
		assertNull(context.lookupVariable("p1"));
		assertNull(context.lookupVariable("vararg"));
	}

	@Test
	public void varArgNull() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", Boolean.class, String[].class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, null, null);

		assertNull(context.lookupVariable("a0"));
		assertNull(context.lookupVariable("p0"));
		assertNull(context.lookupVariable("flag"));

		assertNull(context.lookupVariable("a1"));
		assertNull(context.lookupVariable("p1"));
		assertNull(context.lookupVariable("vararg"));
	}

	@Test
	public void varArgSingle() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", Boolean.class, String[].class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, null, "hello");

		assertNull(context.lookupVariable("a0"));
		assertNull(context.lookupVariable("p0"));
		assertNull(context.lookupVariable("flag"));

		assertEquals("hello", context.lookupVariable("a1"));
		assertEquals("hello", context.lookupVariable("p1"));
		assertEquals("hello", context.lookupVariable("vararg"));
	}

	@Test
	public void varArgMultiple() {
		Method method = ReflectionUtils.findMethod(SampleMethods.class, "hello", Boolean.class, String[].class);
		MethodBasedEvaluationContext context = createEvaluationContext(method, null, "hello", "hi");

		assertNull(context.lookupVariable("a0"));
		assertNull(context.lookupVariable("p0"));
		assertNull(context.lookupVariable("flag"));

		assertArrayEquals(new Object[] {"hello", "hi"}, (Object[]) context.lookupVariable("a1"));
		assertArrayEquals(new Object[] {"hello", "hi"}, (Object[]) context.lookupVariable("p1"));
		assertArrayEquals(new Object[] {"hello", "hi"}, (Object[]) context.lookupVariable("vararg"));
	}

	private MethodBasedEvaluationContext createEvaluationContext(Method method, Object... args) {
		return new MethodBasedEvaluationContext(this, method, args, this.paramDiscover);
	}


	@SuppressWarnings("unused")
	private static class SampleMethods {

		private void hello(String foo, Boolean flag) {
		}

		private void hello(Boolean flag, String... vararg){
		}
	}

}
