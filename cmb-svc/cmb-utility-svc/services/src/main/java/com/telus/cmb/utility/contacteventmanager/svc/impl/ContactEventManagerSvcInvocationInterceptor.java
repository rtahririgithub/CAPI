package com.telus.cmb.utility.contacteventmanager.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ContactEventManagerSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_CEM_EJB;
	}

}
