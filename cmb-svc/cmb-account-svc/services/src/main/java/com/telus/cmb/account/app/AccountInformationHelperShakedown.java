package com.telus.cmb.account.app;

import com.telus.cmb.account.informationhelper.svc.AccountInformationHelperTestPoint;
import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.eas.framework.info.TestPointResultInfo;


public class AccountInformationHelperShakedown extends AbstractEjbShakedown<AccountInformationHelperTestPoint> implements AccountInformationHelperTestPoint {

	public AccountInformationHelperShakedown() {
		super(ACCOUNT_INFORMATION_HELPER_TESTPOINT);
	}
	
	@Override
	public void testAmdocs(){
	}

	@Override
	public void testDataSources() {
		testCodsDataSource();
		testConeDataSource();
		testDistDataSource();
		testEasDataSource();
		testEcpcsDataSource();
		testKnowbilityDataSource();
	}

	@Override
	public void testWebServices() {
		testPrepaidWirelessCustomerOrderService();
		testPrepaidSubscriberService();
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getRaUtilityPkgVersion();
		getSubscriberPkgVersion();
		getMemoUtilityPkgVersion();
		getUsageUtilityPkgVersion();
		getHistoryUtilityPkgVersion();
		getPortalNotificationPkgVersion();
		getFleetUtilityPkgVersion();
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
	public TestPointResultInfo getRaUtilityPkgVersion() {
		return executeTest("RA Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getRaUtilityPkgVersion();
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
	public TestPointResultInfo getMemoUtilityPkgVersion() {
		return executeTest("Memo Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getMemoUtilityPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getUsageUtilityPkgVersion() {
		return executeTest("Usage Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getUsageUtilityPkgVersion();
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
	public TestPointResultInfo getPortalNotificationPkgVersion() {
		return executeTest("Portal Notification PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getPortalNotificationPkgVersion();
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
	public TestPointResultInfo getClientEquipmentPkgVersion() {
		return executeTest("Client Equipment PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getClientEquipmentPkgVersion();
			}
		});
	}

	/*
	 * TODO 
	 * Remove this method after April/2014 release Surepay Retirement
	 */

	@Override
	public TestPointResultInfo testPrepaidWirelessCustomerOrderService() {
		return executeTest("PrepaidWirelessCustomerOrderService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPrepaidWirelessCustomerOrderService();
			}
		});
	}

	@Override
	public TestPointResultInfo testPrepaidSubscriberService() {
		return executeTest("PrepaidSubscriberService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPrepaidSubscriberService();
			}
		});
	}

	@Override
	public TestPointResultInfo getCreditCheckResultPkgVersion() {
		return executeTest("CRDCHECK_RESULT_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getCreditCheckResultPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getAccRetrievalPkgVersion() {
		return executeTest("ACC_RETRIEVAL_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getAccRetrievalPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getAccAttribRetrievalPkgVersion() {
		return executeTest("ACC_ATTRIB_RETRIEVAL_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getAccAttribRetrievalPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getSubscriberCountPkgVersion() {
		return executeTest("SUB_COUNT_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getSubscriberCountPkgVersion();
			}
		});
	}

	@Override
	public TestPointResultInfo getInvoicePkgVersion() {
		return executeTest("INVOICE_PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getInvoicePkgVersion();
			}
		});
	}
	
	
}
