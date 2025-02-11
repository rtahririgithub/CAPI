package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

public class ContactPropertyInfo extends Info {

	private static final long serialVersionUID = 1L;

	private ConsumerNameInfo name;
	private String homePhoneNumber;
	private String businessPhoneNumber;
	private String businessPhoneExtension;
	private String contactPhoneNumber;
	private String contactPhoneExtension;
	private String contactFax;
	private String otherPhoneType;
	private String otherPhoneNumber;
	private String otherPhoneExtension;
	
	public ConsumerNameInfo getName() {
		return name;
	}
	public void setName(ConsumerNameInfo name) {
		this.name = name;
	}
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}
	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}
	public String getBusinessPhoneNumber() {
		return businessPhoneNumber;
	}
	public void setBusinessPhoneNumber(String businessPhoneNumber) {
		this.businessPhoneNumber = businessPhoneNumber;
	}
	public String getBusinessPhoneExtension() {
		return businessPhoneExtension;
	}
	public void setBusinessPhoneExtension(String businessPhoneExtension) {
		this.businessPhoneExtension = businessPhoneExtension;
	}
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}
	public String getContactPhoneExtension() {
		return contactPhoneExtension;
	}
	public void setContactPhoneExtension(String contactPhoneExtension) {
		this.contactPhoneExtension = contactPhoneExtension;
	}
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	public String getOtherPhoneType() {
		return otherPhoneType;
	}
	public void setOtherPhoneType(String otherPhoneType) {
		this.otherPhoneType = otherPhoneType;
	}
	public String getOtherPhoneNumber() {
		return otherPhoneNumber;
	}
	public void setOtherPhoneNumber(String otherPhoneNumber) {
		this.otherPhoneNumber = otherPhoneNumber;
	}
	public String getOtherPhoneExtension() {
		return otherPhoneExtension;
	}
	public void setOtherPhoneExtension(String otherPhoneExtension) {
		this.otherPhoneExtension = otherPhoneExtension;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("ContactPropertyInfo:[\n");
		s.append ("   name=" + name + "\n");
		s.append ("   homePhoneNumber=" + homePhoneNumber + "\n");
		s.append ("   businessPhoneNumber=" + businessPhoneNumber + "\n");
		s.append ("   businessPhoneExtension=" + businessPhoneExtension + "\n");
		s.append ("   contactPhoneNumber=" + contactPhoneNumber + "\n");
		s.append ("   contactPhoneExtension=" + contactPhoneExtension + "\n");
		s.append ("   contactFax=" + contactFax + "\n");
		s.append ("   otherPhoneType=" + otherPhoneType + "\n");
		s.append ("   otherPhoneNumber=" + otherPhoneNumber + "\n");
		s.append ("   otherPhoneExtension=" + otherPhoneExtension + "\n");
		s.append("]");
		
		return s.toString();
	}
}
