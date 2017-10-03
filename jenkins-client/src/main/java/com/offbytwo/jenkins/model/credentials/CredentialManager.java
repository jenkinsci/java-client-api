package com.offbytwo.jenkins.model.credentials;


import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.BaseModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredentialManager {

    public static final String V1URL = "/credential-store/domain/_";
    public static final String V2URL = "/credentials/store/system/domain/_";

    String baseUrl = V2URL;
    JenkinsHttpClient jenkinsClient;
    boolean isVersion1 = false;

    public CredentialManager( String version, JenkinsHttpClient client) {
        if (version.startsWith("1")) {
            this.isVersion1 = true;
            this.baseUrl = V1URL;
        }
        this.jenkinsClient = client;
    }

    /**
     * Return the list of exsting credentials.
     * NOTE: for each credential insstance, only the following fields are set:
     *  - id
     *  - description
     *  - displayName
     *  - fullName
     *  - typeName
     *  - username (depending on the type of the credential)
     * @return the existing credentials from Jenkins
     * @throws IOException
     */
    public Map<String, Credential> listCredentials() throws IOException {
        String url = String.format("%s?depth=2", this.baseUrl);
        if (this.isVersion1) {
            CredentialResponseV1 response = this.jenkinsClient.get(url, CredentialResponseV1.class);
            Map<String, Credential> credentials = response.getCredentials();
            //need to set the id on the credentials as it is not returned in the body
            for (String crendentialId : credentials.keySet()) {
                credentials.get(crendentialId).setId(crendentialId);
            }
            return credentials;
        } else {
            CredentialResponse response = this.jenkinsClient.get(url, CredentialResponse.class);
            List<Credential> credentials = response.getCredentials();
            Map<String, Credential> credentialMap = new HashMap<>();
            for(Credential credential : credentials) {
                credentialMap.put(credential.getId(), credential);
            }
            return credentialMap;
        }
    }

    /**
     * Create a new credential
     * @param credential the credential instance to create.
     * @param crumbFlag
     * @throws IOException
     */
    public void createCredential(Credential credential, Boolean crumbFlag) throws IOException {
        String url = String.format("%s/%s?", this.baseUrl, "createCredentials");
        if (credential.useMultipartForm()) {
            this.jenkinsClient.post_multipart_form_json(url, credential.dataForCreate(), crumbFlag);
        } else {
            this.jenkinsClient.post_form_json(url, credential.dataForCreate(), crumbFlag);
        }
    }

    /**
     * Update an existing credential.
     * @param credentialId the id of the credential to update
     * @param credential the credential to update
     * @param crumbFlag
     * @throws IOException
     */
    public void updateCredential(String credentialId, Credential credential, Boolean crumbFlag) throws IOException {
        credential.setId(credentialId);
        String url = String.format("%s/%s/%s/%s?", this.baseUrl, "credential", credentialId, "updateSubmit");
        if (credential.useMultipartForm()) {
            this.jenkinsClient.post_multipart_form_json(url, credential.dataForUpdate(), crumbFlag);
        } else {
            this.jenkinsClient.post_form_json(url, credential.dataForUpdate(), crumbFlag);
        }
    }

    /**
     * Delete the credential with the given id
     * @param credentialId the id of the credential
     * @param crumbFlag
     * @throws IOException
     */
    public void deleteCredential(String credentialId, Boolean crumbFlag) throws IOException {
        String url = String.format("%s/%s/%s/%s?", this.baseUrl, "credential", credentialId, "doDelete");
        this.jenkinsClient.post_form(url, new HashMap<String, String>(), crumbFlag);
    }

    /**
     * Represents the list response from Jenkins with the 2.x credentials plugin
     */
    public static class CredentialResponse extends BaseModel {
        private List<Credential> credentials;

        public void setCredentials(List<Credential> credentials) {
            this.credentials = credentials;
        }

        public List<Credential> getCredentials() {
            return credentials;
        }
    }

    /**
     * Represents the list response from Jenkins with the 1.x credentials plugin
     */
    public static class CredentialResponseV1 extends BaseModel {

        private Map<String, Credential> credentials;

        public Map<String, Credential> getCredentials() {
            return credentials;
        }

        public void setCredentials(Map<String, Credential> credentials) {
            this.credentials = credentials;
        }

    }
}
