package com.offbytwo.jenkins.integration;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Job;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedTrigger extends AbstractJenkinsIntegrationCase {

    private Job pluginManager;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        pluginManager = jenkinsServer.getJob("test");
    }

    @Test
    public void buildWillStartABuildButWillNotBeingExecuted() throws IOException {
        pluginManager.build();
    }
}
