package com.telus.cmb.subscriber.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.subscriber.lifecyclefacade.svc.SubscriberLifecycleFacadeTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;

public class SubscriberFacadeShakedown extends AbstractEjbShakedown<SubscriberLifecycleFacadeTestPoint> implements SubscriberLifecycleFacadeTestPoint {

	public SubscriberFacadeShakedown() {
		super(SUBSCRIBER_LIFECYCLE_FACADE_TESTPOINT);
	}

	@Override
	public void testDataSources() {}

	@Override
	public void testWebServices() {
		testConsumerProductDataManagementService();
		testLogicalResourceService();
		testMinMdnService();
		//testFallout();
		testProvisioningOrderLookup("123", "4161234567");
		testCancelPortInRequestSoap();
		testSubmitPortInRequestService();
		testPortabilityService();
		testHardwarePurchaseAccountService();
		testWirelessProvisioningService();
		testVOIPSupplementaryService();
		testPenaltyCalculationService();
		testEnterpriseConsumerProfileRegistrationService();
		testCommunicationSuiteMgmtService();
		testCommunicationSuiteMgmtRestService();
		
	}

	@Override
	public void testOtherApi() {}

	@Override
	public void testPackages() {}

	@Override
	public TestPointResultInfo testConsumerProductDataManagementService() {
		return executeTest("ConsumerProductDataManagementService", new TestPointExecutionCallback() {	
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testConsumerProductDataManagementService();
			}
		});
	}


	@Override
	public TestPointResultInfo testLogicalResourceService() {
		return executeTest("LogicalResource Service",new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testLogicalResourceService();
			}
		});
	}

	@Override
	public TestPointResultInfo testMinMdnService() {
		return executeTest("MinMdn Service",new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testMinMdnService();
			}
		});
	}


	@Override
	public TestPointResultInfo testEligibilityCheckService() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TestPointResultInfo testCancelPortInRequestSoap() {
		return executeTest("CancelPortInRequest Service", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCancelPortInRequestSoap();
			}
		});
	}

	@Override
	public TestPointResultInfo testPortRequestInformationService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestPointResultInfo testActivatePortInRequestService() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TestPointResultInfo testCreatePortInRequestService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestPointResultInfo testSubmitPortInRequestService() {
		return executeTest("SubmitPortInRequest Service", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testSubmitPortInRequestService();
			}
		});
	}


	@Override
	public TestPointResultInfo testProvisioningOrderLookup(final String transactionNo, final String subscriberId) {
		return executeTest("ProvisioningOrderLookup Service", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testProvisioningOrderLookup(transactionNo ,subscriberId);
			}
		});
	}

	@Override
	public TestPointResultInfo testPortabilityService() {
		return executeTest("PortabilityService Service", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPortabilityService();
			}
		});
	}

	@Override
	public TestPointResultInfo testHardwarePurchaseAccountService() {
		return executeTest("HardwarePurchaseAccount Service", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testHardwarePurchaseAccountService();
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
	public TestPointResultInfo testVOIPSupplementaryService() {
		return executeTest("VoipSupplementaryService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testVOIPSupplementaryService();
			}
		});
	}
	
	@Override
	public TestPointResultInfo testPenaltyCalculationService() {
		return executeTest("PenaltyCalculationService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testPenaltyCalculationService();
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
	public TestPointResultInfo testCommunicationSuiteMgmtRestService() {
		return executeTest("CommunicationSuiteMgmtIntRestService", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCommunicationSuiteMgmtRestService();
			}
		});
	}

	
	@Override
	public TestPointResultInfo testCommunicationSuiteMgmtService() {
		return executeTest("CommunicationSuiteMgmtRESTService(Kong)", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testCommunicationSuiteMgmtService();
			}
		});
	}
	

}
