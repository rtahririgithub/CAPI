package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.FollowUp;
import com.telus.api.account.FollowUpText;

public class TMFollowUpIntTest extends BaseTest {

	static {
		setupEASECA_QA();
		
//		setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMFollowUpIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	private TMFollowUp getTMFollowUp() throws TelusAPIException{
		Account account = api.getAccountManager().findAccountByBAN(12474);
		int count=100;
		TMFollowUp followUp= null;
		TMFollowUp[] followUps=(TMFollowUp[])account.getFollowUps(count);
		if(followUps.length>0){
			followUps[0].setDueDate(getDateInput(2015, 01, 02));
			followUp=followUps[0];
		}
		
		return followUp;
	}
	
	public void testCreate() throws TelusAPIException{
		System.out.println("testCreate start");
		getTMFollowUp().create();
		System.out.println("testCreate end");
	}
	
	public void testGetAdditionalText() throws TelusAPIException{
		System.out.println("testGetAdditionalText start");
		FollowUpText[] followUpTexts=getTMFollowUp().getAdditionalText(true);
		assertEquals(0, followUpTexts.length);
		System.out.println("testGetAdditionalText end");
	}
	
	public void testGetHistory() throws TelusAPIException{
		System.out.println("testGetHistory start");
		FollowUp[] followUps=	getTMFollowUp().getHistory(true);
		assertEquals(0, followUps.length);
		System.out.println("testGetHistory end");
	}
	
	public void testRefresh() throws TelusAPIException{
		System.out.println("testRefresh start");
		getTMFollowUp().refresh();
		System.out.println("testRefresh end");
	}
	
	public void testSave() throws TelusAPIException{
		System.out.println("testSave start");
		getTMFollowUp().save();
		System.out.println("testSave end");
	}
}


