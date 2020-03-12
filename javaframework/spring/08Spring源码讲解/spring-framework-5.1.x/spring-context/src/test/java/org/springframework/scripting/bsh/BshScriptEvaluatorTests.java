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

package org.springframework.scripting.bsh;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.ScriptEvaluator;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.scripting.support.StaticScriptSource;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 */
public class BshScriptEvaluatorTests {

	@Test
	public void testBshScriptFromString() {
		ScriptEvaluator evaluator = new BshScriptEvaluator();
		Object result = evaluator.evaluate(new StaticScriptSource("return 3 * 2;"));
		assertEquals(6, result);
	}

	@Test
	public void testBshScriptFromFile() {
		ScriptEvaluator evaluator = new BshScriptEvaluator();
		Object result = evaluator.evaluate(new ResourceScriptSource(new ClassPathResource("simple.bsh", getClass())));
		assertEquals(6, result);
	}

	@Test
	public void testGroovyScriptWithArguments() {
		ScriptEvaluator evaluator = new BshScriptEvaluator();
		Map<String, Object> arguments = new HashMap<>();
		arguments.put("a", 3);
		arguments.put("b", 2);
		Object result = evaluator.evaluate(new StaticScriptSource("return a * b;"), arguments);
		assertEquals(6, result);
	}

}
