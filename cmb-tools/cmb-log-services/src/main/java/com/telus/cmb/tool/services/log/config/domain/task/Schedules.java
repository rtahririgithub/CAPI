package com.telus.cmb.tool.services.log.config.domain.task;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Schedules {

	private List<Schedule> schedules;

	@XmlElement(name = "schedule")
	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}

}
