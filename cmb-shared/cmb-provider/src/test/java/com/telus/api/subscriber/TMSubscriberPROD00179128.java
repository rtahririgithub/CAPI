package com.telus.api.subscriber;

import com.telus.api.AuthenticationException;
import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountManager;
import com.telus.api.account.Contract;
import com.telus.api.account.InvalidSubscriberStatusException;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.DiscountPlan;

public class TMSubscriberPROD00179128 extends BaseTest {

	public TMSubscriberPROD00179128(String name) throws Throwable {
		super(name);
	}
	static{
//		setupEASECA_QA();
		setupEASECA_PT168();
	}
	public void testDefectPROD00179128() throws AuthenticationException, TelusAPIException{
		
		AccountManager accountManager = provider.getAccountManager();
		
		String phoneNumber = "5061752006";
		System.out.println("Looking up subscriber: " + phoneNumber);
		Subscriber sub = accountManager.findSubscriberByPhoneNumber(phoneNumber);
		System.out.println(sub.getAccount().getBanId());
		System.out.println("Found subscriber. Loading contracts..");
		Contract c = sub.getContract();
		System.out.println("Contract loaded.");
		
		for (int i = 0; i < c.getFeatures().length; i++) {
			System.out.println("Feature code found - " + c.getFeatures()[i].getCode());
		}
		
		

		Account account = accountManager.findAccountByBAN(70602790);                      
		Subscriber subscriber = account.getSubscriber("5061752006");            
		
		try {
			subscriber.getContract();
		//	fail("Exception expected.");
		} catch (InvalidSubscriberStatusException e) {
			fail("Exception expected.");
		}
		
	}
	
	public void testSubscriber() throws Throwable {
		String phoneNumber = "9056263055";
		phoneNumber = "6041652369 ";
		
		Subscriber s = api.getAccountManager().findSubscriberByPhoneNumber(phoneNumber);
		System.out.println(s.getCommitment().getMonths());
		
//		 getReferenceDataHelper().retrieveDiscountPlans(true,subscriberInfo.getPricePlan(), 
//					subscriberInfo.getMarketProvince(), equipmentInfo.getProductPromoTypeList(), equipmentInfo.isInitialActivation(), 
//					subscriberContractInfo.getCommitmentMonths());
		
		DiscountPlan[] dp = api.getReferenceDataManager().getDiscountPlans(true, s.getContract().getPricePlan().getCode(), s.getMarketProvince(), s.getEquipment(), s.getCommitment().getMonths());
		 
		System.out.println(dp.length);
		for (DiscountPlan d : dp) {
			System.out.println(d.getCode());
			System.out.println(d);
		}
		System.out.println("End");
	}

}
