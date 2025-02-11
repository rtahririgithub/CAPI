/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapper;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.cmb.jws.PaymentMethodChangeHistory;
import com.telus.cmb.jws.PaymentMethodChangeHistory.CreditCardInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.BankAccount;


/**
 * @author Edmir
 *
 */
public class PaymentMethodChangeMapper extends AbstractSchemaMapper<PaymentMethodChangeHistory, PaymentMethodChangeHistoryInfo> {

	public PaymentMethodChangeMapper(){
		super(PaymentMethodChangeHistory.class, PaymentMethodChangeHistoryInfo.class);
		
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PaymentMethodChangeHistory performSchemaMapping(PaymentMethodChangeHistoryInfo source, PaymentMethodChangeHistory target) {
	   
		target = new PaymentMethodChangeHistory();  
	    target.setDate(source.getDate());
	    target.setDirectDebitStatusCode(source.getDirectDebitStatusCode());
	    target.setStatusCode(source.getDirectDebitStatusCode());

	    if (source.getBankCode() != null) {
			BankAccount bi = new BankAccount();
			bi.setBankAccountNumber(source.getBankAccountNumber());
			bi.setBankBranchNumber(source.getBankBranchNumber());
			bi.setBankCode(source.getBankCode());
			target.setBankInfo(bi);
		} 
	     
	    
		if (source.getCreditCardType() != null) {
			CreditCardInfo cc = new CreditCardInfo();
			if (source.getCreditCardExpiry() != null) {	
				cc.setExpiryMonth(Integer.parseInt(source.getCreditCardExpiry().substring(0, 2)));
				cc.setExpiryYear(Integer.parseInt(source.getCreditCardExpiry().substring(2, 4)));
			}			
			cc.setFirst6(source.getCreditCardLeadingDisplayDigits());
			cc.setLast4(source.getCreditCardTrailingDisplayDigits());
			cc.setCreditCardType(source.getCreditCardType());
			cc.setToken(source.getCreditCardToken());
			target.setCreditCardInfo(cc);
		}

		return super.performSchemaMapping(source, target);
	}
	
}
