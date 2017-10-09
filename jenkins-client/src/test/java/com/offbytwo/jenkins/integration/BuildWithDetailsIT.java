package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildCause;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class BuildWithDetailsIT {

    private final String JENKINS_WITH_DETAILS_TEST_JOB = "build_with_details";

    private JenkinsServer server;

    @Rule
    public JenkinsRule jenkinsRule = new JenkinsRule();

    @Before
    public void setUp() throws Exception {
        jenkinsRule.getInstance().getCrumbIssuer().getCrumb();
        jenkinsRule.getInstance().getCrumbIssuer().getCrumbRequestField();
        server = new JenkinsServer(jenkinsRule.getURL().toURI());
    }

    @Test
    public void checkCauses() throws Exception {
        FreeStyleProject pr = jenkinsRule.getInstance().createProject(FreeStyleProject.class,
                JENKINS_WITH_DETAILS_TEST_JOB);

        pr.scheduleBuild2(0, new Cause.UserIdCause(),
                new ParametersAction(new StringParameterValue("BUILD NUMBER", "FirstBuild"))).get();

        JobWithDetails job = server.getJobs().get(JENKINS_WITH_DETAILS_TEST_JOB).details();
        BuildWithDetails build = job.getBuilds().get(0).details();
        assertEquals(BuildResult.SUCCESS, build.getResult());

        List<BuildCause> causes = build.getCauses();
        assertNotNull(causes);

        assertThat(build.getBuiltOn()).isEmpty();
    }
    
    
    
    @Test
    public void testGetBuildDetails_FullDetails() throws Exception {
        final FreeStyleProject proj = jenkinsRule.createFreeStyleProject();
        final FreeStyleBuild build = proj.scheduleBuild2(0, new Cause.UserIdCause()).get();
        final BuildWithDetails details = server.getBuildDetails(proj.getName(), build.number);
        assertEquals(BuildResult.SUCCESS, details.getResult());
        assertNotNull(details.getFullDisplayName());
        assertNotNull(details.getActions());
        assertNotNull(details.getUrl());
    }
    
    
    @Test
    public void testGetBuildDetails_SomeDetails() throws Exception {
        final FreeStyleProject proj = jenkinsRule.createFreeStyleProject();
        final FreeStyleBuild build = proj.scheduleBuild2(0, new Cause.UserIdCause()).get();
        final String[] treeProps = {"building", "result", "actions[causes]"};
        BuildWithDetails details = server.getBuildDetails(proj.getName(), build.number, treeProps);
        assertEquals(BuildResult.SUCCESS, details.getResult());
        assertFalse(details.isBuilding());
        assertNull(details.getFullDisplayName());
        assertNotNull(details.getActions());
        assertNull(details.getUrl());
    }
    


}
