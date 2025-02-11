package com.telus.cmb.utility.configurationmanager.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ConfigurationMnagerSvcIncovationInterceptor extends NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_CFGMGR_EJB;
	}

}
