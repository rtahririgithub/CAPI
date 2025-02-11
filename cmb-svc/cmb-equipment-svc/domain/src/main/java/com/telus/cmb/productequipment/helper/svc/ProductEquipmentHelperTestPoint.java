package com.telus.cmb.productequipment.helper.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductEquipmentHelperTestPoint {
	TestPointResultInfo testKnowbilityDataSource();
	TestPointResultInfo testDistDataSource();
    TestPointResultInfo testProductOfferingService();
    TestPointResultInfo testVoucherValidationService();
	TestPointResultInfo getClientEquipmentPkgVersion();
	String getVersion();
}
