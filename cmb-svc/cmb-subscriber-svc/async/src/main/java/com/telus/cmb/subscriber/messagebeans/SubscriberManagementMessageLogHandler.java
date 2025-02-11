package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.cmb.common.jms.DelegatingFailureLog.MessageLogHandler;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.eas.framework.info.FalloutProcessInfo;

public abstract class SubscriberManagementMessageLogHandler<T> extends MessageLogHandler<T> {

	@Override
	public void writeToFallout(FalloutProcessInfo falloutInfo, MessageBeanContext beanContext) throws ApplicationException {
		
		// TODO: PS - are we using this?
		
//		if (falloutInfo != null && isFalloutFlagOn()) {
//			SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);	
//			subscriberLifecycleFacade.reportFallout(falloutInfo);
//		}
		
	}

	@Override
	public FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutInfo = super.createFalloutData(messageInfo);
		falloutInfo.setServiceName(FalloutProcessInfo.SERVICE_NAME_SUBSCRIBERLIFECYCLEFACADE);
		
		return falloutInfo;
	}
	
	

}
