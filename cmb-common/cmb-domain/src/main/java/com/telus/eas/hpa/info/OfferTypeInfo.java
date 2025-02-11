package com.telus.eas.hpa.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class OfferTypeInfo extends Info {

	static final long serialVersionUID = 1L;

	private long offerID;
	private long offerSourceSystemID;
	private Date offerRedemptionDate;

	public long getOfferID() {
		return offerID;
	}

	public void setOfferID(long offerID) {
		this.offerID = offerID;
	}

	public long getOfferSourceSystemID() {
		return offerSourceSystemID;
	}

	public void setOfferSourceSystemID(long offerSourceSystemID) {
		this.offerSourceSystemID = offerSourceSystemID;
	}

	public Date getOfferRedemptionDate() {
		return offerRedemptionDate;
	}

	public void setOfferRedemptionDate(Date offerRedemptionDate) {
		this.offerRedemptionDate = offerRedemptionDate;
	}

	public String toString() {

		StringBuffer s = new StringBuffer();
		s.append("OfferTypeInfo: {\n");
		s.append("    offerID=[").append(getOfferID()).append("]\n");
		s.append("    offerSourceSystemID=[").append(getOfferSourceSystemID()).append("]\n");
		s.append("    offerRedemptionDate=[").append(getOfferRedemptionDate()).append("]\n");
		s.append("}");
		
		return s.toString();
	}

}