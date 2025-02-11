package com.telus.cmb.reference.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.reference.svc.ReferenceDataFacadeTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class ReferenceDataFacadeShakedown extends AbstractEjbShakedown<ReferenceDataFacadeTestPoint> implements ReferenceDataFacadeTestPoint {

	public ReferenceDataFacadeShakedown() {
		super(REFERENCE_DATA_FACADE_TESTPOINT);
	}

	@Override
	public void testDataSources() {
	}

	@Override
	public void testWebServices() {
	}

	@Override
	public void testOtherApi() {
		testRefPds();
	}

	@Override
	public void testPackages() {
	}

	@Override
	public TestPointResultInfo testRefPds() {
		return executeTest("RefPDS System", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testRefPds();
			}
		});
	}
	
}
