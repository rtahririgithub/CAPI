package com.telus.cmb.account.lifecyclemanager.svc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;
import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.BillNotificationContact;
import com.telus.api.account.PaymentTransfer;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.BillCycle;
import com.telus.cmb.account.bo.ChargeAndAdjustmentBo;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.dao.AccountDao;
import com.telus.cmb.account.lifecyclemanager.dao.AdjustmentDao;
import com.telus.cmb.account.lifecyclemanager.dao.CollectionDao;
import com.telus.cmb.account.lifecyclemanager.dao.ContactDao;
import com.telus.cmb.account.lifecyclemanager.dao.CreditBalanceTransferDao;
import com.telus.cmb.account.lifecyclemanager.dao.CreditCheckDao;
import com.telus.cmb.account.lifecyclemanager.dao.FleetDao;
import com.telus.cmb.account.lifecyclemanager.dao.FollowUpDao;
import com.telus.cmb.account.lifecyclemanager.dao.InvoiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.MemoDao;
import com.telus.cmb.account.lifecyclemanager.dao.PaymentDao;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidSubscriberServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriberDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionBalanceMgmtServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.SubscriptionManagementServiceDao;
import com.telus.cmb.account.lifecyclemanager.dao.UserDao;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManagerTestPoint;
import com.telus.cmb.account.payment.kafka.CreditCheckResultEventPublisher;
import com.telus.cmb.account.payment.kafka.MultiSubscriberStatusEventPublisher;
import com.telus.cmb.account.utilities.AppConfiguration;
import com.telus.cmb.account.utilities.AuditHeaderUtil;
import com.telus.cmb.common.aop.utilities.BANValue;
import com.telus.cmb.common.dao.amdocs.AmdocsSessionManager;
import com.telus.cmb.common.dao.testpoint.AmdocsTestPointDao;
import com.telus.cmb.common.dao.testpoint.DataSourceTestPointDao;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.kafka.account.AccountEventPublisher;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.cmb.common.prepaid.PrepaidUtils;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.common.util.Utility;
import com.telus.cmb.reference.svc.BillingInquiryReferenceFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ActivationTopUpInfo;
import com.telus.eas.account.info.ActivationTopUpPaymentArrangementInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.AddressValidationResultInfo;
import com.telus.eas.account.info.AutoTopUpInfo;
import com.telus.eas.account.info.BillNotificationContactInfo;
import com.telus.eas.account.info.BillParametersInfo;
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
import com.telus.eas.account.info.CustomerNotificationPreferenceInfo;
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
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.framework.info.ChargeAdjustmentCodeInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeAdjustmentWithTaxInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.framework.info.FollowUpInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.AccountTypeInfo;
import com.telus.eas.utility.info.BillCycleInfo;
import com.telus.eas.utility.info.ClientConsentIndicatorInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.framework.config.ConfigContext;

