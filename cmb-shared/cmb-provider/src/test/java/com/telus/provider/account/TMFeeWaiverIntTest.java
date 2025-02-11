package com.telus.provider.account;

import java.util.Calendar;
import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.eas.account.info.FeeWaiverInfo;

public class TMFeeWaiverIntTest extends BaseTest {

	static {
		//setupD3();
		setupEASECA_QA();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
	}
	
	public TMFeeWaiverIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
		
	public void testAdd() throws  Throwable{
		System.out.println("testAdd start");
		
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(12474);
		
		FeeWaiverInfo info = new FeeWaiverInfo();
		info.setBanId(account.getBanId());
		info.setTypeCode("ICC");
		info.setReasonCode("WLPC");
		info.setEffectiveDate(getDateInput(2011, 11, 01));
		info.setExpiryDate(getDateInput(2012, 12, 01));
		info.setMode(FeeWaiverInfo.INSERT);
		TMFeeWaiver feeWaiver= new TMFeeWaiver(provider, info);
		feeWaiver.add();
		
		System.out.println("testAdd End");
	}
	
	private Date getDateInput(int year, int month, int date){
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date);
		return cal.getTime();
	}
	
	public void testDelete() throws  Throwable{
		System.out.println("testDelete start");
		try{
			TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(12474);
		 
		FeeWaiverInfo info = new FeeWaiverInfo();
		info.setBanId(account.getBanId());
		info.setTypeCode("ICC");
		info.setReasonCode("WLPC");
		info.setEffectiveDate(getDateInput(2006, 0, 01));
		info.setExpiryDate(getDateInput(2012, 0, 01));
		info.setMode(FeeWaiverInfo.DELETE);
		TMFeeWaiver feeWaiver= new TMFeeWaiver(provider, info);
		feeWaiver.delete();
		}catch(TelusAPIException e){
			System.out.println("Error Message: Invalid FeeWaivers actMode: Can not delete Fee waiver that is not exist.");
			e.printStackTrace();
		}
		System.out.println("testDelete end");
	}
	
	public void testUpdate() throws  Throwable{
		System.out.println("testUpdate start");
		try{
		
			TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(12474);
		 
		FeeWaiverInfo info = new FeeWaiverInfo();
		info.setBanId(account.getBanId());
		info.setTypeCode("ICC");
		info.setReasonCode("WLPC");
		info.setEffectiveDate(getDateInput(2006, 0, 01));
		info.setExpiryDate(getDateInput(2015, 0, 01));
		info.setMode(FeeWaiverInfo.UPDATE);
		TMFeeWaiver feeWaiver= new TMFeeWaiver(provider, info);
		feeWaiver.update();
		}catch(TelusAPIException e){
			System.out.println("Error Message: Invalid FeeWaivers actMode: Can not update Fee waiver that is not exist.");
//			e.printStackTrace();
		}
		System.out.println("testUpdate end");
	}
	
	
}


