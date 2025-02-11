package com.telus.cmb.tool.services.log.config.domain.task;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang3.StringUtils;

import com.telus.cmb.tool.services.log.utils.StringUtil;

public class Schedule {

	private String name;
	private String seconds;
	private String minutes;
	private String hours;
	private String dayOfMonth;
	private String month;
	private String dayOfWeek;

	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getSeconds() {
		return seconds;
	}

	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}

	@XmlAttribute
	public String getMinutes() {
		return minutes;
	}

	public void setMinutes(String minutes) {
		this.minutes = minutes;
	}

	@XmlAttribute
	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	@XmlAttribute
	public String getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	@XmlAttribute
	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@XmlAttribute
	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getCron() {
		if (StringUtils.isNotBlank(seconds) && StringUtils.isNotBlank(minutes) && StringUtils.isNotBlank(hours) && StringUtils.isNotBlank(dayOfMonth) && StringUtils.isNotBlank(month) && StringUtils.isNotBlank(dayOfWeek)) {
			return seconds + " " + minutes + " " + hours + " " + dayOfMonth + " " + month + " " + dayOfWeek;
		}
		return null;
	}
	
	public String getCronDescription() {		
		return StringUtil.describeCron(getCron());
	}
	
}
