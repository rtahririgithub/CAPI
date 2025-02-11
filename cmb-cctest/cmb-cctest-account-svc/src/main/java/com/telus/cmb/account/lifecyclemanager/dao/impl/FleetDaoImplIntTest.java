package com.telus.cmb.account.lifecyclemanager.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.account.info.FleetInfo;


public class FleetDaoImplIntTest extends BaseLifecycleManagerIntTest {
	
	@Autowired
	FleetDaoImpl fleetDao; 
	
	String sessionId;
	
	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = fleetDao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	
	@Test
	public void testAddFleet() throws ApplicationException {		
		FleetInfo fleetInfo = new FleetInfo();
		fleetInfo.getIdentity0().setFleetId(131072);
		fleetInfo.getIdentity0().setUrbanId(905);
		fleetDao.addFleet(70107112, (short)0, fleetInfo, 0, sessionId);
	}

}
