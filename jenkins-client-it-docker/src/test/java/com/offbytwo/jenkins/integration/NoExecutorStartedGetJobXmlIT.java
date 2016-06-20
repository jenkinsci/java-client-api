package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Joiner;

@Test( groups = { Groups.NO_EXECUTOR_GROUP } )
public class NoExecutorStartedGetJobXmlIT
    extends AbstractJenkinsIntegrationCase
{

    private String jobXml;

    @BeforeMethod
    public void beforeMethod()
        throws IOException
    {
        jobXml = jenkinsServer.getJobXml( "test" );
    }
    
    private static final String[] CONFIG_XML = {
        "<?xml version='1.0' encoding='UTF-8'?>",
        "<project>",
        "  <actions/>",
        "  <description>This is the description with umlauts äöü</description>",
        "  <keepDependencies>false</keepDependencies>",
        "  <properties>",
        "    <hudson.plugins.throttleconcurrents.ThrottleJobProperty plugin=\"throttle-concurrents@1.8.4\">",
        "      <maxConcurrentPerNode>0</maxConcurrentPerNode>",
        "      <maxConcurrentTotal>0</maxConcurrentTotal>",
        "      <throttleEnabled>false</throttleEnabled>",
        "      <throttleOption>project</throttleOption>",
        "    </hudson.plugins.throttleconcurrents.ThrottleJobProperty>",
        "  </properties>",
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

    @Test
    public void getJobXmlShouldReturnTheExpectedConfigXml()
    {
        String expectedXml = Joiner.on( "\n" ).join( CONFIG_XML );
        assertThat( jobXml ).isEqualTo( expectedXml );
    }

}
