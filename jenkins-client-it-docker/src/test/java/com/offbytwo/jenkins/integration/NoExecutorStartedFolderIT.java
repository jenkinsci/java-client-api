package com.offbytwo.jenkins.integration;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { Groups.NO_EXECUTOR_GROUP }, groups = { Groups.NO_EXECUTOR_GROUP_FOLDER })
public class NoExecutorStartedFolderIT extends AbstractJenkinsIntegrationCase {

    @BeforeMethod
    public void beforeMethod() throws IOException {
    }

    @Test
    public void createFolderShouldCreateTheFolder() throws IOException {
        jenkinsServer.createFolder("First-Folder");
    }

}
