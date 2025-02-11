package com.telus.cmb.subscriber.lifecyclefacade.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface SubscriberLifecycleFacadeTestPoint {
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	TestPointResultInfo testLogicalResourceService();
	TestPointResultInfo testMinMdnService();
	TestPointResultInfo testEligibilityCheckService();
	TestPointResultInfo testCancelPortInRequestSoap();
	TestPointResultInfo testActivatePortInRequestService();
	TestPointResultInfo testCreatePortInRequestService();
    TestPointResultInfo testSubmitPortInRequestService();
	TestPointResultInfo testPortRequestInformationService();
    TestPointResultInfo testConsumerProductDataManagementService();
    TestPointResultInfo testProvisioningOrderLookup(String transactionNo, String subscriberId);
    String getVersion();
    TestPointResultInfo testPortabilityService();
    TestPointResultInfo testHardwarePurchaseAccountService();
    TestPointResultInfo testWirelessProvisioningService();
    TestPointResultInfo testVOIPSupplementaryService();
    TestPointResultInfo testPenaltyCalculationService();
    TestPointResultInfo testEnterpriseConsumerProfileRegistrationService();
    TestPointResultInfo testCommunicationSuiteMgmtRestService();
    TestPointResultInfo testCommunicationSuiteMgmtService();


}
