package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.account.info.FeeWaiverInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;

@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class AdjustmentDaoImplIntTest extends BaseLifecycleManagerIntTest {

	@Autowired
	AdjustmentDaoImpl dao;

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}
	@Test
	public void testRetrieveFeeWaivers() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 8;
		try{
			dao.retrieveFeeWaivers(ban, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	

	@Test
	public void testapplyFeeWaiver() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		FeeWaiverInfo feeWaiverInfo=new FeeWaiverInfo();
		feeWaiverInfo.setBanId(234523);
		feeWaiverInfo.setReasonCode("ABC");

		try{
			dao.applyFeeWaiver(feeWaiverInfo, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}

	@Test
	public void testreverseCreditForSubscriber(){
		
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(23454354);
		String reversalReasonCode = "";
		String memoText="";
		boolean overrideThreshold=false;
		
		try{
			dao.reverseCreditForSubscriber(creditInfo, reversalReasonCode, memoText, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}

	}
	
	@Test
	public void testreverseCreditForBan(){
		
		CreditInfo creditInfo = new CreditInfo();
		creditInfo.setBan(23454354);
		String reversalReasonCode = "";
		String memoText="";
		boolean overrideThreshold=false;
		
		try{
			dao.reverseCreditForBan(creditInfo, reversalReasonCode, memoText, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testdeleteChargeForSubscriber(){
		
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		String deletionReasonCode="";
		String memoText="";
		boolean overrideThreshold=false;
		
		try{
			dao.deleteChargeForSubscriber(chargeInfo, deletionReasonCode, memoText, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testdeleteChargeForBan(){
		
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		String deletionReasonCode="";
		String memoText="";
		boolean overrideThreshold=false;
		
		try{
			dao.deleteChargeForBan(chargeInfo, deletionReasonCode, memoText, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testapplyChargeToAccountForSubscriber(){
		
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		boolean overrideThreshold=false;
		
		try{
			dao.applyChargeToAccountForSubscriber(chargeInfo, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testapplyChargeToAccountForBan(){
		
		ChargeInfo chargeInfo=new ChargeInfo();
		chargeInfo.setBan(5646323);
		boolean overrideThreshold=false;
		
		try{
			dao.applyChargeToAccountForBan(chargeInfo, overrideThreshold, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
}
