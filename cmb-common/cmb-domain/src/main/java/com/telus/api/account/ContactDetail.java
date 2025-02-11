package com.telus.api.account;

public interface ContactDetail {
	String getEmailAddress();
	void setEmailAddress(String emailAddress);
	
	String getContactPhone();
	void setContactPhone(String tn);
	
	String getContactPhoneExtension();
	void setContactPhoneExtension(String ext);
	
	String getContactFax();
	void setContactFax(String fax);
	
	String getHomePhone();
	void setHomePhone(String tn);
	
	String getWorkPhone();
	void setWorkPhone(String tn);
	
	String getWorkPhoneExtension();
	void setWorkPhoneExtension(String ext);
	
	String getOtherPhoneType();
	void setOtherPhoneType(String type);
	
	String getOtherPhone();
	void setOtherPhone(String tn);
	
	String getOtherPhoneExt();
	void setOtherPhoneExt(String ext);
}
