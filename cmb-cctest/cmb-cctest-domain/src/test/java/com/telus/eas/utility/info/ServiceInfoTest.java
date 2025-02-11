package com.telus.eas.utility.info;

import junit.framework.TestCase;
import com.telus.api.reference.FundSource;

public class ServiceInfoTest  extends TestCase{
	ServiceInfo si;
	
	public void setUp() throws Exception{
		super.setUp();
		si=new ServiceInfo();
	}
	
	public void testSetallowedPurchaseFundSourceArray(){
		
		FundSource[] fundSource = new FundSource[2];
		fundSource[0] = new FundSourceInfo();
		fundSource[1] = new FundSourceInfo();
		si.setAllowedPurchaseFundSourceArray(fundSource);
		assertEquals(2,si.getAllowedPurchaseFundSourceArray().length);
	}
}
