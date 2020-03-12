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

package org.springframework.web.servlet.handler;

import org.junit.Before;
import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockServletContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import static org.junit.Assert.*;

/**
 * @author Alef Arendsen
 * @author Juergen Hoeller
 */
public class PathMatchingUrlHandlerMappingTests {

	public static final String CONF = "/org/springframework/web/servlet/handler/map3.xml";

	private HandlerMapping hm;

	private ConfigurableWebApplicationContext wac;

	@Before
	public void setUp() throws Exception {
		MockServletContext sc = new MockServletContext("");
		wac = new XmlWebApplicationContext();
		wac.setServletContext(sc);
		wac.setConfigLocations(new String[] {CONF});
		wac.refresh();
		hm = (HandlerMapping) wac.getBean("urlMapping");
	}

	@Test
	public void requestsWithHandlers() throws Exception {
		Object bean = wac.getBean("mainController");

		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/welcome.html");
		HandlerExecutionChain hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/show.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/bookseats.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);
	}

	@Test
	public void actualPathMatching() throws Exception {
		// there a couple of mappings defined with which we can test the
		// path matching, let's do that...

		Object bean = wac.getBean("mainController");
		Object defaultBean = wac.getBean("starController");

		// testing some normal behavior
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/pathmatchingTest.html");
		HandlerExecutionChain hec = getHandler(req);
		assertTrue("Handler is null", hec != null);
		assertTrue("Handler is correct bean", hec.getHandler() == bean);
		assertEquals("/pathmatchingTest.html", req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

		// no match, no forward slash included
		req = new MockHttpServletRequest("GET", "welcome.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);
		assertEquals("welcome.html", req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

		// testing some ????? behavior
		req = new MockHttpServletRequest("GET", "/pathmatchingAA.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);
		assertEquals("pathmatchingAA.html", req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

		// testing some ????? behavior
		req = new MockHttpServletRequest("GET", "/pathmatchingA.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);
		assertEquals("/pathmatchingA.html", req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));

		// testing some ????? behavior
		req = new MockHttpServletRequest("GET", "/administrator/pathmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		// testing simple /**/behavior
		req = new MockHttpServletRequest("GET", "/administrator/test/pathmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		// this should not match because of the administratorT
		req = new MockHttpServletRequest("GET", "/administratort/pathmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		// this should match because of *.jsp
		req = new MockHttpServletRequest("GET", "/bla.jsp");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		// should match because exact pattern is there
		req = new MockHttpServletRequest("GET", "/administrator/another/bla.xml");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		// should not match, because there's not .gif extension in there
		req = new MockHttpServletRequest("GET", "/administrator/another/bla.gif");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		// should match because there testlast* in there
		req = new MockHttpServletRequest("GET", "/administrator/test/testlastbit");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		// but this not, because it's testlast and not testla
		req = new MockHttpServletRequest("GET", "/administrator/test/testla");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/administrator/testing/longer/bla");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/administrator/testing/longer/test.jsp");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/administrator/testing/longer2/notmatching/notmatching");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/shortpattern/testing/toolong");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/XXpathXXmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/pathXXmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/XpathXXmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/XXpathmatching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/show12.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/show123.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/show1.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/reallyGood-test-is-this.jpeg");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/reallyGood-tst-is-this.jpeg");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/testing/test.jpeg");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/testing/test.jpg");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/anotherTest");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);

		req = new MockHttpServletRequest("GET", "/stillAnotherTest");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		// there outofpattern*yeah in the pattern, so this should fail
		req = new MockHttpServletRequest("GET", "/outofpattern*ye");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/test't est/path'm atching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);

		req = new MockHttpServletRequest("GET", "/test%26t%20est/path%26m%20atching.html");
		hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == defaultBean);
	}

	@Test
	public void defaultMapping() throws Exception {
		Object bean = wac.getBean("starController");
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/goggog.html");
		HandlerExecutionChain hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);
	}

	@Test
	public void mappingExposedInRequest() throws Exception {
		Object bean = wac.getBean("mainController");
		MockHttpServletRequest req = new MockHttpServletRequest("GET", "/show.html");
		HandlerExecutionChain hec = getHandler(req);
		assertTrue("Handler is correct bean", hec != null && hec.getHandler() == bean);
		assertEquals("Mapping not exposed", "show.html",
				req.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
	}

	private HandlerExecutionChain getHandler(MockHttpServletRequest req) throws Exception {
		HandlerExecutionChain hec = hm.getHandler(req);
		HandlerInterceptor[] interceptors = hec.getInterceptors();
		if (interceptors != null) {
			for (HandlerInterceptor interceptor : interceptors) {
				interceptor.preHandle(req, null, hec.getHandler());
			}
		}
		return hec;
	}

}
