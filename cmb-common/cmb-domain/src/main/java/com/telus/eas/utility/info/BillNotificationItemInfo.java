package com.telus.eas.utility.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class BillNotificationItemInfo extends Info {
	
	static final long serialVersionUID = 1L;
	
	private String registrationType;
	private String registrationId;
	private BillNotificationContactAddressInfo addressInfo;
	private BillNotificationUserIdInfo userInfo;
	private String portalUserFirstName;
	private String portalUserLastName;
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private String mechType;
	private String status;
	private String ban;
	
	public String getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(String registrationType) {
		this.registrationType = registrationType;
	}
	public String getRegistrationId() {
		return registrationId;
	}
	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}
	public BillNotificationContactAddressInfo getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(BillNotificationContactAddressInfo addressInfo) {
		this.addressInfo = addressInfo;
	}
	public BillNotificationUserIdInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(BillNotificationUserIdInfo userInfo) {
		this.userInfo = userInfo;
	}
	public String getPortalUserFirstName() {
		return portalUserFirstName;
	}
	public void setPortalUserFirstName(String portalUserFirstName) {
		this.portalUserFirstName = portalUserFirstName;
	}
	public String getPortalUserLastName() {
		return portalUserLastName;
	}
	public void setPortalUserLastName(String portalUserLastName) {
		this.portalUserLastName = portalUserLastName;
	}
	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	public String getMechType() {
		return mechType;
	}
	public void setMechType(String mechType) {
		this.mechType = mechType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBan() {
		return ban;
	}
	public void setBan(String ban) {
		this.ban = ban;
	}
	
	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("[BillNotificationItemInfo");
		buf.append(" | registrationType: " + registrationType);
		buf.append(" | registrationId: " + registrationId);
		buf.append(" | addressInfo: " + addressInfo);
		buf.append(" | userInfo: " + userInfo);
		buf.append(" | portalUserFirstName: " + portalUserFirstName);
		buf.append(" | portalUserLastName: " + portalUserLastName);
		buf.append(" | effectiveStartDate: " + effectiveStartDate);
		buf.append(" | effectiveEndDate: " + effectiveEndDate);
		buf.append(" | mechType: " + mechType);
		buf.append(" | status: " + status);
		buf.append(" | ban: " + ban);
		buf.append("]");
		
		return buf.toString();
	}

}
