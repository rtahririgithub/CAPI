package com.telus.eas.subscriber.info;

import java.io.Serializable;
import java.util.Date;

import com.telus.api.account.CallingFeatureCycle;

public class CallingFeatureCycleInfo implements	CallingFeatureCycle, Serializable {

	private static final long serialVersionUID = 1L;
	private Date startDate;
	private Date endDate;
	private Date lastUpdateDate;
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
