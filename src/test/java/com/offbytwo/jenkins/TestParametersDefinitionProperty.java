package com.offbytwo.jenkins;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.dom4j.io.DocumentResult;
import org.junit.Test;

import com.offbytwo.jenkins.model.ParameterDefinitions;
import com.offbytwo.jenkins.model.ParametersDefinitionProperty;
import com.offbytwo.jenkins.model.StringParameterDefinition;

public class TestParametersDefinitionProperty {

    @Test
    public void testMarshalling() throws JAXBException {
        StringWriter sw = new StringWriter();
        JAXBContext jaxbContext = JAXBContext.newInstance(ParametersDefinitionProperty.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringParameterDefinition s1 = new StringParameterDefinition("aaa", "aaa", "aaa");
        StringParameterDefinition s2 = new StringParameterDefinition("bbb", "bbb", "bbb");
        List<StringParameterDefinition> list = new ArrayList<StringParameterDefinition>();
        list.add(s1);
        list.add(s2);
        ParameterDefinitions pd = new ParameterDefinitions();
        pd.setStringParams(list);
        ParametersDefinitionProperty pdp = new ParametersDefinitionProperty();
        pdp.setPd(pd);
        marshaller.marshal(pdp, sw);
        System.out.println(sw.toString());
    }
}
