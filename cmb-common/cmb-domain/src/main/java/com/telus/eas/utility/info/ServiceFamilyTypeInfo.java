package com.telus.eas.utility.info;

import com.telus.api.reference.ServiceFamilyType;
import com.telus.eas.framework.info.Info;

public class ServiceFamilyTypeInfo extends Info implements ServiceFamilyType {

	public static final long serialVersionUID = 1L;
	
	private String socCode;
	private String familyType;
	
	public String getSocCode() {
		return socCode;
	}

	public void setSocCode(String socCode) {
		this.socCode = socCode;
	}
	
	public String getFamilyType() {
		return familyType;
	}
	
	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDescriptionFrench() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCode() {
		return (socCode.trim() + familyType.trim());
	}
	
}
