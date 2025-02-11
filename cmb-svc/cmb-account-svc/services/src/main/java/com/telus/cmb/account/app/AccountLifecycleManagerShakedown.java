package com.telus.cmb.account.app;

import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManagerTestPoint;
import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.eas.framework.info.TestPointResultInfo;


public class AccountLifecycleManagerShakedown extends AbstractEjbShakedown<AccountLifecycleManagerTestPoint> implements AccountLifecycleManagerTestPoint {
	
	public AccountLifecycleManagerShakedown() {
		super(ACCOUNT_LIFECYCLE_MANAGER_TESTPOINT);
	}

	@Override
	public void testDataSources() {
		testCodsDataSource();
	}

	@Override
	public void testWebServices() {
		testPrepaidWirelessCustomerOrderService();
		testPrepaidSubscriberService();
		testSubscriptionBalanceMgmtService();
		testSubscriptionManagementService();
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
	public TestPointResultInfo testSubscriptionBalanceMgmtService() {
		return executeTest("SubscriptionBalanceMgmtService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testSubscriptionBalanceMgmtService();
			}
		});
	}

	@Override
	public TestPointResultInfo testSubscriptionManagementService() {
		return executeTest("SubscriptionManagementService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testSubscriptionManagementService();
			}
		});
	}

}
