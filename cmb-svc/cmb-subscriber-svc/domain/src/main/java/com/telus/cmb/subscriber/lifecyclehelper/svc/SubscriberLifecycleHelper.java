package com.telus.cmb.subscriber.lifecyclehelper.svc;


import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.InvoiceTaxInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.portability.info.PortOutEligibilityInfo;
import com.telus.eas.subscriber.info.CallingCirclePhoneListInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeHistoryInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.PricePlanChangeHistoryInfo;
import com.telus.eas.subscriber.info.ResourceChangeHistoryInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeHistoryInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.subscriber.info.VendorServiceChangeHistoryInfo;
import com.telus.eas.utility.info.PrepaidEventTypeInfo;

public interface SubscriberLifecycleHelper {

	/**
	 * Retrieves a list of Phone Directory entries from the CODS database using the 
	 * subscription ID as key ; the results will be limited to 100 entries 
	 * to limit the network overhead
	 * 
	 * @param subcriptionID
	 * @return List of Phone Directory entries for the subscriber
	 */
	PhoneDirectoryEntry[] getPhoneDirectory(long subcriptionID) throws ApplicationException;

	/**
	 * Update the Phone Directory entries for a given subscriber ; New entries , keyed using 
	 * the subscription ID and the phone number , will be inserted and existing entries will be updated
	 * 
	 * @param subscriptionID
	 * @param entries
	 */
	void updatePhoneDirectory(long subscriptionID, PhoneDirectoryEntry[] entries)  throws ApplicationException;

	/**
	 * Remove from the CODS database the given list of Phone Directory entries
	 * 
	 * @param subscriptionID
	 * @param entries
	 */
	void deletePhoneDirectoryEntries(long subscriptionID, PhoneDirectoryEntry[] entries)  throws ApplicationException;

	/**
	 * Retrieves subscriber from KB data source by ban and subscriber ID
	 * 
	 * @param ban - Billing Account Number
	 * @param subscriberId - Subscriber Identifier
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriber(int ban, String subscriberId) throws ApplicationException;

	/**
	 * Retrieves subscriber from KB data source by subscriber ID. This method return the subscriber that's "the latest" and may not
	 * be suitable for all scenarios.
	 * 
	 * @param subscriberId - Subscriber Identifier
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriber(String subscriberId) throws ApplicationException;

	/**
	 * Retrieves subscriber from KB data source by ban and phone number
	 * 
	 * @deprecated use {@link #retrieveSubscriberByBanAndPhoneNumber(int, String)}
	 * @param ban - Billing Account Number
	 * @param phoneNumber - Subscriber phone number Identifier
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriberByPhoneNumber(int ban, String phoneNumber) throws ApplicationException;
	
	/**
	 * Retrieves subscriber from KB data source by ban and phone number
	 * 
	 * @param ban - Billing Account Number
	 * @param phoneNumber - Subscriber phone number Identifier
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriberByBanAndPhoneNumber(int ban, String phoneNumber) throws ApplicationException;
	/**
	 *Returns Array of ServiceAgreementInfo
	 * 
	 * @param phoneNumber		phoneNumber
	 * @return	Array of ServiceAgreementInfo[]
	 * @throws ApplicationException
	 */
	ServiceAgreementInfo[] retrieveFeaturesForPrepaidSubscriber(String phoneNumber)throws ApplicationException;
	
	/**
	 * Returns List of ProvisioningTransactionInfo
	 * 
	 * @param customerID			customerID
	 * @param subscriberID		subscriberID
	 * @param from			from
	 * @param toDate			toDate
	 * @throws ApplicationException
	 * @return
	 */
	List retrieveProvisioningTransactions(int customerID,String subscriberID,Date from,Date toDate)throws ApplicationException; 
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @throws ApplicationException
	 * @return
	 */
	List retrievePrepaidCallHistory(String phoneNumber, java.util.Date from, java.util.Date to)throws ApplicationException;
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @throws ApplicationException
	 * @return
	 */
	List retrievePrepaidEventHistory(String phoneNumber, java.util.Date from, java.util.Date to)throws ApplicationException; 
	/**
	 * @param phoneNumber
	 * @param from
	 * @param to
	 * @throws ApplicationException
	 * @param prepaidEventTypes
	 * @return
	 */
	List retrievePrepaidEventHistory(String phoneNumber, java.util.Date from, java.util.Date to, PrepaidEventTypeInfo[] prepaidEventTypes)throws ApplicationException;

