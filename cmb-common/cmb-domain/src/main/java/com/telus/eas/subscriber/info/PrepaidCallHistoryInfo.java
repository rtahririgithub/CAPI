package com.telus.eas.subscriber.info;

/**
 * Title:        Telus Domain Project -KB61
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

import com.telus.api.account.PrepaidCallHistory;
import com.telus.api.reference.PrepaidRateProfile;
import com.telus.eas.framework.info.Info;

/**
 * <b>TODO</b>: any modification requested should result in deprecating this class and having Prepaid web services handle the requested changes.  For futher details, 
 * refer to Client API Design.doc design document for Prepaid Real Time Rating project
 */
public class PrepaidCallHistoryInfo extends Info implements PrepaidCallHistory {

	static final long serialVersionUID = 1L;

	public PrepaidCallHistoryInfo() {
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setOrigin_cd(String origin_cd) {
		this.origin_cd = origin_cd;
	}

	public String getOrigin_cd() {
		return origin_cd;
	}

	public void setChargeDuration(int chargeDuration) {
		this.chargeDuration = chargeDuration;
	}

	public int getChargeDuration() {
		return chargeDuration;
	}

	public void setCallingPhoneNumber(String callingPhoneNumber) {
		this.callingPhoneNumber = callingPhoneNumber;
	}

	public String getCallingPhoneNumber() {
		return callingPhoneNumber;
	}

	public void setCalledPhoneNumber(String calledPhoneNumber) {
		this.calledPhoneNumber = calledPhoneNumber;
	}

	public String getCalledPhoneNumber() {
		return calledPhoneNumber;
	}

	public void setChargedAmount(double chargedAmount) {
		this.chargedAmount = chargedAmount;
	}

	public double getChargedAmount() {
		return chargedAmount;
	}

	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}

