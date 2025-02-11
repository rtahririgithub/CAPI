package com.telus.cmb.common.eligibility;

import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;

public class EsimDeviceSwapEligibilityCheckCriteria extends DefaultEligibilityCheckCriteria {
	private String simType;
	private String newDeviceType;
	private String currentDeviceType;
	
	public String getSimType() {
		return simType;
	}
	
	public void setSimType(String simType) {
		this.simType = simType;
	}
	
	public String getNewDeviceType() {
		return newDeviceType;
	}
	
	public void setNewDeviceType(String newDeviceType) {
		this.newDeviceType = newDeviceType;
	}
	
	public String getCurrentDeviceType() {
		return currentDeviceType;
	}
	
	public void setCurrentDeviceType(String currentDeviceType) {
		this.currentDeviceType = currentDeviceType;
	}
	
	@Override
	public String toString() {
		return "EsimDeviceSwapEligibilityCheckCriteria [simType=" + simType + ", newDeviceType=" + newDeviceType + ", currentDeviceType=" + currentDeviceType + "]";
	}
}
