/*
 * Copyright 2002-2013 the original author or authors.
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

package org.springframework.ejb.config;

import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.ComponentDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.tests.beans.CollectingReaderEventListener;

import static org.junit.Assert.*;

/**
 * @author Torsten Juergeleit
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class JeeNamespaceHandlerEventTests {

	private CollectingReaderEventListener eventListener = new CollectingReaderEventListener();

	private XmlBeanDefinitionReader reader;

	private DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();


	@Before
	public void setUp() throws Exception {
		this.reader = new XmlBeanDefinitionReader(this.beanFactory);
		this.reader.setEventListener(this.eventListener);
		this.reader.loadBeanDefinitions(new ClassPathResource("jeeNamespaceHandlerTests.xml", getClass()));
	}

	@Test
	public void testJndiLookupComponentEventReceived() {
		ComponentDefinition component = this.eventListener.getComponentDefinition("simple");
		assertTrue(component instanceof BeanComponentDefinition);
	}

	@Test
	public void testLocalSlsbComponentEventReceived() {
		ComponentDefinition component = this.eventListener.getComponentDefinition("simpleLocalEjb");
		assertTrue(component instanceof BeanComponentDefinition);
	}

	@Test
	public void testRemoteSlsbComponentEventReceived() {
		ComponentDefinition component = this.eventListener.getComponentDefinition("simpleRemoteEjb");
		assertTrue(component instanceof BeanComponentDefinition);
	}

}
