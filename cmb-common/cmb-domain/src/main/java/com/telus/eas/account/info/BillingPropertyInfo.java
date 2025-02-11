package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class BillingPropertyInfo extends Info {

	private static final long serialVersionUID = 1L;

	private String fullName;
	private String legalBusinessName;
	private String tradeNameAttention;
	private ConsumerNameInfo name;
	private AddressInfo address;
	private Date verifiedDate;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLegalBusinessName() {
		return legalBusinessName;
	}
	public void setLegalBusinessName(String legalBusinessName) {
		this.legalBusinessName = legalBusinessName;
	}
	public String getTradeNameAttention() {
		return tradeNameAttention;
	}
	public void setTradeNameAttention(String tradeNameAttention) {
		this.tradeNameAttention = tradeNameAttention;
	}
	public ConsumerNameInfo getName() {
		return name;
	}
	public void setName(ConsumerNameInfo name) {
		this.name = name;
	}
	public AddressInfo getAddress() {
		return address;
	}
	public void setAddress(AddressInfo address) {
		this.address = address;
	}
	public Date getVerifiedDate() {
		return verifiedDate;
	}
	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("BillingPropertyInfo:[\n");
		s.append ("   fullName=" + fullName + "\n");
		s.append ("   legalBusinessName=" + legalBusinessName + "\n");
		s.append ("   tradeNameAttention=" + tradeNameAttention + "\n");
		s.append ("   name=" + name + "\n");
		s.append ("   address=" + address + "\n");
		s.append ("   verifiedDate=" + verifiedDate + "\n");
		s.append("]");
		
		return s.toString();
	}
}
