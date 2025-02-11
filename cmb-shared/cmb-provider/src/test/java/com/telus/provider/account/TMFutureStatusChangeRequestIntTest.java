package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;

public class TMFutureStatusChangeRequestIntTest extends BaseTest {

	static {
		setupEASECA_QA();
		//setupD3();
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMFutureStatusChangeRequestIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}

	public void testSave() throws TelusAPIException{
		System.out.println("testSave Start");
		try{
			FutureStatusChangeRequestInfo info= new FutureStatusChangeRequestInfo();
			info.setBan(12474);
			TMFutureStatusChangeRequest changeRequest=new TMFutureStatusChangeRequest(provider, info);
			changeRequest.save();
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: Sequence Number does not match with an existing number");
		}
	    System.out.println("testSave End");
	}
	
	public void testDelete() throws TelusAPIException{
		System.out.println("testDelete Start");
		try{
			FutureStatusChangeRequestInfo info= new FutureStatusChangeRequestInfo();
			info.setBan(12474);
			TMFutureStatusChangeRequest changeRequest=new TMFutureStatusChangeRequest(provider, info);
			changeRequest.delete();
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: Sequence Number does not match with an existing number");
		}
	    System.out.println("testDelete End");
	}
	
	
	
	}


