/*
 * Copyright 2002-2010 the original author or authors.
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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link TypeUtils}.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public class TypeUtilsTests {

	public static Object object;

	public static String string;

	public static Integer number;

	public static List<Object> objects;

	public static List<String> strings;

	public static List<? extends Object> openObjects;

	public static List<? extends Number> openNumbers;

	public static List<? super Object> storableObjectList;

	public static List<Number>[] array;

	public static List<? extends Number>[] openArray;


	@Test
	public void withClasses() {
		assertTrue(TypeUtils.isAssignable(Object.class, Object.class));
		assertTrue(TypeUtils.isAssignable(Object.class, String.class));
		assertFalse(TypeUtils.isAssignable(String.class, Object.class));
		assertTrue(TypeUtils.isAssignable(List.class, List.class));
		assertTrue(TypeUtils.isAssignable(List.class, LinkedList.class));
		assertFalse(TypeUtils.isAssignable(List.class, Collection.class));
		assertFalse(TypeUtils.isAssignable(List.class, HashSet.class));
	}

	@Test
	public void withParameterizedTypes() throws Exception {
		Type objectsType = getClass().getField("objects").getGenericType();
		Type openObjectsType = getClass().getField("openObjects").getGenericType();
		Type stringsType = getClass().getField("strings").getGenericType();
		assertTrue(TypeUtils.isAssignable(Object.class, objectsType));
		assertTrue(TypeUtils.isAssignable(Object.class, openObjectsType));
		assertTrue(TypeUtils.isAssignable(Object.class, stringsType));
		assertTrue(TypeUtils.isAssignable(List.class, objectsType));
		assertTrue(TypeUtils.isAssignable(List.class, openObjectsType));
		assertTrue(TypeUtils.isAssignable(List.class, stringsType));
		assertTrue(TypeUtils.isAssignable(objectsType, List.class));
		assertTrue(TypeUtils.isAssignable(openObjectsType, List.class));
		assertTrue(TypeUtils.isAssignable(stringsType, List.class));
		assertTrue(TypeUtils.isAssignable(objectsType, objectsType));
		assertTrue(TypeUtils.isAssignable(openObjectsType, openObjectsType));
		assertTrue(TypeUtils.isAssignable(stringsType, stringsType));
		assertTrue(TypeUtils.isAssignable(openObjectsType, objectsType));
		assertTrue(TypeUtils.isAssignable(openObjectsType, stringsType));
		assertFalse(TypeUtils.isAssignable(stringsType, objectsType));
		assertFalse(TypeUtils.isAssignable(objectsType, stringsType));
	}

	@Test
	public void withWildcardTypes() throws Exception {
		ParameterizedType openObjectsType = (ParameterizedType) getClass().getField("openObjects").getGenericType();
		ParameterizedType openNumbersType = (ParameterizedType) getClass().getField("openNumbers").getGenericType();
		Type storableObjectListType = getClass().getField("storableObjectList").getGenericType();

		Type objectType = getClass().getField("object").getGenericType();
		Type numberType = getClass().getField("number").getGenericType();
		Type stringType = getClass().getField("string").getGenericType();

		Type openWildcard = openObjectsType.getActualTypeArguments()[0]; // '?'
		Type openNumbersWildcard = openNumbersType.getActualTypeArguments()[0]; // '? extends number'

		assertTrue(TypeUtils.isAssignable(openWildcard, objectType));
		assertTrue(TypeUtils.isAssignable(openNumbersWildcard, numberType));
		assertFalse(TypeUtils.isAssignable(openNumbersWildcard, stringType));
		assertFalse(TypeUtils.isAssignable(storableObjectListType, openObjectsType));
	}

	@Test
	public void withGenericArrayTypes() throws Exception {
		Type arrayType = getClass().getField("array").getGenericType();
		Type openArrayType = getClass().getField("openArray").getGenericType();
		assertTrue(TypeUtils.isAssignable(Object.class, arrayType));
		assertTrue(TypeUtils.isAssignable(Object.class, openArrayType));
		assertTrue(TypeUtils.isAssignable(List[].class, arrayType));
		assertTrue(TypeUtils.isAssignable(List[].class, openArrayType));
		assertTrue(TypeUtils.isAssignable(arrayType, List[].class));
		assertTrue(TypeUtils.isAssignable(openArrayType, List[].class));
		assertTrue(TypeUtils.isAssignable(arrayType, arrayType));
		assertTrue(TypeUtils.isAssignable(openArrayType, openArrayType));
		assertTrue(TypeUtils.isAssignable(openArrayType, arrayType));
	}

}
