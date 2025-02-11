package com.telus.cmb.subscriber.kafka.json.mapper.v1;

import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.TransactionEventInfo;
import com.telus.cmb.common.kafka.TransactionEventMapper;
import com.telus.cmb.common.kafka.subscriber_v1.Subscriber;
import com.telus.cmb.common.mapping.AbstractSchemaMapper;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class SubscriberEventMapperV1 extends AbstractSchemaMapper<Subscriber, TransactionEventInfo> implements TransactionEventMapper{

	private static final Long KB_MASTER_SOURCE_ID = Long.valueOf(130L);
	private static final String RESOURCE_TYPE_CODE = "TN";

	
	public SubscriberEventMapperV1() {
		super(Subscriber.class, TransactionEventInfo.class);
	}
	
	@Override
	protected Subscriber performSchemaMapping(TransactionEventInfo source, Subscriber target){
		
		mapSubscriberData(source.getSubscriberInfo(), target);
		target.setPortIn(source.isPortIn());

		if(source.getAccountInfo().isPostpaidBusinessConnect()){
			target.setSeatData(SeatDataMapper.mapSeatData(source.getSubscriberInfo().getSeatData()));
		}
		target.setConsumerName(ConsumerNameMapper.mapConsumerName(source.getSubscriberInfo().getConsumerName()));
		target.setAccount(AccountMapper.mapAccountData(source.getAccountInfo()));
		target.setServiceAgreement(ServiceAgreementMapper.mapServiceAgreementData(source.getNewContractInfo()));
		if (!source.getEquipmentInfo().isHSIADummyEquipment() && !source.getEquipmentInfo().isVOIPDummyEquipment()) {
			target.setEquipment(EquipmentMapper.mapEquipment(source.getEquipmentInfo()));
		}

		// set eventTypes
		if (source.isActivation()) {
			target.setEventType(String.valueOf(KafkaEventType.SUBSCRIBER_ACTIVATE));
		}
		// map auditInfo
		target.setAuditInfo(mapAuditInfo(source.getAuditInfo(),source.getSubscriberInfo().getDealerCode(), source.getSubscriberInfo().getSalesRepId()));
		if(source.getAuditInfo()!=null){
			target.setOriginatorApplicationId(source.getAuditInfo().getOriginatorAppId());
		}
		return super.performSchemaMapping(source, target);
	}
	
	
	private static Subscriber mapSubscriberData(SubscriberInfo subscriberInfo,Subscriber target) {
		
		target.setSubscriptionId(String.valueOf(subscriberInfo.getSubscriptionId()));
		target.setSubscriberId(subscriberInfo.getSubscriberId());
		target.setPhoneNumber(subscriberInfo.getPhoneNumber());
		target.setPortType(subscriberInfo.getPortType());
		target.setBillingMasterSourceId(String.valueOf(KB_MASTER_SOURCE_ID));
		target.setPrimaryServiceResourceType(RESOURCE_TYPE_CODE);
		target.setActivityCode(subscriberInfo.getActivityCode());
		target.setActivityReasonCode(subscriberInfo.getActivityReasonCode());
		target.setProductType(subscriberInfo.getProductType());
		target.setStatus(String.valueOf(subscriberInfo.getStatus()));
		target.setStatusDate(subscriberInfo.getStatusDate());
		target.setLanguage(subscriberInfo.getLanguage());
		target.setEmailAddress(subscriberInfo.getEmailAddress());
		target.setStartServiceDate(subscriberInfo.getStartServiceDate());
		return target;
	}

	private static com.telus.cmb.common.kafka.subscriber_v1.AuditInfo mapAuditInfo(AuditInfo info,String dealerCode,String salesRepCode) {
		com.telus.cmb.common.kafka.subscriber_v1.AuditInfo auditInfo = new com.telus.cmb.common.kafka.subscriber_v1.AuditInfo();
		auditInfo.setDealerCode(dealerCode);
		auditInfo.setSalesRepCode(salesRepCode);
		if(auditInfo!=null){
			auditInfo.setOriginatorAppId(info.getOriginatorAppId());
			auditInfo.setKbuserId(info.getUserId());
		}
		return auditInfo;
	}
}
