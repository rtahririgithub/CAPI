package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.framework.info.ProductEnterpriseDataInfo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;

public class ProductEnterpriseDataSyncLogHandler extends SubscriberManagementMessageLogHandler<MessageInfo<ProductEnterpriseDataInfo>> {

	@Override
	public void writeLog(MessageInfo<ProductEnterpriseDataInfo> messageInfo) {
		ProductEnterpriseDataInfo message = messageInfo.getMessageObject();
		StringBuffer content = new StringBuffer();
		content.append(message.getMessageType()).append("|");
		content.append(message.getBillingAccountNumber()).append("|");
		content.append(message.getSubscriberId()).append("|");
		content.append(message.getProcessType()).append("|");
		messageInfo.writeLog(content.toString());
	}
	
	@Override
	public FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutInfo = super.createFalloutData(messageInfo);
		ProductEnterpriseDataInfo message = (ProductEnterpriseDataInfo) messageInfo.getMessageObject();
		
		falloutInfo.setApplicationId(FalloutProcessInfo.APPID_CLIENTAPI_EJB+";"+FalloutProcessInfo.APPID_DATA_SERVICE);
		falloutInfo.setCorrelationId(String.valueOf(message.getBillingAccountNumber()));

		if (message.getEquipmentInfo() != null) {
			EquipmentInfo equipment = message.getEquipmentInfo();
			falloutInfo.setResourceId(equipment.getSerialNumber()+";"+equipment.getEquipmentGroup());
		}
		falloutInfo.setServiceTelephoneNumber(message.getPhoneNumber());
		falloutInfo.setServiceName(FalloutProcessInfo.DATA_SERVICE_CONSUMER_PRODUCT+";"+message.getProcessType());
		falloutInfo.setOrderNumber(message.getSubscriberId());
		
		return falloutInfo;
	}

	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {
		return AppConfiguration.isProductDataSyncFalloutOn();
	
	}

}
