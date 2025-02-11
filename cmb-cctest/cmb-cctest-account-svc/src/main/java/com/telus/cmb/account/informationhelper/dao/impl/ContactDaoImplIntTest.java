package com.telus.cmb.account.informationhelper.dao.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.informationhelper.BaseInformationHelperIntTest;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ContactPropertyInfo;

@ContextConfiguration(locations = {"classpath:application-context-informationhelper-test.xml"})
public class ContactDaoImplIntTest extends BaseInformationHelperIntTest {

	@Autowired
	ContactDaoImpl contactDao; 
	
	
	@Test
	public void testRetrieveBillingInformation() throws ApplicationException {		
		
		int billingAccountNumber=82;
		BillingPropertyInfo billingPropertyInfo=contactDao.retrieveBillingInformation(billingAccountNumber);
		assertEquals("GEORGE HARRISON",billingPropertyInfo.getFullName());
		assertEquals(new Date(2005-1900,2-1,18), billingPropertyInfo.getVerifiedDate());
	}
	
	@Test
	public void testRetrieveContactInformation() throws ApplicationException {		
		
		int billingAccountNumber=81;
		ContactPropertyInfo contactPropertyInfo=contactDao.retrieveContactInformation(billingAccountNumber);
		assertEquals("4163333336",contactPropertyInfo.getBusinessPhoneNumber());
	}

}
