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

package org.springframework.beans.propertyeditors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test the conversion of Strings to {@link java.util.Properties} objects,
 * and other property editors.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rick Evans
 */
public class PropertiesEditorTests {

	@Test
	public void oneProperty() {
		String s = "foo=bar";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains one entry", p.entrySet().size() == 1);
		assertTrue("foo=bar", p.get("foo").equals("bar"));
	}

	@Test
	public void twoProperties() {
		String s = "foo=bar with whitespace\n" +
			"me=mi";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains two entries", p.entrySet().size() == 2);
		assertTrue("foo=bar with whitespace", p.get("foo").equals("bar with whitespace"));
		assertTrue("me=mi", p.get("me").equals("mi"));
	}

	@Test
	public void handlesEqualsInValue() {
		String s = "foo=bar\n" +
			"me=mi\n" +
			"x=y=z";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains two entries", p.entrySet().size() == 3);
		assertTrue("foo=bar", p.get("foo").equals("bar"));
		assertTrue("me=mi", p.get("me").equals("mi"));
		assertTrue("x='y=z'", p.get("x").equals("y=z"));
	}

	@Test
	public void handlesEmptyProperty() {
		String s = "foo=bar\nme=mi\nx=";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains two entries", p.entrySet().size() == 3);
		assertTrue("foo=bar", p.get("foo").equals("bar"));
		assertTrue("me=mi", p.get("me").equals("mi"));
		assertTrue("x='y=z'", p.get("x").equals(""));
	}

	@Test
	public void handlesEmptyPropertyWithoutEquals() {
		String s = "foo\nme=mi\nx=x";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains three entries", p.entrySet().size() == 3);
		assertTrue("foo is empty", p.get("foo").equals(""));
		assertTrue("me=mi", p.get("me").equals("mi"));
	}

	/**
	 * Comments begin with #
	 */
	@Test
	public void ignoresCommentLinesAndEmptyLines() {
		String s = "#Ignore this comment\n" +
			"foo=bar\n" +
			"#Another=comment more junk /\n" +
			"me=mi\n" +
			"x=x\n" +
			"\n";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains three entries", p.entrySet().size() == 3);
		assertTrue("foo is bar", p.get("foo").equals("bar"));
		assertTrue("me=mi", p.get("me").equals("mi"));
	}

	/**
	 * We'll typically align by indenting with tabs or spaces.
	 * These should be ignored if at the beginning of a line.
	 * We must ensure that comment lines beginning with whitespace are
	 * still ignored: The standard syntax doesn't allow this on JDK 1.3.
	 */
	@Test
	public void ignoresLeadingSpacesAndTabs() {
		String s = "    #Ignore this comment\n" +
			"\t\tfoo=bar\n" +
			"\t#Another comment more junk \n" +
			" me=mi\n" +
			"x=x\n" +
			"\n";
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(s);
		Properties p = (Properties) pe.getValue();
		assertTrue("contains 3 entries, not " + p.size(), p.size() == 3);
		assertTrue("foo is bar", p.get("foo").equals("bar"));
		assertTrue("me=mi", p.get("me").equals("mi"));
	}

	@Test
	public void nullValue() {
		PropertiesEditor pe= new PropertiesEditor();
		pe.setAsText(null);
		Properties p = (Properties) pe.getValue();
		assertEquals(0, p.size());
	}

	@Test
	public void emptyString() {
		PropertiesEditor pe = new PropertiesEditor();
		pe.setAsText("");
		Properties p = (Properties) pe.getValue();
		assertTrue("empty string means empty properties", p.isEmpty());
	}

	@Test
	public void usingMapAsValueSource() throws Exception {
		Map<String, String> map = new HashMap<>();
		map.put("one", "1");
		map.put("two", "2");
		map.put("three", "3");
		PropertiesEditor pe = new PropertiesEditor();
		pe.setValue(map);
		Object value = pe.getValue();
		assertNotNull(value);
		assertTrue(value instanceof Properties);
		Properties props = (Properties) value;
		assertEquals(3, props.size());
		assertEquals("1", props.getProperty("one"));
		assertEquals("2", props.getProperty("two"));
		assertEquals("3", props.getProperty("three"));
	}

}
