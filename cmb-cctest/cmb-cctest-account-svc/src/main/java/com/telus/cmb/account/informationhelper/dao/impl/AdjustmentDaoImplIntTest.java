package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.api.account.Account;
import com.telus.api.account.Charge;
import com.telus.api.reference.ChargeType;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao;
import com.telus.cmb.account.informationhelper.dao.AdjustmentDao.CreditInfoHolder;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class AdjustmentDaoImplIntTest extends BaseInformationHelperIntTest {
	
	@Autowired
	AdjustmentDao dao;
	@Autowired
	Integer sampleBAN;
	Integer BANwithCharge = 8;
	
	static {
		System.setProperty("getPendingCharges.method.rollback", "false");
		System.setProperty("getUnbilledCredits.method.rollback", "false");
		System.setProperty("getBilledCredits.method.rollback", "false");
	}
	@Test
	public void testretrieveCreditByFollowUpId(){
		Collection<CreditInfo> creditInfo=dao.retrieveCreditByFollowUpId(18150420);
		assertEquals(1, creditInfo.size());
		for (CreditInfo ci : creditInfo) {
			assertEquals("9057160719",ci.getSubscriberId());
			assertEquals("ADJ ", ci.getActivityCode());
			assertEquals(400,ci.getAmount(),0);
			break;
		}
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveBilledCredits() throws ApplicationException{
		CreditInfoHolder cih=dao.retrieveBilledCredits(832198, new Date(100,03,22), new Date(100,04,12), "B", "", "", '1', "", 500);
		assertFalse(cih.hasMore());
		assertEquals(243, cih.getCreditInfo().size());
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveUnbilledCredits() throws ApplicationException{
		CreditInfoHolder cih=dao.retrieveUnbilledCredits(20070738, new Date(104,0,01), new Date(107,12,01), "U", "", "", '1', "", 300);
//		assertTrue(cih.hasMore());
//		assertEquals(300, cih.getCreditInfo().size());
		System.out.println(cih.hasMore());
		System.out.println(cih.getCreditInfo().size());
	}
	
	@Test
	public void testRetrieveCharges() {
		
		List<ChargeInfo> charges = dao.retrieveCharges(sampleBAN, new String[]{"CNRD"}, Account.BILL_STATE_ALL, 'A', "4033404108", new Date(), new Date(), 10);
		
		assertEquals(0, charges.size());
		Calendar fromCal = Calendar.getInstance();
		fromCal.set(Calendar.YEAR, 1999);
		charges = dao.retrieveCharges(8, new String[]{"CNRD"}, Account.BILL_STATE_ALL, ChargeType.CHARGE_LEVEL_SUBSCRIBER, "4033404108", fromCal.getTime(), new Date(), 10);
		
		assertEquals(5, charges.size());
		
	}
	
	@Test
	public void testRetrieveRelatedChargesForCredit() {
		Collection<ChargeInfo> chargeInfo;
		double pChargeSeqNo = 0;
		
		chargeInfo = dao.retrieveRelatedChargesForCredit(-1, pChargeSeqNo);
		assertEquals(0, chargeInfo.size());
		
		chargeInfo = dao.retrieveRelatedChargesForCredit(BANwithCharge, pChargeSeqNo);		
		assertEquals(0, chargeInfo.size());
		
		pChargeSeqNo = 547371566;
		chargeInfo = dao.retrieveRelatedChargesForCredit(BANwithCharge, pChargeSeqNo);
		assertEquals(1, chargeInfo.size());		
		for (ChargeInfo info: chargeInfo) {
			assertEquals("I     ", info.getReasonCode());
		}
	}
	
	@Test
	public void testRetrieveRelatedCreditsForCharge() {
		Collection<CreditInfo> creditInfo;
		double pChargeSeqNo = 0;				
		
		creditInfo = dao.retrieveRelatedCreditsForCharge(-1, pChargeSeqNo);
		assertEquals(0, creditInfo.size());
		
		creditInfo = dao.retrieveRelatedCreditsForCharge(BANwithCharge, pChargeSeqNo);
		assertEquals(0, creditInfo.size());
		
		pChargeSeqNo = 104154041;
		creditInfo = dao.retrieveRelatedCreditsForCharge(BANwithCharge, pChargeSeqNo);
		assertEquals(1, creditInfo.size());
		for (CreditInfo info: creditInfo) {
			assertEquals("RCADJ ", info.getReasonCode());
		}
	}
	
	@Test
	public void testRetrievePendingChargeHistory() {
		Date pFromDate = null;
		Date pToDate = null;
		char level = 'Z';
		String pSubscriber = null;
		int maximum = 100;
		SearchResultsInfo results;
		
		Calendar cal = Calendar.getInstance();		
					
		cal.set(1990, 0, 1);
		pFromDate = cal.getTime();
		pToDate = new Date();
		results = dao.retrievePendingChargeHistory(BANwithCharge, pFromDate, pToDate, level, pSubscriber, maximum);
		assertEquals(100, results.getCount());
		assertEquals(true, results.hasMore());
		
		cal.set(2003, 4, 21);
		pFromDate = cal.getTime();
		pToDate = pFromDate;
		results = dao.retrievePendingChargeHistory(BANwithCharge, pFromDate, pToDate, level, pSubscriber, maximum);
		assertEquals(2, results.getCount());
		assertEquals(false, results.hasMore());
		
	}

	@Test
	public void testRetrieveCreditById() {
		
		int banId = 70674364; int entSeqNo = 172907469;
		CreditInfo creditInfo = dao.retrieveCreditById(banId, entSeqNo);
		assertEquals(entSeqNo, creditInfo.getId() );
		
	}
	
	@Test
	public void testRetrieveApproveCreditByFollowUpId() {
		
		int banId = 70674364; int followUpId = 11748192;
		List<CreditInfo> creditInfo = dao.retrieveApprovedCreditByFollowUpId(banId, followUpId);
		assertTrue(creditInfo.size()>0);
		assertEquals(70674364, creditInfo.get(0).getBan() );
		
	}
}
