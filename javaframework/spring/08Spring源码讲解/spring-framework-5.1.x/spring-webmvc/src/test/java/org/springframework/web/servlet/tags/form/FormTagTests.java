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

package org.springframework.web.servlet.tags.form;

import java.util.Collections;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.junit.Test;

import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Rob Harrop
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Scott Andrews
 * @author Jeremy Grelle
 * @author Rossen Stoyanchev
 */
public class FormTagTests extends AbstractHtmlElementTagTests {

	private static final String REQUEST_URI = "/my/form";

	private static final String QUERY_STRING = "foo=bar";

	private FormTag tag;

	private MockHttpServletRequest request;


	@Override
	@SuppressWarnings("serial")
	protected void onSetUp() {
		this.tag = new FormTag() {
			@Override
			protected TagWriter createTagWriter() {
				return new TagWriter(getWriter());
			}
		};
		this.tag.setPageContext(getPageContext());
	}

	@Override
	protected void extendRequest(MockHttpServletRequest request) {
		request.setRequestURI(REQUEST_URI);
		request.setQueryString(QUERY_STRING);
		this.request = request;
	}

	@Test
	public void writeForm() throws Exception {
		String commandName = "myCommand";
		String name = "formName";
		String action = "/form.html";
		String method = "POST";
		String target = "myTarget";
		String enctype = "my/enctype";
		String acceptCharset = "iso-8859-1";
		String onsubmit = "onsubmit";
		String onreset = "onreset";
		String autocomplete = "off";
		String cssClass = "myClass";
		String cssStyle = "myStyle";
		String dynamicAttribute1 = "attr1";
		String dynamicAttribute2 = "attr2";

		this.tag.setName(name);
		this.tag.setCssClass(cssClass);
		this.tag.setCssStyle(cssStyle);
		this.tag.setModelAttribute(commandName);
		this.tag.setAction(action);
		this.tag.setMethod(method);
		this.tag.setTarget(target);
		this.tag.setEnctype(enctype);
		this.tag.setAcceptCharset(acceptCharset);
		this.tag.setOnsubmit(onsubmit);
		this.tag.setOnreset(onreset);
		this.tag.setAutocomplete(autocomplete);
		this.tag.setDynamicAttribute(null, dynamicAttribute1, dynamicAttribute1);
		this.tag.setDynamicAttribute(null, dynamicAttribute2, dynamicAttribute2);

		int result = this.tag.doStartTag();
		assertEquals(Tag.EVAL_BODY_INCLUDE, result);
		assertEquals("Form attribute not exposed", commandName,
				getPageContext().getRequest().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME));

		result = this.tag.doEndTag();
		assertEquals(Tag.EVAL_PAGE, result);

