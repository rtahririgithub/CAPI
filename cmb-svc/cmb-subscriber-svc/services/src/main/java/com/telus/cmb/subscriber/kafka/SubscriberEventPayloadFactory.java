package com.telus.cmb.subscriber.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.TransactionEventMapper;
import com.telus.cmb.common.kafka.subscriber_v2.SubscriberEvent;
import com.telus.cmb.subscriber.kafka.json.mapper.v1.SubscriberEventMapperV1;
import com.telus.cmb.subscriber.kafka.json.mapper.v2.SubscriberEventMapperV2;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class SubscriberEventPayloadFactory {
	
	private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
	
	// create subscriber activation event
	public static String createSubscriberActivationEvent(SubscriberInfo subscriberInfo, AccountInfo accountInfo, SubscriberContractInfo newContractInfo, 
			boolean isPortIn, String portProcessType,AuditInfo auditInfo) throws Exception {
		// Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.SUBSCRIBER_ACTIVATE, accountInfo, subscriberInfo, auditInfo, subscriberInfo.getDealerCode(),subscriberInfo.getSalesRepId(),false);
		// Set the transaction specific event data
		eventInfo.setNewContractInfo(newContractInfo);
		eventInfo.setEquipmentInfo(subscriberInfo.getEquipment0());
		eventInfo.setPortIn(isPortIn);
		eventInfo.setPortProcessType(portProcessType);
		return convertObjectToJson(getSubscriberEventMapper(KafkaEventVersion.SUBSCRIBER_ACTIVATE.getVersion()).mapToSchema(eventInfo));
	}
		
	// create subscriber cancel event
	public static String createSubscriberCancelEvent(SubscriberInfo subscriberInfo, AccountInfo accountInfo,Date activityDate, String activityReasonCode,String depositReturnMethod, 
			String waiveReason,String userMemoText, boolean portOutActivityInd,AuditInfo auditInfo,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd) throws Exception {
		// Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.SUB_CANCEL, accountInfo, subscriberInfo, auditInfo, null,null,notificationSuppressionInd);
		// Set the transaction specific event data
		eventInfo.setActivityDate(activityDate);
		eventInfo.setActivityReasonCode(activityReasonCode);
		eventInfo.setDepositReturnMethod(depositReturnMethod);
		eventInfo.getWaiveReasonCodeList().add(waiveReason);
		eventInfo.setUserMemoText(userMemoText);		
		eventInfo.setPortOutActivityInd(portOutActivityInd);
		eventInfo.setActivityDueToPrimaryCancelInd(activityDueToPrimaryCancelInd);
		return convertObjectToJson(getSubscriberEventMapper(KafkaEventVersion.SUBSCRIBER_STATUS_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	// create subscriber cancel event
	public static String createSubscriberCancelPortOutEvent(SubscriberInfo subscriberInfo, AccountInfo accountInfo,Date activityDate, String activityReasonCode, 
			boolean portOutActivityInd,boolean interBrandPortOutInd,AuditInfo auditInfo,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd) throws Exception {
		// Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.SUB_CANCEL_PORT_OUT, accountInfo, subscriberInfo, auditInfo, null,null,notificationSuppressionInd);
		// Set the transaction specific event data
		eventInfo.setActivityDate(activityDate);
		eventInfo.setActivityReasonCode(activityReasonCode);		
		eventInfo.setPortOutActivityInd(portOutActivityInd);
		eventInfo.setInterBrandPortOutInd(interBrandPortOutInd);
		eventInfo.setActivityDueToPrimaryCancelInd(activityDueToPrimaryCancelInd);

		return convertObjectToJson(getSubscriberEventMapper(KafkaEventVersion.SUBSCRIBER_STATUS_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	
		// create subscriber move event
	public static String createSubscriberMoveEvent(AccountInfo accountInfo,SubscriberInfo subscriberInfo, int targetBan, Date activityDate,String activityReasonCode, boolean transferOwnership,
			String userMemoText, String dealerCode, String salesRepCode,AuditInfo auditInfo,boolean notificationSuppressionInd) throws Exception {
		
		// Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.MOVE, accountInfo, subscriberInfo, auditInfo,dealerCode,salesRepCode,notificationSuppressionInd);
		
		// Set the transaction specific event data
		eventInfo.setActivityDate(activityDate);
		eventInfo.setActivityReasonCode(activityReasonCode);
		eventInfo.setUserMemoText(userMemoText);
		eventInfo.setTargetBan(targetBan);
		eventInfo.setTransferOwnership(transferOwnership);
		
		return convertObjectToJson(getSubscriberEventMapper(KafkaEventVersion.SUBSCRIBER_MOVE.getVersion()).mapToSchema(eventInfo));
	}
				
	// create service agreement change event
	public static String createServiceAgreementChangeEvent(AccountInfo accountInfo, SubscriberInfo subscriberInfo,SubscriberContractInfo newContractInfo,SubscriberContractInfo oldContractInfo, 
			EquipmentInfo equipmentInfo,String dealerCode,String salesRepCode, AuditInfo auditInfo,boolean notificationSuppressionInd) throws Exception {
		
		// Set the base transaction event data
		TransactionEventInfo eventInfo = populateBaseTransactionEventInfo(KafkaEventType.SERVICE_AGREEMENT_CHANGE, accountInfo, subscriberInfo, auditInfo, dealerCode,salesRepCode,notificationSuppressionInd);
		// Set the transaction specific event data
		eventInfo.setNewContractInfo(newContractInfo);
		eventInfo.setOldContractInfo(oldContractInfo);
		eventInfo.setEquipmentInfo(equipmentInfo);

		return convertObjectToJson(getSubscriberEventMapper(KafkaEventVersion.SERVICE_AGREEMENT_CHANGE.getVersion()).mapToSchema(eventInfo));
	}
	
	private static TransactionEventMapper getSubscriberEventMapper(String versionNum) {
		if ("1".equals(versionNum)) {
			return new SubscriberEventMapperV1();
		} else if ("2".equals(versionNum)) {
			return new SubscriberEventMapperV2();
		}
		return null;
	}

	
	private static TransactionEventInfo populateBaseTransactionEventInfo(KafkaEventType eventType, AccountInfo accountInfo,SubscriberInfo subscriberInfo, AuditInfo auditInfo,String dealerCode, 
			String salesRepCode,boolean notificationSuppressionInd) {
		TransactionEventInfo eventInfo = new TransactionEventInfo();
		eventInfo.setEventType(eventType);
		eventInfo.setTransactionDate(ServiceAgreementUtil.getLogicalDate());
		eventInfo.setAccountInfo(accountInfo);
		eventInfo.setSubscriberInfo(subscriberInfo);
		eventInfo.setAuditInfo(auditInfo);
		eventInfo.setDealerCode(dealerCode);
		eventInfo.setSalesRepCode(salesRepCode);
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
