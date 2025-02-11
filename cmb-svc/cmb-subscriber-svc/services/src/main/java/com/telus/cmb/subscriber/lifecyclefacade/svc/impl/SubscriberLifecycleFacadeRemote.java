package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

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

/**
 * @author Anitha Duraisamy
 *
 */
public interface SubscriberLifecycleFacadeRemote extends EJBObject {

	String openSession(String userId, String password, String applicationId) throws ApplicationException, RemoteException;

	void assignTNResources(String phoneNumber, String networkType, String localIMSI, String remoteIMSI) throws ApplicationException, RemoteException;

	void changeIMSIs(String phoneNumber, String networkType, String newLocalIMSI, String newRemoteIMSI) throws ApplicationException, RemoteException;

	void changeTN(String oldPhoneNumber, String newPhoneNumber, String networkType) throws ApplicationException, RemoteException;

	void changeNetwork(String phoneNumber, String oldNetworkType, String newNetworkType, String localIMSI, String remoteIMSI, String usimId) throws ApplicationException, RemoteException;

	void releaseTNResources(String phoneNumber, String networkType) throws ApplicationException, RemoteException;

	String retrieveTNProvisionAttributes(String phoneNumber, String networkType) throws ApplicationException, RemoteException;

	void setIMSIStatus(String networkType, String localIMSI, String remoteIMSI, String status) throws ApplicationException, RemoteException;

	void setTNStatus(String phoneNumber, String networkType, String status) throws ApplicationException, RemoteException;

	
	/**
	 * Retrieve Provisioning Transaction Detail Info for Subscriber
	 *
	 * @param String
	 *            - subscriber number/Id
	 * @param String
	 *            - provisioning transaction number
	 * @return Array - ProvisioningTransactionDetailInfo[]
	 * @throws ApplicationException
	 *
	 */
	ProvisioningTransactionDetailInfo[] retrieveProvisioningTransactionDetails(String subscriberId, String transactionNo) throws ApplicationException, RemoteException;

	EquipmentChangeRequestInfo changeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException, RemoteException;

	EquipmentChangeRequestInfo changeEquipment(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException, RemoteException;

	SubscriberContractInfo getServiceAgreement(String subscriberId, int billingAccountNumber) throws ApplicationException, RemoteException;

	SubscriberContractInfo getServiceAgreement(SubscriberInfo subscriberInfo, AccountInfo accountInfo) throws ApplicationException, RemoteException;

	ContractChangeInfo validateServiceAgreement(ContractChangeInfo contractChangeInfo, String sessionId) throws ApplicationException, RemoteException;

	ContractChangeInfo getServiceAgreementForUpdate(String subscriberId, int billingAccountNumber) throws ApplicationException, RemoteException;

	SubscriberContractInfo getServiceAgreementForEquipmentChange(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest)
			throws ApplicationException, RemoteException;

	SubscriberContractInfo getServiceAgreementForEquipmentChange(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, SubscriberContractInfo contractInfo)
			throws ApplicationException, RemoteException;

	ContractChangeInfo saveServiceAgreement(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	EquipmentChangeRequestInfo swapEquipmentForPhoneNumberInSems(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest) throws ApplicationException, RemoteException;

	ArrayList browseAllMessages(String queueBeanId) throws RemoteException;

	ArrayList browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType) throws RemoteException;

	ArrayList browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType, String subType) throws RemoteException;

	void asyncInsertProductInstance(int billingAccountNumber, String subscriberId, String processType, String sessionId) throws RemoteException;

	void insertProductInstance(int billingAccountNumber, String subscriberId, String processType, String sessionId) throws ApplicationException, RemoteException;

