package com.telus.cmb.account.lifecyclemanager.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;

@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class SubscriberDaoImplIntTest extends BaseLifecycleManagerIntTest {
	
	
	@Autowired
	SubscriberDaoImpl dao;

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	@Test
	public void testSuspendSubscribers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {
		
		int ban = 20002207;
		Date activityDate = new Date();
		String activityReasonCode = "VAD";
		String[] subscriberId = new String[1];
		String userMemoText = "user memo test";
		
		try{
			dao.suspendSubscribers(ban, activityDate, activityReasonCode, subscriberId, userMemoText, sessionId);
			
		}catch(Exception e){
			e.printStackTrace();
		}
     }
	
	
	@Test
	public void restoreSuspendedSubscribers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, RemoteException {
		
		int ban = 20001552;
		Date restoreDate = new Date();
		String restoreReasonCode = "PVR";
		String[] subscriberId = new String[1];
		String restoreComment = "restore";
		
		try{
			dao.restoreSuspendedSubscribers(ban, restoreDate, restoreReasonCode, subscriberId, restoreComment, sessionId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
