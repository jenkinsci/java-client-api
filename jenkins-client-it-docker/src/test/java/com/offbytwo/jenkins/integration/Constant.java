package com.offbytwo.jenkins.integration;

import java.net.URI;

public final class Constant {

    /**
     * The URL for the running Jenkins server (currently a Docker image). On
     * travis it is localhost (127.0.0.1) on my machine it is different. At the
     * moment it is solved by a profile in pom..but could that somehow
     * identified by docker itself ?
     */
    public static final URI JENKINS_URI = URI.create(System.getProperty("docker.container.network"));

}
