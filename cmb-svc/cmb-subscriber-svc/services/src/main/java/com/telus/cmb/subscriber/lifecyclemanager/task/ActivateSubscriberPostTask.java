package com.telus.cmb.subscriber.lifecyclemanager.task;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class ActivateSubscriberPostTask extends SubscriberTask {

	public ActivateSubscriberPostTask(BaseChangeInfo changeInfo) throws SystemException, ApplicationException {
		super(changeInfo);
	}

	@Override
	public void execute() throws ApplicationException {
		applyPromotionalDiscount();	
	}

}
