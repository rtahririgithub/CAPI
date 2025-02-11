package com.telus.cmb.account.messagebeans;

import org.apache.commons.lang.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.jms.BaseMessageDrivenBean.MessageInfo;
import com.telus.eas.framework.info.FalloutProcessInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.cmb.account.utilities.AppConfiguration;

public class MemoCreationLogHandler extends AccountManagementMessageLogHandler<MessageInfo<MemoInfo>> {

	@Override
	public void writeLog(MessageInfo<MemoInfo> messageInfo) {
		MemoInfo memoInfo = messageInfo.getMessageObject();
		StringBuffer content = new StringBuffer();
		content.append(memoInfo.getBanId()).append("|");
		content.append(memoInfo.getMemoType()).append("|");
		checkBeforeAppend(content, memoInfo.getSubscriberId());
		checkBeforeAppend(content, memoInfo.getProductType());
		content.append(messageInfo.getDateFormat().format(memoInfo.getDate())).append("|");
		messageInfo.writeLog(content.toString());
	}
	
	
	
	@Override
	protected FalloutProcessInfo createFalloutData(MessageInfo<Object> messageInfo) {
		FalloutProcessInfo falloutProcess = super.createFalloutData(messageInfo);
		MemoInfo memoInfo = (MemoInfo) messageInfo.getMessageObject();
		falloutProcess.setCorrelationId(String.valueOf(memoInfo.getBanId()));
		if (StringUtils.isNotBlank(memoInfo.getSubscriberId())) {
			falloutProcess.setServiceTelephoneNumber(memoInfo.getSubscriberId());
		}
		
		return falloutProcess;
	}



	private void checkBeforeAppend(StringBuffer content, String value) {
		if (StringUtils.isNotBlank(value))
			content.append(value).append("|");
	}


	@Override
	public boolean isFalloutFlagOn() throws ApplicationException {
		return AppConfiguration.isMemoCreationFalloutOn();
	}
	
}
