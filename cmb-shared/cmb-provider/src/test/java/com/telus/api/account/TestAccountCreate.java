package com.telus.api.account;

import com.telus.api.BaseTest;
import com.telus.api.ClientAPI;
import com.telus.api.reference.Brand;

public class TestAccountCreate extends BaseTest {

	private static ClientAPI api;
	static {
		//setupD3();
		setupEASECA_QA();
		
		try {
			api = ClientAPI.getInstance("18654", "apollo", "CLIENTAPITEST");
		}catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public TestAccountCreate(String name) throws Throwable {
		super(name);
	}

	private static void setupPostpaidConsumerRegularAccount(PCSPostpaidConsumerAccount account) throws Throwable {
		setupAccount(account);
		account.getContactName().setFirstName("ATTest");
		account.getContactName().setLastName("HSPA");
		account.getName().setFirstName("ATTest");
		account.getName().setLastName("HSPA");
		account.setHomePhone("4169404981");
		account.setContactPhone("4169404981");
		account.getPaymentMethod().setPaymentMethod(com.telus.api.account.PaymentMethod.PAYMENT_METHOD_REGULAR);
	}
	
	private static void setupAccount(Account account) throws Throwable {
		setupAddress(account.getAddress());
		account.setEmail("email@telusmobility.com");
		account.setPin("2222");
		account.setLanguage("EN");
		account.setDealerCode("A001000001");
		account.setSalesRepCode("0000");
	}
	
	private static void setupAddress(Address address) throws Throwable {
		address.setStreetNumber("90");
		address.setStreetName("GERRARD ST W");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("M5G1J6");
		address.setCountry("CAN");
		// address.validate();
	}
	
	public int createPostpaidConsumerRegularAccount(int brandId) throws Throwable {
		PCSPostpaidConsumerAccount account = api.getAccountManager().newPCSPostpaidConsumerAccount();
		setupPostpaidConsumerRegularAccount(account);
		account.setBrandId(brandId);
		account.save();
		//account.checkCredit();
		return account.getBanId();
	}
	
	public void testCreatePostpaidConsumerRegularAccount() throws Throwable {
		int ban = createPostpaidConsumerRegularAccount (Brand.BRAND_ID_TELUS);
		System.out.println("Created Consumer/Regular BAN "+ban);
	}
	
}
