//package com.telus.cmb.account.lifecyclemanager.dao.impl;
//
//import static org.junit.Assert.fail;
//
//import java.lang.reflect.InvocationTargetException;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import com.telus.api.ApplicationException;
//import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
//import com.telus.cmb.common.identity.ClientIdentity;
//import com.telus.eas.account.info.LMSLetterRequestInfo;
//import com.telus.eas.framework.info.LMSRequestInfo;
//
//@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
//public class LetterDaoImplIntTest extends BaseLifecycleManagerIntTest {
////	@Autowired
////	LetterDaoImpl dao;
////	String sessionId;
////
////	@Before
////	public void setup() throws ApplicationException {
////		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
////		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
////	}
////	@Test
////	public void testCreateManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
////		LMSLetterRequestInfo letterRequestInfo =new  LMSLetterRequestInfo();
////		letterRequestInfo.setBanId(1233);
////		letterRequestInfo.setId(10);
////		try{
////			dao.createManualLetterRequest(letterRequestInfo, sessionId);
////			fail("Exception expected");
////		}catch(Throwable t){
////			t.printStackTrace();
////		}
////	}
////	@Test
////	public void testRemoveManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
////		int ban=2261;
////		int reqNum=2;
////		try{
////			dao.removeManualLetterRequest(ban, reqNum, sessionId);
////			fail("Exception expected");
////		}catch(Throwable t){
////			t.printStackTrace();
////		}
////	}
////	
////	@Test
////	public void testcreateManualLetterRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
////		LMSRequestInfo letterRequestInfo =new  LMSRequestInfo();
////		letterRequestInfo.setBan(1233);
////		letterRequestInfo.setSubscriberId("923923932");
////		try{
////			dao.createManualLetterRequest(letterRequestInfo, sessionId);
////			fail("Exception expected");
////		}catch(Throwable t){
////			t.printStackTrace();
////		}
////	}
//
//}
