package com.telus.cmb.productequipment.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.productequipment.manager.svc.ProductEquipmentManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class ProductEquipmentManagerShakedown extends AbstractEjbShakedown<ProductEquipmentManagerTestPoint> implements ProductEquipmentManagerTestPoint  {

	public ProductEquipmentManagerShakedown() {
		super(PRODUCT_EQUIPMENT_MANAGER_TESTPOINT);
	}

	@Override
	public void testAmdocs(){
		//Amdocs test not required 
	}

	@Override
	public void testDataSources() {
		testDistDataSource();
		testKnowbilityDataSource();
	}

	@Override
	public void testWebServices() {
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
	}

	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return executeTest("KB DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testKnowbilityDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return executeTest("DIST DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testDistDataSource();
			}
		});
	}
}
