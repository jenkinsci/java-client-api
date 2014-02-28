/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.client;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.offbytwo.jenkins.model.BaseModel;

public class JenkinsHttpClient {

    private URI uri;
    private DefaultHttpClient client;
    private BasicHttpContext localContext;

    private ObjectMapper mapper;
    private String context;

    /**
     * Create an unauthenticated Jenkins HTTP client
     * 
     * @param uri
     *            Location of the jenkins server (ex. http://localhost:8080)
     */
    public JenkinsHttpClient(URI uri) {
        this(uri, new DefaultHttpClient());
    }

    /**
     * Create an unauthenticated Jenkins HTTP client
     * 
     * @param uri
     *            Location of the jenkins server (ex. http://localhost:8080)
     * @param defaultHttpClient
     *            Configured DefaultHttpClient to be used
     */
    public JenkinsHttpClient(URI uri, DefaultHttpClient defaultHttpClient) {
        this.context = uri.getPath();
        if (!context.endsWith("/")) {
            context += "/";
        }
        this.uri = uri;
        this.mapper = getDefaultMapper();
        this.client = defaultHttpClient;
    }

    /**
     * Create an authenticated Jenkins HTTP client
     * 
     * @param uri
     *            Location of the jenkins server (ex. http://localhost:8080)
     * @param username
     *            Username to use when connecting
     * @param password
     *            Password or auth token to use when connecting
     */
    public JenkinsHttpClient(URI uri, String username, String password) {
        this(uri);
        if (isNotBlank(username)) {
            CredentialsProvider provider = client.getCredentialsProvider();
            AuthScope scope = new AuthScope(uri.getHost(), uri.getPort(), "realm");
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(scope, credentials);

            localContext = new BasicHttpContext();
            localContext.setAttribute("preemptive-auth", new BasicScheme());
            client.addRequestInterceptor(new PreemptiveAuth(), 0);
        }
    }

    /**
     * Perform a GET request and parse the response to the given class
     * 
     * @param path
     *            path to request, can be relative or absolute
     * @param cls
     *            class of the response
     * @param <T>
     *            type of the response
     * @return an instance of the supplied class
     * @throws IOException
     *             , HttpResponseException
     */
    public <T extends BaseModel> T get(String path, Class<T> cls) {
        HttpResponse response = null;
        try {
            response = client.execute(new HttpGet(api(path)), localContext);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new JenkinsClientException(status, response.getStatusLine().getReasonPhrase());
            }
            return objectFromResponse(cls, response);
        } catch (ClientProtocolException e) {
            throw new JenkinsClientException(e);
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        } finally {
            consumeEntity(response);
        }
    }

    /**
     * Perform a GET request and parse the response and return a simple string of the content
     * 
     * @param path
     *            path to request, can be relative or absolute
     * @return the entity text
     * @throws IOException
     *             , HttpResponseException
     */
    public String get(String path) {
        HttpResponse response = null;
        try {
            response = client.execute(new HttpGet(api(path)), localContext);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new JenkinsClientException(status, response.getStatusLine().getReasonPhrase());
            }
            Scanner s = new Scanner(response.getEntity().getContent());
            s.useDelimiter("\\z");
            StringBuffer sb = new StringBuffer();
            while (s.hasNext()) {
                sb.append(s.next());
            }
            s.close();
            return sb.toString();
        } catch (ClientProtocolException e) {
            throw new JenkinsClientException(e);
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        } finally {
            consumeEntity(response);
        }
    }

    /**
     * Perform a GET request and return the response as InputStream
     * 
     * @param path
     *            path to request, can be relative or absolute
     * @return the response stream
     * @throws IOException
     *             , HttpResponseException
     */
    public InputStream getFile(URI path) {
        HttpResponse response = null;
        try {
            response = client.execute(new HttpGet(path), localContext);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new JenkinsClientException(status, response.getStatusLine().getReasonPhrase());
            }
            return response.getEntity().getContent();
        } catch (ClientProtocolException e) {
            throw new JenkinsClientException(e);
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        } finally {
            consumeEntity(response);
        }
    }

    /**
     * Perform a POST request and parse the response to the given class
     * 
     * @param path
     *            path to request, can be relative or absolute
     * @param data
     *            data to post
     * @param cls
     *            class of the response
     * @param <R>
     *            type of the response
     * @param <D>
     *            type of the data
     * @return an instance of the supplied class
     * @throws IOException
     *             , HttpResponseException
     */
    public <R extends BaseModel, D> R post(String path, D data, Class<R> cls) {
        HttpPost request = new HttpPost(api(path));
        HttpResponse response = null;

        try {
            if (data != null) {
                StringEntity stringEntity = new StringEntity(mapper.writeValueAsString(data), "application/json");
                request.setEntity(stringEntity);
            }
            response = client.execute(request, localContext);
            int status = response.getStatusLine().getStatusCode();

            if (status < 200 || status >= 300) {
                throw new JenkinsClientException(status, response.getStatusLine().getReasonPhrase());
            }

            if (cls != null) {
                return objectFromResponse(cls, response);
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            throw new JenkinsClientException(e);
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        } finally {
            consumeEntity(response);
        }
    }

    /**
     * Perform a POST request of XML (instead of using json mapper) and return a string rendering of the response
     * entity.
     * 
     * @param path
     *            path to request, can be relative or absolute
     * @param XML
     *            data data to post
     * @return A string containing the xml response (if present)
     * @throws IOException
     *             , HttpResponseException
     */
    public String post_xml(String path, String xml_data) {
        HttpPost request = new HttpPost(api(path));
        HttpResponse response = null;
        if (xml_data != null) {
            request.setEntity(new StringEntity(xml_data, ContentType.APPLICATION_XML));
        }
        try {
            response = client.execute(request, localContext);
            int status = response.getStatusLine().getStatusCode();
            if (status < 200 || status >= 300) {
                throw new JenkinsClientException(status, response.getStatusLine().getReasonPhrase());
            }
            InputStream content = response.getEntity().getContent();
            Scanner s = new Scanner(content);
            StringBuffer sb = new StringBuffer();
            while (s.hasNext()) {
                sb.append(s.next());
            }
            s.close();
            return sb.toString();
        } catch (ClientProtocolException e) {
            throw new JenkinsClientException(e);
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        } finally {
            consumeEntity(response);
        }
    }

    /**
     * Perform POST request that takes no parameters and returns no response
     * 
     * @param path
     *            path to request
     * @throws IOException
     *             , HttpResponseException
     */
    public void post(String path) {
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
        URI requestUri = uri.resolve("/").resolve(path);
        return requestUri;
    }

    private <T extends BaseModel> T objectFromResponse(Class<T> cls, HttpResponse response) {
        try {
            InputStream content = response.getEntity().getContent();
            T result = mapper.readValue(content, cls);
            result.setClient(this);
            return result;
        } catch (Exception e) {
            throw new JenkinsClientException(e);
        }
    }

    private ObjectMapper getDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig();
        mapper.setDeserializationConfig(deserializationConfig
                .without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES));
        return mapper;
    }

    private void consumeEntity(HttpResponse response) {
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw new JenkinsClientException(e);
        }
    }
}
