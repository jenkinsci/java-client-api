/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

public class Build extends BaseModel {

    int number;
    String url;

    public Build() {}

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

    public String getUrl() {
        return url;
    }

    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }
	
	public String Stop() throws HttpResponseException, IOException {
        return client.get(url + "stop");
    }
	
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Build build = (Build) o;

        if (number != build.number) return false;
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
