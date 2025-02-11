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

public class AccountStatusEventPublisherImpl implements AccountStatusEventPublisher  {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String ACCOUNT_STATUS_CHANGE = "ACCOUNT_STATUS_CHANGE";

	
	private static final String CLIENTAPI_CMDB_ID = "5284";

	
	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void publishAccountCancel(final AccountInfo accountInfo,final List<String> phoneNumbers ,final Date activityDate,final String activityReasonCode, final String depositReturnMethod, 
			final String waiveReason, final String userMemoText, final boolean portOutActivityInd,final boolean brandPortOutActivityInd,final AuditInfo auditInfo
			,final Date transactionDate,final boolean activityDueToPrimaryCancelInd,final boolean notificationSuppressionInd ) {
		
		if(!AppConfiguration.isKafkaAccountCancelEnabled()) {
			logger.info("publishAccountCancel - kafka account call is disabled");
			return;
		}	
		
		logger.info("begin publishAccountCancel for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, ACCOUNT_STATUS_CHANGE);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.ACCOUNT_STATUS_CHANGE.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createAccountCancel(accountInfo, phoneNumbers, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, portOutActivityInd, 
							brandPortOutActivityInd,auditInfo, transactionDate, activityDueToPrimaryCancelInd,notificationSuppressionInd));
					
				}
			}, KafkaEventType.ACC_CANCEL,KafkaContentType.JSON);
		logger.info("end publishAccountCancel for ban = [ "+ accountInfo.getBanId() + " ] ");
	}

	@Override
	public void publishAccountCancelPortOut(final AccountInfo accountInfo,
			final List<String> phoneNumbers, final Date activityDate,
			final String activityReasonCode, final boolean portOutActivityInd,
			final boolean brandPortOutActivityInd, final AuditInfo auditInfo,
			final Date transactionDate, final boolean activityDueToPrimaryCancelInd,final boolean notificationSuppressionInd) {
	
		if(!AppConfiguration.isKafkaAccountCancelEnabled()) {
			logger.info("publishAccountCancelPortOut - kafka account call is disabled");
			return;
		}	
		
		logger.info("begin publishAccountCancelPortOut for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, ACCOUNT_STATUS_CHANGE);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.ACCOUNT_STATUS_CHANGE.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createAccountCancel(accountInfo, phoneNumbers, activityDate, activityReasonCode, null, null, null, portOutActivityInd, brandPortOutActivityInd,auditInfo, transactionDate, activityDueToPrimaryCancelInd,notificationSuppressionInd));
					
				}
			}, KafkaEventType.ACC_CANCEL_PORT_OUT,KafkaContentType.JSON);
		logger.info("end publishAccountCancelPortOut for ban = [ "+ accountInfo.getBanId() + " ] ");
	}
	
	}
	

