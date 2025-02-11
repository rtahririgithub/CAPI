package com.telus.cmb.utility.queueevent.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class QueueEventManagerSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_QEM_EJB;
	}

}
