package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.account.EBillRegistrationReminder;
import com.telus.eas.framework.info.Info;

public class EBillRegistrationReminderInfo extends Info implements EBillRegistrationReminder {

	static final long serialVersionUID = 1L;

	private String activatedPhoneNumber;

	private Date lastNotificationDate;

	public Date getLastNotificationDate() {
		return lastNotificationDate;
	}

	public void setLastNotificationDate(Date lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	public String getActivatedPhoneNumber() {
		return activatedPhoneNumber;
	}

	public void setActivatedPhoneNumber(String activatedPhoneNumber) {
		this.activatedPhoneNumber = activatedPhoneNumber;
	}

	public String toString() {
		StringBuffer s = new StringBuffer();

		s.append("EBillRegistrationReminderInfo:{\n");
		s.append("    activatedPhoneNumber =[").append(activatedPhoneNumber).append("]\n");
		s.append("    lastNotificationDate =[").append(lastNotificationDate).append("]\n");
		s.append("}");

		return s.toString();
	}

}
