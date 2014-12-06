package com.offbytwo.jenkins.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class View extends MainView {

	private String name = "";
	private String description = "";
	private String url = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public View() {
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		return xstream.toXML(this);
	}
	
}
