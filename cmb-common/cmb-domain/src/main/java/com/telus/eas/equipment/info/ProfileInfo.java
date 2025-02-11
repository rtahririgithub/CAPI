package com.telus.eas.equipment.info;

import com.telus.eas.framework.info.Info;

public class ProfileInfo extends Info {

	static final long serialVersionUID = 1L;

	private String localIMSI;
	private String remoteIMSI;
	
	public String getLocalIMSI() {
		return localIMSI;
	}

	public void setLocalIMSI(String localIMSI) {
		this.localIMSI = localIMSI;
	}

	public String getRemoteIMSI() {
		return remoteIMSI;
	}

	public void setRemoteIMSI(String remoteIMSI) {
		this.remoteIMSI = remoteIMSI;
	}
	
	//defect 6737 fix: Oct 31,2012 mliao
	//localIMSI is unique for each USIM card, so comparing only localIMSI is sufficient to identify  
	//if two USIM card is same or not
	public boolean isSame(ProfileInfo info) {
		
		boolean result =false;
		if (this==info) { 
			result = true;
		} else if (info!=null && localIMSI.equals(info.localIMSI) ) {
			result =true;
		}
		return result;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("ProfileInfo[\n");
		buffer.append("localIMSI = ").append(localIMSI).append("\n");
		buffer.append("remoteIMSI = ").append(remoteIMSI).append("\n");
		buffer.append("]\n");
		return buffer.toString();
	}

}
