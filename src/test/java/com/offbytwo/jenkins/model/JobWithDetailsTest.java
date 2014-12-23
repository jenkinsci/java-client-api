package com.offbytwo.jenkins.model;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class JobWithDetailsTest {

    @Test
    public void shouldReturnNullBuildsIfJobWithDetailsDoesntHaveAnyBuildsYet() {
        // given
        JobWithDetails job = givenNewJobWithoutAnyBuilds();

        // when
        Build lastBuild = job.getLastBuild();
        Build lastCompletedBuild = job.getLastCompletedBuild();
        Build lastFailedBuild = job.getLastFailedBuild();
        Build lastStableBuild = job.getLastStableBuild();
        Build lastSuccessfulBuild = job.getLastSuccessfulBuild();
        Build lastUnstableBuild = job.getLastUnstableBuild();
        Build lastUnsuccessfulBuild = job.getLastUnsuccessfulBuild();

        // then
        assertNull(lastBuild);
        assertNull(lastCompletedBuild);
        assertNull(lastFailedBuild);
        assertNull(lastStableBuild);
        assertNull(lastSuccessfulBuild);
        assertNull(lastUnstableBuild);
        assertNull(lastUnsuccessfulBuild);
    }

    private JobWithDetails givenNewJobWithoutAnyBuilds() {
        return new JobWithDetails();
    }
}