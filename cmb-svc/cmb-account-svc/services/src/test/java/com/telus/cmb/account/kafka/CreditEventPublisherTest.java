package com.telus.cmb.account.kafka;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.telus.api.account.Account;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.account.payment.kafka.CreditEventPublisher;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.FollowUpUpdateInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.transaction.info.AuditInfo;

@Test
@ContextConfiguration(locations = { "classpath:application-context-test.xml","classpath:kafka-context-test.xml" })
@ActiveProfiles("standalone")
public class CreditEventPublisherTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private CreditEventPublisher creditEventPublisher;

	@Autowired
	private AccountInformationHelper helper;

	
	@Test
	public void publishCreateCredit() throws Throwable{
		System.out.println("begin publishCreateCredit...");
		int  ban = 70804610;
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		creditEventPublisher.publishCreateCredit(accountInfo, getCreditInfo(), getAuditInfo(), new Date(), false);		
		System.out.println("end publishCreateCredit...");
	}

	@Test
	public void publishCreditForChargeAdj() throws Throwable{
		System.out.println("begin publishCreditForChargeAdj...");
		int  ban = 70804610;
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		creditEventPublisher.publishCreditForChargeAdj(accountInfo, getCreditInfo(), getChargeInfo(), getAuditInfo(), new Date(), false);		
		System.out.println("end publishCreditForChargeAdj...");
	}
	
	@Test
	public void publishFollowUpCredit() throws Throwable{
		System.out.println("begin publishFollowUpCredit...");
		int  ban = 70804610;
		AccountInfo accountInfo = helper.retrieveAccountByBan(ban, Account.ACCOUNT_LOAD_ALL_BUT_NO_CDA);
		creditEventPublisher.publishFollowUpApprovalCredit(accountInfo, getCreditInfo(), getFollowUpUpdateInfo(), getAuditInfo(), new Date(), false);	
			
		System.out.println("end publishFollowUpCredit...");
	}
	
	

	private ChargeInfo getChargeInfo(){
		ChargeInfo info = new ChargeInfo();
		info.setId(454656);
		info.setBilled(false);
		info.setBillSequenceNo(0);
		return info;
		
	}


	private FollowUpUpdateInfo getFollowUpUpdateInfo(){
		FollowUpUpdateInfo info = new FollowUpUpdateInfo();
		info.setFollowUpId(334676);
		info.setFollowUpType("TEST");
		info.setCloseReasonCode(FollowUpUpdateInfo.FOLLOW_REASON_ADJUST_CHARGE);
		return info;
		
	}
		
	private CreditInfo getCreditInfo(){
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(11111);
		//creditInfo.setSubscriberId(subscriberId); // just comment out this if we would need to test BalanceAdjustment Ban level transactions
		creditInfo.setAmount(11);
	
		creditInfo.setEffectiveDate(new Date());
		creditInfo.setReasonCode("ACSSC");
		
		// set the tax amounts..
		creditInfo.getTaxSummary().setGSTAmount(17);
		creditInfo.getTaxSummary().setPSTAmount(13);
		creditInfo.getTaxSummary().setHSTAmount(6);
		
		//set Recurring flags
		creditInfo.setRecurring(false);
		creditInfo.setNumberOfRecurring(1);
		return creditInfo;
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