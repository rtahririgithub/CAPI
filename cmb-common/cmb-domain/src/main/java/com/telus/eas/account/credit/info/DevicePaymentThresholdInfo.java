package com.telus.eas.account.credit.info;

import com.telus.eas.framework.info.Info;

public class DevicePaymentThresholdInfo extends Info {

	static final long serialVersionUID = 1L;

	private String devicePaymentPlanCode;
	private int noSecurityDepositRequiredMaxNumber;
	private int noDownPaymentRequiredMaxNumber;

	public String getDevicePaymentPlanCode() {
		return devicePaymentPlanCode;
	}

	public void setDevicePaymentPlanCode(String devicePaymentPlanCode) {
		this.devicePaymentPlanCode = devicePaymentPlanCode;
	}

	public int getNoSecurityDepositRequiredMaxNumber() {
		return noSecurityDepositRequiredMaxNumber;
	}

	public void setNoSecurityDepositRequiredMaxNumber(int noSecurityDepositRequiredMaxNumber) {
		this.noSecurityDepositRequiredMaxNumber = noSecurityDepositRequiredMaxNumber;
	}

	public int getNoDownPaymentRequiredMaxNumber() {
		return noDownPaymentRequiredMaxNumber;
	}

	public void setNoDownPaymentRequiredMaxNumber(int noDownPaymentRequiredMaxNumber) {
		this.noDownPaymentRequiredMaxNumber = noDownPaymentRequiredMaxNumber;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("DevicePaymentThresholdInfo: {\n");
		s.append("    devicePaymentPlanCode=[").append(getDevicePaymentPlanCode()).append("]\n");
		s.append("    noSecurityDepositRequiredMaxNumber=[").append(getNoSecurityDepositRequiredMaxNumber()).append("]\n");
		s.append("    noDownPaymentRequiredMaxNumber=[").append(getNoDownPaymentRequiredMaxNumber()).append("]\n");
		s.append("}");

		return s.toString();
	}

}