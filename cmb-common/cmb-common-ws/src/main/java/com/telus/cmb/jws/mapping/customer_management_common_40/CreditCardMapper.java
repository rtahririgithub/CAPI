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
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v4.CreditCard;


/**
 * @author Anitha Durasiamy
 *
 */
public class CreditCardMapper extends AbstractSchemaMapper<CreditCard, CreditCardInfo> {

	public CreditCardMapper(){
		super(CreditCard.class, CreditCardInfo.class);
	}

		
	@Override
	protected CreditCardInfo performDomainMapping(CreditCard source, CreditCardInfo target) {
		if (source == null) {
			return null;
		}
		target.setExpiryMonth(source.getExpiryMonth());
		target.setExpiryYear(source.getExpiryYear());
		target.setHolderName(source.getHolderName());
		target.setToken(source.getToken());
		target.setLeadingDisplayDigits(source.getFirst6());
		target.setTrailingDisplayDigits(source.getLast4());
		target.setCardVerificationData(source.getCardVerificationData());
		return super.performDomainMapping(source, target);
	}

}
