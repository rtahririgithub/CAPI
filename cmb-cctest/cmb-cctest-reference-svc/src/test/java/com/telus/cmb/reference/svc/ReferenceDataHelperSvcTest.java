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

import static org.junit.Assert.fail;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Pavel Simonovsky
 *
 */

public abstract class ReferenceDataHelperSvcTest{

	@Autowired
	protected ReferenceDataHelper service = null;
	
	
	
	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#authenticate(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testAuthenticate() {
		System.out.println(service);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#filterServiceListByPricePlan(java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testFilterServiceListByPricePlan() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#filterServiceListByProvince(java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testFilterServiceListByProvince() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#isAssociatedIncludedPromotion(java.lang.String, int, java.lang.String)}.
	 */
	@Test
	public void testIsAssociatedIncludedPromotion() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#isDefaultDealer(java.lang.String)}.
	 */
	@Test
	public void testIsDefaultDealer() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#isPortOutAllowed(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsPortOutAllowed() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#isPrivilegeAvailable(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsPrivilegeAvailable() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#isServiceAssociatedToPricePlan(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testIsServiceAssociatedToPricePlan() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAccountTypes()}.
	 */
	@Test
	public void testRetrieveAccountTypes() throws Exception {
		writeResults("retrieveAccountTypes", service.retrieveAccountTypes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveActivityTypes()}.
	 */
	@Test
	public void testRetrieveActivityTypes() throws Exception {
		writeResults ("retrieveActivityTypes", service.retrieveActivityTypes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAdjustmentReasons()}.
	 */
	@Test
	public void testRetrieveAdjustmentReasons() throws Exception {
		writeResults ("retrieveAdjustmentReasons", service.retrieveAdjustmentReasons(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAllProvinces()}.
	 */
	@Test
	public void testRetrieveAllProvinces() throws Exception {
		writeResults ("retrieveAllProvinces", service.retrieveAllProvinces(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAllTitles()}.
	 */
	@Test
	public void testRetrieveAllTitles() throws Exception{
		writeResults ("retrieveAllTitles", this.service.retrieveAllTitles(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAlternateRCContractStartDate(java.lang.String)}.
	 */
	@Test
	public void testRetrieveAlternateRCContractStartDate() throws Exception {
		writeResults ("retrieveAlternateRCContractStartDate", service.retrieveAlternateRCContractStartDate("ON"), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAlternateRecurringCharge(com.telus.eas.utility.info.ServiceInfo, java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveAlternateRecurringCharge() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAmountBarCodes()}.
	 */
	@Test
	public void testRetrieveAmountBarCodes() throws Exception {
		writeResults ("retrieveAmountBarCodes", service.retrieveAmountBarCodes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveApplicationMessageMappings()}.
	 */
	@Test
	public void testRetrieveApplicationMessageMappings() throws Exception {
		writeResults ("retrieveApplicationMessageMappings", service.retrieveApplicationMessageMappings(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveApplicationMessages()}.
	 */
	@Test
	public void testRetrieveApplicationMessages() throws Exception {
		writeResults ("retrieveApplicationMessages", service.retrieveApplicationMessages(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveApplicationSummaries()}.
	 */
	@Test
	public void testRetrieveApplicationSummaries() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveAudienceTypes()}.
	 */
	@Test
	public void testRetrieveAudienceTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBillCycle(java.lang.String)}.
	 */
	@Test
	public void testRetrieveBillCycle() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBillCycleLeastUsed()}.
	 */
	@Test
	public void testRetrieveBillCycleLeastUsed() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBillCycles()}.
	 */
	@Test
	public void testRetrieveBillCycles() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBillCycles(java.lang.String)}.
	 */
	@Test
	public void testRetrieveBillCyclesString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBillHoldRedirectDestinations()}.
	 */
	@Test
	public void testRetrieveBillHoldRedirectDestinations() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBrands()}.
	 */
	@Test
	public void testRetrieveBrands() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBrandSwapRules()}.
	 */
	@Test
	public void testRetrieveBrandSwapRules() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveBusinessRoles()}.
	 */
	@Test
	public void testRetrieveBusinessRoles() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveClientConsentIndicators()}.
	 */
	@Test
	public void testRetrieveClientConsentIndicators() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveClientStateReasons()}.
	 */
	@Test
	public void testRetrieveClientStateReasons() throws Exception {
		writeResults("retrieveClientStateReasons", service.retrieveClientStateReasons(), false);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCollectionActivities()}.
	 */
	@Test
	public void testRetrieveCollectionActivities() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCollectionAgencies()}.
	 */
	@Test
	public void testRetrieveCollectionAgencies() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCollectionPathDetails()}.
	 */
	@Test
	public void testRetrieveCollectionPathDetails() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCollectionStates()}.
	 */
	@Test
	public void testRetrieveCollectionStates() throws Exception {
		writeResults("retrieveCollectionStates", service.retrieveCollectionStates(), false);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCollectionStepApproval()}.
	 */
	@Test
	public void testRetrieveCollectionStepApproval() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCommitmentReasons()}.
	 */
	@Test
	public void testRetrieveCommitmentReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCorporateAccountReps()}.
	 */
	@Test
	public void testRetrieveCorporateAccountReps() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCountries()}.
	 */
	@Test
	public void testRetrieveCountries() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCoverageRegions()}.
	 */
	@Test
	public void testRetrieveCoverageRegions() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditCardPaymentTypes()}.
	 */
	@Test
	public void testRetrieveCreditCardPaymentTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditCardTypes()}.
	 */
	@Test
	public void testRetrieveCreditCardTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditCheckDepositChangeReasons()}.
	 */
	@Test
	public void testRetrieveCreditCheckDepositChangeReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditClasses()}.
	 */
	@Test
	public void testRetrieveCreditClasses() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditMessageByCode(java.lang.String)}.
	 */
	@Test
	public void testRetrieveCreditMessageByCode() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveCreditMessages()}.
	 */
	@Test
	public void testRetrieveCreditMessages() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDataUsageServiceTypes()}.
	 */
	@Test
	public void testRetrieveDataUsageServiceTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDataUsageUnits()}.
	 */
	@Test
	public void testRetrieveDataUsageUnits() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDealerbyDealerCode(java.lang.String)}.
	 */
	@Test
	public void testRetrieveDealerbyDealerCodeString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDealerbyDealerCode(java.lang.String, boolean)}.
	 */
	@Test
	public void testRetrieveDealerbyDealerCodeStringBoolean() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDealerSalesRepByCode(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveDealerSalesRepByCodeStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDealerSalesRepByCode(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testRetrieveDealerSalesRepByCodeStringStringBoolean() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDefaultLDC()}.
	 */
	@Test
	public void testRetrieveDefaultLDC() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDepartments()}.
	 */
	@Test
	public void testRetrieveDepartments() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDiscountPlans()}.
	 */
	@Test
	public void testRetrieveDiscountPlans() throws Exception {
		writeResults("retrieveDiscountPlans()", service.retrieveDiscountPlans(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDiscountPlans(boolean)}.
	 */
	@Test
	public void testRetrieveDiscountPlansBoolean() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDiscountPlans(boolean, java.lang.String, int)}.
	 */
	@Test
	public void testRetrieveDiscountPlansBooleanStringInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDiscountPlans(boolean, java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public void testRetrieveDiscountPlansBooleanStringStringInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveDiscountPlans(boolean, java.lang.String, java.lang.String, long[], boolean, int)}.
	 */
	@Test
	public void testRetrieveDiscountPlansBooleanStringStringLongArrayBooleanInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveEncodingFormats()}.
	 */
	@Test
	public void testRetrieveEncodingFormats() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveEquipmentPossessions()}.
	 */
	@Test
	public void testRetrieveEquipmentPossessions() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveEquipmentProductTypes()}.
	 */
	@Test
	public void testRetrieveEquipmentProductTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveEquipmentStatuses()}.
	 */
	@Test
	public void testRetrieveEquipmentStatuses() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveEquipmentTypes()}.
	 */
	@Test
	public void testRetrieveEquipmentTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveExceptionReasons()}.
	 */
	@Test
	public void testRetrieveExceptionReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFeatureCategories()}.
	 */
	@Test
	public void testRetrieveFeatureCategories() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFeatures()}.
	 */
	@Test
	public void testRetrieveFeatures() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFeeWaiverReasons()}.
	 */
	@Test
	public void testRetrieveFeeWaiverReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFeeWaiverTypes()}.
	 */
	@Test
	public void testRetrieveFeeWaiverTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFleetByFleetIdentity(com.telus.eas.account.info.FleetIdentityInfo)}.
	 */
	@Test
	public void testRetrieveFleetByFleetIdentity() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFleetClasses()}.
	 */
	@Test
	public void testRetrieveFleetClasses() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFleetsByFleetType(char)}.
	 */
	@Test
	public void testRetrieveFleetsByFleetType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFollowUpCloseReason(java.lang.String)}.
	 */
	@Test
	public void testRetrieveFollowUpCloseReason() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFollowUpCloseReasons()}.
	 */
	@Test
	public void testRetrieveFollowUpCloseReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveFollowUpTypes()}.
	 */
	@Test
	public void testRetrieveFollowUpTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveGenerations()}.
	 */
	@Test
	public void testRetrieveGenerations() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveIncludedPromotions(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	@Test
	public void testRetrieveIncludedPromotions() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveInvoiceCallSortOrderTypes()}.
	 */
	@Test
	public void testRetrieveInvoiceCallSortOrderTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveInvoiceSuppressionLevels()}.
	 */
	@Test
	public void testRetrieveInvoiceSuppressionLevels() throws Exception  {
		writeResults ("retrieveInvoiceSuppressionLevels", service.retrieveInvoiceSuppressionLevels(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveKnowbilityOperatorInfo(java.lang.String)}.
	 */
	@Test
	public void testRetrieveKnowbilityOperatorInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveKnowbilityOperators()}.
	 */
	@Test
	public void testRetrieveKnowbilityOperators() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLanguages()}.
	 */
	@Test
	public void testRetrieveLanguages() {
		fail("Not yet implemented"); 
	}

