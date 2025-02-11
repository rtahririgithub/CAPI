package com.telus.cmb.common.enums;

import org.apache.commons.lang3.EnumUtils;

public enum BillNotificationActivityType {

	ACCOUNT_CANCEL, ACCOUNT_SUSPEND, SUBSCRIBER_CANCEL, SUBSCRIBER_SUSPEND;

	public static BillNotificationActivityType getBillNotificationActivityType(String name) {
		return EnumUtils.getEnum(BillNotificationActivityType.class, name);
	}

}
