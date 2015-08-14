package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.BaseUnitTest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public abstract class BaseXmlMarshallingTest extends BaseUnitTest {

    JAXBContext jaxbContext;
    Marshaller marshaller;

    protected void configureXmlMarshallerFor(Class<?> clazz) throws JAXBException {
        jaxbContext = JAXBContext.newInstance(clazz);
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    }
}
