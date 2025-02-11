package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.TransactionEventInfo;

public class ActivityDetailMapper {

	public static com.telus.cmb.common.kafka.subscriber_v2.ActivityDetail mapActivityDetail(TransactionEventInfo eventInfo) {
		com.telus.cmb.common.kafka.subscriber_v2.ActivityDetail info = new com.telus.cmb.common.kafka.subscriber_v2.ActivityDetail();
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
