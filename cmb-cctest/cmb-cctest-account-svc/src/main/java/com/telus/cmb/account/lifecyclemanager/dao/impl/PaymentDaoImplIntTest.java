package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.PaymentTransfer;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;


public class PaymentDaoImplIntTest extends BaseLifecycleManagerIntTest {
	@Autowired
	PaymentDaoImpl dao;

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	@Test
	public void testUpdateTransferPayment() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 8;
		int seqNo=2;
		String memoText ="memoTest"	;
		PaymentTransfer[] paymentTransfer = new PaymentTransfer[0]; 
		try{
			dao.updateTransferPayment( ban, seqNo, paymentTransfer, true, memoText, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	@Test
	public void testRefundPaymentToAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 8;
		int paymentSeq=2;
		String memoText ="memoTest"	;
		boolean isManual = false;
		String authorizationCode="test";
		String reasonCode="RC";
		try{
			dao.refundPaymentToAccount(ban, paymentSeq, reasonCode, memoText, isManual, authorizationCode, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

}
