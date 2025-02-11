package com.telus.cmb.tool.services.log.config.domain.app;

import javax.xml.bind.annotation.XmlAttribute;

public class OverrideValue {

	private String name;
	private String value;
	private String environments;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@XmlAttribute
	public String getEnvironments() {
		return environments;
	}

	public void setEnvironments(String environments) {
		this.environments = environments;
	}

}
