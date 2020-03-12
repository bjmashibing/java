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

package org.springframework.web.servlet.view.xml;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.oxm.Marshaller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 */
public class MarshallingViewTests {

	private Marshaller marshallerMock;

	private MarshallingView view;


	@Before
	public void createView() throws Exception {
		marshallerMock = mock(Marshaller.class);
		view = new MarshallingView(marshallerMock);
	}


	@Test
	public void getContentType() {
		assertEquals("Invalid content type", "application/xml", view.getContentType());
	}

	@Test
	public void isExposePathVars() {
		assertEquals("Must not expose path variables", false, view.isExposePathVariables());
	}

	@Test
	public void isExposePathVarsDefaultConstructor() {
		assertEquals("Must not expose path variables", false, new MarshallingView().isExposePathVariables());
	}

	@Test
	public void renderModelKey() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		view.setModelKey(modelKey);
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(Object.class)).willReturn(true);
		marshallerMock.marshal(eq(toBeMarshalled), isA(StreamResult.class));

		view.render(model, request, response);
		assertEquals("Invalid content type", "application/xml", response.getContentType());
		assertEquals("Invalid content length", 0, response.getContentLength());
	}

	@Test
	public void renderModelKeyWithJaxbElement() throws Exception {
		String toBeMarshalled = "value";
		String modelKey = "key";
		view.setModelKey(modelKey);
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, new JAXBElement<>(new QName("model"), String.class, toBeMarshalled));

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(String.class)).willReturn(true);
		marshallerMock.marshal(eq(toBeMarshalled), isA(StreamResult.class));

		view.render(model, request, response);
		assertEquals("Invalid content type", "application/xml", response.getContentType());
		assertEquals("Invalid content length", 0, response.getContentLength());
	}

	@Test
	public void renderInvalidModelKey() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		view.setModelKey("invalidKey");
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		try {
			view.render(model, request, response);
			fail("IllegalStateException expected");
		}
		catch (IllegalStateException ex) {
			// expected
		}
		assertEquals("Invalid content length", 0, response.getContentLength());
	}

	@Test
	public void renderNullModelValue() throws Exception {
		String modelKey = "key";
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, null);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		try {
			view.render(model, request, response);
			fail("IllegalStateException expected");
		}
		catch (IllegalStateException ex) {
			// expected
		}
		assertEquals("Invalid content length", 0, response.getContentLength());
	}

	@Test
	public void renderModelKeyUnsupported() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		view.setModelKey(modelKey);
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(Object.class)).willReturn(false);

		try {
			view.render(model, request, response);
			fail("IllegalStateException expected");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

	@Test
	public void renderNoModelKey() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(Object.class)).willReturn(true);

		view.render(model, request, response);
		assertEquals("Invalid content type", "application/xml", response.getContentType());
		assertEquals("Invalid content length", 0, response.getContentLength());
		verify(marshallerMock).marshal(eq(toBeMarshalled), isA(StreamResult.class));
	}

	@Test
	public void renderNoModelKeyAndBindingResultFirst() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		Map<String, Object> model = new LinkedHashMap<>();
		model.put(BindingResult.MODEL_KEY_PREFIX + modelKey, new BeanPropertyBindingResult(toBeMarshalled, modelKey));
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(BeanPropertyBindingResult.class)).willReturn(true);
		given(marshallerMock.supports(Object.class)).willReturn(true);

		view.render(model, request, response);
		assertEquals("Invalid content type", "application/xml", response.getContentType());
		assertEquals("Invalid content length", 0, response.getContentLength());
		verify(marshallerMock).marshal(eq(toBeMarshalled), isA(StreamResult.class));
	}

	@Test
	public void testRenderUnsupportedModel() throws Exception {
		Object toBeMarshalled = new Object();
		String modelKey = "key";
		Map<String, Object> model = new HashMap<>();
		model.put(modelKey, toBeMarshalled);

		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		given(marshallerMock.supports(Object.class)).willReturn(false);

		try {
			view.render(model, request, response);
			fail("IllegalStateException expected");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}

}
