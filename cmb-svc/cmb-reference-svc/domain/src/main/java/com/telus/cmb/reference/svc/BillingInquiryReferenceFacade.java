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

import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
import com.telus.api.reference.TaxationPolicy;
import com.telus.eas.framework.exception.TelusException;
import com.telus.eas.utility.info.AdjustmentReasonInfo;
import com.telus.eas.utility.info.BillCycleInfo;
import com.telus.eas.utility.info.ChargeTypeInfo;
import com.telus.eas.utility.info.PaymentMethodInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.eas.utility.info.PrepaidRechargeDenominationInfo;
import com.telus.eas.utility.info.TaxationPolicyInfo;

/**
 * @author Pavel Simonovsky
 *
 */
public interface BillingInquiryReferenceFacade {

	PaymentMethodInfo[] getPaymentMethods() throws TelusException;

	PaymentSourceTypeInfo[] getPaymentSourceTypes() throws TelusException;

	PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations() throws TelusException;

	/**
	 * Retrieves all the credits (adjustments) that can be applied to an account.
	 * 
	 * @return AdjustmentReasonInfo[] an array of adjustment reasons
	 * @throws TelusException
	 */
	AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException;

	/**
	 * Retrieves the credit (adjustment) by code that can be applied to an account.
	 * 
	 * @param String code
	 * 
	 * @return AdjustmentReasonInfo an adjustment reason
	 * @throws TelusException
	 */
	AdjustmentReasonInfo getAdjustmentReason(String code) throws TelusException;

	/**
	 * Retrieves the one-time charge details for all manual charge types.
	 * 
	 * @return ChargeTypeInfo[] an array of one-time manual charge types
	 * @throws TelusException
	 */
	ChargeTypeInfo[] getManualChargeTypes() throws TelusException;

	/**
	 * Retrieves the one-time charge details by code for a manual charge type.
	 * 
	 * @param String code
	 * 
	 * @return ChargeTypeInfo a one-time manual charge type
	 * @throws TelusException
	 */
	ChargeTypeInfo getManualChargeType(String code) throws TelusException;

	/**
	 * Retrieves the one-time charge details for paper bill on the account.
	 *
	 * Any of the parameters passed can be null or empty, however there must be at
	 * least one parameter populated, otherwise will throw an IllegalArgumentException.
	 * 
	 * @param int brand Id, can be zero for wildcard
	 * @param String province code, can be null or empty for wildcard
	 * @param char account type, can be null (that is '\u0000') for wildcard
	 * @param char account sub-type, can be null (that is '\u0000') for wildcard
	 * @param String account GL segment, can be null or empty for wildcard
	 * @param String invoiceSuppressionLevel, can be null or empty for wildcard
	 * 
	 * @return ChargeTypeInfo a one-time charge type
	 * @throws TelusException
	 */
	ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode, char accountType,
			char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException;
	
	
	/**
	 * Retrieves Taxation Policy list filtered by province code
	 * 
	 * @param provinceCode
	 * @return TaxationPolicy
	 * @throws TelusException
	 */
	TaxationPolicy getTaxationPolicy(String provinceCode) throws TelusException;
	
	/**
	 * Retrieves Taxation Policy list filtered by province code
	 * 
	 * @param provinceCode
	 * @return TaxationPolicy
	 * @throws TelusException
	 */
	TaxationPolicyInfo[] getTaxationPolicies() throws TelusException;
	
	/**
	 * Method calculates applicable taxes for input amount based on province code and tax exemption information
	 * 
	 * @param provinceCode
	 * @param amount
	 * @param taxExemptionInfo
	 * @return TaxSummaryInfo
	 * @throws TelusException
	 */
	TaxSummaryInfo getTaxCalculationListByProvince(String provinceCode, double amount, 
			TaxExemptionInfo taxExemptionInfo) throws TelusException;

	
	BillCycleInfo getBillCycle(String code) throws TelusException;
	
	BillCycleInfo[] getBillCycles() throws TelusException;
}