@Stateless(name="AccountLifecycleManager", mappedName="AccountLifecycleManager")
@Remote({AccountLifecycleManager.class, AccountLifecycleManagerTestPoint.class})
@RemoteHome(AccountLifecycleManagerHome.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AccountLifecycleManagerImpl implements AccountLifecycleManager, AccountLifecycleManagerTestPoint{

	private static final Logger LOGGER = Logger.getLogger(AccountLifecycleManagerImpl.class);

	private static final short prepaidBillCycle = 88;

	private ReferenceDataHelper referenceDataHelper = null;
	
	private ReferenceDataFacade referenceDataFacade= null;
	
	private BillingInquiryReferenceFacade billingInquiryReferenceFacade= null;

	@EJB
	private AccountInformationHelper accountInformationHelper;
	
	@EJB
	private AccountLifecycleFacade accountLifecycleFacade;
	
	@Autowired
	private AccountDao accountDao;

	@Autowired
	private AdjustmentDao adjustmentDao;

	@Autowired
	private CollectionDao collectionDao;

	@Autowired
	private CreditCheckDao creditCheckDao;

	@Autowired
	private FleetDao fleetDao;

	@Autowired
	private FollowUpDao followUpDao;

	@Autowired
	private InvoiceDao invoiceDao;

	@Autowired
	private MemoDao memoDao;

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private SubscriberDao subscriberDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ContactDao contactDao;
	
	@Autowired
	private AmdocsSessionManager amdocsSessionManager;
	
	@Autowired
	@Qualifier("managerTestPointDao")
	private DataSourceTestPointDao testPointDao;
	
	@Autowired
	@Qualifier("managerAmdocsTestDao")
	private AmdocsTestPointDao amdocstestpointDao;
	
	@Autowired
	private CreditBalanceTransferDao creditBalanceTransferDao;

	@Autowired
	private PrepaidWirelessCustomerOrderServiceDao pwcosDao;
	@Autowired
	private PrepaidSubscriberServiceDao pssDao;
	@Autowired
	private SubscriptionBalanceMgmtServiceDao sbmsDao;
	@Autowired
	private SubscriptionManagementServiceDao smsDao;
		
	@Autowired
	private AccountEventPublisher eventPublisher;
	
	@Autowired
	private MultiSubscriberStatusEventPublisher multiSubEventPublisher;
	
	@Autowired
	private CreditCheckResultEventPublisher creditCheckResultEventPublisher;
	
	public void setEventPublisher(AccountEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setAdjustmentDao(AdjustmentDao adjustmentDao) {
		this.adjustmentDao = adjustmentDao;
	}

	public void setCollectionDao(CollectionDao collectionDao) {
		this.collectionDao = collectionDao;
	}

	public void setCreditCheckDao(CreditCheckDao creditCheckDao) {
		this.creditCheckDao = creditCheckDao;
	}

	public void setFleetDao(FleetDao fleetDao) {
		this.fleetDao = fleetDao;
	}

	public void setFollowUpDao(FollowUpDao followUpDao) {
		this.followUpDao = followUpDao;
	}

	public void setInvoiceDao(InvoiceDao invoiceDao) {
		this.invoiceDao = invoiceDao;
	}

//	public void setLetterDao(LetterDao letterDao) {
//		this.letterDao = letterDao;
//	}

	public void setMemoDao(MemoDao memoDao) {
		this.memoDao = memoDao;
	}

	public void setPaymentDao(PaymentDao paymentDao) {
		this.paymentDao = paymentDao;
	}

	public void setSubscriberDao(SubscriberDao subscriberDao) {
		this.subscriberDao = subscriberDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setContactDao(ContactDao contactDao) {
		this.contactDao = contactDao;
	}
	
	public void setContactDao(CreditBalanceTransferDao creditBalanceTransferDao) {
		this.creditBalanceTransferDao = creditBalanceTransferDao;
	}
	
	public void setAmdocsSessionManager(AmdocsSessionManager amdocsSessionManager) {
		this.amdocsSessionManager = amdocsSessionManager;
	}

	public void setAccountInformationHelper(
			AccountInformationHelper accountInformationHelper) {
		this.accountInformationHelper = accountInformationHelper;
	}

	@Override
	public String openSession(String userId, @Sensitive String password, String applicationId) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(userId, password, applicationId);
		return amdocsSessionManager.openSession(identity);
	}

	@Override
	public void testAmdocsConnectivity(String sessionId) throws ApplicationException {
		amdocstestpointDao.testConnectivity(sessionId);
	}

	@Override
	public void updateEmailAddress(@BANValue int ban, String emailAddress, String sessionId) throws ApplicationException {
		accountDao.updateEmailAddress(ban, emailAddress, sessionId);
		LOGGER.info("["+ban+"] email address updated to ["+emailAddress+"]");
		eventPublisher.publishPreferredEmailUpdateEvent(emailAddress, ban, sessionId);
	}

	@Override
	public void updateInvoiceProperties(@BANValue int ban, InvoicePropertiesInfo invoicePropertiesInfo, String sessionId) throws ApplicationException {
		invoiceDao.updateInvoiceProperties(ban, invoicePropertiesInfo, sessionId);
	}

	@Override
	public void updateBillCycle(@BANValue int ban, short billCycle, String sessionId)throws ApplicationException {
		accountDao.updateBillCycle(ban, billCycle, sessionId);
		eventPublisher.publishBillCycleUpdateEvent(billCycle, ban, sessionId);

	}

	@SuppressWarnings("static-access")
	@Override
	public int createAccount(AccountInfo pAccountInfo , String sessionId) throws ApplicationException {
		int ban = 0;
		try {
			String accountInfoType = AccountInfo.getBaseName(pAccountInfo.getClass().getName());
			// Check that accountInfo passed in is instance of a implemented
			// account type
			if (accountInfoType.equals("PostpaidBoxedConsumerAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessDealerAccountInfo") ||
					accountInfoType.equals("PostpaidEmployeeAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessOfficialAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessPersonalAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessRegularAccountInfo") ||
					accountInfoType.equals("PostpaidConsumerAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateRegularAccountInfo") ||
					accountInfoType.equals("PostpaidCorporatePersonalAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateRegionalAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateVPNAccountInfo") ||
					accountInfoType.equals("PrepaidConsumerAccountInfo") ||
					accountInfoType.equals("WesternPrepaidConsumerAccountInfo") ||
					accountInfoType.equals("QuebectelPrepaidConsumerAccountInfo")) {

				LOGGER.debug("Account instance: " + accountInfoType);
			} else {
				LOGGER.debug(SystemCodes.CMB_ALM_EJB+"; Account Type: " + accountInfoType + " not implemented");
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.ACCOUNT_TYPE_NOT_IMPLEMENTED, 
						"Account Type: " + accountInfoType + " not implemented", "");
			}
			LOGGER.debug("set/validate information...");

			// Set bill cycle
			if (pAccountInfo.isPrepaidConsumer()) {
				pAccountInfo.setBillCycle(prepaidBillCycle);
			} else { // postpaid
				int currentCycle = pAccountInfo.getBillCycle();
				
				String currentProvince = pAccountInfo.getHomeProvince()!=null?pAccountInfo.getHomeProvince():pAccountInfo.getAddress().getProvince();
				
				if (currentCycle == 0) {
					pAccountInfo.setBillCycle(getAvailableBillCycle(currentProvince));										
				} else {
					LOGGER.debug("currentCycle...not 0..."+currentCycle);
					BillCycleInfo[] cyclesArray = getReferenceDataFacade().getBillCycles();
					BillCycle[] billCyclesFiltered = getReferenceDataFacade().removeBillCyclesByProvince(cyclesArray, currentProvince);
					
					boolean availableCycle = false;
					int cycle = 0;

					for (int i = 0; i < billCyclesFiltered.length; i++) {
						cycle = Integer.parseInt(billCyclesFiltered[i].getCode());
						if ( cycle == currentCycle ){  // bill cycle from pAccountInfo is available
							availableCycle = true;
							break;
						}
					} //end-for
					
					if (availableCycle){
						LOGGER.debug("availableCycle matches with accountinfo object? ..."+availableCycle);
						pAccountInfo.setBillCycle(currentCycle);
					} else{
						pAccountInfo.setBillCycle(getAvailableBillCycle(currentProvince));
					}
				} // try to use current bill cycle
			} // postpaid

			// Set ixc code
			String ixcCode = pAccountInfo.getIxcCode() == null ? "" : pAccountInfo.getIxcCode().trim();
			if (ixcCode.equals("")) {
				pAccountInfo.setIxcCode(getReferenceDataHelper().retrieveDefaultLDC());
			}

			// create Account
			ban = accountDao.createAccount(pAccountInfo,sessionId);

			if ( pAccountInfo.isNoOfInvoiceChanged() && pAccountInfo.getNoOfInvoice() > 1 ) { // the default is 1 so there's no need to reset if getNoOfInvoice()==1
				BillParametersInfo billParamsInfo = accountInformationHelper.retrieveBillParamsInfo(ban);
				billParamsInfo.setNoOfInvoice((short) pAccountInfo.getNoOfInvoice());
				if (billParamsInfo.getBillFormat() == null) billParamsInfo.setBillFormat(billParamsInfo.DEFAULT_BILL_FORMAT);
				if (billParamsInfo.getMediaCategory() == null) billParamsInfo.setMediaCategory(billParamsInfo.DEFAULT_BILL_MEDIA);
				accountDao.updateBillParamsInfo(ban,  billParamsInfo ,sessionId );
				pAccountInfo.setBillParamsInfo(billParamsInfo);
			}
		} catch (TelusException te) {
			LOGGER.error("TelusException occurred",  te);
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, te.id 
					, te.getMessage() == null ? "TelusException occurred (" + te.toString() + ") - see stack trace for details" 
							: te.getMessage()
					, "", te); 
		} 

		return ban;
	}

	private int getAvailableBillCycle(String currentProvince) throws TelusException, ApplicationException {
		int[] billCycleList = getReferenceDataHelper().retrieveBillCycleListLeastUsed();
		BillCycle[] billCycles = new BillCycle[billCycleList.length];
		for (int x = 0; x < billCycleList.length; x++) {
			BillCycleInfo bci = new BillCycleInfo();
			bci.setCode(Integer.toString(billCycleList[x]));
			billCycles[x]=bci;
		}
		LOGGER.debug("retrieveBillCycleListLeastUsed...available bill cycles array length..."+billCycles.length);		
		BillCycle[] billCyclesFiltered = getReferenceDataFacade().removeBillCyclesByProvince(billCycles, currentProvince);			
		if (billCyclesFiltered == null || billCyclesFiltered.length == 0) {
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.UNEXPECTED_NO_AVAILABLE_BILL_CYCLE_ERROR, "No bill cycle available for province [" + currentProvince + 
					"]. Please check data.", ""); 
		}		
		String firstBillCycleCode = billCyclesFiltered[0].getCode();		
		LOGGER.debug("removeBillCyclesByProvince...available billCyclesFiltered array length..."+ billCyclesFiltered.length + "...and first item in array is " + firstBillCycleCode);		
		return Integer.parseInt(firstBillCycleCode);
	}

	private ReferenceDataHelper getReferenceDataHelper() {
		if (referenceDataHelper == null) {
			referenceDataHelper = EJBUtil.getHelperProxy(ReferenceDataHelper.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_HELPER);
		}
		return referenceDataHelper;
	}
	
	private ReferenceDataFacade getReferenceDataFacade() {
		if (referenceDataFacade == null) {
			referenceDataFacade = EJBUtil.getHelperProxy(ReferenceDataFacade.class, EJBUtil.TELUS_CMBSERVICE_REFERENCE_DATA_FACADE);
		}
		return referenceDataFacade;
	}

	private BillingInquiryReferenceFacade getBillingInquiryReferenceFacade() {
		if (billingInquiryReferenceFacade == null) {
			billingInquiryReferenceFacade = EJBUtil.getHelperProxy(BillingInquiryReferenceFacade.class, EJBUtil.TELUS_CMBSERVICE_BILLING_INQUIRY_REFERENCE_FACADE);
		}
		return billingInquiryReferenceFacade;
	}
	
	
	@Override
	public void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException {
		saveCreditCheckInfo (pAccountInfo, pCreditCheckResultInfo, pCreditParamType, null, null,sessionId);
	}

	@Override
	public void saveCreditCheckInfo(AccountInfo pAccountInfo, CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, ConsumerNameInfo pConsumerNameInfo, 
			AddressInfo pAddressInfo, String sessionId)	throws ApplicationException {
		creditCheckDao.saveCreditCheckInfo(pAccountInfo, pCreditCheckResultInfo, pCreditParamType, pConsumerNameInfo, pAddressInfo, sessionId);
		creditCheckResultEventPublisher.publishNewCreditCheckResult(pAccountInfo, pCreditCheckResultInfo,
				createAuditInfoFromSession(sessionId), getLogicalDate(), false);
	}

	private Date getLogicalDate() throws ApplicationException {
		try {
			return getReferenceDataFacade().getLogicalDate();
		} catch (TelusException e) {
			LOGGER.error("logical date error.", e);
		}
		return null;
	}
	

	@Override
	public void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness, 
			CreditCheckResultInfo pCreditCheckResultInfo, String pCreditParamType, String sessionId) throws ApplicationException {
		creditCheckDao.saveCreditCheckInfoForBusiness(pAccountInfo, listOfBusinesses, selectedBusiness, pCreditCheckResultInfo, pCreditParamType, sessionId);
		creditCheckResultEventPublisher.publishNewCreditCheckResult(pAccountInfo, pCreditCheckResultInfo,
				createAuditInfoFromSession(sessionId), getLogicalDate(), false);
	}

	//This method will create new credit check for business[CREDIT_CHECK_CREATE]
	@Override
	public void saveCreditCheckInfoForBusiness(AccountInfo pAccountInfo, BusinessCreditIdentityInfo[] listOfBusinesses, BusinessCreditIdentityInfo selectedBusiness, 
			CreditCheckResultInfo pCreditCheckResultInfo, String sessionId) throws ApplicationException {
		saveCreditCheckInfoForBusiness(pAccountInfo, listOfBusinesses, selectedBusiness, pCreditCheckResultInfo, "B", sessionId);
	}

	//This method will update the deposit for ban [CREDIT_CHECK_DEPOSIT_UPDATE]
	@Override
	public void updateCreditCheckResult(@BANValue int pBan, String pCreditClass, CreditCheckResultDepositInfo[] pCreditCheckResultDepositInfo, String pDepositChangedReasonCode, 
			String pDepositChangeText, String sessionId) throws ApplicationException {
		creditCheckDao.updateCreditCheckResult(pBan, pCreditClass, pCreditCheckResultDepositInfo, pDepositChangedReasonCode, pDepositChangeText, sessionId);
		CreditCheckResultInfo info = new CreditCheckResultInfo();
		info.setCreditClass(pCreditClass);
		info.setDeposits(pCreditCheckResultDepositInfo);
		creditCheckResultEventPublisher.publishModifiedCreditCheckResult(accountInformationHelper.retrieveLwAccountByBan(pBan), info,
				createAuditInfoFromSession(sessionId), getLogicalDate(), false);
	}

	//This method will update the credit limit for ban [CREDIT_CHECK_LIMIT_UPDATE]
	@Override
	public void updateCreditProfile(@BANValue int ban, String newCreditClass,double newCreditLimit, String memoText, String sessionId) throws ApplicationException {
		creditCheckDao.updateCreditProfile(ban, newCreditClass, newCreditLimit,  memoText, sessionId);
		CreditCheckResultInfo info = new CreditCheckResultInfo();
		info.setCreditClass(newCreditClass);
		info.setLimit(newCreditLimit);
		creditCheckResultEventPublisher.publishModifiedCreditCheckResult(accountInformationHelper.retrieveLwAccountByBan(ban), info,
				createAuditInfoFromSession(sessionId), getLogicalDate(), false);
	}

	//THIS METHOD NOT IN USE
	@Override
	public void updateCreditClass(@BANValue int ban, String newCreditClass, String memoText, String sessionId) throws ApplicationException {
		creditCheckDao.updateCreditClass(ban, newCreditClass, memoText, sessionId);

	}
	
	
	@Override
	public void applyPaymentToAccount(PaymentInfo pPaymentInfo, String sessionId) throws ApplicationException {
		paymentDao.applyPaymentToAccount(pPaymentInfo, sessionId);
	}

	@Override
	public void updateAccountPassword(@BANValue int pBan, @Sensitive String pAccountPassword,String sessionId) throws ApplicationException {
		accountDao.updateAccountPassword(pBan,pAccountPassword, sessionId);
	}

	
	@Override
	public void createMemo(MemoInfo pMemoInfo, String sessionId) throws ApplicationException {
		if (pMemoInfo.isSubscriberLevel()) {
			memoDao.createMemoForSubscriber(pMemoInfo, sessionId);	
		} else {
			memoDao.createMemoForBan(pMemoInfo, sessionId);
		}
	}

	@Override
	public void changePaymentMethodToRegular(@BANValue int pBan, String sessionId)throws ApplicationException {
		paymentDao.changePaymentMethodToRegular(pBan, sessionId);
	}

	@Override
	public CancellationPenaltyInfo retrieveCancellationPenalty(@BANValue int pBan, String sessionId) throws ApplicationException {
		return accountDao.retrieveCancellationPenalty(pBan, sessionId);
	}

	@Override
	public CancellationPenaltyInfo[] retrieveCancellationPenaltyList(@BANValue int banId, String[] subscriberId, String sessionId) throws ApplicationException {
		return accountDao.retrieveCancellationPenaltyList(banId,subscriberId, sessionId);
	}

	@Override
	public PaymentMethodInfo updatePaymentMethod(@BANValue int pBan, @Sensitive PaymentMethodInfo pPaymentMethodInfo, String sessionId)	throws ApplicationException {
		return paymentDao.updatePaymentMethod(pBan,pPaymentMethodInfo, sessionId);
	}

	@Override
	public void removeTalkGroup(@BANValue int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException {
		fleetDao.removeTalkGroup(ban, talkGroupInfo, sessionId);
	}

	@Override
	public void dissociateFleet(@BANValue int ban, FleetInfo fleetInfo, String sessionId) throws ApplicationException {
		fleetDao.dissociateFleet(ban, fleetInfo, sessionId);
	}

	@Override
	public void addFleet(@BANValue int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException {
		fleetDao.addFleet(ban, network, fleetInfo, numberOfSubscribers, sessionId);
	}

	@Override
	public FleetInfo createFleet(@BANValue int ban, short network, FleetInfo fleetInfo, int numberOfSubscribers, String sessionId) throws ApplicationException {
		return fleetDao.createFleet(ban, network, fleetInfo, numberOfSubscribers, sessionId);
	}

	@Override
	public void addTalkGroups(@BANValue int ban, TalkGroupInfo[] talkGroupInfo, String sessionId) throws ApplicationException {
		for (TalkGroupInfo talkGroup : talkGroupInfo) {
			fleetDao.addTalkGroup(ban, talkGroup, sessionId);
		}
	}

	@Override
	public void addTalkGroup(@BANValue int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException {
		fleetDao.addTalkGroup(ban, talkGroupInfo, sessionId);
	}

	@Override
	public TalkGroupInfo createTalkGroup(@BANValue int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException {
		return fleetDao.createTalkGroup(ban, talkGroupInfo, sessionId);
	}

	@Override
	public void updateTalkGroup(@BANValue int ban, TalkGroupInfo talkGroupInfo, String sessionId) throws ApplicationException {		
		fleetDao.updateTalkGroup(ban, talkGroupInfo, sessionId);
	}

	@Override
	public void createFollowUp(FollowUpInfo followUpInfo, String sessionId) throws ApplicationException {
		followUpDao.createFollowUp(followUpInfo, sessionId);
	}

	@Override
	public void updateFollowUp(FollowUpUpdateInfo followUpUpdateInfo, String sessionId) throws ApplicationException {
		followUpDao.updateFollowUp(followUpUpdateInfo, sessionId);
	}

	@Override
	public void updateBillSuppression(@BANValue int ban, boolean suppressBill, Date effectiveDate, Date expiryDate, String sessionId) throws ApplicationException {
		if (suppressBill == true && effectiveDate != null && expiryDate != null && effectiveDate.after(expiryDate)){
			LOGGER.debug("id=VAL10008; Effective Date must be before Expiry Date");
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.EFFECTIVE_DATE_MUST_BE_BEFORE_EXPIRY_DATE, "Effective Date must be before Expiry Date", "");
		}
		invoiceDao.updateBillSuppression(ban, suppressBill, effectiveDate, expiryDate, sessionId);
	}

	@Override
	public void updateInvoiceSuppressionIndicator(@BANValue int ban, String sessionId) throws ApplicationException {
		invoiceDao.updateInvoiceSuppressionIndicator(ban, sessionId);
	}

	@Override
	public void updateReturnEnvelopeIndicator(@BANValue int ban, boolean returnEnvelopeRequested, String sessionId) throws ApplicationException {
		invoiceDao.updateReturnEnvelopeIndicator(ban, returnEnvelopeRequested, sessionId);
	}

	@Override
	public void updateNationalGrowth(@BANValue int ban, String nationalGrowthIndicator, String homeProvince, String sessionId) throws ApplicationException {
		accountDao.updateNationalGrowth(ban, nationalGrowthIndicator, homeProvince, sessionId);
	}

	@Override
	public void restoreSuspendedAccount(@BANValue int ban, String restoreReasonCode, String sessionId) throws ApplicationException {
		restoreSuspendedAccount(ban, new Date(), restoreReasonCode, new String(""), true, sessionId);
	}

	@Override
	public void restoreSuspendedAccount(@BANValue int ban, Date restoreDate, String restoreReasonCode, String restoreComment, boolean collectionSuspensionsOnly, String sessionId) 
			throws ApplicationException {
		accountDao.restoreSuspendedAccount(ban, restoreDate, restoreReasonCode, restoreComment, collectionSuspensionsOnly, sessionId);
	}

	@Override
	public void updateFutureStatusChangeRequest(@BANValue int ban, FutureStatusChangeRequestInfo futureStatusChangeRequestInfo, String sessionId) throws ApplicationException {
		accountDao.updateFutureStatusChangeRequest(ban, futureStatusChangeRequestInfo, sessionId);
	}

	@Override
	public CollectionStateInfo retrieveBanCollectionInfo(@BANValue int ban, String sessionId) throws ApplicationException {
		return collectionDao.retrieveBanCollectionInfo(ban, sessionId);
	}

	@Override
	public void updateNextStepCollection(@BANValue int ban, int stepNumber, Date stepDate, String pathCode,  String sessionId) throws ApplicationException {
		collectionDao.updateNextStepCollection(ban, stepNumber, stepDate, pathCode, sessionId);
	}

	@Override
	public void cancelSubscribers(AccountInfo accountInfo, String[] subscriberIdArray,String[] waiveReasonArray,Date activityDate, String activityReasonCode, char depositReturnMethod, 
			 String userMemoText,Date logicalDate,boolean activityDueToPrimaryCancelInd,boolean notificationSuppressionInd,String sessionId) throws ApplicationException {
		
		subscriberDao.cancelSubscribers(accountInfo.getBanId(), activityDate, activityReasonCode, depositReturnMethod, subscriberIdArray, waiveReasonArray, userMemoText, sessionId);
		
		//publish the subscriber cancel event into kafka
		multiSubEventPublisher.publishMultiSubscriberCancel(accountInfo, Arrays.asList(subscriberIdArray), Arrays.asList(waiveReasonArray), activityDate, activityReasonCode, 
				String.valueOf(depositReturnMethod), userMemoText, createAuditInfoFromSession(sessionId), logicalDate, activityDueToPrimaryCancelInd, notificationSuppressionInd);
		
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
	
	private ClientIdentity getClientIdentity(String sessionId) throws ApplicationException {
		ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
		if (ci == null) {
			throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
		}
		return ci;
	}

	@Override
	public void changeKnowbilityPassword(@Sensitive String userId, @Sensitive String oldPassword, String newPassword, String sessionId) throws ApplicationException {
		try {
			userDao.changeKnowbilityPassword(userId, oldPassword, newPassword, sessionId);
		} catch (ApplicationException ae) {
			String errorCode = ae.getErrorCode();
			if (errorCode.equals("1118020")) { 
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ae.getErrorCode(), "Old password incorrect.", "", ae);
			} else if (errorCode.equals("1118021")) { 
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ae.getErrorCode(), "Invalid password length", "", ae);
			} else if (errorCode.equals("1118022")) {
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ae.getErrorCode(), "Invalid password first last digit", "", ae);
			} else if (errorCode.equals("1118023")) {
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ae.getErrorCode(), "Password has no digits", "", ae);								
			} else if (errorCode.equals("1118024")) {
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ae.getErrorCode(), "Password has already been used", "", ae);
			}
		}
	}


	@Override
	public void suspendAccount(@BANValue int ban, Date activityDate, String activityReasonCode, 
			String userMemoText,String sessionId) throws ApplicationException {
		if (activityReasonCode == null || activityReasonCode.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Activity Reason code should not be null.", "");
		}

		accountDao.suspendAccount(ban, activityDate, activityReasonCode, userMemoText, sessionId);
		eventPublisher.publishAccountSuspendStatusUpdateEvent(activityDate, activityReasonCode, userMemoText, null, ban, sessionId);
	}

