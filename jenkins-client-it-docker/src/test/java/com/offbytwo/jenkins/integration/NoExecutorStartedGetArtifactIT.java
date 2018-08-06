package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.BaseModel;
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
        build.details().getArtifacts().forEach(a -> assertArtifact(a));
    }

    private void assertArtifact(Artifact artifact) {
        assertBaseModel(artifact);
        assertBaseModel(artifact.getBuildWithDetails());
        assertBaseModel(artifact.getBuildWithDetails().getBuild());
        assertBaseModel(artifact.getBuildWithDetails().getBuild().getJobWithDetails());
        assertBaseModel(artifact.getBuildWithDetails().getBuild().getJobWithDetails().getJob());
        assertThat(artifact.getBuildWithDetails().getBuild().getJobWithDetails().getJenkinsServer()).isNotNull();
        assertThat(artifact.getBuildWithDetails().getBuild().getJobWithDetails().getJob().getJenkinsServer()).isNotNull();
    }

    private void assertBaseModel(BaseModel baseModel) {
        assertThat(baseModel).isNotNull();
        // TODO: this will work once #337 is fixed: assertThat(baseModel.getClient()).isNotNull();
    }

}
