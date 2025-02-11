package com.telus.cmb.account.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
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
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public interface AccountLifecycleFacadeRemote extends EJBObject {

	String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;

	CreditCardResponseInfo validateCreditCard(CreditCardTransactionInfo pCreditCardTransactionInfo, String sessionId) throws ApplicationException, RemoteException;;

	void voidCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException, RemoteException;

	String payBill(double amount, CreditCardTransactionInfo creditCardTransactionInfo, int ban, String paymentSourceType, String paymentSourceID, AccountInfo accountInfo,
			boolean notificaiotnSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	String payDeposit(double amount, CreditCardTransactionInfo creditCardTransactionInfo, int ban, String paymentSourceType, String paymentSourceID, String sessionId)
			throws ApplicationException, RemoteException;

	String applyCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException, RemoteException;

	String reverseCreditCardCharge(CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException, RemoteException;

	void refundCreditCardPayment(int banId, int paymentSeq, String reasonCode, String memoText, CreditCardTransactionInfo creditCardTransactionInfo, String sessionId)
			throws ApplicationException, RemoteException;
	
	CreditCardResponseInfo validateCreditCard(int billingAccountNumber, CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException, RemoteException;

	void registerTopUpCreditCard(int billingAccountNumber, CreditCardInfo creditCard, String sessionId) throws ApplicationException, RemoteException;

	void updateAccountPassword(int ban, String newPassword, String sessionId) throws ApplicationException, RemoteException;

	void asyncCreateMemo(MemoInfo memoInfo, String sessionId) throws ApplicationException, SystemException, RemoteException;

	double getTotalDataOutstandingAmount(int banId, java.util.Date fromDate) throws ApplicationException, SystemException, RemoteException;

	double getTotalUnbilledDataAmount(int banId, int billCycleYear, int billCycleMonth, int billCycle) throws ApplicationException, SystemException, RemoteException;

	PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, String creditCheckInd, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId)
					throws ApplicationException, RemoteException;

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, BusinessCreditIdentityInfo[] businessCreditIdentityList, BusinessCreditIdentityInfo selectedBusinessCreditIdentity,
			boolean manualCreditCheck, String sessionId) throws ApplicationException, RemoteException;

	CreditCheckResultInfo checkCredit(AccountInfo accountInfo, ConsumerNameInfo consumerNameInfo, AddressInfo addressInfo, boolean manualCreditCheck, AuditHeader auditHeader, String sessionId)
			throws ApplicationException, RemoteException;

	int createPrepaidAccount(AccountInfo pAccountInfo, String compassCustomerId, String businessRole, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	AddressValidationResultInfo validateAddress(AddressInfo addressInfo) throws ApplicationException, RemoteException;

	void asyncInsertBillingAccount(int billingAccountNumber, String customerID, String processType, String sessionId) throws RemoteException;

	void insertBillingAccount(int billingAccountNumber, String customerID, String processType, String sessionId) throws ApplicationException, RemoteException;

	void asyncUpdateBillingAccount(AccountInfo accountInfo, String processType, String sessionId) throws RemoteException;

	void asyncUpdateBillingAccount(int ban, String processType, String sessionId) throws RemoteException;

	void updateBillingAccount(AccountInfo accountInfo, String processType, String sessionId) throws ApplicationException, RemoteException;

	void updateBillingAccount(final int ban, String processType, String sessionId) throws ApplicationException, RemoteException;

	void asyncInsertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId) throws RemoteException;

	void insertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId) throws ApplicationException, RemoteException;

	boolean isEnterpriseManagedData(int brandId, char accountType, char accountSubType, String productType, String processType) throws ApplicationException, RemoteException;

	/**
	 * Following methods are based on AccountLifecycleManager
	 * 
	 * BEGIN
	 */
	int createAccount(AccountInfo pAccountInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateBillCycle(int ban, short newBillCycle, String sessionId) throws ApplicationException, RemoteException;

	void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException, RemoteException;

	void updatePaymentMethod(int pBan, AccountInfo accountInfo, PaymentMethodInfo pPaymentMethodInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason, String userMemoText, String phoneNumber,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;
	
	void cancelAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, boolean isBrandPort, CommunicationSuiteInfo commSuiteInfo ,
			 boolean notificationSuppressionInd,String sessionId) throws ApplicationException ,RemoteException;


	void suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText, Integer brandId, Boolean isPostpaidBusinessConnect, String sessionId)
			throws ApplicationException, RemoteException;

	void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, CommunicationSuiteInfo commSuiteInfo ,String sessionId) throws ApplicationException ,RemoteException;

	void cancelSubscribers(int ban, java.util.Date activityDate, String activityReasonCode, char depositReturnMethod, String[] subscriberIds, String[] waiveReason, String userMemoText, boolean isIDEN,
			boolean notificationSuppressionInd,ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException, RemoteException;

	void suspendSubscribers(int ban, Date activityDate, String activityReasonCode, String[] subscriberIds, String userMemoText, String sessionId)
			throws ApplicationException, RemoteException;

	void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode, String[] subscriberIds, String restoreComment, Integer brandId, Boolean isPostpaidBusinessConnect,
			Character accoountStaus, String sessionId) throws ApplicationException, RemoteException;

	void restoreSuspendedAccount(int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, Integer brandId, Boolean isPostpaidBusinessConnect,
			String sessionId) throws ApplicationException, RemoteException;

	/**
	 * END
	 * 
	 */
	
	int createCustomerAndAccount(AccountInfo pAccountInfo, String customerId, String sessionId) throws ApplicationException, RemoteException;

	void processBillMediumChanges(int banId, char accountType, int brandId, String subscriberId, String activityType, String sessionId) throws ApplicationException ,RemoteException;		

	void asyncCreateFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException, RemoteException;

	void asyncUpdateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void asyncUpdateCreditCheckResult(int pBan, String pCreditClass, CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo, String pDepositChangedReasonCode, String pDepositChangeText,
			String sessionId) throws ApplicationException, RemoteException;

	void asyncSaveCreditCheckInfo(int ban, PersonalCreditInfo personalCreditInfo, CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId)
			throws ApplicationException, RemoteException;

	void asyncSaveCreditCheckInfoForBusiness(int ban, BusinessCreditInfo businessCreditInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness,
			CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException, RemoteException;

	void applyCreditToAccount(CreditInfo delegate, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	double adjustCharge(ChargeInfo delegate, double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo,
			String sessionId) throws ApplicationException, RemoteException;

	void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException, RemoteException;


	void applyPaymentToAccount(AccountInfo accountInfo, PaymentInfo pPaymentInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	void asyncSubmitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException, RemoteException;

	void submitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException, RemoteException;

	CreditCheckResultInfo checkNewSubscriberEligibility(AccountInfo accountInfo, int subscriberCount, double thresholdAmount, String sessionId) throws ApplicationException, RemoteException;

	HCDclpActivationOptionDetailsInfo getCLPActivationOptionsDetail(int ban) throws ApplicationException, RemoteException;

	String processPayment(double paymentAmount, AccountInfo accountInfo, CreditCardTransactionInfo creditCardTransactionInfo, String paymentSourceType, String paymentSourceID,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	List getCreditEvaluationBusinessList(AccountInfo accountInfo, String sessionId) throws ApplicationException, RemoteException;

	List getDuplicateAccountList(AccountInfo accountInfo) throws ApplicationException, RemoteException;
	
	void updateCreditWorthiness(int ban, String creditClass, double creditLimit, String limitChangeText, boolean isCreditProfileChange, CreditCheckResultDepositInfo[] creditCheckResultDeposits,
			String depositChangeReasonCode, String creditResultChangeText, boolean isCreditResultChange, boolean isAsync, AuditInfo auditInfo, AuditHeader auditHeader, String sessionId)
					throws ApplicationException, RemoteException;
	
	void updateCreditProfile(int ban, String creditClass, double creditLimit, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void updateCreditCheckResult(int ban, String creditClass, CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String memoText,
			String sessionId) throws ApplicationException, RemoteException;
	
	boolean validateCommunicationSuiteEligibility(int brandId, char accountType, char accountSubType) throws ApplicationException,RemoteException ;


}