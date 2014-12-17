package com.offbytwo.jenkins.client.validator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.junit.Test;

public class HttpRequestValidatorTest {

    private boolean httpResponseExceptionThrown = false;
    private HttpResponse httpResponse;

    @Test
    public void shouldThrowHttpResponseExceptionWhenStatusIsLowerThan200() {
        // given
        httpResponse = givenResponseWithCode(170);

        // when
        httpResponseExceptionThrown = validateResponse(httpResponse);

        // then
        assertTrue(httpResponseExceptionThrown);
    }

    private HttpResponseValidator validator = new HttpResponseValidator();

    @Test
    public void shouldThrowHttpResponseExceptionWhenStatusIsHigherThan400() {
        // given
        httpResponse = givenResponseWithCode(403);

        // when
        httpResponseExceptionThrown = validateResponse(httpResponse);

        // then
        assertTrue(httpResponseExceptionThrown);
    }

    @Test
    public void shouldThrowHttpResponseExceptionWhenStatusIs400() {
        // given
        httpResponse = givenResponseWithCode(400);

        // when
        httpResponseExceptionThrown = validateResponse(httpResponse);

        // then
        assertTrue(httpResponseExceptionThrown);
    }

    @Test
    public void shouldNotThrowHttpResponseExceptionWhenStatusIsBetween200and400() {
        // given
        httpResponse = givenResponseWithCode(220);

        // when
        httpResponseExceptionThrown = validateResponse(httpResponse);

        // then
        assertFalse(httpResponseExceptionThrown);
    }

    private HttpResponse givenResponseWithCode(Integer statusCode) {
        HttpResponse httpResponse = mock(HttpResponse.class);
        StatusLine statusLine = mock(StatusLine.class);

        given(httpResponse.getStatusLine()).willReturn(statusLine);
        given(statusLine.getStatusCode()).willReturn(statusCode);

        return httpResponse;
    }

    private boolean validateResponse(HttpResponse httpResponse) {
        boolean httpResponseExceptionThrown = false;
        try {
            validator.validateResponse(httpResponse);
        } catch (HttpResponseException e) {
            httpResponseExceptionThrown = true;
        }
        return httpResponseExceptionThrown;
    }
}
