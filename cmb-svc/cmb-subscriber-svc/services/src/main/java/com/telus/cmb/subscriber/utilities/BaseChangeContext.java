package com.telus.cmb.subscriber.utilities;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.reference.ApplicationSummary;
import com.telus.api.reference.AudienceType;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelper;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacade;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManager;
import com.telus.cmb.reference.svc.ApplicationMessageFacade;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.bo.AccountBo;
import com.telus.cmb.subscriber.bo.ContractBo;
import com.telus.cmb.subscriber.bo.EquipmentBo;
import com.telus.cmb.subscriber.bo.SubscriberBo;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManager;
import com.telus.cmb.utility.dealermanager.svc.DealerManager;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.subscriber.info.BaseChangeInfo;
import com.telus.eas.subscriber.info.SubscriberContractInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

public abstract class BaseChangeContext<T extends BaseChangeInfo> implements ChangeContext {
	private static final Log logger = LogFactory.getLog(BaseChangeContext.class);
	
	private T changeInfo;
	private ClientIdentity clientIdentity;
	private Date systemDate;
	private Date logicalDate;
	protected ContractBo currentContract;
	protected SubscriberBo currentSubscriber;
	protected AccountBo currentAccount;
	protected EquipmentBo currentEquipment;
	protected SubscriberBo previousSubscriber;
	protected Map<Object, String> sessionIdCache = new HashMap<Object, String>();
	protected EJBController ejbController;
	
	private AuditInfo auditInfo;
	private boolean notificationSuppressionInd;
	
	
	public BaseChangeContext(T changeInfo) throws SystemException, ApplicationException {
		this.changeInfo = changeInfo;
	}

	public T getChangeInfo() {
		return changeInfo;
	}

	public void setChangeInfo(T changeInfo) {
		this.changeInfo = changeInfo;
	}
	
	public void initialize() throws SystemException, ApplicationException {
		T changeInfo = getChangeInfo();
		if (changeInfo.getCurrentAccountInfo() != null) {
			currentAccount = new AccountBo(changeInfo.getCurrentAccountInfo(), this);
		}
		
		if (changeInfo.getCurrentSubscriberInfo() != null) {
			currentSubscriber = new SubscriberBo(changeInfo.getCurrentSubscriberInfo(), this);
		}
		
		if (changeInfo.getCurrentContractInfo() != null) {
			currentContract = new ContractBo (changeInfo.getCurrentContractInfo(), this, true);
		}
		
		if (changeInfo.getCurrentEquipmentInfo() != null) {
			currentEquipment = new EquipmentBo (changeInfo.getCurrentEquipmentInfo(), this);
		}
		if (changeInfo.getPreviousSubscriberInfo() != null) {
			previousSubscriber = new SubscriberBo (changeInfo.getPreviousSubscriberInfo(), this);
		}
		
	}

	public ClientIdentity getClientIdentity() {
		return clientIdentity;
	}

	public void setClientIdentity(ClientIdentity clientIdentity) {
		this.clientIdentity = clientIdentity;
	}

