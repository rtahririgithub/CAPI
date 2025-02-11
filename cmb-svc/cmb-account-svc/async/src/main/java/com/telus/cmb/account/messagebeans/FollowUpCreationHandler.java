package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.eas.framework.info.FollowUpInfo;

public class FollowUpCreationHandler implements MessageHandler<FollowUpInfo> {

	@Override
	public void handle(FollowUpInfo followUpInfo, ClientIdentity clientIdentity,
			MessageBeanContext beanContext) throws ApplicationException {
		AccountLifecycleManager accountLifecycleManager = beanContext.getEjb(AccountLifecycleManager.class);
		String sessionId = accountLifecycleManager.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication() );
		accountLifecycleManager.createFollowUp(followUpInfo, sessionId );
		
	}

}
