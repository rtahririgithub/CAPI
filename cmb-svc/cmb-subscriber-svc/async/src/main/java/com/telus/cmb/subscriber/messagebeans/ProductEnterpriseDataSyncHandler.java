package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacade;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;

public class ProductEnterpriseDataSyncHandler implements MessageHandler<ProductEnterpriseDataInfo> {

	@Override
	public void handle(ProductEnterpriseDataInfo message, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {
		String messageType = message.getMessageType();
		SubscriberLifecycleFacade subscriberLifecycleFacade = beanContext.getEjb(SubscriberLifecycleFacade.class);
		
		String sessionId = subscriberLifecycleFacade.openSession(clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
		
		if (ProductEnterpriseDataInfo.MESSAGE_TYPE_INSERT_PRODUCT_INSTANCE.equals(messageType)) {
			if (message.getAccountInfo()!=null && message.getSubscriberInfo() != null && message.getEquipmentInfo() != null && message.getSubscriberContractInfo() != null) {
				subscriberLifecycleFacade.insertProductInstance(message.getAccountInfo(), message.getSubscriberInfo(), message.getEquipmentInfo(), message.getSubscriberContractInfo(),message.getProcessType(),sessionId);
			} else {
				subscriberLifecycleFacade.insertProductInstance(message.getBillingAccountNumber(), message.getSubscriberId(), message.getProcessType(), sessionId);
			}

		}else if (ProductEnterpriseDataInfo.MESSAGE_TYPE_UPDATE_PRODUCT_INSTANCE.equals(messageType)) {
			if (message.getSubscriberInfo() != null && message.getEquipmentInfo() != null && message.getSubscriberContractInfo() != null) {
				subscriberLifecycleFacade.updateProductInstance(message.getBillingAccountNumber(), message.getSubscriberInfo(), message.getEquipmentInfo(), message.getSubscriberContractInfo(), message.getProcessType(), sessionId);
			}else {
				subscriberLifecycleFacade.updateProductInstance(message.getBillingAccountNumber(), message.getSubscriberId(), message.getPhoneNumber(), message.getProcessType(), sessionId);
			}
		}
//			else if (ProductEnterpriseDataInfo.MESSAGE_TYPE_MANAGE_PRODUCT_PARAMETERS.equals(messageType)) {
//			subscriberLifecycleFacade.manageProductParameters(message.getBillingAccountNumber(), message.getSubscriberId(), message.getProcessType(), sessionId);
//		}else if (ProductEnterpriseDataInfo.MESSAGE_TYPE_MANAGE_PRODUCT_RESOURCES.equals(messageType)) {
//			subscriberLifecycleFacade.manageProductResources(message.getBillingAccountNumber(), message.getSubscriberId(), message.getProcessType(), sessionId);			
//		}
	}

}
