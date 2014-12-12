package com.offbytwo.jenkins;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;
import org.junit.Test;

import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.JobConfiguration;

public class TestJobConfiguration {

    @Test
    public void testAddStringParam() throws URISyntaxException, IOException, DocumentException, JAXBException {
        JenkinsHttpClient jhc = new JenkinsHttpClient(new URI("http://localhost/jenkins/"), "", "");
        JenkinsServer js = new JenkinsServer(jhc);
        String configXml = js.getJobXml("TestCreateJob");
        JobConfiguration job = new JobConfiguration(configXml);
        System.out.println(job.addStringParam("testPara", "testDesc", "testDefaultValue").asXml());
    }
}
