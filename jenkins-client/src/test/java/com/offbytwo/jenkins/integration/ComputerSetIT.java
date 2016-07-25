package com.offbytwo.jenkins.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.dom4j.DocumentException;
import org.junit.Test;

import com.offbytwo.jenkins.model.ComputerSet;
import com.offbytwo.jenkins.model.ComputerWithDetails;

import jenkins.model.Jenkins;

public class ComputerSetIT extends BaseForIntegrationTests {

    @Test
    public void shouldGetTotalExecutors() throws JAXBException, IOException, DocumentException {
        Jenkins ji = jenkinsRule.getInstance();

        ComputerSet computerSet = jenkinsServer.getComputerSet();
        assertThat(computerSet).isNotNull();
        assertThat(computerSet.getBusyExecutors()).isEqualTo(0);
        assertThat(computerSet.getTotalExecutors()).isEqualTo(ji.getNumExecutors());
        assertThat(computerSet.getDisplayName()).isEqualTo("Nodes");
    }

    @Test
    public void shouldGetComputerWithDetailsAndExecutors() throws IOException {
        Jenkins ji = jenkinsRule.getInstance();

        List<ComputerWithDetails> computerSet = jenkinsServer.getComputerSet().getComputers();
        ComputerWithDetails computerWithDetails = computerSet.get(0);
        assertThat(computerWithDetails.getExecutors()).isNotNull();
        assertThat(computerWithDetails.getNumExecutors()).isEqualTo(ji.getNumExecutors());
        assertThat(computerWithDetails.getOfflineCause()).isNull();
    }
    
    @Test
    public void shouldTrunFromOnlineToOffline() throws IOException {
        ComputerWithDetails computerWithDetails = jenkinsServer.getComputerSet().getComputers().get( 0 );
        computerWithDetails.toggleOffline(true);

        ComputerWithDetails computerWithDetailsAfterStarting = jenkinsServer.getComputerSet().getComputers().get( 0 );

        assertThat( computerWithDetailsAfterStarting.getOffline() ).isTrue();
    }
}
