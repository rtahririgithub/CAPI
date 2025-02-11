package com.telus.cmb.productequipment.helper.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ProductEquipmentHelperSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_PEH_EJB;
	}

}
