package com.telus.cmb.account.lifecyclefacade.svc.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.telus.api.ApplicationException;
import com.telus.api.ClientAPI;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PostpaidAccount;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.ProductSubscriberList;
import com.telus.api.account.Subscriber;
import com.telus.api.account.SubscriberIdentifier;
import com.telus.api.equipment.AirtimeCard;
import com.telus.api.equipment.Equipment;
import com.telus.api.reference.PrepaidRechargeDenomination;
import com.telus.api.reference.ReferenceDataManager;
import com.telus.api.reference.SeatType;
import com.telus.api.servicerequest.ServiceRequestHeader;
import com.telus.cmb.account.bo.BillNotificationBo;
import com.telus.cmb.account.bo.ProcessPaymentBo;
import com.telus.cmb.account.bo.context.ProcessPaymentContext;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.dao.BillingAccountDataMgmtDao;
import com.telus.cmb.account.lifecyclefacade.dao.CardPaymentServiceDao;
import com.telus.cmb.account.lifecyclefacade.dao.CconDao;
import com.telus.cmb.account.lifecyclefacade.dao.CreditProfileServiceDao;
import com.telus.cmb.account.lifecyclefacade.dao.EnterpriseAddressDao;
import com.telus.cmb.account.lifecyclefacade.dao.SummarizedDataUsageDao;
import com.telus.cmb.account.lifecyclefacade.dao.WirelessCreditAssessmentProxyServiceDao;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacadeTestPoint;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.account.payment.PaymentBo;
import com.telus.cmb.account.payment.PaymentBo.PaymentProcessCallback;
import com.telus.cmb.account.payment.kafka.AccountStatusEventPublisher;
import com.telus.cmb.account.payment.kafka.CreditEventPublisher;
import com.telus.cmb.account.payment.kafka.MultiSubscriberStatusEventPublisher;
import com.telus.cmb.account.payment.kafka.PaymentEventPublisher;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.account.utilities.AuditHeaderUtil;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceDao;
import com.telus.cmb.common.dao.provisioning.WirelessProvisioningServiceRequestFactory;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.ejb.AbstractLifecycleFacade;
import com.telus.cmb.common.ejb.LdapTestPoint;
import com.telus.cmb.common.eligibility.CommunicationSuiteEligibilityUtil;
import com.telus.cmb.common.eligibility.EligibilityUtilities;
import com.telus.cmb.common.eligibility.EnterpriseManagementEligibilityCheckCriteria;
import com.telus.cmb.common.eligibility.EnterpriseManagementEligibilityEvaluationStrategy;
import com.telus.cmb.common.enums.BillNotificationActivityType;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.JmsSupport;
import com.telus.cmb.common.kafka.account.AccountEventPublisher;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileRegistrationOrigin;
import com.telus.cmb.common.svc.identityprofile.IdentityProfileService;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.wsclient.PolicyException_v1;
import com.telus.cmb.wsclient.ServiceException_v1;
import com.telus.cmb.wsclient.ServiceException_v3;
import com.telus.eas.account.credit.info.CreditAssessmentInfo;
import com.telus.eas.account.credit.info.MatchedAccountInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.BusinessCreditInfo;
import com.telus.eas.account.info.BusinessRegistrationInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.account.info.HCDclpActivationOptionDetailsInfo;
import com.telus.eas.account.info.PaymentArrangementEligibilityResponseInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.account.info.PaymentNotificationResponseInfo;
import com.telus.eas.account.info.PersonalCreditInfo;
import com.telus.eas.account.info.PostpaidAccountCreationResponseInfo;
import com.telus.eas.account.info.PostpaidBusinessPersonalAccountInfo;
import com.telus.eas.account.info.PostpaidBusinessRegularAccountInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.ProductSubscriberListInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.BillingAccountEnterpriseDataInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.CommunicationSuiteInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.eas.utility.info.ProvisioningRequestInfo;
import com.telus.eas.utility.info.SegmentationInfo;
import com.telus.framework.config.ConfigContext;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.PolicyFaultInfo;
import com.telus.tmi.xmlschema.xsd.common.exceptions.exceptions_v1_0.ServiceFaultInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.exceptions_v3.FaultExceptionDetailsType;

