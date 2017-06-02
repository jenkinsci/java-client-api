package com.offbytwo.jenkins.model.credentials;

import java.util.HashMap;
import java.util.Map;

/**
 * SSH Key Credential type. Can be used with 1.x and 2.x versions of the credentials plugin.
 */
public class SSHKeyCredential extends Credential {

    public static final String TYPENAME = "SSH Username with private key";

    private static final String BASECLASS = "com.cloudbees.jenkins.plugins.sshcredentials.impl.BasicSSHUserPrivateKey";

    private static final String DIRECT_ENTRY_CLASS = BASECLASS + "$DirectEntryPrivateKeySource";
    private static final String FILE_ON_MASTER_CLASS = BASECLASS +  "$FileOnMasterPrivateKeySource";
    private static final String USERS_PRIVATE_KEY_CLASS = BASECLASS +  "$UsersPrivateKeySource";


    private String username;
    private String passphrase;
    private String privateKeyValue;

    public SSHKeyCredential() {
        setTypeName(TYPENAME);
    }

    /**
     * The type of the private key.
     */
    public enum PRIVATE_KEY_TYPES {
        /**
         * Plain text
         */
        DIRECT_ENTRY (DIRECT_ENTRY_CLASS, 0),
        /**
         * A file path on the master node
         */
        FILE_ON_MASTER (FILE_ON_MASTER_CLASS, 1),

        /**
         * From the Jenkins master ~/.ssh
         */
        USERS_PRIVATE_KEY (USERS_PRIVATE_KEY_CLASS, 2);

        private String privateKeyTypeClass;
        private int typeValue;

        PRIVATE_KEY_TYPES(String typeClass, int typeValue) {
            this.privateKeyTypeClass = typeClass;
            this.typeValue = typeValue;
        }

        public String getTypeClass() {
            return this.privateKeyTypeClass;
        }

        public int getTypeValue() {
            return this.typeValue;
        }
    }

    private PRIVATE_KEY_TYPES privateKeyType;


    public String getUsername() {
        return username;
    }

    /**
     * Set the username of the ssh key
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassphrase() {
        return passphrase;
    }

    /**
     * Set the passphrash of the ssh key
     * @param passphrase
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getPrivateKeyValue() {
        return privateKeyValue;
    }

    /**
     * Set the value of the private key.
     * Depending on the type of the private key, it should be either the content of the key, or the path of the private key file.
     * @param privateKeyValue
     */
    public void setPrivateKeyValue(String privateKeyValue) {
        this.privateKeyValue = privateKeyValue;
    }

    public PRIVATE_KEY_TYPES getPrivateKeyType() {
        return privateKeyType;
    }

    /**
     * The source of the private key.
     * @param privateKeyType
     */
    public void setPrivateKeyType(PRIVATE_KEY_TYPES privateKeyType) {
        this.privateKeyType = privateKeyType;
    }

    @Override
    public Map<String, Object> dataForCreate() {
        Map<String, String> privateKeySourceMap = new HashMap<>();
        privateKeySourceMap.put("value", String.valueOf(this.getPrivateKeyType().getTypeValue()));
        privateKeySourceMap.put("privateKey", this.getPrivateKeyValue());
        privateKeySourceMap.put("stapler-class", this.getPrivateKeyType().getTypeClass());

        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("scope", SCOPE_GLOBAL);
        innerMap.put("id", this.getId());
        innerMap.put("username", this.getUsername());
        innerMap.put("description", this.getDescription());
        innerMap.put("passphrase", this.getPassphrase());
        innerMap.put("stapler-class", BASECLASS);
        innerMap.put("$class", BASECLASS);
        innerMap.put("privateKeySource", privateKeySourceMap);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("", "1");
        jsonData.put("credentials", innerMap);
        return jsonData;
    }

    @Override
    public Map<String, Object> dataForUpdate() {
        Map<String, String> privateKeySourceMap = new HashMap<>();
        privateKeySourceMap.put("value", String.valueOf(this.getPrivateKeyType().getTypeValue()));
        privateKeySourceMap.put("privateKey", this.getPrivateKeyValue());
        privateKeySourceMap.put("stapler-class", this.getPrivateKeyType().getTypeClass());

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("scope", SCOPE_GLOBAL);
        jsonData.put("id", this.getId());
        jsonData.put("username", this.getUsername());
        jsonData.put("description", this.getDescription());
        jsonData.put("passphrase", this.getPassphrase());
        jsonData.put("stapler-class", BASECLASS);
        jsonData.put("$class", BASECLASS);
        jsonData.put("privateKeySource", privateKeySourceMap);

        return jsonData;
    }
}
