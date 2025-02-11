package com.telus.cmb.subscriber.utilities;

import com.telus.api.ApplicationException;
import com.telus.api.SystemException;
import com.telus.eas.subscriber.info.BaseChangeInfo;

public class AsyncSubscriberCommitContext extends BaseChangeContext<BaseChangeInfo> {
	
	public AsyncSubscriberCommitContext(BaseChangeInfo baseInfo) throws SystemException, ApplicationException {
		super(baseInfo);
	}

}
