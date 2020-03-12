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

package org.springframework.expression.spel.support;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ParseException;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;
import org.springframework.expression.spel.AbstractExpressionTests;
import org.springframework.expression.spel.SpelUtilities;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.support.ReflectionHelper.ArgumentsMatchKind;

import static org.junit.Assert.*;

/**
 * Tests for reflection helper code.
 *
 * @author Andy Clement
 */
public class ReflectionHelperTests extends AbstractExpressionTests {

	@Test
	public void testUtilities() throws ParseException {
		SpelExpression expr = (SpelExpression)parser.parseExpression("3+4+5+6+7-2");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		SpelUtilities.printAbstractSyntaxTree(ps, expr);
		ps.flush();
		String s = baos.toString();
//		===> Expression '3+4+5+6+7-2' - AST start
//		OperatorMinus  value:(((((3 + 4) + 5) + 6) + 7) - 2)  #children:2
//		  OperatorPlus  value:((((3 + 4) + 5) + 6) + 7)  #children:2
//		    OperatorPlus  value:(((3 + 4) + 5) + 6)  #children:2
//		      OperatorPlus  value:((3 + 4) + 5)  #children:2
//		        OperatorPlus  value:(3 + 4)  #children:2
//		          CompoundExpression  value:3
//		            IntLiteral  value:3
//		          CompoundExpression  value:4
//		            IntLiteral  value:4
//		        CompoundExpression  value:5
//		          IntLiteral  value:5
//		      CompoundExpression  value:6
//		        IntLiteral  value:6
//		    CompoundExpression  value:7
//		      IntLiteral  value:7
//		  CompoundExpression  value:2
//		    IntLiteral  value:2
//		===> Expression '3+4+5+6+7-2' - AST end
		assertTrue(s.contains("===> Expression '3+4+5+6+7-2' - AST start"));
		assertTrue(s.contains(" OpPlus  value:((((3 + 4) + 5) + 6) + 7)  #children:2"));
	}

	@Test
	public void testTypedValue() {
		TypedValue tv1 = new TypedValue("hello");
		TypedValue tv2 = new TypedValue("hello");
		TypedValue tv3 = new TypedValue("bye");
		assertEquals(String.class, tv1.getTypeDescriptor().getType());
		assertEquals("TypedValue: 'hello' of [java.lang.String]", tv1.toString());
		assertEquals(tv1, tv2);
		assertEquals(tv2, tv1);
		assertNotEquals(tv1, tv3);
		assertNotEquals(tv2, tv3);
		assertNotEquals(tv3, tv1);
		assertNotEquals(tv3, tv2);
		assertEquals(tv1.hashCode(), tv2.hashCode());
		assertNotEquals(tv1.hashCode(), tv3.hashCode());
		assertNotEquals(tv2.hashCode(), tv3.hashCode());
	}

	@Test
	public void testReflectionHelperCompareArguments_ExactMatching() {
		StandardTypeConverter tc = new StandardTypeConverter();

		// Calling foo(String) with (String) is exact match
		checkMatch(new Class<?>[] {String.class}, new Class<?>[] {String.class}, tc, ReflectionHelper.ArgumentsMatchKind.EXACT);

		// Calling foo(String,Integer) with (String,Integer) is exact match
		checkMatch(new Class<?>[] {String.class, Integer.class}, new Class<?>[] {String.class, Integer.class}, tc, ArgumentsMatchKind.EXACT);
	}

	@Test
	public void testReflectionHelperCompareArguments_CloseMatching() {
		StandardTypeConverter tc = new StandardTypeConverter();

		// Calling foo(List) with (ArrayList) is close match (no conversion required)
		checkMatch(new Class<?>[] {ArrayList.class}, new Class<?>[] {List.class}, tc, ArgumentsMatchKind.CLOSE);

		// Passing (Sub,String) on call to foo(Super,String) is close match
		checkMatch(new Class<?>[] {Sub.class, String.class}, new Class<?>[] {Super.class, String.class}, tc, ArgumentsMatchKind.CLOSE);

		// Passing (String,Sub) on call to foo(String,Super) is close match
		checkMatch(new Class<?>[] {String.class, Sub.class}, new Class<?>[] {String.class, Super.class}, tc, ArgumentsMatchKind.CLOSE);
	}

