package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;
import org.junit.Ignore;
import org.junit.Test;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.offbytwo.jenkins.model.View;

import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.ListView;
import hudson.model.TopLevelItem;

public class JenkinsClientViewIT extends BaseForIntegrationTests {

    public static final String TEST_VIEW = "testView";

    @Test
    @Ignore("I need to check what real cause of this is: JenkinsClientViewIT.shouldObtainView:36 » NullPointer")
    public void shouldObtainView() throws URISyntaxException, IOException, JAXBException, DocumentException {
        // given
        jenkinsRule.getInstance().addView(new TestView());

        // when
        com.offbytwo.jenkins.model.View testView = jenkinsServer.getView(TEST_VIEW);

        // then
        assertThat(testView).isNotNull();
    }

    @Test
    public void shouldGetAllViews() throws URISyntaxException, IOException, JAXBException, DocumentException {
        ListView createdFirstView = new ListView("FirstView");
        ListView createdSecondView = new ListView("SecondView");
        // given
        jenkinsRule.getInstance().addView(createdFirstView);
        jenkinsRule.getInstance().addView(createdSecondView);

        // when
        Map<String, View> testView = jenkinsServer.getViews();

        assertThat(testView).hasSize(3);
        // TODO: Check why this does not work?
        // // then
        // View first = new View();
        // first.setName("FirstView");
        // assertThat(testView.get("FirstView")).isEqualTo(first);
        // View second = new View();
        // second.setName("SecondView");
        // assertThat(testView.get("SecondView")).isEqualTo(second);
    }

    private static class TestView extends hudson.model.View implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

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
        protected void submit(StaplerRequest staplerRequest)
                throws IOException, ServletException, Descriptor.FormException {

        }

        @Override
        public Item doCreateItem(StaplerRequest staplerRequest, StaplerResponse staplerResponse)
                throws IOException, ServletException {
            return null;
        }
    }
}
