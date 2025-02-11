package com.telus.eas.account.credit.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class CreditProgramInfo extends Info {

	static final long serialVersionUID = 1L;

	public static final String TYPE_CLP = "CLP";
	public static final String TYPE_DEPOSIT = "DEPOSIT";
	public static final String TYPE_DECLINED = "DECLINED";
	public static final String TYPE_NDP = "NDP";
	
	private String creditProgramName;
	private String creditProgramType;
	private String creditClassCode;
	private Date creditClassDate;
	private String creditDecisionCode;
	private Date creditDecisionDate;
	private int clpContractTerm;
	private double clpRatePlanAmount;
	private double clpCreditLimitAmount;
	private double securityDepositAmount;
	
	// New Risk Level attributes from WLSCreditManagementSvc 2.0
	private int riskLevelNumber;
	private String riskLevelDecisionCode;
	private Date riskLevelDate;

	public String getCreditProgramName() {
		return creditProgramName;
	}

	public void setCreditProgramName(String creditProgramName) {
		this.creditProgramName = creditProgramName;
	}	

	public String getCreditProgramType() {
		return creditProgramType;
	}

	public void setCreditProgramType(String creditProgramType) {
		this.creditProgramType = creditProgramType;
	}

	public String getCreditClassCode() {
		return creditClassCode;
	}

	public void setCreditClassCode(String creditClassCode) {
		this.creditClassCode = creditClassCode;
	}

	public Date getCreditClassDate() {
		return creditClassDate;
	}

	public void setCreditClassDate(Date creditClassDate) {
		this.creditClassDate = creditClassDate;
	}

	public String getCreditDecisionCode() {
		return creditDecisionCode;
	}

	public void setCreditDecisionCode(String creditDecisionCode) {
		this.creditDecisionCode = creditDecisionCode;
	}

	public Date getCreditDecisionDate() {
		return creditDecisionDate;
	}

	public void setCreditDecisionDate(Date creditDecisionDate) {
		this.creditDecisionDate = creditDecisionDate;
	}

	public int getCLPContractTerm() {
		return clpContractTerm;
	}

	public void setCLPContractTerm(int clpContractTerm) {
		this.clpContractTerm = clpContractTerm;
	}

	public double getCLPRatePlanAmount() {
		return clpRatePlanAmount;
	}

	public void setCLPRatePlanAmount(double clpRatePlanAmount) {
		this.clpRatePlanAmount = clpRatePlanAmount;
	}

	public double getCLPCreditLimitAmount() {
		return clpCreditLimitAmount;
	}

	public void setCLPCreditLimitAmount(double clpCreditLimitAmount) {
		this.clpCreditLimitAmount = clpCreditLimitAmount;
	}

	public double getSecurityDepositAmount() {
		return securityDepositAmount;
	}

	public void setSecurityDepositAmount(double securityDepositAmount) {
		this.securityDepositAmount = securityDepositAmount;
	}	

	public int getRiskLevelNumber() {
		return riskLevelNumber;
	}

	public void setRiskLevelNumber(int riskLevelNumber) {
		this.riskLevelNumber = riskLevelNumber;
	}

	public String getRiskLevelDecisionCode() {
		return riskLevelDecisionCode;
	}

	public void setRiskLevelDecisionCode(String riskLevelDecisionCode) {
		this.riskLevelDecisionCode = riskLevelDecisionCode;
	}

	public Date getRiskLevelDate() {
		return riskLevelDate;
	}

	public void setRiskLevelDate(Date riskLevelDate) {
		this.riskLevelDate = riskLevelDate;
	}

	public String toString() {
		
		StringBuffer s = new StringBuffer();

		s.append("CreditProgramInfo: {\n");
		s.append("    creditProgramName=[").append(getCreditProgramName()).append("]\n");
		s.append("    creditProgramType=[").append(getCreditProgramType()).append("]\n");
		s.append("    creditClassCode=[").append(getCreditClassCode()).append("]\n");
		s.append("    creditClassDate=[").append(getCreditClassDate()).append("]\n");
		s.append("    creditDecisionCode=[").append(getCreditDecisionCode()).append("]\n");
		s.append("    creditDecisionDate=[").append(getCreditDecisionDate()).append("]\n");
		s.append("    clpContractTerm=[").append(getCLPContractTerm()).append("]\n");
		s.append("    clpRatePlanAmount=[").append(getCLPRatePlanAmount()).append("]\n");
		s.append("    clpCreditLimitAmount=[").append(getCLPCreditLimitAmount()).append("]\n");
		s.append("    securityDepositAmount=[").append(getSecurityDepositAmount()).append("]\n");
		s.append("}");

		return s.toString();
	}

}