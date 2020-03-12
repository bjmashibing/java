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

package org.springframework.web.servlet.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.util.UrlPathHelper;

/**
 * Convenience methods for use in MVC namespace BeanDefinitionParsers.
 *
 * @author Rossen Stoyanchev
 * @author Brian Clozel
 * @since 3.1
 */
public abstract class MvcNamespaceUtils {

	private static final String BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME =
			BeanNameUrlHandlerMapping.class.getName();

	private static final String SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME =
			SimpleControllerHandlerAdapter.class.getName();

	private static final String HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME =
			HttpRequestHandlerAdapter.class.getName();

	private static final String URL_PATH_HELPER_BEAN_NAME = "mvcUrlPathHelper";

	private static final String PATH_MATCHER_BEAN_NAME = "mvcPathMatcher";

	private static final String CORS_CONFIGURATION_BEAN_NAME = "mvcCorsConfigurations";

	private static final String HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME = "mvcHandlerMappingIntrospector";


	public static void registerDefaultComponents(ParserContext parserContext, @Nullable Object source) {
		registerBeanNameUrlHandlerMapping(parserContext, source);
		registerHttpRequestHandlerAdapter(parserContext, source);
		registerSimpleControllerHandlerAdapter(parserContext, source);
		registerHandlerMappingIntrospector(parserContext, source);
	}

	/**
	 * Adds an alias to an existing well-known name or registers a new instance of a {@link UrlPathHelper}
	 * under that well-known name, unless already registered.
	 * @return a RuntimeBeanReference to this {@link UrlPathHelper} instance
	 */
	public static RuntimeBeanReference registerUrlPathHelper(
			@Nullable RuntimeBeanReference urlPathHelperRef, ParserContext parserContext, @Nullable Object source) {

		if (urlPathHelperRef != null) {
			if (parserContext.getRegistry().isAlias(URL_PATH_HELPER_BEAN_NAME)) {
				parserContext.getRegistry().removeAlias(URL_PATH_HELPER_BEAN_NAME);
			}
			parserContext.getRegistry().registerAlias(urlPathHelperRef.getBeanName(), URL_PATH_HELPER_BEAN_NAME);
		}
		else if (!parserContext.getRegistry().isAlias(URL_PATH_HELPER_BEAN_NAME) &&
				!parserContext.getRegistry().containsBeanDefinition(URL_PATH_HELPER_BEAN_NAME)) {
			RootBeanDefinition urlPathHelperDef = new RootBeanDefinition(UrlPathHelper.class);
			urlPathHelperDef.setSource(source);
			urlPathHelperDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			parserContext.getRegistry().registerBeanDefinition(URL_PATH_HELPER_BEAN_NAME, urlPathHelperDef);
			parserContext.registerComponent(new BeanComponentDefinition(urlPathHelperDef, URL_PATH_HELPER_BEAN_NAME));
		}
		return new RuntimeBeanReference(URL_PATH_HELPER_BEAN_NAME);
	}

	/**
	 * Adds an alias to an existing well-known name or registers a new instance of a {@link PathMatcher}
	 * under that well-known name, unless already registered.
	 * @return a RuntimeBeanReference to this {@link PathMatcher} instance
	 */
	public static RuntimeBeanReference registerPathMatcher(@Nullable RuntimeBeanReference pathMatcherRef,
			ParserContext parserContext, @Nullable Object source) {

		if (pathMatcherRef != null) {
			if (parserContext.getRegistry().isAlias(PATH_MATCHER_BEAN_NAME)) {
				parserContext.getRegistry().removeAlias(PATH_MATCHER_BEAN_NAME);
			}
			parserContext.getRegistry().registerAlias(pathMatcherRef.getBeanName(), PATH_MATCHER_BEAN_NAME);
		}
		else if (!parserContext.getRegistry().isAlias(PATH_MATCHER_BEAN_NAME) &&
				!parserContext.getRegistry().containsBeanDefinition(PATH_MATCHER_BEAN_NAME)) {
			RootBeanDefinition pathMatcherDef = new RootBeanDefinition(AntPathMatcher.class);
			pathMatcherDef.setSource(source);
			pathMatcherDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			parserContext.getRegistry().registerBeanDefinition(PATH_MATCHER_BEAN_NAME, pathMatcherDef);
			parserContext.registerComponent(new BeanComponentDefinition(pathMatcherDef, PATH_MATCHER_BEAN_NAME));
		}
		return new RuntimeBeanReference(PATH_MATCHER_BEAN_NAME);
	}

