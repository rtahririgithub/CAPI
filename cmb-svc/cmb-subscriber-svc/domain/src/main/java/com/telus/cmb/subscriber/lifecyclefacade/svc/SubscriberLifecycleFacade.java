package com.telus.cmb.subscriber.lifecyclefacade.svc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.InvalidServiceException;
import com.telus.api.SystemException;
import com.telus.api.account.Address;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.ServicesValidation;
import com.telus.api.equipment.Equipment;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.resource.Resource;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.SubscriberIdentifierInfo;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountPinActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.config.info.AddressChangeInfo;
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.PaymentMethodChangeInfo;
import com.telus.eas.config.info.PrepaidTopupInfo;
import com.telus.eas.config.info.PricePlanChangeInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.config.info.ServiceChangeInfo;
import com.telus.eas.config.info.SubscriberChangeInfo;
import com.telus.eas.config.info.SubscriberChargeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.eas.task.info.SubscriberResumedPostTaskInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.ProvisioningLicenseInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.eas.utility.info.ServiceInfo;

public interface SubscriberLifecycleFacade {

	public static final String PROCESS_ACTIVATION = "Activation";
	public static final String PROCESS_MIGRATION = "Migration";
	public static final String PROCESS_RENEWAL = "Renewal";

	void assignTNResources(String phoneNumber, String networkType, String localIMSI, String remoteIMSI) throws ApplicationException;

	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param phoneNumber		Mandatory, 10 digits numeric.
	 * @param networkType		One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param newLocalIMSI		Mandatory.
	 * @param newRemoteIMSI		Not Mandatory.
	 * @throws ApplicationException
	 * 
	 */
	void changeIMSIs(String phoneNumber, String networkType, String newLocalIMSI, String newRemoteIMSI) throws ApplicationException;

	void changeTN(String oldPhoneNumber, String newPhoneNumber, String networkType) throws ApplicationException;

	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param phoneNumber		Mandatory, 10 digits numeric.
	 * @param oldNetworkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param newNetworkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param localIMSI			Only if NEW network type is HSPA.
	 * @param remoteIMSI		Only if NEW network type is HSPA, not mandatory.
	 * @param usimId			Only if NEW network type is HSPA.
	 * @throws ApplicationException
	 */
	void changeNetwork(String phoneNumber, String oldNetworkType, String newNetworkType, String localIMSI, String remoteIMSI, String usimId) throws ApplicationException;

	void releaseTNResources(String phoneNumber, String networkType) throws ApplicationException;

	String retrieveTNProvisionAttributes(String phoneNumber, String networkType) throws ApplicationException;

	void setIMSIStatus(String networkType, String localIMSI, String remoteIMSI, String status) throws ApplicationException;

	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param phoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param status		Mandatory. Valid values are AS or AI. This is the Provisioning Status.
	 * @throws ApplicationException
	 */
	void setTNStatus(String phoneNumber, String networkType, String status) throws ApplicationException;

	/**
	 * Obtain sessionId for the given user credentials
	 * 
	 * @param userId The user id.
	 * @param password The password.
	 * @param applicationId The application id.
	 */
	String openSession(String userId, String password, String applicationId) throws ApplicationException;

	/**
	 * Retrieve Provisioning Transaction Detail Info for Subscriber
	 *
	 * @param 	String - subscriber number/Id
	 * @param 	String - provisioning transaction number
	 * @return 	Array - ProvisioningTransactionDetailInfo[]
	 * @throws 	ApplicationException
	 *
	 */
	ProvisioningTransactionDetailInfo[] retrieveProvisioningTransactionDetails(String subscriberId, String transactionNo) throws ApplicationException;

	/*
	 * @deprecated
	 * 
	 */
	SubscriberLifecycleInfo resumeCancelledSubscriber(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, String sessionId)
			throws ApplicationException, SystemException;

	/*
	 * @deprecated
	 * 
	 */
	List resumeCancelledSubscriber(SubscriberInfo subscriberInfo, AccountInfo accountInfo, String salesRepCode, String dealerCode, String reasonCode, String memoText, String sessionId)
			throws ApplicationException, SystemException;
	
