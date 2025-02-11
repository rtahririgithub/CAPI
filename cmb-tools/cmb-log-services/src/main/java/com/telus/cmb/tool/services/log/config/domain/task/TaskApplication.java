package com.telus.cmb.tool.services.log.config.domain.task;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TaskApplication {

	private String shortname;
	private List<TaskComponent> taskComponents;

	@XmlAttribute
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@XmlElement(name = "taskComponent")
	public List<TaskComponent> getTaskComponents() {
		return taskComponents;
	}

	public void setTaskComponents(List<TaskComponent> taskComponents) {
		this.taskComponents = taskComponents;
	}

}
