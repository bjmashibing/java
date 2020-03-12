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

package org.springframework.util;

import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import org.springframework.tests.sample.objects.DerivedTestObject;
import org.springframework.tests.sample.objects.ITestInterface;
import org.springframework.tests.sample.objects.ITestObject;
import org.springframework.tests.sample.objects.TestObject;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ClassUtils}.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Rick Evans
 * @author Sam Brannen
 */
public class ClassUtilsTests {

	private final ClassLoader classLoader = getClass().getClassLoader();


	@Before
	public void clearStatics() {
		InnerClass.noArgCalled = false;
		InnerClass.argCalled = false;
		InnerClass.overloadedCalled = false;
	}


	@Test
	public void testIsPresent() {
		assertTrue(ClassUtils.isPresent("java.lang.String", classLoader));
		assertFalse(ClassUtils.isPresent("java.lang.MySpecialString", classLoader));
	}

	@Test
	public void testForName() throws ClassNotFoundException {
		assertEquals(String.class, ClassUtils.forName("java.lang.String", classLoader));
		assertEquals(String[].class, ClassUtils.forName("java.lang.String[]", classLoader));
		assertEquals(String[].class, ClassUtils.forName(String[].class.getName(), classLoader));
		assertEquals(String[][].class, ClassUtils.forName(String[][].class.getName(), classLoader));
		assertEquals(String[][][].class, ClassUtils.forName(String[][][].class.getName(), classLoader));
		assertEquals(TestObject.class, ClassUtils.forName("org.springframework.tests.sample.objects.TestObject", classLoader));
		assertEquals(TestObject[].class, ClassUtils.forName("org.springframework.tests.sample.objects.TestObject[]", classLoader));
		assertEquals(TestObject[].class, ClassUtils.forName(TestObject[].class.getName(), classLoader));
		assertEquals(TestObject[][].class, ClassUtils.forName("org.springframework.tests.sample.objects.TestObject[][]", classLoader));
		assertEquals(TestObject[][].class, ClassUtils.forName(TestObject[][].class.getName(), classLoader));
		assertEquals(short[][][].class, ClassUtils.forName("[[[S", classLoader));
	}

	@Test
	public void testForNameWithPrimitiveClasses() throws ClassNotFoundException {
		assertEquals(boolean.class, ClassUtils.forName("boolean", classLoader));
		assertEquals(byte.class, ClassUtils.forName("byte", classLoader));
		assertEquals(char.class, ClassUtils.forName("char", classLoader));
		assertEquals(short.class, ClassUtils.forName("short", classLoader));
		assertEquals(int.class, ClassUtils.forName("int", classLoader));
		assertEquals(long.class, ClassUtils.forName("long", classLoader));
		assertEquals(float.class, ClassUtils.forName("float", classLoader));
		assertEquals(double.class, ClassUtils.forName("double", classLoader));
		assertEquals(void.class, ClassUtils.forName("void", classLoader));
	}

	@Test
	public void testForNameWithPrimitiveArrays() throws ClassNotFoundException {
		assertEquals(boolean[].class, ClassUtils.forName("boolean[]", classLoader));
		assertEquals(byte[].class, ClassUtils.forName("byte[]", classLoader));
		assertEquals(char[].class, ClassUtils.forName("char[]", classLoader));
		assertEquals(short[].class, ClassUtils.forName("short[]", classLoader));
		assertEquals(int[].class, ClassUtils.forName("int[]", classLoader));
		assertEquals(long[].class, ClassUtils.forName("long[]", classLoader));
		assertEquals(float[].class, ClassUtils.forName("float[]", classLoader));
		assertEquals(double[].class, ClassUtils.forName("double[]", classLoader));
	}

	@Test
	public void testForNameWithPrimitiveArraysInternalName() throws ClassNotFoundException {
		assertEquals(boolean[].class, ClassUtils.forName(boolean[].class.getName(), classLoader));
		assertEquals(byte[].class, ClassUtils.forName(byte[].class.getName(), classLoader));
		assertEquals(char[].class, ClassUtils.forName(char[].class.getName(), classLoader));
		assertEquals(short[].class, ClassUtils.forName(short[].class.getName(), classLoader));
		assertEquals(int[].class, ClassUtils.forName(int[].class.getName(), classLoader));
		assertEquals(long[].class, ClassUtils.forName(long[].class.getName(), classLoader));
		assertEquals(float[].class, ClassUtils.forName(float[].class.getName(), classLoader));
		assertEquals(double[].class, ClassUtils.forName(double[].class.getName(), classLoader));
	}

