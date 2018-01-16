/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

public class Job extends BaseModel {

    private String name;
    private String url;
    private String fullName;

    public Job() {
    }

    public Job(String name, String url) {
        this();
        this.name = name;
        this.url = url;
        this.fullName = null;
    }

    public Job(String name, String url, String fullName) {
        this();
        this.name = name;
        this.url = url;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getFullName() {
        return fullName;
    }

    public JobWithDetails details() throws IOException {
        return client.get(url, JobWithDetails.class);
    }

    /**
     * Get a file from workspace.
     *
     * @param fileName The name of the file to download from workspace. You can
     *                 also access files which are in sub folders of the workspace.
     * @return The string which contains the content of the file.
     * @throws IOException in case of an error.
     */
    public String getFileFromWorkspace(String fileName) throws IOException {
        InputStream is = client.getFile(URI.create(url + "/ws/" + fileName));
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    /**
     * Trigger a build without parameters
     *
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build() throws IOException {
        ExtractHeader location = client.post(url + "build", null, ExtractHeader.class, false);
        return new QueueReference(location.getLocation());

    }

    /**
     * Trigger a build with crumbFlag.
     *
     * @param crumbFlag true or false.
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(boolean crumbFlag) throws IOException {
        ExtractHeader location = client.post(url + "build", null, ExtractHeader.class, crumbFlag);
        return new QueueReference(location.getLocation());
    }

    /**
     * Trigger a parameterized build
     *
     * @param params the job parameters
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params) throws IOException {
        return build(params, false);
    }

    /**
     * Trigger a parameterized build
     *
     * @param params    the job parameters
     * @param crumbFlag determines whether crumb flag is used
     * @return {@link QueueReference} for further analysis of the queued build.
     * @throws IOException in case of an error.
     */
    public QueueReference build(Map<String, String> params, boolean crumbFlag) throws IOException {
        List<NameValuePair> paramsList = new ArrayList<>(Collections2.transform(params.entrySet(),
                new MapEntryToNameValuePair()));
        HttpResponse response = this.client
                .post_form_with_result(this.url + "buildWithParameters", paramsList, crumbFlag);
        String location = response.getFirstHeader("Location").getValue();
        EntityUtils.consume(response.getEntity());
        return new QueueReference(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Job job = (Job) o;

        if (name != null ? !name.equals(job.name) : job.name != null)
            return false;
        if (url != null ? !url.equals(job.url) : job.url != null)
            return false;
        if (fullName != null ? !fullName.equals(job.fullName) : job.fullName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0) + (fullName != null ? fullName.hashCode() : 0);
        return result;
    }

    private static class MapEntryToNameValuePair implements Function<Map.Entry<String, String>, NameValuePair> {
        @Override
        public NameValuePair apply(Map.Entry<String, String> entry) {
            return new BasicNameValuePair(entry.getKey(), entry.getValue());
        }
    }
}
