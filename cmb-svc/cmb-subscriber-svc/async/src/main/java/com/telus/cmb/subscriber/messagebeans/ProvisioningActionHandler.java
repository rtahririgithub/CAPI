package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.utility.info.ProvisioningRequestInfo;

public class ProvisioningActionHandler implements MessageHandler<ProvisioningRequestInfo> {

	@Override
	public void handle(ProvisioningRequestInfo provisioningRequestInfo, ClientIdentity clientIdentity, MessageBeanContext beanContext) 
			throws ApplicationException {
		
		SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);
		subscriberLifecycleFacade.submitProvisioningOrder(provisioningRequestInfo);
	}
	
}