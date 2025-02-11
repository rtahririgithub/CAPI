package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

public class BusinessRegistrationInfo extends Info {

	static final long serialVersionUID = 1L;

	private String businessRegistrationNumber;
	private String businessRegistrationType;

	public String getBusinessRegistrationNumber() {
		return businessRegistrationNumber;
	}

	public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
		this.businessRegistrationNumber = businessRegistrationNumber;
	}

	public String getBusinessRegistrationType() {
		return businessRegistrationType;
	}

	public void setBusinessRegistrationType(String businessRegistrationType) {
		this.businessRegistrationType = businessRegistrationType;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("BusinessRegistrationInfo:{\n");
		s.append("    businessRegistrationNumber=[").append(businessRegistrationNumber).append("]\n");
		s.append("    businessRegistrationType=[").append(businessRegistrationType).append("]\n");
		s.append("}");

		return s.toString();
	}

}
