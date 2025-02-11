//package com.telus.provider.account;
//
//import com.telus.api.BaseTest;
//import com.telus.api.TelusAPIException;
//import com.telus.api.reference.Letter;
//
//public class TMLMLetterRequestIntTest extends BaseTest {
//
//	static {
//		setupEASECA_QA();
////		setupD3();
////		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
////		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
////		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
//	}
//	
//	public TMLMLetterRequestIntTest(String name) throws Throwable {
//		super(name);
//	}
//		
//	public void setUp() throws Exception{
//		super.setUp();
//		
//	}
//		
//	public void testSave() throws TelusAPIException{
//		System.out.println("testSave start");
////		TMAccount account =(TMAccount)api.getAccountManager().findAccountByBAN(12474);
////		 String letterCategory = "CLP";
////		 String letterCode ="CLME";
////		 Letter letter = provider.getReferenceDataManager().getLetter(letterCategory ,letterCode);
////		 TMLMSLetterRequest newLetter = (TMLMSLetterRequest)account.newLMSLetterRequest(letter);
////		 newLetter.save();
//		
//       	System.out.println("testSave End");
//	}
//	
//	public void testDelete() throws TelusAPIException{
//		System.out.println("testDelete start");
////		try{
////		TMAccount account =(TMAccount)api.getAccountManager().findAccountByBAN(12474);
////		 String letterCategory = "CLP";
////		 String letterCode ="CLME";
////		 Letter letter = provider.getReferenceDataManager().getLetter(letterCategory ,letterCode);
////		 TMLMSLetterRequest newLetter = (TMLMSLetterRequest)account.newLMSLetterRequest(letter);
////		 newLetter.delete();
////		}catch(TelusAPIException e){
////			System.out.println("AMDOCS Error Message: Request Number does not belong to BAN");
////		}
//		
//       	System.out.println("testDelete End");
//	}
//	
//	}
//
//
