package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.cmb.account.informationhelper.dao.PaymentDao;
import com.telus.eas.account.info.PaymentActivityInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.RefundHistoryInfo;

public class PaymentDaoImplIntTest extends BaseInformationHelperIntTest {
	@Autowired
	PaymentDao dao;

	@Test
	public void testRetrieveLastPaymentActivity() {
		PaymentHistoryInfo info = dao.retrieveLastPaymentActivity(134);		
		assertNull(info); 	
		
		info = dao.retrieveLastPaymentActivity(8);		
		assertNotNull(info); 
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testretrievePaymentHistory(){
		Collection<PaymentHistoryInfo> paymentHistoryList = new ArrayList<PaymentHistoryInfo>();
		paymentHistoryList =dao.retrievePaymentHistory(8,new java.sql.Date(2003-1900,05,27),new java.sql.Date(2003-1900,05,27));
		assertEquals(2,paymentHistoryList.size());
		for(PaymentHistoryInfo paymentHistoryInfo:paymentHistoryList){
		assertEquals("CC",paymentHistoryInfo.getPaymentMethodCode());
		assertEquals(225,paymentHistoryInfo.getOriginalAmount(),0);
		break;
		}
	}
	@Test
	public void testRetrievePaymentActivities(){
		Collection<PaymentActivityInfo> paymentActivity = new ArrayList<PaymentActivityInfo>();
		paymentActivity = dao.retrievePaymentActivities(8, 884811);
		assertEquals(1,paymentActivity.size());
		for(PaymentActivityInfo payActInf:paymentActivity){
		assertEquals("PYM",payActInf.getActivityCode().trim());
		assertEquals(146.47,payActInf.getAmount(),0);
		assertEquals(null,payActInf.getCreditCardAuthorizationCode());
		assertEquals(null,payActInf.getExceptionReasonCode());
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrievePaymentMethodChangeHistory(){
		Collection<PaymentMethodChangeHistoryInfo> paymentActivity = new ArrayList<PaymentMethodChangeHistoryInfo>();
		paymentActivity = dao.retrievePaymentMethodChangeHistory(20007060, new Date(103,0,01),new Date(110,0,01));
		assertEquals(1,paymentActivity.size());
		for(PaymentMethodChangeHistoryInfo payActInf:paymentActivity){
		assertEquals("100000000000000006551",payActInf.getCreditCardToken());
		assertEquals("1203",payActInf.getCreditCardExpiry());
		assertEquals("@@",payActInf.getBankCode().trim());
		assertEquals("@@",payActInf.getBankAccountNumber().trim());
		}
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testRetrieveRefundHistory(){
		List<RefundHistoryInfo> refundHistoryList = new ArrayList<RefundHistoryInfo>();
		refundHistoryList =dao.retrieveRefundHistory(99999924,new Date(2002-1900,01,20),new Date(2006-1900,01,26));
		assertEquals(37,refundHistoryList.size());
		for(RefundHistoryInfo refundHistoryInfo:refundHistoryList){
		assertEquals("RFERPY",refundHistoryInfo.getReasonCode());
		assertEquals("RFN ",refundHistoryInfo.getCode());
		assertEquals(292.12,refundHistoryInfo.getAmount(),0);
		break;
		}
	}

}
