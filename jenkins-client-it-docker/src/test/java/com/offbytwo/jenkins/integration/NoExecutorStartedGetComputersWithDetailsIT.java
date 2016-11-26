package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.ComputerWithDetails;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetComputersWithDetailsIT extends AbstractJenkinsIntegrationCase {

    private ComputerWithDetails computerWithDetails;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        computerWithDetails = jenkinsServer.getComputerSet().getComputers().get(0);
    }

    @Test
    public void getIdleShouldReturnTrue() {
        assertThat(computerWithDetails.getIdle()).isTrue();
    }

    @Test
    public void getJnlpShouldReturnTrue() {
        assertThat(computerWithDetails.getJnlp()).isNull();
    }

    @Test
    public void getExecutorsShouldReturnTwo() {
        assertThat(computerWithDetails.getExecutors()).hasSize(2);
    }

    @Test
    public void getLaunchSupportedShouldReturnTrue() {
        assertThat(computerWithDetails.getLaunchSupported()).isTrue();
    }

    @Test
    public void getManualLanuchShouldReturnTrue() {
        assertThat(computerWithDetails.getManualLaunchAllowed()).isTrue();
    }

    @Test
    public void getNumExecutorsShouldReturnTwo() {
        assertThat(computerWithDetails.getNumExecutors()).isEqualTo(2);
    }

    @Test
    public void getOffLineShouldReturnTrue() {
        assertThat(computerWithDetails.getOffline()).isTrue();
    }

    @Test
    public void getOfflineReasonShouldReturnNonNull() {
        assertThat(computerWithDetails.getOfflineCauseReason()).isEqualTo("Manually turned off");
    }

    @Test
    public void getTemporarilyOfflineShouldReturnTrue() throws IOException {
        assertThat(computerWithDetails.getTemporarilyOffline()).isTrue();
    }

    @Test
    public void getOfflineCauseShouldReturnNonNull() throws IOException {
        assertThat(computerWithDetails.getOfflineCause()).isNotNull();
    }

}
