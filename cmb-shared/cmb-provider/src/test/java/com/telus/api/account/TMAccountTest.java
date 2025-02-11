package com.telus.api.account;

import junit.framework.Assert;

import com.telus.api.BaseTest;
import com.telus.api.BrandNotSupportedException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;

public class TMAccountTest extends BaseTest {

	public TMAccountTest(String name) throws Throwable {
		super(name);
	}

	private static ClientAPI api;
	static {
		setupCHNLECA_PT168();
		//setupEASECA_QA();
		//localhostWithD3Ldap();
		//System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
		//System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		
		try {
			api = ClientAPI.getInstance("18654", "apollo", "CLIENTAPITEST");
		}catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	public void testGetProductSubscriberLists() throws UnknownBANException, BrandNotSupportedException, TelusAPIException {
		Account account = api.getAccountManager().findAccountByBAN(12474);
		System.out.println(account.getProductSubscriberLists().length);
	}

	public void testRefreshWithSupressionIndicatorSetToTrue() throws TelusAPIException {
		Account account = api.getAccountManager().findAccountByBAN(12474);
		account.setTransientNotificationSuppressionInd(true);
		account.setHomeProvince("ON");
		account.refresh();
		boolean TransientNoficationSuppressionInd=account.getTransientNotificationSuppressionInd();
		Assert.assertEquals(true, TransientNoficationSuppressionInd);
		Assert.assertEquals("AB",account.getHomeProvince());

	}
	public void testRefreshWithSupressionIndicatorWithDefautValue() throws TelusAPIException {
		Account account = api.getAccountManager().findAccountByBAN(12474);
		account.setHomeProvince("ON");
		account.refresh();
		boolean TransientNoficationSuppressionInd=account.getTransientNotificationSuppressionInd();
		Assert.assertEquals(false, TransientNoficationSuppressionInd);
		Assert.assertEquals("AB",account.getHomeProvince());

	}
	
	public void testBalanceCapOrThresholdCode() throws TelusAPIException {
		int ban=70679807;
		
		Account account = api.getAccountManager().findAccountByBAN(ban);
		if (account.isPrepaidConsumer()) {
			PrepaidConsumerAccount prepaidAccount = (PrepaidConsumerAccount)account;
			String thresholdCode = prepaidAccount.getBalanceCapOrThresholdCode();
			System.out.println("Balance: " + prepaidAccount.getBalance());
			System.out.println("ThresholdCode: " + thresholdCode);
			assertNotNull(thresholdCode);
		} else {
			System.out.println("Account with " + ban + " is not a prepaid account.");
		}

	}
	

}
