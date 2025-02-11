package com.telus.eas.account.info;

import junit.framework.TestCase;

public class EnterpriseAddressInfoTest  extends TestCase{
	EnterpriseAddressInfo eai;
	
	public void setUp() throws Exception{
		super.setUp();
		eai=new EnterpriseAddressInfo();
	}
	
	public void testTranslateAddress(){
		
		AddressInfo adri=new AddressInfo();
		adri.setAddressType("R");
		adri.setAttention("care of");
		adri.setPrimaryLine("test1");
		adri.setSecondaryLine("test2");
		adri.setRrSite("site");
		adri.setRrCompartment("comp");
		adri.setRrGroup("grp");
		adri.setCountry("CA");
		adri.setCity("SCARBOROUGH");
		adri.setRrBox("45");
		adri.setPostalCode("M1P4P1");
		adri.setProvince("ON");
		adri.setStreetDirection("NRT");
		adri.setStreetName("KENNEDY");
		adri.setStreetType("RD");
		adri.setUnitIdentifier("112");
		adri.setUnitDesignator("APT");
		adri.setCivicNo("112");
		adri.setCivicNoSuffix("APT");
		adri.setRrQualifier("45");
		adri.setRrDesignator("M");
		adri.setRrAreaNumber("PROGRESS");
		
		eai.translateAddress(adri);
				
		assertEquals("ON", eai.getProvinceStateCode());
		assertEquals("GRP", eai.getAdditionalAddressInformation().get(1));
		assertEquals("R", eai.getAddressTypeCode());
		assertEquals("45", eai.getRuralRouteNumber());
		assertEquals("MR", eai.getRuralRouteTypeCode());
	}
}