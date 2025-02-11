package com.telus.cmb.tool.services.log.config;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.telus.cmb.tool.services.log.config.domain.task.Schedule;
import com.telus.cmb.tool.services.log.config.domain.task.Schedules;
import com.telus.cmb.tool.services.log.config.domain.task.Task;

public class ScheduledTaskConfig extends BaseConfig {

	private static final String TASK_XML_CONFIG_FILENAME_REGEX = "task-.*[.]xml";

	private Map<String, Task> taskCache;
	private Map<String, Schedule> scheduleCache;

	private static ScheduledTaskConfig INSTANCE = null;

	private ScheduledTaskConfig() {
	}

	public synchronized static ScheduledTaskConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ScheduledTaskConfig();
			INSTANCE.initialize();
		}
		return INSTANCE;
	}
	
	private void initialize() {
		try {
			initializeScheduleCache();
			initializeTaskCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initializeTaskCache() throws JAXBException {

		taskCache = new LinkedHashMap<String, Task>();		
		File resourceFolder = new File(getRootXmlConfigFolder() + File.separator + "tasks");
		File[] taskConfigFiles = resourceFolder.listFiles(new FilenameFilter() {
			public boolean accept(File resourceFolder, String filename) {
				return filename.matches(TASK_XML_CONFIG_FILENAME_REGEX);
			}
		});

		for (File taskConfigFile : taskConfigFiles) {
			Task task = parseConfigXml(Task.class, taskConfigFile);
			taskCache.put(task.getShortname(), task);
		}
	}

	private void initializeScheduleCache() throws JAXBException {
		scheduleCache = new HashMap<String, Schedule>();
		for (Schedule schedule : parseConfigXml(Schedules.class).getSchedules()) {
			scheduleCache.put(schedule.getName(), schedule);
		}
	}

	public List<Task> getTaskList() {
		return new ArrayList<Task>(taskCache.values());
	}
	
	public Task getTask(String shortname) {
		return taskCache.get(shortname);
	}
	
	public Schedule getSchedule(String name) {
		return scheduleCache.get(name);
	}
	
}
