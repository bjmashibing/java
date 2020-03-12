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

package org.springframework.web.servlet.mvc.method.annotation;

import java.lang.reflect.Method;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * Base class for {@code @RequestAttribute} and {@code @SessionAttribute} method
 * method argument resolution tests.
 *
 * @author Rossen Stoyanchev
 * @since 4.3
 */
public abstract class AbstractRequestAttributesArgumentResolverTests {

	private ServletWebRequest webRequest;

	private HandlerMethodArgumentResolver resolver;

	private Method handleMethod;


	@Before
	public void setup() throws Exception {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		this.webRequest = new ServletWebRequest(request, response);

		this.resolver = createResolver();

		this.handleMethod = AbstractRequestAttributesArgumentResolverTests.class
				.getDeclaredMethod(getHandleMethodName(), Foo.class, Foo.class, Foo.class, Optional.class);
	}


	protected abstract HandlerMethodArgumentResolver createResolver();

	protected abstract String getHandleMethodName();

	protected abstract int getScope();


	@Test
	public void supportsParameter() throws Exception {
		assertTrue(this.resolver.supportsParameter(new MethodParameter(this.handleMethod, 0)));
		assertFalse(this.resolver.supportsParameter(new MethodParameter(this.handleMethod, -1)));
	}

	@Test
	public void resolve() throws Exception {
		MethodParameter param = initMethodParameter(0);
		try {
			testResolveArgument(param);
			fail("Should be required by default");
		}
		catch (ServletRequestBindingException ex) {
			assertTrue(ex.getMessage().startsWith("Missing "));
		}

		Foo foo = new Foo();
		this.webRequest.setAttribute("foo", foo, getScope());
		assertSame(foo, testResolveArgument(param));
	}

	@Test
	public void resolveWithName() throws Exception {
		MethodParameter param = initMethodParameter(1);
		Foo foo = new Foo();
		this.webRequest.setAttribute("specialFoo", foo, getScope());
		assertSame(foo, testResolveArgument(param));
	}

	@Test
	public void resolveNotRequired() throws Exception {
		MethodParameter param = initMethodParameter(2);
		assertNull(testResolveArgument(param));

		Foo foo = new Foo();
		this.webRequest.setAttribute("foo", foo, getScope());
		assertSame(foo, testResolveArgument(param));
	}

	@Test
	public void resolveOptional() throws Exception {
		WebDataBinder dataBinder = new WebRequestDataBinder(null);
		dataBinder.setConversionService(new DefaultConversionService());
		WebDataBinderFactory factory = mock(WebDataBinderFactory.class);
		given(factory.createBinder(this.webRequest, null, "foo")).willReturn(dataBinder);

		MethodParameter param = initMethodParameter(3);
		Object actual = testResolveArgument(param, factory);
		assertNotNull(actual);
		assertEquals(Optional.class, actual.getClass());
		assertFalse(((Optional<?>) actual).isPresent());

		Foo foo = new Foo();
		this.webRequest.setAttribute("foo", foo, getScope());

		actual = testResolveArgument(param, factory);
		assertNotNull(actual);
		assertEquals(Optional.class, actual.getClass());
		assertTrue(((Optional<?>) actual).isPresent());
		assertSame(foo, ((Optional<?>) actual).get());
	}

	private Object testResolveArgument(MethodParameter param) throws Exception {
		return testResolveArgument(param, null);
	}

	private Object testResolveArgument(MethodParameter param, WebDataBinderFactory factory) throws Exception {
		ModelAndViewContainer mavContainer = new ModelAndViewContainer();
		return this.resolver.resolveArgument(param, mavContainer, this.webRequest, factory);
	}

	private MethodParameter initMethodParameter(int parameterIndex) {
		MethodParameter param = new SynthesizingMethodParameter(this.handleMethod, parameterIndex);
		param.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
		GenericTypeResolver.resolveParameterType(param, this.resolver.getClass());
		return param;
	}


	@SuppressWarnings("unused")
	private void handleWithRequestAttribute(
			@RequestAttribute Foo foo,
			@RequestAttribute("specialFoo") Foo namedFoo,
			@RequestAttribute(name="foo", required = false) Foo notRequiredFoo,
			@RequestAttribute(name="foo") Optional<Foo> optionalFoo) {
	}

	@SuppressWarnings("unused")
	private void handleWithSessionAttribute(
			@SessionAttribute Foo foo,
			@SessionAttribute("specialFoo") Foo namedFoo,
			@SessionAttribute(name="foo", required = false) Foo notRequiredFoo,
			@SessionAttribute(name="foo") Optional<Foo> optionalFoo) {
	}


	private static class Foo {
	}

}
