package com.offbytwo.jenkins.client;

public class FormBinaryField {
  private String fileName;
  private String contentType;
  private byte[] content;

  public FormBinaryField(String fileName, String contentType, byte[] content) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.content = content;
  }

  public String getFileName() {
    return fileName;
  }

  public String getContentType() {
    return contentType;
  }

  public byte[] getContent() {
    return content;
  }
}
