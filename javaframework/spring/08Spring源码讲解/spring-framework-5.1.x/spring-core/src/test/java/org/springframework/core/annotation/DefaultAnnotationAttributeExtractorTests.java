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

package org.springframework.core.annotation;

import java.lang.annotation.Annotation;

/**
 * Unit tests for {@link DefaultAnnotationAttributeExtractor}.
 *
 * @author Sam Brannen
 * @since 4.2.1
 */
public class DefaultAnnotationAttributeExtractorTests extends AbstractAliasAwareAnnotationAttributeExtractorTestCase {

	@Override
	protected AnnotationAttributeExtractor<?> createExtractorFor(Class<?> clazz, String expected, Class<? extends Annotation> annotationType) {
		return new DefaultAnnotationAttributeExtractor(clazz.getAnnotation(annotationType), clazz);
	}

}
