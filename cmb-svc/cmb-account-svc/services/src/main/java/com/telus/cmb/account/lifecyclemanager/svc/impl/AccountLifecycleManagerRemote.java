package com.telus.cmb.account.lifecyclemanager.svc.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.PaymentTransfer;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.CancellationPenaltyInfo;
import com.telus.eas.account.info.CollectionStateInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.ContactPropertyInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.account.info.FleetInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.TalkGroupInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.TelusValidationException;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
//import com.telus.eas.framework.info.LMSRequestInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.ServiceInfo;

public interface AccountLifecycleManagerRemote extends EJBObject {

	void addFleet(int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException, RemoteException;

	TalkGroupInfo createTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException, RemoteException;

	void addTalkGroups(int ban, TalkGroupInfo[] talkGroupInfo, String sessionId) throws ApplicationException, RemoteException;

	void addTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException, RemoteException;

	FleetInfo createFleet(int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException, RemoteException;

	void dissociateFleet(int ban, FleetInfo fleetInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException, RemoteException;

	void removeTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException, RemoteException;

	String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;

	void updateEmailAddress(int ban, String emailAddress, String sessionId) throws ApplicationException, RemoteException;

	void updateHotlineInd(int ban, boolean hotLineInd, String sessionId) throws ApplicationException, RemoteException;

	void updateInvoiceProperties(int ban, InvoicePropertiesInfo invoicePropertiesInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateBillCycle(int ban, short billCycle, String sessionId) throws ApplicationException, RemoteException;

	int createAccount(AccountInfo pAccountInfo, String sessionId) throws ApplicationException, RemoteException;

	void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, 
			String pCreditParamType,String sessionId) throws ApplicationException,RemoteException;

	void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, 
			String pCreditParamType, ConsumerNameInfo pConsumerNameInfo, AddressInfo pAddressInfo,
			String sessionId) throws ApplicationException,RemoteException;

	void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness,
			CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException, RemoteException;

	void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness,
			CreditCheckResultInfo pCreditCheckResultInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateCreditCheckResult(int pBan, String pCreditClass, CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo, String pDepositChangedReasonCode, String pDepositChangeText,
			String sessionId) throws ApplicationException, RemoteException;

	void updateAccountPassword(int pBan, String pAccountPassword, String sessionId) throws ApplicationException, RemoteException;

	void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void createMemo(MemoInfo pMemoInfo, String sessionId) throws ApplicationException, RemoteException;

	void changePaymentMethodToRegular(int pBan, String sessionId) throws ApplicationException, RemoteException;

	PaymentMethodInfo updatePaymentMethod(int pBan, PaymentMethodInfo pPaymentMethodInfo, String sessionId) throws ApplicationException, RemoteException;

	CancellationPenaltyInfo retrieveCancellationPenalty(int pBan, String sessionId) throws ApplicationException, RemoteException;

	CancellationPenaltyInfo[] retrieveCancellationPenaltyList(int banId, String[] subscriberId, String sessionId) throws ApplicationException, RemoteException;

	void createFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateBillSuppression(int ban, boolean suppressBill, Date eEffectiveDate, Date expiryDate, String sessionId) throws ApplicationException, RemoteException;

	void updateInvoiceSuppressionIndicator(int ban, String sessionId) throws ApplicationException, RemoteException;

	void updateReturnEnvelopeIndicator(int ban, boolean returnEnvelopeRequested, String sessionId) throws ApplicationException, RemoteException;

	void updateNationalGrowth(int ban, String nationalGrowthIndicator, String homeProvince, String sessionId) throws ApplicationException, RemoteException;

	void restoreSuspendedAccount(int ban, String restoreReasonCode, String sessionId) throws ApplicationException, RemoteException;

	void restoreSuspendedAccount(int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, String sessionId)
			throws ApplicationException, RemoteException;

	void updateFutureStatusChangeRequest(int ban, FutureStatusChangeRequestInfo futureStatusChangeRequestInfo, String sessionId) throws ApplicationException, RemoteException;

	CollectionStateInfo retrieveBanCollectionInfo(int ban, String sessionId) throws ApplicationException, RemoteException;

	void updateNextStepCollection(int ban, int stepNumber, Date stepDate, String pathCode, String sessionId) throws ApplicationException, RemoteException;
	
	void cancelSubscribers(AccountInfo accountInfo, String[] subscriberIdArray,String[] waiveReasonArray, Date activityDate,
			String activityReasonCode, char depositReturnMethod,String userMemoText, Date logicalDate,
			boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd, String sessionId) throws ApplicationException,RemoteException;

	void changeKnowbilityPassword(String userId, String oldPassword, String newPassword, String sessionId) throws ApplicationException, RemoteException;

	void suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Creates ManualLetterRequest
	 * 
	 * @param LMSLetterRequestInfo 	letterRequestInfo
	 * @param String	sessionId
	 * @throws ApplicationException
	 */
//	void createManualLetterRequest(LMSLetterRequestInfo letterRequestInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Removes ManualLetterRequest
	 * 
	 * @param Integer 	banId
	 * @param Integer	requestNumber
	 * @param String	 sessionId
	 * @throws ApplicationException
	 */
	//void removeManualLetterRequest(int ban, int requestNumber, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param int banId
	 * @param String sessionID
	 * @return
	 * @throws ApplicationException
	 * @throws RemoteException
	 */
	List retrieveFeeWaivers(int banId, String sessionID) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 * @throws RemoteException
	 */
	List retrieveFutureStatusChangeRequests(int ban, String sessionId) throws ApplicationException, RemoteException;

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
	 * @exception ApplicationException,RemoteException
	 */
	void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason, String userMemoText, boolean isPortActivity, String sessionId)
			throws ApplicationException, RemoteException;

	void updateAuthorizationNames(int ban, ConsumerNameInfo[] authorizationNames, String sessionId) throws ApplicationException, RemoteException;

	void updateAutoTreatment(int ban, boolean holdAutoTreatment, String sessionId) throws ApplicationException, RemoteException;

	void updateBrand(int ban, int brandId, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void updateSpecialInstructions(int ban, String specialInstructions, String sessionId) throws ApplicationException, RemoteException;

	void applyFeeWaiver(FeeWaiverInfo feeWaiver, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @param newCreditClass
	 * @param memoText
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updateCreditClass(int ban, String newCreditClass, String memoText, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param ban
	 * @param seqNo
	 * @param paymentTransfers
	 * @param allowOverPayment
	 * @param memonText
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updateTransferPayment(int ban, int seqNo, PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText, String sessionId) throws ApplicationException, RemoteException;

	AddressValidationResultInfo validateAddress(AddressInfo addressInfo, String sessionId) throws ApplicationException, RemoteException;

	CreditCheckResultInfo retrieveAmdocsCreditCheckResultByBan(int ban, String sessionId) throws ApplicationException, RemoteException;

	Collection retrieveAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId, String sessionId) throws ApplicationException, RemoteException;

	void changePostpaidConsumerToPrepaidConsumer(int ban, String sessionId) throws ApplicationException, RemoteException;

	List retrieveDiscounts(int ban, String sessionId) throws ApplicationException, RemoteException;

	void cancelAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, boolean isBrandPort, String sessionId) throws ApplicationException, RemoteException;

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
	 * @exception ApplicationException,RemoteException
	 */
	void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Cancels a list of subscribers
	 *
	 * @param  int       ban
	 * @param  Date 	 activityDate
	 * @param  String    activityReasonCode
	 * @param  String[]  subscriberId 
	 * @param  String    userMemoText
	 * @throws ApplicationException
	 *
	 */

	void suspendSubscribers(int pBban, Date activityDate, String activityReasonCode, String[] subscriberId, String userMemoText, String sessionId) throws ApplicationException, RemoteException;

	void reverseCredit(CreditInfo creditInfo, String reversalReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void reverseCreditWithOverride(CreditInfo creditInfo, String reversalReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void deleteCharge(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	void deleteChargeWithOverride(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	double applyChargeToAccount(ChargeInfo chargeInfo, String sessionId) throws ApplicationException, RemoteException;

	double applyChargeToAccountWithOverride(ChargeInfo chargeInfo, String sessionId) throws ApplicationException, RemoteException;

	double applyChargeToAccount(ChargeInfo chargeInfo, boolean isWebserviceCall, String sessionId) throws ApplicationException, RemoteException;

	double applyChargeToAccountWithOverride(ChargeInfo chargeInfo, boolean isWebserviceCall, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param pChargeInfo
	 * @param pAdjustmentAmount
	 * @param pAdjustmentReasonCode
	 * @param pMemoText
	 * @return
	 * @throws ApplicationException
	 */
	double adjustCharge(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param pChargeInfo
	 * @param pAdjustmentAmount
	 * @param pAdjustmentReasonCode
	 * @param pMemoText
	 * @return
	 * @throws ApplicationException
	 */
	double adjustChargeWithOverride(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * restored a cancelled list of subscribers
	 *
	 * @param int		ban 
	 * @param Date  	restoreDate 
	 * @param String 	restoreReasonCode 
	 * @param String[] 	subscriberId 
	 * @param String  	restoreComment 
	 * 
	 * @throws ApplicationException
	 */
	void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode, String[] subscriberId, String restoreComment, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Creates ManualLetterRequest
	 * 
	 * @param LMSRequestInfo 	letterRequestInfo
	 * @param String	sessionId
	 * @throws ApplicationException
	 */
//	void createManualLetterRequest(LMSRequestInfo lmsRequestInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param integer		ban
	 * @param intger		paymentSeq
	 * @param String		reasonCode
	 * @param String		memoText
	 * @param boolean		isManual
	 * @param String		authorizationCode
	 * @param String		sessionId
	 * @throws ApplicationException
	 */
	void refundPaymentToAccount(int ban, int paymentSeq, String reasonCode, String memoText, boolean isManual, String authorizationCode, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param discountInfo
	 * @throws ApplicationException
	 */
	void applyDiscountToAccount(DiscountInfo discountInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Perform post account-creation tasks that only apply to Prepaid accounts. This method must be
	 * called once per prepaid account created. It assigns the prepaid activation code to the ban and
	 * also charges the 'purchase-now' fee to the customers credit card.
	 *
	 * The billing account must be in 'Tentative' status.
	 *
	 * @param   int                           billing account number
	 * @param   PrepaidConsumerAccountInfo    all attributes related to a prepaid account
	 * @return  String                        reference number from credit card transaction
	 *
	 * @see PrepaidConsumerAccountInfo
	 *
	 * @exception   TelusValidationException  VAL10003  BAN must be in tentative status"
	 *
	 * @ejbgen:remote-method
	 */
	String performPostAccountCreationPrepaidTasks(int ban, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader, String sessionId)
			throws ApplicationException, RemoteException;

	/**
	 * Suspend Account for Port Out
	 * @param int banNumber  - Billing Account Number
	 * @param String deactivationReason - deactivation reason code 
	 * @param Date activityDate - activity date 
	 * @param String portOutInd - port out indicator 
	 * @excpetion ApplicationException
	 */
	void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param CreditInfo		creditInfo
	 * @param String			sessionID
	 * @return true  to indicate the adjustment(s) is created in KB
	 * @throws ApplicationException
	 */
	boolean applyCreditToAccount(CreditInfo creditInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * @param CreditInfo		creditInfo
	 * @param String			sessionID
	 * @return true  to indicate the adjustment(s) is created in KB
	 * @throws ApplicationException
	 */
	boolean applyCreditToAccountWithOverride(CreditInfo creditInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit).
	 *
	 * @param pBan         billing account ID
	 * @param pPhoneNumber      phone number
	 * @param pUserId      userId
	 * @param pAmount      adjustment amount
	 * @param pReasonCode      adjustment reason code
	 * @param pTransactionId      transaction id (required for some adjustments)
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId) throws ApplicationException, RemoteException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit).
	 * Tax will also be debited or credited.
	 *
	 * @param pBan         	billing account ID
	 * @param pPhoneNumber      	phone number
	 * @param pUserId      	userId
	 * @param pAmount      	adjustment amount
	 * @param pReasonCode      	adjustment reason code
	 * @param pTransactionId      	transaction id (required for some adjustments)
	 * @param pTax	tax information for adjustment
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax)
			throws ApplicationException, RemoteException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit)
	 * and the supplied tax option parameter.  Tax will also be debited or credited.
	 *
	 * @param pBan         	billing account ID
	 * @param pPhoneNumber      	phone number
	 * @param pUserId      	userId
	 * @param pAmount      	adjustment amount
	 * @param pReasonCode      	adjustment reason code
	 * @param pTransactionId      	transaction id (required for some adjustments)
	 * @param pTax	tax information for adjustment
	 * @param pTaxOption			tax option for adjustment - one the Credit interface TAX_OPTION_XXXX constants
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax, char pTaxOption)
			throws ApplicationException, RemoteException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber. Since the input to this method is a charge, the
	 * balance of the subscriber will be decreased.
	 *
	 * @param pUserId      userId
	 * @param pChargeInfo  charge information
	 *
	 * @throws ApplicationException
	 *
	 * @see ChargeInfo
	 */
	void adjustBalanceForPayAndTalkSubscriber(String pUserId, ChargeInfo pChargeInfo) throws ApplicationException, RemoteException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber. Since the input to this method is a credit, the
	 * balance of the subscriber will be increased.
	 *
	 * Note:  Tax will also be credited based on the taxOption constant stored in the CreditInfo
	 * 		object (added 2005/03/16).
	 *
	 * @param pUserId      userId
	 * @param pCreditInfo  credit information
	 *
	 * @throws ApplicationException
	 *
	 * @see CreditInfo
	 */
	void adjustBalanceForPayAndTalkSubscriber(String pUserId, CreditInfo pCreditInfo) throws ApplicationException, RemoteException;

	void applyCreditForFeatureCard(CardInfo cardInfo, ServiceInfo[] cardServices, String userId) throws ApplicationException, RemoteException;

	/**
	 * Apply Top-Up with Airtime Card for a Pay&Talk subscriber.
	 *
	 * @param pBan      BAN (Billing Account Number)
	 * @param pPhoneNumber   Phonenumber
	 * @param pAirtimeCard airtime card
	 * @param pUserId   userId
	 * @param pApplicationId   application ID
	 *
	 * @return Transaction ID (confirmation number) for top up
	 *
	 * @throws ApplicationException
	 *
	 */
	String applyTopUpForPayAndTalkSubscriber(int pBan, String pPhoneNumber, CardInfo pAirtimeCard, String pUserId, String pApplicationId) throws ApplicationException, RemoteException;

	/**
	 * Apply Top-Up for a Pay&Talk subscriber. The amount will be charged to the registered
	 * top-up credit card.
	 *
	 * @param pBan      BAN (Billing Account Number)
	 * @param pPhoneNumber   Phonenumber
	 * @param pAmount   amount
	 * @param pOrderNumber   card Type ("C" for credit card, or "D" for debit card
	 * @param pUserId   userId
	 * @param pApplicationId   application ID
	 *
	 * @return String BIH reference number
	 *
	 * @throws ApplicationException
	 */
	String applyTopUpForPayAndTalkSubscriber(int pBan, String pPhoneNumber, double pAmount, String pOrderNumber, String pUserId, String pApplicationId) throws ApplicationException, RemoteException;

	/**
	 * Validates information for a new prepaid account (against Pay&Talk database).
	 *
	 * @param applicationId                	applicationId
	 * @param pUserId                      	userId
	 * @param activationTopUpInfo          	activationTopUpInfo
	 * @param phoneNumber                  	phoneNumber
	 * @param esn                        	esn
	 * @param provinceCode    				province code
	 * @param pPrepaidConsumerAccountInfo	all attributes related to a prepaid account
	 *
	 *
	 * @exception ApplicationException
	 *
	 * @see PrepaidConsumerAccountInfo
	 *
	 */
	void creditSubscriberMigration(String applicationId, String pUserId, ActivationTopUpInfo activationTopUpInfo, String phoneNumber, String esn, String provinceCode,
			PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException, RemoteException;

	/**
	 * removeTopupCreditCard
	 * @param MDN
	 * 
	 * @throws ApplicationException
	 */
	void removeTopupCreditCard(String MDN) throws ApplicationException, RemoteException;

	/**
	 * save activation topUp arrangement
	 * @param banId
	 * @param MDN
	 * @param serialNo
	 * @param topUpPaymentArrangement
	 * @param user
	 * 
	 * @throws ApplicationException
	 */
	void saveActivationTopUpArrangement(String banId, String MDN, String serialNo, ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement, String user)
			throws ApplicationException, RemoteException;

	/**
	 * @param int	    billing account number (BAN)
	 * @param long		portalUserID
	 * @param Array		BillNotificationContact
	 * @param String	applicationCode
	 * @deprecated
	 */
	void saveBillNotificationDetails(int ban, long portalUserID, BillNotificationContactInfo[] pBillNotificationContact, String applicationCode) throws ApplicationException, RemoteException;

	void updateAccountPIN(int banId, String MDN, String serialNo, String prevPIN, String PIN, String user) throws ApplicationException, RemoteException;

	void updateAutoTopUp(AutoTopUpInfo pAutoTopUpInfo, String pUserId, boolean existingAutoTopUp, boolean existingThresholdRecharge) throws ApplicationException, RemoteException;

	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param serialNo
	 * @param creditCardInfo
	 * @param user
	 * @param encrypted
	 * 
	 * @deprecated Deprecated as part of Cambio. Use  updateTopUpCreditCard(String, String, CreditCardInfo, String, Boolean).
	 * 
	 */
	void updateTopupCreditCard(String banId, String MDN, String serialNo, CreditCardInfo creditCard, String user, boolean encrypted) throws RemoteException;

	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param creditCardInfo
	 * @param user
	 * @param encrypted
	 * @param isFirstTimeRegistration
	 */
	void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user, boolean encrypted, boolean isFirstTimeRegistration) throws RemoteException;

	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param creditCardInfo
	 * @param user
	 * @param encrypted
	 */
	void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user, boolean encrypted) throws RemoteException;

	void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException, RemoteException;

	void updateContactInformation(int billingAccountNumber, ContactPropertyInfo contactPropertyInfo, String sessionId) throws ApplicationException, RemoteException;

	/**
     * Retrieves the last credit check info by billing account number (BAN), merging CDA and KB data
     * 
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType, String sessionId) throws ApplicationException, RemoteException;

	/**
     * Retrieves only the KB credit check info by billing account number (BAN)
     * 
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType, String sessionId) throws ApplicationException, RemoteException;

	/**
	   * Return User's Profile ID
	   * @returns String       User's profile id
	   * @exception ApplicationException
	   */
	public String getUserProfileID(String sessionId) throws ApplicationException, RemoteException;

	public void updatePersonalCreditInformation(int ban, PersonalCreditInfo personalCreditInfo, String sessionId) throws ApplicationException, RemoteException;

	public void updateBusinessCreditInformation(int ban, BusinessCreditInfo businessCreditInfo, String sessionId) throws ApplicationException, RemoteException;

	public void createCreditBalanceTransferRequest(int sourceBan, int targetBan, String sessionId) throws ApplicationException, RemoteException;

	public void cancelCreditBalanceTransferRequest(int creditTransferSequenceNumber, String sessionId) throws ApplicationException, RemoteException;

	public List getCreditBalanceTransferRequestList(int ban, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	public List applyChargesAndAdjustmentsToAccount(List chargeInfoList, String sessionId) throws ApplicationException, RemoteException;

	boolean applyCreditToAccountAndSubscriber(CreditInfo creditInfo, boolean overrideThresholdInd, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * Applies charges and Adjustment to account based on BAN and Subscriber
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List applyChargesAndAdjustmentsToAccountForSubscriber(List chargeInfoList, String sessionId) throws ApplicationException, RemoteException;

	List applyChargesAndAdjustmentsToAccountForSubscriberWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, String sessionId)
			throws ApplicationException, RemoteException;

	List applyChargesAndAdjustmentsToAccountWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, String sessionId) throws ApplicationException, RemoteException;

	List adjustChargeToAccountForSubscriberReturn(int ban, String subscriberNumber, String phoneNumber, List chargeInfoList, Date searchFromDate, Date searchToDate, String adjustmentMemoText,
			String productType, boolean bypassAuthorizationInd, boolean overrideThresholdInd, String sessionId) throws ApplicationException, RemoteException;

	List getCustomerNotificationPreferenceList(int ban, String sessionId) throws ApplicationException, RemoteException;

	void updateCustomerNotificationPreferenceList(int ban, List notificationPreferenceList, String sessionId) throws ApplicationException, RemoteException;

	void testAmdocsConnectivity(String sessionId) throws ApplicationException, RemoteException;
}
