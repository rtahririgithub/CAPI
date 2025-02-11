package com.telus.cmb.common.kafka;

public enum TransactionType {
	ADD("ADD"), REMOVE("REMOVE"), MODIFY("MODIFY"), NO_CHG("NO_CHG");

	private String transactionType;

	private TransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getValue() {
		return transactionType;
	}
	
}