		this.tag.doFinally();
		assertNull("Form attribute not cleared after tag ends",
				getPageContext().getRequest().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME));

		String output = getOutput();
		assertFormTagOpened(output);
		assertFormTagClosed(output);

		assertContainsAttribute(output, "class", cssClass);
		assertContainsAttribute(output, "style", cssStyle);
		assertContainsAttribute(output, "action", action);
		assertContainsAttribute(output, "method", method);
		assertContainsAttribute(output, "target", target);
		assertContainsAttribute(output, "enctype", enctype);
		assertContainsAttribute(output, "accept-charset", acceptCharset);
		assertContainsAttribute(output, "onsubmit", onsubmit);
		assertContainsAttribute(output, "onreset", onreset);
		assertContainsAttribute(output, "autocomplete", autocomplete);
		assertContainsAttribute(output, "id", commandName);
		assertContainsAttribute(output, "name", name);
		assertContainsAttribute(output, dynamicAttribute1, dynamicAttribute1);
		assertContainsAttribute(output, dynamicAttribute2, dynamicAttribute2);
	}

	@Test
	public void withActionFromRequest() throws Exception {
		String commandName = "myCommand";
		String enctype = "my/enctype";
		String method = "POST";
		String onsubmit = "onsubmit";
		String onreset = "onreset";

		this.tag.setModelAttribute(commandName);
		this.tag.setMethod(method);
		this.tag.setEnctype(enctype);
		this.tag.setOnsubmit(onsubmit);
		this.tag.setOnreset(onreset);

		int result = this.tag.doStartTag();
		assertEquals(Tag.EVAL_BODY_INCLUDE, result);
		assertEquals("Form attribute not exposed", commandName,
				getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		result = this.tag.doEndTag();
		assertEquals(Tag.EVAL_PAGE, result);

		this.tag.doFinally();
		assertNull("Form attribute not cleared after tag ends",
				getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		String output = getOutput();
		assertFormTagOpened(output);
		assertFormTagClosed(output);

		assertContainsAttribute(output, "action", REQUEST_URI + "?" + QUERY_STRING);
		assertContainsAttribute(output, "method", method);
		assertContainsAttribute(output, "enctype", enctype);
		assertContainsAttribute(output, "onsubmit", onsubmit);
		assertContainsAttribute(output, "onreset", onreset);
		assertAttributeNotPresent(output, "name");
	}

	@Test
	public void prependServletPath() throws Exception {

		this.request.setContextPath("/myApp");
		this.request.setServletPath("/main");
		this.request.setPathInfo("/index.html");

		String commandName = "myCommand";
		String action = "/form.html";
		String enctype = "my/enctype";
		String method = "POST";
		String onsubmit = "onsubmit";
		String onreset = "onreset";

		this.tag.setModelAttribute(commandName);
		this.tag.setServletRelativeAction(action);
		this.tag.setMethod(method);
		this.tag.setEnctype(enctype);
		this.tag.setOnsubmit(onsubmit);
		this.tag.setOnreset(onreset);

		int result = this.tag.doStartTag();
		assertEquals(Tag.EVAL_BODY_INCLUDE, result);
		assertEquals("Form attribute not exposed", commandName,
				getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		result = this.tag.doEndTag();
		assertEquals(Tag.EVAL_PAGE, result);

		this.tag.doFinally();
		assertNull("Form attribute not cleared after tag ends",
				getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));

		String output = getOutput();
		assertFormTagOpened(output);
		assertFormTagClosed(output);

		assertContainsAttribute(output, "action", "/myApp/main/form.html");
		assertContainsAttribute(output, "method", method);
		assertContainsAttribute(output, "enctype", enctype);
		assertContainsAttribute(output, "onsubmit", onsubmit);
		assertContainsAttribute(output, "onreset", onreset);
		assertAttributeNotPresent(output, "name");
	}

	@Test
	public void withNullResolvedCommand() throws Exception {
		try {
			tag.setModelAttribute(null);
			tag.doStartTag();
			fail("Must not be able to have a command name that resolves to null");
		}
		catch (IllegalArgumentException ex) {
			// expected
		}
	}

	/**
	 * https://jira.spring.io/browse/SPR-2645
	 */
	@Test
	public void xssExploitWhenActionIsResolvedFromQueryString() throws Exception {
		String xssQueryString = QUERY_STRING + "&stuff=\"><script>alert('XSS!')</script>";
		request.setQueryString(xssQueryString);
		tag.doStartTag();
		assertEquals("<form id=\"command\" action=\"/my/form?foo=bar&amp;stuff=&quot;&gt;&lt;" +
						"script&gt;alert(&#39;XSS!&#39;)&lt;/script&gt;\" method=\"post\">", getOutput());
	}

	@Test
	public void get() throws Exception {
		this.tag.setMethod("get");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);
		String inputOutput = getInputTag(output);

		assertContainsAttribute(formOutput, "method", "get");
		assertEquals("", inputOutput);
	}

	@Test
	public void post() throws Exception {
		this.tag.setMethod("post");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);
		String inputOutput = getInputTag(output);

		assertContainsAttribute(formOutput, "method", "post");
		assertEquals("", inputOutput);
	}

	@Test
	public void put() throws Exception {
		this.tag.setMethod("put");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);
		String inputOutput = getInputTag(output);

		assertContainsAttribute(formOutput, "method", "post");
		assertContainsAttribute(inputOutput, "name", "_method");
		assertContainsAttribute(inputOutput, "value", "put");
		assertContainsAttribute(inputOutput, "type", "hidden");
	}

	@Test
	public void delete() throws Exception {
		this.tag.setMethod("delete");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);
		String inputOutput = getInputTag(output);

		assertContainsAttribute(formOutput, "method", "post");
		assertContainsAttribute(inputOutput, "name", "_method");
		assertContainsAttribute(inputOutput, "value", "delete");
		assertContainsAttribute(inputOutput, "type", "hidden");
	}

	@Test
	public void customMethodParameter() throws Exception {
		this.tag.setMethod("put");
		this.tag.setMethodParam("methodParameter");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);
		String inputOutput = getInputTag(output);

		assertContainsAttribute(formOutput, "method", "post");
		assertContainsAttribute(inputOutput, "name", "methodParameter");
		assertContainsAttribute(inputOutput, "value", "put");
		assertContainsAttribute(inputOutput, "type", "hidden");
	}

	@Test
	public void clearAttributesOnFinally() throws Exception {
		this.tag.setModelAttribute("model");
		getPageContext().setAttribute("model", "foo bar");
		assertNull(getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));
		this.tag.doStartTag();
		assertNotNull(getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));
		this.tag.doFinally();
		assertNull(getPageContext().getAttribute(FormTag.MODEL_ATTRIBUTE_VARIABLE_NAME, PageContext.REQUEST_SCOPE));
	}

	@Test
	public void requestDataValueProcessorHooks() throws Exception {
		String action = "/my/form?foo=bar";
		RequestDataValueProcessor processor = getMockRequestDataValueProcessor();
		given(processor.processAction(this.request, action, "post")).willReturn(action);
		given(processor.getExtraHiddenFields(this.request)).willReturn(Collections.singletonMap("key", "value"));

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();

		assertEquals("<div>\n<input type=\"hidden\" name=\"key\" value=\"value\" />\n</div>", getInputTag(output));
		assertFormTagOpened(output);
		assertFormTagClosed(output);
	}

	@Test
	public void defaultActionEncoded() throws Exception {

		this.request.setRequestURI("/a b c");
		request.setQueryString("");

		this.tag.doStartTag();
		this.tag.doEndTag();
		this.tag.doFinally();

		String output = getOutput();
		String formOutput = getFormTag(output);

		assertContainsAttribute(formOutput, "action", "/a%20b%20c");
	}

	private String getFormTag(String output) {
		int inputStart = output.indexOf("<", 1);
		int inputEnd = output.lastIndexOf(">", output.length() - 2);
		return output.substring(0, inputStart) + output.substring(inputEnd + 1);
	}

	private String getInputTag(String output) {
		int inputStart = output.indexOf("<", 1);
		int inputEnd = output.lastIndexOf(">", output.length() - 2);
		return output.substring(inputStart, inputEnd + 1);
	}


	private static void assertFormTagOpened(String output) {
		assertTrue(output.startsWith("<form "));
	}

	private static void assertFormTagClosed(String output) {
		assertTrue(output.endsWith("</form>"));
	}

}
