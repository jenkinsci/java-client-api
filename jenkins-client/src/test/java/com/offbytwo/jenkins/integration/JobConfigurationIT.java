package com.offbytwo.jenkins.integration;

import com.offbytwo.jenkins.model.JobConfiguration;
import hudson.model.FreeStyleProject;
import org.dom4j.DocumentException;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class JobConfigurationIT extends BaseForIntegrationTests {

    public static final String TEST_JOB = "TestCreateJob";
    public static final String TEST_PARA = "testPara";
    public static final String TEST_DESC = "testDesc";
    public static final String TEST_DEFAULT_VALUE = "testDefaultValue";

    @Test
    public void shouldAddStringParamToGivenJob()
            throws URISyntaxException, IOException, DocumentException, JAXBException {
        // given
        jenkinsRule.getInstance().createProject(FreeStyleProject.class, TEST_JOB);
        String configXml = jenkinsServer.getJobXml(TEST_JOB);
        JobConfiguration job = new JobConfiguration(configXml);

        // when
        job.addStringParam(TEST_PARA, TEST_DESC, TEST_DEFAULT_VALUE);

        // then
        assertTrue(job.asXml().contains(TEST_PARA));
        assertTrue(job.asXml().contains(TEST_DESC));
        assertTrue(job.asXml().contains(TEST_DEFAULT_VALUE));
    }
}
