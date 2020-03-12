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

package org.springframework.context.annotation;

import org.junit.Test;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * @author Andy Wilkinson
 */
public class ImportVersusDirectRegistrationTests {

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void thingIsNotAvailableWhenOuterConfigurationIsRegisteredDirectly() {
		try (AnnotationConfigApplicationContext directRegistration = new AnnotationConfigApplicationContext()) {
			directRegistration.register(AccidentalLiteConfiguration.class);
			directRegistration.refresh();
			directRegistration.getBean(Thing.class);
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void thingIsNotAvailableWhenOuterConfigurationIsRegisteredWithClassName() {
		try (AnnotationConfigApplicationContext directRegistration = new AnnotationConfigApplicationContext()) {
			directRegistration.registerBeanDefinition("config",
					new RootBeanDefinition(AccidentalLiteConfiguration.class.getName()));
			directRegistration.refresh();
			directRegistration.getBean(Thing.class);
		}
	}

	@Test(expected = NoSuchBeanDefinitionException.class)
	public void thingIsNotAvailableWhenOuterConfigurationIsImported() {
		try (AnnotationConfigApplicationContext viaImport = new AnnotationConfigApplicationContext()) {
			viaImport.register(Importer.class);
			viaImport.refresh();
			viaImport.getBean(Thing.class);
		}
	}

}


@Import(AccidentalLiteConfiguration.class)
class Importer {
}


class AccidentalLiteConfiguration {

	@Configuration
	class InnerConfiguration {

		@Bean
		public Thing thing() {
			return new Thing();
		}
	}
}


class Thing {
}
