package com.telus.cmb.subscriber.utilities;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.BaseContext;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.VOIPAccountInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.LicenseInfo;
import com.telus.eas.utility.info.ServiceEditionInfo;

public class BusinessConnectContext extends BaseContext {

	private boolean validContext;
	private BusinessConnectContextTypeEnum contextType;
	
	private SubscriberInfo subscriberInfo;
	private SubscriberContractInfo subscriberContractInfo;
	private AccountInfo accountInfo;
	private AddressInfo addressInfo;
	private VOIPAccountInfo voipAccountInfo;
	
	private Date logicalDate;
	private List<ServiceEditionInfo> serviceEditions;
	private List<LicenseInfo> licenses;
	private boolean isPrimaryStarterSeatActivation;

	public BusinessConnectContext(BusinessConnectContextTypeEnum contextType, SubscriberInfo subscriberInfo, SubscriberContractInfo subscriberContractInfo, EJBController ejbController, ClientIdentity clientIdentity, AuditInfo auditInfo) {		
		super(ejbController, clientIdentity, auditInfo);
		this.contextType = contextType;
		this.subscriberInfo = subscriberInfo;
		this.subscriberContractInfo = subscriberContractInfo;
		this.validContext = contextType != null && subscriberInfo.isSeatSubscriber();
	}

	public ReferenceDataFacade getReferenceDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	public ReferenceDataHelper getReferenceDataHelper() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataHelper.class);
	}

	public AccountInformationHelper getAccountInformationHelper() throws ApplicationException {
		return ejbController.getEjb(AccountInformationHelper.class);
	}

	public AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}

	public AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}

	public SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleFacade.class);
	}
	
	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleHelper.class);
	}

	public String getAccountLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleFacade());
	}

	public String getAccountLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleManager());
	}

	@SuppressWarnings("unchecked")
	public void initialize() throws ApplicationException {
		// Only initialize context for valid Business Connect subscribers and flows
		if (isValid()) {
			// Retrieve context objects required to execute Business Connect functionality
			try {
				this.logicalDate = getReferenceDataFacade().getLogicalDate();
				this.serviceEditions = getReferenceDataFacade().getServiceEditions();
				this.licenses = getReferenceDataFacade().getLicenses();
			} catch (TelusException te) {
				throw new ApplicationException(ErrorCodes.REFERENCE_DATA_ERROR, "Error retrieving reference data.", StringUtils.EMPTY, te);
			}
			if (isActivation()) {
				// Business Connect activation request needs to be validated before creating the subscriber in KB
				BusinessConnectUtil.validateBusinessConnectSeatActivationRequest(getSubscriber(), getSubscriberContract(), getAccountInformationHelper().retrieveProductSubscriberLists(getSubscriber().getBanId()));
				this.isPrimaryStarterSeatActivation = BusinessConnectUtil.isBusinessConnectPrimaryStarterSeatActivation(getSubscriber(), getAccount());
				this.addressInfo = (AddressInfo) getSubscriber().getAddress();
			} else {
				// Retrieve the VOIPAccountInfo for this BAN - this also verifies the existence of the VOIP account
				this.voipAccountInfo = getSubscriberLifecycleFacade().getVOIPAccountInfo(getSubscriber().getBanId());
			}
		}
	}

	public SubscriberInfo getSubscriber() {
		return subscriberInfo;
	}

	public SubscriberContractInfo getSubscriberContract() {
		return subscriberContractInfo;
	}
	
	public AccountInfo getAccount() throws ApplicationException {
		if (accountInfo == null) {
			this.accountInfo = getAccountInformationHelper().retrieveAccountByBan(subscriberInfo.getBanId(), Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		}
		return accountInfo;
	}
	
	public VOIPAccountInfo getVOIPAccountInfo() throws ApplicationException {
		if (voipAccountInfo == null) {
			this.voipAccountInfo = getSubscriberLifecycleFacade().getVOIPAccountInfo(getSubscriber().getBanId());
		}
		return voipAccountInfo;
	}
	
	@Override
	public void refresh() throws SystemException, ApplicationException {
		this.subscriberInfo = getSubscriberLifecycleHelper().retrieveSubscriber(subscriberInfo.getBanId(), subscriberInfo.getPhoneNumber());
		try {
			if (addressInfo == null) {
				this.addressInfo = getSubscriberLifecycleHelper().retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
			}
			subscriberInfo.setAddress(addressInfo);
		} catch (Throwable t) {
			throw new ApplicationException(ErrorCodes.ADDRESS_RETRIEVAL_ERROR, "Error retrieving address data for subscriber [" + subscriberInfo.getSubscriberId() + "].", StringUtils.EMPTY, t);
		}
	}
	
	public boolean isValid() {
		return validContext;
	}
	
	public boolean isActivation() {
		return contextType == BusinessConnectContextTypeEnum.ACTIVATION;
	}
	
	public boolean isPricePlanChange() {
		return contextType == BusinessConnectContextTypeEnum.PRICE_PLAN_CHANGE;
	}
	
	public boolean isServiceChange() {
		return contextType == BusinessConnectContextTypeEnum.SERVICE_CHANGE;
	}
	
	public boolean isPrimaryStarterSeatActivation() {
		return isPrimaryStarterSeatActivation;
	}

	public Date getLogicalDate() {
		return logicalDate;
	}
	
	public List<ServiceEditionInfo> getServiceEditions() {
		return serviceEditions;
	}

	public List<LicenseInfo> getLicenses() {
		return licenses;
	}

	public enum BusinessConnectContextTypeEnum {
		
		ACTIVATION(1), 
		PRICE_PLAN_CHANGE(2), 
		SERVICE_CHANGE(3);
		
		private final int type;

		BusinessConnectContextTypeEnum(int type) {
			this.type = type;
		}
	
		public int type() {
			return type;
		}
		
		public static BusinessConnectContextTypeEnum getBusinessConnectContextEnum(String name) {
			return EnumUtils.getEnum(BusinessConnectContextTypeEnum.class, name);
		}
		
	}

}