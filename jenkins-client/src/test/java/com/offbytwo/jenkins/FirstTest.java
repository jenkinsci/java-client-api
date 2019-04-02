package com.offbytwo.jenkins;

import com.offbytwo.jenkins.client.util.EncodingUtils;
import com.offbytwo.jenkins.model.Build;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Test;

import java.net.URI;
import java.util.List;

public class FirstTest {

    @Test
    public void name() {
        String url = "http://localhost:8080/job/test/1";

        URI uri = URI.create(url);
        System.out.println("Fragement:" + uri.getFragment());
        System.out.println("Host:" + uri.getHost());
        System.out.println("Authority:" + uri.getAuthority());
        System.out.println("Path:" + uri.getPath());
        System.out.println("Query:" + uri.getQuery());
        System.out.println("Port:" + uri.getPort());
        System.out.println("Scheme:" + uri.getScheme());


        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.setPath(uri.getPath() + "/testReport");
        uriBuilder.addParameter("depth", "1");

        System.out.println("Resulting: " + uriBuilder.toString());

    }

    @Test
    public void secondTest() {


        String url = "http://localhost:8080/";


//        List<Build> builds = client.get(path + "job/" + EncodingUtils.encode(this.getName())
//                + "?tree=allBuilds[number[*],url[*],queueId[*]]", AllBuilds.class).getAllBuilds();

        // url + "/testReport/?depth=1"

//        this.getUrl() + "/testReport/?depth=1", TestReport.class

//        uri.getRawPath();
//
//    public URI(String scheme,
//                String userInfo, String host, int port,
//        String path, String query, String fragment)

    }
}
