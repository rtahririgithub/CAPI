package com.telus.cmb.utility.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.utility.queueevent.svc.QueueEventManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class QueueEventManagerShakedown extends AbstractEjbShakedown<QueueEventManagerTestPoint> implements QueueEventManagerTestPoint {

	public QueueEventManagerShakedown() {
		super(QUEUE_EVENT_MANAGER_TESTPOINT);
	}

	@Override
	public void testAmdocs(){
		//Amdocs test not required 
	}

	@Override
	public void testDataSources() {
		testCCEventsDataSource();
	}

	@Override
	public void testWebServices() {
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getccEventPkgVersion();
	}

	@Override
	public TestPointResultInfo testCCEventsDataSource() {
		return executeTest("CcEvents DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCCEventsDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo getccEventPkgVersion() {
		return executeTest("CcEvents Package", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getccEventPkgVersion();
			}
		});
	}
	
	
}
