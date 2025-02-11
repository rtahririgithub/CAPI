package com.telus.cmb.tool.services.log.config.domain.task;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Task {

	private String name;
	private String shortname;
	private String type;
	private List<TaskEnvironment> taskEnvironments;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "taskEnvironment")
	public List<TaskEnvironment> getTaskEnvironments() {
		return taskEnvironments;
	}

	public void setTaskEnvironments(List<TaskEnvironment> taskEnvironments) {
		this.taskEnvironments = taskEnvironments;
	}

	public List<String> getEnvironmentList() {
		
		List<String> environments = new ArrayList<String>();
		if (this.taskEnvironments != null) {
			for (TaskEnvironment taskEnvironment : taskEnvironments) {
				environments.add(taskEnvironment.getShortname());
			}
		}
		
		return environments;
	}
	
}
