package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class BillNotificationUserIdInfo extends Info {

	static final long serialVersionUID = 1L;
	
	private String uuid;
	private String portalUserId;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getPortalUserId() {
		return portalUserId;
	}
	public void setPortalUserId(String portalUserId) {
		this.portalUserId = portalUserId;
	}
	
	public String toString() {
		
		StringBuffer buf = new StringBuffer();
		buf.append("[BillNotificationUserIdInfo");
		buf.append(" | uuid: " + uuid);
		buf.append(" | portalUserId: " + portalUserId);
		buf.append("]");
		
		return buf.toString();
	}
}
