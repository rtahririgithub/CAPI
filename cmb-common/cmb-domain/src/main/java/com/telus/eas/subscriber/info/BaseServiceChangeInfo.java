package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class BaseServiceChangeInfo extends Info {
	private String code;
	private Date effectiveDate;
	private Date expiryDate;
	private String transactionType;
	
	public String getCode() {
		return code;
	}

	public void setCode(String serviceCode) {
		this.code = serviceCode;
	}
	
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
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	
}
