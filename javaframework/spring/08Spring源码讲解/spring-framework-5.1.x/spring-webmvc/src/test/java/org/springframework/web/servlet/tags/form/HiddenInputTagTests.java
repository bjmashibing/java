/*
 * Copyright 2002-2015 the original author or authors.
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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.junit.Test;

import org.springframework.tests.sample.beans.TestBean;
import org.springframework.validation.BeanPropertyBindingResult;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 */
public class HiddenInputTagTests extends AbstractFormTagTests {

	private HiddenInputTag tag;

	private TestBean bean;

	@Override
	@SuppressWarnings("serial")
	protected void onSetUp() {
		this.tag = new HiddenInputTag() {
			@Override
			protected TagWriter createTagWriter() {
				return new TagWriter(getWriter());
			}
		};
		this.tag.setPageContext(getPageContext());
	}

	@Test
	public void render() throws Exception {
		this.tag.setPath("name");
		int result = this.tag.doStartTag();
		assertEquals(Tag.SKIP_BODY, result);

		String output = getOutput();

		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "type", "hidden");
		assertContainsAttribute(output, "value", "Sally Greenwood");
		assertAttributeNotPresent(output, "disabled");
	}

	@Test
	public void withCustomBinder() throws Exception {
		this.tag.setPath("myFloat");

		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(this.bean, COMMAND_NAME);
		errors.getPropertyAccessor().registerCustomEditor(Float.class, new SimpleFloatEditor());
		exposeBindingResult(errors);

		assertEquals(Tag.SKIP_BODY, this.tag.doStartTag());

		String output = getOutput();

		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "type", "hidden");
		assertContainsAttribute(output, "value", "12.34f");
	}

	@Test
	public void dynamicTypeAttribute() throws JspException {
		try {
			this.tag.setDynamicAttribute(null, "type", "email");
			fail("Expected exception");
		}
		catch (IllegalArgumentException e) {
			assertEquals("Attribute type=\"email\" is not allowed", e.getMessage());
		}
	}

	@Test
	public void disabledTrue() throws Exception {
		this.tag.setDisabled(true);

		this.tag.doStartTag();
		this.tag.doEndTag();

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertContainsAttribute(output, "disabled", "disabled");
	}

	// SPR-8661

	@Test
	public void disabledFalse() throws Exception {
		this.tag.setDisabled(false);

		this.tag.doStartTag();
		this.tag.doEndTag();

		String output = getOutput();
		assertTagOpened(output);
		assertTagClosed(output);

		assertAttributeNotPresent(output, "disabled");
	}

	private void assertTagClosed(String output) {
		assertTrue(output.endsWith("/>"));
	}

	private void assertTagOpened(String output) {
		assertTrue(output.startsWith("<input "));
	}

	@Override
	protected TestBean createTestBean() {
		this.bean = new TestBean();
		bean.setName("Sally Greenwood");
		bean.setMyFloat(new Float("12.34"));
		return bean;
	}

}
