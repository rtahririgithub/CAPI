package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;

import com.telus.api.SystemCodes;
import com.telus.cmb.common.svc.NewSvcInvocationInterceptor;

public class ProductEquipmentLifecycleFacadeSvcInvocationInterceptor extends
NewSvcInvocationInterceptor {

	@Override
	public String getSystemCode() {
		return SystemCodes.CMB_PELF_EJB;
	}

}
