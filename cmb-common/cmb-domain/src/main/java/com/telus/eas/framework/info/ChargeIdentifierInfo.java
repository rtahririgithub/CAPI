package com.telus.eas.framework.info;

public class ChargeIdentifierInfo implements  java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int accountNumber;
	private Double chargeSequenceNumber;

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Double getChargeSequenceNumber() {
		return chargeSequenceNumber;
	}

	public void setChargeSequenceNumber(Double chargeSequenceNumber) {
		this.chargeSequenceNumber = chargeSequenceNumber;
	}

	public String toString() {
		return "ChargeIdentifierInfo [accountNumber=" + accountNumber
				+ ", chargeSequenceNumber=" + chargeSequenceNumber + "]";
	}
	
}
