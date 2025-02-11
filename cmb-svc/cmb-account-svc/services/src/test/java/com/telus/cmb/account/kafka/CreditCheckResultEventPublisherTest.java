package com.telus.cmb.account.kafka;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.lifecyclemanager.svc.AccountLifecycleManager;
import com.telus.cmb.account.payment.kafka.CreditCheckResultEventPublisher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations = { "classpath:application-context-test.xml","classpath:kafka-context-test.xml" })
@ActiveProfiles("standalone")
public class CreditCheckResultEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private CreditCheckResultEventPublisher creditCheckResultEventPublisher;

	@Autowired
	private AccountInformationHelper helper;
	
	@Autowired
	private AccountLifecycleManager manager;

	
	@Test
	public void publishCreateCredit() throws Throwable{
		System.out.println("begin publishCreateCredit...");
		int  ban = 70948427;
		AccountInfo accountInfo = helper.retrieveLwAccountByBan(ban);
		CreditCheckResultInfo creditCheckResultInfo = manager.retrieveAmdocsCreditCheckResultByBan(ban,manager.openSession("18654", "apollo", "SMARTDESKTOP"));
		creditCheckResultEventPublisher.publishNewCreditCheckResult(accountInfo, creditCheckResultInfo, getAuditInfo(), new Date(), false);
		System.out.println("end publishCreateCredit...");
	}
	
	private AuditInfo getAuditInfo() {
		AuditInfo auditInfo = new AuditInfo();
		auditInfo.setOutletId("642343251");
		auditInfo.setSalesRepId("ftsale");
		auditInfo.setOriginatorAppId("SMARTDESKTOP");
		auditInfo.setUserId("18654");
		return auditInfo;
	}
	
	
}