package com.offbytwo.jenkins.model.credentials;


import java.util.HashMap;
import java.util.Map;

/**
 * Secret Text credential type. Can be used with 2.x version of the credentials plugins.
 */
public class SecretTextCredential extends Credential {

    public static final String TYPENAME = "Secret text";
    private static final String CLASSNAME = "org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl";
    private String secret;

    public SecretTextCredential() {
        setTypeName(TYPENAME);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public Map<String, Object> dataForCreate() {
        Map<String, String> innerMap = new HashMap<>();
        innerMap.put("scope", this.getScope());
        innerMap.put("id", this.getId());
        innerMap.put("secret", this.getSecret());
        innerMap.put("description", this.getDescription());
        innerMap.put("$class", CLASSNAME);
        innerMap.put("stapler-class", CLASSNAME);
        Map<String, Object> data = new HashMap<>();
        data.put("", "1");
        data.put("credentials", innerMap);
        return data;
    }

    @Override
    public Map<String, Object> dataForUpdate() {
        Map<String, Object> data = new HashMap<>();
        data.put("scope", this.getScope());
        data.put("id", this.getId());
        data.put("secret", this.getSecret());
        data.put("description", this.getDescription());
        data.put("stapler-class", CLASSNAME);
        return  data;
    }
}
