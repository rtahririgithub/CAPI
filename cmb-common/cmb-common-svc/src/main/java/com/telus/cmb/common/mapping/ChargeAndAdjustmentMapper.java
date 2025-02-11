package com.telus.cmb.common.mapping;

import java.util.Date;

import amdocs.APILink.datatypes.ChargeAndAdjDetailInfo;
import amdocs.APILink.datatypes.CreateChargeAdjustInfo;
import amdocs.APILink.datatypes.CreateChargeAdjustWithTaxInfo;

import com.telus.api.reference.Province;
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeAdjustmentWithTaxInfo;
import com.telus.eas.utility.info.TaxationPolicyInfo;


public class ChargeAndAdjustmentMapper {

	private static final String ALBERTA_SUBMARKET_CODE = "CAL";
	
	public static ChargeAdjustmentInfo getFailedTransactionsDetails(ChargeAdjustmentInfo chargeAdjustmentInfo, String errorCode, String errorMessage) {
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();
		info.copy(chargeAdjustmentInfo);
		info.setErrorCode(errorCode);
		info.setErrorMessage(errorMessage);
		info.setChargeApplied(false);
		info.setTimestamp(new Date());
		return info;
	}

	public static CreateChargeAdjustInfo mapToAmdocsCreateChargeAdjustInfo(ChargeAdjustmentInfo chargeInfo) {
		CreateChargeAdjustInfo createChargeAdjustInfo = new CreateChargeAdjustInfo();
		createChargeAdjustInfo.chargeAmount = chargeInfo.getChargeAmount();
		createChargeAdjustInfo.chargeMemoText = chargeInfo.getChargeMemoText();
		createChargeAdjustInfo.chargeCode = chargeInfo.getChargeCode();
		createChargeAdjustInfo.chargeEffectiveDate = chargeInfo.getChargeEffectiveDate();
		createChargeAdjustInfo.adjustReasonCode = chargeInfo.getAdjustmentReasonCode();
		createChargeAdjustInfo.adjustMemoUserText = chargeInfo.getAdjustmentMemoText();
		createChargeAdjustInfo.adjAmt = chargeInfo.getAdjustmentAmount();
		createChargeAdjustInfo.bypassAuthorization = chargeInfo.isBypassAuthorization();
		createChargeAdjustInfo.authorizedToCreateFollowUp = chargeInfo.isAuthorizedToCreateFollowUp();
		return createChargeAdjustInfo;
	}

	public static ChargeAdjustmentInfo mapToTelusChargeAdjustmentInfo(ChargeAndAdjDetailInfo chargeAndAdjDetailInfo, ChargeAdjustmentInfo chargeAdjustmentInfo) {
		ChargeAdjustmentInfo info = new ChargeAdjustmentInfo();
		info.copy(chargeAdjustmentInfo);
		info.setAdjustmentId(chargeAndAdjDetailInfo.adjustDetailInfo.adjustmentId);
		info.setChargeSequenceNumber(chargeAndAdjDetailInfo.chargeDetailInfo.chargeSeqNo);
		info.setChargeApplied(true);
		return info;
	}

