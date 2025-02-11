package com.telus.cmb.subscriber.kafka;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberEvent;
import com.telus.cmb.subscriber.kafka.json.mapper.v2.SubscriberEventMapperV2;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

/**
 * Responsible for coordinating in composing(mapping) kafka message event 
 * and transforming into json payload.
 * 
 */
public class ChangePhoneNumberEventPayloadFactory {

	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	
	private ChangePhoneNumberEventPayloadFactory() {
	}

	//Create regular phone number change event
	public static String createPhoneNumberChanegEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, String oldPhoneNumber,
			AuditInfo auditInfo, String dealerCode, String salesRepCode,String sessionId,boolean notificationSuppressionInd) throws Exception {
		
		//Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.PHONENUMBER_CHANGE_REGULAR,accountInfo,
				subscriberInfo, oldPhoneNumber, auditInfo, sessionId,notificationSuppressionInd);
		
		return convertObjectToJson(new SubscriberEventMapperV2().mapToSchema(eventInfo));
	}
		

	//Create phone number change portIn event
	public static String createPhoneNumberChanegPortInEvent(KafkaEventType eventType,AccountInfo accountInfo,SubscriberInfo subscriberInfo, String oldPhoneNumber,
			String portProcessType,AuditInfo auditInfo, String dealerCode, String salesRepCode,String sessionId,boolean notificationSuppressionInd) throws Exception {
		
		//Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(eventType, accountInfo,
				subscriberInfo, oldPhoneNumber, auditInfo, sessionId,notificationSuppressionInd);
		eventInfo.setPortIn(true);
		eventInfo.setPortProcessType(portProcessType);
		
		return convertObjectToJson(new SubscriberEventMapperV2().mapToSchema(eventInfo));
	}
	
	private static TransactionEventInfo populateBaseTransactionEventInfo(KafkaEventType eventType, AccountInfo accountInfo,SubscriberInfo subscriberInfo, 
			String oldPhoneNumber,AuditInfo auditInfo,String sessionId,boolean notificationSuppressionInd) {
		TransactionEventInfo eventInfo = new TransactionEventInfo();
		eventInfo.setEventType(eventType);
		eventInfo.setTransactionDate(ServiceAgreementUtil.getLogicalDate());
		eventInfo.setAccountInfo(accountInfo);
		eventInfo.setSubscriberInfo(subscriberInfo);
		eventInfo.setOldPhoneNumber(oldPhoneNumber);
		eventInfo.setAuditInfo(auditInfo);
		eventInfo.setDealerCode(subscriberInfo.getDealerCode());
		eventInfo.setSalesRepCode(subscriberInfo.getSalesRepId());
		eventInfo.setSessionId(sessionId);
		return eventInfo;
	}
	
	private static String convertObjectToJson(Object obj) throws JsonProcessingException {
		// Set the date format
		ObjectMapper jsonMapper = new ObjectMapper();
		jsonMapper.addMixInAnnotations(SubscriberEvent.class, SubscriberEventMixIn.class);
		jsonMapper.setDateFormat(new SimpleDateFormat(SIMPLE_DATE_FORMAT));
		// Convert Object to Json
		return jsonMapper.writeValueAsString(obj);
	}
}
