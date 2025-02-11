package com.telus.cmb.productequipment.helper.svc.impl;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import javax.naming.Context;

import org.junit.Before;
import org.junit.Test;

import com.telus.api.ApplicationException;
import com.telus.eas.equipment.info.CardInfo;
import com.telus.eas.equipment.info.EquipmentInfo;
import com.telus.eas.equipment.info.EquipmentModeInfo;
import com.telus.eas.equipment.info.WarrantyInfo;
import com.telus.eas.framework.info.ActivationCreditInfo;
import com.telus.eas.utility.info.ServiceInfo;


public class ProductEquipmentHelperImplIntTest {
	
	ProductEquipmentHelperRemote helperImpl = null;
	//String url="t3://localhost:7001";
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
		ProductEquipmentHelperHome productEquipmentHelperHome 
		= (ProductEquipmentHelperHome) context.lookup("ProductEquipmentHelper#com.telus.cmb.productequipment.helper.svc.impl.ProductEquipmentHelperHome");
		helperImpl = (ProductEquipmentHelperRemote)productEquipmentHelperHome.create();
	}
	
	@Test
	public void testGetEquipmentInfobySerialNo() throws ApplicationException,RemoteException {
		System.out.println("testGetEquipmentInfobySerialNo START");
		
//        EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("21101107135");	// PCS Equipment
       EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("000100006978080");	// IEMI Equipment
// 		EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("300000000100223");	// HSPA Equipment
//   		EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNo("8912230000093531549");	// USIM Equipment
  		
    		
			System.out.println("EquipmentInfo = " + equip);
			
			System.out.println("ProductGroupTypeID :"+equip.getProductGroupTypeID()+" ProductTypeID : "+ equip.getProductTypeID());
			System.out.println("testGetEquipmentInfobySerialNo END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobySerialNumber() throws ApplicationException {
		System.out.println("testGetEquipmentInfobySerialNumber START");
		try{
            EquipmentInfo equip = helperImpl.getEquipmentInfobySerialNumber("21101107135");	// PCS Equipment

    		System.out.println("EquipmentInfo = " + equip);
			System.out.println("testGetEquipmentInfobySerialNumber END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	
	
	
	@Test
	public void testGetESNByPseudoESN() throws ApplicationException {
		System.out.println("testGetESNByPseudoESN START");
		try{
            String[] esns = helperImpl.getESNByPseudoESN("21101107135");	// PCS Equipment

    		System.out.println("ESNByPseudoESN = " + esns);
			System.out.println("testGetESNByPseudoESN END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}	

	
	@Test
	public void testIsValidESNPrefix() throws ApplicationException {
		System.out.println("testIsValidESNPrefix START");
		try{
            
    		boolean validESNPrefix = helperImpl.isValidESNPrefix("21101107135");	

    		System.out.println("isValidESNPrefix = " + validESNPrefix);
			System.out.println("testIsValidESNPrefix END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	
	@Test
	public void testIsVirtualESN() throws ApplicationException {
		System.out.println("testIsVirtualESN START");
		try{
            boolean virtualESN = helperImpl.isVirtualESN("21101107135");	

    		System.out.println("isVirtualESN = " + virtualESN);
			System.out.println("testIsVirtualESN END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testGetEquipmentInfobyCapCode() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyCapCode START");
		
		String capCode="E1614803";
		String encodingFormat="F";
		EquipmentInfo equip = helperImpl.getEquipmentInfoByCapCode(capCode, encodingFormat);
    	System.out.println("EquipmentInfo = " + equip);
		
    	System.out.println("testGetEquipmentInfobyCapCode END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobyProductCode() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyProductCode START");
		
		String productCode="*13A";
		EquipmentInfo equip = helperImpl.getEquipmentInfobyProductCode(productCode);
		System.out.println("EquipmentInfo = " + equip);
		
    	System.out.println("testGetEquipmentInfobyProductCode END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobySerialNo_1() throws ApplicationException {
		System.out.println("testGetEquipmentInfobySerialNo_1 START");
		try{
    	   EquipmentInfo[] equip = helperImpl.getEquipmentInfobySerialNo("21101107135", true);	// PCS Equipment
           System.out.println("Length : "+equip.length);
           for (EquipmentInfo ei : equip) {
            	System.out.println("EquipmentInfo = " + ei);
    	   }
			
			System.out.println("testGetEquipmentInfobySerialNo_1 END"); 
		} catch (Throwable t){
			t.printStackTrace();
		}
	}
	
	@Test
	public void testGetEquipmentList() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentList START");
		//Test 1
		String pTechTypeClass="PCS";
		//Test 2
//		String pTechTypeClass="MIKE";
		//Test 3
//		String pTechTypeClass="ANALOG";
		//Test 4
//		String pTechTypeClass="SIM";
		
		//Test 5
		boolean inUse=true;
		
		String startSerialNo="10000000000";
		int n=50;
//		boolean inUse=false;
		String[] equList = helperImpl.getEquipmentList(pTechTypeClass, n, inUse, startSerialNo);
		for (String el : equList) {
			System.out.println(el);
		}
    	System.out.println("testGetEquipmentList END"); 
		
	}
	
	@Test
	public void testGetEquipmentModes() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentModes START");
		
		String pProductCode="*13A";
		EquipmentModeInfo[] equModes = helperImpl.getEquipmentModes(pProductCode);
		for (EquipmentModeInfo el : equModes) {
			System.out.println("EquipmentModeInfo : "+el);
		}
    	System.out.println("testGetEquipmentModes END"); 
		
	}
	
	@Test
	public void testGetIDENShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetIDENShippedToLocation START");
		
		String serialNumber="000100006978080";
		long  shippedLocation= helperImpl.getIDENShippedToLocation(serialNumber);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetIDENShippedToLocation END"); 
		
	}
	
	@Test
	public void testGetPCSShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetPCSShippedToLocation START");
		
		String serialNumber="00000000003";
		long  shippedLocation= helperImpl.getPCSShippedToLocation(serialNumber);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetPCSShippedToLocation END"); 
		
	}
	
	@Test
	public void testGetProductFeatures() throws ApplicationException, RemoteException {
		System.out.println("testGetProductFeatures START");
		
		String pProductCode="*6600";
		String[]  features= helperImpl.getProductFeatures(pProductCode);
		for (String feature : features) {
			System.out.println("feature : "+feature);
		}
		
    	System.out.println("testGetProductFeatures END"); 
		
	}
	
	@Test
	public void testGetShippedToLocation() throws ApplicationException, RemoteException {
		System.out.println("testGetShippedToLocation START");
		
		String serialNumber="21101107130";
		long  shippedLocation= helperImpl.getShippedToLocation(serialNumber);
		System.out.println("shippedLocation : "+shippedLocation);
		
    	System.out.println("testGetShippedToLocation END"); 
		
	}
	
	@Test
	public void testIsNewPrepaidHandset() throws ApplicationException, RemoteException {
		System.out.println("testIsNewPrepaidHandset START");
		
		String serialNo="05300004542";
		String productCode="10043386";
		boolean  isNewPrepaidHandset= helperImpl.isNewPrepaidHandset(serialNo, productCode);
		System.out.println("isNewPrepaidHandset : "+isNewPrepaidHandset);
		
    	System.out.println("testIsNewPrepaidHandset END"); 
		
	}
	
	@Test
	public void testGetAssociatedHandsetByUSIMID() throws ApplicationException, RemoteException {
		System.out.println("testGetAssociatedHandsetByUSIMID START");
		
		String USIMID="8912230000093531556";
		EquipmentInfo  equipmentInfo= helperImpl.getAssociatedHandsetByUSIMID(USIMID);
		System.out.println("EquipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetAssociatedHandsetByUSIMID END"); 
		
	}
	
	@Test
	public void testGetEquipmentInfobyPhoneNo() throws ApplicationException, RemoteException {
		System.out.println("testGetEquipmentInfobyPhoneNo START");
		
		String pPhoneNo="7053210335";//PCS
		EquipmentInfo  equipmentInfo= helperImpl.getEquipmentInfobyPhoneNo(pPhoneNo);
		System.out.println("EquipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetEquipmentInfobyPhoneNo END"); 
		
	}
	
	@Test
	public void testGetIMEIBySIM() throws ApplicationException, RemoteException {
		System.out.println("testGetIMEIBySIM START");
		
		String pSimID="000800000028304";
		String  imeiID= helperImpl.getIMEIBySIM(pSimID);
		System.out.println("imeiID : "+imeiID);
		
    	System.out.println("testGetIMEIBySIM END"); 
		
	}
	
	@Test
	public void testGetMuleBySIM() throws ApplicationException, RemoteException {
		System.out.println("testGetMuleBySIM START");
		
		String pSimID="000800000028304";
		EquipmentInfo  equipmentInfo= helperImpl.getMuleBySIM(pSimID);
		System.out.println("equipmentInfo : "+equipmentInfo);
		
    	System.out.println("testGetMuleBySIM END"); 
		
	}
	
	@Test
	public void testGetSIMByIMEI() throws ApplicationException, RemoteException {
		System.out.println("testGetSIMByIMEI START");
		
		String pImeiID="000100000052057";
		String  simid= helperImpl.getSIMByIMEI(pImeiID);
		System.out.println("simid : "+simid);
		
    	System.out.println("testGetSIMByIMEI END"); 
		
	}
	
	@Test
	public void testGetWarrantyInfo() throws ApplicationException, RemoteException {
		System.out.println("testGetWarrantyInfo START");
		
		String pSerialNo="25300880026";
		WarrantyInfo  warrantyInfo= helperImpl.getWarrantyInfo(pSerialNo);
		System.out.println("warrantyInfo : "+warrantyInfo);
		
		System.out.println("WarrantyExpiryDate: "+warrantyInfo.getWarrantyExpiryDate());
	    System.out.println("InitialActivationDate :"+ warrantyInfo.getInitialActivationDate());
	    System.out.println("InitialManufactureDate"+  warrantyInfo.getInitialManufactureDate());
	    System.out.println("LatestPendingDate"+ warrantyInfo.getLatestPendingDate());
	    System.out.println("warrantyInfo"+ warrantyInfo.getLatestPendingModel());
	    System.out.println("Message"+ warrantyInfo.getMessage());
	    System.out.println("WarrantyExtensionDate"+ warrantyInfo.getWarrantyExtensionDate());
	    System.out.println("DOAExpiryDate"+ warrantyInfo.getDOAExpiryDate());
		
    	System.out.println("testGetWarrantyInfo END"); 
		
	}
	
	/* ***************** Calling from SubscriberHelperEJB. Added for testing *******************
	@Test
	public void testGetUSIMListByIMSIs() throws ApplicationException, RemoteException {
		System.out.println("testGetUSIMListByIMSIs START");
		
		
		Enumeration simidsKey=null;
		String str=null;
		String[] IMISIs={"1111112300","1111111301", "302220999918404","214078036618404"};
		Hashtable  simids= helperImpl.getUSIMListByIMSIs(IMISIs);
		
		simidsKey = simids.keys(); 
		while(simidsKey.hasMoreElements()) { 
			str = (String) simidsKey.nextElement(); 
			System.out.println(str + ": " + simids.get(str)); 
		} 
		
    	System.out.println("testGetUSIMListByIMSIs END"); 
		
	}*/
	
	
	@Test
	public void testGetCardByFullCardNo() throws ApplicationException, RemoteException {
		System.out.println("testGetCardByFullCardNo START");
		
		String fullCardNo="30900516059";
		String phoneNumber="9058019871";
		String equipmentSerialNo="076050911901234";
		String userId="12459";
		
		CardInfo  cardInfo= helperImpl.getCardByFullCardNo(fullCardNo, phoneNumber, equipmentSerialNo, userId);
		System.out.println("cardInfo : "+cardInfo);
		
    	System.out.println("testGetCardByFullCardNo END"); 
		
	}
	
	@Test
	public void testGetCardServices() throws ApplicationException, RemoteException{
		System.out.println("testGetCardServices START");
		
		String serialNo="07605091190";
		String techType="1RTT";
		String billType="POSTPAID";
		ServiceInfo[] serviceInfo= helperImpl.getCardServices(serialNo, techType, billType);
		for(ServiceInfo info:serviceInfo)
		System.out.println("serviceInfo : "+info);
		
    	System.out.println("testGetCardServices END"); 
		
	}
	
	@Test
	public void testGetCards() throws ApplicationException, RemoteException{
		System.out.println("testGetCards START");
		
		String pPhoneNo="9058019871";
		//String pCardType="FEA";
		//CardInfo[] cardInfo= helperImpl.getCards(pPhoneNo, pCardType);
		CardInfo[] cardInfo= helperImpl.getCards(pPhoneNo);
		for(CardInfo info:cardInfo)
			System.out.println("cardInfo : "+info);
		
    	System.out.println("testGetCards END"); 
		
	}
	
	@Test
	public void testRetrieveVirtualEquipment() throws ApplicationException, RemoteException {
		System.out.println("testRetrieveVirtualEquipment START");
		
		String pSerialNo="21101011103";
		String techTypeClass="VIRTUAL";
		EquipmentInfo  equipmentInfo= helperImpl.retrieveVirtualEquipment(pSerialNo, techTypeClass);
		System.out.println("equipmentInfo : "+equipmentInfo);
		
    	System.out.println("testRetrieveVirtualEquipment END"); 
		
	}
	
	@Test
	public void testRetrievePagerEquipmentInfo() throws ApplicationException, RemoteException {
		System.out.println("testRetrievePagerEquipmentInfo START");
		
		String serialNo="07605091190";
		EquipmentInfo equInfo= helperImpl.retrievePagerEquipmentInfo(serialNo);
		System.out.println("EquipmentInfo : "+equInfo);
		
    	System.out.println("testRetrievePagerEquipmentInfo END"); 
		
	}
	
	@Test
	public void testGetBaseProductPrice() throws ApplicationException, RemoteException {
		System.out.println("testGetBaseProductPrice START");
		
		String serialNumber="07605091190";
		String province="ON";
		String npa="416";
		
		double basePrice= helperImpl.getBaseProductPrice(serialNumber, province, npa);
		System.out.println("basePrice : "+basePrice);
		
    	System.out.println("testGetBaseProductPrice END"); 
		
	}
	
	@Test
	public void testGetBaseProductPriceByProductCode() throws ApplicationException, RemoteException {
		System.out.println("testGetBaseProductPrice START");
		
		String productCode="SPHA523";
		String province="MB";
		String npa="416";
		
		double basePrice= helperImpl.getBaseProductPriceByProductCode(productCode, province, npa);
		System.out.println("basePrice : "+basePrice);
		
    	System.out.println("testGetBaseProductPrice END"); 
		
	}
	
	@Test
	public void testGetAirCardByCardNo() throws ApplicationException, RemoteException {
		System.out.println("testGetAirCardByCardNo START");
		
		String fullCardNo="309012324040000";
		String phoneNumber="4162060215";
		String equipmentSerialNo="21101120760";
		String userId="12459";
		
		CardInfo cardInfo= helperImpl.getAirCardByCardNo(fullCardNo, phoneNumber, equipmentSerialNo, userId);
		System.out.println("cardInfo : "+cardInfo);
		
    	System.out.println("testGetAirCardByCardNo END"); 
		
	}
	
	@Test
	public void testGetCardBySerialNo() throws ApplicationException, RemoteException {
		System.out.println("testGetCardBySerialNo START");
		
		String serialNo="30901232404";
		
		CardInfo cardInfo= helperImpl.getCardBySerialNo(serialNo);
		System.out.println("cardInfo : "+cardInfo);
		
    	System.out.println("testGetCardBySerialNo END"); 
		
	}
	
	@Test
	public void testGetActivationCreditsByProductCode() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCreditsByProductCode START");
		
		String productCode="SPHA523";
		Date activationDate=new Date();
		String province="MB";
		String npa="416";
		int contractTermMonths=36;
		String productType="C";
		boolean isInitialActivation=false;
		boolean fidoConversion= true;
		
		ActivationCreditInfo[] activationCreditInfos= helperImpl.getActivationCreditsByProductCode(productCode, 
				province, npa, contractTermMonths, activationDate, fidoConversion, productType, isInitialActivation);
		System.out.println("ActivationCreditInfo length: "+activationCreditInfos.length);
		for (ActivationCreditInfo ac : activationCreditInfos) {
        	System.out.println("ActivationCreditInfo = " + ac);
	   }
		
    	System.out.println("testGetActivationCreditsByProductCode END"); 
		
	}
	
	@Test
	public void testGetActivationCreditsByProductCodes() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCreditsByProductCode START");
		
		String[] productCodes={"SPHA523"};
		Date activationDate=new Date();
		String province="MB";
		String npa="416";
		int contractTermMonths=36;
		String[] productTypes={"C"};
		boolean isInitialActivation=false;
		boolean fidoConversion= true;
		
		HashMap map= helperImpl.getActivationCreditsByProductCodes(productCodes, 
				province, npa, contractTermMonths, activationDate, fidoConversion, productTypes, isInitialActivation);
		 for( int i=0; i<productCodes.length; i++ ) {
			 ActivationCreditInfo[]activationCreditInfos= (ActivationCreditInfo[])map.get( productCodes[i]);
			 System.out.println("ActivationCreditInfos length: "+activationCreditInfos.length);
				for (ActivationCreditInfo ac : activationCreditInfos) {
		        	System.out.println("ActivationCreditInfo = " + ac);
			   }
		 }
		
    	System.out.println("testGetActivationCreditsByProductCode END"); 
		
	}
	
	@Test
	public void testGetActivationCredits_1() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCredits START");
		
		String province="AB";
		String npa="416";
		String creditType="ACT";
		String serialNumber="11914582716";
		
		ActivationCreditInfo[] activationCreditInfo= helperImpl.getActivationCredits(serialNumber, province, npa, creditType);
		for(ActivationCreditInfo info :activationCreditInfo)
		System.out.println("ActivationCreditInfo : "+info);
		
    	System.out.println("testGetActivationCredits END"); 
		
	}
	
	@Test
	public void testGetActivationCredits_2() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCredits START");
		
		String province="MB";
		String npa="416";
		String serialNumber="24205462132";
		int contractTermMonths=36;
		boolean fidoConversion= true;
		
		ActivationCreditInfo[] activationCreditInfo= helperImpl.getActivationCredits(serialNumber,
				province, npa, contractTermMonths, fidoConversion);
		for(ActivationCreditInfo info :activationCreditInfo)
		System.out.println("ActivationCreditInfo : "+info);
		
    	System.out.println("testGetActivationCredits END"); 
		
	}
	
	@Test
	public void testGetActivationCredits_3() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCredits START");
		
		String province="MB";
		String npa="416";
		String serialNumber="24205462132";
		int contractTermMonths=36;
		boolean fidoConversion= true;
		String pricePlan= "FNTK50M2";
		
		ActivationCreditInfo[] activationCreditInfo= helperImpl.getActivationCredits(serialNumber, 
				province, npa, contractTermMonths, pricePlan, fidoConversion);
		for(ActivationCreditInfo info :activationCreditInfo)
		System.out.println("ActivationCreditInfo : "+info);
		
    	System.out.println("testGetActivationCredits END"); 
		
	}
	
	@Test
	public void testGetActivationCredits_4() throws ApplicationException, RemoteException {
		System.out.println("testGetActivationCredits START");
		
		String province="AB";
		String npa="416";
		String serialNumber="11914582716";
		int contractTermMonths=36;
		boolean fidoConversion= true;
		
		ActivationCreditInfo[] activationCreditInfo= helperImpl.getActivationCredits(serialNumber, 
				province, npa, contractTermMonths, new Date(), fidoConversion);
		for(ActivationCreditInfo info :activationCreditInfo)
		System.out.println("ActivationCreditInfo : "+info);
		
    	System.out.println("testGetActivationCredits END"); 
		
	}
	
	
}
