/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_40;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.CreditCheckResultDeposit;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.CreditDecision;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.ReferToCreditAnalyst;


/**
 * @author Anitha Duraisamy
 *
 */
public class CreditCheckResultMapper extends AbstractSchemaMapper<CreditCheckResult, CreditCheckResultInfo> {

	public CreditCheckResultMapper(){
		super(CreditCheckResult.class, CreditCheckResultInfo.class);
	}

	
	@Override
	protected CreditCheckResultInfo performDomainMapping(CreditCheckResult source, CreditCheckResultInfo target) {

	      target.setCreditClass(source.getCreditClass());
	      target.setCreditScore(source.getCreditScore());
	      target.setDeposits(mapCreditCheckDeposits(source.getDepositList().toArray(new CreditCheckResultDeposit[0])));
	      target.setLimit(source.getCreditLimit());
	      target.setMessage(source.getCreditDecision().getCreditDecisionMessage());
	      target.setMessageFrench(source.getCreditDecision().getCreditDecisionMessageFrench());
	      target.setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
	      if(source.getReferToCreditAnalyst()!=null){
		      target.getReferToCreditAnalyst().setReferToCreditAnalyst(source.getReferToCreditAnalyst().isReferToCreditAnalystInd());
		      target.getReferToCreditAnalyst().setReasonCode(source.getReferToCreditAnalyst().getReasonCode());
		      target.getReferToCreditAnalyst().setReasonMessage(source.getReferToCreditAnalyst().getReasonMessage());
	     }
	      
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
	
	@Override
	protected CreditCheckResult performSchemaMapping(CreditCheckResultInfo source, CreditCheckResult target) {
		target.setCreditClass(source.getCreditClass());
		target.setCreditScore(source.getCreditScore());
		CreditCheckResultDepositInfo[] creditCheckDeposits = (CreditCheckResultDepositInfo[]) source.getDeposits();
		if (creditCheckDeposits != null) {
			for (int i = 0; i < creditCheckDeposits.length; i++) {
				CreditCheckResultDeposit deposit = new CreditCheckResultDeposit();
				deposit.setDepositAmount(creditCheckDeposits[i].getDeposit());
				deposit.setProductType(creditCheckDeposits[i].getProductType());
				target.getDepositList().add(deposit);
			}
		}
		
		target.setCreditLimit(source.getLimit());
		if(source.getMessage()!=null || source.getMessageFrench()!=null){
			CreditDecision creditDecision= new CreditDecision();
			creditDecision.setCreditDecisionMessage(source.getMessage());
			creditDecision.setCreditDecisionMessageFrench(source.getMessageFrench());
			target.setCreditDecision(creditDecision);
		}
		
		target.setReferToCreditAnalyst(new ReferToCreditAnalystMapper().mapToSchema(source.getReferToCreditAnalyst()));
		if(source.getBureauFile()!=null)
			target.setBureauFile(source.getBureauFile().getBytes());

		return super.performSchemaMapping(source, target);
	}

	
	
}
