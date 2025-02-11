package com.telus.eas.account.info;

import com.telus.api.account.ContactDetail;
import com.telus.eas.framework.info.Info;

public class ContactDetailInfo extends Info implements ContactDetail {

	private static final long serialVersionUID = 1L;
	private String emailAddress;
	private String contactPhone;
	private String contactFax;
	private String contactPhoneExt;
	private String homePhone;
	private String otherPhone;
	private String otherPhoneType;
	private String otherPhoneExt;
	private String workPhone;
	private String workPhoneExt;

	public ContactDetailInfo () {
		
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		 this.emailAddress = emailAddress;
		
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String tn) {
		 this.contactPhone = tn;
		
	}

	public String getContactPhoneExtension() {
		return contactPhoneExt;
	}

	public void setContactPhoneExtension(String ext) {
		this.contactPhoneExt = ext;
	}

	public String getContactFax() {
		return contactFax;
	}

	public void setContactFax(String fax) {
		this.contactFax = fax;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String tn) {
		this.homePhone = tn;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String tn) {
		this.workPhone = tn;
	}

	public String getWorkPhoneExtension() {
		return workPhoneExt;
	}

	public void setWorkPhoneExtension(String ext) {
		this.workPhoneExt = ext;
		
	}

	public String getOtherPhoneType() {
		return otherPhoneType;
	}

	public void setOtherPhoneType(String phoneType) {
		this.otherPhoneType = phoneType;
		
	}

	public String getOtherPhone() {
		return otherPhone;
	}

	public void setOtherPhone(String tn) {
		this.otherPhone = tn;
		
	}

	public String getOtherPhoneExt() {
		return otherPhoneExt;
	}

	public void setOtherPhoneExt(String ext) {
		this.otherPhoneExt = ext;
		
	}

	public String toString() {
		StringBuffer s = new StringBuffer(128);
		s.append("ContactDetailInfo:[\n");
		s.append ("   emailAddress=" + emailAddress + "\n");
		s.append ("   contactPhone=" + contactPhone + "\n");
		s.append ("   contactFax=" + contactFax + "\n");
		s.append ("   contactPhoneExt=" + contactPhoneExt + "\n");
		s.append ("   homePhone=" + homePhone + "\n");
		s.append ("   otherPhone=" + otherPhone + "\n");
		s.append ("   otherPhoneType=" + otherPhoneType + "\n");
		s.append ("   otherPhoneExt=" + otherPhoneExt + "\n");
		s.append ("   workPhone=" + workPhone + "\n");
		s.append ("   workPhoneExt=" + workPhoneExt + "\n");
		s.append("]");
		
		return s.toString();
	}

	
}
