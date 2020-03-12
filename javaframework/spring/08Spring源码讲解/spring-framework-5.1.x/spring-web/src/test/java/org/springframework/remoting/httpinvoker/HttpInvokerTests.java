/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.remoting.httpinvoker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInvocation;

import org.junit.Test;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.mock.web.test.MockHttpServletRequest;
import org.springframework.mock.web.test.MockHttpServletResponse;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.support.DefaultRemoteInvocationExecutor;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.springframework.tests.sample.beans.ITestBean;
import org.springframework.tests.sample.beans.TestBean;

import static org.junit.Assert.*;

/**
 * @author Juergen Hoeller
 * @since 09.08.2004
 */
public class HttpInvokerTests {

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporter() throws Throwable {
		doTestHttpInvokerProxyFactoryBeanAndServiceExporter(false);
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithExplicitClassLoader() throws Throwable {
		doTestHttpInvokerProxyFactoryBeanAndServiceExporter(true);
	}

	private void doTestHttpInvokerProxyFactoryBeanAndServiceExporter(boolean explicitClassLoader) throws Throwable {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");

		pfb.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
			@Override
			protected RemoteInvocationResult doExecuteRequest(
					HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
				assertEquals("http://myurl", config.getServiceUrl());
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				request.setContent(baos.toByteArray());
				exporter.handleRequest(request, response);
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
		});
		if (explicitClassLoader) {
			((BeanClassLoaderAware) pfb.getHttpInvokerRequestExecutor()).setBeanClassLoader(getClass().getClassLoader());
		}

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		assertEquals("myname", proxy.getName());
		assertEquals(99, proxy.getAge());
		proxy.setAge(50);
		assertEquals(50, proxy.getAge());
		proxy.setStringArray(new String[] {"str1", "str2"});
		assertTrue(Arrays.equals(new String[] {"str1", "str2"}, proxy.getStringArray()));
		proxy.setSomeIntegerArray(new Integer[] {1, 2, 3});
		assertTrue(Arrays.equals(new Integer[] {1, 2, 3}, proxy.getSomeIntegerArray()));
		proxy.setNestedIntegerArray(new Integer[][] {{1, 2, 3}, {4, 5, 6}});
		Integer[][] integerArray = proxy.getNestedIntegerArray();
		assertTrue(Arrays.equals(new Integer[] {1, 2, 3}, integerArray[0]));
		assertTrue(Arrays.equals(new Integer[] {4, 5, 6}, integerArray[1]));
		proxy.setSomeIntArray(new int[] {1, 2, 3});
		assertTrue(Arrays.equals(new int[] {1, 2, 3}, proxy.getSomeIntArray()));
		proxy.setNestedIntArray(new int[][] {{1, 2, 3}, {4, 5, 6}});
		int[][] intArray = proxy.getNestedIntArray();
		assertTrue(Arrays.equals(new int[] {1, 2, 3}, intArray[0]));
		assertTrue(Arrays.equals(new int[] {4, 5, 6}, intArray[1]));

		try {
			proxy.exceptional(new IllegalStateException());
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
		try {
			proxy.exceptional(new IllegalAccessException());
			fail("Should have thrown IllegalAccessException");
		}
		catch (IllegalAccessException ex) {
			// expected
		}
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithIOException() throws Exception {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");

		pfb.setHttpInvokerRequestExecutor(new HttpInvokerRequestExecutor() {
			@Override
			public RemoteInvocationResult executeRequest(
					HttpInvokerClientConfiguration config, RemoteInvocation invocation) throws IOException {
				throw new IOException("argh");
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		try {
			proxy.setAge(50);
			fail("Should have thrown RemoteAccessException");
		}
		catch (RemoteAccessException ex) {
			// expected
			assertTrue(ex.getCause() instanceof IOException);
		}
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithGzipCompression() throws Throwable {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter() {
			@Override
			protected InputStream decorateInputStream(HttpServletRequest request, InputStream is) throws IOException {
				if ("gzip".equals(request.getHeader("Compression"))) {
					return new GZIPInputStream(is);
				}
				else {
					return is;
				}
			}
			@Override
			protected OutputStream decorateOutputStream(
					HttpServletRequest request, HttpServletResponse response, OutputStream os) throws IOException {
				if ("gzip".equals(request.getHeader("Compression"))) {
					return new GZIPOutputStream(os);
				}
				else {
					return os;
				}
			}
		};
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");

		pfb.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
			@Override
			protected RemoteInvocationResult doExecuteRequest(
					HttpInvokerClientConfiguration config, ByteArrayOutputStream baos)
					throws IOException, ClassNotFoundException {
				assertEquals("http://myurl", config.getServiceUrl());
				MockHttpServletRequest request = new MockHttpServletRequest();
				request.addHeader("Compression", "gzip");
				MockHttpServletResponse response = new MockHttpServletResponse();
				request.setContent(baos.toByteArray());
				try {
					exporter.handleRequest(request, response);
				}
				catch (ServletException ex) {
					throw new IOException(ex.toString());
				}
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
			@Override
			protected OutputStream decorateOutputStream(OutputStream os) throws IOException {
				return new GZIPOutputStream(os);
			}
			@Override
			protected InputStream decorateInputStream(InputStream is) throws IOException {
				return new GZIPInputStream(is);
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		assertEquals("myname", proxy.getName());
		assertEquals(99, proxy.getAge());
		proxy.setAge(50);
		assertEquals(50, proxy.getAge());

		try {
			proxy.exceptional(new IllegalStateException());
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
		try {
			proxy.exceptional(new IllegalAccessException());
			fail("Should have thrown IllegalAccessException");
		}
		catch (IllegalAccessException ex) {
			// expected
		}
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithWrappedInvocations() throws Throwable {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter() {
			@Override
			protected RemoteInvocation doReadRemoteInvocation(ObjectInputStream ois)
					throws IOException, ClassNotFoundException {
				Object obj = ois.readObject();
				if (!(obj instanceof TestRemoteInvocationWrapper)) {
					throw new IOException("Deserialized object needs to be assignable to type [" +
							TestRemoteInvocationWrapper.class.getName() + "]: " + obj);
				}
				return ((TestRemoteInvocationWrapper) obj).remoteInvocation;
			}
			@Override
			protected void doWriteRemoteInvocationResult(RemoteInvocationResult result, ObjectOutputStream oos)
					throws IOException {
				oos.writeObject(new TestRemoteInvocationResultWrapper(result));
			}
		};
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");

		pfb.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
			@Override
			protected RemoteInvocationResult doExecuteRequest(
					HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
				assertEquals("http://myurl", config.getServiceUrl());
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				request.setContent(baos.toByteArray());
				exporter.handleRequest(request, response);
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
			@Override
			protected void doWriteRemoteInvocation(RemoteInvocation invocation, ObjectOutputStream oos) throws IOException {
				oos.writeObject(new TestRemoteInvocationWrapper(invocation));
			}
			@Override
			protected RemoteInvocationResult doReadRemoteInvocationResult(ObjectInputStream ois)
					throws IOException, ClassNotFoundException {
				Object obj = ois.readObject();
				if (!(obj instanceof TestRemoteInvocationResultWrapper)) {
					throw new IOException("Deserialized object needs to be assignable to type ["
							+ TestRemoteInvocationResultWrapper.class.getName() + "]: " + obj);
				}
				return ((TestRemoteInvocationResultWrapper) obj).remoteInvocationResult;
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		assertEquals("myname", proxy.getName());
		assertEquals(99, proxy.getAge());
		proxy.setAge(50);
		assertEquals(50, proxy.getAge());

		try {
			proxy.exceptional(new IllegalStateException());
			fail("Should have thrown IllegalStateException");
		}
		catch (IllegalStateException ex) {
			// expected
		}
		try {
			proxy.exceptional(new IllegalAccessException());
			fail("Should have thrown IllegalAccessException");
		}
		catch (IllegalAccessException ex) {
			// expected
		}
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithInvocationAttributes() throws Exception {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.setRemoteInvocationExecutor(new DefaultRemoteInvocationExecutor() {
			@Override
			public Object invoke(RemoteInvocation invocation, Object targetObject)
					throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
				assertNotNull(invocation.getAttributes());
				assertEquals(1, invocation.getAttributes().size());
				assertEquals("myValue", invocation.getAttributes().get("myKey"));
				assertEquals("myValue", invocation.getAttribute("myKey"));
				return super.invoke(invocation, targetObject);
			}
		});
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");
		pfb.setRemoteInvocationFactory(new RemoteInvocationFactory() {
			@Override
			public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
				RemoteInvocation invocation = new RemoteInvocation(methodInvocation);
				invocation.addAttribute("myKey", "myValue");
				try {
					invocation.addAttribute("myKey", "myValue");
					fail("Should have thrown IllegalStateException");
				}
				catch (IllegalStateException ex) {
					// expected: already defined
				}
				assertNotNull(invocation.getAttributes());
				assertEquals(1, invocation.getAttributes().size());
				assertEquals("myValue", invocation.getAttributes().get("myKey"));
				assertEquals("myValue", invocation.getAttribute("myKey"));
				return invocation;
			}
		});

		pfb.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
			@Override
			protected RemoteInvocationResult doExecuteRequest(
					HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
				assertEquals("http://myurl", config.getServiceUrl());
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				request.setContent(baos.toByteArray());
				exporter.handleRequest(request, response);
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		assertEquals("myname", proxy.getName());
		assertEquals(99, proxy.getAge());
	}

	@Test
	public void httpInvokerProxyFactoryBeanAndServiceExporterWithCustomInvocationObject() throws Exception {
		TestBean target = new TestBean("myname", 99);

		final HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
		exporter.setServiceInterface(ITestBean.class);
		exporter.setService(target);
		exporter.setRemoteInvocationExecutor(new DefaultRemoteInvocationExecutor() {
			@Override
			public Object invoke(RemoteInvocation invocation, Object targetObject)
					throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
				assertTrue(invocation instanceof TestRemoteInvocation);
				assertNull(invocation.getAttributes());
				assertNull(invocation.getAttribute("myKey"));
				return super.invoke(invocation, targetObject);
			}
		});
		exporter.afterPropertiesSet();

		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl("http://myurl");
		pfb.setRemoteInvocationFactory(new RemoteInvocationFactory() {
			@Override
			public RemoteInvocation createRemoteInvocation(MethodInvocation methodInvocation) {
				RemoteInvocation invocation = new TestRemoteInvocation(methodInvocation);
				assertNull(invocation.getAttributes());
				assertNull(invocation.getAttribute("myKey"));
				return invocation;
			}
		});

		pfb.setHttpInvokerRequestExecutor(new AbstractHttpInvokerRequestExecutor() {
			@Override
			protected RemoteInvocationResult doExecuteRequest(
					HttpInvokerClientConfiguration config, ByteArrayOutputStream baos) throws Exception {
				assertEquals("http://myurl", config.getServiceUrl());
				MockHttpServletRequest request = new MockHttpServletRequest();
				MockHttpServletResponse response = new MockHttpServletResponse();
				request.setContent(baos.toByteArray());
				exporter.handleRequest(request, response);
				return readRemoteInvocationResult(
						new ByteArrayInputStream(response.getContentAsByteArray()), config.getCodebaseUrl());
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();
		assertEquals("myname", proxy.getName());
		assertEquals(99, proxy.getAge());
	}

	@Test
	public void httpInvokerWithSpecialLocalMethods() throws Exception {
		String serviceUrl = "http://myurl";
		HttpInvokerProxyFactoryBean pfb = new HttpInvokerProxyFactoryBean();
		pfb.setServiceInterface(ITestBean.class);
		pfb.setServiceUrl(serviceUrl);

		pfb.setHttpInvokerRequestExecutor(new HttpInvokerRequestExecutor() {
			@Override
			public RemoteInvocationResult executeRequest(
					HttpInvokerClientConfiguration config, RemoteInvocation invocation) throws IOException {
				throw new IOException("argh");
			}
		});

		pfb.afterPropertiesSet();
		ITestBean proxy = (ITestBean) pfb.getObject();

		// shouldn't go through to remote service
		assertTrue(proxy.toString().contains("HTTP invoker"));
		assertTrue(proxy.toString().contains(serviceUrl));
		assertEquals(proxy.hashCode(), proxy.hashCode());
		assertTrue(proxy.equals(proxy));

		// should go through
		try {
			proxy.setAge(50);
			fail("Should have thrown RemoteAccessException");
		}
		catch (RemoteAccessException ex) {
			// expected
			assertTrue(ex.getCause() instanceof IOException);
		}
	}


	@SuppressWarnings("serial")
	private static class TestRemoteInvocation extends RemoteInvocation {

		public TestRemoteInvocation(MethodInvocation methodInvocation) {
			super(methodInvocation);
		}
	}


	@SuppressWarnings("serial")
	private static class TestRemoteInvocationWrapper implements Serializable {

		private final RemoteInvocation remoteInvocation;

		public TestRemoteInvocationWrapper(RemoteInvocation remoteInvocation) {
			this.remoteInvocation = remoteInvocation;
		}
	}


	@SuppressWarnings("serial")
	private static class TestRemoteInvocationResultWrapper implements Serializable {

		private final RemoteInvocationResult remoteInvocationResult;

		public TestRemoteInvocationResultWrapper(RemoteInvocationResult remoteInvocationResult) {
			this.remoteInvocationResult = remoteInvocationResult;
		}
	}

}
