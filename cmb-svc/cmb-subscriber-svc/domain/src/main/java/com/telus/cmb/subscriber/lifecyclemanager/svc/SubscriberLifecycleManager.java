package com.telus.cmb.subscriber.lifecyclemanager.svc;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ServicesValidation;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.AdditionalMsiSdnFtrInfo;
import com.telus.eas.subscriber.info.CallInfo;
import com.telus.eas.subscriber.info.CallListInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.eas.subscriber.info.SearchResultByMsiSdn;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.UsageProfileListsSummaryInfo;
import com.telus.eas.utility.info.NumberGroupInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;


public interface SubscriberLifecycleManager {

	/**
	 * Obtain sessionId for the given user credentials
	 * 
	 * @param userId The user id.
	 * @param password The password.
	 * @param applicationId The application id.
	 */
	String openSession(String userId, String password, String applicationId) throws ApplicationException;


	/**









	 * @param phoneNumber
	 * @param serviceAgreementInfo
	 * @throws ApplicationException
	 */
	//void activateMultipleFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo[] serviceAgreementInfo)throws ApplicationException;
	
	/**
	 * 
	 * @param phoneNumber
	 * @param serviceAgreementInfo
	 * @throws ApplicationException
	 */
	void activateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException;
	
	/**
	 * @param phoneNumber
	 * @param serviceAgreementInfo
	 * @param existingServiceAgreementInfo
	 * @throws ApplicationException
	 */
	void updateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException;
	
	/**
	 * @param phoneNumber
	 * @param serviceAgreementInfo
	 * @throws ApplicationException
	 */
	void deactivateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException;
	















	/**
	 * @param userId
	 * @param phoneNumber
	 * @param serviceAgreementInfoArray
	 * @throws ApplicationException
	 */
	void changeFeaturesForPrepaidSubscriber(String userId, String phoneNumber, ServiceAgreementInfo[] serviceAgreementInfoArray)throws ApplicationException;

	













	/**
	 * @param subscriber
	 * @param featuresPurchaseList
	 * @param user
	 * @throws ApplicationException
	 */
	void saveActivationFeaturesPurchaseArrangement (SubscriberInfo subscriber, ActivationFeaturesPurchaseArrangementInfo[] featuresPurchaseList, String user)throws ApplicationException;

	/**
	 * @param applicationId
	 * @param userId
	 * @param phoneNumber
	 * @param serviceAgreement
	 * @param action
	 * @throws ApplicationException
	 */
	void updatePrepaidCallingCircleParameters( String applicationId, String userId, String phoneNumber, ServiceAgreementInfo serviceAgreement, byte action)throws ApplicationException;
	
	/**
	 * save the subscription preference
	 *
	 * @param preferenceInfo
	 * @param user, who did this transaction
	 * 
	 */
	void saveSubscriptionPreference(SubscriptionPreferenceInfo preferenceInfo, String user);

