package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetJobXmlIT extends AbstractJenkinsIntegrationCase {

    private String jobXml;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        jobXml = jenkinsServer.getJobXml("test");
    }

    //@formatter:off
    private static final String[] CONFIG_XML = { 
        "<?xml version='1.0' encoding='UTF-8'?>", 
        "<project>", 
        "  <actions/>",
        "  <description>This is the description with umlauts äöü</description>",
        "  <keepDependencies>false</keepDependencies>", 
        "  <properties/>", 
        "  <scm class=\"hudson.scm.NullSCM\"/>",
        "  <canRoam>true</canRoam>", 
        "  <disabled>false</disabled>",
        "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>",
        "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>", 
        "  <triggers/>",
        "  <concurrentBuild>false</concurrentBuild>", 
        "  <builders>", 
        "    <hudson.tasks.Shell>",
        "      <command>echo &quot;test&quot;</command>", 
        "    </hudson.tasks.Shell>", 
        "  </builders>",
        "  <publishers/>", 
        "  <buildWrappers/>", 
        "</project>" 
    };
    //@formatter:on

    @Test
    public void getJobXmlShouldReturnTheExpectedConfigXml() {
        String expectedXml = String.join("\n", CONFIG_XML);
        assertThat(jobXml).isEqualTo(expectedXml);
    }

}
