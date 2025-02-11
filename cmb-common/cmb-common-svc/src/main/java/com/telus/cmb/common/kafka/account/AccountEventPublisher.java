package com.telus.cmb.common.kafka.account;

import java.util.Date;

import amdocs.APILink.datatypes.ActivityInfo;
import amdocs.APILink.datatypes.CancelInfo;

import com.telus.cmb.common.kafka.KafkaEventType;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

public interface AccountEventPublisher {

	/*
	 * This method is to be used for KafkaEventType BILLCYCLE_UPDATED
	 */
	public void publishBillCycleUpdateEvent(short billCycle, int billingAccountId, String sessionId);
	
	/*
	 * This method is to be used for KafkaEventType ACCOUNT_CREATED
	 */
	public void publishAccountCreatedEvent(AccountInfo accountInfo, String sessionId);
	
	/*
	 * This method is to be used for KafkaEventType ACCOUNT_UPDATED
	 */
	public void publishAccountUpdatedEvent(AccountInfo accountInfo, String sessionId);
	
	/*
	 * This method is to be used for KafkaEventType EMAIL_UPDATED
	 */
	public void publishPreferredEmailUpdateEvent(String email, int billingAccountId, String sessionId);
	
	/*
	 * This method is to be used for KafkaEventType AUTHORIZEDCONTACT_UPDATED
	 */
	public void publishAuthorizedContactsUpdateEvent(ConsumerNameInfo[] authorizedContacts, int billingAccountId, String sessionId);

	/*
	 * This method is to be used for KafkaEventType BILLING_ACCOUNT_STATUS
	 */
	public void publishAccountCancelStatusUpdateEvent(Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason, String userMemoText, boolean isBrandPortActivity, boolean isPortActivity, final int billingAccountId, final String sessionId);
	
	public void publishAccountSuspendStatusUpdateEvent(Date activityDate, String activityReasonCode, String userMemoText, String portOutInd, int billingAccountId, String sessionId);
	
	/*
	 * This method is to be used for KafkaEventTypes BILLING_NAME and BILLING_ADDRESS
	 */
	public void publishBillingInfoUpdateEvent(BillingPropertyInfo billingPropertyInfo, int billingAccountNumber, String sessionId);

	/**
	 * @param contactPropertyInfo
	 * @param billingAccountId
	 * @param sessionId
	 */
	public void publishContactInfoUpdateEvent(ContactPropertyInfo contactPropertyInfo, int billingAccountId, String sessionId);
}