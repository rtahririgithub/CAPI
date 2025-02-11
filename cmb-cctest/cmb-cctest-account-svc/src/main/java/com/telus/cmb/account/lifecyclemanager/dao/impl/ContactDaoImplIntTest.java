package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.account.info.BillingPropertyInfo;
import com.telus.eas.account.info.ContactPropertyInfo;


public class ContactDaoImplIntTest extends BaseLifecycleManagerIntTest {

	@Autowired
	ContactDaoImpl contactDao; 
	
	String sessionId;
	
	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = contactDao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	
	@Test
	public void testUpdateBillingInformation() throws ApplicationException {		
		
		int billingAccountNumber=8;
		BillingPropertyInfo billingPropertyInfo=new BillingPropertyInfo();
		billingPropertyInfo.setFullName("NEW TEST");
		billingPropertyInfo.setLegalBusinessName("TEST INC.");
		try{
			contactDao.updateBillingInformation(billingAccountNumber, billingPropertyInfo, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateContactInformation() throws ApplicationException {		
		
		int billingAccountNumber=8;
		ContactPropertyInfo contactPropertyInfo=new ContactPropertyInfo();
		contactPropertyInfo.setBusinessPhoneNumber("4163842304");
		contactPropertyInfo.setBusinessPhoneExtension("4563");
		try{
			contactDao.updateContactInformation(billingAccountNumber, contactPropertyInfo, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

}
