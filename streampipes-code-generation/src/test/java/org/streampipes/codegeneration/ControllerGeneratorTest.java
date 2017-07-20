package org.streampipes.codegeneration;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;

import org.streampipes.sdk.PrimitivePropertyBuilder;
import org.streampipes.sdk.stream.SchemaBuilder;
import org.streampipes.sdk.stream.StreamBuilder;
import org.streampipes.model.impl.EventStream;
import org.streampipes.model.impl.eventproperty.EventProperty;
import org.streampipes.model.impl.eventproperty.EventPropertyPrimitive;
import org.streampipes.model.impl.output.AppendOutputStrategy;
import org.streampipes.model.impl.output.OutputStrategy;
import org.streampipes.model.vocabulary.XSD;
import org.streampipes.codegeneration.flink.sepa.FlinkSepaControllerGenerator;
import org.streampipes.codegeneration.utils.TV;
import org.streampipes.commons.Utils;

public class ControllerGeneratorTest {

//	@Test
//	public void testGetEventStream() {
//
//		Builder b = MethodSpec.methodBuilder("testMethod");
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		EventStream eventStream = StreamBuilder.createStream(TV.NAME, TV.DESCRIPTION, TV.PATH_NAME)
//				.schema(SchemaBuilder.create().build()).build();
//
//		Builder actual = cb.getEventStream(b, eventStream, 0);
//		String expected = "void testMethod() {\n  java.util.List<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty> eventProperties0 = new java.util.ArrayList<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty>();\n"
//				+ "  de.fzi.cep.sepa.model.impl.EventStream stream0 = de.fzi.cep.sepa.model.builder.StreamBuilder.createStream(\"TestProject\", \"Example description\", \"sepa/testProject\").schema(de.fzi.cep.sepa.model.builder.SchemaBuilder.create().properties(eventProperties0).build()).build();\n}\n";
//
//		//TODO fix test
////		assertEquals(expected, actual.build().toString());
//	}
//
//	@Test
//	public void testGetEventProperties() {
//		Builder b = MethodSpec.methodBuilder("testMethod");
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		List<EventProperty> eventProperties = new ArrayList<EventProperty>();
//		eventProperties.add(PrimitivePropertyBuilder.createPropertyRestriction("http://test.org#mytest").build());
//
//		String actual = cb.getEventProperties(b, eventProperties, 0).build().toString();
//		String expected = "void testMethod() {\n  java.util.List<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty> eventProperties0 = new java.util.ArrayList<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty>();\n"
//				+ "  de.fzi.cep.sepa.model.impl.eventproperty.EventProperty e0 = de.fzi.cep.sepa.model.builder.PrimitivePropertyBuilder.createPropertyRestriction(\"http://test.org#mytest\").build();\n"
//				+ "  eventProperties0.add(e0);\n}\n";
//
//		//TODO fix test
////		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void testAppendGetOutputStrategyWithNoEventProperty() {
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		Builder b = MethodSpec.methodBuilder("testMethod");
//
//		AppendOutputStrategy appendStrategy = new AppendOutputStrategy();
//
//		String actual = cb.getAppendOutputStrategy(b, appendStrategy, 0).build().toString();
//		String expected = "void testMethod() {\n"
//				+ "  de.fzi.cep.sepa.model.impl.output.AppendOutputStrategy outputStrategy0 = new de.fzi.cep.sepa.model.impl.output.AppendOutputStrategy();\n"
//				+ "  java.util.List<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty> appendProperties = new java.util.ArrayList<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty>();\n"
//				+ "  outputStrategy0.setEventProperties(appendProperties);\n" + "}\n";
//
//		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void testAppendGetOutputStrategyWithEventProperties() {
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		Builder b = MethodSpec.methodBuilder("testMethod");
//
//		AppendOutputStrategy appendStrategy = new AppendOutputStrategy();
//		List<EventProperty> eventProperties = new ArrayList<EventProperty>();
//		eventProperties.add(new EventPropertyPrimitive(XSD._long.toString(), "testTime", "",
//				Utils.createURI("http://schema.org/Number")));
//		appendStrategy.setEventProperties(eventProperties);
//
//		String actual = cb.getAppendOutputStrategy(b, appendStrategy, 0).build().toString();
//		String expected = "void testMethod() {\n"
//				+ "  de.fzi.cep.sepa.model.impl.output.AppendOutputStrategy outputStrategy0 = new de.fzi.cep.sepa.model.impl.output.AppendOutputStrategy();\n"
//				+ "  java.util.List<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty> appendProperties = new java.util.ArrayList<de.fzi.cep.sepa.model.impl.eventproperty.EventProperty>();\n"
//				+ "  appendProperties.add(de.fzi.cep.sepa.model.builder.EpProperties.stringEp(\"testTime\", \"http://schema.org/Number\"));\n"
//				+ "  outputStrategy0.setEventProperties(appendProperties);\n" + "}\n";
//
//		//TODO fixtest
////		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void testGetOutputStrategies() {
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		Builder b = MethodSpec.methodBuilder("testMethod");
//
//		List<OutputStrategy> strategies = new ArrayList<OutputStrategy>();
//
//		String actual = cb.getOutputStrategies(b, strategies).build().toString();
//		String expected = "void testMethod() {\n"
//				+ "  java.util.List<de.fzi.cep.sepa.model.impl.output.OutputStrategy> strategies = new java.util.ArrayList<de.fzi.cep.sepa.model.impl.output.OutputStrategy>();\n"
//				+ "}\n";
//
//		assertEquals(expected, actual);
//	}
//
//	@Test
//	public void testGetSupportedGrounding() {
//		FlinkSepaControllerGenerator cb = new FlinkSepaControllerGenerator(null, "", "");
//		Builder b = MethodSpec.methodBuilder("testMethod");
//
//		String actual = cb.getSupportedGrounding(b, null).build().toString();
//		String expected = "void testMethod() {\n"
//				+ "  desc.setSupportedGrounding(de.fzi.cep.sepa.client.util.StandardTransportFormat.getSupportedGrounding());\n"
//				+ "}\n";
//
//		assertEquals(expected, actual);
//	}
}
