package com.telus.eas.contactevent.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class RoamingServiceNotificationInfo extends Info {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceCode;
	private String rlhInd;
	private String ppacRate;
	private String pprcRate;
	private List<String> socGroups;
	
	
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getRlhInd() {
		return rlhInd;
	}
	public void setRlhInd(String rlhInd) {
		this.rlhInd = rlhInd;
	}
	
	public String getPpacRate() {
		return ppacRate;
	}
	public void setPpacRate(String ppacRate) {
		this.ppacRate = ppacRate;
	}
	public String getPprcRate() {
		return pprcRate;
	}
	public void setPprcRate(String pprcRate) {
		this.pprcRate = pprcRate;
	}
	
	public List<String> getSocGroups() {
		return socGroups;
	}
	public void setSocGroups(List<String> socGroups) {
		this.socGroups = socGroups;
	}
	
	@Override
	public String toString() {
		return "RoamingServiceNotificationInfo [serviceCode=" + serviceCode
				+ ", rlhInd=" + rlhInd + ", ppacRate=" + ppacRate
				+ ", pprcRate=" + pprcRate + ", socGroups=" + socGroups + "]";
	}
}