	public static CreateChargeAdjustWithTaxInfo mapToAmdocsCreateChargeAdjustWithTaxInfo(ChargeAdjustmentWithTaxInfo chargeWithTaxInfo) {
		CreateChargeAdjustWithTaxInfo createChargeAdjustWithTaxInfo = new CreateChargeAdjustWithTaxInfo();
		ChargeAdjustmentInfo chargeInfo = chargeWithTaxInfo.getChargeAdjustmentInfo();
		TaxSummaryInfo taxSummaryInfo = chargeWithTaxInfo.getTaxSummaryInfo();
		TaxExemptionInfo taxExemptionInfo = chargeWithTaxInfo.getTaxExemptionInfo();
		createChargeAdjustWithTaxInfo.chargeAmount = chargeInfo.getChargeAmount();
		createChargeAdjustWithTaxInfo.chargeMemoText = chargeInfo.getChargeMemoText();
		createChargeAdjustWithTaxInfo.chargeCode = chargeInfo.getChargeCode();
		createChargeAdjustWithTaxInfo.chargeEffectiveDate = chargeInfo.getChargeEffectiveDate();
		createChargeAdjustWithTaxInfo.adjustReasonCode = chargeInfo.getAdjustmentReasonCode();
		createChargeAdjustWithTaxInfo.adjustMemoUserText = chargeInfo.getAdjustmentMemoText();
		createChargeAdjustWithTaxInfo.adjAmt = chargeInfo.getAdjustmentAmount();
		createChargeAdjustWithTaxInfo.bypassAuthorization = chargeInfo.isBypassAuthorization();
		createChargeAdjustWithTaxInfo.authorizedToCreateFollowUp = chargeInfo.isAuthorizedToCreateFollowUp();
		createChargeAdjustWithTaxInfo.subMarketCd = Province.PROVINCE_AB.equals(taxSummaryInfo.getProvince()) ? ALBERTA_SUBMARKET_CODE : taxSummaryInfo.getProvince();
		createChargeAdjustWithTaxInfo.taxGstAmount = taxSummaryInfo.getGSTAmount();
		createChargeAdjustWithTaxInfo.taxGstExemptionInd = (byte)(taxExemptionInfo.isGstExemptionInd() ? 1 : 0);
		createChargeAdjustWithTaxInfo.taxGstTaxableAmount = getGSTTaxableAmount(taxExemptionInfo, taxSummaryInfo, chargeInfo);
		createChargeAdjustWithTaxInfo.taxHstAmount = taxSummaryInfo.getHSTAmount();
		createChargeAdjustWithTaxInfo.taxHstExemptionInd =  (byte)(taxExemptionInfo.isHstExemptionInd() ? 1 : 0);
		createChargeAdjustWithTaxInfo.taxHstTaxableAmount = getHSTTaxableAmount(taxExemptionInfo, taxSummaryInfo, chargeInfo);
		createChargeAdjustWithTaxInfo.taxPstAmount = taxSummaryInfo.getPSTAmount();
		createChargeAdjustWithTaxInfo.taxPstExemptionInd = (byte)(taxExemptionInfo.isHstExemptionInd() ? 1 : 0);
		createChargeAdjustWithTaxInfo.taxPstTaxableAmount = getPSTTaxableAmount(taxExemptionInfo, taxSummaryInfo, chargeInfo);
		return createChargeAdjustWithTaxInfo;
	}
	
	/**
	 * Method to calculate the GST Taxable Amount
	 * @param taxSummaryInfo
	 * @param chargeInfo
	 * @return
	 */
	private static double getGSTTaxableAmount(TaxExemptionInfo taxExemptionInfo, TaxSummaryInfo taxSummaryInfo, ChargeAdjustmentInfo chargeInfo) {
		if (taxExemptionInfo.isGstExemptionInd() || taxSummaryInfo.getGSTAmount() == 0) {
			return 0;
		} 
		return chargeInfo.getChargeAmount();
	}

	/**
	 * Method to calculate the HST Taxable Amount
	 * @param taxSummaryInfo
	 * @param chargeInfo
	 * @return
	 */
	private static double getHSTTaxableAmount(TaxExemptionInfo taxExemptionInfo, TaxSummaryInfo taxSummaryInfo, ChargeAdjustmentInfo chargeInfo) {
		if (taxExemptionInfo.isHstExemptionInd() || taxSummaryInfo.getHSTAmount() == 0) {
			return 0;
		} 
		return chargeInfo.getChargeAmount();
	}
	
	/**
	 * Method to calculate the PST Taxable Amount based on logic found in ReferenceDataFacadeImpl.getTaxCalculationListByProvince
	 * @param taxSummaryInfo
	 * @param chargeInfo
	 * @return
	 */
	private static double getPSTTaxableAmount(TaxExemptionInfo taxExemptionInfo, TaxSummaryInfo taxSummaryInfo, ChargeAdjustmentInfo chargeInfo) {
		if (taxExemptionInfo.isPstExemptionInd() || taxSummaryInfo.getPSTAmount() == 0) {
			return 0;
		}
		//The following code is required in case the tax policy ever changes to calculate the PST on base amount + GST
		//The default PSTTaxable amount is the base amount (charge)
		//The full logic has been copied from ReferenceDataFacadeImpl.getTaxCalculationListByProvince
		TaxationPolicyInfo taxPolicy = taxSummaryInfo.getTaxationPolicy();
		if (taxPolicy.getMethod() == TaxationPolicyInfo.METHOD_PST_ON_GST) {
			return chargeInfo.getChargeAmount() + taxSummaryInfo.getGSTAmount();
		} 
		return chargeInfo.getChargeAmount();
	}
}
