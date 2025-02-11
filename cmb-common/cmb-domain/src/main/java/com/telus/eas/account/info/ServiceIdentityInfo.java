package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class ServiceIdentityInfo extends Info  {
	
	private static final long serialVersionUID = -357000938555109044L;
	
	private String serviceCode;
	private Date effectiveDate;
	

	public String getServiceCode() {
		return serviceCode;
	}


	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}


	public Date getEffectiveDate() {
		return effectiveDate;
	}


	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}


	public String toString() {
		return "ServiceIdentityInfo [serviceCode=" + serviceCode + ", effectiveDate=" + effectiveDate + "]";
	}
	

}
