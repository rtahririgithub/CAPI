package com.telus.eas.account.info;

import com.telus.api.TelusAPIException;

import junit.framework.TestCase;

public class CreditCardInfoTest extends TestCase {

	public void testCopyFrom() throws TelusAPIException {
		String token = "token";
		String leadingDisplayDigits = "leadingDisplayDigits";
		String trailingDispayDigits = "trailingDispayDigits";
		int expiryMonth = 1;
		int expiryYear = 1;
		String holderName = "holderName";
		String cardVerificationData = "cardVerificationData";
		
		CreditCardInfo ccInfo = new CreditCardInfo();
		ccInfo.setToken(token, leadingDisplayDigits, trailingDispayDigits);
		ccInfo.setExpiryMonth(expiryMonth);
		ccInfo.setExpiryYear(expiryYear);
		ccInfo.setHolderName(holderName);
		ccInfo.setCardVerificationData(cardVerificationData);
		
		CreditCardInfo ccInfoCopy = new CreditCardInfo();
		ccInfoCopy.copyFrom(ccInfo);
		assertEquals(token, ccInfoCopy.getToken());
		assertEquals(leadingDisplayDigits, ccInfoCopy.getLeadingDisplayDigits());
		assertEquals(trailingDispayDigits, ccInfoCopy.getTrailingDisplayDigits());
		assertEquals(expiryMonth, ccInfoCopy.getExpiryMonth());
		assertEquals(expiryYear, ccInfoCopy.getExpiryYear());
		assertEquals(holderName.toUpperCase(), ccInfoCopy.getHolderName());
		assertEquals(cardVerificationData, ccInfoCopy.getCardVerificationData());
	}

}
