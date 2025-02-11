package com.telus.provider.account;

import java.util.ArrayList;
import java.util.List;

import com.telus.api.BaseTest;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.Address;
import com.telus.api.account.EnterpriseAddress;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.account.info.EnterpriseAddressInfo;

public class TMAddressIntTest extends BaseTest {

	private TMAccountManager accountManager;
	
	static {
		
	//	setupSMARTDESKTOP_D3();
		setupEASECA_QA();
	//	System.setProperty("cmb.services.AccountLifecycleFacade.url", "t3://localhost:7001");
		/*System.setProperty("cmb.services.AccountLifecycleManager.url", "t3://localhost:7001");
		System.setProperty("cmb.services.AccountInformationHelper.url", "t3://localhost:7001");
*/
		
//		System.setProperty("cmb.services.AccountLifecycleManager.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountLifecycleFacade.usedByProvider", "false");
//		System.setProperty("cmb.services.AccountInformationHelper.usedByProvider", "false");

	}
	
	public TMAddressIntTest(String name) throws Throwable {
		super(name);
	}
		
	public void setUp() throws Exception{
		super.setUp();
		accountManager = super.provider.getAccountManager0();
	}
	

	
	public void testValidate() throws TelusAPIException{
		
		System.out.println("testValidate start");
		Account account = accountManager.findAccountByBAN(6000055);
		
		TMAddress address = (TMAddress)account.getAddress();
		
		/*EnterpriseAddressInfo eai = new EnterpriseAddressInfo(new AddressInfo());
		eai.setAddressTypeCode("U");
		List aai = new ArrayList();
		aai.add(new String("test1"));
		aai.add(new String("test2"));
		eai.setAdditionalAddressInformation(aai);
		eai.setCareOf("care of");
		eai.setCountryCode("CA");
		eai.setMunicipalityName("SCARBOROUGH");
		eai.setPostOfficeBoxNumber("45");
		eai.setPostalZipCode("M1P4P1");
		eai.setProvinceStateCode("ON");
		eai.setStreetDirectionCode("NRT");
		eai.setStreetName("KENNEDY");
		eai.setStreetTypeCode("RD");
		eai.setUnitNumber("112");
		eai.setUnitTypeCode("APT");
		eai.setCivicNumber("112");
		eai.setCivicNumberSuffix("APT");
		eai.setRuralRouteNumber("45");
		eai.setRuralRouteTypeCode("MRs");
		eai.setStationName("PROGRESS");
		//  address.setCountry("CANADA");
		 * 
		 
		address.translateAddress(eai);*/
		System.out.println(address+"\n===========");
       System.out.println(address.getPrimaryLine());
     
       System.out.println(address.getCountry().trim().toUpperCase().startsWith("CA"));
       	address.validate();
       	System.out.println("testValidate End");
       	
       	/*Error:com.telus.api.account.InvalidAddressException: N-Code has a suggestion
    	at com.telus.provider.account.TMAddress.validate_aroundBody118(TMAddress.java:441)
    	at com.telus.provider.account.TMAddress.validate_aroundBody119$advice(TMAddress.java:54)
    	at com.telus.provider.account.TMAddress.validate(TMAddress.java:1)
    	*/


	}
	
	public void testTranslateAddress() throws TelusAPIException {
		
		EnterpriseAddressInfo eai = new EnterpriseAddressInfo(new AddressInfo());
		eai.setAddressTypeCode("U");
		List aai = new ArrayList();
		aai.add(new String("test1"));
		aai.add(new String("test2"));
		eai.setAdditionalAddressInformation(aai);
		eai.setCareOf("care of");
		eai.setCountryCode("CA");
		eai.setMunicipalityName("SCARBOROUGH");
		eai.setPostOfficeBoxNumber("45");
		eai.setPostalZipCode("M1P4P1");
		eai.setProvinceStateCode("ON");
		eai.setStreetDirectionCode("NRT");
		eai.setStreetName("KENNEDY");
		eai.setStreetTypeCode("RD");
		eai.setUnitNumber("112");
		eai.setUnitTypeCode("APT");
		eai.setCivicNumber("112");
		eai.setCivicNumberSuffix("APT");
		eai.setRuralRouteNumber("45");
		eai.setRuralRouteTypeCode("MRs");
		eai.setStationName("PROGRESS");
		
		Account account = accountManager.newPCSPostpaidBusinessRegularAccount();
		
		Address address = (TMAddress)account.getAddress();
		
		address.translateAddress(eai);
		
		assertEquals("ON", address.getProvince());
		assertEquals("test2", address.getSecondaryLine());
		assertEquals("F", address.getAddressType());
		assertEquals("PROGRESS", address.getRuralLocation());
		assertEquals("F", address.getRuralType());
	}
	
	public void testNewEnterpriseAddress() throws TelusAPIException{
		
		EnterpriseAddressInfo eai = new EnterpriseAddressInfo(new AddressInfo());
		eai.setAddressTypeCode("U");
		List aai = new ArrayList();
		aai.add(new String("test1"));
		aai.add(new String("test2"));
		eai.setAdditionalAddressInformation(aai);
		eai.setCareOf("care of");
		eai.setCountryCode("CA");
		eai.setMunicipalityName("SCARBOROUGH");
		eai.setPostOfficeBoxNumber("45");
		eai.setPostalZipCode("M1P4P1");
		eai.setProvinceStateCode("ON");
		eai.setStreetDirectionCode("NRT");
		eai.setStreetName("KENNEDY");
		eai.setStreetTypeCode("RD");
		eai.setUnitNumber("112");
		eai.setUnitTypeCode("APT");
		eai.setCivicNumber("112");
		eai.setCivicNumberSuffix("APT");
		eai.setRuralRouteNumber("45");
		eai.setRuralRouteTypeCode("MRs");
		eai.setStationName("PROGRESS");
		
		Account account = accountManager.newPCSPostpaidBusinessRegularAccount();
		
		Address address = (TMAddress)account.getAddress();
		
		address.translateAddress(eai);
		
		EnterpriseAddress ea = address.newEnterpriseAddress();
		
		assertEquals("ON", ea.getProvinceStateCode());
		assertEquals("test2", ea.getAdditionalAddressInformation().get(1));
		assertEquals("I", ea.getAddressTypeCode());
		assertEquals("45", ea.getPostOfficeBoxNumber());
	}
	
}


