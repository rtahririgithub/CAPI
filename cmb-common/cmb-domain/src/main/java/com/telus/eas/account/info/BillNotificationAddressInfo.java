package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class BillNotificationAddressInfo extends Info {
	
	private static final long serialVersionUID = 1L;

	private Date effectiveDate;
	private Date expiryDate;
	private String phoneNumber;
	private String emailAddress;

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	@Override
	public String toString() {
		return "BillNotificationAddressInfo [effectiveDate=" + effectiveDate + ", expiryDate=" + expiryDate + ", phoneNumber=" + phoneNumber + ", emailAddress=" + emailAddress + "]";
	}

}
