package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.Call;
import com.telus.api.account.CallSummary;
import com.telus.eas.framework.info.Info;

/**
 * Title:        CallSummaryInfo<p>
 * Description:  The CallSummaryInfo class holds the summary attributes for a particular billed call for a subscriber.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class CallSummaryInfo extends Info implements CallSummary {


	static final long serialVersionUID = 1L;
	
	private Date date;	
	private String switchId;
	private String productType;	
	private String locationDescription;	
	private String locationProvince;
	private String locationCity;
	private String callToCity;	
	private String callToState;
	private String callToNumber;
	private String billPresentationNumber;
	private double callDuration;
	private double airtimeChargeAmount;
	private double tollChargeAmount;
	private double additionalChargeAmount;
	private double taxAmount;
	private double creditedAmount;
	private String periodLevel;
	private double roamingTaxTollAmount;
	private double roamingTaxAirtimeAmount;
	private double roamingTaxAdditionalAmount;
	private boolean extendedHomeArea;
	private Boolean lteHspaHandover;
	private CallInfo callDetails;
	private int messageType;
	private String callTypeFeature;
	private String callActionCode;
	
	public CallSummaryInfo() {		
	}
	
	/**
	 * @return Returns the isLteHspaHandover.
	 */
	public Boolean isLteHspaHandover() {
		return lteHspaHandover;
	}
	/**
	 * @param lteHspaHandover The lteHspaHandover to set.
	 */
	public void setLteHspaHandover(Boolean lteHspaHandover) {
		this.lteHspaHandover = lteHspaHandover;
	}
	/**
	 * @return Returns the additionalChargeAmount.
	 */
	public double getAdditionalChargeAmount() {
		return additionalChargeAmount;
	}
	/**
	 * @param additionalChargeAmount The additionalChargeAmount to set.
	 */
	public void setAdditionalChargeAmount(double additionalChargeAmount) {
		this.additionalChargeAmount = additionalChargeAmount;
	}
	
	/**
	 * @return Returns the airtimeChargeAmount.
	 */
	public double getAirtimeChargeAmount() {
		return airtimeChargeAmount;
	}
	/**
	 * @param airtimeChargeAmount The airtimeChargeAmount to set.
	 */
	public void setAirtimeChargeAmount(double airtimeChargeAmount) {
		this.airtimeChargeAmount = airtimeChargeAmount;
	}
	
	/**
	 * @return Returns the callDuration.
	 */
	public double getCallDuration() {
		return callDuration;
	}
	/**
	 * @param callDuration The callDuration to set.
	 */
	public void setCallDuration(double callDuration) {
		this.callDuration = callDuration;
	}
	
	/**
	 * @return Returns the callToCity.
	 */
	public String getCallToCity() {
		return callToCity;
	}
	/**
	 * @param callToCity The callToCity to set.
	 */
	public void setCallToCity(String callToCity) {
		this.callToCity = callToCity;
	}
	
	/**
	 * @return Returns the callToNumber.
	 */
	public String getCallToNumber() {
		return callToNumber;
	}
	/**
	 * @param callToNumber The callToNumber to set.
	 */
	public void setCallToNumber(String callToNumber) {
		this.callToNumber = callToNumber;
	}
	
	/**
	 * @return Returns the callToState.
	 */
	public String getCallToState() {
		return callToState;
	}
	/**
	 * @param callToState The callToState to set.
	 */
	public void setCallToState(String callToState) {
		this.callToState = callToState;
	}
	
	/**
	 * @return Returns the creditedAmount.
	 */
	public double getCreditedAmount() {
		return creditedAmount;
	}
	/**
	 * @param creditedAmount The creditedAmount to set.
	 */
	public void setCreditedAmount(double creditedAmount) {
		this.creditedAmount = creditedAmount;
	}
	
	/**
	 * @return Returns the date.
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @param date The date to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return Returns the isExtendedHomeArea.
	 */
	public boolean isExtendedHomeArea() {
		return extendedHomeArea;
	}
	/**
	 * @param extendedHomeArea The isExtendedHomeArea to set.
	 */
	public void setExtendedHomeArea(boolean extendedHomeArea) {
		this.extendedHomeArea = extendedHomeArea;
	}
	
	/**
	 * @return Returns the locationCity.
	 */
	public String getLocationCity() {
		return locationCity;
	}
	/**
	 * @param locationCity The locationCity to set.
	 */
	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}
	
	/**
	 * @return Returns the locationDescription.
	 */
	public String getLocationDescription() {
		return locationDescription;
	}
	/**
	 * @param locationDescription The locationDescription to set.
	 */
	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}
	
	/**
	 * @return Returns the locationProvince.
	 */
	public String getLocationProvince() {
		return locationProvince;
	}
	/**
	 * @param locationProvince The locationProvince to set.
	 */
	public void setLocationProvince(String locationProvince) {
		this.locationProvince = locationProvince;
	}
	
	/**
	 * @return Returns the periodLevel.
	 */
	public String getPeriodLevel() {
		return periodLevel;
	}
	/**
	 * @param periodLevel The periodLevel to set.
	 */
	public void setPeriodLevel(String periodLevel) {
		this.periodLevel = periodLevel;
	}
	
	/**
	 * @return Returns the productType.
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType The productType to set.
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	/**
	 * @return Returns the roamingTaxAdditionalAmount.
	 */
	public double getRoamingTaxAdditionalAmount() {
		return roamingTaxAdditionalAmount;
	}
	/**
	 * @param roamingTaxAdditionalAmount The roamingTaxAdditionalAmount to set.
	 */
	public void setRoamingTaxAdditionalAmount(double roamingTaxAdditionalAmount) {
		this.roamingTaxAdditionalAmount = roamingTaxAdditionalAmount;
	}
	
	/**
	 * @return Returns the roamingTaxAirtimeAmount.
	 */
	public double getRoamingTaxAirtimeAmount() {
		return roamingTaxAirtimeAmount;
	}
	/**
	 * @param roamingTaxAirtimeAmount The roamingTaxAirtimeAmount to set.
	 */
	public void setRoamingTaxAirtimeAmount(double roamingTaxAirtimeAmount) {
		this.roamingTaxAirtimeAmount = roamingTaxAirtimeAmount;
	}
	
	/**
	 * @return Returns the roamingTaxTollAmount.
	 */
	public double getRoamingTaxTollAmount() {
		return roamingTaxTollAmount;
	}
	/**
	 * @param roamingTaxTollAmount The roamingTaxTollAmount to set.
	 */
	public void setRoamingTaxTollAmount(double roamingTaxTollAmount) {
		this.roamingTaxTollAmount = roamingTaxTollAmount;
	}
	
	/**
	 * @return Returns the switchId.
	 */
	public String getSwitchId() {
		return switchId;
	}
	/**
	 * @param switchId The switchId to set.
	 */
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	
	/**
	 * @return Returns the taxAmount.
	 */
	public double getTaxAmount() {
		return taxAmount;
	}
	/**
	 * @param taxAmount The taxAmount to set.
	 */
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	
	/**
	 * @return Returns the tollChargeAmount.
	 */
	public double getTollChargeAmount() {
		return tollChargeAmount;
	}
	/**
	 * @param tollChargeAmount The tollChargeAmount to set.
	 */
	public void setTollChargeAmount(double tollChargeAmount) {
		this.tollChargeAmount = tollChargeAmount;
	}

	/**
	 * @return Returns the callDetails.
	 */
	public Call getCallDetails() {
		return callDetails;
	}	
	/**
	 * @param callDetails The callDetails to set.
	 */
	public void setCallDetails(CallInfo callDetails) {
		this.callDetails = callDetails;
	}	
	
	/**
	 * @return Returns the billPresentationNumber.
	 */
	public String getBillPresentationNumber() {
		return billPresentationNumber;
	}
	
	/**
	 * @param billPresentationNumber The billPresentationNumber to set.
	 */
	public void setBillPresentationNumber(String billPresentationNumber) {
		this.billPresentationNumber = billPresentationNumber;
	}
	
	/**
	 * NO-OP method.
	 */
	public void adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException{
	    throw new UnsupportedOperationException("Method not implemented here.");
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("CallSummaryInfo:[\n");
		s.append("callDetails=[").append(callDetails).append("]\n");
		s.append("    date=[").append(date).append("]\n");
		s.append("    switchId=[").append(switchId).append("]\n");
		s.append("    productType=[").append(productType).append("]\n");
		s.append("    locationDescription=[").append(locationDescription).append("]\n");
		s.append("    locationProvince=[").append(locationProvince).append("]\n");
		s.append("    locationCity=[").append(locationCity).append("]\n");
		s.append("    callToCity=[").append(callToCity).append("]\n");
		s.append("    callToState=[").append(callToState).append("]\n");
		s.append("    callToNumber=[").append(callToNumber).append("]\n");
		s.append("    callDuration=[").append(callDuration).append("]\n");
		s.append("    airtimeChargeAmount=[").append(airtimeChargeAmount).append("]\n");		
		s.append("    tollChargeAmount=[").append(tollChargeAmount).append("]\n");
		s.append("    additionalChargeAmount=[").append(additionalChargeAmount).append("]\n");
		s.append("    taxAmount=[").append(taxAmount).append("]\n");
		s.append("    creditedAmount=[").append(creditedAmount).append("]\n");
		s.append("    periodLevel=[").append(periodLevel).append("]\n");
		s.append("    roamingTaxTollAmount=[").append(roamingTaxTollAmount).append("]\n");
		s.append("    roamingTaxAirtimeAmount=[").append(roamingTaxAirtimeAmount).append("]\n");
		s.append("    roamingTaxAdditionalAmount=[").append(roamingTaxAdditionalAmount).append("]\n");
		s.append("    extendedHomeArea=[").append(extendedHomeArea).append("]\n");
		s.append("    lteHspaHandover=[").append(lteHspaHandover).append("]\n");
		s.append("    billPresentationNumber=[").append(billPresentationNumber).append("]\n");
		s.append("    callActionCode=[").append(callActionCode).append("]\n");
		s.append("    callTypeFeature=[").append(callTypeFeature).append("]\n");
		s.append("    messageType=[").append(messageType).append("]\n");
		s.append("]");

		return s.toString();
	}

	public String getCallActionCode() {
		return callActionCode;
	}

	public String getCallTypeFeature() {
		return callTypeFeature;
	}

	public int getMessageType() {
		
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public void setCallTypeFeature(String callTypeFeature) {
		this.callTypeFeature = callTypeFeature;
	}

	public void setCallActionCode(String callActionCode) {
		this.callActionCode = callActionCode;
	}
}
