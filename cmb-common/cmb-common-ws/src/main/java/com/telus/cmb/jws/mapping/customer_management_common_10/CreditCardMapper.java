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
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCard;

/**
 * @author Dimitry Siganevich
 *
 */
public class CreditCardMapper extends AbstractSchemaMapper<CreditCard, CreditCardInfo> {

	public CreditCardMapper(){
		super(CreditCard.class, CreditCardInfo.class);
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performSchemaMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CreditCard performSchemaMapping(CreditCardInfo source, CreditCard target) {
	     target = null;

	      if (source.getToken() != null && !source.getToken().trim().equals("")) {
	        target = new CreditCard ();
	        
	        if (source.getAuthorizationCode() != null) 
	        	target.setAuthorizationCode(source.getAuthorizationCode());
	        else
	        	target.setAuthorizationCode("");
	        target.setExpiryMonth(source.getExpiryMonth());
	        target.setExpiryYear(source.getExpiryYear());
	        if (source.getHolderName() != null)
	        	target.setHolderName(source.getHolderName());
	        else
	        	target.setHolderName("");
	       target.setToken(source.getToken());
	       target.setFirst6(source.getLeadingDisplayDigits());
	       target.setLast4(source.getTrailingDisplayDigits());
	    }

		return super.performSchemaMapping(source, target);
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.mapping.AbstractSchemaMapper#performDomainMapping(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected CreditCardInfo performDomainMapping(CreditCard source, CreditCardInfo target) {
		if (source == null) {
			return null;
		}
		target.setAuthorizationCode(source.getAuthorizationCode());
		target.setExpiryMonth(source.getExpiryMonth());
		target.setExpiryYear(source.getExpiryYear());
		target.setHolderName(source.getHolderName());
		target.setToken(source.getToken());
		target.setLeadingDisplayDigits(source.getFirst6());
		target.setTrailingDisplayDigits(source.getLast4());
		return super.performDomainMapping(source, target);
	}

}
