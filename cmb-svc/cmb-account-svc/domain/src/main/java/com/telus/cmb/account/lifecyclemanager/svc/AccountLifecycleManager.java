package com.telus.cmb.account.lifecyclemanager.svc;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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

public interface AccountLifecycleManager {

	/**
	 * Obtain sessionId for the given user credentials
	 * 
	 * @param userId The user id.
	 * @param password The password.
	 * @param applicationId The application id.
	 */
	String openSession(String userId, String password, String applicationId) throws ApplicationException;

	/**
	 * Update the email address for a ban.
	 * @param ban The ban we want to update the email address.
	 * @param emailAddress A valid email address.
	 */
	void updateEmailAddress(int ban, String emailAddress, String sessionId) throws ApplicationException;

	/**
	 * Update the hotLineInd address for a ban.
	 * @param ban The ban we want to update the email address.
	 * @param hotLineInd  A new hotline indicator.
	 */
	void updateHotlineInd(int ban, boolean hotLineInd, String sessionId) throws ApplicationException;

	/**
	 * Updates the invoice properties for a ban.
	 * @param ban The ban we want to update the invoice properties.
	 * @param invoicePropertiesInfo The invoice properties.
	 */
	void updateInvoiceProperties(int ban, InvoicePropertiesInfo invoicePropertiesInfo, String sessionId) throws ApplicationException;

