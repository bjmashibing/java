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

package org.springframework.util.comparator;

import java.util.Comparator;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Test for {@link CompoundComparator}.
 *
 * @author Keith Donald
 * @author Chris Beams
 * @author Phillip Webb
 */
@Deprecated
public class CompoundComparatorTests {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldNeedAtLeastOneComparator() {
		Comparator<String> c = new CompoundComparator<>();
		thrown.expect(IllegalStateException.class);
		c.compare("foo", "bar");
	}

}
