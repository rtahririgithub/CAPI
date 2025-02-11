package com.telus.cmb.account.informationhelper.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class AccountInformationHelperSvcInvocationInterceptor extends
		NewSvcInvocationInterceptor {

	
	public String getSystemCode() {
		return SystemCodes.CMB_AIH_EJB;
	}

}