	@Test
	public void testReflectionHelperCompareArguments_RequiresConversionMatching() {
		StandardTypeConverter tc = new StandardTypeConverter();

		// Calling foo(String,int) with (String,Integer) requires boxing conversion of argument one
		checkMatch(new Class<?>[] {String.class, Integer.TYPE}, new Class<?>[] {String.class,Integer.class},tc, ArgumentsMatchKind.CLOSE);

		// Passing (int,String) on call to foo(Integer,String) requires boxing conversion of argument zero
		checkMatch(new Class<?>[] {Integer.TYPE, String.class}, new Class<?>[] {Integer.class, String.class},tc, ArgumentsMatchKind.CLOSE);

		// Passing (int,Sub) on call to foo(Integer,Super) requires boxing conversion of argument zero
		checkMatch(new Class<?>[] {Integer.TYPE, Sub.class}, new Class<?>[] {Integer.class, Super.class}, tc, ArgumentsMatchKind.CLOSE);

		// Passing (int,Sub,boolean) on call to foo(Integer,Super,Boolean) requires boxing conversion of arguments zero and two
		// TODO checkMatch(new Class<?>[] {Integer.TYPE, Sub.class, Boolean.TYPE}, new Class<?>[] {Integer.class, Super.class, Boolean.class}, tc, ArgsMatchKind.REQUIRES_CONVERSION);
	}

	@Test
	public void testReflectionHelperCompareArguments_NotAMatch() {
		StandardTypeConverter typeConverter = new StandardTypeConverter();

		// Passing (Super,String) on call to foo(Sub,String) is not a match
		checkMatch(new Class<?>[] {Super.class,String.class}, new Class<?>[] {Sub.class,String.class}, typeConverter, null);
	}

	@Test
	public void testReflectionHelperCompareArguments_Varargs_ExactMatching() {
		StandardTypeConverter tc = new StandardTypeConverter();

		// Passing (String[]) on call to (String[]) is exact match
		checkMatch2(new Class<?>[] {String[].class}, new Class<?>[] {String[].class}, tc, ArgumentsMatchKind.EXACT);

		// Passing (Integer, String[]) on call to (Integer, String[]) is exact match
		checkMatch2(new Class<?>[] {Integer.class, String[].class}, new Class<?>[] {Integer.class, String[].class}, tc, ArgumentsMatchKind.EXACT);

		// Passing (String, Integer, String[]) on call to (String, String, String[]) is exact match
		checkMatch2(new Class<?>[] {String.class, Integer.class, String[].class}, new Class<?>[] {String.class,Integer.class, String[].class}, tc, ArgumentsMatchKind.EXACT);

		// Passing (Sub, String[]) on call to (Super, String[]) is exact match
		checkMatch2(new Class<?>[] {Sub.class, String[].class}, new Class<?>[] {Super.class,String[].class}, tc, ArgumentsMatchKind.CLOSE);

		// Passing (Integer, String[]) on call to (String, String[]) is exact match
		checkMatch2(new Class<?>[] {Integer.class, String[].class}, new Class<?>[] {String.class, String[].class}, tc, ArgumentsMatchKind.REQUIRES_CONVERSION);

		// Passing (Integer, Sub, String[]) on call to (String, Super, String[]) is exact match
		checkMatch2(new Class<?>[] {Integer.class, Sub.class, String[].class}, new Class<?>[] {String.class,Super .class, String[].class}, tc, ArgumentsMatchKind.REQUIRES_CONVERSION);

		// Passing (String) on call to (String[]) is exact match
		checkMatch2(new Class<?>[] {String.class}, new Class<?>[] {String[].class}, tc, ArgumentsMatchKind.EXACT);

		// Passing (Integer,String) on call to (Integer,String[]) is exact match
		checkMatch2(new Class<?>[] {Integer.class, String.class}, new Class<?>[] {Integer.class, String[].class}, tc, ArgumentsMatchKind.EXACT);

		// Passing (String) on call to (Integer[]) is conversion match (String to Integer)
		checkMatch2(new Class<?>[] {String.class}, new Class<?>[] {Integer[].class}, tc, ArgumentsMatchKind.REQUIRES_CONVERSION);

		// Passing (Sub) on call to (Super[]) is close match
		checkMatch2(new Class<?>[] {Sub.class}, new Class<?>[] {Super[].class}, tc, ArgumentsMatchKind.CLOSE);

		// Passing (Super) on call to (Sub[]) is not a match
		checkMatch2(new Class<?>[] {Super.class}, new Class<?>[] {Sub[].class}, tc, null);

		checkMatch2(new Class<?>[] {Unconvertable.class, String.class}, new Class<?>[] {Sub.class, Super[].class}, tc, null);

		checkMatch2(new Class<?>[] {Integer.class, Integer.class, String.class}, new Class<?>[] {String.class, String.class, Super[].class}, tc, null);

		checkMatch2(new Class<?>[] {Unconvertable.class, String.class}, new Class<?>[] {Sub.class, Super[].class}, tc, null);

		checkMatch2(new Class<?>[] {Integer.class, Integer.class, String.class}, new Class<?>[] {String.class, String.class, Super[].class}, tc, null);

		checkMatch2(new Class<?>[] {Integer.class, Integer.class, Sub.class}, new Class<?>[] {String.class, String.class, Super[].class}, tc, ArgumentsMatchKind.REQUIRES_CONVERSION);

		checkMatch2(new Class<?>[] {Integer.class, Integer.class, Integer.class}, new Class<?>[] {Integer.class, String[].class}, tc, ArgumentsMatchKind.REQUIRES_CONVERSION);
		// what happens on (Integer,String) passed to (Integer[]) ?
	}