	@Test
	public void testIsCacheSafe() {
		ClassLoader childLoader1 = new ClassLoader(classLoader) {};
		ClassLoader childLoader2 = new ClassLoader(classLoader) {};
		ClassLoader childLoader3 = new ClassLoader(classLoader) {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {
				return childLoader1.loadClass(name);
			}
		};
		Class<?> composite = ClassUtils.createCompositeInterface(
				new Class<?>[] {Serializable.class, Externalizable.class}, childLoader1);

		assertTrue(ClassUtils.isCacheSafe(String.class, null));
		assertTrue(ClassUtils.isCacheSafe(String.class, classLoader));
		assertTrue(ClassUtils.isCacheSafe(String.class, childLoader1));
		assertTrue(ClassUtils.isCacheSafe(String.class, childLoader2));
		assertTrue(ClassUtils.isCacheSafe(String.class, childLoader3));
		assertFalse(ClassUtils.isCacheSafe(InnerClass.class, null));
		assertTrue(ClassUtils.isCacheSafe(InnerClass.class, classLoader));
		assertTrue(ClassUtils.isCacheSafe(InnerClass.class, childLoader1));
		assertTrue(ClassUtils.isCacheSafe(InnerClass.class, childLoader2));
		assertTrue(ClassUtils.isCacheSafe(InnerClass.class, childLoader3));
		assertFalse(ClassUtils.isCacheSafe(composite, null));
		assertFalse(ClassUtils.isCacheSafe(composite, classLoader));
		assertTrue(ClassUtils.isCacheSafe(composite, childLoader1));
		assertFalse(ClassUtils.isCacheSafe(composite, childLoader2));
		assertTrue(ClassUtils.isCacheSafe(composite, childLoader3));
	}

	@Test
	public void testGetShortName() {
		String className = ClassUtils.getShortName(getClass());
		assertEquals("Class name did not match", "ClassUtilsTests", className);
	}

	@Test
	public void testGetShortNameForObjectArrayClass() {
		String className = ClassUtils.getShortName(Object[].class);
		assertEquals("Class name did not match", "Object[]", className);
	}

	@Test
	public void testGetShortNameForMultiDimensionalObjectArrayClass() {
		String className = ClassUtils.getShortName(Object[][].class);
		assertEquals("Class name did not match", "Object[][]", className);
	}

	@Test
	public void testGetShortNameForPrimitiveArrayClass() {
		String className = ClassUtils.getShortName(byte[].class);
		assertEquals("Class name did not match", "byte[]", className);
	}

	@Test
	public void testGetShortNameForMultiDimensionalPrimitiveArrayClass() {
		String className = ClassUtils.getShortName(byte[][][].class);
		assertEquals("Class name did not match", "byte[][][]", className);
	}

	@Test
	public void testGetShortNameForInnerClass() {
		String className = ClassUtils.getShortName(InnerClass.class);
		assertEquals("Class name did not match", "ClassUtilsTests.InnerClass", className);
	}

	@Test
	public void testGetShortNameAsProperty() {
		String shortName = ClassUtils.getShortNameAsProperty(this.getClass());
		assertEquals("Class name did not match", "classUtilsTests", shortName);
	}

	@Test
	public void testGetClassFileName() {
		assertEquals("String.class", ClassUtils.getClassFileName(String.class));
		assertEquals("ClassUtilsTests.class", ClassUtils.getClassFileName(getClass()));
	}

	@Test
	public void testGetPackageName() {
		assertEquals("java.lang", ClassUtils.getPackageName(String.class));
		assertEquals(getClass().getPackage().getName(), ClassUtils.getPackageName(getClass()));
	}

	@Test
	public void testGetQualifiedName() {
		String className = ClassUtils.getQualifiedName(getClass());
		assertEquals("Class name did not match", "org.springframework.util.ClassUtilsTests", className);
	}

	@Test
	public void testGetQualifiedNameForObjectArrayClass() {
		String className = ClassUtils.getQualifiedName(Object[].class);
		assertEquals("Class name did not match", "java.lang.Object[]", className);
	}

	@Test
	public void testGetQualifiedNameForMultiDimensionalObjectArrayClass() {
		String className = ClassUtils.getQualifiedName(Object[][].class);
		assertEquals("Class name did not match", "java.lang.Object[][]", className);
	}

	@Test
	public void testGetQualifiedNameForPrimitiveArrayClass() {
		String className = ClassUtils.getQualifiedName(byte[].class);
		assertEquals("Class name did not match", "byte[]", className);
	}

	@Test
	public void testGetQualifiedNameForMultiDimensionalPrimitiveArrayClass() {
		String className = ClassUtils.getQualifiedName(byte[][].class);
		assertEquals("Class name did not match", "byte[][]", className);
	}

