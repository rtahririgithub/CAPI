package com.telus.cmb.account.lifecyclemanager.dao.impl;

import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.telus.api.ApplicationException;
import com.telus.cmb.account.lifecyclemanager.BaseLifecycleManagerIntTest;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.account.info.ConsumerNameInfo;
import com.telus.eas.account.info.FutureStatusChangeRequestInfo;

@ContextConfiguration(locations = {"classpath:application-context-lifecyclemanager-test.xml"})
public class AccountDaoImplIntTest extends BaseLifecycleManagerIntTest{

	@Autowired
	AccountDaoImpl dao;

	String sessionId;

	@Before
	public void setup() throws ApplicationException {
		ClientIdentity clientIdentity = new ClientIdentity("18654", "apollo", "OLN");
		sessionId = dao.getAmdocsTemplate().getSessionManager().openSession(clientIdentity);
	}

	@Test
	public void testupdateNationalGrowth() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 2261;
		String nationalGrowthIndicator="Simple";
		String homeProvince="Test";
		try{
			dao.updateNationalGrowth(ban, nationalGrowthIndicator, homeProvince, sessionId);
			fail("Exception expected");
		}catch(Throwable t){

		}
	}

	@Test	
	public void testrestoreSuspendedAccount1() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 82;
		String restoreReasonCode="SimpleTest";

		try{
			dao.restoreSuspendedAccount(ban, new Date(),restoreReasonCode,"",false, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
		}
	}

	@Test	
	public void testrestoreSuspendedAccount2() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 2263;
		Date restoreDate= new Date();
		String restoreComment="Simple";
		boolean collectionSuspensionsOnly = false;
		String restoreReasonCode="Test";

		try{
			dao.restoreSuspendedAccount(ban, restoreDate, restoreReasonCode, restoreComment, collectionSuspensionsOnly, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
		}
	}

	@Test
	public void testupdateFutureStatusChangeRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 2264;
		FutureStatusChangeRequestInfo futureStatusChangeRequestInfo=new FutureStatusChangeRequestInfo();
		futureStatusChangeRequestInfo.setActivityCode("abcdef");

		try{
			dao.updateFutureStatusChangeRequest(ban, futureStatusChangeRequestInfo, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
		}
	}
	
	@Test
	public void testSuspendAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {


		int ban = 82;
		String userMemoText="memo input test";
		String activityReasonCode="NB";
		Date activityDate=new Date();
		String sessionId = "246135";
		try{
		dao.suspendAccount(ban, activityDate, activityReasonCode, userMemoText, sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
		
	}
	@Test
	public void testRetrieveFutureStatusChangeRequest() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 2264;
		try{
			dao.retrieveFutureStatusChangeRequests(ban, sessionId);
			fail("Exception expected");
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testCancelAccount() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		int ban = 82;
		String userMemoText="memo input test";
		String activityReasonCode="NB";
		Date activityDate=new Date();
		String depositReturnMethod="R";
		String waiveReason="";
		boolean isPortActivity= false;
		
		try{
			dao.cancelAccount(ban, activityDate, activityReasonCode, depositReturnMethod, waiveReason, userMemoText,isPortActivity, sessionId);
		}catch(Throwable t){
			t.printStackTrace();
		}
		
	}
	
	@Test
	public void testupdateAuthorizationNames() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 82;
		ConsumerNameInfo[] authorizationNames = new ConsumerNameInfo[1];
		authorizationNames[0]=new ConsumerNameInfo();
		authorizationNames[0].setFirstName("SIMPLE");
		authorizationNames[0].setLastName("TEST");
		
		try{
			
			dao.updateAuthorizationNames(ban, authorizationNames, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testupdateAutoTreatment() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 82;
		boolean holdAutoTreatment=false;
		try{
			
			dao.updateAutoTreatment(ban, holdAutoTreatment, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testupdateBrand() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 82;
		int brandId=0;
		String memoText="";
		
		try{
			
			dao.updateBrand(ban, brandId, memoText, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testupdateSpecialInstructions() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 82;
		String specialInstructions="";
		try{
			dao.updateSpecialInstructions(ban, specialInstructions, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}

	@Test
	public void testchangePostpaidConsumerToPrepaidConsumer() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 234523;
		short prepaidBillCycle=0;
		try{
			dao.changePostpaidConsumerToPrepaidConsumer(ban, prepaidBillCycle, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}

	@Test
	public void testretrieveDiscounts() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 5645322;
		try{
			dao.retrieveDiscounts(ban, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}

	@Test
	public void testcancelAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 35662121;
		String activityReasonCode="";
		Date activityDate=new Date();
		boolean portOutInd=false;
		boolean isBrandPort=false;
		try{
			dao.cancelAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, isBrandPort, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
	
	@Test
	public void testsuspendAccountForPortOut() throws ApplicationException, IllegalArgumentException, SecurityException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		int ban = 35662121;
		String activityReasonCode="";
		Date activityDate=new Date();
		String portOutInd="";
		try{
			dao.suspendAccountForPortOut(ban, activityReasonCode, activityDate, portOutInd, sessionId);
			fail("Exception expected");
		}catch(Throwable t){t.printStackTrace();
		}
	}
}
