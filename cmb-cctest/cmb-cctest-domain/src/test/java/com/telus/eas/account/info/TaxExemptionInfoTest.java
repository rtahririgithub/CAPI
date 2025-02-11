package com.telus.eas.account.info;

import java.util.Date;

import junit.framework.TestCase;

public class TaxExemptionInfoTest  extends TestCase{
	TaxExemptionInfo txi;
	
	public void setUp() throws Exception{
		super.setUp();
		txi=new TaxExemptionInfo();
	}
	
	public void testSetPstExemptionInd(){
		
		txi.setPstExemptionInd(true);
		assertTrue(txi.isPstExemptionInd());
	}
	
	public void testSetGstExemptionInd(){
			
			txi.setGstExemptionInd(false);
			assertFalse(txi.isGstExemptionInd());
		}
	
	public void testSetHstExemptionInd(){
		
		txi.setHstExemptionInd(true);
		assertTrue(txi.isHstExemptionInd());
	}
	
	public void testSetGSTExemptEffectiveDate(){
		
		try{
			txi.setGSTExemptEffectiveDate(new Date());
		}catch(UnsupportedOperationException usoe){
			assertEquals("method not implemented here", usoe.getMessage());
		}
	}
}