	@Test
	public void testConvertArguments() throws Exception {
		StandardTypeConverter tc = new StandardTypeConverter();
		Method oneArg = TestInterface.class.getMethod("oneArg", String.class);
		Method twoArg = TestInterface.class.getMethod("twoArg", String.class, String[].class);

		// basic conversion int>String
		Object[] args = new Object[] {3};
		ReflectionHelper.convertArguments(tc, args, oneArg, null);
		checkArguments(args, "3");

		// varargs but nothing to convert
		args = new Object[] {3};
		ReflectionHelper.convertArguments(tc, args, twoArg, 1);
		checkArguments(args, "3");

		// varargs with nothing needing conversion
		args = new Object[] {3, "abc", "abc"};
		ReflectionHelper.convertArguments(tc, args, twoArg, 1);
		checkArguments(args, "3", "abc", "abc");

		// varargs with conversion required
		args = new Object[] {3, false ,3.0d};
		ReflectionHelper.convertArguments(tc, args, twoArg, 1);
		checkArguments(args, "3", "false", "3.0");
	}

	@Test
	public void testConvertArguments2() throws Exception {
		StandardTypeConverter tc = new StandardTypeConverter();
		Method oneArg = TestInterface.class.getMethod("oneArg", String.class);
		Method twoArg = TestInterface.class.getMethod("twoArg", String.class, String[].class);

		// Simple conversion: int to string
		Object[] args = new Object[] {3};
		ReflectionHelper.convertAllArguments(tc, args, oneArg);
		checkArguments(args, "3");

		// varargs conversion
		args = new Object[] {3, false, 3.0f};
		ReflectionHelper.convertAllArguments(tc, args, twoArg);
		checkArguments(args, "3", "false", "3.0");

		// varargs conversion but no varargs
		args = new Object[] {3};
		ReflectionHelper.convertAllArguments(tc, args, twoArg);
		checkArguments(args, "3");

		// null value
		args = new Object[] {3, null, 3.0f};
		ReflectionHelper.convertAllArguments(tc, args, twoArg);
		checkArguments(args, "3", null, "3.0");
	}

	@Test
	public void testSetupArguments() {
		Object[] newArray = ReflectionHelper.setupArgumentsForVarargsInvocation(
				new Class<?>[] {String[].class}, "a", "b", "c");

		assertEquals(1, newArray.length);
		Object firstParam = newArray[0];
		assertEquals(String.class,firstParam.getClass().getComponentType());
		Object[] firstParamArray = (Object[]) firstParam;
		assertEquals(3,firstParamArray.length);
		assertEquals("a",firstParamArray[0]);
		assertEquals("b",firstParamArray[1]);
		assertEquals("c",firstParamArray[2]);
	}

