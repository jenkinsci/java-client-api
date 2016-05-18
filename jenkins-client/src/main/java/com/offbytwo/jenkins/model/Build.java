/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;

public class Build extends BaseModel {

    /**
     * This will be returned by the API in cases where no build has ever 
     * been executed like {@link JobWithDetails#getLastBuild()} etc.
     */
    public static final Build BUILD_HAS_NEVER_RAN = new Build (-1, -1, "UNKNOWN");

    private int number;
    private int queueId;
    private String url;

    private Build(int number, int queueId, String url) {
        super();
        this.number = number;
        this.queueId = queueId;
        this.url = url;
    }
    
    public Build() {
    }

    public Build(Build from) {
        this(from.getNumber(), from.getUrl());
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public int getQueueId() {
        return queueId;
    }

    public String getUrl() {
        return url;
    }

    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }

    /*
     * This Change (Bad Practice) is due to inconsistencies in Jenkins various
     * versions. Jenkins changed their API from post to get and from get to post
     * and so on. For example version 1.565 is supporting stop method as a post
     * call, version 1.609.1 support it as a get call and version 1.609.2
     * support it as a post call Thus, when a get is not allowed, an 405 error
     * message is generated "Method Not Allowed" and the stop isn't executed (a
     * post is required). Changed the code in order to do a post call in case a
     * get is not allowed.
     */

    public String Stop() throws HttpResponseException, IOException {
        try {

            return client.get(url + "stop");
        } catch (IOException ex) {
            if (((HttpResponseException) ex).getStatusCode() == 405) {
                stopPost();
                return "";
            }
        }
        return "";
    }

    private void stopPost() throws HttpResponseException, IOException {
        client.post(url + "stop");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Build build = (Build) o;

        if (number != build.number)
            return false;
        if (url != null ? !url.equals(build.url) : build.url != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }
}
