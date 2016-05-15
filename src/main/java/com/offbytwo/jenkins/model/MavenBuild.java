package com.offbytwo.jenkins.model;

import java.io.IOException;

public class MavenBuild extends Build {

    public MavenBuild() {
    }

    public MavenBuild(Build from) {
        this(from.getNumber(), from.getUrl());
    }

    public MavenBuild(int number, String url) {
        super(number, url);
    }

    public MavenModule getMavenModule() throws IOException {
        return client.get(this.getUrl() + "/mavenArtifacts/", MavenModule.class);
    }

    public TestReport getTestReport() throws IOException {
        return client.get(this.getUrl() + "/testReport/?depth=1", TestReport.class);
    }
    
    public TestNGReport getTestNGReport() throws IOException {
        return client.get(this.url + "/testngreports/", TestNGReport.class);
    }
    
    public JacocoCoverageReport getJacocoCodeCoverageReport() throws IOException {
        return client.get(this.url + "/jacoco/", JacocoCoverageReport.class);
    }
}
