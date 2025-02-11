package com.telus.cmb.utility.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.utility.dealermanager.svc.DealerManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class DealerManagerShakedown extends AbstractEjbShakedown<DealerManagerTestPoint> implements DealerManagerTestPoint {

	public DealerManagerShakedown() {
		super(DEALER_MANAGER_TESTPOINT);
	}

	@Override
	public void testDataSources() {
		testDistDataSource();
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
	public TestPointResultInfo testDistDataSource() {
		return executeTest("DIST DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testDistDataSource();
			}
		});
	}
	
	
}
