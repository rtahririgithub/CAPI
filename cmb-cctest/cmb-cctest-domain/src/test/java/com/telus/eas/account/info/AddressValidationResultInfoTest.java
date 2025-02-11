package com.telus.eas.account.info;

import java.util.ArrayList;
import java.util.List;

import com.telus.eas.account.info.AddressValidationResultInfo.VerificationResultStateInfo;

import junit.framework.TestCase;

public class AddressValidationResultInfoTest  extends TestCase{
	AddressValidationResultInfo avri;
	
	public void setUp() throws Exception{
		super.setUp();
		avri=new AddressValidationResultInfo();
	}
	
	public void testSettersAndGetters(){
		
		List xyz = new ArrayList();
		xyz.add(new String("test1"));
		xyz.add(new String("test2"));
		
		avri.setVerificationResultStateList(xyz);
		
		xyz = new ArrayList();
		xyz.add(new String("test1"));
		xyz.add(new String("test2"));
		xyz.add(new String("test3"));
		xyz.add(new String("test4"));
		
		avri.setMatchingAddressList(xyz);
		avri.setnCodeReturnStatus("Code Status");
		avri.setValidAddressInd(true);
		VerificationResultStateInfo vrsi = avri.new VerificationResultStateInfo();
		vrsi.setDescription("description");
		vrsi.setState("State");
		
		assertEquals("test1", avri.getVerificationResultStateList().get(0));
		assertEquals("test4", avri.getMatchingAddressList().get(3));
		assertEquals("Code Status", avri.getnCodeReturnStatus());
		assertTrue(avri.isValidAddressInd());
		assertEquals("description", vrsi.getDescription());
		assertEquals("State", vrsi.getState());
	}
}