	/**
	 * Registers  an {@link HttpRequestHandlerAdapter} under a well-known
	 * name unless already registered.
	 */
	private static void registerBeanNameUrlHandlerMapping(ParserContext context, @Nullable Object source) {
		if (!context.getRegistry().containsBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME)) {
			RootBeanDefinition mappingDef = new RootBeanDefinition(BeanNameUrlHandlerMapping.class);
			mappingDef.setSource(source);
			mappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			mappingDef.getPropertyValues().add("order", 2);	// consistent with WebMvcConfigurationSupport
			RuntimeBeanReference corsRef = MvcNamespaceUtils.registerCorsConfigurations(null, context, source);
			mappingDef.getPropertyValues().add("corsConfigurations", corsRef);
			context.getRegistry().registerBeanDefinition(BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME, mappingDef);
			context.registerComponent(new BeanComponentDefinition(mappingDef, BEAN_NAME_URL_HANDLER_MAPPING_BEAN_NAME));
		}
	}

	/**
	 * Registers  an {@link HttpRequestHandlerAdapter} under a well-known
	 * name unless already registered.
	 */
	private static void registerHttpRequestHandlerAdapter(ParserContext context, @Nullable Object source) {
		if (!context.getRegistry().containsBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME)) {
			RootBeanDefinition adapterDef = new RootBeanDefinition(HttpRequestHandlerAdapter.class);
			adapterDef.setSource(source);
			adapterDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			context.getRegistry().registerBeanDefinition(HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME, adapterDef);
			context.registerComponent(new BeanComponentDefinition(adapterDef, HTTP_REQUEST_HANDLER_ADAPTER_BEAN_NAME));
		}
	}

	/**
	 * Registers a {@link SimpleControllerHandlerAdapter} under a well-known
	 * name unless already registered.
	 */
	private static void registerSimpleControllerHandlerAdapter(ParserContext context, @Nullable Object source) {
		if (!context.getRegistry().containsBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME)) {
			RootBeanDefinition beanDef = new RootBeanDefinition(SimpleControllerHandlerAdapter.class);
			beanDef.setSource(source);
			beanDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			context.getRegistry().registerBeanDefinition(SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME, beanDef);
			context.registerComponent(new BeanComponentDefinition(beanDef, SIMPLE_CONTROLLER_HANDLER_ADAPTER_BEAN_NAME));
		}
	}

	/**
	 * Registers a {@code Map<String, CorsConfiguration>} (mapped {@code CorsConfiguration}s)
	 * under a well-known name unless already registered. The bean definition may be updated
	 * if a non-null CORS configuration is provided.
	 * @return a RuntimeBeanReference to this {@code Map<String, CorsConfiguration>} instance
	 */
	public static RuntimeBeanReference registerCorsConfigurations(
			@Nullable Map<String, CorsConfiguration> corsConfigurations,
			ParserContext context, @Nullable Object source) {

		if (!context.getRegistry().containsBeanDefinition(CORS_CONFIGURATION_BEAN_NAME)) {
			RootBeanDefinition corsDef = new RootBeanDefinition(LinkedHashMap.class);
			corsDef.setSource(source);
			corsDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			if (corsConfigurations != null) {
				corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
			}
			context.getReaderContext().getRegistry().registerBeanDefinition(CORS_CONFIGURATION_BEAN_NAME, corsDef);
			context.registerComponent(new BeanComponentDefinition(corsDef, CORS_CONFIGURATION_BEAN_NAME));
		}
		else if (corsConfigurations != null) {
			BeanDefinition corsDef = context.getRegistry().getBeanDefinition(CORS_CONFIGURATION_BEAN_NAME);
			corsDef.getConstructorArgumentValues().addIndexedArgumentValue(0, corsConfigurations);
		}
		return new RuntimeBeanReference(CORS_CONFIGURATION_BEAN_NAME);
	}

	/**
	 * Registers  an {@link HandlerMappingIntrospector} under a well-known name
	 * unless already registered.
	 */
	private static void registerHandlerMappingIntrospector(ParserContext parserContext, @Nullable Object source) {
		if (!parserContext.getRegistry().containsBeanDefinition(HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME)) {
			RootBeanDefinition beanDef = new RootBeanDefinition(HandlerMappingIntrospector.class);
			beanDef.setSource(source);
			beanDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			beanDef.setLazyInit(true);
			parserContext.getRegistry().registerBeanDefinition(HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME, beanDef);
			parserContext.registerComponent(new BeanComponentDefinition(beanDef, HANDLER_MAPPING_INTROSPECTOR_BEAN_NAME));
		}
	}

	/**
	 * Find the {@code ContentNegotiationManager} bean created by or registered
	 * with the {@code annotation-driven} element.
	 * @return a bean definition, bean reference, or {@code null} if none defined
	 */
	@Nullable
	public static Object getContentNegotiationManager(ParserContext context) {
		String name = AnnotationDrivenBeanDefinitionParser.HANDLER_MAPPING_BEAN_NAME;
		if (context.getRegistry().containsBeanDefinition(name)) {
			BeanDefinition handlerMappingBeanDef = context.getRegistry().getBeanDefinition(name);
			return handlerMappingBeanDef.getPropertyValues().get("contentNegotiationManager");
		}
		name = AnnotationDrivenBeanDefinitionParser.CONTENT_NEGOTIATION_MANAGER_BEAN_NAME;
		if (context.getRegistry().containsBeanDefinition(name)) {
			return new RuntimeBeanReference(name);
		}
		return null;
	}

}