//	@Override
//	@Deprecated
//	public void createManualLetterRequest(
//			LMSLetterRequestInfo letterRequestInfo, String sessionId)
//	throws ApplicationException {
//		letterDao.createManualLetterRequest(letterRequestInfo, sessionId);
//	}
//
//	@Override
//	@Deprecated
//	public void removeManualLetterRequest(@BANValue int ban, int requestNumber,
//			String sessionId) throws ApplicationException {
//		letterDao.removeManualLetterRequest(ban,requestNumber, sessionId);
//
//	}

	@Override
	public List retrieveFeeWaivers(@BANValue int banId, String sessionID)
	throws ApplicationException {
		return adjustmentDao.retrieveFeeWaivers(banId, sessionID);
	}

	@Override
	public List retrieveFutureStatusChangeRequests(
			@BANValue int ban, String sessionId) throws ApplicationException {
		return accountDao.retrieveFutureStatusChangeRequests(ban, sessionId);
	}

	@Override
	public void cancelAccount(@BANValue int ban, Date activityDate, String activityReasonCode, String depositReturnMethod, 
			String waiveReason, String userMemoText, boolean isPortActivity ,String sessionId) throws ApplicationException {

		if (activityReasonCode == null || activityReasonCode.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Activity Reason code should not be null.", "");
		}
		if (depositReturnMethod==null || depositReturnMethod.trim().equals("")) {
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Deposit Return Method should not be null.", "");			
		}
		accountDao.cancelAccount(ban, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText,isPortActivity, sessionId);
		// This one publish the cancel event into kafka uses xml pay load for CODS , CODS should use json pay load in future migration 
		eventPublisher.publishAccountCancelStatusUpdateEvent(activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText, false, isPortActivity, ban, sessionId);
	}


	@Override
	public void updateAuthorizationNames(@BANValue int ban, ConsumerNameInfo[] authorizationNames,String sessionId) throws ApplicationException {
		accountDao.updateAuthorizationNames(ban, authorizationNames, sessionId);
		
	}

	@Override
	public void updateAutoTreatment(@BANValue int ban, boolean holdAutoTreatment,String sessionId) throws ApplicationException {
		accountDao.updateAutoTreatment(ban, holdAutoTreatment, sessionId);

	}

	@Override
	public void updateBrand(@BANValue int ban, int brandId, String memoText,String sessionId) throws ApplicationException {
		accountDao.updateBrand(ban, brandId, memoText, sessionId);
	}

	@Override
	public void updateSpecialInstructions(@BANValue int ban, String specialInstructions,String sessionId) throws ApplicationException {
		accountDao.updateSpecialInstructions(ban, specialInstructions, sessionId);
	}

	@Override
	public void applyFeeWaiver(FeeWaiverInfo feeWaiver, String sessionId) throws ApplicationException {
		adjustmentDao.applyFeeWaiver(feeWaiver, sessionId);

	}

	@Override
	public void updateTransferPayment(@BANValue int ban, int seqNo, PaymentTransfer[] paymentTransfers, boolean allowOverPayment, String memonText, String sessionId) 
			throws ApplicationException {
		paymentDao.updateTransferPayment(ban, seqNo, paymentTransfers, allowOverPayment, memonText, sessionId);
	}

	@Override
	public AddressValidationResultInfo validateAddress(AddressInfo addressInfo, String sessionId) throws ApplicationException {
		return accountDao.validateAddress(addressInfo, sessionId);
	}

	@Override
	public CreditCheckResultInfo retrieveAmdocsCreditCheckResultByBan(@BANValue int ban, String sessionId) throws ApplicationException {
		return creditCheckDao.retrieveAmdocsCreditCheckResult(ban, sessionId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<AccountInfo> retrieveAccountsByTalkGroup(int urbanId, int fleetId, int talkGroupId, String sessionId) throws ApplicationException {
		int[] banArray = accountDao.retrieveAccountsByTalkGroup(urbanId, fleetId, talkGroupId, sessionId);
		return (Collection<AccountInfo>)accountInformationHelper.retrieveAccountsByBan(banArray);
	}

	@Override
	public void changePostpaidConsumerToPrepaidConsumer(@BANValue int ban, String sessionId) throws ApplicationException {
		AccountInfo accountInfo = null;
		try {
			accountInfo = accountInformationHelper.retrieveLwAccountByBan(ban);
		} catch (ApplicationException ae) {
			LOGGER.debug("Retrieve BAN status failed - ApplicationException occurred");
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Retrieve BAN status failed!", "",ae);
		}

		// Check that ban is in 'Tentative' status
		if (accountInfo != null) {
			if (accountInfo.getStatus() != AccountInfo.STATUS_TENTATIVE) {
				LOGGER.error("BAN must be in tentative status");
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.ACCOUNT_MUST_BE_TENTATIVE
						, "BAN must be in tentative status", "");
			}
			if (accountInfo.getAccountType() != AccountInfo.ACCOUNT_TYPE_CONSUMER || accountInfo.getAccountSubType() != AccountInfo.ACCOUNT_SUBTYPE_PCS_REGULAR) {
				LOGGER.error("BAN must be of account type Individual/Regular");
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.ACCOUNT_MUST_BE_INDIVIDUAL_REGULAR
						, "BAN must be of account type Individual/Regular", "");
			}
		}

		// change account sub type and bill cycle
		accountDao.changePostpaidConsumerToPrepaidConsumer(ban, prepaidBillCycle, sessionId);
	}

	@Override
	public List retrieveDiscounts(@BANValue int ban, String sessionId) throws ApplicationException {
		return accountDao.retrieveDiscounts(ban, sessionId);
		
	}

	@Override
	public void cancelAccountForPortOut(@BANValue int ban, String activityReasonCode, Date activityDate, String portOutInd, boolean isBrandPort, String sessionId) throws ApplicationException {
		
		boolean isPortOut = portOutInd.equalsIgnoreCase("Y");
		try {			
			accountDao.cancelAccountForPortOut(ban, activityReasonCode, activityDate, isPortOut, isBrandPort, sessionId);
			// This one publish the cancel event into kafka uses xml pay load for CODS , CODS should use json pay load in future migration 
			eventPublisher.publishAccountCancelStatusUpdateEvent(activityDate, activityReasonCode, null, null, null, isBrandPort, isPortOut, ban, sessionId);			
		} catch (ApplicationException exception) {
			if(Utility.isConcurrentUpdateError(exception, ban)){
				String methodName = "cancelAccountForPortOut";
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") The error is concurrent update error, now retry the transaction ...");
				accountDao.cancelAccountForPortOut(ban, activityReasonCode, activityDate, isPortOut, isBrandPort, sessionId);
				LOGGER.debug("("+getClass().getName()+"."+ methodName+") retry succeeded, Leaving...");
				//return;
			} else {
				throw exception;
			}
		}		
	}

	@SuppressWarnings("static-access")
	@Override
	public void updateAccount(AccountInfo accountInfo, String sessionId) throws ApplicationException {
		String methodName = "updateAccount";
		String accountInfoType = AccountInfo.getBaseName(accountInfo.getClass().getName());
		LOGGER.debug("("+getClass().getName()+"."+ methodName+") Entering...");

		try {
			// Check that acocuntInfo passed in is instance of a implemented
			// account type
			if (accountInfoType.equals("PostpaidBoxedConsumerAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessDealerAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessPersonalAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessRegularAccountInfo") ||
					accountInfoType.equals("PostpaidConsumerAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateRegularAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateRegionalAccountInfo") ||
					accountInfoType.equals("PostpaidCorporateVPNAccountInfo") ||
					accountInfoType.equals("PrepaidConsumerAccountInfo") ||
					accountInfoType.equals("QuebectelPrepaidConsumerAccountInfo") ||
					accountInfoType.equals("WesternPrepaidConsumerAccountInfo")||
					accountInfoType.equals("PostpaidEmployeeAccountInfo") ||
					accountInfoType.equals("PostpaidBusinessOfficialAccountInfo") ||
					accountInfoType.equals("PostpaidCorporatePersonalAccountInfo")) {
				LOGGER.debug("(" + getClass().getName() + "." + methodName + ")" + "Account instance: " + AccountInfo.getBaseName(accountInfo.getClass().getName()));
			} else {
				LOGGER.debug("(" + getClass().getName() + "." + methodName + ") "+ SystemCodes.CMB_ALM_EJB + "; Account Type: " + AccountInfo.getBaseName(accountInfo.getClass().getName()) + 
						" not implemented");
				throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Account Type: " + AccountInfo.getBaseName(accountInfo.getClass().getName()) + " not implemented", "");
			}

			// Determine the account type billing name format
			accountInfo.setBillingNameFormat((accountInfo.isPostpaidCorporateRegular() || accountInfo.isPostpaidBusinessRegular() ||
					(accountInfo.isCorporate() && !accountInfo.isPostpaidCorporatePersonal()))
					? AccountTypeInfo.BILLING_NAME_FORMAT_BUSINESS : AccountTypeInfo.BILLING_NAME_FORMAT_PERSONAL);
			AccountTypeInfo[] accountTypes = getReferenceDataFacade().getAccountTypes();
			for (int i = 0 ; i < (accountTypes != null ? accountTypes.length : 0); i++) {
				AccountTypeInfo accountType = accountTypes[i];
				if (accountType.getAccountType() == accountInfo.getAccountType() && accountType.getAccountSubType() == accountInfo.getAccountSubType()) {
					accountInfo.setBillingNameFormat(accountType.getBillingNameFormat());
					break;
				}
			}

			if (accountInfo.isNoOfInvoiceChanged()) {
				BillParametersInfo billParamsInfo = accountInformationHelper.retrieveBillParamsInfo(accountInfo.getBanId());
				int noOfInvoice = accountInfo.getNoOfInvoice();

				if (billParamsInfo.getNoOfInvoice() == noOfInvoice) {
					accountInfo.setNoOfInvoiceChanged(false);
				} else {
					billParamsInfo.setNoOfInvoice((short) noOfInvoice);
					if (billParamsInfo.getBillFormat() == null) billParamsInfo.setBillFormat(billParamsInfo.DEFAULT_BILL_FORMAT);
					if (billParamsInfo.getMediaCategory() == null) billParamsInfo.setMediaCategory(billParamsInfo.DEFAULT_BILL_MEDIA);
				}
				accountInfo.setBillParamsInfo(billParamsInfo);
			}

			boolean blockDirectUpdate = false;
			
			try {
				int brandId = accountInfo.getBrandId();
				char accountType = accountInfo.getAccountType();
				char accountSubType = accountInfo.getAccountSubType();
				String productType = Subscriber.PRODUCT_TYPE_PCS;
				if (accountInfo.isIDEN()) {
					productType = Subscriber.PRODUCT_TYPE_IDEN;
				}
				
				blockDirectUpdate = AppConfiguration.isBlockDirectUpdate() && accountLifecycleFacade.isEnterpriseManagedData(brandId, accountType, accountSubType, productType, 
						AccountSummary.PROCESS_TYPE_ACCOUNT_UPDATE);
			} catch (Exception e) {
				LOGGER.error("Error evaluating blockDirectUpdate in updateAccount. Defaulting to " + blockDirectUpdate, e);
			}
				
			// call method on DAO
			accountDao.updateAccount(accountInfo, blockDirectUpdate, sessionId);
			
			eventPublisher.publishAccountUpdatedEvent(accountInfo, sessionId);

			LOGGER.debug("("+getClass().getName()+"."+ methodName+") Leaving...");
			return;
		} catch (TelusException te) {
			LOGGER.error("TelusException occurred",  te);
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, te.id, 
					te.getMessage() == null ? "TelusException occurred (" + te.toString() + ") - see stack trace for details" : te.getMessage(), "", te); 
		} 

	}

	@Override
	public void suspendSubscribers(@BANValue int ban, Date activityDate, String activityReasonCode, String[] subscriberId, String userMemoText, String sessionId) throws ApplicationException {
		subscriberDao.suspendSubscribers(ban, activityDate, activityReasonCode, subscriberId, userMemoText, sessionId);
	}	

	@Override
	public void reverseCredit(CreditInfo creditInfo, String reversalReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (creditInfo.isSubscriberLevel()) {
			adjustmentDao.reverseCreditForSubscriber(creditInfo, reversalReasonCode, memoText, false,sessionId);
		} else {
			adjustmentDao.reverseCreditForBan(creditInfo, reversalReasonCode, memoText, false,sessionId);
		}
	}

	@Override
	public void reverseCreditWithOverride(CreditInfo creditInfo, String reversalReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (creditInfo.isSubscriberLevel()) {
			adjustmentDao.reverseCreditForSubscriber(creditInfo, reversalReasonCode, memoText, true,sessionId);
		} else {
			adjustmentDao.reverseCreditForBan(creditInfo, reversalReasonCode, memoText, true,sessionId);
		}
	}

	@Override
	public void deleteCharge(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (chargeInfo.isSubscriberLevel()) {
			adjustmentDao.deleteChargeForSubscriber(chargeInfo, deletionReasonCode, memoText, false,sessionId);
		} else {
			adjustmentDao.deleteChargeForBan(chargeInfo, deletionReasonCode, memoText, false,sessionId);
		}
	}

	@Override
	public void deleteChargeWithOverride(ChargeInfo chargeInfo, String deletionReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (chargeInfo.isSubscriberLevel()) {
			adjustmentDao.deleteChargeForSubscriber(chargeInfo, deletionReasonCode, memoText, true,sessionId);
		} else {
			adjustmentDao.deleteChargeForBan(chargeInfo, deletionReasonCode, memoText, true,sessionId);
		}
	}

	@Override
	public double applyChargeToAccount(ChargeInfo chargeInfo, String sessionId) throws ApplicationException {
		double chargeSeqNo = 0;
		if (chargeInfo.isSubscriberLevel()) {
			chargeSeqNo = applyChargeToAccountForSubscriber(chargeInfo, false, sessionId);
		} else {
			chargeSeqNo = adjustmentDao.applyChargeToAccountForBan(chargeInfo, false,sessionId);
		}		
		return chargeSeqNo;
	}

	@Override
	public double applyChargeToAccountWithOverride(ChargeInfo chargeInfo, String sessionId) throws ApplicationException {
		double chargeSeqNo = 0;
		if (chargeInfo.isSubscriberLevel()){
			chargeSeqNo = applyChargeToAccountForSubscriber (chargeInfo, true, sessionId);
		} else {
			chargeSeqNo = adjustmentDao.applyChargeToAccountForBan(chargeInfo, true,sessionId);
		}
		return chargeSeqNo;
	}
	
	private double applyChargeToAccountForSubscriber(ChargeInfo chargeInfo, boolean overrideThreshold, String sessionId) throws ApplicationException {
		if (chargeInfo.isPrepaid()) {
			ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
			String userId = null;
			if (ci != null) {
				userId = ci.getPrincipal();
			} else {
				throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
			}
			adjustBalanceForPayAndTalkSubscriber(userId, chargeInfo);
			return 0;
		}
		return adjustmentDao.applyChargeToAccountForSubscriber(chargeInfo, overrideThreshold, sessionId);
	}

	@Override
	public double adjustCharge(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (chargeInfo.isSubscriberLevel()) {
			return adjustmentDao.adjustChargeForSubscriber(chargeInfo,adjustmentAmount,adjustmentReasonCode,memoText,false,sessionId);
		} else {
			return adjustmentDao.adjustChargeForBan(chargeInfo,adjustmentAmount,adjustmentReasonCode,memoText,false,sessionId);
		}
	}

	@Override
	public double adjustChargeWithOverride(ChargeInfo chargeInfo, double adjustmentAmount, String adjustmentReasonCode, String memoText, String sessionId) throws ApplicationException {
		if (chargeInfo.isSubscriberLevel()) {
			return adjustmentDao.adjustChargeForSubscriber(chargeInfo,adjustmentAmount,adjustmentReasonCode,memoText,true,sessionId);
		} else {
			return adjustmentDao.adjustChargeForBan(chargeInfo,adjustmentAmount,adjustmentReasonCode,memoText,true,sessionId);
		}
	}

	@Override
	public void restoreSuspendedSubscribers(@BANValue int ban, Date restoreDate, String restoreReasonCode, String[] subscriberId, String restoreComment, String sessionId) 
			throws ApplicationException {
		subscriberDao.restoreSuspendedSubscribers(ban, restoreDate, restoreReasonCode, subscriberId, restoreComment, sessionId);
	}

