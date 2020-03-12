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

package org.springframework.test.web.servlet.result;

import org.junit.Test;

import org.springframework.test.web.servlet.StubMvcResult;
import org.springframework.web.servlet.FlashMap;

/**
 * @author Craig Walls
 */
public class FlashAttributeResultMatchersTests {

	@Test
	public void attributeExists() throws Exception {
		new FlashAttributeResultMatchers().attributeExists("good").match(getStubMvcResult());
	}

	@Test(expected = AssertionError.class)
	public void attributeExists_doesntExist() throws Exception {
		new FlashAttributeResultMatchers().attributeExists("bad").match(getStubMvcResult());
	}

	@Test
	public void attribute() throws Exception {
		new FlashAttributeResultMatchers().attribute("good", "good").match(getStubMvcResult());
	}

	@Test(expected = AssertionError.class)
	public void attribute_incorrectValue() throws Exception {
		new FlashAttributeResultMatchers().attribute("good", "not good").match(getStubMvcResult());
	}

	private StubMvcResult getStubMvcResult() {
		FlashMap flashMap = new FlashMap();
		flashMap.put("good", "good");
		StubMvcResult mvcResult = new StubMvcResult(null, null, null, null, null, flashMap, null);
		return mvcResult;
	}

}
