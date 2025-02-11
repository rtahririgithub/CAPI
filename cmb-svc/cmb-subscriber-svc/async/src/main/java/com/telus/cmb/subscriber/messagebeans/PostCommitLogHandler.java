package com.telus.cmb.subscriber.messagebeans;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.framework.info.PostCommitInfo;
import com.telus.cmb.subscriber.utilities.AppConfiguration;

public class PostCommitLogHandler extends SubscriberManagementMessageLogHandler<MessageInfo<PostCommitInfo>> {

	@Override
	public void writeLog(MessageInfo<PostCommitInfo> messageInfo) {
		PostCommitInfo postCommitInfo = messageInfo.getMessageObject();
		StringBuffer content = new StringBuffer();
		content.append(postCommitInfo.getProcessType()).append("|");
		content.append(postCommitInfo.getBan()).append("|");
		content.append(postCommitInfo.getSubscriberId()).append("|");
		messageInfo.writeLog(content.toString());
	}

	@Override
	public FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutProcess = super.createFalloutData(messageInfo);
		PostCommitInfo postCommitInfo = (PostCommitInfo) messageInfo.getMessageObject();
		
		falloutProcess.setCorrelationId(String.valueOf(postCommitInfo.getBan()));
		falloutProcess.setServiceTelephoneNumber(postCommitInfo.getPhoneNumber());
		falloutProcess.setOrderNumber(postCommitInfo.getSubscriberId()+";"+postCommitInfo.getExternalId());
		falloutProcess.setServiceName(FalloutProcessInfo.SERVICE_NAME_SUBSCRIBERLIFECYCLEFACADE+";"+postCommitInfo.getProcessType());
		
		return falloutProcess;
	}

	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {
		return AppConfiguration.isPostCommitFalloutOn();
	}
	
	
}