	/**
	 * retrieve Calling circle phone number list history for given subscriber and time frame
	 *
	 * @param banId banId - account no
	 * @param subscriberNo subscriberNo - subscriber id
	 * @param productType productType - subscriber product type
	 * @param from from - start date
	 * @param to to - end date
	 * @return array of CallingCirclePhoneListInfo
	 *
	 *
	 * @see com.telus.eas.subscriber.info.CallingCirclePhoneListInfo
	 *
	 */
	CallingCirclePhoneListInfo[] retrieveCallingCirclePhoneNumberListHistory(
			int banId, String subscriberNo, String productType, Date from, Date to) throws ApplicationException;

	/**
	 * Retrieve Contract Change History Info for Subscriber
	 *
	 * @param ban
	 * @param subscriberID subscriber Id
	 * @param from from
	 * @param to to
	 * @return Array ContractChangeHistory[]
	 *
	 */
	ContractChangeHistoryInfo[] retrieveContractChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to) throws ApplicationException;

	/**
	 * retrieve Feature parameter history for given subscriber and time frame
	 *
	 * @param banId banId - account no
	 * @param subscriberNo subscriberNo - subscriber id
	 * @param productType productType - subscriber product type
	 * @param parameterNames[] parameterNames -  array of parameter name
	 * @param from from - start date
	 * @param	to to - end date
	 * @return array of FeatureParameterHistoryInfo
	 *
	 * @see com.telus.eas.subscriber.info.FeatureParameterHistoryInfo
	 *
	 */
	FeatureParameterHistoryInfo[] retrieveFeatureParameterHistory(
			int banId, String subscriberNo, String productType, String[] parameterNames, Date from, Date to) throws ApplicationException;

	/**
	 * Retrieve multi-ring phone numbers.
	 * @param subscriberId String
	 * @throws ApplicationException
	 * @return String[]
	 *
	 */
	String[] retrieveMultiRingPhoneNumbers(String subscriberId) throws ApplicationException;

	/**
	 * Retrieve Price Plan Change History Info for Subscriber
	 *
	 * @param ban
	 * @param subscriberID subscriber Id
	 * @param from from
	 * @param to to
	 * @return Array PricePlanChangeHistory[]
	 *
	 */
	PricePlanChangeHistoryInfo[] retrievePricePlanChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to) throws ApplicationException;

	/**
	 * Retrieve the subscriber current service agreement, includes all socs and their features
	 *
	 * @param phoneNumber subscriber number
	 * @return SubscriberContractInfo 
	 *
	 * @see com.telus.eas.subscriber.info.SubscriberContractInfo
	 *
	 */
	SubscriberContractInfo retrieveServiceAgreementByPhoneNumber(String phoneNumber) throws ApplicationException;
	
	/**
	 * Retrieve the subscriber current service agreement, includes all socs and their features
	 * 
	 * @param subscriberId  SUBSCRIBER_NO
	 * @return SubscriberContractInfo
	 * @throws ApplicationException
	 */
	SubscriberContractInfo retrieveServiceAgreementBySubscriberId(String subscriberId) throws ApplicationException;

	/**
	 * Retrieve Service Change History Info for Subscriber
	 *
	 * @param ban
	 * @param subscriberID subscriber Id
	 * @param from from
	 * @param to to
	 * @return Array ServiceChangeHistory[]
	 *
	 */
	ServiceChangeHistoryInfo[] retrieveServiceChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to) throws ApplicationException;

	/**
	 * Retrieve Service Change History Info for Subscriber
	 *
	 * @param ban
	 * @param subscriberID subscriber Id
	 * @param from from
	 * @param to to
	 * @param includeAllServices includeAllServices
	 * @return Array ServiceChangeHistory[]
	 *
	 */
	ServiceChangeHistoryInfo[] retrieveServiceChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to, boolean includeAllServices) throws ApplicationException;

	/**
	 * Retrieve the subscriber's Vendor Service Change History - introduced for MobileApps project (June 2008)
	 *
	 * @param ban 		BAN
	 * @param subscriberId 	subscriber ID
	 * @param categoryCodes  array of vendor category codes to search upon
	 * 
	 * @return VendorServiceChangeHistoryInfo[]
	 *
	 */
	VendorServiceChangeHistoryInfo[] retrieveVendorServiceChangeHistory(int ban, String subscriberId, String[] categoryCodes) throws ApplicationException;

	/**
	 * Retrieve the subscriber current Voice Mail feature
	 *
	 * @param phoneNumber subscriber number
	 * @param productType product Type
	 * @return Array, String[2] of Soc code and Feature Code  for Voice Mail
	 *
	 */
	String[] retrieveVoiceMailFeatureByPhoneNumber(String phoneNumber,String productType) throws ApplicationException;
	/**
	 * Retrieves Provisioning Status using ban and Phone Number
	 * 
	 * @param ban 	ban
	 * @param phoneNumber	phoneNumber
	 * @return	String	Status
	 * @throws ApplicationException
	 */
	String retrieveSubscriberProvisioningStatus(int ban, String phoneNumber)throws ApplicationException;

	/**
	 * Retrieves SubscriberInfo details using the phoneNumber
	 * 
	 * @param phoneNumber	phoneNumber
	 * @throws ApplicationException
	 * @return	SubscriberInfo Object
	 */
	SubscriberInfo retrieveBanForPartiallyReservedSub(String phoneNumber)throws ApplicationException;
	/**
	 * Retrieves BAN with the phone Number as input
	 * 
	 * @param phoneNumber 		phoneNumber
	 * @throws ApplicationException
	 * @return	Integer BAN
	 */
	int retrieveBanIdByPhoneNumber(String phoneNumber)throws ApplicationException;
	/**
	 * Retrieves AddressInfo Details for the given BAN and subscriber.
	 * 
	 * @param ban 	BAN
	 * @param subscriber	subscriber
	 * @throws ApplicationException
	 * @return
	 */
	AddressInfo retrieveSubscriberAddress(int ban, String subscriber)throws ApplicationException;

	/**
	 * retrieve a list of partially reserved subscriber info by ban
	 * @param ban ban
	 * @param maximum maximum
	 * 
	 * @return Collection collection of SubscriberInfo
	 * 
	 * @throws ApplicationException
	 */
	Collection retrievePartiallyReservedSubscriberListByBan(int ban, int maximum) throws ApplicationException;

	/**
	 * retrieve a list of subscriber info for ported out subscribers by BAN ordered by activation date, most recent first
	 *
	 * @param ban BAN
	 * @param listLength listLength to retrieve <code>0</code> means all
	 * @return Collection collection of SubscriberInfo
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.SubscriberInfo
	 */	
	Collection retrievePortedSubscriberListByBAN(int ban, int listLength) throws ApplicationException;

	/**
	 * Retrieve Resource Change History Info for Subscriber
	 *
	 * @param ban
	 * @param subscriberID subscriber Id
	 * @param type type
	 * @param from from
	 * @param to to
	 * @return Array ResourceChangeHistory[]
	 *
	 * @throws ApplicationException
	 */
	ResourceChangeHistoryInfo[] retrieveResourceChangeHistory(int ban, String subscriberID, String type, java.util.Date from, java.util.Date to) throws ApplicationException;

	/**
	 * retireve light weight subscriber list for given BAN
	 * @param banId
	 * @param listLength
	 * @param includeCancelled
	 * 
	 * @throws ApplicationException
	 * 
	 */
	Collection retrieveLightWeightSubscriberListByBAN( int banId, boolean isIDEN, int listLength, boolean includeCancelled) throws ApplicationException;

	/**
	 * retrieve a Subscription ID by imsi
	 *
	 * @param imsi imsi
	 * @return Subscribtion ID
	 *
	 * @throws ApplicationException
	 *
	 * @see com.telus.eas.subscriber.info.SubscriberInfo
	 * 
	 * transaction-attribute = NotSupported
	 */
	String retrieveLastAssociatedSubscriptionId (String imsi) throws ApplicationException;

	/**
	 * Returns a boolean value of HotlineIndicator.
	 * 
	 * @param subscriberId String
	 * 
	 * @throws ApplicationException
	 * 
	 * @return boolean
	 *
	 */
	boolean retrieveHotlineIndicator(String subscriberId) throws ApplicationException;

	String getPortProtectionIndicator(int ban, String subscriberId, String phoneNumber,String status) throws ApplicationException;

	/**
	 * Retrieves a list of subscription preference for the given sbuscriptionId and preferenceTopicId
	 *
	 * @param subscriptionId
	 * @param preferenceTopicId
	 * 
	 * @throws ApplicationException
	 *
	 */
	SubscriptionPreferenceInfo retrieveSubscriptionPreference( long subscriptionId, int preferenceTopicId) throws ApplicationException;

	/**
	 * Retrieve talkgroups associated to a subscriber
	 *
	 * @param   pPhoneNumberReservation              Subscriber Id
	 * @return TalkGroupInfo[]     array of TalkGroupInfo
	 *
	 * @exception ApplicationException
	 *
	 */
	String[] retrieveAvailableCellularPhoneNumbersByRanges(
			PhoneNumberReservationInfo pPhoneNumberReservation,
			String     startFromPhoneNumber,
			String     searchPattern,
			boolean   asian,
			int pMaxNumber) throws ApplicationException;
	/**
	 * Retrieves List of DepositHistoryInfo
	 * 
	 * @param ban			ban
	 * @param subscriber			subscriber
	 * @return	List 
	 */
	List retrieveDepositHistory (int ban, String subscriber)throws ApplicationException;
	/**
	 * @param banId		banId
	 * @param subscriberNo		subscriberNo
	 * @param productType		productType
	 * 
	 * @exception ApplicationException
	 * @return	double
	 */
	double retrievePaidSecurityDeposit( int banId, String subscriberNo, String productType)throws ApplicationException;
	/**
	 * Retrieves List TalkGroupInfo  Details 
	 * 
	 * @param subscriberId		subscriberId
	 * @exception ApplicationException
	 * @return	List
	 */
	List retrieveTalkGroupsBySubscriber(String subscriberId)throws ApplicationException; 
	/**
	 * @param ban		ban
	 * @param subscriberId		subscriberId
	 * @param billSeqNo		billSeqNo
	 * @exception ApplicationException
	 * @return	InvoiceTaxInfo
	 */
	InvoiceTaxInfo retrieveInvoiceTaxInfo(int ban, String subscriberId, int billSeqNo)throws ApplicationException;
	/**
	 * Retrieves MemoInfoObject
	 * 
	 * @param ban		ban
	 * @param subscriberID		subscriberID
	 * @param memoType		memoType
	 * @exception ApplicationException
	 * @return MemoInfo
	 */
	MemoInfo retrieveLastMemo(int ban, String subscriberID, String memoType)throws ApplicationException;
	/**
	 * Retrieves Count for the given Repair ID
	 * 
	 * @param repairID		repairID
	 * @return	Integer		
	 */
	int getCountForRepairID(String repairID)throws ApplicationException;
	/**
	 *Retrieves List of EquipmentChangeHistoryInfo Objects
	 * 
	 * @param ban		ban
	 * @param subscriberID		subscriberID
	 * @param from			from
	 * @param to			to
	 * @return
	 */
	List retrieveEquipmentChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to)throws ApplicationException;
	/**
	 *Retrieves List of HandsetChangeHistoryInfo Objects
	 * 
	 * @param ban		ban
	 * @param subscriberID		subscriberID
	 * @param from			from
	 * @param to			to
	 * @return
	 */
	List retrieveHandsetChangeHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to)throws ApplicationException;

	/**
	 * Retrieves subscriber identifier from KB data source by BAN and phone number
	 * 
	 * @param ban
	 * @param phoneNumber
	 * @return string subscriber identifier
	 * @throws ApplicationException
	 */
	String retrieveSubscriberIDByPhoneNumber(int ban, String phoneNumber) throws ApplicationException;

	/**
	 * Returns boolean value of PostRestricted Constraint
	 * 
	 * @param ban		ban
	 * @param subscriberId		subscriberId
	 * @param phoneNumber		phoneNumber
	 * @param status		status
	 *  @throws ApplicationException
	 * @return
	 */
	boolean isPortRestricted(int ban, String subscriberId, String phoneNumber, String status)throws ApplicationException;


	/**
	 *Retrieves Subscriber Info Object with phone Number
	 * 
	 * @param phoneNumber	phoneNumber
	 * @return
	 */
	SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber)throws ApplicationException;

	/**
	 * @param ban
	 * @param subscriberID
	 * @param from
	 * @throws ApplicationException
	 * @param to
	 * @return
	 */
	List retrieveSubscriberHistory(int ban, String subscriberID, java.util.Date from, java.util.Date to)throws ApplicationException;

	/**
	 * Retrives Collection of Subscriber
	 * @param BAN		iBAN
	 * @param listLength		listLength
	 *   @throws ApplicationException
	 * @return
	 */
	Collection retrieveSubscriberListByBAN(int BAN, int listLength)throws ApplicationException;
	/**
	 * @param BAN
	 * @param listLength
	 * @param includeCancelled
	 *   @throws ApplicationException
	 * @return
	 */
	Collection retrieveSubscriberListByBAN(int BAN, int listLength, boolean includeCancelled)throws ApplicationException;
	/**
	 * @param ban		ban
	 * @param urbanId		urbanId
	 * @param fleetId		fleetId
	 * @param listLength		listLength
	 * @throws ApplicationException
	 * @return
	 */
	Collection retrieveSubscriberListByBanAndFleet(int ban, int urbanId, int fleetId, int listLength)throws ApplicationException;

	/**
	 * @param ban
	 * @param urbanId
	 * @param fleetId
	 * @param talkGroupId
	 * @param listLength
	 * @return
	 */
	Collection retrieveSubscriberListByBanAndTalkGroup(int ban, int urbanId, int fleetId, int talkGroupId, int listLength)throws ApplicationException;

	/**
	 * Retrieves SubscriptionRoleInfo Object
	 * 
	 * @param phoneNumber		phoneNumber
	 * @return
	 */
	SubscriptionRoleInfo retrieveSubscriptionRole(String phoneNumber)throws ApplicationException;
	/**
	 * Retrives List of PhoneNumbers for a subscriberStatus,accounttype,banstatus
	 * 
	 * @param subscriberStatus		subscriberStatus
	 * @param accountType		accountType
	 * @param accountSubType		accountSubType
	 * @param banStatus		banStatus
	 * @param maximum	maximum
	 * @throws ApplicationException
	 * @return
	 */
	List retrieveSubscriberPhonenumbers(char subscriberStatus, char accountType, char accountSubType, char banStatus, int maximum)throws ApplicationException;
	/**
	 * Retrives List of PhoneNumbers for a subscriberStatus,accounttype,banstatus,addressType
	 * 
	 * @param subscriberStatus		subscriberStatus
	 * @param accountType		accountType
	 * @param accountSubType		accountSubType
	 * @param banStatus		banStatus
	 * @param addressType
	 * @param maximum	maximum
	 * @throws ApplicationException
	 * @return
	 */
	List retrieveSubscriberPhonenumbers(char subscriberStatus, char accountType, char accountSubType, char banStatus, String addressType,int maximum)throws ApplicationException;
	/**
	 * Retrieves Collection Of Subscriber
	 * 
	 * @param serialNumber		serialNumber
	 * @param includeCancelled		includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection retrieveSubscriberListBySerialNumber(String serialNumber, boolean includeCancelled )throws ApplicationException;
	/**
	 * Retrieves Collection Of Subscriber
	 * 
	 * @param serialNumber		serialNumber
	 * @return
	 * @throws ApplicationException
	 */
	Collection retrieveSubscriberListBySerialNumber(String serialNumber)throws ApplicationException;
	/**
	 * @param phoneNumber			phoneNumber
	 * @param listLength			listLength
	 * @param includeCancelled			includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection retrieveSubscriberListByPhoneNumber(String phoneNumber, int listLength, boolean includeCancelled)throws ApplicationException;
	/**
	 * @param phoneNumbers			phoneNumbers
	 * @param includeCancelled			includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection retrieveSubscriberListByPhoneNumbers(String[] phoneNumbers, boolean includeCancelled)throws ApplicationException;
	/**
	 * Retrieves a list of subscribers corresponding to the given equipment.
	 *
	 * @param serialNumber
	 * @param active
	 * 
	 * @throws ApplicationException
	 *
	 */
	EquipmentSubscriberInfo[] retrieveEquipmentSubscribers(String serialNumber, boolean active) throws ApplicationException;

	/**
	 * retrieves a Collection of subscriber info by IMSI
	 *
	 * @param IMSI
	 * @param includeCancelled
	 * @return
	 * @throws ApplicationException
	 */
	Collection retrieveHSPASubscriberListByIMSI(String IMSI, boolean includeCancelled) throws ApplicationException;
	/**
	 * @param imsi		imsi
	 * @param includeCancelled		includeCancelled
	 * @throws ApplicationException
	 * @return
	 */
	Collection retrieveSubscriberListByImsi(String imsi, boolean includeCancelled)throws ApplicationException;
	/**
	 * Retrieves PortOutEligibilityInfo Object
	 * 
	 * @param phoneNumber		phoneNumber
	 * @param ndpInd		ndpInd
	 * @return
	 * @throws ApplicationException
	 */
	PortOutEligibilityInfo checkSubscriberPortOutEligibility(String phoneNumber, String ndpInd)throws ApplicationException;

	/**
	 * Returns VoiceusageInfo Details
	 * 
	 * @param Integer			banId
	 * @param String 			subscriberId
	 * @param String			featureCode
	 * @return
	 */
	VoiceUsageSummaryInfo retrieveVoiceUsageSummary(int banId, String subscriberId, String featureCode)throws ApplicationException;
	
	/**
	 * Retrieve the subscription id from CRDB database (CODS)
	 *
	 * @param   int ban - the billing account number
	 * @param   String phoneNumber - the phone number
	 * @param   String status - the status of the sub
	 *
	 * @exception ApplicationException
	 */
	long getSubscriptionId(int ban, String phoneNumber, String status) throws ApplicationException;
	
	/**
	 * Retrieve Email Address 
	 * 
	 * @param ban
	 * @param SubscriberNumber
	 * @return
	 * @throws ApplicationException
	 */
	String retrieveEmailAddress(int ban, String subscriberNumber) throws ApplicationException;

	/**
	 * Retrieve the last effective feature parameter from service_feature_parameters table in KB.
	 * 
	 * @param banId
	 * @param subscriberId
	 * @param productType
	 * @param serviceCode
	 * @param featureCode
	 * @return
	 * @throws ApplicationException
	 */
	FeatureParameterHistoryInfo retrieveLastEffectiveFeatureParameter(int banId, String subscriberId, String productType, String serviceCode,String featureCode)  throws ApplicationException;
	

	/**
	 * Retrieve valid calling-circle (and call home free) parameter record for given subscriber and date 
	 * 
	 * The result may contain 
	 *   <li>pending CC list</li>
	 *   <li>current CC list : effectiveDate <=from and expirationDate is null </li> 
	 *   <li>expired CC list : expirateDate>=from and expirationDate>=effectiveDate </li>
	 *    
	 * The result is sorted by parameter effective date in descending order.
	 * 
	 * @param banId
	 * @param subscriberId
	 * @param productType
	 * @param fromDate
	 * @return
	 * @throws ApplicationException
	 */
	FeatureParameterHistoryInfo[] retrieveCallingCircleParametersByDate(int banId, String subscriberId, String productType, Date fromDate) throws ApplicationException;

	/**
	 * This is similar to retrieveSubscriberByPhoneNumber. This method includes the subscriber that was in cancelled state and returns the one that's the latest.
	 * @param phoneNumber
	 * @return
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveLatestSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException;
	SubscriberInfo retrieveLatestSubscriberBySubscriptionId(long subscriptionId) throws ApplicationException;
	SubscriberIdentifierInfo retrieveSubscriberIdentifierBySubscriptionId(long subscriptionId) throws ApplicationException;
	String retrieveChangedSubscriber(int ban, String subscriberId, String productType, Date searchFromDate, Date searchToDate)throws ApplicationException;
	List retrieveAdustmentDetails(int ban, String adjustmentReasonCode,  String subscriberId,  Date searchFromDate, Date searchToDate) throws ApplicationException;
	/**
	 * Retrieves a list of subscribers corresponding to the given CDMA & MIKe equipment.
	 *
	 * @param serialNumber
	 * @param active
	 * @return
	 * @throws ApplicationException, RemoteException
	 *
	 */
	EquipmentSubscriberInfo[] retrieveNonHSPAEquipmentSubscribers(String serialNumber, boolean active) throws ApplicationException;
	/**
	 * Retrieves a list of subscribers corresponding to the given HSPA equipment.
	 *
	 * @param serialNumber
	 * @param active
	 * @param imsi
	 * @return 
	 * @throws ApplicationException, RemoteException
	 *
	 */
	EquipmentSubscriberInfo[] retrieveHSPAEquipmentSubscribers(String serialNumber, boolean active,String[] imsi) throws ApplicationException;
	

	/**
	 *Retrieves Subscriber Info Object with phone Number
	 * 
	 * @param phoneNumber	- 	it may be wireless phoneNumber or seat resource number
	 * @param PhoneNumberSearchOptionInfo - PhoneNumberSearchOption criteria(VOIP/Wireless search)
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberInfo retrieveSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException ;

	/**
	 * @param phoneNumber		-	it may be wireless phoneNumber or voip resource number
	 * @param PhoneNumberSearchOptionInfo - PhoneNumberSearchOption criteria(VOIP/Wireless search)
	 * @param listLength			
	 * @param includeCancelled
	 * @return SubscriberInfo object collection
	 * @throws ApplicationException
	 */
   Collection retrieveSubscriberListByPhoneNumber(String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo,int listLength, boolean includeCancelled) throws ApplicationException;

	/**
	 * Retrieves subscriber from KB data source by ban and phone number
	 * 
	 * @param ban - Billing Account Number
	 * @param phoneNumber - it may be wireless phoneNumber or voip resource number
	 * @param PhoneNumberSearchOptionInfo - PhoneNumberSearchOption criteria(VOIP/Wireless search)
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
    SubscriberInfo retrieveSubscriberByBanAndPhoneNumber(int ban, String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException ;

	/**
	 * Retrieves subscriber identifier from KB data source by ban and phone number
	 * 
	 * @param ban - Billing Account Number
	 * @param phoneNumber - it may be wireless phoneNumber or voip resource number
	 * @param PhoneNumberSearchOptionInfo - PhoneNumberSearchOption criteria(VOIP/Wireless search)
	 * @return SubscriberInfo object
	 * @throws ApplicationException
	 */
	SubscriberIdentifierInfo retrieveSubscriberIdentifiersByPhoneNumber(int ban, String phoneNumber,PhoneNumberSearchOptionInfo searchOptionInfo) throws ApplicationException;

	/**
	 * Retrieves Subscriber Identifier Info from KB data source by BAN and phone number
	 * 
	 * @param ban
	 * @param phoneNumber
	 * @return SubscriberIdentifierInfo
	 * @throws ApplicationException
	 */
	SubscriberIdentifierInfo retrieveSubscriberIdentifiersByPhoneNumber(int ban, String phoneNumber) throws ApplicationException;
    SubscriberInfo retrieveLatestSubscriberByPhoneNumber(String phoneNumber,PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo)throws ApplicationException;
    String[] retrieveCSCFeatureByPhoneNumber(String phoneNumber,String productType) throws ApplicationException;

	void updateSubscriptionRole(String phoneNumber, String subscriptionRoleCd) throws ApplicationException;

	/**
	 * Retrieves the communication suite by BAN and subscriber number
	 * @param ban - Billing Account Number
	 * @param subscriberNum - subscriber number. it's usually the same as phone #
	 * @param companionCheckLevel - indicate to retrieve the suite as a primary (1), companion(2) or either (0)
	 * @return
	 * @throws ApplicationException
	 */
	
	CommunicationSuiteInfo retrieveCommunicationSuite(int ban, String subscriberNum, int companionCheckLevel) throws ApplicationException ;
	List retrieveLightWeightSubscriberListInSameBan(int ban, String[] subscriberNumList) throws ApplicationException;
	Collection retrieveSubscriberListByPhoneNumbersPkgRuleHint(String[] phoneNumbers, boolean includeCancelled) throws ApplicationException ;

}
