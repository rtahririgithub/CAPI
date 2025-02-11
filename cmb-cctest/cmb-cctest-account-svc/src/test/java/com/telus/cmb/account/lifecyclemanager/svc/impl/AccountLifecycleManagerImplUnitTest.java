/**
 * 
 */
package com.telus.cmb.account.lifecyclemanager.svc.impl;

import static org.junit.Assert.assertEquals;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.telus.cmb.account.lifecyclemanager.dao.AccountDao;
import com.telus.cmb.reference.svc.ReferenceDataFacade;
import com.telus.cmb.reference.svc.ReferenceDataHelper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.PostpaidConsumerAccountInfo;

/**
 * @author Canh Tran
 *
 */
@SuppressWarnings("deprecation")
public class AccountLifecycleManagerImplUnitTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	/* This test is only for postpaid account */
	public void testCreateAccount_WithNoBillCycle(@Mocked(methods = {"getAvailableBillCycle", "getReferenceDataHelper", "getReferenceDataFacade"}) final AccountLifecycleManagerImpl accountLifecycleManager) throws Exception {
		final int newBan=123;
		final String sessionId = "abc";
		final int billcode=3;
		
		final AccountInfo pAccountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		AddressInfo  address =  new AddressInfo();
		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");
		pAccountInfo.setAddress0(address);

		new NonStrictExpectations() 
		{
			@Mocked AccountDao accountDao;
			@Mocked ReferenceDataHelper referenceHelper;
			@Mocked ReferenceDataFacade referenceFacade;
			{
				invoke(accountLifecycleManager,"getAvailableBillCycle","aa"); returns(billcode);
				invoke(accountLifecycleManager,"getReferenceDataHelper"); returns(referenceHelper);
				invoke(accountLifecycleManager,"getReferenceDataFacade"); returns(referenceFacade);
				invoke(accountLifecycleManager,"setAccountDao",accountDao); 
				invoke(accountDao,"createAccount",pAccountInfo, sessionId); returns(newBan);
			}
		};
		
		assertEquals(newBan,accountLifecycleManager.createAccount(pAccountInfo, sessionId));
	}
	
	@Test
	/* This test is only for postpaid account */
	public void testCreateAccount_WithBillCycle(@Mocked(methods = {"getAvailableBillCycle", "getReferenceDataHelper", "getReferenceDataFacade"}) final AccountLifecycleManagerImpl accountLifecycleManager) throws Exception {
		final int newBan=123;
		final String sessionId = "abc";
		final int billcode=3;
		
		final AccountInfo pAccountInfo = PostpaidConsumerAccountInfo.newPCSInstance();
		pAccountInfo.setBillCycle(33);
		AddressInfo  address =  new AddressInfo();
		address.setStreetNumber("90");
		address.setStreetName("gerrard street");
		address.setCity("toronto");
		address.setProvince("ON");
		address.setPostalCode("m5g1j6");
		address.setCountry("CAN");
		pAccountInfo.setAddress0(address);

		new NonStrictExpectations() 
		{
			@Mocked AccountDao accountDao;
			@Mocked ReferenceDataHelper referenceHelper;
			@Mocked ReferenceDataFacade referenceFacade;
			{
				invoke(accountLifecycleManager,"getAvailableBillCycle","aa"); returns(billcode);
				invoke(accountLifecycleManager,"getReferenceDataHelper"); returns(referenceHelper);
				invoke(accountLifecycleManager,"getReferenceDataFacade"); returns(referenceFacade);
				invoke(accountLifecycleManager,"setAccountDao",accountDao); 
				invoke(accountDao,"createAccount",pAccountInfo, sessionId); returns(newBan);
			}
		};
		
		assertEquals(newBan,accountLifecycleManager.createAccount(pAccountInfo, sessionId));
	}
}
