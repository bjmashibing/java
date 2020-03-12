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

package org.springframework.web.context.support;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

/**
 * {@link org.springframework.web.context.WebApplicationContext WebApplicationContext}
 * implementation which accepts <em>component classes</em> as input &mdash; in particular
 * {@link org.springframework.context.annotation.Configuration @Configuration}-annotated
 * classes, but also plain {@link org.springframework.stereotype.Component @Component}
 * classes and JSR-330 compliant classes using {@code javax.inject} annotations.
 *
 * <p>Allows for registering classes one by one (specifying class names as config
 * location) as well as for classpath scanning (specifying base packages as config location).
 *
 * <p>This is essentially the equivalent of
 * {@link org.springframework.context.annotation.AnnotationConfigApplicationContext
 * AnnotationConfigApplicationContext} for a web environment.
 *
 * <p>To make use of this application context, the
 * {@linkplain ContextLoader#CONTEXT_CLASS_PARAM "contextClass"} context-param for
 * ContextLoader and/or "contextClass" init-param for FrameworkServlet must be set to
 * the fully-qualified name of this class.
 *
 * <p>As of Spring 3.1, this class may also be directly instantiated and injected into
 * Spring's {@code DispatcherServlet} or {@code ContextLoaderListener} when using the
 * {@link org.springframework.web.WebApplicationInitializer WebApplicationInitializer}
 * code-based alternative to {@code web.xml}. See its Javadoc for details and usage examples.
 *
 * <p>Unlike {@link XmlWebApplicationContext}, no default configuration class locations
 * are assumed. Rather, it is a requirement to set the
 * {@linkplain ContextLoader#CONFIG_LOCATION_PARAM "contextConfigLocation"}
 * context-param for {@link ContextLoader} and/or "contextConfigLocation" init-param for
 * FrameworkServlet.  The param-value may contain both fully-qualified
 * class names and base packages to scan for components. See {@link #loadBeanDefinitions}
 * for exact details on how these locations are processed.
 *
 * <p>As an alternative to setting the "contextConfigLocation" parameter, users may
 * implement an {@link org.springframework.context.ApplicationContextInitializer
 * ApplicationContextInitializer} and set the
 * {@linkplain ContextLoader#CONTEXT_INITIALIZER_CLASSES_PARAM "contextInitializerClasses"}
 * context-param / init-param. In such cases, users should favor the {@link #refresh()}
 * and {@link #scan(String...)} methods over the {@link #setConfigLocation(String)}
 * method, which is primarily for use by {@code ContextLoader}.
 *
 * <p>Note: In case of multiple {@code @Configuration} classes, later {@code @Bean}
 * definitions will override ones defined in earlier loaded files. This can be leveraged
 * to deliberately override certain bean definitions via an extra {@code @Configuration}
 * class.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.0
 * @see org.springframework.context.annotation.AnnotationConfigApplicationContext
 */
