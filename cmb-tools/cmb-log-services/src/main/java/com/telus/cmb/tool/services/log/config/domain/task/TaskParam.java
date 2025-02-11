package com.telus.cmb.tool.services.log.config.domain.task;

import javax.xml.bind.annotation.XmlAttribute;

public class TaskParam {

	private String name;
	private String value;

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
	
}
