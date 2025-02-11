package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public class ProvisioningActionHandler implements MessageHandler<ProvisioningRequestInfo> {

	@Override
	public void handle(ProvisioningRequestInfo provisioningRequestInfo, ClientIdentity clientIdentity, MessageBeanContext beanContext) 
			throws ApplicationException {
		
		AccountLifecycleFacade accountLifecycleFacade = beanContext.getEjb(AccountLifecycleFacade.class);
		accountLifecycleFacade.submitProvisioningOrder(provisioningRequestInfo);
	}
	
}