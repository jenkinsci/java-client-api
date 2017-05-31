package com.offbytwo.jenkins.model.credentials;


import com.offbytwo.jenkins.client.JenkinsHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CredentialManagerTest {

    private JenkinsHttpClient client = mock(JenkinsHttpClient.class);

    private CredentialManager credentialManager = new CredentialManager("2.0.0", client);

    private UsernamePasswordCredential credential1 = new UsernamePasswordCredential();
    private UsernamePasswordCredential credential2 = new UsernamePasswordCredential();

    @Before
    public void setup() {
        credential1.setId("credential1");
        credential1.setUsername("test1");
        credential1.setPassword("test1");

        credential2.setId("credential2");
        credential2.setUsername("test2");
        credential2.setPassword("test2");
    }

    @Test
    public void testListCredentials() throws IOException {
        List<Credential> credentialList = new ArrayList<>();
        credentialList.add(credential1);
        credentialList.add(credential2);

        CredentialManager.CredentialResponse response = new CredentialManager.CredentialResponse();
        response.setCredentials(credentialList);

        given(client.get(anyString(), eq(CredentialManager.CredentialResponse.class))).willReturn(response);

        Map<String, Credential> credentials = credentialManager.listCredentials();
        assertTrue(credentials.containsKey(credential1.getId()));
        assertTrue(credentials.containsKey(credential2.getId()));

        verify(client).get(CredentialManager.V2URL + "?depth=2", CredentialManager.CredentialResponse.class);
    }

    @Test
    public void testCreateCredential() throws IOException {
        UsernamePasswordCredential credentialToCreate = new UsernamePasswordCredential();
        credentialToCreate.setId("testCreation");
        credentialToCreate.setUsername("testuser");
        credentialToCreate.setPassword("password");

        credentialManager.createCredential(credentialToCreate, false);
        verify(client).post_form_json(eq (CredentialManager.V2URL + "/createCredentials?"), anyMap(), eq(false));
    }

    @Test
    public void testUpdateCredential() throws IOException {
        String credentialId = "testUpdate";
        UsernamePasswordCredential credentialToUpdate = new UsernamePasswordCredential();
        credentialToUpdate.setId(credentialId);
        credentialToUpdate.setUsername("testuser");
        credentialToUpdate.setPassword("password");

        credentialManager.updateCredential(credentialId, credentialToUpdate, false);
        verify(client).post_form_json(eq(CredentialManager.V2URL + "/credential/" + credentialId + "/updateSubmit?"), anyMap(), eq(false));
    }

    @Test
    public void testDeleteCredential() throws IOException {
        String credentialId = "testDelete";
        credentialManager.deleteCredential(credentialId, false);

        verify(client).post_form(eq(CredentialManager.V2URL + "/credential/" + credentialId + "/doDelete?"), anyMap(), eq(false));
    }
}
