package com.telus.cmb.tool.services.log.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.ScheduledTaskConfig;
import com.telus.cmb.tool.services.log.config.domain.Environment;
import com.telus.cmb.tool.services.log.config.domain.task.Schedule;
import com.telus.cmb.tool.services.log.config.domain.task.Task;
import com.telus.cmb.tool.services.log.config.domain.task.TaskEnvironment;
import com.telus.cmb.tool.services.log.controllers.LogFileReadController;
import com.telus.cmb.tool.services.log.domain.task.AvailableTask;
import com.telus.cmb.tool.services.log.domain.task.ScheduledTask;
import com.telus.cmb.tool.services.log.tasks.BaseTask;
import com.telus.cmb.tool.services.log.tasks.ServiceFactory;
import com.telus.cmb.tool.services.log.tasks.ServiceFactory.TaskEnum;

@Service
public class TaskSchedulerServiceImpl implements TaskSchedulerService {

	private static Logger logger = Logger.getLogger(LogFileReadController.class);
	private FilePathConfig filePathConfig = FilePathConfig.getInstance();

	private ScheduledTaskConfig taskConfig = ScheduledTaskConfig.getInstance();
	private HashMap<String, AvailableTask> availableTasks;
	private HashMap<String, ScheduledTask> scheduledTasks;
	private String currentProdEnv;

	@Autowired
	@Qualifier("taskScheduler")
	private ThreadPoolTaskScheduler scheduler;

	@Autowired
	ServiceFactory taskServiceFactory;
	
	@PostConstruct
	private void initialize() {		
		availableTasks = new LinkedHashMap<String, AvailableTask>();
		scheduledTasks = new LinkedHashMap<String, ScheduledTask>();
		initializeCurrentEnvironments();
		initializeAvailableTasks();
	}
	
	private void initializeCurrentEnvironments() {
		for (Environment environment : filePathConfig.getEnvironments()) {
			if (environment.isProduction() && environment.isCurrent()) {
				currentProdEnv = environment.getShortname();
			}
		}
	}
	
	private void initializeAvailableTasks() {
		
		for (Task task : taskConfig.getTaskList()) {
			// Make sure the task actually has configured environments
			if (!CollectionUtils.isEmpty(task.getTaskEnvironments())) {
				TaskEnum taskType = TaskEnum.getTaskEnumByType(task.getType());
				// Make sure the task type is a valid one that has an implementation
				if (taskType != null) {
					BaseTask baseTask = taskServiceFactory.getTask(taskType.getTaskName());
					baseTask.initialize(task.getTaskEnvironments().get(0));
					AvailableTask availableTask = new AvailableTask(task);
					availableTask.setDescription(baseTask.getDescription());
					availableTask.setCurrentProdEnv(currentProdEnv);
					removeNonScheduledTaskEnvironments(task, availableTask);
					if (!availableTask.getEnvironments().isEmpty()) {
						availableTasks.put(task.getShortname(), availableTask);
					}
				}
			}
		}
	}
	
	private void removeNonScheduledTaskEnvironments(Task task, AvailableTask availableTask) {
		for (TaskEnvironment taskEnv : task.getTaskEnvironments()) {
			if (taskEnv.getSchedule().getCron() == null) {
				availableTask.removeEnvironment(taskEnv.getShortname());
			}
		}
	}
	
	@Override
	public List<ScheduledTask> getScheduledTasks() {
		return new ArrayList<ScheduledTask>(scheduledTasks.values());
	}

	@Override
	public List<AvailableTask> getAvailableTasks() {
		return new ArrayList<AvailableTask>(availableTasks.values());
	}
	
	@Override
	public void scheduleTask(String taskShortname) {		
		Task task = taskConfig.getTask(taskShortname);
		if (task != null) {
			for (TaskEnvironment taskEnvironment : task.getTaskEnvironments()) {				
				scheduleTask(task, taskEnvironment);
			}			
		}
	}
	
	@Override
	public void scheduleTask(String taskShortname, List<String> environments) {
		
		Task task = taskConfig.getTask(taskShortname);
		if (task != null) {
			for (TaskEnvironment taskEnvironment : task.getTaskEnvironments()) {
				if (!CollectionUtils.isEmpty(environments) && environments.contains(taskEnvironment.getShortname())) {
					scheduleTask(task, taskEnvironment);
				}
			}			
		}
	}
	
