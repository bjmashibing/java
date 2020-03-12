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

package org.springframework.web.reactive.function.client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Arjen Poutsma
 */
public class ExchangeStrategiesTests {

	@Test
	public void empty() {
		ExchangeStrategies strategies = ExchangeStrategies.empty().build();
		assertTrue(strategies.messageReaders().isEmpty());
		assertTrue(strategies.messageWriters().isEmpty());
	}

	@Test
	public void withDefaults() {
		ExchangeStrategies strategies = ExchangeStrategies.withDefaults();
		assertFalse(strategies.messageReaders().isEmpty());
		assertFalse(strategies.messageWriters().isEmpty());
	}

	@Test
	@SuppressWarnings("deprecation")
	public void mutate() {
		ExchangeStrategies strategies = ExchangeStrategies.empty().build();
		assertTrue(strategies.messageReaders().isEmpty());
		assertTrue(strategies.messageWriters().isEmpty());

		ExchangeStrategies mutated = strategies.mutate().codecs(codecs -> codecs.registerDefaults(true)).build();
		assertFalse(mutated.messageReaders().isEmpty());
		assertFalse(mutated.messageWriters().isEmpty());
	}

}
