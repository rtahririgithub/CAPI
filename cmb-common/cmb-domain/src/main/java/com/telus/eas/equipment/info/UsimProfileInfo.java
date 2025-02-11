package com.telus.eas.equipment.info;

public class UsimProfileInfo implements java.io.Serializable {
	private long productId;
	private String simType;
	private String profileId;
	private String profileCode;
	private String profileDescription;
	private String profileVersion;
	private static final long serialVersionUID = 1L;
	
	public long getProductId() {
		return productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public String getSimType() {
		return simType;
	}
	
	public void setSimType(String simType) {
		this.simType = simType;
	}
	
	public String getProfileId() {
		return profileId;
	}
	
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	public String getProfileCode() {
		return profileCode;
	}
	
	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}
	
	public String getProfileDescription() {
		return profileDescription;
	}
	
	public void setProfileDescription(String profileDescription) {
		this.profileDescription = profileDescription;
	}
	
	public String getProfileVersion() {
		return profileVersion;
	}
	
	public void setProfileVersion(String profileVersion) {
		this.profileVersion = profileVersion;
	}

	@Override
	public String toString() {
		return "UsimProfileInfo [productId=" + productId + ", simType=" + simType + ", profileId=" + profileId + ", profileCode=" + profileCode + ", profileDescription=" + profileDescription + ", profileVersion=" + profileVersion + "]";
	}
}