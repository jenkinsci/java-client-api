package com.offbytwo.jenkins.model;

public class Artifact extends BaseModel {
	String displayPath;
	String fileName;
	String relativePath;
	public String getDisplayPath() {
		return displayPath;
	}
	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
}
