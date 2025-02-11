package com.telus.cmb.tool.services.log.service;

import java.util.List;

import com.telus.cmb.tool.services.log.domain.task.AvailableTask;
import com.telus.cmb.tool.services.log.domain.task.ScheduledTask;

public interface TaskSchedulerService {
	
	public List<ScheduledTask> getScheduledTasks();

	public List<AvailableTask> getAvailableTasks();
	
	public void scheduleTask(String taskShortname);

	public void scheduleTask(String taskShortname, List<String> environments);
	
	public void scheduleAllTasks();
	
	public void endTask(String taskShortname, String envShortname);
	
	public void endAllTasks();

	public void executeTask(String taskShortname, String envShortname);
}
