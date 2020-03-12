/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.format.datetime.joda;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * @author Phillip Webb
 * @author Sam Brannen
 */
public class DateTimeFormatterFactoryBeanTests {

	private final DateTimeFormatterFactoryBean factory = new DateTimeFormatterFactoryBean();


	@Test
	public void isSingleton() {
		assertThat(factory.isSingleton(), is(true));
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void getObjectType() {
		assertThat(factory.getObjectType(), is(equalTo((Class) DateTimeFormatter.class)));
	}

	@Test
	public void getObject() {
		factory.afterPropertiesSet();
		assertThat(factory.getObject(), is(equalTo(DateTimeFormat.mediumDateTime())));
	}

	@Test
	public void getObjectIsAlwaysSingleton() {
		factory.afterPropertiesSet();
		DateTimeFormatter formatter = factory.getObject();
		assertThat(formatter, is(equalTo(DateTimeFormat.mediumDateTime())));
		factory.setStyle("LL");
		assertThat(factory.getObject(), is(sameInstance(formatter)));
	}

}
