package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.subscriber.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.eas.account.info.AddressInfo;

@RunWith(SpringJUnit4ClassRunner.class)
public class AddressDaoImplIntTest extends BaseLifecycleManagerIntTest {

	@Autowired
	AddressDaoImpl dao;
	
	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		//System.setProperty("cmb.services.amdocs.url", "t3://corsair:5001");
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	
	@Test
	public void testUpdateAddress() throws ApplicationException{
		System.out.println("testUpdateAddress Start");
		try{
			int ban=8;
			String subscriber="4033404108";
			AddressInfo addressInfo=new AddressInfo();
			addressInfo.setProvince("ON");
			addressInfo.setStreetName("58, Stoneton Dr");
			addressInfo.setPostalCode("M1h2P6");
			
			dao.updateAddress(ban, subscriber, "C", addressInfo, sessionId);
			fail("Exception expected");
		}catch(Throwable t){

		}
		System.out.println("testUpdateAddress End");
	}	
	
}
