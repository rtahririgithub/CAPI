package com.telus.cmb.subscriber.messagebeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.cmb.common.util.EJBUtil;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManager;
import com.telus.eas.activitylog.queue.info.ConfigurationManagerInfo;
import com.telus.eas.config.info.AccountStatusChangeInfo;
import com.telus.eas.config.info.AddressChangeInfo;
import com.telus.eas.config.info.BillPaymentInfo;
import com.telus.eas.config.info.EquipmentChangeInfo;
import com.telus.eas.config.info.PaymentMethodChangeInfo;
import com.telus.eas.config.info.PhoneNumberChangeInfo;
import com.telus.eas.config.info.PrepaidTopupInfo;
import com.telus.eas.config.info.PricePlanChangeInfo;
import com.telus.eas.config.info.RoleChangeInfo;
import com.telus.eas.config.info.ServiceChangeInfo;
import com.telus.eas.config.info.SubscriberChangeInfo;
import com.telus.eas.config.info.SubscriberChargeInfo;

public class ConfigurationManagerHandler implements MessageHandler<ConfigurationManagerInfo> {

	private static final Log logger = LogFactory.getLog(ConfigurationManagerHandler.class);
	private ConfigurationManager configurationService = null;
	
	@Override
	public void handle(ConfigurationManagerInfo info, ClientIdentity clientIdentity, 
			MessageBeanContext beanContext) throws ApplicationException {
		
		String messageType=info.getMessageType();
		
		if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ROLE_CHANGE)){
			getConfigurationManager().report_changeRole(info.getTransactionId(), info.getTransactionDate(),
					((RoleChangeInfo)info).getOldRole(), ((RoleChangeInfo)info).getNewRole());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_BILL_PAYMENT)){
			getConfigurationManager().report_makePayment(info.getTransactionId(), info.getTransactionDate(),
					((BillPaymentInfo)info).getPaymentMethod(),((BillPaymentInfo)info).getPaymentAmount());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PRICE_PLAN_CHANGE)){
			getConfigurationManager().report_changePricePlan(info.getTransactionId(), info.getTransactionDate(),
					((PricePlanChangeInfo)info).getOldPricePlanCode(),((PricePlanChangeInfo)info).getNewPricePlanCode(),
					((PricePlanChangeInfo)info).getServiceAgreementInfo());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ADDRESS_CHANGE)){
			getConfigurationManager().report_changeAddress(info.getTransactionId(), info.getTransactionDate(),
					((AddressChangeInfo)info).getOldAddress(),((AddressChangeInfo)info).getNewAddress());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SUBSCRIBER_CHARGE)){
			getConfigurationManager().report_subscriberNewCharge(info.getTransactionId(), info.getTransactionDate(),
					((SubscriberChargeInfo)info).getChargeCode(),((SubscriberChargeInfo)info).getWaiverCode());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PAYMENT_METHOD_CHANGE)){
			getConfigurationManager().report_changePaymentMethod(info.getTransactionId(), info.getTransactionDate(),
					((PaymentMethodChangeInfo)info).getOldPaymentMethod(),((PaymentMethodChangeInfo)info).getNewPaymentMethod());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ACCOUNT_STATUS_CHANGE)){
			getConfigurationManager().report_accountStatusChange(info.getTransactionId(), info.getTransactionDate(),
					((AccountStatusChangeInfo)info).getOldHotlinedInd(),((AccountStatusChangeInfo)info).getNewHotlinedInd(),
					((AccountStatusChangeInfo)info).getOldStatus(),((AccountStatusChangeInfo)info).getNewStatus(),
					((AccountStatusChangeInfo)info).getStatusFlag());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PREPAID_TOPUP)){
			getConfigurationManager().report_prepaidAccountTopUp(info.getTransactionId(), info.getTransactionDate(),
					((PrepaidTopupInfo)info).getAmount(),((PrepaidTopupInfo)info).getCardType(),
					((PrepaidTopupInfo)info).getTopUpType());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PHONE_NUMBER_CHANGE)){
			getConfigurationManager().report_changePhoneNumber(info.getTransactionId(), info.getTransactionDate(),
					((PhoneNumberChangeInfo)info).getOldPhoneNumber(),((PhoneNumberChangeInfo)info).getNewPhoneNumber());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SERVICE_CHANGE)){
			getConfigurationManager().report_changeService(info.getTransactionId(), info.getTransactionDate(),
					((ServiceChangeInfo)info).getServices());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SUBSCRIBER_CHANGE)){
			getConfigurationManager().report_changeSubscriber(info.getTransactionId(), info.getTransactionDate(),
					((SubscriberChangeInfo)info).getOldSubscriber(),((SubscriberChangeInfo)info).getNewSubscriber());
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_EQUIPMENT_CHANGE)){
			getConfigurationManager().report_subscriberChangeEquipment(info.getTransactionId(), info.getTransactionDate(),
					((EquipmentChangeInfo)info).getSubscriberInfo(),((EquipmentChangeInfo)info).getOldEquipmentInfo(),
					((EquipmentChangeInfo)info).getNewEquipmentInfo(),((EquipmentChangeInfo)info).getDealerCode(),
					((EquipmentChangeInfo)info).getSalesRepCode(),((EquipmentChangeInfo)info).getRequestorId(),
					((EquipmentChangeInfo)info).getRepairId(),((EquipmentChangeInfo)info).getSwapType(),
					((EquipmentChangeInfo)info).getAssociatedMuleEquipmentInfo(),((EquipmentChangeInfo)info).getApplicationName());
		}else {
			logger.warn("Invalid messageType["+messageType+"]");
		}
	
	}
	
	private ConfigurationManager getConfigurationManager() {
		if (configurationService == null) {
			configurationService = EJBUtil.getHelperProxy(ConfigurationManager.class, EJBUtil.TELUS_CMBSERVICE_CONFIGURATION_MANAGER);
		}
		return configurationService;
	}

	
}
