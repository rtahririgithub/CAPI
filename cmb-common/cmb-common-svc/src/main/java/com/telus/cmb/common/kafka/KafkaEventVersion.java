package com.telus.cmb.common.kafka;

public enum KafkaEventVersion {
	
	MAKE_PAYMENT("1"),PAYMENT_METHOD_CHANGE("1"), SUBSCRIBER_ACTIVATE("2"), SERVICE_AGREEMENT_CHANGE("2"),SUBSCRIBER_MOVE("2"),SUBSCRIBER_STATUS_CHANGE("2"),PHONENUMBER_CHANGE("2"),
	ACCOUNT_STATUS_CHANGE("1"),CREATE_CREDIT("1"),CHARGE_ADJUSTMENT("1"),FOLLOWUP_APPROVAL("1"),MULTI_SUBSCRIBER_STATUS_CHANGE("1"),CREDIT_CHECK_CHANGE("1");
	
	private String versionNum;

	private KafkaEventVersion(String versionNum) {
		this.versionNum = versionNum;
	}

	public String getVersion() {
		return versionNum;
	}

}
