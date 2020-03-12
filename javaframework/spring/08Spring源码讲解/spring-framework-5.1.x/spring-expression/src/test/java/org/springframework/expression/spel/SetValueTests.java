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

package org.springframework.expression.spel;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;

import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.testresources.PlaceOfBirth;

import static org.junit.Assert.*;

/**
 * Tests set value expressions.
 *
 * @author Keith Donald
 * @author Andy Clement
 */
public class SetValueTests extends AbstractExpressionTests {

	private final static boolean DEBUG = false;


	@Test
	public void testSetProperty() {
		setValue("wonNobelPrize", true);
	}

	@Test
	public void testSetNestedProperty() {
		setValue("placeOfBirth.city", "Wien");
	}

	@Test
	public void testSetArrayElementValue() {
		setValue("inventions[0]", "Just the telephone");
	}

	@Test
	public void testErrorCase() {
		setValueExpectError("3=4", null);
	}

	@Test
	public void testSetElementOfNull() {
		setValueExpectError("new org.springframework.expression.spel.testresources.Inventor().inventions[1]",
				SpelMessage.CANNOT_INDEX_INTO_NULL_VALUE);
	}

	@Test
	public void testSetArrayElementValueAllPrimitiveTypes() {
		setValue("arrayContainer.ints[1]", 3);
		setValue("arrayContainer.floats[1]", 3.0f);
		setValue("arrayContainer.booleans[1]", false);
		setValue("arrayContainer.doubles[1]", 3.4d);
		setValue("arrayContainer.shorts[1]", (short)3);
		setValue("arrayContainer.longs[1]", 3L);
		setValue("arrayContainer.bytes[1]", (byte) 3);
		setValue("arrayContainer.chars[1]", (char) 3);
	}

	@Test
	public void testIsWritableForInvalidExpressions_SPR10610() {
		Expression e = null;
		StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();

		// PROPERTYORFIELDREFERENCE
		// Non existent field (or property):
		e = parser.parseExpression("arrayContainer.wibble");
		assertFalse("Should not be writable!",e.isWritable(lContext));

		e = parser.parseExpression("arrayContainer.wibble.foo");
		try {
			assertFalse("Should not be writable!",e.isWritable(lContext));
			fail("Should have had an error because wibble does not really exist");
		}
		catch (SpelEvaluationException see) {
//			org.springframework.expression.spel.SpelEvaluationException: EL1008E:(pos 15): Property or field 'wibble' cannot be found on object of type 'org.springframework.expression.spel.testresources.ArrayContainer' - maybe not public?
//					at org.springframework.expression.spel.ast.PropertyOrFieldReference.readProperty(PropertyOrFieldReference.java:225)
			// success!
		}

		// VARIABLE
		// the variable does not exist (but that is OK, we should be writable)
		e = parser.parseExpression("#madeup1");
		assertTrue("Should be writable!",e.isWritable(lContext));

		e = parser.parseExpression("#madeup2.bar"); // compound expression
		assertFalse("Should not be writable!",e.isWritable(lContext));

		// INDEXER
		// non existent indexer (wibble made up)
		e = parser.parseExpression("arrayContainer.wibble[99]");
		try {
			assertFalse("Should not be writable!",e.isWritable(lContext));
			fail("Should have had an error because wibble does not really exist");
		}
		catch (SpelEvaluationException see) {
			// success!
		}

		// non existent indexer (index via a string)
		e = parser.parseExpression("arrayContainer.ints['abc']");
		try {
			assertFalse("Should not be writable!",e.isWritable(lContext));
			fail("Should have had an error because wibble does not really exist");
		}
		catch (SpelEvaluationException see) {
			// success!
		}
	}

	@Test
	public void testSetArrayElementValueAllPrimitiveTypesErrors() {
		// none of these sets are possible due to (expected) conversion problems
		setValueExpectError("arrayContainer.ints[1]", "wibble");
		setValueExpectError("arrayContainer.floats[1]", "dribble");
		setValueExpectError("arrayContainer.booleans[1]", "nein");
		// TODO -- this fails with NPE due to ArrayToObject converter - discuss with Andy
		//setValueExpectError("arrayContainer.doubles[1]", new ArrayList<String>());
		//setValueExpectError("arrayContainer.shorts[1]", new ArrayList<String>());
		//setValueExpectError("arrayContainer.longs[1]", new ArrayList<String>());
		setValueExpectError("arrayContainer.bytes[1]", "NaB");
		setValueExpectError("arrayContainer.chars[1]", "NaC");
	}

	@Test
	public void testSetArrayElementNestedValue() {
		setValue("placesLived[0].city", "Wien");
	}

