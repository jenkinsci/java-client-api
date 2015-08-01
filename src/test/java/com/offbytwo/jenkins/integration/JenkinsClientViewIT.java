package com.offbytwo.jenkins.integration;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.TopLevelItem;
import org.dom4j.DocumentException;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

public class JenkinsClientViewIT extends BaseForIntegrationTests {

    public static final String TEST_VIEW = "testView";

    @Test
    public void shouldObtainView() throws URISyntaxException, IOException, JAXBException, DocumentException {
        // given
        jenkinsRule.getInstance().addView(new TestView());

        // when
        com.offbytwo.jenkins.model.View testView = jenkinsServer.getView(TEST_VIEW);

        // then
        assertNotNull(testView);
    }

    private static class TestView extends hudson.model.View implements Serializable {
        public TestView() {
            super(TEST_VIEW);
        }

        @Override
        public Collection<TopLevelItem> getItems() {
            return null;
        }

        @Override
        public boolean contains(TopLevelItem topLevelItem) {
            return false;
        }

        @Override
        protected void submit(StaplerRequest staplerRequest) throws IOException, ServletException, Descriptor.FormException {

        }

        @Override
        public Item doCreateItem(StaplerRequest staplerRequest, StaplerResponse staplerResponse) throws IOException, ServletException {
            return null;
        }
    }
}
