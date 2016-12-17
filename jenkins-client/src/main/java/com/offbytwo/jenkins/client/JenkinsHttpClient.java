/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.client;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.client.util.RequestReleasingInputStream;
//import com.offbytwo.jenkins.client.util.HttpResponseContentExtractor;
import com.offbytwo.jenkins.client.validator.HttpResponseValidator;
import com.offbytwo.jenkins.model.BaseModel;
import com.offbytwo.jenkins.model.Crumb;
import com.offbytwo.jenkins.model.ExtractHeader;

import net.sf.json.JSONObject;

public class JenkinsHttpClient {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private URI uri;
    private CloseableHttpClient client;
    private HttpContext localContext;
    private HttpResponseValidator httpResponseValidator;
    // private HttpResponseContentExtractor contentExtractor;

    private ObjectMapper mapper;
    private String context;

    private String jenkinsVersion;

    /**
     * Create an unauthenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     * @param client Configured CloseableHttpClient to be used
     */
    public JenkinsHttpClient(URI uri, CloseableHttpClient client) {
        this.context = uri.getPath();
        if (!context.endsWith("/")) {
            context += "/";
        }
        this.uri = uri;
        this.mapper = getDefaultMapper();
        this.client = client;
        this.httpResponseValidator = new HttpResponseValidator();
        // this.contentExtractor = new HttpResponseContentExtractor();
        this.jenkinsVersion = null;
        LOGGER.debug("uri={}", uri.toString());
    }

    /**
     * Create an unauthenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     * @param builder Configured HttpClientBuilder to be used
     */
    public JenkinsHttpClient(URI uri, HttpClientBuilder builder) {
        this(uri, builder.build());
    }

    /**
     * Create an unauthenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     */
    public JenkinsHttpClient(URI uri) {
        this(uri, HttpClientBuilder.create());
    }

    /**
     * Create an authenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     * @param username Username to use when connecting
     * @param password Password or auth token to use when connecting
     */
    public JenkinsHttpClient(URI uri, String username, String password) {
        this(uri, addAuthentication(HttpClientBuilder.create(), uri, username, password));
        if (isNotBlank(username)) {
            localContext = new BasicHttpContext();
            localContext.setAttribute("preemptive-auth", new BasicScheme());
        }
    }

    /**
     * Perform a GET request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param cls class of the response
     * @param <T> type of the response
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    public <T extends BaseModel> T get(String path, Class<T> cls) throws IOException {
        HttpGet getMethod = new HttpGet(api(path));

        HttpResponse response = client.execute(getMethod, localContext);
        getJenkinsVersionFromHeader(response);
        try {
            httpResponseValidator.validateResponse(response);
            return objectFromResponse(cls, response);
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(getMethod);
        }
    }

    /**
     * Perform a GET request and parse the response and return a simple string
     * of the content
     *
     * @param path path to request, can be relative or absolute
     * @return the entity text
     * @throws IOException in case of an error.
     */
    public String get(String path) throws IOException {
        HttpGet getMethod = new HttpGet(api(path));
        HttpResponse response = client.execute(getMethod, localContext);
        getJenkinsVersionFromHeader(response);
        LOGGER.debug("get({}), version={}, responseCode={}", path, this.jenkinsVersion,
                response.getStatusLine().getStatusCode());
        try {
            httpResponseValidator.validateResponse(response);
            return IOUtils.toString(response.getEntity().getContent());
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(getMethod);
        }

    }

    /**
     * Perform a GET request and parse the response to the given class, logging
     * any IOException that is thrown rather than propagating it.
     *
     * @param path path to request, can be relative or absolute
     * @param cls class of the response
     * @param <T> type of the response
     * @return an instance of the supplied class
     */
    public <T extends BaseModel> T getQuietly(String path, Class<T> cls) {
        T value;
        try {
            value = get(path, cls);
            return value;
        } catch (IOException e) {
            LOGGER.debug("getQuietly({}, {})", path, cls.getName(), e);
            // TODO: Is returing null a good idea?
            return null;
        }
    }

