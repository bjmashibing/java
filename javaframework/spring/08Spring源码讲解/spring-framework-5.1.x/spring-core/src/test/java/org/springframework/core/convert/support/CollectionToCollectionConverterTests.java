/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.core.convert.support;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import static org.junit.Assert.*;

/**
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Stephane Nicoll
 */
public class CollectionToCollectionConverterTests {

	private GenericConversionService conversionService = new GenericConversionService();


	@Before
	public void setUp() {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
	}


	@Test
	public void scalarList() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("9");
		list.add("37");
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("scalarListTarget"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		try {
			conversionService.convert(list, sourceType, targetType);
		}
		catch (ConversionFailedException ex) {
			assertTrue(ex.getCause() instanceof ConverterNotFoundException);
		}
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		assertTrue(conversionService.canConvert(sourceType, targetType));
		@SuppressWarnings("unchecked")
		List<Integer> result = (List<Integer>) conversionService.convert(list, sourceType, targetType);
		assertFalse(list.equals(result));
		assertEquals(9, result.get(0).intValue());
		assertEquals(37, result.get(1).intValue());
	}

	@Test
	public void emptyListToList() throws Exception {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		List<String> list = new ArrayList<>();
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("emptyListTarget"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		assertEquals(list, conversionService.convert(list, sourceType, targetType));
	}

	@Test
	public void emptyListToListDifferentTargetType() throws Exception {
		conversionService.addConverter(new CollectionToCollectionConverter(conversionService));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		List<String> list = new ArrayList<>();
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("emptyListDifferentTarget"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		@SuppressWarnings("unchecked")
		LinkedList<Integer> result = (LinkedList<Integer>) conversionService.convert(list, sourceType, targetType);
		assertEquals(LinkedList.class, result.getClass());
		assertTrue(result.isEmpty());
	}

	@Test
	public void collectionToObjectInteraction() throws Exception {
		List<List<String>> list = new ArrayList<>();
		list.add(Arrays.asList("9", "12"));
		list.add(Arrays.asList("37", "23"));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		assertTrue(conversionService.canConvert(List.class, List.class));
		assertSame(list, conversionService.convert(list, List.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void arrayCollectionToObjectInteraction() throws Exception {
		List<String>[] array = new List[2];
		array[0] = Arrays.asList("9", "12");
		array[1] = Arrays.asList("37", "23");
		conversionService.addConverter(new ArrayToCollectionConverter(conversionService));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		assertTrue(conversionService.canConvert(String[].class, List.class));
		assertEquals(Arrays.asList(array), conversionService.convert(array, List.class));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void objectToCollection() throws Exception {
		List<List<String>> list = new ArrayList<>();
		list.add(Arrays.asList("9", "12"));
		list.add(Arrays.asList("37", "23"));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverter(new ObjectToCollectionConverter(conversionService));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("objectToCollection"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		List<List<List<Integer>>> result = (List<List<List<Integer>>>) conversionService.convert(list, sourceType, targetType);
		assertEquals((Integer) 9, result.get(0).get(0).get(0));
		assertEquals((Integer) 12, result.get(0).get(1).get(0));
		assertEquals((Integer) 37, result.get(1).get(0).get(0));
		assertEquals((Integer) 23, result.get(1).get(1).get(0));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void stringToCollection() throws Exception {
		List<List<String>> list = new ArrayList<>();
		list.add(Arrays.asList("9,12"));
		list.add(Arrays.asList("37,23"));
		conversionService.addConverterFactory(new StringToNumberConverterFactory());
		conversionService.addConverter(new StringToCollectionConverter(conversionService));
		conversionService.addConverter(new ObjectToCollectionConverter(conversionService));
		conversionService.addConverter(new CollectionToObjectConverter(conversionService));
		TypeDescriptor sourceType = TypeDescriptor.forObject(list);
		TypeDescriptor targetType = new TypeDescriptor(getClass().getField("objectToCollection"));
		assertTrue(conversionService.canConvert(sourceType, targetType));
		List<List<List<Integer>>> result = (List<List<List<Integer>>>) conversionService.convert(list, sourceType, targetType);
		assertEquals((Integer) 9, result.get(0).get(0).get(0));
		assertEquals((Integer) 12, result.get(0).get(0).get(1));
		assertEquals((Integer) 37, result.get(1).get(0).get(0));
		assertEquals((Integer) 23, result.get(1).get(0).get(1));
	}

	@Test
	public void convertEmptyVector_shouldReturnEmptyArrayList() {
		Vector<String> vector = new Vector<>();
		vector.add("Element");
		testCollectionConversionToArrayList(vector);
	}

	@Test
	public void convertNonEmptyVector_shouldReturnNonEmptyArrayList() {
		Vector<String> vector = new Vector<>();
		vector.add("Element");
		testCollectionConversionToArrayList(vector);
	}

	@Test
	public void testCollectionsEmptyList() throws Exception {
		CollectionToCollectionConverter converter = new CollectionToCollectionConverter(new GenericConversionService());
		TypeDescriptor type = new TypeDescriptor(getClass().getField("list"));
		converter.convert(list, type, TypeDescriptor.valueOf(Class.forName("java.util.Collections$EmptyList")));
	}

	@SuppressWarnings("rawtypes")
	private void testCollectionConversionToArrayList(Collection<String> aSource) {
		Object myConverted = (new CollectionToCollectionConverter(new GenericConversionService())).convert(
				aSource, TypeDescriptor.forObject(aSource), TypeDescriptor.forObject(new ArrayList()));
		assertTrue(myConverted instanceof ArrayList<?>);
		assertEquals(aSource.size(), ((ArrayList<?>) myConverted).size());
	}

	@Test
	public void listToCollectionNoCopyRequired() throws NoSuchFieldException {
		List<?> input = new ArrayList<>(Arrays.asList("foo", "bar"));
		assertSame(input, conversionService.convert(input, TypeDescriptor.forObject(input),
				new TypeDescriptor(getClass().getField("wildcardCollection"))));
	}

	@Test
	public void differentImpls() throws Exception {
		List<Resource> resources = new ArrayList<>();
		resources.add(new ClassPathResource("test"));
		resources.add(new FileSystemResource("test"));
		resources.add(new TestResource());
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertSame(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test
	public void mixedInNulls() throws Exception {
		List<Resource> resources = new ArrayList<>();
		resources.add(new ClassPathResource("test"));
		resources.add(null);
		resources.add(new FileSystemResource("test"));
		resources.add(new TestResource());
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertSame(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test
	public void allNulls() throws Exception {
		List<Resource> resources = new ArrayList<>();
		resources.add(null);
		resources.add(null);
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertSame(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test(expected = ConverterNotFoundException.class)
	public void elementTypesNotConvertible() throws Exception {
		List<String> resources = new ArrayList<>();
		resources.add(null);
		resources.add(null);
		TypeDescriptor sourceType = new TypeDescriptor(getClass().getField("strings"));
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test(expected = ConversionFailedException.class)
	public void nothingInCommon() throws Exception {
		List<Object> resources = new ArrayList<>();
		resources.add(new ClassPathResource("test"));
		resources.add(3);
		TypeDescriptor sourceType = TypeDescriptor.forObject(resources);
		assertEquals(resources, conversionService.convert(resources, sourceType, new TypeDescriptor(getClass().getField("resources"))));
	}

	@Test
	public void testStringToEnumSet() throws Exception {
		conversionService.addConverterFactory(new StringToEnumConverterFactory());
		List<String> list = new ArrayList<>();
		list.add("A");
		list.add("C");
		assertEquals(EnumSet.of(MyEnum.A, MyEnum.C),
				conversionService.convert(list, TypeDescriptor.forObject(list), new TypeDescriptor(getClass().getField("enumSet"))));
	}


	public ArrayList<Integer> scalarListTarget;

	public List<Integer> emptyListTarget;

	public LinkedList<Integer> emptyListDifferentTarget;

	public List<List<List<Integer>>> objectToCollection;

	public List<String> strings;

	public List<?> list = Collections.emptyList();

	public Collection<?> wildcardCollection = Collections.emptyList();

	public List<Resource> resources;

	public EnumSet<MyEnum> enumSet;


	public static abstract class BaseResource implements Resource {

		@Override
		public InputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public boolean exists() {
			return false;
		}

		@Override
		public boolean isReadable() {
			return false;
		}

		@Override
		public boolean isOpen() {
			return false;
		}

		@Override
		public boolean isFile() {
			return false;
		}

		@Override
		public URL getURL() throws IOException {
			return null;
		}

		@Override
		public URI getURI() throws IOException {
			return null;
		}

		@Override
		public File getFile() throws IOException {
			return null;
		}

		@Override
		public long contentLength() throws IOException {
			return 0;
		}

		@Override
		public long lastModified() throws IOException {
			return 0;
		}

		@Override
		public Resource createRelative(String relativePath) throws IOException {
			return null;
		}

		@Override
		public String getFilename() {
			return null;
		}

		@Override
		public String getDescription() {
			return null;
		}
	}


	public static class TestResource extends BaseResource {
	}


	public enum MyEnum {A, B, C}

}
