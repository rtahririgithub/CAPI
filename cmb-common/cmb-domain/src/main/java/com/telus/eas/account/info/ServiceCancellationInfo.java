package com.telus.eas.account.info;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceCancellationInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Date effectiveDate;
	private List phoneNumbers;
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public List getPhoneNumbers() {
		if (phoneNumbers==null) 
			phoneNumbers= new ArrayList();
		return phoneNumbers;
	}
	public void setPhoneNumbers(List phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

}
