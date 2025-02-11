package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.account.CollectionHistory;
import com.telus.eas.account.info.CollectionStateInfo;

public class TMFinancialHistoryIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMFinancialHistoryIntTest(String name) throws Throwable {
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
		
	public void testGetCollectionHistory() throws  Throwable{
		System.out.println("testGetCollectionHistory start");
		
		Date from = getDateInput(2001, 02, 01);
		Date to = getDateInput(2010, 02, 01);
		
		TMAccount account =(TMAccount) api.getAccountManager().findAccountByBAN(70028375);
		TMFinancialHistory financialHistory= (TMFinancialHistory) account.getFinancialHistory();
		CollectionHistory[] history =financialHistory.getCollectionHistory(from, to);
		assertEquals(2,history.length);
		
		System.out.println("testGetCollectionHistory End");
	}
	
	public void testRetrieveCollectionState() throws  Throwable{
		System.out.println("testRetrieveCollectionState start");
		
		TMAccount account =(TMAccount) api.getAccountManager().findAccountByBAN(70028375);
		TMFinancialHistory financialHistory= (TMFinancialHistory) account.getFinancialHistory();
		CollectionStateInfo info =financialHistory.retrieveCollectionState();
		assertEquals("C",info.getNextStepApprovalCode());
		
		System.out.println("testRetrieveCollectionState End");
	}
	
	
	
	}