	/**
	 * Resume a cancelled subscriber
	 * 
	 * This method resumes a cancelled subscriber. The subscriber status has to
	 * be 'Cancelled'.
	 * 
	 * @param subscriberInfo
	 *            subscriber information (mandatory)
	 * @param activityReasonCode
	 *            reason code for resuming the cancelled subscriber (mandatory)
	 * @param userMemoText
	 *            optional comment for cancelling the subscriber (optional)
	 * @param portIn
	 * @param portProcessType
	 * @param oldBanId
	 * @param oldSubscriberId
	 * 
	 * @exception ApplicationException
	 * 
	 * @deprecated
	 */
	void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId,
			String sessionId) throws ApplicationException;
	
	void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId, SubscriberResumedPostTaskInfo taskInfo,
			String sessionId) throws ApplicationException;

	/**
	 * This method should be used when required objects are not pre-populated in EquipmentChangeRequestInfo.
	 * 
	 * @param subscriberId
	 * @param billingAccountNumber
	 * @param equipmentChangeRequest
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	EquipmentChangeRequestInfo changeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException, SystemException;

	/**
	 * This method should not be used if all the required information are not pre-populated. see {@link #changeEquipment(String, int, EquipmentChangeRequestInfo, String)} instead
	 * @param subscriberInfo
	 * @param accountInfo
	 * @param equipmentChangeRequest
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 * @throws SystemException
	 */
	EquipmentChangeRequestInfo changeEquipment(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException, SystemException;

	SubscriberContractInfo getServiceAgreement(String subscriberId, int billingAccountNumber) throws ApplicationException, SystemException;

	SubscriberContractInfo getServiceAgreement(SubscriberInfo subscriberInfo, AccountInfo accountInfo) throws ApplicationException, SystemException;

	ContractChangeInfo validateServiceAgreement(ContractChangeInfo contractChangeInfo, String sessionId) throws ApplicationException;

	ContractChangeInfo getServiceAgreementForUpdate(String subscriberId, int billingAccountNumber) throws SystemException, ApplicationException;

	SubscriberContractInfo getServiceAgreementForEquipmentChange(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest)
			throws SystemException, ApplicationException;

	SubscriberContractInfo getServiceAgreementForEquipmentChange(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, SubscriberContractInfo contractInfo)
			throws SystemException, ApplicationException;

	ContractChangeInfo saveServiceAgreement(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, SystemException;

	EquipmentChangeRequestInfo swapEquipmentForPhoneNumberInSems(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest) throws ApplicationException;

	ArrayList browseAllMessages(String queueBeanId);

	ArrayList browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType);

	ArrayList browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType, String subType);

	void asyncInsertProductInstance(int billingAccountNumber, String subscriberId, String processType, String sessionId);

	void insertProductInstance(int billingAccountNumber, String subscriberId, String processType, String sessionId) throws ApplicationException;
	
	void asyncInsertProductInstance(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException;
	
	void insertProductInstance(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException;
			
	void asyncUpdateProductInstance(int billingAccountNumber, String subscriberId, String phoneNumber, String processType, String sessionId) ;

	void asyncUpdateProductInstance(int billingAccountNumber, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId);

	void updateProductInstance(int billingAccountNumber, String subscriberId, String phoneNumber, String processType, String sessionId) throws ApplicationException;

	void updateProductInstance(int billingAccountNumber, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException;

	/* Commented out as they shouldn't be needed anymore after redesign (Mar 2012). This is left in the code in case it may be required again */
	//	void asyncManageProductParameters (int billingAccountNumber, String subscriberId, String processType, String sessionId);
	//	void manageProductParameters (int billingAccountNumber, String subscriberId, String processType, String sessionId) throws ApplicationException;
	//	void asyncManageProductResources (int billingAccountNumber, String subscriberId, String processType, String sessionId);
	//	void manageProductResources (int billingAccountNumber, String subscriberId, String processType, String sessionId) throws ApplicationException;

	/**
	 * Following methods are based on those in SLCM
	 * BEGIN
	 */

	/**
	 * 
	 * @param banNumber
	 * @param phoneNumber
	 * @param deactivationReason
	 * @param activityDate
	 * @param portOutInd
	 * @param isBrandPort
	 * @param subscriberId
	 * @param commSuiteInfo
	 * @param suppressNotificationInd
	 * @param srpdsHeader
	 * @param sessionId
	 * @throws ApplicationException
	 */

	void cancelPortedInSubscriber(int banNumber, String phoneNumber, String deactivationReason, Date activityDate, String portOutInd, boolean isBrandPort, String subscriberId,
			CommunicationSuiteInfo commSuiteInfo, boolean suppressNotificationInd, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException;

	/**
	 * Cancel a Subscriber
	 * 
	 * This method cancels a subscriber unless it is the last active subscriber
	 * on that account. In that case, the account itself needs to be cancelled.
	 * 
	 * The account has to be in 'Open' or 'Suspended' status and the subscriber
	 * status has to be 'Active' or 'Suspended'.
	 * 
	 * Under certain conditions the subscriber is charged a cancellation penalty
	 * which will be applied automatically unless the fee is waived by giving a
	 * reason.
	 * 
	 * @param pSubscriberInfo
	 *            - Subscriber information (mandatory)
	 * @param pActivityDate
	 *            - Date on which to cancel the subscriber (optional - defaul:
	 *            today)
	 * @param pActivityReasonCode
	 *            - Reason code for cancelling the subscriber (mandatory)
	 * @param pDepositReturnMethod
	 *            - Method by which the deposit should be returned (mandatory)
	 *            possible values are: 'O' cover open debts 'R' refund entire
	 *            amount 'E' refund excess amount
	 * @param pWaiveReason
	 *            - Reason for waving applicable cancellation fee (optional -
	 *            default: fee is charged))
	 * @param pUserMemoText
	 *            - Optional comment for cancelling the subscriber (optional)
	 ** @param notificationSuppressionInd
	 *            - notificationSuppressionInd to send the email notification for (optional)
	 * @param auditInfo
	 *            - auditInfo  to send the email notification for cancelling the subscriber (optional)
	 * 
	 *  @param commSuiteInfo - Communication Suite companion subscribers cancellation 
	  * @param header - companion subscriber SRPDS update.
	 * @exception ApplicationException
	 */

	void cancelSubscriber(SubscriberInfo pSubscriberInfo, java.util.Date pActivityDate, String pActivityReasonCode, String pDepositReturnMethod, String pWaiveReason, String pUserMemoText,
			boolean notificationSuppressionInd, AuditInfo auditInfo, CommunicationSuiteInfo commSuiteInfo, ServiceRequestHeader header, String sessionId) throws ApplicationException;

	/**
	 * Suspend a Subscriber
	 * 
	 * This method suspends a subscriber unless it is the last active subscriber
	 * on that account. In that case, the account itself needs to be suspended.
	 * 
	 * The account has to be in 'Open' status and the subscriber status has to
	 * be 'Active'.
	 * 
	 * @param subscriberInfo
	 *            subscriber information (mandatory)
	 * @param activityDate
	 *            date on which to suspend the subscriber (optional - defaul:
	 *            today)
	 * @param activityReasonCode
	 *            reason code for suspending the subscriber (mandatory)
	 * @param userMemoText
	 *            optional comment for suspending the subscriber (optional)
	 * 
	 * @exception ApplicationException
	 */
	void suspendSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException;

	/**
	 * Restore a suspended subscriber
	 * 
	 * This method restores a suspended subscriber to 'active' status. The
	 * subscriber status has to be 'Suspended'.
	 * 
	 * @param subscriberInfo
	 *            subscriber information (mandatory)
	 * @param activityDate
	 *            date on which to suspend the subscriber (optional - defaul:
	 *            today)
	 * @param activityReasonCode
	 *            reason code for restoring the suspended subscriber (mandatory)
	 * @param userMemoText
	 *            optional comment for cancelling the subscriber (optional)
	 * 
	 * @exception ApplicationException
	 */
	void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate, String activityReasonCode, String userMemoText, boolean portIn, String sessionId)
			throws ApplicationException;

	

	/**
	 * Changes Equipment for PCS and IDEN subscribers
	 * 
	 * This method updates primary and secondary equipment for a given
	 * subscriber. It also invokes an associated price plan change if the
	 * subscriber's contract information is passed.
	 * 
	 * @param subscriberInfo
	 *            - Subscriber information (mandatory)
	 * @param oldPrimaryEquipmentInfo
	 *            - old primary equipment information (mandatory)
	 * @param newPrimaryEquipmentInfo
	 *            - new primary equipment information (mandatory)
	 * @param newSecondaryEquipmentInfo
	 *            - new secondary equipment information (optional)<BR>
	 *            - if null is passed, secondary equipment will not be processed<BR>
	 *            - if an empty array is passed, all secondary equipment will be
	 *            removed<BR>
	 *            - all equipment passed in this array will be added as active,
	 *            non-primary equipment with an esn level set to > 1 and
	 *            corresponding to sequence in the array
	 * @param dealerCode
	 *            - Dealer code (optional) - used for price plan change
	 *            commissioning
	 * @param salesRepCode
	 *            - Sales rep code (optional) - used for price plan change
	 *            commissioning
	 * @param requesterId
	 *            - Requester id (optional except for mule-to-mule) - used for
	 *            warranty swap
	 * @param swapType
	 *            - Swap type (optional except for mule-to-mule) - used for
	 *            warranty swap
	 * @param subscriberContractInfo
	 *            - Subscriber's contract information (optional except when
	 *            changing price plan) - used for price plan and services change
	 * @param pricePlanValidation
	 * 
	 * @throws ApplicationException
	 */
	void changeEquipment(SubscriberInfo subscriberInfo, EquipmentInfo oldPrimaryEquipmentInfo, EquipmentInfo newPrimaryEquipmentInfo, EquipmentInfo[] newSecondaryEquipmentInfo, String dealerCode,
			String salesRepCode, String requesterId, String swapType, SubscriberContractInfo subscriberContractInfo, PricePlanValidationInfo pricePlanValidation, AuditInfo audidInfo,
			boolean notificationSuppressInd, SubscriberContractInfo oldContract, String sessionId) throws ApplicationException;

	/**
	 * Change subscribers IP address
	 * 
	 * @param ban
	 * @param subscriberId
	 * @param productType
	 * @param newIp
	 *            - new IP address (if left empty, random ip address is being
	 *            assigned)
	 * @param newIpType
	 *            - new IP type (P=Private, B=Public, C=Corporate)
	 * @param newIpCorpCode
	 *            - new IP Corporate Code
	 * 
	 * @exception ApplicationException
	 */
	void changeIP(int ban, String subscriberId, String newIp, String newIpType, String newIpCorpCode, String sessionId) throws ApplicationException;

	/**
	 * Change Fax Number This method attempts to change the subscriber's fax
	 * number to a system-assigned number based on his current phone number.
	 * 
	 * @param subscriber
	 *            - contains subscriber information
	 * @exception ApplicationException
	 */
	void changeFaxNumber(SubscriberInfo subscriber, String sessionId) throws ApplicationException;

	/**
	 * Change Fax Number This method attempts to change the subscriber's fax
	 * number to the specific fax number given.
	 * 
	 * @param subscriber
	 *            - contains subscriber information
	 * @param newFaxNumber
	 *            - contains information about the new fax number
	 * @exception ApplicationException
	 */
	void changeFaxNumber(SubscriberInfo subscriber, AvailablePhoneNumberInfo newFaxNumber, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber fleet and member id
	 * 
	 * This method is used to change UFMI to another fleet and member id. The
	 * fleet must be associated to the ban.
	 * 
	 * @param idenSubscriberInfo
	 *            subscriber information
	 * @param newUrbanId
	 *            new urban id
	 * @param newFleetId
	 *            new fleet id
	 * @param newMemberId
	 *            new member id
	 * 
	 * @exception ApplicationException
	 *                VAL10010 Fleet not associated to BAN.
	 * @exception ApplicationException
	 *                VAL20033 The supplied member id is not available.
	 * @exception ApplicationException
	 */
	void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo, int newUrbanId, int newFleetId, String newMemberId, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber phone number
	 * 
	 * @param subscriberInfo
	 *            - Subscriber information
	 * @param newPhoneNumber
	 *            - New phone number
	 * @param reasonCode
	 *            - Change reason code
	 * @param dealerCode
	 *            - Dealer Code
	 * @param salesRepCode
	 *            - Sales Representative Code
	 * 
	 * @exception ApplicationException
	 *                VAL20026 The supplied phone number is not available.
	 * @exception ApplicationException
	 */
	void changePhoneNumber(SubscriberInfo subscriberInfo, AvailablePhoneNumberInfo newPhoneNumber, String reasonCode, String dealerCode, String salesRepCode, String sessionId)
			throws ApplicationException;

	void changePhoneNumberPortIn(SubscriberInfo subscriberInfo,AvailablePhoneNumberInfo newPhoneNumber, 
			String reasonCode,String dealerCode, String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException;
	
	/**
	 * 
	 * @param subscriberInfo
	 * @param accountInfo
	 * @param equipmentChangeRequest
	 * @throws ApplicationException
	 */
	void changeEsimEnabledDevice(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException;
	
	/**
	 * Create new subscriber
	 * 
	 * <P>
	 * This method creates a new PCS or IDEN subscriber.
	 * 
	 * @param subscriberInfo
	 *            Subscriber Information (i.e. price plan, name etc.)
	 * @param subscriberContractInfo
	 *            SubscriberContractInfo
	 * @param activate
	 *            Activate yes/no
	 * @param overridePatternSearchFee
	 *            Apply OverridePatternSearchFee yes/no
	 * @param activationFeeChargeCode
	 *            ActivationFeeChargeCode to apply (if not empty)
	 * @param dealerHasDeposit
	 *            Has Dealer kept the deposit yes/no
	 * @param portedIn
	 *            is it portedIn subscriber
	 * @exception ApplicationException
	 */
	void createSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, boolean activate, boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn, ServicesValidation srvValidation, String portProcessType, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException;

	/**
	 * Retrieves a list of Mike subscribers for a given Talkgroup.
	 * 
	 * @param srcSubscriberInfo
	 * @param newSubscriberInfo
	 * @param activityDate
	 * @param subscriberContractInfo
	 * @param newPrimaryEquipmentInfo
	 * @param newSecondaryEquipmentInfo
	 * @param migrationRequestInfo
	 * 
	 * @throws ApplicationException
	 */
	void migrateSubscriber(SubscriberInfo srcSubscriberInfo, SubscriberInfo newSubscriberInfo, Date activityDate, SubscriberContractInfo subscriberContractInfo,
			com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo, MigrationRequestInfo migrationRequestInfo,
			String sessionId) throws ApplicationException;

	/**
	 * Move an existing subscriber to another account
	 * 
	 * This method moves a subscriber from the current account to another
	 * account. The subscriber status has to be 'Active' and the account where
	 * the subscriber is moved to can be in any status except 'Closed'.
	 * 
	 * @param subscriberInfo
	 *            existing subscriber information
	 * @param targetBan
	 * @param activityDate
	 *            activity date
	 * @param transferOwnership
	 * @param activityReasonCode
	 *            reason code for moving the subscriber
	 * @param userMemoText
	 *            optional comment for moving the subscriber (optional)
	 * @param dealerCode
	 *            Knowbility Dealer Code
	 * @param salesRepCode
	 *            Knowbility Sales Rep Code
	 * 
	 * @exception ApplicationException
	 */
	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Move an existing subscriber to another account
	 * 
	 * This method moves a subscriber from the current account to another
	 * account. The subscriber status has to be 'Active' and the account where
	 * the subscriber is moved to can be in any status except 'Closed'.
	 * 
	 * @param subscriberInfo
	 *            Existing subscriber information
	 * @param targetBan
	 *            Account to which the subscriber is being moved (mandatory)
	 * @param activityDate
	 *            Activity date (mandatory)
	 * @param activityReasonCode
	 *            Reason code for moving the subscriber (mandatory)
	 * @param userMemoText
	 *            Optional comment for moving the subscriber (optional)
	 * 
	 * @exception ApplicationException
	 */
	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, boolean notificationSuppressionInd,
			AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * change price plan for subscriber
	 * 
	 * @param subscriberInfo
	 *            - Subscriber information
	 * @param subscriberContractInfo
	 *            - Subscriber Contract Information
	 * @param dealerCode
	 *            - Dealer Code
	 * @param salesRepCode
	 *            - Sales Rep Code
	 * @param pricePlanValidation
	 * 
	 * @throws ApplicationException
	 * 
	 * @see com.telus.eas.subscriber.info.ServiceAgreementInfo
	 * @see com.telus.eas.subscriber.info.ServiceFeatureInfo
	 */
	void changePricePlan(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			boolean notificationSuppressionInd, AuditInfo auditInfo, SubscriberContractInfo oldContractInfo, String sessionId) throws ApplicationException;

	/**
	 * change service agreement by adding, deleting, or updating services and
	 * features
	 * 
	 * @param subscriberInfo
	 *            - Subscriber information
	 * @param subscriberContractInfo
	 *            Subscriber Contract Information
	 * @param dealerCode
	 *            Dealer Code
	 * @param salesRepCode
	 *            Sales Rep Code
	 * @param pricePlanValidation
	 * 
	 * @throws ApplicationException
	 * 
	 */

	void changeServiceAgreement(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Release reserved subscriber
	 * 
	 * @param subscriberInfo
	 * @param subscriberContractinfo
	 * @param equipmentInfo
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void releaseSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractinfo, EquipmentInfo equipmentInfo, String sessionId) throws ApplicationException;

	/**
	 * Release ported number.
	 * 
	 * @param subscriberInfo
	 * @param subscriberContractinfo
	 * @param equipmentInfo
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void releasePortedInSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractinfo, EquipmentInfo equipmentInfo, String sessionId) throws ApplicationException;

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
	void activateReservedSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, Date startServiceDate, String activityReasonCode, ServicesValidation srvValidation,
			String portProcessType, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException;

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
	void updateCommitment(SubscriberInfo pSubscriberInfo, CommitmentInfo pCommitmentInfo, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException;

	/**
	* END of SLCM equivalents
	*/

	//PRM CALLS MIGRATION

	PortInEligibility checkPortInEligibility(String phoneNumber, String portVisibility, int incomingBrand) throws ApplicationException;

	PortRequestInfo[] getCurrentPortRequestsByPhoneNumber(String phoneNumber, int brandId) throws ApplicationException;

	PortRequestInfo[] getCurrentPortRequestsByBan(int banNumber) throws ApplicationException;

	PortRequestSummary checkPortRequestStatus(String phoneNumber, int brandId) throws ApplicationException;

	void validatePortInRequest(PortRequestInfo portRequest, String applicationId, String user) throws ApplicationException;

	void activatePortInRequest(PortRequestInfo portRequest, String applicationId) throws ApplicationException;

	void cancelPortInRequest(String requestId, String reasonCode, String applicationId) throws ApplicationException;

	String createPortInRequest(SubscriberInfo subscriber, String portProcessType, int incomingBrandId, int outgoingBrandId, String sourceNetwork, String targetNetwork, String applicationId,
			String user, PortRequestInfo portRequest) throws ApplicationException;

	void submitPortInRequest(String requestId, String applicationId) throws ApplicationException;

	// THIS METHOD MAY NOT HAVE ANY CALLERS ???
	// Commented for WNP-WLI Upgrade 2012 Oct Release
	//public void modifyPortInRequest(SubscriberInfo subscriber, String applicationId, String user,PortRequestInfo portReq) throws ApplicationException;

	PRMReferenceData[] retrieveReferenceData(String category) throws ApplicationException;

	/**
	 * This method checks to see if the Price Plan or Service to be added is compatible with the Account.  
	 * If it is not compatible, a specific subclass of InvalidServiceException is thrown with a corresponding  
	 * error reason code.  
	 * 
	 * InvalidPricePlanChangeException is thrown with reason code
	 * InvalidPricePlanChangeException.ACCOUNT_TYPE_SUBTYPE_MISMATCH if serviceInfo is an instance of 
	 * PricePlanInfo and serviceInfo has familyTypes array with value 'Y'
	 * ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE.
	 * 
	 * InvalidServiceChangeException is thrown with reason code
	 * InvalidServiceChangeException.UNAVAILABLE_SERVICE if serviceInfo is an instance of 
	 * ServiceInfo and serviceInfo has familyTypes array with value  
	 * ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE.
	 * 
	 * @param accountInfo
	 * @param serviceInfo
	 * @throws InvalidServiceException
	 */
	void testServiceAddToBusinessAnywhereAccount(AccountInfo accountInfo, ServiceInfo serviceInfo) throws ApplicationException;

	/**
	 * Get Calling Circle Information
	 * @param billingAccountNumber
	 * @param subscriberNumber
	 * @param serviceCode
	 * @param featureCode
	 * @param productType
	 * @param sessionId
	 * @return CallingCircleParametersInfo
	 * @throws ApplicationException
	 */
	CallingCircleParametersInfo getCallingCircleInformation(int billingAccountNumber, String subscriberNumber, String serviceCode, String featureCode, String productType, String sessionId)
			throws ApplicationException;

	/**
	 * Get Base Service Agreement
	 * @param billingAccountNumber
	 * @param subscriberNumber
	 * @return SubscriberContractInfo
	 * @throws ApplicationException
	 */
	SubscriberContractInfo getBaseServiceAgreement(String subscriberNumber, int billingAccountNumber) throws ApplicationException;

	void reportChangeSubscriberStatus(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException, SystemException;

	void reportChangeSubscriberStatus(int banId, SubscriberInfo subscriber, String dealerCode, String salesRepCode, String userId, char oldSubscriberStatus, char newSubscriberStatus, String reason,
			Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException;

	void reportChangeEquipment(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, Equipment oldEquipment, Equipment newEquipment, String repairId, String swapType,
			Equipment oldAssociatedMuleEquipment, Equipment newAssociatedMuleEquipment, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangeContract(String subscriberId, int ban, ContractChangeInfo changeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId) throws ApplicationException;

	void reportChangeContract(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, SubscriberContractInfo newContractInfo, SubscriberContractInfo oldContractInfo,
			ContractService[] addedServices, ContractService[] removedServices, ContractService[] updatedServices, ContractFeature[] updatedFeatures,
			com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangePhoneNumber(int ban, PhoneNumberChangeInfo phoneNumberChangeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId) throws ApplicationException;

	void reportChangePhoneNumber(int banId, String subscriberId, String newSubscriberId, String dealerCode, String salesRepCode, String userId, String oldPhoneNumber, String newPhoneNumber,
			com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportMoveSubscriber(int oldBanId, int newBanId, String subscriberId, String dealerCode, String salesRepCode, String userId, String phoneNumber, char subscriberStatus,
			Date subscriberActivationDate, String reason, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangeAccountType(int banId, String dealerCode, String salesRepCode, String userId, char accountStatus, char oldAccountType, char newAccountType, char oldAccountSubType,
			char newAccountSubType, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangeAccountAddress(int banId, String dealerCode, String salesRepCode, String userId, Address address, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangeAccountPin(int banId, String dealerCode, String salesRepCode, String userId, com.telus.api.servicerequest.ServiceRequestHeader header);

	void reportChangePaymentMethod(int banId, String dealerCode, String salesRepCode, String userId, PaymentMethod paymentMethod, com.telus.api.servicerequest.ServiceRequestHeader header);

	/**
	 * Reports the Subscriber status changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeSubscriberStatus(ChangeSubscriberStatusActivity activity) throws ApplicationException;

	/**
	 * Reports the equipment changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeEquipment(ChangeEquipmentActivity activity) throws ApplicationException;

	/**
	 * Reports the contract changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeContract(ChangeContractActivity activity) throws ApplicationException;

	/**
	 * Reports the phone number changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangePhoneNumber(ChangePhoneNumberActivity activity) throws ApplicationException;

	/**
	 * Reports the subscriber move from BAN to BAN information to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportMoveSubscriber(MoveSubscriberActivity activity) throws ApplicationException;

	/**
	 * Reports the account type changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeAccountType(ChangeAccountTypeActivity activity) throws ApplicationException;

	/**
	 * Reports the account address changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeAccountAddress(ChangeAccountAddressActivity activity) throws ApplicationException;

	/**
	 * Reports the PIN number changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangeAccountPin(ChangeAccountPinActivity activity) throws ApplicationException;

	/**
	 * Reports the payment method changes to SRPDS system in asynchronous mode
	 * @param activity
	 */
	void asyncReportChangePaymentMethod(ChangePaymentMethodActivity activity) throws ApplicationException;

	void asyncLogChangePricePlan(PricePlanChangeInfo pricePlanChangeInfo) throws ApplicationException;

	void asyncLogChangeAddress(AddressChangeInfo addressChangeInfo) throws ApplicationException;

	void asyncLogSubscriberNewCharge(SubscriberChargeInfo subscriberChargeInfo) throws ApplicationException;

	void asyncLogChangePaymentMethod(PaymentMethodChangeInfo paymentMethodChangeInfo) throws ApplicationException;

	void asyncLogMakePayment(BillPaymentInfo billPaymentInfo) throws ApplicationException;

	void asyncLogAccountStatusChange(AccountStatusChangeInfo accountStatusChangeInfo) throws ApplicationException;

	void asyncLogPrepaidAccountTopUp(PrepaidTopupInfo prepaidTopupInfo) throws ApplicationException;

	void asyncLogChangePhoneNumber(com.telus.eas.config.info.PhoneNumberChangeInfo phoneNumberChangeInfo) throws ApplicationException;

	void asyncLogChangeService(ServiceChangeInfo serviceChangeInfo) throws ApplicationException;

	void asyncLogChangeSubscriber(SubscriberChangeInfo subscriberChangeInfo) throws ApplicationException;

	void asyncLogSubscriberChangeEquipment(EquipmentChangeInfo equipmentChangeInfo) throws ApplicationException;

	void asyncLogChangeRole(RoleChangeInfo roleChangeInfo) throws ApplicationException;

	SubscriberContractInfo getBaseServiceAgreement(String phoneNumber) throws ApplicationException;

	ContractChangeInfo getServiceAgreementForUpdate(String phoneNumber) throws ApplicationException;

	SubscriberContractInfo prepopulateCallingCircleList(ContractChangeInfo changeInfo) throws ApplicationException;

	SubscriberContractInfo evaluateCallingCircleCommitmentAttributeData(ContractChangeInfo changeInfo) throws ApplicationException;

	void deactivateMVNESubcriber(String phoneNumber) throws ApplicationException;

	boolean isPortActivity(String phoneNumber);

	List getAirTimeAllocations(SubscriberIdentifierInfo subscriberIdentifierInfo, Date effectiveDate, List serviceIdentityInfoList, String sessionId) throws ApplicationException;

	List getHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException;

	void closeHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId, boolean isAsync) throws ApplicationException;

	void closeHPAAccount(int billingAccountNumber, String phoneNumber, boolean isAsync) throws ApplicationException;

	// WAR - nextGen
	MigrationChangeInfo validateMigrateSubscriber(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException;

	MigrationChangeInfo migrateSubscriber(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException;

	// WAR - Apple activation
	ActivationChangeInfo validateActivateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException;

	ActivationChangeInfo activateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException;

	SubscriberInfo reservePhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;

	SubscriberInfo reserveOnHoldPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException;

	SubscriberInfo reservePortedInPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, PortInEligibilityInfo portInEligibilityInfo, String sessionId)
			throws ApplicationException;

	void unreservePhoneNumber(SubscriberInfo subscriberInfo, boolean cancelPortIn, PortInEligibilityInfo portInEligibilityInfo, String sessionId) throws ApplicationException;

	SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId, Boolean cancellationInd) throws ApplicationException;

	/**
	 * Validate migrating an existing Business Connect subscriber to/from another account.
	 * 
	 * This method migrates a Business Connect subscriber from the current account to another
	 * account and vice versa.
	 * 
	 * @param migrateSeatChangeInfo seat migration information including account, subscriber, contract, seat data, target BAN, etc.
	 * @param notificationSuppressionInd
	 * @param auditInfo audit info
	 * @param sessionId session ID
	 * @return MigrateSeatChangeInfo post-seat migration information including new account, new subscriber, new contract, etc.
	 *  
	 * @exception ApplicationException
	 */
	MigrateSeatChangeInfo validateMigrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Subscriber seat migration (see migrateSeatPricePlanChange).
	 * 
	 * This method initiates the seat migration process of a subscriber to/from a Business Connect account.
	 * 
	 * @param migrateSeatChangeInfo seat migration information including account, subscriber, contract, seat data, target BAN, etc.
	 * @param notificationSuppressionInd
	 * @param auditInfo audit info
	 * @param sessionId session ID
	 * @return MigrateSeatChangeInfo post-seat migration information including new account, new subscriber, new contract, etc.
	 *  
	 * @exception ApplicationException
	 */
	MigrateSeatChangeInfo migrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber VOIP resource numbers.
	 * 
	 * @param subscriberInfo subscriber information
	 * @param resourceList list of ResourceActivityInfo VOIP resources to change
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeVOIPResource(SubscriberInfo subscriberInfo, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException;

	/**
	 * Change subscriber VOIP resource numbers.
	 * 
	 * @param subscriptionId subscriber's subscription ID
	 * @param resourceList list of ResourceActivityInfo VOIP resources to change
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeVOIPResource(long subscriptionId, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException;

	/**
	 * Add or remove a subscriber VOIP number with recurring charge. Note, unlike the changeVOIPResource method, this call only supports a single activity,
	 * with an associated service to add or remove.
	 * 
	 * @param subscriberInfo subscriber information
	 * @param resourceActivityInfo the VOIP resource number to add or remove
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param serviceCode service code related to VOIP number addition or removal
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeVOIPResourceWithCharge(SubscriberInfo subscriberInfo, ResourceActivityInfo resourceActivityInfo, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException;

	/**
	 * Add or remove a subscriber VOIP number with recurring charge. Note, unlike the changeVOIPResource method, this call only supports a single activity,
	 * with an associated service to add or remove.
	 * 
	 * @param subscriptionId subscriber's subscription ID
	 * @param resourceActivityInfo the VOIP resource number to add or remove
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param serviceCode service code related to VOIP number addition or removal
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void changeVOIPResourceWithCharge(long subscriptionId, ResourceActivityInfo resourceActivityInfo, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException;

	/**
	 * Move a VOIP number from source to target subscriber on the same account.
	 * 
	 * @param subscriberInfo source subscriber information
	 * @param subscriberInfo target subscriber information
	 * @param resourceInfo the VOIP resource number to reassign
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void moveVOIPResource(SubscriberInfo source, SubscriberInfo target, Resource resourceInfo, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException;

	/**
	 * Move a VOIP number from source to target subscriber on the same account.
	 * 
	 * @param sourceSubscriptionId source subscriber's subscription ID
	 * @param targetSubscriptionId target subscriber's subscription ID
	 * @param resourceNumber the VOIP resource number to reassign
	 * @param activityDate activity date of the change
	 * @param outgoingRequestInd flag indicating if this request requires provisioning to the network - true or false
	 * @param sessionId session ID
	 * 
	 * @exception ApplicationException
	 */
	void moveVOIPResource(long sourceSubscriptionId, long targetSubscriptionId, String resourceNumber, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException;

	/**
	 * Method called to process Provisioning requests.
	 * 
	 * Note: this method first attempts a synchronous call to submit the provisioning order. In the event of failure, a second attempt is made using an asynchronous call.
	 * 
	 * @param provisioningRequestInfo
	 * @throws ApplicationException
	 */
	void asyncSubmitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException;

	/**
	 * Method called by MDB handler to process Provisioning requests.
	 * 
	 * @param provisioningRequestInfo
	 * @throws ApplicationException
	 */
	void submitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException;

	/**
	 * remove PPS service if necessary from subscribers under new account.
	 * 
	 * @param billing account number
	 * @param sessionId
	 * 
	 * @exception ApplicationException
	 */
	void validatePPSServicesWhenAccountTypeChanged(int banId, char oldAccountType, char oldAccountSubType, char newAccountType, char newAccountSubType, String sessionId) throws ApplicationException;
	
	SubscriberInfo updateSubscriberProfile(String billingAccountNumber, String subscriberId, Boolean prepaidInd, ConsumerNameInfo consumerNameInfo, AddressInfo addresInfo, String emailAddress,
			String language, String invoiceCallSortOrderCd, String subscriptionRoleCd, String sessionId) throws ApplicationException;

	/**
	 * Subscriber seat migration price plan change (see migrateSeat).
	 * 
	 * This method performs the price plan component of the subscriber seat migration process. Although exposed through the EJB interface, this method should not be called on its own. 
	 * 
	 * @param contractChangeInfo contract change information required for the price plan change
	 * @param notificationSuppressionInd
	 * @param auditInfo audit info
	 * @param sessionId session ID
	 *  
	 * @exception ApplicationException
	 */
	void migrateSeatChangePricePlan(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException;

	ActivationChangeInfo performPortInActivation(String sessionId, PortRequestInfo portRequest, ActivationChangeInfo activationChange, ServiceRequestHeaderInfo requestHeader, AuditInfo auditInfo,
			String portProcessCode, boolean portedIn) throws ApplicationException;

	DataSharingSocTransferInfo validateDataSharingBeforeCancelSubscriber(String subscriberId) throws ApplicationException;

	/**
	 * 
	 * @param subscriberInfo
	 * @param subscriberContractInfo
	 * @param newHandset
	 * @return returns VoLTE soc if eligible, otherwise empty
	 */
	ServiceInfo getVolteSocIfEligible(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, boolean pricePlanChange);

	/**
	 * This method is used to cancel the companions of a single communication suite
	 * @param ban
	 * @param primarySubscriberNum
	 * @param commSuiteInfo
	 * @param activityDate
	 * @param cancelReasonCode
	 * @param depositReturnMethod
	 * @param waiverReason
	 * @param memoText
	 * @param suppressNotification
	 * @param srpdsHeader
	 * @param sessionId
	 * @throws ApplicationException
	 */
	void cancelCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date activityDate, String cancelReasonCode, char depositReturnMethod,
			String waiverReason, String memoText, boolean suppressNotification, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException;

	void suspendCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date effectiveDate, String suspendReasonCode, String userMemoText,
			String sessionId) throws ApplicationException;

	void suspendPortedInSubscriber(int ban , String phoneNumber, String activityReasonCode, Date activityDate, String portOutInd,String sessionId) throws ApplicationException ;

	void removeFromCommunicationSuite(int ban, String companionPhoneNumber, CommunicationSuiteInfo communicationSuite, boolean silentFailure) throws ApplicationException;

	/** 
	* @param volteSoc
	* @param subscriberContractInfo
	* @return return true iff volte soc successfully added
	*/
	boolean addVolteService(ServiceInfo volteSoc, SubscriberContractInfo subscriberContractInfo);

	boolean addVOLTEServiceForNewEquipmentAndSave(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, String dealerCode, String salesRepCode,
			String sessionId, boolean pricePlanChange);

	void updateSapccAccountPurchaseAmount(SubscriberInfo subscriberInfo, double domesticAmount, double roamingAmount, String actionCode,
			String applicationId) throws ApplicationException;
	
	VOIPAccountInfo getVOIPAccountInfo(int ban) throws ApplicationException;
	
	void removeLicenses(int ban, String subscriptionId, List<String> switchCodes) throws ApplicationException;
	void addLicenses(int ban, String subscriptionId, List<String> switchCodes) throws ApplicationException;
    void asyncSubmitProvisioningLicenseOrder(ProvisioningLicenseInfo provisioningLicenseInfo) throws ApplicationException;
	
	void repairCommunicationSuite(CommunicationSuiteRepairData repairData, String sessionId) throws ApplicationException;

	void asyncRepairCommunicationSuiteDueToPhoneNumberChange(int ban, String newSubscriberId, String oldSubscriberId, String sessionId) throws ApplicationException;

	void asyncRepairCommunicationSuiteDueToSubscriberResumed(int ban, String subscriberId, String sessionId) throws ApplicationException;

}
