package com.telus.cmb.productequipment.manager.svc.impl;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;


public class ProductEquipmentManagerImplIntTest {
	
	ProductEquipmentManagerRemote managerImpl = null;
	//String url="t3://ln98550.corp.ads:12022";
	String url="t3://ln98557:30022";
	
	@Before
	public void setup() throws Exception {
		javax.naming.Context context = new javax.naming.InitialContext(setEnvContext());
		getRemoteObject(context);		
	}

	private Hashtable<Object,Object> setEnvContext(){
		Hashtable<Object,Object> env = new Hashtable<Object,Object>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		env.put(Context.PROVIDER_URL, url);
		return env;
	}
	
	private void getRemoteObject(Context context) throws Exception{
		ProductEquipmentManagerHome productEquipmentManagerHome 
		= (ProductEquipmentManagerHome) context.lookup("ProductEquipmentManager#com.telus.cmb.productequipment.manager.svc.impl.ProductEquipmentManagerHome");
		managerImpl = (ProductEquipmentManagerRemote)productEquipmentManagerHome.create();
	}

//	@Test
//	public void testResetUserPassword() throws ApplicationException {
//		System.out.println("testResetUserPassword START");
//		try{
//           
//			String channelCode = "01054";
//			String userCode = "00001";
//			String newPassword = "9999";
//    		
//    		managerImpl.resetUserPassword(channelCode, userCode, newPassword);
//    		
//			System.out.println("testResetUserPassword END"); 
//		} catch (Throwable t){
//			t.printStackTrace();
//		}
//	}

	
	
	@Test
	public void testInsertAnalogEquipment() throws ApplicationException, RemoteException {
		System.out.println("testInsertAnalogEquipment START");
		try{
            managerImpl.insertAnalogEquipment("21906091383","00001");
    		
			System.out.println("testInsertAnalogEquipment END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testSetCardStatus() throws ApplicationException, RemoteException {
		System.out.println("testSetCardStatus START");
		try{
			String serialNo="10407367764";
			int statusId=102;
			String user="ClientAPI";
			
            managerImpl.setCardStatus(serialNo, statusId, user);
    		
			System.out.println("testSetCardStatus END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testCreditCard() throws ApplicationException, RemoteException {
		System.out.println("testCreditCard START");
		try{
			String serialNo="24700401235";
			String user="clientapi";
			int ban=17605;
			String phoneNumber="4034850238";
			String equipmentSerialNo="10407367764";
			boolean autoRenewInd=true;
			
            managerImpl.creditCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
    		
			System.out.println("testCreditCard END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testActivateCard() throws ApplicationException, RemoteException {
		System.out.println("testActivateCard START");
		try{
			String serialNo="30900874236";
			String user="telus";
			int ban=70521436;
			String phoneNumber="9058019871";
			String equipmentSerialNo="07605091190";
			boolean autoRenewInd=true;
			
			
            managerImpl.activateCard(serialNo, ban, phoneNumber, equipmentSerialNo, autoRenewInd, user);
    		
			System.out.println("testActivateCard END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testGetMasterLockbySerialNo() throws ApplicationException, RemoteException {
		System.out.println("testGetMasterLockbySerialNo START");
		
		String pSerialNo="24113041693";
		String pUserID="NRT_APP";
		long pLockReasonID=7;
        
		assertEquals("016522",managerImpl.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID));
			
		System.out.println("testGetMasterLockbySerialNo END"); 
		
	}
	
	@Test
	public void testGetMasterLockbySerialNo1() throws ApplicationException, RemoteException {
		System.out.println("testGetMasterLockbySerialNo1 START");
		
		String pSerialNo="24113041693";
		String pUserID="NRT_APP";
		long pLockReasonID=7;
		long pOutletID=10000214;
		long pChnlOrgID=10000211;
        
		assertEquals("016522",managerImpl.getMasterLockbySerialNo(pSerialNo, pUserID, pLockReasonID, pOutletID, pChnlOrgID));
		
		System.out.println("testGetMasterLockbySerialNo1 END"); 
		
	}
	
	@Test
	public void testInsertPagerEquipment() throws ApplicationException, RemoteException {
		System.out.println("testInsertPagerEquipment START");

		String pSerialNo="519K1304";
		String pCapCode="E1531248";
		String pEncoderFormat="P";
		String pFrequencyCode="21";
		String pModelType="T";
		String pUserID="pt148";
		
		managerImpl.insertPagerEquipment(pSerialNo, pCapCode, pEncoderFormat, pFrequencyCode, pModelType, pUserID);
		
		
		System.out.println("testInsertPagerEquipment END"); 
		
	}
	
	@Test
	public void testUpdateStatus() throws ApplicationException, RemoteException {
		System.out.println("testUpdateStatus START");

		String pSerialNo="21101107129";
		String pUserID="telus";
		long pEquipmentStatusTypeID=3;
		long pEquipmentStatusID=11;
		String pTechType="PCS";
		long pProductClassID=10000100;
		
		managerImpl.updateStatus(pSerialNo, pUserID, pEquipmentStatusTypeID, pEquipmentStatusID, pTechType, pProductClassID);
		
		System.out.println("testUpdateStatus END"); 
		
	}
	
	@Test
	public void testSetSIMMule() throws ApplicationException, RemoteException {
		System.out.println("testSetSIMMule START");
		
		String sim="000800052473300";
		String mule="";
		Date activationDate=null;
		String eventType="ACT";//ACT, RESVD
		
		managerImpl.setSIMMule(sim, mule, activationDate, eventType);
		
    	System.out.println("testSetSIMMule END"); 
		
	}
	
	@Test
	public void testStartSIMMuleRelation() throws ApplicationException, RemoteException {
		System.out.println("testSetSIMMule START");
		
		String sim="000800052473300";
		Date activationDate=new Date();
		
		managerImpl.startSIMMuleRelation(sim, activationDate);
		
    	System.out.println("testSetSIMMule END"); 
		
	}
	@Test
	public void testActivateSIMMule() throws ApplicationException, RemoteException {
		System.out.println("testSetSIMMule START");
		
		String sim="000800052473300";
		String mule="";
		Date activationDate=new Date();
		
		managerImpl.activateSIMMule(sim, mule, activationDate);
		
    	System.out.println("testSetSIMMule END"); 
		
	}
}