//	@Override
//	public void createManualLetterRequest(LMSRequestInfo lmsRequestInfo, String sessionId) throws ApplicationException {
//		letterDao.createManualLetterRequest(lmsRequestInfo, sessionId);
//	}

	@Override
	public void refundPaymentToAccount(@BANValue int ban, int paymentSeq, String reasonCode, String memoText, boolean isManual, String authorizationCode, String sessionId)	
			throws ApplicationException {
		paymentDao.refundPaymentToAccount(ban, paymentSeq, reasonCode, memoText, isManual, authorizationCode, sessionId);
	}

	@Override
	public void applyDiscountToAccount(DiscountInfo discountInfo, String sessionId) throws ApplicationException {
		if (discountInfo.isSubscriberLevel()) {
			adjustmentDao.applyDiscountToAccountForSubscriber(discountInfo, sessionId);
		} else {
			adjustmentDao.applyDiscountToAccountForBan(discountInfo, sessionId);
		}
	}

	@Override
	public String performPostAccountCreationPrepaidTasks(@BANValue int ban, PrepaidConsumerAccountInfo prepaidConsumerAccountInfo, AuditHeader auditHeader,String sessionId) 
			throws ApplicationException {
		String creditCardReferenceNumber;
		char accountStatus;
		AuditHeader header = null;
		String applicationId = "";
		String userId = "";
		ClientIdentity ci=null;

		// Check that ban is in 'Tentative' status
		try {
			accountStatus = accountInformationHelper.retrieveAccountStatusByBan(ban).charAt(0);
		} catch (ApplicationException ae) {
			LOGGER.debug("Retrieve BAN status failed - ApplicationException occurred");
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Retrieve BAN status failed!", "", ae);
		}

		if (accountStatus != AccountInfo.STATUS_TENTATIVE) {
			LOGGER.error("BAN must be in tentative status");
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, ErrorCodes.ACCOUNT_MUST_BE_TENTATIVE, "BAN must be in tentative status", "");
		}

		prepaidConsumerAccountInfo.setBanId(ban);

		ci = this.amdocsSessionManager.getClientIdentity(sessionId);

		if ( ci!= null) {
			applicationId = ci.getApplication();
			userId = ci.getPrincipal();
		} else {
			throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
		}

		if ( auditHeader!=null ) {
			header = AuditHeaderUtil.appendToAuditHeader(auditHeader,applicationId,userId);
		}

		creditCardReferenceNumber = accountInformationHelper.validatePayAndTalkSubscriberActivation(applicationId, userId, prepaidConsumerAccountInfo, header );

		return creditCardReferenceNumber;
	}

	@Override
	public void suspendAccountForPortOut(@BANValue int ban, String activityReasonCode, Date activityDate, String portOutInd, String sessionId) throws ApplicationException {
		accountDao.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, sessionId);
		eventPublisher.publishAccountSuspendStatusUpdateEvent(activityDate, activityReasonCode, null, portOutInd, ban, sessionId);
	}

	@Override
	public boolean applyCreditToAccount(CreditInfo creditInfo, String sessionId) throws ApplicationException {
		if (creditInfo.isSubscriberLevel()){
			return applyCreditToAccountForSubscriber(creditInfo, false, sessionId);
		} else {
			return adjustmentDao.applyCreditToAccountForBan(creditInfo, false, sessionId);
		}
	}

	@Override
	public boolean applyCreditToAccountWithOverride(CreditInfo creditInfo, String sessionId) throws ApplicationException {
		if (creditInfo.isSubscriberLevel()) {
			return applyCreditToAccountForSubscriber(creditInfo, true, sessionId);
		} else {
			return adjustmentDao.applyCreditToAccountForBan(creditInfo, true, sessionId);
		}
	}
	
	private boolean applyCreditToAccountForSubscriber(CreditInfo creditInfo, boolean overrideThreshold, String sessionId) throws ApplicationException {
		if (creditInfo.isPrepaid()) {
			ClientIdentity ci = this.amdocsSessionManager.getClientIdentity(sessionId);
			String userId = null;

			if (ci != null) {
				userId = ci.getPrincipal();
			} else {
				throw new ApplicationException(SystemCodes.AMDOCS, "Amdocs Session is not initialized", "");
			}
			adjustBalanceForPayAndTalkSubscriber(userId, creditInfo);
			return true;
		}
		
		return adjustmentDao.applyCreditToAccountForSubscriber(creditInfo, overrideThreshold, sessionId);
	}


	@Override
	public void adjustBalanceForPayAndTalkSubscriber(@BANValue int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId) 
			throws ApplicationException {
		adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, null, CreditInfo.TAX_OPTION_NO_TAX);		
	}

	@Override
	public void adjustBalanceForPayAndTalkSubscriber(@BANValue int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax)
			throws ApplicationException {
		adjustBalanceForPayAndTalkSubscriber(pBan, pPhoneNumber, pUserId, pAmount, pReasonCode, pTransactionId, pTax, CreditInfo.TAX_OPTION_ALL_TAXES);		
	}

	@Override
	public void adjustBalanceForPayAndTalkSubscriber(@BANValue int pBan, String pPhoneNumber, String pUserId, double pAmount, String pReasonCode, String pTransactionId, TaxSummaryInfo pTax,
			char pTaxOption) throws ApplicationException {		
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("adjustBalanceForPayAndTalkSubscriber with phone number: " + pPhoneNumber + " and amount " + pAmount);
		}
		
		sbmsDao.adjustBalance(pPhoneNumber, pAmount, pReasonCode, pTransactionId,pUserId);
		// Handle taxes for adjustment for if adjustment is taxable
		if (pTaxOption != CreditInfo.TAX_OPTION_NO_TAX) {
			// GST
			if (pTax.getGSTAmount() != 0) {
				sbmsDao.adjustBalance(pPhoneNumber, pTax.getGSTAmount(), pTax.getPrepaidGSTAdjustmentReasonCode(), pTransactionId,pUserId);
			}
			// PST
			if ((pTax.getPSTAmount() != 0) && (pTaxOption != CreditInfo.TAX_OPTION_GST_ONLY)) {
				sbmsDao.adjustBalance(pPhoneNumber, pTax.getPSTAmount(), pTax.getPrepaidPSTAdjustmentReasonCode(), pTransactionId,pUserId);
			}
			// HST
			if ((pTax.getHSTAmount() != 0) && (pTaxOption != CreditInfo.TAX_OPTION_GST_ONLY)) {
				sbmsDao.adjustBalance(pPhoneNumber, pTax.getHSTAmount(), pTax.getPrepaidHSTAdjustmentReasonCode(), pTransactionId,pUserId);
			}
		}
		
	}

	@Override
	public void adjustBalanceForPayAndTalkSubscriber(String pUserId, ChargeInfo pChargeInfo) throws ApplicationException {
		if (LOGGER.isDebugEnabled() && pChargeInfo != null) {
			LOGGER.debug("adjustBalanceForPayAndTalkSubscriber (Charge) with phone number: " + pChargeInfo.getSubscriberId());
		}
		sbmsDao.charge(pChargeInfo,pUserId);
	}

	@Override
	public void adjustBalanceForPayAndTalkSubscriber(String c, CreditInfo pCreditInfo) throws ApplicationException {

		// The following section is migrated from old code but it appears to be useless because
		// there is no way to set GSTAmount, PSTAmount, HSTAmount on CreditInfo
		// The private taxSummary field in the CreditInfo class has no mutator methods

		// Handle taxes for credits for if credit is taxable
			String phoneNumber = pCreditInfo.getPhoneNumber();
			if (phoneNumber == null) {
				phoneNumber = pCreditInfo.getSubscriberId();
			}
			if (LOGGER.isDebugEnabled() && pCreditInfo != null) {
				LOGGER.info("adjustBalanceForPayAndTalkSubscriber (Credit) with phone number=" + pCreditInfo.getPhoneNumber() +", subscriberId="+pCreditInfo.getSubscriberId());
			}
			
			sbmsDao.credit(pCreditInfo,c);
			if ((pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_NO_TAX)) {
				// GST
				if (pCreditInfo.getGSTAmount() != 0) {
					sbmsDao.adjustBalance(phoneNumber, pCreditInfo.getGSTAmount(), pCreditInfo.getGSTAdjustmentReasonCode(), "",c);
				}
				// PST
				if ((pCreditInfo.getPSTAmount() != 0) && (pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_GST_ONLY)){
					sbmsDao.adjustBalance(phoneNumber, pCreditInfo.getPSTAmount(), pCreditInfo.getPSTAdjustmentReasonCode(), "",c);
				}
				// HST
				if ((pCreditInfo.getHSTAmount() != 0) && (pCreditInfo.getTaxOption() != CreditInfo.TAX_OPTION_GST_ONLY)) {
					sbmsDao.adjustBalance(phoneNumber, pCreditInfo.getHSTAmount(), pCreditInfo.getHSTAdjustmentReasonCode(), "",c);
				}	    	  
			}
	}

	/**
	 * @deprecated since 2011 September release
	 */	

	@SuppressWarnings("deprecation")
	@Override
	public void saveBillNotificationDetails(@BANValue int ban, long portalUserID, BillNotificationContactInfo[] billNotificationContact, String applicationCode) throws ApplicationException {
		if (billNotificationContact != null && billNotificationContact.length > 0) {
			// TODO Remove EPOST related code and IF branch as part of the BillNotificationManagement
			//      web service hand over , target for August-November 2010
			boolean hasEPost = accountInformationHelper.hasEPostSubscription(ban);
			if (null != billNotificationContact[0].getBillNotificationType()) {
				if (!hasEPost && !(billNotificationContact[0].getBillNotificationType().equalsIgnoreCase(BillNotificationContact.NOTIFICATION_TYPE_EPOST))) {
					invoiceDao.hasEPostFalseNotificationTypeNotEPost(ban, portalUserID, billNotificationContact, applicationCode);
				} else if (!hasEPost && (billNotificationContact[0].getBillNotificationType().equalsIgnoreCase(BillNotificationContact.NOTIFICATION_TYPE_EPOST))){
					invoiceDao.hasEPostFalseNotificationTypeEPost(ban, portalUserID, billNotificationContact, applicationCode);
				} else if (hasEPost && !(billNotificationContact[0].getBillNotificationType().equalsIgnoreCase(BillNotificationContact.NOTIFICATION_TYPE_EPOST))){
					invoiceDao.hasEPostNotificationTypeNotEPost(ban, portalUserID, billNotificationContact, applicationCode);
				} else if (hasEPost && (billNotificationContact[0].getBillNotificationType().equalsIgnoreCase(BillNotificationContact.NOTIFICATION_TYPE_EPOST))){
					// Upate Email. Enable/Disable Notification 
					invoiceDao.hasEPostNotificationTypeEPost(ban, portalUserID, billNotificationContact, applicationCode);
				}
			} else if (hasEPost) {
				invoiceDao.hasEPostNotificationTypeNotEPost(ban, portalUserID, billNotificationContact, applicationCode);
			}
		} else {
			LOGGER.debug("BillNotification Array is Empty");
		}

	}

	@Override
	public void applyCreditForFeatureCard(CardInfo cardInfo,
			ServiceInfo[] cardServices, String userId) {
			//creditForFeatureCard - this method is deprecated, no replacement in Prepaid Web Services.
			LOGGER.info("ApplyCreditForFeatureCard is called but PrepaidApi.creditForFeatureCard is deprecated and no replacement. PhoneNumber: " + cardInfo.getPhoneNumber());
	}


	@Override
	public void saveActivationTopUpArrangement(String banId, String MDN, String serialNo, 
			ActivationTopUpPaymentArrangementInfo topUpPaymentArrangement, String user) throws ApplicationException {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("saveActivationTopUpArrangement with BAN " + banId + "phone number: " + MDN);
			}
			pwcosDao.saveActivationTopUpArrangement(banId, serialNo, MDN, topUpPaymentArrangement.retrievePaymentCardList());
	}

	@Override
	public void removeTopupCreditCard(String MDN) throws ApplicationException {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("removeTopupCreditCard with phone number: " + MDN);
			}
			smsDao.removeTopupCreditCard(MDN);
	}

	@Override
	public void creditSubscriberMigration(String applicationId, String pUserId,
			ActivationTopUpInfo activationTopUpInfo, String phoneNumber,
			String esn, String provinceCode,
			PrepaidConsumerAccountInfo pPrepaidConsumerAccountInfo)
	throws ApplicationException {
			String source = PrepaidUtils.getSourceFromAppId(applicationId);
			if (pPrepaidConsumerAccountInfo != null) {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("creditSubscriberMigration with phone number: " + phoneNumber);
				
				pwcosDao.creditSubscriberMigration("" + pPrepaidConsumerAccountInfo.getBanId(),
						esn,
						pPrepaidConsumerAccountInfo.getAssociatedHandsetIMEI(),
						phoneNumber,
						provinceCode,
						pPrepaidConsumerAccountInfo.getPin(),
						activationTopUpInfo.getAmount(),
						"" + activationTopUpInfo.getRateId(),
						activationTopUpInfo.getExpiryDays(),
						source,
						pUserId,
						"P2P",
						activationTopUpInfo.getReasonCode());
			} else {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("PrepaidConsumerAccountInfo is null for creditSubscriberMigration");
			}
		}

	/* (non-Javadoc)
	 * @see com.telus.cmb.account.informationhelper.svc.AccountInformationHelper#applyTopUpForPayAndTalkSubscriber(int, java.lang.String, com.telus.eas.equipment.info.CardInfo, java.lang.String, java.lang.String)
	 */
	@Override
	public String applyTopUpForPayAndTalkSubscriber(@BANValue int pBan,
			String pPhoneNumber, CardInfo pAirtimeCard, String pUserId,
			String pApplicationId) throws ApplicationException {
		String transactionId = null;

			if (LOGGER.isInfoEnabled())
				LOGGER.info("applyTopUpForPayAndTalkSubscriber with phone number: " + pPhoneNumber);
			
			String airtimeCardSerialNumber = pAirtimeCard.getSerialNumber().substring(3) + pAirtimeCard.getPIN();
			smsDao.applyTopUp(pPhoneNumber, airtimeCardSerialNumber);
			
			if (StringUtils.isBlank(transactionId)) {
				//Requested by Prepaid team to provide a dummy id if no transaction Id being returned - Surepay Retirement April/2014
				transactionId = "100000";
			}
			if (LOGGER.isInfoEnabled())
				LOGGER.info("applyTopUpForPayAndTalkSubscriber with transaction Id " + transactionId 
						+ " for BAN " + pBan + " and phone number " + pPhoneNumber);
		return transactionId;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.account.informationhelper.svc.AccountInformationHelper#applyTopUpForPayAndTalkSubscriber(int, java.lang.String, double, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String applyTopUpForPayAndTalkSubscriber(@BANValue int pBan,
			String pPhoneNumber, double pAmount, String pOrderNumber,
			String pUserId, String pApplicationId) throws ApplicationException {
		String bihReferenceNumber = "";
			if (pOrderNumber == null || pOrderNumber.equals("") ) {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("applyTopUpForPayAndTalkSubscriber (applyTopUpWithCreditCard) with phone number: " + pPhoneNumber);
				bihReferenceNumber = smsDao.applyTopUpWithCreditCard(pPhoneNumber, pAmount);
			} else {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("applyTopUpForPayAndTalkSubscriber (applyTopUpWithDebitCard) with phone number: " + pPhoneNumber);
				bihReferenceNumber = smsDao.applyTopUpWithDebitCard(pPhoneNumber, pAmount);
			}
			
			if (StringUtils.isBlank(bihReferenceNumber)) {
				//Requested by Prepaid team to provide a dummy number if no reference number being returned - Surepay Retirement April/2014
				bihReferenceNumber = "100000";
		}
		if (LOGGER.isInfoEnabled())
			LOGGER.info("applyTopUpForPayAndTalkSubscriber with reference number " + bihReferenceNumber 
					+ " for BAN " + pBan + " and phone number " + pPhoneNumber);
		return bihReferenceNumber;
	}

	@Override
	public void updateAccountPIN(@BANValue int ban, String mdn, String serialNo, String prevPIN, String PIN, String user) throws ApplicationException {
		if(PIN == null || PIN.trim().length() == 0)
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "PIN should be a non-empty string", "");
		if(prevPIN.equals(PIN))
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "New password can not be same as oldPassword", "");
		
			if (LOGGER.isInfoEnabled())
				LOGGER.info("updateAccountPIN with phone number: " + mdn);
			
			pssDao.updateAccountPIN(mdn, PIN);
		}
	@Deprecated
	@Override
	public void updateTopupCreditCard(String banId, String MDN,
			String serialNo, @Sensitive CreditCardInfo creditCard, String user,
			boolean encrypted) {

			updateTopupCreditCard(banId, MDN, creditCard, false);

	}
	public void updateTopupCreditCard(String banId, String MDN, @Sensitive CreditCardInfo creditCard, boolean isFirstTimeRegistration) {
		//TODO should we throw ApplicationException in this API???
		try {
			//We need to register the CC if that is not available
			if (isFirstTimeRegistration) {
				if (LOGGER.isInfoEnabled())
					LOGGER.info("updateTopupCreditCard by registering credit card with phone number: " + MDN);
				
				smsDao.registerTopupCreditCard(MDN, creditCard);
			} else { 
				if (LOGGER.isInfoEnabled())
					LOGGER.info("updateTopupCreditCard by updating credit card with phone number: " + MDN);
				
				smsDao.updateTopupCreditCard(MDN, creditCard);
			}
		} catch (Throwable t) {
			//TODO update the API to throw ApplicationException
			throw new SystemException(SystemCodes.PREPAID, "", "Prepaid subsystem exception.", "", t);
		}
		
	}

	@Override
	public void updateAutoTopUp(AutoTopUpInfo pAutoTopUpInfo, String pUserId, boolean existingAutoTopUp, 
			boolean existingThresholdRecharge) throws ApplicationException {

		int pBan = pAutoTopUpInfo.getBan();
		String pPhoneNumber = pAutoTopUpInfo.getPhoneNumber();
		
			if (LOGGER.isInfoEnabled())
				LOGGER.info("updateAutoTopUp with phone number: " + pPhoneNumber);
			smsDao.updateAutoTopUp(pPhoneNumber, pAutoTopUpInfo, existingAutoTopUp, existingThresholdRecharge);
	}

	@Override
	public CreditCheckResultInfo retrieveLastCreditCheckResultByBan(@BANValue int ban, String productType, String sessionId) throws ApplicationException {

		CreditCheckResultInfo amdocsCreditCheckResult = retrieveAmdocsCreditCheckResultByBan(ban, sessionId);
		CreditCheckResultInfo creditCheckResult = accountInformationHelper.retrieveLastCreditCheckResultByBan(ban, productType);
		if (amdocsCreditCheckResult != null) {
			creditCheckResult.copyAmdocsInfo(amdocsCreditCheckResult);
		}

		return creditCheckResult;
	}
	
	@Override
	public CreditCheckResultInfo retrieveKBCreditCheckResultByBan(@BANValue int ban, String productType, String sessionId) throws ApplicationException {

		CreditCheckResultInfo amdocsCreditCheckResult = retrieveAmdocsCreditCheckResultByBan(ban, sessionId);
		CreditCheckResultInfo creditCheckResult = accountInformationHelper.retrieveKBCreditCheckResultByBan(ban, productType);
		if (amdocsCreditCheckResult != null) {
			creditCheckResult.copyAmdocsInfo(amdocsCreditCheckResult);
		}

		return creditCheckResult;
	}

	@Override
	public String getUserProfileID(String sessionId) throws ApplicationException {
	      return userDao.getUserProfileID(sessionId);
	}

	@Override
	public void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user, boolean encrypted) {
		updateTopupCreditCard(banId, MDN, creditCard, user, encrypted, false);
	}

	@Override
	public void updateTopupCreditCard(String banId, String MDN, CreditCardInfo creditCard, String user, boolean encrypted, boolean isFirstTimeRegistration) {
		updateTopupCreditCard(banId, MDN, creditCard, isFirstTimeRegistration);
	}
	
	@Override
	public void updateBillingInformation(@BANValue int billingAccountNumber, BillingPropertyInfo billingPropertyInfo, String sessionId) throws ApplicationException {
		this.contactDao.updateBillingInformation(billingAccountNumber, billingPropertyInfo, sessionId);
		eventPublisher.publishBillingInfoUpdateEvent(billingPropertyInfo, billingAccountNumber, sessionId);		
	}

	@Override
	public void updateContactInformation(@BANValue int billingAccountNumber, ContactPropertyInfo contactPropertyInfo, String sessionId) throws ApplicationException {
		this.contactDao.updateContactInformation(billingAccountNumber, contactPropertyInfo, sessionId);
		eventPublisher.publishContactInfoUpdateEvent(contactPropertyInfo, billingAccountNumber, sessionId);
	}

	@Override
	public void updatePersonalCreditInformation(int ban, PersonalCreditInfo personalCreditInfo, String sessionId) throws ApplicationException {
		this.accountDao.updatePersonalCreditInformation(ban, personalCreditInfo, sessionId);		
	}

	@Override
	public void updateBusinessCreditInformation(int ban, BusinessCreditInfo businessCreditInfo, String sessionId) throws ApplicationException {
		this.accountDao.updateBusinessCreditInformation(ban, businessCreditInfo, sessionId);		
	}

	@Override
	public TestPointResultInfo testCodsDataSource() {
		return testPointDao.testCodsDataSource();
	}

	@Override
	public TestPointResultInfo testPrepaidWirelessCustomerOrderService() {
		return pwcosDao.test();
	}
	
	@Override
	public TestPointResultInfo testPrepaidSubscriberService() {
		return pssDao.test();
	}
	
	@Override
	public TestPointResultInfo testSubscriptionBalanceMgmtService() {
		return sbmsDao.test();
	}
	
	@Override
	public TestPointResultInfo testSubscriptionManagementService() {
		return smsDao.test();
	}
	
	@Override
	public String getVersion() {
		return ConfigContext.getProperty("fw_buildLabel");
	}

	@Override
	public void createCreditBalanceTransferRequest(int sourceBan, int targetBan, String sessionId) throws ApplicationException {
		creditBalanceTransferDao.createCreditBalanceTransferRequest(sourceBan, targetBan, sessionId);		
	}

	@Override
	public void cancelCreditBalanceTransferRequest(int ban, String sessionId) throws ApplicationException {
		creditBalanceTransferDao.cancelCreditBalanceTransferRequest(ban, sessionId);		
	}

	@Override
	public List getCreditBalanceTransferRequestList(int creditTransferSequenceNumber,
			String sessionId) throws ApplicationException {
		return creditBalanceTransferDao.getCreditBalanceTransferRequestList(creditTransferSequenceNumber, sessionId);
	}
	
	@Override
	public boolean applyCreditToAccountAndSubscriber(CreditInfo creditInfo, boolean overrideThresholdInd , String sessionId) throws ApplicationException {
		TaxSummaryInfo taxSummaryInfo = getTaxSummaryInfo(creditInfo.getBan(), creditInfo.getAmount());
		creditInfo.getTaxSummary().copyFrom(taxSummaryInfo);
		if (overrideThresholdInd) {
			return applyCreditToAccountWithOverride(creditInfo, sessionId);
		} else {
			return applyCreditToAccount(creditInfo, sessionId);
		}
	}
	
	private TaxSummaryInfo getTaxSummaryInfo(int ban, double amount) throws ApplicationException {
		AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL);
		String provinceCode = accountInfo.getHomeProvince();
		TaxExemptionInfo taxExemptionInfo = getTaxExemptionInfo(accountInfo, false);
		return getTaxSummaryInfo(provinceCode, amount, taxExemptionInfo);
	}

	private TaxSummaryInfo getTaxSummaryInfo(String provinceCode, double amount, TaxExemptionInfo taxExemptionInfo) throws ApplicationException {
		try {
			TaxSummaryInfo taxSummaryInfo = getBillingInquiryReferenceFacade().getTaxCalculationListByProvince(provinceCode, amount, taxExemptionInfo);
			taxSummaryInfo.setProvince(provinceCode);
			return taxSummaryInfo;
		} catch (Exception e) {
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, "Error while Calculating Tax", e.getMessage(), "");
		}
	}	
	
	@Override
	public List applyChargesAndAdjustmentsToAccount(List chargeAdjInfoList, String sessionId) throws ApplicationException {
		return adjustmentDao.applyChargesAndAdjustmentsToAccount(chargeAdjInfoList, sessionId);
	}

	@Override
	public List applyChargesAndAdjustmentsToAccountWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, 
			String sessionId) throws ApplicationException {			
		return adjustmentDao.applyChargesAndAdjustmentsToAccountWithTax(getChargeAdjustmentWithTaxInfo(chargeAdjInfoList, taxationProvinceCode, waiveTaxInd), sessionId);
	}

	@Override
	public List applyChargesAndAdjustmentsToAccountForSubscriber(List chargeAdjInfoList, String sessionId) throws ApplicationException {		
		return adjustmentDao.applyChargesAndAdjustmentsToAccountForSubscriber(chargeAdjInfoList, sessionId);
	}

	@Override
	public List applyChargesAndAdjustmentsToAccountForSubscriberWithTax(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd, 
			String sessionId) throws ApplicationException {	
		return adjustmentDao.applyChargesAndAdjustmentsToAccountForSubscriberWithTax(getChargeAdjustmentWithTaxInfo(chargeAdjInfoList, taxationProvinceCode, waiveTaxInd), sessionId);
	}

	private List getChargeAdjustmentWithTaxInfo(List chargeAdjInfoList, String taxationProvinceCode, boolean waiveTaxInd)
			throws ApplicationException {
		List<ChargeAdjustmentWithTaxInfo> chargeAdjTaxInfoList = new ArrayList<ChargeAdjustmentWithTaxInfo>();		
		for (ChargeAdjustmentInfo chargeAdjInfo : (List<ChargeAdjustmentInfo>) chargeAdjInfoList) {
			AccountInfo accountInfo = accountInformationHelper.retrieveAccountByBan(chargeAdjInfo.getBan(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
			TaxExemptionInfo taxExemptionInfo = getTaxExemptionInfo(accountInfo, waiveTaxInd);
			ChargeAdjustmentWithTaxInfo chargeAdjTaxInfo = new ChargeAdjustmentWithTaxInfo();
			chargeAdjTaxInfo.setChargeAdjustmentInfo(chargeAdjInfo);	
			chargeAdjTaxInfo.setTaxExemptionInfo(taxExemptionInfo);			
			chargeAdjTaxInfo.setTaxSummaryInfo(getTaxSummaryInfo(taxationProvinceCode, chargeAdjInfo.getChargeAmount(), taxExemptionInfo));
			chargeAdjTaxInfoList.add(chargeAdjTaxInfo);
		}
		return chargeAdjTaxInfoList;
	}

	private TaxExemptionInfo getTaxExemptionInfo(AccountInfo accountInfo, boolean waiveTaxInd) {
		TaxExemptionInfo taxExemptionInfo = new TaxExemptionInfo();
		if (waiveTaxInd) {
			taxExemptionInfo.setGstExemptionInd(true);
			taxExemptionInfo.setPstExemptionInd(true);
			taxExemptionInfo.setHstExemptionInd(true);
		} else {
			taxExemptionInfo.setGstExemptionInd(accountInfo.isGSTExempt());
			taxExemptionInfo.setPstExemptionInd(accountInfo.isPSTExempt());
			taxExemptionInfo.setHstExemptionInd(accountInfo.isHSTExempt());
		}
		return taxExemptionInfo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List adjustChargeToAccountForSubscriberReturn(int ban, String subscriberNumber, String phoneNumber, List chargeAdjInfoList, Date searchFromDate, 
			Date searchToDate, String adjustmentMemoText, String productType, boolean bypassAuthorizationInd, boolean overrideThresholdInd, String sessionId) throws ApplicationException {		
		Map<String,ChargeAdjustmentInfo> resultMap; 
		List<ChargeAdjustmentInfo> resultList = new ArrayList<ChargeAdjustmentInfo>();
		ChargeAndAdjustmentBo chargeAndAdjustmentBo = new ChargeAndAdjustmentBo (this, accountInformationHelper);
		chargeAndAdjustmentBo.setAdjustmentMemoText(adjustmentMemoText);
		chargeAndAdjustmentBo.setBan(ban);
		chargeAndAdjustmentBo.setBypassAuthorizationInd(bypassAuthorizationInd);
		chargeAndAdjustmentBo.setOverrideThresholdInd(overrideThresholdInd);
		chargeAndAdjustmentBo.setPhoneNumber(phoneNumber);
		chargeAndAdjustmentBo.setProductType(productType);
		chargeAndAdjustmentBo.setSearchFromDate(searchFromDate);
		chargeAndAdjustmentBo.setSearchToDate(searchToDate);
		chargeAndAdjustmentBo.setSubscriberId(subscriberNumber);
		
		for(ChargeAdjustmentCodeInfo chargeAdjustmentInfo : (List<ChargeAdjustmentCodeInfo>)chargeAdjInfoList){
			chargeAndAdjustmentBo.initialize(chargeAdjustmentInfo);
			chargeAndAdjustmentBo.retrieveCharges();
			chargeAndAdjustmentBo.performCTNChangeSearchIfNecessary();
			chargeAndAdjustmentBo.applyChargesAndAdjustments(sessionId);
		}
		
		resultMap = chargeAndAdjustmentBo.getResultMap();
		resultList.addAll(resultMap.values());
		chargeAndAdjustmentBo.clear();
		return resultList;
	}

	@Override
	public double applyChargeToAccount(ChargeInfo chargeInfo, boolean isWebserviceCall, String sessionId) throws ApplicationException {
		if (isWebserviceCall) {
			AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(chargeInfo.getBan());
			chargeInfo.setPrepaid(!accountInfo.isPostpaid());
		}
		return applyChargeToAccount(chargeInfo, sessionId);
	}

	@Override
	public double applyChargeToAccountWithOverride(ChargeInfo chargeInfo, boolean isWebserviceCall, String sessionId) throws ApplicationException {
		if (isWebserviceCall) {
			AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByBan(chargeInfo.getBan());
			chargeInfo.setPrepaid(!accountInfo.isPostpaid());
		}
		return applyChargeToAccountWithOverride(chargeInfo, sessionId);
	}

	@Override
	public List getCustomerNotificationPreferenceList(@BANValue int ban, String sessionId) throws ApplicationException {
		String methodName = "getCustomerNotificationPreferenceList";
		List<CustomerNotificationPreferenceInfo> preferenceList = accountDao.getCustomerNotificationPreferenceList(ban, sessionId);
		updateDescription(preferenceList);
		if (LOGGER.isDebugEnabled() && preferenceList != null) {
			LOGGER.debug("("+getClass().getName()+"."+ methodName+") BAN: " + ban);
			for (CustomerNotificationPreferenceInfo info : preferenceList) {
				LOGGER.debug("Code: " + info.getCode() + " | Desc: " + info.getDescription());
			}
		}
		return preferenceList;
	}
	
	private void updateDescription(List<CustomerNotificationPreferenceInfo> preferenceList) throws ApplicationException {
		if (preferenceList != null && preferenceList.size() > 0) {
			for (CustomerNotificationPreferenceInfo info:preferenceList) {
				info.setDescription(getClientConsentIndicatorDesc(info.getCode()));
			}
		}
	}
	
	private String getClientConsentIndicatorDesc(String code) throws ApplicationException {
		String result = "";
		try {
			ClientConsentIndicatorInfo[] indicators = getReferenceDataFacade().getClientConsentIndicators();
			if (indicators != null && indicators.length >0) {
				for (ClientConsentIndicatorInfo indicator:indicators) {
					if (indicator.getCode().trim().equals(code.trim())) {
						result = indicator.getDescription();
						break;
					}
				}
			}
		} catch (TelusException te) {
			LOGGER.error("TelusException occurred",  te);
			throw new ApplicationException(SystemCodes.CMB_ALM_EJB, te.id, 
					te.getMessage() == null ? "TelusException occurred (" + te.toString() + ") - see stack trace for details" : te.getMessage(), "", te); 
		} 
		return result;
	}
	
	@Override
	public void updateCustomerNotificationPreferenceList(@BANValue int ban, List notificationPreferenceList, String sessionId) throws ApplicationException {
		accountDao.updateCustomerNotificationPreferenceList(ban, notificationPreferenceList, sessionId);
	}

	@Override
	public void updateHotlineInd(int ban, boolean hotLineInd, String sessionId) throws ApplicationException {		
		accountDao.updateHotlineInd(ban, hotLineInd, sessionId);
	}
	
}
