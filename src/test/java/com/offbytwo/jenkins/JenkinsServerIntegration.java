/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Computer;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.offbytwo.jenkins.client.JenkinsHttpClient;

public class JenkinsServerIntegration {

    private JenkinsHttpClient client;
    private JenkinsServer server;
    private ExecutorService executor;

    @Before
    public void setUp() throws Exception {
        client = new JenkinsHttpClient(new URI("http://localhost:8080"));
        server = new JenkinsServer(client);
        executor = Executors.newCachedThreadPool();
    }

    @Test
    public void shouldReturnListOfJobs() throws Exception {
        assertTrue(server.getJobs().containsKey("trunk"));
    }

    @Test
    public void shouldReturnBuildsForJob() throws Exception {
        JobWithDetails job = server.getJobs().get("trunk").details();
        assertEquals(5, job.getBuilds().get(0).getNumber());
    }

    @Test
    public void shouldReturnBuildStatusForBuild() throws Exception {
        JobWithDetails job = server.getJobs().get("pr").details();
        BuildWithDetails build = job.getBuilds().get(0).details();
        assertEquals(BuildResult.SUCCESS, build.getResult());
        assertEquals("foobar", build.getParameters().get("REVISION"));
    }

    @Test
    public void shouldReturnListOfComputers() throws Exception {
        assertTrue(server.getComputers().containsKey("master"));
    }

    @Test
    public void shouldReturnDetailOfComputer() throws Exception {
        Map<String, Computer> computers =  server.getComputers();
        assertTrue(computers.get("master").details().getDisplayName().equals("master"));
    }

    @Test
    public void shouldReturnDetailOfLablel() throws Exception {
        assertTrue(server.getLabel("master").getName().equals("master"));
    }

    // Note this test depends upon the xml in job-template.xml being a valid job
    // description for the instance of jenkins you are running.
    @Test
    public void testGetJobXml() throws Exception {
        final String jobName = "pr";

        String xmlReturned = server.getJobXml(jobName);

        assertTrue(xmlReturned.length() > 0);
    }

    @Test
    public void testGetJobByName() throws Exception {
        final String jobName = "trunk";

        JobWithDetails job = server.getJob(jobName);

        assertEquals("trunk",job.getName());
        assertEquals("trunk",job.getDisplayName());
    }

    @Test
    public void testGetJobByNameDoesntExist() throws Exception {
        final String jobName = "imprettysurethereisnojobwiththisname";

        JobWithDetails job = server.getJob(jobName);

        assertEquals(null, job);
    }

    // Note this test depends upon the "pr" job existing and successfully building
    @Test
    public void testCreateJob() throws Exception {
        final String sourceJob = "pr";
        final String jobName = "test-job-" + UUID.randomUUID().toString();

        String sourceXml = server.getJobXml(sourceJob);

        server.createJob(jobName, sourceXml);

        Map<String, Job> jobs = server.getJobs();
        assertTrue(jobs.containsKey(jobName));
        JobWithDetails thisJob = jobs.get(jobName).details();
        assertNotNull(thisJob);
        assertTrue(thisJob.getBuilds().size() == 0);
        thisJob.build(ImmutableMap.of("foo_param", "MUST_PROVIDE_VALUES_DEFAULTS_DONT_WORK"));

        // wait to see if the job finishes, but with a timeout
        Future<Void> future = executor.submit(new PerformPollingTest(server, jobName));

        // If this times out, either jenkins is slow or our test failed!
        // IME, usually takes about 10-15 seconds
        future.get(30, TimeUnit.SECONDS);

        Build b = server.getJobs().get(jobName).details().getLastSuccessfulBuild();
        assertTrue(b != null);
    }

    // Note this test depends upon the "pr" job existing and successfully building
    @Test
    public void testUpdateJob() throws Exception {
        final String sourceJob = "pr";
        final String description = "test-" + UUID.randomUUID().toString();

        String sourceXml = server.getJobXml(sourceJob);
        String newXml = sourceXml.replaceAll("<description>.*</description>", "<description>" + description + "</description>");
        server.updateJob(sourceJob, newXml);

        String confirmXml = server.getJobXml(sourceJob);
        assertTrue(confirmXml.contains(description));

    }

    private class PerformPollingTest implements Callable<Void> {
        private final JenkinsServer server;
        private final String jobName;
        public PerformPollingTest(JenkinsServer server, String jobName) {
            this.server = server;
            this.jobName = jobName;
        }
        public Void call() throws InterruptedException, IOException {
            while(true) {
                Thread.sleep(500);
                JobWithDetails jwd = server.getJobs().get(jobName).details();

                try {
                    // Throws NPE until the first build succeeds
                    jwd.getLastSuccessfulBuild();
                } catch (NullPointerException e) {
                    continue;
                }
                // build succeeded
                return null;
            }
        }
    }
}
