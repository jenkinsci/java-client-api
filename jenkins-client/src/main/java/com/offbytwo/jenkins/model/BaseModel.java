/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.client.JenkinsHttpClient;

/**
 * The base model.
 *
 */
public class BaseModel {

    //TODO: We should make this private
    protected JenkinsHttpClient client;

    public JenkinsHttpClient getClient() {
        return client;
    }

    public void setClient(JenkinsHttpClient client) {
        this.client = client;
    }
}
