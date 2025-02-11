package com.telus.cmb.account.lifecyclefacade.svc;

import java.util.Date;
import java.util.List;
import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.HCDclpActivationOptionDetailsInfo;
import com.telus.eas.account.info.PaymentArrangementEligibilityResponseInfo;
import com.telus.eas.account.info.PaymentArrangementInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PaymentNotificationInfo;
import com.telus.eas.account.info.PaymentNotificationResponseInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.ServiceCancellationInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public interface AccountLifecycleFacade {

	/**
	 * Obtain sessionId for the given user credentials
	 * 
	 * @param userId The user id.
	 * @param password The password.
	 * @param applicationId The application id.
	 */
	String openSession(String userId, String password, String applicationId) throws ApplicationException;

	/**
	 * Validates a credit card by sending it to the bank.
	 * @param   CreditCardTransactionInfo   all information necessary to perform credit card transaction
	 * @throws  TelusException
	 * @see     CreditCardTransactionInfo
	 */
	CreditCardResponseInfo validateCreditCard(CreditCardTransactionInfo pCreditCardTransactionInfo, String sessionId) throws ApplicationException;

	/**
	 * Pay Bill - Charge Credit Card And Apply Payment or Deposit to billing account.
	 *
	 * @param   int             billing account number
	 * @param   String          payment source type
	 * @param   String          payment source id
	 * @param   double          payment amount
	 * @param   CreditCardInfo  credit card information
	 *
	 * @return  String          Authorization Number/Code
	 *
	 *
	 * @see PaymentInfo
	 */
	String payBill(double amount, CreditCardTransactionInfo creditCardTransactionInfo, int ban, String paymentSourceType, String paymentSourceID, AccountInfo accountInfo,
			boolean notificaiotnSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Pay Deposit - Charge Credit Card And Apply Payment or Deposit to billing account.
	 *
	 * @param   int             billing account number
	 * @param   String          payment source type
	 * @param   String          payment source id
	 * @param   double          payment amount
	 * @param   CreditCardInfo  credit card information
	 *
	 * @return  String          Authorization Number/Code
	 *
	 * @see PaymentInfo	  
	 */
	String payDeposit(double amount, CreditCardTransactionInfo creditCardTransactionInfo, int ban, String paymentSourceType, String paymentSourceID, String sessionId) throws ApplicationException;

	/**
	 * Applies a charge to a credit card. It does not apply the payment to any customers account.
	 *
	 * @param   CreditCardTransactionInfo   all information necessary to perform credit card transaction
	 * @return  String          Authorization Number/Code
	 *
	 * @see     CreditCardTransactionInfo
	 */
	String applyCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException;

	void voidCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException;

	/**
	 * Reverses a charge to a credit card.
	 *
	 * @param   CreditCardTransactionInfo   all information necessary to perform credit card transaction
	 * @return  String           Authorization Number/Code
	 *
	 * @throws  ApplicationException
	 *
	 * @see     CreditCardTransactionInfo
	 */
	String reverseCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException;

	/**
	 * This method refund the credit card and post a refund in KB
	 * 
	 * @param banId
	 * @param paymentSeq
	 * @param reasonCode
	 * @param memoText
	 * @param isManaual
	 * @param creditCardTransactionInfo
	 * @throws ApplicationException
	 */
	void refundCreditCardPayment(int banId, int paymentSeq, String reasonCode, String memoText, CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException;

	CreditCardResponseInfo validateCreditCard(int billingAccountNumber, CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException;

	void registerTopUpCreditCard(int billingAccountNumber, CreditCardInfo creditCard, String sessionId) throws ApplicationException;

	void updateAccountPassword(int ban, String newPassword, String sessionId) throws ApplicationException;

	/**
	 * Create a new memo
	 * @param   MemoInfo    all attributes for the memo (i.e. ban #, memo type, memo text etc)
	 * @exception ApplicationException
	 * @see MemoInfo
	 */
	void asyncCreateMemo(MemoInfo memoInfo, String sessionId) throws ApplicationException;

	/**
	 * This method invoke DSAL SummaryDataServicesUsageService to retrieve unpaid data usage amount for a given BAN. This method shall be used
	 * only for account that does not have any invoice history: newly created account.
	 * 
	 * For account does have invoice history, shall switch to getTotalUnbilledDataAmount( int banId, int billCycleYear, int billCycleMonth, int billCycle )
	 *  
	 * @param banId
	 * @param fromDate
	 * @return
	 * @throws ApplicationException
	 */
	double getTotalDataOutstandingAmount(int banId, java.util.Date fromDate) throws ApplicationException;

	/**
	 * @param banId
	 * @param billCycleYear - the open (current) bill cycle year
	 * @param billCycleMonth - the open (current) bill cycle month
	 * @param billCycle  - bill cycle code
	 * @return
	 * @throws ApplicationException
	 */
	double getTotalUnbilledDataAmount(int banId, int billCycleYear, int billCycleMonth, int billCycle) throws ApplicationException;

	/**
	 * Create a postpaid account
	 * 
	 * @param accountInfo
	 * @param compassCustomerId
	 * @param businessCreditIdentityList
	 * @param selectedBusinessCreditIdentity
	 * @param creditCardAuditHeader
	 * @param auditInfo
	 * 
	 * @return PostpaidAccountCreationResponseInfo
	 * @throws ApplicationException
	 */
	PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Create a postpaid account (with addition of the creditCheckInd for future CDA release)
	 * 
	 * @param accountInfo
	 * @param compassCustomerId
	 * @param businessCreditIdentityList
	 * @param selectedBusinessCreditIdentity
	 * @param creditCheckInd
	 * @param creditCardAuditHeader
	 * @param auditInfo
	 * 
	 * @return PostpaidAccountCreationResponseInfo
	 * @throws ApplicationException
	 */
	PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, String creditCheckInd, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, BusinessCreditIdentityInfo[] businessCreditIdentityList, BusinessCreditIdentityInfo selectedBusinessCreditIdentity,
			boolean manualCreditCheck, String sessionId) throws ApplicationException;

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, ConsumerNameInfo consumerNameInfo, AddressInfo addressInfo, boolean manualCreditCheck, AuditHeader auditHeader, String sessionId)
			throws ApplicationException;

	int createPrepaidAccount(AccountInfo pAccountInfo, String compassCustomerId, String businessRole, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId)
			throws ApplicationException;

	AddressValidationResultInfo validateAddress(AddressInfo addressInfo) throws ApplicationException;

	void asyncInsertBillingAccount(int billingAccountNumber, String customerID, String processType, String sessionId);

	void insertBillingAccount(int billingAccountNumber, String customerID, String processType, String sessionId) throws ApplicationException;

	void asyncUpdateBillingAccount(AccountInfo accountInfo, String processType, String sessionId);

	void asyncUpdateBillingAccount(int ban, String processType, String sessionId);

	void updateBillingAccount(AccountInfo accountInfo, String processType, String sessionId) throws ApplicationException;

	void updateBillingAccount(int ban, String processType, String sessionId) throws ApplicationException;

	void asyncInsertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId);

	void insertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId) throws ApplicationException;

	boolean isEnterpriseManagedData(int brandId, char accountType, char accountSubType, String productType, String processType) throws ApplicationException;

	/** Following methods are based on AccountLifecycleManager
	 * 
	 * BEGIN
	 */
	/**
	 * Create a new account - includes validation of all information except address and duplicate
	 *                        account check. It is assumed that the address that is being passed in
	 *                        is the address that needs to be stored. (It is up to the client to perform address validation if
	 *                        required.)
	 * @param   AccountInfo   all general attributes of the account (i.e. account type, sub type etc)
	 * @return  int           billing account number of the newly created account
	 * @see AccountInfo
	 */
	int createAccount(AccountInfo pAccountInfo, String sessionId) throws ApplicationException;

	/**
	 * Update an account - includes validation of all information except address
	 * and duplicate account check. It is assumed that the address that is being
	 * passed in is the address that needs to be stored, therefore the address
	 * apply type is set to 'I'. (It is up to the client to perform address
	 * validation if required.)
	 *
	 * @param int
	 *            billing account number
	 * @param AccountInfo
	 *            all general attributes of the account (i.e. account type, sub
	 *            type etc)
	 *
	 * @see AccountInfo
	 *
	 * @exception ApplicationException
	 */
	void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException;

	/**
	 * Update Bill cycle for a account
	 * 
	 * @param int BAN - billing account number
	 * @param short bill cycle
	 */
	void updateBillCycle(int ban, short newBillCycle, String sessionId) throws ApplicationException;

	void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException;

	/**
	 * Update Payment Method for a billing account
	 * @param pBan  billing account number of the newly created account
	 * @param accountInfo 
	 * @param pPaymentMethodInfo payment method attributes
	 * @param notificaitonSuppressionInd
	 * @param auditInfo
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updatePaymentMethod(int pBan, AccountInfo accountInfo, PaymentMethodInfo pPaymentMethodInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException;

	/**
	 * Cancel an account
	 * 
	 * The account has to be in 'Open' or 'Suspended' status.
	 *
	 * Under certain conditions the subscriber is charged a cancellation penalty which will be
	 * applied automatically unless the fee is waived by giving a reason.
	 *
	 * @param   int         billing account number  (mandatory)
	 * @param   Date        date on which to cancel the account (optional - defaul: today)
	 * @param   String      reason code for cancelling the account (mandatory)
	 * @param   String      method by which the deposit should be returned (mandatory)
	 *                      possible values are: 'O' cover open debts
	 *                                           'R' refund entire amount
	 *                                           'E' refund excess amount
	 * @param   String      reason for waving applicable cancellation fee (optional - default: fee is charged))
	 * @param   String      optional comment for cancelling the account (optional)
	 *
	 * @exception ApplicationException
	 */
	void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason, String userMemoText, String phoneNumber,
			boolean notificationSuppressionInd, AuditInfo auditInfo,String sessionId) throws ApplicationException;

	
	 void cancelAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, boolean isBrandPort, CommunicationSuiteInfo commSuiteInfo ,
			 boolean notificationSuppressionInd,String sessionId) throws ApplicationException ;

		
	/**
	 * Suspend an account
	 * 
	 * The account has to be in 'Open' status.
	 *
	 * @param   int         billing account number  (mandatory)
	 * @param   Date        date on which to suspend the account (optional - defaul: today)
	 * @param   String      reason code for suspending the account (mandatory)
	 * @param   String      optional comment for suspending the account (optional)
	 * @param   Integer      brandId
	 * @param  Boolean   isPostpaidBusinessConnect
	 *
	 * @exception ApplicationException
	 */
	void suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText, Integer brandId, Boolean isPostpaidBusinessConnect, String sessionId) throws ApplicationException;

	/**
	 * Suspend Account for Port Out
	 * @param int banNumber  - Billing Account Number
	 * @param String deactivationReason - deactivation reason code 
	 * @param Date activityDate - activity date 
	 * @param String portOutInd - port out indicator 
	 * @param CommunicationSuiteInfo - commSuiteInfo 
	 * @excpetion ApplicationException
	 */
	void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, CommunicationSuiteInfo commSuiteInfo ,String sessionId) throws ApplicationException;

	/**
	 * 
	 * @param ban
	 * @param activityDate
	 * @param activityReasonCode
	 * @param depositReturnMethod
	 * @param subscriberIds
	 * @param waiveReason
	 * @param userMemoText
	 * @param isIDEN
	 * @param notificationSuppressionInd
	 * @param srpdsHeader
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void cancelSubscribers(int ban, java.util.Date activityDate, String activityReasonCode, char depositReturnMethod, String[] subscriberIds, String[] waiveReason, String userMemoText, boolean isIDEN,
			boolean notificationSuppressionInd, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException;

	/**
	 * Cancels a list of subscribers
	 *
	 * @param  int       ban
	 * @param  Date 	 activityDate
	 * @param  String    activityReasonCode
	 * @param  String[]  subscriberId 
	 * @param  String    userMemoText
	 * @param brandId  Integer
	 * @param isPostpaidBusinessConnect Boolean
	 * @throws ApplicationException
	 *
	 */
	void suspendSubscribers(int ban, Date activityDate, String activityReasonCode, String[] subscriberIds, String userMemoText,String sessionId)
			throws ApplicationException;

	/**
	 * restored a cancelled list of subscribers
	 *
	 * @param int		ban 
	 * @param Date  	restoreDate 
	 * @param String 	restoreReasonCode 
	 * @param String[] 	subscriberIds 
	 * @param String  	restoreComment 
	 * @param brandId Integer
	 * @param isPostpaidBusinessConnect Boolean
	 * @throws ApplicationException
	 *
	 */
	void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode, String[] subscriberIds, String restoreComment, Integer brandId, Boolean isPostpaidBusinessConnect,
			Character accoountStaus, String sessionId) throws ApplicationException;

	/**
	 * Restore Suspended Account/Subscribers
	 *
	 * This method restores a suspended account to 'open' status and also restores suspended subscribers
	 * to 'active' status.
	 * Depending on the value passed in for the last parameter, all suspended subscribers will be
	 * restored or only the subscribers that were suspended for collection reasons.
	 *
	 * @param   int         billing account number  (mandatory)
	 * @param   Date        date on which to restore the account (optional - defaul: today)
	 * @param   String      reason code for restoring suspended account (mandatory)
	 * @param   String      optional comment for restoring the account (optional)
	 * @param   boolean     restore subscribers suspended for collection reasons
	 *                      true - only subscribers suspended for collection reasons are restored
	 *                      false - all suspended subscribers are restored
	 *@param brandId Integer
	 * @param isPostpaidBusinessConnect Boolean
	 *
	 * @excpetion TelusException
	 * 
	 * @ejbgen:remote-method
	 */
	void restoreSuspendedAccount(int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, Integer brandId, Boolean isPostpaidBusinessConnect,
			String sessionId) throws ApplicationException;

	/**
	 * END
	 * 
	 */

	int createCustomerAndAccount(AccountInfo pAccountInfo, String customerId, String sessionId) throws ApplicationException;

	public void processBillMediumChanges(int banId, char accountType, int brandId, String subscriberId, String activityType, String sessionId) throws ApplicationException;
	
	void asyncCreateFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException;

	void asyncUpdateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText, String sessionId) throws ApplicationException;

	void asyncUpdateCreditCheckResult(int pBan, String pCreditClass, CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo, String pDepositChangedReasonCode, String pDepositChangeText,
			String sessionId) throws ApplicationException;

	void asyncSaveCreditCheckInfo(int ban, PersonalCreditInfo personalCreditInfo, CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException;

	void asyncSaveCreditCheckInfoForBusiness(int ban, BusinessCreditInfo businessCreditInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness,
			CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException;

	void applyCreditToAccount(CreditInfo delegate, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	double adjustCharge(ChargeInfo delegate, double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo,
			String sessionId) throws ApplicationException;

	void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException;

	void applyPaymentToAccount(AccountInfo accountInfo, PaymentInfo pPaymentInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;
	
	void asyncSubmitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException;

	void submitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException;

	CreditCheckResultInfo checkNewSubscriberEligibility(AccountInfo accountInfo, int subscriberCount, double thresholdAmount, String sessionId) throws ApplicationException;

	HCDclpActivationOptionDetailsInfo getCLPActivationOptionsDetail(int ban) throws ApplicationException;

	String processPayment(double paymentAmount, AccountInfo accountInfo, CreditCardTransactionInfo creditCardTransactionInfo, String paymentSourceType, String paymentSourceID,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	List getCreditEvaluationBusinessList(AccountInfo accountInfo, String sessionId) throws ApplicationException;

	List getDuplicateAccountList(AccountInfo accountInfo) throws ApplicationException;

	void updateCreditWorthiness(int ban, String creditProgram, String creditClass, double creditLimit, String limitChangeText, boolean isCreditProfileChange,
			CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String creditResultChangeText, boolean isCreditResultChange, boolean isAsync, AuditInfo auditInfo,
			AuditHeader auditHeader, String sessionId) throws ApplicationException;

	void updateCreditWorthiness(int ban, String creditClass, double creditLimit, String limitChangeText, boolean isCreditProfileChange,
			CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String creditResultChangeText, boolean isCreditResultChange, boolean isAsync, AuditInfo auditInfo,
			AuditHeader auditHeader, String sessionId) throws ApplicationException;
	
	void updateCreditProfile(int ban, String creditClass, double creditLimit, String memoText, String sessionId) throws ApplicationException;

	void updateCreditCheckResult(int ban, String creditClass, CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String memoText,
			String sessionId) throws ApplicationException;
	
	boolean validateCommunicationSuiteEligibility(int brandId, char accountType, char accountSubType) throws ApplicationException ;
	
}
