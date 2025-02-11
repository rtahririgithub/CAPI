package com.telus.eas.utility.info;

import java.util.Date;
import java.util.List;

import com.telus.eas.framework.info.Info;

public class OfferCriteriaInfo extends Info {
	
	static final long serialVersionUID = 1L;
	
	private String systemId;
	private long offerId;	
	private Date perspectiveDate;
	private List<Long> promotionIdList;
	private Boolean mscPaidIndicator;	

	public List<Long> getPromotionIdList() {
		return promotionIdList;
	}


	public void setPromotionIdList(List<Long> promotionIdList) {
		this.promotionIdList = promotionIdList;
	}

	public Boolean isMscPaidIndicator() {
		return mscPaidIndicator;
	}


	public void setMscPaidIndicator(Boolean mscPaidIndicator) {
		this.mscPaidIndicator = mscPaidIndicator;
	}


	public String getSystemId() {
		return systemId;
	}


	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}


	public long getOfferId() {
		return offerId;
	}


	public void setOfferId(long offerId) {
		this.offerId = offerId;
	}


	public Date getPerspectiveDate() {
		return perspectiveDate;
	}


	public void setPerspectiveDate(Date perspectiveDate) {
		this.perspectiveDate = perspectiveDate;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("[OfferCriteriaInfo");
		buf.append(" | systemId: " + systemId);			
		buf.append(" | offerId: " + offerId);
		buf.append(" | perspectiveDate: " + perspectiveDate);
		buf.append(" | promotionIdList: " + promotionIdList);
		buf.append(" | mscPaidIndicator: " + mscPaidIndicator);
		buf.append("]");
		return buf.toString();
	}
}
