package com.telus.cmb.subscriber.lifecyclefacade.svc.impl;

import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.CreateException;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.jms.Message;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.InvalidServiceChangeException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.TelusAPIException;
import com.telus.api.UnknownObjectException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.Address;
import com.telus.api.account.AvailablePhoneNumber;
import com.telus.api.account.CallingCircleParameters;
import com.telus.api.account.ContractFeature;
import com.telus.api.account.ContractService;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.ServicesValidation;
import com.telus.api.account.Subscriber;
import com.telus.api.equipment.CellularDigitalEquipment;
import com.telus.api.equipment.CellularEquipment;
import com.telus.api.equipment.Equipment;
import com.telus.api.equipment.EquipmentSubscriber;
import com.telus.api.portability.InterBrandPortRequestException;
import com.telus.api.portability.PRMReferenceData;
import com.telus.api.portability.PortInEligibility;
import com.telus.api.portability.PortRequestSummary;
import com.telus.api.reference.AccountType;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Feature;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.Province;
import com.telus.api.reference.RatedFeature;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.Service;
import com.telus.api.reference.ServiceSummary;
import com.telus.api.resource.Resource;
import com.telus.api.resource.ResourceActivity;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.api.util.ClientApiUtils;
import com.telus.api.util.RemoteBeanProxyFactory;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.dao.provisioning.VOIPLicensePurchaseServiceDao;
import com.telus.cmb.common.dao.provisioning.VOIPSupplementaryServiceDao;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceDao;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.common.ejb.AbstractLifecycleFacade;
import com.telus.cmb.common.ejb.LdapTestPoint;
import com.telus.cmb.common.eligibility.CallingCircleEligibilityCheckCriteria;
import com.telus.cmb.common.eligibility.CallingCircleEligibilityEvaluationStrategy;
import com.telus.cmb.common.eligibility.CommunicationSuiteEligibilityUtil;
import com.telus.cmb.common.eligibility.EligibilityUtilities;
import com.telus.cmb.common.eligibility.rules.CallingCircleEligibilityEvaluationResult;
import com.telus.cmb.common.enums.BillNotificationActivityType;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.JmsQueueSupport;
import com.telus.cmb.common.jms.JmsSupport;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileRegistrationOrigin;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileService;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.util.StringUtil;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.reference.svc.ApplicationMessageFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.bo.BusinessConnectBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.SapccUpdateAccountPurchaseBo;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.cmb.subscriber.domain.HpaRewardAccountInfo;
import com.telus.cmb.subscriber.kafka.ChangePhoneNumberPublisher;
import com.telus.cmb.subscriber.kafka.ServiceAgreementEventPublisher;
import com.telus.cmb.subscriber.kafka.SubscriberEventPublisher;
import com.telus.cmb.subscriber.lifecyclefacade.dao.ActivatePortInRequestServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CancelPortInRequestDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CommSuiteMgmtRestSvcDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CommunicationSuiteMgmtSvcDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.CreatePortInRequestDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.DeactivateMVNESubscriberRequestDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.EligibilityCheckRequestDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.HardwarePurchaseAccountServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.LogicalResourceServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.MinMdnServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.OcssamServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PenaltyCalculationServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PortRequestInformationDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.PortabilityServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.ProductDataMgmtDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.RedkneeSubscriptionMgmtServiceDao;
import com.telus.cmb.subscriber.lifecyclefacade.dao.SubmitPortInRequestDao;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.cmb.subscriber.utilities.ActivatePortinContext;
import com.telus.cmb.subscriber.utilities.ActivationChangeContext;
import com.telus.cmb.subscriber.utilities.AppConfiguration;
import com.telus.cmb.subscriber.utilities.AsyncSubscriberCommitContext;
import com.telus.cmb.subscriber.utilities.BaseChangeContext;
import com.telus.cmb.subscriber.utilities.BusinessConnectContext;
import com.telus.cmb.subscriber.utilities.BusinessConnectContext.BusinessConnectContextTypeEnum;
import com.telus.cmb.subscriber.utilities.ChangeContextFactory;
import com.telus.cmb.subscriber.utilities.ContractChangeContext;
import com.telus.cmb.subscriber.utilities.DataSharingUtil;
import com.telus.cmb.subscriber.utilities.EquipmentChangeContext;
import com.telus.cmb.subscriber.utilities.FeatureTransactionContext;
import com.telus.cmb.subscriber.utilities.MigrateSeatChangeContext;
import com.telus.cmb.subscriber.utilities.MigrationChangeContext;
import com.telus.cmb.subscriber.utilities.SapccUpdateAccountPurchaseContext;
import com.telus.cmb.subscriber.utilities.activation.ActivationPostTask;
import com.telus.cmb.subscriber.utilities.activation.CallingCircleUtilities;
import com.telus.cmb.subscriber.utilities.contract.ContractUtilities;
import com.telus.cmb.subscriber.utilities.port.PortoutHelper;
import com.telus.cmb.subscriber.utilities.port.PortoutUtilities;
import com.telus.cmb.subscriber.utilities.transition.BrandTransitionMatrix;
import com.telus.cmb.subscriber.utilities.transition.EquipmentTransitionMatrix;
import com.telus.cmb.subscriber.utilities.transition.ValidationResult;
import com.telus.cmb.utility.activitylogging.svc.ActivityLoggingService;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager;
import com.telus.cmb.utility.dealermanager.svc.DealerManager;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AvailablePhoneNumberInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.MigrationRequestInfo;
import com.telus.eas.account.info.PhoneNumberReservationInfo;
import com.telus.eas.account.info.PricePlanValidationInfo;
import com.telus.eas.account.info.ServiceIdentityInfo;
import com.telus.eas.account.info.SubscriberDataSharingDetailInfo;
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
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.InteractionInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentSubscriberInfo;
import com.telus.eas.equipment.info.ProfileInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.exception.WarningFaultInfo;
import com.telus.eas.framework.info.Info;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.message.info.ApplicationMessageInfo;
import com.telus.eas.portability.info.LocalServiceProviderInfo;
import com.telus.eas.portability.info.PortInEligibilityInfo;
import com.telus.eas.portability.info.PortRequestInfo;
import com.telus.eas.servicerequest.info.ServiceRequestHeaderInfo;
import com.telus.eas.subscriber.info.ActivationChangeInfo;
import com.telus.eas.subscriber.info.BaseAgreementInfo;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.CallingCircleParametersInfo;
import com.telus.eas.subscriber.info.CommitmentInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.ContractChangeInfo;
import com.telus.eas.subscriber.info.DataSharingSocTransferInfo;
import com.telus.eas.subscriber.info.EquipmentChangeRequestInfo;
import com.telus.eas.subscriber.info.FeatureChangeInfo;
import com.telus.eas.subscriber.info.FeatureParameterHistoryInfo;
import com.telus.eas.subscriber.info.IDENSubscriberInfo;
import com.telus.eas.subscriber.info.LightWeightSubscriberInfo;
import com.telus.eas.subscriber.info.MigrateSeatChangeInfo;
import com.telus.eas.subscriber.info.MigrationChangeInfo;
import com.telus.eas.subscriber.info.PhoneNumberChangeInfo;
import com.telus.eas.subscriber.info.PrepaidServicePropertyInfo;
import com.telus.eas.subscriber.info.PrepaidSubscriberInfo;
import com.telus.eas.subscriber.info.PricePlanChangeInfo;
import com.telus.eas.subscriber.info.ProvisioningTransactionDetailInfo;
import com.telus.eas.subscriber.info.ResourceActivityInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceChangeInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriberLifecycleInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCResultInfo;
import com.telus.eas.subscriber.info.SubscriptionMSCSpendingInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.eas.task.info.SubscriberResumedPostTaskInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.FeatureAirTimeAllocationInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.ProvisioningLicenseInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ReferenceInfo;
import com.telus.eas.utility.info.ServiceAirTimeAllocationInfo;
import com.telus.eas.utility.info.ServiceFeatureClassificationInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.eas.utility.info.ServicePeriodInfo;
import com.telus.framework.config.ConfigContext;
import com.telus.provisioning.bssAdapter.common.ObjectNotFoundException;
import com.telus.provisioning.bssAdapter.common.OrderLineItemStatus;
import com.telus.provisioning.bssAdapter.ejb.ProvisioningOrderLookupRemote;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceFaultInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;
import com.telusmobility.rcm.common.ejb.MinMdnService;
import com.telusmobility.rcm.common.exception.RcmException;

@Stateless(name = "SubscriberLifecycleFacade", mappedName = "SubscriberLifecycleFacade")
@Interceptors(SpringBeanAutowiringInterceptor.class)
@Remote({ SubscriberLifecycleFacade.class, SubscriberLifecycleFacadeTestPoint.class, LdapTestPoint.class })
@RemoteHome(SubscriberLifecycleFacadeHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class SubscriberLifecycleFacadeImpl extends AbstractLifecycleFacade implements SubscriberLifecycleFacade, SubscriberLifecycleFacadeTestPoint {

	private static final Log logger = LogFactory.getLog(SubscriberLifecycleFacadeImpl.class);
	
	public static final String TRANSACTION_TYPE_CHANGE_PRICE_PLAN = "P";
	public static final String TRANSACTION_TYPE_CHANGE_SERVICE = "S";
	public static final String TRANSACTION_TYPE_CHANGE_PHONE_NUMBER = "N";
	public static final String TRANSACTION_TYPE_CHANGE_EQUIPMENT = "E";
	public static final String TRANSACTION_TYPE_CHANGE_SUBSCRIBER = "R";

	public static final String TRANSACTION_TYPE_CHANGE_ADDRESS = "A";
	public static final String TRANSACTION_TYPE_CHARGE_SUBSCRIBER = "C";
	public static final String TRANSACTION_TYPE_CHANGE_PAYMENT_METHOD = "M";
	public static final String TRANSACTION_TYPE_MAKE_PAYMENT = "Y";
	public static final String TRANSACTION_TYPE_CHANGE_STATUS = "B";
	public static final String TRANSACTION_TYPE_CHANGE_ACCOUNT_STATUS = TRANSACTION_TYPE_CHANGE_STATUS;
	public static final String TRANSACTION_TYPE_PREPAID_TOPUP = "T";
	public static final String TRANSACTION_TYPE_CHANGE_ROLE = "O";
	
	public static final char STATUS_CHANGE_FLAG_ACCOUNT = 'A';
	public static final char STATUS_CHANGE_FLAG_SUBSCRIBER = 'S';

	public static final String DEFAULT_APPLICATION_NAME = "Client API";

	public static final String RCM_SERVICE_EJB_HOME = "rcm.ejb.session.minmdnservice";

	private static final String TELUS_EJBHOME_PROVISIONING_ORDER_LOOKUP = "OrderLookupBean";

	private static final String locale = "English";
	//private static final String RELEASE_FROM_RESERVED_ACTIVATION = "RRA";

	private static final String POSTPAID = "O";
	private static final String PREPAID = "R";
	private static final String APPLICATION = "ClientAPI";
	private static final String DUMMY_REPAIR_ID = "DUMMY0";
	private static final String[] SUBSCRIBER_VOIP_RESOURCE_TYPES = { Subscriber.RESOURCE_TYPE_PRIMARY_VOIP, Subscriber.RESOURCE_TYPE_ADDITIONAL_VOIP, Subscriber.RESOURCE_TYPE_TOLLFREE_VOIP };
	private static final String WPS_PROFILE_STATUS_ACTIVE = "ACTIVE";
	
	private ProvisioningOrderLookupRemote provisioningOrderLookup;

	private MinMdnService minMdnService;

	@Autowired
	private ApplicationContext applicationContext;
	
	@EJB
	private SubscriberLifecycleManager subscriberLifeCycleManager;

	@EJB
	private SubscriberLifecycleHelper subscriberLifecycleHelper;

	@Autowired
	private AmdocsSessionManager amdocsSessionManager;

	@Autowired
	private JmsSupport queueSender;

	@Autowired
	private BrandTransitionMatrix brandTransitionMatrix;

	@Autowired
	private EquipmentTransitionMatrix equipmentTransitionMatrix;

	@Autowired
	private EJBController ejbController;

	@Autowired
	private ProductDataMgmtDao productDataMgmtDao;

	@Autowired
	private SubmitPortInRequestDao submitPortInRequestDao;

	@Autowired
	private CancelPortInRequestDao cancelPortInRequestDao;

	@Autowired
	private CreatePortInRequestDao createPortInRequestDao;
	
	@Autowired
	private EligibilityCheckRequestDao eligibilityCheckRequestDao;

	@Autowired
	private DeactivateMVNESubscriberRequestDao deactivateMVNESubscriberRequestDao;
	
	@Autowired
	private RedkneeSubscriptionMgmtServiceDao redkneeSubscriptionMgmtServiceDao;

	@Autowired
	private LogicalResourceServiceDao logicalResourceServiceDao;

	@Autowired
	private MinMdnServiceDao minMdnServiceDao;
	
	@Autowired
	private PortabilityServiceDao portabilityServiceDao;

	@Autowired
	private ActivatePortInRequestServiceDao actvPortInRequestDao;
	
	@Autowired
	private HardwarePurchaseAccountServiceDao hardwarePurchaseAccountServiceDao;
	
	@Autowired
	private PortRequestInformationDao portRequestInformationDao;
	
	@Autowired
	private WirelessProvisioningServiceDao wirelessProvisioningServiceDao;

	@Autowired
	private VOIPSupplementaryServiceDao voipSupplementaryServiceDao;

	@Autowired
	private PenaltyCalculationServiceDao penaltyCalculationServiceDao;

	@Autowired
	private IdentityProfileService identityProfileService;
	
	@Autowired
	private CommunicationSuiteMgmtSvcDao communicationSuiteMgmtSvcDao;

	@Autowired
	private OcssamServiceDao ocssamServiceDao;
	
	@Autowired
	private VOIPLicensePurchaseServiceDao voipLicensePurchaseServiceDao;
	
	@Autowired
	private SubscriberEventPublisher subscriberEventPublisher;
	
	@Autowired
	private ServiceAgreementEventPublisher serviceAgreementEventPublisher;
	
	@Autowired
	CommSuiteMgmtRestSvcDao commSuiteMgmtRestSvcDao;
	
	@Autowired
	private ChangePhoneNumberPublisher changePhoneNumberPublisher;
	
	public void setIdentityProfileService(IdentityProfileService identityProfileService) {
		this.identityProfileService = identityProfileService;
	}
	
	public void setActivatePortRequestDao(ActivatePortInRequestServiceDao actvPortInRequestDao) {
		this.actvPortInRequestDao = actvPortInRequestDao;
	}

	public void setProductDataMgmtDao(ProductDataMgmtDao productDataMgmtDao) {
		this.productDataMgmtDao = productDataMgmtDao;
	}
	
	@PostConstruct
	public void initialize() {
		initializeEjbController();
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(userId, password, applicationId);
		return amdocsSessionManager.openSession(identity);
	}

	private void handleException(Throwable throwable) throws ApplicationException {
		
		ApplicationException exception = null;

		if (throwable instanceof PolicyException_v1) {
			PolicyFaultInfo pe = ((PolicyException_v1) throwable).getFaultInfo();
			logger.debug("PolicyException : " + pe.getErrorCode() + " : " + pe.getErrorMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, pe.getErrorCode(), "PolicyException : " + pe.getErrorCode() + " : " + pe.getErrorMessage(), "", throwable);
		} else if (throwable instanceof ServiceException_v1) {
			ServiceFaultInfo se = ((ServiceException_v1) throwable).getFaultInfo();
			logger.error("ServiceException : " + se.getErrorCode() + " : " + se.getErrorMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, se.getErrorCode(), "ServiceException : " + se.getErrorCode() + " : " + se.getErrorMessage(), "", throwable);
		} else if (throwable instanceof ServiceException_v3) {
			FaultExceptionDetailsType se = ((ServiceException_v3) throwable).getFaultInfo();
			logger.error("ServiceException : " + se.getErrorCode() + " : " + se.getErrorMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, se.getErrorCode(), "ServiceException : " + se.getErrorCode() + " : " + se.getErrorMessage(), "", throwable);
		} else if (throwable instanceof SOAPFaultException) {
			SOAPFaultException e = (SOAPFaultException) throwable;
			logger.error("SOAPFaultException : " + e.getFault().getFaultString());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getFault().getFaultCode(), "SOAPFaultException : " + e.getFault().getFaultCode() + " : " + e.getFault().getFaultString(),
					"", e);
		} else if (throwable instanceof RcmException) {
			RcmException e = (RcmException) throwable;
			logger.error("RcmException : " + e.getMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getErrorCode(), "RcmException : " + e.getErrorCode() + " : " + e.getMessage(), "", e);
		} else if (throwable instanceof RemoteException) {
			RemoteException e = (RemoteException) throwable;
			logger.error("RemoteException : " + e.getMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, "RemoteException : " + e.getMessage(), "", e);
		} else if (throwable instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) throwable;
			logger.debug("ApplicationException : " + ae.getMessage());
			exception = ae;
		} else {
			Exception e = (Exception) throwable;
			logger.error("Exception : " + e.getMessage());
			exception = new ApplicationException(SystemCodes.CMB_SLF_EJB, "Exception : " + e.getMessage(), "", e);
		}

		throw exception;
	}

	private WarningFaultInfo swallowException(Throwable t) {
		logger.warn(t);
		if (t instanceof TelusAPIException) {
			TelusAPIException e = (TelusAPIException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, SystemCodes.CMB_SLF_EJB, null, null, e.getStackTrace0(), null);
			return warning;
		} else if (t instanceof ApplicationException) {
			ApplicationException e = (ApplicationException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.APPLICATION_EXCEPTION, SystemCodes.CMB_SLF_EJB, null, e.getErrorCode(), e.getStackTraceAsString(), null);
			return warning;
		} else if (t instanceof SystemException) {
			SystemException e = (SystemException) t;
			WarningFaultInfo warning = new WarningFaultInfo(WarningFaultInfo.SYSTEM_EXCEPTION, SystemCodes.CMB_SLF_EJB, null, e.getErrorCode(), e.getStackTraceAsString(), null);
			return warning;
		}

		return null;
	}

	/**
	 * The WS support is available for HSPA only
	 * 
	 * @param phoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param localIMSI		Required for HSPA only.
	 * @param remoteIMSI	Required for HSPA only, not mandatory.
	 * @param uiccid		Required for HSPA only.
	 * @throws ApplicationException
	 */
	@Override
	public void assignTNResources(String phoneNumber, String networkType, String localIMSI, String remoteIMSI) throws ApplicationException {
		
		logger.info("ASSIGN TN [phoneNumber: " + phoneNumber + ", NetworkType:" + networkType + "]");
		try {
			if (!networkType.equalsIgnoreCase(NetworkType.NETWORK_TYPE_HSPA)) {
				getMinMdnService().assignMinForSubscriber(phoneNumber, APPLICATION);
			}
		} catch (Throwable throwable) {
			handleException(throwable);
		}
	}

	/**
	 * The WS support is available for HSPA only
	 * 
	 * @param oldPhoneNumber	Mandatory, 10 digits numeric.
	 * @param newPhoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType		One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @throws ApplicationException
	 */
	@Override
	public void changeTN(String oldPhoneNumber, String newPhoneNumber, String networkType) throws ApplicationException {
		
		logger.info("CHANGE TN [oldPhoneNumber: " + oldPhoneNumber + ", newPhoneNumber:" + newPhoneNumber + ", networkType:" + networkType + "]");
		try {
			if (!networkType.equals(NetworkType.NETWORK_TYPE_HSPA)) {				
				getMinMdnService().changeMdn(oldPhoneNumber, newPhoneNumber, APPLICATION);
			}
		} catch (Throwable throwable) {
			handleException(throwable);
		}
	}
	
	/**
	 * 
	 * @param phoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @throws ApplicationException
	 */
	@Override
	public void releaseTNResources(String phoneNumber, String networkType) throws ApplicationException {
		
		logger.info("RELEASE TN [phoneNumber: " + phoneNumber + ", networkType:" + networkType + "]");
		try {
			if (!networkType.equals(NetworkType.NETWORK_TYPE_HSPA)) {
				// call to RCM EJB releaseMin() method
				getMinMdnService().releaseMin(phoneNumber, APPLICATION);
			}
		} catch (Throwable throwable) {
			handleException(throwable);
		}
	}

	/**
	 * The WS support is available for HSPA only
	 * 
	 * @param phoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @return A GetTNProvisioningAttributesResponse object.
	 * @throws ApplicationException
	 */

	@Override
	public String retrieveTNProvisionAttributes(String phoneNumber, String networkType) throws ApplicationException {
		
		logger.debug("RETRIEVE TN ATTRS [phoneNumber: " + phoneNumber + ", networkType:" + networkType + "]");
		String min = "";
		try {
			min = minMdnServiceDao.retrieveMin(phoneNumber, networkType, APPLICATION);
		} catch (Throwable throwable) {
			handleException(throwable);
		}
		
		return min;
	}

	/**
	 * 
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param localIMSI		Mandatory.
	 * @param remoteIMSI	Not Mandatory.
	 * @param status		Mandatory . Valid values are AS or AI.
	 * @throws ApplicationException
	 */

	@Override
	public void setIMSIStatus(String networkType, String localIMSI, String remoteIMSI, String status) throws ApplicationException {
		// call to RCM WS setIMSIStatus() operation
		logger.debug("SET IMSI STATUS [status:" + status + ", networkType:" + networkType + ", localIMSI:" + localIMSI + ", remoteIMSI:" + remoteIMSI + "]");
		logicalResourceServiceDao.setIMSIStatus(networkType, localIMSI, remoteIMSI, status);
	}

	private MinMdnService getMinMdnService() {
		
		try {
			if (minMdnService == null) {
				minMdnService = (MinMdnService) RemoteBeanProxyFactory.createProxy(MinMdnService.class, RCM_SERVICE_EJB_HOME, AppConfiguration.getMinMdnServiceUrl());
			}
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
		
		return minMdnService;
	}


	private ProvisioningOrderLookupRemote getProvisioningOrderLookup() throws CreateException {

		if (provisioningOrderLookup == null) {
			provisioningOrderLookup = EJBUtil.getStatelessProxy(ProvisioningOrderLookupRemote.class, TELUS_EJBHOME_PROVISIONING_ORDER_LOOKUP, AppConfiguration.getProvisioningOrderBeanUrl());
		}
		
		return provisioningOrderLookup;
	}

	@Override
	public ProvisioningTransactionDetailInfo[] retrieveProvisioningTransactionDetails(String subscriberId, String transactionNo) throws ApplicationException {
		
		ProvisioningTransactionDetailInfo[] provisioningTransactionDetails = null;
		try {
			// Call the Provisioning OrderLookUp bean.
			ProvisioningOrderLookupRemote provOrderLookup = getProvisioningOrderLookup();
			logger.debug("Calling getOrderLineItemStatus for key=[" + transactionNo + "]...");
			List list = provOrderLookup.getOrderLineItemStatus(transactionNo, subscriberId);
			logger.debug("# of statuses found:" + list.size());

			// Map status to ProvisioningTransactionDetailInfo object.
			provisioningTransactionDetails = new ProvisioningTransactionDetailInfo[list.size()];
			for (int i = 0; i < list.size(); i++) {
				OrderLineItemStatus status = (OrderLineItemStatus) list.get(i);
				logger.debug(" getErrReason=[" + status.getErrReason() + "]");
				logger.debug(" getExternalOrderId=[" + status.getExternalOrderId() + "]");
				logger.debug(" getLastUpdateDate=[" + status.getLastUpdateDate() + "]");
				logger.debug(" getServiceName=[" + status.getServiceName() + "]");
				logger.debug(" getStatusCode=[" + status.getStatusCode() + "]");

				provisioningTransactionDetails[i] = new ProvisioningTransactionDetailInfo();
				provisioningTransactionDetails[i].setErrorReason(status.getErrReason());
				provisioningTransactionDetails[i].setEffectiveDate(status.getLastUpdateDate());
				provisioningTransactionDetails[i].setService(status.getServiceName());
				provisioningTransactionDetails[i].setStatus(status.getStatusCode());
			}
			
			return provisioningTransactionDetails;

		} catch (ObjectNotFoundException onfe) {
			logger.error("Provisioning ObjectNotFoundException occurred - no status records found", onfe);
			return provisioningTransactionDetails = new ProvisioningTransactionDetailInfo[0];
		} catch (UnmarshalException ue) {
			logger.error("Provisioning UnmarshalException occurred - unable to retrieve status records", ue);
			return provisioningTransactionDetails = new ProvisioningTransactionDetailInfo[0];
		} catch (com.telus.provisioning.bssAdapter.common.ConnectionException ce) {
			logger.error("Provisioning Connection Exception occurred", ce);
			provisioningOrderLookup = null;
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ce.getMessage() == null ? "Provisioning Connection Exception occurred (" + ce.toString() + ") - see stack trace for details"
					: ce.getMessage(), "", ce);
		} catch (Throwable t) {
			logger.error(" Throwable occurred", t);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, t.getMessage() == null ? "Throwable occurred (" + t.toString() + ") - see stack trace for details" : t.getMessage(), "", t);
		}
	}

	/**
	 * @deprecated Entry point from SubscriberLifecycleManagementService with no usage
	 */
	@Override
	public SubscriberLifecycleInfo resumeCancelledSubscriber(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, String sessionId)
			throws ApplicationException {
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);
		subscriberLifecycleInfo.setOldSubscriberInfo(subscriberInfo);
		AccountInfo accountInfo = getAccountInformationHelper().retrieveAccountByBan(billingAccountNumber, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		List<Exception> systemExceptionList = resumeCancelledSubscriber(subscriberInfo, accountInfo, subscriberLifecycleInfo.getSalesRepCode(), subscriberLifecycleInfo.getDealerCode(),
				subscriberLifecycleInfo.getReasonCode(), subscriberLifecycleInfo.getMemoText(), sessionId);
		for (Exception e : systemExceptionList) {
			subscriberLifecycleInfo.addSystemWarning(swallowException(e));
		}
		return subscriberLifecycleInfo;
	}

	/**
	 * @deprecated Entry point from SubscriberLifecycleManagementService with no usage
	 */
	@Override
	public List<Exception> resumeCancelledSubscriber(SubscriberInfo subscriberInfo, AccountInfo accountInfo, String salesRepCode, String dealerCode, String reasonCode, String memoText,
			String sessionId) throws ApplicationException {
		List<Exception> exceptionList = new ArrayList<Exception>();
		boolean oldAccountHotlinedStatus = accountInfo.getFinancialHistory().isHotlined();
		char oldSubscriberStatus = subscriberInfo.getStatus();

		subscriberLifeCycleManager.resumeCancelledSubscriber(subscriberInfo, reasonCode, memoText, false, PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT, 0, null,
				getSubscriberLifecycleManagerSessionId(sessionId));

		EquipmentInfo equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber());
		if (equipmentInfo.isHSPA()) {
			try {
				getProductEquipmentLifecycleFacade().assignEquipmentToPhoneNumber(subscriberInfo.getPhoneNumber(), equipmentInfo.getSerialNumber(), equipmentInfo.getAssociatedHandsetIMEI());
			} catch (ApplicationException ae) {
				exceptionList.add(ae);
			} catch (SystemException se) {
				exceptionList.add(se);
			}
		}
		
		/**
		 * Refresh subscriber
		 */
		SubscriberInfo newSubscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
		AccountInfo newAccountInfo = getAccountInformationHelper().retrieveAccountByBan(accountInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);

		boolean statusChanged = (oldSubscriberStatus != newSubscriberInfo.getStatus());
		if (statusChanged) {
			InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_STATUS, dealerCode, salesRepCode, newSubscriberInfo.getBanId(), newSubscriberInfo.getSubscriberId(),
					getClientIdentity(sessionId));

			getConfigurationManager().report_accountStatusChange(report.getId(), report.getDatetime(), booleanToChar(oldAccountHotlinedStatus),
					booleanToChar(newAccountInfo.getFinancialHistory().isHotlined()), oldSubscriberStatus, subscriberInfo.getStatus(), STATUS_CHANGE_FLAG_SUBSCRIBER);
		}
		return exceptionList;
	}

	@Override
	public EquipmentChangeRequestInfo changeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId) throws ApplicationException {
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);
		AccountInfo accountInfo = getAccountInformationHelper().retrieveAccountByBan(billingAccountNumber, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		return changeEquipment(subscriberInfo, accountInfo, equipmentChangeRequest, sessionId);
	}

	@Override
	public EquipmentChangeRequestInfo changeEquipment(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException {
		equipmentChangeRequest = updateEquipmentChangeRequest(subscriberInfo, equipmentChangeRequest);
		return changeEquipment0(subscriberInfo, accountInfo, equipmentChangeRequest, sessionId);
	}

	/**
	 * IDEN logic has been removed
	 * 
	 * @param subscriberInfo
	 * @param accountInfo
	 * @param equipmentChangeRequest
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 */
	private EquipmentChangeRequestInfo changeEquipment0(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException {

		EquipmentInfo oldPrimaryEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();
		EquipmentInfo newPrimaryEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		EquipmentInfo currentAssociatedHandset = (EquipmentInfo) equipmentChangeRequest.getCurrentAssociatedHandset();
		EquipmentInfo newAssociatedHandset = (EquipmentInfo) equipmentChangeRequest.getAssociatedHandset();
		EquipmentInfo newAssociatedMule = (EquipmentInfo) equipmentChangeRequest.getAssociatedMuleEquipment();

		boolean hspaHandsetOnly = newPrimaryEquipmentInfo.isUSIMCard() && oldPrimaryEquipmentInfo.getSerialNumber().equals(newPrimaryEquipmentInfo.getSerialNumber());

		if (!hspaHandsetOnly) {
			equipmentChangeRequest = validateChangeEquipment(accountInfo, subscriberInfo, equipmentChangeRequest, sessionId);
		}

		if (((oldPrimaryEquipmentInfo.isSIMCard() && currentAssociatedHandset == null) || newAssociatedMule == null) && !hspaHandsetOnly) {
			PricePlanValidationInfo ppValidationInfo = new PricePlanValidationInfo();
			changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, (EquipmentInfo[]) equipmentChangeRequest.getSecondaryEquipments(),
					equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), equipmentChangeRequest.getRequestorId(), equipmentChangeRequest.getSwapType(), null,
					ppValidationInfo, null, true, null, getSubscriberLifecycleManagerSessionId(sessionId));
		}

		if (newPrimaryEquipmentInfo.isStolen()) {
			reportEquipmentFound(subscriberInfo, equipmentChangeRequest, newPrimaryEquipmentInfo, sessionId);
		}

		ContractChangeInfo contractChangeInfo = new ContractChangeInfo();
		contractChangeInfo.setCurrentAccountInfo(accountInfo);
		contractChangeInfo.setCurrentSubscriberInfo(subscriberInfo);
		contractChangeInfo.setBan(accountInfo.getBanId());
		contractChangeInfo.setSubscriberId(subscriberInfo.getSubscriberId());
		ContractChangeContext changeContext = getContractChangeContext(contractChangeInfo, sessionId);
		ContractBo contract = changeContext.getNewContract();

		if (newPrimaryEquipmentInfo.isUSIMCard()) {
			if (hspaHandsetOnly) { //no actual changeEquipment in KB was called and so VOLTE logic didn't kick in with the actual equipment change call
				ServiceInfo volte = getVolteSocIfEligible(subscriberInfo, contract.getDelegate(), newAssociatedHandset, false);
				addVolteService(volte, contract.getDelegate());
			}else {
				// adjust contract base on new equipment. should be obsolete since there should be no more inter-network (i.e. HSPA->CDMA) change
				try {
					// make sure the subscriber on the contract is fresh
					// and new equipment is on the subscriber already at this point
					// Different Equipment Types: Add and/or remove services to/from
					// the contract as necessary
					addRemoveServices(contract, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, true);
					String[] KBDealer = getKBDealer(equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), accountInfo);
					contract.save(KBDealer[0], KBDealer[1]);
				} catch (Throwable t) {
					equipmentChangeRequest.addSystemWarning(swallowException(t));
				}
			}

			//Update SEMS
			equipmentChangeRequest = swapEquipmentForPhoneNumberInSems(subscriberInfo, equipmentChangeRequest);

			try {
				InteractionInfo report = newReport(TRANSACTION_TYPE_CHANGE_EQUIPMENT, equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), accountInfo.getBanId(),
						subscriberInfo.getSubscriberId(), changeContext.getClientIdentity());

				getConfigurationManager().report_subscriberChangeEquipment(report.getId(), report.getDatetime(), subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo,
						equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), equipmentChangeRequest.getRequestorId(), equipmentChangeRequest.getRepairId(),
						equipmentChangeRequest.getSwapType(), newAssociatedMule, changeContext.getClientIdentity().getApplication());
			} catch (Throwable t) {
				logger.error("Error occured in changeEquipment0 for ban=" + accountInfo.getBanId() + ",subscriberId=" + subscriberInfo.getSubscriberId() + ": " + t);
			}

			if (oldPrimaryEquipmentInfo.isCDMA() && newPrimaryEquipmentInfo.isHSPA()) {
				if (equipmentChangeRequest.isInvokeAPNFix()) {
					logger.debug("[" + subscriberInfo.getPhoneNumber() + "] changeHSPAEquimpent: switching from CDMA to HSPA. Refreshing SOC and Features.");
					contract.refreshSocAndFeatures();
				} else {
					logger.debug("[" + subscriberInfo.getPhoneNumber() + "] changeHSPAEquimpent: switching from CDMA to HSPA but invokeAPNFixForHSPA=false");
				}
			}
		} else if (newPrimaryEquipmentInfo.isCellular()
				&& !((oldPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || oldPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG))
						&& newPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) && equipmentChangeRequest.preserveDigitalServices())) {
			// for cellular RIM
			removeNonMatchingServices(new EquipmentBo(newPrimaryEquipmentInfo, changeContext), contract);
		}

		if (!((oldPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || oldPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG))
				&& newPrimaryEquipmentInfo.getEquipmentType().equals(Equipment.EQUIPMENT_TYPE_ANALOG) && equipmentChangeRequest.preserveDigitalServices())) {
			removeDispatchOnlyConflicts(contract);
		}

		// 6. Save the contract
		try {
			String[] KBDealer = getKBDealer(equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), accountInfo);
			contract.save(KBDealer[0], KBDealer[1]);
		} catch (Throwable t) {
			//			 logFailure(methodName, activity, t, "contract.save(): SubscriberManagerEJB().changeServiceAgreement() failed");
		}
		
		return equipmentChangeRequest;
	}

	private EquipmentChangeRequestInfo validateChangeEquipment(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException {
		EquipmentInfo newEquipment = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		//Holborn R2
		if (newEquipment.isUSIMCard()) {
			if (newEquipment.isVirtual()) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_TYPE_VIRTUAL, "USIMCard sn(" + newEquipment.getSerialNumber() + ") is virtual.", "");
			}
			if (newEquipment.isExpired()) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_SERIAL_NUMBER_EXPIRED, "USIMCard sn(" + newEquipment.getSerialNumber() + ") is expired.", "");
			}
			if (newEquipment.isPreviouslyActivated()) {
				String imsi = newEquipment.getProfile().getLocalIMSI();
				String lastAssociatedSubId = subscriberLifecycleHelper.retrieveLastAssociatedSubscriptionId(imsi);
				if (subscriberInfo.getSubscriptionId() != Long.valueOf(lastAssociatedSubId).longValue()) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_SERIAL_NUMBER_IN_USE, "USIMCard sn(" + newEquipment.getSerialNumber()
							+ ") is previously activated with subscription id: " + lastAssociatedSubId, "");
				}
			}
			// fix  for PROD00141498
			if (newEquipment.isStolen() && !newEquipment.isPreviouslyActivated()) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_SERIAL_NUMBER_STOLEN, "The new equipment is lost or stolen.", "");
			}
		}

		validateEquipmentSwapRules(subscriberInfo, equipmentChangeRequest, sessionId);
		ApplicationMessageInfo[] messages = getEquipmentSwapWarningMessages(accountInfo, subscriberInfo, equipmentChangeRequest, sessionId);
		equipmentChangeRequest.addApplicationMessageList(messages);
		
		return equipmentChangeRequest;
	}

	private void validateEquipmentSwapRules(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId) throws ApplicationException {
		EquipmentInfo newEquipment = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		EquipmentInfo currentEquipment = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();

		String requestorId = equipmentChangeRequest.getRequestorId();
		String swapType = equipmentChangeRequest.getSwapType();
		String repairId = equipmentChangeRequest.getRepairId();
		ClientIdentity ci = getClientIdentity(sessionId);

		// validate general input parameters
		String newSerialNumber = newEquipment != null ? newEquipment.getSerialNumber() : null;

		if (swapType == null || swapType.trim().length() < 1 || newSerialNumber == null || newSerialNumber.trim().length() < 1) {

			StringBuffer sb = new StringBuffer();

			sb.append("Mandatory field(s) are missing or invalid. <br>[dealerCode=");
			sb.append(", swapType=");
			sb.append(swapType);
			sb.append(", newSerialNumber=");
			sb.append(newSerialNumber);
			sb.append("]");

			String exceptionMsg = sb.toString();

			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_MANDATORY_FIELDS_MISSING, exceptionMsg, exceptionMsg);
		}

		// check if the new equipment type is null
		if (newEquipment.getEquipmentType() == null) {
			String exceptionMsg = "The newEquipmentType is NULL - Data Problem. [newSerialNumber=" + newSerialNumber + ", newEquipmentType=" + newEquipment.getEquipmentType() + "]";

			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_EQUIPMENT_TYPE_IS_NULL, exceptionMsg, exceptionMsg);
		}

		// check if the new equipment's product type is the same as the
		// subscriber's one
		if (newEquipment.getProductType() == null || !newEquipment.getProductType().equals(subscriberInfo.getProductType())) {
			String exceptionMsg = "The new equipment's ProductType, [" + newEquipment.getProductType() + "], is different from the subscriber's one, [" + subscriberInfo.getProductType() + "].";

			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_IMPOSSIBLE_SWAP_TYPES, exceptionMsg, exceptionMsg);
		}

		if (checkEquipmentInUse(subscriberInfo.getBanId(), newEquipment, equipmentChangeRequest.getAllowDuplicateSerialNumber())) {
			String exceptionMsg = "The new serial number is in use. [newSerialNumber = " + newSerialNumber + "]";

			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_SERIAL_NUMBER_IN_USE, exceptionMsg, exceptionMsg);
		}

		// determine swap type
		boolean repair = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPAIR);
		boolean replacement = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_REPLACEMENT);
		boolean loaner = swapType.trim().toUpperCase().equals(Equipment.SWAP_TYPE_LOANER);

		// Fix for PROD00131124 begin
		// !!!NOTE, Mar 4,2009 M.Liao:
		// As business still does not have clear picture of how to handle the repair situation for: CDMA - HSPA, HSPA - CDMA, HSPA - HSPA
		// we are putting temporary work-around: skip the repair validation at all for any above swap
		// As there are multiple validation logics regarding repair, so I take a extreme shortcut: by setting repair flag to false.
		// Once business determine the validation rule, the following work-around shall be taken away.
		if (currentEquipment.isHSPA() || newEquipment.isHSPA()) {
			repair = false;
		}
		// Fix for PROD00131124 end

		// validate technology and product class compatibility
		checkTechnologyAndProductClassCompatibility(subscriberInfo, equipmentChangeRequest, sessionId);

		// ========================================================================
		// validation: if equipment is lost/stolen and not previously active on
		// the same account - do not allow
		// ========================================================================
		if (!newEquipment.isUSIMCard()) { // fix for PROD00141498
			if (newEquipment.isStolen() && isEquipmentInUseOnAnotherBan(newEquipment.getSerialNumber(), subscriberInfo.getBanId(), false)) {
				String exceptionMsg = "The new equipment is lost or stolen.";
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_NEW_EQUIPMENT_IS_LOST_STOLEN, exceptionMsg, exceptionMsg);
			}
		}

		// Equipment swaps for prepaid subscribers, delegate to CellularEquipment.isValidForPrepaid() to check Equipment prepaid eligibilty
		AccountInfo accountInfo = getAccountInformationHelper().retrieveAccountByBan(subscriberInfo.getBanId(), Account.ACCOUNT_LOAD_ALL);
		if (accountInfo.isPrepaidConsumer()) {
			boolean equipmentValidForPrepaid = false;
			if (newEquipment instanceof CellularEquipment) {
				equipmentValidForPrepaid = ((CellularEquipment) newEquipment).isValidForPrepaid();
			}
			if (equipmentValidForPrepaid == false) {
				String exceptionMsg = "Forbidden equipment swap for prepaid account.";
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_INVALID_SWAP_FOR_PREPAID_ACCOUNT, exceptionMsg, exceptionMsg);
			}
		}

		// validate mandatory equipment fields
		if (currentEquipment.getTechType() == null || (repair && currentEquipment.getProductTypeDescription() == null) || newEquipment.getTechType() == null
				|| (repair && newEquipment.getProductTypeDescription() == null)) {

			StringBuffer sb = new StringBuffer();

			sb.append("Data problem - mandatory equipment field(s) are null.<br>");
			sb.append("[<old equipment> techType=");
			sb.append(currentEquipment.getTechType());
			sb.append(", productTypeDescription=");
			sb.append(currentEquipment.getProductTypeDescription());
			sb.append("]<br>[<new equipment> techType=");
			sb.append(newEquipment.getTechType());
			sb.append(", productTypeDescription=");
			sb.append(newEquipment.getProductTypeDescription());
			sb.append("]");

			String exceptionMsg = sb.toString();
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_MANDATORY_EQUIPMENT_INFO_NULL, exceptionMsg, exceptionMsg);
		}

		// set mandatory equipment fields to default values if null
		if (currentEquipment.getProductCode() == null) {
			currentEquipment.setProductCode("");
		}
		if (currentEquipment.getProductStatusCode() == null) {
			currentEquipment.setProductStatusCode("");
		}
		if (currentEquipment.getProductClassCode() == null) {
			currentEquipment.setProductClassCode("");
		}
		if (currentEquipment.getProductGroupTypeCode() == null) {
			currentEquipment.setProductGroupTypeCode("");

		}
		if (newEquipment.getProductCode() == null) {
			newEquipment.setProductCode("");
		}
		if (newEquipment.getProductStatusCode() == null) {
			newEquipment.setProductStatusCode("");
		}
		if (newEquipment.getProductClassCode() == null) {
			newEquipment.setProductClassCode("");
		}
		if (newEquipment.getProductGroupTypeCode() == null) {
			newEquipment.setProductGroupTypeCode("");

			// determine swap category
		}
