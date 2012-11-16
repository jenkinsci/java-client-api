/*
 * Copyright (c) 2012 Cosmin Stejerean.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.client;

import com.offbytwo.jenkins.model.BaseModel;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class JenkinsHttpClient {
    private URI uri;
    private DefaultHttpClient client;
    private BasicHttpContext localContext;

    private ObjectMapper mapper;

    /**
     * Create an unauthenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     */
    public JenkinsHttpClient(URI uri) {
        this.uri = uri;
        this.mapper = getDefaultMapper();
        this.client = new DefaultHttpClient();
    }

    /**
     * Create an authenticated Jenkins HTTP client
     *
     * @param uri Location of the jenkins server (ex. http://localhost:8080)
     * @param username Username to use when connecting
     * @param password Password or auth token to use when connecting
     */
    public JenkinsHttpClient(URI uri, String username, String password) {
        this(uri);
        CredentialsProvider provider = client.getCredentialsProvider();
        AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
        provider.setCredentials(scope, credentials);

        localContext = new BasicHttpContext();
        localContext.setAttribute("preemptive-auth", new BasicScheme());
        client.addRequestInterceptor(new PreemptiveAuth(), 0);
    }

    /**
     * Perform a GET request and parse the response to the given class
     *
     * @param path path to request, can be relative or absolute
     * @param cls class of the response
     * @param <T> type of the response
     * @return an instance of the supplied class
     * @throws IOException
     */
    public <T extends BaseModel> T get(String path, Class<T> cls) throws IOException {
        HttpResponse response = client.execute(new HttpGet(api(path)), localContext);
        return objectFromResponse(cls, response);
    }

    public <T extends BaseModel> T post(String path, HttpEntity data, Class<T> cls) throws IOException {
        HttpPost request = new HttpPost(api(path));
        request.setEntity(data);
        HttpResponse response = client.execute(request, localContext);
        return objectFromResponse(cls, response);
    }

    private URI api(String path) {
        return uri.resolve(path).resolve("api/json");
    }

    private <T extends BaseModel> T objectFromResponse(Class<T> cls, HttpResponse response) throws IOException {
        InputStream content = response.getEntity().getContent();
        T result = mapper.readValue(content, cls);
        result.setClient(this);
        return result;
    }

    private class PreemptiveAuth implements HttpRequestInterceptor {
        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            if (authState.getAuthScheme() == null) {
                AuthScheme authScheme = (AuthScheme) context.getAttribute("preemptive-auth");
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                if (authScheme != null) {
                    Credentials creds = credsProvider.getCredentials(
                            new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                    if (creds == null) {
                        throw new HttpException("No credentials for preemptive authentication");
                    }
                    authState.update(authScheme, creds);
                }
            }
        }
    }

    private ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        mapper.setDeserializationConfig(deserializationConfig.without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        return mapper;
    }
}
