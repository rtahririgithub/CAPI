/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import com.telus.eas.utility.info.OfferCriteriaInfo;
import com.telus.eas.utility.info.OfferPricePlanSetInfo;
import com.telus.eas.utility.info.PricePlanInfo;
import com.telus.eas.utility.info.PricePlanSelectionCriteriaInfo;
import com.telus.eas.utility.info.ServiceInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@Test
@ContextConfiguration(locations="classpath:application-context-test.xml")
//@ActiveProfiles({"remote","pt148"})
@ActiveProfiles("standalone")

public class ReferenceDataHelperTest extends AbstractTestNGSpringContextTests {

	@Autowired 
	private ReferenceDataHelper helper;
	
	@Test
	public void retrieveAlternateRCContractStartDate() throws Exception {
		System.out.println(helper.retrieveAlternateRCContractStartDate("ON"));
	}
	
    @Test(invocationCount=150, threadPoolSize=20)
    public void retrieve_price_plan_by_code() throws Exception {
    	String pricePlanCode = "0PTB3GB";//0PTB3GB   PADSH50B
    	PricePlanInfo info = helper.retrievePricePlan(pricePlanCode);
		//System.out.println(info);
	}

    @Test
	public void retrieveRegularServices() throws Exception {
		ServiceInfo[] serviceList = helper.retrieveRegularServices();
		System.out.println(serviceList.length);
	}
    
    @Test
   	public void retrievePricePlanList() throws Exception {
    	PricePlanInfo[] pricePlanInfoList = helper.retrievePricePlanList(getPricePlanSelectionCriteriaInfo(),null);
    	for (PricePlanInfo pricePlanInfo : pricePlanInfoList) {
			if(pricePlanInfo.isSelectedOffer()){
				System.out.println("offer priceplancode  : " +pricePlanInfo.getCode());
			}
		}
   		System.out.println(pricePlanInfoList.length);
   	}
    
    
    @Test
   	public void retrieveOfferPricePlanInfo() throws Exception {
    	OfferPricePlanSetInfo offerPricePlanSetInfo = helper.retrieveOfferPricePlanInfo(getPricePlanSelectionCriteriaInfo());
   		System.out.println(offerPricePlanSetInfo);
   	}
    
    
    private PricePlanSelectionCriteriaInfo getPricePlanSelectionCriteriaInfo(){
    	PricePlanSelectionCriteriaInfo criteriaInfo =  new PricePlanSelectionCriteriaInfo();
    	criteriaInfo.setProductType("C");
    	criteriaInfo.setEquipmentType("P");
    	criteriaInfo.setProvinceCode("ON");
    	criteriaInfo.setAccountType('I');
    	criteriaInfo.setAccountSubType('R');
    	criteriaInfo.setBrandId(1);
    	criteriaInfo.setCurrentPlansOnly(false);
    	criteriaInfo.setAvailableForActivationOnly(false);
    	criteriaInfo.setTerm(24);
    	criteriaInfo.setNetworkType("H");
    	criteriaInfo.setInitialActivation(false);
    	OfferCriteriaInfo offerCriteria  = new OfferCriteriaInfo();
    	offerCriteria.setSystemId("13573");
    	offerCriteria.setOfferId(1016107);
    	offerCriteria.setPerspectiveDate(formatStrictDate(2019, 11, 13));
    	criteriaInfo.setOfferCriteria(offerCriteria);
		return criteriaInfo;
    	
    }
    
    public static Date formatStrictDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    

}
