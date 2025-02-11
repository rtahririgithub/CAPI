package com.telus.cmb.productequipment.manager.svc;

import com.telus.eas.framework.info.TestPointResultInfo;

public interface ProductEquipmentManagerTestPoint {
	TestPointResultInfo testKnowbilityDataSource();
	TestPointResultInfo testDistDataSource();
	String getVersion();
}
