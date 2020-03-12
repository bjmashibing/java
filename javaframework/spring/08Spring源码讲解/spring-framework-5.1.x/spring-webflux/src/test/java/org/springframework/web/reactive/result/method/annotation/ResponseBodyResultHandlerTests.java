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

package org.springframework.web.reactive.result.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Mono;
import rx.Completable;
import rx.Single;

import org.springframework.core.codec.ByteBufferEncoder;
import org.springframework.core.codec.CharSequenceEncoder;
import org.springframework.http.codec.EncoderHttpMessageWriter;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ResourceHttpMessageWriter;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.codec.xml.Jaxb2XmlEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.web.method.ResolvableMethod.on;

/**
 * Unit tests for {@link ResponseBodyResultHandler}.When adding a test also
 * consider whether the logic under test is in a parent class, then see:
 * <ul>
 * 	<li>{@code MessageWriterResultHandlerTests},
 *  <li>{@code ContentNegotiatingResultHandlerSupportTests}
 * </ul>
 *
 * @author Sebastien Deleuze
 * @author Rossen Stoyanchev
 */
public class ResponseBodyResultHandlerTests {

	private ResponseBodyResultHandler resultHandler;


	@Before
	public void setup() throws Exception {
		List<HttpMessageWriter<?>> writerList = new ArrayList<>(5);
		writerList.add(new EncoderHttpMessageWriter<>(new ByteBufferEncoder()));
		writerList.add(new EncoderHttpMessageWriter<>(CharSequenceEncoder.allMimeTypes()));
		writerList.add(new ResourceHttpMessageWriter());
		writerList.add(new EncoderHttpMessageWriter<>(new Jaxb2XmlEncoder()));
		writerList.add(new EncoderHttpMessageWriter<>(new Jackson2JsonEncoder()));
		RequestedContentTypeResolver resolver = new RequestedContentTypeResolverBuilder().build();
		this.resultHandler = new ResponseBodyResultHandler(writerList, resolver);
	}


	@Test
	public void supports() throws NoSuchMethodException {
		Object controller = new TestController();
		Method method;

		method = on(TestController.class).annotPresent(ResponseBody.class).resolveMethod();
		testSupports(controller, method);

		method = on(TestController.class).annotNotPresent(ResponseBody.class).resolveMethod("doWork");
		HandlerResult handlerResult = getHandlerResult(controller, method);
		assertFalse(this.resultHandler.supports(handlerResult));
	}

	@Test
	public void supportsRestController() throws NoSuchMethodException {
		Object controller = new TestRestController();
		Method method;

		method = on(TestRestController.class).returning(String.class).resolveMethod();
		testSupports(controller, method);

		method = on(TestRestController.class).returning(Mono.class, String.class).resolveMethod();
		testSupports(controller, method);

		method = on(TestRestController.class).returning(Single.class, String.class).resolveMethod();
		testSupports(controller, method);

		method = on(TestRestController.class).returning(Completable.class).resolveMethod();
		testSupports(controller, method);
	}

	private void testSupports(Object controller, Method method) {
		HandlerResult handlerResult = getHandlerResult(controller, method);
		assertTrue(this.resultHandler.supports(handlerResult));
	}

	private HandlerResult getHandlerResult(Object controller, Method method) {
		HandlerMethod handlerMethod = new HandlerMethod(controller, method);
		return new HandlerResult(handlerMethod, null, handlerMethod.getReturnType());
	}

	@Test
	public void defaultOrder() throws Exception {
		assertEquals(100, this.resultHandler.getOrder());
	}



	@RestController
	@SuppressWarnings("unused")
	private static class TestRestController {

		public Mono<Void> handleToMonoVoid() { return null;}

		public String handleToString() {
			return null;
		}

		public Mono<String> handleToMonoString() {
			return null;
		}

		public Single<String> handleToSingleString() {
			return null;
		}

		public Completable handleToCompletable() {
			return null;
		}
	}


	@Controller
	@SuppressWarnings("unused")
	private static class TestController {

		@ResponseBody
		public String handleToString() {
			return null;
		}

		public String doWork() {
			return null;
		}
	}

}
