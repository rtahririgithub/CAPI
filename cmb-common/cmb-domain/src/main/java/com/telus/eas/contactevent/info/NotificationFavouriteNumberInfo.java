package com.telus.eas.contactevent.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;

public class NotificationFavouriteNumberInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	private String serviceCode;
	private String featureCode;
	private Date effectiveDate;

	protected List<String> phoneNumberList;
	
	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<String> getPhoneNumberList() {
		return phoneNumberList;
	}

	public void setPhoneNumberList(List<String> phoneNumberList) {
		this.phoneNumberList = phoneNumberList;
	}

}