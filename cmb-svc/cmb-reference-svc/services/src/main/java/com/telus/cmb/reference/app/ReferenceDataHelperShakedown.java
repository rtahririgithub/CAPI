package com.telus.cmb.reference.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.reference.svc.ReferenceDataHelperTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class ReferenceDataHelperShakedown extends AbstractEjbShakedown<ReferenceDataHelperTestPoint> implements ReferenceDataHelperTestPoint {

	public ReferenceDataHelperShakedown() {
		super(REFERENCE_DATA_HELPER_TESTPOINT);
	}

	@Override
	public void testDataSources() {
		testCodsDataSource();
		testConeDataSource();
		testDistDataSource();
		testEasDataSource();
		testEcpcsDataSource();
		testKnowbilityDataSource();
		testRefDataSource();
		testEmcmDataSource();
	}

	@Override
	public void testWebServices() {
		testProductOfferingService();
		testWsoisService();
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getPricePlanUtilityPkgVersion();
		getReferencePkgVersion();
		getRuleUtilityPkgVersion();
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
	public TestPointResultInfo testEcpcsDataSource() {
		return executeTest("ECPCS DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEcpcsDataSource();
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

	@Override
	public TestPointResultInfo testEmcmDataSource() {
		return executeTest("EMCM DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEmcmDataSource();
			}
		});
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
	public TestPointResultInfo testEasDataSource() {
		return executeTest("EAS DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEasDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testRefDataSource() {
		return executeTest("REF DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testRefDataSource();
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
	public TestPointResultInfo testWsoisService() {
		return executeTest("WirelessSubscriberOfferInformationService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testWsoisService();
			}
		});
	}

	@Override
	public TestPointResultInfo getPricePlanUtilityPkgVersion() {
		return executeTest("PricePlanUtility Pkg", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getPricePlanUtilityPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getReferencePkgVersion() {
		return executeTest("Reference Pkg", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getReferencePkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getRuleUtilityPkgVersion() {
		return executeTest("RuleUtility Pkg", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getRuleUtilityPkgVersion();
			}
		});
	}
}
