package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacade;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.cmb.common.jms.DelegatingFailureLog.MessageLogHandler;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.eas.framework.info.FalloutProcessInfo;


public abstract class AccountManagementMessageLogHandler<T> extends MessageLogHandler<T> {
	
	@Override
	public void writeToFallout(FalloutProcessInfo falloutInfo, MessageBeanContext beanContext) throws ApplicationException {
		if (falloutInfo != null && isFalloutFlagOn()) {
			AccountLifecycleFacade accountLifecycleFacade = beanContext.getEjb(AccountLifecycleFacade.class);
			//accountLifecycleFacade.reportFallout(falloutInfo);
			//TODO: PS - ?
		}
	}
	
	@Override
	protected FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutInfo = super.createFalloutData(messageInfo);
		falloutInfo.setServiceName(FalloutProcessInfo.SERVICE_NAME_ACCOUNTLIFECYCLEFACADE + ";"+ messageInfo.getMessageId()+";"+messageInfo.getMessageType()+";"+messageInfo.getMessageSubType());
		
		return falloutInfo;
	}

}
