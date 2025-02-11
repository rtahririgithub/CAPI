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
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.PaymentMethod;


public class PaymentMethodMapper extends AbstractSchemaMapper<PaymentMethod, PaymentMethodInfo> {

	public PaymentMethodMapper(){
		super(PaymentMethod.class, PaymentMethodInfo.class);
	}

	@Override
	protected PaymentMethodInfo performDomainMapping(PaymentMethod source, PaymentMethodInfo target) {
		
		ChequeInfo chequeInfo = new ChequeMapper().mapToDomain(source.getCheque());
		target.setCheque0(chequeInfo);
		
		CreditCardInfo creditCardInfo = new CreditCardMapper().mapToDomain(source.getCreditCard());
		target.setCreditCard0(creditCardInfo);
		
		if (source.getEndDate() != null)
			target.setEndDate(source.getEndDate());
		target.setPaymentMethod(source.getPaymentMethod());
		if (source.getStartDate() != null)
			target.setStartDate(source.getStartDate());
		target.setStatus(source.getStatus());
		target.setStatusReason(source.getStatusReason());
		if (source.isSuppressReturnEnvelopeInd() != null)
			target.setSuppressReturnEnvelope(source.isSuppressReturnEnvelopeInd());

		return super.performDomainMapping(source, target);
	}

}
