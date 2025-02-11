package com.telus.cmb.tool.services.log.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public class DateRangeFilter {

	private String dateFormat;
	private Date startDate;
	private Date endDate;

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getTrueEndDate() {				
		// If end date is before start date, take the next day after start date
		if (DateUtils.truncatedCompareTo(endDate, startDate, Calendar.DATE) < 0) {
			return DateUtils.addDays(startDate, 1);
		}
		// To make sure the end date is covered, take the next day after end date
		return DateUtils.addDays(endDate, 1);
	}

	public String getDisplayText() {
		if (startDate != null && endDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			return " between " + sdf.format(startDate) + " to " + sdf.format(endDate);
		}
		return "";
	}
	
}