//		boolean handToSim = currentEquipment.isHandset() && newEquipment.isSIMCard();
//		boolean handToMule = currentEquipment.isHandset() && newEquipment.isMule();
		boolean simToSim = currentEquipment.isSIMCard() && newEquipment.isSIMCard();
//		boolean simToMule = currentEquipment.isSIMCard() && newEquipment.isMule();
//		boolean simToHand = currentEquipment.isSIMCard() && newEquipment.isHandset();
//		boolean muleToSim = currentEquipment.isMule() && newEquipment.isSIMCard();
//		boolean muleToHand = currentEquipment.isMule() && newEquipment.isHandset();

		// check for invalid combinations
		if (simToSim && (loaner || repair)) {
			String exceptionMsg = "No 'loaner repair or repair' for sim-to-sim.";
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_NO_LOANER_FOR_SIM2SIM, exceptionMsg, exceptionMsg);
		}

		if (!replacement && !simToSim && (repairId == null || repairId.trim().length() == 0 || repairId.trim().length() > 10 || isLetterOrDigit(repairId) == false)) {
			String exceptionMsg = "Valid repair ID is mandatory except for 'replacement' or sim-to-sim.";
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_REPAIR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT_AND_SIM2SIM, exceptionMsg, exceptionMsg);
		}

		if (!replacement && (requestorId == null || requestorId.trim().length() < 1)) {
			String exceptionMsg = "Valid requestorId is mandatory except for 'replacement'";
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_REQUESTOR_ID_IS_MANDATORY_EXCEPT_REPLACEMENT, exceptionMsg, exceptionMsg);
		}

		// repair Id must be unique for 'repair' except for sim to sim.
		// Loaner Repair IDs should not be unique as the dealer will often
		// provide the same repair ID as used for the repair.
		if (repair && !simToSim) {
			if (!repairId.equals(DUMMY_REPAIR_ID)) { // except for repair swaps
				// performed by clients

				int numOfRepairIdFound = 0;

				numOfRepairIdFound = subscriberLifecycleHelper.getCountForRepairID(repairId.trim());

				if (numOfRepairIdFound > 0) {
					String exceptionMsg = "Repair ID must be unique for 'repair' except for sim to sim.";
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_REPAIR_ID_NOT_UNIQUE_EXCEPT_SIM2SIM, exceptionMsg, exceptionMsg);
				}
			}
		}

		// old & new product types must be the same for 'repair'
		if (repair && !currentEquipment.getProductTypeDescription().equals(newEquipment.getProductTypeDescription())) {
			String exceptionMsg = "Old/new product type must be the same for 'repair'. [old=" + currentEquipment.getProductTypeDescription() + " new=" + newEquipment.getProductTypeDescription() + "]";
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_OLD_NEW_EQUIPMENT_TYPE_NOT_SAME_FOR_REPAIR, exceptionMsg, exceptionMsg);
		}

		// Added by Roman. Walmart Equipment swaps error message: if newEquipment
		// is not available for activation, client should return it to Wal-Mart
		if (newEquipment.isUnscanned()) {
			String exceptionMsg = getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), 89).getText(Locale.ENGLISH);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_UNKNOWN, exceptionMsg, exceptionMsg);
		}
	}

	/**
	 * Reference TMSubscriber:checkNonMuleEquipmentInUse()
	 * @param equipmentChangeRequest
	 * @return
	 * @throws ApplicationException
	 */
	private boolean checkEquipmentInUse(int banId, EquipmentInfo equipment, char allowDuplicateSerialNo) throws ApplicationException {
		boolean inUseCheckResult = false;

		if (allowDuplicateSerialNo == Subscriber.SWAP_DUPLICATESERIALNO_ALLOWOTHERBAN) {
			//duplicate serial no is allowed in any ban, so bypass checking
		} else if (allowDuplicateSerialNo == Subscriber.SWAP_DUPLICATESERIALNO_ALLOWSAMEBAN) {
			//duplicate serial no is only allow in same ban
			if (equipment.isInUse() //in use
					&& equipment.isMule() == false //equipment is NOT mule
					&& isEquipmentInUseOnAnotherBan(equipment.getSerialNumber(), banId, true) // used in other ban
					) {
				inUseCheckResult = true;
			}
		} else { //( allowDuplicateSerialNo==SWAP_DUPLICATESERIALNO_DONOTALLOW)
			//duplicate serial no is NOT allowed at all
			if (equipment.isInUse() //in use
					&& equipment.isMule() == false //equipment is NOT mule
					) {
				inUseCheckResult = true;
			}
		}
		return inUseCheckResult;
	}

	/**
	 * M2M: Unlike the original TMSubscriber.checkTechnologyAndProductClassCompatibility, this method does not support IDEN subscribers.
	 * @param account
	 * @param subscriber
	 * @param equipmentChangeRequest
	 * @throws ApplicationException
	 */
	private void checkTechnologyAndProductClassCompatibility(SubscriberInfo subscriber, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId) throws ApplicationException {
		
		String errorMessage = "Incompatible technology or product class. ";
		ValidationResult validationResult;
		EquipmentInfo newEquipment = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		EquipmentInfo oldEquipment = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();
		String applicationId = getClientIdentity(sessionId).getApplication();

		// brand swap validation
		int oldBrandId;

		try {
			oldBrandId = getReferenceDataFacade().getPricePlan(subscriber.getPricePlan()).getBrandId();
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
		}
		validationResult = brandTransitionMatrix.validTransition(oldBrandId, newEquipment, equipmentChangeRequest.getSwapType(), applicationId, false);

		if (validationResult != ValidationResult.VALID) {
			ApplicationMessageInfo appMsg = getApplicationMessageFacade().getApplicationMessage("0", equipmentChangeRequest.getAudienceType(), oldBrandId, validationResult.getMessageId());
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_INCOMPATIBLE_BRAND, errorMessage + appMsg.getText(Locale.ENGLISH), errorMessage
					+ appMsg.getText(Locale.FRENCH));
		}

		// technology swap validation
		validationResult = equipmentTransitionMatrix.validTransition(oldEquipment, newEquipment, equipmentChangeRequest.getSwapType(), applicationId);

		if (validationResult != ValidationResult.VALID) {
			ApplicationMessageInfo appMsg = getApplicationMessageFacade().getApplicationMessage("0", equipmentChangeRequest.getAudienceType(), oldBrandId, validationResult.getMessageId());
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.EQUIPMENT_CHANGE_INCOMPATIBLE_TECH_TYPE, errorMessage + appMsg.getText(Locale.ENGLISH), errorMessage
					+ appMsg.getText(Locale.FRENCH));
		}

	}

	private EquipmentChangeRequestInfo reportEquipmentFound(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, EquipmentInfo equipmentInfo, String sessionId)
			throws ApplicationException {
		
		getProductEquipmentLifecycleFacade().markEquipmentFound(equipmentInfo.getSerialNumber(), equipmentInfo.getEquipmentGroup());
		EquipmentSubscriber[] associatedSubscribers;
		List<String> imsi = new ArrayList<String>();
		try {
			if (equipmentInfo.isUSIMCard()) {
				ProfileInfo profInfo = equipmentInfo.getProfile();
				String localIMSI = profInfo.getLocalIMSI();
				String remoteIMSI = profInfo.getRemoteIMSI();
				imsi.add(localIMSI);
				imsi.add(remoteIMSI);
				setIMSIStatus(equipmentInfo.getNetworkType(), localIMSI, remoteIMSI, "AI");
			}
		} catch (Throwable e) {
			equipmentChangeRequest.addSystemWarning(swallowException(e));
		}

		equipmentInfo.setEquipmentStatusTypeID(Equipment.STATUS_TYPE_REPORT_BY_CLIENT);
		equipmentInfo.setEquipmentStatusID(Equipment.STATUS_REPORT_BY_CLIENT_FOUND);
		if (imsi.isEmpty()) {
			associatedSubscribers = subscriberLifecycleHelper.retrieveNonHSPAEquipmentSubscribers(equipmentInfo.getSerialNumber(), true);
		} else {
			associatedSubscribers = subscriberLifecycleHelper.retrieveHSPAEquipmentSubscribers(equipmentInfo.getSerialNumber(), true, imsi.toArray(new String[imsi.size()]));
		}
	
		for (int i = 0; i < associatedSubscribers.length; i++) {
			int ban = associatedSubscribers[i].getBanId();
			String subscriberId = associatedSubscribers[i].getSubscriberId();
			String productType = associatedSubscribers[i].getProductType();
			subscriberLifeCycleManager.refreshSwitch(ban, subscriberId, productType, getSubscriberLifecycleManagerSessionId(sessionId));
		}

		return equipmentChangeRequest;
	}

	@Override
	public EquipmentChangeRequestInfo swapEquipmentForPhoneNumberInSems(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest) throws ApplicationException {
		
		EquipmentInfo oldPrimaryEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();
		EquipmentInfo newPrimaryEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		String phoneNumber = subscriberInfo.getPhoneNumber();
		String oldUsimId = oldPrimaryEquipmentInfo.getSerialNumber();
		String oldAssociatedHandsetIMEI = oldPrimaryEquipmentInfo.getAssociatedHandsetIMEI();
		String oldNetworkType = null;

		try {
			oldNetworkType = oldPrimaryEquipmentInfo.getNetworkType();
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_NETWORK_TYPE, e.getMessage(), "", e);
		}
		String newUsimId = newPrimaryEquipmentInfo.getSerialNumber();
		String newNetworkType = null;

		try {
			newNetworkType = newPrimaryEquipmentInfo.getNetworkType();
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_NETWORK_TYPE, e.getMessage(), "", e);
		}
		String newAssociatedHandsetIMEI = newPrimaryEquipmentInfo.getAssociatedHandsetIMEI();
		
		if (newAssociatedHandsetIMEI == null && equipmentChangeRequest.getNewAssociatedHandsetSerialNumber() != null) {
			newAssociatedHandsetIMEI = equipmentChangeRequest.getNewAssociatedHandsetSerialNumber();
		}

		if (phoneNumber == null || phoneNumber.trim().equals("") || oldUsimId == null || oldUsimId.trim().equals("") || newUsimId == null || newUsimId.trim().equals("") || oldNetworkType == null
				|| oldNetworkType.trim().equals("") || newNetworkType == null || newNetworkType.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Missing or invalid input parameter !", "");
		}

		try {
			getProductEquipmentLifecycleFacade().swapEquipmentForPhoneNumber(phoneNumber, oldUsimId, oldAssociatedHandsetIMEI, oldNetworkType, newUsimId, newAssociatedHandsetIMEI, newNetworkType);
		} catch (Throwable t) {
			if (t instanceof ApplicationException && ErrorCodes.UNSUPPORTED_NETWORK_SWAP.equals(((ApplicationException) t).getErrorCode())) {
				throw (ApplicationException) t;
			} else {
				equipmentChangeRequest.addSystemWarning(swallowException(t));
			}
		}

		return equipmentChangeRequest;
	}

	private CPMSDealerInfo getCPMSDealerInfo(String dealerCode, String salesRepCode) throws ApplicationException {
		
		String channelOrgCode = dealerCode;
		String userCode = salesRepCode;
		int[] brandIds = null;

		try {
			// check if it's KB dealer Code
			getReferenceDataFacade().getDealerSalesRep(dealerCode, salesRepCode);
			try {
				CPMSDealerInfo cpmsDealer = getDealerManagerSvc().getCPMSDealerByKBDealerCode(dealerCode, salesRepCode);
				channelOrgCode = cpmsDealer.getChannelCode();
				userCode = cpmsDealer.getUserCode();
				brandIds = cpmsDealer.getBrandIds();
			} catch (ApplicationException e) {
				if ("VAL10017".equals(e.getErrorCode())) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getErrorCode(), "Unknown Dealer Code and Sales Rep Code " + dealerCode + salesRepCode, "", e);
				}
			}
		} catch (TelusException e) {
			// not KB dealer
			logger.warn("Not a KB dealer - retrieving CPMS dealer info using dealer code and sales rep code");
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage() == null ? "Not a KB dealer - retrieving CPMS dealer info using dealer code and sales rep code"
					: e.getMessage(), "", e);
		}
		CPMSDealerInfo cpmsDealerInfo = getDealerManagerSvc().getCPMSDealerInfo(channelOrgCode, userCode);
		cpmsDealerInfo.setBrandIds(brandIds);

		return cpmsDealerInfo;
	}

	@Override
	public SubscriberContractInfo getServiceAgreement(String subscriberId, int billingAccountNumber) throws ApplicationException {
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(billingAccountNumber);
		return getServiceAgreement(subscriberInfo, accountInfo);
	}
	
	private SubscriberContractInfo getServiceAgreement(SubscriberInfo subscriberInfo) throws ApplicationException {
		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		} else if (subscriberInfo.getBanId() == 0) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.BAN_NOT_FOUND, "Missing BAN in SubscriberInfo.", "");
		}
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		return getServiceAgreement(subscriberInfo, accountInfo);
	}
	
	/**
	 * Implementation based on TMSubscriber:getContract0()
	 */
	@Override
	public SubscriberContractInfo getServiceAgreement(SubscriberInfo subscriberInfo, AccountInfo accountInfo) throws ApplicationException {
		if (subscriberInfo == null || accountInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Subscriber or Account cannot be null.", "");
		}

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}

		//		SubscriberContractInfo info = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber(subscriberInfo.getPhoneNumber());
		SubscriberContractInfo info = subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(subscriberInfo.getSubscriberId());
		info.setCommitmentReasonCode(subscriberInfo.getCommitment().getReasonCode());
		info.setCommitmentMonths(subscriberInfo.getCommitment().getMonths());
		info.setCommitmentStartDate(subscriberInfo.getCommitment().getStartDate());
		info.setCommitmentEndDate(subscriberInfo.getCommitment().getEndDate());

		EquipmentInfo currentEquipmentInfo = subscriberInfo.getEquipment0();
		if (currentEquipmentInfo == null) {
			currentEquipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber());
		}

		PricePlanInfo pricePlanInfo = null;
		try {
			pricePlanInfo = getReferenceDataFacade().getPricePlan(subscriberInfo.getProductType(), subscriberInfo.getPricePlan(),
					ContractUtilities.translateEquipmentType(currentEquipmentInfo, getProductEquipmentHelper()),
					subscriberInfo.getMarketProvince() != null ? subscriberInfo.getMarketProvince() : accountInfo.getHomeProvince(), String.valueOf(accountInfo.getAccountType()),
							String.valueOf(accountInfo.getAccountSubType()),
							ContractUtilities.getBrandId(subscriberInfo, accountInfo, getReferenceDataFacade().getBrands(), getReferenceDataFacade().getPricePlan(subscriberInfo.getPricePlan())));
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
		}
		info.setPricePlanInfo(pricePlanInfo);

		// Retrieve WPS services for prepaid account.
		//------------------------------------------------------------------------------
		if (!accountInfo.isPostpaid()) {
			ServiceAgreementInfo[] wpsServices = subscriberLifecycleHelper.retrieveFeaturesForPrepaidSubscriber(subscriberInfo.getPhoneNumber());

			int wpsServicesNo = wpsServices != null ? wpsServices.length : 0;

			for (int i = 0; i < wpsServicesNo; i++) {
				ServiceAgreementInfo sa = wpsServices[i];
				sa.setTransaction(BaseAgreementInfo.NO_CHG);
				info.addService(sa);

				ServiceInfo wpsServiceInfo;
				try {
					wpsServiceInfo = getReferenceDataFacade().getWPSService(sa.getServiceCode());
				} catch (TelusException e) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
				}
				if (wpsServiceInfo == null) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Couldn't find Prepaid service for code: [" + sa.getServiceCode() + "]", "");
				}

				/* The corresponding  call to Prepaid API ,getPromotionBucketDetail is Deprecated.Hence commenting out.
				 * if (ServiceSummary.WPS_SERVICE_TYPE_PROMOTIONAL.equals(wpsServiceInfo.getWPSServiceType())) {
					PrepaidPromotionDetailInfo prepaidPromotionDetailInfo = subscriberLifecycleHelper.retrievePrepaidSubscriberPromotion(subscriberInfo.getPhoneNumber(),
							Integer.parseInt(wpsServiceInfo.getCode().trim()));

					sa.setPrepaidPromotionDetail(prepaidPromotionDetailInfo);
				}*/
			}
		}
		info.getCommitment().setModified(false);
		if (subscriberInfo.isPCS() && ContractUtilities.containsFeature(info, Feature.CATEGORY_CODE_MULTI_RING)) {
			info.setMultiRingPhoneNumbers(subscriberLifecycleHelper.retrieveMultiRingPhoneNumbers(subscriberInfo.getSubscriberId()));
		}
		return info;
	}

	@Override
	public ContractChangeInfo getServiceAgreementForUpdate(String subscriberId, int billingAccountNumber) throws ApplicationException {
		
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);
		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}

		return getContractChangeInfo(subscriberInfo, billingAccountNumber);
	}

	private ContractChangeInfo getContractChangeInfo(SubscriberInfo subscriberInfo, int billingAccountNumber) throws ApplicationException {

		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(billingAccountNumber);
		SubscriberContractInfo contractInfo = getServiceAgreement(subscriberInfo, accountInfo);

		ContractChangeInfo contractChangeInfo = new ContractChangeInfo();
		contractChangeInfo.setCurrentAccountInfo(accountInfo);
		contractChangeInfo.setCurrentSubscriberInfo(subscriberInfo);
		contractChangeInfo.setCurrentContractInfo(contractInfo);
		contractChangeInfo.setNewSubscriberContractInfo(contractInfo);

		AccountType acctType;
		try {
			acctType = getReferenceDataFacade().getAccountType(String.valueOf(accountInfo.getAccountType()) + String.valueOf(accountInfo.getAccountSubType()), accountInfo.getBrandId());
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
		}

		contractChangeInfo.setDealerCode(acctType.getDefaultDealer());
		contractChangeInfo.setSalesRepCode(acctType.getDefaultSalesCode());

		return contractChangeInfo;
	}

	@Override
	public SubscriberContractInfo getServiceAgreementForEquipmentChange(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest)
			throws ApplicationException {
		SubscriberContractInfo contractInfo = getServiceAgreement(subscriberInfo, accountInfo);
		return getServiceAgreementForEquipmentChange(subscriberInfo, equipmentChangeRequest, contractInfo);
	}

	@Override
	public SubscriberContractInfo getServiceAgreementForEquipmentChange(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, SubscriberContractInfo contractInfo)
			throws ApplicationException {
		
		EquipmentInfo newDevice = (EquipmentInfo) equipmentChangeRequest.deriveNewDevice();
		if (newDevice == null) {
			newDevice = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		}

		Equipment currDevice = equipmentChangeRequest.getCurrentAssociatedHandset();
		if (currDevice == null) {
			currDevice = equipmentChangeRequest.getCurrentEquipment();
		}
		ContractChangeInfo contractChangeInfo = new ContractChangeInfo();
		contractChangeInfo.setBan(subscriberInfo.getBanId());
		contractChangeInfo.setSubscriberId(subscriberInfo.getSubscriberId());
		contractChangeInfo.setCurrentSubscriberInfo(subscriberInfo);
		ContractBo contract;
		try {
			ContractChangeContext changeContext = getContractChangeContext(contractChangeInfo, null);
			contract = new ContractBo(contractInfo, changeContext);
			addRemoveServices(contract, currDevice, newDevice, equipmentChangeRequest.preserveDigitalServices());
			removeNonMatchingServices(new EquipmentBo(newDevice, changeContext), contract);
			removeDispatchOnlyConflicts(contract);
			contract.checkForVoicemailService();
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.GET_SERVICEAGREEMENT_FOR_EQUIPCHG_ERROR, e.getMessage(), "", e);
		}

		return contract.getDelegate();
	}

	@Override
	public ContractChangeInfo saveServiceAgreement(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		// use validateServiceAgreement method to generate the end-state of contract changes the returned contractChangeInfo should contain the new SubscriberContractInfo already
		ContractChangeContext context = getContractChangeContext(contractChangeInfo, sessionId);
		context.setNotificationSuppressionInd(notificationSuppressionInd);
		context.setAuditInfo(getPopulatedAuditInfo(auditInfo, sessionId));
		
		ContractBo newContract = null;
		if (context.getCurrentContract() != null && contractChangeInfo.getCurrentContractInfo() == null) {
			contractChangeInfo.setCurrentContractInfo(context.getCurrentContract().getDelegate());
		}
		newContract = context.getNewContract();
		updateSubscriberContractInfo(newContract, contractChangeInfo);
		validatePPSServices(newContract, context.getCurrentAccount().getDelegate());
		updateSubscriberFeatureChangeInfo(newContract, contractChangeInfo);
		newContract.save(contractChangeInfo.getDealerCode(), contractChangeInfo.getSalesRepCode());
		contractChangeInfo.setNewSubscriberContractInfo(newContract.getDelegate());
		
		return contractChangeInfo;
	}

	protected void addRemoveServices(ContractBo contract, Equipment oldEquipment, Equipment newEquipment, boolean preserveDigitalServices) throws ApplicationException {
		
		String oldEquipmentType = oldEquipment.getEquipmentType();
		String newEquipmentType = newEquipment.getEquipmentType();

		try {
			logger.debug(">>>> oldEquipmentType [" + oldEquipmentType + "], oldEquipment networkType=" + oldEquipment.getNetworkType());
			logger.debug(">>>> newEquipmentType [" + newEquipmentType + "], newEquipment networkType=" + newEquipment.getNetworkType());
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_NETWORK_TYPE, e.getMessage(), "", e);
		}

		if (!newEquipment.isHSPA() && !oldEquipmentType.equals(newEquipmentType)) {
			ServiceAgreementInfo[] includedServices = contract.getDelegate().getIncludedServices0(false);
			ServiceAgreementInfo[] optionalServices = contract.getDelegate().getOptionalServices0(false);

			// Add Included Services only
			logger.debug("Adding Included Services to contract.");
			Service[] predefinedIncludedServices = contract.getPricePlan().getIncludedServices();
			List<String> predefinedServiceList = new ArrayList<String>();
			for (int i = 0; i < predefinedIncludedServices.length; i++) {
				predefinedServiceList.add(predefinedIncludedServices[i].getCode());

			}
			logger.debug("Predefined Included Services: " + predefinedServiceList.toString());

			for (int i = 0; i < includedServices.length; i++) {
				if (predefinedServiceList.contains(includedServices[i].getCode())) {
					predefinedServiceList.remove(includedServices[i].getCode());

				}
			}
			if (predefinedServiceList.size() > 0) {
				Iterator<String> iterator = predefinedServiceList.iterator();
				String serviceCode = null;
				while (iterator.hasNext()) {
					try {
						serviceCode = iterator.next();
						Service serviceToBeAdded = contract.getPricePlan().getIncludedService(serviceCode);

						if (serviceToBeAdded.isNetworkEquipmentTypeCompatible(newEquipment)) {
							contract.addService(serviceCode, preserveDigitalServices);
							logger.debug("Successfully Added Included Service [" + serviceCode + "]");
						} else {
							logger.debug("==> Included Service [" + serviceCode + "] Does Not support New Equipment, Service will not be added");
						}
					} catch (TelusAPIException e) {
						logger.debug("Failed to add the Service to the Contract. ServiceCode [" + serviceCode + "]");
					}
				}
			} else {
				logger.debug("==> No Included Services will be added");

				// Remove Services
			}
			if (!((oldEquipmentType.equals(Equipment.EQUIPMENT_TYPE_DIGITAL) || oldEquipmentType.equals(Equipment.EQUIPMENT_TYPE_ANALOG)) && newEquipmentType.equals(Equipment.EQUIPMENT_TYPE_ANALOG) && preserveDigitalServices)) {
				logger.debug("==> Removing Services ....");

				// Remove included services
				for (int i = 0; i < includedServices.length; i++) {
					if (false == includedServices[i].getService0().isNetworkEquipmentTypeCompatible(newEquipment)) {
						try {
							contract.removeService(includedServices[i].getCode());
							logger.debug("Successfully Removed Included Service [" + includedServices[i].getCode() + "] - " + includedServices[i].getDescription());
						} catch (TelusAPIException e) {
							logger.debug("Failed to remove the Service from the Contract, ServiceCode = " + includedServices[i].getCode());
						}
					}
				}

				// Remove optional services
				for (int i = 0; i < optionalServices.length; i++) {
					if (false == optionalServices[i].getService0().isNetworkEquipmentTypeCompatible(newEquipment)) {
						try {
							contract.removeService(optionalServices[i].getCode());
							logger.debug("Successfully Removed Optonal Service [" + optionalServices[i].getCode() + "] - " + optionalServices[i].getDescription());
						} catch (TelusAPIException e) {
							logger.debug("Failed to remove the Service from the Contract, ServiceCode = " + optionalServices[i].getCode());
						}
					}
				}
			}
		}
	}

	protected void removeNonMatchingServices(EquipmentBo equipment, ContractBo c) throws ApplicationException {
		logger.debug(">>>> removeNonMatchingServices()");

		ServiceAgreementInfo[] services = c.getDelegate().getOptionalServices0(false);
		if (services == null || services.length == 0) {
			logger.debug(">>>> No optional services were found.");
			return;
		}

		List<String> serviceList = new ArrayList<String>();
		for (int i = 0; i < services.length; i++) {
			serviceList.add(services[i].getCode());
		}
		logger.debug(">>>> optional service list " + serviceList.toString());

		ServiceAgreementInfo[] matchingServices = ContractUtilities.retainServices(services, equipment);
		ServiceAgreementInfo[] nonMatchingServices = ContractUtilities.difference(services, matchingServices);

		if (nonMatchingServices != null && nonMatchingServices.length > 0) {

			for (int i = 0; i < nonMatchingServices.length; i++) {
				try {
					if (serviceList.contains(nonMatchingServices[i].getCode())) {
						c.removeService(nonMatchingServices[i].getCode());
						logger.debug(">>>> optional service removed [" + nonMatchingServices[i].getCode() + "]");
					}
				} catch (TelusAPIException e) {
					logger.debug("Failed to remove the Service from the Contract, ServiceCode = " + nonMatchingServices[i].getCode());
				}
			}
		}
	}

	protected void removeDispatchOnlyConflicts(ContractBo contract) throws ApplicationException {
		logger.debug("====> removeDispatchOnlyConflicts()");

		if (!contract.isTelephonyEnabled()) { // dispatch only
			ServiceAgreementInfo[] services = ContractUtilities.retainTelephonyDisabledConflicts(contract.getDelegate().getServices0(false));
			ServiceFeatureInfo[] features = ContractUtilities.retainTelephonyDisabledConflicts(contract.getDelegate().getFeatures0(false));

			try {
				for (int i = 0; i < services.length; i++) {
					try {
						contract.removeService(false, services[i].getCode());
					} catch (InvalidServiceChangeException e) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_SERVICE_CHANGE, e.getMessage(), "", e);
					} catch (TelusAPIException e) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, e.getMessage(), "", e);
					}
				}

				for (int i = 0; i < features.length; i++) {
					contract.removeFeature(features[i].getCode());
				}
			} catch (UnknownObjectException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, e.getMessage(), "", e);
			}
		}
	}

	private EquipmentChangeRequestInfo updateEquipmentChangeRequest(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest) throws ApplicationException {
		String newEquipmentSerialNumber = equipmentChangeRequest.getNewEquipmentSerialNumber();
				
		if (equipmentChangeRequest.getCurrentEquipment() == null && subscriberInfo != null && subscriberInfo.getSerialNumber() != null) {
			EquipmentInfo currentEquipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber());
			equipmentChangeRequest.setCurrentEquipment(currentEquipmentInfo);
		}
		if (equipmentChangeRequest.getNewEquipment() == null && newEquipmentSerialNumber != null) {
			EquipmentInfo newEquipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(newEquipmentSerialNumber);
			
			String newAssociatedHandsetIMEI = equipmentChangeRequest.getNewAssociatedHandsetSerialNumber();
			if (newAssociatedHandsetIMEI != null) {
				newEquipmentInfo.setAssociatedHandsetIMEI(newAssociatedHandsetIMEI);
				
				try {
					EquipmentInfo associatedHandset = getProductEquipmentHelper().getEquipmentInfobySerialNo(newAssociatedHandsetIMEI);
					newEquipmentInfo.setAssociatedHandset(associatedHandset);
				}catch (ApplicationException ae) {
					if (ErrorCodes.UNKNOWN_SERIAL_NUMBER.equals(ae.getErrorCode())) {
						logger.error("updateEquipmentChangeRequest: Unknown newAssociatedHandsetIMEI=["+newAssociatedHandsetIMEI+"]. newEquipmentSerialNumber=[" + newEquipmentSerialNumber + "] subscriber=["+subscriberInfo+"]. ");						
					}
				}
			}
			equipmentChangeRequest.setNewEquipment(newEquipmentInfo);
		}

		if (equipmentChangeRequest.getCurrentEquipment() != null && equipmentChangeRequest.getCurrentEquipment().isUSIMCard() && equipmentChangeRequest.getNewEquipment() != null
				&& equipmentChangeRequest.getNewEquipment().isUSIMCard() == false) {
			EquipmentInfo currentHandset = ContractUtilities.getLastAssociatedHandsetForUSIMEquipment((EquipmentInfo) equipmentChangeRequest.getCurrentEquipment(), getProductEquipmentHelper());
			equipmentChangeRequest.setCurrentAssociatedHandset(currentHandset);
		}

		if (equipmentChangeRequest.getAssociatedHandset() == null && equipmentChangeRequest.getNewAssociatedHandsetSerialNumber() != null) {
			EquipmentInfo newAssociatedHandset = getProductEquipmentHelper().getEquipmentInfobySerialNo(equipmentChangeRequest.getNewAssociatedHandsetSerialNumber());
			if (newAssociatedHandset.isMule()) {
				equipmentChangeRequest.setAssociatedMuleEquipment(newAssociatedHandset);
			} else {
				equipmentChangeRequest.setAssociatedHandset(newAssociatedHandset);
			}
		}
		
		return equipmentChangeRequest;
	}

	public boolean isEquipmentInUseOnAnotherBan(String serialNumber, int banId, boolean active) throws ApplicationException {
		
		EquipmentSubscriberInfo[] associatedSubscribers = subscriberLifecycleHelper.retrieveEquipmentSubscribers(serialNumber, active);

		if (associatedSubscribers == null) {
			return false;
		}

		for (int i = 0; i < associatedSubscribers.length; i++) {
			if (associatedSubscribers[i].getBanId() != banId) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * 
	 * @param dealerCode
	 * @param salesRepCode
	 * @param accountInfo
	 * @return String[]  code[0] = dealerCode; code[1] = salesRepCode;
	 * @throws ApplicationException
	 */
	private String[] getKBDealer(String dealerCode, String salesRepCode, AccountInfo accountInfo) throws ApplicationException {
		
		String[] codes = new String[2];
		CPMSDealerInfo dealer = getCPMSDealerInfo(dealerCode, salesRepCode);

		if (dealerCode.equals(dealer.getChannelCode()) && salesRepCode.equals(dealer.getUserCode())) {
			AccountType acctType;
			try {
				acctType = getReferenceDataFacade().getAccountType(String.valueOf(accountInfo.getAccountType()) + String.valueOf(accountInfo.getAccountSubType()), accountInfo.getBrandId());
			} catch (TelusException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
			}

			codes[0] = acctType.getDefaultDealer();
			codes[1] = acctType.getDefaultSalesCode();

		} else {
			codes[0] = dealerCode;
			codes[1] = salesRepCode;
		}

		return codes;
	}

	@Override
	public ContractChangeInfo validateServiceAgreement(ContractChangeInfo contractChangeInfo, String sessionId) throws ApplicationException {
		
		ContractChangeContext context = getContractChangeContext(contractChangeInfo, sessionId);

		ContractBo newContract = null;
		newContract = context.getNewContract();
		updateSubscriberContractInfo(newContract, contractChangeInfo);
		validatePPSServices(newContract, context.getCurrentAccount().getDelegate());

		ContractChangeInfo ccChangeInfo = new ContractChangeInfo();
		ccChangeInfo.setCurrentAccountInfo(context.getCurrentAccount().getDelegate());
		ccChangeInfo.setCurrentSubscriberInfo(context.getCurrentSubscriber().getDelegate());
		ccChangeInfo.setCurrentContractInfo(newContract.getDelegate());
		SubscriberContractInfo contractInfo = evaluateCallingCircleCommitmentAttributeData(ccChangeInfo);

		contractChangeInfo.setNewSubscriberContractInfo(contractInfo);
		
		return contractChangeInfo;
	}
	
	/*
	 * Feature change not taking effects(copy data from the Price Plan change object)
	 */
	private void updateSubscriberFeatureChangeInfo(ContractBo contract, ContractChangeInfo contractChangeInfo) {
		PricePlanChangeInfo ppcInfo = contractChangeInfo.getPricePlanChangeInfo();
		ServiceFeatureInfo[] features = contract.getDelegate().getFeatures0(true);
		if (ppcInfo != null) {
			for (FeatureChangeInfo info : ppcInfo.getFeatureChangeInfoList()) {
				for (ServiceFeatureInfo feature : features) {
					if (info.getCode().equals(feature.getCode().trim())) {
						feature.setParameter(info.getFeatureParameter());
						feature.setTransaction(ContractUtilities.translateTransactionType(info.getTransactionType()));
					}
				}
			}
		}
	}

	/**
	 * This method would update the contract per business logics
	 * @param contract
	 * @param contractChangeInfo
	 * @throws ApplicationException
	 */
	private void updateSubscriberContractInfo(ContractBo contract, ContractChangeInfo contractChangeInfo) throws ApplicationException {
		PricePlanChangeInfo ppcInfo = contractChangeInfo.getPricePlanChangeInfo();
		if (ppcInfo != null) {
			// Validation may not be necessary since we allow backdating price plan change to current bill cycle in SD. Commented for now.
			//			Date newContractEffectiveDate = ppcInfo.getEffectiveDate();
			//			try {
			//				if (newContractEffectiveDate != null && newContractEffectiveDate.after(getReferenceDataFacade().getLogicalDate())) {
			//					contract.setEffectiveDate(ppcInfo.getEffectiveDate());
			//				}
			//			} catch (TelusException e) {
			//				throw new SystemException (SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
			//			}
			contract.setEffectiveDate(ppcInfo.getEffectiveDate());
		}

		ServiceChangeInfo[] serviceChangeInfoArray = contractChangeInfo.getServiceChangeInfoList();
		for (ServiceChangeInfo svcChgInfo : serviceChangeInfoArray) {
			processServiceChangeOnContract(contract, svcChgInfo);
		}
		postContractUpdate(contract, contractChangeInfo);
	}

	private void validatePPSServices(ContractBo contract, AccountInfo accountInfo) throws ApplicationException {
		// Validate if PP&S services are included in the contract
		// 1) accountType/subAccountType are eligible
		// 2) if the contract has PP&S AddOn it must have a PP&S Bundle and
		// addon expiry is equal to bundle expiry
		// Since PP&S service require immediate activation the contract should
		// have one bundle and one addon at a time

		Collection<ServiceAgreementInfo> ppsBundles = new ArrayList<ServiceAgreementInfo>();
		Collection<ServiceAgreementInfo> ppsAddOns = new ArrayList<ServiceAgreementInfo>();
		
		ServiceAgreementInfo[] optionalServices = contract.getOptionalServices();
		if (optionalServices != null) {
			for (ServiceAgreementInfo service : optionalServices) {
				if (service.getService().isPPSBundle()) {
					ppsBundles.add(service);
					//hasPPSBundle = true;
				} else if (service.getService().isPPSAddOn()) {
					//hasPPSAddOn = true;
					ppsAddOns.add(service);
				}
			}
		}

		ServiceAgreementInfo[] includedPPSBundles = ppsBundles.toArray(new ServiceAgreementInfo[ppsBundles.size()]);
		ServiceAgreementInfo[] includedPPSAddOns = ppsAddOns.toArray(new ServiceAgreementInfo[ppsAddOns.size()]);
		if ((includedPPSBundles.length + includedPPSAddOns.length) != 0) {
			try {
				if (!getReferenceDataFacade().isPPSEligible(accountInfo.getAccountType(), accountInfo.getAccountSubType())) {
					ContractService service = (includedPPSBundles.length != 0) ? includedPPSBundles[0] : includedPPSAddOns[0];
					throw new InvalidServiceChangeException(InvalidServiceChangeException.ACCOUNT_INELIGIBLE, "Account/SubAccount not eligible for PP&S Services", service.getService(), service, null);
				
				}
				if ((includedPPSAddOns.length > 0) && (includedPPSBundles.length == 0)) {
					throw new InvalidServiceChangeException(InvalidServiceChangeException.REQUIRED_SERVICE_IS_MISSING, "PP&S add-on requires a PP&S bundle", includedPPSAddOns[0].getService(),
							includedPPSAddOns[0], null);
				}
				Date addOnExpiryDate = null;
				if (includedPPSAddOns.length > 0) {
					addOnExpiryDate = includedPPSAddOns[0].getExpiryDate();
					if (includedPPSAddOns[0].getService().isPromotion()) {
						// check if the follow up is present
						if (includedPPSAddOns.length > 1) {
							addOnExpiryDate = includedPPSAddOns[1].getExpiryDate();
						} else {
							// Assume Follow up SOCK not added yet by KB and will have
							// null expiry
							addOnExpiryDate = null;
						}

					}
				}
				Date bundleExpiryDate = null;
				if (includedPPSBundles.length > 0) {
					bundleExpiryDate = includedPPSBundles[0].getExpiryDate();
					if (includedPPSBundles[0].getService().isPromotion()) {
						// check if the follow up is present
						if (includedPPSBundles.length > 1) {
							bundleExpiryDate = includedPPSBundles[1].getExpiryDate();
						} else {
							// Assume Follow up SOCK not added yet by KB and will have
							// null expiry
							bundleExpiryDate = null;
						}

					}
				}

				if ((bundleExpiryDate != null) && (addOnExpiryDate != null)) {
					if (!addOnExpiryDate.equals(bundleExpiryDate)) {
						// For PP&S the expiry of the bundle and add-on have to be
						// the same.
						// Originallly the add-on expiry date was required to be
						// before bundle expiry date
						// (addOnExpiryDate.after(bundleExpiryDate))) {
						throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_EXPIRYDATE_CONFLICT, "PP&S add-on expiry date must be equal to  the PP&S bundle expiry date",
								includedPPSAddOns[0].getService(), includedPPSAddOns[0], null);
					}
				} else if (bundleExpiryDate != addOnExpiryDate) {
					throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_EXPIRYDATE_CONFLICT, "PP&S add-on expiry date must be equal to  the PP&S bundle expiry date",
							includedPPSAddOns[0].getService(), includedPPSAddOns[0], null);
				}
			} catch (InvalidServiceChangeException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, e.getMessage(), "", e);
			} catch (TelusException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, e.getMessage(), "", e);
			} catch (TelusAPIException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, e.getMessage(), "", e);
			}
			}
		// end PP&S validation
	}
	
	private void validatePPSServiceEffectiveDate(ContractBo contract, ServiceChangeInfo svcChgInfo) throws InvalidServiceChangeException, UnknownObjectException {
		Service service = contract.getService(svcChgInfo.getCode()).getService();
		if (service.isPPSAddOn() || service.isPPSBundle()) {
			if (svcChgInfo.getEffectiveDate() != null && getLogicalDate().before(svcChgInfo.getEffectiveDate())) {
				 throw new InvalidServiceChangeException(InvalidServiceChangeException.SERVICE_ACTIVATIONDATE_CONFLICT, "PP&S services require immediate activation [" + svcChgInfo.getCode() + "]",
						service, null, null);
			 }
		}
	}
	
	private void processServiceChangeOnContract(ContractBo contract, ServiceChangeInfo svcChgInfo) throws ApplicationException {
		
		if (svcChgInfo.getTransactionType() != null) {
			try {
				if (ContractUtilities.WS_TRANSACTION_TYPE_ADD.equals(svcChgInfo.getTransactionType())) {
					if (contract.containsService(svcChgInfo.getCode()) && contract.getService(svcChgInfo.getCode()).getService0().isBoundService()) {
						logger.info("Trying to add SOC[" + svcChgInfo.getCode()
								+ "] which is already on the contract, could result from adding a leading SOC in same change request, this service change request is ignored");
					} else {
						ServiceAgreementInfo cs = null;
						//boolean isDurationAddRequested = svcChgInfo.isDurationService(); // REASON: ladderedServiceInd is not currently used 
						ServiceInfo service = getReferenceDataFacade().getRegularService(svcChgInfo.getCode());
						if (service != null && service.getDurationServiceHours() > 0) {
							throw new TelusAPIException("Attempt using a duration servie ,x-hour services not supported " + service.getCode());
						} else {
							cs = contract.addService(svcChgInfo.getCode(), svcChgInfo.getEffectiveDate(), svcChgInfo.getExpiryDate());
						} 
						processPrepaidServicePropertiesOnContract(cs, svcChgInfo.getPrepaidServicePropertyInfo());
						processFeatureChangeOnContract(cs, svcChgInfo.getFeatureChangeInfoList());
						validatePPSServiceEffectiveDate(contract, svcChgInfo);
					}
				} else if (ContractUtilities.WS_TRANSACTION_TYPE_MODIFY.equals(svcChgInfo.getTransactionType())) {
					ServiceAgreementInfo cs = contract.getService(svcChgInfo.getCode());
					if (cs != null) {
						if (svcChgInfo.getEffectiveDate() != null) {
							cs.setEffectiveDate(svcChgInfo.getEffectiveDate());
						}
						cs.setExpiryDate(svcChgInfo.getExpiryDate());
					}
					processPrepaidServicePropertiesOnContract(cs, svcChgInfo.getPrepaidServicePropertyInfo());
					processFeatureChangeOnContract(cs, svcChgInfo.getFeatureChangeInfoList());
					validatePPSServiceEffectiveDate(contract, svcChgInfo);
				} else if (ContractUtilities.WS_TRANSACTION_TYPE_REMOVE.equals(svcChgInfo.getTransactionType())) {
					if (svcChgInfo.isDurationService() && svcChgInfo.getEffectiveDate() == null) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_SOC_EFFECTIVE_DATE, "Attempt to remove XHOUR SOC " + svcChgInfo.getCode()
								+ " without providing effective date.", "", null);
					}
					String serviceMappingCode = ClientApiUtils.getContractServiceMappingKey(svcChgInfo.getCode(), svcChgInfo.isDurationService() ? svcChgInfo.getEffectiveDate() : null);
								
					if (contract.containsService(serviceMappingCode) == false && contract.getDelegate().containsService0(serviceMappingCode, true)) {
						// The SOC is in the delete service list: possible reason is that Client sending both leading / follow SOC for bound service pair
						// and the leading SOC is processed first, which result the follow SOC being removed automatically
						logger.info("SOC[" + serviceMappingCode + "] is already marked as to be deleted, this service change request is ignored");
					} else {
						contract.removeService(serviceMappingCode);
					}
				} else if (ContractUtilities.WS_TRANSACTION_TYPE_NOCHG.equals(svcChgInfo.getTransactionType())) {
					ServiceAgreementInfo cs = contract.getService(svcChgInfo.getCode());
					processFeatureChangeOnContract(cs, svcChgInfo.getFeatureChangeInfoList());
				}
			} catch (InvalidServiceChangeException e) {
				if (e.getReason() == InvalidServiceChangeException.BOUND_SERVICE) {
					//ignore the service change request for adding /removing  bound service
					logger.info("Trying to " + svcChgInfo.getTransactionType() + " bound service [" + svcChgInfo.getCode() + "], change request is ignored");
				} else {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_SERVICE_CHANGE, e.getMessage(), "", e);
				}
			} catch (TelusAPIException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, e.getMessage(), "", e);
			}
		}
	}

	private void processPrepaidServicePropertiesOnContract(ServiceAgreementInfo sa, PrepaidServicePropertyInfo prepaidServiecProperties) {
		if (sa.isWPS() && prepaidServiecProperties != null) {
			sa.setAutoRenewFundSource(prepaidServiecProperties.getAutoRenewalFundSource());
			sa.setPurchaseFundSource(prepaidServiecProperties.getPurchaseFundSource());
			sa.setAutoRenew(prepaidServiecProperties.isAutoRenewalInd());
		}
	}

	private void processFeatureChangeOnContract(ServiceAgreementInfo cs, FeatureChangeInfo[] featureChangeInfoList) throws ApplicationException {
		for (FeatureChangeInfo featureChangeInfo : featureChangeInfoList) {
			processFeatureChangeOnContract(cs, featureChangeInfo);
		}
	}

	private void processFeatureChangeOnContract(ServiceAgreementInfo cs, FeatureChangeInfo featureChgInfo) throws ApplicationException {
		try {
			if (ContractUtilities.WS_TRANSACTION_TYPE_ADD.equals(featureChgInfo.getTransactionType())) {
				ServiceFeatureInfo cf = cs.getFeature0(featureChgInfo.getCode(), false);
				if (featureChgInfo.getFeatureParameter() != null) {
					cf.setParameter(featureChgInfo.getFeatureParameter());
				}
				if ((cf.isCallingCircle() || cf.isPrepaidCallingCircle()) && featureChgInfo.getCallingCirclePhoneNumberList() != null) {
					cf.setCallingCirclePhoneNumberList(featureChgInfo.getCallingCirclePhoneNumberList());
				}
				if (featureChgInfo.getEffectiveDate() != null) {
					cf.setEffectiveDate(featureChgInfo.getEffectiveDate());
				}
				if (featureChgInfo.getExpiryDate() != null) {
					cf.setExpiryDate(featureChgInfo.getExpiryDate());
				}
			} else if (ContractUtilities.WS_TRANSACTION_TYPE_MODIFY.equals(featureChgInfo.getTransactionType())) {
				ServiceFeatureInfo cf = cs.getFeature0(featureChgInfo.getCode(), false);
				cf.setParameter(featureChgInfo.getFeatureParameter());
				if ((cf.isCallingCircle() || cf.isPrepaidCallingCircle())) {
					if (featureChgInfo.getCallingCirclePhoneNumberList() != null) {
						cf.setCallingCirclePhoneNumberList(featureChgInfo.getCallingCirclePhoneNumberList());
					}
					if (featureChgInfo.getNewServiceFeatureInfo() != null && featureChgInfo.getNewServiceFeatureInfo().getCallingCircleCommitmentAttributeData() != null) {
						cf.setCallingCircleCommitmentAttributeData(featureChgInfo.getNewServiceFeatureInfo().getCallingCircleCommitmentAttributeData0());
					}
				}
				/* we shall never change effective/expiration date on feature level, there is no KB-API for this!!!
				if (featureChgInfo.getEffectiveDate() != null) {
					cf.setEffectiveDate(featureChgInfo.getEffectiveDate());
				}
				if (featureChgInfo.getExpiryDate() != null) {
					cf.setExpiryDate(featureChgInfo.getExpiryDate());
				}
				 */
			} else if (ContractUtilities.WS_TRANSACTION_TYPE_REMOVE.equals(featureChgInfo.getTransactionType())) {
				cs.removeFeature(featureChgInfo.getCode());
			}
		} catch (UnknownObjectException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_FEATURE, "Unknown Feature " + featureChgInfo.getCode(), "", e);
		}
	}

	/**
	 * This method performs settings that are not part of any business logic. i.e. pure setters only
	 * @param contract
	 * @param contractChangeInfo
	 */
	private void postContractUpdate(ContractBo contract, ContractChangeInfo contractChangeInfo) {
		if (contractChangeInfo.isContractRenewalInd()) {
			if (contractChangeInfo.getNewCommitmentInfo() != null) {
				contract.setCommitmentMonths(contractChangeInfo.getNewCommitmentInfo().getMonths());
				contract.setCommitmentStartDate(contractChangeInfo.getNewCommitmentInfo().getStartDate());
				contract.setCommitmentEndDate(contractChangeInfo.getNewCommitmentInfo().getEndDate());
				contract.setCommitmentReasonCode(contractChangeInfo.getNewCommitmentInfo().getReasonCode());
			}
		}
	}


	private AccountInformationHelper getAccountInformationHelper() throws ApplicationException {
		return ejbController.getEjb(AccountInformationHelper.class);
	}

	private ProductEquipmentHelper getProductEquipmentHelper() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentHelper.class);
	}

	private ProductEquipmentLifecycleFacade getProductEquipmentLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentLifecycleFacade.class);
	}

	private ReferenceDataFacade getReferenceDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	private ReferenceDataHelper getReferenceDataHelper() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataHelper.class);
	}

	private ApplicationMessageFacade getApplicationMessageFacade() throws ApplicationException {
		return ejbController.getEjb(ApplicationMessageFacade.class);
	}

	private ActivityLoggingService getActivityLoggingService() throws ApplicationException {
		return ejbController.getEjb(ActivityLoggingService.class);
	}

	private AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}

	private AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}

	private DealerManager getDealerManagerSvc() throws ApplicationException {
		return ejbController.getEjb(DealerManager.class);
	}

	private com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager getConfigurationManager() throws ApplicationException {
		return ejbController.getEjb(com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager.class);
	}

	private ClientIdentity getClientIdentity(String sessionId) throws ApplicationException {
		ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
		if (ci == null) {
			throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
		}

		return ci;
	}

	/**
	 * This method should be used to open a session with SubscriberLifecycleManager and get the session ID
	 * 
	 * @param sessionId
	 * @return
	 * @throws ApplicationException
	 */
	private String getSubscriberLifecycleManagerSessionId(String sessionId) throws ApplicationException {
		ClientIdentity ci = getClientIdentity(sessionId);
		return subscriberLifeCycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
	}
	
	private String getAccountLifecycleManagerSessionId(String sessionId) throws ApplicationException {
		ClientIdentity ci = getClientIdentity(sessionId);
		return getAccountLifecycleManager().openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
	}
	
	private String getAccountLifecycleFacadeSessionId(String sessionId) throws ApplicationException {
		ClientIdentity ci = getClientIdentity(sessionId);
		return getAccountLifecycleFacade().openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
	}

	private String getReferenceDataFacadeSessionId(String sessionId) throws ApplicationException {
		ClientIdentity ci = getClientIdentity(sessionId);
		return getReferenceDataFacade().openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
	}

	private boolean isLetterOrDigit(String letterOrDigit) {

		if (letterOrDigit == null || letterOrDigit.length() < 1) {
			throw new NullPointerException("A String 'letterOrDigit' parameter is required");
		}

		for (int i = 0; i < letterOrDigit.length(); i++) {
			if (!Character.isLetterOrDigit(letterOrDigit.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	private ContractChangeContext getContractChangeContext(ContractChangeInfo contractChangeInfo, String sessionId) throws ApplicationException {
		ContractChangeContext context;
		context = ChangeContextFactory.createChangeContext(contractChangeInfo);
		initializeChangeContext(context, sessionId);
		if (contractChangeInfo.getEquipmentChangeRequestInfo() != null) {
			updateEquipmentChangeRequest(context.getCurrentSubscriber().getDelegate(), contractChangeInfo.getEquipmentChangeRequestInfo());
		}

		return context;
	}

	private EquipmentChangeContext getEquipmentChangeContext(AccountInfo accountInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		EquipmentChangeContext context;
		context = ChangeContextFactory.createEquipmentChangeContext(accountInfo, subscriberInfo);
		initializeChangeContext(context, sessionId);
		return context;
	}

	private AsyncSubscriberCommitContext getAsyncSubscriberCommitContext(int ban, String subscriberId, ClientIdentity clientIdentity) throws ApplicationException {
		AsyncSubscriberCommitContext context = ChangeContextFactory.createAsyncSubscriberCommitContext(ban, subscriberId);
		context.setEjbController(ejbController);
		context.setClientIdentity(clientIdentity);
		return context;
	}
	
	private MigrationChangeContext getMigrationChangeContext(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException {
		MigrationChangeContext context = ChangeContextFactory.createChangeContext(migrationChangeInfo);
		initializeChangeContext(context, sessionId);
		return context;
	}
	
	private ActivationChangeContext getActivationChangeContext(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException {
		ActivationChangeContext context = ChangeContextFactory.createChangeContext(activationChangeInfo);
		initializeChangeContext(context, sessionId);
		return context;
	}
	
	private ActivatePortinContext getActivatePortinChangeContext(ActivationChangeInfo activationChangeInfo, PortRequestInfo portRequest, ServiceRequestHeaderInfo requestHeader, AuditInfo auditInfo,
			String sessionId) throws ApplicationException {
		ActivatePortinContext context = ChangeContextFactory.createActivatePortinContext(activationChangeInfo, portRequest, requestHeader, auditInfo);
		initializeChangeContext(context, sessionId);
		return context;
	}
	
	private MigrateSeatChangeContext getMigrateSeatChangeContext(MigrateSeatChangeInfo migrateSeatChangeInfo, String sessionId) throws ApplicationException {
		MigrateSeatChangeContext context = ChangeContextFactory.createChangeContext(migrateSeatChangeInfo);
		initializeChangeContext(context, sessionId);
		return context;
	}

	private void initializeChangeContext(BaseChangeContext<?> context, String sessionId) throws ApplicationException {
		context.setEjbController(ejbController);
		context.setSessionId(this, sessionId);

		if (sessionId != null) {
			context.setClientIdentity(getClientIdentity(sessionId));
		}

		context.initialize();
	}

	private void initializeEjbController() {
		ejbController.setEjb(AccountLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE, null);
		ejbController.setEjb(AccountInformationHelper.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER, null);
		ejbController.setEjb(AccountLifecycleManager.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER, null);
		ejbController.setEjb(ActivityLoggingService.class, EJBUtil.TELUS_CMBSERVICE_ACTIVITY_LOGGING, null);
		ejbController.setEjb(ApplicationMessageFacade.class, EJBUtil.TELUS_CMBSERVICE_APPLICATION_MESSAGE_FACADE, null);
		ejbController.setEjb(com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager.class, EJBUtil.TELUS_CMBSERVICE_CONFIGURATION_MANAGER, null);
		ejbController.setEjb(ContactEventManager.class, EJBUtil.TELUS_CMBSERVICE_CONTACT_EVENT_MANAGER, null);
		ejbController.setEjb(DealerManager.class, EJBUtil.TELUS_CMBSERVICE_DEALER_MANAGER, null);
		ejbController.setEjb(ProductEquipmentHelper.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER, null);
		ejbController.setEjb(ProductEquipmentManager.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_MANAGER, null);
		ejbController.setEjb(ProductEquipmentLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_LIFECYCLE_FACADE, null);
		ejbController.setEjb(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE, null);
		ejbController.setEjb(ReferenceDataHelper.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER, null);
		ejbController.setEjb(SubscriberLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE, this);
		ejbController.setEjb(SubscriberLifecycleHelper.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER, subscriberLifecycleHelper);
		ejbController.setEjb(SubscriberLifecycleManager.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_MANAGER, subscriberLifeCycleManager);
	}

	private InteractionInfo newReport(String transactionType, String dealerCode, String salesRepCode, int banId, String subscriberId, ClientIdentity clientIdentity) {
		try {
			long reasonId = mapActivityReasonCodesToReasonId(null, null);
			InteractionInfo info = null;
			info = getConfigurationManager().newReport(transactionType, clientIdentity.getApplication(), Integer.parseInt(clientIdentity.getPrincipal()), dealerCode, salesRepCode, banId,
					subscriberId, reasonId);
			return info;
		} catch (Throwable e) {
			// Silent failure
			logger.debug(e);
		}
		return null;
	}

	private long mapActivityReasonCodesToReasonId(String activityCode, String reasonCode) throws ApplicationException {
		long reasonId = 0;
		if (activityCode != null && reasonCode != null) {
			String code = activityCode + "_" + reasonCode;
			try {
				reasonId = getReferenceDataFacade().getClientStateReason(code).getReasonId();
			} catch (TelusException e) {
				logger.debug(e);
			}
		}

		return reasonId;
	}

	public static char booleanToChar(boolean value) {
		return (value) ? 'Y' : 'N';
	}

	private ApplicationMessageInfo[] getEquipmentSwapWarningMessages(AccountInfo accountInfo, SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId)
			throws ApplicationException {
		boolean preserveDigitalServices = equipmentChangeRequest.isPreserveDigitalServices();

		ArrayList<ApplicationMessageInfo> messageList = new ArrayList<ApplicationMessageInfo>();
		SubscriberContractInfo contract = getServiceAgreement(subscriberInfo, accountInfo);
		EquipmentInfo oldEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();
		EquipmentInfo newEquipmentInfo = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();

		EquipmentChangeContext changeContext = getEquipmentChangeContext(accountInfo, subscriberInfo, sessionId);
		EquipmentBo oldEquipment = new EquipmentBo(oldEquipmentInfo, changeContext);
		EquipmentBo newEquipment = new EquipmentBo(newEquipmentInfo, changeContext);

		ClientIdentity ci = getClientIdentity(sessionId);

		if (newEquipment.isCellularDigital() && newEquipment.isPTTEnabled() && !ContractUtilities.isPTTServiceIncluded(contract.getServices())) {
			messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 76));

		}

		if (oldEquipment.isCellularDigital() && oldEquipment.isPTTEnabled() && ContractUtilities.isPTTServiceIncluded(contract.getServices())) {
			messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 77));

			// Digital -> Analog
		}

		if (oldEquipment.isCellularDigital() && newEquipment.isAnalog()) {
			if (preserveDigitalServices) {
				messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 78));
			} else {
				messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 79));
			}
		}

		if (!newEquipment.isHSPA() && !newEquipment.getEquipmentType().equals(oldEquipment.getEquipmentType())
				&& !((oldEquipment.isAnalog() && newEquipment.isCellularDigital()) || (oldEquipment.isCellularDigital() && newEquipment.isAnalog()))) {

			boolean isSupportedNewEquipment = false;

			try {
				isSupportedNewEquipment = contract.getPricePlan().isCompatible(newEquipment.getNetworkType(), newEquipment.getEquipmentType());
			} catch (TelusAPIException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}

			if (!isSupportedNewEquipment) {
				if (oldEquipment.isCellularDigital() && !((CellularDigitalEquipment) oldEquipment).isPDA() && newEquipment.isCellularDigital() && ((CellularDigitalEquipment) newEquipment).isPDA()) {
					messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 85)); // changed
					// by
					// Dimitry
				} else if (oldEquipment.isCellularDigital() && ((CellularDigitalEquipment) oldEquipment).isPDA() && newEquipment.isCellularDigital()
						&& !((CellularDigitalEquipment) newEquipment).isPDA()) {
					messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 86));
				} else {
					messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 80));
				}
			} else if (!newEquipment.isAnalog() && !newEquipment.isCellularDigital()) {
				messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 81));
			}
		}

		// Third-Part E-mail: visto -> non-visto
		if (ContractUtilities.isVistoCapable(oldEquipment.getProductFeatures()) && !ContractUtilities.isVistoCapable(newEquipment.getProductFeatures())) {
			boolean isRemoveVistoServiceRequired = false;
			ServiceAgreementInfo[] optionalServices = contract.getOptionalServices0(false);
			for (int i = 0; i < (optionalServices != null ? optionalServices.length : 0); i++) {
				setServiceDetailInformation(contract, optionalServices[i]);
				isRemoveVistoServiceRequired |= optionalServices[i].getService().isVisto();
			}
			if (isRemoveVistoServiceRequired) {
				messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 83));
			}
		}

		// Third-Part E-mail: non-visto -> visto
		if (!ContractUtilities.isVistoCapable(oldEquipment.getProductFeatures()) && ContractUtilities.isVistoCapable(newEquipment.getProductFeatures())) {
			boolean isAddVistoServiceRequired = true;
			ServiceAgreementInfo[] optionalServices = contract.getOptionalServices0(false);
			for (int i = 0; i < (optionalServices != null ? optionalServices.length : 0); i++) {
				setServiceDetailInformation(contract, optionalServices[i]);
				isAddVistoServiceRequired &= !optionalServices[i].getService().isVisto();
			}
			if (isAddVistoServiceRequired) {
				messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 84));
			}
		}

		// non GPS-> GPS (added by Roman)
		if (newEquipment.isGPS() && !oldEquipment.isGPS()) {
			messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 87));
		}

		// GPS-> non GPS (added by Roman)
		if (!newEquipment.isGPS() && oldEquipment.isGPS()) {
			messageList.add(getApplicationMessageFacade().getApplicationMessage(ci.getApplication(), equipmentChangeRequest.getAudienceType(), accountInfo.getBrandId(), 88));
		}

		return messageList.toArray(new ApplicationMessageInfo[messageList.size()]);
	}

	private void setServiceDetailInformation(SubscriberContractInfo contract, ServiceAgreementInfo sa) throws ApplicationException {
		ServiceInfo service = null;

		if (sa.isWPS() == true) {
			try {
				service = getReferenceDataFacade().getWPSService(sa.getServiceCode());
			} catch (TelusException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
			}
			if (service == null) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Couldn't find Prepaid service for code: [" + sa.getServiceCode() + "]", "");
			}

			if (service.getFeatures() != null) {
				ServiceFeatureInfo[] wpsFeatures = sa.getFeatures0(true);
				for (int j = 0; j < wpsFeatures.length; j++) {
					ServiceFeatureInfo wpsFtr = wpsFeatures[j];
					RatedFeatureInfo wpsRatedFtr;
					try {
						wpsRatedFtr = service.getFeature0(wpsFtr.getFeatureCode().trim());
					} catch (UnknownObjectException e) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, e.getMessage(), "", e);
					}
					wpsFtr.setFeature(wpsRatedFtr);
				}
			}
		} else {
			try {
				service = contract.getPricePlan0().getService0(sa.getServiceCode());
			} catch (UnknownObjectException e) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, e.getMessage(), "", e);
			}
			if (service == null) {
				// Add non-priceplan SOC.
				try {
					service = getReferenceDataFacade().getRegularService(sa.getServiceCode());
				} catch (TelusException e) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
				}
				if (service == null) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "SOC [" + sa.getServiceCode() + "] does not exist in the system.", "");
				}
			}

			ServiceFeatureInfo[] features = sa.getFeatures0(true);
			for (int j = 0; j < features.length; j++) {
				ServiceFeatureInfo f = features[j];
				try {
					f.setFeature(service.getFeature0(f.getFeatureCode()));
				} catch (UnknownObjectException e) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, e.getMessage(), "", e);
				}
			}
		}

		sa.setService(service);
	}


	@Override
	public ArrayList<Message> browseAllMessages(String queueBeanId) {
		logger.debug("browseAllMessages() for " + queueBeanId);
		return getJmsQueueBean(queueBeanId).browseAllMessages();
	}

	@Override
	public ArrayList<Message> browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType) {
		logger.debug("browseMessageByCmbJMSType() for " + queueBeanId + " with cmbType=" + cmbJmsType);
		return getJmsQueueBean(queueBeanId).browseMessageByCmbJMSType(cmbJmsType);
	}

	@Override
	public ArrayList<Message> browseMessageByCmbJMSType(String queueBeanId, String cmbJmsType, String subType) {
		logger.debug("browseMessageByCmbJMSType() for " + queueBeanId + " with cmbType=" + cmbJmsType + " and subType=" + subType);
		return getJmsQueueBean(queueBeanId).browseMessageByCmbJMSType(cmbJmsType, subType);
	}

	private JmsQueueSupport getJmsQueueBean(String queueBeanId) {
		return JmsQueueSupport.QueueHelper.getQueueBean(queueBeanId);
	}

	private abstract class AsyncEnterpriseDataCallback {
		
		protected abstract void setProductEnterpriseDataInfo(ProductEnterpriseDataInfo info);

		public void execute(int billingAccountNumber, String processType, String sessionId) {
			if (AppConfiguration.isAsyncPublishEnterpriseDataEnabled()) {
				ProductEnterpriseDataInfo info = new ProductEnterpriseDataInfo();
				try {
					info.setBillingAccountNumber(billingAccountNumber);
					info.setProcessType(processType);
					setProductEnterpriseDataInfo(info);
					jmsSend(String.valueOf(billingAccountNumber), info, "msgSubTypeProductEnterpriseDataSync", amdocsSessionManager.getClientIdentity(sessionId));
				} catch (Exception e) {
					logger.error("Error in EnterpriseDataCallBack: " + e + ". ProductEnterpriseDataInfo=" + info);
				}
			}
		}

		private void jmsSend(String unitOfOrderName, Object object, String messageSubType, ClientIdentity clientIdentity) {
			if (AppConfiguration.isUnitOfOrderDisabledForEnterpriseData()) {
				queueSender.send(object, messageSubType, clientIdentity);
			} else {
				queueSender.sendUnitOfOrder(unitOfOrderName, object, messageSubType, clientIdentity);
			}
		}
	}

	private abstract class ProductEnterpriseDataMgmtCallback {
		
		private AccountInfo accountInfo;
		private SubscriberInfo subscriberInfo;
		private EquipmentInfo equipmentInfo;
		private SubscriberContractInfo subscriberContractInfo;
		private boolean retrieveAccount = false;
		private boolean retrieveSubscriberInfo = false;
		private boolean retrieveEquipmentInfo = false;
		private boolean retrieveSubscriberContractInfo = false;
		private int billingAccountNumber;
		private String subscriberId;
		private String phoneNumber;

		protected abstract void executeDao(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId)
				throws ApplicationException;

		protected abstract void setData();

		/**
		 * @param accountInfo the accountInfo to set
		 */
		public void setAccountInfo(AccountInfo accountInfo) {
			this.accountInfo = accountInfo;
		}
		
		/**
		 * @param subscriberInfo the subscriberInfo to set
		 */
		public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
			this.subscriberInfo = subscriberInfo;
		}
		
		/**
		 * @param equipmentInfo the equipmentInfo to set
		 */
		public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
			this.equipmentInfo = equipmentInfo;
		}
		
		/**
		 * @param subscriberContractInfo the subscriberContractInfo to set
		 */
		public void setSubscriberContractInfo(SubscriberContractInfo subscriberContractInfo) {
			this.subscriberContractInfo = subscriberContractInfo;
		}
		
		/**
		 * @param retrieveAccount the retrieveAccount to set
		 */
		public void setRetrieveAccount(boolean retrieveAccount) {
			this.retrieveAccount = retrieveAccount;
		}
		
		/**
		 * @param retrieveSubscriberInfo the retrieveSubscriberInfo to set
		 */
		public void setRetrieveSubscriberInfo(boolean retrieveSubscriberInfo) {
			this.retrieveSubscriberInfo = retrieveSubscriberInfo;
		}
		
		/**
		 * @param retrieveEquipmentInfo the retrieveEquipmentInfo to set
		 */
		public void setRetrieveEquipmentInfo(boolean retrieveEquipmentInfo) {
			this.retrieveEquipmentInfo = retrieveEquipmentInfo;
		}
		
		/**
		 * @param retrieveSubscriberContractInfo the retrieveSubscriberContractInfo to set
		 */
		public void setRetrieveSubscriberContractInfo(boolean retrieveSubscriberContractInfo) {
			this.retrieveSubscriberContractInfo = retrieveSubscriberContractInfo;
		}
		
		/**
		 * @param billingAccountNumber the billingAccountNumber to set
		 */
		public void setBillingAccountNumber(int billingAccountNumber) {
			this.billingAccountNumber = billingAccountNumber;
		}
		
		/**
		 * @param subscriberId the subscriberId to set
		 */
		public void setSubscriberId(String subscriberId) {
			this.subscriberId = subscriberId;
		}

		/**
		 * @return the phoneNumber
		 */
		public String getPhoneNumber() {
			return phoneNumber;
		}
		
		/**
		 * @param phoneNumber the phoneNumber to set
		 */
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		
		public void execute(String processType, String sessionId) throws ApplicationException {
			
			setData();

			if (retrieveAccount && billingAccountNumber != 0) {
				accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(billingAccountNumber);
			}

			if (retrieveSubscriberInfo && subscriberId != null && subscriberId.isEmpty() == false) {
				subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);
			}

			if (retrieveSubscriberInfo && subscriberInfo == null && phoneNumber != null && phoneNumber.isEmpty() == false) {
				// this block should be triggered only if subscriberId is absent. The subscriberId should be treated as the
				// primary way to retrieve a subscriber. However, sometimes the subscriberId may be unavailable or it's invalid.
				// Example would be in a phone # change scenario. The only available is the new phone # as the subscriber ID has
				// already  changed with the phone #. In this case, we have to pass in the phone # and retrieve the subscriberID based on
				// phone #
				subscriberId = subscriberLifecycleHelper.retrieveSubscriberIDByPhoneNumber(billingAccountNumber, phoneNumber);
				subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);
			}

			if (retrieveEquipmentInfo && subscriberInfo != null) {
				equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(subscriberInfo.getSerialNumber());
			}

			if (retrieveSubscriberContractInfo && subscriberId != null && subscriberId.isEmpty() == false) {
				subscriberContractInfo = subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(subscriberId);
			}

			process(accountInfo, subscriberInfo, equipmentInfo, subscriberContractInfo, processType, sessionId);
		}

		private void process(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String processType, String sessionId)
				throws ApplicationException {
			if (EligibilityUtilities.isEnterpriseDataEligible(account, processType)) {
				ClientIdentity clientIdentity = getClientIdentity(sessionId);
				executeDao(account, subscriberInfo, equipmentInfo, subscriberContractInfo, clientIdentity.getPrincipal());
			}
		}
	}

	
	/**
	 *  These two insertProductInstance deprecated method to be removed post July 2019 release , keeping here for rollback purpose
	 */
	@Override
	@Deprecated
	public void asyncInsertProductInstance(final int billingAccountNumber, final String subscriberId, final String processType, String sessionId) {
		new AsyncEnterpriseDataCallback() {

			@Override
			protected void setProductEnterpriseDataInfo(ProductEnterpriseDataInfo info) {
				info.setMessageType(ProductEnterpriseDataInfo.MESSAGE_TYPE_INSERT_PRODUCT_INSTANCE);
				info.setSubscriberId(subscriberId);
			}

		}.execute(billingAccountNumber, processType, sessionId);
	}

	

	@Override
	@Deprecated
	public void insertProductInstance(final int billingAccountNumber, final String subscriberId, final String processType, final String sessionId) throws ApplicationException {
		
		new ProductEnterpriseDataMgmtCallback() {

			@Override
			protected void setData() {
				setBillingAccountNumber(billingAccountNumber);
				setRetrieveAccount(true);
				setSubscriberId(subscriberId);
				setRetrieveSubscriberInfo(true);
				setRetrieveEquipmentInfo(true);
				setRetrieveSubscriberContractInfo(true);
			}

			@Override
			protected void executeDao(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId)
					throws ApplicationException {
				if (subscriberInfo != null && subscriberContractInfo != null) {
					productDataMgmtDao.insertProductInstance(subscriberInfo, equipmentInfo, subscriberContractInfo, userId);
				} else {
					logger.error("Skipping insertProductInstance because subscriberInfo or subscriberContractInfo is null. [" + billingAccountNumber + "/" + subscriberId + "]");
				}
			}
			
		}.execute(processType, sessionId);
	}
	
		
	@Override
	public void asyncInsertProductInstance(final AccountInfo accountInfo,final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo,
			final SubscriberContractInfo subscriberContractInfo, final String processType,final String sessionId) throws ApplicationException {
		new AsyncEnterpriseDataCallback() {

			@Override
			protected void setProductEnterpriseDataInfo(ProductEnterpriseDataInfo info) {
				info.setMessageType(ProductEnterpriseDataInfo.MESSAGE_TYPE_INSERT_PRODUCT_INSTANCE);
				info.setSubscriberId(subscriberInfo.getSubscriberId());
				info.setAccountInfo(accountInfo);
				info.setSubscriberInfo(subscriberInfo);
				info.setEquipmentInfo(equipmentInfo);
				info.setSubscriberContractInfo(subscriberContractInfo);
			}

		}.execute(accountInfo.getBanId(), processType, sessionId);
	}
	
	
	@Override
	public void insertProductInstance(final AccountInfo accountInfo, final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo,final SubscriberContractInfo subscriberContractInfo,
			String processType, String sessionId) throws ApplicationException{
		
		new ProductEnterpriseDataMgmtCallback() {

			@Override
			protected void setData() {
				setAccountInfo(accountInfo);
				setSubscriberInfo(subscriberInfo);
				setEquipmentInfo(equipmentInfo);
				setSubscriberContractInfo(subscriberContractInfo);
			}

			@Override
			protected void executeDao(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId)
					throws ApplicationException {
				if (subscriberInfo != null && subscriberContractInfo != null) {
					productDataMgmtDao.insertProductInstance(subscriberInfo, equipmentInfo, subscriberContractInfo, userId);
				} else {
					logger.error("Skipping insertProductInstance because subscriberInfo or subscriberContractInfo is null. [" + accountInfo.getBanId() + "/" + subscriberInfo.getSubscriberId() + "]");
				}
			}
			
		}.execute(processType, sessionId);
	}

	@Override
	public void asyncUpdateProductInstance(final int billingAccountNumber, final String subscriberId, final String phoneNumber, final String processType, String sessionId) {
		
		new AsyncEnterpriseDataCallback() {

			@Override
			protected void setProductEnterpriseDataInfo(ProductEnterpriseDataInfo info) {
				info.setMessageType(ProductEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_PRODUCT_INSTANCE);
				info.setSubscriberId(subscriberId);
				info.setPhoneNumber(phoneNumber);
			}

		}.execute(billingAccountNumber, processType, sessionId);
	}

	@Override
	public void asyncUpdateProductInstance(final int billingAccountNumber, final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo, final SubscriberContractInfo subscriberContractInfo,
			final String processType, String sessionId) {
		
		new AsyncEnterpriseDataCallback() {

			@Override
			protected void setProductEnterpriseDataInfo(ProductEnterpriseDataInfo info) {
				info.setMessageType(ProductEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_PRODUCT_INSTANCE);
				info.setSubscriberId(subscriberInfo.getSubscriberId());
				info.setSubscriberInfo(subscriberInfo);
				info.setEquipmentInfo(equipmentInfo);
				info.setSubscriberContractInfo(subscriberContractInfo);
			}

		}.execute(billingAccountNumber, processType, sessionId);
	}

	@Override
	public void updateProductInstance(final int billingAccountNumber, final String subscriberId, final String phoneNumber, String processType, String sessionId) throws ApplicationException {
		
		new ProductEnterpriseDataMgmtCallback() {

			@Override
			protected void setData() {
				setBillingAccountNumber(billingAccountNumber);
				setRetrieveAccount(true);
				setSubscriberId(subscriberId);
				setPhoneNumber(phoneNumber);
				setRetrieveSubscriberInfo(true);
				setRetrieveEquipmentInfo(true);
				setRetrieveSubscriberContractInfo(true);
			}

			@Override
			protected void executeDao(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId)
					throws ApplicationException {
				if (subscriberInfo != null && subscriberContractInfo != null) {
					productDataMgmtDao.updateProductInstance(subscriberInfo, equipmentInfo, subscriberContractInfo, userId);
				} else {
					logger.error("Skipping updateProductInstance because subscriberInfo or subscriberContractInfo is null. [" + billingAccountNumber + "/" + subscriberId + "/" + phoneNumber + "]");
				}
			}

		}.execute(processType, sessionId);
	}

	@Override
	public void updateProductInstance(final int billingAccountNumber, final SubscriberInfo subscriberInfo, final EquipmentInfo equipmentInfo, final SubscriberContractInfo subscriberContractInfo,
			String processType, String sessionId) throws ApplicationException {
		
		new ProductEnterpriseDataMgmtCallback() {

			@Override
			protected void setData() {
				setBillingAccountNumber(billingAccountNumber);
				setRetrieveAccount(true);
				setSubscriberInfo(subscriberInfo);
				setEquipmentInfo(equipmentInfo);
				setSubscriberContractInfo(subscriberContractInfo);
			}

			@Override
			protected void executeDao(AccountInfo account, SubscriberInfo subscriberInfo, EquipmentInfo equipmentInfo, SubscriberContractInfo subscriberContractInfo, String userId)
					throws ApplicationException {
				if (subscriberInfo != null && subscriberContractInfo != null) {
					productDataMgmtDao.updateProductInstance(subscriberInfo, equipmentInfo, subscriberContractInfo, userId);
				} else {
					logger.error("Skipping updateProductInstance because subscriberInfo or subscriberContractInfo is null. [" + billingAccountNumber + "]");
				}
			}
		}.execute(processType, sessionId);
	}

	@Override
	public void cancelPortedInSubscriber(int banNumber, String phoneNumber, String deactivationReason, Date activityDate, String portOutInd, boolean isBrandPort, String subscriberId, CommunicationSuiteInfo commSuiteInfo,
	    boolean notificationSuppressionInd, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException {
		
        //step 1: remove the communication suite if subscriber is companion
	    if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == false) {
	    	if (logger.isDebugEnabled()) {
	    		logger.debug("remove the communication suite due to port cancelation of companion subscriber [" +phoneNumber+"] ." );
	    	}
			removeFromCommunicationSuite(banNumber, phoneNumber, commSuiteInfo, true);
	    } 
	    
		boolean companionCancelDueToPrimaryPortOutInd = commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == true && 
				commSuiteInfo.getCompanionPhoneNumberList().isEmpty() == false;

	    // step 2: cancel the port out subscriber in KB
		subscriberLifeCycleManager.cancelPortedInSubscriber(banNumber, phoneNumber, deactivationReason, activityDate, portOutInd, isBrandPort, sessionId);

		
		//step 3: cancel the companion subscriber if subscriber is primary and it has linked with any watches
	    if (companionCancelDueToPrimaryPortOutInd) {
	        try {
		        // cancel the companion subscribers and update companion subscribers cancel status in SRPDS ,CODS.
	        	if (logger.isDebugEnabled()) {
	        		logger.debug("canceling the all the companion subscribers due to primary phone [" +phoneNumber+"] port out, ban = [" + banNumber + "] ,companionSubs =[" + Arrays.toString(commSuiteInfo.getCompanionPhoneNumberList().toArray()));
	        	}
	            cancelCommunicationSuiteCompanionSubs(banNumber, phoneNumber, commSuiteInfo, activityDate, deactivationReason, 'O', "", "", notificationSuppressionInd, srpdsHeader, sessionId);
	        } catch (Throwable t) {
	            logger.error("error occured when canceling the communication suite due to port cancelation of primary subscriber (Or) SRPDS update , ban = [" + banNumber + "] , primary phoneNumber = [" + phoneNumber + "],companionSubs =[" + Arrays.toString(commSuiteInfo.getCompanionPhoneNumberList().toArray()), t);
	        }

	    }
	    //step 4: update CODS
	    try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				asyncUpdateProductInstance(banNumber, subscriberId, phoneNumber, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_CANCELLATION, sessionId);
			}
		} catch (TelusException e) {
			logger.error("cancelPortedInSubscriber error.", e);
		}
	    
	   // step 5: publish the cancel port in event into kafka
		AccountInfo lwAccount = getAccountInformationHelper().retrieveLwAccountByBan(banNumber);
		SubscriberInfo subInfo = new SubscriberInfo();
		subInfo.setPhoneNumber(phoneNumber);
		subInfo.setSubscriberId(phoneNumber);
		boolean portOutActivityInd = "Y".equalsIgnoreCase(portOutInd) ? true : false;
		subscriberEventPublisher.publishSubscriberCancelPortOutEvent(lwAccount, subInfo, activityDate,deactivationReason,portOutActivityInd,isBrandPort,createAuditInfoFromSession(sessionId),false,notificationSuppressionInd);

	}

	
	@Override
	public boolean isPortActivity(String phoneNumber) {
		
		boolean isPortActivity = false;
		try {
			LocalServiceProviderInfo localServiceProviderInfo = portabilityServiceDao.getLocalServiceProvider(phoneNumber);
			String localServiceProviderId = localServiceProviderInfo.getLocalServiceProviderId();
			if (localServiceProviderId != null && !localServiceProviderId.equals("null")) {
				logger.debug("LocalServiceProvider for PhoneNumber " + phoneNumber + " \n" + localServiceProviderInfo.toString());
				if (!(localServiceProviderId.equals(AppConfiguration.getLocalServiceProviderIDTELUS()))) {
					isPortActivity = true;
				}
			}
		} catch (Exception e) {
			isPortActivity = false;
			logger.error("error while getting info from PortabilityService for phoneNumber[" + phoneNumber + "]: " + e.getMessage(), e);
		}
		logger.debug("phoneNumber :" + phoneNumber + ", isPortActivity : " + isPortActivity);
		
		return isPortActivity;
	}

	
	@Override
	public void cancelSubscriber(SubscriberInfo subscriberInfo,Date activityDate, String activityReasonCode,String depositReturnMethod, String waiveReason,String userMemoText, 
			boolean notificationSuppressionInd,AuditInfo auditInfo, CommunicationSuiteInfo commSuiteInfo,ServiceRequestHeader header, String sessionId) throws ApplicationException {
		
		boolean isPortActivity = false;
		boolean isFutureDatedCancellation = false;
		Date logicalDate = null;

		// validate future dated transaction
		try {
			logicalDate = getReferenceDataFacade().getLogicalDate();
			if (activityDate != null && DateUtil.isAfter(activityDate, logicalDate)) {
				isFutureDatedCancellation = true;
			}
		} catch (TelusException e) {
			logger.error("cancelSubscriber error.", e);
		}		
		
		// step 1:  retrieve the subscriber portActivity Indicator 
		if (!isFutureDatedCancellation) {
			isPortActivity = isPortActivity(subscriberInfo.getPhoneNumber());
		}
		
		// step 2:  removeFromCommunicationSuite if subscriber is companion
		if (logger.isDebugEnabled()) {
			logger.debug("cancelSubscriber .. ban=["+subscriberInfo.getBanId()+",subscriberId="+subscriberInfo.getSubscriberId()+",commSuiteInfo="+commSuiteInfo);
		}
		// Break the communication suite ( Phone and Watch Pairing) if the subscriber is companion subscriber ( watch subscriber).
		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == false) {
			removeFromCommunicationSuite(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), commSuiteInfo, true);
		}
		// step 3 : cancel the subscriber in KB 			
		subscriberLifeCycleManager.cancelSubscriber(subscriberInfo, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, isPortActivity, sessionId);
		
		boolean companionCancelDueToPrimaryPortOutInd = commSuiteInfo!=null && commSuiteInfo.isRetrievedAsPrimary()==true && commSuiteInfo.getCompanionPhoneNumberList().isEmpty()==false;

		 // step 4 : cancel the companion subscribers  due to primary and update companion subscribers cancel status in SRPDS.
		if(companionCancelDueToPrimaryPortOutInd){
			cancelCommunicationSuiteCompanionSubs(subscriberInfo.getBanId(), subscriberInfo.getPhoneNumber(), commSuiteInfo, activityDate, activityReasonCode, 
					depositReturnMethod.trim().charAt(0), waiveReason, userMemoText, notificationSuppressionInd, header, sessionId);
		} 
		// step 5 : update the CODS for subscriber cancellation
		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, logicalDate)) {
				asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(),
						ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_CANCELLATION, sessionId);
			}
		} catch (Exception e) {
			logger.error("cancelSubscriber error.", e);
		}		
		// step 6 : trigger business connect provisioning cancel order
		if (subscriberInfo.isSeatSubscriber()) { 
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createSeatCancelRequest(subscriberInfo, activityDate));
		}
		
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		
		// step 7 : publish the subscriber cancel event into kafka
		subscriberEventPublisher.publishSubscriberCancelEvent(accountInfo, subscriberInfo, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, isPortActivity,createAuditInfoFromSession(sessionId),false,notificationSuppressionInd);

		// step 8: process bill medium changes
		getAccountLifecycleFacade().processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), subscriberInfo.getSubscriberId(),
				BillNotificationActivityType.SUBSCRIBER_CANCEL.name(), sessionId);

	}

	@Override
	public void suspendSubscriber(SubscriberInfo subscriberInfo, Date activityDate, String activityReasonCode, String userMemoText, String sessionId) throws ApplicationException {
		
		subscriberLifeCycleManager.suspendSubscriber(subscriberInfo, activityDate, activityReasonCode, userMemoText, sessionId);
		
		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_SUSPENSION,
						sessionId);
			}
		} catch (TelusException e) {
			logger.error("suspendSubscriber error.", e);
		}
		
		if (subscriberInfo.isSeatSubscriber()) {
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createSeatSuspendRequest(subscriberInfo, activityDate, activityReasonCode));
		}

		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		getAccountLifecycleFacade().processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), subscriberInfo.getSubscriberId(),
				BillNotificationActivityType.SUBSCRIBER_SUSPEND.name(), sessionId);
	}

	@Override
	public void restoreSuspendedSubscriber(SubscriberInfo subscriberInfo, Date activityDate, String activityReasonCode, String userMemoText, boolean portIn, String sessionId)
			throws ApplicationException {
		AccountInfo accountInfo = null;
		if (subscriberInfo.isSeatSubscriber()) {
			 accountInfo = getAccountInformationHelper().retrieveAccountByBan(subscriberInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		}
		subscriberLifeCycleManager.restoreSuspendedSubscriber(subscriberInfo, activityDate, activityReasonCode, userMemoText, portIn, sessionId);	
		
		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(),
						ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESTORE_SUSPENDED, sessionId);
			}
		} catch (TelusException e) {
			logger.error("restoreSuspendedSubscriber error.", e);
		}		
		if (subscriberInfo.isSeatSubscriber()) {	
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			if (AccountSummary.STATUS_SUSPENDED == accountInfo.getStatus()) {
				asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountRestoreRequest(accountInfo.getBanId(), accountInfo.getBrandId(), activityDate, activityReasonCode));
			} else {
				asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createSeatResumeRequest(subscriberInfo, activityDate, activityReasonCode));	
			}
			
		}
	}

	/**
	 * @deprecated 
	 */
	@Override
	public void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId,
			String sessionId) throws ApplicationException {
		resumeCancelledSubscriber(subscriberInfo, activityReasonCode, userMemoText, portIn, portProcessType, oldBanId, oldSubscriberId, null, sessionId);
	}
	
	@Override
	public void resumeCancelledSubscriber(SubscriberInfo subscriberInfo, String activityReasonCode, String userMemoText, boolean portIn, String portProcessType, int oldBanId, String oldSubscriberId, SubscriberResumedPostTaskInfo taskInfo,
			String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.resumeCancelledSubscriber(subscriberInfo, activityReasonCode, userMemoText, portIn, portProcessType, oldBanId, oldSubscriberId, sessionId);
		asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESTORE_CANCELLED,
				sessionId);
		if (taskInfo != null) {
			if (taskInfo.isRepairCommSuite()) {
				this.asyncRepairCommunicationSuiteDueToSubscriberResumed(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), sessionId);
			}
		}
	}

	/**
	 * The SubscriberContractInfo that's passed in is supposed to perform a PPC together with equipment change only. It should NOT be used with a simple service
	 * agreement change.
	 */
	@Override
	public void changeEquipment(SubscriberInfo subscriberInfo, EquipmentInfo oldPrimaryEquipmentInfo, EquipmentInfo newPrimaryEquipmentInfo, EquipmentInfo[] newSecondaryEquipmentInfo,
			String dealerCode, String salesRepCode, String requesterId, String swapType, SubscriberContractInfo subscriberContractInfo, PricePlanValidationInfo pricePlanValidation,
			AuditInfo auditInfo, boolean notificationSuppressInd, SubscriberContractInfo oldContractInfo, String sessionId) throws ApplicationException {
		
		EquipmentInfo newHandset = null;
		int banId = subscriberInfo.getBanId();
		String subscriberId = subscriberInfo.getSubscriberId();
		
		if (subscriberContractInfo != null) {
			// If there are contract changes, execute the following logic for WCC 2017.
			// Establish the SapccUpdateAccountPurchaseBo context first - this will initialize all the required context data for the OCSSAM call
			SapccUpdateAccountPurchaseBo sapccUpdateAccountPurchaseBo = getSapccUpdateAccountPurchaseBo(subscriberInfo, subscriberContractInfo, getPopulatedAuditInfo(auditInfo, sessionId), sessionId);
			boolean sapccUpdated = sapccUpdateAccountPurchaseBo.chargeSapccAcountPurchaseAmount();

			try {
				subscriberLifeCycleManager.changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, dealerCode, salesRepCode, requesterId, swapType,
						subscriberContractInfo, pricePlanValidation, sessionId);
			} catch (Exception e) {
				ApplicationException ae	= e instanceof ApplicationException ? (ApplicationException) e : new ApplicationException(SystemCodes.CMB_SLM_EJB, e.getMessage(), StringUtils.EMPTY, e);
				if (sapccUpdated) {
					logger.error("Error occurred while calling subscriberLifeCycleManager.changeEquipment; reimbursing SAPCC account purchase counter for subscriber [" + subscriberId + "].", ae);
					sapccUpdated = rollbackSapccUpdateAccountPurchase(sapccUpdateAccountPurchaseBo, ae);
				}
				throw ae;
			}
		} else {
			// Otherwise, it's just an equipment change
			if (logger.isDebugEnabled()) {
				logger.debug("Improper use of the method. subscriber=["+ subscriberId +"] ban=["+banId+"]");
			}
			subscriberLifeCycleManager.changeEquipment(subscriberInfo, oldPrimaryEquipmentInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo, dealerCode, salesRepCode, requesterId, swapType, subscriberContractInfo,
					pricePlanValidation, sessionId);
		}

		asyncUpdateProductInstance(banId, subscriberId, subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE,
				sessionId);
		
		if (newPrimaryEquipmentInfo != null && newPrimaryEquipmentInfo.isHSPA() == true) {
			newHandset = newPrimaryEquipmentInfo.getAssociatedHandset();
		}
		boolean volteAdded = addVOLTEServiceForNewEquipmentAndSave(subscriberInfo, subscriberContractInfo, newHandset, dealerCode, salesRepCode, sessionId, false);
		AccountInfo accountInfo = null;
		if (subscriberContractInfo != null || volteAdded) {//price plan changed or VOLTE SOC added
			// retrieve the light weight accountInfo.
			accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(banId);
		    // publish serviceAgreement changes into kafka
			serviceAgreementEventPublisher.publishServiceAgreementChangeEvent(accountInfo, subscriberInfo, subscriberContractInfo, oldContractInfo, newPrimaryEquipmentInfo,dealerCode, salesRepCode, createAuditInfoFromSession(sessionId),notificationSuppressInd);
			
		}
		if (accountInfo == null) {
			accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(banId);
		}
	}

   
	
	/**
	 * Reserve an ICCID for the new eSIM and then change the associated eSIM enabled devices
	 * 
	 * Assumptions:
	 * The SIM profile of an eSIM can be provisioned in the same way as a physical USIM
	 * No SIM reservation rollback is required in the event of equipment change failure.
	 * 
	 * @param subscriberInfo
	 * @param accountInfo
	 * @param equipmentChangeRequest
	 * @param sessionId
	 */
	@Override
	public void changeEsimEnabledDevice(SubscriberInfo subscriberInfo, AccountInfo accountInfo, EquipmentChangeRequestInfo equipmentChangeRequest, String sessionId) throws ApplicationException {		
		String reservedIccId = null;
		String newImei = null;
		EquipmentInfo newEsim = null;
		EquipmentInfo newEsimDevice = null;
		
		// Check if current USIM and the new ESIM enabled device are set properly inside equipmentChangeRequest
		if (equipmentChangeRequest.getCurrentEquipment() == null || equipmentChangeRequest.getNewAssoicatedHandset() == null) {
			String errMsg = "Invalid parameter equipmentChangeRequest: both current USIM and new ESIM device are required";
			logger.error(errMsg);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, errMsg, StringUtils.EMPTY);
		}
		
		logger.debug("Enter changeEsimEnabledDevice(), ban: "+ accountInfo.getBanId() + ", subscriber: " + subscriberInfo.getSubscriberId() + ", equipmentChangeRequest: " + equipmentChangeRequest);
		
		newEsimDevice = (EquipmentInfo)equipmentChangeRequest.getNewAssoicatedHandset();
		
		newImei = StringUtils.isBlank(newEsimDevice.getSerialNumber()) ? equipmentChangeRequest.getNewAssociatedHandsetSerialNumber() : newEsimDevice.getSerialNumber();
		
		if (StringUtils.isBlank(newImei)) {
			String errorMsg = "Invalid parameter equipmentChangeRequest: IMEI of the new assoicated handset is not found";
			logger.error(errorMsg);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, errorMsg, StringUtils.EMPTY);
		}
		
		logger.debug("IMEI of new ESIM device: " + newImei);
		logger.debug("SIM profile code of new ESIM device: " + newEsimDevice.getSimProfileCd());
					
		// Use IMEI of the new eSim enabled device to do an ICCID reservation for the new eSIM
		reservedIccId = getProductEquipmentLifecycleFacade().reserveSimProfile(newImei, newEsimDevice.getSimProfileCd(), null).getSerialNumber();
		logger.info("Reserved ICCID: " + reservedIccId);
		
		newEsim = getProductEquipmentHelper().getEquipmentInfobySerialNumber(reservedIccId);
		equipmentChangeRequest.setNewEquipment(newEsim);
		logger.debug("New ESIM: " + newEsim);
		
		// Call the existing routine for change equipment
		changeEquipment0(subscriberInfo, accountInfo, equipmentChangeRequest, sessionId);
		logger.debug("Exit changeEsimEnabledDevice(), ban: "+ accountInfo.getBanId() + ", subscriber: " + subscriberInfo.getSubscriberId());
	}
	
	public boolean addVOLTEServiceForNewEquipmentAndSave(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, String dealerCode,
			String salesRepCode, String sessionId, boolean pricePlanChange) {
		boolean volteAdded = false;
		
		try {
			if (newHandset != null) {
				logger.debug("addVOLTEServiceForNewEquipmentAndSave: handset serial number for VoLTE is " + newHandset.getSerialNumber());
			} else {
				logger.debug("addVOLTEServiceForNewEquipmentAndSave: handset is null");
			}
			if (subscriberContractInfo == null) {
				subscriberContractInfo = this.getServiceAgreement(subscriberInfo);	
			}
			ServiceInfo volte = getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, newHandset, pricePlanChange);
			volteAdded = addVolteService(volte, subscriberContractInfo);

			if (volteAdded == true) {
				PricePlanValidationInfo pricePlanValidation = new PricePlanValidationInfo();
				pricePlanValidation.setEquipmentServiceMatch(false);
				subscriberLifeCycleManager.changeServiceAgreement(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
				logger.info("addVOLTEServiceAndSave : Volte soc successfully added on contract for  :  " + subscriberInfo.getPhoneNumber());
				asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE,
					sessionId);
			}
			
		} catch (Throwable t) {
			logger.error("Error trying to add VOLTE SOC for [" + subscriberInfo.getSubscriberId() + "]", t);
		}
		
		return volteAdded;
	}

	@Override
	public void changeIP(int ban, String subscriberId, String newIp, String newIpType, String newIpCorpCode, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.changeIP(ban, subscriberId, newIp, newIpType, newIpCorpCode, sessionId);
		asyncUpdateProductInstance(ban, subscriberId, null, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE, sessionId);
	}

	@Override
	public void changeFaxNumber(SubscriberInfo subscriber, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.changeFaxNumber(subscriber, sessionId);
		asyncUpdateProductInstance(subscriber.getBanId(), subscriber.getSubscriberId(), subscriber.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE, sessionId);
	}

	@Override
	public void changeFaxNumber(SubscriberInfo subscriber, AvailablePhoneNumberInfo newFaxNumber, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.changeFaxNumber(subscriber, newFaxNumber, sessionId);
		asyncUpdateProductInstance(subscriber.getBanId(), subscriber.getSubscriberId(), subscriber.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE, sessionId);
	}

	@Override
	public void changeMemberIdentity(IDENSubscriberInfo idenSubscriberInfo, int newUrbanId, int newFleetId, String newMemberId, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.changeMemberIdentity(idenSubscriberInfo, newUrbanId, newFleetId, newMemberId, sessionId);
		asyncUpdateProductInstance(idenSubscriberInfo.getBanId(), idenSubscriberInfo.getSubscriberId(), idenSubscriberInfo.getPhoneNumber(),
				ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE, sessionId);
	}

	@Override
	public void changePhoneNumber(SubscriberInfo subscriberInfo,AvailablePhoneNumberInfo newPhoneNumber, String reasonCode,
			String dealerCode, String salesRepCode, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.changePhoneNumber(subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, sessionId);
		asyncUpdateProductInstance(subscriberInfo.getBanId(), null, newPhoneNumber.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_RESOURCE_UPDATE, sessionId);
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		//refresh the subscriber after phone number change to load new start service date , status date ,etc
		SubscriberInfo newSubscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(subscriberInfo.getBanId(), newPhoneNumber.getPhoneNumber());
		changePhoneNumberPublisher.publishChangePhoneNumberEvent(accountInfo, newSubscriberInfo, subscriberInfo.getPhoneNumber(), dealerCode, salesRepCode, createAuditInfoFromSession(sessionId), false, sessionId);
	}

	@Override
	public void changePhoneNumberPortIn(SubscriberInfo subscriberInfo,AvailablePhoneNumberInfo newPhoneNumber, 
			String reasonCode,String dealerCode, String salesRepCode, String portProcessType,int oldBanId, String oldSubscriberId, 
			String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.portChangeSubscriberNumber(subscriberInfo, newPhoneNumber, reasonCode, dealerCode, salesRepCode, portProcessType, oldBanId, oldSubscriberId, sessionId);
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		//refresh the subscriber after phone number change to load new start service date , status date ,etc
		SubscriberInfo newSubscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(subscriberInfo.getBanId(), newPhoneNumber.getPhoneNumber());
		changePhoneNumberPublisher.publishChangePhoneNumberPortInEvent(accountInfo, newSubscriberInfo, subscriberInfo.getPhoneNumber(), portProcessType,dealerCode, salesRepCode, createAuditInfoFromSession(sessionId), false, sessionId);
	}
	
	// Updated for BC Integrations July 2018
	@Override
	public void createSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, boolean activate, boolean overridePatternSearchFee, String activationFeeChargeCode,
			boolean dealerHasDeposit, boolean portedIn, ServicesValidation srvValidation, String portProcessType, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
			
		// Business Connect context needs to be initialized before creating the subscriber in KB
		AuditInfo auditInfo = getPopulatedAuditInfo(null, sessionId);
		BusinessConnectBo businessConnectBo = getBusinessConnectBo(subscriberInfo, subscriberContractInfo, activate ? BusinessConnectContextTypeEnum.ACTIVATION : null, auditInfo, sessionId);
	
		// Add VoLTE SOC if eligible
		EquipmentInfo associatedHandset = null;
		if (subscriberInfo.getEquipment0() != null) {
			associatedHandset = subscriberInfo.getEquipment0().getAssociatedHandset();
		}
		ServiceInfo volte = getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, associatedHandset, false);
		addVolteService(volte, subscriberContractInfo);
			
		// Create the subscriber in KB
		subscriberLifeCycleManager.createSubscriber(subscriberInfo, subscriberContractInfo, activate, overridePatternSearchFee, activationFeeChargeCode, dealerHasDeposit, portedIn, srvValidation, 
				portProcessType, oldBanId, oldSubscriberId, sessionId);
		
		// If the KB update is successful, provision Business Connect services, if applicable
		businessConnectBo.provision();

		// execute activation post tasks
		ActivationPostTask postTask = new ActivationPostTask(subscriberEventPublisher, identityProfileService);
		
		postTask.initialize(subscriberInfo, subscriberContractInfo, portedIn,portProcessType, activationFeeChargeCode,
				overridePatternSearchFee, activate,createAuditInfoFromSession(sessionId), sessionId);
		postTask.apply();
	}
	
	@Override
	public void migrateSubscriber(SubscriberInfo srcSubscriberInfo, SubscriberInfo newSubscriberInfo, Date activityDate, SubscriberContractInfo subscriberContractInfo,
			EquipmentInfo newPrimaryEquipmentInfo, EquipmentInfo[] newSecondaryEquipmentInfo, MigrationRequestInfo migrationRequestInfo, String sessionId) throws ApplicationException {
		
		EquipmentInfo associatedHandset = null;
		if (newPrimaryEquipmentInfo != null) {
			associatedHandset = newPrimaryEquipmentInfo.getAssociatedHandset();
		}
		ServiceInfo volte = getVolteSocIfEligible(newSubscriberInfo, subscriberContractInfo, associatedHandset, false);
		addVolteService(volte, subscriberContractInfo);

		subscriberLifeCycleManager.migrateSubscriber(srcSubscriberInfo, newSubscriberInfo, activityDate, subscriberContractInfo, newPrimaryEquipmentInfo, newSecondaryEquipmentInfo,
			migrationRequestInfo, sessionId);
		asyncUpdateProductInstance(newSubscriberInfo.getBanId(), newSubscriberInfo.getSubscriberId(), newSubscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_MIGRATION,
				sessionId);
		
		Account account = getAccountInformationHelper().retrieveLwAccountByBan(newSubscriberInfo.getBanId());
		identityProfileService.registerConsumerProfile(newSubscriberInfo, account, IdentityProfileRegistrationOrigin.SUBSCRIBER_MIGRATION);
	}	
	
	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, String dealerCode,
			String salesRepCode, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {		

		AccountInfo targetAccount = getAccountInformationHelper().retrieveLwAccountByBan(targetBan);
		AccountInfo sourceAccount = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		
		
		subscriberLifeCycleManager.moveSubscriber(subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, dealerCode, salesRepCode, sessionId);
		asyncUpdateProductInstance(targetBan, subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_MOVE, sessionId);
		validatePPSServicesWhenAccountChanged(subscriberInfo.getSubscriberId(), targetBan, subscriberInfo.getBanId(), dealerCode, salesRepCode, sessionId);
		subscriberEventPublisher.publishSubscriberMoveEvent(sourceAccount, subscriberInfo, targetBan, activityDate, activityReasonCode, transferOwnership, userMemoText, dealerCode, salesRepCode, createAuditInfoFromSession(sessionId), notificationSuppressionInd);

		identityProfileService.registerConsumerProfile(subscriberInfo, targetAccount, IdentityProfileRegistrationOrigin.SUBSCRIBER_MOVE);
	}

	@Override
	public void moveSubscriber(SubscriberInfo subscriberInfo, int targetBan, Date activityDate, boolean transferOwnership, String activityReasonCode, String userMemoText, 
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		AccountInfo sourceAccount = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		AccountInfo targetAccount = getAccountInformationHelper().retrieveLwAccountByBan(targetBan);

		subscriberLifeCycleManager.moveSubscriber(subscriberInfo, targetBan, activityDate, transferOwnership, activityReasonCode, userMemoText, sessionId);
		asyncUpdateProductInstance(targetBan, subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_MOVE, sessionId);
		validatePPSServicesWhenAccountChanged(subscriberInfo.getSubscriberId(), targetBan, subscriberInfo.getBanId(), null, null, sessionId);
		subscriberEventPublisher.publishSubscriberMoveEvent(sourceAccount, subscriberInfo, targetBan, activityDate, activityReasonCode, transferOwnership, userMemoText, null, null, createAuditInfoFromSession(sessionId), notificationSuppressionInd);

		identityProfileService.registerConsumerProfile(subscriberInfo, targetAccount, IdentityProfileRegistrationOrigin.SUBSCRIBER_MOVE);
	}
	
	// Updated for BC Integrations July 2018
	@Override
	public void changePricePlan(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode, PricePlanValidationInfo pricePlanValidation,
			boolean notificationSuppressionInd, AuditInfo auditInfo, SubscriberContractInfo oldContractInfo, String sessionId) throws ApplicationException {

		
		EquipmentInfo newHandset = null;
		if (subscriberInfo.getEquipment0() != null) {
			newHandset = subscriberInfo.getEquipment0().getAssociatedHandset();
		}		
		
		ServiceInfo volte = getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, newHandset, true);
		addVolteService(volte, subscriberContractInfo);
		
		// Deprovision Business Connect services, if applicable, and if successful then update KB
		BusinessConnectBo businessConnectBo = getBusinessConnectBo(subscriberInfo, subscriberContractInfo, BusinessConnectContextTypeEnum.PRICE_PLAN_CHANGE, getPopulatedAuditInfo(auditInfo, sessionId), sessionId);
		businessConnectBo.deprovision();
		
		// Establish the SapccUpdateAccountPurchaseBo context first - this will initialize all the required context data for the OCSSAM call
		SapccUpdateAccountPurchaseBo sapccUpdateAccountPurchaseBo = getSapccUpdateAccountPurchaseBo(subscriberInfo, subscriberContractInfo, getPopulatedAuditInfo(auditInfo, sessionId), sessionId);
		boolean sapccUpdated = sapccUpdateAccountPurchaseBo.chargeSapccAcountPurchaseAmount();
		try {
			subscriberLifeCycleManager.changePricePlan(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
		} catch (ApplicationException ae) {
			if (sapccUpdated) {
				logger.error("Error occurred while calling subscriberLifeCycleManager.changePricePlan; reimbursing SAPCC account purchase counter for subscriber [" + subscriberInfo.getSubscriberId() + "].", ae);
				sapccUpdated = rollbackSapccUpdateAccountPurchase(sapccUpdateAccountPurchaseBo, ae);
			}
			throw ae;
		}
		
		// If the KB update is successful, provision Business Connect services, if applicable
		businessConnectBo.provision();
		
	    asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
		// retrieve the light weight accountInfo.
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
	    // publish serviceAgreement changes into kafka
		serviceAgreementEventPublisher.publishServiceAgreementChangeEvent(accountInfo, subscriberInfo, subscriberContractInfo, oldContractInfo, null ,dealerCode, salesRepCode,createAuditInfoFromSession(sessionId),notificationSuppressionInd);
	}

	// Updated for BC Integrations July 2018
	@Override
	public void changeServiceAgreement(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, String dealerCode, String salesRepCode,
			PricePlanValidationInfo pricePlanValidation, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		
		// Need to always retrieve the old contract since VOLTE SOC may have been added previously; previous condition retrieves old contract only "if (xhourSocList.size() > 0)"
		SubscriberContractInfo currentContractBeforeChanges = subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(subscriberInfo.getSubscriberId());  

		EquipmentInfo newHandset = null;
		if (subscriberInfo.getEquipment0() != null) {
			newHandset = subscriberInfo.getEquipment0().getAssociatedHandset();
		}
		ServiceInfo volte = getVolteSocIfEligible(subscriberInfo, subscriberContractInfo, newHandset, false);
		addVolteService(volte, subscriberContractInfo);
		
		// Deprovision Business Connect services, if applicable, and if successful then update KB
		BusinessConnectBo businessConnectBo = getBusinessConnectBo(subscriberInfo, subscriberContractInfo, BusinessConnectContextTypeEnum.SERVICE_CHANGE, getPopulatedAuditInfo(auditInfo, sessionId), sessionId);
		businessConnectBo.deprovision();
		
		// Establish the SapccUpdateAccountPurchaseBo context first - this will initialize all the required context data for the OCSSAM call
		SapccUpdateAccountPurchaseBo sapccUpdateAccountPurchaseBo = getSapccUpdateAccountPurchaseBo(subscriberInfo, subscriberContractInfo, getPopulatedAuditInfo(auditInfo, sessionId), sessionId);
		boolean sapccUpdated = sapccUpdateAccountPurchaseBo.chargeSapccAcountPurchaseAmount();
		try {
			subscriberLifeCycleManager.changeServiceAgreement(subscriberInfo, subscriberContractInfo, dealerCode, salesRepCode, pricePlanValidation, sessionId);
		} catch (ApplicationException ae) {
			if (sapccUpdated) {
				logger.error("Error occurred while calling subscriberLifeCycleManager.changeServiceAgreement; reimbursing SAPCC account purchase counter for subscriber [" + subscriberInfo.getSubscriberId() + "].", ae);
				sapccUpdated = rollbackSapccUpdateAccountPurchase(sapccUpdateAccountPurchaseBo, ae);
			}
			throw ae;
		}
		
		// If the KB update is successful, provision Business Connect services, if applicable
		businessConnectBo.provision();
		
		asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
		// retrieve the light weight accountInfo..
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
	    // publish serviceAgreement changes into kafka
		serviceAgreementEventPublisher.publishServiceAgreementChangeEvent(accountInfo, subscriberInfo, subscriberContractInfo, null, null,dealerCode, salesRepCode,createAuditInfoFromSession(sessionId),notificationSuppressionInd);
	}
	
	private boolean rollbackSapccUpdateAccountPurchase(SapccUpdateAccountPurchaseBo sapccUpdateAccountPurchaseBo, ApplicationException nestedException) throws ApplicationException {
		try {
			return sapccUpdateAccountPurchaseBo.reimburseSapccAcountPurchaseAmount();
		} catch (Throwable t) {
			logger.error("Error occurred while calling SapccUpdateAccountPurchaseBo.reimburseSapccAcountPurchaseAmount; unable to reimburse SAPCC account purchase counter.", t);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.OCS_PPU_ROLLBACK_ERROR, t.getMessage(), StringUtils.EMPTY, nestedException);
		}
	}
	
	
	
	/**
	 * 
	 * @param subscriberInfo
	 * @param subscriberContractInfo
	 * @param newHandset
	 * @return returns VoLTE soc if eligible, otherwise empty
	 */
	@Override
	public ServiceInfo getVolteSocIfEligible(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo newHandset, boolean pricePlanChange) {
		String phoneNumber = subscriberInfo.getPhoneNumber();
		logger.info("enter getVolteSocIfEligible for phone number :  " + phoneNumber);

		// This code block is used to exit the method if the Brand is not telus or Koodo
		if (subscriberInfo.getBrandId() != 0 && (subscriberInfo.getBrandId() == Brand.BRAND_ID_TELUS || subscriberInfo.getBrandId() == Brand.BRAND_ID_KOODO) == false) {
			logger.info("Exiting getVolteSocIfEligible Brand(" + subscriberInfo.getBrandId() + ") not Eligible");
			return null;
		}
		// Check to see if contract is null
		if (subscriberContractInfo == null) {
			logger.error("Exiting getVolteSocIfEligible: SubscriberContractInfo is null for [" + phoneNumber + "]");
			return null;
		}		
		
		try {
			// retrieve the associatedHandset info from subscriber primary equipment to validate Volte device
			EquipmentInfo subscriberEquipment = subscriberInfo.getEquipment0();
			
			if (newHandset == null && subscriberEquipment != null) {
				logger.info("getVolteSocIfEligible: newHandset is null for [" + phoneNumber + "]. Trying to obtain associated handset. associatdHandsetIMEI=["+subscriberEquipment.getAssociatedHandsetIMEI()+"]. subscriberEquipment=["+subscriberEquipment.getSerialNumber()+']');
				newHandset = subscriberEquipment.getAssociatedHandset();
			}  
			
			if (newHandset == null && subscriberEquipment != null && StringUtils.isEmpty(subscriberEquipment.getAssociatedHandsetIMEI()) == false) {
				logger.info("getVolteSocIfEligible: retrieving the newHandset by associatedHandsetIMEI [" + subscriberEquipment.getAssociatedHandsetIMEI()+"]");
				try {
					newHandset = getProductEquipmentHelper().getEquipmentInfobySerialNumber(subscriberEquipment.getAssociatedHandsetIMEI());
				} catch (ApplicationException ae) {
					if (ErrorCodes.UNKNOWN_SERIAL_NUMBER.equals(ae.getErrorCode())) {
						logger.info("Unknown serial number for subscriber [" + phoneNumber + "]. VOLTE SOC not added.");
					}
				}
			}
			
			// there is chance to come usim equipment as handset , to prevent this we added below check before we validate the Volte logic.
			if (newHandset != null && newHandset.isUSIMCard() == false) {
				
				logger.info("getVolteSocIfEligible: new handset serial number : [" + newHandset.getSerialNumber() + "] for [" + phoneNumber + "]");
				
				AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
				int brandId = accountInfo.getBrandId();
				String volteProductFeature = AppConfiguration.getVolteProductFeatureByBrand(brandId);
				
				if (AppConfiguration.isAutoAddVolteEnabled(brandId) && accountInfo.isPostpaid() && accountInfo.isPCS() && newHandset.isHSPA() && 
						getProductEquipmentHelper().isProductFeatureEnabled(newHandset.getProductCode(), volteProductFeature)) {
					ServiceInfo volteSoc = null;
					
					String volteSocCode = AppConfiguration.getVolteSocByBrand(brandId);
						
					if (volteSocCode != null) {
						volteSoc = getReferenceDataFacade().getRegularService(volteSocCode);
					}
					
					if (volteSoc != null && volteSoc.isAvailable() && shouldAddVolteService(subscriberInfo, subscriberContractInfo, pricePlanChange)) {
						return volteSoc;
					}else {
						return null;
					}

				} else {
					logger.info("Ineligible to add VOLTE [" + phoneNumber + "], newHandset=[" + newHandset.getSerialNumber() + "]");
				}
			} else if (newHandset != null && newHandset.isUSIMCard() == true) {
				logger.info("getVolteSocIfEligible:  USIM is not valid valid to add volte soc for  :  " + phoneNumber + ", serial number :  " + newHandset.getSerialNumber());
			} else {
				logger.error("getVolteSocIfEligible: Unable to determine handset for [" + phoneNumber + "]");
			}
			
		} catch (Exception e) {
			logger.error("Unable to add VOLTE SOC for [" + phoneNumber + "]: " + e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * This method is called after a subscriber is determined to be VOLTE capable. It will check if the VOLTE SOC should be added on various checks.
	 * A VOLTE SOC cannot be removed by any transaction per business requirement. If it is attempted to be removed, we will add it back automatically. 
	 * The only case it can be removed is if another VOLTE SOC is being added in the same transaction.
	 * 
	 * @param subscriberInfo
	 * @param subscriberContractInfo
	 * @param pricePlanChange
	 * @return true if VOLTE SOC should be added. false otherwise
	 */
	private boolean shouldAddVolteService(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, boolean pricePlanChange) {
		try {
			List<String> volteServiceDeleteList = new ArrayList<String> ();
			List<String> volteServiceNonDeleteList = new ArrayList<String> ();

			// If a VOLTE SOC is marked as DELETE, we should add it back since this method is triggered only if the client is VOLTE capable. This prevents VOLTE SOC removal.
			// A VOLTE SOC can only be removed if there's another VOLTE SOC being added in the same transaction. This may not be the case in PP configuration but is built for future-proof.
			// If a VOLTE SOC is already specified other than DELETE (ADD, NO_CHG, UPDATE), then we do not need to add the VOLTE SOC.
			String[] transactionSocList = subscriberContractInfo.getServiceCodes0(true);
			ServiceInfo[] referenceServiceInfoList = getReferenceDataFacade().getRegularServices(transactionSocList);
			String subscriberId = subscriberInfo.getSubscriberId();
			
			for (ServiceInfo serviceInfo : referenceServiceInfoList) {
				RatedFeatureInfo[] featureList = serviceInfo.getFeatures0();
				for (RatedFeatureInfo feature : featureList) {
					if (feature.getSwitchCode() != null && StringUtils.equalsIgnoreCase(feature.getSwitchCode().trim(), AppConfiguration.getVolteSwitchCode())) {
						String soc = serviceInfo.getCode();
						logger.info("Matched VOLTE transaction SOC ["+ soc +" with feature swithc code [" + feature.getSwitchCode()+"]. subscriberId=["+subscriberId+"]" );
						ServiceAgreementInfo socInTransaction = subscriberContractInfo.getService0(soc, true);
						
						if (socInTransaction == null) {//unexpected, since this one should exist in current subscriberContractInfo
							logger.info("No VOLTE transaction SOC found for ["+subscriberId+"] [" + soc + "." + feature.getCode() + "] " + subscriberContractInfo);
							return false; //return false for now for this unexpected scenario. socInTransaction variable should not be null.
						}else {
							logger.info("VOLTE in current transaction for [" + subscriberId + "]. [" + soc + "." + feature.getCode() + "." + socInTransaction.getTransaction() + "]");
							
							if (socInTransaction.getTransaction() == BaseAgreementInfo.DELETE) {
								volteServiceDeleteList.add(soc);
							}else { //ADD or NO_CHG
								volteServiceNonDeleteList.add(soc);
							}
						}
					}
				}
			}
			
			if (volteServiceNonDeleteList.isEmpty() == false) {
				logger.info("VOLTE is already in current transaction for [" + subscriberInfo.getSubscriberId() + "].");
				return false;
			}else if (volteServiceDeleteList.isEmpty() == false) {
				logger.info("VOLTE SOC deletion without different VOLTE SOC add is detected for [" + subscriberInfo.getSubscriberId() + "]. Since subscriber is VOLTE capable, returning true for addition.");
				return true;
			}

			
			//If price plan change, all optional socs are dropped anyway so we do not do this check
			//If the current transaction does not mention anything about the VOLTE SOC, we still need to check the database to see if the client already has the SOC.
			if (!pricePlanChange) {
				//If there is already a SOC with "VOLTE" switch code in database, skip automatic add of another SOC with "VOLTE" switch code
				SubscriberContractInfo currentContractBeforeChanges = subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(subscriberInfo.getSubscriberId());
				String[] databaseSocList = currentContractBeforeChanges.getServiceCodes0(false);
				ServiceInfo[] serviceInfoList = getReferenceDataFacade().getRegularServices(databaseSocList);
				for (ServiceInfo serviceInfo : serviceInfoList) {
					RatedFeature[] featureList = serviceInfo.getFeatures();
					for (RatedFeature feature : featureList) {
						if (feature.getSwitchCode() != null && StringUtils.equalsIgnoreCase(feature.getSwitchCode().trim(), AppConfiguration.getVolteSwitchCode())) {
							logger.info("VOLTE already in database for [" + subscriberInfo.getSubscriberId() + "]");
							return false;
						}
					}
				}
			}


		} catch (ApplicationException e) {
			logger.error("[" + e.getErrorCode() + "] Error in shouldAddVolteService for the subscriber:" + subscriberInfo.getSubscriberId(), e);
		} catch (TelusException e) {
			logger.error("[" + e.getMessage() + "] Error in shouldAddVolteService for the subscriber:" + subscriberInfo.getSubscriberId(), e);
		}
		//If there are no issues, add VoLTE
		return true;
	}
	
	@Override
	public boolean addVolteService(ServiceInfo volteSoc, SubscriberContractInfo subscriberContractInfo) {
		if (volteSoc != null && volteSoc.isAvailable()) {
			subscriberContractInfo.addService(volteSoc, new Date());
			return true;
		}
		return false;
	}

	
	
	

	@Override
	public void releaseSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo equipmentInfo, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.releaseSubscriber(subscriberInfo, sessionId);
		asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo, equipmentInfo, subscriberContractInfo, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
	}

	@Override
	public void releasePortedInSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EquipmentInfo equipmentInfo, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.releasePortedInSubscriber(subscriberInfo, sessionId);
		asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo, equipmentInfo, subscriberContractInfo, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
	}

	@Override
	public PortInEligibility checkPortInEligibility(String phoneNumber, String portVisibility, int incomingBrand) throws ApplicationException {
		return eligibilityCheckRequestDao.checkPortInEligibility(phoneNumber, portVisibility, incomingBrand);
	}

	@Override
	public PortRequestInfo[] getCurrentPortRequestsByPhoneNumber(String phoneNumber, int brandId) throws ApplicationException {

		if (phoneNumber == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Phone number cannot be null.", "");
		}

		Province[] provinces = null;
		try {
			provinces = getReferenceDataFacade().getProvinces();
		} catch (Exception ex) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Error while retrieving Provinces from ReferenceFacade", "", ex);
		}
		
		try {
			return portRequestInformationDao.getCurrentPortRequestsByPhoneNumber(phoneNumber, brandId, provinces).toArray(new PortRequestInfo[0]);
		} catch (Exception e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_DAO, e.getMessage() == null ? "Exception in getCurrentPortRequestsByPhoneNumber " + e.toString() : e.getMessage().toString(), "", e);
		}
	}

	@Override
	public PortRequestInfo[] getCurrentPortRequestsByBan(int banNumber) throws ApplicationException {
		
		Province[] provinces = null;
		try {
			provinces = getReferenceDataFacade().getProvinces();
		} catch (Exception ex) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Error while retrieving Provinces from ReferenceFacade", "", ex);
		}
		
		return portRequestInformationDao.getCurrentPortRequestsByBan(banNumber, provinces).toArray(new PortRequestInfo[0]);
	}

	@Override
	public PortRequestSummary checkPortRequestStatus(String phoneNumber, int brandId) throws ApplicationException {
		return portRequestInformationDao.getPortRequestStatus(phoneNumber, brandId);
	}

	@Override
	public void validatePortInRequest(PortRequestInfo portRequest, String applicationId, String user) throws ApplicationException {
		
		Province[] provinces = null;
		try {
			provinces = getReferenceDataFacade().getProvinces();
		} catch (Exception ex) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Error while retrieving Provinces from ReferenceFacade", "", ex);
		}
		if (!portRequestInformationDao.isValidPortInRequest(portRequest, applicationId, user, provinces)) {
			throw new ApplicationException(SystemCodes.CMB_SLF_DAO, "PRM_FALSE", "Port-in request is invalid.", "");
		}
	}

	@Override
	public void activatePortInRequest(PortRequestInfo portRequest, String applicationId) throws ApplicationException {
		actvPortInRequestDao.activatePortInRequest(portRequest, applicationId);
	}

	@Override
	public void cancelPortInRequest(String requestId, String reasonCode, String applicationId) throws ApplicationException {
		cancelPortInRequestDao.cancelPortInRequest(requestId, reasonCode, applicationId);
	}

	@Override
	public String createPortInRequest(SubscriberInfo subscriber, String portProcessType, int incomingBrandId, int outgoingBrandId, String sourceNetwork, String targetNetwork, String applicationId,
			String user, PortRequestInfo portReq) throws ApplicationException {
		
		Province[] provinces = null;
		Brand[] brands = null;
		try {
			provinces = getReferenceDataFacade().getProvinces();
			brands = getReferenceDataFacade().getBrands();
		} catch (Exception ex) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, "Error while retrieving Provinces/Brands from ReferenceFacade", "", ex);
		}
		
		return createPortInRequestDao.createPortInRequest(subscriber, portProcessType, incomingBrandId, outgoingBrandId, sourceNetwork, targetNetwork, applicationId, user, provinces, brands, portReq);
	}

	@Override
	public void submitPortInRequest(String requestId, String applicationId) throws ApplicationException {
		submitPortInRequestDao.submitPortInRequest(requestId, applicationId);
	}

	// Commented for WNP-WLI Upgrade 2012 Oct Release
	/*	@Override
	public void modifyPortInRequest(SubscriberInfo subscriber,
			String applicationId, String user,PortRequestInfo portReq) throws ApplicationException {
		Province[] provinces = null;
		try{
			provinces = getReferenceDataFacade().getProvinces();
		}catch(Exception ex){
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB,"Error while retrieving Provinces from ReferenceFacade","",ex);
		}
		portRequestDao.modifyPortInRequest(subscriber, applicationId, user, provinces, portReq);
	}
	*/
	
	@Override
	public PRMReferenceData[] retrieveReferenceData(String category) throws ApplicationException {
		return portRequestInformationDao.getReferenceData(category).toArray(new PRMReferenceData[0]);
	}

	@Override
	public void testServiceAddToBusinessAnywhereAccount(AccountInfo accountInfo, ServiceInfo serviceInfo) throws ApplicationException {

		char accountType = accountInfo.getAccountType();
		char accountSubType = accountInfo.getAccountSubType();

		if (!((accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS && (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_REGULAR || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL)) || (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE && accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_ANYWHERE))) {

			for (String familyType : serviceInfo.getFamilyTypes()) {
				if (familyType.equals(ServiceSummary.FAMILY_TYPE_CODE_BUSINESS_ANYWHERE)) {
					if (serviceInfo instanceof PricePlanInfo) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ERROR_INCOMPATIBLE_PRICEPLAN_ACCOUNT,
								"PricePlan to be added to subscriber is not compatible with the account: AccountType[" + accountType + "] AccountSubType[" + accountSubType + "] PricePlan["
										+ serviceInfo.getCode().trim() + "] familyType[" + familyType + "]", "");
					}

					if (serviceInfo instanceof ServiceInfo) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ERROR_INCOMPATIBLE_SERVICE_ACCOUNT,
								"Service to be added to subscriber is not compatible with the account: AccountType[" + accountType + "] AccountSubType[" + accountSubType + "] Service["
										+ serviceInfo.getCode().trim() + "] familyType[" + familyType + "]", "");
					}
				}
			}
		}
	}

	@Override
	public CallingCircleParametersInfo getCallingCircleInformation(int billingAccountNumber, String subscriberNumber, String serviceCode0, String featureCode0, String productType, String sessionId)
			throws ApplicationException {
		CallingCircleParameters callingCircleParametersInfo = null;
		String serviceCode = Info.padService(serviceCode0);
		String featureCode = Info.padFeature(featureCode0);

		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberNumber);
		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(billingAccountNumber);
		SubscriberContractInfo subscriberContractInfo = this.getBaseServiceAgreement(subscriberInfo, accountInfo);

		ContractUtilities.checkServiceFeatureInfo(subscriberContractInfo, serviceCode, featureCode);
		try {
			FeatureInfo featureInfo = getReferenceDataFacade().getFeature(featureCode);
			if (featureInfo.isCallingCircle() & accountInfo.isPostpaid()) {
				callingCircleParametersInfo = subscriberLifeCycleManager.retrieveCallingCircleParameters(billingAccountNumber, subscriberNumber, serviceCode, featureCode, productType, sessionId);
			} else if (!accountInfo.isPostpaid()) {
				ServiceInfo prepaidServiceInfo = getReferenceDataFacade().getWPSService(serviceCode);
				String kbServiceCode = (prepaidServiceInfo != null && prepaidServiceInfo.isWPS()) ? prepaidServiceInfo.getWPSMappedKBSocCode() : serviceCode;
				ServiceAgreementInfo kbService = subscriberContractInfo.getService0(kbServiceCode, false);
				if (kbService == null) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_KB_TO_PREPAID_SERVICE_ASSOCIATION, "Associated KB Service Code was not found for Prepaid Service "
							+ serviceCode, "");
				}

				ServiceInfo kbServiceInfo = getReferenceDataFacade().getRegularService(kbServiceCode);
				RatedFeatureInfo kbFeatureInfo = ContractUtilities.getKbCallingCircleFeature(kbServiceInfo.getFeatures0());
				if (kbFeatureInfo == null) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_KB_TO_PREPAID_SERVICE_ASSOCIATION,
							"Associated KB Calling Circle Feature Code was not found for Prepaid Service " + serviceCode, "");
				}

				ServiceFeatureInfo kbFeature = kbService.getFeature0(kbFeatureInfo.getCode(), false);
				String kbFtrCategoryCode = kbFeatureInfo.getCategoryCode();
				if (kbFtrCategoryCode.equalsIgnoreCase(Feature.CATEGORY_CODE_CALLING_CIRCLE)) {
					callingCircleParametersInfo = subscriberLifeCycleManager.retrieveCallingCircleParameters(billingAccountNumber, subscriberNumber, kbService.getCode(), kbFeature.getCode(),
							productType, sessionId);
				} else if (kbFtrCategoryCode.equalsIgnoreCase(Feature.CATEGORY_CODE_CALL_HOME_FREE)) {
					callingCircleParametersInfo = ContractUtilities.getCallingCircleParametersInfo(kbFeature);
				}
			}
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
		} catch (UnknownObjectException uoe) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.UNKNOWN_SERVICE, "Service Code [" + serviceCode + "] is not available on Subscriber Service Agreement", "");
		}

		return (CallingCircleParametersInfo) callingCircleParametersInfo;
	}

	@Override
	public SubscriberContractInfo getBaseServiceAgreement(String subscriberId, int billingAccountNumber) throws ApplicationException {
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(billingAccountNumber);
		return getBaseServiceAgreement(subscriberInfo, accountInfo);
	}

	/**
	 * Implementation based on TMSubscriber:getContract0()
	 * 
	 * This method is currently not exposed in the interface. (Remove this comment and add annotation if this has changed)
	 */
	public SubscriberContractInfo getBaseServiceAgreement(SubscriberInfo subscriberInfo, AccountInfo accountInfo) throws ApplicationException {
		if (subscriberInfo == null || accountInfo == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Subscriber or Account cannot be null.", "");
		}

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}

		//		SubscriberContractInfo info = subscriberLifecycleHelper.retrieveServiceAgreementByPhoneNumber(subscriberInfo.getPhoneNumber());
		SubscriberContractInfo info = subscriberLifecycleHelper.retrieveServiceAgreementBySubscriberId(subscriberInfo.getSubscriberId());
		info.setCommitmentReasonCode(subscriberInfo.getCommitment().getReasonCode());
		info.setCommitmentMonths(subscriberInfo.getCommitment().getMonths());
		info.setCommitmentStartDate(subscriberInfo.getCommitment().getStartDate());
		info.setCommitmentEndDate(subscriberInfo.getCommitment().getEndDate());

		// Retrieve WPS services for prepaid account.
		//------------------------------------------------------------------------------
		if (!accountInfo.isPostpaid()) {
			ServiceAgreementInfo[] wpsServices = subscriberLifecycleHelper.retrieveFeaturesForPrepaidSubscriber(subscriberInfo.getPhoneNumber());

			int wpsServicesNo = wpsServices != null ? wpsServices.length : 0;

			for (int i = 0; i < wpsServicesNo; i++) {
				ServiceAgreementInfo sa = wpsServices[i];
				sa.setTransaction(BaseAgreementInfo.NO_CHG);
				info.addService(sa);
			}
		}
		info.getCommitment().setModified(false);
		if (subscriberInfo.isPCS() && ContractUtilities.containsFeature(info, Feature.CATEGORY_CODE_MULTI_RING)) {
			info.setMultiRingPhoneNumbers(subscriberLifecycleHelper.retrieveMultiRingPhoneNumbers(subscriberInfo.getSubscriberId()));
		}
		return info;
	}

	@Override
	public void activateReservedSubscriber(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, Date startServiceDate, String activityReasonCode,
			ServicesValidation srvValidation, String portProcessType, int oldBanId, String oldSubscriberId, String sessionId) throws ApplicationException {
		
		subscriberLifeCycleManager.activateReservedSubscriber(subscriberInfo, subscriberContractInfo, startServiceDate, activityReasonCode, srvValidation, portProcessType, oldBanId, oldSubscriberId,sessionId);
		
		if (!AppConfiguration.isActivationPostTaskRollback()) {
			logger.debug("activationPostTaskRollBack flag set to false  , begin the activateReservedSubscriber - activationPostTask.apply");
			// execute reservedSubscriber activation post tasks
			ActivationPostTask postTask = new ActivationPostTask(subscriberEventPublisher,identityProfileService);
			postTask.initialize(subscriberInfo,subscriberContractInfo, false, null,null, true,true, getPopulatedAuditInfo(null, sessionId), sessionId);
			postTask.applyReservedSubscriberPostTasks();
		}  else{
			logger.debug("activationPostTaskRollBack flag set to true  , begin the activateReservedSubscriber post tasks through old code");

			asyncUpdateProductInstance(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_ACTIVATION,
					sessionId);

			AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
			identityProfileService.registerConsumerProfile(subscriberInfo, accountInfo, IdentityProfileRegistrationOrigin.SUBSCRIBER_ACTIVATION);
		}
		
	}

	@Override
	public void updateCommitment(SubscriberInfo pSubscriberInfo, CommitmentInfo pCommitmentInfo, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.updateCommitment(pSubscriberInfo, pCommitmentInfo, dealerCode, salesRepCode, sessionId);
		asyncUpdateProductInstance(pSubscriberInfo.getBanId(), pSubscriberInfo.getSubscriberId(), pSubscriberInfo.getPhoneNumber(), ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_UPDATE, sessionId);
	}

	@Override
	public void asyncReportChangeSubscriberStatus(ChangeSubscriberStatusActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);
	}

	@Override
	public void asyncReportChangeEquipment(ChangeEquipmentActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangeContract(ChangeContractActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangePhoneNumber(ChangePhoneNumberActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportMoveSubscriber(MoveSubscriberActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangeAccountType(ChangeAccountTypeActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangeAccountAddress(ChangeAccountAddressActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangeAccountPin(ChangeAccountPinActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);

	}

	@Override
	public void asyncReportChangePaymentMethod(ChangePaymentMethodActivity activity) throws ApplicationException {
		queueSender.send(activity, "msgSubTypeActivityLoggingService", null);
	}

	@Override
	public void reportMoveSubscriber(int oldBanId, int newBanId, String subscriberId, String dealerCode, String salesRepCode, String userId, String phoneNumber, char subscriberStatus,
			Date subscriberActivationDate, String reason, ServiceRequestHeader header) {

		try {
			MoveSubscriberActivity activity = new MoveSubscriberActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);
			activity.setBanId(oldBanId);
			activity.setNewBanId(newBanId);
			activity.setSubscriberId(subscriberId);
			activity.setPhoneNumber(phoneNumber);
			activity.setReason(reason);
			activity.setSubscriberActivationDate(subscriberActivationDate);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logMoveSubscriberActivity(activity);
			} else {
				asyncReportMoveSubscriber(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportMoveSubscriber :  Unable to write Move subscriber change service request to SRPDS -[" + subscriberId + "/" + oldBanId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangeAccountType(int banId, String dealerCode, String salesRepCode, String userId, char accountStatus, char oldAccountType, char newAccountType, char oldAccountSubType,
			char newAccountSubType, ServiceRequestHeader header) {

		try {
			ChangeAccountTypeActivity activity = new ChangeAccountTypeActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);
			activity.setBanId(banId);
			activity.setOldAccountType(oldAccountType);
			activity.setNewAccountType(newAccountType);
			activity.setOldAccountSubType(oldAccountSubType);
			activity.setNewAccountSubType(newAccountSubType);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangeAccountTypeActivity(activity);
			} else {
				asyncReportChangeAccountType(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeAccountType :  Unable to write account type change service request to SRPDS - [" + banId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangeAccountAddress(int banId, String dealerCode, String salesRepCode, String userId, Address address, ServiceRequestHeader header) {
		try {
			ChangeAccountAddressActivity activity = new ChangeAccountAddressActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);
			activity.setBanId(banId);
			activity.setAddress(address);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangeAccountAddressActivity(activity);
			} else {
				asyncReportChangeAccountAddress(activity);
			}
		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeAccountAddress :  Unable to write account address change service request to SRPDS - [" + banId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangeAccountPin(int banId, String dealerCode, String salesRepCode, String userId, ServiceRequestHeader header) {
		try {
			ChangeAccountPinActivity activity = new ChangeAccountPinActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);
			activity.setBanId(banId);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangeAccountPinActivity(activity);
			} else {
				asyncReportChangeAccountPin(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeAccountPin :  Unable to write account pin change service request to SRPDS - [" + banId + "][" + t + "]");
			logger.error(t);
		}

	}

	@Override
	public void reportChangePaymentMethod(int banId, String dealerCode, String salesRepCode, String userId, PaymentMethod paymentMethod, ServiceRequestHeader header) {
		try {
			ChangePaymentMethodActivity activity = new ChangePaymentMethodActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);
			activity.setBanId(banId);
			activity.setPaymentMethodCode(paymentMethod.getPaymentMethod());

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangePaymentMethodActivity(activity);
			} else {
				asyncReportChangePaymentMethod(activity);
			}
		} catch (Throwable t) {
			logger.warn("ERROR: reportChangePaymentMethod :  Unable to write payment method change service request to SRPDS -[" + banId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangeEquipment(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, Equipment oldEquipment, Equipment newEquipment, String repairId,
			String swapType, Equipment oldAssociatedMuleEquipment, Equipment newAssociatedMuleEquipment, com.telus.api.servicerequest.ServiceRequestHeader header) {

		try {
			ChangeEquipmentActivity activity = new ChangeEquipmentActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);

			activity.setBanId(banId);
			activity.setSubscriberId(subscriberId);
			activity.setOldEquipment(oldEquipment);
			activity.setNewEquipment(newEquipment);
			activity.setOldAssociatedMuleEquipment(oldAssociatedMuleEquipment);
			activity.setNewAssociatedMuleEquipment(newAssociatedMuleEquipment);
			activity.setRepairId(repairId);
			activity.setSwapType(swapType);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangeEquipmentActivity(activity);
			} else {
				asyncReportChangeEquipment(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeEquipment :  Unable to write change equipment service request to SRPDS - [" + subscriberId + "/" + banId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangeEquipment(String subscriberId, int billingAccountNumber, EquipmentChangeRequestInfo equipmentChangeRequest, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException {

		ClientIdentity ci = getClientIdentity(sessionId);

		reportChangeEquipment(billingAccountNumber, subscriberId, equipmentChangeRequest.getDealerCode(), equipmentChangeRequest.getSalesRepCode(), ci.getPrincipal(),
				equipmentChangeRequest.getCurrentEquipment(), equipmentChangeRequest.getNewEquipment(), equipmentChangeRequest.getRepairId(), equipmentChangeRequest.getSwapType(),
				equipmentChangeRequest.getCurrentAssociatedHandset(), equipmentChangeRequest.getAssociatedMuleEquipment(), header);
	}

	@Override
	public void reportChangeContract(int banId, String subscriberId, String dealerCode, String salesRepCode, String userId, SubscriberContractInfo newContractInfo,
			SubscriberContractInfo oldContractInfo, ContractService[] addedServices, ContractService[] removedServices, ContractService[] updatedServices, ContractFeature[] updatedFeatures,
			com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {

			ChangeContractActivity activity = new ChangeContractActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);

			activity.setBanId(banId);
			activity.setSubscriberId(subscriberId);
			activity.setNewContract(newContractInfo);
			activity.setOldContract(oldContractInfo);
			activity.setAddedServices(addedServices);
			activity.setRemovedServices(removedServices);
			activity.setUpdatedServices(updatedServices);
			activity.setUpdatedFeatures(updatedFeatures);

			if (newContractInfo.getEffectiveDate() != null) {
				activity.setDate(newContractInfo.getEffectiveDate());
			}

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangeContractActivity(activity);
			} else {
				asyncReportChangeContract(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeContract :  Unable to write change contract service request to SRPDS -[" + subscriberId + "/" + banId + "][" + t + "]");
			logger.error(t);
		}
	}
	
	@Override
	public void reportChangeContract(String subscriberId, int ban, ContractChangeInfo changeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId)
			throws ApplicationException {

		ClientIdentity ci = getClientIdentity(sessionId);

		reportChangeContract(ban, subscriberId, changeInfo.getDealerCode(), changeInfo.getSalesRepCode(), ci.getPrincipal(), changeInfo.getNewSubscriberContractInfo(),
				changeInfo.getCurrentContractInfo(), changeInfo.getNewSubscriberContractInfo().getAddedServices(), changeInfo.getNewSubscriberContractInfo().getDeletedServices(), changeInfo
						.getNewSubscriberContractInfo().getChangedServices(), changeInfo.getNewSubscriberContractInfo().getChangedFeatures(), header);

	}

	@Override
	public void reportChangePhoneNumber(int banId, String subscriberId, String newSubscriberId, String dealerCode, String salesRepCode, String userId, String oldPhoneNumber, String newPhoneNumber,
			com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			ChangePhoneNumberActivity activity = new ChangePhoneNumberActivity(header);
			activity.setActors(dealerCode, salesRepCode, userId);

			activity.setBanId(banId);
			activity.setSubscriberId(subscriberId);
			activity.setNewSubscriberId(newSubscriberId);
			activity.setOldPhoneNumber(oldPhoneNumber);
			activity.setNewPhoneNumber(newPhoneNumber);

			if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
				getActivityLoggingService().logChangePhoneNumberActivity(activity);
			} else {
				asyncReportChangePhoneNumber(activity);
			}

		} catch (Throwable t) {
			logger.warn("ERROR: reportChangePhoneNumber:  Unable to write change phone number service request to SRPDS -[" + subscriberId + "/" + banId + "][" + t + "]");
			logger.error(t);
		}
	}

	@Override
	public void reportChangePhoneNumber(int ban, PhoneNumberChangeInfo phoneNumberChangeInfo, com.telus.api.servicerequest.ServiceRequestHeader header, String sessionId) throws ApplicationException {

		ClientIdentity ci = getClientIdentity(sessionId);

		reportChangePhoneNumber(ban, phoneNumberChangeInfo.getSubscriberId(), phoneNumberChangeInfo.getNewSubscriberId(), phoneNumberChangeInfo.getDealerCode(),
				phoneNumberChangeInfo.getSalesRepCode(), ci.getPrincipal(), phoneNumberChangeInfo.getOldNumberGroup().getPhoneNumber(), phoneNumberChangeInfo.getNewPhoneNumber(), header);
	}

	@Override
	public void reportChangeSubscriberStatus(String subscriberId, int billingAccountNumber, SubscriberLifecycleInfo subscriberLifecycleInfo, com.telus.api.servicerequest.ServiceRequestHeader header,
			String sessionId) throws ApplicationException {

		ClientIdentity ci = getClientIdentity(sessionId);
		SubscriberInfo subscriber = subscriberLifecycleHelper.retrieveSubscriber(billingAccountNumber, subscriberId);

		reportChangeSubscriberStatus(billingAccountNumber, subscriber, subscriberLifecycleInfo.getDealerCode(), subscriberLifecycleInfo.getSalesRepCode(), ci.getPrincipal(), subscriberLifecycleInfo
				.getOldSubscriberInfo().getStatus(), subscriber.getStatus(), subscriberLifecycleInfo.getReasonCode(), new Date(), header);
	}

	@Override
	public void reportChangeSubscriberStatus(int banId, SubscriberInfo subscriber, String dealerCode, String salesRepCode, String userId, char oldSubscriberStatus, char newSubscriberStatus,
			String reason, Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header) {
		try {
			reportChangeSubscriberStatusInSRPDS(banId, subscriber, dealerCode, salesRepCode, userId, oldSubscriberStatus, newSubscriberStatus, reason, activityDate, header);
		} catch (Throwable t) {
			logger.warn("ERROR: reportChangeSubscriberStatus(int, SubscriberInfo..): Unable to write subscriber status change service request to SRPDS [" + subscriber != null ? subscriber
					.getSubscriberId() : "null" + "/" + banId + "]- [" + t + "]");
			logger.error(t);
		}
	}

	private void reportChangeSubscriberStatusInSRPDS(int banId, SubscriberInfo subscriber, String dealerCode, String salesRepCode, String userId, char oldSubscriberStatus, char newSubscriberStatus,
			String reason, Date activityDate, com.telus.api.servicerequest.ServiceRequestHeader header) throws ApplicationException {
		logger.debug("Entering method SubscriberLifecycleFacade.reportChangeSubscriberStatus(int, SubscriberInfo..)...");

		ChangeSubscriberStatusActivity activity = new ChangeSubscriberStatusActivity(header);
		activity.setActors(dealerCode, salesRepCode, userId);

		activity.setBanId(banId);
		activity.setSubscriberId(subscriber.getSubscriberId());
		activity.setNewSubscriberStatus(newSubscriberStatus);
		activity.setOldSubscriberStatus(oldSubscriberStatus);
		activity.setReason(reason);
		activity.setPhoneNumber(subscriber.getPhoneNumber());
		activity.setSubscriberActivationDate(subscriber.getStartServiceDate());
		if (activityDate != null) {
			activity.setDate(activityDate);
		} else {
			activity.setDate(new Date()); //this must be set. Otherwise, the effective date in header will be null and SRPDS will throw a ValidateException
		}

		if (!AppConfiguration.isAsynchronousInvoke_SRPDS()) {
			getActivityLoggingService().logChangeSubscriberStatusActivity(activity);
		} else {
			asyncReportChangeSubscriberStatus(activity);
		}
		logger.debug("Exiting method SubscriberLifecycleFacade.reportChangeSubscriberStatus(int, SubscriberInfo..)...");
	}

	@Override
	public void asyncLogChangeRole(RoleChangeInfo roleChangeInfo) throws ApplicationException {
		queueSender.send(roleChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogMakePayment(BillPaymentInfo billPaymentInfo) throws ApplicationException {
		queueSender.send(billPaymentInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangePricePlan(com.telus.eas.config.info.PricePlanChangeInfo pricePlanChangeInfo) throws ApplicationException {
		queueSender.send(pricePlanChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangeAddress(com.telus.eas.config.info.AddressChangeInfo addressChangeInfo) throws ApplicationException {
		queueSender.send(addressChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogSubscriberNewCharge(com.telus.eas.config.info.SubscriberChargeInfo subscriberChargeInfo) throws ApplicationException {
		queueSender.send(subscriberChargeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangePaymentMethod(com.telus.eas.config.info.PaymentMethodChangeInfo paymentMethodChangeInfo) throws ApplicationException {
		queueSender.send(paymentMethodChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogAccountStatusChange(com.telus.eas.config.info.AccountStatusChangeInfo accountStatusChangeInfo) throws ApplicationException {
		queueSender.send(accountStatusChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogPrepaidAccountTopUp(com.telus.eas.config.info.PrepaidTopupInfo prepaidTopupInfo) throws ApplicationException {
		queueSender.send(prepaidTopupInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangePhoneNumber(com.telus.eas.config.info.PhoneNumberChangeInfo phoneNumberChangeInfo) throws ApplicationException {
		queueSender.send(phoneNumberChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangeService(com.telus.eas.config.info.ServiceChangeInfo serviceChangeInfo) throws ApplicationException {
		queueSender.send(serviceChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogChangeSubscriber(com.telus.eas.config.info.SubscriberChangeInfo subscriberChangeInfo) throws ApplicationException {
		queueSender.send(subscriberChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public void asyncLogSubscriberChangeEquipment(com.telus.eas.config.info.EquipmentChangeInfo equipmentChangeInfo) throws ApplicationException {
		queueSender.send(equipmentChangeInfo, "msgSubTypeConfigurationManager", null);

	}

	@Override
	public SubscriberContractInfo getBaseServiceAgreement(String phoneNumber) throws ApplicationException {
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phoneNumber);

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}
		AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(subscriberInfo.getBanId());
		return getBaseServiceAgreement(subscriberInfo, accountInfo);
	}

	@Override
	public ContractChangeInfo getServiceAgreementForUpdate(String phoneNumber) throws ApplicationException {

		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(phoneNumber);

		if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot getContract on cancelled subscriber.", "");
		}
		return getContractChangeInfo(subscriberInfo, subscriberInfo.getBanId());

	}

	public CallingCircleEligibilityEvaluationResult getCallingCircleEligibilityReuslt(BaseChangeInfo changeInfo) throws ApplicationException {
		
		CallingCircleEligibilityCheckCriteria criteria = new CallingCircleEligibilityCheckCriteria();
		AccountInfo accountInfo = changeInfo.getCurrentAccountInfo();
		criteria.setAccountCombinedType("" + accountInfo.getAccountType() + accountInfo.getAccountSubType());
		criteria.setBrandId(accountInfo.getBrandId());
		criteria.setProductType(changeInfo.getCurrentSubscriberInfo().getProductType());
		CallingCircleEligibilityEvaluationResult result = (CallingCircleEligibilityEvaluationResult) CallingCircleEligibilityEvaluationStrategy.getInstance().evaluate(criteria);

		return result;

	}
	
	private Date getLogicalDate() {
		try {
			return getReferenceDataFacade().getLogicalDate();
		} catch (Throwable t) {
			throw new SystemException(SystemCodes.CMB_SLF_EJB, t.getMessage(), "", t);
		}
	}
	
	private void marshalContractChangeInfo(ContractChangeInfo changeInfo, boolean skipServiceAddValidation) throws ApplicationException {

		ContractChangeContext changeContext = getContractChangeContext(changeInfo, null);
		changeContext.setSkipSubscriberAndServiceCompatibilityTest(skipServiceAddValidation);

		if (changeInfo.getPreviousSubscriberInfo() == null && changeContext.getPreviousSubscriber() != null) {
			changeInfo.setPreviousSubscriberInfo(changeContext.getPreviousSubscriber().getDelegate());
		}
		if (changeInfo.getCurrentSubscriberInfo() == null && changeContext.getCurrentSubscriber() != null) {
			changeInfo.setCurrentSubscriberInfo(changeContext.getCurrentSubscriber().getDelegate());
		}
		if (changeInfo.getCurrentAccountInfo() == null && changeContext.getCurrentAccount() != null) {
			changeInfo.setCurrentAccountInfo(changeContext.getCurrentAccount().getDelegate());
		}
		if (changeInfo.getCurrentContractInfo() == null && changeContext.getNewContract() != null) {
			updateSubscriberContractInfo(changeContext.getNewContract(), changeInfo);
			changeInfo.setCurrentContractInfo(changeContext.getNewContract().getDelegate());
		}
	}

	/*
	 * Required information in ContractChangeInfo
	 *    ban, subscriberId
	 *    or
	 *    currentAccountInfo, currentSubscrierInfo, currentContractInfo
	 * 
	 *  migration, following attributes  are also required
	 *    previousBan / previousSubscriberId
	 *    or
	 *    privouseSubscriberInfo
	 */
	@Override
	public SubscriberContractInfo prepopulateCallingCircleList(ContractChangeInfo changeInfo) throws ApplicationException {

		marshalContractChangeInfo(changeInfo, true);

		SubscriberInfo historySubscriber = (changeInfo.getPreviousSubscriberInfo() != null) ? changeInfo.getPreviousSubscriberInfo() : changeInfo.getCurrentSubscriberInfo();

		SubscriberContractInfo contractInfo = changeInfo.getCurrentContractInfo();
		ServiceFeatureInfo[] contractFeatures = contractInfo.getEmptyCCListFeatures();

		if (logger.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("Prepopulate CC list for ").append(changeInfo.toString());
			for (ServiceFeatureInfo feature : contractFeatures) {
				sb.append(feature.getServiceCode().trim()).append("/").append(feature.getFeatureCode().trim()).append("; ");
			}
			sb.append("]");
			logger.info(sb);
		}

		if (contractFeatures.length > 0) {

			CallingCircleEligibilityEvaluationResult ccEligibility = getCallingCircleEligibilityReuslt(changeInfo);
			if (ccEligibility.isCarryOverInd() == false) {
				return contractInfo;
			}

			Date logicalDate = getLogicalDate();

			Date from = DateUtil.addDay(logicalDate, (0 - ccEligibility.getCarryOverPeriod()));

			FeatureParameterHistoryInfo[] parameters = subscriberLifecycleHelper.retrieveCallingCircleParametersByDate(historySubscriber.getBanId(), historySubscriber.getSubscriberId(),
							historySubscriber.getProductType(), from);

			CallingCircleUtilities ccUtils = new CallingCircleUtilities(ccEligibility.getMaxAllowedChangesPerPeriod(), logicalDate, changeInfo.getCurrentAccountInfo().getBillCycleCloseDay(),
					changeInfo.getCurrentAccountInfo().isPostpaid());

			FeatureTransactionContext[] features = FeatureTransactionContext.newFeatureTransactionContexts(contractFeatures, contractInfo, changeInfo.isPricePlanChangeInd(), logicalDate);

			ccUtils.populateCallingCircleInfo(features, parameters, contractInfo);

		}

		// enhancement: so that any existing CC feature will have commitment
		// attribute data populated,
		evaluateCallingCircleCommitmentAttributeData(changeInfo);

		return contractInfo;
	}

	/*
	 * Required information in ContractChangeInfo
	 *    currentAccountInfo, currentSubscrierInfo, currentContractInfo
	 */
	@Override
	public SubscriberContractInfo evaluateCallingCircleCommitmentAttributeData(ContractChangeInfo changeInfo) throws ApplicationException {

		SubscriberContractInfo contractInfo = changeInfo.getCurrentContractInfo();
		ServiceFeatureInfo[] contractFeatures = contractInfo.getNullCCCommitmentDataFeatures();

		if (logger.isInfoEnabled()) {
			StringBuilder sb = new StringBuilder("evaluate CC commitment for ").append(changeInfo.toString());
			sb.append("  features[");
			for (ServiceFeatureInfo feature : contractFeatures) {
				sb.append(feature.getServiceCode().trim()).append("/").append(feature.getFeatureCode().trim()).append("; ");
			}
			sb.append("]");
			logger.info(sb);
		}

		if (contractFeatures.length == 0) {
			return contractInfo;
		}

		CallingCircleEligibilityEvaluationResult ccEligibility = getCallingCircleEligibilityReuslt(changeInfo);
		if (ccEligibility.isCommitmentRulesInd() == false) {
			return contractInfo;
		}

		Date logicalDate = getLogicalDate();

		CallingCircleUtilities ccUtils = new CallingCircleUtilities(ccEligibility.getMaxAllowedChangesPerPeriod(), getLogicalDate(), changeInfo.getCurrentAccountInfo().getBillCycleCloseDay(),
				changeInfo.getCurrentAccountInfo().isPostpaid());

		FeatureTransactionContext[] features = FeatureTransactionContext.newFeatureTransactionContexts(contractFeatures, contractInfo, changeInfo.isPricePlanChangeInd(), logicalDate);

		FeatureParameterHistoryInfo[] parameters = null;
		Date from = changeInfo.getCurrentSubscriberInfo().getStartServiceDate();
		if (from == null) {
			//no start services date means this is activation or migration
			ccUtils.populateCommitmentData(features);
		} else {
			//365 days is more than enough, the normal future dated PP change is the start date of next bill cycle.
			Date to = DateUtil.addDay(logicalDate, 365);

			parameters = subscriberLifecycleHelper.retrieveFeatureParameterHistory(changeInfo.getCurrentSubscriberInfo().getBanId(), changeInfo.getCurrentSubscriberInfo().getSubscriberId(),
					changeInfo.getCurrentSubscriberInfo().getProductType(), new String[] { "CALLING-CIRCLE" }, from, to);
			ccUtils.evaluateCommitmentData(features, parameters);
		}
		return contractInfo;
	}
	
	@Override
	public SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		// logger.debug(" *** In SubscriberLifecycleFacadeImpl old cancelPortOutSubscriber(), cancellationInd will be set as false.");
		return cancelPortOutSubscriber(incomingBan, phoneNumber, effectiveDate, forceCancelImmediateIndicator, subLifecycleInfo, header, notificationSuppressionInd, auditInfo, sessionId, false);
	}

	@Override
	public SubscriberLifecycleInfo cancelPortOutSubscriber(String incomingBan, String phoneNumber, Date effectiveDate, boolean forceCancelImmediateIndicator, SubscriberLifecycleInfo subLifecycleInfo,
			ServiceRequestHeader header, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId, Boolean cancellationInd) throws ApplicationException {
				
		CommunicationSuiteInfo commSuiteInfo = null;
		SubscriberInfo subInfo = PortoutUtilities.getSubscriberInfo(incomingBan, phoneNumber, subscriberLifecycleHelper);
		subLifecycleInfo.setOldSubscriberInfo(subInfo);
		Date logicalDate = PortoutUtilities.getLogicalDate(getReferenceDataHelper());
		PortoutHelper portoutHelper = new PortoutHelper(ejbController, subInfo.getBanId(), phoneNumber, logicalDate, subLifecycleInfo, getClientIdentity(sessionId), sessionId);

		try {
			// step 1: validateSubscriber and suspend the subscriber before cancel
			portoutHelper.checkSubscriberStatusAndRestoreSuspendSubscriber(subInfo);

			AccountInfo accInfo = getAccountInformationHelper().retrieveAccountByBan(subInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
			Date billCycleCloseDate = PortoutUtilities.getBillCycleCloseDate(accInfo.getBillCycleCloseDay(), effectiveDate);
			Date activityDate = logicalDate;
			// set the portOut Indicator as "Y" if cancellation indicator is not provided or false
			String portOutInd = "N";
			if (cancellationInd == null || !cancellationInd) {
					portOutInd = "Y";
			}					
			// step 2 : retrieve the communication suite to cancel the companion subscribers if it is primary cancel port out. 
		    // Note : companions should cancel as non port out.
			if (CommunicationSuiteEligibilityUtil.validateCommunicationSuiteEligibility(accInfo.getBrandId(), accInfo.getAccountType(), accInfo.getAccountSubType())) {
				commSuiteInfo = subscriberLifecycleHelper.retrieveCommunicationSuite(subInfo.getBanId(), phoneNumber, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
			}
									
			// step 3: cancel the subscriber/account if cancelImmediately set to true or meet any of the below conditions.
			char accStatus;
			if (PortoutUtilities.cancelImmediately(forceCancelImmediateIndicator, subInfo, effectiveDate, billCycleCloseDate, accInfo.getAccountType())) {
				// cancel immediately
				accStatus = PortoutUtilities.getAccountStatusChangeAfterCancel(accInfo, subInfo, commSuiteInfo);
			} else {
				// suspend before cancel
				portoutHelper.suspendBeforeCancel(subInfo, accInfo, portOutInd, commSuiteInfo);
				
				// refresh the communication suite after suspension of companion subscribers if the suite is not null.
				if(commSuiteInfo!=null){
					commSuiteInfo = subscriberLifecycleHelper.retrieveCommunicationSuite(subInfo.getBanId(), phoneNumber, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
				}

				accStatus = PortoutUtilities.getAccountStatusChangeAfterCancel(accInfo, subInfo, commSuiteInfo);
				activityDate = effectiveDate.before(billCycleCloseDate) ? billCycleCloseDate : PortoutUtilities.getNextBillCycleCloseDate(accInfo, effectiveDate);
				// Since we have suspended the subscriber with port out indicator as "Y" ,so set the  port out indicator as "N" for cancellation.
				portOutInd = "N";
			}
			// step 4: perform the cancellation based on account status 
			portoutHelper.updateAccountAndSubscriberForPortOut(subInfo, accStatus, portOutInd, activityDate,commSuiteInfo);
			if (header != null) {
				try {
					SubscriberInfo newSubInfo = subscriberLifecycleHelper.retrieveSubscriber(subInfo.getBanId(),subInfo.getSubscriberId());
					
					reportChangeSubscriberStatusInSRPDS(subInfo.getBanId(),newSubInfo,newSubInfo.getDealerCode(),newSubInfo.getSalesRepId(),
							getClientIdentity(sessionId).getPrincipal(),subLifecycleInfo.getOldSubscriberInfo().getStatus(),newSubInfo.getStatus(),
							subLifecycleInfo.getReasonCode(), activityDate,header);
					
				} catch (ApplicationException e) {
					portoutHelper.addWarning("300115","Report subscriber status change to SRPDS failed. "+ e.getStackTrace());
				}
			}
			
			// step 5: close koodo HPA account 
			portoutHelper.closeHPAAccount(activityDate);

			// step 6 : send the business connect seat cancel order
			if (subInfo.isSeatSubscriber()) {
				activityDate = activityDate!=null ? activityDate : getLogicalDate();
				asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createSeatCancelRequest(subInfo, activityDate));
			}
			
		} catch (ApplicationException e) {
			if ("APP20002".equals(e.getErrorCode())) {
				portoutHelper.cancelAdditionMsisdn(subInfo.getBanId(),phoneNumber, e);
			}
			throw e;
		}

		return portoutHelper.getSubscriberLifecycleInfo();
	}

	@Override
	public void deactivateMVNESubcriber(String phoneNumber) throws ApplicationException {
		if (AppConfiguration.isRedKneeSubMgmtWsRollback()) {
			Date logicalDate = getLogicalDate();
			deactivateMVNESubscriberRequestDao.deactivateMVNESubcriber(phoneNumber, logicalDate);
		} else {
			redkneeSubscriptionMgmtServiceDao.updateSubscriptionWithStateTransition(phoneNumber);
		}
	}

	@Override
	public TestPointResultInfo testConsumerProductDataManagementService() {
		return productDataMgmtDao.test();
	}

	@Override
	public TestPointResultInfo testLogicalResourceService() {
		return logicalResourceServiceDao.test();
	}

	@Override
	public TestPointResultInfo testMinMdnService() {
		return minMdnServiceDao.test();
	}

	@Override
	public TestPointResultInfo testWirelessProvisioningService() {
		return wirelessProvisioningServiceDao.test();
	}

	@Override
	public TestPointResultInfo testVOIPSupplementaryService() {
		return voipSupplementaryServiceDao.test();
	}
	
	@Override 
	public TestPointResultInfo testPenaltyCalculationService() {
		return penaltyCalculationServiceDao.test();
	}
	
	@Override
	public TestPointResultInfo testPortRequestInformationService() {
		return null;
	}

	@Override
	public TestPointResultInfo testEligibilityCheckService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestPointResultInfo testCancelPortInRequestSoap() {
		return null;
	}

	@Override
	public TestPointResultInfo testActivatePortInRequestService() {
		return null;
	}

	@Override
	public TestPointResultInfo testCreatePortInRequestService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestPointResultInfo testSubmitPortInRequestService() {
		return null;
	}

	@Override
	public TestPointResultInfo testPortabilityService() {
		return null;
	}
	
	@Override
	public TestPointResultInfo testProvisioningOrderLookup(String transactionNo, String subscriberId) {

		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		resultInfo.setTestPointName("ProvisioningOrderLookup EJB");
		try {
			ProvisioningOrderLookupRemote provOrderLookup = getProvisioningOrderLookup();
			provOrderLookup.getOrderLineItemStatus(transactionNo, subscriberId);
			resultInfo.setResultDetail("Invoked ProvisioningOrderLookupRemote successfully");
			resultInfo.setPass(true);
		} catch (ObjectNotFoundException onfEx) {
			resultInfo.setResultDetail("Call succeeded but could not find transactionNo: " + transactionNo + " for subscriberId: " + subscriberId);
			resultInfo.setExceptionDetail(onfEx);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		return resultInfo;
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getAirTimeAllocations(SubscriberIdentifierInfo subscriberIdentifierInfo, Date effectiveDate, List serviceIdentityInfoList, String sessionId) throws ApplicationException {
		List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfos = null;
		validateInput(subscriberIdentifierInfo, effectiveDate, serviceIdentityInfoList);
		if (subscriberIdentifierInfo != null && (serviceIdentityInfoList == null || serviceIdentityInfoList.isEmpty())) {
			serviceAirTimeAllocationInfos = retrieveVoiceAllocationBySubscriberIdentifier(subscriberIdentifierInfo.getBan(), subscriberIdentifierInfo.getPhoneNumber(), sessionId);
			populatePeriodBasedFeature(serviceAirTimeAllocationInfos);
		} else if (subscriberIdentifierInfo == null && (serviceIdentityInfoList != null && !serviceIdentityInfoList.isEmpty())) {
			serviceAirTimeAllocationInfos = retrieveVoiceAllocationByServiceIdentity(serviceIdentityInfoList, effectiveDate, sessionId);
			mapServiceAirTimeAllocationInfo(serviceAirTimeAllocationInfos);
		} else if (subscriberIdentifierInfo != null && (serviceIdentityInfoList != null && !serviceIdentityInfoList.isEmpty())) {
			serviceAirTimeAllocationInfos = retrieveVoiceAllocationByAllInputs(subscriberIdentifierInfo, effectiveDate, serviceIdentityInfoList, sessionId);
			mapServiceAirTimeAllocationInfo(serviceAirTimeAllocationInfos);
		}
		mapMarketingDescription(serviceAirTimeAllocationInfos);
		return serviceAirTimeAllocationInfos;
	}

	private void mapMarketingDescription(List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocations) throws ApplicationException {
		try {
			for (ServiceAirTimeAllocationInfo satAllocation : serviceAirTimeAllocations) {
				if (StringUtils.equals(satAllocation.getServiceType(), ServiceInfo.SERVICE_TYPE_CODE_PRICE_PLAN)) {
					ReferenceInfo referenceInfo = getReferenceDataFacade().retrieveMarketingDescriptionBySoc(satAllocation.getServiceCode());
					if (referenceInfo != null) {
						satAllocation.setMarketingDescription(referenceInfo.getDescription());
						satAllocation.setMarketingDescriptionFrench(referenceInfo.getDescriptionFrench());
					}
				}
			}
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
		}
	}

	private List<ServiceAirTimeAllocationInfo> retrieveVoiceAllocationByAllInputs(SubscriberIdentifierInfo subscriberIdentifierInfo, Date effectiveDate,
			List<ServiceIdentityInfo> serviceIdentityInfoList, String sessionId) throws ApplicationException {

		List<ServiceAirTimeAllocationInfo> subscriberModeList = null;
		List<ServiceAirTimeAllocationInfo> serviceModeList = null;
		List<ServiceAirTimeAllocationInfo> outputList = null;

		try {
			subscriberModeList = retrieveVoiceAllocationBySubscriberIdentifier(subscriberIdentifierInfo.getBan(), subscriberIdentifierInfo.getPhoneNumber(), sessionId);

		} catch (ApplicationException aex) {
			logger.error(aex);
			if (aex.getSystemCode().equals(SystemCodes.AMDOCS)) {
				throw aex;
			}
		}

		serviceModeList = retrieveVoiceAllocationByServiceIdentity(serviceIdentityInfoList, effectiveDate, sessionId);

		if ((subscriberModeList != null && !subscriberModeList.isEmpty()) && (serviceModeList != null && !serviceModeList.isEmpty())) {
			outputList = new ArrayList<ServiceAirTimeAllocationInfo>();
			for (ServiceAirTimeAllocationInfo serviceModeInfo : serviceModeList) {
				boolean copyFromSubscriberModeList = false;
				for (ServiceAirTimeAllocationInfo subModeInfo : subscriberModeList) {
					if ((serviceModeInfo.getServiceCode() != null && subModeInfo.getServiceCode() != null) && (serviceModeInfo.getServiceCode().trim()).equals(subModeInfo.getServiceCode().trim())) {
						outputList.add(subModeInfo);
						copyFromSubscriberModeList = true;
						break;
					}
				}
				if (!copyFromSubscriberModeList) {
					outputList.add(serviceModeInfo);
				}
			}
			populatePeriodBasedFeature(outputList);
			return outputList;
		} else {
			return serviceModeList;
		}
	}

	private void validateInput(SubscriberIdentifierInfo subscriberIdentifierInfo, Date effectiveDate, List<ServiceIdentityInfo> serviceIdentityInfoList) throws ApplicationException {

		if (subscriberIdentifierInfo == null && (serviceIdentityInfoList == null || serviceIdentityInfoList.isEmpty())) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_MANDATORY_INPUT, "Neither Subscriber Identity nor Service Identity is provided", "");
		}

		if (subscriberIdentifierInfo != null) {
			String phoneNumber = subscriberIdentifierInfo.getPhoneNumber();
			if (phoneNumber == null || phoneNumber.trim().isEmpty() || subscriberIdentifierInfo.getBan() <= 0) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MISSING_BAN_PHONENUMBER_FIELD, "Missing mandatory input field- Ban or PhoneNumber cannot be empty or null", "");
			}
		}

		if (effectiveDate != null && DateUtil.isBefore(effectiveDate, getLogicalDate())) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_ACTIVITY_DATE, "Activity Date Cannot be Past Date", "");
		}
	}

	private List<ServiceAirTimeAllocationInfo> retrieveVoiceAllocationBySubscriberIdentifier(int ban, String phoneNumber, String sessionId) throws ApplicationException {

		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriberByPhoneNumber(ban, phoneNumber);
		if (subscriberInfo.getStatus() != Subscriber.STATUS_ACTIVE && subscriberInfo.getStatus() != Subscriber.STATUS_SUSPENDED) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_CANCELED_STATUS, "Cannot retrieve voice allocation on non active/suspended subscriber. Status="
					+ subscriberInfo.getStatus() + " for [" + ban + "/" + phoneNumber + "]", "");
		}

		return Arrays.asList(subscriberLifeCycleManager.retrieveVoiceAllocation(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), getSubscriberLifecycleManagerSessionId(sessionId)));
	}

	private void populatePeriodBasedFeature(List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfos) throws ApplicationException {

		if (serviceAirTimeAllocationInfos != null && serviceAirTimeAllocationInfos.size() > 0) {
			for (ServiceAirTimeAllocationInfo serviceAirTimeInfo : serviceAirTimeAllocationInfos) {
				if (serviceAirTimeInfo != null) {
					FeatureAirTimeAllocationInfo[] featureInfos = serviceAirTimeInfo.getFeatureAirTimeAllocations0();
					try {
						if (featureInfos != null) {
							Map<String, ServicePeriodInfo> periodMap = null;
							if (serviceAirTimeInfo.isContainPeriodBasedFeature()) {
								periodMap = getReferenceDataFacade().getServicePeriodInfo(serviceAirTimeInfo.getServiceCode(), serviceAirTimeInfo.getServiceType());
							}
							//populate extra information on the feature level
							for (FeatureAirTimeAllocationInfo featureAirTimeInfo : featureInfos) {
								ServiceFeatureClassificationInfo classification = getReferenceDataFacade().getServiceFeatureClassification(featureAirTimeInfo.getClassification0().getCode());
								//the setClassification already took care of checking NULL object.
								featureAirTimeInfo.setClassification(classification);
								if (featureAirTimeInfo.isPeriodBased() && periodMap != null) {
									ServicePeriodInfo periodInfo = periodMap.get(featureAirTimeInfo.getPeriodValueCode());
									if (periodInfo != null) {
										featureAirTimeInfo.setPeriod(periodInfo);
									}
								}
							}
						}
					} catch (TelusException e) {
						throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
					}
				}
			}
		}
	}

	private List<ServiceAirTimeAllocationInfo> retrieveVoiceAllocationByServiceIdentity(List<ServiceIdentityInfo> serviceIdentityInfoList, Date effectiveDate, String sessionId)
			throws ApplicationException {
		ServiceAirTimeAllocationInfo[] airTimeAllocationInfos = null;
		List<String> serviceCodes = new ArrayList<String>();

		if (effectiveDate == null) {
			effectiveDate = getLogicalDate();
		}

		for (ServiceIdentityInfo serviceIdentityInfo : serviceIdentityInfoList) {
			serviceCodes.add(serviceIdentityInfo.getServiceCode());
		}

		try {
			airTimeAllocationInfos = getReferenceDataFacade().getCalculatedEffectedVoiceAllocation(serviceCodes.toArray(new String[serviceCodes.size()]), effectiveDate,
					getReferenceDataFacadeSessionId(sessionId));
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.id, e.getMessage(), "", e);
		}

		return Arrays.asList(airTimeAllocationInfos);
	}

	private void mapServiceAirTimeAllocationInfo(List<ServiceAirTimeAllocationInfo> serviceAirTimeAllocationInfos) {
		if (serviceAirTimeAllocationInfos != null && !serviceAirTimeAllocationInfos.isEmpty()) {
			Date logicalDate = getLogicalDate();
			for (ServiceAirTimeAllocationInfo serviceAirTimeAllocationInfo : serviceAirTimeAllocationInfos) {
				if (!serviceAirTimeAllocationInfo.isValidSOC()) {
					serviceAirTimeAllocationInfo.setTimeStamp(logicalDate);
					serviceAirTimeAllocationInfo.setLocale(locale);
				}
			}
		}
	}

	@Override
	public List getHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId) throws ApplicationException {
		return hardwarePurchaseAccountServiceDao.getRewardAccount(billingAccountNumber, phoneNumber, subscriptionId);
	}

	@Override
	public void closeHPAAccount(int billingAccountNumber, String phoneNumber, long subscriptionId, boolean isAsync) throws ApplicationException {
		if (isAsync && AppConfiguration.isHPAAsynchronousEnabled()) {
			HpaRewardAccountInfo hpaRewardAccountInfo = new HpaRewardAccountInfo();
			hpaRewardAccountInfo.setBan(billingAccountNumber);
			hpaRewardAccountInfo.setPhoneNumber(phoneNumber);
			hpaRewardAccountInfo.setSubscriptionId(subscriptionId);
			hpaRewardAccountInfo.setHpaMthodType(HpaRewardAccountInfo.METHOD_TYPE_CLOSE_HPA_ACCOUNT);
			queueSender.send(hpaRewardAccountInfo, "msgSubTypeHPAAccount", null);
		} else {
			hardwarePurchaseAccountServiceDao.closeRewardAccount(billingAccountNumber, phoneNumber, subscriptionId);
		}
	}

	@Override
	public void closeHPAAccount(int billingAccountNumber, String phoneNumber, boolean isAsync) throws ApplicationException {
		closeHPAAccount(billingAccountNumber, phoneNumber, 0, isAsync);
	}

	//CDR - Confirmation Notification project related changes - begin
	
	private AuditInfo getPopulatedAuditInfo(AuditInfo auditInfo, String sessionId) throws ApplicationException {
		
		if (auditInfo == null) {
			auditInfo = new AuditInfo();
		}
		if (auditInfo.getOriginatorAppId() == null) {
			auditInfo.setinternalPopulatedAppId(true);
			ClientIdentity clientIdentity = getClientIdentity(sessionId);
			auditInfo.setOriginatorAppId(clientIdentity.getApplication());
			// As per workshop from 06/08, CAPI will always set this value to TRUE. Captured in 'FR834758'
			// If True, email is sent to both subscriber and owner (if different than subscriber); otherwise, only subscriber email is sent.
		}
		
		
		return auditInfo;
	}
	
	
	private AuditInfo createAuditInfoFromSession(String sessionId) throws ApplicationException {
		AuditInfo auditInfo = new AuditInfo();
		ClientIdentity clientIdentity = getClientIdentity(sessionId);
		auditInfo.setinternalPopulatedAppId(true);
		auditInfo.setUserId(clientIdentity.getPrincipal());
		auditInfo.setUserTypeCode("2");
		auditInfo.setOriginatorAppId(clientIdentity.getApplication());
		return auditInfo;
	}

	
	
	
	@Override
	public TestPointResultInfo testHardwarePurchaseAccountService() {
		return hardwarePurchaseAccountServiceDao.test();
	}
	
	@Override
	public MigrationChangeInfo validateMigrateSubscriber(MigrationChangeInfo migrationChangeInfo, String sessionId) throws ApplicationException {
		
		try {
			MigrationChangeContext changeContext = getMigrationChangeContext(migrationChangeInfo, sessionId);
			return changeContext.migrate(true);
			
		} catch (ApplicationException e) { 
			throw e;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	@Override
	public MigrationChangeInfo migrateSubscriber(MigrationChangeInfo changeInfo, String sessionId) throws ApplicationException {
		
		try {
			MigrationChangeContext changeContext = getMigrationChangeContext(changeInfo, sessionId);
			changeInfo = changeContext.migrate(false);
			return changeInfo;
		} catch (ApplicationException e) { 
			throw e;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
	@Override
	public ActivationChangeInfo validateActivateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException {
		
		try {
			ActivationChangeContext changeContext = getActivationChangeContext(activationChangeInfo, sessionId);
			return changeContext.activate(true);
			
		} catch (ApplicationException e) { 
			throw e;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	@Override
	public ActivationChangeInfo activateSubscriber(ActivationChangeInfo activationChangeInfo, String sessionId) throws ApplicationException {
		
		try {
			ActivationChangeContext changeContext = getActivationChangeContext(activationChangeInfo, sessionId);
			ActivationChangeInfo changeInfo = changeContext.activate(false); 
			return changeInfo;
			
		} catch (ApplicationException e) { 
			throw e;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}

	@Override
	public SubscriberInfo reservePhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		SubscriberInfo reservedSubscriberInfo = localReservePhoneNumber(phoneNumberReservationInfo, subscriberInfo, false, sessionId);
		return reservedSubscriberInfo;
	}

	@Override
	public SubscriberInfo reserveOnHoldPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, String sessionId) throws ApplicationException {
		
		if (phoneNumberReservationInfo.getPhoneNumberPattern().indexOf("*") >= 0 || phoneNumberReservationInfo.getPhoneNumberPattern().indexOf("%") >= 0
				|| phoneNumberReservationInfo.getPhoneNumberPattern().length() < 10 || phoneNumberReservationInfo.isLikeMatch()) {
			String errorMessage = "Invalid phone number for offline activation process. The number should be 10 digits without any wildcard charcters: ["
					+ phoneNumberReservationInfo.getPhoneNumberPattern() + "].";
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_PHONE_NUMBER, errorMessage, "");
		}
		
		SubscriberInfo reservedSubscriberInfo = null;
		reservedSubscriberInfo = subscriberLifeCycleManager.reservePhoneNumber(subscriberInfo, phoneNumberReservationInfo, true, getSubscriberLifecycleManagerSessionId(sessionId));
		reservedSubscriberInfo.setMarketProvince(phoneNumberReservationInfo.getNumberGroup().getProvinceCode());
		reservedSubscriberInfo.setNumberGroup(phoneNumberReservationInfo.getNumberGroup());
		
		return reservedSubscriberInfo;
	}
	
	@Override
	public SubscriberInfo reservePortedInPhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, PortInEligibilityInfo portInEligibilityInfo, String sessionId)
			throws ApplicationException {

		SubscriberInfo reservedSubscriberInfo = null;
		boolean reserveNumberOnly = false;

		String portProcess = determinePortProcessType(portInEligibilityInfo);
		// Deal with inter-carrier and inter-MVNE ports
		if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT) || portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT)) {
			if (subscriberInfo.isIDEN()) {
				// Reserve port-in number for IDEN
				boolean reserveUfmi = false;
				boolean ptnBased = false;
				byte ufmiReserveMethod = 0;
				int urbanId = 0;
				int fleetId = 0;
				int memberId = 0;
				AvailablePhoneNumber availablePhoneNumber = null;
				reservedSubscriberInfo = subscriberLifeCycleManager.reservePortedInPhoneNumberForIden(subscriberInfo, phoneNumberReservationInfo, reserveNumberOnly, reserveUfmi, ptnBased,
						ufmiReserveMethod, urbanId, fleetId, memberId, availablePhoneNumber, getSubscriberLifecycleManagerSessionId(sessionId));
			} else {
				// Reserve port-in number for PCS
				reservedSubscriberInfo = subscriberLifeCycleManager.reservePortedInPhoneNumber(subscriberInfo, phoneNumberReservationInfo, reserveNumberOnly,
						getSubscriberLifecycleManagerSessionId(sessionId));
			}
			reservedSubscriberInfo.setMarketProvince(phoneNumberReservationInfo.getNumberGroup().getProvinceCode());
			reservedSubscriberInfo.setNumberGroup(phoneNumberReservationInfo.getNumberGroup());
			reservedSubscriberInfo.setPortInd(true);
			
		} else if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
			// For now, throw an exception for inter-brand ports as we cannot support a hybrid web-services reservation -> Provider activation flow for these types of port-ins. 
			// This will change once we have actually defined a web-services activation flow (RF, 20140929).
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_INPUT_PARAMETERS, "Invalid port process type [" + portProcess 
					+ "]. Inter-brand port-in phone number reservation is not currently supported in this method.", "");
		}
		/* Commented out speculative code that may be used to support web-services inter-brand port-in phone number reservations. Keep this around until further notice (RF, 20140929).
		else {
			// What's left are inter-brand ports. Since this number is already active on the TELUS network, we aren't really reserving a phone number here.
			reservedSubscriberInfo = subscriberInfo;
			reservedSubscriberInfo.setPhoneNumber(portInEligibilityInfo.getPhoneNumber());
			reservedSubscriberInfo.setPortInd(true);
			try {
				// Retrieve the necessary reference data to populate the number group
				NumberGroupInfo numberGroupInfo = getReferenceDataHelper().retrieveNumberGroupByPortedInPhoneNumberProductType(reservedSubscriberInfo.getPhoneNumber(), reservedSubscriberInfo.getProductType());
				Dealer dealer = getReferenceDataHelper().retrieveDealerbyDealerCode(reservedSubscriberInfo.getDealerCode(), true);
				// Populate the number group
				numberGroupInfo.setNumberLocation(dealer.getNumberLocationCD());
				numberGroupInfo.setDefaultDealerCode(reservedSubscriberInfo.getDealerCode());
				numberGroupInfo.setDefaultSalesCode("0000");
				// Populate the subscriber
				reservedSubscriberInfo.setNumberGroup(numberGroupInfo);
				reservedSubscriberInfo.setMarketProvince(numberGroupInfo.getProvinceCode());
			} catch (TelusException te) {
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.REFERENCE_DATA_ERROR, te.getMessage(), "", te);
			}
		}
		*/

		return reservedSubscriberInfo;
	}
	
	private SubscriberInfo localReservePhoneNumber(PhoneNumberReservationInfo phoneNumberReservationInfo, SubscriberInfo subscriberInfo, boolean retry, String sessionId) throws ApplicationException {

		SubscriberInfo reservedSubscriberInfo;
		phoneNumberReservationInfo.setProductType(subscriberInfo.getProductType());		
		
		// Reserve the subscriber
		if (phoneNumberReservationInfo.isLikeMatch()) {
			reservedSubscriberInfo = subscriberLifeCycleManager.reserveLikePhoneNumber(subscriberInfo, phoneNumberReservationInfo, getSubscriberLifecycleManagerSessionId(sessionId));
		} else {
			reservedSubscriberInfo = subscriberLifeCycleManager.reservePhoneNumber(subscriberInfo, phoneNumberReservationInfo, false, getSubscriberLifecycleManagerSessionId(sessionId));
		}
		reservedSubscriberInfo.setMarketProvince(phoneNumberReservationInfo.getNumberGroup().getProvinceCode());
		reservedSubscriberInfo.setNumberGroup(phoneNumberReservationInfo.getNumberGroup());
		
		int brandID = getAccountInformationHelper().retrieveAccountByPhoneNumber(reservedSubscriberInfo.getPhoneNumber()).getBrandId();
		String portVisibilityType = determinePortVisibilityType(subscriberInfo.getSerialNumber());
		try {
			// Perform the port-in eligibility check if any of the following is TRUE:
			// 1. The reservation phone number pattern contains wildcards (i.e., the phone number is unknown)
			// 2. This is a retry attempt after the port-in eligibility check failed and the next available number is picked and needs to be tested
			// 3. When 'isLikeMatch' is TRUE, the number to be reserved is also unknown until reservation time
			boolean doEligibilityCheck = phoneNumberReservationInfo.getPhoneNumberPattern().indexOf("*") >= 0 || phoneNumberReservationInfo.getPhoneNumberPattern().indexOf("%") >= 0
					|| phoneNumberReservationInfo.getPhoneNumberPattern().length() < 10 || retry || phoneNumberReservationInfo.isLikeMatch();
			if (doEligibilityCheck) {
				// Check port-in eligibility
				checkPortInEligibility(reservedSubscriberInfo.getPhoneNumber(), portVisibilityType, brandID);
			}
		} catch (ApplicationException ae) {
			// Check to see if the underlying cause of the ApplicationException is a port-in eligibility check failure
			if (!ae.getErrorCode().isEmpty()) {
				// If the port-in eligibility check failed, release the reserved subscriber as this is not a valid phone number
				subscriberLifeCycleManager.releaseSubscriber(reservedSubscriberInfo, getSubscriberLifecycleManagerSessionId(sessionId));
				// Attempt another reservation only if the specific error is 'PRM_FALSE' and 'retry' is FALSE
				if (StringUtils.equals(ae.getMessage(), "PRM_FALSE") && !retry) {
					// Get the available phone numbers - note that we're setting the subscriber ID to 'null'
					AvailablePhoneNumberInfo[] numbers = subscriberLifeCycleManager.retrieveAvailablePhoneNumbers(subscriberInfo.getBanId(), null, phoneNumberReservationInfo, 100,
							getSubscriberLifecycleManagerSessionId(sessionId));
					if (numbers != null) {
						for (AvailablePhoneNumberInfo number : numbers) {
							// If the phone number matches the TN which failed the port-in eligibility check, skip to the next number
							if (!StringUtils.equals(number.getPhoneNumber(), reservedSubscriberInfo.getPhoneNumber())) {
								// Otherwise, set the phoneNumberReservationInfo and exit the loop
								phoneNumberReservationInfo.setPhoneNumberPattern(number.getPhoneNumber());
								phoneNumberReservationInfo.setLikeMatch(false);
								break;
							}
						}
					}
					// Attempt to reserve using the new TN by making a recursive call - note that we've set the 'retry' to TRUE, so we won't get caught in an infinite recursive loop
					return localReservePhoneNumber(phoneNumberReservationInfo, subscriberInfo, true, sessionId);
				}
				
				// We need to create an actual null String to make the call to retrieve the application message based on the PRM error response code due to signature ambiguity
				String nullString = null;
				String prmErrorMessage = getApplicationMessageFacade().getApplicationMessage(nullString, nullString, ApplicationSummary.APP_PRM, ae.getErrorCode(), brandID, ae)
						.getText(Locale.ENGLISH);
				throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.PRM_CHECK_PORTIN_ELIGIBILITY_ERROR, prmErrorMessage, "");
			}
			
			// Throw the original ApplicationException otherwise
			throw ae;
		}
		
		return reservedSubscriberInfo;
	}

	private String determinePortVisibilityType(String serialNumber) throws ApplicationException {

		String portVisibilityType = "";
		if (Equipment.DUMMY_HSPA_NETWORK_NUMBER.equalsIgnoreCase(serialNumber)) {
			portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2H;
		} else {
			EquipmentInfo equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(serialNumber);
			if (!equipmentInfo.isPager()) {
				if (equipmentInfo.isCDMA()) {
					portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2C;
				} else if (equipmentInfo.isHSPA()) {
					portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2H;
				} else {
					portVisibilityType = PortInEligibility.PORT_VISIBILITY_TYPE_INTERNAL_2I;
				}
			}
		}

		return portVisibilityType;
	}
	
	private SubscriberInfo retrieveLastPortedOutCancelledSubscriberByPhoneNumber(String phoneNumber) throws ApplicationException {

		String applicationCode = null, audienceTypeCode = null;
		Collection<SubscriberInfo> subscriberInfoList = subscriberLifecycleHelper.retrieveSubscriberListByPhoneNumber(phoneNumber, 2, true);
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (subscriberInfo.getStatus() == Subscriber.STATUS_CANCELED && subscriberInfo.getPortType().equals(Subscriber.PORT_TYPE_PORT_OUT)) {
				return subscriberInfo;
			}
		}

		String prmErrorMessage = getApplicationMessageFacade().getApplicationMessage(applicationCode, audienceTypeCode, InterBrandPortRequestException.ERR006).getText(Locale.ENGLISH);
		throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INTER_BRAND_CANCELED_PORT_OUT_SUBSCRIBER_ERROR, prmErrorMessage, "");
	}

	private String getInterBrandPortActivityReasonCode(SubscriberInfo incomingSub, SubscriberInfo outgoingSub) throws ApplicationException {

		String applicationCode = null, audienceTypeCode = null;

		AccountInfo inComingAccountInfo = getAccountInformationHelper().retrieveLwAccountByBan(incomingSub.getBanId());
		AccountInfo outgoingAccountInfo = getAccountInformationHelper().retrieveLwAccountByBan(outgoingSub.getBanId());

		String outgoingKey = outgoingAccountInfo.getBrandId() + outgoingSub.getProductType() + (outgoingAccountInfo.isPostpaid() == true ? POSTPAID : PREPAID);
		String incomingKey = inComingAccountInfo.getBrandId() + incomingSub.getProductType() + (inComingAccountInfo.isPostpaid() == true ? POSTPAID : PREPAID);
		HashMap<String, String> interBrandPortActivityReasonCodesKeyMap = AppConfiguration.getInterBrandPortActivityReasonCodesKeyMap();

		String reasonCode = (String) interBrandPortActivityReasonCodesKeyMap.get(outgoingKey) + (String) interBrandPortActivityReasonCodesKeyMap.get(incomingKey);
		if (AppConfiguration.getInterBrandPortActivityReasonCodes().contains(reasonCode)) {
			return reasonCode;
		} else {
			String prmErrorMessage = getApplicationMessageFacade().getApplicationMessage(applicationCode, audienceTypeCode, InterBrandPortRequestException.ERR009).getText(Locale.ENGLISH);
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INTER_BRAND_PORT_ACTIVITY_REASON_CODE_ERROR, prmErrorMessage, "");
		}
	} 
	
	private void interBrandPortRollbackResumeCancelledSubscriber(SubscriberInfo outgoingSub, SubscriberInfo incomingSub, String activityReasonCode, ServiceRequestHeader header, String sessionId)
			throws ApplicationException {
		
		resumeCancelledSubscriber(outgoingSub, activityReasonCode, "Inter-brand port rollback from BAN [" + incomingSub.getBanId() + "] to BAN [" + outgoingSub.getBanId() + "] and subscriber ["
				+ outgoingSub.getSubscriberId() + "]", true, PortInEligibility.PORT_PROCESS_ROLLBACK, outgoingSub.getBanId(), outgoingSub.getSubscriberId(), null, sessionId);
		
		
		// call RCM to assign the MIN after the (old) outgoing subscriber is resumed
		if (outgoingSub.isPCS()) {
			try {
				EquipmentInfo equipmentInfo = getProductEquipmentHelper().getEquipmentInfobySerialNo(incomingSub.getSerialNumber());
				assignTNResources(incomingSub.getPhoneNumber(), equipmentInfo.getNetworkType(), equipmentInfo.getProfile().getLocalIMSI(), equipmentInfo.getProfile().getRemoteIMSI());
			} catch (Throwable t) {
				logger.error("assignTNResources call failed for " + incomingSub.getPhoneNumber() + " with the reason" + t.getMessage());
			}
		}
		
		// set the subscriber port indicator in KB back to Subscriber.PORT_TYPE_PORT_IN if necessary and with the correct date
		if (outgoingSub.getPortType() != null && outgoingSub.getPortType().equals(Subscriber.PORT_TYPE_PORT_IN)) {
			try {
				subscriberLifeCycleManager.setSubscriberPortIndicator(outgoingSub.getPhoneNumber(), outgoingSub.getPortDate(), sessionId);
			} catch (Throwable t) {
				logger.error("setSubscriberPortIndicator call failed for " + outgoingSub.getPhoneNumber() + " with the reason" + t.getMessage());
			}
		}
	}

	private String determinePortProcessType(PortInEligibilityInfo portInEligibilityInfo) throws ApplicationException {

		Brand[] brands;
		try {
			brands = getReferenceDataFacade().getBrands();
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
		}

		if (portInEligibilityInfo.isPortInFromMVNE()) {
			return PortInEligibility.PORT_PROCESS_INTER_MVNE_PORT;
		} else if (ReferenceDataManager.Helper.validateBrandId(portInEligibilityInfo.getIncomingBrandId(), brands) 
				&& ReferenceDataManager.Helper.validateBrandId(portInEligibilityInfo.getOutgoingBrandId(), brands)) {
			return PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT;
		} else {
			return PortInEligibility.PORT_PROCESS_INTER_CARRIER_PORT;
		}
	}
	
	@Override
	public void unreservePhoneNumber(SubscriberInfo subscriberInfo, boolean cancelPortIn, PortInEligibilityInfo portInEligibilityInfo, String sessionId) throws ApplicationException {
		
		if (cancelPortIn) {
			String portProcess = determinePortProcessType(portInEligibilityInfo);
			subscriberLifeCycleManager.releasePortedInSubscriber(subscriberInfo, sessionId);

			// 2.determine the port carrier and resume the last cancelled subscriber if it is inter-brand portIn
			if (portProcess.equals(PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT)) {
				SubscriberInfo oldSubscriberInfo = retrieveLastPortedOutCancelledSubscriberByPhoneNumber(subscriberInfo.getSubscriberId());
				String activityReasonCode = getInterBrandPortActivityReasonCode(subscriberInfo, oldSubscriberInfo);
				interBrandPortRollbackResumeCancelledSubscriber(oldSubscriberInfo, subscriberInfo, activityReasonCode, null, sessionId);
			}

			/*
			PortRequestSummary prSummary = checkPortRequestStatus(subscriberInfo.getPhoneNumber(),subscriberInfo.getBrandId());
			String portRequestId = prSummary != null ? prSummary.getPortRequestId() : null;
			if (portRequestId != null && prSummary.canBeCanceled()) {
			cancelPortInRequest(portRequestId,RELEASE_FROM_RESERVED_ACTIVATION,amdocsSessionManager.getClientIdentity(sessionId).getApplication());
			}
			*/

		} else {
			subscriberLifeCycleManager.releaseSubscriber(subscriberInfo, sessionId);
		}
	}
	
	@Override
	public MigrateSeatChangeInfo validateMigrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException {
		try {
			MigrateSeatChangeContext changeContext = getMigrateSeatChangeContext(migrateSeatChangeInfo, sessionId);
			return changeContext.migrate(true, notificationSuppressionInd, auditInfo);
		} catch (ApplicationException ae) { 
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
	@Override
	public MigrateSeatChangeInfo migrateSeat(MigrateSeatChangeInfo migrateSeatChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		try {
			MigrateSeatChangeContext changeContext = getMigrateSeatChangeContext(migrateSeatChangeInfo, sessionId);
			return changeContext.migrate(false, notificationSuppressionInd, auditInfo);
		} catch (ApplicationException ae) { 
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MIGRATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
	
	@Override
	public void migrateSeatChangePricePlan(ContractChangeInfo contractChangeInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		// Create and initialize the change context for the price plan change
		ContractChangeContext context = getContractChangeContext(contractChangeInfo, sessionId);
		context.setNotificationSuppressionInd(notificationSuppressionInd);
		context.setAuditInfo(auditInfo);

		// Get the new contract (with the new price plan) - note, optional services are not preserved since this is a price plan change
		ContractBo contract = context.getNewContract();

		// Change the service agreement to the new price plan
		contract.save(contractChangeInfo.getDealerCode(), contractChangeInfo.getSalesRepCode());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void changeVOIPResource(long subscriptionId, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException {
		// Note: there's no validation whatsoever that the supplied resource list consists of the old VOIP resource to cancel and the new VOIP resource
		// to activate. We assume the web service has performed all necessary validation and push the changes through to KB and Provisioning. However, there
		// is special validation when the call is from the Provisioning VOIP Notification Service, (i.e., outgoingRequestInd = false). Since they only supply a 
		// single resource, for ADD activities we must check if a primary VOIP resource already exists to set as the resource type (new activation port-in VOIP 
		// number change scenario). Otherwise, the resource type is set to additional VOIP (normal VOIP number change scenario). For CANCEL activities, we need to 
		// check if the resource number exists in order to map the resource type. This is required so KB can expire the correct record.
		
		// Retrieve the subscriber
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveLatestSubscriberBySubscriptionId(subscriptionId);

		// Do some additional pre-processing if the call originates from Provisioning (resource type is 'provision') - Provisioning only supplies one resource per call
		ResourceActivityInfo resourceActivity = (ResourceActivityInfo) resourceList.get(0);
		if (StringUtils.equalsIgnoreCase(Subscriber.RESOURCE_TYPE_PROVISIONING, resourceActivity.getResource().getResourceType())) {
			if (StringUtils.equalsIgnoreCase(resourceActivity.getResourceActivity(), ResourceActivity.ADD)) {
				// Set the resource type to primary VOIP as the default
				resourceActivity.getResource().setResourceType(Subscriber.RESOURCE_TYPE_PRIMARY_VOIP);
				if (subscriberInfo.getSeatData() != null) {
					for (Resource subscriberResource : subscriberInfo.getSeatData().getResources()) {
						// For activity type ADD, check if the subscriber has already has a primary VOIP resource
						if (StringUtils.equalsIgnoreCase(subscriberResource.getResourceType(), Subscriber.RESOURCE_TYPE_PRIMARY_VOIP)) {
							// This subscriber already has a primary VOIP - set the resource type to additional VOIP instead
							resourceActivity.getResource().setResourceType(Subscriber.RESOURCE_TYPE_ADDITIONAL_VOIP);
						}
					}
				}
			}
			if (StringUtils.equalsIgnoreCase(resourceActivity.getResourceActivity(), ResourceActivity.CANCEL)) {
				if (subscriberInfo.getSeatData() != null) {
					// For activity type CANCEL, check if the resource number exists for this subscriber
					for (Resource subscriberResource : subscriberInfo.getSeatData().getResources()) {
						if (StringUtils.equalsIgnoreCase(subscriberResource.getResourceNumber(), resourceActivity.getResource().getResourceNumber())) {
							// If the resource number matches, set the resource type
							resourceActivity.getResource().setResourceType(subscriberResource.getResourceType()); 
						}
					}
				}
				// Provisioning supplies 'provision' as their placeholder resource type value. If we didn't find a matching resource, then the resource type will not match one of the following valid
				// resource types: { 'V', 'L', 'O' }. Throw an exception as we can't properly process the CANCEL activity.
				if (!Arrays.asList(SUBSCRIBER_VOIP_RESOURCE_TYPES).contains(resourceActivity.getResource().getResourceType())) {
					throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_RESOURCE_NOT_FOUND, "No matching resource: Subscriber ID=[" + subscriberInfo.getSubscriberId()
							+ "], resource number=[" + resourceActivity.getResource().getResourceNumber() + "], resource type=[" + resourceActivity.getResource().getResourceType() + "], activity=["
							+ resourceActivity.getResourceActivity() + "], outgoingRequestInd=[" + outgoingRequestInd + "].", "");
				}
			}
		}
		
		// Change the VOIP resource
		changeVOIPResource(subscriberInfo, resourceList, activityDate, outgoingRequestInd, sessionId);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void changeVOIPResource(SubscriberInfo subscriberInfo, List resourceList, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException {
		// Note: there's no validation whatsoever that the supplied resource list consists of the old VOIP resource to cancel and the new VOIP resource
		// to activate. We assume the web service has performed all necessary validation and push the changes through to KB and Provisioning.
		
		// If no date has been supplied, use the current date
		if (activityDate == null) {
			activityDate = getLogicalDate();
		}
		
		try {
			// Call Amdocs to change the subscriber resource in KB
			subscriberLifeCycleManager.changeResources(subscriberInfo, resourceList, activityDate, sessionId);
		} catch (Throwable t) {
			// No fallout email is required at this point, since nothing has completed - just throw the exception
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_RESOURCE_CHANGE_ERROR, t.getMessage(), "", t);
		}

		// Call Provisioning - the outgoingRequestInd flag tells them if we need to provision the new VOIP resource in the network
		asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAddRemoveVOIPChangeRequest(subscriberInfo, activityDate, resourceList, outgoingRequestInd));
	}

	@Override
	public void changeVOIPResourceWithCharge(long subscriptionId, ResourceActivityInfo resourceActivity, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException {
		// Note: there's no validation whatsoever that the supplied resource list consists of the old VOIP resource to cancel and the new VOIP resource
		// to activate. We assume the web service has performed all necessary validation and push the changes through to KB and Provisioning.
		
		// Retrieve the subscriber
		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveLatestSubscriberBySubscriptionId(subscriptionId);
		
		// Change the VOIP resource with charge
		changeVOIPResourceWithCharge(subscriberInfo, resourceActivity, activityDate, outgoingRequestInd, serviceCode, sessionId);
	}
	
	@Override
	public void changeVOIPResourceWithCharge(SubscriberInfo subscriberInfo, ResourceActivityInfo resourceActivity, Date activityDate, boolean outgoingRequestInd, String serviceCode, String sessionId)
			throws ApplicationException {
		// Note: unlike the changeVOIPResource method, this call only supports a single activity, with an associated service to add or remove.

		// If no date has been supplied, use the current date
		if (activityDate == null) {
			activityDate = getLogicalDate();
		}
		
		// Put the resource into a list
		List<ResourceActivityInfo> resourceList = new ArrayList<ResourceActivityInfo>();
		resourceList.add(resourceActivity);
		
		// Change the VOIP resource
		changeVOIPResource(subscriberInfo, resourceList, activityDate, outgoingRequestInd, sessionId);

		try {
			// Add/remove the VOIP service from the subscriber's service agreement
			addRemoveVOIPService(subscriberInfo, resourceActivity, activityDate, serviceCode, sessionId);
		} catch (Throwable t) {
			// Business made decision not to send fallout email notification
			if (t instanceof ApplicationException) {
				ApplicationException ae = (ApplicationException) t;
				throw ae;
			}
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, t.getMessage(), "", t);
		}
	}
	
	private void addRemoveVOIPService(SubscriberInfo subscriberInfo, ResourceActivityInfo resourceActivity, Date activityDate, String serviceCode, String sessionId) throws ApplicationException {
		// Note: there's no validation whatsoever that the supplied service code corresponds to a VOIP SOC or is on the subscriber's service agreement.
		
		// Get the subscriber's contract
		ContractChangeInfo contractChangeInfo = getContractChangeInfo(subscriberInfo, subscriberInfo.getBanId());		
		ContractChangeContext context = getContractChangeContext(contractChangeInfo, sessionId);
		context.setNotificationSuppressionInd(true);
		context.setAuditInfo(getPopulatedAuditInfo(getPopulatedAuditInfo(null, sessionId), sessionId));
		ContractBo contract = context.getNewContract();
		
		try {
			// Add the VOIP service
			if (resourceActivity.getResourceActivity().equalsIgnoreCase(ResourceActivity.ADD) && StringUtils.isNotBlank(serviceCode)) {
				contract.addService(serviceCode);
			}

			// Remove the VOIP service
			if (resourceActivity.getResourceActivity().equalsIgnoreCase(ResourceActivity.CANCEL) && StringUtils.isNotBlank(serviceCode)) {
				contract.removeService(serviceCode);
			}

		} catch (InvalidServiceChangeException isce) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.INVALID_SERVICE_CHANGE, isce.getMessage(), "", isce);
		} catch (TelusAPIException tapie) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.CONTRACT_CHANGE_ERROR, tapie.getMessage(), "", tapie);
		}

		// Save the changes to the contract
		contract.save(contractChangeInfo.getDealerCode(), contractChangeInfo.getSalesRepCode());
	}
	
	@Override
	public void moveVOIPResource(long sourceSubscriptionId, long targetSubscriptionId, String resourceNumber, Date activityDate, boolean outgoingRequestInd, String sessionId)
			throws ApplicationException {
		
		// Retrieve the source subscriber
		SubscriberInfo source = subscriberLifecycleHelper.retrieveLatestSubscriberBySubscriptionId(sourceSubscriptionId);
		
		// Retrieve the target subscriber
		SubscriberInfo target = subscriberLifecycleHelper.retrieveLatestSubscriberBySubscriptionId(targetSubscriptionId);
		
		// Find the moving resource based on the supplied resource number
		Resource movingResource = null;
		if (source.getSeatData() != null) {
			for (Resource resource : source.getSeatData().getResources()) {
				if (resource.getResourceNumber().equalsIgnoreCase(resourceNumber)) {
					// If the resource number matches, set the resource
					movingResource = resource;
				}
			}
		}
		if (movingResource == null) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_RESOURCE_NOT_FOUND, "No matching resource: Subscriber ID=[" + source.getSubscriberId() 
					+ "], resource number=[" + resourceNumber + "].", "");
		}
		
		// Move the VOIP resource
		moveVOIPResource(source, target, movingResource, activityDate, outgoingRequestInd, sessionId);
	}
	
	@Override
	public void moveVOIPResource(SubscriberInfo source, SubscriberInfo target, Resource resource, Date activityDate, boolean outgoingRequestInd, String sessionId) throws ApplicationException {
		
		// If no date has been supplied, use the current date
		if (activityDate == null) {
			activityDate = getLogicalDate();
		}
		
		// Remove the resource from the source subscriber
		// Create a new resource activity to remove from the source
		ResourceActivityInfo removeResourceActivity = new ResourceActivityInfo();
		removeResourceActivity.setResource(resource);
		removeResourceActivity.setResourceActivity(ResourceActivity.CANCEL);
		
		// Put the resource into a list
		List<ResourceActivityInfo> removeList = new ArrayList<ResourceActivityInfo>();
		removeList.add(removeResourceActivity);
		
		try {
			// Call Amdocs to change the source subscriber resource in KB
			subscriberLifeCycleManager.changeResources(source, removeList, activityDate, sessionId);
		} catch (Throwable t) {
			// No fallout email is required at this point, since nothing has completed - just throw the exception
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_RESOURCE_CHANGE_ERROR, t.getMessage(), "", t);
		}
		
		// Add the resource to the target subscriber
		// Create a new resource activity to add to the target
		ResourceActivityInfo addResourceActivity = new ResourceActivityInfo();
		addResourceActivity.setResource(resource);
		addResourceActivity.setResourceActivity(ResourceActivity.ADD);
		
		// Put the resource into a list
		List<ResourceActivityInfo> addList = new ArrayList<ResourceActivityInfo>();
		addList.add(addResourceActivity);
		
		try {
			// Call Amdocs to change the target subscriber resource in KB
			subscriberLifeCycleManager.changeResources(target, addList, activityDate, sessionId);
			
		} catch (Throwable t) {
			// Business decided not to send the fallout email notification
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.SUBSCRIBER_RESOURCE_CHANGE_ERROR, t.getMessage(), "", t);
		}

		// Call Provisioning - the outgoingRequestInd flag tells them if we need to provision the new VOIP resource in the network
		asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createMoveVOIPChangeRequest(source, target, activityDate, resource.getResourceNumber(), outgoingRequestInd));
	}
	
	@Override
	public void asyncSubmitProvisioningOrder(ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException {
		try {
			submitProvisioningOrder(provisioningRequestInfo);
		} catch (Throwable t) {
			queueSender.send(provisioningRequestInfo, "msgSubTypeProvisioning", null);
		}
	}
	
	@Override
	public void submitProvisioningOrder(ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException {
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);
	}
	
	@Override
	public void validatePPSServicesWhenAccountTypeChanged(int banId, char oldAccountType, char oldAccountSubType, char newAccountType, char newAccountSubType, String sessionId)
			throws ApplicationException {

		// AccountInfo accountInfo = getAccountInformationHelper().retrieveAccountByBan(banId);
		boolean oldAccountPPSEligible = false;
		boolean newAccountPPSEligible = false;
		try {
			oldAccountPPSEligible = getReferenceDataFacade().isPPSEligible(oldAccountType, oldAccountSubType);

			if (!oldAccountPPSEligible)
				return; // If old account is not PPS eligible, no need to check since contracts should not contain PP&S services

			newAccountPPSEligible = getReferenceDataFacade().isPPSEligible(newAccountType, newAccountSubType);

			if (newAccountPPSEligible)
				return; // If new account is PPS eligible, no need to check for PP&S services removal

			AccountType accountType = getReferenceDataFacade().getAccountType(String.valueOf(newAccountType) + String.valueOf(newAccountSubType), banId);
			String defaultDealerCode = accountType.getDefaultDealer();
			String defaultSalesCode = accountType.getDefaultSalesCode();

			// Subscriber[] subscribers = subscriberLifecycleHelper.retrieveSubscriberListByBAN(banId, 1000, false);
			Collection<SubscriberInfo> subscribers = subscriberLifecycleHelper.retrieveSubscriberListByBAN(banId, 1000, false);
			for (SubscriberInfo subscriberInfo : subscribers) {
				if (subscriberInfo.getStatus() == Subscriber.STATUS_ACTIVE) {// Active
					// SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(subscriber.getBanId(), subscriber.getSubscriberId());
					removePPSServices(subscriberInfo, defaultDealerCode, defaultSalesCode, sessionId);
				}
			}
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), null, e);
		} 
	}

	public void validatePPSServicesWhenAccountChanged(String subscriberId, int newBanId, int oldBanId, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException {
		if (newBanId == oldBanId) {
			// No validation since the account has not changed
			return;
		}
		try {
			AccountInfo oldAccount = getAccountInformationHelper().retrieveLwAccountByBan(oldBanId);

			boolean oldAccountPPSEligible;
			oldAccountPPSEligible = getReferenceDataFacade().isPPSEligible(oldAccount.getAccountType(), oldAccount.getAccountSubType());

			if (!oldAccountPPSEligible) { // If old account is not PPS eligible there should be no PPS services in the contract
				return;
			}

			AccountInfo newAccount = getAccountInformationHelper().retrieveLwAccountByBan(newBanId);

			boolean newAccountPPSEligible = getReferenceDataFacade().isPPSEligible(newAccount.getAccountType(), newAccount.getAccountSubType());
			if (newAccountPPSEligible) {
				return; // If new account is PPS eligible, no need to check for PP&S services removal
			}
			SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(newBanId, subscriberId);
			removePPSServices(subscriberInfo, dealerCode, salesRepCode, sessionId);
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), null, e);
		}
	}	

	public void removePPSServices(SubscriberInfo subscriberInfo, String dealerCode, String salesRepCode, String sessionId) throws ApplicationException {
		
		ContractChangeInfo contractChangeInfo = getContractChangeInfo(subscriberInfo, subscriberInfo.getBanId());		
		ContractChangeContext context = getContractChangeContext(contractChangeInfo, sessionId);
		context.setNotificationSuppressionInd(true);
		context.setAuditInfo(getPopulatedAuditInfo(getPopulatedAuditInfo(null, sessionId), sessionId));
		ContractBo contract = context.getNewContract();
		try {
				ContractService[] optionalServices = contract.getOptionalServices();
				ArrayList<String> ppsServices = new ArrayList<String>();
				if (optionalServices != null) {
					for (ContractService service : optionalServices) {
					if (service.getService().isPPSBundle() || service.getService().isPPSAddOn()) {
								ppsServices.add(service.getService().getCode().trim()); 
						}
					}
					
					if (!ppsServices.isEmpty()) {
					for (String serviceCode : ppsServices) {
							if (contract.containsService(serviceCode)) {
								contract.removeService(serviceCode);
							}
						}
						contract.save(dealerCode, salesRepCode);
					}
				}
				
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), null, e);
		} catch (TelusAPIException e) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, e.getMessage(), null, e);
		}
	}

	@Override
	public SubscriberInfo updateSubscriberProfile(String billingAccountNumber, String subscriberId, Boolean prepaidInd, ConsumerNameInfo consumerNameInfo, AddressInfo addresInfo, String emailAddress,
			String language, String invoiceCallSortOrderCd, String subscriptionRoleCd, String sessionId) throws ApplicationException {

		SubscriberInfo subscriberInfo = subscriberLifecycleHelper.retrieveSubscriber(Integer.valueOf(billingAccountNumber), subscriberId);

		String productType = subscriberInfo.getProductType();

		/****** Prepaid check for language *****/

		if (StringUtils.isBlank(language) == false) {
			if (null == prepaidInd) {
				AccountInfo accountInfo = getAccountInformationHelper().retrieveAccountByBan(subscriberInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
				prepaidInd = accountInfo.isPrepaidConsumer();
			}

			if (prepaidInd) {
				PrepaidSubscriberInfo prepaidSubscriberInfo = new PrepaidSubscriberInfo();
				prepaidSubscriberInfo.setBan(Integer.valueOf(billingAccountNumber));
				prepaidSubscriberInfo.setPhoneNumber(subscriberId);
				prepaidSubscriberInfo.setLanguage(language);
				subscriberLifeCycleManager.updatePrepaidSubscriber(prepaidSubscriberInfo);
			}
			subscriberInfo.setLanguage(language);
		}

		//updating the subscriber address in knowbility  			
		if (addresInfo != null) {
			subscriberLifeCycleManager.updateAddress(Integer.valueOf(billingAccountNumber), subscriberId, productType, addresInfo, sessionId);
		}

		//updating the subscriber emailAddress in knowbility  
		if (StringUtils.isBlank(emailAddress) == false) {
			subscriberInfo.setEmailAddress(emailAddress);
		}

		//updating the subscriber invoiceCallSortOrderCd in knowbility  
		if (StringUtils.isBlank(invoiceCallSortOrderCd) == false) {
			subscriberInfo.setInvoiceCallSortOrderCode(invoiceCallSortOrderCd);
		}
		
		//updating the subscriptionRoleCd in Cods  
		if (StringUtils.isBlank(subscriptionRoleCd) == false) {
			SubscriptionRoleInfo subscriberRoleInfo = subscriberLifecycleHelper.retrieveSubscriptionRole(subscriberId);
			subscriberLifeCycleManager.updateSubscriptionRole(Integer.valueOf(billingAccountNumber), subscriberId, subscriptionRoleCd, subscriberRoleInfo.getDealerCode(),
					subscriberRoleInfo.getSalesRepCode(), subscriberRoleInfo.getCsrId());
			//subscriberLifecycleHelper.updateSubscriptionRole(subscriberId,subscriptionRoleCd);
		} 	

		//updating the subscriber consumerNameInfo in knowbility  
		if (null != consumerNameInfo) {
			subscriberInfo.getConsumerName().setTitle(consumerNameInfo.getTitle());
			subscriberInfo.getConsumerName().setFirstName(consumerNameInfo.getFirstName());
			subscriberInfo.getConsumerName().setLastName(consumerNameInfo.getLastName());
			subscriberInfo.getConsumerName().setMiddleInitial(consumerNameInfo.getMiddleInitial());
			subscriberInfo.getConsumerName().setGeneration(consumerNameInfo.getGeneration());
			subscriberInfo.getConsumerName().setAdditionalLine(consumerNameInfo.getAdditionalLine());
			subscriberInfo.getConsumerName().setNameFormat(consumerNameInfo.getNameFormat());

		}

		subscriberInfo = subscriberLifeCycleManager.updateSubscriber(subscriberInfo, getSubscriberLifecycleManagerSessionId(sessionId));				

		return subscriberInfo;
	}
	
	// TODO change signature to remove portProcessCode and portedIn parameters - these values are derived during initialization of the ActivatePortinContext (see private performPortInActivation method below)
	@Override
	public ActivationChangeInfo performPortInActivation(String sessionId, PortRequestInfo portRequest, ActivationChangeInfo activationChange, ServiceRequestHeaderInfo requestHeader,
			AuditInfo auditInfo, String portProcessCode, boolean portedIn) throws ApplicationException {
		return performPortInActivation(sessionId, portRequest, activationChange, requestHeader, auditInfo);
	}
	
	// TODO make this signature the public method eventually
	private ActivationChangeInfo performPortInActivation(String sessionId, PortRequestInfo portRequest, ActivationChangeInfo activationChange, ServiceRequestHeaderInfo requestHeader,
			AuditInfo auditInfo) throws ApplicationException {
		
		try {
			ActivatePortinContext changeContext = getActivatePortinChangeContext(activationChange, portRequest, requestHeader, auditInfo, sessionId);
			activationChange = changeContext.activatePortin(false);
			return activationChange;
			
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.ACTIVATE_GENERAL_ERROR, t.getMessage(), "", t);
		}
	}
		
	@Override
	public DataSharingSocTransferInfo validateDataSharingBeforeCancelSubscriber(String subscriberId) throws ApplicationException {	
		//Step 1: Subscriber must be active/suspended and account must be open/suspended
		SubscriberInfo leavingSubscriber = subscriberLifecycleHelper.retrieveSubscriber(subscriberId);
		AccountInfo account = getAccountInformationHelper().retrieveLwAccountByBan(leavingSubscriber.getBanId());
		if (DataSharingUtil.isValidAccountForDataSharingValidation(leavingSubscriber, account) == false) {
			return null;
		}
		
		//Step 2: Subscriber's brand must be supported (loaded from LDAP)
		if (DataSharingUtil.isSupportedDataSharingBrand(leavingSubscriber) == false) {
			return null;
		}
		
		//Step 3: Subscriber must be contributing in data sharing	
		Set<String> dataSharingGroups = getContributingDataSharingGroups(leavingSubscriber, account);
		if (dataSharingGroups.isEmpty()) {
			return null;
		}
		
		//Step 4: Subscriber cannot be contributing to more than 1 data sharing group
		if (dataSharingGroups.size() > 1) {
			throw new ApplicationException(SystemCodes.CMB_SLF_EJB, ErrorCodes.MULTIPLE_CONTRIBUTING_DATA_SHARING_GROUPS, "Cannot validate data sharing for cancelling subscriber [" + subscriberId
					+ "] because they are contributing to multiple " + "data sharing groups:" + dataSharingGroups.toString(), "");
		}
		
		//Step 5: Subscriber's data sharing group must be in scope		
		String dataSharingGroup = dataSharingGroups.iterator().next();
		if (isDataSharingGroupOutOfPricingScope(dataSharingGroup, account.getAccountType(), account.getAccountSubType())) {
			return null;
		}
		
		//Step 6: Verify if there is at least 1 contributor on the account when there are 1 or more accessors
		SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails = getAccountInformationHelper().retrieveSubscriberDataSharingInfoList(account.getBanId(), new String[] { dataSharingGroup });
		int contributorCount = DataSharingUtil.getContributorCount(subscriberDataSharingDetails, dataSharingGroup);
		if (contributorCount < 2) {
			if (contributorCount == subscriberDataSharingDetails.length) {
				return null;
			} else {
				return createDataSharingSocTransferInfo(subscriberDataSharingDetails, dataSharingGroup, leavingSubscriber);				
			}
		}
		
		//Step 7: Verify that there is no MSC breakage
		List<SubscriptionMSCSpendingInfo> subscriptionMSCSpending = DataSharingUtil.getSubscriptionMSCSpending(subscriberDataSharingDetails, subscriberId);
		SubscriptionMSCResultInfo subscriptionMSCResult = penaltyCalculationServiceDao.validateSubscriptionMSCList(subscriptionMSCSpending);	
		if (subscriptionMSCResult.isMSCBroken()) {
			return createDataSharingSocTransferInfo(subscriberDataSharingDetails, dataSharingGroup, leavingSubscriber);	
		}
				
		return null;
	}
	
	private Set<String> getContributingDataSharingGroups(SubscriberInfo leavingSubscriber, AccountInfo account) throws ApplicationException {
		Set<String> dataSharingGroups = new HashSet<String>();
		SubscriberContractInfo subscriberContract = getServiceAgreement(leavingSubscriber, account);
		try {
			List<String> serviceCodes = new ArrayList<String>();
			for (ContractService contractService : subscriberContract.getServices()) {
				serviceCodes.add(contractService.getCode());
			}		
			ServiceInfo[] services = getReferenceDataFacade().getRegularServices(serviceCodes.toArray(new String[0]));
			dataSharingGroups.addAll(DataSharingUtil.getContributingServiceDataSharingGroups(services));
			
			PricePlanInfo pricePlan = getReferenceDataFacade().getPricePlan(subscriberContract.getCode());
			dataSharingGroups.addAll(DataSharingUtil.getContributingPricePlanDataSharingGroups(pricePlan));
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.REFERENCE_DATA_ERROR, e.getMessage(), "");
		}
		return dataSharingGroups;
	}
	
	private boolean isDataSharingGroupOutOfPricingScope(String dataSharingGroup, char accountType, char accountSubType) throws ApplicationException {
		try {			
			@SuppressWarnings("unchecked")
			Map<String, List<String>> dataSharingPricingGroupMap = getReferenceDataFacade().getDataSharingPricingGroups();			
			List<String> dataSharingPricingGroups = dataSharingPricingGroupMap.get("" + accountType + accountSubType);
//			logger.info("Data sharing groups from RefPDS" + dataSharingPricingGroups.toString());
			return dataSharingPricingGroups == null || dataSharingPricingGroups.contains(dataSharingGroup) == false;
		} catch (TelusException e) {
			throw new ApplicationException(SystemCodes.CMB_EJB, ErrorCodes.REFERENCE_DATA_ERROR, e.getMessage(), "");
		} 
	}

	private DataSharingSocTransferInfo createDataSharingSocTransferInfo(SubscriberDataSharingDetailInfo[] subscriberDataSharingDetails, String dataSharingGroup, Subscriber leavingSubscriber) 
			throws ApplicationException {
		int DS_SUBSCRIBER_SIZE = 100;
		Map<String, SubscriberInfo> subscriberMap = new HashMap<String, SubscriberInfo>();
		Collection subscribers = subscriberLifecycleHelper.retrieveSubscriberListByBAN(leavingSubscriber.getBanId(), DS_SUBSCRIBER_SIZE);
		for (Iterator subIter = subscribers.iterator(); subIter.hasNext();) {		
			SubscriberInfo subscriber = (SubscriberInfo) subIter.next();
			subscriberMap.put(subscriber.getSubscriberId(), subscriber);
		}
		return DataSharingUtil.createDataSharingSocTransferInfo(subscriberDataSharingDetails, dataSharingGroup, leavingSubscriber.getSubscriberId(), subscriberMap);
	}
	
	@Override
	public void cancelCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date activityDate, String cancelReasonCode, char depositReturnMethod,
			String waiverReason, String memoText, boolean suppressNotification, ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException {

		if (ban <= 0 || commSuiteInfo ==null || primarySubscriberNum == null || primarySubscriberNum.isEmpty()) {
			logger.debug("Invalid input parameters in cancelCommunicationSuiteCompanionSubs. ban=[" + ban + "], primarySubscriberNum=[" + primarySubscriberNum + "]");
			return;
		}
		
		List<String> companionPhNumList = commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList();
		
		if (commSuiteInfo.isRetrievedAsPrimary() == true && primarySubscriberNum.equals(commSuiteInfo.getPrimaryPhoneNumber()) &&
				companionPhNumList.isEmpty() == false) {
			
			// populate the waiveReasonArray
			String[] waiveReasonArray = new String[companionPhNumList.size()];
			for (int i = 0 ; i < waiveReasonArray.length; i++) {
				waiveReasonArray[i] = waiverReason;
			}
			// step 1: cancel the companion subscribers in KB 
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Trying to cancel companions ["+StringUtils.join(companionPhNumList, ",")+"] due to primary ["+ban+"/"+primarySubscriberNum+"] cancellation");
				}
				AccountInfo accountInfo = getAccountInformationHelper().retrieveLwAccountByBan(ban);
				getAccountLifecycleManager().cancelSubscribers(accountInfo, companionPhNumList.toArray(new String[companionPhNumList.size()]), waiveReasonArray, activityDate, cancelReasonCode, depositReturnMethod, memoText, getLogicalDate(), 
						true, false, getAccountLifecycleManagerSessionId(sessionId));
			}catch ( ApplicationException ae ) {
		  		if ("1111750".equals(ae.getErrorCode())) {
		  			logger.info("Need to cancel account ["+ban+"] in multi-companion cancellation");
		  			try {
		  				getAccountLifecycleFacade().cancelAccount(ban, activityDate, cancelReasonCode, String.valueOf(depositReturnMethod), waiverReason, memoText, primarySubscriberNum, suppressNotification, null,getAccountLifecycleFacadeSessionId(sessionId));
		  			}catch (Throwable t) {
		  				logger.error("Error cancelling communication suite's ban. " + commSuiteInfo, ae);
						createCompanionCancellationFailureMemo(ban, commSuiteInfo.getPrimaryPhoneNumber(), companionPhNumList, sessionId);
		  			}
		  		} else {
		  			logger.error("Error cancelling communication suite companions. " + commSuiteInfo, ae);
		  			createCompanionCancellationFailureMemo(ban, commSuiteInfo.getPrimaryPhoneNumber(), companionPhNumList, sessionId);
		  		}
		  		return;
			} catch (Throwable t) {
				logger.error("Error cancelling communication suite companions. " + commSuiteInfo, t);
				createCompanionCancellationFailureMemo(ban, commSuiteInfo.getPrimaryPhoneNumber(), companionPhNumList, sessionId);
				return;
			}
			
			if (logger.isDebugEnabled()) {
				logger.debug("Finished cancelling companions ["+StringUtils.join(companionPhNumList, ",")+"] due to primary ["+ban+"/"+primarySubscriberNum+"] cancellation");
			}
			
			// step 2: update the CODS for companion cancellation.
		
			try {
				if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
					for (String subId : companionPhNumList) {
						try {
							asyncUpdateProductInstance(ban, subId, null, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_CANCELLATION, sessionId);
						} catch (Throwable t) {
							logger.error("CODS companionPhoneNumberList cancel status change update error , ban = [" + ban + "] ,phoneNumber = [" + subId + "]", t);
						}
					}
				}
			} catch (Throwable t) {
				logger.error(
						"CODS companionPhoneNumberList cancel status change update error , ban = [" + ban + "] ,primary phoneNumber = [" + primarySubscriberNum + "],companionSubs =["
								+ Arrays.toString(commSuiteInfo.getCompanionPhoneNumberList().toArray()), t);
			}
			
			// step 4: update the SRPDS for companion cancellation.
			if (srpdsHeader != null) {

				List<LightWeightSubscriberInfo> lwOldSubList = commSuiteInfo.getActiveAndSuspendedCompanionSubscribers();
				for (LightWeightSubscriberInfo oldSubInfo : lwOldSubList) {
					try {
						SubscriberInfo newSubInfo = subscriberLifecycleHelper.retrieveSubscriberByBanAndPhoneNumber(oldSubInfo.getBanId(), oldSubInfo.getPhoneNumber());
						reportChangeSubscriberStatusInSRPDS(oldSubInfo.getBanId(), newSubInfo, newSubInfo.getDealerCode(), newSubInfo.getSalesRepId(), getClientIdentity(sessionId).getPrincipal(),
								oldSubInfo.getStatus(), newSubInfo.getStatus(), cancelReasonCode, activityDate, srpdsHeader);
					} catch (Throwable t) {
						logger.error("SRPDS companionPhoneNumberList cancel status change update error , ban = [" + ban + "] , phoneNumber = [" + oldSubInfo.getPhoneNumber() + "]", t);
					}
				}

			}
		} else {
			logger.info("companionPhoneNumberList is empty for the ban=[" + ban + "], primarySubscriberNum=[" + primarySubscriberNum + "]");
		}
	}
		
	private void createCompanionCancellationFailureMemo(int ban, String primarySubscriberId, List<String> companionCancellationList, String sessionId) {
		try {
			MemoInfo memoInfo = new MemoInfo();
			memoInfo.setBanId(ban);
			memoInfo.setSubscriberId(primarySubscriberId);
			memoInfo.setDate(new Date());
			memoInfo.setMemoType(CommunicationSuiteInfo.MEMO_COMPANION_CANCELLATION_FAILURE);
			memoInfo.setText("Primary device "+primarySubscriberId+" cancellation was successful. Companion device(s) " + StringUtil.listToString(companionCancellationList,",") +" could not be cancelled successfully. Communication suite information under the companion may be incorrect.");
			getAccountLifecycleManager().createMemo(memoInfo, getAccountLifecycleManagerSessionId(sessionId));
		} catch (Throwable t) {
			logger.error("createCompanionCancellationFailureMemo error ban[" + ban + "], primary=["+primarySubscriberId+"]", t);
		}
	}
	
	@Override
	public void suspendCommunicationSuiteCompanionSubs(int ban, String primarySubscriberNum, CommunicationSuiteInfo commSuiteInfo, Date effectiveDate, String suspendReasonCode, String userMemoText,
			String sessionId) throws ApplicationException {

		// Retrieve the communicationsuite info if it is null.
		if (commSuiteInfo == null) {
			commSuiteInfo = subscriberLifecycleHelper.retrieveCommunicationSuite(ban, primarySubscriberNum, CommunicationSuiteInfo.CHECK_LEVEL_PRIMARY_ONLY);
		}

		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == true && commSuiteInfo.getCompanionPhoneNumberList().isEmpty() == false) {
			List<String> companionPhoneNumberList = commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList();
			if (companionPhoneNumberList != null && companionPhoneNumberList.isEmpty() == false) {
				// Suspend the communicationsuite companion subscribers.
				try {
					getAccountLifecycleManager().suspendSubscribers(ban, effectiveDate, suspendReasonCode, companionPhoneNumberList.toArray(new String[companionPhoneNumberList.size()]), userMemoText,
							sessionId);
				} catch (Throwable t) {
					logger.error("Error suspending communication suite companions. " + commSuiteInfo, t);
					return;
				}

				// Update the CODS.
				try {
					if (effectiveDate == null || !DateUtil.isAfter(effectiveDate, getReferenceDataFacade().getLogicalDate())) {
						for (String subId : companionPhoneNumberList) {
							asyncUpdateProductInstance(ban, subId, null, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_SUSPENSION, sessionId);
						}
					}
				} catch (Throwable t) {
					logger.error("CODS companionPhoneNumberList suspend status change update error , ban = [" + ban + "] ,primary phoneNumber = [" + primarySubscriberNum + "],companionSubs =["
							+ Arrays.toString(commSuiteInfo.getCompanionPhoneNumberList().toArray()), t);
				}
			}
		}
		else{
			logger.info("suspend communicationInfo is null (or) companionPhoneNumberList is empty for the ban=[" + ban + "], primarySubscriberNum=[" + primarySubscriberNum + "]");
		}
	}
	
	@Override
	public void removeFromCommunicationSuite(int ban, String companionPhoneNumber, CommunicationSuiteInfo communicationSuite, boolean silentFailure) throws ApplicationException {
		if (logger.isDebugEnabled()) {
			logger.debug("removeFromCommunicationSuite..ban="+ban+",companionPhoneNumber="+companionPhoneNumber);
		}
		
		try {
			if (communicationSuite != null && communicationSuite.getActiveAndSuspendedCompanionPhoneNumberList().contains(companionPhoneNumber) && ban == communicationSuite.getBan()) {
				communicationSuiteMgmtSvcDao.removeFromCommunicationSuite(ban, companionPhoneNumber, communicationSuite.getPrimaryPhoneNumber());
			}else {
				if (logger.isDebugEnabled()) {
					logger.debug("removeFromCommunicationSuite criteria not met.. ban="+ban+",companionPhoneNumber="+companionPhoneNumber);
				}
			}
		}catch (Throwable t) {
			logger.error("Error breaking communication suite ban=["+ban+"], companionPhoneNumber=["+companionPhoneNumber+"], commSuite=["+communicationSuite+"]", t);
			if (silentFailure == false) {
				handleException(t);
			}
		}
	}
	
	private SapccUpdateAccountPurchaseBo getSapccUpdateAccountPurchaseBo(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, AuditInfo auditInfo, String sessionId)
			throws ApplicationException {
		// Create and initialize the context
		SapccUpdateAccountPurchaseContext context = new SapccUpdateAccountPurchaseContext(subscriberInfo, subscriberContractInfo, ejbController, getClientIdentity(sessionId), auditInfo);
		context.initialize();
		// Create and return the business object
		return new SapccUpdateAccountPurchaseBo(context);
	}
	
	public void updateSapccAccountPurchaseAmount(SubscriberInfo subscriberInfo, double domesticAmount, double roamingAmount, String actionCode, String applicationId) throws ApplicationException {
		ocssamServiceDao.updateAccountPurchaseAmount(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId(), subscriberInfo.getPhoneNumber(), domesticAmount, roamingAmount, actionCode, applicationId);
	}
	
	// Added for BC Integrations July 2018
	private BusinessConnectBo getBusinessConnectBo(SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, BusinessConnectContextTypeEnum contextType, AuditInfo auditInfo, String sessionId)
			throws ApplicationException {
		// Create and initialize the context
		BusinessConnectContext context = new BusinessConnectContext(contextType, subscriberInfo, subscriberContractInfo, ejbController, getClientIdentity(sessionId), auditInfo);
		context.initialize();
		// Create and return the business object
		return new BusinessConnectBo(context);
	}
	
	// Added for BC Integrations July 2018
	@Override
	public VOIPAccountInfo getVOIPAccountInfo(int ban) throws ApplicationException {
		return voipSupplementaryServiceDao.getVOIPAccountInfo(ban);
	}		

	// Added for BC Integrations July 2018
	@Override
	public void addLicenses(int ban, String subscriptionId,List<String> switchCodes) throws ApplicationException {
		voipLicensePurchaseServiceDao.addLicenses(ban, subscriptionId, switchCodes);
	}
	
	// Added for BC Integrations July 2018
	@Override
	public void removeLicenses(int ban, String subscriptionId,List<String> switchCodes) throws ApplicationException {
		voipLicensePurchaseServiceDao.removeLicenses(ban, subscriptionId, switchCodes);
	}

	// Added for BC Integrations July 2018
	@Override
	public void asyncSubmitProvisioningLicenseOrder(ProvisioningLicenseInfo provisioningLicenseInfo) throws ApplicationException {
		try {
			if (StringUtils.equalsIgnoreCase(provisioningLicenseInfo.getTransactionType(), ProvisioningLicenseInfo.PROV_LICENSE_TRANSACTION_TYPE_ADD)) {
				addLicenses(provisioningLicenseInfo.getBan(), provisioningLicenseInfo.getSubscriptionId(), provisioningLicenseInfo.getSwitchCodes());
			} else if (StringUtils.equalsIgnoreCase(provisioningLicenseInfo.getTransactionType(), ProvisioningLicenseInfo.PROV_LICENSE_TRANSACTION_TYPE_REMOVE)) {
				removeLicenses(provisioningLicenseInfo.getBan(), provisioningLicenseInfo.getSubscriptionId(), provisioningLicenseInfo.getSwitchCodes());
			}
		} catch (Throwable t) {
			queueSender.send(provisioningLicenseInfo, "msgSubTypeProvisioningLicenseOrder", null);
		}
	}

	@Override
	public void suspendPortedInSubscriber(int ban , String phoneNumber, String activityReasonCode, Date activityDate, String portOutInd,String sessionId) throws ApplicationException {
		subscriberLifeCycleManager.suspendPortedInSubscriber(ban, phoneNumber, activityReasonCode, activityDate, portOutInd, sessionId);
	}
	
	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param phoneNumber		Mandatory, 10 digits numeric.
	 * @param networkType		One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param newLocalIMSI		Mandatory.
	 * @param newRemoteIMSI		Not Mandatory.
	 * @param usimId			Mandatory.
	 * @throws ApplicationException
	 * 
	 */
	@Deprecated
	@Override
	public void changeIMSIs(String phoneNumber, String networkType, String newLocalIMSI, String newRemoteIMSI) throws ApplicationException {
		// call to RCM WS changeIMSI() operation
		logger.info("CHANGE IMSI [phoneNumber: " + phoneNumber + ", NetworkType:" + networkType + ", newLocalIMSI:" + newLocalIMSI + ",newRemoteIMSI" + newRemoteIMSI + "]");
		logicalResourceServiceDao.changeIMSIs(phoneNumber, networkType, newLocalIMSI, newRemoteIMSI);
	}

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
	@Deprecated
	@Override
	public void changeNetwork(String phoneNumber, String oldNetworkType, String newNetworkType, String localIMSI, String remoteIMSI, String usimId) throws ApplicationException { 
		// Do nothing
	}

	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param phoneNumber	Mandatory, 10 digits numeric.
	 * @param networkType	One of these 3 values : CDMA, IDEN, HSPA (from the NetworkType class)
	 * @param status		Mandatory. Valid values are AS or AI. This is the Provisioning Status.
	 * @throws ApplicationException
	 */
	@Deprecated
	@Override
	public void setTNStatus(String phoneNumber, String networkType, String status) throws ApplicationException {		
		logger.info("SET TN STATUS [phoneNumber: " + phoneNumber + ", networkType:" + networkType + "]");		
		// call to RCM WS setTNStatus() operation		
		logicalResourceServiceDao.setTNStatus(phoneNumber, networkType, status);
	}

	/**
	 * @deprecated since April 2013 release for IPv6 Phase 3
	 * 
	 * @param subscriberInfo
	 * @param equipmentChangeRequest
	 * @return
	 * @throws ApplicationException
	 */
	@Deprecated
	private EquipmentChangeRequestInfo updateEquipmentChangeInRCM(SubscriberInfo subscriberInfo, EquipmentChangeRequestInfo equipmentChangeRequest) throws ApplicationException {
		EquipmentInfo newEquipment = (EquipmentInfo) equipmentChangeRequest.getNewEquipment();
		EquipmentInfo oldEquipment = (EquipmentInfo) equipmentChangeRequest.getCurrentEquipment();
		return equipmentChangeRequest;
	}

	@Override
	public TestPointResultInfo testEnterpriseConsumerProfileRegistrationService() {
		return identityProfileService.testEnterpriseConsumerProfileRegistrationService();
	}

	@Override
	public void repairCommunicationSuite(CommunicationSuiteRepairData data, String sessionId) throws ApplicationException {
		if (data != null) {
			logger.debug("repairCommunicationSuite "+data);
			commSuiteMgmtRestSvcDao.repairCommunicationSuite(data, sessionId);
		}
	}
	
	@Override
	public void asyncRepairCommunicationSuiteDueToPhoneNumberChange(int ban, String newSubscriberId, String oldSubscriberId, String sessionId) throws ApplicationException {
		CommunicationSuiteRepairData repairData = new CommunicationSuiteRepairData();
		repairData.setBan(ban);
		repairData.setSubscriberId(newSubscriberId);
		repairData.setOldSubscriberId(oldSubscriberId);
		repairData.setActionCode(CommunicationSuiteRepairData.COMM_SUITE_RESYNC_ACTION_CODE);
		repairData.setReasonCode(CommunicationSuiteRepairData.COMM_SUITE_CTN_CHANGE_REPAIR_REASON_CODE);
		repairData.setSessionId(sessionId);
		queueSender.send(repairData,"msgSubTypeCommunicationSuite");
	}
	
	public void asyncRepairCommunicationSuiteDueToSubscriberResumed(int ban, String subscriberId, String sessionId) throws ApplicationException {
		CommunicationSuiteRepairData data = new CommunicationSuiteRepairData();
		data.setBan(ban);
		data.setSubscriberId(subscriberId);
		data.setApplyToWholeSuiteInd(false);
		data.setActionCode(CommunicationSuiteRepairData.COMM_SUITE_RESET_ACTION_CODE);
		data.setReasonCode(CommunicationSuiteRepairData.COMM_SUITE_SUB_RESUME_REPAIR_REASON_CODE);
		data.setSessionId(sessionId);
		queueSender.send(data,"msgSubTypeCommunicationSuite");
	}

	@Override
	public TestPointResultInfo testCommunicationSuiteMgmtRestService() {
		return commSuiteMgmtRestSvcDao.test();
	}

	@Override
	public TestPointResultInfo testCommunicationSuiteMgmtService() {
		return communicationSuiteMgmtSvcDao.test();
	}
}
