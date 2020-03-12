/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.test.context.web;

import java.io.File;

import javax.servlet.ServletContext;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.ServletWebRequest;

import static org.junit.Assert.*;

/**
 * @author Sam Brannen
 * @since 3.2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public abstract class AbstractBasicWacTests implements ServletContextAware {

	protected ServletContext servletContext;

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	protected MockServletContext mockServletContext;

	@Autowired
	protected MockHttpServletRequest request;

	@Autowired
	protected MockHttpServletResponse response;

	@Autowired
	protected MockHttpSession session;

	@Autowired
	protected ServletWebRequest webRequest;

	@Autowired
	protected String foo;


	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Test
	public void basicWacFeatures() throws Exception {
		assertNotNull("ServletContext should be set in the WAC.", wac.getServletContext());

		assertNotNull("ServletContext should have been set via ServletContextAware.", servletContext);

		assertNotNull("ServletContext should have been autowired from the WAC.", mockServletContext);
		assertNotNull("MockHttpServletRequest should have been autowired from the WAC.", request);
		assertNotNull("MockHttpServletResponse should have been autowired from the WAC.", response);
		assertNotNull("MockHttpSession should have been autowired from the WAC.", session);
		assertNotNull("ServletWebRequest should have been autowired from the WAC.", webRequest);

		Object rootWac = mockServletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		assertNotNull("Root WAC must be stored in the ServletContext as: "
				+ WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, rootWac);
		assertSame("test WAC and Root WAC in ServletContext must be the same object.", wac, rootWac);
		assertSame("ServletContext instances must be the same object.", mockServletContext, wac.getServletContext());
		assertSame("ServletContext in the WAC and in the mock request", mockServletContext, request.getServletContext());

		assertEquals("Getting real path for ServletContext resource.",
			new File("src/main/webapp/index.jsp").getCanonicalPath(), mockServletContext.getRealPath("index.jsp"));

	}

}
