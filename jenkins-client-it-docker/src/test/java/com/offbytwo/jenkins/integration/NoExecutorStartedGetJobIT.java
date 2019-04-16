package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetJobIT extends AbstractJenkinsIntegrationCase {

    private JobWithDetails job;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        job = jenkinsServer.getJob("test");
    }

    @Test
    public void getBuildsShouldContainOnlyASingleBuild() {
        assertThat(job.getBuilds()).hasSize(1);
    }

    private void checkJob(BuildWithDetails details) throws IOException {
        assertThat(details.getResult()).isEqualTo(BuildResult.SUCCESS);
        assertThat(details.isBuilding()).isFalse();
        assertThat(details.getNumber()).isEqualTo(1);
        assertThat(details.getQueueId()).isEqualTo(1);
        assertThat(details.getChangeSet()).isNotNull();
        // FIXME: Currently getActions is only a plain List should be improved.
        assertThat(details.getActions()).isNotNull();
        assertThat(details.getFullDisplayName()).isEqualTo("test #1");
        assertThat(details.getDescription()).isNull();
        assertThat(details.getId()).isEqualTo("1");
        // FIXME: Think hard about this, cause this is only valid for this
        // special case which is committed in the current state of the git
        // repository
        // for this job.
        assertThat(details.getDuration()).isEqualTo(236);
        assertThat(details.getEstimatedDuration()).isEqualTo(236);

        String[] expectedOutputLines = { "Started by user anonymous",
                "Building in workspace /var/jenkins_home/jobs/test/workspace",
                "[workspace] $ /bin/sh -xe /tmp/hudson2556403647634111927.sh", "+ echo test", "test",
                "Finished: SUCCESS", "" };
        String expectedOutput = String.join("\r\n", expectedOutputLines);
        // Hint: It looks like the consoleOutputText contains CR+LF
        String resultingOutput = details.getConsoleOutputText();
        assertThat(resultingOutput).isEqualTo(expectedOutput);
    }

    @Test
    public void getFirstBuildShouldNotBeNull() throws IOException {
        assertThat(job.getFirstBuild()).isNotNull();
        checkJob(job.getFirstBuild().details());
    }

    @Test
    public void getLastBuildShouldNotBeNull() throws IOException {
        assertThat(job.getLastBuild()).isNotNull();
        checkJob(job.getLastBuild().details());
    }

    @Test
    public void getLastCompletedBuldShouldNotBeNull() throws IOException {
        assertThat(job.getLastCompletedBuild()).isNotNull();
        checkJob(job.getLastCompletedBuild().details());
    }

    @Test
    public void getLastFailedBuildShouldBeBUILD_HAS_NEVER_RAN() {
        assertThat(job.getLastFailedBuild()).isEqualTo(Build.BUILD_HAS_NEVER_RUN);
    }

    @Test
    public void hasLastBuildShouldBeTrue() {
        assertThat(job.hasLastBuildRun()).isTrue();
    }

    @Test
    public void hasLastFailedBuildShouldBeFalse() {
        assertThat(job.hasLastFailedBuildRun()).isFalse();
    }

    @Test
    public void getLastStableBuildShouldNotBeNull() throws IOException {
        assertThat(job.getLastStableBuild()).isNotNull();
        checkJob(job.getLastStableBuild().details());
    }

    @Test
    public void getLastSuccessfulBuildShouldNotBeNull() throws IOException {
        assertThat(job.getLastSuccessfulBuild()).isNotNull();
        checkJob(job.getLastSuccessfulBuild().details());
    }

    @Test
    public void getLastUnstableBuildShouldBeBUILD_HAS_NEVER_RAN() {
        assertThat(job.getLastUnstableBuild()).isEqualTo(Build.BUILD_HAS_NEVER_RUN);
    }

    @Test
    public void hasLastUnstableBuildShouldBeFalse() {
        assertThat(job.hasLastUnstableBuildRun()).isFalse();
    }

    @Test
    public void getLastUnsuccessfulBuildShouldBeBUILD_HAS_NEVER_RAN() {
        assertThat(job.getLastUnsuccessfulBuild()).isEqualTo(Build.BUILD_HAS_NEVER_RUN);
    }

    @Test
    public void hasLastUnsuccessfulBuildShouldBeFalse() {
        assertThat(job.hasLastUnsuccessfulBuildRun()).isFalse();
    }

    @Test
    public void getDescriptionShouldRetrunTheDescription() {
        assertThat(job.getDescription()).isEqualTo("This is the description with umlauts äöü");
    }

}
