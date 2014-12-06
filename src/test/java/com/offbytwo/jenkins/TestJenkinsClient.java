package com.offbytwo.jenkins;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.ParameterDefinitions;
import com.offbytwo.jenkins.model.ParametersDefinitionProperty;
import com.offbytwo.jenkins.model.StringParameterDefinition;

public class TestJenkinsClient {

    public static void main(String[] args) throws URISyntaxException, IOException, JAXBException, DocumentException {
        JenkinsHttpClient jhc = new JenkinsHttpClient(new URI("http://localhost/jenkins/"), "", "");
        JenkinsServer js = new JenkinsServer(jhc);
        js.addStringParam("TestCreateJob", "paramTest2", "Desc", "1");
    }

    public static String testMarshalling() throws JAXBException {
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
//		System.out.println(sw.toString());
        return sw.toString();
    }
}
