package com.telus.ait.tests.clientapi.stepgroups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.StepGroup;
import net.thucydides.core.annotations.Steps;

import com.telus.ait.cmb.steps.billing.BillingInquirySteps;
import com.telus.ait.cmb.steps.db.ClientApiKbDbSteps;
import com.telus.ait.fwk.test.WebServiceBaseSteps;
import com.telus.ait.integration.kb.info.ChargeInfo;
import com.telus.ait.tests.clientapi.helpers.BISHelper;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax;
import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ChargesAndAdjustmentsId;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentForSubscriber;

public class BillingStepGroups extends WebServiceBaseSteps {
	
	@Steps
	private BillingInquirySteps billingSteps;
	
	@Steps
	private ClientApiKbDbSteps kbSteps;

	//CSV Input Variables	
	public String chargeAmount, chargeCode, adjustmentAmount, adjustmentCode, productType, taxProvince, waiveTaxInd;										//Charge & Adjustments
	public String expectedGstAmount, expectedHstAmount, expectedPstAmount, expectedTaxableGstAmount, expectedTaxableHstAmount, expectedTaxablePstAmount;	//Expected values
	
	@StepGroup
	public void applyChargesAndAdjustmentsWithTax(String ban, String subscriberId) {
		List<ChargesAndAdjustmentsId> results = applyChargesAndAdjustmentsToAccountForSubscriberWithTax(getChargesAndAdjustmentsParams(ban, subscriberId));
		verifyChargesAndAdjustmentsWithTaxInKB(ban, expectedGstAmount, expectedHstAmount, expectedPstAmount, expectedTaxableGstAmount, expectedTaxableHstAmount, expectedTaxablePstAmount, results);
	}
	
	private ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax getChargesAndAdjustmentsParams(String ban, String subscriberId) {
		List<ChargeAndAdjustmentForSubscriber> chargesAndAdjustments = BISHelper.createChargesAndAdjustmentsForSubscriber(ban, subscriberId, chargeAmount, chargeCode, 
				adjustmentAmount, adjustmentCode, productType);
		return BISHelper.createApplyChgAndAdjToAcctForSubWithTaxParams(taxProvince, waiveTaxInd, chargesAndAdjustments);		
	}
	
	@StepGroup
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccountForSubscriberWithTax(ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax parameters) {			
		List<ChargesAndAdjustmentsId> result;
		try {
			result = billingSteps.applyChargesAndAdjustmentsToAccountForSubscriberWithTax(parameters);
			super.displayRequest();	
			super.displayResponse();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@StepGroup
	public void verifyChargesAndAdjustmentsWithTaxInKB(String ban, String expectedGstAmount, String expectedHstAmount, String expectedPstAmount, String expectedTaxableGstAmount, 
			String expectedTaxableHstAmount, String expectedTaxablePstAmount, List<ChargesAndAdjustmentsId> results) {
		assertThat(results, notNullValue());
		assertThat(results.size(), greaterThan(0));
		String chargeSequenceNumber = String.format("%.0f", results.get(0).getChargeSequenceNumber());
		String adjustmentId = String.format("%.0f", results.get(0).getAdjustmentId());
		ChargeInfo chargeInfo = kbSteps.getCharge(ban, chargeSequenceNumber);
		verifyGstTaxAmount(chargeInfo);
		verifyHstTaxAmount(chargeInfo);
		verifyPstTaxAmount(chargeInfo);
		verifyGstTaxableAmount(chargeInfo);
		verifyHstTaxableAmount(chargeInfo);
		verifyPstTaxableAmount(chargeInfo);
		verifyChargeSequenceNumberForAdjustment(chargeSequenceNumber, ban, adjustmentId);
	}

	@Step
	private void verifyGstTaxAmount(ChargeInfo chargeInfo) {		
		assertThat(chargeInfo.getGstTaxAmount(), equalTo(convertToNull(expectedGstAmount)));
	}

	@Step
	private void verifyHstTaxAmount(ChargeInfo chargeInfo) {
		assertThat(chargeInfo.getHstTaxAmount(), equalTo(convertToNull(expectedHstAmount)));
	}

	@Step
	private void verifyPstTaxAmount(ChargeInfo chargeInfo) {
		assertThat(chargeInfo.getPstTaxAmount(), equalTo(convertToNull(expectedPstAmount)));
	}

	@Step
	private void verifyGstTaxableAmount(ChargeInfo chargeInfo) {
		assertThat(chargeInfo.getGstTaxableAmount(), equalTo(convertToNull(expectedTaxableGstAmount)));
	}

	@Step
	private void verifyHstTaxableAmount(ChargeInfo chargeInfo) {
		assertThat(chargeInfo.getHstTaxableAmount(), equalTo(convertToNull(expectedTaxableHstAmount)));
	}

	@Step
	private void verifyPstTaxableAmount(ChargeInfo chargeInfo) {
		assertThat(chargeInfo.getPstTaxableAmount(), equalTo(convertToNull(expectedTaxablePstAmount)));
	}

	@Step
	private void verifyChargeSequenceNumberForAdjustment(String chargeSequenceNumber, String ban, String adjustmentId) {
		assertThat(chargeSequenceNumber, equalTo(kbSteps.getChargeSequenceNumber(ban, adjustmentId)));
	}
	
	private String convertToNull(String value) {
		if (value.trim().toLowerCase().equals("null")) {
			return null;
		}
		return value;
	}
}
