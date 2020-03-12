/*
 * Copyright 2002-2014 the original author or authors.
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

package org.springframework.jdbc.datasource.lookup;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Rick Evans
 * @author Chris Beams
 */
public class JndiDataSourceLookupTests {

	private static final String DATA_SOURCE_NAME = "Love is like a stove, burns you when it's hot";

	@Test
	public void testSunnyDay() throws Exception {
		final DataSource expectedDataSource = new StubDataSource();
		JndiDataSourceLookup lookup = new JndiDataSourceLookup() {
			@Override
			protected <T> T lookup(String jndiName, Class<T> requiredType) {
				assertEquals(DATA_SOURCE_NAME, jndiName);
				return requiredType.cast(expectedDataSource);
			}
		};
		DataSource dataSource = lookup.getDataSource(DATA_SOURCE_NAME);
		assertNotNull("A DataSourceLookup implementation must *never* return null from getDataSource(): this one obviously (and incorrectly) is", dataSource);
		assertSame(expectedDataSource, dataSource);
	}

	@Test(expected = DataSourceLookupFailureException.class)
	public void testNoDataSourceAtJndiLocation() throws Exception {
		JndiDataSourceLookup lookup = new JndiDataSourceLookup() {
			@Override
			protected <T> T lookup(String jndiName, Class<T> requiredType) throws NamingException {
				assertEquals(DATA_SOURCE_NAME, jndiName);
				throw new NamingException();
			}
		};
		lookup.getDataSource(DATA_SOURCE_NAME);
	}

}
