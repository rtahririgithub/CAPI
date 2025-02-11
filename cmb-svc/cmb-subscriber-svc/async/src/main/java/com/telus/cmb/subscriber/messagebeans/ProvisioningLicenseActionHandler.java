package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.utility.info.ProvisioningLicenseInfo;

public class ProvisioningLicenseActionHandler implements MessageHandler<ProvisioningLicenseInfo> {

	@Override
	public void handle(ProvisioningLicenseInfo provisioningLicenseInfo , ClientIdentity clientIdentity, MessageBeanContext beanContext)  throws ApplicationException {
		SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);
		
		if(provisioningLicenseInfo.getTransactionType().equals(ProvisioningLicenseInfo.PROV_LICENSE_TRANSACTION_TYPE_ADD)){
			subscriberLifecycleFacade.addLicenses(provisioningLicenseInfo.getBan(), provisioningLicenseInfo.getSubscriptionId(), provisioningLicenseInfo.getSwitchCodes());
		}else if(provisioningLicenseInfo.getTransactionType().equals(ProvisioningLicenseInfo.PROV_LICENSE_TRANSACTION_TYPE_REMOVE)){
			subscriberLifecycleFacade.removeLicenses(provisioningLicenseInfo.getBan(), provisioningLicenseInfo.getSubscriptionId(), provisioningLicenseInfo.getSwitchCodes());
		}
	}
	
}