package com.telus.api.account;

import java.util.Date;

public interface BillNotificationHistoryRecord {
	
	public static int ACTIVITY_TYPE_EPOST = 1;
	public static int ACTIVITY_TYPE_PAPER_INV_SUPPR = 2;
	public static int ACTIVITY_ACTION_REGISTRATION = 1;
	public static int ACTIVITY_ACTION_CANCELLATION = 2;
	public static int ACTIVITY_ACTION_ACTIVATION = 3;
	public static int ACTIVITY_REASON_CUSTOMER_REQ = 1;
	public static int ACTIVITY_REASON_EPOST_REGISTRATION = 2;
	public static int ACTIVITY_REASON_EPOST_CANCELLATION = 3;

	String getBan();
	String getActivityType();
	String getActionType();
	String getActivityReason();
	String getActivityTypeFr();
	String getActionTypeFr();
	String getActivityReasonFr();
	String getSrcReferenceId();
	String getEmailAddress();
	Date getEffectiveStartDate();
	Date getEffectiveEndDate();
	boolean getMostRecentInd();

}
