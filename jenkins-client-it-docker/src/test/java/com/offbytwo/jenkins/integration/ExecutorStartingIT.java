package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.ComputerWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

@Test(dependsOnGroups = { Groups.NO_EXECUTOR_GROUP_FOLDER }, groups = { Groups.EXECUTOR_STARTING_GROUP })
public class ExecutorStartingIT extends AbstractJenkinsIntegrationCase {

    private JobWithDetails job;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        job = jenkinsServer.getJob("test");
        assertThat(job).isNotNull();
    }

    @Test
    public void shouldTriggerJobTest() throws IOException {
        ComputerWithDetails computerWithDetails = jenkinsServer.getComputerSet().getComputers().get(0);
        computerWithDetails.toggleOffline();

        ComputerWithDetails computerWithDetailsAfterStarting = jenkinsServer.getComputerSet().getComputers().get(0);

        assertThat(computerWithDetailsAfterStarting.getOffline()).isFalse();
    }

}
