package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;

public class TMMemoIntTest extends BaseTest {

	static {
		setupEASECA_QA();
//		setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMMemoIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
		
	public void testCreate() throws TelusAPIException{
		System.out.println("testCreate start");
		String MEMO_DEPOSIT_PAYMENT = "ADEP";
		Account account = api.getAccountManager().findAccountByBAN(12474);
		TMMemo memo = (TMMemo)account.newMemo(); 
		memo.setMemoType(MEMO_DEPOSIT_PAYMENT);
		memo.setText("Memo testing");
		memo.create();
		
       	System.out.println("testCreate End");
	}
	
	public void testSave() throws TelusAPIException{
		System.out.println("testSave start");
		String MEMO_TYPE_SPECIAL_INSTRUCTIONS = "3000";
		Account account = api.getAccountManager().findAccountByBAN(12474);
		TMMemo memo = (TMMemo)account.newMemo(); 
		memo.setMemoType(MEMO_TYPE_SPECIAL_INSTRUCTIONS);
		memo.setText("Memo testing");
		memo.save();
		
       	System.out.println("testSave End");
	}
	
	public void testSaveSpecialInstructions() throws TelusAPIException{
		System.out.println("testSaveSpecialInstructions start");		
		Account account = api.getAccountManager().findAccountByBAN(12474);
		TMMemo memo = (TMMemo)account.getSpecialInstructionsMemo(); 
		memo.setText("Test");
		memo.save();
		
       	System.out.println("testSaveSpecialInstructions End");
	}	
	}