	void asyncInsertProductInstance(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException ,RemoteException;
	
	void insertProductInstance(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException,RemoteException;
	
	void asyncUpdateProductInstance(int billingAccountNumber, String subscriberId, String phoneNumber, String processType, String sessionId) throws RemoteException;

	void asyncUpdateProductInstance(int billingAccountNumber, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws RemoteException;

	void updateProductInstance(int billingAccountNumber, String subscriberId, String phoneNumber, String processType, String sessionId) throws ApplicationException, RemoteException;

	void updateProductInstance(int billingAccountNumber, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType,
			String sessionId) throws ApplicationException, RemoteException;

	// void asyncManageProductParameters (int billingAccountNumber, String
	// subscriberId, String processType, String sessionId) throws
	// RemoteException;
	// void manageProductParameters (int billingAccountNumber, String
	// subscriberId, String processType, String sessionId) throws
	// ApplicationException, RemoteException;
	// void asyncManageProductResources (int billingAccountNumber, String
	// subscriberId, String processType, String sessionId) throws
	// RemoteException;
	// void manageProductResources (int billingAccountNumber, String
	// subscriberId, String processType, String sessionId) throws
	// ApplicationException, RemoteException;

	/**
	 * Following methods are based on those in SLCM BEGIN
	 */
	

	 void cancelPortedInSubscriber(int banNumber, String phoneNumber,String deactivationReason, Date activityDate, String portOutInd,boolean isBrandPort, String subscriberId,CommunicationSuiteInfo commSuiteInfo, 
			boolean suppressNotificationInd ,ServiceRequestHeader srpdsHeader,String sessionId) throws ApplicationException,RemoteException;

	void cancelSubscriber(SubscriberInfo pSubscriberInfo, java.util.Date pActivityDate, String pActivityReasonCode, String pDepositReturnMethod, String pWaiveReason, String pUserMemoText,
				boolean notificationSuppressionInd, AuditInfo auditInfo, CommunicationSuiteInfo  commSuiteInfo,ServiceRequestHeader header,String sessionId) throws ApplicationException, RemoteException;
		
	void suspendSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException, RemoteException;

	 void suspendPortedInSubscriber(int ban , String phoneNumber, String activityReasonCode, Date activityDate, String portOutInd,String sessionId) throws ApplicationException ,RemoteException;

	void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo, java.util.Date activityDate, String activityReasonCode, String userMemoText, boolean portIn, String sessionId)
			throws ApplicationException, RemoteException;

	void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId,
			String sessionId) throws ApplicationException, RemoteException;
	
	/*
	 * @deprecated
	 * 
	 */
	SubscriberLifecycleInfo resumeCancelledSubscriber(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, String sessionId)
			throws ApplicationException, SystemException, RemoteException;

	/*
	 * @deprecated
	 * 
	 */
	List resumeCancelledSubscriber(SubscriberInfo subscriberInfo, AccountInfo accountInfo, String salesRepCode, String dealerCode, String reasonCode, String memoText, String sessionId)
			throws ApplicationException, SystemException, RemoteException;
	
	
	void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId, SubscriberResumedPostTaskInfo taskInfo,
			String sessionId) throws ApplicationException, RemoteException;

	void changeEquipment(SubscriberInfo subscriberInfo, EquipmentInfo oldPrimaryEquipmentInfo, EquipmentInfo newPrimaryEquipmentInfo, EquipmentInfo[] newSecondaryEquipmentInfo, String dealerCode,
			String salesRepCode, String requesterId, String swapType, SubscriberContractInfo subscriberContractInfo, PricePlanValidationInfo pricePlanValidation, AuditInfo audidInfo,
			boolean notificationSuppressInd, SubscriberContractInfo oldContract, String sessionId) throws ApplicationException, RemoteException;

	void changeIP(int ban, String subscriberId, String newIp, String newIpType, String newIpCorpCode, String sessionId) throws ApplicationException, RemoteException;

	void changeFaxNumber(SubscriberInfo subscriber, String sessionId) throws ApplicationException, RemoteException;

	void changeFaxNumber(SubscriberInfo subscriber, AvailablePhoneNumberInfo newFaxNumber, String sessionId) throws ApplicationException, RemoteException;

