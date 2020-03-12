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

package org.springframework.test.util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.test.util.subpackage.Component;
import org.springframework.test.util.subpackage.LegacyEntity;
import org.springframework.test.util.subpackage.Person;
import org.springframework.test.util.subpackage.PersonEntity;
import org.springframework.test.util.subpackage.StaticFields;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

/**
 * Unit tests for {@link ReflectionTestUtils}.
 *
 * @author Sam Brannen
 * @author Juergen Hoeller
 */
public class ReflectionTestUtilsTests {

	private static final Float PI = Float.valueOf((float) 22 / 7);

	private final Person person = new PersonEntity();

	private final Component component = new Component();

	private final LegacyEntity entity = new LegacyEntity();

	@Rule
	public final ExpectedException exception = ExpectedException.none();


	@Before
	public void resetStaticFields() {
		StaticFields.reset();
	}

	@Test
	public void setFieldWithNullTargetObject() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Either targetObject or targetClass"));
		setField((Object) null, "id", Long.valueOf(99));
	}

	@Test
	public void getFieldWithNullTargetObject() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Either targetObject or targetClass"));
		getField((Object) null, "id");
	}

	@Test
	public void setFieldWithNullTargetClass() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Either targetObject or targetClass"));
		setField((Class<?>) null, "id", Long.valueOf(99));
	}

	@Test
	public void getFieldWithNullTargetClass() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Either targetObject or targetClass"));
		getField((Class<?>) null, "id");
	}

	@Test
	public void setFieldWithNullNameAndNullType() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Either name or type"));
		setField(person, null, Long.valueOf(99), null);
	}

	@Test
	public void setFieldWithBogusName() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Could not find field 'bogus'"));
		setField(person, "bogus", Long.valueOf(99), long.class);
	}

	@Test
	public void setFieldWithWrongType() throws Exception {
		exception.expect(IllegalArgumentException.class);
		exception.expectMessage(startsWith("Could not find field"));
		setField(person, "id", Long.valueOf(99), String.class);
	}

	@Test
	public void setFieldAndGetFieldForStandardUseCases() throws Exception {
		assertSetFieldAndGetFieldBehavior(this.person);
	}

	@Test
	public void setFieldAndGetFieldViaJdkDynamicProxy() throws Exception {
		ProxyFactory pf = new ProxyFactory(this.person);
		pf.addInterface(Person.class);
		Person proxy = (Person) pf.getProxy();
		assertTrue("Proxy is a JDK dynamic proxy", AopUtils.isJdkDynamicProxy(proxy));
		assertSetFieldAndGetFieldBehaviorForProxy(proxy, this.person);
	}

	@Test
	public void setFieldAndGetFieldViaCglibProxy() throws Exception {
		ProxyFactory pf = new ProxyFactory(this.person);
		pf.setProxyTargetClass(true);
		Person proxy = (Person) pf.getProxy();
		assertTrue("Proxy is a CGLIB proxy", AopUtils.isCglibProxy(proxy));
		assertSetFieldAndGetFieldBehaviorForProxy(proxy, this.person);
	}

	private static void assertSetFieldAndGetFieldBehavior(Person person) {
		// Set reflectively
		setField(person, "id", Long.valueOf(99), long.class);
		setField(person, "name", "Tom");
		setField(person, "age", Integer.valueOf(42));
		setField(person, "eyeColor", "blue", String.class);
		setField(person, "likesPets", Boolean.TRUE);
		setField(person, "favoriteNumber", PI, Number.class);

		// Get reflectively
		assertEquals(Long.valueOf(99), getField(person, "id"));
		assertEquals("Tom", getField(person, "name"));
		assertEquals(Integer.valueOf(42), getField(person, "age"));
		assertEquals("blue", getField(person, "eyeColor"));
		assertEquals(Boolean.TRUE, getField(person, "likesPets"));
		assertEquals(PI, getField(person, "favoriteNumber"));

		// Get directly
		assertEquals("ID (private field in a superclass)", 99, person.getId());
		assertEquals("name (protected field)", "Tom", person.getName());
		assertEquals("age (private field)", 42, person.getAge());
		assertEquals("eye color (package private field)", "blue", person.getEyeColor());
		assertEquals("'likes pets' flag (package private boolean field)", true, person.likesPets());
		assertEquals("'favorite number' (package field)", PI, person.getFavoriteNumber());
	}

	private static void assertSetFieldAndGetFieldBehaviorForProxy(Person proxy, Person target) {
		assertSetFieldAndGetFieldBehavior(proxy);

		// Get directly from Target
		assertEquals("ID (private field in a superclass)", 99, target.getId());
		assertEquals("name (protected field)", "Tom", target.getName());
		assertEquals("age (private field)", 42, target.getAge());
		assertEquals("eye color (package private field)", "blue", target.getEyeColor());
		assertEquals("'likes pets' flag (package private boolean field)", true, target.likesPets());
		assertEquals("'favorite number' (package field)", PI, target.getFavoriteNumber());
	}

	@Test
	public void setFieldWithNullValuesForNonPrimitives() throws Exception {
		// Fields must be non-null to start with
		setField(person, "name", "Tom");
		setField(person, "eyeColor", "blue", String.class);
		setField(person, "favoriteNumber", PI, Number.class);
		assertNotNull(person.getName());
		assertNotNull(person.getEyeColor());
		assertNotNull(person.getFavoriteNumber());

		// Set to null
		setField(person, "name", null, String.class);
		setField(person, "eyeColor", null, String.class);
		setField(person, "favoriteNumber", null, Number.class);

		assertNull("name (protected field)", person.getName());
		assertNull("eye color (package private field)", person.getEyeColor());
		assertNull("'favorite number' (package field)", person.getFavoriteNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setFieldWithNullValueForPrimitiveLong() throws Exception {
		setField(person, "id", null, long.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setFieldWithNullValueForPrimitiveInt() throws Exception {
		setField(person, "age", null, int.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setFieldWithNullValueForPrimitiveBoolean() throws Exception {
		setField(person, "likesPets", null, boolean.class);
	}

	@Test
	public void setStaticFieldViaClass() throws Exception {
		setField(StaticFields.class, "publicField", "xxx");
		setField(StaticFields.class, "privateField", "yyy");

		assertEquals("public static field", "xxx", StaticFields.publicField);
		assertEquals("private static field", "yyy", StaticFields.getPrivateField());
	}

	@Test
	public void setStaticFieldViaClassWithExplicitType() throws Exception {
		setField(StaticFields.class, "publicField", "xxx", String.class);
		setField(StaticFields.class, "privateField", "yyy", String.class);

		assertEquals("public static field", "xxx", StaticFields.publicField);
		assertEquals("private static field", "yyy", StaticFields.getPrivateField());
	}

	@Test
	public void setStaticFieldViaInstance() throws Exception {
		StaticFields staticFields = new StaticFields();
		setField(staticFields, null, "publicField", "xxx", null);
		setField(staticFields, null, "privateField", "yyy", null);

		assertEquals("public static field", "xxx", StaticFields.publicField);
		assertEquals("private static field", "yyy", StaticFields.getPrivateField());
	}

	@Test
	public void getStaticFieldViaClass() throws Exception {
		assertEquals("public static field", "public", getField(StaticFields.class, "publicField"));
		assertEquals("private static field", "private", getField(StaticFields.class, "privateField"));
	}

	@Test
	public void getStaticFieldViaInstance() throws Exception {
		StaticFields staticFields = new StaticFields();
		assertEquals("public static field", "public", getField(staticFields, "publicField"));
		assertEquals("private static field", "private", getField(staticFields, "privateField"));
	}

	@Test
	public void invokeSetterMethodAndInvokeGetterMethodWithExplicitMethodNames() throws Exception {
		invokeSetterMethod(person, "setId", Long.valueOf(1), long.class);
		invokeSetterMethod(person, "setName", "Jerry", String.class);
		invokeSetterMethod(person, "setAge", Integer.valueOf(33), int.class);
		invokeSetterMethod(person, "setEyeColor", "green", String.class);
		invokeSetterMethod(person, "setLikesPets", Boolean.FALSE, boolean.class);
		invokeSetterMethod(person, "setFavoriteNumber", Integer.valueOf(42), Number.class);

		assertEquals("ID (protected method in a superclass)", 1, person.getId());
		assertEquals("name (private method)", "Jerry", person.getName());
		assertEquals("age (protected method)", 33, person.getAge());
		assertEquals("eye color (package private method)", "green", person.getEyeColor());
		assertEquals("'likes pets' flag (protected method for a boolean)", false, person.likesPets());
		assertEquals("'favorite number' (protected method for a Number)", Integer.valueOf(42), person.getFavoriteNumber());

		assertEquals(Long.valueOf(1), invokeGetterMethod(person, "getId"));
		assertEquals("Jerry", invokeGetterMethod(person, "getName"));
		assertEquals(Integer.valueOf(33), invokeGetterMethod(person, "getAge"));
		assertEquals("green", invokeGetterMethod(person, "getEyeColor"));
		assertEquals(Boolean.FALSE, invokeGetterMethod(person, "likesPets"));
		assertEquals(Integer.valueOf(42), invokeGetterMethod(person, "getFavoriteNumber"));
	}

	@Test
	public void invokeSetterMethodAndInvokeGetterMethodWithJavaBeanPropertyNames() throws Exception {
		invokeSetterMethod(person, "id", Long.valueOf(99), long.class);
		invokeSetterMethod(person, "name", "Tom");
		invokeSetterMethod(person, "age", Integer.valueOf(42));
		invokeSetterMethod(person, "eyeColor", "blue", String.class);
		invokeSetterMethod(person, "likesPets", Boolean.TRUE);
		invokeSetterMethod(person, "favoriteNumber", PI, Number.class);

		assertEquals("ID (protected method in a superclass)", 99, person.getId());
		assertEquals("name (private method)", "Tom", person.getName());
		assertEquals("age (protected method)", 42, person.getAge());
		assertEquals("eye color (package private method)", "blue", person.getEyeColor());
		assertEquals("'likes pets' flag (protected method for a boolean)", true, person.likesPets());
		assertEquals("'favorite number' (protected method for a Number)", PI, person.getFavoriteNumber());

		assertEquals(Long.valueOf(99), invokeGetterMethod(person, "id"));
		assertEquals("Tom", invokeGetterMethod(person, "name"));
		assertEquals(Integer.valueOf(42), invokeGetterMethod(person, "age"));
		assertEquals("blue", invokeGetterMethod(person, "eyeColor"));
		assertEquals(Boolean.TRUE, invokeGetterMethod(person, "likesPets"));
		assertEquals(PI, invokeGetterMethod(person, "favoriteNumber"));
	}

	@Test
	public void invokeSetterMethodWithNullValuesForNonPrimitives() throws Exception {
		invokeSetterMethod(person, "name", null, String.class);
		invokeSetterMethod(person, "eyeColor", null, String.class);
		invokeSetterMethod(person, "favoriteNumber", null, Number.class);

		assertNull("name (private method)", person.getName());
		assertNull("eye color (package private method)", person.getEyeColor());
		assertNull("'favorite number' (protected method for a Number)", person.getFavoriteNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public void invokeSetterMethodWithNullValueForPrimitiveLong() throws Exception {
		invokeSetterMethod(person, "id", null, long.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invokeSetterMethodWithNullValueForPrimitiveInt() throws Exception {
		invokeSetterMethod(person, "age", null, int.class);
	}

	@Test(expected = IllegalArgumentException.class)
	public void invokeSetterMethodWithNullValueForPrimitiveBoolean() throws Exception {
		invokeSetterMethod(person, "likesPets", null, boolean.class);
	}

	@Test
	public void invokeMethodWithAutoboxingAndUnboxing() {
		// IntelliJ IDEA 11 won't accept int assignment here
		Integer difference = invokeMethod(component, "subtract", 5, 2);
		assertEquals("subtract(5, 2)", 3, difference.intValue());
	}

	@Test
	@Ignore("[SPR-8644] findMethod() does not currently support var-args")
	public void invokeMethodWithPrimitiveVarArgs() {
		// IntelliJ IDEA 11 won't accept int assignment here
		Integer sum = invokeMethod(component, "add", 1, 2, 3, 4);
		assertEquals("add(1,2,3,4)", 10, sum.intValue());
	}

	@Test
	public void invokeMethodWithPrimitiveVarArgsAsSingleArgument() {
		// IntelliJ IDEA 11 won't accept int assignment here
		Integer sum = invokeMethod(component, "add", new int[] { 1, 2, 3, 4 });
		assertEquals("add(1,2,3,4)", 10, sum.intValue());
	}

	@Test
	public void invokeMethodSimulatingLifecycleEvents() {
		assertNull("number", component.getNumber());
		assertNull("text", component.getText());

		// Simulate autowiring a configuration method
		invokeMethod(component, "configure", Integer.valueOf(42), "enigma");
		assertEquals("number should have been configured", Integer.valueOf(42), component.getNumber());
		assertEquals("text should have been configured", "enigma", component.getText());

		// Simulate @PostConstruct life-cycle event
		invokeMethod(component, "init");
		// assertions in init() should succeed

		// Simulate @PreDestroy life-cycle event
		invokeMethod(component, "destroy");
		assertNull("number", component.getNumber());
		assertNull("text", component.getText());
	}

	@Test
	public void invokeInitMethodBeforeAutowiring() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage(equalTo("number must not be null"));
		invokeMethod(component, "init");
	}

	@Test
	public void invokeMethodWithIncompatibleArgumentTypes() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage(startsWith("Method not found"));
		invokeMethod(component, "subtract", "foo", 2.0);
	}

	@Test
	public void invokeMethodWithTooFewArguments() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage(startsWith("Method not found"));
		invokeMethod(component, "configure", Integer.valueOf(42));
	}

	@Test
	public void invokeMethodWithTooManyArguments() {
		exception.expect(IllegalStateException.class);
		exception.expectMessage(startsWith("Method not found"));
		invokeMethod(component, "configure", Integer.valueOf(42), "enigma", "baz", "quux");
	}

	@Test // SPR-14363
	public void getFieldOnLegacyEntityWithSideEffectsInToString() {
		Object collaborator = getField(entity, "collaborator");
		assertNotNull(collaborator);
	}

	@Test // SPR-9571 and SPR-14363
	public void setFieldOnLegacyEntityWithSideEffectsInToString() {
		String testCollaborator = "test collaborator";
		setField(entity, "collaborator", testCollaborator, Object.class);
		assertTrue(entity.toString().contains(testCollaborator));
	}

	@Test // SPR-14363
	public void invokeMethodOnLegacyEntityWithSideEffectsInToString() {
		invokeMethod(entity, "configure", Integer.valueOf(42), "enigma");
		assertEquals("number should have been configured", Integer.valueOf(42), entity.getNumber());
		assertEquals("text should have been configured", "enigma", entity.getText());
	}

	@Test // SPR-14363
	public void invokeGetterMethodOnLegacyEntityWithSideEffectsInToString() {
		Object collaborator = invokeGetterMethod(entity, "collaborator");
		assertNotNull(collaborator);
	}

	@Test // SPR-14363
	public void invokeSetterMethodOnLegacyEntityWithSideEffectsInToString() {
		String testCollaborator = "test collaborator";
		invokeSetterMethod(entity, "collaborator", testCollaborator);
		assertTrue(entity.toString().contains(testCollaborator));
	}

}
