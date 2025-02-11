/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * <CODE>CreditCard</CODE>
 * 
 */
public interface CreditCard extends PaymentCard {


	Address getAddress();

	int getExpiryMonth();

	void setExpiryMonth(int expiryMonth);

	int getExpiryYear();

	void setExpiryYear(int expiryYear);

	String getHolderName();

	/**
	 * Set the Credit Card Holder Name. For consumer accounts, it's lastName;
	 * for business accounts it's legalBusinessName.
	 * 
	 */
	void setHolderName(String holderName);

	String getAuthorizationCode();

	void setAuthorizationCode(String authorizationCode);

	void copyFrom(CreditCard o);

	boolean equals(CreditCard o);

	boolean equals(Object o);


	/**
	 * Authenticates this card at the bank. This method returns silently if the
	 * card is valid, otherwise it throws an InvalidCreditCardException.
	 * 
	 * <P>
	 * This method may involve a remote method call.
	 * 
	 * @param reason  the purpose of this validation attempt.
	 * @param businessRole 
	 * @param auditHeader
	 * 
	 * @see AuditHeader
s	 */
	void validate(String reason, String businessRole, AuditHeader auditHeader)	throws TelusAPIException, InvalidCreditCardException;

	/**
	 * @param token of the credit card number
	 * @param leadingDisplayDigits is first 6 digits of the credit card number
	 * @param trailingDisplayDigits is last 4 digits of the credit card number
	 * @throws TelusAPIException if any of input parameter is null
	 */
	void setToken(String token, String leadingDisplayDigits, String trailingDisplayDigits) throws TelusAPIException;

	/**
	 * Returns the token that represent the credit card number.As a general rule, application should not 
	 * show token to user. It should only be displayed to privileged users, such as the fraud investigation team
	 * 
	 * @return the token that represent the credit card number
	 * 
	 * @see #getTrailingDisplayDigits()
	 * @see #getLeadingDisplayDigits()
	 */
	String getToken();

	/**
	 * Returns the leading credit card digits that the TELUS security policy allows applications to display 
	 * to privileged users. As a general rule, applications should not show these digits to users. 
	 * They should only be displayed to privileged users, such as the fraud investigation team.
	 * 
	 * @return String
	 *  
	 * @see #getTrailingDisplayDigits()
	 * @see #getToken()
	 */
	String getLeadingDisplayDigits();

	/**
	 * Returns the trailing credit card digits that the TELUS security policy allows applications to display.
	 * @return trailing credit card digits
	 *  
	 * @see #getLeadingDisplayDigits()
	 * @see #getToken()
	 */
	String getTrailingDisplayDigits();
	
	void setCardVerificationData(String cardVerificationData);
	
	String getCardVerificationData();
}
