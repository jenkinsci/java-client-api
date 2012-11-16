/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

public class Build extends BaseModel {
    int number;
    String url;

    public Build() {
    }

    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }
}
