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
        return client.get(this.url + "/mavenArtifacts/", MavenModule.class);
    }
    
}
 