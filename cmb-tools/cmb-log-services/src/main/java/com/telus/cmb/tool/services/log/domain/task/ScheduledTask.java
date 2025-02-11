package com.telus.cmb.tool.services.log.domain.task;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import com.telus.cmb.tool.services.log.config.domain.task.Task;
import com.telus.cmb.tool.services.log.config.domain.task.TaskEnvironment;
import com.telus.cmb.tool.services.log.utils.StringUtil;

public class ScheduledTask {

	private String name;
	private String shortname;
	private String description;
	private String schedule;
	private String environment;
	private ScheduledFuture<?> scheduledFuture;
	private Date lastManualExecution;
	
	private long COOLDOWN_TIME = 300000;

	public ScheduledTask(Task task, TaskEnvironment environment, ScheduledFuture<?> scheduledFuture, String description) {
		this.name = task.getName();
		this.setShortname(task.getShortname());
		this.schedule = StringUtil.describeCron(environment.getSchedule().getCron());
		this.environment = environment.getShortname();
		this.scheduledFuture = scheduledFuture;
		this.description = description;
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

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public ScheduledFuture<?> getScheduledFuture() {
		return scheduledFuture;
	}

	public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
		this.scheduledFuture = scheduledFuture;
	}

	public void setLastManualExecution(Date lastManualExecution) {
		this.lastManualExecution = lastManualExecution;
	}

	public boolean isUnderCooldown() {
		if (lastManualExecution != null) {
			return (new Date().getTime() - lastManualExecution.getTime()) < COOLDOWN_TIME;
		}
		return false;
	}
	
}
