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

package org.springframework.test.context.cache;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the LRU eviction policy in {@link DefaultContextCache}.
 *
 * @author Sam Brannen
 * @since 4.3
 * @see ContextCacheTests
 */
public class LruContextCacheTests {

	private static final MergedContextConfiguration abcConfig = config(Abc.class);
	private static final MergedContextConfiguration fooConfig = config(Foo.class);
	private static final MergedContextConfiguration barConfig = config(Bar.class);
	private static final MergedContextConfiguration bazConfig = config(Baz.class);


	private final ConfigurableApplicationContext abcContext = mock(ConfigurableApplicationContext.class);
	private final ConfigurableApplicationContext fooContext = mock(ConfigurableApplicationContext.class);
	private final ConfigurableApplicationContext barContext = mock(ConfigurableApplicationContext.class);
	private final ConfigurableApplicationContext bazContext = mock(ConfigurableApplicationContext.class);


	@Test(expected = IllegalArgumentException.class)
	public void maxCacheSizeNegativeOne() {
		new DefaultContextCache(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void maxCacheSizeZero() {
		new DefaultContextCache(0);
	}

	@Test
	public void maxCacheSizeOne() {
		DefaultContextCache cache = new DefaultContextCache(1);
		assertEquals(0, cache.size());
		assertEquals(1, cache.getMaxSize());

		cache.put(fooConfig, fooContext);
		assertCacheContents(cache, "Foo");

		cache.put(fooConfig, fooContext);
		assertCacheContents(cache, "Foo");

		cache.put(barConfig, barContext);
		assertCacheContents(cache, "Bar");

		cache.put(fooConfig, fooContext);
		assertCacheContents(cache, "Foo");
	}

	@Test
	public void maxCacheSizeThree() {
		DefaultContextCache cache = new DefaultContextCache(3);
		assertEquals(0, cache.size());
		assertEquals(3, cache.getMaxSize());

		cache.put(fooConfig, fooContext);
		assertCacheContents(cache, "Foo");

		cache.put(fooConfig, fooContext);
		assertCacheContents(cache, "Foo");

		cache.put(barConfig, barContext);
		assertCacheContents(cache, "Foo", "Bar");

		cache.put(bazConfig, bazContext);
		assertCacheContents(cache, "Foo", "Bar", "Baz");

		cache.put(abcConfig, abcContext);
		assertCacheContents(cache, "Bar", "Baz", "Abc");
	}

	@Test
	public void ensureLruOrderingIsUpdated() {
		DefaultContextCache cache = new DefaultContextCache(3);

		// Note: when a new entry is added it is considered the MRU entry and inserted at the tail.
		cache.put(fooConfig, fooContext);
		cache.put(barConfig, barContext);
		cache.put(bazConfig, bazContext);
		assertCacheContents(cache, "Foo", "Bar", "Baz");

		// Note: the MRU entry is moved to the tail when accessed.
		cache.get(fooConfig);
		assertCacheContents(cache, "Bar", "Baz", "Foo");

		cache.get(barConfig);
		assertCacheContents(cache, "Baz", "Foo", "Bar");

		cache.get(bazConfig);
		assertCacheContents(cache, "Foo", "Bar", "Baz");

		cache.get(barConfig);
		assertCacheContents(cache, "Foo", "Baz", "Bar");
	}

	@Test
	public void ensureEvictedContextsAreClosed() {
		DefaultContextCache cache = new DefaultContextCache(2);

		cache.put(fooConfig, fooContext);
		cache.put(barConfig, barContext);
		assertCacheContents(cache, "Foo", "Bar");

		cache.put(bazConfig, bazContext);
		assertCacheContents(cache, "Bar", "Baz");
		verify(fooContext, times(1)).close();

		cache.put(abcConfig, abcContext);
		assertCacheContents(cache, "Baz", "Abc");
		verify(barContext, times(1)).close();

		verify(abcContext, never()).close();
		verify(bazContext, never()).close();
	}


	private static MergedContextConfiguration config(Class<?> clazz) {
		return new MergedContextConfiguration(null, null, new Class<?>[] { clazz }, null, null);
	}

	@SuppressWarnings("unchecked")
	private static void assertCacheContents(DefaultContextCache cache, String... expectedNames) {

		Map<MergedContextConfiguration, ApplicationContext> contextMap =
				(Map<MergedContextConfiguration, ApplicationContext>) ReflectionTestUtils.getField(cache, "contextMap");

		// @formatter:off
		List<String> actualNames = contextMap.keySet().stream()
			.map(cfg -> cfg.getClasses()[0])
			.map(Class::getSimpleName)
			.collect(toList());
		// @formatter:on

		assertEquals(asList(expectedNames), actualNames);
	}


	private static class Abc {}
	private static class Foo {}
	private static class Bar {}
	private static class Baz {}

}
