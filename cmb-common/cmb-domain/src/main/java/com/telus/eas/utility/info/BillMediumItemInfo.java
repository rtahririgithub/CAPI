package com.telus.eas.utility.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class BillMediumItemInfo extends Info {
	
	static final long serialVersionUID = 1L;
	
	private String billingMethod;
	private String suppressionLevel;
	private Date effectiveStartDate;
	private Date effectiveEndDate;
	private boolean mostRecentInd;
	private String callingApp;
	
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public String getSuppressionLevel() {
		return suppressionLevel;
	}
	public void setSuppressionLevel(String suppressionLevel) {
		this.suppressionLevel = suppressionLevel;
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
	public boolean isMostRecentInd() {
		return mostRecentInd;
	}
	public void setMostRecentInd(boolean mostRecentInd) {
		this.mostRecentInd = mostRecentInd;
	}
	public String getCallingApp() {
		return callingApp;
	}
	public void setCallingApp(String callingApp) {
		this.callingApp = callingApp;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		
		buf.append("[BillMediumItemInfo");
		buf.append(" | billingMethod: " + billingMethod);
		buf.append(" | suppressionLevel: " + suppressionLevel);
		buf.append(" | effectiveStartDate: " + effectiveStartDate);
		buf.append(" | effectiveEndDate: " + effectiveEndDate);
		buf.append(" | mostRecentInd: " + mostRecentInd);
		buf.append(" | callingApp: " + callingApp);
		buf.append("]");
		
		return buf.toString();
	}

}
