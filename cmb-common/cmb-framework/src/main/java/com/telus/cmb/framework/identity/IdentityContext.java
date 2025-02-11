package com.telus.cmb.framework.identity;

public class IdentityContext {

	// the original principal associate to the WebServiceContext.
	private String principal = "none";

	//
	private String telusTID;
	//
	private String appId;

	private String telusHeaderAppId;

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getTelusTID() {
		return telusTID;
	}

	public void setTelusTID(String telusTID) {
		this.telusTID = telusTID;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTelusHeaderAppId() {
		return telusHeaderAppId;
	}

	public void setTelusHeaderAppId(String telusHeaderAppId) {
		this.telusHeaderAppId = telusHeaderAppId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IdentityContext [principal=").append(principal)
				.append(", telusTID=").append(telusTID).append(", appId=")
				.append(appId).append(", telusHeaderAppId=")
				.append(telusHeaderAppId).append("]");
		return builder.toString();
	}

}