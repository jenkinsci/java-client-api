package com.offbytwo.jenkins.client;

import org.apache.http.protocol.HttpContext;

/**
 * User: zjzhai
 * Date: 1/3/14
 * Time: 6:49 PM
 */
public interface Auth {

    boolean authentication();

    HttpContext getContext();

}
