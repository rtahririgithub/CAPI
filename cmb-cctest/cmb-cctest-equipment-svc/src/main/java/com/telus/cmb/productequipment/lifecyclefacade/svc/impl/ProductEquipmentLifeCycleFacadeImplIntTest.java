package com.telus.cmb.productequipment.lifecyclefacade.svc.impl;


import java.rmi.RemoteException;
import java.util.Hashtable;
import javax.naming.Context;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.telus.api.ApplicationException;
import com.telus.api.equipment.Warranty;
import com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome;
import com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperRemote;
import com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint;
import com.telus.eas.equipment.info.CellularDigitalEquipmentUpgradeInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.framework.info.TestPointResultInfo;


public class ProductEquipmentLifeCycleFacadeImplIntTest {
	
	ProductEquipmentLifecycleFacadeRemote facadeImpl = null;
	ProductEquipmentHelperRemote helperImpl = null;
	ProductEquipmentLifecycleFacadeTestPoint producttestpoint = null;
	//String url="t3://localhost:7001";
	String url="t3://ln99231:30022"; //Pt148
	//String url="t3://ln98556:31022";
//String url="t3://ln98550:12022";
	

	
	@Before
	public void setup() throws Exception {
		System.setProperty("com.telusmobility.config.java.naming.provider.url"
				,"ldap://ldapread-qa.tmi.telus.com:589/cn=qa_81,o=telusconfiguration");
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
		producttestpoint = (ProductEquipmentLifecycleFacadeTestPoint) context.lookup("ProductEquipmentLifecycleFacade#com.telus.cmb.productequipment.lifecyclefacade.svc.ProductEquipmentLifecycleFacadeTestPoint");
		
		ProductEquipmentLifecycleFacadeHome productEquipmentLifecycleFacadeHome 
		= (ProductEquipmentLifecycleFacadeHome) context.lookup("ProductEquipmentLifecycleFacade#com.telus.cmb.productequipment.lifecyclefacade.svc.impl.ProductEquipmentLifecycleFacadeHome");
		facadeImpl = (ProductEquipmentLifecycleFacadeRemote)productEquipmentLifecycleFacadeHome.create();
		
		ProductEquipmentHelperHome productEquipmentHelperHome 
		= (ProductEquipmentHelperHome) context.lookup("ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome");
		helperImpl = (ProductEquipmentHelperRemote)productEquipmentHelperHome.create();
		
		
	}
	
	@Test
	public void testGetCellularDigitalEquipmentUpgrades() throws ApplicationException, RemoteException {
		System.out.println("testGetCellularDigitalEquipmentUpgrades START");
		
		String pSerialNo="21101107446";
		EquipmentInfo pEquipmentInfo=helperImpl.getEquipmentInfobySerialNo(pSerialNo);
		System.out.println("EquipmentInfo : "+pEquipmentInfo);
	    CellularDigitalEquipmentUpgradeInfo[] equip = facadeImpl.getCellularDigitalEquipmentUpgrades(pEquipmentInfo);
        System.out.println("Length "+equip.length);
	    System.out.println("testGetCellularDigitalEquipmentUpgrades END"); 
		
	}
	
	@Test
	public void testIsApple() throws ApplicationException, RemoteException {
		
		System.out.println("testIsApple START");
		String productCode="IACUP205901";
		boolean isapple = facadeImpl.isApple(productCode);
		System.out.println("isapple: "+isapple);
		System.out.println("testIsApple END"); 
		
	}
	
	@Test
	public void testAssignEquipmentToPhoneNumber() throws Exception {
		String phoneNumber = "4034850238";
		String serialNumber = "8912230000002550622";
		facadeImpl.assignEquipmentToPhoneNumber(phoneNumber, serialNumber, null);
	}
	
	@Test
	public void testGetWarrantySummaryForSuccessfulScenario() throws Exception {
		System.out.println("in");
		String serialNumber ="8912230000000261180";
		EquipmentInfo equipmentInfo=helperImpl.getEquipmentInfobySerialNo("8912230000000261180");
		String equipmentGroup=equipmentInfo.getEquipmentGroup();
        Warranty warranty=facadeImpl.getWarrantySummary(serialNumber, equipmentGroup);
        Assert.assertNotNull(warranty.getWarrantyExpiryDate());
        System.out.println("out");
	}
	@Test
	public void testGetWarrantySummaryForNonSuccessfulScenario() throws Exception {
		//For non successful scenario, there is no response from WebService
		System.out.println("in");
		String serialNumber ="900000001433763";
		EquipmentInfo equipmentInfo=helperImpl.getEquipmentInfobySerialNo("900000001433763");
		String equipmentGroup=equipmentInfo.getEquipmentGroup();
        Warranty warranty=facadeImpl.getWarrantySummary(serialNumber, equipmentGroup);
        Assert.assertNull(warranty.getWarrantyExpiryDate());
        System.out.println("out");
	}
	
	
	@Test
	public void testchangePhoneNumber() throws Exception {
		facadeImpl.changePhoneNumber("8912230000002550580", "9059995924");
	}
	
