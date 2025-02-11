package com.telus.eas.account.info;

import com.telus.eas.utility.info.TaxationPolicyInfo;

import junit.framework.TestCase;

public class TaxSummaryInfoTest  extends TestCase{
	TaxSummaryInfo tsi;
	
	public void setUp() throws Exception{
		super.setUp();
		tsi=new TaxSummaryInfo();
	}
	
	public void testSetTaxationPolicy(){
		TaxationPolicyInfo tpi = new TaxationPolicyInfo();
		tpi.setGSTRate(101.00);
		tpi.setProvince("ON");
		tsi.setTaxationPolicy(tpi);
		
		assertEquals("ON", tsi.getTaxationPolicy().getProvince());
	}
}