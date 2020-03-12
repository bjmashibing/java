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

package org.springframework.web.context.support;

import java.io.File;

import org.junit.Test;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.web.util.WebUtils;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 28.08.2003
 */
public class WebApplicationObjectSupportTests {

	@Test
	@SuppressWarnings("resource")
	public void testWebApplicationObjectSupport() {
		StaticWebApplicationContext wac = new StaticWebApplicationContext();
		wac.setServletContext(new MockServletContext());
		File tempDir = new File("");
		wac.getServletContext().setAttribute(WebUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, tempDir);
		wac.registerBeanDefinition("test", new RootBeanDefinition(TestWebApplicationObject.class));
		wac.refresh();
		WebApplicationObjectSupport wao = (WebApplicationObjectSupport) wac.getBean("test");
		assertEquals(wao.getServletContext(), wac.getServletContext());
		assertEquals(wao.getTempDir(), tempDir);
	}

	@Test
	@SuppressWarnings("resource")
	public void testWebApplicationObjectSupportWithWrongContext() {
		StaticApplicationContext ac = new StaticApplicationContext();
		ac.registerBeanDefinition("test", new RootBeanDefinition(TestWebApplicationObject.class));
		WebApplicationObjectSupport wao = (WebApplicationObjectSupport) ac.getBean("test");
		try {
			wao.getWebApplicationContext();
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
	}


	public static class TestWebApplicationObject extends WebApplicationObjectSupport {
	}

}
