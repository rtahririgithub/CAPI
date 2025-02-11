package com.telus.cmb.productequipment.manager.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ProductEquipmentManagerSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_PEM_EJB;
	}

}
