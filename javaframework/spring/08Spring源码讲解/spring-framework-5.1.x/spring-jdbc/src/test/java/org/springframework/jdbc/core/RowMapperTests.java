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

package org.springframework.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

/**
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 02.08.2004
 */
public class RowMapperTests {

	private final Connection connection = mock(Connection.class);

	private final Statement statement = mock(Statement.class);

	private final PreparedStatement preparedStatement = mock(PreparedStatement.class);

	private final ResultSet resultSet = mock(ResultSet.class);

	private final JdbcTemplate template = new JdbcTemplate();

	private final RowMapper<TestBean> testRowMapper =
			(rs, rowNum) -> new TestBean(rs.getString(1), rs.getInt(2));

	private List<TestBean> result;

	@Before
	public void setUp() throws SQLException {
		given(connection.createStatement()).willReturn(statement);
		given(connection.prepareStatement(anyString())).willReturn(preparedStatement);
		given(statement.executeQuery(anyString())).willReturn(resultSet);
		given(preparedStatement.executeQuery()).willReturn(resultSet);
		given(resultSet.next()).willReturn(true, true, false);
		given(resultSet.getString(1)).willReturn("tb1", "tb2");
		given(resultSet.getInt(2)).willReturn(1, 2);

		template.setDataSource(new SingleConnectionDataSource(connection, false));
		template.setExceptionTranslator(new SQLStateSQLExceptionTranslator());
		template.afterPropertiesSet();
	}

	@After
	public void verifyClosed() throws Exception {
		verify(resultSet).close();
		// verify(connection).close();
	}

	@After
	public void verifyResults() {
		assertNotNull(result);
		assertEquals(2, result.size());
		TestBean testBean1 = result.get(0);
		TestBean testBean2 = result.get(1);
		assertEquals("tb1", testBean1.getName());
		assertEquals("tb2", testBean2.getName());
		assertEquals(1, testBean1.getAge());
		assertEquals(2, testBean2.getAge());
	}

	@Test
	public void staticQueryWithRowMapper() throws SQLException {
		result = template.query("some SQL", testRowMapper);
		verify(statement).close();
	}

	@Test
	public void preparedStatementCreatorWithRowMapper() throws SQLException {
		result = template.query(con -> preparedStatement, testRowMapper);
		verify(preparedStatement).close();
	}

	@Test
	public void preparedStatementSetterWithRowMapper() throws SQLException {
		result = template.query("some SQL", ps -> ps.setString(1, "test"), testRowMapper);
		verify(preparedStatement).setString(1, "test");
		verify(preparedStatement).close();
	}

	@Test
	public void queryWithArgsAndRowMapper() throws SQLException {
		result = template.query("some SQL", new Object[] { "test1", "test2" }, testRowMapper);
		preparedStatement.setString(1, "test1");
		preparedStatement.setString(2, "test2");
		preparedStatement.close();
	}

	@Test
	public void queryWithArgsAndTypesAndRowMapper() throws SQLException {
		result = template.query("some SQL",
				new Object[] { "test1", "test2" },
				new int[] { Types.VARCHAR, Types.VARCHAR },
				testRowMapper);
		verify(preparedStatement).setString(1, "test1");
		verify(preparedStatement).setString(2, "test2");
		verify(preparedStatement).close();
	}

}
