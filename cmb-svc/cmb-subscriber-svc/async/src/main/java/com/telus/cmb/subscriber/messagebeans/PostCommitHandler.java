package com.telus.cmb.subscriber.messagebeans;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageBeanContext;
import com.telus.cmb.common.jms.DelegatingMessageDrivenBean.MessageHandler;
import com.telus.eas.framework.info.PostCommitInfo;

public class PostCommitHandler implements MessageHandler<PostCommitInfo> {
	private static final Log logger = LogFactory.getLog(PostCommitHandler.class);

	@Override
	public void handle(PostCommitInfo postCommitInfo, ClientIdentity clientIdentity, MessageBeanContext beanContext) throws ApplicationException {
		logger.info("removed the PostCommitHandler code part of ECNMS decommissioning project");
	}

	
}