public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext
		implements AnnotationConfigRegistry {

	@Nullable
	private BeanNameGenerator beanNameGenerator;

	@Nullable
	private ScopeMetadataResolver scopeMetadataResolver;

	private final Set<Class<?>> componentClasses = new LinkedHashSet<>();

	private final Set<String> basePackages = new LinkedHashSet<>();


	/**
	 * Set a custom {@link BeanNameGenerator} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}.
	 * <p>Default is {@link org.springframework.context.annotation.AnnotationBeanNameGenerator}.
	 * @see AnnotatedBeanDefinitionReader#setBeanNameGenerator
	 * @see ClassPathBeanDefinitionScanner#setBeanNameGenerator
	 */
	public void setBeanNameGenerator(@Nullable BeanNameGenerator beanNameGenerator) {
		this.beanNameGenerator = beanNameGenerator;
	}

	/**
	 * Return the custom {@link BeanNameGenerator} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}, if any.
	 */
	@Nullable
	protected BeanNameGenerator getBeanNameGenerator() {
		return this.beanNameGenerator;
	}

	/**
	 * Set a custom {@link ScopeMetadataResolver} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}.
	 * <p>Default is an {@link org.springframework.context.annotation.AnnotationScopeMetadataResolver}.
	 * @see AnnotatedBeanDefinitionReader#setScopeMetadataResolver
	 * @see ClassPathBeanDefinitionScanner#setScopeMetadataResolver
	 */
	public void setScopeMetadataResolver(@Nullable ScopeMetadataResolver scopeMetadataResolver) {
		this.scopeMetadataResolver = scopeMetadataResolver;
	}

	/**
	 * Return the custom {@link ScopeMetadataResolver} for use with {@link AnnotatedBeanDefinitionReader}
	 * and/or {@link ClassPathBeanDefinitionScanner}, if any.
	 */
	@Nullable
	protected ScopeMetadataResolver getScopeMetadataResolver() {
		return this.scopeMetadataResolver;
	}


	/**
	 * Register one or more component classes to be processed.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param componentClasses one or more component classes,
	 * e.g. {@link org.springframework.context.annotation.Configuration @Configuration} classes
	 * @see #scan(String...)
	 * @see #loadBeanDefinitions(DefaultListableBeanFactory)
	 * @see #setConfigLocation(String)
	 * @see #refresh()
	 */
	@Override
	public void register(Class<?>... componentClasses) {
		Assert.notEmpty(componentClasses, "At least one component class must be specified");
		Collections.addAll(this.componentClasses, componentClasses);
	}

	/**
	 * Perform a scan within the specified base packages.
	 * <p>Note that {@link #refresh()} must be called in order for the context
	 * to fully process the new classes.
	 * @param basePackages the packages to check for component classes
	 * @see #loadBeanDefinitions(DefaultListableBeanFactory)
	 * @see #register(Class...)
	 * @see #setConfigLocation(String)
	 * @see #refresh()
	 */
	@Override
	public void scan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		Collections.addAll(this.basePackages, basePackages);
	}


	/**
	 * Register a {@link org.springframework.beans.factory.config.BeanDefinition} for
	 * any classes specified by {@link #register(Class...)} and scan any packages
	 * specified by {@link #scan(String...)}.
	 * <p>For any values specified by {@link #setConfigLocation(String)} or
	 * {@link #setConfigLocations(String[])}, attempt first to load each location as a
	 * class, registering a {@code BeanDefinition} if class loading is successful,
	 * and if class loading fails (i.e. a {@code ClassNotFoundException} is raised),
	 * assume the value is a package and attempt to scan it for component classes.
	 * <p>Enables the default set of annotation configuration post processors, such that
	 * {@code @Autowired}, {@code @Required}, and associated annotations can be used.
	 * <p>Configuration class bean definitions are registered with generated bean
	 * definition names unless the {@code value} attribute is provided to the stereotype
	 * annotation.
	 * @param beanFactory the bean factory to load bean definitions into
	 * @see #register(Class...)
	 * @see #scan(String...)
	 * @see #setConfigLocation(String)
	 * @see #setConfigLocations(String[])
	 * @see AnnotatedBeanDefinitionReader
	 * @see ClassPathBeanDefinitionScanner
	 *
	 * 	载入注解Bean定义资源
	 */
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
		//为容器设置注解Bean定义读取器
		AnnotatedBeanDefinitionReader reader = getAnnotatedBeanDefinitionReader(beanFactory);
		//为容器设置类路径Bean定义扫描器
		ClassPathBeanDefinitionScanner scanner = getClassPathBeanDefinitionScanner(beanFactory);

		//获取容器的Bean名称生成器
		BeanNameGenerator beanNameGenerator = getBeanNameGenerator();
		//为注解Bean定义读取器和类路径扫描器设置Bean名称生成器
		if (beanNameGenerator != null) {
			reader.setBeanNameGenerator(beanNameGenerator);
			scanner.setBeanNameGenerator(beanNameGenerator);
			beanFactory.registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
		}

		//获取容器的作用域元信息解析器
		ScopeMetadataResolver scopeMetadataResolver = getScopeMetadataResolver();
		//为注解Bean定义读取器和类路径扫描器设置作用域元信息解析器
		if (scopeMetadataResolver != null) {
			reader.setScopeMetadataResolver(scopeMetadataResolver);
			scanner.setScopeMetadataResolver(scopeMetadataResolver);
		}

		if (!this.componentClasses.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Registering component classes: [" +
						StringUtils.collectionToCommaDelimitedString(this.componentClasses) + "]");
			}
			reader.register(ClassUtils.toClassArray(this.componentClasses));
		}

		if (!this.basePackages.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("Scanning base packages: [" +
						StringUtils.collectionToCommaDelimitedString(this.basePackages) + "]");
			}
			scanner.scan(StringUtils.toStringArray(this.basePackages));
		}

		//获取容器定义的Bean定义资源路径
		String[] configLocations = getConfigLocations();
		//如果定义的Bean定义资源路径不为空
		if (configLocations != null) {
			for (String configLocation : configLocations) {
				try {
					//使用当前容器的类加载器加载定位路径的字节码文件
					Class<?> clazz = ClassUtils.forName(configLocation, getClassLoader());
					if (logger.isTraceEnabled()) {
						logger.trace("Registering [" + configLocation + "]");
					}
					reader.register(clazz);
				}
				catch (ClassNotFoundException ex) {
					if (logger.isTraceEnabled()) {
						logger.trace("Could not load class for config location [" + configLocation +
								"] - trying package scan. " + ex);
					}
					//如果容器类加载器加载定义路径的Bean定义资源失败，则启用容器类路径扫描器扫描给定路径包及其子包中的类
					int count = scanner.scan(configLocation);
					if (count == 0 && logger.isDebugEnabled()) {
						logger.debug("No component classes found for specified class/package [" + configLocation + "]");
					}
				}
			}
		}
	}


	/**
	 * Build an {@link AnnotatedBeanDefinitionReader} for the given bean factory.
	 * <p>This should be pre-configured with the {@code Environment} (if desired)
	 * but not with a {@code BeanNameGenerator} or {@code ScopeMetadataResolver} yet.
	 * @param beanFactory the bean factory to load bean definitions into
	 * @since 4.1.9
	 * @see #getEnvironment()
	 * @see #getBeanNameGenerator()
	 * @see #getScopeMetadataResolver()
	 */
	protected AnnotatedBeanDefinitionReader getAnnotatedBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
		return new AnnotatedBeanDefinitionReader(beanFactory, getEnvironment());
	}

	/**
	 * Build a {@link ClassPathBeanDefinitionScanner} for the given bean factory.
	 * <p>This should be pre-configured with the {@code Environment} (if desired)
	 * but not with a {@code BeanNameGenerator} or {@code ScopeMetadataResolver} yet.
	 * @param beanFactory the bean factory to load bean definitions into
	 * @since 4.1.9
	 * @see #getEnvironment()
	 * @see #getBeanNameGenerator()
	 * @see #getScopeMetadataResolver()
	 */
	protected ClassPathBeanDefinitionScanner getClassPathBeanDefinitionScanner(DefaultListableBeanFactory beanFactory) {
		return new ClassPathBeanDefinitionScanner(beanFactory, true, getEnvironment());
	}

}
