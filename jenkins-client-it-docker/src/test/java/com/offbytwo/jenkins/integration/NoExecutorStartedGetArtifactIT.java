package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.Build;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetArtifactIT extends AbstractJenkinsIntegrationCase {

    private Build build;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        build = jenkinsServer.getJob("test").getBuildByNumber(1);
    }

    @Test
    public void getBuildShouldContainTwoArtifacts() throws IOException {
        assertThat(build.details().getArtifacts()).hasSize(2);
    }

    @Test
    public void traverseFromArtifactToJenkinsServerShouldNotFail() throws IOException {
        Artifact artifact = build.details().getArtifacts().get(0);
        assertThat(artifact).isNotNull();
        assertThat(artifact.getBuildWithDetails()
                           .getBuild()
                           .getJobWithDetails()
                           .getJob()
                           .getJenkinsServer())
                    .isNotNull();
    }

}
