package com.telus.cmb.subscriber.domain;

import java.io.Serializable;

public class HpaRewardAccountInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String METHOD_TYPE_CLOSE_HPA_ACCOUNT = "CLOSE_HPA";
	
	private String HpaMethodType;
	private int ban;
	private String phoneNumber;
	private long subscriptionId;

	public String getHpaMthodType() {
		return HpaMethodType;
	}

	public void setHpaMthodType(String hpaMthodType) {
		HpaMethodType = hpaMthodType;
	}

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
}