//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLetter(java.lang.String, java.lang.String)}.
//	 */
//	@Test
//	public void testRetrieveLetterStringString() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLetter(java.lang.String, java.lang.String, int)}.
//	 */
//	@Test
//	public void testRetrieveLetterStringStringInt() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLetterCategories()}.
//	 */
//	@Test
//	public void testRetrieveLetterCategories() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLettersByCategory(java.lang.String)}.
//	 */
//	@Test
//	public void testRetrieveLettersByCategory() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLettersByTitleKeyword(java.lang.String)}.
//	 */
//	@Test
//	public void testRetrieveLettersByTitleKeyword() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLetterSubCategories()}.
//	 */
//	@Test
//	public void testRetrieveLetterSubCategories() {
//		fail("Not yet implemented"); 
//	}
//
//	/**
//	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLetterVariables(java.lang.String, java.lang.String, int)}.
//	 */
//	@Test
//	public void testRetrieveLetterVariables() {
//		fail("Not yet implemented"); 
//	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLockReasons()}.
	 */
	@Test
	public void testRetrieveLockReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveLogicalDate()}.
	 */
	@Test
	public void testRetrieveLogicalDate() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveManualChargeTypes()}.
	 */
	@Test
	public void testRetrieveManualChargeTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMarketProvinces(java.lang.String)}.
	 */
	@Test
	public void testRetrieveMarketProvinces() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMemoTypeCategories()}.
	 */
	@Test
	public void testRetrieveMemoTypeCategories() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMemoTypes()}.
	 */
	@Test
	public void testRetrieveMemoTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMigrationTypes()}.
	 */
	@Test
	public void testRetrieveMigrationTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMinutePoolingContributorServiceCodes()}.
	 */
	@Test
	public void testRetrieveMinutePoolingContributorServiceCodes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveMobileCountries()}.
	 */
	@Test
	public void testRetrieveMobileCountries() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNetworks()}.
	 */
	@Test
	public void testRetrieveNetworks() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNetworkTypes()}.
	 */
	@Test
	public void testRetrieveNetworkTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNotificationMessageTemplateInfo()}.
	 */
	@Test
	public void testRetrieveNotificationMessageTemplateInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNotificationType()}.
	 */
	@Test
	public void testRetrieveNotificationType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNpaNxxForMsisdnReservation(java.lang.String)}.
	 */
	@Test
	public void testRetrieveNpaNxxForMsisdnReservationString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNpaNxxForMsisdnReservation(java.lang.String, boolean)}.
	 */
	@Test
	public void testRetrieveNpaNxxForMsisdnReservationStringBoolean() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberGroupByPhoneNumberProductType(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveNumberGroupByPhoneNumberProductType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberGroupByPortedInPhoneNumberProductType(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveNumberGroupByPortedInPhoneNumberProductType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberGroupList(char, char, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveNumberGroupListCharCharStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberGroupList(char, char, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveNumberGroupListCharCharStringStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberGroupListByProvince(char, char, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveNumberGroupListByProvince() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveNumberRanges()}.
	 */
	@Test
	public void testRetrieveNumberRanges() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePagerEquipmentTypes()}.
	 */
	@Test
	public void testRetrievePagerEquipmentTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePagerFrequencies()}.
	 */
	@Test
	public void testRetrievePagerFrequencies() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePaymentMethodTypes()}.
	 */
	@Test
	public void testRetrievePaymentMethodTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePaymentSourceTypes()}.
	 */
	@Test
	public void testRetrievePaymentSourceTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePaymentTransferReasons()}.
	 */
	@Test
	public void testRetrievePaymentTransferReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePhoneNumberResource(java.lang.String)}.
	 */
	@Test
	public void testRetrievePhoneNumberResource() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePoolingGroups()}.
	 */
	@Test
	public void testRetrievePoolingGroups() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidAdjustmentReasons()}.
	 */
	@Test
	public void testRetrievePrepaidAdjustmentReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidEventType(int)}.
	 */
	@Test
	public void testRetrievePrepaidEventType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidEventTypes()}.
	 */
	@Test
	public void testRetrievePrepaidEventTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidFeatureAddWaiveReasons()}.
	 */
	@Test
	public void testRetrievePrepaidFeatureAddWaiveReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidManualAdjustmentReasons()}.
	 */
	@Test
	public void testRetrievePrepaidManualAdjustmentReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidRates()}.
	 */
	@Test
	public void testRetrievePrepaidRates() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidRechargeDenominations()}.
	 */
	@Test
	public void testRetrievePrepaidRechargeDenominations() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePrepaidTopUpWaiveReasons()}.
	 */
	@Test
	public void testRetrievePrepaidTopUpWaiveReasons() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlan(java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanString() throws Exception {
		writeResults("retrievePricePlan(String)", service.retrievePricePlan("PTLK75CF"), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlan(java.lang.String, java.lang.String, java.lang.String, char, char, int)}.
	 */
	@Test
	public void testRetrievePricePlanStringStringStringCharCharInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlan(java.lang.String, java.lang.String, java.lang.String, java.lang.String, char, char, int)}.
	 */
	@Test
	public void testRetrievePricePlanStringStringStringStringCharCharInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, boolean, boolean, int, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntBooleanBooleanIntString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, boolean, boolean, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntBooleanBooleanString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, boolean, boolean, java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntBooleanBooleanStringStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, long[], boolean, boolean, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntLongArrayBooleanBooleanString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, long[], boolean, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntLongArrayBooleanString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanList(java.lang.String, java.lang.String, java.lang.String, char, char, int, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListStringStringStringCharCharIntString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanListByAccType(java.lang.String, java.lang.String, char, java.lang.String, int, boolean, boolean, java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanListByAccType() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePricePlanTermInfo(java.lang.String)}.
	 */
	@Test
	public void testRetrievePricePlanTermInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProductTypes()}.
	 */
	@Test
	public void testRetrieveProductTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePromoTerm(java.lang.String)}.
	 */
	@Test
	public void testRetrievePromoTerm() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePromotionalDiscounts(java.lang.String, long[], boolean)}.
	 */
	@Test
	public void testRetrievePromotionalDiscounts() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProvinces()}.
	 */
	@Test
	public void testRetrieveProvinces() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProvinces(java.lang.String)}.
	 */
	@Test
	public void testRetrieveProvincesString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProvisioningPlatformTypes()}.
	 */
	@Test
	public void testRetrieveProvisioningPlatformTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProvisioningTransactionStatuses()}.
	 */
	@Test
	public void testRetrieveProvisioningTransactionStatuses() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveProvisioningTransactionTypes()}.
	 */
	@Test
	public void testRetrieveProvisioningTransactionTypes() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRegularServices()}.
	 */
	@Test
	public void testRetrieveRegularServices() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRegularServices(java.lang.String, java.lang.String, boolean)}.
	 */
	@Test
	public void testRetrieveRegularServicesStringStringBoolean() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRelations(java.lang.String)}.
	 */
	@Test
	public void testRetrieveRelations() throws Exception {
		writeResults ("retrieveRelations", service.retrieveRelations("RELATION"), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveResourceStatus(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveResourceStatus() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRoamingCapability()}.
	 */
	@Test
	public void testRetrieveRoamingCapability() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRoutes()}.
	 */
	@Test
	public void testRetrieveRoutes() throws Exception{
		writeResults ("retrieveRoutes", service.retrieveRules(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRules()}.
	 */
	@Test
	public void testRetrieveRules() throws Exception {
		writeResults ("retrieveRules", service.retrieveRules(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveRules(int)}.
	 */
	@Test
	public void testRetrieveRulesInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSalesRepListByDealer(java.lang.String)}.
	 */
	@Test
	public void testRetrieveSalesRepListByDealer() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSegmentations()}.
	 */
	@Test
	public void testRetrieveSegmentations() throws Exception {
		writeResults ("retrieveSegementations", service.retrieveSegmentations(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceEquipmentNetworkInfo(java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceEquipmentNetworkInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceEquipmentTypeInfo(java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceEquipmentTypeInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceExclusionGroups()}.
	 */
	@Test
	public void testRetrieveServiceExclusionGroups() throws Exception{
		writeResults ("retrieveServiceExclusionGroups", service.retrieveServiceExclusionGroups(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceFamily(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceFamilyStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceFamily(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceFamilyStringStringString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceFamily(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, int)}.
	 */
	@Test
	public void testRetrieveServiceFamilyStringStringStringStringStringBooleanInt() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceFamilyGroupCodes(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceFamilyGroupCodes() throws Exception {
		writeResults ("retrieveServiceFamilyGroupCodes", service.retrieveServiceFamilyGroupCodes("SOC", "FAMILY_TYPE"), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServicePolicyExceptions()}.
	 */
	@Test
	public void testRetrieveServicePolicyExceptions() throws Exception{
		writeResults ("retrieveServicePolicyExceptions", service.retrieveServicePolicyExceptions(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveServiceUsageInfo(java.lang.String)}.
	 */
	@Test
	public void testRetrieveServiceUsageInfo() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSIDs()}.
	 */
	@Test
	public void testRetrieveSIDs() throws Exception{
		writeResults ("retrieveSIDs", service.retrieveSIDs(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSpecialNumberRanges()}.
	 */
	@Test
	public void testRetrieveSpecialNumberRanges() throws Exception {
		writeResults ("retrieveSpecialNumberRanges", service.retrieveSpecialNumberRanges(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSpecialNumbers()}.
	 */
	@Test
	public void testRetrieveSpecialNumbers() throws Exception {
		writeResults ("retrieveSpecialNumbers", service.retrieveSpecialNumbers(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveStates()}.
	 */
	@Test
	public void testRetrieveStates() throws Exception {
		writeResults ("retrieveStates", service.retrieveStates(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveStreetTypes()}.
	 */
	@Test
	public void testRetrieveStreetTypes() throws Exception {
		writeResults ("retrieveStreetTypes", service.retrieveStreetTypes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSubscriptionRoleTypes()}.
	 */
	@Test
	public void testRetrieveSubscriptionRoleTypes() throws Exception {
		writeResults ("retrieveSubscriptionRoleTypes", service.retrieveSubscriptionRoleTypes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSwapRules()}.
	 */
	@Test
	public void testRetrieveSwapRules() throws Exception {
		writeResults ("retrieveSwapRules", service.retrieveSwapRules(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveSystemDate()}.
	 */
	@Test
	public void testRetrieveSystemDate() throws Exception {
		writeResults ("retrieveSystemDate", service.retrieveSystemDate(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveTalkGroupPriorities()}.
	 */
	@Test
	public void testRetrieveTalkGroupPriorities() throws Exception {
		writeResults ("retrieveTalkGroupPriorities", service.retrieveTalkGroupPriorities(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveTalkGroupsByFleetIdentity(com.telus.eas.account.info.FleetIdentityInfo)}.
	 */
	@Test
	public void testRetrieveTalkGroupsByFleetIdentity() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveTaxPolicies()}.
	 */
	@Test
	public void testRetrieveTaxPolicies() throws Exception {
		writeResults ("retrieveTaxPolicies", service.retrieveTaxPolicies(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveTitles()}.
	 */
	@Test
	public void testRetrieveTitles() throws Exception {
		writeResults ("retrieveTitles", service.retrieveTitles(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveUnitTypes()}.
	 */
	@Test
	public void testRetrieveUnitTypes() throws Exception {
		writeResults ("retrieveUnitTypes", service.retrieveUnitTypes(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveUrbanIdByNumberGroup(com.telus.eas.utility.info.NumberGroupInfo)}.
	 */
	@Test
	public void testRetrieveUrbanIdByNumberGroup() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveVendorServices()}.
	 */
	@Test
	public void testRetrieveVendorServices() throws Exception {
		writeResults ("retrieveVendorServices", service.retrieveVendorServices(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWorkFunctions()}.
	 */
	@Test
	public void testRetrieveWorkFunctions() throws Exception {
		writeResults ("retrieveWorkFunctions", service.retrieveWorkFunctions(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWorkFunctions(java.lang.String)}.
	 */
	@Test
	public void testRetrieveWorkFunctionsString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWorkPosition(java.lang.String)}.
	 */
	@Test
	public void testRetrieveWorkPosition() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWorkPositions()}.
	 */
	@Test
	public void testRetrieveWorkPositions() throws Exception {
		writeResults ("retrieveWorkPositions", service.retrieveWorkPositions(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWorkPositions(java.lang.String)}.
	 */
	@Test
	public void testRetrieveWorkPositionsString() {
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWPSFeature(int)}.
	 */
	@Test
	public void testRetrieveWPSFeature() throws Exception{
		fail("Not yet implemented"); 
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveWPSFeaturesList()}.
	 */
	@Test
	public void testRetrieveWPSFeaturesList() throws Exception {
		writeResults ("retrieveWPSFeaturesList", service.retrieveWPSFeaturesList(), true);
	}

	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrieveZeroMinutePoolingContributorServiceCodes()}.
	 */
	@Test
	public void testRetrieveZeroMinutePoolingContributorServiceCodes() throws Exception {
		writeResults ("retrieveZeroMinutePoolingContributorServiceCodes", service.retrieveZeroMinutePoolingContributorServiceCodes(), true);
	}


	protected void writeResults(String methodName, Object data, boolean displayContent) {
		writeResults(methodName, new Object[] {data}, displayContent);
	}
	
	protected void writeResults(String methodName, Object[] data, boolean displayContent) {
		writeResults(methodName, Arrays.asList(data), new PrintWriter(System.out), displayContent);
	}
	
	protected void writeResults(String methodName, Object[] data, PrintWriter writer, boolean displayContent) {
		writeResults(methodName, Arrays.asList(data), writer, displayContent);
	}

	protected void writeResults(String methodName, Collection<?> data, PrintWriter writer, boolean displayContent) {
		writer.println("Method [" + methodName + "]");
		writer.println("Number of elements [" + data.size() + "]");
		if (!data.isEmpty() && displayContent) {
			writer.println("Elements:");
			int idx = 0;
			for (Object element : data) {
				writer.println("Element [" + idx++ + "]");
				writer.println(element.toString());
			}
		}
		writer.flush();
	}
	
	//Added for charge Paper Bill - Anitha Duraisamy- start
	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataRetrievalSvcImpl#retrievePaperBillChargeType()}.
	 */
	@Test
	
	public void testRetrievePaperBillChargeType() {
		fail("Not yet implemented"); 		
	}
	
	//Added for charge Paper Bill - Anitha Duraisamy- end
	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataHelperImpl#retrieveServiceGroupRelation(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testRetrieveServiceGroupRelation() throws Exception {
		fail("Not yet implemented"); 		
	}
	
	/**
	 * Test method for {@link com.telus.cmb.reference.svc.impl.ReferenceDataHelperImpl#retrieveServiceTerm(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testRetrieveServiceTerm() throws Exception{
		fail("Not yet implemented"); 		
	}
	
}