	void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo, int newUrbanId, int newFleetId, String newMemberId, String sessionId) throws ApplicationException, RemoteException;

	void changePhoneNumber(SubscriberInfo subscriberInfo, AvailablePhoneNumberInfo newPhoneNumber, String reasonCode, String dealerCode, String salesRepCode, String sessionId)
			throws ApplicationException, RemoteException;

	void changePhoneNumberPortIn(SubscriberInfo subscriberInfo,AvailablePhoneNumberInfo newPhoneNumber, 
			String reasonCode,String dealerCode, String salesRepCode, String portProcessType,
			int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException ,RemoteException;
	
	void changeEsimEnabledDevice(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId) throws ApplicationException, RemoteException;
	
	void createSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, boolean activate, boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn, ServicesValidation srvValidation, String portProcessType, int oldBanId, String oldSubscriberId, String sessionId)
					throws ApplicationException, RemoteException;

	void migrateSubscriber(SubscriberInfo srcSubscriberInfo, SubscriberInfo newSubscriberInfo, Date activityDate, SubscriberContractInfo subscriberContractInfo,
			com.telus.eas.equipment.info.EquipmentInfo newPrimaryEquipmentInfo, com.telus.eas.equipment.info.EquipmentInfo[] newSecondaryEquipmentInfo, MigrationRequestInfo migrationRequestInfo,
			String sessionId) throws ApplicationException, RemoteException;

	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, boolean notificationSuppressionInd,
			AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	void changePricePlan(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			boolean notificationSuppressionInd, AuditInfo auditInfo, SubscriberContractInfo oldContractInfo, String sessionId) throws ApplicationException, RemoteException;

	void changeServiceAgreement(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	void releaseSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractinfo, EquipmentInfo equipmentInfo, String sessionId) throws ApplicationException, RemoteException;

	void releasePortedInSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractinfo, EquipmentInfo equipmentInfo, String sessionId)
			throws ApplicationException, RemoteException;

	void activateReservedSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, Date startServiceDate, String activityReasonCode, ServicesValidation srvValidation,
			String portProcessType, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException, RemoteException;

	void updateCommitment(SubscriberInfo pSubscriberInfo, CommitmentInfo pCommitmentInfo, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException, RemoteException;

	/**
	 * END of SLCM equivalents
	 */

	// PRM CALLS MIGRATION

	public PortInEligibility checkPortInEligibility(String phoneNumber, String portVisibility, int incomingBrand) throws ApplicationException, RemoteException;

	public PortRequestInfo[] getCurrentPortRequestsByPhoneNumber(String phoneNumber, int brandId) throws ApplicationException, RemoteException;

	public PortRequestInfo[] getCurrentPortRequestsByBan(int banNumber) throws ApplicationException, RemoteException;

	public PortRequestSummary checkPortRequestStatus(String phoneNumber, int brandId) throws ApplicationException, RemoteException;

	public void validatePortInRequest(PortRequestInfo portRequest, String applicationId, String user) throws ApplicationException, RemoteException;

	public void activatePortInRequest(PortRequestInfo portRequest, String applicationId) throws ApplicationException, RemoteException;

	public void cancelPortInRequest(String requestId, String reasonCode, String applicationId) throws ApplicationException, RemoteException;

	public String createPortInRequest(SubscriberInfo subscriber, String portProcessType, int incomingBrandId, int outgoingBrandId, String sourceNetwork, String targetNetwork, String applicationId,
			String user, PortRequestInfo portRequest) throws ApplicationException, RemoteException;

	public void submitPortInRequest(String requestId, String applicationId) throws ApplicationException, RemoteException;

	// THIS METHOD MAY NOT HAVE ANY CALLERS ???
	// Commented for WNP-WLI Upgrade 2012 Oct Release
	// public void modifyPortInRequest(SubscriberInfo subscriber, String
	// applicationId, String user,PortRequestInfo portReq) throws
	// ApplicationException, RemoteException;

	public PRMReferenceData[] retrieveReferenceData(String category) throws ApplicationException, RemoteException;

	/**
	 * This method checks to see if the Price Plan or Service to be added is
	 * compatible with the Account. If it is not compatible, a specific subclass
	 * of InvalidServiceException is thrown with a corresponding error reason
	 * code.
	 * 
	 * InvalidPricePlanChangeException is thrown with reason code
	 * InvalidPricePlanChangeException.ACCOUNT_TYPE_SUBTYPE_MISMATCH if
	 * serviceInfo is an instance of PricePlanInfo and serviceInfo has
	 * familyTypes array with value 'Y'
	 * ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE.
	 * 
	 * InvalidServiceChangeException is thrown with reason code
	 * InvalidServiceChangeException.UNAVAILABLE_SERVICE if serviceInfo is an
	 * instance of ServiceInfo and serviceInfo has familyTypes array with value
	 * ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE.
	 * 
	 * @param accountInfo
	 * @param serviceInfo
	 * @throws InvalidServiceException,
	 *             RemoteException
	 */
	void testServiceAddToBusinessAnywhereAccount(AccountInfo accountInfo, ServiceInfo serviceInfo) throws ApplicationException, RemoteException;

	/**
	 * Get Calling Circle Information
	 * 
	 * @param billingAccountNumber
	 * @param subscriberNumber
	 * @param serviceCode
	 * @param featureCode
	 * @param productType
	 * @param sessionId
	 * @return CallingCircleParametersInfo
	 * @throws ApplicationException
	 */
	public CallingCircleParametersInfo getCallingCircleInformation(int billingAccountNumber, String subscriberNumber, String serviceCode, String featureCode, String productType, String sessionId)
			throws ApplicationException, RemoteException;

	/**
	 * Get Base Service Agreement
	 * 
	 * @param billingAccountNumber
	 * @param subscriberNumber
	 * @return SubscriberContractInfo
	 * @throws ApplicationException
	 */
	public SubscriberContractInfo getBaseServiceAgreement(String subscriberNumber, int billingAccountNumber) throws ApplicationException, RemoteException;

	void reportChangeSubscriberStatus(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException, RemoteException;

	void reportChangeSubscriberStatus(int banId, SubscriberInfo subscriber, String dealerCode, String salesRepCode, String userId, char oldSubscriberStatus, char newSubscriberStatus, String reason,
			Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException, RemoteException;

	void reportChangeEquipment(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, Equipment oldEquipment, Equipment newEquipment, String repairId, String swapType,
			Equipment oldAssociatedMuleEquipment, Equipment newAssociatedMuleEquipment, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangeContract(String subscriberId, int ban, ContractChangeInfo changeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId)
			throws ApplicationException, RemoteException;

	void reportChangeContract(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, SubscriberContractInfo newContractInfo, SubscriberContractInfo oldContractInfo,
			ContractService[] addedServices, ContractService[] removedServices, ContractService[] updatedServices, ContractFeature[] updatedFeatures,
			com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangePhoneNumber(int ban, PhoneNumberChangeInfo phoneNumberChangeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId)
			throws ApplicationException, RemoteException;

	void reportChangePhoneNumber(int banId, String subscriberId, String newSubscriberId, String dealerCode, String salesRepCode, String userId, String oldPhoneNumber, String newPhoneNumber,
			com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportMoveSubscriber(int oldBanId, int newBanId, String subscriberId, String dealerCode, String salesRepCode, String userId, String phoneNumber, char subscriberStatus,
			Date subscriberActivationDate, String reason, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangeAccountType(int banId, String dealerCode, String salesRepCode, String userId, char accountStatus, char oldAccountType, char newAccountType, char oldAccountSubType,
			char newAccountSubType, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangeAccountAddress(int banId, String dealerCode, String salesRepCode, String userId, Address address, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangeAccountPin(int banId, String dealerCode, String salesRepCode, String userId, com.telus.api.servicerequest.ServiceRequestHeader header) throws RemoteException;

	void reportChangePaymentMethod(int banId, String dealerCode, String salesRepCode, String userId, PaymentMethod paymentMethod, com.telus.api.servicerequest.ServiceRequestHeader header)
			throws RemoteException;

	/**
	 * Reports the Subscriber status changes to SRPDS system in asynchronous
	 * mode
	 * 
	 * @param activity
	 * @param sessionId
	 */
	void asyncReportChangeSubscriberStatus(ChangeSubscriberStatusActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the equipment changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangeEquipment(ChangeEquipmentActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the contract changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangeContract(ChangeContractActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the phone number changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangePhoneNumber(ChangePhoneNumberActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the subscriber move from BAN to BAN information to SRPDS system
	 * in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportMoveSubscriber(MoveSubscriberActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the account type changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */

	void asyncReportChangeAccountType(ChangeAccountTypeActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the account address changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangeAccountAddress(ChangeAccountAddressActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the PIN number changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangeAccountPin(ChangeAccountPinActivity activity) throws ApplicationException, RemoteException;

	/**
	 * Reports the payment method changes to SRPDS system in asynchronous mode
	 * 
	 * @param activity
	 */
	void asyncReportChangePaymentMethod(ChangePaymentMethodActivity activity) throws ApplicationException, RemoteException;

	void asyncLogChangePricePlan(PricePlanChangeInfo pricePlanChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogChangeAddress(AddressChangeInfo addressChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogSubscriberNewCharge(SubscriberChargeInfo subscriberChargeInfo) throws ApplicationException, RemoteException;

	void asyncLogChangePaymentMethod(PaymentMethodChangeInfo paymentMethodChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogMakePayment(BillPaymentInfo billPaymentInfo) throws ApplicationException, RemoteException;

	void asyncLogAccountStatusChange(AccountStatusChangeInfo accountStatusChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogPrepaidAccountTopUp(PrepaidTopupInfo prepaidTopupInfo) throws ApplicationException, RemoteException;

	void asyncLogChangePhoneNumber(com.telus.eas.config.info.PhoneNumberChangeInfo phoneNumberChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogChangeService(ServiceChangeInfo serviceChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogChangeSubscriber(SubscriberChangeInfo subscriberChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogSubscriberChangeEquipment(EquipmentChangeInfo equipmentChangeInfo) throws ApplicationException, RemoteException;

	void asyncLogChangeRole(RoleChangeInfo roleChangeInfo) throws ApplicationException, RemoteException;

	SubscriberContractInfo getBaseServiceAgreement(String phoneNumber) throws ApplicationException, RemoteException;

	ContractChangeInfo getServiceAgreementForUpdate(String phoneNumber) throws ApplicationException, RemoteException;

	SubscriberContractInfo prepopulateCallingCircleList(ContractChangeInfo changeInfo) throws ApplicationException, RemoteException;

	SubscriberContractInfo evaluateCallingCircleCommitmentAttributeData(ContractChangeInfo changeInfo) throws ApplicationException, RemoteException;

	void deactivateMVNESubcriber(String phoneNumber) throws ApplicationException, RemoteException;

	boolean isPortActivity(String phoneNumber) throws RemoteException;

	List getAirTimeAllocations(SubscriberIdentifierInfo subscriberIdentifierInfo, Date effectiveDate, List serviceIdentityInfoList, String sessionId) throws ApplicationException, RemoteException;

	List getHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException, RemoteException;
	
	public void closeHPAAccount(int billingAccountNumber, String phoneNumber, boolean isAsync) throws ApplicationException, RemoteException;

	public void closeHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId, boolean isAsync) throws ApplicationException, RemoteException;

	// WAR - nextGen
	public MigrationChangeInfo validateMigrateSubscriber(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException, RemoteException;

	public MigrationChangeInfo migrateSubscriber(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException, RemoteException;

	// WAR - Apple activation
	public ActivationChangeInfo validateActivateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException, RemoteException;

	public ActivationChangeInfo activateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException, RemoteException;

	public SubscriberInfo reservePhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException, RemoteException;

	public SubscriberInfo reserveOnHoldPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException, RemoteException;

	public SubscriberInfo reservePortedInPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, PortInEligibilityInfo portInEligibilityInfo,
			String sessionId) throws ApplicationException, RemoteException;

	public void unreservePhoneNumber(SubscriberInfo subscriberInfo, boolean cancelPortIn, PortInEligibilityInfo portInEligibilityInfo, String sessionId) throws ApplicationException, RemoteException;

	SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId, Boolean cancellationInd) throws ApplicationException, RemoteException;

	void changeVOIPResource(long subscriptionId, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException, RemoteException;

	void changeVOIPResource(SubscriberInfo subscriberInfo, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException, RemoteException;

	void changeVOIPResourceWithCharge(long subscriptionId, ResourceActivityInfo resourceActivity, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException, RemoteException;

	void changeVOIPResourceWithCharge(SubscriberInfo subscriberInfo, ResourceActivityInfo resourceActivityInfo, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException, RemoteException;

	void moveVOIPResource(long sourceSubscriptionId, long targetSubscriptionId, String resourceNumber, Date activityDate, boolean outgoingRequestInd, String sessionId)
			throws ApplicationException, RemoteException;

	void moveVOIPResource(SubscriberInfo source, SubscriberInfo target, Resource resourceInfo, Date activityDate, boolean outgoingRequestInd, String sessionId)
			throws ApplicationException, RemoteException;

	void asyncSubmitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException, RemoteException;

	void submitProvisioningOrder(ProvisioningRequestInfo ProvisioningRequestInfo) throws ApplicationException, RemoteException;

	MigrateSeatChangeInfo validateMigrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	MigrateSeatChangeInfo migrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException, RemoteException;

	void validatePPSServicesWhenAccountTypeChanged(int banId, char oldAccountType, char oldAccountSubType, char newAccountType, char newAccountSubType, String sessionId)
			throws ApplicationException, RemoteException;

	SubscriberInfo updateSubscriberProfile(String billingAccountNumber, String subscriberId, Boolean prepaidInd, ConsumerNameInfo consumerNameInfo, AddressInfo addresInfo, String emailAddress,
			String language, String invoiceCallSortOrderCd, String subscriptionRoleCd, String sessionId) throws ApplicationException, RemoteException;

	void migrateSeatChangePricePlan(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException, RemoteException;

	ActivationChangeInfo performPortInActivation(String sessionId, PortRequestInfo portRequest, ActivationChangeInfo activationChange, ServiceRequestHeaderInfo requestHeader, AuditInfo auditInfo,
			String portProcessCode, boolean portedIn) throws ApplicationException, RemoteException;

	public DataSharingSocTransferInfo validateDataSharingBeforeCancelSubscriber(String subscriberId) throws ApplicationException, RemoteException;

	void cancelCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date activityDate, String cancelReasonCode, char depositReturnMethod,
			String waiverReason, String memoText, boolean suppressNotification, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException, RemoteException;

	void suspendCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date effectiveDate, String suspendReasonCode, String userMemoText,
			String sessionId) throws ApplicationException, RemoteException;

	void removeFromCommunicationSuite(int ban, String companionPhoneNumber, CommunicationSuiteInfo communicationSuite, boolean silentFailure) throws ApplicationException, RemoteException;

	ServiceInfo getVolteSocIfEligible(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, boolean pricePlanChange) throws RemoteException;

	boolean addVolteService(ServiceInfo volteSoc, SubscriberContractInfo subscriberContractInfo) throws RemoteException;

	boolean addVOLTEServiceForNewEquipmentAndSave(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, String dealerCode, String salesRepCode,
			String sessionId, boolean pricePlanChange) throws RemoteException;

	void updateSapccAccountPurchaseAmount(SubscriberInfo subscriberInfo, double domesticAmount, double roamingAmount, String actionCode, String applicationId) throws ApplicationException,
			RemoteException;

	VOIPAccountInfo getVOIPAccountInfo(int ban) throws ApplicationException, RemoteException;

	void removeLicenses(int ban, String subscriptionId, List<String> switchCodes) throws ApplicationException, RemoteException;
	
	void addLicenses(int ban, String subscriptionId, List<String> switchCodes) throws ApplicationException,RemoteException;
    
    void asyncSubmitProvisioningLicenseOrder(ProvisioningLicenseInfo provisioningLicenseInfo) throws ApplicationException,RemoteException ;
	
	void repairCommunicationSuite(CommunicationSuiteRepairData repairData, String sessionId) throws ApplicationException, RemoteException;

	void asyncRepairCommunicationSuiteDueToPhoneNumberChange(int ban, String newSubscriberId, String oldSubscriberId, String sessionId) throws ApplicationException, RemoteException;

	void asyncRepairCommunicationSuiteDueToSubscriberResumed(int ban, String subscriberId, String sessionId) throws ApplicationException, RemoteException;
}
