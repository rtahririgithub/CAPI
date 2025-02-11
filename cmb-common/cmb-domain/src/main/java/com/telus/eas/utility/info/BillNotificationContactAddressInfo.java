package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class BillNotificationContactAddressInfo extends Info {

	static final long serialVersionUID = 1L;
	
	private String registrationReference;
	private String contactPhoneNumber;
	
	public String getRegistrationReference() {
		return registrationReference;
	}
	public void setRegistrationReference(String registrationReference) {
		this.registrationReference = registrationReference;
	}
	public String getContactPhoneNumber() {
		return contactPhoneNumber;
	}
	public void setContactPhoneNumber(String contactPhoneNumber) {
		this.contactPhoneNumber = contactPhoneNumber;
	}
	
	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		buf.append("[BillNotificationContactAddressInfo");
		buf.append(" | registrationReference: " + registrationReference);
		buf.append(" | contactPhoneNumber: " + contactPhoneNumber);
		buf.append("]");

		return buf.toString();
	}
}
