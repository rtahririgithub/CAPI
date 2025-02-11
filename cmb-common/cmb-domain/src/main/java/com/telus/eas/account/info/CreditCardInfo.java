/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;

import com.telus.api.*;
import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

/**
 * @author liaomi
 *
 */
public class CreditCardInfo extends Info implements CreditCard {

	static final long serialVersionUID = 1L;

	private int expiryMonth;
	private int expiryYear;
	private String holderName = "";
	private String authorizationCode;
	private boolean modified;
	private String token;
	private String leadingDisplayDigits;
	private String trailingDisplayDigits;
	private String cardVerificationData;

	public int getExpiryMonth() {
		return expiryMonth;
	}

	public void setExpiryMonth(int expiryMonth) {
		this.expiryMonth = expiryMonth;
		modified = true;
	}

	public int getExpiryYear() {
		return expiryYear;
	}

	public void setExpiryYear(int expiryYear) {
		this.expiryYear = expiryYear;
		modified = true;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = toUpperCase(holderName);
		modified = true;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
		modified = true;
	}

	public boolean isModified() {
		return modified;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public void commit() {
		this.modified = false;
	}

	public boolean getNeedsValidation() {
		return modified && getToken() != null;
	}

	public Date getExpiryDate() {

		if (expiryYear == 0) {
			return null;
		} else {
			// return last day of expiry month as date
			GregorianCalendar gc = new GregorianCalendar(expiryYear, expiryMonth, 1);
			gc.add(java.util.Calendar.DATE, -1);

			return gc.getTime();
		}
	}

	public void copyFrom(CreditCard o) {

		if (o != null) {
			token = o.getToken();
			leadingDisplayDigits = o.getLeadingDisplayDigits();
			trailingDisplayDigits = o.getTrailingDisplayDigits();
			expiryMonth = o.getExpiryMonth();
			expiryYear = o.getExpiryYear();
			holderName = toUpperCase(o.getHolderName());
			if (!isEmpty(o.getCardVerificationData())) {
				cardVerificationData = o.getCardVerificationData();
			}
			if (o instanceof CreditCardInfo) {
				modified = ((CreditCardInfo) o).modified;
			}
		}
	}

	public boolean equals(CreditCard o) {

		if (!(o instanceof CreditCardInfo)) {
			return false;
		}
		CreditCardInfo a = (CreditCardInfo) o;

		return expiryMonth == a.expiryMonth && expiryYear == a.expiryYear && modified == a.modified && compare(token, a.token) && compare(holderName, a.holderName);
	}

	public boolean equals(Object o) {
		return o != null && o instanceof CreditCard && equals((CreditCard) o);
	}

	public Address getAddress() {
		throw new UnsupportedOperationException("Method not implemented here");
	}

	public boolean isExpired() {

		Calendar today = Calendar.getInstance();
		if (expiryYear == today.get(Calendar.YEAR)) {
			return expiryMonth <= today.get(Calendar.MONTH) + 1;
		}

		return expiryYear < today.get(Calendar.YEAR);
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("CreditCardInfo:{\n");
		s.append("    trailingDigits=[").append(trailingDisplayDigits).append("]\n");
		s.append("    expiryMonth=[").append(expiryMonth).append("]\n");
		s.append("    expiryYear=[").append(expiryYear).append("]\n");
		s.append("    holderName=[").append(holderName).append("]\n");
		s.append("    authorizationCode=[").append(authorizationCode).append("]\n");
		s.append("    modified=[").append(modified).append("]\n");
		s.append("	  cardVerificationData=[").append(cardVerificationData).append("]\n");
		s.append("}");

		return s.toString();
	}

	// PCI related changes
	public void clear() {
		this.token = null;
		this.leadingDisplayDigits = null;
		this.trailingDisplayDigits = null;
	}

	public boolean hasToken() {
		return token != null && token.trim().length() > 0;
	}

	public boolean isSame(CreditCardInfo o) {
		return expiryMonth == o.expiryMonth && expiryYear == o.expiryYear && compare(token, o.token);
	}

	public String getLeadingDisplayDigits() {
		return leadingDisplayDigits;
	}

	public String getToken() {
		return token;
	}

	public String getTrailingDisplayDigits() {
		return trailingDisplayDigits;
	}

	public void setToken(String token, String leadingDisplayDigits, String trailingDispayDigits) throws TelusAPIException {
		this.token = token;
		setLeadingDisplayDigits(leadingDisplayDigits);
		setTrailingDisplayDigits(trailingDispayDigits);
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setLeadingDisplayDigits(String leadingDisplayDigits) {
		this.leadingDisplayDigits = leadingDisplayDigits;
	}

	public void setTrailingDisplayDigits(String trailingDispayDigits) {
		this.trailingDisplayDigits = trailingDispayDigits;
	}

	@Deprecated
	public void setType(String type) {
		// This operation does nothing, type is determined by first digit of card number
	}

	public String getType() {

		if (leadingDisplayDigits != null && leadingDisplayDigits.matches("[0-9].*")) {
			return determineCardType(Integer.parseInt(leadingDisplayDigits.substring(0, 1)));
		} else {
			return "";
		}
	}

	public String getCardVerificationData() {
		return cardVerificationData;
	}

	public void setCardVerificationData(String cardVerificationData) {
		this.cardVerificationData = cardVerificationData;
	}

	public static String determineCardType(int firstDigit) {

		String cardType;
		switch (firstDigit) {
		// Mastercard first digit is both 2 and 5
		case 2:
		case 5:
			cardType = "M";
			break;
		case 3:
			cardType = "AE";
			break;
		case 4:
			cardType = "VL";
			break;
		default:
			cardType = "";
			break;
		}

		return cardType;
	}

	public void validate(String reason, String businessRole, AuditHeader auditHeader) throws TelusAPIException, InvalidCreditCardException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

}