/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.account.payment.kafka;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class MultiSubscriberStatusEventPublisherImpl implements MultiSubscriberStatusEventPublisher  {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String SUBSCRIBER_STATUS_CHANGE = "SUBSCRIBER_STATUS_CHANGE";

	private static final String CLIENTAPI_CMDB_ID = "5284";

	
	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void publishMultiSubscriberCancel(final AccountInfo accountInfo,final List<String> phoneNumbers,
			final List<String> waiveReasonCodeList, final Date activityDate,final String activityReasonCode, 
			final String depositReturnMethod,final String userMemoText, final AuditInfo auditInfo,
			final Date transactionDate, final boolean activityDueToPrimaryCancelInd,final boolean notificationSuppressionInd) {
		
		if(!AppConfiguration.isKafkaMultiSubscriberCancelEnabled()) {
			logger.info("publishMultiSubscriberCancel - kafka multi subscriber cancel call is disabled");
			return;
		}
		
		logger.info("begin publishMultiSubscriberCancelEvent for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, SUBSCRIBER_STATUS_CHANGE);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.MULTI_SUBSCRIBER_STATUS_CHANGE.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createMultiSubscriberCancel(accountInfo, phoneNumbers, waiveReasonCodeList,activityDate, activityReasonCode, depositReturnMethod, userMemoText, auditInfo, transactionDate, activityDueToPrimaryCancelInd,notificationSuppressionInd));
					
				}
			}, KafkaEventType.SUB_CANCEL,KafkaContentType.JSON);
		logger.info("end publishMultiSubscriberCancelEvent for ban = [ "+ accountInfo.getBanId() + " ] ");
		}
	
	}
	

