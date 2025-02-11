package com.telus.cmb.account.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.TransactionEventInfo;

public class ActivityDetailMapper {

	public static com.telus.cmb.common.kafka.account_v1_0.ActivityDetail mapActivityDetail(TransactionEventInfo eventInfo) {
		com.telus.cmb.common.kafka.account_v1_0.ActivityDetail info = new com.telus.cmb.common.kafka.account_v1_0.ActivityDetail();
		info.setActivityDate(eventInfo.getActivityDate());
		info.setActivityReasonCd(eventInfo.getActivityReasonCode());
		info.setDepositReturnMethod(eventInfo.getDepositReturnMethod());
		info.setPortOutActivityInd(eventInfo.isPortOutActivityInd());
		if(eventInfo.isPortOutActivityInd()){
			info.setInterBrandPortOutInd(eventInfo.isInterBrandPortOutInd());
		}
		info.setActivityDueToPrimaryCancelInd(eventInfo.isActivityDueToPrimaryCancelInd());
		return info;
	}
}
