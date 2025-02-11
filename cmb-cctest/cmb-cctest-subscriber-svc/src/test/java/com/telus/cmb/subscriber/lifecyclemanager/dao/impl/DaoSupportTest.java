package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import amdocs.APILink.datatypes.ImsiInfo;

import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.ProfileInfo;

/**
 * 
 * @author x119951
 * 
 * Below 3 test cases are for testing V3SIM change in DaoSupport class.  
 */
public class DaoSupportTest {
	private static final String LOCAL_IMSI = "1234567890";
	private static final String REMOTE_IMSI = "0987654321";
	private static final byte TRANSACTION_TYPE = 'b';
	
	EquipmentInfo eInfo;
	ProfileInfo pInfo;
	
	@Before
	public void setup() {
		eInfo = new EquipmentInfo();
		pInfo = new ProfileInfo();
		eInfo.setProfile(pInfo);
		pInfo.setLocalIMSI(LOCAL_IMSI);
	}

	@Test
	public void extractImsiInfoWithoutRemoteSIM() {
		pInfo.setRemoteIMSI(null);
		ImsiInfo[] ret = DaoSupport.extractImsiInfo(eInfo, TRANSACTION_TYPE);
		assertNotNull(ret);
		assertEquals(ret.length, 1);
		assertEquals(ret[0].imsi, LOCAL_IMSI);
	}
	
	@Test
	public void extractImsiInfoWithEmptyRemoteSIM() {
		pInfo.setRemoteIMSI("");
		ImsiInfo[] ret = DaoSupport.extractImsiInfo(eInfo, TRANSACTION_TYPE);
		assertNotNull(ret);
		assertEquals(ret.length, 1);
		assertEquals(ret[0].transactionType, TRANSACTION_TYPE);
	}

	@Test
	public void extractImsiInfoWithRemoteSIM() {
		pInfo.setRemoteIMSI(REMOTE_IMSI);
		ImsiInfo[] ret = DaoSupport.extractImsiInfo(eInfo, TRANSACTION_TYPE);
		assertNotNull(ret);
		assertEquals(ret.length, 2);
		assertEquals(ret[1].imsi, REMOTE_IMSI);
		assertEquals(ret[1].transactionType, TRANSACTION_TYPE);
	}

}