	public double getStartBalance() {
		return startBalance;
	}

	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}

	public double getEndBalance() {
		return endBalance;
	}

	public void setVoiceMailIndicator(String voiceMailIndicator) {
		this.voiceMailIndicator = voiceMailIndicator;
	}

	public String getVoiceMailIndicator() {
		return voiceMailIndicator;
	}

	public void setLocalLongDistanceIndicator(int localLongDistanceIndicator) {
		this.localLongDistanceIndicator = localLongDistanceIndicator;
	}

	public int getLocalLongDistanceIndicator() {
		return localLongDistanceIndicator;
	}

	public void setInternationalDomesticIndicator(
			int internationalDomesticIndicator) {
		this.internationalDomesticIndicator = internationalDomesticIndicator;
	}

	public int getInternationalDomesticIndicator() {
		return internationalDomesticIndicator;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getRateId() {
		return rateId;
	}

	public void setRateId(String rateId) {
		this.rateId = (rateId == null ? "" : rateId);
	}

	public String getServingSID() {
		return servingSID;
	}

	public void setServingSID(String servingSID) {
		this.servingSID = servingSID;
	}

	public int getReasonTypeId() {
		return reasonTypeId;
	}

	public void setReasonTypeId(int reasonTypeId) {
		this.reasonTypeId = reasonTypeId;
	}

	public String getReasonId() {
		return reasonId;
	}

	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}

	public String getWPSServiceCode() {
		return WPSServiceCode;
	}

	public void setWPSServiceCode(String serviceCode) {
		WPSServiceCode = serviceCode;
	}

	private java.util.Date startDate;
	private java.util.Date endDate;
	private String origin_cd;
	private int chargeDuration;
	private String callingPhoneNumber;
	private String calledPhoneNumber;
	private String voiceMailIndicator;
	private double endBalance;
	private double startBalance;
	private double chargedAmount;
	private int localLongDistanceIndicator;
	private int internationalDomesticIndicator;
	private double rate;
	private String rateId = "";
	private String servingSID;
	private int reasonTypeId;
	private String reasonId;
	private String WPSServiceCode;
	private double longDistanceCallCost; 
	private double localCallCost;
	private double roamingCallCost;
	private String calledMarketCode;
	private String callingMarketCode;
	private String switchId;
	private String routeId;
	private PrepaidRateProfile[] rates;

	/* Commented the Prepaid 5.1 rel changes
	private String[] discountIds;
	private double longDistanceRate;
	*/

	public double getLongDistanceCallCost() {
		return longDistanceCallCost;
	}

	public void setLongDistanceCallCost(double longDistanceCallCost) {
		this.longDistanceCallCost = longDistanceCallCost;
	}

	public double getLocalCallCost() {
		return localCallCost;
	}

	public void setLocalCallCost(double localCallCost) {
		this.localCallCost = localCallCost;
	}
	
	public double getRoamingCallCost() {
		return roamingCallCost;
	}
	
	public void setRoamingCallCost(double roamingCallCost) {
		this.roamingCallCost = roamingCallCost;
	}
	
	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
	public String getSwitchId() {
		return switchId;
	}
	
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}

	public String getCalledMarketCode() {
		return calledMarketCode;
	}
	public void setCalledMarketCode(String calledMarketCode) {
		this.calledMarketCode = calledMarketCode;
	}
	public String getCallingMarketCode() {
		return callingMarketCode;
	}
	public void setCallingMarketCode(String callingMarketCode) {
		this.callingMarketCode = callingMarketCode;
	}
	
	public PrepaidRateProfile[] getRates() {
		return rates;
	}
	public void setRates(PrepaidRateProfile[] rates) {
		this.rates = rates;
	}
	/* Commented the Prepaid 5.1 rel changes
	public String[] getDiscountIds() {
		return discountIds;
	}
	
	public void setDiscountIds(String[] discountIds) {
		this.discountIds = discountIds;
	}
	
	public double getLongDistanceRate() {
		return longDistanceRate;
	}
	
	public void setLongDistanceRate(double longDistanceRate) {
		this.longDistanceRate = longDistanceRate;
	}
	*/
	public String toString() {
		StringBuffer s = new StringBuffer(128);
		s.append("PrepaidCallHistoryInfo:[\n");
		s.append("    startDate=[").append(startDate).append("]\n");
		s.append("    endDate=[").append(endDate).append("]\n");
		s.append("    origin_cd=[").append(origin_cd).append("]\n");
		s.append("    chargeDuration=[").append(chargeDuration).append("]\n");
		s.append("    callingPhoneNumber=[").append(callingPhoneNumber).append("]\n");
		s.append("    calledPhoneNumber=[").append(calledPhoneNumber).append("]\n");
		s.append("    chargedAmount=[").append(chargedAmount).append("]\n");
		s.append("    startBalance=[").append(startBalance).append("]\n");
		s.append("    endBalance=[").append(endBalance).append("]\n");
		s.append("    voiceMailIndicator=[").append(voiceMailIndicator).append("]\n");
		s.append("    localLongDistanceIndicator=[").append(localLongDistanceIndicator).append("]\n");
		s.append("    internationalDomesticIndicator=[").append(internationalDomesticIndicator).append("]\n");
		s.append("    rate=[").append(rate).append("]\n");
		s.append("    rateId=[").append(rateId).append("]\n");
		s.append("    servingSID=[").append(servingSID).append("]\n");
		s.append("    reasonTypeId=[").append(reasonTypeId).append("]\n");
		s.append("    reasonId=[").append(reasonId).append("]\n");
		s.append("    WPSServiceCode=[").append(WPSServiceCode).append("]\n");
		s.append("    longDistanceCallCost=[").append(longDistanceCallCost).append("]\n");
		s.append("    localCallCost=[").append(localCallCost).append("]\n");
		s.append("    roamingCallCost=[").append(roamingCallCost).append("]\n");
		s.append("    switchId=[").append(switchId).append("]\n");
		s.append("    routeId=[").append(routeId).append("]\n");
		s.append("    calledMarketCode=[").append(calledMarketCode).append("]\n");
		s.append("    callingMarketCode=[").append(callingMarketCode).append("]\n");
		/* Commented the Prepaid 5.1 rel changes
		s.append("    discountIds=[").append(discountIds).append("]\n");
		s.append("    longDistanceRate=[").append(longDistanceRate).append("]\n");
		*/
		s.append("]");
		return s.toString();
	}

}
