/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.client;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.offbytwo.jenkins.model.BaseModel;

public class JenkinsHttpClient {

    private Auth auth;

    private URL jenkinsUrl;

    private HttpContext localContext;

    private ObjectMapper mapper;

    private String context;

    /**
     * Create an unauthenticated Jenkins HTTP client
     */
    public JenkinsHttpClient(URL jenkinsUrl, Auth auth) {
        try {
            this.context = jenkinsUrl.toURI().getPath();
            if (!context.endsWith("/")) {
                context += "/";
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        this.mapper = getDefaultMapper();
        this.auth = auth;
        this.jenkinsUrl = jenkinsUrl;
        if (auth.authentication()) {
            localContext = auth.getContext();
        } else {
            throw new RuntimeException("authentication fails");
        }
    }

    /**
     * Perform a GET request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param cls  class of the response
     * @param <T>  type of the response
     * @return an instance of the supplied class
     * @throws IOException, HttpResponseException
     */
    public <T extends BaseModel> T get(String path, Class<T> cls) throws IOException, HttpResponseException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(new HttpGet(api(path)), localContext);
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
        try {
            return objectFromResponse(cls, response);
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Perform a GET request and parse the response and return a simple string of the content
     *
     * @param path path to request, can be relative or absolute
     * @return the entity text
     * @throws IOException, HttpResponseException
     */
    public String get(String path) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(new HttpGet(api(path)), localContext);
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
        Scanner s = new Scanner(response.getEntity().getContent());
        s.useDelimiter("\\z");
        StringBuffer sb = new StringBuffer();
        while (s.hasNext()) {
            sb.append(s.next());
        }
        return sb.toString();
    }

    /**
     * Perform a GET request and return the response as InputStream
     *
     * @param path path to request, can be relative or absolute
     * @return the response stream
     * @throws IOException, HttpResponseException
     */
    public InputStream getFile(URI path) throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(new HttpGet(path), localContext);
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
        return response.getEntity().getContent();
    }

    /**
     * Perform a POST request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param cls  class of the response
     * @param <R>  type of the response
     * @param <D>  type of the data
     * @return an instance of the supplied class
     * @throws IOException, HttpResponseException
     */
    public <R extends BaseModel, D> R post(String path, D data, Class<R> cls) throws IOException, HttpResponseException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(api(path));
        if (data != null) {
            StringEntity stringEntity = new StringEntity(mapper.writeValueAsString(data), "application/json");
            request.setEntity(stringEntity);
        }
        HttpResponse response = client.execute(request, localContext);
        int status = response.getStatusLine().getStatusCode();

        try {
            if (status < 200 || status >= 300) {
                throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
            }

            if (cls != null) {
                return objectFromResponse(cls, response);
            } else {
                return null;
            }
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Perform a POST request of XML (instead of using json mapper) and return a string rendering of the response entity.
     *
     * @param path     path to request, can be relative or absolute
     * @param xml_data data data to post
     * @return A string containing the xml response (if present)
     * @throws IOException, HttpResponseException
     */
    public String post_xml(String path, String xml_data) throws IOException, HttpResponseException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(api(path));
        if (xml_data != null) {
            request.setEntity(new StringEntity(xml_data, ContentType.APPLICATION_XML));
        }
        HttpResponse response = client.execute(request, localContext);
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 300) {
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
        try {
            InputStream content = response.getEntity().getContent();
            Scanner s = new Scanner(content);
            StringBuffer sb = new StringBuffer();
            while (s.hasNext()) {
                sb.append(s.next());
            }
            return sb.toString();
        } finally {
            EntityUtils.consume(response.getEntity());
        }
    }

    /**
     * Perform POST request that takes no parameters and returns no response
     *
     * @param path path to request
     * @throws IOException, HttpResponseException
     */
    public void post(String path) throws IOException, HttpResponseException {
        post(path, null, null);
    }

    private String urlJoin(String path1, String path2) {
        if (!path1.endsWith("/")) {
            path1 += "/";
        }
        if (path2.startsWith("/")) {
            path2 = path2.substring(1);
        }
        return path1 + path2;
    }

    private URI api(String path) {
        if (!path.toLowerCase().matches("https?://.*")) {
            path = urlJoin(this.context, path);
        }
        if (!path.contains("?")) {
            path = urlJoin(path, "api/json");
        } else {
            String[] components = path.split("\\?", 2);
            path = urlJoin(components[0], "api/json") + "?" + components[1];
        }
        URI requestUri = null;
        try {
            requestUri = jenkinsUrl.toURI().resolve("/").resolve(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return requestUri;
    }

    private <T extends BaseModel> T objectFromResponse(Class<T> cls, HttpResponse response) throws IOException {
        InputStream content = response.getEntity().getContent();
        T result = mapper.readValue(content, cls);
        result.setClient(this);
        return result;
    }

    private ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        mapper.setDeserializationConfig(deserializationConfig.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        return mapper;
    }


}
