package com.telus.cmb.productequipment.lifecyclefacade.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductEquipmentLifecycleFacadeTestPoint {
	TestPointResultInfo testEquipmentActivationSupportService();
	TestPointResultInfo testEquipmentLifeCycleManagementService();
    TestPointResultInfo testEquipmentInfoService_1_0();
    TestPointResultInfo testProductFacadeEJB();
    TestPointResultInfo testNrtEligibilityManagerEJB (String serialNumber);
    String getVersion();
}
 