package com.telus.cmb.account.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.framework.info.BillingAccountEnterpriseDataInfo;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.cmb.account.utilities.AppConfiguration;

public class BillingAccountEnterpriseDataSyncLogHandler extends AccountManagementMessageLogHandler<MessageInfo<BillingAccountEnterpriseDataInfo>> {

	@Override
	public void writeLog(MessageInfo<BillingAccountEnterpriseDataInfo> messageInfo) {
		BillingAccountEnterpriseDataInfo message = messageInfo.getMessageObject();
		StringBuffer content = new StringBuffer();
		content.append(message.getMessageType()).append("|");
		content.append(message.getBillingAccountNumber()).append("|");
		content.append(message.getCustomerID()).append("|");
		content.append(message.getProcessType()).append("|");
		messageInfo.writeLog(content.toString());
	}

	@Override
	protected FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutInfo = super.createFalloutData(messageInfo);
		BillingAccountEnterpriseDataInfo message = (BillingAccountEnterpriseDataInfo) messageInfo.getMessageObject();
		falloutInfo.setApplicationId(FalloutProcessInfo.APPID_CLIENTAPI_EJB+";"+FalloutProcessInfo.APPID_DATA_SERVICE);
		
		String correlationId = String.valueOf(message.getBillingAccountNumber());
		if (message.getAccountInfo() != null) {
			AccountInfo accountInfo = message.getAccountInfo();
			correlationId += ";"+ accountInfo.getAccountType()+";"+accountInfo.getAccountSubType()+";";
		}
		falloutInfo.setCorrelationId(correlationId);
		falloutInfo.setCustomerId(message.getCustomerID());
		falloutInfo.setServiceName(FalloutProcessInfo.DATA_SERVICE_CONSUMER_BILLINGACCOUNT+";"+message.getProcessType());
		
		return falloutInfo;
	}

	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {
		return AppConfiguration.isBillingAccountDataSyncFalloutOn();
	}


}
