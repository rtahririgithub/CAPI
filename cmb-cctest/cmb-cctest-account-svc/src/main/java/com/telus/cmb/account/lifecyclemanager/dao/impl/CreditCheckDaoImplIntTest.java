package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;

@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class CreditCheckDaoImplIntTest extends BaseLifecycleManagerIntTest {
	@Autowired
	CreditCheckDaoImpl dao;

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	@Test
	public void testUpdateCreditClass() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 8;
		String newCreditClass="Test";
		String memoText ="memoTest"	;
		try{
			dao.updateCreditClass(ban, newCreditClass, memoText, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

}
