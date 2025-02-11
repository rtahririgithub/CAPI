package com.telus.cmb.account.app;

import com.telus.cmb.account.lifecyclefacade.svc.AccountLifecycleFacadeTestPoint;
import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.eas.framework.info.TestPointResultInfo;

public class AccountLifecycleFacadeShakedown extends AbstractEjbShakedown<AccountLifecycleFacadeTestPoint> implements AccountLifecycleFacadeTestPoint {

	public AccountLifecycleFacadeShakedown() {
		super(ACCOUNT_LIFECYCLE_FACADE_TESTPOINT);
	}

	@Override
	public void testDataSources() {
		testCconDataSource();
	}

	@Override
	public void testWebServices() {
		testCreditProfileService();
//		testFalloutProcessService();
		testEnterpriseAddressValidationService();
		//testConsumerBillingAccountDataManagementService();
		testSummaryDataServicesUsageService();
		testCardPaymentService();
		testEnterpriseConsumerProfileRegistrationService();
		testBillNotificationManagementService();
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
	}

	@Override
	public TestPointResultInfo testCconDataSource() {
		return executeTest("CCON Data Source", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCconDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testEnterpriseAddressValidationService() {
		return executeTest("EnterpriseAddressValidationService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEnterpriseAddressValidationService();
			}
		});
	}

	@Override
	public TestPointResultInfo testConsumerBillingAccountDataManagementService() {
		return executeTest("ConsumerBillingAccountDataManagementService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testConsumerBillingAccountDataManagementService();
			}
		});
	}

	@Override
	public TestPointResultInfo testCreditProfileService() {
		return executeTest("CreditProfileService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCreditProfileService();
			}
		});
	}

	@Override
	public TestPointResultInfo testSummaryDataServicesUsageService() {
		return executeTest("SummaryDataServicesUsageService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testSummaryDataServicesUsageService();
			}
		});
	}

	@Override
	public TestPointResultInfo testWirelessProvisioningService() {
		return executeTest("WirelessProvisioningService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testWirelessProvisioningService();
			}
		});
	}
	
	@Override
	public TestPointResultInfo testCardPaymentService() {
		return executeTest("CardPaymentService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCardPaymentService();
			}
		});
	}

	@Override
	public TestPointResultInfo testEnterpriseConsumerProfileRegistrationService() {
		return executeTest("EnterpriseConsumerProfileRegistrationService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEnterpriseConsumerProfileRegistrationService();
			}
		});
	}

	@Override
	public TestPointResultInfo testBillNotificationManagementService() {
		return executeTest("BillNotificationManagementService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testBillNotificationManagementService();
			}
		});
	}

	@Override
	public TestPointResultInfo testPortalProfileMgmtService() {
		return executeTest("PortalProfileMgmtService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPortalProfileMgmtService();
			}
		});
	}
}
