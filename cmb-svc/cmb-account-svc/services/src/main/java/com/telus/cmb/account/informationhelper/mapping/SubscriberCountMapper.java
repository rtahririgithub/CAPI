package com.telus.cmb.account.informationhelper.mapping;

import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberCountInfo;

public class SubscriberCountMapper {
	public static void mapSubscriberCount(AccountInfo accountInfo, SubscriberCountInfo subCountInfo) {
		if (accountInfo == null || subCountInfo == null) {
			return;
		}
		
		accountInfo.setActiveSubscribersCount(subCountInfo.getActiveSubscribersCount());
		accountInfo.setReservedSubscribersCount(subCountInfo.getReservedSubscribersCount());
		accountInfo.setSuspendedSubscribersCount(subCountInfo.getSuspendedSubscribersCount());
		accountInfo.setCancelledSubscribersCount(subCountInfo.getCancelledSubscribersCount());
		accountInfo.setAllActiveSubscribersCount(subCountInfo.getAllActiveSubscribersCount());
		accountInfo.setAllReservedSubscribersCount(subCountInfo.getAllReservedSubscribersCount());
		accountInfo.setAllSuspendedSubscribersCount(subCountInfo.getAllSuspendedSubscribersCount());
		accountInfo.setAllCancelledSubscribersCount(subCountInfo.getAllCancelledSubscribersCount());
	}
}
