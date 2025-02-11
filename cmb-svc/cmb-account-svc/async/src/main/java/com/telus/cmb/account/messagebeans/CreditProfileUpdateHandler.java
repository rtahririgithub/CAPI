package com.telus.cmb.account.messagebeans;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandlerList;

public class CreditProfileUpdateHandler implements MessageHandlerList<List<Object>>{

	@Override
	public void handle(List<Object> messageList, ClientIdentity clientIdentity,
			MessageBeanContext beanContext) throws ApplicationException {
		AccountLifecycleManager accountLifecycleManager = beanContext.getEjb(AccountLifecycleManager.class);
		String sessionId = accountLifecycleManager.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication() );
		accountLifecycleManager.updateCreditProfile(Integer.parseInt(messageList.get(1).toString()), 
				messageList.get(2).toString(), Double.parseDouble(messageList.get(3).toString()), 
				messageList.get(4).toString(), sessionId);
		
	}

}
