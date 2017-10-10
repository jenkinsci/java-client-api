/*
 * Copyright (c) 2017, suren <zxjlwt@126.com>
 *
 * Distributed under the MIT license: http://opensource.org/licenses/MIT
 */

package com.offbytwo.jenkins.model;

import java.io.IOException;

import com.google.common.collect.ImmutableMap;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.util.EncodingUtils;

/**
 * pipeline manager
 * @author suren
 */
public class PipelineManager {
	
	private JenkinsHttpClient client;
	private String folderName;
	private String viewName;
	private String jobName;

	public PipelineManager(JenkinsHttpClient client,
			String viewName, String folderName, String jobName) {
		this.client = client;
		this.viewName = viewName;
		this.folderName = folderName;
		this.jobName = jobName;
	}
	
	/**
	 * Replay the pipeline of job base a history number.
	 * @param historyNum history number of job building
	 * @param pipeline pipeline script
	 * @throws IOException
	 */
	public void replay(int historyNum, String pipeline) throws IOException {
        ImmutableMap<String, String> params = ImmutableMap.of(
        		"mainScript", pipeline,
                "Submit", "Run");
        
        String viewPath = "";
        if(this.viewName != null && !this.viewName.equals("")) {
        	viewPath = "/view/" + EncodingUtils.encodeParam(this.viewName);
        }
        
        String folderPath = "";
        if(this.folderName != null && !this.folderName.equals("")) {
        	folderPath = "/job/" + EncodingUtils.encodeParam(this.folderName);
        }
        
        String uri = String.format("%s%s/job/%s/%s/replay/run?",
        		viewPath, folderPath, EncodingUtils.encodeParam(jobName), historyNum);
        
        client.post_form(uri, params, false);
	}
}
