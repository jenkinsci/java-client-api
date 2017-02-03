package com.offbytwo.jenkins.integration;

import java.net.URI;

public final class Constant {

    /**
     * The URL for the running Jenkins server (currently a Docker image). On
     * travis it is localhost (127.0.0.1).
     */
    public static final URI JENKINS_URI = URI.create("http://127.0.0.1:8080/");

}
