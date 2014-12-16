package com.offbytwo.jenkins.client.validator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;

public class HttpResponseValidator {

    public void validateResponse(HttpResponse response) throws HttpResponseException {
        int status = response.getStatusLine().getStatusCode();
        if (status < 200 || status >= 400) {
            System.out.println("Unknown response code: " + status);
            throw new HttpResponseException(status, response.getStatusLine().getReasonPhrase());
        }
    }
}
