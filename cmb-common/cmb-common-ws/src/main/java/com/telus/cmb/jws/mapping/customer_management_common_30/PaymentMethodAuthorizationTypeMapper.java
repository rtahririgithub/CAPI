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
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethodAuthorizationType;

/**
 * @author Dimitry Siganevich
 *
 */
public class PaymentMethodAuthorizationTypeMapper extends AbstractSchemaMapper<PaymentMethodAuthorizationType, CreditCardResponseInfo> {

	public PaymentMethodAuthorizationTypeMapper(){
		super(PaymentMethodAuthorizationType.class, CreditCardResponseInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected PaymentMethodAuthorizationType performSchemaMapping(CreditCardResponseInfo source, PaymentMethodAuthorizationType target) {
	     target = new PaymentMethodAuthorizationType();

	     target.setAuthorizationNumber(source.getAuthorizationCode());
	     target.setReferenceNumber(source.getReferenceNum());
	     if (source.getResponseCode()!= null) {
	    	 target.setGlobalPaymentResponseCode(source.getResponseCode());
	     }
	     if (source.getResponseText()!= null) {
	    	 target.setGlobalPaymentResponseText(source.getResponseText());
	     }
	     if (source.getCardVerificationDataResult()!=null) {
	    	 target.setCardVerificationDataResult(source.getCardVerificationDataResult());
	     }
	     
		return super.performSchemaMapping(source, target);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CreditCardResponseInfo performDomainMapping(PaymentMethodAuthorizationType source, CreditCardResponseInfo target) {
		if (source == null) {
			return null;
		}

		target = new CreditCardResponseInfo();
	    target.setAuthorizationCode(source.getAuthorizationNumber());
	    target.setReferenceNum(source.getReferenceNumber());
	    if (source.getGlobalPaymentResponseCode()!= null) {
	    	 target.setResponseCode(source.getGlobalPaymentResponseCode());
	    }
	     if (source.getGlobalPaymentResponseText()!= null) {
	    	 target.setResponseText(source.getGlobalPaymentResponseText());
	     }
	     if (source.getCardVerificationDataResult()!=null) {
	    	 target.setCardVerificationDataResult(source.getCardVerificationDataResult());
	     }
		
		return super.performDomainMapping(source, target);
	}

}
