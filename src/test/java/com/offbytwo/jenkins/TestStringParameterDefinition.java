package com.offbytwo.jenkins;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.dom4j.io.DocumentResult;
import org.junit.Test;

import com.offbytwo.jenkins.model.StringParameterDefinition;

public class TestStringParameterDefinition {

	@Test
	public void testMarshalling() throws JAXBException{
		StringWriter sw = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(StringParameterDefinition.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
		marshaller.marshal(new StringParameterDefinition("aaa","bbb","ccc"), sw);
		System.out.println(sw.toString());
	}
}
