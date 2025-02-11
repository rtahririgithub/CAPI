package com.telus.cmb.reference.bo;

import static org.testng.Assert.assertEquals;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.OfferPricePlanSetInfo;
import com.telus.eas.utility.info.PricePlanInfo;

@RunWith(MockitoJUnitRunner.class)
public class PriceplanBoUnitTest {

	OfferPricePlanSetInfo offerPricePlanSet;
	PricePlanInfo[] kbPriceplans;

	@Before
	public void startup() throws TelusException {

		setupKbPriceplans();

		offerPricePlanSet = new OfferPricePlanSetInfo();

		offerPricePlanSet.setFetchInMarketPricePlansInd(true);
		offerPricePlanSet.setInMarketPricePlansOfferInd(true);

	}

	@Test
	public void filterKBPricePlansUseCase_1() throws TelusException {

		/**
		 *  compatiblePricePlanList  - P1 ,P2
		 *  IncompatiblePricePlanList  - P3 ,P4
		 *  FetchInMarketPricePlansInd  - true and InMarketPricePlansOfferInd - false
		 *  Expected Result : P1- True , P2 - True , P3 - False ,P4 - False , P5 - False , P6 -False 
		 */
		
		setUpOfferPricePlanCodeList();

		offerPricePlanSet.setFetchInMarketPricePlansInd(true);
		offerPricePlanSet.setInMarketPricePlansOfferInd(false);

		PricePlanInfo[] pricePlanInfoArray = PricePlanBo.filterKBPricePlanListByTOMOffer(offerPricePlanSet,kbPriceplans);

		for (PricePlanInfo pricePlanInfo : pricePlanInfoArray) {

			System.out.println(" pp code :  " + pricePlanInfo.getCode() + "offer indicator : " + pricePlanInfo.isSelectedOffer());

			if (StringUtils.equals(pricePlanInfo.getCode().trim(), "P1") || StringUtils.equals(pricePlanInfo.getCode().trim(), "P2")) {
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.TRUE);
			} else{
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.FALSE);
			}
		}
	}
	
	@Test
	public void filterKBPricePlansUseCase_2() throws TelusException {

		/**
		 *  Offer group price plan - P6
		 *  compatiblePricePlanList  - P1 ,P2
		 *  IncompatiblePricePlanList  - P3 ,P4
		 *  FetchInMarketPricePlansInd  - true and InMarketPricePlansOfferInd - true
		 *  Expected Result : P1- True , P2 - True , P3 - False ,P4 - False , P5 - True , P6 -True 
		 */
		
		setUpOfferPricePlanCodeList();
		 
		setUpOfferGroupPricePlan("P6");
		
		offerPricePlanSet.setFetchInMarketPricePlansInd(true);
		offerPricePlanSet.setFetchInMarketPricePlansInd(true);


		PricePlanInfo[] pricePlanInfoArray = PricePlanBo.filterKBPricePlanListByTOMOffer(offerPricePlanSet,kbPriceplans);

		for (PricePlanInfo pricePlanInfo : pricePlanInfoArray) {

			System.out.println(" pp code :  " + pricePlanInfo.getCode() + "offer indicator : " + pricePlanInfo.isSelectedOffer());

			if (StringUtils.equals(pricePlanInfo.getCode().trim(), "P3") || StringUtils.equals(pricePlanInfo.getCode().trim(), "P4")) {
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.FALSE);
			} else{
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.TRUE);
			}
		}
	}

	@Test
	public void filterKBPricePlansUseCase_3() throws TelusException {
		
		/**
		 *  compatiblePricePlanList  - P1 ,P2
		 *  IncompatiblePricePlanList  - P3 ,P4
		 *  FetchInMarketPricePlansInd  - false
		 *  Expected Result : P1- True , P2 - True , P3 - False ,P4 - False 
		 */
		

		setUpOfferPricePlanCodeList();

		offerPricePlanSet.setFetchInMarketPricePlansInd(false);

		PricePlanInfo[] pricePlanInfoArray = PricePlanBo.filterKBPricePlanListByTOMOffer(offerPricePlanSet,kbPriceplans);
		for (PricePlanInfo pricePlanInfo : pricePlanInfoArray) { 
			System.out.println(" pp code :  " + pricePlanInfo.getCode()+ "offer indicator : " + pricePlanInfo.isSelectedOffer());
		}
		assertEquals(pricePlanInfoArray.length, 5);
		
	}
	

	@Test
	public void filterKBPricePlansUseCase_4() throws TelusException {

		/**	Offer group price plan - P4
		 *  compatiblePricePlanList  - P1 ,P2
		 *  IncompatiblePricePlanList  - P3 ,P4
		 *  FetchInMarketPricePlansInd  - false
		 *  Expected Result : P1- True , P2 - True , P3 - False ,P4 - False   ( P4 flag override by IncompatiblePricePlanList)
		 */
		
		setUpOfferPricePlanCodeList();
		
		setUpOfferGroupPricePlan("P4");

		
		offerPricePlanSet.setFetchInMarketPricePlansInd(false);


		PricePlanInfo[] pricePlanInfoArray = PricePlanBo.filterKBPricePlanListByTOMOffer(offerPricePlanSet,kbPriceplans);

		for (PricePlanInfo pricePlanInfo : pricePlanInfoArray) {

			System.out.println(" pp code :  " + pricePlanInfo.getCode() + "offer indicator : " + pricePlanInfo.isSelectedOffer());

			if (StringUtils.equals(pricePlanInfo.getCode().trim(), "P3") || StringUtils.equals(pricePlanInfo.getCode().trim(), "P4")) {
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.FALSE);
			} else{
				assertEquals(pricePlanInfo.isSelectedOffer(),Boolean.TRUE);
			}
		}
	}
	
	private void setUpOfferPricePlanCodeList() {

		offerPricePlanSet.getOfferPricePlanCodeList().add("P1");
		offerPricePlanSet.getOfferPricePlanCodeList().add("P2");

		offerPricePlanSet.getOfferIncompatiblePricePlanCodeList().add("P3");
		offerPricePlanSet.getOfferIncompatiblePricePlanCodeList().add("P4");

	}

	
	private void setUpOfferGroupPricePlan(String planCode) {
		for (PricePlanInfo pricePlanInfo : kbPriceplans) {
			if(pricePlanInfo.getCode().equals(planCode)){
				pricePlanInfo.setSelectedOffer(true);
			}
		}
	}
	
	
	private void setupKbPriceplans() {

		PricePlanInfo kbPP1 = new PricePlanInfo();
		kbPP1.setCode("P1");
		PricePlanInfo kbPP2 = new PricePlanInfo();
		kbPP2.setCode("P2");
		PricePlanInfo kbPP3 = new PricePlanInfo();
		kbPP3.setCode("P3");
		PricePlanInfo kbPP4 = new PricePlanInfo();
		kbPP4.setCode("P4");
		PricePlanInfo kbPP5 = new PricePlanInfo();
		kbPP5.setCode("P5");
		PricePlanInfo kbPP6 = new PricePlanInfo();
		kbPP6.setCode("P6");

		kbPriceplans = new PricePlanInfo[] { kbPP1, kbPP2, kbPP3, kbPP4, kbPP5 ,kbPP6};
	}
}