	@Override
	public void scheduleAllTasks() {		
		for (Task task : taskConfig.getTaskList()) {
			for (TaskEnvironment taskEnvironment : task.getTaskEnvironments()) {
				scheduleTask(task, taskEnvironment);
			}
		}		
	}

	@Override
	public void endTask(String taskShortname, String envShortname) {
		
		String scheduledTaskId = getScheduledTaskId(taskShortname, envShortname);
		ScheduledTask scheduledTask = scheduledTasks.get(scheduledTaskId);
		if (scheduledTask != null) {
			logger.info("Ending task [" + taskShortname + "] for environment [" + envShortname + "]");
			String description = scheduledTask.getDescription();
			scheduledTask.getScheduledFuture().cancel(true);
			scheduledTasks.remove(scheduledTaskId);			
			addAvailableTask(taskShortname, envShortname, description);			
		} else {
			logger.info("Cannot find task [" + taskShortname + "] for environment [" + envShortname + "]");
		}
	}

	@Override
	public void endAllTasks() {
		for (ScheduledTask scheduledTask : scheduledTasks.values()) {
			scheduledTask.getScheduledFuture().cancel(true);
		}
		// scheduler.shutdown();
		scheduledTasks.clear();
	}
	
	@Override
	public void executeTask(String taskShortname, String envShortname) {

		Task task = taskConfig.getTask(taskShortname);
		if (task != null) {
			for (TaskEnvironment taskEnvironment : task.getTaskEnvironments()) {
				if (StringUtils.equals(taskEnvironment.getShortname(), envShortname)) {
					BaseTask baseTask = taskServiceFactory.getTask(TaskEnum.getTaskEnumByType(task.getType()).getTaskName());
					baseTask.initialize(taskEnvironment);
					baseTask.run();
					ScheduledTask scheduledTask = scheduledTasks.get(getScheduledTaskId(task.getShortname(), taskEnvironment.getShortname()));
					if (scheduledTask != null) {
						scheduledTask.setLastManualExecution(new Date());
					}
					break;
				}
			}			
		}
	}
	private void scheduleTask(Task task, TaskEnvironment taskEnvironment) {
		
		String scheduledTaskId = getScheduledTaskId(task.getShortname(), taskEnvironment.getShortname());
		if (scheduledTasks.get(scheduledTaskId) == null) {
			Schedule taskSchedule = taskConfig.getSchedule(taskEnvironment.getScheduleName());
			if (taskSchedule != null) {
				
				// Get the task by type and initialize it to the specified environment
				logger.info("Scheduling task..." + task.getName() + " in " + task.getShortname());				
				BaseTask baseTask = taskServiceFactory.getTask(TaskEnum.getTaskEnumByType(task.getType()).getTaskName());
				baseTask.initialize(taskEnvironment);
				
				String cron = taskSchedule.getCron();
				if (cron != null) {
					// Schedule the task
					ScheduledFuture<?> scheduledFuture = scheduler.schedule(baseTask, new CronTrigger(cron));
					
					// Add the task to scheduledMap and remove it from the availableMap
					ScheduledTask scheduledTask = new ScheduledTask(task, taskEnvironment, scheduledFuture, baseTask.getDescription());
					scheduledTasks.put(scheduledTaskId, scheduledTask);
					removeAvailableTask(task.getShortname(), taskEnvironment.getShortname());
				}
			}			
		} else {
			logger.info("Task " + task.getName() + " in " + task.getShortname() + "is already running.");
		}		
	}
	
	private void removeAvailableTask(String taskShortname, String envShortname) {
		AvailableTask availableTask = availableTasks.get(taskShortname);
		availableTask.removeEnvironment(envShortname);
		if (CollectionUtils.isEmpty(availableTask.getEnvironments())) {
			availableTasks.remove(taskShortname);
		}
	}
	
	private void addAvailableTask(String taskShortname, String envShortname, String description) {
		
		AvailableTask availableTask = availableTasks.get(taskShortname);
		if (availableTask == null) {
			Task task = taskConfig.getTask(taskShortname);
			availableTask = new AvailableTask(task);
			availableTask.setDescription(description);
			availableTask.setCurrentProdEnv(currentProdEnv);
			availableTask.removeAllEnvironments();
		}		
		availableTask.addEnvironment(envShortname);		
		availableTasks.put(taskShortname, availableTask);
	}
	
	private String getScheduledTaskId(String task, String environment) {
		return task + ":" + environment;
	}
}
