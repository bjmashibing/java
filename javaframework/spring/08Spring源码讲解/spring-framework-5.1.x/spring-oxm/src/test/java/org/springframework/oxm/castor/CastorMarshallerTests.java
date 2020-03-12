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

package org.springframework.oxm.castor;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;

import org.castor.xml.XMLProperties;
import org.exolab.castor.xml.XercesXMLSerializerFactory;
import org.junit.Test;
import org.mockito.InOrder;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xmlunit.builder.Input;
import org.xmlunit.xpath.JAXPXPathEngine;

import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.AbstractMarshallerTests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.xmlunit.matchers.CompareMatcher.*;

/**
 * Tests the {@link CastorMarshaller} class.
 *
 * @author Arjen Poutsma
 * @author Jakub Narloch
 * @author Sam Brannen
 */
@Deprecated
public class CastorMarshallerTests extends AbstractMarshallerTests<CastorMarshaller> {

	/**
	 * Represents the expected result that doesn't contain the xml declaration.
	 */
	private static final String DOCUMENT_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<tns:flights xmlns:tns=\"http://samples.springframework.org/flight\">" +
			"<tns:flight><tns:number>42</tns:number></tns:flight></tns:flights>";

	/**
	 * Represents the expected result that doesn't contain the xml namespaces.
	 */
	private static final String SUPPRESSED_NAMESPACE_EXPECTED_STRING =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?><flights><flight><number>42</number></flight></flights>";

	/**
	 * Represents the expected result with modified root element name.
	 */
	private static final String ROOT_ELEMENT_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<tns:canceledFlights xmlns:tns=\"http://samples.springframework.org/flight\">" +
			"<tns:flight><tns:number>42</tns:number></tns:flight></tns:canceledFlights>";

	/**
	 * Represents the expected result with 'xsi:type' attribute.
	 */
	private static final String XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<objects><castor-object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			" xmlns:java=\"http://java.sun.com\"" +
			" xsi:type=\"java:org.springframework.oxm.castor.CastorObject\">" +
			"<name>test</name><value>8</value></castor-object></objects>";

	/**
	 * Represents the expected result with suppressed 'xsi:type' attribute.
	 */
	private static final String SUPPRESSED_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<objects><castor-object><name>test</name><value>8</value></castor-object></objects>";

	/**
	 * Represents the expected result with 'xsi:type' attribute for root element.
	 */
	private static final String ROOT_WITH_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<objects xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			" xmlns:java=\"http://java.sun.com\"" +
			" xsi:type=\"java:java.util.Arrays$ArrayList\">" +
			"<castor-object xsi:type=\"java:org.springframework.oxm.castor.CastorObject\">" +
			"<name>test</name><value>8</value></castor-object></objects>";

	/**
	 * Represents the expected result without 'xsi:type' attribute for root element.
	 */
	private static final String ROOT_WITHOUT_XSI_EXPECTED_STRING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<objects><castor-object xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			" xmlns:java=\"http://java.sun.com\"" +
			" xsi:type=\"java:org.springframework.oxm.castor.CastorObject\">" +
			"<name>test</name><value>8</value></castor-object></objects>";


	@Override
	protected CastorMarshaller createMarshaller() throws Exception {
		CastorMarshaller marshaller = new CastorMarshaller();
		ClassPathResource mappingLocation = new ClassPathResource("mapping.xml", CastorMarshaller.class);
		marshaller.setMappingLocation(mappingLocation);
		Map<String, String> props = new HashMap<>(1);
		props.put(XMLProperties.SERIALIZER_FACTORY, XercesXMLSerializerFactory.class.getName());
		marshaller.setCastorProperties(props);
		marshaller.afterPropertiesSet();
		return marshaller;
	}

	@Override
	protected Object createFlights() {
		Flight flight = new Flight();
		flight.setNumber(42L);
		Flights flights = new Flights();
		flights.addFlight(flight);
		return flights;
	}


	@Test
	public void marshalSaxResult() throws Exception {
		ContentHandler contentHandler = mock(ContentHandler.class);
		SAXResult result = new SAXResult(contentHandler);
		marshaller.marshal(flights, result);
		InOrder ordered = inOrder(contentHandler);
		ordered.verify(contentHandler).startDocument();
		ordered.verify(contentHandler).startPrefixMapping("tns", "http://samples.springframework.org/flight");
		ordered.verify(contentHandler).startElement(eq("http://samples.springframework.org/flight"),
				eq("flights"), eq("tns:flights"), isA(Attributes.class));
		ordered.verify(contentHandler).startElement(eq("http://samples.springframework.org/flight"),
				eq("flight"), eq("tns:flight"), isA(Attributes.class));
		ordered.verify(contentHandler).startElement(eq("http://samples.springframework.org/flight"),
				eq("number"), eq("tns:number"), isA(Attributes.class));
		ordered.verify(contentHandler).characters(eq(new char[]{'4', '2'}), eq(0), eq(2));
		ordered.verify(contentHandler).endElement("http://samples.springframework.org/flight", "number", "tns:number");
		ordered.verify(contentHandler).endElement("http://samples.springframework.org/flight", "flight", "tns:flight");
		ordered.verify(contentHandler).endElement("http://samples.springframework.org/flight", "flights", "tns:flights");
		ordered.verify(contentHandler).endPrefixMapping("tns");
		ordered.verify(contentHandler).endDocument();
	}

