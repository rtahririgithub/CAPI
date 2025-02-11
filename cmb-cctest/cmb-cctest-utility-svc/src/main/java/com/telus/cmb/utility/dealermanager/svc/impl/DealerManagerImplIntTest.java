package com.telus.cmb.utility.dealermanager.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CPMSDealerInfo;
import com.telus.eas.utility.info.DealerInfo;
import com.telus.eas.utility.info.SalesRepInfo;


public class DealerManagerImplIntTest {
	
	DealerManagerRemote managerImpl = null;
@Test
	public void testAddDealer() throws ApplicationException, RemoteException {
		System.out.println("testAddDealer START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		DealerInfo dealerInfo = new DealerInfo();
    		dealerInfo.setCode("TESTA00002");
    		dealerInfo.setDepartmentCode("U115");
    		dealerInfo.setDescription("Test Dealer2");
    		dealerInfo.setDescriptionFrench("Test Dealer2");
    		dealerInfo.setEffectiveDate(new Date());
    		dealerInfo.setName("Test Dealer2");
    		dealerInfo.setNumberLocationCD("TLS");
    		
    		managerImpl.addDealer(dealerInfo, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testAddDealer END"); 
	}
	
@Test
	public void testAddSalesperson() throws ApplicationException, RemoteException {
		System.out.println("testAddSalesperson START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://um-generalutilities-dv103.tmi.telus.com:12152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		SalesRepInfo salesRepInfo = new SalesRepInfo();
    		salesRepInfo.setCode("5555");
    		salesRepInfo.setDealerCode("TESTA00001");
    		salesRepInfo.setDescription("Test Dealer");
    		salesRepInfo.setDescriptionFrench("Test Dealer");
    		salesRepInfo.setEffectiveDate(new Date());
    		salesRepInfo.setName("Test SalePerson");
    		
    		managerImpl.addSalesperson(salesRepInfo, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testAddSalesperson END"); 
	}
	
@Test
	public void testChangeDealerName() throws ApplicationException, RemoteException {
		System.out.println("testChangeDealerName START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "TESTA00001";
    		String dealerName = "Test Dealer";
    		managerImpl.changeDealerName(dealerCode, dealerName, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testChangeDealerName END"); 
	}	
	
@Test
	public void testChangeSalespersonName() throws ApplicationException, RemoteException {
		System.out.println("testChangeSalespersonName START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "B00AB00003";
    		String salesCode = "0001";
    		String salesName = "Sales Rep 148 - modified";

    		managerImpl.changeSalespersonName(dealerCode, salesCode, salesName, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testChangeSalespersonName END"); 
	}
	
  @Test
	public void testExpireDealer() throws ApplicationException, RemoteException {
		System.out.println("testExpireDealer START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "B00AB00003";
    		Date endDate = new Date();

    		managerImpl.expireDealer(dealerCode, endDate, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testExpireDealer END"); 
	}				

	
 @Test
	public void testExpireSalesperson() throws ApplicationException, RemoteException {
		System.out.println("testExpireSalesperson START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "B00AB00003";
    		String salesCode = "0001";
    		Date endDate = new Date();

    		managerImpl.expireSalesperson(dealerCode, salesCode, endDate, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testExpireSalesperson END"); 
	}	
	
 @Test
	public void testUnexpireDealer() throws ApplicationException, RemoteException {
		System.out.println("testUnexpireDealer START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "B00AB00003";

    		managerImpl.unexpireDealer(dealerCode, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testUnexpireDealer END"); 
	}
	
  @Test
	public void testUnexpireSalesperson() throws ApplicationException, RemoteException {
		System.out.println("testUnexpireSalesperson START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String dealerCode = "B00AB00003";
    		String salesCode = "0001";

    		managerImpl.unexpireSalesperson(dealerCode, salesCode, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testUnexpireSalesperson END"); 
	}		

	@Test
	public void testTransferSalesperson() throws ApplicationException, RemoteException {
		System.out.println("testTransferSalesperson START"); 

		try {
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
    		String sessionId = managerImpl.openSession("18654", "apollo", "OLN");
    		String newDealerCode = "B00AB00003";
    		Date transferDate = new Date();
    		SalesRepInfo salesInfo = new SalesRepInfo();
    		salesInfo.setCode("0001");
    		salesInfo.setDealerCode("B00AB00003");
    		salesInfo.setDescription("New Test Sales Person");
    		salesInfo.setDescriptionFrench("New Test Sales Person");
    		salesInfo.setEffectiveDate(new Date());
    		salesInfo.setName("New SalesPerson");
    		
    		
    		managerImpl.transferSalesperson(salesInfo, newDealerCode, transferDate, sessionId);
		} catch (Throwable t){
			t.printStackTrace();
		}
		System.out.println("testTransferSalesperson END"); 
	}			
	
@Test
	public void testResetUserPassword() throws ApplicationException {
		System.out.println("testResetUserPassword START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			String channelCode = "01054";
			String userCode = "00001";
			String newPassword = "9999";
    		
    		managerImpl.resetUserPassword(channelCode, userCode, newPassword);
    		
			System.out.println("testResetUserPassword END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	

	@Test
	public void testChangeUserPassword() throws ApplicationException {
		System.out.println("testChangeUserPassword START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			String channelCode = "01054";
			String userCode = "00001";
			String oldPassword = "9999";
			String newPassword = "8888";
    		
    		managerImpl.changeUserPassword(channelCode, userCode, oldPassword, newPassword);
    		
			System.out.println("testChangeUserPassword END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	
  @Test
	public void testValidUser() throws ApplicationException {
		System.out.println("testValidUser START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			String channelCode = "01054";
			String userCode = "00001";
			String password = "8888";

    		
    		managerImpl.validUser(channelCode, userCode, password);
    		
			System.out.println("testValidUser END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	
	
	
  @Test
	public void n() throws ApplicationException {
		System.out.println("testGetCPMSDealerByKBDealerCode START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			String pKBDealerCode = "B00AB00003";
			String pKBSalesRepCode = "0001";
    		
			CPMSDealerInfo pCPMSDealerInfo = 
				managerImpl.getCPMSDealerByKBDealerCode(pKBDealerCode, pKBSalesRepCode);
    		
			System.out.println("CPMSDealerInfo = " + pCPMSDealerInfo);
			System.out.println("testGetCPMSDealerByKBDealerCode END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}			
	
	@Test
	public void testGetCPMSDealerByLocationTelephoneNumber() throws ApplicationException {
		System.out.println("testGetCPMSDealerByLocationTelephoneNumber START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			String pLocationTelephoneNumber = "5196590850";
    		
			CPMSDealerInfo pCPMSDealerInfo = 
				managerImpl.getCPMSDealerByLocationTelephoneNumber(pLocationTelephoneNumber);
    		
			System.out.println("CPMSDealerInfo = " + pCPMSDealerInfo);
			System.out.println("testGetCPMSDealerByLocationTelephoneNumber END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testGetCPMSDealerInfo() throws ApplicationException {
		System.out.println("testGetCPMSDealerInfo START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
//			String pChannelCode = "01054";
//			String pUserCode = "00001";
			String pChannelCode = "";
			String pUserCode = "";
 		
			CPMSDealerInfo pCPMSDealerInfo = 
				managerImpl.getCPMSDealerInfo(pChannelCode, pUserCode);
    		
			System.out.println("CPMSDealerInfo = " + pCPMSDealerInfo);
			System.out.println("testGetCPMSDealerInfo END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	
	
	@Test
	public void testGetChnlOrgAssociation() throws ApplicationException {
		System.out.println("testGetChnlOrgAssociation START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			long chnlOrgId = 106782655;
			String associateReasonCd = "SH";

			long [] chnlOrgAssociation = managerImpl.getChnlOrgAssociation(chnlOrgId, associateReasonCd);
    		for (int i =0; i < chnlOrgAssociation.length; i++ ){
    			System.out.println("ChnlOrgAssociation[" + "i}" + " = " + chnlOrgAssociation[i]);
    		}
			System.out.println("testGetChnlOrgAssociation END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	
	
	@Test
	public void testGetChnlOrgAssociationSAPSoldToParty() throws ApplicationException {
		System.out.println("testGetChnlOrgAssociationSAPSoldToParty START");
		try{
            Hashtable<Object,Object> env = new Hashtable<Object,Object>();
    		env.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
    		env.put(Context.PROVIDER_URL, "t3://sn25257:30152");

    		javax.naming.Context context = new javax.naming.InitialContext(env);

    		DealerManagerHome dealerManagerHome 
    		= (DealerManagerHome) context.lookup("DealerManager#com.telus.cmb.utility.dealermanager.svc.impl.DealerManagerHome");
    		managerImpl = (DealerManagerRemote)dealerManagerHome.create();
			long chnlOrgId = 10002528;
			
			long [] chnlOrgAssociation = managerImpl.getChnlOrgAssociationSAPSoldToParty(chnlOrgId);
    		
    		for (int i =0; i < chnlOrgAssociation.length; i++ ){
    			System.out.println("ChnlOrgAssociation[" + "i}" + " = " + chnlOrgAssociation[i]);
    		}
    		System.out.println("testGetChnlOrgAssociationSAPSoldToParty END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}					
	
}	
	
