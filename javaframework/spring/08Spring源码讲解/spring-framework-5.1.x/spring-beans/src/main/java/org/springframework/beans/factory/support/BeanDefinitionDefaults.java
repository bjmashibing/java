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

package org.springframework.beans.factory.support;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

/**
 * A simple holder for {@code BeanDefinition} property defaults.
 *
 * @author Mark Fisher
 * @author Juergen Hoeller
 * @since 2.5
 */
public class BeanDefinitionDefaults {

	private boolean lazyInit;

	private int autowireMode = AbstractBeanDefinition.AUTOWIRE_NO;

	private int dependencyCheck = AbstractBeanDefinition.DEPENDENCY_CHECK_NONE;

	@Nullable
	private String initMethodName;

	@Nullable
	private String destroyMethodName;


	/**
	 * Set whether beans should be lazily initialized by default.
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 */
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}

	/**
	 * Return whether beans should be lazily initialized by default, i.e. not
	 * eagerly instantiated on startup. Only applicable to singleton beans.
	 * @return whether to apply lazy-init semantics ({@code false} by default)
	 */
	public boolean isLazyInit() {
		return this.lazyInit;
	}

	/**
	 * Set the autowire mode. This determines whether any automagical detection
	 * and setting of bean references will happen. Default is AUTOWIRE_NO
	 * which means there won't be convention-based autowiring by name or type
	 * (however, there may still be explicit annotation-driven autowiring).
	 * @param autowireMode the autowire mode to set.
	 * Must be one of the constants defined in {@link AbstractBeanDefinition}.
	 */
	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}

	/**
	 * Return the default autowire mode.
	 */
	public int getAutowireMode() {
		return this.autowireMode;
	}

	/**
	 * Set the dependency check code.
	 * @param dependencyCheck the code to set.
	 * Must be one of the constants defined in {@link AbstractBeanDefinition}.
	 */
	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}

	/**
	 * Return the default dependency check code.
	 */
	public int getDependencyCheck() {
		return this.dependencyCheck;
	}

	/**
	 * Set the name of the default initializer method.
	 */
	public void setInitMethodName(@Nullable String initMethodName) {
		this.initMethodName = (StringUtils.hasText(initMethodName) ? initMethodName : null);
	}

	/**
	 * Return the name of the default initializer method.
	 */
	@Nullable
	public String getInitMethodName() {
		return this.initMethodName;
	}

	/**
	 * Set the name of the default destroy method.
	 */
	public void setDestroyMethodName(@Nullable String destroyMethodName) {
		this.destroyMethodName = (StringUtils.hasText(destroyMethodName) ? destroyMethodName : null);
	}

	/**
	 * Return the name of the default destroy method.
	 */
	@Nullable
	public String getDestroyMethodName() {
		return this.destroyMethodName;
	}

}
