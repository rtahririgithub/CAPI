package com.telus.cmb.subscriber.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class SubscriberManagerShakedown extends AbstractEjbShakedown<SubscriberLifecycleManagerTestPoint> implements SubscriberLifecycleManagerTestPoint {

	public SubscriberManagerShakedown() {
		super(SUBSCRIBER_LIFECYCLE_MANAGER_TESTPOINT);
	}
	
	@Override
	public void testDataSources() {
		testCodsDataSource();
	}

	@Override
	public void testWebServices() {
		testPrepaidSubscriberService();
		testPrepaidWirelessCustomerOrderService();
		testOrderService();
		testSubscriptionService();
	}

	public void testOtherApi() {
	}

	@Override
	public void testPackages() {}

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
	public TestPointResultInfo testPrepaidSubscriberService() {
		return executeTest("PrepaidSubscriptionService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPrepaidSubscriberService();
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
	public TestPointResultInfo testOrderService() {
		return executeTest("OrderService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testOrderService();
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
