package com.telus.eas.equipment.info;

/**
 * Title: Telus Domain Project -KB61 Description: Copyright: Copyright (c) 2002
 * Company:
 * 
 * @author @version 1.0
 */

import com.telus.api.dealer.HoursOfOperation;
import com.telus.eas.framework.info.Info;

public class HoursOfOperationInfo extends Info implements HoursOfOperation {

	static final long serialVersionUID = 1L;

	private String openTime;

	private String closeTime;

	private int day;

	// constructors
	public HoursOfOperationInfo() {
	}

	public String getCloseTime() {
		return closeTime;
	}

	public int getDay() {
		return day;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("HoursOfOperationInfo:{\n");
		s.append("    openTime=[").append(openTime).append("]\n");
		s.append("    closeTime=[").append(closeTime).append("]\n");
		s.append("    day=[").append(day).append("]\n");
		s.append("}");

		return s.toString();
	}

}