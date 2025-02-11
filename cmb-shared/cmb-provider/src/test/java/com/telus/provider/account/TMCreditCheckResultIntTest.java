package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.util.SessionUtil;
import com.telus.eas.account.info.CreditCheckResultInfo;

public class TMCreditCheckResultIntTest extends BaseTest {

	static {
		setupEASECA_QA();
	//	setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMCreditCheckResultIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
		
	public void testUpdateCreditClass() throws  Throwable{
		System.out.println("testUpdateCreditClass start");
		
		TMAccount account =(TMAccount) api.getAccountManager().findAccountByBAN(17605);
		CreditCheckResultInfo info = provider.getAccountLifecycleManager().retrieveAmdocsCreditCheckResultByBan(account.getBanId(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		TMCreditCheckResult tmCreditCheckResult = new TMCreditCheckResult(provider, info);
		tmCreditCheckResult.updateCreditClass(account.getBanId(), "C", "Testing");
		
		System.out.println("testUpdateCreditClass End");
	}
	
	public void testUpdateCreditProfile() throws Throwable{
		System.out.println("testUpdateCreditProfile start");
		
		TMAccount account =(TMAccount) api.getAccountManager().findAccountByBAN(17605);
		CreditCheckResultInfo info = provider.getAccountLifecycleManager().retrieveAmdocsCreditCheckResultByBan(account.getBanId(), SessionUtil.getSessionId(provider.getAccountLifecycleManager()));
		TMCreditCheckResult tmCreditCheckResult = new TMCreditCheckResult(provider, info);
		tmCreditCheckResult.updateCreditProfile(account.getBanId(), "C", 100.00, "testing");
		
		System.out.println("testUpdateCreditProfile End");
	}
	
	}


