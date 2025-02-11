package com.telus.eas.account.info;

import java.util.Date;

import junit.framework.TestCase;

public class BillingPropertyInfoTest extends TestCase{
	BillingPropertyInfo bpi;
	
	public void setUp() throws Exception{
		super.setUp();
		bpi=new BillingPropertyInfo();
	}
	
	public void testBillingPropertyInfoSetters(){
		
	AddressInfo address=new AddressInfo();
	address.setCity("toronto");
	bpi.setAddress(address);
	bpi.setFullName("Unit Test");
	bpi.setLegalBusinessName("TEST");
	ConsumerNameInfo cni=new ConsumerNameInfo();
	cni.setFirstName("Unit");
	cni.setLastName("Test");
	bpi.setName(cni);
	bpi.setTradeNameAttention("Ms");
	Date verifyDate = new Date(2011,04,11);
	bpi.setVerifiedDate(verifyDate );
	
	assertEquals("Unit Test", bpi.getFullName());
	assertEquals("Ms", bpi.getTradeNameAttention());
	assertEquals("TORONTO", bpi.getAddress().getCity());
	assertEquals("TEST", bpi.getLegalBusinessName());
	assertEquals("TEST", bpi.getName().getLastName());
	assertEquals(new Date(2011,04,11), bpi.getVerifiedDate());
	}
}