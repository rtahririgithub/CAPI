package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class CallingCirclePhoneListInfo extends Info implements
		com.telus.api.account.CallingCirclePhoneList {
	private static final long serialVersionUID = 1L;
	private Date effectiveDate;
	private Date expiryDate;
	private String[] phoneNumberList;

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
	public String[] getPhoneNumberList() {
		return phoneNumberList;
	}
	public void setPhoneNumberList(String[] phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}
}
