package com.telus.cmb.tool.services.log.config.domain.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.collections4.CollectionUtils;

import com.telus.cmb.tool.services.log.config.ScheduledTaskConfig;

public class TaskEnvironment {

	private String shortname;
	private String scheduleName;
	private List<TaskApplication> taskApplications;
	private List<TaskParam> taskParams;
	private List<NotifyParty> notifyParties;
	private Map<String, String> taskParamMap;
	
	@XmlAttribute
	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@XmlAttribute(name = "schedule")
	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	@XmlElement(name = "taskApplication")
	public List<TaskApplication> getTaskApplications() {
		return taskApplications;
	}

	public void setTaskApplications(List<TaskApplication> taskApplications) {
		this.taskApplications = taskApplications;
	}

	@XmlElement(name = "taskParam")
	public List<TaskParam> getTaskParams() {
		return taskParams;
	}

	public void setTaskParams(List<TaskParam> taskParams) {
		this.taskParams = taskParams;
	}

	@XmlElement(name = "notifyParty")
	public List<NotifyParty> getNotifyParties() {
		return notifyParties;
	}

	public void setNotifyParties(List<NotifyParty> notifyParties) {
		this.notifyParties = notifyParties;
	}
	
	public List<String> getNotificationEmails() {
		return getNotificationEmails(null);
	}
	
	public List<String> getNotificationEmails(Boolean success) {
		
		List<String> emailList = new ArrayList<String>();
		for (NotifyParty notifyParty : notifyParties) {			
			if (notifyParty.isEmailEligible(success)) {
				emailList.add(notifyParty.getEmail());	
			} 
		}
		
		return emailList;
	}
	
	public Schedule getSchedule() { 
		Schedule schedule = ScheduledTaskConfig.getInstance().getSchedule(this.scheduleName);
		return schedule == null ? new Schedule() : schedule;
	}
		
	public Map<String, String> getTaskParamMap() {
		
		if (taskParamMap == null) {
			taskParamMap = new HashMap<String, String>();
			if (CollectionUtils.isNotEmpty(taskParams)) {
				for (TaskParam taskParam : taskParams) {
					taskParamMap.put(taskParam.getName(), taskParam.getValue());
				}	
			}
		}
		
		return taskParamMap;
	}

}
