package com.telus.cmb.tool.services.log.config.domain.task;

import javax.xml.bind.annotation.XmlAttribute;

public class TaskGrepString {

	private String value;

	@XmlAttribute
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
