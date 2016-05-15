package com.offbytwo.jenkins.model;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class StringParameterDefinitionTest extends BaseXmlMarshallingTest {

    public static final String EXPECTED = "<hudson.model.StringParameterDefinition><name>aaa</name><description>bbb</description><defaultValue>ccc</defaultValue></hudson.model.StringParameterDefinition>";

    @Test
    public void shouldMarshallStringParameterDefinition() throws JAXBException {
        // given
        configureXmlMarshallerFor(StringParameterDefinition.class);
        StringWriter sw = new StringWriter();

        // when
        marshaller.marshal(new StringParameterDefinition("aaa", "bbb", "ccc"), sw);

        // then
        assertEquals(EXPECTED, sw.toString());
    }
}
