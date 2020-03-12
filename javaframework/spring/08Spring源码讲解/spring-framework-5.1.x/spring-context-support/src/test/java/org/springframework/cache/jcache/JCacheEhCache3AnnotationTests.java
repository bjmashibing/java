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

package org.springframework.cache.jcache;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

/**
 * Just here to be run against EHCache 3, whereas the original JCacheEhCacheAnnotationTests
 * runs against EhCache 2.x with the EhCache-JCache add-on.
 *
 * @author Juergen Hoeller
 */
public class JCacheEhCache3AnnotationTests extends JCacheEhCacheAnnotationTests {

	@Override
	protected CachingProvider getCachingProvider() {
		return Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
	}

}
