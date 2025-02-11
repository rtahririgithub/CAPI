package com.telus.eas.subscriber.info;

import junit.framework.TestCase;

public class ServiceAgreementInfoTest  extends TestCase{
	ServiceAgreementInfo sai;
	
	public void setUp() throws Exception{
		super.setUp();
		sai=new ServiceAgreementInfo();
	}
	
	public void testSetPurchaseFundSource(){
		
		sai.setPurchaseFundSource(10);
		assertEquals(10, sai.getPurchaseFundSource());
	}
}