package com.telus.eas.utility.info;


public class SapccMappedServiceInfo extends ReferenceInfo {

	private static final long serialVersionUID = 1L;
	
	private String offerId;

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SapccMappedServiceInfo [offerId=").append(offerId).append("]");		
		return buffer.toString();
	}

}