package com.offbytwo.jenkins.model.credentials;

import java.util.HashMap;
import java.util.Map;

/**
 * Username and password credential type. Can be used with 1.x and 2.x versions of the credentials plugin.
 */
public class UsernamePasswordCredential extends Credential {

    public static final String TYPENAME = "Username with password";
    private static final String CLASSNAME = "com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl";

    private String username;
    private String password;

    public UsernamePasswordCredential() {
        this.setTypeName(TYPENAME);
    }

    public String getUsername() {
        if (this.username != null) {
            return this.username;
        }
        if (this.getDisplayName() != null) {
            return this.getDisplayName().split("/") [0];
        }
        return null;
    }

    /**
     * Set the username of the credential
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the credential
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Map<String, Object> dataForCreate() {
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("scope", this.getScope());
        innerMap.put("id", this.getId());
        innerMap.put("username", this.getUsername());
        innerMap.put("password", this.getPassword());
        innerMap.put("description", this.getDescription());
        innerMap.put("$class", CLASSNAME);
        Map<String, Object> data = new HashMap<>();
        data.put("", "0");
        data.put("credentials", innerMap);
        return data;
    }

    @Override
    public Map<String, Object> dataForUpdate() {
        Map<String, Object> data = new HashMap<>();
        data.put("scope", this.getScope());
        data.put("id", this.getId());
        data.put("username", this.getUsername());
        data.put("password", this.getPassword());
        data.put("description", this.getDescription());
        data.put("stapler-class", CLASSNAME);
        return  data;
    }
}
