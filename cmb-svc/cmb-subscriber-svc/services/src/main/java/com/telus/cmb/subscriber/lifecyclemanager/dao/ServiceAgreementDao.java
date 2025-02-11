package com.telus.cmb.subscriber.lifecyclemanager.dao;

import com.telus.api.ApplicationException;
import com.telus.api.account.CallingCircleParameters;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.NumberGroupInfo;

public interface ServiceAgreementDao {

	/**
	 * change price plan for subscriber
	 *
	 * @param SubscriberInfo Subscriber information
	 * @param SubscriberContractInfo Subscriber Contract Information
	 * @param String Dealer Code
	 * @param String Sales Rep Code
	 * @param PricePlanValidationInfo pricePlanValidation
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 * @see com.telus.eas.subscriber.info.ServiceFeatureInfo
	 */
	void changePricePlan(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, 
			String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation, String sessionId)throws ApplicationException;
	
	/**
	 * change service agreement by adding, deleting, or updating services and features
	 *
	 * @param SubscriberInfo Subscriber information
	 * @param SubscriberContractInfo Subscriber Contract Information
	 * @param NumberGroupInfo Number Group Information
	 * @param String Dealer Code
	 * @param String Sales Rep Code
	 * @param PricePlanValidationInfo
	 *
	 * @throws ApplicationException
	 *
	 */
	
	 void changeServiceAgreement(SubscriberInfo pSubscriberInfo,
            SubscriberContractInfo pSubscriberContractInfo,
            NumberGroupInfo pNumberGroupInfo,
            String pDealerCode,
            String pSalesRepCode,
            PricePlanValidationInfo pricePlanValidation, String sessionId) throws ApplicationException;
	 
	   /**
		 * Delete a future-date price plan change.
		 *
		 * @param   int ban - the billing account number
		 * @param   String subscriberId - the subscriber ID
		 * @param   String productType - the subscriber product type
		 * 
		 * @exception ApplicationException
		 */
	 void deleteFutureDatedPricePlan(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;
	 
	 /**
		 * retrieve Calling circle parameters for given subscriber
		 *
		 * @param int banId - account no
		 * @param String subscriberNo - subscriber id
		 * @param String productType - subscriber product type
		 * @param String soc - soc 
		 * @param String featureCode
		 * 
		 * @return array of CallingCircleParameters
		 *
		 * @throws ApplicationException
		 */
	 
	 CallingCircleParameters retrieveCallingCircleParameters(int banId, String subscriberNo, String soc, String featureCode, 
			 String productType, String sessionId) throws ApplicationException;
	 
	 /**
		 * Update commitment/contract terms for a subscriber.
		 *
		 * This method updates contract details for the existing subscriber. In order to use this API,
		 * the subscriber has to be 'Active'.
		 *
		 * Changes in commitment period using this method method will cause a new entry to be
		 * added to the contract history table.
		 *
		 * Also, please note that the dealer/sales rep information from the subscriber information will
		 * be used for this contract term change.
		 *
		 * @param pSubscriberInfo
		 * @param pCommitmentInfo
		 * @param dealerCode
		 * @param salesRepCode
		 * @throws ApplicationException
		 */
		void updateCommitment(SubscriberInfo pSubscriberInfo, CommitmentInfo pCommitmentInfo, String dealerCode, 
				String salesRepCode, String sessionId) throws ApplicationException;


}
