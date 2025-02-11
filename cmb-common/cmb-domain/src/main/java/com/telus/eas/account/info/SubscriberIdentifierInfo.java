package com.telus.eas.account.info;

import com.telus.api.account.SubscriberIdentifier;
import com.telus.eas.framework.info.Info;

public class SubscriberIdentifierInfo extends Info implements
		SubscriberIdentifier {
	
	private static final long serialVersionUID = -357000938555109044L;
	private String subscriberId;
	private long subscriptionId;
	private int ban;
	private String phoneNumber;
	private String seatGroup;
	private String seatType;

	private int brandId;

	

	public String getSeatGroup() {
		return seatGroup;
	}

	public void setSeatGroup(String seatGroup) {
		this.seatGroup = seatGroup;
	}

	public String getSeatType() {
		return seatType;
	}

	public void setSeatType(String seatType) {
		this.seatType = seatType;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public long getSubscriptionId() {
		return subscriptionId;
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

	public void setBrandId(int brandId) {
		this.brandId=brandId;
	}

	public int getBrandId() {
		return brandId;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);
		s.append("SubscriberIdentifierInfo [subscriberId=" + subscriberId
				+ ", subscriptionId=" + subscriptionId + ", ban=" + ban
				+ ", phoneNumber=" + phoneNumber + ", brandId=" + brandId);

		if (seatType != null && seatGroup != null) {
			s.append("seatGroup=" + seatGroup + " seatType=" + seatType);
		}
		return s.toString();

	}
	
}
