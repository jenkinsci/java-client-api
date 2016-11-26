package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Job;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetJobsIT extends AbstractJenkinsIntegrationCase {

    private Map<String, Job> jobs;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        jobs = jenkinsServer.getJobs();
    }

    @Test
    public void numberOfJobsIsEqualOne() {
        assertThat(jobs).hasSize(1);
    }

    @Test
    public void getNameShouldReturnTest() {
        String key = jobs.keySet().iterator().next();
        assertThat(jobs.get(key).getName()).isEqualTo("test");
    }

}
