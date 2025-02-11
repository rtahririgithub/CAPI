package com.telus.cmb.subscriber.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.portability.PortInEligibility;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.subscriber.kafka.ChangePhoneNumberPublisher;
import com.telus.cmb.subscriber.lifecyclehelper.svc.SubscriberLifecycleHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations={"classpath:application-context-test.xml","classpath:kafka-context-test.xml"})
@ActiveProfiles("standalone")
//@ActiveProfiles({"remote", "pt148"})
public class ChangePhoneNumberEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ChangePhoneNumberPublisher changePhoneNumberPublisher;
	
	@Autowired
	private ServiceAgreementEventPublisher serviceAgreementEventPublisher;
	
	@Autowired
	private SubscriberLifecycleHelper helper;
	
	@Autowired
	private AccountInformationHelper accountInformationHelper;
	
	static {
		System.setProperty("weblogic.Name", "standalone");
		System.setProperty("weblogic.security.SSL.ignoreHostnameVerification", "true");
		System.setProperty("UseSunHttpHandler", "true");		
	}

	@Test
	public void testPublishChangePhoneNumberEvent() throws Exception {
		
		String phoneNumber = "6472148009";
		String newPhoneNumber = "6472148088";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByPhoneNumber(phoneNumber);
		changePhoneNumberPublisher.publishChangePhoneNumberEvent(accountInfo, subscriberInfo, newPhoneNumber, "A00000111DealerCode", "12345SaleRepId", getAuditInfo(), false, "testSessionId");
	}
	
	@Test
	public void testPublishChangePhoneNumberPortInEvent() throws Exception {
		
		String phoneNumber = "6472148009";
		String newPhoneNumber = "6472148088";
		SubscriberInfo subscriberInfo = helper.retrieveSubscriberByPhoneNumber(phoneNumber);
		AccountInfo accountInfo = accountInformationHelper.retrieveLwAccountByPhoneNumber(phoneNumber);
		changePhoneNumberPublisher.publishChangePhoneNumberPortInEvent(accountInfo, subscriberInfo, newPhoneNumber, PortInEligibility.PORT_PROCESS_INTER_BRAND_PORT,
				"A00000111DealerCode", "12345SaleRepId", getAuditInfo(), false, "testSessionId");
	}
	private AuditInfo getAuditInfo() {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("827717");
		//auditInfo.setSalesRepId("9012");
		
		return auditInfo;
	}
	
	
}
