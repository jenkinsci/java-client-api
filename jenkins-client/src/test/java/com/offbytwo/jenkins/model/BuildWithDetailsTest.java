package com.offbytwo.jenkins.model;

import com.offbytwo.jenkins.BaseUnitTest;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.client.JenkinsHttpConnection;
import com.offbytwo.jenkins.helper.BuildConsoleStreamListener;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

/**
 */
public class BuildWithDetailsTest extends BaseUnitTest {

    private JenkinsHttpConnection client;
    private BuildWithDetails buildWithDetails;

    @Before
    public void setUp() {
        client = mock(JenkinsHttpClient.class);
        buildWithDetails = givenBuild();
    }

    private BuildWithDetails givenBuild() {
        BuildWithDetails buildWithDetails = new BuildWithDetails();
        buildWithDetails.setClient(client);
        return buildWithDetails;
    }

    @Test
    public void getBuildLogWithBuffer() {
        try {
            HttpResponse response = mock(HttpResponse.class);
            String text = "test test test";
            int textLength = text.length();
            given(response.getEntity()).willReturn(new StringEntity(text));
            BasicHeader moreDataHeader = new BasicHeader(BuildWithDetails.MORE_DATA_HEADER, "false");
            BasicHeader textSizeHeader = new BasicHeader(BuildWithDetails.TEXT_SIZE_HEADER, Integer.toString(textLength));
            given(response.getFirstHeader(BuildWithDetails.MORE_DATA_HEADER)).willReturn(moreDataHeader);
            given(response.getFirstHeader(BuildWithDetails.TEXT_SIZE_HEADER)).willReturn(textSizeHeader);
            given(client.post_form_with_result(anyString(),anyListOf(NameValuePair.class),anyBoolean())).willReturn(response);
            ConsoleLog consoleOutputText = buildWithDetails.getConsoleOutputText(500, false);
            assertThat(consoleOutputText.getConsoleLog()).isEqualTo(text);
            assertThat(consoleOutputText.getCurrentBufferSize()).isEqualTo(textLength);
            assertThat(consoleOutputText.getHasMoreData()).isFalse();
        } catch (IOException e) {
            fail("Should not return exception",e);
        }
    }


    @Test
    public void poolBuildLog() throws InterruptedException {
        try {
            HttpResponse response = mock(HttpResponse.class);
            final String text = "test test test";
            int textLength = text.length();
            given(response.getEntity()).willReturn(new StringEntity(text));
            BasicHeader moreDataHeader = new BasicHeader(BuildWithDetails.MORE_DATA_HEADER, "false");
            BasicHeader textSizeHeader = new BasicHeader(BuildWithDetails.TEXT_SIZE_HEADER, Integer.toString(textLength));
            given(response.getFirstHeader(BuildWithDetails.MORE_DATA_HEADER)).willReturn(moreDataHeader);
            given(response.getFirstHeader(BuildWithDetails.TEXT_SIZE_HEADER)).willReturn(textSizeHeader);
            given(client.post_form_with_result(anyString(),anyListOf(NameValuePair.class),anyBoolean())).willReturn(response);
            final StringBuffer buffer=new StringBuffer();
            buildWithDetails.streamConsoleOutput(new BuildConsoleStreamListener() {
                @Override
                public void onData(String newLogChunk) {
                    assertThat(newLogChunk).isEqualTo(text);
                    buffer.append(text);
                }

                @Override
                public void finished() {
                    assertThat(buffer.toString()).isEqualTo(text);
                }
            },1,2, false);
        } catch (IOException e) {
            fail("Should not return exception",e);
        }
    }

    @Test
    public void poolBuildLogShouldTimeout() throws InterruptedException {
        try {
            HttpResponse response = mock(HttpResponse.class);
            final String text = "test test test";
            int textLength = text.length();
            given(response.getEntity()).willReturn(new StringEntity(text));
            BasicHeader moreDataHeader = new BasicHeader(BuildWithDetails.MORE_DATA_HEADER, "true");
            BasicHeader textSizeHeader = new BasicHeader(BuildWithDetails.TEXT_SIZE_HEADER, Integer.toString(textLength));
            given(response.getFirstHeader(BuildWithDetails.MORE_DATA_HEADER)).willReturn(moreDataHeader);
            given(response.getFirstHeader(BuildWithDetails.TEXT_SIZE_HEADER)).willReturn(textSizeHeader);
            given(client.post_form_with_result(anyString(),anyListOf(NameValuePair.class),anyBoolean())).willReturn(response);
            buildWithDetails.streamConsoleOutput(new BuildConsoleStreamListener() {
                @Override
                public void onData(String newLogChunk) {
                    assertThat(newLogChunk).isEqualTo(text);
                }

                @Override
                public void finished() {
                    fail("Should timeout");
                }
            },1,2, false);
        } catch (IOException e) {
            fail("Should not return exception",e);
        }
    }

}
