package com.offbytwo.jenkins.integration;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Plugin;
import com.offbytwo.jenkins.model.credentials.*;
import org.apache.commons.lang.RandomStringUtils;
import org.testng.SkipException;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

@Test(groups = { Groups.NO_EXECUTOR_GROUP} )
public class NoExecutorStartedManageCredentialsIT extends AbstractJenkinsIntegrationCase {

    @Test
    public void credentialCRUDL() throws IOException {
        List<Plugin> plugins = jenkinsServer.getPluginManager().getPlugins();
        Plugin credentialPlugin = jenkinsServer.findPluginWithName("credentials");
        if (credentialPlugin == null) {
            throw new SkipException("No credentials plugin found. Skip Test");
        }

        String pluginVersion = credentialPlugin.getVersion();
        if (pluginVersion.startsWith("1.")) {
            runTest(jenkinsServer);

            //test CertificateCredential with upload cert file. The 2.x version may throw exceptions.
            CertificateCredential certificateCredential = new CertificateCredential();
            certificateCredential.setId("certficateTest-" + RandomStringUtils.randomAlphanumeric(24));
            certificateCredential.setCertificateSourceType(CertificateCredential.CERTIFICATE_SOURCE_TYPES.UPLOAD_CERT_FILE);
            certificateCredential.setCertificateContent("testcert".getBytes());
            certificateCredential.setPassword("testpasssword");

            credentialOperations(jenkinsServer, certificateCredential);

        } else {
            runTest(jenkinsServer);

            //test SecretTextCredential, this is v2 only
            SecretTextCredential secretText = new SecretTextCredential();
            secretText.setId("secrettextcredentialTest-" + RandomStringUtils.randomAlphanumeric(24));
            secretText.setSecret("testsecrettext");

            credentialOperations(jenkinsServer, secretText);
        }
    }

    private void runTest(JenkinsServer jenkinsServer) throws IOException {
        String testUsername = "testusername";
        String testPassword = "testpassword";
        String credentialDescription = "testDescription";
        //test UsernamePasswordCredential
        UsernamePasswordCredential testUPCredential = new UsernamePasswordCredential();
        testUPCredential.setId("usernamepasswordcredentialTest-" + RandomStringUtils.randomAlphanumeric(24));
        testUPCredential.setUsername(testUsername);
        testUPCredential.setPassword(testPassword);
        testUPCredential.setDescription(credentialDescription);

        credentialOperations(jenkinsServer, testUPCredential);

        //test SSHKeyCredential
        SSHKeyCredential sshCredential = new SSHKeyCredential();
        sshCredential.setId("sshusercredentialTest-" + RandomStringUtils.randomAlphanumeric(24));
        sshCredential.setUsername(testUsername);
        sshCredential.setPassphrase(testPassword);
        sshCredential.setPrivateKeyType(SSHKeyCredential.PRIVATE_KEY_TYPES.DIRECT_ENTRY);
        sshCredential.setPrivateKeyValue("testPrivateKeyContent");

        credentialOperations(jenkinsServer, sshCredential);

        //test credential
        CertificateCredential certificateCredential = new CertificateCredential();
        certificateCredential.setId("certficateTest-" + RandomStringUtils.randomAlphanumeric(24));
        certificateCredential.setCertificateSourceType(CertificateCredential.CERTIFICATE_SOURCE_TYPES.FILE_ON_MASTER);
        certificateCredential.setCertificatePath("/tmp/test");
        certificateCredential.setPassword("testpasssword");

        credentialOperations(jenkinsServer, certificateCredential);

    }

    private void credentialOperations(JenkinsServer jenkinsServer, Credential credential) throws IOException {
        //create the credential
        String credentialId = credential.getId();
        jenkinsServer.createCredential(credential, false);

        //check if has been created by listing
        Map<String, Credential> credentials = jenkinsServer.listCredentials();
        Credential found = credentials.get(credentialId);
        assertNotNull(found);
        assertEquals(credential.getTypeName(), found.getTypeName());

        //compare fields
        assertEquals(credentialId, found.getId());
        assertNotNull(found.getDisplayName());

        //update the credential
        String updateDescription = "updatedDescription";
        credential.setDescription(updateDescription);
        jenkinsServer.updateCredential(credentialId, credential, false);

        //verify it is updated
        credentials = jenkinsServer.listCredentials();
        found = credentials.get(credentialId);
        assertEquals(updateDescription, found.getDescription());

        //delete the credential
        jenkinsServer.deleteCredential(credentialId, false);
        credentials = jenkinsServer.listCredentials();
        assertFalse(credentials.containsKey(credentialId));
    }
}
