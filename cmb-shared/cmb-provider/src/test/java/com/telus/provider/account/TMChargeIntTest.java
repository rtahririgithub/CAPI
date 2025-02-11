package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Credit;

public class TMChargeIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
		//setupD3();
		setupEASECA_QA();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMChargeIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	
	public void testApply() throws TelusAPIException{
		System.out.println("testApply start");
		try{
		Account account = accountManager.findAccountByBAN(17605);
		TMCharge charge=(TMCharge) account.newCharge();
		charge.setAmount(10.00);
		charge.setChargeCode("CNV");
		double out=charge.apply(true);
		
		}catch(TelusAPIException t){
			System.out.println("Amdocs error - Invalid chargecode");
		}
		
       	System.out.println("testApply End");
	}
	
	public void testAdjust() throws TelusAPIException{
		System.out.println("testAdjust start");
		try{
			TMAccount account = (TMAccount)accountManager.findAccountByBAN(17605);
			TMCharge charge=(TMCharge) account.newCharge();
			
			double adjustmentAmount=100.00;
			String adjustmentReasonCode="CNV";
			String memoText= "memotext";
			
			double out=charge.adjust(adjustmentAmount, adjustmentReasonCode, memoText, false);
		}catch(TelusAPIException t){
			System.out.println("Amdocs Validation Exception occured");
		}
       	System.out.println("testAdjust End");
	}
	
	public void testDelete() throws TelusAPIException{
		
		System.out.println("testDelete start");
		try{
			Account account = accountManager.findAccountByBAN(17605);
			TMCharge charge=(TMCharge) account.newCharge();
			String deletionReasonCode="PCR";
			String memoText= "memotext";
			
			charge.delete( deletionReasonCode, memoText, true);
		}catch(TelusAPIException t){
			System.out.println("Amdocs Validation Exception occured");
		}
       	System.out.println("testDelete End");
	}
	public void testGetRelatedCredits() throws TelusAPIException{
		
		System.out.println("testGetRelatedCredits start");
		
		Account account = accountManager.findAccountByBAN(17605);
		TMCharge charge=(TMCharge) account.newCharge();
		
		Credit[] credits=charge.getRelatedCredits();
		System.out.println("Credits: "+credits.length);
		assertEquals(0, credits.length);
		
       	System.out.println("testGetRelatedCredits End");
	}
	
	
	
}


