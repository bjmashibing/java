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
package org.springframework.context.annotation

import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.getBean
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * @author Sebastien Deleuze
 */
class Spr16022Tests {

	@Test
	fun `Register beans with multiple constructors with AnnotationConfigApplicationContext`() {
		assert(AnnotationConfigApplicationContext(Config::class.java))
	}

	@Test
	fun `Register beans with multiple constructors with ClassPathXmlApplicationContext`() {
		assert(ClassPathXmlApplicationContext(CONTEXT))
	}

	private fun assert(context: BeanFactory) {
		val bean1 = context.getBean<MultipleConstructorsTestBean>("bean1")
		assertEquals(0, bean1.foo)
		val bean2 = context.getBean<MultipleConstructorsTestBean>("bean2")
		assertEquals(1, bean2.foo)
		val bean3 = context.getBean<MultipleConstructorsTestBean>("bean3")
		assertEquals(3, bean3.foo)

	}

	@Suppress("unused")
	class MultipleConstructorsTestBean(val foo: Int) {
		constructor(bar: String) : this(bar.length)
		constructor(foo: Int, bar: String) : this(foo + bar.length)
	}

	@Configuration @ImportResource(CONTEXT)
	open class Config
}

private const val CONTEXT = "org/springframework/context/annotation/multipleConstructors.xml"