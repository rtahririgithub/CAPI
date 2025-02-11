/**
 * 
 */
package com.telus.eas.subscriber.info;

import com.telus.api.account.LightWeightSubscriber;
import com.telus.api.account.Subscriber;
import com.telus.api.reference.NetworkType;
import com.telus.eas.framework.info.Info;

public class LightWeightSubscriberInfo extends Info implements LightWeightSubscriber {
	
	private static final long serialVersionUID = 1L;
	
	private int banId;
	private String subscriberId;
	private char status;
	private String phoneNumber;
	private String firstName="";
	private String lastName;
	
	private String productType;
	private String networkType;
	private long subscriptionId;
	private String seatGroup;
	private String seatType;

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
	
	public String getNetworkType() {
		return networkType;
	}
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}
	public String getSubscriberId() {
		return subscriberId;
	}
	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public int getBanId() {
		return banId;
	}
	public void setBanId(int banId) {
		this.banId = banId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName( String firstName ) {
		this.firstName = toUpperCase(firstName);
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = toUpperCase(lastName);
	}
	public boolean hasCDMAEquipment() {
		return isPCSProductType() && NetworkType.NETWORK_TYPE_CDMA.equalsIgnoreCase(networkType);
	}
	public boolean hasHSPAEquipment() {
		return isPCSProductType() &&  NetworkType.NETWORK_TYPE_HSPA.equalsIgnoreCase(networkType);
	}
	public boolean isPCSProductType() {
		return Subscriber.PRODUCT_TYPE_PCS.equalsIgnoreCase(productType);
	}
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("LightWeightSubscriberInfo:[\n");
		s.append("    banId=[").append(banId).append("]\n");	
		s.append("    subscriberId=[").append(subscriberId).append("]\n");
		s.append("    status=[").append(status).append("]\n");		
		s.append("    phoneNumber=[").append(phoneNumber).append("]\n");		
		s.append("    firstName=[").append(firstName).append("]\n");
		s.append("    lastName=[").append(lastName).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");
		s.append("    networkType=[").append(networkType).append("]\n");
		s.append("    subscriptionId=[").append(subscriptionId).append("]\n");
		s.append("    seatGroup=[").append(seatGroup).append("]\n");
		s.append("    seatType=[").append(seatType).append("]\n");
				
		s.append("]");

		return s.toString();
	}	
}