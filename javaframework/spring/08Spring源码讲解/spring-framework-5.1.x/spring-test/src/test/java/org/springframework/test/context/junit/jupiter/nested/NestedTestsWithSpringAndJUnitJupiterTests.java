/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.test.context.junit.jupiter.nested;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.SpringJUnitJupiterTestSuite;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.nested.NestedTestsWithSpringAndJUnitJupiterTests.TopLevelConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests that verify support for {@code @Nested} test classes
 * in conjunction with the {@link SpringExtension} in a JUnit Jupiter environment.
 *
 * <p>To run these tests in an IDE that does not have built-in support for the JUnit
 * Platform, simply run {@link SpringJUnitJupiterTestSuite} as a JUnit 4 test.
 *
 * @author Sam Brannen
 * @since 5.0
 * @see NestedTestsWithConstructorInjectionWithSpringAndJUnitJupiterTests
 * @see org.springframework.test.context.junit4.nested.NestedTestsWithSpringRulesTests
 */
@SpringJUnitConfig(TopLevelConfig.class)
class NestedTestsWithSpringAndJUnitJupiterTests {

	@Autowired
	String foo;


	@Test
	void topLevelTest() {
		assertEquals("foo", foo);
	}


	@Nested
	@SpringJUnitConfig(NestedConfig.class)
	class NestedTests {

		@Autowired
		String bar;


		@Test
		void nestedTest() throws Exception {
			// In contrast to nested test classes running in JUnit 4, the foo
			// field in the outer instance should have been injected from the
			// test ApplicationContext for the outer instance.
			assertEquals("foo", foo);
			assertEquals("bar", bar);
		}
	}

	// -------------------------------------------------------------------------

	@Configuration
	static class TopLevelConfig {

		@Bean
		String foo() {
			return "foo";
		}
	}

	@Configuration
	static class NestedConfig {

		@Bean
		String bar() {
			return "bar";
		}
	}

}
