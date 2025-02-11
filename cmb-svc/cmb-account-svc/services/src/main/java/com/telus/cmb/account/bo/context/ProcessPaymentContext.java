package com.telus.cmb.account.bo.context;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.Subscriber;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.util.BaseContext;
import com.telus.cmb.common.util.EJBController;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.transaction.info.AuditInfo;

public class ProcessPaymentContext extends BaseContext {

	private AccountInfo account;
	private List<InvoiceHistoryInfo> invoiceHistory;
	private CreditCheckResultInfo creditCheckResult;
	private boolean notificationSuppressionInd;
	
	public ProcessPaymentContext(AccountInfo accountInfo, EJBController ejbController, ClientIdentity clientIdentity, AuditInfo auditInfo) {
		super(ejbController, clientIdentity, auditInfo);
		this.account = accountInfo;
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
	
	public String getAccountLifecycleFacadeSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleFacade());
	}
	
	public String getAccountLifecycleManagerSessionId() throws ApplicationException {
		return getSessionId(getAccountLifecycleManager());
	}

	@SuppressWarnings("unchecked")
	public void initialize() throws ApplicationException {
		// Retrieve context objects required to execute payment notification and arrangement functionality - anything not initialized here should use the setters
		this.invoiceHistory = getAccountInformationHelper().retrieveInvoiceHistory(getAccount().getBanId(), getAccount().getStartServiceDate(), getStartOfDayDate());
		// Sometimes account retrieval includes the credit check result - check here
		if (getAccount().getCreditCheckResult0() == null) {
			this.creditCheckResult = getAccountLifecycleManager().retrieveLastCreditCheckResultByBan(getAccount().getBanId(),
					getAccount().isIDEN() ? Subscriber.PRODUCT_TYPE_IDEN : Subscriber.PRODUCT_TYPE_PCS, getAccountLifecycleFacadeSessionId());
			// Refresh the delegate as well
			getAccount().getCreditCheckResult0().copyFrom(creditCheckResult);
		} else {
			this.creditCheckResult = getAccount().getCreditCheckResult0();
		}
	}
	
	public void refresh() throws ApplicationException {
		// If the refresh method is called, we must assume some context objects have changed - reload these objects here
		this.account = getAccountInformationHelper().retrieveAccountByBan(getAccount().getBanId(), Account.ACCOUNT_LOAD_ALL);
		initialize();
	}

	public AccountInfo getAccount() {
		return account;
	}

	public List<InvoiceHistoryInfo> getInvoiceHistory() {
		return invoiceHistory;
	}

	public CreditCheckResultInfo getCreditCheckResult() {
		return creditCheckResult;
	}

	public boolean isNotificationSuppressionInd() {
		return notificationSuppressionInd;
	}

	public void setNotificationSuppressionInd(boolean notificationSuppressionInd) {
		this.notificationSuppressionInd = notificationSuppressionInd;
	}
		
	public static Date getStartOfDayDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getEndOfDayDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

}