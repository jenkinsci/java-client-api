/*
 * Copyright (c) 2013 Cosmin Stejerean, Karl Heinz Marbaise, and contributors.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */
package com.offbytwo.jenkins.client.util;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * @author Dell Green
 */
class ResponseUtilsTest {

  @Test
  void testGetJenkinsVersion() {
    final Header header = mock(Header.class);
    final HttpResponse response = mock(HttpResponse.class);
    given(response.getHeaders("X-Jenkins")).willReturn(new Header[]{header});
    given(header.getValue()).willReturn("1.234");
    assertThat(ResponseUtils.getJenkinsVersion(response)).isEqualTo("1.234");
  }

  @Test
  public void testGetJenkinsVersion_NoHeader() {
    final HttpResponse response = mock(HttpResponse.class);
    given(response.getHeaders("X-Jenkins")).willReturn(new Header[0]);
    assertThat(ResponseUtils.getJenkinsVersion(response)).isEmpty();
  }


  @Test
  void testGetJenkinsVersion_NullResponse() {
    assertThatNullPointerException().isThrownBy(() -> ResponseUtils.getJenkinsVersion(null));
  }


}