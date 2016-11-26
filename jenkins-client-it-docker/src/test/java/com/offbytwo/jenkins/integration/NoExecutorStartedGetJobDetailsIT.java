package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.BuildCause;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetJobDetailsIT extends AbstractJenkinsIntegrationCase {

    private JobWithDetails job;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        job = jenkinsServer.getJob("test");
    }

    @Test
    public void shouldCheckTheBuildCause() throws IOException {
        BuildWithDetails details = job.getFirstBuild().details();
        List<BuildCause> causes = details.getCauses();
        assertThat(causes).hasSize(1);
        BuildCause buildCause = causes.get(0);

        assertThat(buildCause.getShortDescription()).isEqualTo("Started by user anonymous");
        assertThat(buildCause.getUserName()).isEqualTo("anonymous");
        assertThat(buildCause.getUpstreamBuild()).isEqualTo(0);
        assertThat(buildCause.getUpstreamProject()).isNull();
        assertThat(buildCause.getUserId()).isNull();

    }

}
