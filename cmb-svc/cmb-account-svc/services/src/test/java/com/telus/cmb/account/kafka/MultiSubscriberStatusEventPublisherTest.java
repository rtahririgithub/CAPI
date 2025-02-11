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
import com.telus.cmb.account.payment.kafka.MultiSubscriberStatusEventPublisher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations = { "classpath:application-context-test.xml","classpath:kafka-context-test.xml" })
@ActiveProfiles("standalone")
public class MultiSubscriberStatusEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private MultiSubscriberStatusEventPublisher multiSubscriberStatusEventPublisher;

	@Autowired
	private AccountInformationHelper helper;

	
	@Test
	public void publishMultiSubscriberCancelEvent() throws Throwable{
		System.out.println("begin publishMultiSubscriberCancelEvent...");
		int  ban = 70804610;
		List<String> phoneNumberList = Arrays.asList("4564576786", "6784449494");
		List<String> waiveReasonCodeList = Arrays.asList("WAIVE1", "WAIVE2");

		AccountInfo accountInfo = helper.retrieveLwAccountByBan(ban);
		multiSubscriberStatusEventPublisher.publishMultiSubscriberCancel(accountInfo, phoneNumberList, waiveReasonCodeList,new Date(), "CR", "R", "customer request", getAuditInfo(), new Date(), true,false);
		System.out.println("end publishMultiSubscriberCancelEvent...");
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