	@Test
	public void testReflectivePropertyAccessor() throws Exception {
		ReflectivePropertyAccessor rpa = new ReflectivePropertyAccessor();
		Tester t = new Tester();
		t.setProperty("hello");
		EvaluationContext ctx = new StandardEvaluationContext(t);
		assertTrue(rpa.canRead(ctx, t, "property"));
		assertEquals("hello",rpa.read(ctx, t, "property").getValue());
		assertEquals("hello",rpa.read(ctx, t, "property").getValue()); // cached accessor used

		assertTrue(rpa.canRead(ctx, t, "field"));
		assertEquals(3,rpa.read(ctx, t, "field").getValue());
		assertEquals(3,rpa.read(ctx, t, "field").getValue()); // cached accessor used

		assertTrue(rpa.canWrite(ctx, t, "property"));
		rpa.write(ctx, t, "property", "goodbye");
		rpa.write(ctx, t, "property", "goodbye"); // cached accessor used

		assertTrue(rpa.canWrite(ctx, t, "field"));
		rpa.write(ctx, t, "field", 12);
		rpa.write(ctx, t, "field", 12);

		// Attempted write as first activity on this field and property to drive testing
		// of populating type descriptor cache
		rpa.write(ctx, t, "field2", 3);
		rpa.write(ctx, t, "property2", "doodoo");
		assertEquals(3,rpa.read(ctx, t, "field2").getValue());

		// Attempted read as first activity on this field and property (no canRead before them)
		assertEquals(0,rpa.read(ctx, t, "field3").getValue());
		assertEquals("doodoo",rpa.read(ctx, t, "property3").getValue());

		// Access through is method
		assertEquals(0,rpa .read(ctx, t, "field3").getValue());
		assertEquals(false,rpa.read(ctx, t, "property4").getValue());
		assertTrue(rpa.canRead(ctx, t, "property4"));

		// repro SPR-9123, ReflectivePropertyAccessor JavaBean property names compliance tests
		assertEquals("iD",rpa.read(ctx, t, "iD").getValue());
		assertTrue(rpa.canRead(ctx, t, "iD"));
		assertEquals("id",rpa.read(ctx, t, "id").getValue());
		assertTrue(rpa.canRead(ctx, t, "id"));
		assertEquals("ID",rpa.read(ctx, t, "ID").getValue());
		assertTrue(rpa.canRead(ctx, t, "ID"));
		// note: "Id" is not a valid JavaBean name, nevertheless it is treated as "id"
		assertEquals("id",rpa.read(ctx, t, "Id").getValue());
		assertTrue(rpa.canRead(ctx, t, "Id"));

		// repro SPR-10994
		assertEquals("xyZ",rpa.read(ctx, t, "xyZ").getValue());
		assertTrue(rpa.canRead(ctx, t, "xyZ"));
		assertEquals("xY",rpa.read(ctx, t, "xY").getValue());
		assertTrue(rpa.canRead(ctx, t, "xY"));

		// SPR-10122, ReflectivePropertyAccessor JavaBean property names compliance tests - setters
		rpa.write(ctx, t, "pEBS", "Test String");
		assertEquals("Test String",rpa.read(ctx, t, "pEBS").getValue());
	}

