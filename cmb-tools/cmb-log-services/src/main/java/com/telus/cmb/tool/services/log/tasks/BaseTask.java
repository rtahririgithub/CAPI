package com.telus.cmb.tool.services.log.tasks;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.tool.services.log.config.FilePathConfig;
import com.telus.cmb.tool.services.log.config.FilePathConfigRT;
import com.telus.cmb.tool.services.log.config.domain.app.Application;
import com.telus.cmb.tool.services.log.config.domain.task.TaskApplication;
import com.telus.cmb.tool.services.log.config.domain.task.TaskComponent;
import com.telus.cmb.tool.services.log.config.domain.task.TaskEnvironment;
import com.telus.cmb.tool.services.log.domain.DateRangeFilter;
import com.telus.cmb.tool.services.log.service.LogFileService;
import com.telus.cmb.tool.services.log.utils.ConfigUtil;

public abstract class BaseTask implements TaskExecutor {
	
	@Autowired
	LogFileService logFileService;
	
	@Autowired
	FilePathConfigRT filePathConfigRT;
	
	protected FilePathConfig filePathConfig = FilePathConfig.getInstance();	
	protected TaskEnvironment taskEnvironment;
	private static final String DEFAULT_DATE_FORMAT = "dd MMM yyyy";
	
	public void initialize(TaskEnvironment taskEnvironment) {
		this.taskEnvironment = taskEnvironment;
	}
		
	protected String getTaskParam(String paramName, String defaultValue) {
		String value = taskEnvironment.getTaskParamMap().get(paramName);
		return value != null ? value : defaultValue;
	}
	
	protected int getTaskParam(String paramName, int defaultValue) {
		String value = taskEnvironment.getTaskParamMap().get(paramName);
		return StringUtils.isNumeric(value) ? Integer.parseInt(value) : defaultValue;
	}
		
	protected long getTaskParam(String paramName, long defaultValue) {
		String value = taskEnvironment.getTaskParamMap().get(paramName);
		return StringUtils.isNumeric(value) ? Long.parseLong(value) : defaultValue;
	}
	
	protected DateRangeFilter getDateRangeFilter(TaskApplication taskApplication, TaskComponent taskComponent) {
		
		DateRangeFilter dateRangeFilter = new DateRangeFilter();	
		dateRangeFilter.setStartDate(taskComponent.getStartDate());
		dateRangeFilter.setEndDate(taskComponent.getEndDate());
		Application app = filePathConfig.getApplication(taskEnvironment.getShortname(), taskApplication.getShortname());
		String timestampFormat = ConfigUtil.getTimestampFormat(app, app.getComponent(taskComponent.getName()));
		dateRangeFilter.setDateFormat(StringUtils.isBlank(timestampFormat) ? DEFAULT_DATE_FORMAT : timestampFormat);
		
		return dateRangeFilter;
	}
	
}
