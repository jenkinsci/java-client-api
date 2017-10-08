/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;



/**
 *
 * @author Dell Green
 */
public class ResponseUtilsTest {

    

    @Test
    public void testGetJenkinsVersion() {
        final Header header = mock(Header.class);
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeaders("X-Jenkins")).willReturn(new Header[]{header});
        given(header.getValue()).willReturn("1.234");
        assertEquals("1.234", ResponseUtils.getJenkinsVersion(response));
    }
    
    
    @Test
    public void testGetJenkinsVersion_NoHeader() {
        final HttpResponse response = mock(HttpResponse.class);
        given(response.getHeaders("X-Jenkins")).willReturn(new Header[0]);
        assertEquals("", ResponseUtils.getJenkinsVersion(response));
    }
    
    
    @Test(expected = NullPointerException.class)
    public void testGetJenkinsVersion_NullResponse() {
        ResponseUtils.getJenkinsVersion(null);
    }


}