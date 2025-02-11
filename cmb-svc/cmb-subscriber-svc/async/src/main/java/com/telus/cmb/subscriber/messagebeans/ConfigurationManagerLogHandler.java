package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
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
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;

public class ConfigurationManagerLogHandler extends SubscriberManagementMessageLogHandler<MessageInfo<ConfigurationManagerInfo>> {

	
	@Override
	public void writeLog(MessageInfo<ConfigurationManagerInfo> messageInfo) {
		ConfigurationManagerInfo configurationManagerInfo = messageInfo.getMessageObject();
		String messageType=configurationManagerInfo.getMessageType();
		
		StringBuffer content = new StringBuffer();
		content.append("Message Type: "+messageType).append("|");
		content.append("TransactionId: "+configurationManagerInfo.getTransactionId()).append("|");
		content.append("TransactionDate: "+messageInfo.getDateFormat().format(configurationManagerInfo.getTransactionDate())).append("|");
		
		if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ROLE_CHANGE)){
			content.append(changeRoleLogContent(configurationManagerInfo));
		}else if (messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_BILL_PAYMENT)){
			content.append(ChangeBillPaymentLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PRICE_PLAN_CHANGE)){
			content.append(ChangePricePlanLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ADDRESS_CHANGE)){
			content.append(ChangeAddressLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SUBSCRIBER_CHARGE)){
			content.append(ChangeSubscriberChargeLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PAYMENT_METHOD_CHANGE)){
			content.append(ChangePaymentMethodLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_ACCOUNT_STATUS_CHANGE)){
			content.append(ChangeAccountStatusLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PREPAID_TOPUP)){
			content.append(ChangePrepaidTopupLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_PHONE_NUMBER_CHANGE)){
			content.append(ChangePhoneNumberLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SERVICE_CHANGE)){
			content.append(ChangeServiceLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_SUBSCRIBER_CHANGE)){
			content.append(ChangeSubscriberChangeLogContent(configurationManagerInfo));
		}else if(messageType.equals(ConfigurationManagerInfo.MESSAGE_TYPE_EQUIPMENT_CHANGE)){
			content.append(ChangeEquipmentLogContent(configurationManagerInfo));
		}
			
		
		messageInfo.writeLog(content.toString());
		
	}
	
	private StringBuffer changeRoleLogContent(ConfigurationManagerInfo configurationManagerInfo ){
		StringBuffer content =new StringBuffer();
		RoleChangeInfo roleChangeInfo=(RoleChangeInfo)configurationManagerInfo;
		content.append("OldRole: "+roleChangeInfo.getOldRole()).append("|");
		content.append("NewRole: "+roleChangeInfo.getNewRole()).append("|");
		return content;
	}
	
	private StringBuffer ChangeBillPaymentLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		BillPaymentInfo billPaymentInfo=(BillPaymentInfo)configurationManagerInfo;
		content.append("Payment Method: "+billPaymentInfo.getPaymentMethod()).append("|");
		content.append("Payment Amount:"+billPaymentInfo.getPaymentAmount()).append("|");
		return content;
	}

	
	private StringBuffer ChangePricePlanLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		PricePlanChangeInfo pricePlanChangeInfo=(PricePlanChangeInfo)configurationManagerInfo;
		content.append("Old Priceplan Code: "+pricePlanChangeInfo.getOldPricePlanCode()).append("|");
		content.append("New Priceplan Code:"+pricePlanChangeInfo.getNewPricePlanCode()).append("|");
		ServiceAgreementInfo[] services= pricePlanChangeInfo.getServiceAgreementInfo();
		for(ServiceAgreementInfo service: services)
			content.append("Service: "+service.toString()).append("|");
		
		
		return content;
	}

	private StringBuffer ChangeAddressLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		AddressChangeInfo addressChangeInfo=(AddressChangeInfo)configurationManagerInfo;
		content.append("Old Address: "+addressChangeInfo.getOldAddress()).append("|");
		content.append("New Address :"+addressChangeInfo.getNewAddress()).append("|");
		return content;
	}

	private StringBuffer ChangeSubscriberChargeLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		SubscriberChargeInfo subscriberChargeInfo=(SubscriberChargeInfo)configurationManagerInfo;
		content.append("Charge Code: "+subscriberChargeInfo.getChargeCode()).append("|");
		content.append("Waiver Code:"+subscriberChargeInfo.getWaiverCode()).append("|");
		return content;
	}

	private StringBuffer ChangePaymentMethodLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		PaymentMethodChangeInfo paymentMethodChangeInfo=(PaymentMethodChangeInfo)configurationManagerInfo;
		content.append("NewPaymentMethod: "+paymentMethodChangeInfo.getNewPaymentMethod()).append("|");
		content.append("OldPaymentMethod:"+paymentMethodChangeInfo.getOldPaymentMethod()).append("|");
		return content;
	}

	private StringBuffer ChangeAccountStatusLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		AccountStatusChangeInfo accountStatusChangeInfo=(AccountStatusChangeInfo)configurationManagerInfo;
		content.append("OldHotlinedInd: "+accountStatusChangeInfo.getOldHotlinedInd()).append("|");
		content.append("NewHotlinedInd:"+accountStatusChangeInfo.getNewHotlinedInd()).append("|");
		content.append("Old Status: "+accountStatusChangeInfo.getOldStatus()).append("|");
		content.append("New Status:"+accountStatusChangeInfo.getNewStatus()).append("|");
		content.append("Status Flag: "+accountStatusChangeInfo.getStatusFlag()).append("|");
		
		return content;
	}

	private StringBuffer ChangePrepaidTopupLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		PrepaidTopupInfo prepaidTopupInfo=(PrepaidTopupInfo)configurationManagerInfo;
		content.append("Amount: "+prepaidTopupInfo.getAmount()).append("|");
		content.append("Card Type:"+prepaidTopupInfo.getCardType()).append("|");
		content.append("TopUp Type:"+prepaidTopupInfo.getTopUpType()).append("|");
		return content;
	}

	private StringBuffer ChangePhoneNumberLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		PhoneNumberChangeInfo phoneNumberChangeInfo=(PhoneNumberChangeInfo)configurationManagerInfo;
		content.append("Old Phone Number: "+phoneNumberChangeInfo.getOldPhoneNumber()).append("|");
		content.append("New Phone Number:"+phoneNumberChangeInfo.getNewPhoneNumber()).append("|");
		return content;
	}

	private StringBuffer ChangeServiceLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		ServiceChangeInfo serviceChangeInfo=(ServiceChangeInfo)configurationManagerInfo;
		ServiceAgreementInfo[] services= serviceChangeInfo.getServices();
		for(ServiceAgreementInfo service: services)
			content.append("Service: "+service.toString()).append("|");
		
		return content;
	}
	private StringBuffer ChangeSubscriberChangeLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		SubscriberChangeInfo subscriberChangeInfo=(SubscriberChangeInfo)configurationManagerInfo;
		content.append("OldSubscriber: "+subscriberChangeInfo.getOldSubscriber()).append("|");
		content.append("NewSubscriber:"+subscriberChangeInfo.getNewSubscriber()).append("|");
		
		return content;
	}

	private StringBuffer ChangeEquipmentLogContent(ConfigurationManagerInfo configurationManagerInfo){
		StringBuffer content =new StringBuffer();
		EquipmentChangeInfo equipmentChangeInfo=(EquipmentChangeInfo)configurationManagerInfo;
		content.append("SubscriberInfo: "+equipmentChangeInfo.getSubscriberInfo()).append("|");
		content.append("OldEquipmentInfo:"+equipmentChangeInfo.getOldEquipmentInfo()).append("|");
		content.append("NewEquipmentInfo:"+equipmentChangeInfo.getNewEquipmentInfo()).append("|");
		content.append("DealerCode:"+equipmentChangeInfo.getDealerCode()).append("|");
		content.append("SalesRepCode:"+equipmentChangeInfo.getSalesRepCode()).append("|");
		content.append("RequestorId:"+equipmentChangeInfo.getRequestorId()).append("|");
		content.append("RepairId:"+equipmentChangeInfo.getRepairId()).append("|");
		content.append("SwapType:"+equipmentChangeInfo.getSwapType()).append("|");
		content.append("AssociatedMuleEquipmentInfo:"+equipmentChangeInfo.getAssociatedMuleEquipmentInfo()).append("|");
		content.append("ApplicationName:"+equipmentChangeInfo.getApplicationName()).append("|");
		return content;
	}

		
	
	@Override
	public FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutProcess = super.createFalloutData(messageInfo);
		falloutProcess.setServiceName(FalloutProcessInfo.SERVICE_NAME_SUBSCRIBERLIFECYCLEFACADE+";"+messageInfo.getMessageType()+";"+messageInfo.getMessageType()+";"+messageInfo.getMessageSubType());
		return falloutProcess;
	}

	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {	
		return AppConfiguration.isConfigurationManagerFalloutOn();
	}
	
	
	
}
