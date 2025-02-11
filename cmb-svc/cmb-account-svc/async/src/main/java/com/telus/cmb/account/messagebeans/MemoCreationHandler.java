package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.eas.framework.info.MemoInfo;


public class MemoCreationHandler implements MessageHandler<MemoInfo> {
	
	public void handle ( MemoInfo memoInfo, ClientIdentity clientIdentity, MessageBeanContext beanContext ) throws ApplicationException{
		AccountLifecycleManager accountLifecycleManager = beanContext.getEjb(AccountLifecycleManager.class);
		String sessionId = accountLifecycleManager.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication() );
		accountLifecycleManager.createMemo( memoInfo, sessionId );
	}

}
