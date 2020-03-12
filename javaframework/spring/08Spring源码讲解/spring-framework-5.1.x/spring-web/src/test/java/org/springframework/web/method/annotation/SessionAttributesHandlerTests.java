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

package org.springframework.web.method.annotation;


import java.util.HashSet;

import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.tests.sample.beans.TestBean;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.DefaultSessionAttributeStore;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test fixture with {@link SessionAttributesHandler}.
 * @author Rossen Stoyanchev
 */
public class SessionAttributesHandlerTests {

	private final SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();

	private final SessionAttributesHandler sessionAttributesHandler = new SessionAttributesHandler(
		SessionAttributeHandler.class, sessionAttributeStore);

	private final NativeWebRequest request = new ServletWebRequest(new MockHttpServletRequest());


	@Test
	public void isSessionAttribute() throws Exception {
		assertTrue(sessionAttributesHandler.isHandlerSessionAttribute("attr1", String.class));
		assertTrue(sessionAttributesHandler.isHandlerSessionAttribute("attr2", String.class));
		assertTrue(sessionAttributesHandler.isHandlerSessionAttribute("simple", TestBean.class));
		assertFalse(sessionAttributesHandler.isHandlerSessionAttribute("simple", String.class));
	}

	@Test
	public void retrieveAttributes() throws Exception {
		sessionAttributeStore.storeAttribute(request, "attr1", "value1");
		sessionAttributeStore.storeAttribute(request, "attr2", "value2");
		sessionAttributeStore.storeAttribute(request, "attr3", new TestBean());
		sessionAttributeStore.storeAttribute(request, "attr4", new TestBean());

		assertEquals("Named attributes (attr1, attr2) should be 'known' right away",
				new HashSet<>(asList("attr1", "attr2")),
				sessionAttributesHandler.retrieveAttributes(request).keySet());

		// Resolve 'attr3' by type
		sessionAttributesHandler.isHandlerSessionAttribute("attr3", TestBean.class);

		assertEquals("Named attributes (attr1, attr2) and resolved attribute (att3) should be 'known'",
				new HashSet<>(asList("attr1", "attr2", "attr3")),
				sessionAttributesHandler.retrieveAttributes(request).keySet());
	}

	@Test
	public void cleanupAttributes() throws Exception {
		sessionAttributeStore.storeAttribute(request, "attr1", "value1");
		sessionAttributeStore.storeAttribute(request, "attr2", "value2");
		sessionAttributeStore.storeAttribute(request, "attr3", new TestBean());

		sessionAttributesHandler.cleanupAttributes(request);

		assertNull(sessionAttributeStore.retrieveAttribute(request, "attr1"));
		assertNull(sessionAttributeStore.retrieveAttribute(request, "attr2"));
		assertNotNull(sessionAttributeStore.retrieveAttribute(request, "attr3"));

		// Resolve 'attr3' by type
		sessionAttributesHandler.isHandlerSessionAttribute("attr3", TestBean.class);
		sessionAttributesHandler.cleanupAttributes(request);

		assertNull(sessionAttributeStore.retrieveAttribute(request, "attr3"));
	}

	@Test
	public void storeAttributes() throws Exception {
		ModelMap model = new ModelMap();
		model.put("attr1", "value1");
		model.put("attr2", "value2");
		model.put("attr3", new TestBean());

		sessionAttributesHandler.storeAttributes(request, model);

		assertEquals("value1", sessionAttributeStore.retrieveAttribute(request, "attr1"));
		assertEquals("value2", sessionAttributeStore.retrieveAttribute(request, "attr2"));
		assertTrue(sessionAttributeStore.retrieveAttribute(request, "attr3") instanceof TestBean);
	}


	@SessionAttributes(names = { "attr1", "attr2" }, types = { TestBean.class })
	private static class SessionAttributeHandler {
	}

}