	@Test
	public void testHasMethod() {
		assertTrue(ClassUtils.hasMethod(Collection.class, "size"));
		assertTrue(ClassUtils.hasMethod(Collection.class, "remove", Object.class));
		assertFalse(ClassUtils.hasMethod(Collection.class, "remove"));
		assertFalse(ClassUtils.hasMethod(Collection.class, "someOtherMethod"));
	}

	@Test
	public void testGetMethodIfAvailable() {
		Method method = ClassUtils.getMethodIfAvailable(Collection.class, "size");
		assertNotNull(method);
		assertEquals("size", method.getName());

		method = ClassUtils.getMethodIfAvailable(Collection.class, "remove", Object.class);
		assertNotNull(method);
		assertEquals("remove", method.getName());

		assertNull(ClassUtils.getMethodIfAvailable(Collection.class, "remove"));
		assertNull(ClassUtils.getMethodIfAvailable(Collection.class, "someOtherMethod"));
	}

	@Test
	public void testGetMethodCountForName() {
		assertEquals("Verifying number of overloaded 'print' methods for OverloadedMethodsClass.", 2,
				ClassUtils.getMethodCountForName(OverloadedMethodsClass.class, "print"));
		assertEquals("Verifying number of overloaded 'print' methods for SubOverloadedMethodsClass.", 4,
				ClassUtils.getMethodCountForName(SubOverloadedMethodsClass.class, "print"));
	}

	@Test
	public void testCountOverloadedMethods() {
		assertFalse(ClassUtils.hasAtLeastOneMethodWithName(TestObject.class, "foobar"));
		// no args
		assertTrue(ClassUtils.hasAtLeastOneMethodWithName(TestObject.class, "hashCode"));
		// matches although it takes an arg
		assertTrue(ClassUtils.hasAtLeastOneMethodWithName(TestObject.class, "setAge"));
	}

