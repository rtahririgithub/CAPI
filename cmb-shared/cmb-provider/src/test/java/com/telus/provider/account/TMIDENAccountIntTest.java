package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.fleet.Fleet;
import com.telus.api.fleet.TalkGroup;

public class TMIDENAccountIntTest extends BaseTest {

	static {
		setupEASECA_QA();
	}
	
	public TMIDENAccountIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
	}
		
	public void testNewFleet() throws  Throwable{
		System.out.println("testNewFleet start");
		try{
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(12474);
		Fleet f=account.newFleet(403, "west", 10);
		System.out.println(f);
		}catch(TelusAPIException e){
			System.out.println("Amdocs validation Exception : id=1110560; Invalid Network_Id.");
		}
		
		System.out.println("testNewFleet End");
	}
	
	public void testAddFleet() throws  Throwable{
		System.out.println("testAddFleet start");
		try{
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) api.getAccountManager().findAccountByBAN(12474);
		Fleet[] fleet=account.getFleets();
		System.out.println("Length : "+fleet.length);
		if(fleet.length>0 && fleet[0]!=null)
			account.addFleet(fleet[0]);
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: Fleet is already associated to this BAN.");
		}
		System.out.println("testAddFleet End");
	}
	
	
	
	public void testAddTalkGroup() throws  Throwable{
		System.out.println("testAddTalkGroup start");
		try{
		
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) 
		api.getAccountManager().findAccountByBAN(12474);
		
		TMIDENPostpaidBusinessRegularAccount account1 =(TMIDENPostpaidBusinessRegularAccount) 
		api.getAccountManager().findAccountByBAN(6376486);
		
		Fleet[] fleet=account.getFleets();
		if(fleet.length>0 && fleet[0]!=null){
			TalkGroup[] tlkGrp= fleet[0].getTalkGroups();
			account1.addTalkGroup(tlkGrp[0]);
		}
		
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: This combination of fleetId and urbanId is not associated with this Ban.");
		}
		System.out.println("testAddTalkGroup End");
	}
	
	
	public void testRemoveTalkGroup() throws  Throwable{
		System.out.println("testRemoveTalkGroup start");
		try{
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) 
		api.getAccountManager().findAccountByBAN(12474);
		if(account.getFleets().length>0 &&  (account.getFleets())[0]!=null){
			TalkGroup[] tlkGrp=(account.getFleets())[0].getTalkGroups();
			account.removeTalkGroup(tlkGrp[0]);
		}
		}catch(TelusAPIException e){
			System.out.println("Amdocs Error Message: This Talk Group does not belong to this BAN.");
		}
		System.out.println("testRemoveTalkGroup End");
	}
	
	public void testgetFleets0() throws  Throwable{
		System.out.println("testgetFleets0 start");
		
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) 
		api.getAccountManager().findAccountByBAN(12474);
		Fleet[] fleet=account.getFleets();
		assertEquals(1, fleet.length);
		
		System.out.println("testgetFleets0 End");
	}
	
	public void testRemoveFleet() throws  Throwable{
		System.out.println("testRemoveFleet start");
		try{
		TMIDENPostpaidBusinessRegularAccount account =(TMIDENPostpaidBusinessRegularAccount) 
		api.getAccountManager().findAccountByBAN(12474);
		Fleet[] fleet=account.getFleets();
		if(fleet.length>0 &&  fleet[0]!=null)
			account.removeFleet(fleet[0]);
//		assertEquals(1, fleet.length);
		}catch(TelusAPIException e){
			System.out.println("Error Message: Tuxedo Service Failed");
		}
		System.out.println("testRemoveFleet End");
	}
	
	
}


