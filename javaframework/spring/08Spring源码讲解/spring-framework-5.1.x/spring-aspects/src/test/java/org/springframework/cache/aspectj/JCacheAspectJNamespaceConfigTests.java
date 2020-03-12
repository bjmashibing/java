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

package org.springframework.cache.aspectj;

import org.springframework.cache.jcache.config.AbstractJCacheAnnotationTests;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

/**
 * @author Stephane Nicoll
 */
public class JCacheAspectJNamespaceConfigTests extends AbstractJCacheAnnotationTests {

	@Override
	protected ApplicationContext getApplicationContext() {
		return new GenericXmlApplicationContext(
				"/org/springframework/cache/config/annotation-jcache-aspectj.xml");
	}

}
