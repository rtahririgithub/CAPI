package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.eas.framework.info.BillingAccountEnterpriseDataInfo;

public class BillingAccountEnterpriseDataSyncHandler implements MessageHandler<BillingAccountEnterpriseDataInfo> {

	@Override
	public void handle(BillingAccountEnterpriseDataInfo message, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {
		AccountLifecycleFacade accountLifecycleFacade = beanContext.getEjb(AccountLifecycleFacade.class);
		String messageType = message.getMessageType();
		String sessionId = accountLifecycleFacade.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication() );
		
		if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_INSERT_BILLING_ACCOUNT.equals(messageType)) {
			accountLifecycleFacade.insertBillingAccount(message.getBillingAccountNumber(), message.getCustomerID(), message.getProcessType(), sessionId);
		}else if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT.equals(messageType)) {
			if (message.getAccountInfo() != null) {
				accountLifecycleFacade.updateBillingAccount(message.getAccountInfo(), message.getProcessType(), sessionId);
			}else {
				accountLifecycleFacade.updateBillingAccount(message.getBillingAccountNumber(), message.getProcessType(), sessionId);
			}
		}else if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_INSERT_CUSTOMER_WITH_BILLING_ACCOUNT.equals(messageType)) {
			accountLifecycleFacade.insertCustomerWithBillingAccount(message.getBillingAccountNumber(), message.getProcessType(), sessionId);
		}
//		else if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_PAY_CHANNEL.equals(messageType)) {
//			accountLifecycleFacade.updatePayChannel(message.getBillingAccountNumber(), message.getProcessType(), sessionId);
//		}else if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_BILLING_ACCOUNT_STATUS.equals(messageType)) {
//			accountLifecycleFacade.updateBillingAccountStatus(message.getBillingAccountNumber(), message.getStatus(), message.getProcessType(), sessionId);
//		}else if (BillingAccountEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_BILL_CYCLE.equals(messageType)) {
//			accountLifecycleFacade.updateBillingCycle(message.getBillingAccountNumber(), message.getNextBillCycle(), message.getProcessType(), sessionId);
//		}
//		

	}

}
