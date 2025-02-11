/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_30;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCheckResultDeposit;

/**
 * @author Dimitry Siganevich
 *
 */
public class CreditCheckResultMapper extends AbstractSchemaMapper<CreditCheckResult, CreditCheckResultInfo> {

	public CreditCheckResultMapper(){
		super(CreditCheckResult.class, CreditCheckResultInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CreditCheckResult performSchemaMapping(CreditCheckResultInfo source, CreditCheckResult target) {
		target.setCreditClass(source.getCreditClass());
		target.setCreditScore(source.getCreditScore());
		//CreditCheckResultDepositInfo[] creditCheckDeposits = (CreditCheckResultDepositInfo[]) source.getDeposits();
		com.telus.api.account.CreditCheckResultDeposit[] creditCheckDeposits = source.getDeposits();
		if (creditCheckDeposits != null) {
			for (int i = 0; i < creditCheckDeposits.length; i++) {
				CreditCheckResultDeposit deposit = new CreditCheckResultDeposit();
				deposit.setDepositAmount(creditCheckDeposits[i].getDeposit());
				deposit.setProductType(creditCheckDeposits[i].getProductType());
				target.getDeposits().add(deposit);
			}
		}
		target.setLimit(source.getLimit());
		target.setMessage(source.getMessage());
		target.setMessageFrench(source.getMessageFrench());
		target.setReferToCreditAnalystInd(source.isReferToCreditAnalyst());

		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * \(non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CreditCheckResultInfo performDomainMapping(CreditCheckResult source, CreditCheckResultInfo target) {

	      target.setCreditClass(source.getCreditClass());
	      target.setCreditScore(source.getCreditScore());
	      target.setDeposits(mapCreditCheckDeposits(source.getDeposits().toArray(new CreditCheckResultDeposit[0])));
	      target.setLimit(source.getLimit());
	      target.setMessage(source.getMessage());
	      target.setMessageFrench(source.getMessageFrench());
	      target.setReferToCreditAnalyst(source.isReferToCreditAnalystInd());

		return super.performDomainMapping(source, target);
	}

	private CreditCheckResultDepositInfo[] mapCreditCheckDeposits(CreditCheckResultDeposit[] deposits) {
		CreditCheckResultDepositInfo[] depositInfos = null;
		if (deposits != null && deposits.length > 0) {
			depositInfos = new CreditCheckResultDepositInfo[deposits.length];
			for (int i = 0; i < deposits.length; i++) {
				CreditCheckResultDepositInfo depositInfo = new CreditCheckResultDepositInfo();
				depositInfo.setDeposit(deposits[i].getDepositAmount());
				depositInfo.setProductType(deposits[i].getProductType());
				depositInfos[i] = depositInfo;
			}
		}

		return depositInfos;
	}

}
