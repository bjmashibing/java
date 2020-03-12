/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import org.springframework.lang.Nullable;

/**
 * Interface used by {@link CacheInterceptor}. Implementations know how to source
 * cache operation attributes, whether from configuration, metadata attributes at
 * source level, or elsewhere.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.1
 */
public interface CacheOperationSource {

	/**
	 * Return the collection of cache operations for this method,
	 * or {@code null} if the method contains no <em>cacheable</em> annotations.
	 * @param method the method to introspect
	 * @param targetClass the target class (may be {@code null}, in which case
	 * the declaring class of the method must be used)
	 * @return all cache operations for this method, or {@code null} if none found
	 */
	@Nullable
	Collection<CacheOperation> getCacheOperations(Method method, @Nullable Class<?> targetClass);

}
