package com.telus.provider.account;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;

public class TMEnterpriseAddressIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
		setupD3();
//		setupSMARTDESKTOP_D3();
		
//		System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
//		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");

		
//		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");

	}
	
	public TMEnterpriseAddressIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	

	
	public void testGetDelegate() throws TelusAPIException{
		
		Account account = accountManager.findAccountByBAN(81);
		
		EnterpriseAddressInfo eai = new EnterpriseAddressInfo(new AddressInfo());
		//eai.translateAddress(account.getAddress());
		eai.setAddressTypeCode("U");
		TMEnterpriseAddress tmEnterpriseAddress = new TMEnterpriseAddress(super.provider,eai);
		
		System.out.println(tmEnterpriseAddress.getAddressTypeCode());
		System.out.println(tmEnterpriseAddress.getDelegate().getAddressTypeCode());
		
		tmEnterpriseAddress.translateAddress(account.getAddress());
		
		System.out.println(tmEnterpriseAddress.getDelegate().getAddressTypeCode());
	}
	
	public void testTranslateAddress() throws TelusAPIException{
		
		Account account = accountManager.findAccountByBAN(81);
		
		EnterpriseAddressInfo eai = new EnterpriseAddressInfo(new AddressInfo());
		//eai.translateAddress(account.getAddress());
		eai.setAddressTypeCode("U");
		TMEnterpriseAddress tmEnterpriseAddress = new TMEnterpriseAddress(super.provider,eai);
		
		System.out.println(tmEnterpriseAddress.getAddressTypeCode());
		
		tmEnterpriseAddress.translateAddress(account.getAddress());
		
		System.out.println(tmEnterpriseAddress.getAddressTypeCode());
	}
	
}


