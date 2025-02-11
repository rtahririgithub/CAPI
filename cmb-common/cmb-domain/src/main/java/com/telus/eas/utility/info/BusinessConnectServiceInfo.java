package com.telus.eas.utility.info;

public class BusinessConnectServiceInfo extends ReferenceInfo {

	static final long serialVersionUID = 1L;

	protected String businessName;
	protected String businessNameFrench;
	protected String familyType;
	
	public String getBusinessName() {
		return businessName;
	}
	
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessNameFrench() {
		return businessNameFrench;
	}
	
	public void setBusinessNameFrench(String businessNameFrench) {
		this.businessNameFrench = businessNameFrench;
	}
			
	public String getFamilyType() {
		return familyType;
	}

	public void setFamilyType(String familyType) {
		this.familyType = familyType;
	}

}