	@Test
	public void testOptimalReflectivePropertyAccessor() throws Exception {
		ReflectivePropertyAccessor rpa = new ReflectivePropertyAccessor();
		Tester t = new Tester();
		t.setProperty("hello");
		EvaluationContext ctx = new StandardEvaluationContext(t);
		assertTrue(rpa.canRead(ctx, t, "property"));
		assertEquals("hello", rpa.read(ctx, t, "property").getValue());
		assertEquals("hello", rpa.read(ctx, t, "property").getValue()); // cached accessor used

		PropertyAccessor optA = rpa.createOptimalAccessor(ctx, t, "property");
		assertTrue(optA.canRead(ctx, t, "property"));
		assertFalse(optA.canRead(ctx, t, "property2"));
		try {
			optA.canWrite(ctx, t, "property");
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		try {
			optA.canWrite(ctx, t, "property2");
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		assertEquals("hello",optA.read(ctx, t, "property").getValue());
		assertEquals("hello",optA.read(ctx, t, "property").getValue()); // cached accessor used

		try {
			optA.getSpecificTargetClasses();
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		try {
			optA.write(ctx, t, "property", null);
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}

		optA = rpa.createOptimalAccessor(ctx, t, "field");
		assertTrue(optA.canRead(ctx, t, "field"));
		assertFalse(optA.canRead(ctx, t, "field2"));
		try {
			optA.canWrite(ctx, t, "field");
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		try {
			optA.canWrite(ctx, t, "field2");
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		assertEquals(3,optA.read(ctx, t, "field").getValue());
		assertEquals(3,optA.read(ctx, t, "field").getValue());  // cached accessor used

		try {
			optA.getSpecificTargetClasses();
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
		try {
			optA.write(ctx, t, "field", null);
			fail();
		}
		catch (UnsupportedOperationException uoe) {
			// success
		}
	}


	/**
	 * Used to validate the match returned from a compareArguments call.
	 */
	private void checkMatch(Class<?>[] inputTypes, Class<?>[] expectedTypes, StandardTypeConverter typeConverter, ArgumentsMatchKind expectedMatchKind) {
		ReflectionHelper.ArgumentsMatchInfo matchInfo = ReflectionHelper.compareArguments(getTypeDescriptors(expectedTypes), getTypeDescriptors(inputTypes), typeConverter);
		if (expectedMatchKind == null) {
			assertNull("Did not expect them to match in any way", matchInfo);
		}
		else {
			assertNotNull("Should not be a null match", matchInfo);
		}

		if (expectedMatchKind == ArgumentsMatchKind.EXACT) {
			assertTrue(matchInfo.isExactMatch());
		}
		else if (expectedMatchKind == ArgumentsMatchKind.CLOSE) {
			assertTrue(matchInfo.isCloseMatch());
		}
		else if (expectedMatchKind == ArgumentsMatchKind.REQUIRES_CONVERSION) {
			assertTrue("expected to be a match requiring conversion, but was " + matchInfo, matchInfo.isMatchRequiringConversion());
		}
	}

	/**
	 * Used to validate the match returned from a compareArguments call.
	 */
	private void checkMatch2(Class<?>[] inputTypes, Class<?>[] expectedTypes, StandardTypeConverter typeConverter, ArgumentsMatchKind expectedMatchKind) {
		ReflectionHelper.ArgumentsMatchInfo matchInfo = ReflectionHelper.compareArgumentsVarargs(getTypeDescriptors(expectedTypes), getTypeDescriptors(inputTypes), typeConverter);
		if (expectedMatchKind == null) {
			assertNull("Did not expect them to match in any way: " + matchInfo, matchInfo);
		}
		else {
			assertNotNull("Should not be a null match", matchInfo);
		}

		if (expectedMatchKind == ArgumentsMatchKind.EXACT) {
			assertTrue(matchInfo.isExactMatch());
		}
		else if (expectedMatchKind == ArgumentsMatchKind.CLOSE) {
			assertTrue(matchInfo.isCloseMatch());
		}
		else if (expectedMatchKind == ArgumentsMatchKind.REQUIRES_CONVERSION) {
			assertTrue("expected to be a match requiring conversion, but was " + matchInfo, matchInfo.isMatchRequiringConversion());
		}
	}

	private void checkArguments(Object[] args, Object... expected) {
		assertEquals(expected.length,args.length);
		for (int i = 0; i < expected.length; i++) {
			checkArgument(expected[i],args[i]);
		}
	}

	private void checkArgument(Object expected, Object actual) {
		assertEquals(expected,actual);
	}

	private List<TypeDescriptor> getTypeDescriptors(Class<?>... types) {
		List<TypeDescriptor> typeDescriptors = new ArrayList<>(types.length);
		for (Class<?> type : types) {
			typeDescriptors.add(TypeDescriptor.valueOf(type));
		}
		return typeDescriptors;
	}


	public interface TestInterface {

		void oneArg(String arg1);

		void twoArg(String arg1, String... arg2);
	}


	static class Super {
	}


	static class Sub extends Super {
	}


	static class Unconvertable {
	}


	static class Tester {

		String property;
		public int field = 3;
		public int field2;
		public int field3 = 0;
		String property2;
		String property3 = "doodoo";
		boolean property4 = false;
		String iD = "iD";
		String id = "id";
		String ID = "ID";
		String pEBS = "pEBS";
		String xY = "xY";
		String xyZ = "xyZ";

		public String getProperty() { return property; }

		public void setProperty(String value) { property = value; }

		public void setProperty2(String value) { property2 = value; }

		public String getProperty3() { return property3; }

		public boolean isProperty4() { return property4; }

		public String getiD() { return iD; }

		public String getId() { return id; }

		public String getID() { return ID; }

		public String getXY() { return xY; }

		public String getXyZ() { return xyZ; }

		public String getpEBS() { return pEBS; }

		public void setpEBS(String pEBS) { this.pEBS = pEBS; }
	}

}
