package com.offbytwo.jenkins.model;

import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParameterDefinitionsTest extends BaseXmlMarshallingTest {

    public static final String EXPECTED_PARAMETER_DEFINITION = "<parameterDefinitions><hudson.model.StringParameterDefinition><name>aaa</name><description></description><defaultValue></defaultValue></hudson.model.StringParameterDefinition><hudson.model.StringParameterDefinition><name>bbb</name><description>bbb</description><defaultValue>bbb</defaultValue></hudson.model.StringParameterDefinition></parameterDefinitions>";

    @Test
    public void shouldMarshallParameterDefinition() throws JAXBException {
        // given
        configureXmlMarshallerFor(ParameterDefinitions.class);
        ParameterDefinitions pd = givenParameterDefinition();

        // when
        StringWriter sw = new StringWriter();
        marshaller.marshal(pd, sw);

        // then
        assertEquals(EXPECTED_PARAMETER_DEFINITION, sw.toString());
    }

    private ParameterDefinitions givenParameterDefinition() {
        StringParameterDefinition s1 = new StringParameterDefinition("aaa", "", "");
        StringParameterDefinition s2 = new StringParameterDefinition("bbb", "bbb", "bbb");
        List<StringParameterDefinition> list = new ArrayList<StringParameterDefinition>();
        list.add(s1);
        list.add(s2);
        ParameterDefinitions pd = new ParameterDefinitions();
        pd.setStringParams(list);
        return pd;
    }
}
