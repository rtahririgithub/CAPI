package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.account.ChargeTypeTax;
import com.telus.cmb.subscriber.lifecyclehelper.BaseLifecycleHelperIntTest;
import com.telus.eas.account.info.InvoiceTaxInfo;

public class InvoiceDaoImplIntTest extends BaseLifecycleHelperIntTest{
	@Autowired
	InvoiceDaoImpl dao;
	
	@Test
	public void testRetrieveInvoiceTaxInfo(){
		int ban=8;
		String subscriber="4033404108";
		int billSeqNo=52;
		InvoiceTaxInfo info = new InvoiceTaxInfo();
		ChargeTypeTax[] taxArray = new ChargeTypeTax[4];
		info = dao.retrieveInvoiceTaxInfo(ban, subscriber, billSeqNo);
		taxArray=info.getChargeTypeTaxes();
		assertEquals(3,info.getChargeTypeTaxes().length);
		assertEquals(0,taxArray[0].getRoamingTaxAmount(),0);
	}

}
