package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class PrepaidSubscriberInfo extends Info{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int Ban;
	String phoneNumber;
	Date expiryDate;
	long rateId;
	String language;
	String serialNumber;
	
	
	public int getBan() {
		return Ban;
	}
	public void setBan(int ban) {
		Ban = ban;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Date getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	public long getRateId() {
		return rateId;
	}
	public void setRateId(long rateId) {
		this.rateId = rateId;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String toString() {
		return "PrepaidSubscriberInfo [Ban=" + Ban + ", phoneNumber=" + phoneNumber + ", expiryDate=" + expiryDate + ", rateId=" + rateId + ", language=" + language + ", serialNumber=" + serialNumber
				+ "]";
	}

}
