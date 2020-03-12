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

package org.springframework.web.reactive.result.condition;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import org.springframework.http.MediaType;
import org.springframework.mock.web.test.server.MockServerWebExchange;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.server.ServerWebExchange;

import static org.junit.Assert.*;
import static org.springframework.mock.http.server.reactive.test.MockServerHttpRequest.*;

/**
 * Unit tests for {@link ProducesRequestCondition}.
 *
 * @author Rossen Stoyanchev
 */
public class ProducesRequestConditionTests {

	@Test
	public void match() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));
		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain");

		assertNotNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void matchNegated() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));
		ProducesRequestCondition condition = new ProducesRequestCondition("!text/plain");

		assertNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void getProducibleMediaTypes() {
		ProducesRequestCondition condition = new ProducesRequestCondition("!application/xml");
		assertEquals(Collections.emptySet(), condition.getProducibleMediaTypes());
	}

	@Test
	public void matchWildcard() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));
		ProducesRequestCondition condition = new ProducesRequestCondition("text/*");

		assertNotNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void matchMultiple() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));
		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain", "application/xml");

		assertNotNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void matchSingle() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "application/xml"));
		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain");

		assertNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void matchParseError() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "bogus"));
		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain");

		assertNull(condition.getMatchingCondition(exchange));
	}

	@Test
	public void matchParseErrorWithNegation() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "bogus"));
		ProducesRequestCondition condition = new ProducesRequestCondition("!text/plain");

		assertNull(condition.getMatchingCondition(exchange));
	}

	@Test // SPR-17550
	public void matchWithNegationAndMediaTypeAllWithQualityParameter() {
		ProducesRequestCondition condition = new ProducesRequestCondition("!application/json");

		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8"));

		assertNotNull(condition.getMatchingCondition(exchange));
	}

	@Test // gh-22853
	public void matchAndCompare() {
		RequestedContentTypeResolverBuilder builder = new RequestedContentTypeResolverBuilder();
		builder.headerResolver();
		builder.fixedResolver(MediaType.TEXT_HTML);
		RequestedContentTypeResolver resolver = builder.build();

		ProducesRequestCondition none = new ProducesRequestCondition(new String[0], null, resolver);
		ProducesRequestCondition html = new ProducesRequestCondition(new String[] {"text/html"}, null, resolver);

		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "*/*"));

		ProducesRequestCondition noneMatch = none.getMatchingCondition(exchange);
		ProducesRequestCondition htmlMatch = html.getMatchingCondition(exchange);

		assertEquals(1, noneMatch.compareTo(htmlMatch, exchange));
	}

	@Test
	public void compareTo() {
		ProducesRequestCondition html = new ProducesRequestCondition("text/html");
		ProducesRequestCondition xml = new ProducesRequestCondition("application/xml");
		ProducesRequestCondition none = new ProducesRequestCondition();

		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "application/xml, text/html"));

		assertTrue(html.compareTo(xml, exchange) > 0);
		assertTrue(xml.compareTo(html, exchange) < 0);
		assertTrue(xml.compareTo(none, exchange) < 0);
		assertTrue(none.compareTo(xml, exchange) > 0);
		assertTrue(html.compareTo(none, exchange) < 0);
		assertTrue(none.compareTo(html, exchange) > 0);

		exchange = MockServerWebExchange.from(
				get("/").header("Accept", "application/xml, text/*"));

		assertTrue(html.compareTo(xml, exchange) > 0);
		assertTrue(xml.compareTo(html, exchange) < 0);

		exchange = MockServerWebExchange.from(
				get("/").header("Accept", "application/pdf"));

		assertTrue(html.compareTo(xml, exchange) == 0);
		assertTrue(xml.compareTo(html, exchange) == 0);

		// See SPR-7000
		exchange = MockServerWebExchange.from(
				get("/").header("Accept", "text/html;q=0.9,application/xml"));

		assertTrue(html.compareTo(xml, exchange) > 0);
		assertTrue(xml.compareTo(html, exchange) < 0);
	}

	@Test
	public void compareToWithSingleExpression() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));

		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("text/*");

		int result = condition1.compareTo(condition2, exchange);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, exchange);
		assertTrue("Invalid comparison result: " + result, result > 0);
	}

	@Test
	public void compareToMultipleExpressions() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("*/*", "text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("text/*", "text/plain;q=0.7");

		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));

		int result = condition1.compareTo(condition2, exchange);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, exchange);
		assertTrue("Invalid comparison result: " + result, result > 0);
	}

	@Test
	public void compareToMultipleExpressionsAndMultipleAcceptHeaderValues() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/*", "text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/*", "application/xml");

		ServerWebExchange exchange = MockServerWebExchange.from(
				get("/").header("Accept", "text/plain", "application/xml"));

		int result = condition1.compareTo(condition2, exchange);
		assertTrue("Invalid comparison result: " + result, result < 0);

		result = condition2.compareTo(condition1, exchange);
		assertTrue("Invalid comparison result: " + result, result > 0);

		exchange = MockServerWebExchange.from(
				get("/").header("Accept", "application/xml", "text/plain"));

		result = condition1.compareTo(condition2, exchange);
		assertTrue("Invalid comparison result: " + result, result > 0);

		result = condition2.compareTo(condition1, exchange);
		assertTrue("Invalid comparison result: " + result, result < 0);
	}

	// SPR-8536

	@Test
	public void compareToMediaTypeAll() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/"));

		ProducesRequestCondition condition1 = new ProducesRequestCondition();
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/json");

		assertTrue("Should have picked '*/*' condition as an exact match",
				condition1.compareTo(condition2, exchange) < 0);
		assertTrue("Should have picked '*/*' condition as an exact match",
				condition2.compareTo(condition1, exchange) > 0);

		condition1 = new ProducesRequestCondition("*/*");
		condition2 = new ProducesRequestCondition("application/json");

		assertTrue(condition1.compareTo(condition2, exchange) < 0);
		assertTrue(condition2.compareTo(condition1, exchange) > 0);

		exchange = MockServerWebExchange.from(
				get("/").header("Accept", "*/*"));

		condition1 = new ProducesRequestCondition();
		condition2 = new ProducesRequestCondition("application/json");

		assertTrue(condition1.compareTo(condition2, exchange) < 0);
		assertTrue(condition2.compareTo(condition1, exchange) > 0);

		condition1 = new ProducesRequestCondition("*/*");
		condition2 = new ProducesRequestCondition("application/json");

		assertTrue(condition1.compareTo(condition2, exchange) < 0);
		assertTrue(condition2.compareTo(condition1, exchange) > 0);
	}

	// SPR-9021

	@Test
	public void compareToMediaTypeAllWithParameter() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "*/*;q=0.9"));

		ProducesRequestCondition condition1 = new ProducesRequestCondition();
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/json");

		assertTrue(condition1.compareTo(condition2, exchange) < 0);
		assertTrue(condition2.compareTo(condition1, exchange) > 0);
	}

	@Test
	public void compareToEqualMatch() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/*"));

		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("text/xhtml");

		int result = condition1.compareTo(condition2, exchange);
		assertTrue("Should have used MediaType.equals(Object) to break the match", result < 0);

		result = condition2.compareTo(condition1, exchange);
		assertTrue("Should have used MediaType.equals(Object) to break the match", result > 0);
	}

	@Test
	public void combine() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition("application/xml");

		ProducesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition2, result);
	}

	@Test
	public void combineWithDefault() {
		ProducesRequestCondition condition1 = new ProducesRequestCondition("text/plain");
		ProducesRequestCondition condition2 = new ProducesRequestCondition();

		ProducesRequestCondition result = condition1.combine(condition2);
		assertEquals(condition1, result);
	}

	@Test
	public void instantiateWithProducesAndHeaderConditions() {
		String[] produces = new String[] {"text/plain"};
		String[] headers = new String[]{"foo=bar", "accept=application/xml,application/pdf"};
		ProducesRequestCondition condition = new ProducesRequestCondition(produces, headers);

		assertConditions(condition, "text/plain", "application/xml", "application/pdf");
	}

	@Test
	public void getMatchingCondition() {
		MockServerWebExchange exchange = MockServerWebExchange.from(get("/").header("Accept", "text/plain"));

		ProducesRequestCondition condition = new ProducesRequestCondition("text/plain", "application/xml");

		ProducesRequestCondition result = condition.getMatchingCondition(exchange);
		assertConditions(result, "text/plain");

		condition = new ProducesRequestCondition("application/xml");

		result = condition.getMatchingCondition(exchange);
		assertNull(result);
	}

	private void assertConditions(ProducesRequestCondition condition, String... expected) {
		Collection<ProducesRequestCondition.ProduceMediaTypeExpression> expressions = condition.getContent();
		assertEquals("Invalid number of conditions", expressions.size(), expected.length);
		for (String s : expected) {
			boolean found = false;
			for (ProducesRequestCondition.ProduceMediaTypeExpression expr : expressions) {
				String conditionMediaType = expr.getMediaType().toString();
				if (conditionMediaType.equals(s)) {
					found = true;
					break;

				}
			}
			if (!found) {
				fail("Condition [" + s + "] not found");
			}
		}
	}

}
