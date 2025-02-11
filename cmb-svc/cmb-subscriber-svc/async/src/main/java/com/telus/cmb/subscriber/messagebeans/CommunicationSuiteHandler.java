package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.subscriber.domain.CommunicationSuiteRepairData;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;

public class CommunicationSuiteHandler implements MessageHandler<CommunicationSuiteRepairData> {

	@Override
	public void handle(CommunicationSuiteRepairData repairData, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {

		SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);
		subscriberLifecycleFacade.repairCommunicationSuite(repairData, repairData.getSessionId());
	}

}