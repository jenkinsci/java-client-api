package com.offbytwo.jenkins.model.credentials;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Certificate credential type. Can be used with both 1.x and 2.x versions of the credentials plugin.
 *
 * NOTE: there is a bug in 2.x version of the plugin that will thrown exception when uploading a certificate file. See https://issues.jenkins-ci.org/browse/JENKINS-41946.
 * It is fixed in 2.1.12 and later.
 */
public class CertificateCredential extends Credential {
    public static final String TYPENAME = "Certificate";

    private static final String BASECLASS = "com.cloudbees.plugins.credentials.impl.CertificateCredentialsImpl";

    private static final String FILE_ON_MASTER_KEYSTORE_SOURCE_CLASS = BASECLASS +  "$FileOnMasterKeyStoreSource";

    private static final String UPLOAD_KEYSTORE_SOURCE_CLASS = BASECLASS + "$UploadedKeyStoreSource";

    private String password;
    private String certificatePath;
    private byte[] certificateContent;
    private CERTIFICATE_SOURCE_TYPES certificateSourceType;

    /**
     * The source of the certificate.
     */
    public enum CERTIFICATE_SOURCE_TYPES {
        /**
         * The certificate is on the file system of the master node. Set the path via {@link #setCertificatePath(String)} method
         */
        FILE_ON_MASTER(FILE_ON_MASTER_KEYSTORE_SOURCE_CLASS, 0),
        /**
         * Update the certificate content. Should set it via {@link #setCertificateContent(byte[])} method.
         */
        UPLOAD_CERT_FILE(UPLOAD_KEYSTORE_SOURCE_CLASS, 1);

        private String certStoreClass;
        private int certStoreType;

        CERTIFICATE_SOURCE_TYPES(String storeClass, int storeType) {
            this.certStoreClass = storeClass;
            this.certStoreType = storeType;
        }

        public String getCertStoreClass() {
            return this.certStoreClass;
        }

        public int getCertStoreType() {
            return this.certStoreType;
        }
    }

    public CertificateCredential() {
        setTypeName(TYPENAME);
    }

    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the certificate
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    /**
     * Set the path of the certificate. Required if CERTIFICATE_SOURCE_TYPES is FILE_ON_MASTER.
     * @param certificatePath
     */
    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public byte[] getCertificateContent() {
        return certificateContent;
    }

    /**
     * Set the content of the certificate. Required if CERTIFICATE_SOURCE_TYPES is UPLOAD_CERT_FILE.
     * @param certificateContent
     */
    public void setCertificateContent(byte[] certificateContent) {
        this.certificateContent = certificateContent;
    }

    public CERTIFICATE_SOURCE_TYPES getCertificateSourceType() {
        return certificateSourceType;
    }

    /**
     * Set the source of the certificate
     * @param certificateSourceType
     */
    public void setCertificateSourceType(CERTIFICATE_SOURCE_TYPES certificateSourceType) {
        this.certificateSourceType = certificateSourceType;
    }

    @Override
    public Map<String, Object> dataForCreate() {
        Map<String, String> certificateSourceMap = new HashMap<>();
        certificateSourceMap.put("value", String.valueOf(this.getCertificateSourceType().getCertStoreType()));
        certificateSourceMap.put("stapler-class", this.getCertificateSourceType().getCertStoreClass());
        certificateSourceMap.put("$class", this.getCertificateSourceType().getCertStoreClass());

        if (this.getCertificateSourceType() == CERTIFICATE_SOURCE_TYPES.FILE_ON_MASTER) {
            certificateSourceMap.put("keyStoreFile", this.getCertificatePath());
        } else if (this.getCertificateSourceType() == CERTIFICATE_SOURCE_TYPES.UPLOAD_CERT_FILE) {
            certificateSourceMap.put("uploadedKeystore", Base64.encodeBase64String(this.getCertificateContent()));
        }

        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("scope", SCOPE_GLOBAL);
        innerMap.put("id", this.getId());
        innerMap.put("description", this.getDescription());
        innerMap.put("password", this.getPassword());
        innerMap.put("stapler-class", BASECLASS);
        innerMap.put("$class", BASECLASS);
        innerMap.put("keyStoreSource", certificateSourceMap);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("", "1");
        jsonData.put("credentials", innerMap);
        return jsonData;
    }

    @Override
    public Map<String, Object> dataForUpdate() {
        Map<String, String> certificateSourceMap = new HashMap<>();
        certificateSourceMap.put("value", String.valueOf(this.getCertificateSourceType().getCertStoreType()));
        certificateSourceMap.put("stapler-class", this.getCertificateSourceType().getCertStoreClass());
        certificateSourceMap.put("$class", this.getCertificateSourceType().getCertStoreClass());

        if (this.getCertificateSourceType() == CERTIFICATE_SOURCE_TYPES.FILE_ON_MASTER) {
            certificateSourceMap.put("keyStoreFile", this.getCertificatePath());
        } else if (this.getCertificateSourceType() == CERTIFICATE_SOURCE_TYPES.UPLOAD_CERT_FILE) {
            certificateSourceMap.put("uploadedKeystore", Base64.encodeBase64String(this.getCertificateContent()));
        }

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("scope", SCOPE_GLOBAL);
        jsonData.put("id", this.getId());
        jsonData.put("description", this.getDescription());
        jsonData.put("password", this.getPassword());
        jsonData.put("stapler-class", BASECLASS);
        jsonData.put("$class", BASECLASS);
        jsonData.put("keyStoreSource", certificateSourceMap);

        return jsonData;
    }

}
