package com.offbytwo.jenkins.model;

public class Artifact extends BaseModel {
	String displayPath;
	String filename;
	String relativePath;
	public String getDisplayPath() {
		return displayPath;
	}
	public void setDisplayPath(String displayPath) {
		this.displayPath = displayPath;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getRelativePath() {
		return relativePath;
	}
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
}
