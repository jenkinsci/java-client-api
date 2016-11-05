package com.offbytwo.jenkins.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class JobWithDetailsTest {

    private JobWithDetails job;

    @Before
    public void setUp() {
        job = givenNewJobWithoutAnyBuilds();
    }

    private JobWithDetails givenNewJobWithoutAnyBuilds() {
        return new JobWithDetails();
    }

    private void failIfNotBuildHasNeverRanReturned(Build build) {
        assertThat(build).isEqualTo(Build.BUILD_HAS_NEVER_RUN);
    }

    @Test
    public void getLastStableBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastStableBuild());
    }

    @Test
    public void getLastBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastBuild());
    }

    @Test
    public void getLastCompletedBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastCompletedBuild());
    }

    @Test
    public void getLastFailedBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastFailedBuild());
    }

    @Test
    public void getLastSuccessfulBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastSuccessfulBuild());
    }

    @Test
    public void getLastUnstableBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastUnstableBuild());
    }

    @Test
    public void getLastUnsuccessfulBuildShouldReturnBuildHasNeverRan() {
        failIfNotBuildHasNeverRanReturned(job.getLastUnsuccessfulBuild());
    }

    @Test
    public void getDownstreamProjectsShouldReturnEmptyList() {
        assertThat(job.getDownstreamProjects()).isEqualTo(Collections.emptyList());
    }

    @Test
    public void getUpstreamProjectsShouldReturnEmptyList() {
        assertThat(job.getUpstreamProjects()).isEqualTo(Collections.emptyList());
    }
}