	/**
	 * Update Bill cycle for a account
	 *
	 * @param   int    BAN - billing account number
	 * @param   short  bill cycle	 
	 */
	void updateBillCycle(int ban, short billCycle, String sessionId) throws ApplicationException;

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
	 * Save Credit Check Information
	 * @param  pAccountInfo                  billing account information
	 * @param  pCreditCheckResultInfo        credit result returned from credit bureau
	 * @param	 pCreditParamType						   credit check parameter type - "M" (manual) or "I" (individual)
	 * @exception TelusException
	 * @see  CreditCheckResultInfo
	 */
	void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, 
			String pCreditParamType,String sessionId) throws ApplicationException;

	/**
	 * Save Credit Check Information
	 * @param  pAccountInfo                  billing account information
	 * @param  pCreditCheckResultInfo        credit result returned from credit bureau
	 * @param	 pCreditParamType			   credit check parameter type - "M" (manual) or "I" (individual)
	 * @param  pConsumerNameInfo             consumer name information
	 * @param  pAddressInfo                  consumer address information
	 * @exception TelusException
	 * @see  CreditCheckResultInfo
	 */
	void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, 
			String pCreditParamType, ConsumerNameInfo pConsumerNameInfo, AddressInfo pAddressInfo,
			String sessionId) throws ApplicationException;


	/**
	 * Save Credit Check Information for Business
	 * @param pAccountInfo                     billing account information
	 * @param listOfBusinesses                 list of business identities
	 * @param selectedBusiness                 selected business identity
	 * @param pCreditCheckResultInfo           credit result returned from credit bureau
	 * @param pCreditParamType                 credit check parameter type - "M" (manual) or "B" (business)
	 * @throws TelusException
	 * @see  CreditCheckResultInfo
	 */
	void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, 
			BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness, 
			CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, 
			String sessionId) throws ApplicationException;


	/**
	 * Save Credit Check Information for Business
	 * @param  AccountInfo                 		billing account information
	 * @param  BusinessCreditIdentityInfo[]       list of business identities
	 * @param  BusinessCreditIdentityInfo         selected business identity
	 * @param  CreditCheckResultInfo        		credit result returned from credit bureau
	 * @param	 String								credit check parameter type - "M" (manual) or "B" (business)
	 * @exception TelusException
	 * @see  CreditCheckResultInfo
	 */
	void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses, 
			BusinessCreditIdentityInfo selectedBusiness, CreditCheckResultInfo pCreditCheckResultInfo, 
			String sessionId) throws ApplicationException;


	/**
	 * Update Credit Result
	 * - the credit class and deposit amounts will be updated
	 * - new rows in credit_history and crd_deposit are being created
	 * - System memo of type 'DPCH' is being created
	 * @param int                           billing account number
	 * @param String                        credit class
	 * @param CreditCheckResultDepositInfo  billing account number
	 * @param String                        deposit change reason code
	 * @param String                        deposit change text
	 * @exception TelusException
	 * @see CreditCheckResultDepositInfo
	 */
	void updateCreditCheckResult(int pBan, String pCreditClass,
			CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo,
			String pDepositChangedReasonCode, String pDepositChangeText, 
			String sessionId) throws ApplicationException; 	


	/**
	 * Update account password
	 * @param   int         billing account number
	 * @param   String      account password
	 * @excpetion TelusException
	 */
	void updateAccountPassword(int pBan, String pAccountPassword, 
			String sessionId) throws ApplicationException; 


	/**
	 * Apply Payment or Deposit to billing account. It does not charge the credit card.
	 * @param   PaymentInfo      payment information
	 * @TelusException
	 * @see PaymentInfo
	 */
	void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException; 


	/**
	 * Updates Credit Class/Limit
	 * @param int ban
	 * @param String oldCreditClass
	 * @param String newCreditClass
	 * @param int oldCreditLimit
	 * @param int newCreditLimit
	 * @param String memoText
	 * @throws TelusException
	 * @see CreditCheckResultInfo
	 */
	void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText, 
			String sessionId) throws ApplicationException;


	/**
	 * Create a new memo
	 * @param   MemoInfo    all attributes for the memo (i.e. ban #, memo type, memo text etc)
	 * @exception TelusException
	 * @see MemoInfo
	 */
	void createMemo(MemoInfo pMemoInfo,String sessionId) throws ApplicationException;	


	/**
	 * Change Payment Method To Regular.
	 * @param   int     BAN - billing account number
	 * @exception   TelusValidationException  VAL10009  BAN cannot be in tentative status.
	 */
	void changePaymentMethodToRegular(int pBan,String sessionId) throws ApplicationException;

	/**
	 * Update Payment Method for a billing account
	 * @parm    int               billing account number of the newly created account
	 * @param   PaymentMethodInfo payment method attributes
	 * @see PaymentMethodInfo
	 * @excpetion ApplicationException;
	 * @return PaymentMethodInfo
	 */
	PaymentMethodInfo updatePaymentMethod(int pBan, PaymentMethodInfo pPaymentMethodInfo, String sessionId) throws ApplicationException;

	/**
	 * Retrieve cancellation charges (penalty) and deposit information for a given account
	 * @param   int                       billing account number (BAN)
	 * @returns CancellationPenaltyInfo   cancellation penalty charges
	 * @exception TelusException
	 */
	CancellationPenaltyInfo retrieveCancellationPenalty(int pBan,
			String sessionId) throws ApplicationException;

	/**
	 * Retrieves Cancellation Penalty List
	 * @param banId int
	 * @param subscriberId String[]
	 * @throws TelusException
	 * @throws RemoteException
	 * @return CancellationPenaltyInfo[]
	 */
	CancellationPenaltyInfo[] retrieveCancellationPenaltyList(int banId, String[] subscriberId,
			String sessionId) throws ApplicationException;

	/**
	 * Add a talkgroup to an account
	 *
	 * <P>This method associates an existing TalkGroup to the account.
	 *
	 * In order for a talkGroup to be associated with an account, the account has to be associated
	 * with the fleet that the talkgroup belongs to.
	 *
	 * <P>When associating an existing talkgroup, the following attributes are mandatory:
	 *      urbanId/fleetId   primary key to which to attach the talkgroup
	 *      talkGroupId       primary key of talkgroup to be attached
	 *
	 * <P> Each time a talkgroup is added the 'expected # of talkgroups' is increased by 1.
	 *
	 * @param   int           BAN - billing account number
	 * @param   TalkGroupInfo talkgroup information existing
	 *
	 * @see TalkGroupInfo
	 */
	void removeTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;	

	/**
	 * Dissociate a fleet from an account
	 *
	 * <P>This method dissociates an existing fleet from the account.
	 *
	 * @param   int           BAN - billing account number
	 * @param   FleetInfo     fleet information (existing)
	 *
	 * @see FleetInfo
	 */
	void dissociateFleet(int ban, FleetInfo fleetInfo, String sessionId) throws ApplicationException;

	/**
	 * Add a fleet to an account
	 *
	 * <P>This method associates an existing shared fleet to the account.
	 *
	 * <P>The following attributes are mandatory:
	 *      urbanId/fleetId   primary key to fleet to be attached
	 *      #subscribers      expected number of subscribers
	 *
	 * @param   int           BAN - billing account number
	 * @param   short         network/ndap id
	 * @param   FleetInfo     fleet information (existing)
	 * @param   int           number of subscribers that are expected to be added
	 *
	 * @see FleetInfo
	 */
	void addFleet(int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException; 

	/**
	 * Create new fleet (and associate it the account)
	 *
	 * <P>This method creates a new fleet and associates it to the account.
	 *
	 * <P>The following attributes are mandatory:
	 *      network           network/ndap id
	 *      type              fleet type ('B' public, 'P' Private or 'S' shared)
	 *      name              fleet name/alias
	 *      #subscribers      expected number of subscribers
	 *
	 * <P> When creating a new fleet, the 'expected # of subscribers' and 'expected # of talkgroups'
	 * are set as following:
	 *      expected # of subs    as input plus 2 for future growth
	 *      expected # of tg's    0 (number will increased as talk groups are being added)
	 *
	 * @param   int           BAN - billing account number
	 * @param   short         network/ndap id
	 * @param   FleetInfo     fleet information
	 * @param   int           number of subscribers that are expected to be added
	 *
	 * @returns FleetInfo     fleet information
	 *
	 * @see FleetInfo
	 *
	 * @exception   ApplicationException 10012  Failed to get primary NGP for network. 
	 */
	FleetInfo createFleet(int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException;

	/**
	 * Add multiple talkgroups to an account
	 *
	 * <P>This method has the same functionality as addTalkGroup() but allows for the addition
	 * of multiple talkgroups at one time.
	 *
	 * <P>See addTalkGroup() for details.
	 *
	 * @param   int           BAN - billing account number
	 * @param   TalkGroupInfo talkgroup information (new or existing)
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception   ApplicationException  10010  Fleet not associated to BAN - cannot add talkgroup!
	 */
	void addTalkGroups(int ban, TalkGroupInfo[] talkGroupInfo, String sessionId) throws ApplicationException;

	/**
	 * Add a talkgroup to an account
	 *
	 * <P>This method associates an existing TalkGroup to the account.
	 *
	 * In order for a talkGroup to be associated with an account, the account has to be associated
	 * with the fleet that the talkgroup belongs to.
	 *
	 * <P>When associating an existing talkgroup, the following attributes are mandatory:
	 *      urbanId/fleetId   primary key to which to attach the talkgroup
	 *      talkGroupId       primary key of talkgroup to be attached
	 *
	 * <P> Each time a talkgroup is added the 'expected # of talkgroups' is increased by 1.
	 *
	 * @param   int           BAN - billing account number
	 * @param   TalkGroupInfo talkgroup information existing
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception   ApplicationException  10010  Fleet not associated to BAN - cannot add talkgroup!
	 */
	void addTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;

	/**
	 * Create a new talkgroup (and associate it to the account)
	 *
	 * <P>This method creates a new talkgroup and associates it to the account.
	 *
	 * In order for a talkGroup to be associated with an account, the account has to be associated
	 * with the fleet that the talkgroup belongs to.
	 *
	 * <P>When creating a new talkgroup, the following attributes are mandatory:
	 *      urbanId/fleetId   primary key to which to attach the talkgroup
	 *      name              talkgroup name/alias
	 *
	 * <P> Each time a talkgroup is added the 'expected # of talkgroups' is increased by 1.
	 *
	 * @param   int           BAN - billing account number
	 * @param   TalkGroupInfo talkgroup information new
	 *
	 * @returns TalkGroupInfo talkgroup information
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception   ApplicationException  10010  Fleet not associated to BAN - cannot add talkgroup!
	 */
	TalkGroupInfo createTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException; 

	/**
	 * Update a talkgroup
	 *
	 * <P>
	 * This method updates some attributes of a talkgroup.
	 *
	 * The following attributes can be updated:
	 * - alias
	 * - priority
	 *
	 * @param int
	 *            BAN - billing account number
	 * @param TalkGroupInfo
	 *            talkgroup information new
	 *
	 * @see TalkGroupInfo
	 *
	 * @exception ApplicationException
	 *                10010 Fleet not associated to BAN - cannot add
	 *                talkgroup!
	 */
	void updateTalkGroup(int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException;

	/**
	 * Create a new followUp
	 *
	 * @param   followUpInfo    all attributes for the follow-up (i.e. ban #, follow-up type, follow-up text etc)
	 *
	 * @exception ApplicationException
	 *
	 * @see FollowUpInfo
	 */
	void createFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException;

	/**
	 *
	 * @param followUpUpdateInfo
	 * @throws ApplicationException
	 */
	void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException; 

	/**
	 * Update Bill Suppression changes the bill on-hold status of an account.
	 *
	 * <P>To suppress a paper bill, pass in 'true' in the 2nd parameter and specify
	 * the effective and expiry dates. The default dates as indicated below are used if a null
	 * values is passed in either of the dates.
	 *
	 * <P>A bill can only be suppressed for an account that is not tentative.
	 *
	 * <P>The on-hold for an account is removed by passing the BAN and 'false' in the 2nd
	 * parameter. No other parameters are required.
	 *
	 * @param   int     BAN - billing account number
	 * @param   boolean true - add/update paper bill suppression
	 *                  false - remove paper bill suppression
	 * @param   date    effective Date  (default: today's date)
	 * @param   date    expiration Date (default: December 31, 2099)
	 *
	 * @exception   ApplicationException  VAL10008  Effective Date must be before Expiry Date.
	 * @exception   ApplicationException  VAL10009  BAN cannot be in tentative status.
	 *
	 */
	void updateBillSuppression(int ban, boolean suppressBill, Date effectiveDate, Date expiryDate 
			, String sessionId) throws ApplicationException;

	/**
	 * Update Invoice Suppression Indicator.
	 * <P>A bill can only be suppressed for an account that is not tentative.
	 *
	 * @param   int     BAN - billing account number
	 * @exception   ApplicationException  VAL10009  BAN cannot be in tentative status.

	 */
	void updateInvoiceSuppressionIndicator(int ban, String sessionId) throws ApplicationException;

	/**
	 * Update Return Envelope Indicator changes whether the customer receives a return envelope
	 * with his invoice or not.
	 *
	 * <P>The customer might request not to be sent a return envelope with each invoice. This method
	 * allows the changing of the relevent account-level indicator.
	 *
	 * @param   int     BAN - billing account number
	 * @param   boolean true - return envelope will be sent
	 *                  false - no return envelope will be sent

	 */
	void updateReturnEnvelopeIndicator(int ban, boolean returnEnvelopeRequested, String sessionId) throws ApplicationException;

	/**
	 * <P>This method retrieves a BAN Collection information associated with the BAN passed in.
	 * @param   int  BAN - billing account number
	 * @returns CollectionState
	 * */

	CollectionStateInfo retrieveBanCollectionInfo(int ban, String sessionId) throws ApplicationException; 

	void updateNextStepCollection(int ban, int stepNumber, Date stepDate, String pathCode, String sessionId) throws ApplicationException; 
	/**
	 * Update National Growth information
	 *
	 * @param   int    BAN - billing account number
	 * @param   String national account indicator (won't be updated if passed in as null)
	 * @param   String home province (won't be updated if passed in as null)
	 *
	 * @exception TelusException
	 *
	 * @ejbgen:remote-method
	 */
	void updateNationalGrowth(int ban, String nationalGrowthIndicator, String homeProvince, String sessionId ) throws ApplicationException;

	/**
	 * Restore Suspended Account/Subscribers
	 *
	 * This method restores a suspended account to 'open' status and also restores all suspended
	 * subscribers that were suspended for collection reasons. The restore is effective as of the
	 * current date.
	 *
	 * For more options when restoring a suspended account, see alternate method:
	 * - void restoreSuspendedAccount(int pBan, Date pRestoreDate, String pRestoreReasonCode, String pRestoreComment, boolean collectionSuspensionsOnly)
	 *
	 * @param   int         billing account number  (mandatory)
	 * @param   String      reason code for restoring suspended account (mandatory)
	 *
	 * @excpetion TelusException
	 *
	 * @ejbgen:remote-method
	 */
	void restoreSuspendedAccount(int ban, String restoreReasonCode, String sessionId) throws ApplicationException;

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
	 *
	 * @excpetion TelusException
	 *
	 * @ejbgen:remote-method
	 */
	void restoreSuspendedAccount(int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, String sessionId) throws ApplicationException;

	/**
	 * Update Future Status Change Request
	 *
	 * @param		int	BAN - billing account number
	 * @param		FutureStatusChangeRequestInfo - future status change request value object
	 * @see 		FutureStatusChangeRequestInfo
	 * @exception	TelusException
	 *
	 * @ejbgen:remote-method
	 */
	void updateFutureStatusChangeRequest(int ban, FutureStatusChangeRequestInfo futureStatusChangeRequestInfo, String sessionId) throws ApplicationException;

	/**
	 * Changes Knowbility password.
	 * @param oldPassword
	 * @param newPassword
	 * @param userId
	 */
	void changeKnowbilityPassword(String userId, String oldPassword, String newPassword, String sessionId) throws ApplicationException; 


	/**
	 * Suspend an account
	 *
	 * The account has to be in 'Open' status.
	 *
	 * @param   int         billing account number  (mandatory)
	 * @param   Date        date on which to suspend the account (optional - defaul: today)
	 * @param   String      reason code for suspending the account (mandatory)
	 * @param   String      optional comment for suspending the account (optional)
	 *
	 * @exception ApplicationException
	 */
	void  suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText,String sessionId) throws ApplicationException;


	/**
	 * Creates ManualLetterRequest
	 * 
	 * @param LMSLetterRequestInfo 	letterRequestInfo
	 * @param String	sessionId
	 * @throws ApplicationException
	 */
	//void createManualLetterRequest(LMSLetterRequestInfo letterRequestInfo, String sessionId)throws ApplicationException;
	/**
	 * Removes ManualLetterRequest
	 * 
	 * @param Integer 	banId
	 * @param Integer	requestNumber
	 * @param String	 sessionId
	 * @throws ApplicationException
	 */
	//void removeManualLetterRequest(int ban, int requestNumber, String sessionId)throws ApplicationException;

	/**
	 * Returns Array of FeeWaiverInfo
	 * 
	 * @param int 	banId
	 * @param String 	sessionID
	 * @return
	 * @throws ApplicationException
	 */
	List retrieveFeeWaivers(int banId,String sessionID) throws ApplicationException;
	/**
	 * Return Array of FutureStatusChangeRequestInfo
	 * 
	 * @param int	ban
	 * @param String 	sessionId
	 * @return
	 * @throws ApplicationException
	 */
	List retrieveFutureStatusChangeRequests(int ban,String sessionId )throws ApplicationException;

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

	public void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, 
			String waiveReason, String userMemoText, boolean isPortActivity, String sessionId) throws ApplicationException;

	/**
	 * Update Authorization Names
	 *
	 * @parm    int                billing account number of the newly created account
	 * @param   ConsumerNameInfo[] array of authorization names (max is 2)
	 *
	 * @see ConsumerNameInfo
	 *
	 * @excpetion ApplicationException
	 *
	 * @ejbgen:remote-method
	 */
	void updateAuthorizationNames(int ban, ConsumerNameInfo[] authorizationNames,String sessionId) throws ApplicationException;

	/**
	 * Updates the hold auto treatment flag in the billing system for an account.
	 * 
	 * This method sets the ban's treatment status as hold (false, i.e. no automatic treatment)
	 * or release (true, the hold on the automatic treatment is released).
	 * 
	 * @param pBan the billing account number
	 * @param holdAutoTreatment true to release, false to set auto treatment
	 * @throws ApplicationException
	 * 
	 */
	void updateAutoTreatment(int ban, boolean holdAutoTreatment,String sessionId) throws ApplicationException;

	/**
	 * Updates the brand of the account
	 * 
	 * @param ban int
	 * @param brandId int
	 * @param memoText String
	 *
	 * @exception ApplicationException
	 */
	void updateBrand (int ban, int brandId, String memoText,String sessionId) throws ApplicationException;

	/**
	 * Update Special Instructions for an account. Special Instructions are stored
	 * in Knowbility as a special type of memo ('3000') in the memo table.
	 *
	 * @param   int    BAN - billing account number
	 * @param   String Special Instructions text
	 *
	 * @exception ApplicationException
	 *
	 */
	void updateSpecialInstructions(int ban, String specialInstructions,String sessionId ) throws ApplicationException;

	/**
	 * Applies FeeWaiver.
	 *
	 * @param feeWaiver FeeWaiverInfo
	 * @throws ApplicationException
	 * 
	 */
	void applyFeeWaiver(FeeWaiverInfo feeWaiver,String sessionId) throws ApplicationException;

	/**
	 * @param ban
	 * @param newCreditClass
	 * @param memoText
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updateCreditClass(int ban, String newCreditClass, String memoText,String sessionId)throws ApplicationException;
	/**
	 * @param ban
	 * @param seqNo
	 * @param paymentTransfers
	 * @param allowOverPayment
	 * @param memonText
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void updateTransferPayment(int ban, int seqNo, PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText,String sessionId)throws ApplicationException;

	/**
	 * Perform Address validation
	 *
	 * @param   AddressInfo   address information
	 * @return  AddressValidationResultInfo   address validation result
	 *
	 * @exception ApplicationException
	 *
	 * @see AddressInfo
	 * @see AddressValidationResultInfo
	 */
	AddressValidationResultInfo validateAddress(AddressInfo addressInfo, String sessionId) throws ApplicationException;

	/**
	 * Retrieve credit check result info through AMDOCS API
	 *
	 * @param   int     ban       Billing Account Number
	 * @returns CreditCheckResultInfo    the most recent credit check result info of the account
	 *
	 * @exception ApplicationException
	 *
	 * @see CreditCheckResultInfo
	 */
	CreditCheckResultInfo retrieveAmdocsCreditCheckResultByBan(int ban, String sessionId) throws ApplicationException;

	/**
	 * Retrieves a list of Mike accounts for a given Talkgroup.
	 *
	 * @param urbanId
	 * @param fleetId
	 * @param talkGroupId
	 * 
	 * @throws ApplicationException
	 */
	Collection retrieveAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId, String sessionId) throws ApplicationException;

	/**
	 * Change an existing tentative consumer postpaid account to prepaid. The account sub type will
	 * be changed from 'R' (Regular) to 'Q' (Prepaid) and the bill cycle will be changed to the
	 * default prepaid bill cycle (currently '88')
	 *
	 * @param  int           billing account number of the newly created account
	 *
	 * @exception ApplicationException
	 *
	 */
	void changePostpaidConsumerToPrepaidConsumer(int ban, String sessionId) throws ApplicationException;

	/**
	 * Retrieve list of discounts for a given subscriber
	 *
	 * Discounts can be applied at the account level or at the subscriber level. This method will
	 * return all ban level discounts.

	 * @param   int     ban       Billing Account Number
	 * @returns DiscountInfo[]    array of discounts applied to account or subscriber
	 *
	 * @exception ApplicationException
	 *
	 * @see DiscountInfo
	 *
	 */
	List retrieveDiscounts(int ban, String sessionId) throws ApplicationException;

	/**
	 * Cancel Account for Port Out
	 * @param int banNumber  - Billing Account Number
	 * @param String deactivationReason - deactivation reason code 
	 * @param Date activityDate - activity date 
	 * @param String portOutInd - port out indicator 
	 * @excpetion ApplicationException
	 */
	void cancelAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, boolean isBrandPort, String sessionId) throws ApplicationException;

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
	void updateAccount(AccountInfo accountInfo,String sessionId ) throws ApplicationException; 

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

	void suspendSubscribers(int ban, Date activityDate, String activityReasonCode, 
			String[] subscriberId, String userMemoText, String sessionId) throws ApplicationException;

	/**
	 * Reverse a credit
	 *
	 * @param   CreditInfo    all attributes necessary for a credit
	 * @param   String        reason code for reversal
	 * @param   String        memo text
	 *
	 * @exception ApplicationException
	 *
	 * @see CreditInfo
	 *
	 */
	void reverseCredit(CreditInfo creditInfo, String reversalReasonCode, String memoText,String sessionId) throws ApplicationException;

	/**
	 * Reverse a credit and override users threshold
	 *
	 * @param   CreditInfo    all attributes necessary for a credit
	 * @param   String        reason code for reversal
	 * @param   String        memo text
	 *
	 * @exception ApplicationException
	 *
	 * @see CreditInfo
	 *
	 */
	void reverseCreditWithOverride(CreditInfo creditInfo, String reversalReasonCode, String memoText,String sessionId) throws ApplicationException;

	/**
	 * Delete a charge
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 * @param   String        reason code for deletion
	 * @param   String        memo text
	 *
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	void deleteCharge(ChargeInfo chargeInfo, String deletionReasonCode, String memoText,String sessionId) throws ApplicationException;

	/**
	 * Delete an existing charge and override users threshold
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 * @param   String        reason code for deletion
	 * @param   String        memo text
	 *
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	void deleteChargeWithOverride(ChargeInfo chargeInfo, String deletionReasonCode, String memoText,String sessionId) throws ApplicationException;

	/**
	 * Apply a charge to a billing account
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 *
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	double applyChargeToAccount(ChargeInfo chargeInfo,String sessionId) throws ApplicationException;
	

	/**
	 * Apply a charge to a billing account
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 * 
	 * @param   isWebserviceCall flag  is used in EJB to decide the call from Webservice , if yes it will set prepaid flag.
	 *
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	double applyChargeToAccount(ChargeInfo chargeInfo,boolean isWebserviceCall,String sessionId ) throws ApplicationException;

	/**
	 * Apply a charge to a billing account and override users threshold
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 *
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	double applyChargeToAccountWithOverride(ChargeInfo chargeInfo,String sessionId) throws ApplicationException;
	
	/**
	 * Apply a charge to a billing account and override users threshold
	 *
	 * @param   ChargeInfo    all attributes necessary for a charge
	 * @param   isWebserviceCall flag  is used in EJB to decide the call from Webservice , if yes it will set prepaid flag.
	 * @exception ApplicationException
	 *
	 * @see ChargeInfo
	 *
	 */
	double applyChargeToAccountWithOverride(ChargeInfo chargeInfo,boolean isWebserviceCall,String sessionId) throws ApplicationException;
	/**
	 * @param pChargeInfo
	 * @param pAdjustmentAmount
	 * @param pAdjustmentReasonCode
	 * @param pMemoText
	 * @return
	 * @throws ApplicationException
	 */
	double adjustCharge(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, String sessionId )throws ApplicationException; 
	/**
	 * @param pChargeInfo
	 * @param pAdjustmentAmount
	 * @param pAdjustmentReasonCode
	 * @param pMemoText 
	 * @return
	 * @throws ApplicationException
	 */
	double adjustChargeWithOverride(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText,String sessionId )throws ApplicationException;

	/**
	 * restored a cancelled list of subscribers
	 *
	 * @param int		ban 
	 * @param Date  	restoreDate 
	 * @param String 	restoreReasonCode 
	 * @param String[] 	subscriberId 
	 * @param String  	restoreComment 
	 * @throws ApplicationException
	 *
	 */
	void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode, String[] subscriberId, String restoreComment,String sessionId) throws ApplicationException;
	/**
	 * Creates ManualLetterRequest
	 * 
	 * @param LMSRequestInfo 	letterRequestInfo
	 * @param String	sessionId
	 * @throws ApplicationException
	 */
	//void createManualLetterRequest(LMSRequestInfo lmsRequestInfo, String sessionId)throws ApplicationException;
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
	void refundPaymentToAccount( int ban, int paymentSeq, String reasonCode, String memoText, boolean isManual, String authorizationCode,String sessionId)throws ApplicationException;
	/**
	 * @param discountInfo
	 * @throws ApplicationException
	 */
	void applyDiscountToAccount(DiscountInfo discountInfo,String sessionId)throws ApplicationException;

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
	 * @exception   ApplicationException  10003  BAN must be in tentative status"
	 *
	 * @ejbgen:remote-method
	 */
	String performPostAccountCreationPrepaidTasks(int ban, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader,String sessionId ) throws ApplicationException;

	/**
	 * Suspend Account for Port Out
	 * @param int banNumber  - Billing Account Number
	 * @param String deactivationReason - deactivation reason code 
	 * @param Date activityDate - activity date 
	 * @param String portOutInd - port out indicator 
	 * @excpetion ApplicationException
	 */
	void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd,String sessionId) throws ApplicationException;
	/**
	 * @param CreditInfo		creditInfo
	 * @param String			sessionID
     * @return true  to indicate the adjustment(s) is created in KB
	 * @throws ApplicationException
	 */
	boolean applyCreditToAccount(CreditInfo creditInfo,String sessionId ) throws ApplicationException; 
	/**
	 * @param CreditInfo		creditInfo
	 * @param String			sessionID
     * @return true  to indicate the adjustment(s) is created in KB
	 * @throws ApplicationException
	 */
	boolean applyCreditToAccountWithOverride(CreditInfo creditInfo,String sessionId ) throws ApplicationException; 


	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit).
	 *
	 * @param int         billing account ID
	 * @param String      phone number
	 * @param String      userId
	 * @param double      adjustment amount
	 * @param String      adjustment reason code
	 * @param String      transaction id (required for some adjustments)
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId) throws ApplicationException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit).
	 * Tax will also be debited or credited.
	 *
	 * @param int         	billing account ID
	 * @param String      	phone number
	 * @param String      	userId
	 * @param double      	adjustment amount
	 * @param String      	adjustment reason code
	 * @param String      	transaction id (required for some adjustments)
	 * @param TaxSummaryInfo	tax information for adjustment
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax) throws ApplicationException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber.
	 *
	 * Since the input to this method does not explicitly specify whether it is a credit
	 * or debit, the determination will be made based on the amount (< 0 debit, >=0 credit)
	 * and the supplied tax option parameter.  Tax will also be debited or credited.
	 *
	 * @param int         	billing account ID
	 * @param String      	phone number
	 * @param String      	userId
	 * @param double      	adjustment amount
	 * @param String      	adjustment reason code
	 * @param String      	transaction id (required for some adjustments)
	 * @param TaxSummaryInfo	tax information for adjustment
	 * @param char			tax option for adjustment - one the Credit interface TAX_OPTION_XXXX constants
	 *
	 * @throws ApplicationException
	 */
	void adjustBalanceForPayAndTalkSubscriber(int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax, char pTaxOption) throws ApplicationException;

	/**
	 * Adjust Balance of a Pay&Talk subscriber. Since the input to this method is a charge, the
	 * balance of the subscriber will be decreased.
	 *
	 * @param String      userId
	 * @param ChargeInfo  charge information
	 *
	 * @throws ApplicationException
	 *
	 * @see ChargeInfo
	 */
	void adjustBalanceForPayAndTalkSubscriber(String pUserId, ChargeInfo pChargeInfo) throws ApplicationException;	  

	/**
	 * Adjust Balance of a Pay&Talk subscriber. Since the input to this method is a credit, the
	 * balance of the subscriber will be increased.
	 *
	 * Note:  Tax will also be credited based on the taxOption constant stored in the CreditInfo
	 * 		object (added 2005/03/16).
	 *
	 * @param String      userId
	 * @param CreditInfo  credit information
	 *
	 * @throws ApplicationException
	 *
	 * @see CreditInfo
	 */
	void adjustBalanceForPayAndTalkSubscriber(String pUserId, CreditInfo pCreditInfo) throws ApplicationException;

	/**
	 * Apply Credit for a Feature Card to a Pay&Talk subscriber.
	 *
	 * @param CardInfo        feature card information
	 * @param ServiceInfo[]   feature card services (could be empty array)
	 *
	 * @return Transaction ID (confirmation number) for adjustment
	 *
	 * @throws ApplicationException
	 *
	 * @see CreditInfo
	 *
	 */
	void applyCreditForFeatureCard(CardInfo cardInfo, ServiceInfo[] cardServices, String userId);

	/**
	 * Apply Top-Up with Airtime Card for a Pay&Talk subscriber.
	 *
	 * @param int      BAN (Billing Account Number)
	 * @param String   Phonenumber
	 * @param CardInfo airtime card
	 * @param String   userId
	 * @param String   application ID
	 *
	 * @return Transaction ID (confirmation number) for top up
	 * @throws ApplicationException	
	 *
	 */	
	String applyTopUpForPayAndTalkSubscriber(int pBan, String pPhoneNumber, CardInfo pAirtimeCard, String pUserId, String pApplicationId) throws ApplicationException;

	/**
	 * Apply Top-Up for a Pay&Talk subscriber. The amount will be charged to the registered
	 * top-up credit card.
	 *
	 * @param int      BAN (Billing Account Number)
	 * @param String   Phonenumber
	 * @param double   amount
	 * @param String   card Type ("C" for credit card, or "D" for debit card
	 * @param String   userId
	 * @param String   application ID
	 * @return String BIH reference number	
	 * @throws ApplicationException
	 */
	String applyTopUpForPayAndTalkSubscriber(int pBan, String pPhoneNumber, double pAmount, String pOrderNumber, String pUserId, String pApplicationId) throws ApplicationException;

	/**
	 * Validates information for a new prepaid account (against Pay&Talk database).
	 *
	 * @param   applicationId                applicationId
	 * @param   pUserId                      userId
	 * @param   activationTopUpInfo          activationTopUpInfo
	 * @param   phoneNumber                  phoneNumber
	 * @param   esn                        	esn
	 * @param   provinceCode    				province code
	 * @param pPrepaidConsumerAccountInfo	all attributes related to a prepaid account
	 *
	 *
	 * @exception ApplicationException
	 *
	 * @see PrepaidConsumerAccountInfo
	 *
	 */
	void creditSubscriberMigration(String applicationId, String pUserId, ActivationTopUpInfo activationTopUpInfo, String phoneNumber, String esn, String provinceCode,PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo) throws ApplicationException;

	/**
	 * removeTopupCreditCard
	 * @param MDN
	 * 
	 * @throws ApplicationException
	 */
	void removeTopupCreditCard(String MDN) throws ApplicationException;

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
	void saveActivationTopUpArrangement (String banId, String MDN, String serialNo, 
			ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement, String user)  throws ApplicationException;

	/**
	 * @param int	    billing account number (BAN)
	 * @param long		portalUserID
	 * @param Array		BillNotificationContact
	 * @param String	applicationCode
	 * @deprecated
	 */
	void saveBillNotificationDetails ( int ban, long portalUserID, BillNotificationContactInfo[] pBillNotificationContact, String applicationCode)throws ApplicationException;

	/**
	 * updateAccountPIN
	 * @param banId
	 * @param MDN
	 * @param serialNo
	 * @param prevPIN
	 * @param PIN
	 * @param user
	 * @throws ApplicationException
	 */	
	void updateAccountPIN(int banId, String MDN, String serialNo, String prevPIN, String PIN, String user) throws ApplicationException;

	/**
	 * Update Auto Top-Up information for Pay&Talk subscriber
	 *
	 * @param pAutoTopUpInfo
	 * @param pUserId
	 * @param existingAutoTopUp
	 * @param existingThresholdRecharge
	 * @throws ApplicationException
	 */
	void updateAutoTopUp(AutoTopUpInfo pAutoTopUpInfo, String pUserId, boolean existingAutoTopUp, 
			boolean existingThresholdRecharge) throws ApplicationException;

	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param serialNo
	 * @param creditCard
	 * @param expiryDate
	 * @param cardType
	 * @param user
	 * @param encrypted
	 * 
	 * @deprecated Deprecated as part of Cambio. Use  updateTopUpCreditCard(String, String, CreditCardInfo, String, Boolean).
	 * 
	 */
	void updateTopupCreditCard(String banId, String MDN,String serialNo, CreditCardInfo creditCard, String user,
			boolean encrypted);
	
	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param creditCardInfo
	 * @param user
	 * @param encrypted
	 */
	void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user,boolean encrypted);
	
	/**
	 * updateTopupCreditCard
	 * @param banId
	 * @param MDN
	 * @param creditCardInfo
	 * @param user
	 * @param encrypted
	 * @param isFirstTimeRegistration
	 */
	void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user, boolean encrypted, boolean isFirstTimeRegistration);

	void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException;
	
	void updateContactInformation(int billingAccountNumber, ContactPropertyInfo contactPropertyInfo, String sessionId) throws ApplicationException;
	
	/**
	 * Retrieves the last credit check info by billing account number (BAN), merging CDA and KB data
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveLastCreditCheckResultByBan(int ban, String productType, String sessionId) throws ApplicationException;
	
	/**
	 * Retrieves only the KB credit check info by billing account number (BAN)
	 *
	 * @param   String      billing account number (BAN)
	 * @param   Char Product Type
	 * @returns CreditInfo  related information
	 * @see AccountInfo
	 */
	CreditCheckResultInfo retrieveKBCreditCheckResultByBan(int ban, String productType, String sessionId) throws ApplicationException;
	
	/**
	 * Return User's Profile ID
	 * @returns String       User's profile id
	 * @exception ApplicationException
	 */
	public String getUserProfileID(String sessionId) throws ApplicationException;

	public void updatePersonalCreditInformation(int ban, PersonalCreditInfo personalCreditInfo, String sessionId) throws ApplicationException;

	public void updateBusinessCreditInformation(int ban, BusinessCreditInfo businessCreditInfo, String sessionId) throws ApplicationException;
	public void createCreditBalanceTransferRequest(int sourceBan ,int targetBan,String sessionId) throws ApplicationException;
	public void cancelCreditBalanceTransferRequest(int creditTransferSequenceNumber,String sessionId)   throws ApplicationException;
	public List getCreditBalanceTransferRequestList(int ban,String sessionId) throws ApplicationException;
	
	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List applyChargesAndAdjustmentsToAccount( List chargeInfoList, String sessionId) throws ApplicationException;
	
	/**
	 * Applies charges and Adjustment to account based on BAN with Tax
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List applyChargesAndAdjustmentsToAccountWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, String sessionId) 
			throws ApplicationException;

	boolean applyCreditToAccountAndSubscriber(CreditInfo creditInfo, boolean overrideThresholdInd , String sessionId) throws ApplicationException ;

	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List applyChargesAndAdjustmentsToAccountForSubscriber(List chargeInfoList, String sessionId) throws ApplicationException;
	
	/**
	 * Applies charges and Adjustment to account based on BAN
	 * 
	 * @param List ChargeAdjustmentInfo
	 * @throws ApplicationException
	 */
	List applyChargesAndAdjustmentsToAccountForSubscriberWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, 
			String sessionId) throws ApplicationException;

	List adjustChargeToAccountForSubscriberReturn(int ban, String subscriberNumber, String phoneNumber,List chargeInfoList, Date searchFromDate, Date searchToDate,
			String adjustmentMemoText,String productType, boolean bypassAuthorizationInd,boolean overrideThresholdInd,String sessionId) throws ApplicationException;
	
	List getCustomerNotificationPreferenceList(int ban, String sessionId) throws ApplicationException;
	
	void updateCustomerNotificationPreferenceList(int ban, List notificationPreferenceList, String sessionId) throws ApplicationException;

	void testAmdocsConnectivity(String sessionId) throws ApplicationException;

	void cancelSubscribers(AccountInfo accountInfo, String[] subscriberIdArray,String[] waiveReasonArray, Date activityDate,
			String activityReasonCode, char depositReturnMethod,String userMemoText, Date logicalDate,
			boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd, String sessionId) throws ApplicationException;
}
