/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.net.URI;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;



/**
 *
 * @author Dell Green
 */
public class JenkinsHttpClientTest {
    private static final String URI = "http://localhost/jenkins";



    @Test
    public void testGet_String() throws Exception {
        final CloseableHttpClient client = mock(CloseableHttpClient.class);
        final CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        final Header versionHeader = mock(Header.class);
        final StatusLine statusLine = mock(StatusLine.class);
        final HttpEntity entity = mock(HttpEntity.class);
        given(client.execute(any(HttpUriRequest.class), eq((HttpContext)null))).willReturn(response);
        given(response.getHeaders("X-Jenkins")).willReturn(new Header[]{versionHeader});
        given(response.getStatusLine()).willReturn(statusLine);
        given(versionHeader.getValue()).willReturn("1.234");
        given(statusLine.getStatusCode()).willReturn(HttpStatus.SC_OK);
        given(response.getEntity()).willReturn(entity);
        given(entity.getContent()).willReturn(new ByteArrayInputStream("someJson".getBytes()));
        final JenkinsHttpConnection jclient = new JenkinsHttpClient(new URI(URI), client);
        final String s = jclient.get("job/someJob");
        assertEquals("someJson", s);
    }



    @Test(expected=IllegalStateException.class)
    public void testClose() throws Exception {
        final JenkinsHttpConnection jclient = new JenkinsHttpClient(new URI(URI));
        jclient.close();
        jclient.close(); //check multiple calls yield no errors
        jclient.get("job/someJob");
    }


}