	@Test
	public void testNoArgsStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "staticMethod");
		method.invoke(null, (Object[]) null);
		assertTrue("no argument method was not invoked.",
				InnerClass.noArgCalled);
	}

	@Test
	public void testArgsStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "argStaticMethod", String.class);
		method.invoke(null, "test");
		assertTrue("argument method was not invoked.", InnerClass.argCalled);
	}

	@Test
	public void testOverloadedStaticMethod() throws IllegalAccessException, InvocationTargetException {
		Method method = ClassUtils.getStaticMethod(InnerClass.class, "staticMethod", String.class);
		method.invoke(null, "test");
		assertTrue("argument method was not invoked.", InnerClass.overloadedCalled);
	}

	@Test
	public void testIsAssignable() {
		assertTrue(ClassUtils.isAssignable(Object.class, Object.class));
		assertTrue(ClassUtils.isAssignable(String.class, String.class));
		assertTrue(ClassUtils.isAssignable(Object.class, String.class));
		assertTrue(ClassUtils.isAssignable(Object.class, Integer.class));
		assertTrue(ClassUtils.isAssignable(Number.class, Integer.class));
		assertTrue(ClassUtils.isAssignable(Number.class, int.class));
		assertTrue(ClassUtils.isAssignable(Integer.class, int.class));
		assertTrue(ClassUtils.isAssignable(int.class, Integer.class));
		assertFalse(ClassUtils.isAssignable(String.class, Object.class));
		assertFalse(ClassUtils.isAssignable(Integer.class, Number.class));
		assertFalse(ClassUtils.isAssignable(Integer.class, double.class));
		assertFalse(ClassUtils.isAssignable(double.class, Integer.class));
	}

	@Test
	public void testClassPackageAsResourcePath() {
		String result = ClassUtils.classPackageAsResourcePath(Proxy.class);
		assertEquals("java/lang/reflect", result);
	}

	@Test
	public void testAddResourcePathToPackagePath() {
		String result = "java/lang/reflect/xyzabc.xml";
		assertEquals(result, ClassUtils.addResourcePathToPackagePath(Proxy.class, "xyzabc.xml"));
		assertEquals(result, ClassUtils.addResourcePathToPackagePath(Proxy.class, "/xyzabc.xml"));

		assertEquals("java/lang/reflect/a/b/c/d.xml",
				ClassUtils.addResourcePathToPackagePath(Proxy.class, "a/b/c/d.xml"));
	}

	@Test
	public void testGetAllInterfaces() {
		DerivedTestObject testBean = new DerivedTestObject();
		List<Class<?>> ifcs = Arrays.asList(ClassUtils.getAllInterfaces(testBean));
		assertEquals("Correct number of interfaces", 4, ifcs.size());
		assertTrue("Contains Serializable", ifcs.contains(Serializable.class));
		assertTrue("Contains ITestBean", ifcs.contains(ITestObject.class));
		assertTrue("Contains IOther", ifcs.contains(ITestInterface.class));
	}

	@Test
	public void testClassNamesToString() {
		List<Class<?>> ifcs = new LinkedList<>();
		ifcs.add(Serializable.class);
		ifcs.add(Runnable.class);
		assertEquals("[interface java.io.Serializable, interface java.lang.Runnable]", ifcs.toString());
		assertEquals("[java.io.Serializable, java.lang.Runnable]", ClassUtils.classNamesToString(ifcs));

		List<Class<?>> classes = new LinkedList<>();
		classes.add(LinkedList.class);
		classes.add(Integer.class);
		assertEquals("[class java.util.LinkedList, class java.lang.Integer]", classes.toString());
		assertEquals("[java.util.LinkedList, java.lang.Integer]", ClassUtils.classNamesToString(classes));

		assertEquals("[interface java.util.List]", Collections.singletonList(List.class).toString());
		assertEquals("[java.util.List]", ClassUtils.classNamesToString(List.class));

		assertEquals("[]", Collections.EMPTY_LIST.toString());
		assertEquals("[]", ClassUtils.classNamesToString(Collections.emptyList()));
	}

	@Test
	public void testDetermineCommonAncestor() {
		assertEquals(Number.class, ClassUtils.determineCommonAncestor(Integer.class, Number.class));
		assertEquals(Number.class, ClassUtils.determineCommonAncestor(Number.class, Integer.class));
		assertEquals(Number.class, ClassUtils.determineCommonAncestor(Number.class, null));
		assertEquals(Integer.class, ClassUtils.determineCommonAncestor(null, Integer.class));
		assertEquals(Integer.class, ClassUtils.determineCommonAncestor(Integer.class, Integer.class));

		assertEquals(Number.class, ClassUtils.determineCommonAncestor(Integer.class, Float.class));
		assertEquals(Number.class, ClassUtils.determineCommonAncestor(Float.class, Integer.class));
		assertNull(ClassUtils.determineCommonAncestor(Integer.class, String.class));
		assertNull(ClassUtils.determineCommonAncestor(String.class, Integer.class));

		assertEquals(Collection.class, ClassUtils.determineCommonAncestor(List.class, Collection.class));
		assertEquals(Collection.class, ClassUtils.determineCommonAncestor(Collection.class, List.class));
		assertEquals(Collection.class, ClassUtils.determineCommonAncestor(Collection.class, null));
		assertEquals(List.class, ClassUtils.determineCommonAncestor(null, List.class));
		assertEquals(List.class, ClassUtils.determineCommonAncestor(List.class, List.class));

		assertNull(ClassUtils.determineCommonAncestor(List.class, Set.class));
		assertNull(ClassUtils.determineCommonAncestor(Set.class, List.class));
		assertNull(ClassUtils.determineCommonAncestor(List.class, Runnable.class));
		assertNull(ClassUtils.determineCommonAncestor(Runnable.class, List.class));

		assertEquals(List.class, ClassUtils.determineCommonAncestor(List.class, ArrayList.class));
		assertEquals(List.class, ClassUtils.determineCommonAncestor(ArrayList.class, List.class));
		assertNull(ClassUtils.determineCommonAncestor(List.class, String.class));
		assertNull(ClassUtils.determineCommonAncestor(String.class, List.class));
	}

	@Test
	public void isPrimitiveWrapper() {
		assertTrue(ClassUtils.isPrimitiveWrapper(Boolean.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Character.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Byte.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Short.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Integer.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Long.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Float.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Double.class));
		assertTrue(ClassUtils.isPrimitiveWrapper(Void.class));
	}

	@Test
	public void isPrimitiveOrWrapper() {
		assertTrue(ClassUtils.isPrimitiveOrWrapper(boolean.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(char.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(byte.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(short.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(int.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(long.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(float.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(double.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(void.class));

		assertTrue(ClassUtils.isPrimitiveOrWrapper(Boolean.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Character.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Byte.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Short.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Integer.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Long.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Float.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Double.class));
		assertTrue(ClassUtils.isPrimitiveOrWrapper(Void.class));
	}


	public static class InnerClass {

		static boolean noArgCalled;
		static boolean argCalled;
		static boolean overloadedCalled;

		public static void staticMethod() {
			noArgCalled = true;
		}

		public static void staticMethod(String anArg) {
			overloadedCalled = true;
		}

		public static void argStaticMethod(String anArg) {
			argCalled = true;
		}
	}

	@SuppressWarnings("unused")
	private static class OverloadedMethodsClass {

		public void print(String messages) {
			/* no-op */
		}

		public void print(String[] messages) {
			/* no-op */
		}
	}

	@SuppressWarnings("unused")
	private static class SubOverloadedMethodsClass extends OverloadedMethodsClass {

		public void print(String header, String[] messages) {
			/* no-op */
		}

		void print(String header, String[] messages, String footer) {
			/* no-op */
		}
	}

}
