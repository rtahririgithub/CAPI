package com.telus.cmb.tool.services.log.config.domain.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;

public class TaskComponent {

	private String name;
	private String daysBefore;
	private String daysAfter;
	private List<TaskGrepString> taskGrepStrings;
	

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "daysbefore")
	public String getDaysBefore() {
		return daysBefore;
	}

	public void setDaysBefore(String daysBefore) {
		this.daysBefore = daysBefore;
	}

	@XmlAttribute(name = "daysafter")
	public String getDaysAfter() {
		return daysAfter;
	}

	public void setDaysAfter(String daysAfter) {
		this.daysAfter = daysAfter;
	}

	@XmlElement(name = "taskGrepString")
	public List<TaskGrepString> getTaskGrepStrings() {
		return taskGrepStrings;
	}

	public void setTaskGrepStrings(List<TaskGrepString> taskGrepStrings) {
		this.taskGrepStrings = taskGrepStrings;
	}
	
	public Set<String> getGrepCriteria() {		
		Set<String> grepStringList = new HashSet<String>();
		for (TaskGrepString taskGrepString : taskGrepStrings) {
			grepStringList.add(taskGrepString.getValue());
		}		
		return grepStringList;
	}
	
	public Date getStartDate() {
		Calendar calendar = getCalendarWithoutTime();
		calendar.add(Calendar.DAY_OF_YEAR, -getTotalDays(daysBefore));		
		return calendar.getTime();
	}
	
	public Date getEndDate() {
		Calendar calendar = getCalendarWithoutTime();		
		calendar.add(Calendar.DAY_OF_YEAR, getTotalDays(daysAfter));		
		return calendar.getTime();
	}
	
	private Calendar getCalendarWithoutTime() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar;
	}
	
	private int getTotalDays(String days) {
		if (StringUtils.isNumeric(days)) {
			return Integer.parseInt(days);
		}
		return 0;
	}
		
}
