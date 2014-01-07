package com.offbytwo.jenkins.auth;

import com.offbytwo.jenkins.client.Auth;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.net.URL;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * User: zjzhai
 * Date: 1/3/14
 * Time: 6:55 PM
 */
public class GeneralAuth implements Auth {

    private URL url;

    private BasicHttpContext localContext;

    private String username;

    private String password;

    public GeneralAuth(URL url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean authentication() {
        DefaultHttpClient client = new DefaultHttpClient();
        if (isNotBlank(username)) {
            CredentialsProvider provider = client.getCredentialsProvider();
            AuthScope scope = new AuthScope(url.getHost(), url.getPort(), "realm");
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(username, password);
            provider.setCredentials(scope, credentials);
            localContext = new BasicHttpContext();
            localContext.setAttribute("preemptive-auth", new BasicScheme());
            client.addRequestInterceptor(new PreemptiveInterceptor(), 0);

        }

        return false;
    }


    @Override
    public HttpContext getContext() {
        return localContext;
    }
}
