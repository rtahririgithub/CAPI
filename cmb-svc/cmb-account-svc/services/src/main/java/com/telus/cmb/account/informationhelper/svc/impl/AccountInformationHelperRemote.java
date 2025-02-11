package com.telus.cmb.account.informationhelper.svc.impl;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CreditCheckResultDeposit;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AirtimeUsageChargeInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CollectionHistoryInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.DepositAssessedHistoryInfo;
import com.telus.eas.account.info.DepositHistoryInfo;
import com.telus.eas.account.info.EBillRegistrationReminderInfo;
import com.telus.eas.account.info.FleetIdentityInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpStatisticsInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.account.info.MaxVoipLineInfo;
import com.telus.eas.account.info.MemoCriteriaInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PhoneNumberSearchOptionInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
import com.telus.eas.account.info.SubscriberEligibilitySupportingInfo;
import com.telus.eas.account.info.SubscribersByDataSharingGroupResultInfo;
import com.telus.eas.account.info.VoiceUsageSummaryInfo;
import com.telus.eas.eligibility.info.InternationalServiceEligibilityCheckResultInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

public interface AccountInformationHelperRemote extends EJBObject {

	List retrieveAccountsByBan(int[] banArray) throws ApplicationException, RemoteException;

	/**
	 * @deprecated
	 * @param ban
	 * @return
	 * @throws ApplicationException
	 * @throws RemoteException
	 */
	AccountInfo retrieveAccountByBan(int ban) throws ApplicationException, RemoteException;
	AccountInfo retrieveAccountByBan(int ban, int retrievalOption) throws ApplicationException, RemoteException;

	/**
	 * @deprecated
	 * @param ban
	 * @return
	 * @throws ApplicationException
	 * @throws RemoteException
	 */
	AccountInfo retrieveHwAccountByBan(int ban) throws ApplicationException, RemoteException;
	AccountInfo retrieveHwAccountByBan(int ban, int retrievalOption) throws ApplicationException, RemoteException;

	AutoTopUpInfo retrieveAutoTopUpInfoForPayAndTalkSubscriber(int pBan, String pPhoneNumber) throws ApplicationException, RemoteException;

	AccountInfo retrieveLwAccountByPhoneNumber(String pPhoneNumber) throws ApplicationException, RemoteException;

	AccountInfo retrieveLwAccountByBan(int ban) throws ApplicationException, RemoteException;

	String retrieveAccountStatusByBan(int ban) throws ApplicationException, RemoteException;

	List retrieveAccountsByPostalCode(String lastName, String postalCode, int maximum) throws RemoteException;

	AccountInfo retrieveAccountByPhoneNumber(String phoneNumber) throws ApplicationException, RemoteException;

	List retrieveAccountsByPhoneNumber(String phoneNumber) throws ApplicationException, RemoteException;

	List retrieveAccountsByPhoneNumber(String phoneNumber, boolean includePastAccounts, boolean onlyLastAccount) throws ApplicationException, RemoteException;

	List retrieveLatestAccountsByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException, RemoteException;

	// String, String
	Map retrievePhoneNumbersForBAN(int ban) throws ApplicationException, RemoteException;

	VoiceUsageSummaryInfo[] retrieveVoiceUsageSummary(int ban, String featureCode) throws ApplicationException, RemoteException;

	PrepaidConsumerAccountInfo retrieveAccountInfo(int ban, String phoneNumber) throws ApplicationException, RemoteException;

	SearchResultsInfo retrieveAccountsByBusinessName(String nameType, String legalBusinessName, boolean legalBusinessNameExactMatch, char accountStatus, char accountType, String provinceCode,
			int brandId, int maximum) throws ApplicationException, RemoteException;

	SearchResultsInfo retrieveAccountsByName(String nameType, String firstName, boolean firstNameExactMatch, String lastName, boolean lastNameExactMatch, char accountStatus, char accountType,
			String provinceCode, int brandId, int maximum) throws ApplicationException, RemoteException;

