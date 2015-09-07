/*
 * Copyright (c) 2013 Rising Oak LLC.
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

import org.apache.http.client.HttpResponseException;


public class Build extends BaseModel {
    
    int number;
    String url;
    
    public Build() {}
    
    public Build(Build from) {
        this(from.getNumber(), from.getUrl());
    }
    
    public Build(int number, String url) {
        this.number = number;
        this.url = url;
    }
    
    public int getNumber() {
        return number;
    }
    
    public String getUrl() {
        return url;
    }
    
    public BuildWithDetails details() throws IOException {
        return client.get(url, BuildWithDetails.class);
    }
    
    /**
     * This Change (Bad Practice) is due to inconsistencies in Jenkins various versions.
     * Jenkins changed their API from post to get and from get to post and so on.
     * For example version  1.565 is supporting stop method as a post call, version 1.609.1 support it as a get call and version 1.609.2 support it as a post call
     * Thus, when a get is not allowed, an 405 error message is generated "Method Not Allowed" and the stop isn't executed (a post is required).
     * Changed the code in order to do a post call in case a get is not allowed.
     */
    public String Stop() throws HttpResponseException, IOException{
    	try {
    		
    		return client.get(url + "stop");
    	}
    	catch (IOException ex){
    		if(((HttpResponseException)ex).getStatusCode() == 405)
    		{
    			stopPost();
    			return "";
    		}
    	}
		return "";
    }
    
    private void stopPost() throws HttpResponseException, IOException{
    	client.post(url + "stop");
    }
}
