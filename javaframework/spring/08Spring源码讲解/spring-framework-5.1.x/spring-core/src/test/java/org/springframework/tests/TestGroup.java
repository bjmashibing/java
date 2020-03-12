/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.tests;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.StringUtils;

import static java.lang.String.*;

/**
 * A test group used to limit when certain tests are run.
 *
 * @see Assume#group(TestGroup)
 * @author Phillip Webb
 * @author Chris Beams
 * @author Sam Brannen
 */
public enum TestGroup {

	/**
	 * Tests that take a considerable amount of time to run. Any test lasting longer than
	 * 500ms should be considered a candidate in order to avoid making the overall test
	 * suite too slow to run during the normal development cycle.
	 */
	LONG_RUNNING,

	/**
	 * Performance-related tests that may fail unpredictably based on CPU profile and load.
	 * Any test using {@link Thread#sleep}, {@link Object#wait}, Spring's
	 * {@code StopWatch}, etc. should be considered a candidate as their successful
	 * execution is likely to be based on events occurring within a given time window.
	 */
	PERFORMANCE,

	/**
	 * Tests requiring the presence of jmxremote_optional.jar in jre/lib/ext in order to
	 * avoid "Unsupported protocol: jmxmp" errors.
	 */
	JMXMP,

	/**
	 * Tests that should only be run on the continuous integration server.
	 */
	CI;


	/**
	 * Parse the specified comma separated string of groups.
	 * @param value the comma separated string of groups
	 * @return a set of groups
	 * @throws IllegalArgumentException if any specified group name is not a
	 * valid {@link TestGroup}
	 */
	public static Set<TestGroup> parse(String value) throws IllegalArgumentException {
		if (!StringUtils.hasText(value)) {
			return Collections.emptySet();
		}
		String originalValue = value;
		value = value.trim();
		if ("ALL".equalsIgnoreCase(value)) {
			return EnumSet.allOf(TestGroup.class);
		}
		if (value.toUpperCase().startsWith("ALL-")) {
			Set<TestGroup> groups = EnumSet.allOf(TestGroup.class);
			groups.removeAll(parseGroups(originalValue, value.substring(4)));
			return groups;
		}
		return parseGroups(originalValue, value);
	}

	private static Set<TestGroup> parseGroups(String originalValue, String value) throws IllegalArgumentException {
		Set<TestGroup> groups = new HashSet<>();
		for (String group : value.split(",")) {
			try {
				groups.add(valueOf(group.trim().toUpperCase()));
			}
			catch (IllegalArgumentException ex) {
				throw new IllegalArgumentException(format(
						"Unable to find test group '%s' when parsing testGroups value: '%s'. " +
						"Available groups include: [%s]", group.trim(), originalValue,
						StringUtils.arrayToCommaDelimitedString(TestGroup.values())));
			}
		}
		return groups;
	}

}
