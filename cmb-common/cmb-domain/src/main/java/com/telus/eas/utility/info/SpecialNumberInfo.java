package com.telus.eas.utility.info;

import com.telus.api.reference.SpecialNumber;
import com.telus.eas.framework.info.Info;

public class SpecialNumberInfo extends Info implements SpecialNumber {
	private static final long serialVersionUID = 1L;
	private String specialNumber;
	private String description;
	private String descriptionFrench;

	public String getSpecialNumber() {
		return specialNumber;
	}

	public String getCode() {
		return getSpecialNumber();
	}

	public String getDescription() {
		return description;
	}

	public String getDescriptionFrench() {
		return descriptionFrench;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDescriptionFrench(String descriptionFrench) {
		this.descriptionFrench = descriptionFrench;
	}

	public void setSpecialNumber(String specialNumber) {
		this.specialNumber = specialNumber;
	}

}
