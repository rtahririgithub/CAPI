package com.telus.cmb.utility.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.utility.contacteventmanager.svc.ContactEventManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class ContactEventManagerShakedown extends AbstractEjbShakedown<ContactEventManagerTestPoint> implements ContactEventManagerTestPoint {

	public ContactEventManagerShakedown() {
		super(CONTACT_EVENT_MANAGER_TESTPOINT);
	}

	@Override
	public void testAmdocs() {
	}

	@Override
	public void testDataSources() {
		testCodsDataSource();
		testConeDataSource();
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
	public TestPointResultInfo testCodsDataSource() {
		return executeTest("CODS DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCodsDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testConeDataSource() {
		return executeTest("CONE DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testConeDataSource();
			}
		});
	}
	
	
}
