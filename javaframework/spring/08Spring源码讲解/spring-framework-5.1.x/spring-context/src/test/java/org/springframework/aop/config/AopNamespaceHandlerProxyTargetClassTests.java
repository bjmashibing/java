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

package org.springframework.aop.config;

import org.junit.Test;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.tests.sample.beans.ITestBean;

import static org.junit.Assert.*;

/**
 * @author Rob Harrop
 * @author Chris Beams
 */
public class AopNamespaceHandlerProxyTargetClassTests extends AopNamespaceHandlerTests {

	@Test
	public void testIsClassProxy() {
		ITestBean bean = getTestBean();
		assertTrue("Should be a CGLIB proxy", AopUtils.isCglibProxy(bean));
		assertTrue("Should expose proxy", ((Advised) bean).isExposeProxy());
	}

}
