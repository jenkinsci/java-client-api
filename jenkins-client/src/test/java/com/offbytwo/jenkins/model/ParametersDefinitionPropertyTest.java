package com.offbytwo.jenkins.model;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ParametersDefinitionPropertyTest extends BaseXmlMarshallingTest {

    @Test
    public void shouldMarshallParametersDefinitionProperty() throws JAXBException {
        // given
        StringWriter sw = new StringWriter();
        configureXmlMarshallerFor(ParametersDefinitionProperty.class);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ParametersDefinitionProperty pdp = givenParametersDefinitionProperty();

        // when
        marshaller.marshal(pdp, sw);

        // then
        assertTrue(sw.toString().contains("<name>aaa</name>"));
        assertTrue(sw.toString().contains("<description>aaa</description>"));
        assertTrue(sw.toString().contains("<defaultValue>aaa</defaultValue>"));

        assertTrue(sw.toString().contains("<name>bbb</name>"));
        assertTrue(sw.toString().contains("<description>bbb</description>"));
        assertTrue(sw.toString().contains("<defaultValue>bbb</defaultValue>"));
    }

    private ParametersDefinitionProperty givenParametersDefinitionProperty() {
        StringParameterDefinition s1 = new StringParameterDefinition("aaa", "aaa", "aaa");
        StringParameterDefinition s2 = new StringParameterDefinition("bbb", "bbb", "bbb");
        List<StringParameterDefinition> list = new ArrayList<StringParameterDefinition>();
        list.add(s1);
        list.add(s2);
        ParameterDefinitions pd = new ParameterDefinitions();
        pd.setStringParams(list);
        ParametersDefinitionProperty pdp = new ParametersDefinitionProperty();
        pdp.setPd(pd);
        return pdp;
    }
}
