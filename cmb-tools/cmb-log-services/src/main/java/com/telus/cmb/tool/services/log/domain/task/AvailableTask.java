package com.telus.cmb.tool.services.log.domain.task;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.telus.cmb.tool.services.log.config.domain.task.Task;

public class AvailableTask {

	private String name;
	private String shortname;
	private String description;
	private Set<String> environments;
	private String currentProdEnv;

	public AvailableTask(Task task) {
		this.name = task.getName();
		this.setShortname(task.getShortname());
		this.environments = new LinkedHashSet<String>(task.getEnvironmentList());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getEnvironments() {
		return environments;
	}

	public void setEnvironments(Set<String> environments) {
		this.environments = environments;
	}

	public String getCurrentProdEnv() {
		return currentProdEnv;
	}

	public void setCurrentProdEnv(String currentProdEnv) {
		this.currentProdEnv = currentProdEnv;
	}

	public void addEnvironment(String environment) {
		if (environments == null) {
			environments = new HashSet<String>();
		}
		environments.add(environment);
	}

	public void removeEnvironment(String environment) {
		if (environments != null) {
			environments.remove(environment);
		}
	}
	
	public void removeAllEnvironments() {
		if (environments != null) {
			environments.clear();
		}
	}

}
