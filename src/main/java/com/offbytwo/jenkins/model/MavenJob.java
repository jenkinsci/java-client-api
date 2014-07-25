package com.offbytwo.jenkins.model;

import java.io.IOException;

public class MavenJob extends Job {
    
    public MavenJob() {

    }
    
    public MavenJob(String name, String url) {
        super(name, url);
    }
    
    public MavenJobWithDetails mavenDetails() throws IOException {
        return client.get(getUrl(), MavenJobWithDetails.class);
    }

}
