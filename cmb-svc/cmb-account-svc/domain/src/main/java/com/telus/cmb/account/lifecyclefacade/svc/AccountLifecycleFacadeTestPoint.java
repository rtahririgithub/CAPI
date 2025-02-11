package com.telus.cmb.account.lifecyclefacade.svc;

import com.telus.api.ApplicationException;
import com.telus.eas.framework.info.TestPointResultInfo;

public interface AccountLifecycleFacadeTestPoint {
	TestPointResultInfo testCconDataSource();
	String openSession(String userId, String password, String applicationId) throws ApplicationException;
	TestPointResultInfo testEnterpriseAddressValidationService();
	TestPointResultInfo testConsumerBillingAccountDataManagementService();
    TestPointResultInfo testCreditProfileService();
    String getVersion();
    TestPointResultInfo testSummaryDataServicesUsageService();
    TestPointResultInfo testWirelessProvisioningService();
	TestPointResultInfo testCardPaymentService();
	TestPointResultInfo testEnterpriseConsumerProfileRegistrationService();
	TestPointResultInfo testBillNotificationManagementService();
	TestPointResultInfo testPortalProfileMgmtService();
}
