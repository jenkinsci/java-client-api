/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

public class Job extends BaseModel {
    private String name;
    private String url;

    public Job() {
    }

    public Job(String name, String url) {
        this();
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JobWithDetails details() throws IOException {
        return client.get(url, JobWithDetails.class);
    }

    /**
     * Trigger a build without parameters
     */
    public void build() throws IOException {
        client.post(url + "build", null, null);
    }
}
