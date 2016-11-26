package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.OfflineCause;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetOfflineCauseIT extends AbstractJenkinsIntegrationCase {

    private OfflineCause offlineCause;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        offlineCause = jenkinsServer.getComputerSet().getComputers().get(0).getOfflineCause();
    }

    /**
     * This is a timestamp so I really can't make a test which compares to a
     * real value. TODO: Think about this...
     */
    @Test
    public void getTimestampShouldReturnNonZero() {
        // FIXME: This magic number is in the config.xml
        // I need to find a simply way to read the config.xml and get the value
        // from there.
        assertThat(offlineCause.getTimestamp()).isEqualTo(1453986179962L);
    }

    @Test
    public void getOfflineCauseGetDescriptionShouldReturnDescription() throws IOException {
        assertThat(offlineCause.getDescription()).isEqualTo("Disconnected by anonymous : Manually turned off");
    }

    @Test
    public void getDescriptionShouldReturnTheAppropriateMessage() {
        assertThat(offlineCause.getDescription()).isEqualTo("Disconnected by anonymous : Manually turned off");
    }
}