    /**
     * Perform a GET request and return the response as InputStream
     *
     * @param path path to request, can be relative or absolute
     * @return the response stream
     * @throws IOException in case of an error.
     */
    public InputStream getFile(URI path) throws IOException {
        HttpGet getMethod = new HttpGet(path);
        HttpResponse response = client.execute(getMethod, localContext);
        getJenkinsVersionFromHeader(response);
        httpResponseValidator.validateResponse(response);
        return new RequestReleasingInputStream(response.getEntity().getContent(), getMethod);
    }

    public <R extends BaseModel, D> R post(String path, D data, Class<R> cls) throws IOException {
        return post(path, data, cls, true);
    }

    /**
     * Perform a POST request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param cls class of the response
     * @param <R> type of the response
     * @param <D> type of the data
     * @param crumbFlag true / false.
     * @return an instance of the supplied class
     * @throws IOException in case of an error.
     */
    public <R extends BaseModel, D> R post(String path, D data, Class<R> cls, boolean crumbFlag) throws IOException {
        HttpPost request = new HttpPost(api(path));
        if (crumbFlag == true) {
            Crumb crumb = getQuietly("/crumbIssuer", Crumb.class);
            if (crumb != null) {
                request.addHeader(new BasicHeader(crumb.getCrumbRequestField(), crumb.getCrumb()));
            }
        }

        if (data != null) {
            String value = mapper.writeValueAsString(data);
            StringEntity stringEntity = new StringEntity(value, ContentType.APPLICATION_JSON);
            request.setEntity(stringEntity);
        }
        HttpResponse response = client.execute(request, localContext);
        getJenkinsVersionFromHeader(response);

        try {
            httpResponseValidator.validateResponse(response);

            if (cls != null) {
                R responseObject;
                if (cls.equals(ExtractHeader.class)) {
                    ExtractHeader location = new ExtractHeader();
                    location.setLocation(response.getFirstHeader("Location").getValue());
                    responseObject = (R) location;
                } else {
                    responseObject = objectFromResponse(cls, response);
                }
                return responseObject;
            } else {
                return null;
            }
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(request);
        }
    }

    /**
     * Perform a POST request using form url encoding.
     * 
     * This method was added for the purposes of creating folders, but may be
     * useful for other API calls as well.
     * 
     * Unlike post and post_xml, the path is *not* modified by adding
     * "/api/json". Additionally, the params in data are provided as both
     * request parameters including a json parameter, *and* in the
     * JSON-formatted StringEntity, because this is what the folder creation
     * call required. It is unclear if any other jenkins APIs operate in this
     * fashion.
     *
     * @param path path to request, can be relative or absolute
     * @param data data to post
     * @param crumbFlag true / false.
     * @throws IOException in case of an error.
     */
    public void post_form(String path, Map<String, String> data, boolean crumbFlag) throws IOException {
        HttpPost request;
        if (data != null) {
            // https://gist.github.com/stuart-warren/7786892 was slightly
            // helpful here
            List<String> queryParams = Lists.newArrayList();
            for (String param : data.keySet()) {
                queryParams.add(param + "=" + EncodingUtils.encodeParam(data.get(param)));
            }

            queryParams.add("json=" + EncodingUtils.encodeParam(JSONObject.fromObject(data).toString()));
            String value = mapper.writeValueAsString(data);
            StringEntity stringEntity = new StringEntity(value, ContentType.APPLICATION_FORM_URLENCODED);
            request = new HttpPost(noapi(path) + StringUtils.join(queryParams, "&"));
            request.setEntity(stringEntity);
        } else {
            request = new HttpPost(noapi(path));
        }

        if (crumbFlag == true) {
            Crumb crumb = get("/crumbIssuer", Crumb.class);
            if (crumb != null) {
                request.addHeader(new BasicHeader(crumb.getCrumbRequestField(), crumb.getCrumb()));
            }
        }

        HttpResponse response = client.execute(request, localContext);
        getJenkinsVersionFromHeader(response);

        try {
            httpResponseValidator.validateResponse(response);
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(request);
        }
    }

    /**
     * Perform a POST request of XML (instead of using json mapper) and return a
     * string rendering of the response entity.
     *
     * @param path path to request, can be relative or absolute
     * @param xml_data data data to post
     * @return A string containing the xml response (if present)
     * @throws IOException in case of an error.
     */
    public String post_xml(String path, String xml_data) throws IOException {
        return post_xml(path, xml_data, true);
    }