	@Test
	public void testSwapEquipmentForPhoneNumber() throws Exception {
		
		String phoneNumber = "4034850238";
		String oldUsimId = "8912230000002550622";
		String oldAssociatedHandsetIMEI = null;
		String newUsimId = "8912230000002550663";
		String newAssociatedHandsetIMEI = null;
		String oldNetworkType = "C";
		String newNetworkType = "C";
		facadeImpl.swapEquipmentForPhoneNumber(phoneNumber, oldUsimId, oldAssociatedHandsetIMEI, oldNetworkType, newUsimId, newAssociatedHandsetIMEI, newNetworkType);
	
	}
	
	@Test
	public void testMarkEquipmentStolen() throws Exception {
		String serialnumber = "8912230000000068239";
		String equipmentGroup = "USIM";
		facadeImpl.markEquipmentStolen(serialnumber, equipmentGroup);
	}
	
	@Test
	public void testMarkEquipmentFound() throws Exception {
		String serialnumber = "8912230000000068239";
		String equipmentGroup = "USIM";
		facadeImpl.markEquipmentFound(serialnumber, equipmentGroup);
	}
	@Test
	public void testMarkEquipmentLost() throws Exception {
		String serialnumber = "8912230000000068239";
		String equipmentGroup = "USIM";
		facadeImpl.markEquipmentLost(serialnumber, equipmentGroup);
	}

	@Test
	public void testApproveReservedEquipmentForPhoneNumber() throws Exception {
		String serialnumber = "24700260120";
		String phoneNumber = "4168940729";
		facadeImpl.approveReservedEquipmentForPhoneNumber(phoneNumber, serialnumber, null);
	}
	@Test
	public void testReleaseReservedEquipmentForPhoneNumber() throws Exception {
		String serialnumber = "24700260120";
		String phoneNumber = "4168940729";
		facadeImpl.releaseReservedEquipmentForPhoneNumber(phoneNumber, serialnumber, null);
	}
	
	@Test
	public void testdisassociateEquipmentFromPhoneNumber() throws Exception {
		String serialnumber = "24700260120";
		String phoneNumber = "4168940729";
		facadeImpl.disassociateEquipmentFromPhoneNumber(phoneNumber, serialnumber);
	}
	@Test
	public void testswapHSPAOnlyEquipmentForPhoneNumber() throws Exception {
		String oldSerialNumber = "24700260120";
		String newSerialNumber = "21101107173";
		String phoneNumber = "4168940729";
		facadeImpl.swapHSPAOnlyEquipmentForPhoneNumber(phoneNumber, oldSerialNumber, newSerialNumber, null, null);
	}
	
	@Test
	public void asyncAssignEquipmentToPhoneNumber() throws Exception {
		String phoneNumber = "4168940729";
		String serialNumber = "8912230000000068239";
		facadeImpl.asyncAssignEquipmentToPhoneNumber(phoneNumber, serialNumber, null);
		
	}
	
	@Test
	public void testNrtEligibilityManagerEJB() throws Exception {
		try{
			TestPointResultInfo tpr = producttestpoint.testNrtEligibilityManagerEJB("111111111111111111");
			String exceptedError = "com.telusmobility.nrt.NRTException: com.telusmobility.nrt.NRTException: ESN: 111111111111111111 not eligible for a reflash";
			System.out.println(tpr.isPass());
		}catch (Throwable e) {
			//String exceptedError = "com.telusmobility.nrt.NRTException: com.telusmobility.nrt.NRTException: ESN: 111111111111111111 not eligible for a reflash";
			//System.out.println(e.getMessage().contains(exceptedError));
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEquipmentLifeCycleManagementService_1_0() throws Exception {
		try {
			TestPointResultInfo tpr = producttestpoint.testEquipmentLifeCycleManagementService();
			System.out.println(tpr.isPass());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	}
