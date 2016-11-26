package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.offbytwo.jenkins.model.ComputerSet;
import com.offbytwo.jenkins.model.ComputerWithDetails;

@Test(groups = { Groups.NO_EXECUTOR_GROUP })
public class NoExecutorStartedGetComputerSetIT extends AbstractJenkinsIntegrationCase {

    private ComputerSet computerSet;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        computerSet = jenkinsServer.getComputerSet();
    }

    @Test
    public void shouldGetNameOfMasterNode() {
        List<ComputerWithDetails> computers = computerSet.getComputers();
        assertThat(computers).hasSize(1);
        assertThat(computers.get(0).getDisplayName()).isEqualTo("master");
    }

    @Test
    public void getTotalExecutorsShouldReturnZero() {
        assertThat(computerSet.getTotalExecutors()).isEqualTo(0);
    }

    @Test
    public void getBusyExecutorsShouldReturnZero() {
        assertThat(computerSet.getBusyExecutors()).isEqualTo(0);
    }

    @Test
    public void getDisplayNameShouldReturnNodes() {
        assertThat(computerSet.getDisplayName()).isEqualTo("Nodes");
    }
}
