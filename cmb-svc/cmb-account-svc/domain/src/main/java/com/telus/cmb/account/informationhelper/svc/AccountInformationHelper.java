package com.telus.cmb.account.informationhelper.svc;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public interface AccountInformationHelper {

	/**
	 * Retrieve Account Status for a given billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * @returns String      account status
	 *                      C - Closed
	 *                      N - Cancelled
	 *                      O - Open
	 *                      S - Suspended
	 *                      T - Tentative
	 * @throws ApplicationException
	 */
	String retrieveAccountStatusByBan(int ban) throws ApplicationException;

	/**
	 * Retrieve accounts by billing account numbers (BANs)
	 *
	 * @param banArray
	 * 
	 */
	List retrieveAccountsByBan(int[] banArray);

	/**
	 * Returns all accounts ever associated with this postal code and last name.
	 *
	 * @param   String      last name
	 * @param   String      postal code
	 * @param   int         maximum number of rows to return
	 * 
	 * @returns Collection  list of AccountInfo
	 * 
	 * @see AccountInfo
	 */
	List retrieveAccountsByPostalCode(String lastName, String postalCode, int maximum);

	/**
	 * Retrieve heavyweight account by billing account number (BAN). This operation differs from retrieveAccountByBan in that it
	 * returns a superset of data including credit check deposits and product subscriber lists in the AccountInfo object. It
	 * combines the results of the retrieveAccountByBan, retrieveDepositsByBan and retrieveProductSubscriberLists operations.
	 *
	 * @param   int - billing account number (BAN)
	 * @returns AccountInfo - account related information
	 * @throws ApplicationException
	 * 
	 * @see retrieveAccountByBan
	 * @see retrieveDepositsByBan
	 * @see retrieveProductSubscriberLists
	 */
	@Deprecated
	AccountInfo retrieveHwAccountByBan(int ban) throws ApplicationException;
	
	AccountInfo retrieveHwAccountByBan(int ban, int retrievalOption) throws ApplicationException;

	/**
	 * Retrieve Account By billing account number (BAN)
	 *
	 * @param   int     billing account number (BAN)
	 * @returns AccountInfo account related information
	 * @throws ApplicationException
	 * @see AccountInfo
	 */
	@Deprecated
	AccountInfo retrieveAccountByBan(int ban) throws ApplicationException;
	
	/**
	 * Retrieve Account By billing account number (BAN)
	 *
	 * @param   int     billing account number (BAN)
	 * @param   int     retrievalOption 
	 * @returns AccountInfo account related information
	 * @throws ApplicationException
	 * @see AccountInfo
	 */
	AccountInfo retrieveAccountByBan(int ban, int retrievalOption) throws ApplicationException;

	/**
	 * Retrieve Lightweight Account By billing account number (BAN)
	 *
	 * @param   int     billing account number (BAN)
	 * @returns AccountInfo account related information that contains subset of account info only. Unlike {@link #retrieveLwAccountByPhoneNumber(String)}, it does
	 * 			not return the Prepaid related information.
	 * @throws ApplicationException
	 * @see AccountInfo
	 */
	AccountInfo retrieveLwAccountByBan(int ban) throws ApplicationException;

	/**
	 * Retrieve Account By PhoneNumber
	 *
	 * @param   String      phone number
	 * @returns AccountInfo account related information	  
	 * @throws ApplicationException
	 * @see AccountInfo
	 */
	AccountInfo retrieveAccountByPhoneNumber(String phoneNumber) throws ApplicationException;

	/**
	 * Returns all accounts ever associated with a mobile number.
	 * @param number the mobile number to search on.
	 * @returns Collection  list of AccountInfo
	 * @throws ApplicationException
	 * @see AccountInfo
	 */
	List retrieveAccountsByPhoneNumber(String phoneNumber) throws ApplicationException;

	/**
	 * Returns all accounts ever associated with a mobile number.
	 *
	 * @param number the mobile number to search on.
	 * @param boolean include Past Accounts, on which subscriber cancelled
	 * @param boolean only Last Account
	 * @throws ApplicationException
	 */
	List retrieveAccountsByPhoneNumber(String phoneNumber, boolean includePastAccounts, boolean onlyLastAccount) throws ApplicationException;

	/**
	 * Returns Recent Active/Cancel account  associated with a mobile number/voip number/tollfree number.
	 *
	 * @param phone number, the wireless/voip/tollfree phone number to search on.
	 * phoneNumberSearchOptionInfo , search criteria option to search by wireless/voip/tollfree/hsia
	 * @throws ApplicationException
	 */
	List retrieveLatestAccountsByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException;

	/**
	 * Returns Retrieve Phone Numbers for BAN
	 * This method is for IDEN only. Improper method name. TO DO: Rename method or change the query to support what the method name 
	 * truly suggests.
	 * 
	 * @param   int        BAN
	 *
	 * @returns HashMap list of Phone Numbers <String, String>	   
	 * @throws ApplicationException
	 */
	Map retrievePhoneNumbersForBAN(int ban) throws ApplicationException;

	VoiceUsageSummaryInfo[] retrieveVoiceUsageSummary(int ban, String featureCode) throws ApplicationException;

	/**
	 * Retrieve Account Information for WPS subscriber.
	 *
	 * @param int      BAN (Billing Account Number)
	 * @param String   Phonenumber
	 *
	 * @return PrepaidConsumerAccountInfo
	 * @throws ApplicationException
	 */
	PrepaidConsumerAccountInfo retrieveAccountInfo(int ban, String phoneNumber) throws ApplicationException;

	/**
	 * Returns all accounts satisfying the Search by Business Name conditions.
	 *
	 * @param nameType
	 * @param legalBusinessName
	 * @param legalBusinessNameExactMatch
	 * @param accountStatus
	 * @param accountType
	 * @param provinceCode
	 * @param maximum
	 * @throws ApplicationException
	 */
	SearchResultsInfo retrieveAccountsByBusinessName(String nameType, String legalBusinessName, boolean legalBusinessNameExactMatch, char accountStatus, char accountType, String provinceCode,
			int brandId, int maximum) throws ApplicationException;

	/**
	 * Returns all accounts satisfying the Search by Name conditions.
	 *
	 * @param nameType
	 * @param firstName
	 * @param firstNameExactMatch
	 * @param lastName
	 * @param lastNameExactMatch
	 * @param accountStatus
	 * @param accountType
	 * @param provinceCode
	 * @param maximum
	 * @throws ApplicationException
	 */
	SearchResultsInfo retrieveAccountsByName(String nameType, String firstName, boolean firstNameExactMatch, String lastName, boolean lastNameExactMatch, char accountStatus, char accountType,
			String provinceCode, int brandId, int maximum) throws ApplicationException;

	/**
	 * Added by Roman
	 *  returns UnpaidAirTimeTotal information for ban
	 * @param int BAN
	 * @returns double
	 * @throws ApplicationException
	 *
	 */
	double retrieveUnpaidAirTimeTotal(int ban) throws ApplicationException;

	/**
	 * Retrieve Unpaid Airtime Usage Charge Info for given ban
	 * @param int ban
	 * @return AirtimeUsageChargeInfo for given the BAN
	 * @throws ApplicationException
	 */
	AirtimeUsageChargeInfo retrieveUnpaidAirtimeUsageChargeInfo(int ban) throws ApplicationException;

	/**
	 * Retrieve Auto Top-Up information for a Pay&Talk subscriber from Pay&Talk database).
	 *
	 * @param int      BAN (Billing Account Number)
	 * @param String   Phonenumber
	 *
	 * @return AutoTopUpInfo automatic top-up information
	 * @throws ApplicationException
	 *
	 */
	AutoTopUpInfo retrieveAutoTopUpInfoForPayAndTalkSubscriber(int pBan, String pPhoneNumber) throws ApplicationException;

	/**
	 * Retrieve Memo  By billing account number (BAN)
	 *
	 * @param   int     billing account number (BAN)
	 * @param   int     maximum memos
	 *
	 * @returns  Memo Info
	 *
	 * @throws ApplicationException
	 */
	List retrieveMemos(int Ban, int Count) throws ApplicationException;

	/**
	 * Retrieve memos by given set of criteria
	 *
	 * <P>This method returns an array of Memo objects.</P>
	 * @param MemoCriteriaInfo memo criteria
	 * @return Memo[] memos
	 * @throws ApplicationException
	 */
	List retrieveMemos(MemoCriteriaInfo MemoCriteria) throws ApplicationException;

	/**
	 * Retrieve Last Memo  By billing account number (BAN), Memo Type
	 *
	 * @param   int     billing account number (BAN)
	 * @param   String Memo Type
	 *
	 * @returns  Memo Info
	 * @throws ApplicationException
	 *
	 */
	MemoInfo retrieveLastMemo(int Ban, String MemoType) throws ApplicationException;

	/**
	 * Retrieve light-weighted Account By PhoneNumber
	 *
	 * @param   String      phone number
	 * @returns AccountInfo account related information
	 * @throws ApplicationException
	 *
	 * @see AccountInfo
	 */
	AccountInfo retrieveLwAccountByPhoneNumber(String pPhoneNumber) throws ApplicationException;

	/**
	 * Retrieve List of Accounts  that were created, or had a subscriber added, by a given
	 * dealership from a specific date.
	 * @param dealerCode the dealership to search on.
	 * @param startDate date to start searching from.
	 * @param endDate   date to search to.
	 * @param maximum the total number of bans to retrieve.
	 * @returns Collection  list of AccountInfo
	 * @see AccountInfo
	 */
	List retrieveAccountsByDealership(char accountStatus, String dealerCode, java.util.Date startDate, java.util.Date endDate, int maximum) throws ApplicationException;

	/**
	 * @param ban
	 * @return
	 * @deprecated
	 */
	BillNotificationContactInfo getLastEBillNotificationSent(int ban) throws ApplicationException;

	/**
	 * @param ban
	 * @param subscriptionType
	 * @return
	 * @deprecated
	 */
	List getBillNotificationHistory(int ban, String subscriptionType) throws ApplicationException;

	/**
	 * @param ban
	 */
	void expireBillNotificationDetails(int ban) throws ApplicationException;

	/**
	 * Retrieve List of Accounts  that were created, or had a subscriber added, by a given
	 * salesRep from a specific date.
	 *
	 * @param dealerCode the dealership to search on.
	 * @param salesRepCode the sales representative to search on.
	 * @param startDate date to start searching from.
	 * @param endDate   date to search to.
	 * @param maximum the total number of bans to retrieve.
	 *
	 * @returns Collection  list of AccountInfo	 
	 *
	 * @see AccountInfo
	 */
	List retrieveAccountsBySalesRep(char accountStatus, String dealerCode, String salesRepCode, java.util.Date startDate, java.util.Date endDate, int maximum);

	/**
	 * Retrieve Account Information for WPS subscriber.
	 *
	 * @param int      BAN (Billing Account Number)
	 * @param String   Phonenumber
	 *
	 * @return PrepaidConsumerAccountInfo
	 * @throws ApplicationException 
	 */
	PrepaidConsumerAccountInfo retrieveAccountInfoForPayAndTalkSubscriber(int ban, String phoneNumber) throws ApplicationException;

	/**
	 * Returns matching ban ids.
	 *
	 */

	int[] retrieveBanIds(char accountType, char accountSubType, char banStatus, int maximum);

	/**
	 * Returns matching ban ids.
	 */

	int[] retrieveBanIdByAddressType(char accountType, char accountSubType, char banStatus, char addressType, int maximum);

	/**
	 * Returns number of subscribers, attached to this fleet.
	 *
	 * @params  FleetIdentityInfo, int Ban
	 * @return  int number of subscribers
	 */
	int retrieveAttachedSubscribersCount(int ban, FleetIdentityInfo fleetIdentity) throws ApplicationException;

	/**
	 * Retrieves Follow Up's additional text.
	 *
	 * @param ban
	 * @param followUpId
	 * @return
	 */
	List retrieveFollowUpAdditionalText(int ban, int followUpId) throws ApplicationException;

	/**
	 * Retrieves the history for a given follow up.
	 *
	 * @param followUpId
	 * @return
	 */
	List retrieveFollowUpHistory(int followUpId);

	/**
	 * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	 *
	 * @param   int      billing account number (BAN)
	 * @param   int  Follow Up ID
	 *
	 */
	FollowUpInfo retrieveFollowUpInfoByBanFollowUpID(int ban, int followUpID) throws ApplicationException;

	/**
	 * Retrieves the history for a given follow up.
	 *
	 * @param followUpCriteria
	 * @return
	 */
	List retrieveFollowUps(FollowUpCriteriaInfo followUpCriteria);

	/**
	 * Retrieve Follow Up Info for a given billing account number (BAN) and Follow Up ID
	 *
	 * @param   int      billing account number (BAN)
	 * @param   int      maximum Follow Ups
	 *
	 */
	List retrieveFollowUps(int ban, int Count) throws ApplicationException;

	/**
	 * Retrieves statistics on the open follow ups for a given BAN.
	 *
	 * @param ban
	 * @return
	 */
	FollowUpStatisticsInfo retrieveFollowUpStatistics(int ban) throws ApplicationException;

	/**
	 * Retrieve Last Follow Up ID a given billing account number (BAN) and Follow Up Type
	 *
	 * @param   int      billing account number (BAN)
	 * @param   String  Follow Up Type
	 *
	 */
	int retrieveLastFollowUpIDByBanFollowUpType(int ban, String followUpType) throws ApplicationException;

	/**
	 * 
	 * @param ban
	 */
	ProductSubscriberListInfo[] retrieveProductSubscriberLists(int ban) throws ApplicationException;

	/**
	 * Retrieves the bill parameters given an BAN
	 * 
	 * @param banId BAN
	 * 
	 * @returns BillParametersInfo
	 * @exception ApplicationException
	 */
	BillParametersInfo retrieveBillParamsInfo(int banId) throws ApplicationException;

	/**
	 * Retrieve fleets associated to a ban
	 *
	 * @param   int       billing account number (BAN)
	 * @returns FleetInfo[]  array of FleetInfo
	 */
	FleetInfo[] retrieveFleetsByBan(int ban) throws ApplicationException;

	/**
	 * Retrieve Accounts By IMSI
	 *
	 * @param   String      imsi
	 * @throws ApplicationException 
	 * @returns Collection of AccountInfo account related information
	 *
	 * @see AccountInfo
	 */
	AccountInfo retrieveAccountByImsi(String imsi) throws ApplicationException;

	/**
	 * Retrieves a list containing up to 'maxNumbers' subscriber Ids.
	 * 
	 * @param   int   billing account number (BAN)
	 * @param   char  status of the subscriber
	 * @param   int   maximum of phone numbers to return
	 * 
	 * @returns list of subscriber IDs
	 * @exception ApplicationException
	 */
	List retrieveSubscriberIdsByStatus(int banId, char status, int maximum) throws ApplicationException;

	/**
	 * Retrieve Accounts By SerialNumber (ESN, IMEI etc)
	 *
	 * @param   String      equipment serial number
	 * @throws ApplicationException 
	 * @returns Collection of AccountInfo account related information
	 *
	 * @see AccountInfo
	 */
	List retrieveAccountsBySerialNumber(String serialNumber) throws ApplicationException;

	AccountInfo retrieveAccountBySerialNumber(String serialNumber) throws ApplicationException;

	/**
	 * Retrieve Credit Check Memo Info By billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * @returns  Memo Info
	 *
	 */
	MemoInfo retrieveLastCreditCheckMemoByBan(int ban) throws ApplicationException;

	/**
	 * Return the list of subscriber phone numbers on this account filtered by specified subscriber status upto the
	 * specified maximum.
	 *
	 * @param   int       billing account number (BAN)
	 * @param char        the subscriber's status ( see one of the Subscriber.STATUS_xxx constants )
	 * @param   int       maximum of suspended phone numbers to return
	 * @returns String[]  list of phone numbers   
	 */
	List retrieveSubscriberPhoneNumbersByStatus(int banId, char status, int maximum) throws ApplicationException;

	String retrieveCorporateName(int id) throws ApplicationException;

	/**
	 * @param ban
	 * @return
	 */
	int getClientAccountId(int ban) throws ApplicationException;

	/**
	 * Retrieve Last Payment Activity
	 *
	 * @param   int      ban
	 * @throws ApplicationException 
	 * @returns PaymentHistoryInfo
	 *
	 */
	PaymentHistoryInfo retrieveLastPaymentActivity(int ban) throws ApplicationException;

	/**
	 * Retrieve Subscriber Invoice Detail
	 *
	 * @param int   Billing Account Number (BAN)
	 * @param int   Bill Sequence Number 
	 * @return SubscriberInvoiceDetailInfo
	 * 
	 * @throws ApplicationException
	 */
	List retrieveSubscriberInvoiceDetails(int banId, int billSeqNo) throws ApplicationException;

	/**
	 * Returns the BillNotification Contact Info
	 * @param int ban
	 * @return Collection<BillNotificationContactInfo>
	 * @deprecated
	 */
	List retrieveBillNotificationContacts(int ban) throws ApplicationException;

	/**
	 * Returns Collection of ChargeInfo
	 * @param int ban
	 * @param int pBillSeqNo
	 * @param String pPhoneNumber
	 * @param Date from
	 * @param Date to
	 * @return Collection<ChargeInfo>
	 */
	List retrieveBilledCharges(int ban, int pBillSeqNo, String pPhoneNumber, Date from, Date to) throws ApplicationException;

	/**
	 * Retrieve Associated TalkGroup Count
	 * 
	 * @param pFleeIdentity
	 * @param ban
	 * @return int	count 
	 */
	int retrieveAssociatedTalkGroupsCount(FleetIdentityInfo pFleeIdentity, int ban) throws ApplicationException;

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
	AddressHistoryInfo[] retrieveAddressHistory(int pBan, Date pFromDate, Date pToDate) throws ApplicationException;

	/**
	 * Returns Paper Bill Suppression At Activation Indicator
	 * 
	 * @param pBan			billing account number (BAN)
	 * @return				one of AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN,
	 * AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_NO, BILL_SUPPRESSION_AT_ACTIVATION_YES
	 * @exception ApplicationException
	 * @deprecated
	 */
	String getPaperBillSupressionAtActivationInd(int pBan) throws ApplicationException;

	/**
	 * Checks if has EPostSubscription
	 * 
	 * @param ban  int
	 * @return	boolean
	 * @deprecated
	 */
	boolean hasEPostSubscription(int ban) throws ApplicationException;

	/**
	 * Checks the FeatureCatogory Exists on the Subscriber
	 * 
	 * @param int       billing account number (BAN)
	 * @param String   pCategoryCode 
	 * @return boolean
	 */
	boolean isFeatureCategoryExistOnSubscribers(int ban, String pCategoryCode) throws ApplicationException;

	/**
	 * Retrieve AlternateAddress By billing account number (BAN)
	 *
	 * @param   int    billing account number (BAN)
	 * @returns Alternate Address Info account related information
	 *
	 * @see AddressInfo
	 */
	AddressInfo retrieveAlternateAddressByBan(int ban) throws ApplicationException;

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
	double getPrepaidActivationCredit(String applicationId, String pUserId, PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException;

	/**
	 * Returns a list of BusinessCreditIdentityInfo objects.
	 *
	 * @param   		int	- billing account number (BAN)
	 * @return		BusinessCreditIdentityInfo[] - array of BusinessCreditIdentityInfo (not null but could be empty)
	 */
	BusinessCreditIdentityInfo[] retrieveBusinessList(int ban) throws ApplicationException;

	/**
	 * Retrieve the authorized names By billing account number (BAN)
	 *
	 * @param   int     billing account number (BAN)	  
	 */
	ConsumerNameInfo[] retrieveAuthorizedNames(int ban) throws ApplicationException;

	/**
	 * Returns deposit history
	 *
	 * @param   int       billing account number (BAN)
	 * @param   Date      from Date
	 * @param   Date      to Date
	 * @returns DepositHistoryInfo[]  array of DepositHistoryInfo (not null but could be empty)
	 */
	DepositHistoryInfo[] retrieveDepositHistory(int ban, Date fromDate, Date toDate) throws ApplicationException;

	/**
	 * Gets Deposit History
	 * @param int ban
	 */
	DepositAssessedHistoryInfo[] retrieveDepositAssessedHistoryList(int ban) throws ApplicationException;

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
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, char level, String subscriber, int maximum) throws ApplicationException;

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
	 */
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, char level, String knowbilityOperatorId, String subscriber, int maximum)
			throws ApplicationException;

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
	 * @returns CreditInfo[] array of CreditInfo (not null but could be empty)
	 *
	 */
	SearchResultsInfo retrieveCredits(int ban, java.util.Date fromDate, java.util.Date toDate, String billState, String knowbilityOperatorId, String reasonCode, char level, String subscriber,
			int maximum) throws ApplicationException;

	/**
	 * Returns the credits based on the follow-up id
	 *
	 *
	 * @param int followup id
	 */
	List retrieveCreditByFollowUpId(int followUpId) throws ApplicationException;

	/**
	 * Retrieves Payment History
	 * 
	 * @param int ban 
	 * @param Date pFromDate
	 * @param Date pToDate
	 * @return	CollectionPaymentHistoryInfo
	 */
	List retrievePaymentHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException;

	/**
	 * Returns Invoice Properties
	 * 
	 * @param int   ban
	 * @return  InvoicePropertiesInfo
	 */
	InvoicePropertiesInfo getInvoiceProperties(int ban) throws ApplicationException;

	/**
	 * 
	 * @param ban
	 * @param chargeCodes
	 * @param billState
	 * @param level
	 * @param subscriberId
	 * @param from
	 * @param to
	 * @param maximum
	 * @return
	 * @throws ApplicationException
	 */
	ChargeInfo[] retrieveCharges(int ban, String[] chargeCodes, String billState, char level, String subscriberId, Date from, Date to, int maximum) throws ApplicationException;

	/**
	 * Gets Deposit History
	 * @param int ban
	 */
	DepositAssessedHistoryInfo[] retrieveOriginalDepositAssessedHistoryList(int ban) throws ApplicationException;

	/**
	 * Retrieves Hotlined Phone Number
	 * 
	 * @param int BAN
	 * @return PhoneNumber String 
	 */
	String retrieveHotlinedSubscriberPhoneNumber(int ban) throws ApplicationException;

	/**
	 * Returns InvoiceHistoryInfo for a given ban and dates
	 * 
	 * @param  int ban
	 * @param Date fromDate
	 * @param Date toDate
	 * @return Collection of Invoice History
	 */
	List retrieveInvoiceHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException;

	/**
	 * Gives the count of Attached Subscribers Talk group
	 * 
	 * @param urbanID
	 * @param fleetID
	 * @param talkGroupId
	 * @param ban
	 * @return int Count 
	 */
	int retrieveAttachedSubscribersCountForTalkGroup(int urbanID, int fleetID, int talkGroupId, int ban) throws ApplicationException;

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
	CollectionHistoryInfo[] retrieveCollectionHistoryInfo(int banId, Date fromDate, Date toDate) throws ApplicationException;

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
	//SearchResultsInfo retrieveLetterRequests(int banId, java.util.Date from, java.util.Date to, char level, String pSubscriber, int maximum) throws ApplicationException;

	/**
	 *Returns List of Payment Activities
	 * 
	 * @param int 		banId
	 * @param int 		paymentSeqNo
	 * @return
	 */
	List retrievePaymentActivities(int banId, int paymentSeqNo) throws ApplicationException;

	/**
	 * Returns Payment Method Change HistoryInfo
	 * 
	 * @param int   			 ban
	 * @param java.Util.Date 	fromDate
	 * @param java.util.Date 	toDate
	 * @return
	 */
	List retrievePaymentMethodChangeHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException;

	/**
	 * Returns refund history
	 *
	 * @param ban
	 * @param fromDate
	 * @param toDate
	 * 
	 * @return RefundHistoryInfo
	 * @exception ApplicationException
	 */
	List retrieveRefundHistory(int ban, Date fromDate, Date toDate) throws ApplicationException;

	/**
	 * Returns related charges for a credit
	 *
	 * @param   pBan       		billing account number (BAN)
	 * @param   pChargeSeqNo    charge sequence number
	 * @return List<ChargeInfo> 	List of ChargeInfo (not null but could be empty)
	 *
	 * @exception ApplicationException
	 *
	 */
	List retrieveRelatedChargesForCredit(int pBan, double pChargeSeqNo) throws ApplicationException;

	/**
	 * Retrieve the number of CDMA / HSPA subscribers. This method is created for IVR for performance purpose. 
	 * Instead of looping through the whole subscriber list, this method queries the database according to the
	 * serial number. If it is an DUMMY ESN (for USIM), then it's counted as HSPA. Otherwise, it's a CDMA. 
	 * It includes subscribers with product type "C" only.
	 * @param int - ban
	 * @return HashMap - the number of CDMA/HSPA subscribers
	 */
	HashMap retrievePCSNetworkCountByBan(int ban) throws ApplicationException;

	/**
	 * Returns an array of PoolingPricePlanSubscriberCountInfo representing all subscribers on the given BAN 
	 * who are zero-minute pooling in all pooling groups.
	 *
	 * @param int - billing account number (BAN)
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrieveZeroMinutePoolingPricePlanSubscriberCounts(int banId) throws ApplicationException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * who are zero-minute pooling in a given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param int - pooling group ID
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrieveZeroMinutePoolingPricePlanSubscriberCounts(int banId, int poolGroupId) throws ApplicationException;

	/**
	 * Returns an array of PoolingPricePlanSubscriberCountInfo representing all subscribers on the given BAN 
	 * participating in all pooling groups.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrievePoolingPricePlanSubscriberCounts(int banId) throws ApplicationException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * participating in the given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @param int - pooling group ID
	 * @returns PoolingPricePlanSubscriberCountInfo[]
	 */
	List retrievePoolingPricePlanSubscriberCounts(int banId, int poolGroupId) throws ApplicationException;

	/**
	 * Returns a PoolingPricePlanSubscriberCountInfo object representing all subscribers on the given BAN 
	 * participating in the given pooling group.
	 *
	 * @param int - billing account number (BAN)
	 * @param String[] - service Codes
	 * @param boolean - include Expired flag
	 * @returns ServiceSubscriberCount[]
	 */
	List retrieveServiceSubscriberCounts(int banId, String[] serviceCodes, boolean includeExpired) throws ApplicationException;

	/**
	 * Returns an array of Subscribers with minute-pooling capable price plans and
	 * pooling-enabled features on their contract.
	 *
	 * @param   int       billing account number (BAN)
	 * @returns String    pooling coverage type - long distance or airtime coverage type
	 */
	List retrieveMinutePoolingEnabledPricePlanSubscriberCounts(int banId, String[] poolingCoverageTypes) throws ApplicationException;

	/**
	 * Returns an array of PricePlanSubscriberCountInfo, which represents all subscribers on the given BAN
	 * on dollar pooling price plans.
	 *
	 * @param int - billing account number (BAN)
	 * @param String - product type
	 * @returns PricePlanSubscriberCountInfo[]
	 *
	 */
	List retrieveDollarPoolingPricePlanSubscriberCounts(int banId, String productType) throws ApplicationException;

	/**
	 * Retrieves shareable (Add-A-Line, Family Talk) priceplan subscriber counts
	 * by billing account number (BAN).
	 *
	 * @param String - billing account number (BAN)
	 * @returns PricePlanSubscriberCountInfo
	 */
	List retrieveShareablePricePlanSubscriberCount(int ban) throws ApplicationException;

	/**
	 * Returns related credits for a charge
	 *
	 * @param   pBan       		billing account number (BAN)
	 * @param   pChargeSeqNo    charge sequence number
	 * @return 	List<CreditInfo> 	list of CreditInfo (not null but could be empty)
	 *
	 * @throws ApplicationException
	 */
	List retrieveRelatedCreditsForCharge(int pBan, double pChargeSeqNo) throws ApplicationException;

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
	String validatePayAndTalkSubscriberActivation(String applicationId, String userId, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader) throws ApplicationException;

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
	SearchResultsInfo retrievePendingChargeHistory(int pBan, Date pFromDate, Date pToDate, char level, String pSubscriber, int maximum) throws ApplicationException;

	/**
	 * Returns Collection of TalkGroupInfo 
	 * @param pBan
	 * @return Collection TalkGroupInfo
	 */
	List retrieveTalkGroupsByBan(int ban) throws ApplicationException;

	/**
	 * Returns Account status change history
	 *
	 * @param   int       billing account number (BAN)
	 * @param   Date      from Date
	 * @param   Date      to Date
	 * @returns StatusChangeHistoryInfo[]  array of StatusChangeHistoryInfo
	 *
	 * @exception ApplicationException;
	 */
	List retrieveStatusChangeHistory(int ban, java.util.Date fromDate, java.util.Date toDate) throws ApplicationException;

	/**
	 * Returns the customer contact info such as phone numbers and email address.
	 * @param int ban
	 * @return ContactDetailInfo
	 * @throws ApplicationException
	 */
	ContactDetailInfo getCustomerContactInfo(int ban) throws ApplicationException;

	/**
	 * Retrieves the last credit check info by billing account number (BAN), merging CDA and KB data
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType) throws ApplicationException;
	
	/**
	 * Retrieves only the KB credit check info by billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType) throws ApplicationException;

	BillingPropertyInfo retrieveBillingInformation(int billingAccountNumber) throws ApplicationException;

	ContactPropertyInfo retrieveContactInformation(int billingAccountNumber) throws ApplicationException;

	PersonalCreditInfo retrievePersonalCreditInformation(int ban) throws ApplicationException;

	BusinessCreditInfo retrieveBusinessCreditInformation(int ban) throws ApplicationException;

	String[] retrieveSubscriberIdsByServiceFamily(AccountInfo account, String familyTypeCode, Date effectiveDate) throws ApplicationException;

	String[] retrieveSubscriberIdsByServiceFamily(int banId, String familyTypeCode, Date effectiveDate) throws ApplicationException;

	SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(AccountInfo account, String[] dataSharingGroupCodes, Date effectiveDate) throws ApplicationException;

	SubscribersByDataSharingGroupResultInfo[] retrieveSubscribersByDataSharingGroupCodes(int banId, String[] dataSharingGroupCodes, Date effectiveDate) throws ApplicationException;

	SubscriberDataSharingDetailInfo[] retrieveSubscriberDataSharingInfoList(int banId, String[] dataSharingGroupCodes) throws ApplicationException;

	SubscriberEligibilitySupportingInfo getSubscriberEligiblitySupportingInfo(int ban, String[] memoTypeList, Date dateFrom, Date dateTo) throws ApplicationException;

	List retrieveRelatedCreditsForChargeList(List chargeIdentifierInfoList) throws ApplicationException;

	String retrieveChangedSubscriber(int ban, String subscriberId, String productType, Date searchFromDate, Date searchToDate) throws ApplicationException;

	List retrieveAdjustedAmounts(int ban, String adjustmentReasonCode, String subscriberId, Date searchFromDate, Date searchToDate) throws ApplicationException;

	List retrieveApprovedCreditByFollowUpId(int banId, int followUpId) throws ApplicationException;

	CreditInfo retrieveCreditById(int banId, int entSeqNo) throws ApplicationException;

	InternationalServiceEligibilityCheckResultInfo checkInternationalServiceEligibility(int ban) throws ApplicationException;

	CreditCheckResultDeposit[] retrieveDepositsByBan(int ban) throws ApplicationException;

	AccountInfo retrieveAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException;

	AccountInfo retrieveLwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException;
		
	AccountInfo retrieveHwAccountByPhoneNumber(String phoneNumber, PhoneNumberSearchOptionInfo phoneNumberSearchOptionInfo) throws ApplicationException;

	String getNextSeatGroupId() throws ApplicationException;

	List getMaxVoipLineList(int ban, long subscriptionId) throws ApplicationException;

	void createMaxVoipLine(MaxVoipLineInfo maxVoipLineInfo) throws ApplicationException;

	void updateMaxVoipLine(List maxVoipLineInfoList) throws ApplicationException;
	
	List retrieveAccountListByImsi(String imsi) throws ApplicationException;
	
}
