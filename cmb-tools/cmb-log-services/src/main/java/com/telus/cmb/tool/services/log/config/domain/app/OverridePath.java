package com.telus.cmb.tool.services.log.config.domain.app;

import javax.xml.bind.annotation.XmlAttribute;

public class OverridePath {

	private String path;
	private String environments;

	@XmlAttribute
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@XmlAttribute
	public String getEnvironments() {
		return environments;
	}

	public void setEnvironments(String environments) {
		this.environments = environments;
	}

}
