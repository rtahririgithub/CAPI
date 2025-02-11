package com.telus.eas.account.credit.info;

import java.math.BigDecimal;
import java.util.List;

import com.telus.eas.framework.info.Info;

public class DevicePaymentPlanInfo extends Info {

	static final long serialVersionUID = 1L;

	public static final String TYPE_FINANCIAL = "FINANCIAL";
	public static final String TYPE_TAB = "TAB";
	public static final String TYPE_SUBSIDY = "SUBSIDY";
	public static final String TYPE_BYOD = "BYOD";
	public static final String TYPE_LEASING = "LEASING";

	private String devicePaymentPlanType;
	private boolean devicePaymentPlanEligibilityInd;
	private List<String> devicePaymentPlanEligibilityReasonList;
	// TODO Determine if devicePaymentPlanCode contains the same values as devicePaymentPlanType
	private String devicePaymentPlanCode;
	// Note: devicePaymentPlanAmount represents different things depending upon the plan type; e.g., if plan type = FINANCIAL, this
	// represents the downpayment amount, if plan type = TAB, this is the tab amount, and for SUBSIDY and BYOD this attribute is not
	// used at all and should always be zero.
	private BigDecimal devicePaymentPlanAmount;
	private BigDecimal securityDepositAmount;
	
	public String getDevicePaymentPlanType() {
		return devicePaymentPlanType;
	}

	public void setDevicePaymentPlanType(String devicePaymentPlanType) {
		this.devicePaymentPlanType = devicePaymentPlanType;
	}

	public boolean isDevicePaymentPlanEligible() {
		return devicePaymentPlanEligibilityInd;
	}

	public void setDevicePaymentPlanEligibilityInd(boolean devicePaymentPlanEligibilityInd) {
		this.devicePaymentPlanEligibilityInd = devicePaymentPlanEligibilityInd;
	}

	public List<String> getDevicePaymentPlanEligibilityReasonList() {
		return devicePaymentPlanEligibilityReasonList;
	}

	public void setDevicePaymentPlanEligibilityReasonList(List<String> devicePaymentPlanEligibilityReasonList) {
		this.devicePaymentPlanEligibilityReasonList = devicePaymentPlanEligibilityReasonList;
	}

	public String getDevicePaymentPlanCode() {
		return devicePaymentPlanCode;
	}

	public void setDevicePaymentPlanCode(String devicePaymentPlanCode) {
		this.devicePaymentPlanCode = devicePaymentPlanCode;
	}

	public BigDecimal getDevicePaymentPlanAmount() {
		return devicePaymentPlanAmount;
	}

	public void setDevicePaymentPlanAmount(BigDecimal devicePaymentPlanAmount) {
		this.devicePaymentPlanAmount = devicePaymentPlanAmount;
	}

	public BigDecimal getSecurityDepositAmount() {
		return securityDepositAmount;
	}

	public void setSecurityDepositAmount(BigDecimal securityDepositAmount) {
		this.securityDepositAmount = securityDepositAmount;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();

		s.append("DevicePaymentPlanInfo: {\n");
		s.append("    devicePaymentPlanType=[").append(getDevicePaymentPlanType()).append("]\n");
		s.append("    devicePaymentPlanEligibilityInd=[").append(isDevicePaymentPlanEligible()).append("]\n");
		s.append("    devicePaymentPlanEligibilityReasonList=[\n");
		if (getDevicePaymentPlanEligibilityReasonList() != null && !getDevicePaymentPlanEligibilityReasonList().isEmpty()) {
			for (String reason : getDevicePaymentPlanEligibilityReasonList()) {
				s.append(reason).append("\n");
			}
		} else {
			s.append("    <null>\n");
		}
		s.append("    ]\n");
		s.append("    devicePaymentPlanCode=[").append(getDevicePaymentPlanCode()).append("]\n");
		s.append("    devicePaymentPlanAmount=[").append(getDevicePaymentPlanAmount()).append("]\n");
		s.append("    securityDepositAmount=[").append(getSecurityDepositAmount()).append("]\n");
		s.append("}");

		return s.toString();
	}

}