	@Test
	public void supports() throws Exception {
		assertTrue("CastorMarshaller does not support Flights", marshaller.supports(Flights.class));
		assertTrue("CastorMarshaller does not support Flight", marshaller.supports(Flight.class));
	}

	@Test
	public void suppressNamespacesTrue() throws Exception {
		marshaller.setSuppressNamespaces(true);
		String result = marshalFlights();
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(SUPPRESSED_NAMESPACE_EXPECTED_STRING));
	}

	@Test
	public void suppressNamespacesFalse() throws Exception {
		marshaller.setSuppressNamespaces(false);
		String result = marshalFlights();
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(EXPECTED_STRING));
	}

	@Test
	public void suppressXsiTypeTrue() throws Exception {
		CastorObject castorObject = createCastorObject();
		marshaller.setSuppressXsiType(true);
		marshaller.setRootElement("objects");
		String result = marshal(Arrays.asList(castorObject));
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(SUPPRESSED_XSI_EXPECTED_STRING));
	}

	@Test
	public void suppressXsiTypeFalse() throws Exception {
		CastorObject castorObject = createCastorObject();
		marshaller.setSuppressXsiType(false);
		marshaller.setRootElement("objects");
		String result = marshal(Arrays.asList(castorObject));
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(XSI_EXPECTED_STRING));
	}

	@Test
	public void marshalAsDocumentTrue() throws Exception {
		marshaller.setMarshalAsDocument(true);
		String result = marshalFlights();
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(DOCUMENT_EXPECTED_STRING));
		assertTrue("Result doesn't contain xml declaration.",
				result.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
	}

	@Test
	public void marshalAsDocumentFalse() throws Exception {
		marshaller.setMarshalAsDocument(true);
		String result = marshalFlights();
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(EXPECTED_STRING));
		assertFalse("Result contains xml declaration.", result.matches("<\\?\\s*xml"));
	}

	@Test
	public void rootElement() throws Exception {
		marshaller.setRootElement("canceledFlights");
		String result = marshalFlights();
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(ROOT_ELEMENT_EXPECTED_STRING));
	}

	@Test
	public void noNamespaceSchemaLocation() throws Exception {
		String noNamespaceSchemaLocation = "flights.xsd";
		marshaller.setNoNamespaceSchemaLocation(noNamespaceSchemaLocation);
		String result = marshalFlights();
		assertXpathEvaluatesTo("The xsi:noNamespaceSchemaLocation hasn't been written or has invalid value.",
				noNamespaceSchemaLocation, "/tns:flights/@xsi:noNamespaceSchemaLocation", result);
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(EXPECTED_STRING));
	}

	@Test
	public void schemaLocation() throws Exception {
		String schemaLocation = "flights.xsd";
		marshaller.setSchemaLocation(schemaLocation);
		String result = marshalFlights();
		assertXpathEvaluatesTo("The xsi:noNamespaceSchemaLocation hasn't been written or has invalid value.",
				schemaLocation, "/tns:flights/@xsi:schemaLocation", result);
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(EXPECTED_STRING));
	}

	@Test
	public void useXsiTypeAsRootTrue() throws Exception {
		CastorObject castorObject = createCastorObject();
		marshaller.setSuppressXsiType(false);
		marshaller.setUseXSITypeAtRoot(true);
		marshaller.setRootElement("objects");
		String result = marshal(Arrays.asList(castorObject));
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(ROOT_WITH_XSI_EXPECTED_STRING));
	}

	@Test
	public void useXsiTypeAsRootFalse() throws Exception {
		CastorObject castorObject = createCastorObject();
		marshaller.setSuppressXsiType(false);
		marshaller.setUseXSITypeAtRoot(false);
		marshaller.setRootElement("objects");
		String result = marshal(Arrays.asList(castorObject));
		assertThat("Marshaller wrote invalid result", result, isSimilarTo(ROOT_WITHOUT_XSI_EXPECTED_STRING));
	}


	private String marshal(Object object) throws Exception {
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		marshaller.marshal(object, result);
		return writer.toString();
	}

	private String marshalFlights() throws Exception {
		return marshal(flights);
	}

	/**
	 * Assert the values of xpath expression evaluation is exactly the same as expected value.
	 * <p>The xpath may contain the xml namespace prefixes, since namespaces from flight example
	 * are being registered.
	 * @param msg the error message that will be used in case of test failure
	 * @param expected the expected value
	 * @param xpath the xpath to evaluate
	 * @param xmlDoc the xml to use
	 * @throws Exception if any error occurs during xpath evaluation
	 */
	private void assertXpathEvaluatesTo(String msg, String expected, String xpath, String xmlDoc) throws Exception {
		Map<String, String> namespaces = new HashMap<>();
		namespaces.put("tns", "http://samples.springframework.org/flight");
		namespaces.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");

		JAXPXPathEngine engine = new JAXPXPathEngine();
		engine.setNamespaceContext(namespaces);

		Source source = Input.fromString(xmlDoc).build();
		Iterable<Node> nodeList = engine.selectNodes(xpath, source);
		assertEquals(msg, expected, nodeList.iterator().next().getNodeValue());
	}

	/**
	 * Create an instance of {@link CastorObject} for testing.
	 */
	private CastorObject createCastorObject() {
		CastorObject castorObject = new CastorObject();
		castorObject.setName("test");
		castorObject.setValue(8);
		return castorObject;
	}

}
