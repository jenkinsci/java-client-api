package com.offbytwo.jenkins.auth;

import com.offbytwo.jenkins.client.Auth;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: zjzhai
 * Date: 1/3/14
 * Time: 6:52 PM
 */
public class CASAuth implements Auth {

    private URL casURL;

    private String username;

    private String password;

    private URL jenkinsAuthenticationUrl;

    private HttpContext context;

    private static final String JENKINS_REST_SERVICE_API = "/securityRealm/finishLogin";

    public CASAuth(URL casURL, URL jenkinsAuthenticationUrl, String username, String password) {
        this.casURL = casURL;
        this.jenkinsAuthenticationUrl = jenkinsAuthenticationUrl;
        this.username = username;
        this.password = password;
    }

    @Override
    public HttpContext getContext() {
        return context;
    }

    @Override
    public boolean authentication() {
        AbstractHttpClient httpClient = new DefaultHttpClient();
        boolean result = false;
        try {

            HttpContext httpContext = new BasicHttpContext();

            //获得与jenkins之间的session
            HttpGet httpGet = new HttpGet(jenkinsAuthenticationUrl.toString());
            httpClient.execute(httpGet, httpContext);
            httpGet.abort();
            HttpPost post = new HttpPost(casURL.toString());
            post.setEntity(new UrlEncodedFormEntity(getUsernamePasswordNameValuePair()));
            HttpResponse response = httpClient.execute(post, httpContext);
            // TODO 需要添加访问失败
            String ticket = getTicketFromHttpResponse(response);

            post.abort();

            HttpPost postService = new HttpPost(casURL.toString() + ticket);
            postService.setEntity(new UrlEncodedFormEntity(getServiceNameValuePair()));

            HttpResponse serviceResponse = httpClient.execute(postService, httpContext);
            // TODO 需要添加访问失败
            String serviceTicket = EntityUtils.toString(serviceResponse.getEntity(), "UTF-8");
            postService.abort();

            //call service
            HttpGet getServiceCall = new HttpGet(jenkinsAuthenticationUrl.toString()
                    + JENKINS_REST_SERVICE_API + "?ticket=" + serviceTicket);
            HttpResponse serviceCall = httpClient.execute(getServiceCall, httpContext);

            result = serviceCall.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
            httpContext.setAttribute(ClientContext.COOKIE_STORE, httpClient.getCookieStore());
            context = httpContext;
            getServiceCall.abort();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.getConnectionManager().shutdown();
        }


        return result;

    }

    private List<NameValuePair> getServiceNameValuePair() {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("service",
                jenkinsAuthenticationUrl.toString() + JENKINS_REST_SERVICE_API));
        return result;
    }

    private String getTicketFromHttpResponse(HttpResponse httpResponse) {
        String headName = "Location";

        if (httpResponse.getHeaders(headName) == null || httpResponse.getHeaders(headName).length == 0) {
            return "";
        }

        return StringUtils.substringAfterLast(httpResponse.getHeaders(headName)[0].toString(),
                "tickets/");
    }

    private List<NameValuePair> getUsernamePasswordNameValuePair() {
        List<NameValuePair> result = new ArrayList<NameValuePair>();
        result.add(new BasicNameValuePair("username", username));
        result.add(new BasicNameValuePair("password", password));
        return result;
    }
}
