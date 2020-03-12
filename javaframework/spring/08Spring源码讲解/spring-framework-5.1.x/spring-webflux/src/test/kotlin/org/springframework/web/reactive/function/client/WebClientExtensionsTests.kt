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

package org.springframework.web.reactive.function.client

import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Answers
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import org.reactivestreams.Publisher
import org.springframework.core.ParameterizedTypeReference

/**
 * Mock object based tests for [WebClient] Kotlin extensions
 *
 * @author Sebastien Deleuze
 */
@RunWith(MockitoJUnitRunner::class)
class WebClientExtensionsTests {

	@Mock(answer = Answers.RETURNS_MOCKS)
	lateinit var requestBodySpec: WebClient.RequestBodySpec

	@Mock(answer = Answers.RETURNS_MOCKS)
	lateinit var responseSpec: WebClient.ResponseSpec


	@Test
	fun `RequestBodySpec#body with Publisher and reified type parameters`() {
		val body = mock<Publisher<List<Foo>>>()
		requestBodySpec.body(body)
		verify(requestBodySpec, times(1)).body(body, object : ParameterizedTypeReference<List<Foo>>() {})
	}

	@Test
	fun `ResponseSpec#bodyToMono with reified type parameters`() {
		responseSpec.bodyToMono<List<Foo>>()
		verify(responseSpec, times(1)).bodyToMono(object : ParameterizedTypeReference<List<Foo>>() {})
	}

	@Test
	fun `ResponseSpec#bodyToFlux with reified type parameters`() {
		responseSpec.bodyToFlux<List<Foo>>()
		verify(responseSpec, times(1)).bodyToFlux(object : ParameterizedTypeReference<List<Foo>>() {})
	}

	class Foo
}
