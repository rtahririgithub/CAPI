package com.telus.cmb.account.kafka;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.payment.kafka.AccountStatusEventPublisher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations = { "classpath:application-context-test.xml","classpath:kafka-context-test.xml" })
@ActiveProfiles("standalone")
public class AccountStatusEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private AccountStatusEventPublisher accountStatusEventPublisher;

	@Autowired
	private AccountInformationHelper helper;

	
	@Test
	public void publishAccountCancel() throws Throwable{
		System.out.println("begin publishAccountCancel...");
		int  ban = 70804610;
		List<String> phoneNumberList = Arrays.asList("6784449490", "6784449494", "6784449498");

		AccountInfo accountInfo = helper.retrieveLwAccountByBan(ban);
		accountStatusEventPublisher.publishAccountCancel(accountInfo, phoneNumberList, new Date(), "CR", "R", "test waive reason code", "customer requseted to cancel the account", false,false, getAuditInfo(), new Date(), false,false);		
		System.out.println("end publishAccountCancel...");
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