@Stateless(name = "AccountLifecycleFacade", mappedName = "AccountLifecycleFacade")
@Remote({ AccountLifecycleFacade.class, AccountLifecycleFacadeTestPoint.class, LdapTestPoint.class })
@RemoteHome(AccountLifecycleFacadeHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccountLifecycleFacadeImpl extends AbstractLifecycleFacade implements AccountLifecycleFacade, AccountLifecycleFacadeTestPoint {

	private static final Logger LOGGER = Logger.getLogger(AccountLifecycleFacadeImpl.class);

	private static final String ECA_SIGN = "@ClientAPI-ECA";
	private static final String FOLLOWUP_CORPORATE_DESTINATION = "12633";
	private static final String FOLLOWUP_CORPORATE_NEW_ACCOUNT = "CMCN";
	private static final int ACTIVATION_TYPE_NORMAL = 0;
	private static final int ACTIVATION_TYPE_AIRTIME_CARD = 7;

	@Autowired
	private AmdocsSessionManager amdocsSessionManager;

	@EJB
	private AccountInformationHelper accountInformationHelper;

	@EJB
	private AccountLifecycleManager accountLifecycleManager;

	@Autowired
	private JmsSupport queueSender;

	@Autowired
	private CconDao cconDao;

	@Autowired
	private EnterpriseAddressDao enterpriseAddressDao;

	@Autowired
	private BillingAccountDataMgmtDao billingAccountDataMgmtDao;

	@Autowired
	@Qualifier("facadeTestPointDao")
	private DataSourceTestPointDao testPointDao;

	@Autowired
	private CreditProfileServiceDao creditProfileServiceDao;

	@Autowired
	private SummarizedDataUsageDao summarizedDataUsageDao;

	@Autowired
	private CardPaymentServiceDao cardPaymentServiceDao;

	@Autowired
	private EJBController ejbController;

	@Autowired
	private WirelessProvisioningServiceDao wirelessProvisioningServiceDao;

	@Autowired
	private IdentityProfileService identityProfileService;

	@Autowired
	private AccountEventPublisher accountEventPublisher;

	@Autowired
	private WirelessCreditAssessmentProxyServiceDao wirelessCreditAssessmentProxyServiceDao;

	@Autowired
	private BillNotificationBo billNotificationBo;
	
	@Autowired
	private PaymentEventPublisher paymentEventPublisher;
	
	@Autowired
	private CreditEventPublisher creditEventPublisher;
	
	@Autowired
	private AccountStatusEventPublisher accountStatusEventPublisher;
	
	@Autowired
	private MultiSubscriberStatusEventPublisher multiSubEventPublisher;
	
	public void setIdentityProfileService(IdentityProfileService identityProfileService) {
		this.identityProfileService = identityProfileService;
	}

	@PostConstruct
	public void initialize() {
		initializeEjbController();
	}

	private void initializeEjbController() {

		ejbController.setEjb(AccountLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_FACADE, this);
		ejbController.setEjb(AccountInformationHelper.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_INFORMATION_HELPER, accountInformationHelper);
		ejbController.setEjb(AccountLifecycleManager.class, EJBUtil.TELUS_CMBSERVICE_ACCOUNT_LIFECYCLE_MANAGER, accountLifecycleManager);

		ejbController.setEjb(ProductEquipmentHelper.class, EJBUtil.TELUS_CMBSERVICE_PRODUCT_EQUIPMENT_HELPER, null);
		ejbController.setEjb(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE, null);
		ejbController.setEjb(ReferenceDataHelper.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER, null);

		ejbController.setEjb(SubscriberLifecycleFacade.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_FACADE, null);
		ejbController.setEjb(SubscriberLifecycleHelper.class, EJBUtil.TELUS_CMBSERVICE_SUBSCRIBER_LIFECYCLE_HELPER, null);
	}

	private SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleHelper.class);
	}

	private SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleFacade.class);
	}

	/**
	 * This is used by AccountLifecycleFacadeImplTest. Not used by any business
	 * objects.
	 * 
	 * @param accountLifecycleManager
	 */
	public void setAccountLifecycleManager(AccountLifecycleManager accountLifecycleManager) {
		this.accountLifecycleManager = accountLifecycleManager;
	}

	private ReferenceDataFacade getReferenceDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	private ProductEquipmentHelper getProductEquipmentHelper() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentHelper.class);
	}

	public void setCconDao(CconDao cconDao) {
		this.cconDao = cconDao;
	}

	public void setEnterpriseAddressDao(EnterpriseAddressDao enterpriseAddressDao) {
		this.enterpriseAddressDao = enterpriseAddressDao;
	}

	public void setBillingAccountDataMgmtDao(BillingAccountDataMgmtDao billingAccountDataMgmtDao) {
		this.billingAccountDataMgmtDao = billingAccountDataMgmtDao;
	}

	@Override
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(userId, password, applicationId);
		return amdocsSessionManager.openSession(identity);
	}

	@Override
	public CreditCardResponseInfo validateCreditCard(@Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {

		// setup 'default' attributes in case they are not set or not set properly
		creditCardTransactionInfo.setAmount(0);
		creditCardTransactionInfo.setTransactionType(CreditCardTransactionInfo.TYPE_CREDIT_CARD_VALIDATION);
		creditCardTransactionInfo.setBan(0);
		creditCardTransactionInfo.setChargeAuthorizationNumber("");

		return performCreditCardTransaction(creditCardTransactionInfo, sessionId);
	}

	/**
	 * Performs credit card transactions. The following types are available:
	 *  - Credit Card Authentication (validating a credit card but not charging anything)
	 *  - Credit Card Charge 
	 *  - Reversal of a Credit Card Charge
	 * 
	 * @param CreditCardTransactionInfo all information necessary to perform transaction
	 * @return long Authorization Number
	 * @throws ApplicationException
	 * @see CreditCardTransactionInfo
	 */
	protected CreditCardResponseInfo performCreditCardTransaction(@Sensitive CreditCardTransactionInfo ccTxnInfo, final String sessionId) throws ApplicationException {

		if (ccTxnInfo.getAuditHeader() == null) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.MISSING_AUDIT_HEADER_FOR_CC, "Missing required AuditHeader for CreditCard related transaction.", "");
		}
		if (ccTxnInfo.getCreditCardInfo() == null) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.MISSING_CC_INFORMATION, "Missing CreditCard information.", "");
		}
		if (ccTxnInfo.getCreditCardInfo().hasToken() == false) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.MISSING_CC_TOKEN, "Missing CreditCard token", "");
		}

		String first6 = ccTxnInfo.getCreditCardInfo().getLeadingDisplayDigits();
		String last4 = ccTxnInfo.getCreditCardInfo().getTrailingDisplayDigits();
		if (first6 == null || first6.trim().length() == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.MISSING_CC_LEADING_DISPLAY_DIGITS, "Missing CreditCard number leadingDisplayDigits - the first six digits.", "");
		}
		if (last4 == null || last4.trim().length() == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.MISSING_CC_TRAILING_DISPLAY_DIGITS, "Missing CreditCard number trailingDisplayDigits - the last four digits.", "");
		}

		final ClientIdentity ci = this.getClientIdentity(sessionId);
		String applicationId = ci.getApplication();
		String userId = ci.getPrincipal();
		AuditHeader auditHeader = appendToAuditHeader(ccTxnInfo.getAuditHeader(), applicationId, userId);

		PaymentProcessCallback callBack = new PaymentProcessCallback() {

			@Override
			public void createPAYIMemo(MemoInfo memoInfo) throws ApplicationException {
				String almSessionId = accountLifecycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
				asyncCreateMemo(memoInfo, almSessionId);
			}
		};

		return new PaymentBo(cconDao, cardPaymentServiceDao).processPayment(applicationId, ccTxnInfo, auditHeader, callBack);
	}

	private ClientIdentity getClientIdentity(String sessionId) throws ApplicationException {

		ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
		if (ci == null) {
			throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
		}

		return ci;
	}

	private AuditHeader appendToAuditHeader(AuditHeader header, String applicationId, String userId) {

		boolean isLastAppInfoECA = false;
		AuditHeader.AppInfo[] apps = header.getAppInfos();
		// check last app info to if it is ECA
		if (apps.length > 0) {
			String userid = apps[apps.length - 1].getUserId();
			if (userid != null && userid.indexOf(ECA_SIGN) != -1) {
				isLastAppInfoECA = true;
			}
		}
		if (isLastAppInfoECA) { // if so, no need to append header again
			return header;
		} else {
			// the AuditHeader does not contain ECA info, so make a clone and append it
			AuditHeaderInfo auditHeader = new AuditHeaderInfo(header);
			try {
				auditHeader.appendAppInfo("kbUser=" + userId + ", appCode=" + applicationId + ECA_SIGN, ClientAPI.CMDB_ID, InetAddress.getLocalHost().getHostAddress());
			} catch (UnknownHostException e) {
				// if can't get local IP, continue the transaction
			}

			return auditHeader;
		}
	}

	private boolean isCreditCheckForBusiness(AccountInfo accountInfo) {

		char accountType = accountInfo.getAccountType();
		char accountSubType = accountInfo.getAccountSubType();
		if (accountType == AccountSummary.ACCOUNT_TYPE_CONSUMER
				|| (accountType == AccountSummary.ACCOUNT_TYPE_BUSINESS
						&& (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_IDEN_PERSONAL
								|| accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_ANYWHERE_PERSONAL || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_PCS_CONNECT_PERSONAL))
				|| (accountType == AccountSummary.ACCOUNT_TYPE_CORPORATE
						&& (accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_EMPLOYEE || accountSubType == AccountSummary.ACCOUNT_SUBTYPE_CORP_PCS_INDIVIDUAL))) {
			return false;
		}

		return true;
	}

	@Override
	public String payBill(double amount, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, @BANValue int ban, String paymentSourceType, String paymentSourceID, AccountInfo accountInfo,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		return payBillOrDeposit(ban, false, paymentSourceType, paymentSourceID, amount, creditCardTransactionInfo, accountInfo, notificationSuppressionInd, auditInfo, sessionId);
	}

	@Override
	public String payDeposit(double amount, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, @BANValue int ban, String paymentSourceType, String paymentSourceID, String sessionId)
			throws ApplicationException {
		return payBillOrDeposit(ban, true, paymentSourceType, paymentSourceID, amount, creditCardTransactionInfo, null, true, null, sessionId);
	}

	/**
	 * Pay Bill or Deposit - Charge Credit Card And Apply Payment or Deposit to billing account.
	 * 
	 * @param auditInfo
	 * @param notificationSuppressionInd
	 * @param accountInfo
	 *
	 * @param int billing account number
	 * @param boolean pay deposit indicator (true - pay deposit / false - pay bill)
	 * @param String payment source type
	 * @param String payment source id
	 * @param double payment amount
	 * @param CreditCardInfo credit card information
	 *
	 * @return long Authorization Number
	 * @throws ApplicationException
	 *
	 * @see PaymentInfo
	 */
	protected String payBillOrDeposit(@BANValue int ban, boolean payDeposit, String paymentSourceType, String paymentSourceID, double amount,
			@Sensitive CreditCardTransactionInfo creditCardTransactionInfo, AccountInfo accountInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId)
					throws ApplicationException {

		String authorizationNumber = "";
		String almSessionId = "";

		// set amount in CreditCardTransactionInfo if necessary
		if (creditCardTransactionInfo.getAmount() == 0) {
			creditCardTransactionInfo.setAmount(amount);
		}

		// First Step - charge credit card
		// check for supported credit cards
		if (creditCardTransactionInfo.getCreditCardInfo().getType().trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.CREDIT_CARD_TYPE_NOT_SUPPORTED, "Credit Card Type not supported", "");
		}

		/* Try to connect to KB earlier before processing payment. 
		 * This helps to prevent any process payment if downstream (KB/Tuxedo) is down and payment has been processed already and needs to be voided */
		try {
			ClientIdentity ci = this.getClientIdentity(sessionId);
			almSessionId = accountLifecycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
			accountLifecycleManager.testAmdocsConnectivity(almSessionId); //test actual KB connectivity as the session from openSession call could be from the cache
		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.KB_CONNECT_ERROR, "Error connecting to KB. Either system is down or the KB credential is incorrect.", t);
		}

		// charge credit card
		authorizationNumber = applyCreditCardCharge(creditCardTransactionInfo, sessionId);
		LOGGER.debug("Credit Card Charge - authorization number: " + authorizationNumber);

		// check for authorization number not being empty
		if (authorizationNumber.trim().equals("")) {
			LOGGER.debug("Credit Card Transaction not successful - Bank Interface Handler not responding.");
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.CREDIT_CARD_SUBSYSTEM_UNAVAILABLE, "Credit Card Transaction not successful - Bank Interface Handler not responding.",
					"");
		}

		creditCardTransactionInfo.getCreditCardInfo().setAuthorizationCode(authorizationNumber);
		creditCardTransactionInfo.setChargeAuthorizationNumber(authorizationNumber);

		// Second Step - apply payment to billing account
		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setBan(ban);
		paymentInfo.setAmount(amount);
		paymentInfo.setPaymentSourceID(paymentSourceID);
		paymentInfo.setPaymentSourceType(paymentSourceType);
		paymentInfo.setCreditCardInfo(creditCardTransactionInfo.getCreditCardInfo());
		paymentInfo.setDepositPaymentIndicator(payDeposit);
		paymentInfo.setDepositDate(new Date());
		paymentInfo.setAllowOverpayment(true);
		paymentInfo.setPaymentMethod(TelusConstants.PAYMENT_METHOD_CREDIT_CARD);
		paymentInfo.setPaymentSourceID(paymentSourceID);
		paymentInfo.setPaymentSourceType(paymentSourceType);
		paymentInfo.setAuthorizationNumber(authorizationNumber);
		try {
			// apply payment to account
			accountLifecycleManager.applyPaymentToAccount(paymentInfo, almSessionId);
			if (!payDeposit) {
				//Refresh the accountInfo to get updated payment due buckets from KB tables.
				accountInfo = accountInformationHelper.retrieveAccountByBan(accountInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
				paymentEventPublisher.publishMakePayment(accountInfo, paymentInfo, createAuditInfoFromSession(sessionId), getLogicalDate(), notificationSuppressionInd);
			}

			return authorizationNumber;

		} catch (RuntimeException re) {
			// Third Step - reverse credit card charge IF APPLY PAYMENT TO BILLING ACCOUNT FAILED
			// reverse credit card charge
			voidCreditCardCharge(creditCardTransactionInfo, sessionId);
			throw re;
		} catch (ApplicationException ae) {
			// Third Step - reverse credit card charge IF APPLY PAYMENT TO BILLING ACCOUNT FAILED
			// reverse credit card charge
			voidCreditCardCharge(creditCardTransactionInfo, sessionId);
			throw ae;
		}
	}

	@Override
	public void voidCreditCardCharge(@Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {

		if (creditCardTransactionInfo.getChargeAuthorizationNumber() == null || creditCardTransactionInfo.getChargeAuthorizationNumber().length() == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Authorization code is null or empty.", "");
		}
		// setup 'default' attributes in case they are not set or not set properly
		creditCardTransactionInfo.setTransactionType(CreditCardTransactionInfo.TYPE_CREDIT_CARD_VOID);
		String authorizationNumberForReversal = performCreditCardTransaction(creditCardTransactionInfo, sessionId).getAuthorizationCode();
		LOGGER.debug("Credit Card Void - authorization number: " + authorizationNumberForReversal);
	}

	@Override
	public String applyCreditCardCharge(@Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
		// setup 'default' attributes in case they are not set or not set properly
		creditCardTransactionInfo.setTransactionType(CreditCardTransactionInfo.TYPE_CREDIT_CARD_PAYMENT);
		creditCardTransactionInfo.setChargeAuthorizationNumber("");
		return performCreditCardTransaction(creditCardTransactionInfo, sessionId).getAuthorizationCode();
	}

	@Override
	public String reverseCreditCardCharge(@Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
		// setup 'default' attributes in case they are not set or not set properly
		creditCardTransactionInfo.setTransactionType(CreditCardTransactionInfo.TYPE_CREDIT_CARD_REFUND);
		return performCreditCardTransaction(creditCardTransactionInfo, sessionId).getAuthorizationCode();
	}

	@Override
	public void refundCreditCardPayment(@BANValue int banId, int paymentSeq, String reasonCode, String memoText, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId)
			throws ApplicationException {

		String authorizationCode = reverseCreditCardCharge(creditCardTransactionInfo, sessionId);
		creditCardTransactionInfo.setChargeAuthorizationNumber(authorizationCode);
		try {
			ClientIdentity ci = this.getClientIdentity(sessionId);
			String almSessionId = accountLifecycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
			accountLifecycleManager.refundPaymentToAccount(banId, paymentSeq, reasonCode, memoText, true, authorizationCode, almSessionId);

		} catch (RuntimeException re) {
			LOGGER.error(re);
			// reverse credit card charge
			voidCreditCardCharge(creditCardTransactionInfo, sessionId);
			throw re;
		} catch (ApplicationException ae) {
			// reverse credit card charge
			voidCreditCardCharge(creditCardTransactionInfo, sessionId);
			throw ae;
		}
	}

	@Override
	public CreditCardResponseInfo validateCreditCard(@BANValue int billingAccountNumber, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String sessionId) throws ApplicationException {
		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(billingAccountNumber, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		return validateCreditCard(deriveCreditCardHolderInfo(accountInfo, creditCardTransactionInfo.getCreditCardInfo(), creditCardTransactionInfo.getAuditHeader()), sessionId);
	}

	private CreditCardTransactionInfo deriveCreditCardHolderInfo(AccountInfo accountInfo, @Sensitive CreditCardInfo creditCard, AuditHeader auditHeader) {

		CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		if (accountInfo != null) {
			creditCardTransactionInfo.setBan(accountInfo.getBanId());
			creditCardTransactionInfo.setBrandId(accountInfo.getBrandId());
			if (accountInfo.isPostpaidConsumer() || accountInfo.isPostpaidBusinessPersonal()) {
				creditCardTransactionInfo.getCreditCardHolderInfo().setFirstName(((PostpaidConsumerAccount) accountInfo).getName().getFirstName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setlastName(((PostpaidConsumerAccount) accountInfo).getName().getLastName());
			} else if (accountInfo.isPrepaidConsumer()) {
				creditCardTransactionInfo.getCreditCardHolderInfo().setFirstName(((PrepaidConsumerAccount) accountInfo).getName().getFirstName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setlastName(((PrepaidConsumerAccount) accountInfo).getName().getLastName());
				creditCardTransactionInfo.getCreditCardHolderInfo().setBirthDate(((PrepaidConsumerAccount) accountInfo).getBirthDate());
			} else {
				CreditCardHolderInfo holderInfo = creditCardTransactionInfo.getCreditCardHolderInfo();
				if (accountInfo instanceof PostpaidConsumerAccount) {
					PostpaidConsumerAccount pca = (PostpaidConsumerAccount) accountInfo;
					holderInfo.setFirstName(pca.getName().getFirstName());
					holderInfo.setlastName(pca.getName().getLastName());
				} else {
					holderInfo.setlastName(accountInfo.getFullName());
				}
			}
			creditCardTransactionInfo.getCreditCardHolderInfo().setClientID("" + accountInfo.getBanId());
			creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(accountInfo.getAddress().getPostalCode());
			creditCardTransactionInfo.getCreditCardHolderInfo().setActivationDate(accountInfo.getCreateDate());
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountType("" + accountInfo.getAccountType());
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountSubType("" + accountInfo.getAccountSubType());
		} else {
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountType("" + AccountSummary.ACCOUNT_TYPE_CONSUMER);
			creditCardTransactionInfo.getCreditCardHolderInfo().setAccountSubType("" + AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID);
			creditCardTransactionInfo.getCreditCardHolderInfo().setActivationDate(new Date());
			creditCardTransactionInfo.getCreditCardHolderInfo().setPostalCode(creditCard.getAddress().getPostalCode());
		}
		creditCardTransactionInfo.setAuditHeader(auditHeader);
		creditCardTransactionInfo.setCreditCardInfo(creditCard);

		return creditCardTransactionInfo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void registerTopUpCreditCard(@BANValue int billingAccountNumber, @Sensitive CreditCardInfo creditCard, String sessionId) throws ApplicationException {

		if (creditCard == null) {
			return;
		}

		PaymentMethodInfo info = new PaymentMethodInfo();
		info.setPaymentMethod(PaymentMethod.PAYMENT_METHOD_REGULAR);
		info.setCreditCard0(creditCard);
		info.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD);
		info.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA);

		final ClientIdentity ci = this.getClientIdentity(sessionId);
		updatePaymentMethod(billingAccountNumber, null, info, false, null, sessionId);
		ArrayList<SubscriberInfo> subscribers = (ArrayList<SubscriberInfo>) getSubscriberLifecycleHelper().retrieveSubscriberListByBAN(billingAccountNumber, 1, false);

		String phoneNumber = null;
		// TODO Wilson to fix the potential NullPointerException
		if (subscribers == null || subscribers.size() > 0) {
			phoneNumber = subscribers.get(0).getPhoneNumber();
		}
		if (phoneNumber != null) {
			accountLifecycleManager.updateTopupCreditCard(Integer.toString(billingAccountNumber), phoneNumber, creditCard, ci.getPrincipal(), false, true);
			LOGGER.debug("== PrepaidConsumer :: " + phoneNumber + " :: (new) CreditCard :: " + creditCard.getTrailingDisplayDigits() + " :: " + creditCard.getExpiryYear() + "/"
					+ creditCard.getExpiryMonth());
		} else {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "There are no active subscriber for the given ban " + billingAccountNumber, "");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateAccountPassword(int ban, String newPassword, String sessionId) throws ApplicationException {

		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);

		ClientIdentity ci = this.getClientIdentity(sessionId);
		String almSessionId = accountLifecycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
		accountLifecycleManager.updateAccountPassword(ban, newPassword, almSessionId);

		if (accountInfo.isPrepaidConsumer()) {
			Collection<SubscriberInfo> subscriberInfoList = getSubscriberLifecycleHelper().retrieveSubscriberListByBAN(ban, 1);
			if (subscriberInfoList != null) {
				for (SubscriberInfo subscriberInfo : subscriberInfoList) {
					accountLifecycleManager.updateAccountPIN(accountInfo.getBanId(), subscriberInfo.getPhoneNumber(), subscriberInfo.getSerialNumber(), accountInfo.getPin(), newPassword,
							ci.getPrincipal());
					LOGGER.debug("== PrepaidConsumer :: " + subscriberInfo.getPhoneNumber() + " :: (new) PIN :: " + accountInfo.getPin());
				}
			}
		}
	}

	@Override
	public void asyncCreateMemo(MemoInfo memoInfo, String sessionId) throws ApplicationException {
		if (memoInfo.getBanId() == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Missing required parameter BAN on memo.", "");
		} else if (memoInfo.getMemoType() == null || memoInfo.getMemoType().trim().isEmpty()) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Missing required parameter memo type on memo. memoType=[" + memoInfo.getMemoType() + "]", "");
		}
		queueSender.send(memoInfo, "msgSubTypeMemo", amdocsSessionManager.getClientIdentity(sessionId));
	}

	@Override
	public double getTotalDataOutstandingAmount(int banId, Date fromDate) throws ApplicationException {
		return summarizedDataUsageDao.getTotalOutstandingAmount(banId, fromDate);
	}

	@Override
	public double getTotalUnbilledDataAmount(int banId, int billCycleYear, int billCycleMonth, int billCycle) throws ApplicationException {
		return summarizedDataUsageDao.getTotalUnbilledAmount(banId, billCycleYear, billCycleMonth, billCycle);
	}

	// Updated for CDA phase 1B July 2018
	private CreditCheckResultInfo checkCredit(AccountInfo accountInfo, ConsumerNameInfo consumerNameInfo, AddressInfo addressInfo, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, boolean manualCreditCheck, AuditInfo auditInfo, AuditHeader auditHeader, String sessionId) throws ApplicationException {

		// Update the audit attributes - depending the source of the call, these objects may be null
		auditInfo = getPopulatedAuditInfo(auditInfo, sessionId);
		if (auditHeader != null) {
			ClientIdentity clientIdentity = getClientIdentity(sessionId);
			auditHeader = AuditHeaderUtil.appendToAuditHeader(auditHeader, clientIdentity.getApplication(), clientIdentity.getPrincipal());
		}
		LOGGER.debug("AuditHeader: " + auditHeader);
		
		CreditCheckResultInfo creditCheckResultInfo = null;
		try {
			// CDA phase 1B July 2018: check if the account type/subtype is applicable in CDA (via RefPDS code/decode table)
			// If so, call the new WirelessCreditAssessmentProxySvc v2.0 instead of the CreditProfileSvc v3.0 to perform the credit check
			if (!getReferenceDataFacade().isCDASupportedAccountTypeSubType(String.valueOf(accountInfo.getAccountType()) + String.valueOf(accountInfo.getAccountSubType()))) {
				// Call the legacy CreditProfileSvc v3.0
				creditCheckResultInfo = creditProfileServiceDao.checkCredit(accountInfo, selectedBusinessCreditIdentity, auditInfo, auditHeader, isCreditCheckForBusiness(accountInfo));
			} else {
				// Call WirelessCreditAssessmentProxySvc v2.0 - note that we call separate methods for manual vs new account credit checks
				CreditAssessmentInfo creditAssessmentInfo = manualCreditCheck ? wirelessCreditAssessmentProxyServiceDao.performManualCreditCheck(accountInfo, auditInfo, auditHeader)
						: wirelessCreditAssessmentProxyServiceDao.performCreditCheck(accountInfo, auditInfo, auditHeader);

				// Map the CreditAssessmentInfo to the CreditCheckResultInfo for backward compatibility
				creditCheckResultInfo = new CreditCheckResultInfo();
				creditCheckResultInfo.copyCDACreditAssessmentInfo(creditAssessmentInfo);
			}
			if (isCreditCheckForBusiness(accountInfo)) {
				accountLifecycleManager.saveCreditCheckInfoForBusiness(accountInfo, businessCreditIdentityList, selectedBusinessCreditIdentity, creditCheckResultInfo, sessionId);
			} else {
				accountLifecycleManager.saveCreditCheckInfo(accountInfo, creditCheckResultInfo, "I",consumerNameInfo, addressInfo, sessionId);
			}

		} catch (Throwable t) {
			handleException(t);
		}

		return creditCheckResultInfo;
	}

	// Updated for CDA phase 1B
	@Override
	public CreditCheckResultInfo checkCredit(AccountInfo accountInfo, BusinessCreditIdentityInfo[] businessCreditIdentityList, BusinessCreditIdentityInfo selectedBusinessCreditIdentity,
			boolean manualCreditCheck, String sessionId) throws ApplicationException {
		return checkCredit(accountInfo, null, null, businessCreditIdentityList, selectedBusinessCreditIdentity, manualCreditCheck, null, null, sessionId);
	}

	// Updated for CDA phase 1B
	@Override
	public CreditCheckResultInfo checkCredit(AccountInfo accountInfo, ConsumerNameInfo consumerNameInfo, AddressInfo addressInfo, boolean manualCreditCheck, AuditHeader auditHeader,
			String sessionId) throws ApplicationException {
		return checkCredit(accountInfo, consumerNameInfo, addressInfo, null, null, manualCreditCheck, null, auditHeader, sessionId);
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

	private void handleException(Throwable throwable) throws ApplicationException {

		// TODO: PS: verify exception handling

		if (throwable instanceof PolicyException_v1) {
			PolicyFaultInfo pe = ((PolicyException_v1) throwable).getFaultInfo();
			LOGGER.debug("PolicyException occurred : " + pe.getErrorCode() + " : " + pe.getErrorMessage());
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, pe.getErrorCode(), "PolicyException occurred : " + pe.getErrorMessage(), "", throwable);

		} else if (throwable instanceof ServiceException_v1) {
			ServiceFaultInfo se = ((ServiceException_v1) throwable).getFaultInfo();
			LOGGER.error("ServiceException_v1 occurred : " + se.getErrorCode() + " : " + se.getErrorMessage());
			throw new SystemException(SystemCodes.CMB_ALF_EJB, se.getErrorCode(), "ServiceException occurred : " + se.getErrorMessage(), "", throwable);

		} else if (throwable instanceof ServiceException_v3) {
			FaultExceptionDetailsType se = ((ServiceException_v3) throwable).getFaultInfo();
			LOGGER.error("ServiceException_v3 occurred : " + se.getErrorCode() + " : " + se.getErrorMessage());
			throw new SystemException(SystemCodes.CMB_ALF_EJB, se.getErrorCode(), "ServiceException occurred : " + se.getErrorMessage(), "", throwable);

		} else if (throwable instanceof SOAPFaultException) {
			SOAPFaultException e = (SOAPFaultException) throwable;
			LOGGER.error("SOAPFaultException occurred : " + e.getFault().getFaultString());
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, e.getFault().getFaultCode(), "SOAPFaultException occurred : " + e.getFault().getFaultString(), "", e);

		} else if (throwable instanceof RemoteException) {
			RemoteException e = (RemoteException) throwable;
			LOGGER.error("RemoteException occurred : " + e.getMessage());
			throw new SystemException(SystemCodes.CMB_ALF_EJB, "RemoteException occurred : " + e.getMessage(), "", e);

		} else if (throwable instanceof ApplicationException) {
			ApplicationException ae = (ApplicationException) throwable;
			LOGGER.debug("ApplicationException occurred : " + ae.getMessage());
			throw ae;

		} else {
			Exception e = (Exception) throwable;
			LOGGER.error("Exception : " + e.getMessage());
			throw new SystemException(SystemCodes.CMB_ALF_EJB, "Exception : " + e.getMessage(), "", e);
		}
	}

	@Override
	public int createPrepaidAccount(AccountInfo accountInfo, String compassCustomerId, String businessRole, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId)
			throws ApplicationException {

		int banId;
		ClientIdentity ci = this.getClientIdentity(sessionId);

		String serialNumber = ((PrepaidConsumerAccountInfo) accountInfo).getSerialNumber();
		Equipment equipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(serialNumber);

		CreditCardInfo topUpCreditCard = ((PrepaidConsumerAccountInfo) accountInfo).getTopUpCreditCard0();
		if (topUpCreditCard.getToken() != null && topUpCreditCard.getLeadingDisplayDigits() != null && topUpCreditCard.getTrailingDisplayDigits() != null) {
			if (creditCardAuditHeader != null)
				creditCardAuditHeader = AuditHeaderUtil.appendToAuditHeader(creditCardAuditHeader, ci.getApplication(), ci.getPrincipal());
			CreditCardTransactionInfo creditCardTransactionInfo = deriveCreditCardHolderInfo(accountInfo, topUpCreditCard, creditCardAuditHeader);
			creditCardTransactionInfo.getCreditCardHolderInfo().setBusinessRole(businessRole);
			validateCreditCard(creditCardTransactionInfo, sessionId);
		}

		if (((PrepaidConsumerAccountInfo) accountInfo).getActivationType() == ACTIVATION_TYPE_AIRTIME_CARD) {
			String activationCode = ((PrepaidConsumerAccountInfo) accountInfo).getActivationCode();
			// Subscriber ID not created yet, so passing user ID as phone number
			CardInfo airCard = getProductEquipmentHelper().getAirCardByCardNo(activationCode, ci.getPrincipal(), equipment.getSerialNumber(), ci.getPrincipal());
			validateAirtimeCardForPrepaidActivation(ci.getApplication(), airCard);
		}

		if (equipment.isHSPA() && ((PrepaidConsumerAccountInfo) accountInfo).getActivationType() == ACTIVATION_TYPE_NORMAL) {
			accountInformationHelper.validatePayAndTalkSubscriberActivation(ci.getApplication(), ci.getPrincipal(), ((PrepaidConsumerAccountInfo) accountInfo), creditCardAuditHeader);
		} else if (equipment.isCDMA()) {
			accountInformationHelper.validatePayAndTalkSubscriberActivation(ci.getApplication(), ci.getPrincipal(), ((PrepaidConsumerAccountInfo) accountInfo), creditCardAuditHeader);
		}

		try {
			SegmentationInfo segmentation = getReferenceDataFacade().getSegmentation(accountInfo.getBrandId(), String.valueOf(accountInfo.getAccountType()), accountInfo.getAddress0().getProvince0());
			if (segmentation != null) {
				accountInfo.setBanSegment(segmentation.getSegment());
				accountInfo.setBanSubSegment(segmentation.getSubSegment());
			}
		} catch (TelusException e) {
			throw new SystemException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "", e);
		}

		banId = createCustomerAndAccount(accountInfo, compassCustomerId, sessionId);
		accountInfo.setBanId(banId);

		accountInformationHelper.validatePayAndTalkSubscriberActivation(ci.getApplication(), ci.getPrincipal(), ((PrepaidConsumerAccountInfo) accountInfo), creditCardAuditHeader);

		return banId;
	}

	private void validateAirtimeCardForPrepaidActivation(String appId, CardInfo card) throws ApplicationException {

		AirtimeCard airtimeCard = null;
		try {
			airtimeCard = card;
		} catch (ClassCastException cce) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Card is not an airtime card. card type=[" + card.getType() + "]", "", cce);
		}

		if (!airtimeCard.isLive()) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Air Time Card is not in live status", "");
		}

		double amountInCard = card.getAmount();
		boolean amountFound = false;
		if (amountInCard > 0.0) {
			String rechargeType = "";
			List<String> appNamesList = AppConfiguration.getAppNamesForAirtimeCard();

			if (!appNamesList.isEmpty()) {
				boolean nameFound = false;
				Iterator<String> it = appNamesList.iterator();
				while (it.hasNext()) {
					if (appId.equalsIgnoreCase(it.next().trim())) {
						rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT;
						nameFound = true;
						break;
					}
				}
				if (!nameFound) {
					rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT_FS;
				}
			} else {
				rechargeType = ReferenceDataManager.RECHARGE_TYPE_ACTIVATION_CREDIT_FS;
			}

			PrepaidRechargeDenomination[] denominations;
			try {
				denominations = getReferenceDataFacade().getPrepaidRechargeDenominations(rechargeType);
			} catch (TelusException e) {
				throw new ApplicationException(SystemCodes.CMB_ALF_EJB, e.getMessage(), "");
			}

			for (int i = 0; i < denominations.length; i++) {
				PrepaidRechargeDenomination prepaidRechargeDenomination = denominations[i];
				if (amountInCard == prepaidRechargeDenomination.getAmount()) {
					amountFound = true;
					break;
				}
			}
		}

		if (!amountFound) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "The amount [" + amountInCard + "] is invalid in AirTimeCard", "");
		}
	}

	@Override
	public AddressValidationResultInfo validateAddress(AddressInfo addressInfo) throws ApplicationException {

		AddressValidationResultInfo info = enterpriseAddressDao.validateAddress(addressInfo);
		if (info == null) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Empty response from Enterprise Address Validation Service: Possibly a service callout failure", "");
		}

		return info;
	}

	/**
	 * Service 2.0 October 2016 Wireless Major Release
	 * Disable call to CustomerBillingAccountDataMgmt Service since Kafka replaces this update
	 * 
	 * @param billingAccountNumber
	 * @param customerID
	 * @param processType
	 * @param sessionId
	 */
	@Override
	public void asyncInsertBillingAccount(int billingAccountNumber, final String customerID, String processType, String sessionId) {

		if (AppConfiguration.isAsyncInsertBillingAccountEnabled()) {

			new AsyncEnterpriseDataCallback() {

				@Override
				protected void setBillingAccountEnterpriseDataInfo(BillingAccountEnterpriseDataInfo info) {
					info.setCustomerID(customerID);
					info.setMessageType(BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_INSERT_BILLING_ACCOUNT);
				}

			}.execute(billingAccountNumber, processType, sessionId);
		}
	}

	@Override
	public void insertBillingAccount(int billingAccountNumber, final String customerID, String processType, String sessionId) throws ApplicationException {

		new BillingAccountDataMgmtCallback() {

			@Override
			protected void executeDao(AccountInfo account, String userId, String appId) throws ApplicationException {
				billingAccountDataMgmtDao.insertBillingAccount(account, customerID, userId, appId);
			}

		}.execute(billingAccountNumber, processType, sessionId);
	}

	/**
	 * Service 2.0 April 2016 Wireless Major Release 
	 * Disable call to CustomerBillingAccountDataMgmt since Kafka replaces this update
	 */
	@Override
	public void asyncUpdateBillingAccount(final AccountInfo accountInfo, String processType, String sessionId) {

		if (AppConfiguration.isAsyncUpdateBillingAccountEnabled()) {

			new AsyncEnterpriseDataCallback() {

				@Override
				protected void setBillingAccountEnterpriseDataInfo(BillingAccountEnterpriseDataInfo info) {
					info.setAccountInfo(accountInfo);
					info.setMessageType(BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT);
				}

			}.execute(accountInfo.getBanId(), processType, sessionId);
		}
	}

	@Override
	public void asyncUpdateBillingAccount(final int ban, String processType, String sessionId) {

		if (AppConfiguration.isAsyncUpdateBillingAccountEnabled()) {

			new AsyncEnterpriseDataCallback() {

				@Override
				protected void setBillingAccountEnterpriseDataInfo(BillingAccountEnterpriseDataInfo info) {
					info.setMessageType(BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT);
				}

			}.execute(ban, processType, sessionId);
		}
	}

	@Override
	public void updateBillingAccount(final AccountInfo accountInfo, String processType, String sessionId) throws ApplicationException {

		new BillingAccountDataMgmtCallback() {

			@Override
			protected void executeDao(AccountInfo account, String userId, String appId) throws ApplicationException {
				billingAccountDataMgmtDao.updateBillingAccount(account, userId, appId);
			}

			@Override
			protected void process(AccountInfo account, String processType, String sessionId) throws ApplicationException {

				if (accountInfo.isAccountTypeChanged()) {
					boolean oldAccountInfoEligible = EligibilityUtilities.isEnterpriseDataEligible(accountInfo, processType);
					boolean newAccountInfoEligible = EligibilityUtilities.isEnterpriseDataEligible(account, processType);
					if (!oldAccountInfoEligible && newAccountInfoEligible) {
						ClientIdentity clientIdentity = getClientIdentity(sessionId);
						billingAccountDataMgmtDao.insertCustomerWithBillingAccount(account, clientIdentity.getPrincipal(), clientIdentity.getApplication());
					} else if (oldAccountInfoEligible || newAccountInfoEligible) {
						ClientIdentity clientIdentity = getClientIdentity(sessionId);
						executeDao(account, clientIdentity.getPrincipal(), clientIdentity.getApplication());
					}
				} else {
					if (EligibilityUtilities.isEnterpriseDataEligible(accountInfo, processType)) {
						ClientIdentity clientIdentity = getClientIdentity(sessionId);
						executeDao(account, clientIdentity.getPrincipal(), clientIdentity.getApplication());
					}
				}
			}

		}.execute(accountInfo.getBanId(), processType, sessionId);
	}

	@Override
	public void updateBillingAccount(int ban, String processType, String sessionId) throws ApplicationException {

		new BillingAccountDataMgmtCallback() {

			@Override
			protected void executeDao(AccountInfo account, String userId, String appId) throws ApplicationException {
				billingAccountDataMgmtDao.updateBillingAccount(account, userId, appId);
			}

		}.execute(ban, processType, sessionId);
	}

	/**
	 * Service 2.0 October 2016 Wireless Major Release 
	 * Disable call to CustomerBillingAccountDataMgmt Service since Kafka replaces this update
	 * 
	 * @param billingAccountNumber
	 * @param processType
	 * @param sessionId
	 */
	@Override
	public void asyncInsertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId) {

		if (AppConfiguration.isAsyncInsertBillingAccountEnabled()) {

			new AsyncEnterpriseDataCallback() {

				@Override
				protected void setBillingAccountEnterpriseDataInfo(BillingAccountEnterpriseDataInfo info) {
					info.setMessageType(BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_INSERT_CUSTOMER_WITH_BILLING_ACCOUNT);
				}

			}.execute(billingAccountNumber, processType, sessionId);
		}
	}

	@Override
	public void insertCustomerWithBillingAccount(int billingAccountNumber, String processType, String sessionId) throws ApplicationException {

		new BillingAccountDataMgmtCallback() {

			@Override
			protected void executeDao(AccountInfo account, String userId, String appId) throws ApplicationException {
				billingAccountDataMgmtDao.insertCustomerWithBillingAccount(account, userId, appId);
			}

		}.execute(billingAccountNumber, processType, sessionId);
	}

	private abstract class AsyncEnterpriseDataCallback {

		protected abstract void setBillingAccountEnterpriseDataInfo(BillingAccountEnterpriseDataInfo info);

		public void execute(int billingAccountNumber, String processType, String sessionId) {

			if (AppConfiguration.isAsyncPublishEnterpriseDataEnabled()) {
				BillingAccountEnterpriseDataInfo info = new BillingAccountEnterpriseDataInfo();
				try {
					info.setBillingAccountNumber(billingAccountNumber);
					info.setProcessType(processType);
					setBillingAccountEnterpriseDataInfo(info);
					jmsSend(String.valueOf(billingAccountNumber), info, "msgSubTypeBillingAccountEnterpriseDataSync", amdocsSessionManager.getClientIdentity(sessionId));
				} catch (Exception e) {
					LOGGER.error("Error in EnterpriseDataCallBack: " + e + ". BillingAccountEnterpriseDataInfo = " + info);
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

	private abstract class BillingAccountDataMgmtCallback {

		protected abstract void executeDao(AccountInfo account, String userId, String appId) throws ApplicationException;

		public void execute(int billingAccountNumber, String processType, String sessionId) throws ApplicationException {
			AccountInfo account = accountInformationHelper.retrieveAccountByBan(billingAccountNumber, Account.ACCOUNT_LOAD_ALL);
			process(account, processType, sessionId);
		}

		protected void process(AccountInfo account, String processType, String sessionId) throws ApplicationException {
			if (EligibilityUtilities.isEnterpriseDataEligible(account, processType)) {
				ClientIdentity clientIdentity = getClientIdentity(sessionId);
				executeDao(account, clientIdentity.getPrincipal(), clientIdentity.getApplication());
			}
		}
	}

	@Override
	public boolean isEnterpriseManagedData(int brandId, char accountType, char accountSubType, String productType, String processType) throws ApplicationException {

		boolean eligible = false;
		EnterpriseManagementEligibilityCheckCriteria criteria = new EnterpriseManagementEligibilityCheckCriteria();
		criteria.setBrandId(brandId);
		criteria.setAccountCombinedType(String.valueOf(accountType) + String.valueOf(accountSubType));
		criteria.setProductType(productType);
		criteria.setProcessType(processType);

		try {
			Boolean result = EnterpriseManagementEligibilityEvaluationStrategy.getInstance().checkEligibility(criteria);
			if (result != null) {
				eligible = result.booleanValue();
			}
		} catch (Exception e) {
			LOGGER.error("Error in isEnterpriseDataEligible: " + e + ". EnterpriseDataSyncEligibilityCheckCriteria=" + criteria);
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Error in isEnterpriseDataEligible: " + e + ". EnterpriseDataSyncEligibilityCheckCriteria=" + criteria, "", e);
		}

		return eligible;
	}

	@Override
	public boolean validateCommunicationSuiteEligibility(int brandId, char accountType, char accountSubType) throws ApplicationException {
		return CommunicationSuiteEligibilityUtil.validateCommunicationSuiteEligibility(brandId, accountType, accountSubType);
	}

	@Override
	public int createAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException {

		int ban = accountLifecycleManager.createAccount(accountInfo, getAccountLifecycleManagerSessionId(sessionId));
		asyncInsertCustomerWithBillingAccount(ban, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCCOUNT_ACTIVATION, sessionId);
		accountInfo.setBanId(ban);
		identityProfileService.registerConsumerProfile(accountInfo, IdentityProfileRegistrationOrigin.ACCOUNT_CREATION);
		accountEventPublisher.publishAccountCreatedEvent(accountInfo, sessionId);

		return ban;
	}

	@Override
	public void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException {
		accountLifecycleManager.updateAccount(accountInfo, getAccountLifecycleManagerSessionId(sessionId));
		asyncUpdateBillingAccount(accountInfo, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_UPDATE, sessionId);
	}

	@Override
	public void updateBillCycle(int ban, short newBillCycle, String sessionId) throws ApplicationException {
		accountLifecycleManager.updateBillCycle(ban, newBillCycle, getAccountLifecycleManagerSessionId(sessionId));
		asyncUpdateBillingAccount(ban, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_UPDATE, sessionId);
	}

	@Override
	public void updateBillingInformation(int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException {
		accountLifecycleManager.updateBillingInformation(billingAccountNumber, billingPropertyInfo, getAccountLifecycleManagerSessionId(sessionId));
		asyncUpdateBillingAccount(billingAccountNumber, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_UPDATE, sessionId);
	}

	@Override
	public void updatePaymentMethod(int pBan, AccountInfo accountInfo, PaymentMethodInfo pPaymentMethodInfo, boolean notificaitonSuppressionInd, AuditInfo auditInfo, String sessionId)
			throws ApplicationException {
		PaymentMethodInfo paymentMethodInfo = accountLifecycleManager.updatePaymentMethod(pBan, pPaymentMethodInfo, getAccountLifecycleManagerSessionId(sessionId));
		asyncUpdateBillingAccount(pBan, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_UPDATE, sessionId);
		AccountInfo lwAccount = accountInfo != null ? accountInfo : accountInformationHelper.retrieveLwAccountByBan(pBan);
		paymentEventPublisher.publishPaymentMethodChange(lwAccount,paymentMethodInfo,createAuditInfoFromSession(sessionId),getLogicalDate(), notificaitonSuppressionInd);
	}

	@Override
	public void processBillMediumChanges(int banId, char accountType, int brandId, String subscriberId, String activityType, String sessionId) throws ApplicationException {		
		billNotificationBo.processBillMediumChanges(banId, accountType, brandId, subscriberId, BillNotificationActivityType.getBillNotificationActivityType(activityType), sessionId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void cancelAccount(int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, String waiveReason, String userMemoText, String phoneNumber,
		boolean notificationSuppressionInd, AuditInfo auditInfo,String sessionId) throws ApplicationException {

		boolean isPortActivity = false;
		Date logicalDate = getLogicalDate();
		
		// check for the future dated transaction 
		boolean isFutureDatedCancellation = false;
		if (activityDate != null && DateUtil.isAfter(activityDate, logicalDate)) {
			isFutureDatedCancellation = true;
		}
		// Retrieve the accountInfo ( don't need CDA info)
		AccountInfo accountInfo = accountInformationHelper.retrieveHwAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);

		// step 1: Retrieve the portOut activity indicator 
		if (!isFutureDatedCancellation && !StringUtils.isBlank(phoneNumber)) {
			isPortActivity = getSubscriberLifecycleFacade().isPortActivity(phoneNumber);
			LOGGER.debug("phoneNumber: " + phoneNumber + ", isPortActivity: " + isPortActivity);
		}

		// step 2: Cancel the account in KB and publish the cancel event into kafka for CODS
		accountLifecycleManager.cancelAccount(ban, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, isPortActivity, getAccountLifecycleManagerSessionId(sessionId));

		// step 3: Cancel the account in Provisioning/RC if account is BusinessConnect 
		if (accountInfo.isPostpaidBusinessConnect()) {
			activityDate = activityDate != null ? activityDate : getLogicalDate();
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountCancelRequest(ban, accountInfo.getBrandId(), activityDate, activityReasonCode));
		}
		
		// step 4: Update bill medium and bill notification due to account cancel ( only consumer accounts are eligible )
	    processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), null, BillNotificationActivityType.ACCOUNT_CANCEL.name(), sessionId);
	    
		// step 5 : Publish the cancel event into kafka for ECP email
		List<String> phoneNumberList = populateActiveAndSuspendSubList(accountInfo.getProductSubscriberLists());
		accountStatusEventPublisher.publishAccountCancel(accountInfo, phoneNumberList, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, isPortActivity, false,createAuditInfoFromSession(sessionId), getLogicalDate(), false,notificationSuppressionInd);

		// step 6 : Update the account cancel in CODS.
		if (!isFutureDatedCancellation) {
			asyncUpdateBillingAccount(ban, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_CANCELLATION, sessionId);
		}
				
	}

	@Override
	public void suspendAccount(int ban, Date activityDate, String activityReasonCode, String userMemoText, Integer brandId, Boolean isPostpaidBusinessConnect, String sessionId)
			throws ApplicationException {

		AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
		
		if (isPostpaidBusinessConnect == null) {
			isPostpaidBusinessConnect = accountInfo.isPostpaidBusinessConnect();
			brandId = accountInfo.getBrandId();
		}

		accountLifecycleManager.suspendAccount(ban, activityDate, activityReasonCode, userMemoText, getAccountLifecycleManagerSessionId(sessionId));

		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				asyncUpdateBillingAccount(ban, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_SUSPENSION, sessionId);
			}
		} catch (TelusException e) {
			LOGGER.error("suspendAccount error.", e);
		}

		if (isPostpaidBusinessConnect) {
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountSuspendRequest(ban, brandId, activityDate, activityReasonCode));
		}

		processBillMediumChanges(ban, accountInfo.getAccountType(), accountInfo.getBrandId(), null, BillNotificationActivityType.ACCOUNT_SUSPEND.name(), sessionId);
	}

	@Override
	public void suspendAccountForPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, CommunicationSuiteInfo commSuiteInfo, String sessionId) throws ApplicationException {

		// [Naresh.A] : We have added below if block comm suite account suspend method to support primary account suspend, especially when last primary is getting suspended.
		// KB API will not support port out cancel/suspend (portOutInd = 'Y')  account if account has more than one active or suspend subscribers. 
		// However, We should suspend all the watches ( subscribers) if the primary is canceling due to port out and that may leads to account cancel as well.		
		if (commSuiteInfo != null) {
			suspendCommSuiteAccountPortOut(ban, activityReasonCode, activityDate, portOutInd, commSuiteInfo, getAccountLifecycleManagerSessionId(sessionId));
		} else {
			accountLifecycleManager.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, getAccountLifecycleManagerSessionId(sessionId));
		}

		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				asyncUpdateBillingAccount(ban, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCOUNT_SUSPENSION, sessionId);
			}
		} catch (TelusException e) {
			LOGGER.error("suspendAccountForPortOut error.", e);
		}
	}

	private void suspendCommSuiteAccountPortOut(int ban, String activityReasonCode, Date activityDate, String portOutInd, CommunicationSuiteInfo commSuiteInfo, String sessionId)
			throws ApplicationException {
		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Entering the suspendCommSuiteAccountPortOut for the ban [=  " + ban + " ] , " + "communication suite info " + commSuiteInfo);
		}

		if (commSuiteInfo != null && commSuiteInfo.isRetrievedAsPrimary() == true && commSuiteInfo.getActiveCompanionCount() > 0) {
			// step 1: suspend the primary subscriber as a port out with port out indicator set as "Y"
			getSubscriberLifecycleFacade().suspendPortedInSubscriber(ban, commSuiteInfo.getPrimaryPhoneNumber(), activityReasonCode, activityDate, portOutInd,
					getSubscriberLifecycleFacadeSessionId(sessionId));
			// set the port out indicator as "N"  to suspend the account as regular suspend .
			portOutInd = "N";
		}

		try {
			// step 2: cancel the account with port type "N" so that companions under account also get canceled .
			accountLifecycleManager.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, getAccountLifecycleManagerSessionId(sessionId));
		} catch (ApplicationException ae) {
			// below if block will execute if we cancel the last primary without any companion subscribers which is why do the silent failure only if the primary has any companions.
			// Otherwise throw an error if primary has no companions to skip the rest of the port cancel process.
			if (commSuiteInfo.isRetrievedAsPrimary() == true && commSuiteInfo.getActiveCompanionCount() > 0) {
				LOGGER.error("Error occurred when suspending the comm suite account and its companions due to last primary number port out ,ban [=  " + ban + " ] , communication suite info " + commSuiteInfo, ae);
			} else {
				LOGGER.error("Error occurred when suspending the comm suite account due to last primary number port out and has no companions ,ban [=  " + ban + " ] , communication suite info " + commSuiteInfo, ae);
				throw ae;
			}
		}
		LOGGER.debug("Exit the suspendCommSuiteAccountPortOut for the ban [=  " + ban + " ] , " + "communication suite info " + commSuiteInfo);

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void cancelSubscribers(int ban, Date activityDate, String activityReasonCode, char depositReturnMethod, String[] subscriberIdArray, String[] waiveReasonArray, String userMemoText,
			boolean isIDEN, boolean notificationSuppressionInd, ServiceRequestHeader srpdsHeader, String sessionId)
					throws ApplicationException {
		
		// KB phoneNumberList for cancellation
		List<String> phoneNumbers = new ArrayList<String>();
		
		// Business Connect provisioning subscriber list 
		List<String> provisioningSubscriptionIdList = null;
		List<String> provisioningSubscriberIdList = null;
		
		// comm suite primary and companion subscriber map
		Map<String, CommunicationSuiteInfo> primaryCommunicationSuiteInfoMap = new HashMap<String, CommunicationSuiteInfo>();
		Map<String, CommunicationSuiteInfo> companionCommunicationSuiteInfoMap = new HashMap<String, CommunicationSuiteInfo>();
		
		Map<String, String> waiveReasonMap = new HashMap<String, String>();

		//validate waiveReasonArray
		if (subscriberIdArray == null || subscriberIdArray.length == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "SubscriberIds should not be null or empty.", "");
		} else if (waiveReasonArray == null || waiveReasonArray.length == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, "Waive Reasons should not be null or empty.", "");
		} else if (subscriberIdArray.length != waiveReasonArray.length) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB,"Number of subscriberIds does not equal number of waiveReasons. [" + subscriberIdArray.length + "] vs [" + waiveReasonArray.length + "]", "");
		}

		for (int i = 0; i < subscriberIdArray.length; i++) {
			waiveReasonMap.put(subscriberIdArray[i], waiveReasonArray[i]);
		}

		// step 1: retrieve the light weight account
		AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
		boolean isPostpaidBusinessConnect = accountInfo.isPostpaidBusinessConnect();
		
		// step 2: validateCommunicationSuiteEligibility
		boolean isEligibleForCommSuite = validateCommunicationSuiteEligibility(accountInfo.getBrandId(), accountInfo.getAccountType(), accountInfo.getAccountSubType());

		// step 3: populate the business connect subscriberId and subscriptionId List
		if (isPostpaidBusinessConnect) {
			ProductSubscriberListInfo[] productSubscriberList = accountInformationHelper.retrieveProductSubscriberLists(ban);
			subscriberIdArray = ConstructGroupSubscriberIdListBeforeCancelOrSuspendSeats(productSubscriberList, subscriberIdArray, waiveReasonMap, true);
			provisioningSubscriptionIdList = filterSubscriptionIdListForProvisioningOrder(productSubscriberList, subscriberIdArray, true);
			provisioningSubscriberIdList = filtersubscriberIdListForProvisioningOrder(productSubscriberList, provisioningSubscriptionIdList, true);
		}
		
		// step 4 : set the phoneNumbers after BC logic
		if (isIDEN) {
			Map subIdPhoneNumberMap = accountInformationHelper.retrievePhoneNumbersForBAN(ban);
			for (String subId : subscriberIdArray) {
				phoneNumbers.add((String) subIdPhoneNumberMap.get(subId));
			}
		} else {
			phoneNumbers.addAll(Arrays.asList(subscriberIdArray));
		}

		// step 5 : perform comm suite pre-task,  remove communicationSuite for companion sub cancellation
		if (isEligibleForCommSuite == true) {
			performMultiCommSuiteCancellationPretask(ban, subscriberIdArray, primaryCommunicationSuiteInfoMap, companionCommunicationSuiteInfoMap);
		}
		// step 6 : cancel the subscribers in KB 
		accountLifecycleManager.cancelSubscribers(accountInfo,subscriberIdArray, waiveReasonArray, activityDate,activityReasonCode, 
				depositReturnMethod, userMemoText,getLogicalDate(), false, notificationSuppressionInd,getAccountLifecycleManagerSessionId(sessionId));

		//step 7 : perform comm suite post-task, cancel the companion subscribers
		if (isEligibleForCommSuite == true) {
			performMultiCommSuiteCancellationPostTask(ban, primaryCommunicationSuiteInfoMap, phoneNumbers, waiveReasonMap, activityDate, activityReasonCode, depositReturnMethod, userMemoText,
					notificationSuppressionInd, srpdsHeader, sessionId);
		}
		//step 8 : update CODS for subscriber cancellation 
		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				String slfcSessionId = getSubscriberLifecycleFacadeSessionId(sessionId);
				for (String subId : subscriberIdArray) {
					getSubscriberLifecycleFacade().asyncUpdateProductInstance(ban, subId, null, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_CANCELLATION, slfcSessionId);
				}
			}
		} catch (TelusException e) {
			LOGGER.error("cancelSubscribers error.", e);
		}
		
		//step 9: send the provisioning cancel order 
		if (isPostpaidBusinessConnect && provisioningSubscriptionIdList != null && provisioningSubscriptionIdList.size() > 0) {
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(
					WirelessProvisioningServiceRequestFactory.createMultiSeatCancelRequest(ban, accountInfo.getBrandId(), provisioningSubscriptionIdList, provisioningSubscriberIdList, activityDate));
		}
		
		//step 10: process bill medium changes
		String lastSubscriberId = subscriberIdArray[subscriberIdArray.length - 1];
		processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), lastSubscriberId, BillNotificationActivityType.SUBSCRIBER_CANCEL.name(), sessionId);
		
	}
	
	private void performMultiCommSuiteCancellationPretask(int ban, String[] subscriberIdArray, Map<String, CommunicationSuiteInfo> primaryCommunicationSuiteInfoMap,
			Map<String, CommunicationSuiteInfo> companionCommunicationSuiteInfoMap) throws ApplicationException {
		List<String> companionsToCancelList = new ArrayList<String>();
		for (String subId : subscriberIdArray) {
			CommunicationSuiteInfo communicationSuite = getSubscriberLifecycleHelper().retrieveCommunicationSuite(ban, subId, CommunicationSuiteInfo.CHECK_LEVEL_ALL);
			if (communicationSuite != null) {
				if (communicationSuite.isRetrievedAsPrimary()) {
					primaryCommunicationSuiteInfoMap.put(subId, communicationSuite);
				} else {
					companionCommunicationSuiteInfoMap.put(subId, communicationSuite);
				}
			}
		}

		if (companionCommunicationSuiteInfoMap != null) {
			for (String companionSubId : companionCommunicationSuiteInfoMap.keySet()) {
				CommunicationSuiteInfo companionSuite = companionCommunicationSuiteInfoMap.get(companionSubId);
				CommunicationSuiteInfo primarySuiteInfo = primaryCommunicationSuiteInfoMap.get(companionSuite.getPrimaryPhoneNumber());
				if (primarySuiteInfo == null) { // companion cancellation without primary on the cancellation list
					companionsToCancelList.add(companionSubId);
				}
			}
		}

		if (companionsToCancelList != null && companionsToCancelList.isEmpty() == false) {
			for (String companionSubId : companionsToCancelList) {
				try {
					CommunicationSuiteInfo companionSuite = companionCommunicationSuiteInfoMap.get(companionSubId);
					if (companionSuite != null) {
						getSubscriberLifecycleFacade().removeFromCommunicationSuite(ban, companionSubId, companionCommunicationSuiteInfoMap.get(companionSubId), true);
					} else {
						LOGGER.debug("Unexpected condition. companionSuite is null for [" + companionSubId + "]");
					}
				} catch (Throwable t) {
					LOGGER.error("Error breaking communication suite for [" + companionSubId + "] ", t);
				}
			}
		}
	}

	private void performMultiCommSuiteCancellationPostTask(int ban, Map<String, CommunicationSuiteInfo> primaryCommunicationSuiteInfoMap, List<String> originalCancellationList,
			Map<String, String> waiveReasonMap, Date activityDate, String cancelReasonCode, char depositReturnMethod, String userMemoText, boolean notificationSuppressionInd,
			ServiceRequestHeader srpdsHeader, String sessionId) throws ApplicationException {
		if (primaryCommunicationSuiteInfoMap != null && primaryCommunicationSuiteInfoMap.isEmpty() == false) {
			for (String primarySubId : primaryCommunicationSuiteInfoMap.keySet()) {
				CommunicationSuiteInfo primarySuite = primaryCommunicationSuiteInfoMap.get(primarySubId);
				if (primarySuite != null) {
					for (String companionSubId : primarySuite.getActiveAndSuspendedCompanionPhoneNumberList()) {
						if (originalCancellationList.contains(companionSubId)) { // already cancelled in original request
							primarySuite.getLwCompanionSubInfo(companionSubId).setStatus(Subscriber.STATUS_CANCELED); //update the sub in comm suite as cancelled in memory
						}
					}

					if (primarySuite.getActiveAndSuspendedCompanionCount() > 0) {
						getSubscriberLifecycleFacade().cancelCommunicationSuiteCompanionSubs(ban, primarySubId, primaryCommunicationSuiteInfoMap.get(primarySubId), activityDate, cancelReasonCode,
								depositReturnMethod, waiveReasonMap.get(primarySubId), userMemoText, notificationSuppressionInd, srpdsHeader, getSubscriberLifecycleFacadeSessionId(sessionId));
					} else {
						LOGGER.debug("No additional companion needs to be cancelled for [" + primarySuite.getPrimaryPhoneNumber() + "]");
					}
				}
			}
		}
	}

	@Override
	public void suspendSubscribers(int ban, Date activityDate, String activityReasonCode, String[] subscriberIds, String userMemoText,String sessionId) throws ApplicationException {

		List<String> provisioningsubscriptionIdList = null;
		List<String> provisioningSubscriberIdList = null;
		
		AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
		
		
		if (accountInfo.isPostpaidBusinessConnect()) {
			ProductSubscriberListInfo[] productSubscriberList = accountInformationHelper.retrieveProductSubscriberLists(ban);
			subscriberIds = ConstructGroupSubscriberIdListBeforeCancelOrSuspendSeats(productSubscriberList, subscriberIds, null, false);
			provisioningsubscriptionIdList = filterSubscriptionIdListForProvisioningOrder(productSubscriberList, subscriberIds, false);
			provisioningSubscriberIdList = filtersubscriberIdListForProvisioningOrder(productSubscriberList, provisioningsubscriptionIdList, false);
		}

		accountLifecycleManager.suspendSubscribers(ban, activityDate, activityReasonCode, subscriberIds, userMemoText, getAccountLifecycleManagerSessionId(sessionId));

		try {
			if (activityDate == null || !DateUtil.isAfter(activityDate, getReferenceDataFacade().getLogicalDate())) {
				String slfcSessionId = getSubscriberLifecycleFacadeSessionId(sessionId);
				for (String subId : subscriberIds) {
					getSubscriberLifecycleFacade().asyncUpdateProductInstance(ban, subId, null, ProductEnterpriseDataInfo.PROCESS_TYPE_SUBSCRIBER_SUSPENSION, slfcSessionId);
				}
			}
		} catch (TelusException e) {
			LOGGER.error("cancelSubscribers error.", e);
		}

		if (accountInfo.isPostpaidBusinessConnect() && provisioningsubscriptionIdList != null && provisioningsubscriptionIdList.size() > 0) {
			if (activityDate == null) {
				activityDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createMultiSeatSuspendRequest(ban, accountInfo.getBrandId(), provisioningsubscriptionIdList, provisioningSubscriberIdList,
					activityDate, activityReasonCode));
		}
		
		
		if (subscriberIds.length > 0) {
			String lastSubscriberId = subscriberIds[subscriberIds.length - 1];
			processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), lastSubscriberId, BillNotificationActivityType.SUBSCRIBER_SUSPEND.name(), sessionId);
		}
	}

	@Override
	public void restoreSuspendedSubscribers(int ban, Date restoreDate, String restoreReasonCode, String[] subscriberIds, String restoreComment, Integer brandId, Boolean isPostpaidBusinessConnect,
			Character accountStatus, String sessionId) throws ApplicationException {

		List<String> provisioningSubscriptionIdList = null;
		List<String> provisioningSubscriberIdList = null;
		AccountInfo accountInfo = null;

		if (isPostpaidBusinessConnect == null || accountStatus == null || brandId == null) {
			accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
			isPostpaidBusinessConnect = accountInfo.isPostpaidBusinessConnect();
			brandId = accountInfo.getBrandId();
			accountStatus = accountInfo.getStatus();
		}

		if (isPostpaidBusinessConnect) {
			ProductSubscriberListInfo[] productSubscriberList = accountInformationHelper.retrieveProductSubscriberLists(ban);
			validateBusinessConnectSeatsBeforeRestore(productSubscriberList, subscriberIds);
			provisioningSubscriptionIdList = filterSubscriptionIdListForProvisioningOrder(productSubscriberList, subscriberIds, true);
			provisioningSubscriberIdList = filtersubscriberIdListForProvisioningOrder(productSubscriberList, provisioningSubscriptionIdList, true);
		}

		accountLifecycleManager.restoreSuspendedSubscribers(ban, restoreDate, restoreReasonCode, subscriberIds, restoreComment, sessionId);

		if (isPostpaidBusinessConnect && provisioningSubscriptionIdList != null && provisioningSubscriptionIdList.size() > 0) {
			if (restoreDate == null) {
				restoreDate = getLogicalDate();
			}
			if (AccountInfo.STATUS_SUSPENDED == accountStatus) {
				asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountRestoreRequest(ban, brandId, restoreDate, restoreReasonCode));
			} else {
				asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createMultiSeatResumeRequest(ban, brandId, provisioningSubscriptionIdList, provisioningSubscriberIdList,
						restoreDate, restoreReasonCode));
			}
		}
	}

	@Override
	public void restoreSuspendedAccount(@BANValue int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, Integer brandId,
			Boolean isPostpaidBusinessConnect, String sessionId) throws ApplicationException {

		if (isPostpaidBusinessConnect == null) {
			AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
			isPostpaidBusinessConnect = accountInfo.isPostpaidBusinessConnect();
			brandId = accountInfo.getBrandId();
		}

		accountLifecycleManager.restoreSuspendedAccount(ban, restoreDate, restoreReasonCode, restoreComment, collectionSuspensionsOnly, sessionId);

		if (isPostpaidBusinessConnect) {
			if (restoreDate == null) {
				restoreDate = getLogicalDate();
			}
			asyncSubmitProvisioningOrder(WirelessProvisioningServiceRequestFactory.createAccountRestoreRequest(ban, brandId, restoreDate, restoreReasonCode));
		}
	}

	private boolean isStarterSeatActiveOnGroup(SubscriberIdentifier[] activeSubscriberIdentifiers, String seatGroup) {

		for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
			if (activeSubscriberIdentifier.getSeatGroup().equals(seatGroup) && SeatType.SEAT_TYPE_STARTER.equals(activeSubscriberIdentifier.getSeatType())) {
				return true;
			}
		}

		return false;
	}

	private boolean isStarterSeatPresentInRestoreSubscriberList(SubscriberIdentifier[] suspendSubscriberIdentifiers, String seatGroup, String[] subscriberIds) {

		for (SubscriberIdentifier suspendSubscriberIdentifier : suspendSubscriberIdentifiers) {
			if (suspendSubscriberIdentifier.getSeatGroup().equals(seatGroup)) {
				for (String subscriberId : subscriberIds) {
					if (suspendSubscriberIdentifier.getSubscriberId().equals(subscriberId)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private void validateBusinessConnectSeatsBeforeRestore(ProductSubscriberList[] productSubscriberList, String[] subscriberIds) throws ApplicationException {

		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriber.getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriber.getSuspendedSubscriberIdentifiers();
			for (SubscriberIdentifier suspendSubscriberIdentifier : suspendedsubscriberIdentifiers) {
				for (String subscriberId : subscriberIds) {
					if (subscriberId.equals(suspendSubscriberIdentifier.getSubscriberId()) && SeatType.SEAT_TYPE_STARTER.equals(suspendSubscriberIdentifier.getSeatType()) == false) {
						if (isStarterSeatActiveOnGroup(activeSubscriberIdentifiers, suspendSubscriberIdentifier.getSeatGroup()) == false
								&& isStarterSeatPresentInRestoreSubscriberList(suspendedsubscriberIdentifiers, suspendSubscriberIdentifier.getSeatGroup(), subscriberIds) == false)
							throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.VALIDATAION_FAILED, "StartSeat not active on Group : " + suspendSubscriberIdentifier.getSeatGroup()
									+ ", we can't restore the non-starter seat : " + subscriberId + " without starter seat active ( or ) without resuming the starter seat.", "");
					}
				}
			}
		}
	}

	@Override
	public int createCustomerAndAccount(AccountInfo pAccountInfo, String customerId, String sessionId) throws ApplicationException {

		int ban = 0;
		if (customerId == null || "0".equals(customerId)) {
			ban = createAccount(pAccountInfo, sessionId);
		} else {
			String almSessionId = getAccountLifecycleManagerSessionId(sessionId);
			ban = accountLifecycleManager.createAccount(pAccountInfo, almSessionId);
			asyncInsertBillingAccount(ban, customerId, BillingAccountEnterpriseDataInfo.PROCESS_TYPE_ACCCOUNT_ACTIVATION, sessionId);
		}

		return ban;
	}

	private String getAccountLifecycleManagerSessionId(String sessionId) throws ApplicationException {
		ClientIdentity clientIdentity = getClientIdentity(sessionId);
		String almSessionId = accountLifecycleManager.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
		return almSessionId;
	}

	private String getSubscriberLifecycleFacadeSessionId(String sessionId) throws ApplicationException {
		ClientIdentity clientIdentity = getClientIdentity(sessionId);
		String slcfSessionId = getSubscriberLifecycleFacade().openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
		return slcfSessionId;
	}

	@Override
	public void asyncCreateFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException {
		queueSender.send(followUpInfo, "msgSubTypeFollowUp", amdocsSessionManager.getClientIdentity(sessionId));
	}

	private List<Object> messageList;

	@Override
	public void asyncUpdateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText, String sessionId) throws ApplicationException {

		messageList = new ArrayList<Object>();
		messageList.add(ban);
		messageList.add(newCreditClass);
		messageList.add(newCreditLimit);
		messageList.add(memoText);

		queueSender.send(messageList, "msgSubTypeCreditProfile", amdocsSessionManager.getClientIdentity(sessionId));
	}

	@Override
	public void asyncUpdateCreditCheckResult(int pBan, String pCreditClass, CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo, String pDepositChangedReasonCode, String pDepositChangeText,
			String sessionId) throws ApplicationException {

		messageList = new ArrayList<Object>();
		messageList.add(pBan);
		messageList.add(pCreditClass);
		messageList.add(pCreditCheckResultDepositInfo);
		messageList.add(pDepositChangedReasonCode);
		messageList.add(pDepositChangeText);

		queueSender.send(messageList, "msgSubTypeCreditCheckResult", amdocsSessionManager.getClientIdentity(sessionId));
	}

	@Override
	public void asyncSaveCreditCheckInfo(int ban, PersonalCreditInfo personalCreditInfo, CreditCheckResultInfo creditCheckResultInfo, String creditParamType, String sessionId)
			throws ApplicationException {

		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setBanId(ban);
		accountInfo.getCreditCheckResult0().setLastCreditCheckPersonalnformation(personalCreditInfo);

		messageList = new ArrayList<Object>();
		messageList.add(accountInfo);
		messageList.add(creditCheckResultInfo);
		messageList.add(creditParamType);

		queueSender.send(messageList, "msgSubTypeCreditCheckInfo", amdocsSessionManager.getClientIdentity(sessionId));
	}

	@Override
	public void asyncSaveCreditCheckInfoForBusiness(int ban, BusinessCreditInfo businessCreditInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness,
			CreditCheckResultInfo creditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException {

		AccountInfo accountInfo = new AccountInfo();
		accountInfo.setBanId(ban);
		accountInfo.getCreditCheckResult0().setLastCreditCheckIncorporationNumber(businessCreditInfo.getIncorporationNumber());
		accountInfo.getCreditCheckResult0().setLastCreditCheckIncorporationDate(businessCreditInfo.getIncorporationDate());

		messageList = new ArrayList<Object>();
		messageList.add(accountInfo);
		messageList.add(listOfBusinesses);
		messageList.add(selectedBusiness);
		messageList.add(creditCheckResultInfo);
		messageList.add(pCreditParamType);

		queueSender.send(messageList, "msgSubTypeBusinessCreditCheckInfo", amdocsSessionManager.getClientIdentity(sessionId));
	}

	@Override
	public TestPointResultInfo testCconDataSource() {
		return testPointDao.testCconDataSource();
	}

	@Override
	public TestPointResultInfo testEnterpriseAddressValidationService() {
		return enterpriseAddressDao.test();
	}

	@Override
	public TestPointResultInfo testConsumerBillingAccountDataManagementService() {
		return billingAccountDataMgmtDao.test();
	}

	@Override
	public TestPointResultInfo testCreditProfileService() {
		return creditProfileServiceDao.test();
	}

	@Override
	public TestPointResultInfo testCardPaymentService() {
		return cardPaymentServiceDao.test();
	}

	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public TestPointResultInfo testSummaryDataServicesUsageService() {
		return summarizedDataUsageDao.test();
	}

	private Date getLogicalDate() throws ApplicationException {

		try {
			return getReferenceDataFacade().getLogicalDate();
		} catch (TelusException e) {
			LOGGER.error("cancelSubscribers error.", e);
		}

		return null;
	}


	private AuditInfo getPopulatedAuditInfo(AuditInfo auditInfo, String sessionId) throws ApplicationException {

		// If the auditInfo doesn't exist, create it based on the Amdocs session
		if (auditInfo == null) {
			return createAuditInfoFromSession(sessionId);
		}
		// If there's no originating application, create it based on the Amdocs session
		if (auditInfo.getOriginatorAppId() == null) {
			auditInfo.setinternalPopulatedAppId(true);
			auditInfo.setOriginatorAppId(getClientIdentity(sessionId).getApplication());
		}

		return auditInfo;
	}

	@Override
	public void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException {
		
		accountLifecycleManager.updateFollowUp(followUpUpdateInfo, sessionId);
		
			if(isFollowUpApprovalForCredit(followUpUpdateInfo)){
				AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(followUpUpdateInfo.getBan(),Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
				List creditList = accountInformationHelper.retrieveApprovedCreditByFollowUpId(followUpUpdateInfo.getBan(),followUpUpdateInfo.getFollowUpId());
				CreditInfo creditInfo = (CreditInfo) creditList.get(0);
				creditInfo.setNumberOfRecurring(creditList.size());
				creditEventPublisher.publishFollowUpApprovalCredit(accountInfo, creditInfo, followUpUpdateInfo, createAuditInfoFromSession(sessionId), getLogicalDate(), false);
			}else{
				LOGGER.debug("followUp is not for manual credit Or charge Adj approval , ban ["+followUpUpdateInfo.getBan()+"] , followUp Id [ "+ followUpUpdateInfo.getFollowUpId() +" ]");
			}
	}

	private boolean isFollowUpApprovalForCredit(FollowUpUpdateInfo fuui) throws ApplicationException {
		
		if(!fuui.getIsApproved()){
			LOGGER.debug("Follow up is not for approval  ,  ban ["+fuui.getBan()+"] , followUp Id [ "+ fuui.getFollowUpId() +" ]");
			return false;
		}
		
		if (fuui.getFollowUpType() == null || fuui.getFollowUpText() == null) {
			LOGGER.debug("retrieving followUp Info ban ["+fuui.getBan()+"] , followUp Id [ "+ fuui.getFollowUpId() +" ]");
			FollowUpInfo fui = accountInformationHelper.retrieveFollowUpInfoByBanFollowUpID(fuui.getBan(), fuui.getFollowUpId());
			fuui.setFollowUpType(fui.getFollowUpType());
			fuui.setFollowUpText(fui.getText());
		}
				
		// adjustment follow up criteria: in FOLLOW_UP table FU_TYPE=ADJT and FU_TEXT leading string is either TYPE=ADB or TYPE=ADC
		if (FollowUpUpdateInfo.FOLLOWUP_TYPE_ADJUSTMENT.equals(fuui.getFollowUpType()) && ( fuui.isFollowUpApprovalForManualCredit() || fuui.isFollowUpApprovalForChargeAdj())) {
			LOGGER.debug("followUp credit approved for ban ["+fuui.getBan()+"] , followUp Id [ "+ fuui.getFollowUpId() +" ]");
			return true;
		}
		
		return false;
	}
	
	@Override
	public double adjustCharge(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo,
			String sessionId) throws ApplicationException {

		double adjustmentId = 0;

		if (overrideUserLimit) {
			adjustmentId = accountLifecycleManager.adjustChargeWithOverride(chargeInfo, adjustmentAmount, adjustmentReasonCode, memoText, sessionId);
		} else {
			adjustmentId = accountLifecycleManager.adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, memoText, sessionId);
		}
		
		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(chargeInfo.getBan(),Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		CreditInfo creditInfo = accountInformationHelper.retrieveCreditById(chargeInfo.getBan(), (int) adjustmentId);
		creditEventPublisher.publishCreditForChargeAdj(accountInfo, creditInfo,chargeInfo, createAuditInfoFromSession(sessionId),getLogicalDate(), suppressionInd);

		return adjustmentId;
	}

	@Override
	public void applyCreditToAccount(CreditInfo creditInfo, boolean overrideUserLimit, boolean suppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		boolean adjustmentCreated = false;
		
		if (overrideUserLimit) {
			adjustmentCreated = accountLifecycleManager.applyCreditToAccountWithOverride(creditInfo, sessionId);
		} else {
			adjustmentCreated = accountLifecycleManager.applyCreditToAccount(creditInfo, sessionId);
		}
		
		if (adjustmentCreated) {
				AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(creditInfo.getBan(),Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
				creditEventPublisher.publishCreateCredit(accountInfo,creditInfo, createAuditInfoFromSession(sessionId), getLogicalDate(),suppressionInd);
		}
	}

	private String[] ConstructGroupSubscriberIdListBeforeCancelOrSuspendSeats(ProductSubscriberList[] productSubscriberList, String[] subscriberIds, Map<String, String> waiveReasonMap,
			boolean isValidateSuspendedSeats) {

		// taken the map to avoid duplicate subscribers when starter seat and multiple seats under starter seats passed to suspend
		HashSet<String> subscriberIdSet = new HashSet<String>();
		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriber.getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriber.getSuspendedSubscriberIdentifiers();

			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
				for (String subscriberId : subscriberIds) {
					if (subscriberId.equalsIgnoreCase(activeSubscriberIdentifier.getSubscriberId())) {
						if (activeSubscriberIdentifier.getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
							List<String> subIdListInGroup = filterSubscriberIdListBySeatGroup(productSubscriberList, activeSubscriberIdentifier.getSeatGroup(), false);
							subscriberIdSet.addAll(subIdListInGroup);
							if (waiveReasonMap != null) {
								for (String groupSubId : subIdListInGroup) {
									waiveReasonMap.put(groupSubId, waiveReasonMap.get(subscriberId));
								}
							}
						} else {
							subscriberIdSet.add(subscriberId);
						}
					}
				}
			}

			if (isValidateSuspendedSeats) {
				for (SubscriberIdentifier suspendedsubscriberIdentifier : suspendedsubscriberIdentifiers) {
					for (String subscriberId : subscriberIds) {
						if (subscriberId.equalsIgnoreCase(suspendedsubscriberIdentifier.getSubscriberId())) {
							if (suspendedsubscriberIdentifier.getSeatType().equalsIgnoreCase(SeatType.SEAT_TYPE_STARTER)) {
								List<String> subIdListInGroup = filterSubscriberIdListBySeatGroup(productSubscriberList, suspendedsubscriberIdentifier.getSeatGroup(), true);
								subscriberIdSet.addAll(subIdListInGroup);
								if (waiveReasonMap != null) {
									for (String groupSubId : subIdListInGroup) {
										waiveReasonMap.put(groupSubId, waiveReasonMap.get(subscriberId));
									}
								}
							} else
								subscriberIdSet.add(subscriberId);
						}
					}
				}
			}

		}

		String[] subscriberIdArray = subscriberIdSet.toArray(new String[subscriberIdSet.size()]);

		return subscriberIdArray;
	}

	private List<String> filterSubscriberIdListBySeatGroup(ProductSubscriberList[] productSubscriberList, String seatGroup, boolean isFilterSuspenedSeats) {

		List<String> subscriberIds = new ArrayList<String>();
		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriber.getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriber.getSuspendedSubscriberIdentifiers();

			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
				if (activeSubscriberIdentifier.getSeatGroup().equalsIgnoreCase(seatGroup)) {
					subscriberIds.add(activeSubscriberIdentifier.getSubscriberId());
				}
			}
			if (isFilterSuspenedSeats) {
				for (SubscriberIdentifier suspendedsubscriberIdentifier : suspendedsubscriberIdentifiers) {
					if (suspendedsubscriberIdentifier.getSeatGroup().equalsIgnoreCase(seatGroup)) {
						subscriberIds.add(suspendedsubscriberIdentifier.getSubscriberId());
					}
				}
			}
		}

		return subscriberIds;
	}

	private List<String> filterSubscriptionIdListForProvisioningOrder(ProductSubscriberList[] productSubscriberList, String[] subscriberIds, boolean isFilterSuspenedSeats) {

		List<String> subscriptionIdList = new ArrayList<String>();
		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriber.getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriber.getSuspendedSubscriberIdentifiers();

			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
				for (String subscriberId : subscriberIds) {
					if (subscriberId.equals(activeSubscriberIdentifier.getSubscriberId()) && SeatType.SEAT_TYPE_MOBILE.equals(activeSubscriberIdentifier.getSeatType()) == false)
						subscriptionIdList.add(String.valueOf(activeSubscriberIdentifier.getSubscriptionId()));
				}

			}
			if (isFilterSuspenedSeats == true) {
				for (SubscriberIdentifier suspendedsubscriberIdentifier : suspendedsubscriberIdentifiers) {
					for (String subscriberId : subscriberIds) {
						if (subscriberId.equals(suspendedsubscriberIdentifier.getSubscriberId()) && SeatType.SEAT_TYPE_MOBILE.equals(suspendedsubscriberIdentifier.getSeatType()) == false)
							subscriptionIdList.add(String.valueOf(suspendedsubscriberIdentifier.getSubscriptionId()));
					}
				}
			}
		}

		return subscriptionIdList;
	}

	private List<String> filtersubscriberIdListForProvisioningOrder(ProductSubscriberList[] productSubscriberList, List<String> subscriptionIdList, boolean isFilterSuspenedSeats) {

		List<String> subscriberIdList = new ArrayList<String>();
		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifiers = productSubscriber.getActiveSubscriberIdentifiers();
			SubscriberIdentifier[] suspendedsubscriberIdentifiers = productSubscriber.getSuspendedSubscriberIdentifiers();

			for (SubscriberIdentifier activeSubscriberIdentifier : activeSubscriberIdentifiers) {
				for (String subscriptionId : subscriptionIdList) {
					if (subscriptionId.equals(String.valueOf(activeSubscriberIdentifier.getSubscriptionId())) && SeatType.SEAT_TYPE_MOBILE.equals(activeSubscriberIdentifier.getSeatType()) == false)
						subscriberIdList.add(String.valueOf(activeSubscriberIdentifier.getSubscriberId()));
				}

			}
			if (isFilterSuspenedSeats) {
				for (SubscriberIdentifier suspendedsubscriberIdentifier : suspendedsubscriberIdentifiers) {
					for (String subscriptionId : subscriptionIdList) {
						if (subscriptionId.equals(String.valueOf(suspendedsubscriberIdentifier.getSubscriptionId()))
								&& SeatType.SEAT_TYPE_MOBILE.equals(suspendedsubscriberIdentifier.getSeatType()) == false)
							subscriberIdList.add(String.valueOf(suspendedsubscriberIdentifier.getSubscriberId()));
					}
				}
			}
		}

		return subscriberIdList;
	}

	@Override
	public void applyPaymentToAccount(AccountInfo accountInfo, PaymentInfo paymentInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		accountLifecycleManager.applyPaymentToAccount(paymentInfo, sessionId);
		// Naresh : refresh the accountInfo to get updated payment due buckets from KB tables.
		accountInfo = accountInformationHelper.retrieveAccountByBan(paymentInfo.getBan(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		paymentEventPublisher.publishMakePayment(accountInfo, paymentInfo, createAuditInfoFromSession(sessionId), getLogicalDate(), notificationSuppressionInd);
	}

	private ProcessPaymentBo getProcessPaymentBo(AccountInfo accountInfo, boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		// Create and initialize the context
		ProcessPaymentContext context = new ProcessPaymentContext(accountInfo, ejbController, getClientIdentity(sessionId), auditInfo);
		context.initialize();
		context.setNotificationSuppressionInd(notificationSuppressionInd);
		// Create and return the business object
		return new ProcessPaymentBo(context);
	}

	@Override
	public String processPayment(double paymentAmount, AccountInfo accountInfo, @Sensitive CreditCardTransactionInfo creditCardTransactionInfo, String paymentSourceType, String paymentSourceID,
			boolean notificationSuppressionInd, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		try {
			// Establish the PaymentArrangementAndNotificationBo context first - this is so that all of the validation checks will be made against
			// account data as it was prior to the call to payBillOrDeposit
			ProcessPaymentBo processPaymentBo = getProcessPaymentBo(accountInfo, notificationSuppressionInd, auditInfo, sessionId);

			// Process the payment
			String authorizationNumber = payBillOrDeposit(accountInfo.getBanId(), false, paymentSourceType, paymentSourceID, paymentAmount, creditCardTransactionInfo, accountInfo,
					notificationSuppressionInd, auditInfo, sessionId);

			// Once payment is made, restore the account if required and minimum payment conditions are met
			processPaymentBo.restoreSuspendedAccount(paymentAmount);

			// Return the authorization number
			return authorizationNumber;

		} catch (ApplicationException ae) {
			throw ae;
		} catch (Throwable t) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.PAYMENT_PROCESSING_ERROR, t.getMessage(), "", t);
		}
	}

	

	
	
	@Override
	public void asyncSubmitProvisioningOrder(ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException {
		queueSender.sendUnitOfOrder(provisioningRequestInfo.getBan(), provisioningRequestInfo, "msgSubTypeProvisioning");
	}

	@Override
	public void submitProvisioningOrder(ProvisioningRequestInfo provisioningRequestInfo) throws ApplicationException {
		wirelessProvisioningServiceDao.submitProvisioningOrder(provisioningRequestInfo);
	}

	@Override
	public TestPointResultInfo testWirelessProvisioningService() {
		return wirelessProvisioningServiceDao.test();
	}

	@Override
	public CreditCheckResultInfo checkNewSubscriberEligibility(AccountInfo accountInfo, int subscriberCount, double thresholdAmount, String sessionId) throws ApplicationException {

		// Construct the auditInfo from the session
		AuditInfo auditInfo = createAuditInfoFromSession(sessionId);

		accountInfo.getCreditCheckResult0().setDeposits(accountInformationHelper.retrieveDepositsByBan(accountInfo.getBanId()));
		CreditCheckResultInfo ccHCDServiceInfo = creditProfileServiceDao.performSubscriberEligibilityCheck(accountInfo, subscriberCount, thresholdAmount, auditInfo);
		CreditCheckResultInfo ccInfo = accountLifecycleManager.retrieveLastCreditCheckResultByBan(accountInfo.getBanId(),
				accountInfo.isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS, sessionId);
		ccInfo.copyHCDServiceInfo(ccHCDServiceInfo);

		// HCD confirmed to ignore below fields in this function since new HCD service, CreditProfileSvc 3.0
		ccInfo.setBureauFile(null);
		ccInfo.setDefaultInd(null);
		ccInfo.setErrorMessage(null);

		return ccInfo;
	}

	@Override
	public HCDclpActivationOptionDetailsInfo getCLPActivationOptionsDetail(int ban) throws ApplicationException {
		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		accountInfo.setProductSubscriberLists(accountInformationHelper.retrieveProductSubscriberLists(ban));
		HCDclpActivationOptionDetailsInfo info = creditProfileServiceDao.getCLPActivationOptionsDetail(accountInfo);
		return info;
	}

	@Override
	public List<BusinessCreditIdentityInfo> getCreditEvaluationBusinessList(AccountInfo accountInfo, String sessionId) throws ApplicationException {

		// Construct the auditInfo from the session
		AuditInfo auditInfo = createAuditInfoFromSession(sessionId);

		// AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		if (accountInfo.isPostpaidBusinessRegular()) {
			return creditProfileServiceDao.retrieveCreditEvaluationBusinessList((PostpaidBusinessRegularAccountInfo) accountInfo, auditInfo);
		}

		throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.INVALID_ACCOUNT_TYPE,
				"Invalid account type/subtype [" + accountInfo.getAccountType() + accountInfo.getAccountSubType() + "].", "");
	}

	@Override
	public List<MatchedAccountInfo> getDuplicateAccountList(AccountInfo accountInfo) throws ApplicationException {
		return creditProfileServiceDao.findAccountsByCustomerProfile(accountInfo);
	}

	// [Naresh Annabathula] We have overloaded createPostpaidAccount method for EWOLF project to support new way of credit check based on creditcheckCD
	// value passed by consumer, presently below method used by WALMS 1.*, so this can be removed once we decommission WALMS 1.*.
	// Note to the above: as of the October 2016 release, the new method of passing creditcheckCd for EWOLF has not been implemented and WALMS 2.0 is
	// an orphaned web service version. I have removed or commented out all technical debt accrued as a result of this and modified the credit check logic
	// to be determined from the ACCOUNT_TYPE table in KB.
	@Override
	public PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId) throws ApplicationException {
		return createPostpaidAccount(accountInfo, compassCustomerId, businessCreditIdentityList, selectedBusinessCreditIdentity, null, creditCardAuditHeader, auditInfo, sessionId);
	}

	@Override
	public PostpaidAccountCreationResponseInfo createPostpaidAccount(AccountInfo accountInfo, String compassCustomerId, BusinessCreditIdentityInfo[] businessCreditIdentityList,
			BusinessCreditIdentityInfo selectedBusinessCreditIdentity, String creditCheckInd, AuditHeader creditCardAuditHeader, AuditInfo auditInfo, String sessionId) throws ApplicationException {

		ClientIdentity ci = this.getClientIdentity(sessionId);
		String almSessionId = accountLifecycleManager.openSession(ci.getPrincipal(), ci.getCredential(), ci.getApplication());
		PostpaidAccountCreationResponseInfo postpaidAccountCreationResponseInfo = new PostpaidAccountCreationResponseInfo();

		PaymentMethod paymentMethod = ((PostpaidAccount) accountInfo).getPaymentMethod();
		if (paymentMethod != null) {
			if (StringUtils.equals(paymentMethod.getPaymentMethod(), PaymentMethod.PAYMENT_METHOD_PRE_AUTHORIZED_CREDITCARD) && paymentMethod.getCreditCard() != null) {
				if (creditCardAuditHeader != null) {
					creditCardAuditHeader = AuditHeaderUtil.appendToAuditHeader(creditCardAuditHeader, ci.getApplication(), ci.getPrincipal());
				}
				CreditCardTransactionInfo creditCardTransactionInfo = deriveCreditCardHolderInfo(accountInfo, (CreditCardInfo) paymentMethod.getCreditCard(), creditCardAuditHeader);
				validateCreditCard(creditCardTransactionInfo, sessionId);
			}
		}

		try {
			SegmentationInfo segmentation = getReferenceDataFacade().getSegmentation(accountInfo.getBrandId(), String.valueOf(accountInfo.getAccountType()), accountInfo.getAddress0().getProvince0());
			if (segmentation != null) {
				accountInfo.setBanSegment(segmentation.getSegment());
				accountInfo.setBanSubSegment(segmentation.getSubSegment());
			}
		} catch (TelusException te) {
			throw new SystemException(SystemCodes.CMB_ALF_EJB, te.getMessage(), "", te);
		}

		// Create BAN in KB
		int banId = createCustomerAndAccount(accountInfo, compassCustomerId, sessionId);

		// Load newly created BAN from KB (but keep the BusinessRegistration value the same as we'll need it in check credit below)
		if (accountInfo.isPostpaidBusinessPersonal()) {
			BusinessRegistrationInfo businessRegistration = ((PostpaidBusinessPersonalAccountInfo)accountInfo).getBusinessRegistration();
			accountInfo = accountInformationHelper.retrieveAccountByBan(banId, Account.ACCOUNT_LOAD_ALL);
			((PostpaidBusinessPersonalAccountInfo)accountInfo).setBusinessRegistration(businessRegistration);
		} else {
			accountInfo = accountInformationHelper.retrieveAccountByBan(banId, Account.ACCOUNT_LOAD_ALL);
		}

		if ((accountInfo.getAccountType() == AccountSummary.ACCOUNT_TYPE_CORPORATE) && (accountInfo.getAccountSubType() != AccountSummary.ACCOUNT_SUBTYPE_PCS_REGIONAL)) {
			FollowUpInfo followUp = new FollowUpInfo();
			followUp.setBan(banId);
			followUp.setFollowUpType(FOLLOWUP_CORPORATE_NEW_ACCOUNT);
			followUp.setAssignedToWorkPositionId(FOLLOWUP_CORPORATE_DESTINATION);
			followUp.setText("corporate account created by dealer " + accountInfo.getDealerCode() + "-" + accountInfo.getSalesRepCode());
			accountLifecycleManager.createFollowUp(followUp, almSessionId);
		}

		postpaidAccountCreationResponseInfo.setBan(banId);

		// Note: as of the October 2016 release, the new method of passing creditcheckCd for EWOLF has not been implemented and WALMS 2.0 is an
		// orphaned web service version. I have removed or commented out all technical debt accrued as a result of this and modified the credit check
		// logic to be determined from the ACCOUNT_TYPE table in KB.
		if (isCreditCheckRequired(accountInfo, null)) {
			CreditCheckResultInfo creditCheckResultInfo = checkCredit(accountInfo, null, null, businessCreditIdentityList, selectedBusinessCreditIdentity, false, auditInfo, creditCardAuditHeader,
					almSessionId);
			postpaidAccountCreationResponseInfo.setCreditCheckResult(creditCheckResultInfo);
			postpaidAccountCreationResponseInfo.setCreditCheckPerformed(true);
		}

		return postpaidAccountCreationResponseInfo;
	}

	private boolean isCreditCheckRequired(AccountInfo accountInfo, String creditCheckInd) throws ApplicationException {

		// Note: as of the October 2016 release, the new method of passing creditcheckCd for EWOLF has not been implemented and WALMS 2.0 is an
		// orphaned web service version. I have removed or commented out all technical debt accrued as a result of this and modified the credit check
		// logic to be determined from the ACCOUNT_TYPE table in KB.
		if (StringUtils.isBlank(creditCheckInd)) {
			try {
				// Return the credit check requirement as configured in KB's ACCOUNT_TYPE table
				for (AccountTypeInfo info : getReferenceDataFacade().getAccountTypes()) {
					if (info.getAccountType() == accountInfo.getAccountType() && info.getAccountSubType() == accountInfo.getAccountSubType()) {
						return info.isCreditCheckRequired();
					}
				}
			} catch (TelusException te) {
				throw new ApplicationException(SystemCodes.CMB_ALF_EJB, te.getMessage() + "Error retrieving the AccountTypeInfo", "", te);
			}
		}

		// Default to FALSE if the account type/sub-type comparison does not result in a match or if creditCheckInd is blank
		return false;
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void updateCreditWorthiness(@BANValue int ban, String creditProgram, String creditClass, double creditLimit, String limitChangeText, boolean isCreditProfileChange,
			CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String creditResultChangeText, boolean isCreditResultChange, boolean isAsync, AuditInfo auditInfo,
			AuditHeader auditHeader, String sessionId) throws ApplicationException {

		ClientIdentity clientIdentity = getClientIdentity(sessionId);
		String almSessionId = accountLifecycleManager.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());

		// Update the audit attributes - depending the source of the call, these objects may be null
		auditInfo = getPopulatedAuditInfo(auditInfo, almSessionId);
		if (auditHeader != null) {
			auditHeader = AuditHeaderUtil.appendToAuditHeader(auditHeader, clientIdentity.getApplication(), clientIdentity.getPrincipal());
		}

		// Update CDA credit worthiness first
		CreditAssessmentInfo creditAssessmentInfo = wirelessCreditAssessmentProxyServiceDao.overrideCreditWorthiness(ban, creditProgram, creditClass, creditLimit, creditCheckResultDeposits, auditInfo, auditHeader);
		
		// If the CDA override is successful, then synchronize credit worthiness attributes with KB - we have to do this with two separate calls, depending on what has changed
		if (isCreditProfileChange) {
			// Call updateCreditProfile to synch the credit class and limit
			if (isAsync) {
				asyncUpdateCreditProfile(ban, creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCreditClassCode(),
						creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCLPCreditLimitAmount(), limitChangeText, almSessionId);
			} else {
				accountLifecycleManager.updateCreditProfile(ban, creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCreditClassCode(),
						creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCLPCreditLimitAmount(), limitChangeText, almSessionId);
			}
		}
		if (isCreditResultChange) {
			// Call updateCreditCheckResult to synch the credit class and deposit
			CreditCheckResultDepositInfo deposit = new CreditCheckResultDepositInfo();
			deposit.setProductType(CreditCheckResultInfo.PRODUCT_TYPE_CELLULAR);
			deposit.setDeposit(creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getSecurityDepositAmount());
			if (isAsync) {
				asyncUpdateCreditCheckResult(ban, creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCreditClassCode(), new CreditCheckResultDepositInfo[] { deposit },
						depositChangeReasonCode, creditResultChangeText, almSessionId);
			} else {
				accountLifecycleManager.updateCreditCheckResult(ban, creditAssessmentInfo.getCreditWorthiness().getCreditProgram().getCreditClassCode(), new CreditCheckResultDepositInfo[] { deposit },
						depositChangeReasonCode, creditResultChangeText, almSessionId);
			}
		}
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void updateCreditWorthiness(@BANValue int ban, String creditClass, double creditLimit, String limitChangeText, boolean isCreditProfileChange,
			CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String creditResultChangeText, boolean isCreditResultChange, boolean isAsync, AuditInfo auditInfo,
			AuditHeader auditHeader, String sessionId) throws ApplicationException {
		updateCreditWorthiness(ban, null, creditClass, creditLimit, limitChangeText, isCreditProfileChange, creditCheckResultDeposits, depositChangeReasonCode, creditResultChangeText,
				isCreditResultChange, isAsync, auditInfo, auditHeader, sessionId);
	}
	
	// Updated for CDA phase 1B July 2018
	@Override
	public void updateCreditProfile(@BANValue int ban, String creditClass, double creditLimit, String memoText, String sessionId) throws ApplicationException {
		updateCreditWorthiness(ban, null, creditClass, creditLimit, memoText, true, null, null, null, false, false, null, null, sessionId);
	}

	// Updated for CDA phase 1B July 2018
	@Override
	public void updateCreditCheckResult(@BANValue int ban, String creditClass, CreditCheckResultDepositInfo[] creditCheckResultDeposits, String depositChangeReasonCode, String memoText,
			String sessionId) throws ApplicationException {
		updateCreditWorthiness(ban, null, creditClass, 0, null, false, creditCheckResultDeposits, depositChangeReasonCode, memoText, true, false, null, null, sessionId);
	}

	
	@Override
	public void cancelAccountForPortOut(int ban, String activityReasonCode,Date activityDate, String portOutInd, boolean interBrandPortOutInd,
			CommunicationSuiteInfo commSuiteInfo, boolean notificationSuppressionInd,String sessionId) throws ApplicationException {
		
		/** KB API will not support cancel account port out if ban has more than one subscriber , also we should not mark as watch as port out.
		 *  we have to handle the companion cancellations due to primary port out.
		 *  We should treat comm suite account cancel ( if primary linked with any companions)  as regular cancel and primary as port out cancel. 
		 *   
		 */
		
		boolean companionCancelDueToPrimaryPortOutInd = commSuiteInfo!=null && commSuiteInfo.isRetrievedAsPrimary() == true 
				&& commSuiteInfo.getActiveAndSuspendedCompanionPhoneNumberList().isEmpty() == false;
		
		// step 1: cancel primary subscriber with port out as "Y" before cancel the account due to last primary portOut
		if (companionCancelDueToPrimaryPortOutInd) {
			String suitePrimaryNumber = commSuiteInfo.getPrimaryPhoneNumber();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("cancel the comm suite primary subscriber [ " + suitePrimaryNumber + " ] with portOut indicator as 'Y' , then cancel the account with portOut indicator as 'N'");
			}
			// step 1 : cancel primary comm suite subscriber with port out indicator as "Y"
			getSubscriberLifecycleFacade().cancelPortedInSubscriber(ban, suitePrimaryNumber, activityReasonCode, activityDate, portOutInd, interBrandPortOutInd, suitePrimaryNumber, null,
					notificationSuppressionInd, null, sessionId);
			portOutInd = "N";
			interBrandPortOutInd = false; //this mus tbe false when portOutInd='N'
		}
		
		// step 2: cancel the account in KB , make sure we retrieve the account after step 1 .
		AccountInfo accountInfo = accountInformationHelper.retrieveHwAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);	
	    accountLifecycleManager.cancelAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, interBrandPortOutInd, sessionId);
		
		// step 3 : Update bill medium and bill notification due to account cancel ( only consumer accounts are eligible )
		processBillMediumChanges(accountInfo.getBanId(), accountInfo.getAccountType(), accountInfo.getBrandId(), null, BillNotificationActivityType.ACCOUNT_CANCEL.name(), sessionId);

		// step 4 : publish the port out cancel event into kafka
		List<String> phoneNumberList = populateActiveAndSuspendSubList(accountInfo.getProductSubscriberLists());
		boolean portOutActivityInd = "Y".equalsIgnoreCase(portOutInd) ? true : false;
		accountStatusEventPublisher.publishAccountCancelPortOut(accountInfo, phoneNumberList, activityDate, activityReasonCode, portOutActivityInd, interBrandPortOutInd, createAuditInfoFromSession(sessionId), getLogicalDate(), companionCancelDueToPrimaryPortOutInd,notificationSuppressionInd);
	}

	private List<String> populateActiveAndSuspendSubList(ProductSubscriberList[] productSubscriberList){
		List<String> phoneNumberList = new ArrayList<String>();
		for (ProductSubscriberList productSubscriber : productSubscriberList) {
			SubscriberIdentifier[] activeSubscriberIdentifierArray = productSubscriber.getActiveSubscriberIdentifiers();
			for (SubscriberIdentifier subscriberIdentifier : activeSubscriberIdentifierArray) {
				phoneNumberList.add(subscriberIdentifier.getSubscriberId());
			}
			SubscriberIdentifier[] suspendSubscriberIdentifierArray = productSubscriber.getSuspendedSubscriberIdentifiers();
			for (SubscriberIdentifier subscriberIdentifier : suspendSubscriberIdentifierArray) {
				phoneNumberList.add(subscriberIdentifier.getSubscriberId());
			}
		}
		return phoneNumberList;
	}
	
	@Override
	public TestPointResultInfo testEnterpriseConsumerProfileRegistrationService() {
		return identityProfileService.testEnterpriseConsumerProfileRegistrationService();
	}

	@Override
	public TestPointResultInfo testBillNotificationManagementService() {
		return billNotificationBo.testBillNotificationManagementService();
	}
	
	@Override
	public TestPointResultInfo testPortalProfileMgmtService() {
		return billNotificationBo.testPortalProfileMgmtService();
	}

}