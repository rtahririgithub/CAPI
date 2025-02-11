package com.telus.provider.contactevent;

import com.telus.api.contactevent.SMSNotification;
import com.telus.eas.contactevent.info.SMSNotificationInfo;
import com.telus.provider.TMProvider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class TMSMSNotification extends TMNotification implements SMSNotification {
	
	private SMSNotificationInfo delegate;
	
	public TMSMSNotification(TMProvider provider, SMSNotificationInfo delegate) {
		super(provider, delegate);
		this.delegate = delegate;
	}
	
	public SMSNotificationInfo getDelegate0() {
		return delegate;
	}
	
	public String getPhoneNumber() {
		return delegate.getPhoneNumber();
	}
	public void setPhoneNumber(String phoneNumber) {
		delegate.setPhoneNumber(phoneNumber);
	}
	public String getEquipmentSerialNumber() {
		return delegate.getEquipmentSerialNumber();
	}
	public void setEquipmentSerialNumber(String equipmentSerialNumber) {
		delegate.setEquipmentSerialNumber(equipmentSerialNumber);
	}
	
	public int getTeamMemberId() {
		return delegate.getTeamMemberId();
	}
	public void setTeamMemberId(int teamMemberId) {
		delegate.setTeamMemberId(teamMemberId);
	}
}
