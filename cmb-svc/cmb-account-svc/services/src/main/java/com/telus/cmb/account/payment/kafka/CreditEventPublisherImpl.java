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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaEventVersion;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
//import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class CreditEventPublisherImpl implements CreditEventPublisher{

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String CREATE_CREDIT = "CREATE_CREDIT";
	private static final String FOLLOWUP_APPROVAL = "FOLLOWUP_APPROVAL";

	private static final String CLIENTAPI_CMDB_ID = "5284";

	
	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void publishCreateCredit(final AccountInfo accountInfo,final CreditInfo creditInfo,final AuditInfo auditInfo,final Date transactionDate,final boolean notificationSuppressionInd ) {
		
		logger.info("begin publishCreateCredit for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, CREATE_CREDIT);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.CREATE_CREDIT.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createCredit(accountInfo, creditInfo, auditInfo, transactionDate,notificationSuppressionInd));
			}
		}, KafkaEventType.PAYMENT_METHOD_CHANGE, KafkaContentType.JSON);
		logger.info("end publishCreateCredit for ban = [ "+ accountInfo.getBanId() + " ] ");
	}

	@Override
	public void publishCreditForChargeAdj(final AccountInfo accountInfo,final CreditInfo creditInfo, final ChargeInfo chargeInfo, final AuditInfo auditInfo,final Date transactionDate, final boolean notificationSuppressionInd) {
	
		logger.info("begin publishCreditForChargeAdj for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, CREATE_CREDIT);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.CREATE_CREDIT.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createCreditForChargeAdj(accountInfo, creditInfo, chargeInfo,auditInfo, transactionDate, notificationSuppressionInd));
				}
			}, KafkaEventType.CREATE_CREDIT,KafkaContentType.JSON);
		logger.info("end publishCreditForChargeAdj for ban = [ "+ accountInfo.getBanId() + " ] ");
	}
	
	@Override
	public void publishFollowUpApprovalCredit(final AccountInfo accountInfo,final CreditInfo creditInfo, final FollowUpUpdateInfo followUpUpdateInfo,final AuditInfo auditInfo,final Date transactionDate, final boolean notificationSuppressionInd) {
		
		logger.info("begin publishFollowUpApprovalCredit for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, getFolowUpApprovalAdjEventType(followUpUpdateInfo));
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.FOLLOWUP_APPROVAL.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createFollowUpCredit(accountInfo, creditInfo, followUpUpdateInfo,auditInfo, transactionDate, notificationSuppressionInd));
				}
			}, KafkaEventType.FOLLOWUP_APPROVAL,KafkaContentType.JSON);
		logger.info("end publishFollowUpApprovalCredit for ban = [ "+ accountInfo.getBanId() + " ] ");
	}
	
	private String getFolowUpApprovalAdjEventType(FollowUpUpdateInfo fuui) {
		String eventType = FOLLOWUP_APPROVAL+".";
		// adjustment follow up criteria: in FOLLOW_UP table FU_TYPE=ADJT and FU_TEXT leading string is either TYPE=ADB or TYPE=ADC
		eventType = eventType + fuui.getFollowUpType()+".";
		if (fuui.isFollowUpApprovalForChargeAdj()) {
			eventType = eventType + FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_BAN.substring(5);
		} else if (fuui.isFollowUpApprovalForManualCredit()) {
			eventType = eventType + FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_CHARGE.substring(5);
		}
		return eventType;
	}

}

