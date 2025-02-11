package com.telus.ait.tests.clientapi.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.telus.tmi.xmlschema.srv.cmo.billinginquirymgmt.billinginquiryservicerequestresponse_1.ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.ProvinceCode;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentForSubscriber;

public class BISHelper extends CommonHelper {

	private static final String TRUE_VALUE = "true";
	
	public static List<ChargeAndAdjustmentForSubscriber> createChargesAndAdjustmentsForSubscriber(String ban, String subscriberId, String chargeAmount, String chargeCode, 
			String adjustmentAmount, String adjustmentCode, String productType) {
		List<ChargeAndAdjustmentForSubscriber> chargesAndAdjustmentsForSubscriberList = new ArrayList<ChargeAndAdjustmentForSubscriber>();
		ChargeAndAdjustmentForSubscriber chargeAndAdjustment = new ChargeAndAdjustmentForSubscriber();
		chargeAndAdjustment.setAccountNumber(ban);
		chargeAndAdjustment.setSubscriberNumber(subscriberId);
		chargeAndAdjustment.setChargeAmount(Double.valueOf(chargeAmount));
		chargeAndAdjustment.setChargeCode(chargeCode);
		chargeAndAdjustment.setAdjustmentAmount(Double.valueOf(adjustmentAmount));
		chargeAndAdjustment.setAdjustmentReasonCode(adjustmentCode);
		chargeAndAdjustment.setProductType(productType);
		chargeAndAdjustment.setChargeEffectiveDate(Calendar.getInstance().getTime());
		chargesAndAdjustmentsForSubscriberList.add(chargeAndAdjustment);
		return chargesAndAdjustmentsForSubscriberList;
	}

	public static ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax createApplyChgAndAdjToAcctForSubWithTaxParams(String taxProvince, String waiveTaxInd, 
			List<ChargeAndAdjustmentForSubscriber> chargesAndAdjustmentsForSubscriberList) {
		ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax parameters = new ApplyChargesAndAdjustmentsToAccountForSubscriberWithTax();
		parameters.setTaxationProvinceCode(ProvinceCode.valueOf(taxProvince));			
		parameters.setWaiveTaxInd(waiveTaxInd != null && waiveTaxInd.toLowerCase().trim().equals(TRUE_VALUE));			
		parameters.setChargesAndAdjustmentsForSubscriberList(chargesAndAdjustmentsForSubscriberList);
		return parameters;
	}
	
}