	@Test
	public void testSetListElementValue() {
		setValue("placesLivedList[0]", new PlaceOfBirth("Wien"));
	}

	@Test
	public void testSetGenericListElementValueTypeCoersion() {
		// TODO currently failing since setValue does a getValue and "Wien" string != PlaceOfBirth - check with andy
		setValue("placesLivedList[0]", "Wien");
	}

	@Test
	public void testSetGenericListElementValueTypeCoersionOK() {
		setValue("booleanList[0]", "true", Boolean.TRUE);
	}

	@Test
	public void testSetListElementNestedValue() {
		setValue("placesLived[0].city", "Wien");
	}

	@Test
	public void testSetArrayElementInvalidIndex() {
		setValueExpectError("placesLived[23]", "Wien");
		setValueExpectError("placesLivedList[23]", "Wien");
	}

	@Test
	public void testSetMapElements() {
		setValue("testMap['montag']","lundi");
	}

	@Test
	public void testIndexingIntoUnsupportedType() {
		setValueExpectError("'hello'[3]", 'p');
	}

	@Test
	public void testSetPropertyTypeCoersion() {
		setValue("publicBoolean", "true", Boolean.TRUE);
	}

	@Test
	public void testSetPropertyTypeCoersionThroughSetter() {
		setValue("SomeProperty", "true", Boolean.TRUE);
	}

	@Test
	public void testAssign() throws Exception {
		StandardEvaluationContext eContext = TestScenarioCreator.getTestEvaluationContext();
		Expression e = parse("publicName='Andy'");
		assertFalse(e.isWritable(eContext));
		assertEquals("Andy",e.getValue(eContext));
	}

	/*
	 * Testing the coercion of both the keys and the values to the correct type
	 */
	@Test
	public void testSetGenericMapElementRequiresCoercion() throws Exception {
		StandardEvaluationContext eContext = TestScenarioCreator.getTestEvaluationContext();
		Expression e = parse("mapOfStringToBoolean[42]");
		assertNull(e.getValue(eContext));

		// Key should be coerced to string representation of 42
		e.setValue(eContext, "true");

		// All keys should be strings
		Set<?> ks = parse("mapOfStringToBoolean.keySet()").getValue(eContext, Set.class);
		for (Object o: ks) {
			assertEquals(String.class,o.getClass());
		}

		// All values should be booleans
		Collection<?> vs = parse("mapOfStringToBoolean.values()").getValue(eContext, Collection.class);
		for (Object o: vs) {
			assertEquals(Boolean.class, o.getClass());
		}

		// One final test check coercion on the key for a map lookup
		Object o = e.getValue(eContext);
		assertEquals(Boolean.TRUE,o);
	}


	private Expression parse(String expressionString) throws Exception {
		return parser.parseExpression(expressionString);
	}

	/**
	 * Call setValue() but expect it to fail.
	 */
	protected void setValueExpectError(String expression, Object value) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			e.setValue(lContext, value);
			fail("expected an error");
		}
		catch (ParseException pe) {
			pe.printStackTrace();
			fail("Unexpected Exception: " + pe.getMessage());
		}
		catch (EvaluationException ee) {
			// success!
		}
	}

	protected void setValue(String expression, Object value) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			assertTrue("Expression is not writeable but should be", e.isWritable(lContext));
			e.setValue(lContext, value);
			assertEquals("Retrieved value was not equal to set value", value, e.getValue(lContext,value.getClass()));
		}
		catch (EvaluationException | ParseException ex) {
			ex.printStackTrace();
			fail("Unexpected Exception: " + ex.getMessage());
		}
	}

	/**
	 * For use when coercion is happening during a setValue().  The expectedValue should be
	 * the coerced form of the value.
	 */
	protected void setValue(String expression, Object value, Object expectedValue) {
		try {
			Expression e = parser.parseExpression(expression);
			if (e == null) {
				fail("Parser returned null for expression");
			}
			if (DEBUG) {
				SpelUtilities.printAbstractSyntaxTree(System.out, e);
			}
			StandardEvaluationContext lContext = TestScenarioCreator.getTestEvaluationContext();
			assertTrue("Expression is not writeable but should be", e.isWritable(lContext));
			e.setValue(lContext, value);
			Object a = expectedValue;
			Object b = e.getValue(lContext);
			if (!a.equals(b)) {
				fail("Not the same: ["+a+"] type="+a.getClass()+"  ["+b+"] type="+b.getClass());
//				assertEquals("Retrieved value was not equal to set value", expectedValue, e.getValue(lContext));
			}
		}
		catch (EvaluationException | ParseException ex) {
			ex.printStackTrace();
			fail("Unexpected Exception: " + ex.getMessage());
		}
	}

}
