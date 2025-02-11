/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.subscriber.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
import com.telus.cmb.subscriber.kafka.SubscriberEventPayloadFactory;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class ServiceAgreementEventPublisherImpl implements ServiceAgreementEventPublisher {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");


	private static final String SERVICE_AGREEMENT_CHANGE = "SERVICE_AGREEMENT_CHANGE";
	private static final String CLIENTAPI_CMDB_ID = "5284";

	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}

	@Override
	public void publishServiceAgreementChangeEvent(final AccountInfo accountInfo, final SubscriberInfo subscriberInfo,final SubscriberContractInfo newContractInfo,
			final SubscriberContractInfo oldContractInfo,final EquipmentInfo newPrimaryEquipmentInfo,final String dealerCode, final String salesRepCode,
			final AuditInfo auditInfo, final boolean notificationSuppressionInd) {
		
		logger.info("begin publishServiceAgreementChangeEvent for ban = [ "+ accountInfo.getBanId() + " ] ," + "subscriberId = [ "+ subscriberInfo.getSubscriberId() + " ]");
		
		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, SERVICE_AGREEMENT_CHANGE);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.SERVICE_AGREEMENT_CHANGE.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(SubscriberEventPayloadFactory.createServiceAgreementChangeEvent(accountInfo,subscriberInfo,newContractInfo, oldContractInfo,newPrimaryEquipmentInfo,dealerCode,salesRepCode,auditInfo,notificationSuppressionInd));
				}
			}, KafkaEventType.SERVICE_AGREEMENT_CHANGE,KafkaContentType.JSON);
			logger.info("end publishServiceAgreementChangeEvent for ban = [ "+accountInfo.getBanId()+" ] ,"+ "subscriberId = [ "+subscriberInfo.getSubscriberId()+" ]");
		} 
	}
	

