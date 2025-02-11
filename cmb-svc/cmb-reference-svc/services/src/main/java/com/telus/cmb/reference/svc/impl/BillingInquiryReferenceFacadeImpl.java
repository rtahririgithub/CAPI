/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.svc.impl;

import javax.ejb.Remote;
import javax.ejb.RemoteHome;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import com.telus.api.reference.TaxationPolicy;
import com.telus.cmb.reference.svc.BillingInquiryReferenceFacade;
import com.telus.eas.account.info.TaxExemptionInfo;
import com.telus.eas.account.info.TaxSummaryInfo;
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
@Stateless(name="BillingInquiryReferenceFacade", mappedName="BillingInquiryReferenceFacade")
@Remote(BillingInquiryReferenceFacade.class)
@RemoteHome(BillingInquiryReferenceFacadeHome.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@Interceptors(SpringBeanAutowiringInterceptor.class)

public class BillingInquiryReferenceFacadeImpl implements BillingInquiryReferenceFacade {

	@Autowired
	private BillingInquiryReferenceFacade facade;
	
	public void setBillingInquiryReferenceFacade(BillingInquiryReferenceFacade facade) {
		this.facade = facade;
	}
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPaymentMethods()
	 */
	@Override
	public PaymentMethodInfo[] getPaymentMethods() throws TelusException {
		return facade.getPaymentMethods();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPaymentSourceTypes()
	 */
	@Override
	public PaymentSourceTypeInfo[] getPaymentSourceTypes() throws TelusException {
		return facade.getPaymentSourceTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPrepaidRechargeDenominations()
	 */
	@Override
	public PrepaidRechargeDenominationInfo[] getPrepaidRechargeDenominations() throws TelusException {
		return facade.getPrepaidRechargeDenominations();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getAdjustmentReasons()
	 */
	@Override
	public AdjustmentReasonInfo[] getAdjustmentReasons() throws TelusException {
		return facade.getAdjustmentReasons();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getAdjustmentReason(java.lang.String)
	 */
	@Override
	public AdjustmentReasonInfo getAdjustmentReason(String code) throws TelusException {
		return facade.getAdjustmentReason(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getManualChargeTypes()
	 */
	@Override
	public ChargeTypeInfo[] getManualChargeTypes() throws TelusException {
		return facade.getManualChargeTypes();
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getManualChargeType(java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getManualChargeType(String code) throws TelusException {
		return facade.getManualChargeType(code);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getPaperBillChargeType(int, java.lang.String, char, char, java.lang.String)
	 */
	@Override
	public ChargeTypeInfo getPaperBillChargeType(int brandId, String provinceCode, char accountType,
			char accountSubType, String segment, String invoiceSuppressionLevel) throws TelusException {
		return facade.getPaperBillChargeType(brandId, provinceCode, accountType, accountSubType, segment, invoiceSuppressionLevel);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getTaxationPolicy(java.lang.String)
	 */
	@Override
	public TaxationPolicy getTaxationPolicy(String provinceCode)
			throws TelusException {
		return facade.getTaxationPolicy(provinceCode);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getTaxationPoliciesList()
	 */
	@Override
	public TaxationPolicyInfo[] getTaxationPolicies() throws TelusException {
		return facade.getTaxationPolicies();
	}	
	
	/* (non-Javadoc)
	 * @see com.telus.cmb.reference.svc.BillingInquiryReferenceFacade#getTaxCalculationListByProvince(java.lang.String, double, com.telus.api.account.TaxExemption)
	 */
	@Override
	public TaxSummaryInfo getTaxCalculationListByProvince(String provinceCode, double amount, TaxExemptionInfo taxExemptionInfo) throws TelusException {
		return facade.getTaxCalculationListByProvince(provinceCode, amount, taxExemptionInfo);
	}

	@Override
	public BillCycleInfo getBillCycle(String code) throws TelusException {
		return facade.getBillCycle(code);
	}

	@Override
	public BillCycleInfo[] getBillCycles() throws TelusException {
		return facade.getBillCycles();
	}

}
