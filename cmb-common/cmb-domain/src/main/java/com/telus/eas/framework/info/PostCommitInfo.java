package com.telus.eas.framework.info;


public class PostCommitInfo extends Info {
	private static final long serialVersionUID = 1L;

	private String processType;
	private int ban;
	private String subscriberId;
	private String phoneNumber;
	private String externalId;

	
	public PostCommitInfo(String processType, int ban, String subscriberId, String phoneNumber, String externalId) {
		this.processType = processType;
		this.ban = ban;
		this.subscriberId = subscriberId;
		this.phoneNumber = phoneNumber;
		this.externalId = externalId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getProcessType() {
		return processType;
	}

	public int getBan() {
		return ban;
	}

	public String getSubscriberId() {
		return subscriberId;
	}
	
	public String toString() {
		return "PostCommitInfo [processType=" + processType + ", ban=" + ban + ", subscriberId=" + subscriberId + "]";
	}
	
	
}
