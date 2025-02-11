/*
 *  Copyright (c) 2015 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.kafka.account;

import java.util.Date;

import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityUtil;
import com.telus.cmb.common.kafka.KafkaContentType;
import com.telus.cmb.common.kafka.KafkaEventMapper;
import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.cmb.common.kafka.KafkaMessage;
import com.telus.cmb.common.kafka.KafkaMessageBuilder;
import com.telus.cmb.common.kafka.KafkaMessagePublisher;
import com.telus.cmb.common.kafka.PublisherConstants;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

/**
 *
 */
public class AccountEventPublisherImpl implements AccountEventPublisher {

	private static final String BILLINGACCOUNT_CREATED = "BILLINGACCOUNT_CREATED";
	private static final String BILLINGACCOUNT_UPDATED = "BILLINGACCOUNT_UPDATED";
	private static final String SOURCE_ID = "5284";
	
	private KafkaMessagePublisher publisher;
	
	private KafkaEventMapper mapper;
	
	public void setMapper(KafkaEventMapper mapper) {
		this.mapper = mapper;
	}
	
	public void setPublisher(KafkaMessagePublisher publisher) {
		this.publisher = publisher;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishBillCycleUpdateEventToKafka(short, int)
	 */
	@Override
	public void publishBillCycleUpdateEvent(final short billCycle, final int billingAccountId, final String sessionId) {
		
		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.addMetadata(PublisherConstants.BAN, billingAccountId);
				message.setContent(mapper.createBillCycleUpdateEvent(KafkaEventType.BILL_CYCLE, billCycle, billingAccountId));
			}
		}, KafkaEventType.BILL_CYCLE,KafkaContentType.XML);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishAccountCreatedEventToKafka(com.telus.eas.account.info.AccountInfo)
	 */
	@Override
	public void publishAccountCreatedEvent(final AccountInfo accountInfo, final String sessionId) {
		
		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable{
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_CREATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.BAN, String.valueOf(accountInfo.getBanId()));
				message.setContent(mapper.createAccountCreatedEvent(KafkaEventType.BILLING_ACCOUNT_ALL, accountInfo));
			}
		}, KafkaEventType.BILLING_ACCOUNT_ALL,KafkaContentType.XML);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishPreferredEmailUpdateEventToKafka(java.lang.String, int)
	 */
	@Override
	public void publishPreferredEmailUpdateEvent(final String email, final int billingAccountId, final String sessionId) {

		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.addMetadata(PublisherConstants.BAN, String.valueOf(billingAccountId));
				message.setContent(mapper.createEmailUpdateEvent(KafkaEventType.BILLING_CONTACT_EMAIL_ADDRESS, email, billingAccountId));
			}
		}, KafkaEventType.BILLING_CONTACT_EMAIL_ADDRESS,KafkaContentType.XML);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishAuthorizedContactsUpdateEventToKafka(com.telus.eas.account.info.ConsumerNameInfo[], int)
	 */
	@Override
	public void publishAuthorizedContactsUpdateEvent(final ConsumerNameInfo[] authorizedContacts, final int billingAccountId, final String sessionId) {

		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.addMetadata(PublisherConstants.BAN, String.valueOf(billingAccountId));
				message.setContent(mapper.createAuthorizedContactsUpdateEvent(KafkaEventType.AUTHORIZED_CONTACTS, authorizedContacts, billingAccountId));
			}
		}, KafkaEventType.AUTHORIZED_CONTACTS,KafkaContentType.XML);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publisherAccountUpdatedEventToKafka(com.telus.eas.account.info.AccountInfo, java.lang.String)
	 */
	@Override
	public void publishAccountUpdatedEvent(final AccountInfo accountInfo, final String sessionId) {
		publisher.publish( new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.addMetadata(PublisherConstants.ACCOUNT_TYPE, String.valueOf(accountInfo.getAccountType()));
				message.addMetadata(PublisherConstants.BRAND, String.valueOf(accountInfo.getBrandId()));
				message.addMetadata(PublisherConstants.BAN, String.valueOf(accountInfo.getBanId()));
				message.setContent(mapper.createAccountCreatedEvent(KafkaEventType.BILLING_ACCOUNT_UPDATED, accountInfo));
			}
		}, KafkaEventType.BILLING_ACCOUNT_UPDATED,KafkaContentType.XML);
	}
	

	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishAccountStatusUpdateEvent(java.util.Date, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void publishAccountCancelStatusUpdateEvent(final Date activityDate, final String activityReasonCode, final String depositReturnMethod, final String waiveReason, final String userMemoText, final boolean isBrandPortActivity, final boolean isPortActivity, final int billingAccountId, final String sessionId) {
		publisher.publish(new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.BAN, billingAccountId);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.setContent(mapper.createAccountCancelStatusUpdateEvent(KafkaEventType.BILLING_ACCOUNT_STATUS, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, isBrandPortActivity, isPortActivity, billingAccountId));
			}
		}, KafkaEventType.BILLING_ACCOUNT_STATUS,KafkaContentType.XML);
		
	}
	
	/*
	 * suspendAccount
	 */
	@Override
	public void publishAccountSuspendStatusUpdateEvent( final Date activityDate, final String activityReasonCode, final String userMemoText, final String portOutInd, final int billingAccountId, final String sessionId){
		publisher.publish(new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.BAN, billingAccountId);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				message.setContent(mapper.createAccountSuspendStatusUpdateEvent(KafkaEventType.BILLING_ACCOUNT_STATUS, activityDate, activityReasonCode, userMemoText, portOutInd, billingAccountId));
				
			}
		}, KafkaEventType.BILLING_ACCOUNT_STATUS,KafkaContentType.XML);
	}

	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishBillingContactUpdateEvent(com.telus.eas.account.info.BillingPropertyInfo, int, boolean, boolean, java.lang.String, )
	 */
	@Override
	public void publishBillingInfoUpdateEvent(final BillingPropertyInfo billingPropertyInfo, final int billingAccountId, final String sessionId) {
		publisher.publish(new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.BAN, billingAccountId);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
						
				message.setContent(mapper.createBillingInfoUpdateEvent(KafkaEventType.BILLING_INFO, billingPropertyInfo, billingAccountId));
			}
		}, KafkaEventType.BILLING_INFO,KafkaContentType.XML);
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.common.kafka.account.AccountEventPublisher#publishContactInfoUpdateEvent(com.telus.eas.account.info.ContactPropertyInfo, int, java.lang.String)
	 */
	@Override
	public void publishContactInfoUpdateEvent(final ContactPropertyInfo contactPropertyInfo, final int billingAccountId, final String sessionId) {
		publisher.publish(new KafkaMessageBuilder() {
			
			@Override
			public void populate(KafkaMessage message) throws Throwable {
				message.addMetadata(PublisherConstants.EVENT_NAME, BILLINGACCOUNT_UPDATED);
				message.addMetadata(PublisherConstants.ORIGINATOR_APPLICATION_ID, SOURCE_ID);
				message.addMetadata(PublisherConstants.BAN, billingAccountId);
				message.addMetadata(PublisherConstants.TRIGGERED_BY_KBID, getTriggeringApplicationId(sessionId));
				
				message.setContent(mapper.createContactInfoUpdateEvent(KafkaEventType.CONTACT_INFO, contactPropertyInfo, billingAccountId));
				
			}
		}, KafkaEventType.CONTACT_INFO,KafkaContentType.XML);
		
	}

	/**
	 * @param sessionId
	 * @return applicationId
	 * @throws Throwable
	 */
	private String getTriggeringApplicationId(final String sessionId) throws Throwable{
		ClientIdentityUtil ciUtil = null;
		ClientIdentity clientIdentity = null;
		
		ciUtil = new ClientIdentityUtil();
		clientIdentity = ciUtil.decrypt(sessionId);
		String applicationId = clientIdentity.getApplication();
		return applicationId;
	}
}
