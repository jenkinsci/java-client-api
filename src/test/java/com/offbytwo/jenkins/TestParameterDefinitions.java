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
import com.offbytwo.jenkins.model.StringParameterDefinition;

public class TestParameterDefinitions {

	@Test
	public void testMarshalling() throws JAXBException{
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(ParameterDefinitions.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		StringParameterDefinition s1 = new StringParameterDefinition("aaa", "", "");
		StringParameterDefinition s2 = new StringParameterDefinition("bbb", "bbb", "bbb");
		List<StringParameterDefinition> list = new ArrayList<StringParameterDefinition>();
		list.add(s1);list.add(s2);
		ParameterDefinitions pd = new ParameterDefinitions();
		pd.setStringParams(list);
		marshaller.marshal(pd, sw);
		System.out.println(sw.toString());
	}
}
