/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.client.JenkinsHttpClient;

public class BaseModel {
    JenkinsHttpClient client;

    public JenkinsHttpClient getClient() {
        return client;
    }

    public void setClient(JenkinsHttpClient client) {
        this.client = client;
    }
}