	/**
	 * Activate a reserved Subscriber
	 *
	 * @param subscriberInfo - Subscriber information
	 * @param subscriberContractInfo - Subscriber contract information
	 * @param startServiceDate
	 * @param activityReasonCode
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 * @see com.telus.eas.subscriber.info.ServiceFeatureInfo
	 */
	void activateReservedSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo
			,Date startServiceDate, String activityReasonCode, ServicesValidation srvValidation
			,String portProcessType, int oldBanId, String oldSubscriberId, String sessionId )throws ApplicationException ;

	/**
	 * Adjust call
	 *
	 * This method adjusts a call for a subscriber.
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 * @param   billSeqNo - the bill sequence number
	 * @param   channelSeizureDate - the channel seizure date
	 * @param   messageSwitchId - the message switch ID
	 * @param   adjustmentAmount - the adjustment amount
	 * @param   adjustmentReasonCode - the reason code for the adjustment
	 * @param   memoText - the memo for the adjustment
	 * @param	usageProductType - the product type on the call (not the same as subscriber product type)
	 *
	 */
	void adjustCall(int ban, String subscriberId, String productType,
			int billSeqNo, Date channelSeizureDate, String messageSwitchId,
			double adjustmentAmount, String adjustmentReasonCode,
			String memoText, String usageProductType, String sessionId)
	throws ApplicationException;

	/**
	 * Retrieve usage profile
	 *
	 * This method retrieves the usage profile.
	 * @param ban
	 * @param subscriberId 
	 * @param billSeqNo - the bill sequence number
	 * @param productType - the subscriber product type
	 *
	 * @return  UsageProfileListInfo
	 */
	UsageProfileListsSummaryInfo getUsageProfileListsSummary(int ban, String subscriberId, int billSeqNo, String productType, String sessionId)
	throws ApplicationException;

	/**
	 * Retrieve billed calls
	 *
	 * This method retrieves the list of billed calls for a subscriber.
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 * @param   billSeqNo - the bill sequence number
	 * @param   fromDate - the start date for retrieving billed calls
	 * @param   toDate - the end date for retrieving billed calls
	 * @param   getAll - boolean flag for retrieving all billed calls (true) or only the first 600 (false)
	 *
	 * @return  CallListInfo
	 */
	CallListInfo retrieveBilledCallsList(int ban, String subscriberId,
			String productType, int billSeqNo, char callType, Date fromDate,
			Date toDate, boolean getAll, String sessionId)
	throws ApplicationException;

	/**
	 * Retrieve call details
	 *
	 * This method retrieves specific call details for a subscriber (billed or unbilled).
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 * @param   billSeqNo - the bill sequence number
	 * @param   channelSeizureDate - the channel seizure date
	 * @param   messageSwitchId - the message switch ID
	 * @param   callProductType - the product type for the billed call
	 *
	 * @return  CallInfo
	 *
	 */
	CallInfo retrieveCallDetails(int ban, String subscriberId,
			String productType, int billSeqNo, Date channelSeizureDate,
			String messageSwitchId, String callProductType, String sessionId)
	throws ApplicationException;

	/**
	 * Retrieve unbilled calls
	 *
	 * This method retrieves the list of unbilled calls for a subscriber.
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 * @param   fromDate - the start date for retrieving unbilled calls
	 * @param   toDate - the end date for retrieving unbilled calls
	 * @param   getAll - boolean flag for retrieving all unbilled calls (true) or only the first 600 (false)
	 *
	 * @return  CallListInfo
	 */
	CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId,
			String productType, Date fromDate, Date toDate, boolean getAll,
			String sessionId) throws ApplicationException;

	/**
	 * Retrieve unbilled calls
	 *
	 * This method retrieves the list of billed calls for a subscriber.
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 *
	 * @return  CallListInfo	 
	 */
	CallListInfo retrieveUnbilledCallsList(int ban, String subscriberId,
			String productType, String sessionId) throws ApplicationException;

	/**
	 * Retrieve list of discounts for a given subscriber
	 *
	 * Discounts can be applied at the account level or at the subscriber level. This method will
	 * return all subscriber level discounts.
	 *
	 * @param   ban             Billing Account Number
	 * @param   subscriberId   Subscriber Number
	 * @param   productType    Subscriber Product Type ('C' Cellular, 'I' Iden)
	 *
	 * @return DiscountInfo[]    array of discounts applied to account or subscriber
	 *
	 *
	 * @see DiscountInfo
	 *
	 */
	DiscountInfo[] retrieveDiscounts(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;

	/**
	 * Retrieve cancellation charges (penalty) and deposit information for a given subscriber
	 *
	 * @param   ban            billing account number (BAN)
	 * @param   subscriberId   subscriber id of subscriber to be cancelled
	 * @param   productType    product type of subscriber to be cancelled
	 * @return sessionId      cancellation penalty charges
	 */
	CancellationPenaltyInfo retrieveCancellationPenalty(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException; 

	/**
	 * Retrieve available phone numbers
	 *
	 * <P>This method looks for available phone numbers based on the criteria
	 * given. It returns an array of available phone numbers.
	 *
	 * <P> The subscriber Id is optional but it determines whether the search is done via
	 * the Amdocs Update bean or the Amdocs New Subscriber bean.
	 *
	 * <P>For PCS (product type 'C'), the attribute 'network' is not applicable
	 * and should be passed in as 0.
	 *
	 * <P>For IDEN (product type 'I'), the network is mandatory and this method also
	 * automatically allocates a randomly-selected public IP address.
	 *
	 * @param ban - billing account number (BAN)
	 * @param subscriberId -subscriber id 
	 * @param phoneNumberReservation - phone number reservation criteria
	 * @param maxNumbers - maximum numbers to retrieve from result
	 *
	 * @return String[] available phone numbers
	 *
	 * @exception   ApplicationException  20001  No phone numbers available matching criteria.
	 *
	 */
	AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban, String subscriberId,
			PhoneNumberReservationInfo phoneNumberReservation,
			int maxNumbers, String sessionId) throws ApplicationException;

	/**
	 * Retrieve available phone numbers
	 * 
	 * @param ban
	 * @param subscriberId
	 * @param numberGroup
	 * @param productType
	 * @param phoneNumberPattern
	 * @param asianInd
	 * @param maxNumbers
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 */
	AvailablePhoneNumberInfo[] retrieveAvailablePhoneNumbers(int ban, String subscriberId, NumberGroupInfo numberGroup,
			String productType, String phoneNumberPattern, boolean asianInd, 
			int maxNumbers, String sessionId) throws ApplicationException;

	/**
	 * Create new subscriber
	 *
	 * <P>This method creates a new PCS or IDEN subscriber.
	 *
	 * @param subscriberInfo             Subscriber Information (i.e. price plan, name etc.)
	 * @param subscriberContractInfo     SubscriberContractInfo
	 * @param activate                   Activate yes/no
	 * @param overridePatternSearchFee   Apply OverridePatternSearchFee yes/no
	 * @param activationFeeChargeCode    ActivationFeeChargeCode to apply (if not empty)
	 * @param dealerHasDeposit           Has Dealer kept the deposit yes/no
	 * @param portedIn					 is it portedIn subscriber
	 * @exception   ApplicationException
	 */
	void createSubscriber(SubscriberInfo subscriberInfo,
			SubscriberContractInfo subscriberContractInfo,
			boolean activate,
			boolean overridePatternSearchFee,
			String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn, 
			ServicesValidation srvValidation,
			String portProcessType, int oldBanId, String oldSubscriberId,
			String sessionId) throws ApplicationException;

	/**
	 * Updates subscriber's address
	 *
	 * @param ban
	 * @param subscriber
	 * @param productType
	 * @param addressInfo
	 * @throws ApplicationException
	 */
	void updateAddress(int ban, String subscriber, String productType, AddressInfo addressInfo, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber phone number
	 *
	 * @param subscriberInfo - Subscriber information
	 * @param newPhoneNumber - New phone number
	 * @param reasonCode 	 - Change reason code
	 * @param dealerCode 	 - Dealer Code
	 * @param salesRepCode	 - Sales Representative Code
	 * 
	 * @exception   ApplicationException  VAL20026  The supplied phone number is not available.
	 * @exception   ApplicationException
	 */
	void changePhoneNumber(SubscriberInfo subscriberInfo
			, AvailablePhoneNumberInfo newPhoneNumber
			, String reasonCode, String dealerCode
			, String salesRepCode, String sessionId) throws ApplicationException;

	/**
	 * Move an existing subscriber to another account
	 *
	 * This method moves a subscriber from the current account to another account.
	 * The subscriber status has to be 'Active' and the account where the subscriber
	 * is moved to can be in any status except 'Closed'.
	 *
	 * @param   subscriberInfo	      existing subscriber information
	 * @param   targetBan		
	 * @param   activityDate          activity date 
	 * @param   transferOwnership
	 * @param   activityReasonCode    reason code for moving the subscriber 
	 * @param   userMemoText          optional comment for moving the subscriber (optional)
	 * @param   dealerCode            Knowbility Dealer Code
	 * @param   salesRepCode          Knowbility Sales Rep Code
	 *
	 * @exception ApplicationException
	 */
	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan
			, Date activityDate, boolean transferOwnership
			, String activityReasonCode, String userMemoText
			, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException;

	/**
	 * Move an existing subscriber to another account
	 *
	 * This method moves a subscriber from the current account to another account.
	 * The subscriber status has to be 'Active' and the account where the subscriber
	 * is moved to can be in any status except 'Closed'.
	 *
	 * @param   subscriberInfo	    Existing subscriber information
	 * @param   targetBan           Account to which the subscriber is being moved (mandatory)
	 * @param   activityDate        Activity date (mandatory)
	 * @param   activityReasonCode  Reason code for moving the subscriber (mandatory)
	 * @param   userMemoText        Optional comment for moving the subscriber (optional)
	 *
	 * @exception ApplicationException
	 */
	void moveSubscriber(SubscriberInfo subscriberInfo
			, int targetBan, Date activityDate
			, boolean transferOwnership, String activityReasonCode
			, String userMemoText, String sessionId) throws ApplicationException;

	/**
	 * Release reserved subscriber
	 *
	 * @param subscriberInfo subscriber information
	 *
	 * @throws ApplicationException
	 */
	void releaseSubscriber(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException; 

	/**
	 * Find and reserve a 'like' phone number
	 *
	 * <P>This method looks for available phone numbers based on the criteria
	 * given which include another phone number. The find mechanism tries to
	 * return a close match to the phone number provided.
	 *
	 * <P>For PCS (product type 'C'), the attribute 'network' is not applicable
	 * and should be passed in as 0.
	 *
	 * <P>For IDEN (product type 'I'), the network is mandatory.
	 *
	 * @param subscriberInfo  subscriber information
	 * @param phoneNumberReservation phone number reservation criteria
	 *
	 * @return SubscriberInfo subscriber information
	 *
	 * @exception   ApplicationException  VAL20003  Like Number must be 10 numbers in length .
	 * @exception   ApplicationException  APP20001  No phone numbers available matching criteria.
	 * @exception   ApplicationException
	 */
	SubscriberInfo reserveLikePhoneNumber(SubscriberInfo subscriberInfo, PhoneNumberReservationInfo phoneNumberReservation, String sessionId) throws ApplicationException;

	/**
	 * Find and reserve a phone number
	 *
	 * <P>This method looks for available phone numbers based on the criteria
	 * given. It updateds the subscriber id and phone number in the subscriber
	 * information class.
	 *
	 * <P>For PCS (product type 'C'), the attribute 'network' is not applicable
	 * and should be passed in as 0.
	 *
	 * <P>For IDEN (product type 'I'), the network is mandatory and this method also
	 * automatically allocates a randomly-selected public IP address.
	 *
	 * @param subscriberInfo  subscriber information
	 * @param phoneNumberReservation phone number reservation criteria
	 * @param isOfflineReservation indicator of reserve from hold status for PCS offline activation  process. if isOfflineReservation set to false then it will support online activation.
	 * @return SubscriberInfo subscriber information
	 *
	 * @exception   ApplicationException  APP20001  No phone numbers available matching criteria.
	 * @exception   ApplicationException
	 */
	SubscriberInfo reservePhoneNumber(SubscriberInfo subscriberInfo, PhoneNumberReservationInfo phoneNumberReservation, boolean isOfflineReservation, String sessionId) throws ApplicationException;

	/**
	 * Cancel Additional Msisdn
	 * 
	 * @param additionalMsiSdnFtrInfo
	 * @param additionalMsisdn  - additional Msisdn
	 * 
	 * @exception ApplicationException
	 */
	void cancelAdditionalMsisdn(AdditionalMsiSdnFtrInfo[] additionalMsiSdnFtrInfo, String additionalMsisdn, String sessionId) throws ApplicationException;

	/**
	 * Cancel ported-in subscriber.
	 * 
	 * @param  banNumber 		  - billing account number
	 * @param  phoneNumber 		  - phone number
	 * @param  deactivationReason - deactivation reason code
	 * @param  activityDate 	  - activity date
	 * @param  portOutInd 		  - port-out indicator
	 * 
	 * @exception ApplicationException
	 */
	void cancelPortedInSubscriber(
			int banNumber, 
			String phoneNumber, 
			String deactivationReason, 
			Date activityDate, 
			String portOutInd, 
			boolean isBrandPort, 
			String sessionId ) throws ApplicationException;

	/**
	 * Change subscribers additional phone numbers (i.e. fax, circuit data etc.). The phone numbers
	 * assigned are from the same npa/nxx as the primary phone number.
	 *
	 * @param subscriberInfo  Subscriber Information
	 *
	 * @exception   ApplicationException
	 */
	void changeAdditionalPhoneNumbers(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException; 

	/**
	 * Change subscribers additional phone numbers for ported in primary phone number(i.e. fax, circuit data etc.). The phone numbers
	 * assigned are from the npa/nxx that has thesame ngp and local_vm_machine as the primary ported in phone number.
	 *
	 * @param subscriberInfo  Subscriber Information
	 *
	 * @exception   ApplicationException
	 */
	void changeAdditionalPhoneNumbersForPortIn(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;

	/**
	 * Change Fax Number
	 * This method attempts to change the subscriber's fax number to a system-assigned number based on his current
	 * phone number.
	 *
	 * @param subscriber - contains subscriber information
	 * @exception ApplicationException
	 */
	void changeFaxNumber(SubscriberInfo subscriber, String sessionId) throws ApplicationException; 

	/**
	 * Change Fax Number
	 * This method attempts to change the subscriber's fax number to the specific fax number given.
	 *
	 * @param  subscriber - contains subscriber information
	 * @param  newFaxNumber  - contains information about the new fax number
	 * @exception ApplicationException
	 */
	void changeFaxNumber(SubscriberInfo subscriber, AvailablePhoneNumberInfo newFaxNumber, String sessionId) throws ApplicationException;

	/**
	 * Change subscribers IMSI
	 *
	 * @param  ban
	 * @param  subscriberId
	 *
	 * @exception   ApplicationException
	 */
	void changeIMSI(int ban, String subscriberId, String sessionId) throws ApplicationException;

	/**
	 * Change subscribers IP address
	 *
	 * @param  ban
	 * @param  subscriberId
	 * @param  productType
	 * @param  newIp - new IP address (if left empty, random ip address is being assigned)
	 * @param  newIpType - new IP type (P=Private, B=Public, C=Corporate)
	 * @param  newIpCorpCode - new IP Corporate Code
	 *
	 * @exception   ApplicationException
	 */
	void changeIP(int ban, String subscriberId,  String newIp, String newIpType, String newIpCorpCode, String sessionId) throws ApplicationException;

	/**
	 * Reserve additional phone number
	 *
	 * @param  ban
	 * @param  productType
	 * @param  subscriberId
	 * @param  additionalPhoneNumber - new phone number
	 *
	 * @exception   ApplicationException
	 */
	void reserveAdditionalPhoneNumber(int ban, String subscriberId, AvailablePhoneNumberInfo additionalPhoneNumber, String sessionId) throws ApplicationException;

	/**
	 * Search IDEN Subscriber By Additional Msisdn
	 * 
	 * @param additionalMsisdn  - additional Msisdn
	 * 
	 * @exception ApplicationException
	 */
	SearchResultByMsiSdn searchSubscriberByAdditionalMsiSdn(String additionalMsisdn, String sessionId) throws ApplicationException; 

	/**
	 * Retrieves a list of Mike subscribers for a given Talkgroup.
	 *
	 * @param srcSubscriberInfo
	 * @param newSubscriberInfo
	 * @param activityDate
	 * @param subscriberContractInfo
	 * @param newPrimaryEquipmentInfo
	 * @param newSecondaryEquipmentInfo 
	 * @param  migrationRequestInfo
	 * 
	 * @throws ApplicationException
	 */
	void migrateSubscriber(SubscriberInfo srcSubscriberInfo, SubscriberInfo newSubscriberInfo
			, Date activityDate, SubscriberContractInfo subscriberContractInfo
			, com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo
			, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo
			, MigrationRequestInfo migrationRequestInfo, String sessionId) throws ApplicationException;

	/**
	 * Sets the subscriber port indicator to Subscriber.PORT_TYPE_PORT_IN with the current logical date.
	 * 
	 * @param phoneNumber - phone number
	 * 
	 * @exception ApplicationException
	 */
	void setSubscriberPortIndicator(String phoneNumber, String sessionId) throws ApplicationException;

	/**
	 * Sets the subscriber port indicator to Subscriber.PORT_TYPE_PORT_IN with the
	 * supplied date.
	 * 
	 * @param  phoneNumber - phone number
	 * @param  portDate - port date
	 * 
	 * @exception ApplicationException
	 */
	void setSubscriberPortIndicator(String phoneNumber, Date portDate, String sessionId) throws ApplicationException; 

	/**
	 * Performs a snapback of a ported-out TELUS resource.
	 * 
	 * @param  phoneNumber - phone number
	 * 
	 * @exception ApplicationException
	 */
	void snapBack(String phoneNumber, String sessionId) throws ApplicationException;

	/**
	 * Send test page to a pager subscriber
	 *
	 * @param  ban
	 * @param  subscriberId
	 * @param  sessionId
	 *
	 * @throws ApplicationException
	 */
	void sendTestPage(int ban, String subscriberId, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber Phone number to ported In number
	 * 
	 * @param subscriberInfo - Subscriber information
	 * @param newPhoneNumber - New phone number
	 * @param reasonCode     - Change reason code
	 * @param dealerCode	 - Dealer Code
	 * @param salesRepCode 
	 * @param portProcessType
	 * @param oldBanId
	 * @param oldSubscriberId
	 * 
	 * 
	 * @exception ApplicationException
	 */
	void portChangeSubscriberNumber(SubscriberInfo subscriberInfo
			, AvailablePhoneNumberInfo newPhoneNumber, String reasonCode
			, String dealerCode, String salesRepCode, String portProcessType
			, int oldBanId, String oldSubscriberId, String sessionId)
	throws ApplicationException;

	/**
	 *  Release ported number.
	 *  @param subscriberInfo
	 */
	void releasePortedInSubscriber(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException; 

	/**
	 * Suspend ported-in subscriber.
	 * 
	 * @param  banNumber - billing account number
	 * @param  phoneNumber - phone number
	 * @param  deactivationReason - deactivation reason code
	 * @param  activityDate - activity date
	 * @param  portOutInd - port-out indicator
	 * 
	 * @exception ApplicationException
	 */
	void suspendPortedInSubscriber(int banNumber, String phoneNumber, String deactivationReason, Date activityDate, String portOutInd, String sessionId) 
	throws ApplicationException;

	/**
	 * Reserve ported in phone number.
	 * @param subscriberInfo
	 * @param phoneNumberReservation
	 * @param reserveNumberOnly
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 */
	SubscriberInfo reservePortedInPhoneNumber(SubscriberInfo subscriberInfo,PhoneNumberReservationInfo phoneNumberReservation, 
			boolean reserveNumberOnly, String sessionId) throws ApplicationException;

	/**
	 * Reserve ported in phone number.
	 * @param subscriberInfo
	 * @param phoneNumberReservation
	 * @param reserveNumberOnly
	 * @param reserveUfmi
	 * @param ptnBased
	 * @param ufmiReserveMethod
	 * @param urbanId
	 * @param fleetId
	 * @param memberId
	 * @param availPhoneNumber
	 * @return
	 * @throws ApplicationException
	 */
	SubscriberInfo reservePortedInPhoneNumberForIden(SubscriberInfo subscriberInfo,PhoneNumberReservationInfo phoneNumberReservation
			, boolean reserveNumberOnly, boolean reserveUfmi, boolean ptnBased
			, byte ufmiReserveMethod, int urbanId, int fleetId, int memberId
			, AvailablePhoneNumber availPhoneNumber, String sessionId) throws ApplicationException;


	/**
	 * change price plan for subscriber
	 *
	 * @param subscriberInfo         - Subscriber information
	 * @param subscriberContractInfo - Subscriber Contract Information
	 * @param dealerCode - Dealer Code
	 * @param salesRepCode - Sales Rep Code
	 * @param pricePlanValidation
	 * 
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 * @see com.telus.eas.subscriber.info.ServiceFeatureInfo
	 */
	void changePricePlan(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, 
			String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation, String sessionId) throws ApplicationException;

	/**
	 * change service agreement by adding, deleting, or updating services and features
	 *
	 * @param subscriberInfo - Subscriber information
	 * @param subscriberContractInfo Subscriber Contract Information
	 * @param dealerCode Dealer Code
	 * @param salesRepCode Sales Rep Code
	 * @param pricePlanValidation
	 *
	 * @throws ApplicationException
	 *
	 */
	void changeServiceAgreement(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, 
			String salesRepCode, PricePlanValidationInfo pricePlanValidation, String sessionId)throws ApplicationException;
	/**
	 * Add ufmi resource to subscriber,P>
	 * At the same time, the service agreement might have to be changed to match
	 * services to new dispatch resource.
	 *
	 * @param subscriberInfo Subscriber information
	 * @param subscriberContractInfo Subscriber Contract Information
	 * @param dealerCode - Dealer Code
	 * @param salesRepCode-  Sales Rep Code
	 * @param urbanId
	 * @param fleetId
	 * @param memberId
	 * @param pricePlanChange
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.IDENSubscriberInfo
	 * @see com.telus.eas.subscriber.info.SubscriberContractInfo
	 */
	void addMemberIdentity(IDENSubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo
			, String dealerCode, String salesRepCode, int urbanId, int fleetId
			, String memberId, boolean pricePlanChange, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber member id
	 *
	 * This method will only update the member id on a class-based fleet. It does not
	 * support the changing of the fleet. Use method changeMemberIdentity() to change
	 * both fleet and member id.
	 *
	 * @param idenSubscriberInfo subscriber information
	 * @param newMemberId  new member id
	 *
	 * @exception   ApplicationException  VAL20033  The supplied member id is not available.
	 * @exception   ApplicationException  VAL20034  Subscriber cannot be on a PTN-based fleet.
	 * @exception   ApplicationException
	 */
	void changeMemberId(IDENSubscriberInfo idenSubscriberInfo, String newMemberId, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber fleet and member id
	 *
	 * This method is used to change UFMI to another fleet and member id. The fleet must be
	 * associated to the ban.
	 *
	 * @param idenSubscriberInfo subscriber information
	 * @param newUrbanId 	new urban id
	 * @param newFleetId		new fleet id
	 * @param newMemberId 	new member id
	 *
	 * @exception   ApplicationException  VAL10010  Fleet not associated to BAN.
	 * @exception   ApplicationException  VAL20033  The supplied member id is not available.
	 * @exception   ApplicationException
	 */
	void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo, int newUrbanId
			, int newFleetId, String newMemberId, String sessionId) throws ApplicationException; 

	/**
	 * Change Talk group(s) for an existing subscriber
	 *
	 * <P>This method attaches/detaches one or more talk groups to a subscriber. The subscriber needs
	 * to have a dispatch resource allocated prior to calling this method.
	 *
	 * Since a subscriber can only belong to ONE fleet, all talk groups passed into this method must
	 * be from the same fleet.
	 *
	 * <P>For PCS (product type 'C') this method is not applicable.
	 *
	 * @param idenSubscriberInfo - subscriber information
	 * @param addedTalkGroups - Talk Groups to be added
	 * @param removedTalkGroups - Talk Groups to be removed
	 *
	 * @exception   ApplicationException  VAL20004 List of Talk Groups empty
	 * @exception   ApplicationException  VAL20005 Talk Groups must be from same fleet
	 * @exception   ApplicationException  VAL20006 Dispatch Resource has to be allocated
	 * @exception   ApplicationException
	 */
	void changeTalkGroups(IDENSubscriberInfo idenSubscriberInfo, com.telus.eas.account.info.TalkGroupInfo[] addedTalkGroups
			, com.telus.eas.account.info.TalkGroupInfo[] removedTalkGroups, String sessionId) throws ApplicationException;

	/**
	 * Retrieves List of Available Member IDs.
	 * 
	 * @param urbanId 	- urban ID
	 * @param fleetId 	- fleet ID
	 * @param memberIdPattern - the desired pattern for Member IDs being searched
	 * @param max - the maximum number of Member IDs to be retrieved
	 * 
	 * @return int [] - List of Available Member IDs
	 * 
	 * @throws ApplicationException
	 */
	int[] getAvailableMemberIDs(int urbanId,int fleetId, String memberIdPattern, int max, String sessionId) throws ApplicationException;

	/**
	 * Remove ufmi resource from subscriber,P>
	 * At the same time, the service agreement might have to be changed to match
	 * services to removed dispatch resource.
	 *
	 * @param subscriberInfo Subscriber information
	 * @param subscriberContractInfo Subscriber Contract Information
	 * @param dealerCode Dealer Code
	 * @param salesRepCode Sales Rep Code
	 * @param pricePlanChange pricePlanChange
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.IDENSubscriberInfo
	 * @see com.telus.eas.subscriber.info.SubscriberContractInfo
	 */

	void removeMemberIdentity(IDENSubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo
			, String dealerCode, String salesRepCode, boolean pricePlanChange, String sessionId) throws ApplicationException;
	
	/**
	 * Find and reserve a member ID
	 *
	 * <P>This method looks for available member ids based on the criteria
	 * given. It returns IDENSubscriber info containing all the subscriber
	 * information passed in plus the fleet and member id allocated.
	 *
	 * The fleet does need to exist but it might not be associated to the
	 * BAN yet. This method will also do that association.
	 *
	 * <P>For PCS (product type 'C'), this method is not applicable
	 *
	 * @param idenSubscriberInfo  subscriber information
	 * @param fleetIdentityInfo fleet information (urban id/fleet id)
	 * @param wildCard
	 *
	 * @return IDENSubscriberInfo subscriber information
	 *
	 * @exception   ApplicationException  APP20004  No member ids available matching criteria.
	 * @exception   ApplicationException
	 */
	IDENSubscriberInfo reserveMemberId(IDENSubscriberInfo idenSubscriberInfo
			, FleetIdentityInfo fleetIdentityInfo, String wildCard, String sessionId) throws ApplicationException;

	/**
	 * Reserve member id on a PTN-based fleet
	 *
	 * <P>This method reserved a PTN-based fleet meaning that the subscribers phone number
	 * will match the UFMI, ie. npa will be equal to the urban id, nxx will be equal to the
	 * fleet id and the last 4 digits of the phone number will be the member id. It returns
	 * IDENSubscriber info containing all the subscriber information passed in
	 * plus the fleet and member id allocated.
	 *
	 * In the case where the PTN-based fleet does not exist yet, this method will create the
	 * fleet and associate it to the BAN. If the fleet exists but it is not associated to the
	 * BAN, the association will be done.
	 *
	 * <P>For PCS (product type 'C'), this method is not applicable
	 *
	 * @param idenSubscriberInfo  subscriber information
	 *
	 * @return IDENSubscriberInfo subscriber information
	 *
	 * @exception   ApplicationException
	 */
	IDENSubscriberInfo reserveMemberId(IDENSubscriberInfo idenSubscriberInfo, String sessionId) throws ApplicationException;

	/**
	 * Retrieve available member ids for a given fleet.
	 *
	 * <P>This method looks for available member ids based on the criteria
	 * given. It returns an array of available member ids.
	 *
	 * <P>For PCS (product type 'C'), this method is not applicable
	 *
	 * @param urbanId - urban id of fleet for which member ids are retrieved
	 * @param fleetId - fleet id of fleet for which member ids are retrieved
	 * @param memberIdPattern - member id search pattern
	 * @param maxMemberIds - maximum member Ids
	 *
	 * @return String[] available member ids
	 *
	 * @exception   ApplicationException  APP20004  No member ids available matching criteria.
	 * @exception   ApplicationException
	 */
	String[] retrieveAvailableMemberIds(int urbanId,
			int fleetId,
			String memberIdPattern,
			int maxMemberIds, String sessionId) throws ApplicationException;

	/**
	 * Retrieve talk groups associated to a subscriber
	 *
	 * <P>This method retrieves a list of all talk groups of a specific subscriber.
	 *
	 * @param   ban                 billing account number
	 * @param   subscriberId              subscriber id
	 * 
	 * @return Collection          list of TalkGroupInfo
	 *
	 * @exception   ApplicationException;
	 */
	Collection retrieveTalkGroupsBySubscriber(int ban, String subscriberId
			, String sessionId) throws ApplicationException; 


	/**
	 * Delete a future-date price plan change.
	 *
	 * @param   ban - the billing account number
	 * @param   subscriberId - the subscriber ID
	 * @param   productType - the subscriber product type
	 * 
	 * @exception ApplicationException
	 */
	void deleteFutureDatedPricePlan(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;

	/**
	 * Reset Voicemail password
	 *
	 * @param ban
	 * @param subscriberId
	 * @param productType
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 * @see com.telus.eas.subscriber.info.ServiceFeatureInfo
	 */
	void resetVoiceMailPassword(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;
	
	   /**
	 * retrieve Calling circle parameters for given subscriber
	 *
	 * @param  banId - account no
	 * @param  subscriberNo - subscriber id
	 * @param  soc - soc 
	 * @param  featureCode
	 * @param  productType - subscriber product type
	 * 
	 * @return array of CallingCircleParameters
	 *
	 * @throws ApplicationException
	 */
 
	CallingCircleParameters retrieveCallingCircleParameters(int banId, String subscriberNo, String soc, String featureCode, 
		 String productType, String sessionId) throws ApplicationException;
		
	/**
	 * Delete Msisdn Feature
	 * 
	 * @param  ftrInfo  - contains deleted feature information
	 * 
	 * @exception ApplicationException
	 */
	void deleteMsisdnFeature(AdditionalMsiSdnFtrInfo ftrInfo, String sessionId) throws ApplicationException;  
	
	/**
	 * Update Subscribers Birthday
	 *
	 * Since Knowbility does not store the subscribers birthday, this method is really
	 * just updating the feature parameter on the 'Free Birthday Calling' service. Currently,
	 * that service is available for Cellular subscribers only.
	 *
	 * @param subscriberInfo
	 * 
	 * @throws ApplicationException
	 */
	void updateBirthDate(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException; 
	
	/**
	 * Changes Equipment for PCS and IDEN subscribers
	 *
	 * This method updates primary and secondary equipment for a given subscriber.
	 * It also invokes an associated price plan change if the subscriber's contract
	 * information is passed.
	 *
	 * @param subscriberInfo 			-	Subscriber information (mandatory)
	 * @param oldPrimaryEquipmentInfo 	-	old primary equipment information (mandatory)
	 * @param newPrimaryEquipmentInfo 	-	new primary equipment information (mandatory)
	 * @param newSecondaryEquipmentInfo -	new secondary equipment information (optional)<BR>
	 * - if null is passed, secondary equipment will not be processed<BR>
	 * - if an empty array is passed, all secondary equipment will be removed<BR>
	 * - all equipment passed in this array will be added as active, non-primary equipment
	 * with an esn level set to > 1 and corresponding to sequence in the array
	 * @param dealerCode 				-  Dealer code (optional) - used for price plan change commissioning
	 * @param salesRepCode 				-  Sales rep code (optional) - used for price plan change commissioning
	 * @param requesterId 				-  Requester id (optional except for mule-to-mule) - used for warranty swap
	 * @param swapType 					-  Swap type (optional except for mule-to-mule) - used for warranty swap
	 * @param subscriberContractInfo 	-  Subscriber's contract information (optional except when changing price plan) - used for price plan and services change
	 * @param pricePlanValidation
	 *
	 * @throws ApplicationException
	 */
	void changeEquipment(SubscriberInfo subscriberInfo,
			EquipmentInfo oldPrimaryEquipmentInfo,
			EquipmentInfo newPrimaryEquipmentInfo,
			EquipmentInfo[] newSecondaryEquipmentInfo,
			String dealerCode,
			String salesRepCode,
			String requesterId,
			String swapType,
			SubscriberContractInfo subscriberContractInfo, 
			PricePlanValidationInfo pricePlanValidation,
			String sessionId) throws ApplicationException;
		
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

	/**
	 * Suspend a Subscriber
	 *
	 * This method suspends a subscriber unless it is the last active subscriber on that
	 * account. In that case, the account itself needs to be suspended.
	 *
	 * The account has to be in 'Open' status and the subscriber status has to be 'Active'.
	 *
	 * @param   subscriberInfo    subscriber information (mandatory)
	 * @param   activityDate              date on which to suspend the subscriber (optional - defaul: today)
	 * @param   activityReasonCode            reason code for suspending the subscriber (mandatory)
	 * @param   userMemoText            optional comment for suspending the subscriber (optional)
	 *
	 * @exception ApplicationException
	 */
	void suspendSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate
			, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException;
	
	/**
	 * Create deposit for a Subscriber
	 *
	 * @param   pSubscriberInfo    subscriber information
	 * @param   amount            deposit amount
	 * @param   memoText            memo text
	 *
	 * @exception ApplicationException
	 */
	void createDeposit(SubscriberInfo pSubscriberInfo, double amount, String memoText, String sessionId) throws ApplicationException;
	
	void updatePortRestriction(int ban, String subscriberNo, boolean restrictPort
			, String userID) throws ApplicationException;
	
	/**
	 * Update Subscriber Information
	 *
	 * The following subscriber attributes can be updated through this method:
	 * - user name
	 * - email address
	 * - subscriber alias (for IDEN only)
	 *
	 * @param subscriberInfo
	 * 
	 * @return SubscriberInfo
	 * 
	 * @throws ApplicationException
	 */
	SubscriberInfo updateSubscriber(SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException; 
	
	/**
	 * Updates a the subscription role staging table STAGED_SUBSCRIBER_ROLE
	 * associated with the ban, subscriber number.
	 * 
	 * @param ban 			-  The ban associated with the subscription to update.
	 * @param subscriberNo	- The subscriber number associated with the subscription to update.
	 * @param subscriptionRoleCode - The new subscription role code.
	 * @param dealerCode	- The dealer performing the operation.
	 * 
	 * @exception ApplicationException
	 */
	void updateSubscriptionRole(int ban, String subscriberNo, String subscriptionRoleCode,
			String dealerCode, String salesRepCode, String csrId) throws ApplicationException;
	
	/**
	 * Cancel a Subscriber
	 *
	 * This method cancels a subscriber unless it is the last active subscriber on that
	 * account. In that case, the account itself needs to be cancelled.
	 *
	 * The account has to be in 'Open' or 'Suspended' status and the subscriber status has to
	 * be 'Active' or 'Suspended'.
	 *
	 * Under certain conditions the subscriber is charged a cancellation penalty which will be
	 * applied automatically unless the fee is waived by giving a reason.
	 *
	 * @param   pSubscriberInfo 	 - Subscriber information (mandatory)
	 * @param   pActivityDate   	 - Date on which to cancel the subscriber (optional - defaul: today)
	 * @param   pActivityReasonCode  - Reason code for cancelling the subscriber (mandatory)
	 * @param   pDepositReturnMethod - Method by which the deposit should be returned (mandatory)
	 *              					possible values are: 
	 *              						'O' cover open debts
	 *                                      'R' refund entire amount
	 *                                      'E' refund excess amount
	 * @param   pWaiveReason   -  Reason for waving applicable cancellation fee (optional - default: fee is charged))
	 * @param   pUserMemoText  -  Optional comment for cancelling the subscriber (optional)
	 * @param   boolean 	   -  Port activity - true when service provider is TELUS
	 *
	 * @exception ApplicationException
	 */
	
	 void cancelSubscriber(SubscriberInfo pSubscriberInfo, java.util.Date pActivityDate, String pActivityReasonCode, 
			String pDepositReturnMethod, String pWaiveReason, String pUserMemoText,  boolean isPortActivity,String sessionId) throws ApplicationException;
			
	/**
	 * Refresh switch information for subscriber
	 *
	 * @param ban Billing Account Number
	 * @param subscriberId Subscriber ID
	 * @param productType Product Type
	 *
	 * @throws ApplicationException
	 */
	 void refreshSwitch(int ban, String subscriberId, String productType, String sessionId) throws ApplicationException;
	 
	 /**
		 * Restore a suspended subscriber
		 *
		 * This method restores a suspended subscriber to 'active' status.
		 * The subscriber status has to be 'Suspended'.
		 *
		 * @param   subscriberInfo    subscriber information (mandatory)
		 * @param   activityDate              date on which to suspend the subscriber (optional - defaul: today)
		 * @param   activityReasonCode            reason code for restoring the suspended subscriber (mandatory)
		 * @param   userMemoText            optional comment for cancelling the subscriber (optional)
		 *
		 * @exception ApplicationException
		 */
	void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate, String activityReasonCode, 
				String userMemoText, boolean portIn, String sessionId) throws ApplicationException;

	/**
	 * Resume a cancelled subscriber
	 *
	 * This method resumes a cancelled subscriber.
	 * The subscriber status has to be 'Cancelled'.
	 *
	 * @param   subscriberInfo    subscriber information (mandatory)
	 * @param   activityReasonCode            reason code for resuming the cancelled subscriber (mandatory)
	 * @param   userMemoText            optional comment for cancelling the subscriber (optional)
	 * @param   portIn
	 * @param   portProcessType
	 * @param   oldBanId
	 * @param   oldSubscriberId
	
	 * @exception ApplicationException
	 */
	 void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode,
			String userMemoText, boolean portIn, String portProcessType, int oldBanId, 
			String oldSubscriberId, String sessionId) throws ApplicationException;

	 /**
	  * Retrieves a list of Mike subscribers for a given Talkgroup.
	  *
	  * @param urbanId
	  * @param fleetId
	  * @param memberId
	  * 
	  * @throws ApplicationException
	  */
	Collection retrieveSubscribersByMemberIdentity(int urbanId, int fleetId, int memberId, String sessionId) throws ApplicationException;

	/**
	 * Update Email Address
	 * 
	 * @param ban
	 * @param subscriberNumber
	 * @param emailAddress
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updateEmailAddress(int ban, String subscriberNumber, String emailAddress, String sessionId) throws ApplicationException;
	
	public double getAirtimeRate(int billingAccountNumber, String subscriberId, String sessionId) throws ApplicationException;
	
	ServiceAirTimeAllocationInfo[] retrieveVoiceAllocation (int billingAccountNumber, String subscriberId, String sessionId)  throws ApplicationException;

	void updatePrepaidSubscriber(PrepaidSubscriberInfo prepaidSubscriberInfo) throws ApplicationException;
	
	/**
	 * Change subscriber resource numbers
	 * 
	 * @param subscriberInfo subscriber information
	 * @param resourceList list of ResourceActivityInfo VOIP resources to change
	 * @param activityDate activity date
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeResources(SubscriberInfo subscriberInfo, List resourceList, Date activityDate, String sessionId)	throws ApplicationException;
	
	/**
	 * Change subscriber seat group
	 * 
	 * @param subscriberInfo subscriber information
	 * @param seatGroupId seat group identifier
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeSeatGroup(SubscriberInfo subscriberInfo, String seatGroupId, String sessionId) throws ApplicationException;
	
	/**
	 * Get CSC feature and to update the feature parameter
	 * 
	 * @param ban
	 * @param subscriberId
	 * @param productType
	 *  @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void resetCSCSubscription(int ban, String subscriberId, String productType,String sessionId) throws ApplicationException;
}