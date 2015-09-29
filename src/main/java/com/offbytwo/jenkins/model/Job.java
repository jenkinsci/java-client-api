/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;

import java.io.IOException;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.join;

public class Job extends BaseModel {

    private String name;
    private String url;

    public Job() {}

    public Job(String name, String url) {
        this();
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public JobWithDetails details() throws IOException {
        return client.get(url, JobWithDetails.class);
    }

    /**
     * Trigger a build without parameters
     */
    public void build() throws IOException {
        client.post(url + "build", true);
    }

    public void build(boolean crumbFlag) throws IOException {
        client.post(url + "build", crumbFlag);
    }

    /**
     * Trigger a parameterized build
     *
     * @param params the job parameters
     * @throws IOException
     */
    public void build(Map<String, String> params) throws IOException {
        String qs = join(Collections2.transform(params.entrySet(), new MapEntryToQueryStringPair()), "&");
        client.post(url + "buildWithParameters?" + qs, null, null);
    }

    /**
     * Trigger a parameterized build
     *
     * @param params the job parameters
     * @param crumbFlag determines whether crumb flag is used
     * @throws IOException
     */
    public void build(Map<String, String> params, boolean crumbFlag) throws IOException {
        String qs = join(Collections2.transform(params.entrySet(), new MapEntryToQueryStringPair()), "&");
        client.post(url + "buildWithParameters?" + qs, null, null, crumbFlag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (name != null ? !name.equals(job.name) : job.name != null)
            return false;
        if (url != null ? !url.equals(job.url) : job.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    private static class MapEntryToQueryStringPair implements Function<Map.Entry<String, String>, String> {
        @Override
        public String apply(Map.Entry<String, String> entry) {
            Escaper escaper = UrlEscapers.urlFormParameterEscaper();
            return escaper.escape(entry.getKey()) + "=" + escaper.escape(entry.getValue());
        }
    }
}
