package com.telus.eas.account.info;

import com.telus.api.fleet.ReservePortInMemberIdentity;

public class ReservePortInMemberIdentityInfo implements ReservePortInMemberIdentity{

	int urbanId;
	int fleetId;
	String memberId;
	
	public int getFleetId() {
		return fleetId;
	}
	
	public void setFleetId(int fleetId) {
		this.fleetId = fleetId;
	}
	
	public String getMemberId() {
		return memberId;
	}
	
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public int getUrbanId() {
		return urbanId;
	}
	
	public void setUrbanId(int urbanId) {
		this.urbanId = urbanId;
	}
	

	
}
