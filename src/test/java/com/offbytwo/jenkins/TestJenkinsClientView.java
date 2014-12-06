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

public class TestJenkinsClientView {
	public static void main(String[] args) throws URISyntaxException, IOException, JAXBException, DocumentException {
		JenkinsHttpClient jhc = new JenkinsHttpClient(new URI("http://localhost:8080/jenkins"), "", "");
		JenkinsServer js = new JenkinsServer(jhc);
		
		System.out.println(js.getView("test"));
	}
}
