package com.telus.provider.account;


import java.util.Date;
import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.Brand;
import com.telus.provider.TestTMProvider;
import com.telus.provider.equipment.TMEquipment;

public class TMEquipmentChangeHistoryIntTest extends BaseTest {
	
	static {
		setupEASECA_QA();
		//setupD3();
	}
	
	public TMEquipmentChangeHistoryIntTest (String name) throws Throwable {
		super(name);
	}

	private TMAccountManager accountManager;
	private TestTMProvider testTMProvider;
	private TMEquipmentChangeHistory testEquipCH;

	public void setUp() throws Exception{
		super.setUp();
		
		accountManager = super.provider.getAccountManager0();
		testTMProvider = new TestTMProvider("18654", "apollo", "", new int[]{Brand.BRAND_ID_TELUS});

	}

	public void testGetEquipment() throws UnknownBANException, TelusAPIException, SecurityException, NoSuchMethodException {
		
		//  PCS Equipment
		Account ai = accountManager.findAccountByBAN0(12474);
		TMPCSSubscriber pcsSubscriber = (TMPCSSubscriber)ai.getSubscriberByPhoneNumber("4033946834");
		TMEquipmentChangeHistory[]  ech = (TMEquipmentChangeHistory[]) pcsSubscriber.getEquipmentChangeHistory(new Date((2002-1900),(2-1),13), new Date((2009-1900),(3-1),31));
		TMEquipment equipment= (TMEquipment) ech[0].getEquipment();
		assertEquals("11914582716",equipment.getSerialNumber());
		
		// Pager Equipment 

		ai = accountManager.findAccountByBAN0(70028375);
		TMPagerSubscriber pagerSubscr = (TMPagerSubscriber)ai.getSubscriberByPhoneNumber("2509600771");
		ech = (TMEquipmentChangeHistory[]) pagerSubscr.getEquipmentChangeHistory(new Date((2002-1900),(2-1),13), new Date((2005-1900),(3-1),31));
		equipment= (TMEquipment) ech[0].getEquipment();
		assertEquals("141XWV3DXF",equipment.getSerialNumber());
				
		

	}
}
