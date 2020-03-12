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

package org.springframework.orm.jpa.persistenceunit;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.Test;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.tests.mock.jndi.SimpleNamingContextBuilder;

import static org.junit.Assert.*;

/**
 * Unit and integration tests for the JPA XML resource parsing support.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @author Nicholas Williams
 */
public class PersistenceXmlParsingTests {

	@Test
	public void testMetaInfCase() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/META-INF/persistence.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);
		assertEquals("OrderManagement", info[0].getPersistenceUnitName());

		assertEquals(2, info[0].getJarFileUrls().size());
		assertEquals(new ClassPathResource("order.jar").getURL(), info[0].getJarFileUrls().get(0));
		assertEquals(new ClassPathResource("order-supplemental.jar").getURL(), info[0].getJarFileUrls().get(1));

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Test
	public void testExample1() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example1.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);
		assertEquals("OrderManagement", info[0].getPersistenceUnitName());

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Test
	public void testExample2() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example2.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);

		assertEquals("OrderManagement2", info[0].getPersistenceUnitName());

		assertEquals(1, info[0].getMappingFileNames().size());
		assertEquals("mappings.xml", info[0].getMappingFileNames().get(0));
		assertEquals(0, info[0].getProperties().keySet().size());

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Test
	public void testExample3() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example3.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);
		assertEquals("OrderManagement3", info[0].getPersistenceUnitName());

		assertEquals(2, info[0].getJarFileUrls().size());
		assertEquals(new ClassPathResource("order.jar").getURL(), info[0].getJarFileUrls().get(0));
		assertEquals(new ClassPathResource("order-supplemental.jar").getURL(), info[0].getJarFileUrls().get(1));

		assertEquals(0, info[0].getProperties().keySet().size());
		assertNull(info[0].getJtaDataSource());
		assertNull(info[0].getNonJtaDataSource());

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Test
	public void testExample4() throws Exception {
		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		DataSource ds = new DriverManagerDataSource();
		builder.bind("java:comp/env/jdbc/MyDB", ds);

		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example4.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);
		assertEquals("OrderManagement4", info[0].getPersistenceUnitName());

		assertEquals(1, info[0].getMappingFileNames().size());
		assertEquals("order-mappings.xml", info[0].getMappingFileNames().get(0));

		assertEquals(3, info[0].getManagedClassNames().size());
		assertEquals("com.acme.Order", info[0].getManagedClassNames().get(0));
		assertEquals("com.acme.Customer", info[0].getManagedClassNames().get(1));
		assertEquals("com.acme.Item", info[0].getManagedClassNames().get(2));

		assertTrue("Exclude unlisted should be true when no value.", info[0].excludeUnlistedClasses());

		assertSame(PersistenceUnitTransactionType.RESOURCE_LOCAL, info[0].getTransactionType());
		assertEquals(0, info[0].getProperties().keySet().size());

		builder.clear();
	}

	@Test
	public void testExample5() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example5.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals(1, info.length);
		assertEquals("OrderManagement5", info[0].getPersistenceUnitName());

		assertEquals(2, info[0].getMappingFileNames().size());
		assertEquals("order1.xml", info[0].getMappingFileNames().get(0));
		assertEquals("order2.xml", info[0].getMappingFileNames().get(1));

		assertEquals(2, info[0].getJarFileUrls().size());
		assertEquals(new ClassPathResource("order.jar").getURL(), info[0].getJarFileUrls().get(0));
		assertEquals(new ClassPathResource("order-supplemental.jar").getURL(), info[0].getJarFileUrls().get(1));

		assertEquals("com.acme.AcmePersistence", info[0].getPersistenceProviderClassName());
		assertEquals(0, info[0].getProperties().keySet().size());

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Test
	public void testExampleComplex() throws Exception {
		DataSource ds = new DriverManagerDataSource();

		String resource = "/org/springframework/orm/jpa/persistence-complex.xml";
		MapDataSourceLookup dataSourceLookup = new MapDataSourceLookup();
		Map<String, DataSource> dataSources = new HashMap<>();
		dataSources.put("jdbc/MyPartDB", ds);
		dataSources.put("jdbc/MyDB", ds);
		dataSourceLookup.setDataSources(dataSources);
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), dataSourceLookup);
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertEquals(2, info.length);

		PersistenceUnitInfo pu1 = info[0];

		assertEquals("pu1", pu1.getPersistenceUnitName());

		assertEquals("com.acme.AcmePersistence", pu1.getPersistenceProviderClassName());

		assertEquals(1, pu1.getMappingFileNames().size());
		assertEquals("ormap2.xml", pu1.getMappingFileNames().get(0));

		assertEquals(1, pu1.getJarFileUrls().size());
		assertEquals(new ClassPathResource("order.jar").getURL(), pu1.getJarFileUrls().get(0));

		assertFalse(pu1.excludeUnlistedClasses());

		assertSame(PersistenceUnitTransactionType.RESOURCE_LOCAL, pu1.getTransactionType());

		Properties props = pu1.getProperties();
		assertEquals(2, props.keySet().size());
		assertEquals("on", props.getProperty("com.acme.persistence.sql-logging"));
		assertEquals("bar", props.getProperty("foo"));

		assertNull(pu1.getNonJtaDataSource());

		assertSame(ds, pu1.getJtaDataSource());

		assertFalse("Exclude unlisted should default false in 1.0.", pu1.excludeUnlistedClasses());

		PersistenceUnitInfo pu2 = info[1];

		assertSame(PersistenceUnitTransactionType.JTA, pu2.getTransactionType());
		assertEquals("com.acme.AcmePersistence", pu2.getPersistenceProviderClassName());

		assertEquals(1, pu2.getMappingFileNames().size());
		assertEquals("order2.xml", pu2.getMappingFileNames().get(0));

		// the following assertions fail only during coverage runs
		// assertEquals(1, pu2.getJarFileUrls().size());
		// assertEquals(new ClassPathResource("order-supplemental.jar").getURL(), pu2.getJarFileUrls().get(0));

		assertTrue(pu2.excludeUnlistedClasses());

		assertNull(pu2.getJtaDataSource());
		assertEquals(ds, pu2.getNonJtaDataSource());

		assertTrue("Exclude unlisted should be true when no value.", pu2.excludeUnlistedClasses());
	}

	@Test
	public void testExample6() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-example6.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);
		assertEquals(1, info.length);
		assertEquals("pu", info[0].getPersistenceUnitName());
		assertEquals(0, info[0].getProperties().keySet().size());

		assertFalse("Exclude unlisted should default false in 1.0.", info[0].excludeUnlistedClasses());
	}

	@Ignore  // not doing schema parsing anymore for JPA 2.0 compatibility
	@Test
	public void testInvalidPersistence() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-invalid.xml";
		try {
			reader.readPersistenceUnitInfos(resource);
			fail("expected invalid document exception");
		}
		catch (RuntimeException expected) {
		}
	}

	@Ignore  // not doing schema parsing anymore for JPA 2.0 compatibility
	@Test
	public void testNoSchemaPersistence() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-no-schema.xml";
		try {
			reader.readPersistenceUnitInfos(resource);
			fail("expected invalid document exception");
		}
		catch (RuntimeException expected) {
		}
	}

	@Test
	public void testPersistenceUnitRootUrl() throws Exception {
		URL url = PersistenceUnitReader.determinePersistenceUnitRootUrl(new ClassPathResource("/org/springframework/orm/jpa/persistence-no-schema.xml"));
		assertNull(url);

		url = PersistenceUnitReader.determinePersistenceUnitRootUrl(new ClassPathResource("/org/springframework/orm/jpa/META-INF/persistence.xml"));
		assertTrue("the containing folder should have been returned", url.toString().endsWith("/org/springframework/orm/jpa"));
	}

	@Test
	public void testPersistenceUnitRootUrlWithJar() throws Exception {
		ClassPathResource archive = new ClassPathResource("/org/springframework/orm/jpa/jpa-archive.jar");
		String newRoot = "jar:" + archive.getURL().toExternalForm() + "!/META-INF/persist.xml";
		Resource insideArchive = new UrlResource(newRoot);
		// make sure the location actually exists
		assertTrue(insideArchive.exists());
		URL url = PersistenceUnitReader.determinePersistenceUnitRootUrl(insideArchive);
		assertTrue("the archive location should have been returned", archive.getURL().sameFile(url));
	}

	@Test
	public void testJpa1ExcludeUnlisted() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-exclude-1.0.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals("The number of persistence units is incorrect.", 4, info.length);

		PersistenceUnitInfo noExclude = info[0];
		assertNotNull("noExclude should not be null.", noExclude);
		assertEquals("noExclude name is not correct.", "NoExcludeElement", noExclude.getPersistenceUnitName());
		assertFalse("Exclude unlisted should default false in 1.0.", noExclude.excludeUnlistedClasses());

		PersistenceUnitInfo emptyExclude = info[1];
		assertNotNull("emptyExclude should not be null.", emptyExclude);
		assertEquals("emptyExclude name is not correct.", "EmptyExcludeElement", emptyExclude.getPersistenceUnitName());
		assertTrue("emptyExclude should be true.", emptyExclude.excludeUnlistedClasses());

		PersistenceUnitInfo trueExclude = info[2];
		assertNotNull("trueExclude should not be null.", trueExclude);
		assertEquals("trueExclude name is not correct.", "TrueExcludeElement", trueExclude.getPersistenceUnitName());
		assertTrue("trueExclude should be true.", trueExclude.excludeUnlistedClasses());

		PersistenceUnitInfo falseExclude = info[3];
		assertNotNull("falseExclude should not be null.", falseExclude);
		assertEquals("falseExclude name is not correct.", "FalseExcludeElement", falseExclude.getPersistenceUnitName());
		assertFalse("falseExclude should be false.", falseExclude.excludeUnlistedClasses());
	}

	@Test
	public void testJpa2ExcludeUnlisted() throws Exception {
		PersistenceUnitReader reader = new PersistenceUnitReader(
				new PathMatchingResourcePatternResolver(), new JndiDataSourceLookup());
		String resource = "/org/springframework/orm/jpa/persistence-exclude-2.0.xml";
		PersistenceUnitInfo[] info = reader.readPersistenceUnitInfos(resource);

		assertNotNull(info);
		assertEquals("The number of persistence units is incorrect.", 4, info.length);

		PersistenceUnitInfo noExclude = info[0];
		assertNotNull("noExclude should not be null.", noExclude);
		assertEquals("noExclude name is not correct.", "NoExcludeElement", noExclude.getPersistenceUnitName());
		assertFalse("Exclude unlisted still defaults to false in 2.0.", noExclude.excludeUnlistedClasses());

		PersistenceUnitInfo emptyExclude = info[1];
		assertNotNull("emptyExclude should not be null.", emptyExclude);
		assertEquals("emptyExclude name is not correct.", "EmptyExcludeElement", emptyExclude.getPersistenceUnitName());
		assertTrue("emptyExclude should be true.", emptyExclude.excludeUnlistedClasses());

		PersistenceUnitInfo trueExclude = info[2];
		assertNotNull("trueExclude should not be null.", trueExclude);
		assertEquals("trueExclude name is not correct.", "TrueExcludeElement", trueExclude.getPersistenceUnitName());
		assertTrue("trueExclude should be true.", trueExclude.excludeUnlistedClasses());

		PersistenceUnitInfo falseExclude = info[3];
		assertNotNull("falseExclude should not be null.", falseExclude);
		assertEquals("falseExclude name is not correct.", "FalseExcludeElement", falseExclude.getPersistenceUnitName());
		assertFalse("falseExclude should be false.", falseExclude.excludeUnlistedClasses());
	}

}
