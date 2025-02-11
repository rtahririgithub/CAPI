package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class AddressInfoTest  extends TestCase{
	AddressInfo adri;
	
	public void setUp() throws Exception{
		super.setUp();
		adri=new AddressInfo();
	}
	
	public void testTranslateAddress(){

		EnterpriseAddressInfo eai = new EnterpriseAddressInfo();
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
		adri.translateAddress(eai);
				
		assertEquals("ON", adri.getProvince());
		assertEquals("test2", adri.getSecondaryLine());
		assertEquals("F", adri.getAddressType());
		assertEquals("PROGRESS", adri.getRrAreaNumber());
		assertEquals("F", adri.getRrDesignator());
	}
}