/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

public class BuildJobWithDetails {

    @Test
    public void shouldAddStringParamToAnExistingJob() throws IOException {
        JenkinsServer js = new JenkinsServer(URI.create("http://192.168.99.100:8080/"));
        // JenkinsServer js = new
        // JenkinsServer(URI.create("http://localhost:10090/"));
        JobWithDetails job = js.getJob("test");

        List<Build> builds = job.getBuilds();

        /*
         *
         * 
         * http://192.168.99.100:8080/job/test/api/json?tree=allBuilds[number[*]
         * ,id[*],url[*],queueId[*]]&pretty
         * 
         * http://192.168.99.100:8080/job/test/api/json?tree=allBuilds[number[*]
         * ,id[*],url[*],queueId[*]]{180,220}&pretty
         * 
         */
        System.out.println("Build: " + builds.size());

        Build firstBuild = job.getFirstBuild();
        Build lastBuild = job.getLastBuild();

        System.out.println("FirstBuild: " + firstBuild.getNumber());
        System.out.println("LastBuild: " + lastBuild.getNumber());

        List<Build> allBuilds = job.getAllBuilds();
        System.out.println("All Build: " + allBuilds.size());

        Build lastBuildEntry = allBuilds.get(allBuilds.size() - 1);
        BuildWithDetails details = lastBuildEntry.details();

    }
}
