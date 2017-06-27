package com.offbytwo.jenkins.model.credentials;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Apple developer profile credential type.
 *
 * NOTE: this type is only available on Jenkins after the xcode plugin (https://wiki.jenkins.io/display/JENKINS/Xcode+Plugin) is installed.
 */
public class AppleDeveloperProfileCredential extends Credential {
  public static final String TYPENAME = "Apple Developer Profile";

  private static final String BASECLASS = "au.com.rayh.DeveloperProfile";
  private static final String FILE_ZERO_FIELD_NAME = "file0";
  private static final String FILE_ONE_FIELD_NAME = "file1";

  private String password;
  private byte[] developerProfileContent;

  public String getPassword() {
    return password;
  }

  /**
   * Set the password of the developer profile
   * @param password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  public byte[] getDeveloperProfileContent() {
    return developerProfileContent;
  }

  /**
   * Set the content of the developer profile. A developer profile file is a zip with the following structure:
   *
   * developerprofile/
   *   - account.keychain (can be empty. Required for validation. The plugin will create a new keychain before build)
   *   - identities
   *    |- <name>.p12 (A exported P12 file. Should contain both certificate and private key)
   *   - profiles
   *    |- <name>.mobileprovision (A mobile provisioning profile)
   * @param developerProfileContent
   */
  public void setDeveloperProfileContent(byte[] developerProfileContent) {
    this.developerProfileContent = developerProfileContent;
  }

  @Override
  public boolean useMultipartForm() {
    return true;
  }

  @Override
  public Map<String, Object> dataForCreate() {
    Map<String, String> credentialMap = new HashMap<String, String>();
    credentialMap.put("image", FILE_ZERO_FIELD_NAME);
    credentialMap.put("password", this.getPassword());
    credentialMap.put("id", this.getId());
    credentialMap.put("description", this.getDescription());
    credentialMap.put("stapler-class", BASECLASS);
    credentialMap.put("$class", BASECLASS);


    Map<String, Object> jsonData = new HashMap<>();
    jsonData.put("", "1");
    jsonData.put("credentials", credentialMap);

    Map<String, Object> formFields = new HashMap<String, Object>();
    formFields.put(FILE_ZERO_FIELD_NAME, this.getDeveloperProfileContent());
    formFields.put("_.scope", SCOPE_GLOBAL);
    formFields.put("_.password", this.getPassword());
    formFields.put("_.id", this.getId());
    formFields.put("_.description", this.getDescription());
    formFields.put("stapler-class", BASECLASS);
    formFields.put("$class", BASECLASS);
    formFields.put("json", JSONObject.fromObject(jsonData).toString());
    return formFields;
  }

  @Override
  public Map<String, Object> dataForUpdate() {
    Map<String, Object> credentialMap = new HashMap<String, Object>();
    credentialMap.put("image", FILE_ONE_FIELD_NAME);
    credentialMap.put("password", this.getPassword());
    credentialMap.put("id", this.getId());
    credentialMap.put("description", this.getDescription());
    credentialMap.put("stapler-class", BASECLASS);
    credentialMap.put("", true);


    Map<String, Object> formFields = new HashMap<String, Object>();
    formFields.put(FILE_ONE_FIELD_NAME, this.getDeveloperProfileContent());
    formFields.put("_.", "on");
    formFields.put("_.password", this.getPassword());
    formFields.put("_.id", this.getId());
    formFields.put("_.description", this.getDescription());
    formFields.put("stapler-class", BASECLASS);
    formFields.put("json", JSONObject.fromObject(credentialMap).toString());
    return formFields;
  }
}
