package com.telus.provider.dealer;

import java.util.Date;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.dealer.CPMSDealer;
import com.telus.api.reference.Brand;
import com.telus.api.reference.Dealer;
import com.telus.api.reference.SalesRep;
import com.telus.provider.TestTMProvider;

public class TMDealerIntTest extends BaseTest {

	static {		
		setupD3();
	}

	public TMDealerIntTest(String name) throws Throwable {
		super(name);
	}

	private TMDealerManager dealerManager;
	

	public void setUp() throws Exception{
		super.setUp();
		dealerManager = (TMDealerManager)super.provider.getDealerManager();
	}

	public void testCreateDealer() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Dealer dealer = dealerManager.newDealer("TestPT148");
		dealer.setDescription("Test Dealer pt148 Description");
		dealer.setName("Test Dealer pt148");
		dealer.save();
	}
	
	public void test() throws Exception 
	{testModifyDealer();
	testExpireDealer();
	testUnExpireDealer();
	testAddSalesRep();
		
	}
	public void testModifyDealer() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Dealer dealer = provider.getDealerManager().findDealer("B00AB00001");
		dealer.setName("Modified TestPT148 ");
		dealer.save();
	}
	
	public void testExpireDealer() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Dealer dealer = provider.getDealerManager().findDealer("B00AB00003");
		System.out.println("dealer"+dealer.toString());
		dealer.setExpiryDate(new Date());
		dealer.save();
	}	
	
	public void testUnExpireDealer() throws TelusAPIException, SecurityException, NoSuchMethodException {
		Dealer dealer = provider.getDealerManager().findDealer("B00AB00001", true);
		System.out.println("dealer"+dealer);
		dealer.setExpiryDate(null);
		dealer.save();
	}		
	
	public void testAddSalesRep() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SalesRep salesRep = provider.getDealerManager().newSalesRep("B00AB00003", "0001");
		salesRep.setDescription("New Sales Rep Description");
		salesRep.setName("Sales Rep pt148");
		salesRep.save();
	}		

	public void testModifySalesRep() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SalesRep salesRep = provider.getDealerManager().findSalesRep("B00AB00003", "0001");
		salesRep.setName("Sales Rep 148 - modified");
		salesRep.save();
	}	
	
	public void testExpireSalesRep() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SalesRep salesRep = provider.getDealerManager().findSalesRep("B00AB00003", "0001");
		salesRep.setExpiryDate(new Date());
		salesRep.save();
	}	
	
	public void testUnExpireSalesRep() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SalesRep salesRep = provider.getDealerManager().findSalesRep("B00AB00003", "0001", true);
		salesRep.setExpiryDate(null);
		salesRep.save();
	}	
	
	public void testTransferSalesRep() throws TelusAPIException, SecurityException, NoSuchMethodException {
		SalesRep salesRep = provider.getDealerManager().findSalesRep("TestDealarpt148", "0001", true);
		salesRep.transferSalesRep("TestDealar", new Date());
		salesRep.save();	
	}		
	
	public void testGetCPMSDealer() throws TelusAPIException, SecurityException, NoSuchMethodException {
		CPMSDealer dealer = provider.getDealerManager().getCPMSDealer("A001000001", "0000");
		System.out.println("CPMS Dealer Name: " + dealer.getChannelDesc());
		System.out.println("CPMS Dealer Phone: " + dealer.getPhone());
	}		
	
	public void testResetDealerPassword() throws TelusAPIException, SecurityException, NoSuchMethodException {
		CPMSDealer dealer = provider.getDealerManager().getCPMSDealer("A001000001", "0000");
		dealer.resetUserPassword("password");
	}			
	
	public void testIsValidDealerPassword() throws TelusAPIException, SecurityException, NoSuchMethodException {
		CPMSDealer dealer = provider.getDealerManager().getCPMSDealer("A001000001", "0000");
		dealer.validUser("password");
	}		
	
}
