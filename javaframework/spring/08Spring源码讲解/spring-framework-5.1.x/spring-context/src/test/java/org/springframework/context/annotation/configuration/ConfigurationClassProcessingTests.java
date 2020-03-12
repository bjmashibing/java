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

package org.springframework.context.annotation.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.inject.Provider;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.NestedTestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;

/**
 * Miscellaneous system tests covering {@link Bean} naming, aliases, scoping and
 * error handling within {@link Configuration} class definitions.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
public class ConfigurationClassProcessingTests {

	@Rule
	public final ExpectedException exception = ExpectedException.none();


	@Test
	public void customBeanNameIsRespectedWhenConfiguredViaNameAttribute() {
		customBeanNameIsRespected(ConfigWithBeanWithCustomName.class,
				() -> ConfigWithBeanWithCustomName.testBean, "customName");
	}

	@Test
	public void customBeanNameIsRespectedWhenConfiguredViaValueAttribute() {
		customBeanNameIsRespected(ConfigWithBeanWithCustomNameConfiguredViaValueAttribute.class,
				() -> ConfigWithBeanWithCustomNameConfiguredViaValueAttribute.testBean, "enigma");
	}

	private void customBeanNameIsRespected(Class<?> testClass, Supplier<TestBean> testBeanSupplier, String beanName) {
		GenericApplicationContext ac = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ac);
		ac.registerBeanDefinition("config", new RootBeanDefinition(testClass));
		ac.refresh();

		assertSame(testBeanSupplier.get(), ac.getBean(beanName));

		// method name should not be registered
		exception.expect(NoSuchBeanDefinitionException.class);
		ac.getBean("methodName");
	}

	@Test
	public void aliasesAreRespectedWhenConfiguredViaNameAttribute() {
		aliasesAreRespected(ConfigWithBeanWithAliases.class,
				() -> ConfigWithBeanWithAliases.testBean, "name1");
	}

	@Test
	public void aliasesAreRespectedWhenConfiguredViaValueAttribute() {
		aliasesAreRespected(ConfigWithBeanWithAliasesConfiguredViaValueAttribute.class,
				() -> ConfigWithBeanWithAliasesConfiguredViaValueAttribute.testBean, "enigma");
	}

	private void aliasesAreRespected(Class<?> testClass, Supplier<TestBean> testBeanSupplier, String beanName) {
		TestBean testBean = testBeanSupplier.get();
		BeanFactory factory = initBeanFactory(testClass);

		assertSame(testBean, factory.getBean(beanName));
		Arrays.stream(factory.getAliases(beanName)).map(factory::getBean).forEach(alias -> assertSame(testBean, alias));

		// method name should not be registered
		exception.expect(NoSuchBeanDefinitionException.class);
		factory.getBean("methodName");
	}

	@Test  // SPR-11830
	public void configWithBeanWithProviderImplementation() {
		GenericApplicationContext ac = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ac);
		ac.registerBeanDefinition("config", new RootBeanDefinition(ConfigWithBeanWithProviderImplementation.class));
		ac.refresh();
		assertSame(ac.getBean("customName"), ConfigWithBeanWithProviderImplementation.testBean);
	}

	@Test  // SPR-11830
	public void configWithSetWithProviderImplementation() {
		GenericApplicationContext ac = new GenericApplicationContext();
		AnnotationConfigUtils.registerAnnotationConfigProcessors(ac);
		ac.registerBeanDefinition("config", new RootBeanDefinition(ConfigWithSetWithProviderImplementation.class));
		ac.refresh();
		assertSame(ac.getBean("customName"), ConfigWithSetWithProviderImplementation.set);
	}

	@Test
	public void testFinalBeanMethod() {
		exception.expect(BeanDefinitionParsingException.class);
		initBeanFactory(ConfigWithFinalBean.class);
	}

	@Test
	public void simplestPossibleConfig() {
		BeanFactory factory = initBeanFactory(SimplestPossibleConfig.class);
		String stringBean = factory.getBean("stringBean", String.class);
		assertEquals("foo", stringBean);
	}

	@Test
	public void configWithObjectReturnType() {
		BeanFactory factory = initBeanFactory(ConfigWithNonSpecificReturnTypes.class);
		assertEquals(Object.class, factory.getType("stringBean"));
		assertFalse(factory.isTypeMatch("stringBean", String.class));
		String stringBean = factory.getBean("stringBean", String.class);
		assertEquals("foo", stringBean);
	}

	@Test
	public void configWithFactoryBeanReturnType() {
		ListableBeanFactory factory = initBeanFactory(ConfigWithNonSpecificReturnTypes.class);
		assertEquals(List.class, factory.getType("factoryBean"));
		assertTrue(factory.isTypeMatch("factoryBean", List.class));
		assertEquals(FactoryBean.class, factory.getType("&factoryBean"));
		assertTrue(factory.isTypeMatch("&factoryBean", FactoryBean.class));
		assertFalse(factory.isTypeMatch("&factoryBean", BeanClassLoaderAware.class));
		assertFalse(factory.isTypeMatch("&factoryBean", ListFactoryBean.class));
		assertTrue(factory.getBean("factoryBean") instanceof List);

		String[] beanNames = factory.getBeanNamesForType(FactoryBean.class);
		assertEquals(1, beanNames.length);
		assertEquals("&factoryBean", beanNames[0]);

		beanNames = factory.getBeanNamesForType(BeanClassLoaderAware.class);
		assertEquals(1, beanNames.length);
		assertEquals("&factoryBean", beanNames[0]);

		beanNames = factory.getBeanNamesForType(ListFactoryBean.class);
		assertEquals(1, beanNames.length);
		assertEquals("&factoryBean", beanNames[0]);

		beanNames = factory.getBeanNamesForType(List.class);
		assertEquals("factoryBean", beanNames[0]);
	}

	@Test
	public void configurationWithPrototypeScopedBeans() {
		BeanFactory factory = initBeanFactory(ConfigWithPrototypeBean.class);

		TestBean foo = factory.getBean("foo", TestBean.class);
		ITestBean bar = factory.getBean("bar", ITestBean.class);
		ITestBean baz = factory.getBean("baz", ITestBean.class);

		assertSame(foo.getSpouse(), bar);
		assertNotSame(bar.getSpouse(), baz);
	}

	@Test
	public void configurationWithNullReference() {
		BeanFactory factory = initBeanFactory(ConfigWithNullReference.class);

		TestBean foo = factory.getBean("foo", TestBean.class);
		assertTrue(factory.getBean("bar").equals(null));
		assertNull(foo.getSpouse());
	}

	@Test
	public void configurationWithAdaptivePrototypes() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithPrototypeBean.class, AdaptiveInjectionPoints.class);
		ctx.refresh();

		AdaptiveInjectionPoints adaptive = ctx.getBean(AdaptiveInjectionPoints.class);
		assertEquals("adaptiveInjectionPoint1", adaptive.adaptiveInjectionPoint1.getName());
		assertEquals("setAdaptiveInjectionPoint2", adaptive.adaptiveInjectionPoint2.getName());

		adaptive = ctx.getBean(AdaptiveInjectionPoints.class);
		assertEquals("adaptiveInjectionPoint1", adaptive.adaptiveInjectionPoint1.getName());
		assertEquals("setAdaptiveInjectionPoint2", adaptive.adaptiveInjectionPoint2.getName());
		ctx.close();
	}

	@Test
	public void configurationWithAdaptiveResourcePrototypes() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithPrototypeBean.class, AdaptiveResourceInjectionPoints.class);
		ctx.refresh();

		AdaptiveResourceInjectionPoints adaptive = ctx.getBean(AdaptiveResourceInjectionPoints.class);
		assertEquals("adaptiveInjectionPoint1", adaptive.adaptiveInjectionPoint1.getName());
		assertEquals("setAdaptiveInjectionPoint2", adaptive.adaptiveInjectionPoint2.getName());

		adaptive = ctx.getBean(AdaptiveResourceInjectionPoints.class);
		assertEquals("adaptiveInjectionPoint1", adaptive.adaptiveInjectionPoint1.getName());
		assertEquals("setAdaptiveInjectionPoint2", adaptive.adaptiveInjectionPoint2.getName());
		ctx.close();
	}

	@Test
	public void configurationWithPostProcessor() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithPostProcessor.class);
		RootBeanDefinition placeholderConfigurer = new RootBeanDefinition(PropertyPlaceholderConfigurer.class);
		placeholderConfigurer.getPropertyValues().add("properties", "myProp=myValue");
		ctx.registerBeanDefinition("placeholderConfigurer", placeholderConfigurer);
		ctx.refresh();

		TestBean foo = ctx.getBean("foo", TestBean.class);
		ITestBean bar = ctx.getBean("bar", ITestBean.class);
		ITestBean baz = ctx.getBean("baz", ITestBean.class);

		assertEquals("foo-processed-myValue", foo.getName());
		assertEquals("bar-processed-myValue", bar.getName());
		assertEquals("baz-processed-myValue", baz.getName());

		SpousyTestBean listener = ctx.getBean("listenerTestBean", SpousyTestBean.class);
		assertTrue(listener.refreshed);
		ctx.close();
	}

	@Test
	public void configurationWithFunctionalRegistration() {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(ConfigWithFunctionalRegistration.class);
		ctx.refresh();

		assertSame(ctx.getBean("spouse"), ctx.getBean(TestBean.class).getSpouse());
		assertEquals("functional", ctx.getBean(NestedTestBean.class).getCompany());
	}


	/**
	 * Creates a new {@link BeanFactory}, populates it with a {@link BeanDefinition}
	 * for each of the given {@link Configuration} {@code configClasses}, and then
	 * post-processes the factory using JavaConfig's {@link ConfigurationClassPostProcessor}.
	 * When complete, the factory is ready to service requests for any {@link Bean} methods
	 * declared by {@code configClasses}.
	 */
	private DefaultListableBeanFactory initBeanFactory(Class<?>... configClasses) {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		for (Class<?> configClass : configClasses) {
			String configBeanName = configClass.getName();
			factory.registerBeanDefinition(configBeanName, new RootBeanDefinition(configClass));
		}
		ConfigurationClassPostProcessor ccpp = new ConfigurationClassPostProcessor();
		ccpp.postProcessBeanDefinitionRegistry(factory);
		ccpp.postProcessBeanFactory(factory);
		factory.freezeConfiguration();
		return factory;
	}


	@Configuration
	static class ConfigWithBeanWithCustomName {

		static TestBean testBean = new TestBean(ConfigWithBeanWithCustomName.class.getSimpleName());

		@Bean(name = "customName")
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithBeanWithCustomNameConfiguredViaValueAttribute {

		static TestBean testBean = new TestBean(ConfigWithBeanWithCustomNameConfiguredViaValueAttribute.class.getSimpleName());

		@Bean("enigma")
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithBeanWithAliases {

		static TestBean testBean = new TestBean(ConfigWithBeanWithAliases.class.getSimpleName());

		@Bean(name = {"name1", "alias1", "alias2", "alias3"})
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithBeanWithAliasesConfiguredViaValueAttribute {

		static TestBean testBean = new TestBean(ConfigWithBeanWithAliasesConfiguredViaValueAttribute.class.getSimpleName());

		@Bean({"enigma", "alias1", "alias2", "alias3"})
		public TestBean methodName() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithBeanWithProviderImplementation implements Provider<TestBean> {

		static TestBean testBean = new TestBean(ConfigWithBeanWithProviderImplementation.class.getSimpleName());

		@Bean(name = "customName")
		public TestBean get() {
			return testBean;
		}
	}


	@Configuration
	static class ConfigWithSetWithProviderImplementation implements Provider<Set<String>> {

		static Set<String> set = Collections.singleton("value");

		@Bean(name = "customName")
		public Set<String> get() {
			return set;
		}
	}


	@Configuration
	static class ConfigWithFinalBean {

		public final @Bean TestBean testBean() {
			return new TestBean();
		}
	}


	@Configuration
	static class SimplestPossibleConfig {

		public @Bean String stringBean() {
			return "foo";
		}
	}


	@Configuration
	static class ConfigWithNonSpecificReturnTypes {

		public @Bean Object stringBean() {
			return "foo";
		}

		public @Bean FactoryBean<?> factoryBean() {
			ListFactoryBean fb = new ListFactoryBean();
			fb.setSourceList(Arrays.asList("element1", "element2"));
			return fb;
		}
	}


	@Configuration
	static class ConfigWithPrototypeBean {

		public @Bean TestBean foo() {
			TestBean foo = new SpousyTestBean("foo");
			foo.setSpouse(bar());
			return foo;
		}

		public @Bean TestBean bar() {
			TestBean bar = new SpousyTestBean("bar");
			bar.setSpouse(baz());
			return bar;
		}

		@Bean @Scope("prototype")
		public TestBean baz() {
			return new TestBean("baz");
		}

		@Bean @Scope("prototype")
		public TestBean adaptive1(InjectionPoint ip) {
			return new TestBean(ip.getMember().getName());
		}

		@Bean @Scope("prototype")
		public TestBean adaptive2(DependencyDescriptor dd) {
			return new TestBean(dd.getMember().getName());
		}
	}


	@Configuration
	static class ConfigWithNullReference extends ConfigWithPrototypeBean {

		@Override
		public TestBean bar() {
			return null;
		}
	}


	@Scope("prototype")
	static class AdaptiveInjectionPoints {

		@Autowired @Qualifier("adaptive1")
		public TestBean adaptiveInjectionPoint1;

		public TestBean adaptiveInjectionPoint2;

		@Autowired @Qualifier("adaptive2")
		public void setAdaptiveInjectionPoint2(TestBean adaptiveInjectionPoint2) {
			this.adaptiveInjectionPoint2 = adaptiveInjectionPoint2;
		}
	}


	@Scope("prototype")
	static class AdaptiveResourceInjectionPoints {

		@Resource(name = "adaptive1")
		public TestBean adaptiveInjectionPoint1;

		public TestBean adaptiveInjectionPoint2;

		@Resource(name = "adaptive2")
		public void setAdaptiveInjectionPoint2(TestBean adaptiveInjectionPoint2) {
			this.adaptiveInjectionPoint2 = adaptiveInjectionPoint2;
		}
	}


	static class ConfigWithPostProcessor extends ConfigWithPrototypeBean {

		@Value("${myProp}")
		private String myProp;

		@Bean
		public POBPP beanPostProcessor() {
			return new POBPP() {

				String nameSuffix = "-processed-" + myProp;

				public void setNameSuffix(String nameSuffix) {
					this.nameSuffix = nameSuffix;
				}

				@Override
				public Object postProcessBeforeInitialization(Object bean, String beanName) {
					if (bean instanceof ITestBean) {
						((ITestBean) bean).setName(((ITestBean) bean).getName() + nameSuffix);
					}
					return bean;
				}

				@Override
				public Object postProcessAfterInitialization(Object bean, String beanName) {
					return bean;
				}

				public int getOrder() {
					return 0;
				}
			};
		}

		// @Bean
		public BeanFactoryPostProcessor beanFactoryPostProcessor() {
			return new BeanFactoryPostProcessor() {
				@Override
				public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
					BeanDefinition bd = beanFactory.getBeanDefinition("beanPostProcessor");
					bd.getPropertyValues().addPropertyValue("nameSuffix", "-processed-" + myProp);
				}
			};
		}

		@Bean
		public ITestBean listenerTestBean() {
			return new SpousyTestBean("listener");
		}
	}


	public interface POBPP extends BeanPostProcessor {
	}


	private static class SpousyTestBean extends TestBean implements ApplicationListener<ContextRefreshedEvent> {

		public boolean refreshed = false;

		public SpousyTestBean(String name) {
			super(name);
		}

		@Override
		public void setSpouse(ITestBean spouse) {
			super.setSpouse(spouse);
		}

		@Override
		public void onApplicationEvent(ContextRefreshedEvent event) {
			this.refreshed = true;
		}
	}


	@Configuration
	static class ConfigWithFunctionalRegistration {

		@Autowired
		void register(GenericApplicationContext ctx) {
			ctx.registerBean("spouse", TestBean.class,
					() -> new TestBean("functional"));
			Supplier<TestBean> testBeanSupplier = () -> new TestBean(ctx.getBean("spouse", TestBean.class));
			ctx.registerBean(TestBean.class,
					testBeanSupplier,
					bd -> bd.setPrimary(true));
		}

		@Bean
		public NestedTestBean nestedTestBean(TestBean testBean) {
			return new NestedTestBean(testBean.getSpouse().getName());
		}
	}

}
