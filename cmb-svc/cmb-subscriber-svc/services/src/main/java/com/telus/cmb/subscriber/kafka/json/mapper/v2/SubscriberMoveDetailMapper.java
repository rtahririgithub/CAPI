package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.TransactionEventInfo;

public class SubscriberMoveDetailMapper {
	
	public static com.telus.cmb.common.kafka.subscriber_v2.SubscriberMoveDetail mapSubscriberMoveDetail(TransactionEventInfo eventInfo) {
		com.telus.cmb.common.kafka.subscriber_v2.SubscriberMoveDetail moveDetail = new com.telus.cmb.common.kafka.subscriber_v2.SubscriberMoveDetail();
		moveDetail.setOwnershipChangeInd(eventInfo.isTransferOwnership());
		moveDetail.setTargetBan(eventInfo.getTargetBan());
		return moveDetail;
	}
	
}
