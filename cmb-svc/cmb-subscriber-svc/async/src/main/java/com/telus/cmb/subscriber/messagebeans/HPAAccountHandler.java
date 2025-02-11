package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.subscriber.domain.HpaRewardAccountInfo;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;

public class HPAAccountHandler implements MessageHandler<HpaRewardAccountInfo> {

	@Override
	public void handle(HpaRewardAccountInfo hpaRewardAccountInfo, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {

		String hpaMethodType = hpaRewardAccountInfo.getHpaMthodType();
		SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);

		if (HpaRewardAccountInfo.METHOD_TYPE_CLOSE_HPA_ACCOUNT.equals(hpaMethodType)) {
			subscriberLifecycleFacade.closeHPAAccount(hpaRewardAccountInfo.getBan(), hpaRewardAccountInfo.getPhoneNumber(), hpaRewardAccountInfo.getSubscriptionId(), false);
		} 
	}

}