package com.telus.cmb.productequipment.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;


public class ProductEquipmentFacadeShakedown extends AbstractEjbShakedown<ProductEquipmentLifecycleFacadeTestPoint> implements ProductEquipmentLifecycleFacadeTestPoint {
	
	public ProductEquipmentFacadeShakedown() {
		super(PRODUCT_EQUIPMENT_LIFECYCLE_FACADE_TESTPOINT);
	}

	@Override
	public void testAmdocs(){
		//Amdocs test not required 
	}
	
	@Override
	public void testDataSources() {
	}

	@Override
	public void testWebServices() {
		testEquipmentActivationSupportService();
		testEquipmentLifeCycleManagementService();
		testEquipmentInfoService_1_0();
		testProductFacadeEJB();
		testNrtEligibilityManagerEJB("111111111111111111");
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
	}

	@Override
	public TestPointResultInfo testEquipmentLifeCycleManagementService() {
		return executeTest("EquipmentLifeCycleManagementService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEquipmentLifeCycleManagementService();
			}
		});
	}

	@Override
	public TestPointResultInfo testEquipmentActivationSupportService() {
		return executeTest("EquipmentActivationSupportService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEquipmentActivationSupportService();
			}
		});
	}
	
	@Override
	public TestPointResultInfo testEquipmentInfoService_1_0() {
		return executeTest("EquipmentInfoService_1_0", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEquipmentInfoService_1_0();
			}
		});
	}

	@Override
	public TestPointResultInfo testProductFacadeEJB() {
		return executeTest("P3MS ProductFacadeEJB", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testProductFacadeEJB();
			}
		});
	}

	@Override
	public TestPointResultInfo testNrtEligibilityManagerEJB(final String serialNumber) {
		return executeTest("NrtEligibilityManagerEJB", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testNrtEligibilityManagerEJB(serialNumber);
			}
		});
	}
	
	
}