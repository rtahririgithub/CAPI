package com.telus.cmb.subscriber.lifecyclemanager.dao;

import java.util.Date;

import com.telus.api.ApplicationException;
import com.telus.api.account.ServicesValidation;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;


public interface SubscriberDao {

	/**
	 * save the subscription preference
	 *
	 * @param preferenceInfo
	 * @param user, who did this transaction
	 * 
	 */
    void saveSubscriptionPreference(SubscriptionPreferenceInfo preferenceInfo, String user);
    
    DiscountInfo[] retrieveDiscounts(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;
    
    CancellationPenaltyInfo retrieveCancellationPenalty(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;
   
    void suspendSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate
    		, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException;

	void updatePortRestriction(int ban, String subscriberNo,
			boolean restrictPort, String userID) throws ApplicationException;

	SubscriberInfo updateSubscriber(SubscriberInfo subscriberInfo,
			String sessionId) throws ApplicationException;

	void updateSubscriptionRole(int ban, String subscriberNo,
			String subscriptionRoleCode, String dealerCode,
			String salesRepCode, String csrId) throws ApplicationException;
	
	void activateReservedSubscriber(int pBan, String pSubscriberId, Date pStartServiceDate, 
			String pActivityReasonCode, String pDealerCode,  String pSalesCode, ServicesValidation srvValidation,
			String portProcessType, int oldBanId, String oldSubscriberId,String productType, String sessionId) throws ApplicationException;
	
	void cancelSubscriber(SubscriberInfo pSubscriberInfo, java.util.Date pActivityDate, String pActivityReasonCode, 
			String pDepositReturnMethod, String pWaiveReason, String pUserMemoText, boolean isPortActivity, String sessionId) throws ApplicationException;
	
	void refreshSwitch(int pBan, String pSubscriberId, String pProductType, String sessionId) throws ApplicationException ;
	
	void restoreSuspendedSubscriber(SubscriberInfo pSubscriberInfo, java.util.Date pActivityDate, 
			String pActivityReasonCode, String pUserMemoText, boolean portIn, String sessionId) throws ApplicationException;

	void resumeCancelledSubscriber(SubscriberInfo pSubscriberInfo, String pActivityReasonCode,
			String pUserMemoText, boolean portIn, String portProcessType, int oldBanId, 
			String oldSubscriberId, String sessionId) throws ApplicationException;
	
	SubscriberInfo[] retrieveSubscribersByMemberIdentity(int urbanId, int fleetId, int memberId, String sessionId) throws ApplicationException;
	 
	void updateEmailAddress(int ban, String subscriberNumber, String emailAddress, String productType, String sessionId) throws ApplicationException;
	
	ServiceAirTimeAllocationInfo[] retrieveVoiceAllocation (int billingAccountNumber, String subscriberId, String productType, String sessionId )  throws ApplicationException;
	
	
	

}
