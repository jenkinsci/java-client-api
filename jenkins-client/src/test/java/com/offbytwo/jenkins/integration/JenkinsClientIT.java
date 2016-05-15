package com.offbytwo.jenkins.integration;

import com.offbytwo.jenkins.model.JobWithDetails;
import hudson.model.FreeStyleProject;
import org.dom4j.DocumentException;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class JenkinsClientIT extends BaseForIntegrationTests {

    public static final String TEST_CREATE_JOB = "TestCreateJob";

    @Test
    public void shouldAddStringParamToAnExistingJob() throws JAXBException, IOException, DocumentException {
        // given
        jenkinsRule.getInstance().createProject(FreeStyleProject.class, "TestCreateJob");

        // when
        jenkinsServer.addStringParam(TEST_CREATE_JOB, "paramTest2", "Desc", "1");

        // then
        JobWithDetails testCreateJob = jenkinsServer.getJob(TEST_CREATE_JOB);
        assertNotNull(testCreateJob);
    }
}