	public ReferenceDataFacade getRefDataFacade() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataFacade.class);
	}

	public ReferenceDataHelper getRefDataHelper() throws ApplicationException {
		return ejbController.getEjb(ReferenceDataHelper.class);
	}
	
	public Date getSystemDate() throws ApplicationException {
		if (systemDate == null) {
			try {
				systemDate = getRefDataHelper().retrieveSystemDate();
			} catch (TelusException e) {
				logger.debug("Error retrieving logical date in refDataHelper. Returning system date."+e);
				return new Date();
			}
		}
		
		return systemDate;
	}
	
	public Date getLogicalDate() throws ApplicationException {
		if (logicalDate == null) {
			try {
				logicalDate = getRefDataHelper().retrieveLogicalDate();
			} catch (TelusException e) {
				logger.debug("Error retrieving logical date in refDataHelper. Returning system date."+e);
				return new Date();
			}
		}
		
		return logicalDate;
	}

	public SubscriberLifecycleHelper getSubscriberLifecycleHelper() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleHelper.class);
	}

	/**
	 * @return the subscriberLifecycleManager
	 * @throws ApplicationException 
	 */
	public SubscriberLifecycleManager getSubscriberLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleManager.class);
	}

	public AccountInformationHelper getAccountInformationHelper() throws ApplicationException {
		return ejbController.getEjb(AccountInformationHelper.class);
	}
	
	/**
	 * @return the accountLifecycleManager
	 * @throws ApplicationException 
	 */
	public AccountLifecycleManager getAccountLifecycleManager() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleManager.class);
	}
	
	public AccountLifecycleFacade getAccountLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(AccountLifecycleFacade.class);
	}

	public ProductEquipmentHelper getProductEquipmentHelper() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentHelper.class);
	}


	/**
	 * @return the productEquipmentManager
	 * @throws ApplicationException 
	 */
	public ProductEquipmentManager getProductEquipmentManager() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentManager.class);
	}

	public SubscriberLifecycleFacade getSubscriberLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(SubscriberLifecycleFacade.class);
	}

	public DealerManager getDealerManager() throws ApplicationException {
		return ejbController.getEjb(DealerManager.class);
	}
	
	public ContactEventManager getContactEventManager() throws ApplicationException {
		return ejbController.getEjb(ContactEventManager.class);
	}

	public ApplicationMessageFacade getApplicationMessageFacade() throws ApplicationException {
		return ejbController.getEjb(ApplicationMessageFacade.class);
	}

	/**
	 * @return the productEquipmentLifecycleFacade
	 * @throws ApplicationException 
	 */
	public ProductEquipmentLifecycleFacade getProductEquipmentLifecycleFacade() throws ApplicationException {
		return ejbController.getEjb(ProductEquipmentLifecycleFacade.class);
	}

	public SubscriberBo getCurrentSubscriber() throws SystemException, ApplicationException {
		if (currentSubscriber == null) {
			if (changeInfo.getBan() != 0 && changeInfo.getSubscriberId() != null) {
				SubscriberInfo subscriber = getSubscriberLifecycleHelper().retrieveSubscriber(changeInfo.getBan(), changeInfo.getSubscriberId());
				currentSubscriber = new SubscriberBo (subscriber, this);
			}
		}
		
		return currentSubscriber;
	}
	
	public SubscriberBo getPreviousSubscriber() throws SystemException, ApplicationException {
		if (previousSubscriber == null) {
			if (changeInfo.getPreviousBan() != 0 && changeInfo.getPreviousSubscriberId() != null) {
				SubscriberInfo subscriber = getSubscriberLifecycleHelper().retrieveSubscriber(changeInfo.getPreviousBan(), changeInfo.getPreviousSubscriberId());
				previousSubscriber = new SubscriberBo (subscriber, this);
			}
		}
		
		return previousSubscriber;
	}
	
	public AccountBo getCurrentAccount() throws ApplicationException {
		if (currentAccount == null) {
			if (changeInfo.getBan() != 0) {
				AccountInfo account = getAccountInformationHelper().retrieveAccountByBan(changeInfo.getBan(), Account.ACCOUNT_LOAD_ALL);
				currentAccount = new AccountBo(account, this);
			}
		}
		
		return currentAccount;
	}
	
	public ContractBo getCurrentContract() throws ApplicationException {
		if (currentContract == null) {
			if (getCurrentSubscriber() != null && getCurrentAccount() != null) {
				//wire current equipment and current subscriber together
				SubscriberInfo subscriberInfo = getCurrentSubscriber() .getDelegate();
				if ( subscriberInfo.getEquipment0()==null && currentEquipment!=null ) {
					subscriberInfo.setEquipment(currentEquipment.getDelegate());
				}
				
				SubscriberContractInfo contractInfo = getSubscriberLifecycleFacade().getServiceAgreement(getCurrentSubscriber().getDelegate(), getCurrentAccount().getDelegate());
				currentContract = new ContractBo (contractInfo, this, true);
			}
		}
		
		return  currentContract;
	}
	
	
	public EquipmentBo getCurrentEquipment() throws SystemException, ApplicationException {
		if (currentEquipment == null) {
			if (getCurrentSubscriber() != null) {
				EquipmentInfo equipment = getProductEquipmentHelper().getEquipmentInfobySerialNo(getCurrentSubscriber().getSerialNumber());
				currentEquipment = new EquipmentBo (equipment, this);
				
				getCurrentSubscriber().setEquipment(currentEquipment);
				getCurrentSubscriber().getDelegate().setEquipment(equipment);
			}
		}
		
		return currentEquipment;
	}
	
	public void setCurrentEquipment(EquipmentBo equipment) {
		this.currentEquipment = equipment;
	}
	
	public String getSubscriberLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(ejbController.getEjb(SubscriberLifecycleManager.class));
	}
	
	public String getAccountLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(ejbController.getEjb(AccountLifecycleManager.class));
	}
	
	public String getAccountLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(ejbController.getEjb(AccountLifecycleFacade.class));
	}
	
	public String getSubscriberLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(ejbController.getEjb(SubscriberLifecycleFacade.class));
	}
	
	private String getSessionId(Object proxy) {
		String sessionId = sessionIdCache.get(proxy);
		if ( sessionId==null ) {
			try {
				Method method = proxy.getClass().getMethod("openSession", String.class, String.class, String.class);
				sessionId = (String) method.invoke(proxy, clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
				sessionIdCache.put(proxy, sessionId );
			}catch (Exception e) {
				throw new SystemException (SystemCodes.CMB_SLF_EJB, e.getMessage(), "", e);
			}
		}
		return sessionId;
	}
	
	/**
	 * allow session ID of the creator EJB to be stored by default 
	 * @param proxy
	 * @param sessionId
	 */
	public void setSessionId(Object proxy, String sessionId) {
		if (proxy != null) {
			sessionIdCache.put(proxy, sessionId);
		}
	}
	
	public void setEjbController(EJBController ejbController) {
		this.ejbController = ejbController;
	}

	public AuditInfo getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(AuditInfo auditInfo) {
		this.auditInfo = auditInfo;
	}

	public boolean isNotificationSuppressionInd() {
		return notificationSuppressionInd;
	}

	public void setNotificationSuppressionInd(boolean notificationSuppressionInd) {
		this.notificationSuppressionInd = notificationSuppressionInd;
	}
	
	public String getApplicationMessage(int messageId) throws ApplicationException {
		return this.getApplicationMessage(messageId, Locale.ENGLISH);
	}
	
	public String getApplicationMessage(int messageId, Locale locale) throws ApplicationException {
		return this.getApplicationMessageFacade().getApplicationMessage(ApplicationSummary.DEFAULT, AudienceType.DEFAULT, messageId).getText(locale);
	}
}
