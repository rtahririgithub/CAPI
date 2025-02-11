package com.telus.cmb.utility.activitylogging.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ActivityLoggingServiceSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_ALS_EJB;
	}

}
