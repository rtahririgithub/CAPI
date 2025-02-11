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
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class PaymentEventPublisherImpl implements PaymentEventPublisher {

	private static final Logger logger = LoggerFactory.getLogger("kafkaLogger");

	private static final String PAYMENT_METHOD_CHANGE = "PAYMENT_METHOD_CHANGE";
	private static final String MAKE_PAYMENT = "MAKE_PAYMENT";

	
	private static final String CLIENTAPI_CMDB_ID = "5284";

	
	private KafkaMessagePublisher publisher;

	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}
	
	@Override
	public void publishPaymentMethodChange(final AccountInfo accountInfo,final PaymentMethodInfo paymentMethodInfo,final AuditInfo auditInfo
			,final Date transactionDate,final boolean notificationSuppressionInd ) {
		
		logger.info("begin publishPaymentMethodChange for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, PAYMENT_METHOD_CHANGE);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.PAYMENT_METHOD_CHANGE.getVersion());
					message.addMetadata(PublisherConstants.PUBLISHER_CMDB_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createPaymentMethodChange(accountInfo,paymentMethodInfo, auditInfo, transactionDate,notificationSuppressionInd));
				}
			}, KafkaEventType.PAYMENT_METHOD_CHANGE,KafkaContentType.JSON);
		logger.info("end publishPaymentMethodChange for ban = [ "+ accountInfo.getBanId() + " ] ");
		}
	
	@Override
	public void publishMakePayment(final AccountInfo accountInfo,final PaymentInfo paymentInfo, final AuditInfo auditInfo,final Date transactionDate, final boolean notificationSuppressionInd) {

		logger.info("begin publishMakePayment for ban = [ "+ accountInfo.getBanId() + " ] ");

		publisher.publish(new KafkaMessageBuilder() {
				@Override
				public void populate(KafkaMessage message) throws Throwable {
					message.addMetadata(PublisherConstants.EVENT_NAME, MAKE_PAYMENT);
					message.addMetadata(PublisherConstants.NOTIFICATION_SUPPRESSION_IND, String.valueOf(notificationSuppressionInd));
					message.addMetadata(PublisherConstants.VERSION, KafkaEventVersion.MAKE_PAYMENT.getVersion());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, auditInfo.getUserId());
					message.addMetadata(PublisherConstants.TRIGGERED_BY_KB_APP_ID, auditInfo.getOriginatorAppId());
					message.addMetadata(PublisherConstants.ORIGINATING_APPLICATION_ID, CLIENTAPI_CMDB_ID);
					message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
					message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
					message.addMetadata(PublisherConstants.ACCOUNT_SUB_TYPE, String.valueOf(accountInfo.getAccountSubType()));
					message.setContent(AccountEventPayloadFactory.createMakePayment(accountInfo,paymentInfo, auditInfo, transactionDate,notificationSuppressionInd));
				}
			}, KafkaEventType.MAKE_PAYMENT,KafkaContentType.JSON);
		logger.info("end publishMakePayment for ban = [ "+ accountInfo.getBanId() + " ] ");
		}
	
	}
	

