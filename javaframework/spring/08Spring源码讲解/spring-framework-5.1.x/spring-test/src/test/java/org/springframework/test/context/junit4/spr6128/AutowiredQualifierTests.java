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

package org.springframework.test.context.junit4.spr6128;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;

/**
 * Integration tests to verify claims made in <a
 * href="https://jira.springframework.org/browse/SPR-6128"
 * target="_blank">SPR-6128</a>.
 *
 * @author Sam Brannen
 * @author Chris Beams
 * @since 3.0
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class AutowiredQualifierTests {

	@Autowired
	private String foo;

	@Autowired
	@Qualifier("customFoo")
	private String customFoo;


	@Test
	public void test() {
		assertThat(foo, equalTo("normal"));
		assertThat(customFoo, equalTo("custom"));
	}

}
