package com.telus.cmb.subscriber.messagebeans;

import java.util.Collection;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.eas.activitylog.domain.ChangeAccountAddressActivity;
import com.telus.eas.activitylog.domain.ChangeAccountTypeActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity;
import com.telus.eas.activitylog.domain.ChangeContractActivity.FeatureHolder;
import com.telus.eas.activitylog.domain.ChangeContractActivity.ServiceHolder;
import com.telus.eas.activitylog.domain.ChangeEquipmentActivity;
import com.telus.eas.activitylog.domain.ChangePaymentMethodActivity;
import com.telus.eas.activitylog.domain.ChangePhoneNumberActivity;
import com.telus.eas.activitylog.domain.ChangeSubscriberStatusActivity;
import com.telus.eas.activitylog.domain.MoveSubscriberActivity;
import com.telus.eas.activitylog.queue.info.ActivityLoggingInfo;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;

public class ActivityLoggingServiceLogHandler extends SubscriberManagementMessageLogHandler<MessageInfo<ActivityLoggingInfo>> {

	
	@Override
	public void writeLog(MessageInfo<ActivityLoggingInfo> messageInfo) {
		ActivityLoggingInfo activityLoggingInfo = messageInfo.getMessageObject();
		String messageType=activityLoggingInfo.getMessageType();
		
		StringBuffer content = new StringBuffer();
		content.append("Message Type: "+messageType).append("|");
		content.append("BanId: "+activityLoggingInfo.getBanId()).append("|");
		content.append("DealerCode: "+activityLoggingInfo.getDealerCode()).append("|");
		content.append("SalesRepCode: "+activityLoggingInfo.getSalesRepCode()).append("|");
		content.append("UserId: "+activityLoggingInfo.getUserId()).append("|");
		
		if(messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_SUBSCRIBER_STATUS)){
			content.append(changeSubscriberStatusActivityLogContent(activityLoggingInfo, messageInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_EQUIPMENT)){
			content.append(ChangeEquipmentActivityLogContent(activityLoggingInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_CONTRACT)){
			content.append(ChangeContractActivityLogContent(activityLoggingInfo, messageInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_PHONE_NUMBER)){
			content.append(ChangePhoneNumberActivityLogContent(activityLoggingInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_MOVE_SUBSCRIBER)){
			content.append(MoveSubscriberActivityLogContent(activityLoggingInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_TYPE)){
			content.append(ChangeAccountTypeActivityLogContent(activityLoggingInfo));
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_ADDRESS)){
			content.append("Address: "+((ChangeAccountAddressActivity)activityLoggingInfo).getAddress().toString()).append("|");
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_ACCOUNT_PIN)){
			//	nothing to add
		}else if (messageType.equals(ActivityLoggingInfo.MESSAGE_TYPE_CHANGE_PAYMENT_METHOD)){
			content.append("Payment Method: "+((ChangePaymentMethodActivity)activityLoggingInfo).getPaymentMethodCode()).append("|");
		}	
		
		messageInfo.writeLog(content.toString());
		
	}
	
	private StringBuffer changeSubscriberStatusActivityLogContent(ActivityLoggingInfo activityLoggingInfo,MessageInfo<ActivityLoggingInfo> messageInfo ){
		StringBuffer content =new StringBuffer();
		ChangeSubscriberStatusActivity changeSubscriberStatusActivity=(ChangeSubscriberStatusActivity)activityLoggingInfo;
		content.append("SubscriberID: "+changeSubscriberStatusActivity.getSubscriberId()).append("|");
		content.append("Phone No: "+changeSubscriberStatusActivity.getPhoneNumber()).append("|");
		content.append("OldSubscriberStatus :"+changeSubscriberStatusActivity.getOldSubscriberStatus()).append("|");
		content.append("NewSubscriberStatus :"+changeSubscriberStatusActivity.getNewSubscriberStatus()).append("|");
		content.append("Reason: "+changeSubscriberStatusActivity.getReason()).append("|");
		content.append("SubscriberActivationDate"+messageInfo.getDateFormat().format(changeSubscriberStatusActivity.getSubscriberActivationDate())).append("|");
		content.append("SubscriberDeactivationDate"+messageInfo.getDateFormat().format(changeSubscriberStatusActivity.getSubscriberDeactivationDate())).append("|");
	
		return content;
	}
	
	private StringBuffer ChangeEquipmentActivityLogContent(ActivityLoggingInfo activityLoggingInfo){
		StringBuffer content =new StringBuffer();
		ChangeEquipmentActivity changeEquipmentActivity=(ChangeEquipmentActivity)activityLoggingInfo;
		content.append("SubscriberID: "+changeEquipmentActivity.getSubscriberId()).append("|");
		content.append("OldEquipment Serial No:"+changeEquipmentActivity.getOldEquipment().getSerialNumber()).append("|");
		content.append("NewEquipment Serial No:"+changeEquipmentActivity.getNewEquipment().getSerialNumber()).append("|");
		if (changeEquipmentActivity.getNewAssociatedMuleEquipment() != null) {
			content.append("NewAssociatedMuleEquipment Serail No: "+changeEquipmentActivity.getNewAssociatedMuleEquipment().getSerialNumber()).append("|");
		}
		if (changeEquipmentActivity.getOldAssociatedMuleEquipment() != null) {
			content.append( "OldAssociatedMuleEquipment Serail No: "+changeEquipmentActivity.getOldAssociatedMuleEquipment().getSerialNumber()).append("|");
		}
		content.append("SwapType: "+changeEquipmentActivity.getSwapType()).append("|");
		content.append("RepairId: "+changeEquipmentActivity.getRepairId()).append("|");
		return content;
	}
	
	private StringBuffer ChangeContractActivityLogContent(ActivityLoggingInfo activityLoggingInfo,MessageInfo<ActivityLoggingInfo> messageInfo ){
		StringBuffer content =new StringBuffer();
		
		ChangeContractActivity changeContractActivity=(ChangeContractActivity)activityLoggingInfo;
		content.append("SubscriberID: "+changeContractActivity.getSubscriberId()).append("|");
		// price plan has changed
		if (!changeContractActivity.getNewContract().getPricePlan().getCode().equals(
				changeContractActivity.getOldContract().getPricePlan().getCode())) {
			content.append("New Priceplan code:"+changeContractActivity.getNewContract().getPricePlan().getCode()).append("|");
			content.append("Old Priceplan code: "+changeContractActivity.getOldContract().getPricePlan().getCode()).append("|");
		}
		
		// services has changed
		
		Collection<ServiceHolder> addedServices = changeContractActivity.getAddedServices();
		Collection<ServiceHolder> removedServices = changeContractActivity.getRemovedServices();
		Collection<ServiceHolder> updatedServices = changeContractActivity.getUpdatedServices();
		if (!addedServices.isEmpty()){
			content.append("AddedServices :");
			for (ServiceHolder service : addedServices)
				content.append(service.getCode()).append("|");
		}
		if (!removedServices.isEmpty()){
			content.append("RemovedServices :");
			for (ServiceHolder service : removedServices) 
				content.append(service.getCode()).append("|");
		}
		if (!updatedServices.isEmpty()) {
			content.append("UpdatedServices :");
			for (ServiceHolder service : updatedServices) 
				content.append(service.getCode()).append("|");
		}
		// features has changed
		if (!changeContractActivity.getUpdatedFeatures().isEmpty()) {
			content.append("UpdatedFeatures :");
			for (FeatureHolder feature : (Collection<FeatureHolder>)changeContractActivity.getUpdatedFeatures()) {
				content.append(feature.getCode()).append("|");
			}
		}
		if (changeContractActivity.getNewContract().getEffectiveDate() != null) {
			content.append("NewContract EffectiveDate: "+messageInfo.getDateFormat().format(changeContractActivity.getNewContract().getEffectiveDate())).append("|");
		}
		return content;
	}
	
	private StringBuffer ChangePhoneNumberActivityLogContent(ActivityLoggingInfo activityLoggingInfo){
		
		StringBuffer content =new StringBuffer();
		ChangePhoneNumberActivity changePhoneNumberActivity=(ChangePhoneNumberActivity)activityLoggingInfo;
		content.append("SubscriberID: "+changePhoneNumberActivity.getSubscriberId()).append("|");
		content.append("New SubscriberId: "+changePhoneNumberActivity.getNewSubscriberId()).append("|");
		content.append("New PhoneNumber: "+changePhoneNumberActivity.getNewPhoneNumber()).append("|");
		content.append("Old PhoneNumber: "+changePhoneNumberActivity.getOldPhoneNumber()).append("|");
		
		return content;
	}
	
	private StringBuffer MoveSubscriberActivityLogContent(ActivityLoggingInfo activityLoggingInfo){
		
		StringBuffer content =new StringBuffer();
		MoveSubscriberActivity moveSubscriberActivity=(MoveSubscriberActivity)activityLoggingInfo;
		content.append("SubscriberID: "+moveSubscriberActivity.getSubscriberId()).append("|");
		content.append("New BAN: "+moveSubscriberActivity.getNewBanId()).append("|");
		content.append("PhoneNumber: "+moveSubscriberActivity.getPhoneNumber()).append("|");
		content.append("Reason: "+moveSubscriberActivity.getReason()).append("|");
		content.append("SubscriberActivationDate: "+moveSubscriberActivity.getSubscriberActivationDate()).append("|");
		
		return content;
	}
	
	private StringBuffer ChangeAccountTypeActivityLogContent(ActivityLoggingInfo activityLoggingInfo){
		StringBuffer content =new StringBuffer();
		ChangeAccountTypeActivity changeAccountTypeActivity=(ChangeAccountTypeActivity)activityLoggingInfo;
		content.append("Old AccountType: "+changeAccountTypeActivity.getOldAccountType()).append("|");
		content.append("Old AccountSubType: "+changeAccountTypeActivity.getOldAccountSubType()).append("|");
		content.append("New AccountType: "+changeAccountTypeActivity.getNewAccountType()).append("|");
		content.append("New AccountSubType: "+changeAccountTypeActivity.getNewAccountSubType()).append("|");
		
		return content;
	}

	@Override
	public FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutProcess = super.createFalloutData(messageInfo);
		
		ActivityLoggingInfo activityLoggingInfo =(ActivityLoggingInfo) messageInfo.getMessageObject();
		falloutProcess.setCorrelationId(String .valueOf(activityLoggingInfo.getBanId()));
		falloutProcess.setCustomerId(activityLoggingInfo.getSubscriberId());
		falloutProcess.setServiceName(FalloutProcessInfo.SERVICE_NAME_SUBSCRIBERLIFECYCLEFACADE+";"+activityLoggingInfo.getMessageType());
		falloutProcess.setOrderNumber(activityLoggingInfo.getSubscriberId());
		return falloutProcess;
	}

	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {
		return AppConfiguration.isSRPDSLoggingFalloutOn();
	}
	
	
	/*
	private void checkBeforeAppend(StringBuffer content, String value) {
		if (StringUtils.isNotBlank(value))
			content.append(value).append("|");
	}
	*/
	
}
