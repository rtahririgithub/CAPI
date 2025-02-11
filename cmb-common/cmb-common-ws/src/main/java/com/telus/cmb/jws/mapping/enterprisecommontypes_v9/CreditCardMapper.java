/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws.mapping.enterprisecommontypes_v9;

import com.telus.cmb.jws.mapping.AbstractSchemaMapper;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.CreditCard;

/**
 * @author R. Fong
 * 
 */
public class CreditCardMapper extends AbstractSchemaMapper<CreditCard, CreditCardInfo> {

	private static CreditCardMapper INSTANCE;

	private CreditCardMapper() {
		super(CreditCard.class, CreditCardInfo.class);
	}

	public static CreditCardMapper getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CreditCardMapper();
		}
		return INSTANCE;
	}

	@Override
	protected CreditCard performSchemaMapping(CreditCardInfo source, CreditCard target) {

		if (source.getToken() != null && !source.getToken().isEmpty()) {		
			target = new CreditCard();
			target.setExpiryMonth(source.getExpiryMonth());
			target.setExpiryYear(source.getExpiryYear());
			if (source.getHolderName() != null) {
				target.setHolderName(source.getHolderName());
			} else {
				target.setHolderName("");
			}
			target.setToken(source.getToken());
			target.setFirst6(source.getLeadingDisplayDigits());
			target.setLast4(source.getTrailingDisplayDigits());
			target.setCardVerificationData(source.getCardVerificationData());
		}

		return super.performSchemaMapping(source, target);
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
