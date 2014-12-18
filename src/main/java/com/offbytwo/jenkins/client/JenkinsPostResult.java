// Copyright 2014 Vladimir Alyamkin. All Rights Reserved.

package com.offbytwo.jenkins.client;

import com.offbytwo.jenkins.model.BaseModel;
import org.apache.http.Header;

/**
 * Created by ufna on 18.12.2014.
 */
public class JenkinsPostResult <R extends BaseModel> {
    /** Response object if not null */
    private R result;

    /** All headers from HttpResponse */
    private Header[] headers;

    /** Default constructor */
    public JenkinsPostResult() {}

    /**
     * Class constructor
     * @param result
     * @param headers
     */
    public JenkinsPostResult(R result, Header[] headers) {
        this.result = result;
        this.headers = headers;
    }

    /**
     * Search for first header with selected key
     * @param key
     * @return
     */
    public Object getFirstHeader(String key) {
        for (Header header : headers) {
            if(key.equals(header.getName())) {
                return header.getValue();
            }
        }

        return null;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }
}
