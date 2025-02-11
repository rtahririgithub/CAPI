package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountSummary;
import com.telus.api.reference.Brand;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.BillParametersInfo;
import com.telus.eas.account.info.BusinessCreditIdentityInfo;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.PrepaidConsumerAccountInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class AccountDaoImplIntTest extends BaseInformationHelperIntTest{

	@Autowired
	AccountDaoImpl dao;
	@Autowired
	Integer sampleBAN;
	
	static {
//		System.setProperty("retrieveAccountsByNameRollback.method.rollback", "true");
		System.setProperty("retrieveAccountsByPostalCodeRollback.method.rollback", "true");
	}
	
	@Test
	public void testRetrieveAccountsByBan()  {
		assertEquals(1, dao.retrieveAccountsByBan(new int[]{sampleBAN}).size());
	}
	
	@Test
	public void testRetrieveAccountByBan()  {
		if (System.getProperty("env") != null && System.getProperty("env").equalsIgnoreCase("PT168")) {
			AccountInfo ai = dao.retrieveAccountByBan(70602349);

			assertTrue(ai.isPrepaidConsumer());
			assertFalse(ai.isPostpaid());

			PrepaidConsumerAccountInfo pai = (PrepaidConsumerAccountInfo) ai;

			assertEquals(0, pai.getBalance(), 0);
		} else {
			AccountInfo ai = dao.retrieveAccountByBan(sampleBAN);

			assertEquals(sampleBAN, (Integer) ai.getBanId());
		}
	}
	
	@Test
	public void testRetrieveAccountStatusByBan() {
		assertEquals("O", dao.retrieveAccountStatusByBan(20007879));
	}
	
	@Test
	public void testRetrieveAccountByPostalCode() {
		Collection<AccountInfo> accounts =  dao.retrieveAccountsByPostalCode("TEST", "M1H3J3", 10);
		
//		assertEquals(1, accounts.size());
		System.out.println(accounts.size());
		for (AccountInfo ai : accounts) {
//			assertEquals(20007358, ai.getBanId());
			System.out.println(ai.getBanId());
		}		
	}
	
	@Test
	public void testRetrieveAccountByPhoneNumberStringBoolean() {
		Collection<AccountInfo> accounts = dao.retrieveAccountsByPhoneNumber("4033048642", true);
		
		assertEquals(1, accounts.size());
		
		for (AccountInfo ai : accounts) {
			assertEquals(1600, ai.getBanId());
		}
		
		accounts = dao.retrieveAccountsByPhoneNumber("4033048642", false);
		
		assertEquals(1, accounts.size());
		
		for (AccountInfo ai : accounts) {
			assertEquals(1600, ai.getBanId());
		}		
	}
	
	
	@Test
	public void testRetrieveAccountsByName() {
		SearchResultsInfo result = dao.retrieveAccountsByName("*", 
				"", 
				false, 
				"a", 
				false, 
				'*', 
				'*', 
				"*", 
				Brand.BRAND_ID_TELUS, 
				100);
		AccountSummary[] as = (AccountSummary[])result.getItems();
		
//		assertEquals(70, as.length);
		for (AccountSummary accountSummary : as) {
			System.out.println(accountSummary.getBanId()+","+accountSummary.getFullName());
		}
	}
	
	@Test
	public void testRetrieveAccountsByDealership() {
		@SuppressWarnings("deprecation")
		Collection<AccountInfo> accounts = dao.retrieveAccountsByDealership('O', "A0010000010000"
				, new Date("01/01/1999"), new Date("01/01/2999"), 10);
		
		assertEquals(10, accounts.size());
	}
	
	@Test
	public void testRetrieveAccountsBySalesRep() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 1999);
		
		Collection<AccountInfo> accounts = dao.retrieveAccountsBySalesRep('O', "A0010000010000", "", cal.getTime(), new Date(), 10);
		
		assertEquals(10, accounts.size());
	}
	
	@Test
	public void testRetrieveBanIds() {
		int[] bans = dao.retrieveBanIds('I', 'R', 'O', 10);
		
		assertEquals(10, bans.length);
	}
	
	@Test
	public void testRetrieveBanIdsByAddressType() {
		int[] bans = dao.retrieveBanIdByAddressType('I', 'R', 'O', 'C', 10);
		
		assertEquals(10, bans.length);
	}
	
	@Test
	public void retrieveBillParamsInfo() {
		BillParametersInfo billParamInfo = dao.retrieveBillParamsInfo(sampleBAN);
		assertEquals(1, billParamInfo.getNoOfInvoice());
		assertEquals("M1", billParamInfo.getBillFormat());
		assertEquals("PA", billParamInfo.getMediaCategory());		
	}
	
	@Test
	public void testRetrieveBanByImsi() {
		
		int ban = dao.retrieveBanByImsi("214030000050753");
		
		assertEquals(70100246, ban);
		
		assertEquals(0, dao.retrieveBanByImsi(""));		
	}
	
	@Test
	public void testRetrievesAccountBySerialNumber() {
		Collection<AccountInfo> accounts = dao.retrieveAccountsBySerialNumber("000005357");
		
		assertEquals(1, accounts.size());
		for (AccountInfo account : accounts) {
			assertEquals(55438827, account.getBanId());
		}
		
		assertEquals(0, dao.retrieveAccountsBySerialNumber("").size());		
	}

	@Test
	public void testGetClientAccount()
	{
		assertEquals(7547708,dao.getClientAccountId(70063821));
		assertEquals(7547710,dao.getClientAccountId(4464648));
		assertEquals(-1,dao.getClientAccountId(1));
	}
	@Test
	public void testretrieveCorporateName()
	{
		assertEquals("SUPER CORPORATION",dao.retrieveCorporateName(10000));
	}
	
	@Test
	public void testGetPaperBillSupressionAtActivationInd() {
		String ind = dao.getPaperBillSupressionAtActivationInd(sampleBAN);
		
		assertEquals(AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_UNKNOWN, ind);
		
		ind = dao.getPaperBillSupressionAtActivationInd(11000060);
		assertEquals(AccountSummary.BILL_SUPPRESSION_AT_ACTIVATION_NO, ind);
	}
	
	@Test
	public void testRetrieveBusinessList() {
		BusinessCreditIdentityInfo[] retrieveBusinessList = dao.retrieveBusinessList(sampleBAN);
		assertNotNull(retrieveBusinessList);
		assertEquals(0, retrieveBusinessList.length);
		
		retrieveBusinessList = dao.retrieveBusinessList(195);
		assertEquals(3, retrieveBusinessList.length);
		
		assertEquals("01,TEST,1 TEST ST,TORONTO,ON", retrieveBusinessList[0].getCompanyName());
		assertEquals(25910885, retrieveBusinessList[0].getMarketAccount(), 0);		
		assertEquals("02,S M S LTD,2235 BLOOR ST W,TORONTO,ON", retrieveBusinessList[1].getCompanyName());
		assertEquals(17580620, retrieveBusinessList[1].getMarketAccount(), 0);
		assertEquals("03,SNT LTD.,1 FIRST CANADIA,TORONTO,ON", retrieveBusinessList[2].getCompanyName());
		assertEquals(15494468, retrieveBusinessList[2].getMarketAccount(), 0);
	}
	
	@Test
	public void testRetrieveAuthorizedNames() {
		ConsumerNameInfo[] results = dao.retrieveAuthorizedNames(sampleBAN);
		assertNotNull(results);
		assertEquals(0, results.length);
		
		results = null;
		results = dao.retrieveAuthorizedNames(70104429);
		assertEquals(2, results.length);
		
		assertEquals("MR.", results[0].getTitle());
		assertEquals("NAMEA  NAMEB   NAMEC", results[0].getFirstName());
		assertEquals("MIDNA   MIDNB   MIDNC", results[0].getMiddleInitial());
		assertEquals("LASTNA    LASTNB   LASTNC", results[0].getLastName());
		assertNull(results[0].getGeneration());
		assertEquals("OTHERNA    OTHERNB     OTHERNC", results[0].getAdditionalLine());
		
		assertNull(results[1].getTitle());
		assertEquals("A", results[1].getFirstName());
		assertEquals("B", results[1].getMiddleInitial());
		assertEquals("C", results[1].getLastName());
		assertNull(results[1].getGeneration());
		assertNull(results[1].getAdditionalLine());		
	}

	@Test
	public void testRetrieveStatusChangeHistory() {
		int ban = 25;
		Calendar cal = Calendar.getInstance();
		cal.set(1999, 1, 1);
		Date fromDate = cal.getTime();
		cal.set(2010, 12, 7);
		Date toDate = cal.getTime();
		List<StatusChangeHistoryInfo> results = dao.retrieveStatusChangeHistory(ban, fromDate, toDate);
		
		assertEquals(7, results.size());
		
		for (StatusChangeHistoryInfo change : results) {
			assertNotNull(change);			
		}
	}
	
	@Test
	public void testRetrievePersonalCreditInformation() throws ApplicationException{
		int ban=20007358;
		dao.retrievePersonalCreditInformation(ban);
	}
	
	@Test
	public void testRetrieveBusinessCreditInformation() throws ApplicationException{
		int ban=20007358;
		dao.retrieveBusinessCreditInformation(ban);
		

	}

	@Test
	public void testRetrieveLwAccountByBan() throws ApplicationException{
		int ban=20007358;
		AccountInfo accountInfo = dao.retrieveLwAccountByBan(ban);
		assertEquals(ban, accountInfo.getBanId());
		assertEquals(40, accountInfo.getBillCycle());
		assertEquals(10, accountInfo.getBillCycleCloseDay());
		assertEquals("A001000001", accountInfo.getDealerCode());
		assertEquals("0000", accountInfo.getSalesRepCode());
		
	}
	
	// Business Connect Test Cases - July 2014
		@Test
		public void testRetrieveBANBySeatNumber() throws ApplicationException{
			String seatNumber="4166880002";
			int BAN=dao.retrieveBANBySeatNumber(seatNumber);
			assertEquals(84, BAN);
			System.out.println("BAN - "+BAN);		
		}
	
}
