package com.telus.cmb.subscriber.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelperTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class SubscriberHelperShakedown extends AbstractEjbShakedown<SubscriberLifecycleHelperTestPoint> implements SubscriberLifecycleHelperTestPoint {
	
	public SubscriberHelperShakedown() {
		super(SUBSCRIBER_LIFECYCLE_HELPER_TESTPOINT);
	}

	@Override
	public void testDataSources() {
		testCodsDataSource();
		testConeDataSource();
		testDistDataSource();
		testEasDataSource();
		testEcpcsDataSource();
		testKnowbilityDataSource();
		testServDataSource();
	}

	@Override
	public void testWebServices() {
		testSubscriptionService();
	}

	@Override
	public void testAmdocs() {}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getSubscriberPrefPkgVersion();
		getSubscriberPkgVersion();
		getSubRetrievalPkgVersion();
		getSubAttrbRetrievalpkgVersion();
		getHistoryUtilityPkgVersion();
		getMemoUtilityPkgVersion();
		getFleetUtilityPkgVersion();
	}

	@Override
	public TestPointResultInfo testKnowbilityDataSource() {
		return executeTest("KB Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testKnowbilityDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testEcpcsDataSource() {
		return executeTest("ECPCS Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEcpcsDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testCodsDataSource() {
		return executeTest("CODS Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCodsDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testServDataSource() {
		return executeTest("SERV Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testServDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testDistDataSource() {
		return executeTest("DIST Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testDistDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testEasDataSource() {
		return executeTest("EAS Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEasDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testConeDataSource() {
		return executeTest("CONE Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testConeDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo getSubscriberPrefPkgVersion() {
		return executeTest("Subscriber Pref PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getSubscriberPrefPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getSubscriberPkgVersion() {
		return executeTest("Subscriber PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getSubscriberPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getHistoryUtilityPkgVersion() {
		return executeTest("History Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getHistoryUtilityPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getMemoUtilityPkgVersion() {
		return executeTest("Memo Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getMemoUtilityPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getFleetUtilityPkgVersion() {
		return executeTest("Fleet Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getFleetUtilityPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getSubRetrievalPkgVersion() {
		return executeTest("SUB_RETRIEVAL_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getSubRetrievalPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getSubAttrbRetrievalpkgVersion() {
		return executeTest("SUB_ATTRIB_RETRIEVAL_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getSubAttrbRetrievalpkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo testSubscriptionService() {
		return executeTest("SubscriptionService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testSubscriptionService();
			}
		});
	}

	
}
