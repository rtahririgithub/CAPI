package com.telus.cmb.subscriber.kafka.json.mapper.v2;

import com.telus.cmb.common.kafka.subscriber_v2.Subscriber;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class SubscriberMapper {

	private static final Long KB_MASTER_SOURCE_ID = Long.valueOf(130L);
	private static final String RESOURCE_TYPE_CODE = "TN";

	public static Subscriber mapSubscriber(SubscriberInfo subscriberInfo) {
		Subscriber subscriber = new Subscriber();
		subscriber.setPortType(subscriberInfo.getPortType());
		subscriber.setSubscriptionId(String.valueOf(subscriberInfo.getSubscriptionId()));
		subscriber.setSubscriberId(subscriberInfo.getSubscriberId());
		subscriber.setPhoneNumber(subscriberInfo.getPhoneNumber());
		subscriber.setBillingMasterSourceId(String.valueOf(KB_MASTER_SOURCE_ID));
		subscriber.setPrimaryServiceResourceType(RESOURCE_TYPE_CODE);
		subscriber.setProductType(subscriberInfo.getProductType());
		subscriber.setStatus(String.valueOf(subscriberInfo.getStatus()));
		subscriber.setStatusDate(subscriberInfo.getStatusDate());
		subscriber.setLanguage(subscriberInfo.getLanguage());
		subscriber.setEmail(subscriberInfo.getEmailAddress());
		subscriber.setStartServiceDate(subscriberInfo.getStartServiceDate());
		return subscriber;
	}

	public static com.telus.cmb.common.kafka.subscriber_v2.AuditInfo mapAuditInfo(AuditInfo auditInfo,String dealerCode,String salesRepCode,String sessionId ) {
		com.telus.cmb.common.kafka.subscriber_v2.AuditInfo info = new com.telus.cmb.common.kafka.subscriber_v2.AuditInfo();
		info.setDealerCode(dealerCode);
		info.setSalesRepCode(salesRepCode);
		info.setSessionId(sessionId);
		if(auditInfo!=null){
			info.setOriginatorApplicationId(auditInfo.getOriginatorAppId());
			info.setKbUserId(auditInfo.getUserId());
		}
		return info;
	}
	
}