	double retrieveUnpaidAirTimeTotal(int ban) throws ApplicationException, RemoteException;

	AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban) throws ApplicationException, RemoteException;

	List retrieveMemos(int Ban, int Count) throws ApplicationException, RemoteException;

	List retrieveMemos(MemoCriteriaInfo MemoCriteria) throws ApplicationException, RemoteException;

	MemoInfo retrieveLastMemo(int Ban, String MemoType) throws ApplicationException, RemoteException;

	List retrieveAccountsByDealership(char accountStatus, String dealerCode, Date startDate, Date endDate, int maximum) throws ApplicationException, RemoteException;

	List retrieveAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, java.util.Date startDate, java.util.Date endDate, int maximum) throws RemoteException;

	PrepaidConsumerAccountInfo retrieveAccountInfoForPayAndTalkSubscriber(int ban, String phoneNumber) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @return
	 * @deprecated
	 */
	BillNotificationContactInfo getLastEBillNotificationSent(int ban) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @param subscriptionType
	 * @return
	 * @deprecated
	 */
	List getBillNotificationHistory(int ban, String subscriptionType) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 */
	void expireBillNotificationDetails(int ban) throws ApplicationException, RemoteException;

	int[] retrieveBanIds(char accountType, char accountSubType, char banStatus, int maximum) throws RemoteException;

	int[] retrieveBanIdByAddressType(char accountType, char accountSubType, char banStatus, char addressType, int maximum) throws RemoteException;

	int retrieveAttachedSubscribersCount(int ban, FleetIdentityInfo fleetIdentity) throws ApplicationException, RemoteException;

	ProductSubscriberListInfo[] retrieveProductSubscriberLists(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieves the bill parameters given an BAN
	 * 
	 * @param banId BAN
	 * 
	 * @return BillParametersInfo
	 * @exception ApplicationException
	 */
	BillParametersInfo retrieveBillParamsInfo(int banId) throws ApplicationException, RemoteException;

	FleetInfo[] retrieveFleetsByBan(int ban) throws ApplicationException, RemoteException;

	AccountInfo retrieveAccountByImsi(String imsi) throws ApplicationException, RemoteException;

	/**
	 * Retrieves a list containing up to 'maxNumbers' subscriber Ids.
	 * 
	 * @param   banId   billing account number (BAN)
	 * @param   status  status of the subscriber
	 * @param   maximum   maximum of phone numbers to return
	 * 
	 * @return list of subscriber IDs
	 * @exception ApplicationException
	 */

	List retrieveSubscriberIdsByStatus(int banId, char status, int maximum) throws ApplicationException, RemoteException;

	/**
	 * Return the list of subscriber phone numbers on this account filtered by specified subscriber status upto the
	 * specified maximum.
	 *
	 * @param   banId       billing account number (BAN)
	 * @param   status      the subscriber's status ( see one of the Subscriber.STATUS_xxx constants )
	 * @param   maximum       maximum of suspended phone numbers to return
	 * 
	 * @return list of phone numbers   
	 * @exception ApplicationException
	 */
	List retrieveSubscriberPhoneNumbersByStatus(int banId, char status, int maximum) throws ApplicationException, RemoteException;

	/**
	 * @param id
	 * @return String
	 */
	String retrieveCorporateName(int id) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @return
	 */
	int getClientAccountId(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Subscriber Invoice Detail
	 *
	 * @param banId   Billing Account Number (BAN)
	 * @param billSeqNo   Bill Sequence Number 
	 * @return list of SubscriberInvoiceDetailInfo
	 * 
	 * @throws ApplicationException
	 */

	List retrieveSubscriberInvoiceDetails(int banId, int billSeqNo) throws ApplicationException, RemoteException;

	/**
	 * Returns the BillNotification Contact Info
	 * @param ban
	 * @return Collection<BillNotificationContactInfo>
	 * @deprecated
	 */

	List retrieveBillNotificationContacts(int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns Collection of ChargeInfo
	 * @param ban
	 * @param pBillSeqNo
	 * @param pPhoneNumber
	 * @param from
	 * @param to
	 * @return Collection<ChargeInfo>
	 */
	List retrieveBilledCharges(int ban, int pBillSeqNo, String pPhoneNumber, Date from, Date to) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Associated TalkGroup Count
	 * 
	 * @param pFleeIdentity
	 * @param ban
	 * @return int	count 
	 */
	int retrieveAssociatedTalkGroupsCount(FleetIdentityInfo pFleeIdentity, int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns Address history
	 *
	 * @param   pBan       				billing account number (BAN)
	 * @param   pFromDate				from Date
	 * @param   pToDate     			to Date
	 * @return AddressHistoryInfo[]  	array of AddressHistoryInfo
	 *
	 * @exception ApplicationException
	 */
	AddressHistoryInfo[] retrieveAddressHistory(int pBan, Date pFromDate, Date pToDate) throws ApplicationException, RemoteException;

	/**
	 * Returns Paper Bill Suppression At Activation Indicator
	 * 
	 * @param pBan			billing account number (BAN)
	 * @return				one of AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN,
	 * AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_NO, BILL_SUPPRESSION_AT_ACTIVATION_YES
	 * @exception ApplicationException
	 * @deprecated
	 */
	String getPaperBillSupressionAtActivationInd(int pBan) throws ApplicationException, RemoteException;

	/**
	 * Returns prepaid activation credit.
	 *
	 * @param   applicationId                applicationId
	 * @param   pUserId                      userId
	 * @param   pPrepaidConsumerAccountInfo	all attributes related to a prepaid account
	 * @return  double                       the activation credit
	 *
	 * @exception ApplicationException
	 *
	 * @see PrepaidConsumerAccountInfo
	 */
	double getPrepaidActivationCredit(String applicationId, String pUserId, PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException, RemoteException;

	/**
	 * Checks if has EPostSubscription
	 * 
	 * @param ban  int
	 * @return	boolean
	 * @deprecated
	 */
	boolean hasEPostSubscription(int ban) throws ApplicationException, RemoteException;

	/**
	 * Checks the FeatureCatogory Exists on the Subscriber
	 * 
	 * @param int       billing account number (BAN)
	 * @param String   pCategoryCode 
	 * @return boolean
	 */
	boolean isFeatureCategoryExistOnSubscribers(int ban, String pCategoryCode) throws ApplicationException, RemoteException;

	AddressInfo retrieveAlternateAddressByBan(int ban) throws ApplicationException, RemoteException;

	BusinessCreditIdentityInfo[] retrieveBusinessList(int ban) throws ApplicationException, RemoteException;

	ConsumerNameInfo[] retrieveAuthorizedNames(int ban) throws ApplicationException, RemoteException;

	DepositHistoryInfo[] retrieveDepositHistory(int ban, Date fromDate, Date toDate) throws ApplicationException, RemoteException;

	DepositAssessedHistoryInfo[] retrieveDepositAssessedHistoryList(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieves Payment History
	 * 
	 * @param int ban 
	 * @param Date pFromDate
	 * @param Date pToDate
	 * @return	CollectionPaymentHistoryInfo
	 */
	List retrievePaymentHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Returns Invoice Properties
	 * 
	 * @param int   ban
	 * @return  InvoicePropertiesInfo
	 */
	InvoicePropertiesInfo getInvoiceProperties(int ban) throws ApplicationException, RemoteException;

	ChargeInfo[] retrieveCharges(int ban, String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws ApplicationException, RemoteException;

	DepositAssessedHistoryInfo[] retrieveOriginalDepositAssessedHistoryList(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieves Hotlined Phone Number
	 * 
	 * @param int BAN
	 * @return PhoneNumber String 
	 */
	String retrieveHotlinedSubscriberPhoneNumber(int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns InvoiceHistoryInfo for a given ban and dates
	 * 
	 * @param  int ban
	 * @param Date fromDate
	 * @param Date toDate
	 * @return Collection of Invoice History
	 */
	List retrieveInvoiceHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Gives the count of Attached Subscribers Talk group
	 * 
	 * @param urbanID
	 * @param fleetID
	 * @param talkGroupId
	 * @param ban
	 * @return int Count 
	 */
	int retrieveAttachedSubscribersCountForTalkGroup(int urbanID, int fleetID, int talkGroupId, int ban) throws ApplicationException, RemoteException;

	/**
	 * retrieveCollectionHistoryInfo
	 * @param banId 		billing account number (BAN)
	 * @param fromDate Date
	 * @param toDate Date
	 * @throws SQLException
	 * @throws ApplicationException
	 * @return CollectionHistoryInfo[]
	 *
	 */
	CollectionHistoryInfo[] retrieveCollectionHistoryInfo(int banId, Date fromDate, Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Retrieves letter requests for specified BAN, level (account/subscriber/all), and created within specified date range.
	 * The result set is limited by specifying the maximum argument.
	 *
	 * @param banId  					billing account number (BAN)
	 * @param from
	 * @param to
	 * @param level						ChargeType.CHARGE_LEVEL_ACCOUNT, ChargeType.CHARGE_LEVEL_SUBSCRIBER, AccountManager.Search_All
	 * @param pSubscriber (optional)
	 * @param maximum
	 * @return SearchResultsInfo with underlying array objects of type LMSLetterRequestInfo
	 * @throws ApplicationException
	 */
//	SearchResultsInfo retrieveLetterRequests(int banId, java.util.Date from, java.util.Date to, char level, String pSubscriber, int maximum) throws ApplicationException, RemoteException;

	/**
	 *Returns Last Payment Activity on Account
	 * 
	 * @param int 		banId
	 * @return
	 */
	PaymentHistoryInfo retrieveLastPaymentActivity(int ban) throws ApplicationException, RemoteException;

	/**
	 *Returns List of Payment Activities
	 * 
	 * @param int 		banId
	 * @param int 		paymentSeqNo
	 * @return
	 */
	List retrievePaymentActivities(int banId, int paymentSeqNo) throws ApplicationException, RemoteException;

	/**
	 * Returns Payment Method Change HistoryInfo
	 * 
	 * @param int   			 ban
	 * @param java.Util.Date 	fromDate
	 * @param java.util.Date 	toDate
	 * @return
	 */
	List retrievePaymentMethodChangeHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Returns refund history
	 *
	 * @param ban
	 * @param fromDate
	 * @param toDate
	 * @return RefundHistoryInfo
	 */

	List retrieveRefundHistory(int ban, Date fromDate, Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Returns related charges for a credit
	 *
	 * @param   pBan       		billing account number (BAN)
	 * @param   pChargeSeqNo    charge sequence number
	 * @return ChargeInfo[] 	array of ChargeInfo (not null but could be empty)
	 *
	 * @exception ApplicationException
	 *
	 */
	List retrieveRelatedChargesForCredit(int pBan, double pChargeSeqNo) throws ApplicationException, RemoteException;

	/**
	 * Retrieve the number of CDMA / HSPA subscribers. This method is created for IVR for performance purpose. 
	 * Instead of looping through the whole subscriber list, this method queries the database according to the
	 * serial number. If it is an DUMMY ESN (for USIM), then it's counted as HSPA. Otherwise, it's a CDMA. 
	 * It includes subscribers with product type "C" only.
	 * 
	 * @param int - ban
	 * @return HashMap - the number of CDMA/HSPA subscribers
	 */

	HashMap retrievePCSNetworkCountByBan(int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns related credits for a charge
	 *
	 * @param  pBan       		billing account number (BAN)
	 * @param  pChargeSeqNo    charge sequence number
	 * @return	CreditInfo[] 	array of CreditInfo (not null but could be empty)
	 *
	 * @throws ApplicationException
	 */
	List retrieveRelatedCreditsForCharge(int pBan, double pChargeSeqNo) throws ApplicationException, RemoteException;

	/**
	 * Returns pending charges history
	 *
	 * @param   pBan       	billing account number (BAN)
	 * @param   pFromDate		from Date
	 * @param   pToDate     	to Date
	 * @param level			ChargeType.CHARGE_LEVEL_ACCOUNT, ChargeType.CHARGE_LEVEL_SUBSCRIBER, AccountManager.Search_All
	 * @param pSubscriber
	 * @param maximum			maximum results returned
	 * 
	 * @return SearchResults 	which includes  array of ChargeInfo (not null but could be empty)
	 *
	 * @throws ApplicationException
	 */
	SearchResultsInfo retrievePendingChargeHistory(int pBan, Date pFromDate, Date pToDate, char level, String pSubscriber, int maximum) throws ApplicationException, RemoteException;

	/**
	 * Returns Collection of TalkGroupInfo 
	 * @param pBan
	 * @return Collection TalkGroupInfo
	 */
	List retrieveTalkGroupsByBan(int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns credits for a given account.
	 *
	 * Depending on the parameter passed in either only billed credits,
	 * only unbilled credits or all credits are being returned.
	 *
	 * @param pBan
	 * @param pFromDate
	 * @param pToDate
	 * @param pBillState
	 * @param level
	 * @param pKnowbilityOperatorId
	 * @param pSubscriber
	 * @param maximum
	 *  
	 * @returns SearchResultsInfo
	 * 
	 */
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, char level, String knowbilityOperatorId, String subscriber, int maximum)
			throws ApplicationException, RemoteException;

	/**
	 * Returns credits for a given account .
	 *
	 * Depending on the parameter passed in either only billed credits,
	 * only unbilled credits or all credits are being returned.
	 *
	 * @param   int       billing account number (BAN)
	 * @param   Date      from Date
	 * @param   Date      to Date
	 * @param   String    bill state (mandatory)
	 *                    where:
	 *                    'B' billed
	 *                    'U' un-billed
	 *                    'A' all
	 * @param   String    Knowbility operator ID (optional - default all)
	 * @param   String    Reason Code
	 * @param   String    Subscriber Number
	 *
	 * @returns SearchResultsInfo
	 *
	 */
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, String knowbilityOperatorId, String reasonCode, char level, String subscriber,
			int maximum) throws ApplicationException, RemoteException;

	/**
	 * Returns an array of PricePlanSubscriberCountInfo, which represents all subscribers on the given BAN
	 * on dollar pooling price plans.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * 
	 * @returns PricePlanSubscriberCountInfo[]
	 *
	 */
	List retrieveDollarPoolingPricePlanSubscriberCounts(int banId, String productType) throws ApplicationException, RemoteException;

	/**
	 * Retrieves Follow Up's additional text.
	 *
	 * @param ban
	 * @param followUpId
	 * 
	 * @return List of FollowUpTextInfo
	 */
	List retrieveFollowUpAdditionalText(int ban, int followUpId) throws ApplicationException, RemoteException;

	/**
	 * Retrieves the history for a given follow up.
	 *
	 * @param followUpId
	 * 
	 * @return List of FollowUpInfo
	 */
	List retrieveFollowUpHistory(int followUpId) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	 *
	 * @param   int      billing account number (BAN)
	 * @param   int  Follow Up ID
	 * 
	 * @return FollowUpInfo
	 *
	 */
	FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int ban, int followUpID) throws ApplicationException, RemoteException;

	/**
	 * Retrieves the history for a given follow up.
	 *
	 * @param followUpCriteria
	 * 
	 * @return List of FollowUpInfo
	 * 
	 */
	List retrieveFollowUps(FollowUpCriteriaInfo followUpCriteria) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	 *
	 * @param   int      billing account number (BAN)
	 * @param   int      maximum Follow Ups
	 * 
	 *  @return List of FollowUpInfo
	 *
	 */
	List retrieveFollowUps(int ban, int Count) throws ApplicationException, RemoteException;

	/**
	 * Retrieves statistics on the open follow ups for a given BAN.
	 *
	 * @param ban
	 * 
	 * @return
	 * 
	 */
	FollowUpStatisticsInfo retrieveFollowUpStatistics(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Credit Check Memo Info By billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * 
	 * @returns  MemoInfo
	 *
	 */
	MemoInfo retrieveLastCreditCheckMemoByBan(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Last Follow Up ID a given billing account number (BAN) and Follow Up Type
	 *
	 * @param   int      billing account number (BAN)
	 * @param   String  Follow Up Type
	 *
	 */
	int retrieveLastFollowUpIDByBanFollowUpType(int ban, String followUpType) throws ApplicationException, RemoteException;

	/**
	 * Returns an array of Subscribers with minute-pooling capable price plans and
	 * pooling-enabled features on their contract.
	 *
	 * @param   int       billing account number (BAN)
	 * @returns String    pooling coverage type - long distance or airtime coverage type
	 */
	List retrieveMinutePoolingEnabledPricePlanSubscriberCounts(int banId, String[] poolingCoverageTypes) throws ApplicationException, RemoteException;

	/**
	 * Returns an array of PoolingPricePlanSubscriberCountInfo representing all subscribers on the given BAN 
	 * participating in all pooling groups.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrievePoolingPricePlanSubscriberCounts(int banId) throws ApplicationException, RemoteException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * participating in the given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @param int - pooling group ID
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrievePoolingPricePlanSubscriberCounts(int banId, int poolGroupId) throws ApplicationException, RemoteException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * participating in the given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param String[] - service Codes
	 * @param boolean - include Expired flag
	 * @returns ServiceSubscriberCount[]
	 */
	List retrieveServiceSubscriberCounts(int banId, String[] serviceCodes, boolean includeExpired) throws ApplicationException, RemoteException;

	/**
	 * Retrieves shareable (Add-A-Line, Family Talk) priceplan subscriber counts
	 * by billing account number (BAN).
	 *
	 * @param String - billing account number (BAN)
	 * @returns PricePlanSubscriberCountInfo
	 */
	List retrieveShareablePricePlanSubscriberCount(int ban) throws ApplicationException, RemoteException;

	/**
	 * Returns an array of PoolingPricePlanSubscriberCountInfo representing all subscribers on the given BAN 
	 * who are zero-minute pooling in all pooling groups.
	 *
	 * @param int - billing account number (BAN)
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrieveZeroMinutePoolingPricePlanSubscriberCounts(int banId) throws ApplicationException, RemoteException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * who are zero-minute pooling in a given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param int - pooling group ID
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrieveZeroMinutePoolingPricePlanSubscriberCounts(int banId, int poolGroupId) throws ApplicationException, RemoteException;

	/**
	 * Validates information for a new prepaid account (against Pay&Talk database).
	 *
	 * @param   String                        userId
	 * @param   PrepaidConsumerAccountInfo    all attributes related to a prepaid account
	 * @return  String                        reference number from credit card transaction
	 *
	 * @exception ApplicationException
	 *
	 * @see PrepaidConsumerAccountInfo
	 *
	 */
	String validatePayAndTalkSubscriberActivation(String applicationId, String userId, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader)
			throws ApplicationException, RemoteException;

	/**
	 * Returns the credits based on the follow-up id
	 *
	 *
	 * @param int followup id
	 */
	List retrieveCreditByFollowUpId(int followUpId) throws ApplicationException, RemoteException;

	/**
	 * Returns the customer contact info such as phone numbers and email address.
	 * @param int ban
	 * @return ContactDetailInfo
	 * @throws ApplicationException
	 * @throws RemoteException
	 */
	ContactDetailInfo getCustomerContactInfo(int ban) throws ApplicationException, RemoteException;

	/**
	 * Retrieve Accounts By SerialNumber (ESN, IMEI etc)
	 *
	 * @param   String      equipment serial number
	 * @throws ApplicationException 
	 * @returns Collection of AccountInfo account related information
	 *
	 * @see AccountInfo
	 */
	List retrieveAccountsBySerialNumber(String serialNumber) throws ApplicationException, RemoteException;

	AccountInfo retrieveAccountBySerialNumber(String serialNumber) throws ApplicationException, RemoteException;

	/**
	 * Returns credits for a given account.
	 *
	 * Depending on the parameter passed in either only billed credits,
	 * only unbilled credits or all credits are being returned.
	 *
	 * @param pBan
	 * @param pFromDate
	 * @param pToDate
	 * @param pBillState
	 * @param level
	 * @param pSubscriber
	 * @param maximum
	 *
	 */
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, char level, String subscriber, int maximum)
			throws ApplicationException, RemoteException;

	/**
	 * Returns Account status change history
	 *
	 * @param   int       billing account number (BAN)
	 * @param   Date      from Date
	 * @param   Date      to Date
	 * @returns StatusChangeHistoryInfo[]  list of StatusChangeHistoryInfo
	 *
	 * @exception ApplicationException,RemoteException;
	 */
	List retrieveStatusChangeHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException, RemoteException;

	/**
	 * Retrieves the last credit check info by billing account number (BAN), merging CDA and KB data
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType) throws ApplicationException, RemoteException;

	/**
	 * Retrieves only the KB credit check info by billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType) throws ApplicationException, RemoteException;
	
	BillingPropertyInfo retrieveBillingInformation(int billingAccountNumber) throws ApplicationException, RemoteException;

	ContactPropertyInfo retrieveContactInformation(int billingAccountNumber) throws ApplicationException, RemoteException;

	PersonalCreditInfo retrievePersonalCreditInformation(int ban) throws ApplicationException, RemoteException;

	BusinessCreditInfo retrieveBusinessCreditInformation(int ban) throws ApplicationException, RemoteException;

	String[] retrieveSubscriberIdsByServiceFamily(AccountInfo account, String familyTypeCode, Date effectiveDate) throws ApplicationException, RemoteException;

	String[] retrieveSubscriberIdsByServiceFamily(int banId, String familyTypeCode, Date effectiveDate) throws ApplicationException, RemoteException;

	SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(AccountInfo account, String[] dataSharingGroupCodes, Date effectiveDate)
			throws ApplicationException, RemoteException;

	SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(int banId, String[] dataSharingGroupCodes, Date effectiveDate) throws ApplicationException, RemoteException;

	SubscriberDataSharingDetailInfo[] retrieveSubscriberDataSharingInfoList(int banId, String[] dataSharingGroupCodes) throws ApplicationException, RemoteException;

	SubscriberEligibilitySupportingInfo getSubscriberEligiblitySupportingInfo(int ban, String[] memoTypeList, Date dateFrom, Date dateTo) throws ApplicationException, RemoteException;

	List retrieveRelatedCreditsForChargeList(List chargeIdentifierInfoList) throws ApplicationException, RemoteException;

	String retrieveChangedSubscriber(int ban, String subscriberId, String productType, Date searchFromDate, Date searchToDate) throws ApplicationException, RemoteException;

	List retrieveAdjustedAmounts(int ban, String adjustmentReasonCode, String subscriberId, Date searchFromDate, Date searchToDate) throws ApplicationException, RemoteException;

	List retrieveApprovedCreditByFollowUpId(int banId, int followUpId) throws ApplicationException, RemoteException;

	CreditInfo retrieveCreditById(int banId, int entSeqNo) throws ApplicationException, RemoteException;

	InternationalServiceEligibilityCheckResultInfo checkInternationalServiceEligibility(int ban) throws ApplicationException, RemoteException;

	CreditCheckResultDeposit[] retrieveDepositsByBan(int ban) throws ApplicationException, RemoteException;

	AccountInfo retrieveAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException, RemoteException;

	AccountInfo retrieveLwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException, RemoteException;

	AccountInfo retrieveHwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException, RemoteException;

	String getNextSeatGroupId() throws ApplicationException, RemoteException;

	List getMaxVoipLineList(int ban, long subscriptionId) throws ApplicationException, RemoteException;

	void createMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo) throws ApplicationException, RemoteException;

	void updateMaxVoipLine(List maxVoipLineInfoList) throws ApplicationException, RemoteException;
	List retrieveAccountListByImsi(String imsi) throws ApplicationException ,RemoteException;


}
