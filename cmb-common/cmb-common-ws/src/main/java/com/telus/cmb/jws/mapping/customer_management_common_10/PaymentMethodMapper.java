/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.customer_management_common_10;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.ChequeInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.Cheque;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PaymentMethod;

/**
 * @author Dimitry Siganevich
 *
 */
public class PaymentMethodMapper extends AbstractSchemaMapper<PaymentMethod, PaymentMethodInfo> {

	public PaymentMethodMapper(){
		super(PaymentMethod.class, PaymentMethodInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PaymentMethod performSchemaMapping(PaymentMethodInfo source, PaymentMethod target) {
		Cheque cheque = new ChequeMapper().mapToSchema(source.getCheque0());
		if (cheque != null)
			target.setCheque(cheque);
		CreditCard cCard = new CreditCardMapper().mapToSchema(source.getCreditCard0());

		target.setCreditCard(cCard);
		target.setEndDate(source.getEndDate());
		target.setPaymentMethod(source.getPaymentMethod());
		target.setStartDate(source.getStartDate());
		target.setStatus(source.getStatus());
		target.setStatusReason(source.getStatusReason());
		target.setSuppressReturnEnvelope(source.getSuppressReturnEnvelope());
		
		return super.performSchemaMapping(source, target);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
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
		if (source.isSuppressReturnEnvelope() != null)
			target.setSuppressReturnEnvelope(source.isSuppressReturnEnvelope());

		return super.performDomainMapping(source, target);
	}

}
