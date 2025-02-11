package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;
import com.telus.api.account.ActivationOption;
import com.telus.api.account.ActivationOptionType;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jul-2006
 */

public class ActivationOptionInfo extends Info implements ActivationOption {

	static final long serialVersionUID = 1L;

	private ActivationOptionType optionType;
	private double deposit;
	private double creditLimit;
	private String creditClass;
	/**
	 * Maximum contract term length allowed (in months).
	 */
	private int maxContractTerm;
	private double cLPPricePlanLimitAmount;

	public ActivationOptionInfo(ActivationOptionType optionType,
			double deposit, double creditLimit, String creditClass,
			int maxContractTerm) {
		this.optionType = optionType;
		this.deposit = deposit;
		this.creditLimit = creditLimit;
		this.creditClass = creditClass;
		this.maxContractTerm = maxContractTerm;
	}

	public ActivationOptionInfo() {
	}

	public void setOptionType(ActivationOptionType optionType) {
		this.optionType = optionType;
	}

	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public void setCreditLimit(double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public void setCreditClass(String creditClass) {
		this.creditClass = creditClass;
	}

	public void setMaxContractTerm(int maxContractTerm) {
		this.maxContractTerm = maxContractTerm;
	}

	public void setCLPPricePlanLimitAmount(double cLPPricePlanLimitAmount) {
		this.cLPPricePlanLimitAmount = cLPPricePlanLimitAmount;
	}

	public ActivationOptionType getOptionType() {
		return optionType;
	}

	public double getDeposit() {
		return deposit;
	}

	public double getCreditLimit() {
		return creditLimit;
	}

	public String getCreditClass() {
		return creditClass;
	}

	public int getMaxContractTerm() {
		return maxContractTerm;
	}
	
	public double getCLPPricePlanLimitAmount() {
		return cLPPricePlanLimitAmount;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ActivationOptionInfo:{\n");
		buffer.append("\tdeposit=[" + deposit + "]\n");
		buffer.append("\tcreditLimit=[" + creditLimit + "]\n");
		buffer.append("\toption=[" + optionType.toString() + "]\n");
		buffer.append("\tcreditClass=[" + creditClass + "]\n");
		buffer.append("\tCLPPricePlanLimitAmount=[" + cLPPricePlanLimitAmount + "]\n");
		buffer.append("}\n)");
		return buffer.toString();
	}

}
