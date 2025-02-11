package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class SapccThresholdInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	private String thresholdType;
	private double thresholdLimitAmount;		
	
	public String getThresholdType() {
		return thresholdType;
	}

	public void setThresholdType(String thresholdType) {
		this.thresholdType = thresholdType;
	}
	
	public double getThresholdLimitAmount() {
		return thresholdLimitAmount;
	}

	public void setThresholdLimitAmount(double thresholdLimitAmount) {
		this.thresholdLimitAmount = thresholdLimitAmount;
	}
	
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("SapccThresholdInfo [\n");
		buffer.append("   thresholdType=").append(thresholdType).append("\n");
		buffer.append("   thresholdLimitAmount=").append(thresholdLimitAmount).append("\n");	
		buffer.append("]");
		
		return buffer.toString();
	}
}