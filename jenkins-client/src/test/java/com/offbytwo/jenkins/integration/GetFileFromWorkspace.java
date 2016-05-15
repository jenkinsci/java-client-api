package com.offbytwo.jenkins.integration;

import java.io.IOException;
import java.net.URI;

import org.junit.Test;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.MavenJobWithDetails;

public class GetFileFromWorkspace
{

    @Test
    public void shouldAddStringParamToAnExistingJob()
        throws IOException
    {
        JenkinsServer js = new JenkinsServer( URI.create( "http://localhost:10090/" ) );
        // JenkinsServer js = new JenkinsServer(URI.create("http://ci.soebes.de:8080/"));
        // MavenJobWithDetails mavenJob = js.getMavenJob("javaee");

        // String s = js.getFileFromWorkspace( "test-maven", "README.md" );

        MavenJobWithDetails mavenJob = js.getMavenJob( "test-maven" );
        //
        String s = mavenJob.getFileFromWorkspace( "ngst-web/target/2016.04.27/CONSOLE_2016.04.27.21.23.17.log" );

        System.out.println( "Content: " + s );

    }

}
