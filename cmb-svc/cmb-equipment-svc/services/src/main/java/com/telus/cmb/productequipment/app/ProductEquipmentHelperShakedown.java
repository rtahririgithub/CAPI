package com.telus.cmb.productequipment.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.productequipment.helper.svc.ProductEquipmentHelperTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class ProductEquipmentHelperShakedown extends AbstractEjbShakedown<ProductEquipmentHelperTestPoint> implements ProductEquipmentHelperTestPoint {

	public ProductEquipmentHelperShakedown() {
		super(PRODUCT_EQUIPMENT_HELPER_TESTPOINT);
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
		testProductOfferingService();
		testVoucherValidationService();
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getClientEquipmentPkgVersion();
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

	@Override
	public TestPointResultInfo getClientEquipmentPkgVersion() {
		return executeTest("Client Equipment PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getClientEquipmentPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo testProductOfferingService() {
		return executeTest("ProductOfferingService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testProductOfferingService();
			}
		});
	}

	@Override
	public TestPointResultInfo testVoucherValidationService() {
		return executeTest("VoucherValidationService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testVoucherValidationService();
			}
		});
	}

}

