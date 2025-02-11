package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

public class SapccOfferInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	public static final String ZONE_INTERNATIONAL = "INTERNATIONAL";
	public static final String ZONE_DOMESTIC = "DOMESTIC";

	private String offerId;
	private String offerName;
	private String recurrenceType;
	private String zone;

	public String getOfferId() {
		return offerId;
	}

	public void setOfferId(String offerId) {
		this.offerId = offerId;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public String getRecurrenceType() {
		return recurrenceType;
	}

	public void setRecurrenceType(String recurrenceType) {
		this.recurrenceType = recurrenceType;
	}
	
	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("SapccOfferInfo [offerId=").append(offerId);
		buffer.append(", offerName=").append(offerName);
		buffer.append(", recurrenceType=").append(recurrenceType);
		buffer.append(", zone=").append(zone).append("]");
		
		return buffer.toString();
	}

}