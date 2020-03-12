/*
 * Copyright 2002-2009 the original author or authors.
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
package org.springframework.context.annotation;

import org.junit.Test;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.junit.Assert.*;
import static org.springframework.beans.factory.support.BeanDefinitionBuilder.*;

/**
 * Unit tests covering cases where a user defines an invalid Configuration
 * class, e.g.: forgets to annotate with {@link Configuration} or declares
 * a Configuration class as final.
 *
 * @author Chris Beams
 */
public class InvalidConfigurationClassDefinitionTests {

	@Test
	public void configurationClassesMayNotBeFinal() {
		@Configuration
		final class Config { }

		BeanDefinition configBeanDef = rootBeanDefinition(Config.class).getBeanDefinition();
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		beanFactory.registerBeanDefinition("config", configBeanDef);

		try {
			ConfigurationClassPostProcessor pp = new ConfigurationClassPostProcessor();
			pp.postProcessBeanFactory(beanFactory);
			fail("expected exception");
		}
		catch (BeanDefinitionParsingException ex) {
			assertTrue(ex.getMessage(), ex.getMessage().contains("Remove the final modifier"));
		}
	}

}