    public String post_xml(String path, String xml_data, boolean crumbFlag) throws IOException {
        HttpPost request = new HttpPost(api(path));
        if (crumbFlag == true) {
            Crumb crumb = getQuietly("/crumbIssuer", Crumb.class);
            if (crumb != null) {
                request.addHeader(new BasicHeader(crumb.getCrumbRequestField(), crumb.getCrumb()));
            }
        }

        if (xml_data != null) {
            request.setEntity(new StringEntity(xml_data, ContentType.create("text/xml", "utf-8")));
        }
        HttpResponse response = client.execute(request, localContext);
        getJenkinsVersionFromHeader(response);
        httpResponseValidator.validateResponse(response);
        try {
            return IOUtils.toString(response.getEntity().getContent());
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(request);
        }
    }

    /**
     * Post a text entity to the given URL using the default content type
     *
     * @param path The path.
     * @param textData data.
     * @param crumbFlag true/false.
     * @return resulting response
     * @throws IOException in case of an error.
     */
    public String post_text(String path, String textData, boolean crumbFlag) throws IOException {
        return post_text(path, textData, ContentType.DEFAULT_TEXT, crumbFlag);
    }

    /**
     * Post a text entity to the given URL with the given content type
     *
     * @param path The path.
     * @param textData The data.
     * @param contentType {@link ContentType}
     * @param crumbFlag true or false.
     * @return resulting response
     * @throws IOException in case of an error.
     */
    public String post_text(String path, String textData, ContentType contentType, boolean crumbFlag)
            throws IOException {
        HttpPost request = new HttpPost(api(path));
        if (crumbFlag == true) {
            Crumb crumb = get("/crumbIssuer", Crumb.class);
            if (crumb != null) {
                request.addHeader(new BasicHeader(crumb.getCrumbRequestField(), crumb.getCrumb()));
            }
        }

        if (textData != null) {
            request.setEntity(new StringEntity(textData, contentType));
        }
        HttpResponse response = client.execute(request, localContext);
        getJenkinsVersionFromHeader(response);
        httpResponseValidator.validateResponse(response);
        try {
            return IOUtils.toString(response.getEntity().getContent());
        } finally {
            EntityUtils.consume(response.getEntity());
            releaseConnection(request);
        }
    }

    /**
     * Perform POST request that takes no parameters and returns no response
     *
     * @param path path to request
     * @throws IOException in case of an error.
     */
    public void post(String path) throws IOException {
        post(path, null, null, false);
    }

    public void post(String path, boolean crumbFlag) throws IOException {
        post(path, null, null, crumbFlag);
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
        return uri.resolve("/").resolve(path.replace(" ", "%20"));
    }

    private URI noapi(String path) {
        if (!path.toLowerCase().matches("https?://.*")) {
            path = urlJoin(this.context, path);
        }
        return uri.resolve("/").resolve(path);
    }

    private <T extends BaseModel> T objectFromResponse(Class<T> cls, HttpResponse response) throws IOException {
        InputStream content = response.getEntity().getContent();
        byte[] bytes = ByteStreams.toByteArray(content);
        T result = mapper.readValue(bytes, cls);
        // TODO: original:
        // T result = mapper.readValue(content, cls);
        result.setClient(this);
        return result;
    }

    private ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    /**
     * @return the version string.
     */
    public String getJenkinsVersion() {
        return this.jenkinsVersion;
    }

    private void getJenkinsVersionFromHeader(HttpResponse response) {
        Header[] headers = response.getHeaders("X-Jenkins");
        if (headers.length == 1) {
            this.jenkinsVersion = headers[0].getValue();
        }
    }

    private void releaseConnection(HttpRequestBase httpRequestBase) {
        httpRequestBase.releaseConnection();
    }

    protected static HttpClientBuilder addAuthentication(HttpClientBuilder builder, URI uri, String username,
            String password) {
        if (isNotBlank(username)) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(scope, credentials);
            builder.setDefaultCredentialsProvider(provider);

            builder.addInterceptorFirst(new PreemptiveAuth());
        }
        return builder;
    }

    protected HttpContext getLocalContext() {
        return localContext;
    }

    protected void setLocalContext(HttpContext localContext) {
        this.localContext = localContext;
    }
}
