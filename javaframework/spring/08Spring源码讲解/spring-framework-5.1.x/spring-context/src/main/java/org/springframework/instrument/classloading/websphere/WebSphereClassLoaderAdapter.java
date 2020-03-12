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

package org.springframework.instrument.classloading.websphere;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Reflective wrapper around a WebSphere 7+ class loader. Used to
 * encapsulate the classloader-specific methods (discovered and
 * called through reflection) from the load-time weaver.
 *
 * @author Costin Leau
 * @author Juergen Hoeller
 * @since 3.1
 */
class WebSphereClassLoaderAdapter {

	private static final String COMPOUND_CLASS_LOADER_NAME = "com.ibm.ws.classloader.CompoundClassLoader";

	private static final String CLASS_PRE_PROCESSOR_NAME = "com.ibm.websphere.classloader.ClassLoaderInstancePreDefinePlugin";

	private static final String PLUGINS_FIELD = "preDefinePlugins";


	private ClassLoader classLoader;

	private Class<?> wsPreProcessorClass;

	private Method addPreDefinePlugin;

	private Constructor<? extends ClassLoader> cloneConstructor;

	private Field transformerList;


	public WebSphereClassLoaderAdapter(ClassLoader classLoader) {
		Class<?> wsCompoundClassLoaderClass;
		try {
			wsCompoundClassLoaderClass = classLoader.loadClass(COMPOUND_CLASS_LOADER_NAME);
			this.cloneConstructor = classLoader.getClass().getDeclaredConstructor(wsCompoundClassLoaderClass);
			this.cloneConstructor.setAccessible(true);

			this.wsPreProcessorClass = classLoader.loadClass(CLASS_PRE_PROCESSOR_NAME);
			this.addPreDefinePlugin = classLoader.getClass().getMethod("addPreDefinePlugin", this.wsPreProcessorClass);
			this.transformerList = wsCompoundClassLoaderClass.getDeclaredField(PLUGINS_FIELD);
			this.transformerList.setAccessible(true);
		}
		catch (Throwable ex) {
			throw new IllegalStateException(
					"Could not initialize WebSphere LoadTimeWeaver because WebSphere API classes are not available", ex);
		}

		if (!wsCompoundClassLoaderClass.isInstance(classLoader)) {
			throw new IllegalArgumentException(
					"ClassLoader must be an instance of [" + COMPOUND_CLASS_LOADER_NAME + "]: " + classLoader);
		}
		this.classLoader = classLoader;
	}


	public ClassLoader getClassLoader() {
		return this.classLoader;
	}

	public void addTransformer(ClassFileTransformer transformer) {
		Assert.notNull(transformer, "ClassFileTransformer must not be null");
		try {
			InvocationHandler adapter = new WebSphereClassPreDefinePlugin(transformer);
			Object adapterInstance = Proxy.newProxyInstance(this.wsPreProcessorClass.getClassLoader(),
					new Class<?>[] {this.wsPreProcessorClass}, adapter);
			this.addPreDefinePlugin.invoke(this.classLoader, adapterInstance);
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("WebSphere addPreDefinePlugin method threw exception", ex.getCause());
		}
		catch (Throwable ex) {
			throw new IllegalStateException("Could not invoke WebSphere addPreDefinePlugin method", ex);
		}
	}

	public ClassLoader getThrowawayClassLoader() {
		try {
			ClassLoader loader = this.cloneConstructor.newInstance(getClassLoader());
			// Clear out the transformers (copied as well)
			List<?> list = (List<?>) this.transformerList.get(loader);
			list.clear();
			return loader;
		}
		catch (InvocationTargetException ex) {
			throw new IllegalStateException("WebSphere CompoundClassLoader constructor failed", ex.getCause());
		}
		catch (Throwable ex) {
			throw new IllegalStateException("Could not construct WebSphere CompoundClassLoader", ex);
		}
	}

}
