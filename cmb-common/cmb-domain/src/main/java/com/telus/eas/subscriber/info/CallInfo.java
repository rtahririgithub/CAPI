package com.telus.eas.subscriber.info;

import com.telus.api.account.Call;
import com.telus.eas.framework.info.Info;

/**
 * Title:        CallInfo<p>
 * Description:  The CallInfo class holds the details for a particular billed call for a subscriber.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class CallInfo extends Info implements Call {

	static final long serialVersionUID = 1L;
	
	private String serialNumber;	
	private String origCellTrunkId;	
	private String termCellTrunkId;	
	private String terminationCode;
	private String airtimeServiceCode;
	private String airtimeFeatureCode;	
	private String tollServiceCode;	
	private String tollFeatureCode;	
	private String additionalChargeServiceCode;
	private String[] featureCodes;
	private String origRouteDescription;
	private String termRouteDescription;
	private double gstAmount;    
	private double pstAmount;
	private double hstAmount;    
	private double taxableGSTAmount;    
	private double taxablePSTAmount;    
	private double taxableHSTAmount;
	private double adjustmentAmount;    
	private double adjustmentGSTAmount;    
	private double adjustmentPSTAmount;    
	private double adjustmentHSTAmount;    
	private double adjustmentRoamingTaxAmount;

	public CallInfo() {
	}
		
	/**
	 * @return Returns the additionalChargeServiceCode.
	 */
	public String getAdditionalChargeServiceCode() {
		return additionalChargeServiceCode;
	}
	/**
	 * @param additionalChargeServiceCode The additionalChargeServiceCode to set.
	 */
	public void setAdditionalChargeServiceCode(String additionalChargeServiceCode) {
		this.additionalChargeServiceCode = additionalChargeServiceCode;
	}
	
	/**
	 * @return Returns the airtimeFeatureCode.
	 */
	public String getAirtimeFeatureCode() {
		return airtimeFeatureCode;
	}
	/**
	 * @param airtimeFeatureCode The airtimeFeatureCode to set.
	 */
	public void setAirtimeFeatureCode(String airtimeFeatureCode) {
		this.airtimeFeatureCode = airtimeFeatureCode;
	}
	
	/**
	 * @return Returns the airtimeServiceCode.
	 */
	public String getAirtimeServiceCode() {
		return airtimeServiceCode;
	}
	/**
	 * @param airtimeServiceCode The airtimeServiceCode to set.
	 */
	public void setAirtimeServiceCode(String airtimeServiceCode) {
		this.airtimeServiceCode = airtimeServiceCode;
	}
	
	/**
	 * @return Returns the featureCodes.
	 */
	public String[] getFeatureCodes() {
		return featureCodes;
	}
	/**
	 * @param featureCodes The featureCodes to set.
	 */
	public void setFeatureCodes(String[] featureCodes) {
		this.featureCodes = featureCodes;
	}
	
	/**
	 * @return Returns the origCellTrunkId.
	 */
	public String getOrigCellTrunkId() {
		return origCellTrunkId;
	}
	/**
	 * @param origCellTrunkId The origCellTrunkId to set.
	 */
	public void setOrigCellTrunkId(String origCellTrunkId) {
		this.origCellTrunkId = origCellTrunkId;
	}

	/**
	 * @return Returns the origRouteDescription.
	 */
	public String getOrigRouteDescription() {
		return origRouteDescription;
	}
	/**
	 * @param origRouteDescription The origRouteDescription to set.
	 */
	public void setOrigRouteDescription(String origRouteDescription) {
		this.origRouteDescription = origRouteDescription;
	}
	
	/**
	 * @return Returns the serialNumber.
	 */
	public String getSerialNumber() {
		return serialNumber;
	}
	/**
	 * @param serialNumber The serialNumber to set.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	/**
	 * @return Returns the termCellTrunkId.
	 */
	public String getTermCellTrunkId() {
		return termCellTrunkId;
	}
	/**
	 * @param termCellTrunkId The termCellTrunkId to set.
	 */
	public void setTermCellTrunkId(String termCellTrunkId) {
		this.termCellTrunkId = termCellTrunkId;
	}
	
	/**
	 * @return Returns the termRouteDescription.
	 */
	public String getTermRouteDescription() {
		return termRouteDescription;
	}
	/**
	 * @param termRouteDescription The termRouteDescription to set.
	 */
	public void setTermRouteDescription(String termRouteDescription) {
		this.termRouteDescription = termRouteDescription;
	}
	
	/**
	 * @return Returns the terminationCode.
	 */
	public String getTerminationCode() {
		return terminationCode;
	}
	/**
	 * @param terminationCode The terminationCode to set.
	 */
	public void setTerminationCode(String terminationCode) {
		this.terminationCode = terminationCode;
	}
	
	/**
	 * @return Returns the tollFeatureCode.
	 */
	public String getTollFeatureCode() {
		return tollFeatureCode;
	}
	/**
	 * @param tollFeatureCode The tollFeatureCode to set.
	 */
	public void setTollFeatureCode(String tollFeatureCode) {
		this.tollFeatureCode = tollFeatureCode;
	}
	
	/**
	 * @return Returns the tollServiceCode.
	 */
	public String getTollServiceCode() {
		return tollServiceCode;
	}
	/**
	 * @param tollServiceCode The tollServiceCode to set.
	 */
	public void setTollServiceCode(String tollServiceCode) {
		this.tollServiceCode = tollServiceCode;
	}	
	
	/**
	 * @return Returns the gstAmount.
	 */
	public double getGSTAmount() {
		return gstAmount;
	}
	/**
	 * @param gstAmount The gstAmount to set.
	 */
	public void setGSTAmount(double gstAmount) {
		this.gstAmount = gstAmount;
	}
	
	/**
	 * @return Returns the hstAmount.
	 */
	public double getHSTAmount() {
		return hstAmount;
	}
	/**
	 * @param hstAmount The hstAmount to set.
	 */
	public void setHSTAmount(double hstAmount) {
		this.hstAmount = hstAmount;
	}
	
	/**
	 * @return Returns the pstAmount.
	 */
	public double getPSTAmount() {
		return pstAmount;
	}
	/**
	 * @param pstAmount The pstAmount to set.
	 */
	public void setPSTAmount(double pstAmount) {
		this.pstAmount = pstAmount;
	}
	
	/**
	 * @return Returns the taxableGSTAmount.
	 */
	public double getTaxableGSTAmount() {
		return taxableGSTAmount;
	}
	/**
	 * @param taxableGSTAmount The taxableGSTAmount to set.
	 */
	public void setTaxableGSTAmount(double taxableGSTAmount) {
		this.taxableGSTAmount = taxableGSTAmount;
	}
	
	/**
	 * @return Returns the taxableHSTAmount.
	 */
	public double getTaxableHSTAmount() {
		return taxableHSTAmount;
	}
	/**
	 * @param taxableHSTAmount The taxableHSTAmount to set.
	 */
	public void setTaxableHSTAmount(double taxableHSTAmount) {
		this.taxableHSTAmount = taxableHSTAmount;
	}
	
	/**
	 * @return Returns the taxablePSTAmount.
	 */
	public double getTaxablePSTAmount() {
		return taxablePSTAmount;
	}
	/**
	 * @param taxablePSTAmount The taxablePSTAmount to set.
	 */
	public void setTaxablePSTAmount(double taxablePSTAmount) {
		this.taxablePSTAmount = taxablePSTAmount;
	}	
	
	/**
	 * @return Returns the adjustmentAmount.
	 */
	public double getAdjustmentAmount() {
		return adjustmentAmount;
	}
	/**
	 * @param adjustmentAmount The adjustmentAmount to set.
	 */
	public void setAdjustmentAmount(double adjustmentAmount) {
		this.adjustmentAmount = adjustmentAmount;
	}
	
	/**
	 * @return Returns the adjustmentGSTAmount.
	 */
	public double getAdjustmentGSTAmount() {
		return adjustmentGSTAmount;
	}
	/**
	 * @param adjustmentGSTAmount The adjustmentGSTAmount to set.
	 */
	public void setAdjustmentGSTAmount(double adjustmentGSTAmount) {
		this.adjustmentGSTAmount = adjustmentGSTAmount;
	}
	
	/**
	 * @return Returns the adjustmentHSTAmount.
	 */
	public double getAdjustmentHSTAmount() {
		return adjustmentHSTAmount;
	}
	/**
	 * @param adjustmentHSTAmount The adjustmentHSTAmount to set.
	 */
	public void setAdjustmentHSTAmount(double adjustmentHSTAmount) {
		this.adjustmentHSTAmount = adjustmentHSTAmount;
	}
	
	/**
	 * @return Returns the adjustmentPSTAmount.
	 */
	public double getAdjustmentPSTAmount() {
		return adjustmentPSTAmount;
	}
	/**
	 * @param adjustmentPSTAmount The adjustmentPSTAmount to set.
	 */
	public void setAdjustmentPSTAmount(double adjustmentPSTAmount) {
		this.adjustmentPSTAmount = adjustmentPSTAmount;
	}
	
	/**
	 * @return Returns the adjustmentRoamingTaxAmount.
	 */
	public double getAdjustmentRoamingTaxAmount() {
		return adjustmentRoamingTaxAmount;
	}
	/**
	 * @param adjustmentRoamingTaxAmount The adjustmentRoamingTaxAmount to set.
	 */
	public void setAdjustmentRoamingTaxAmount(double adjustmentRoamingTaxAmount) {
		this.adjustmentRoamingTaxAmount = adjustmentRoamingTaxAmount;
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("CallInfo:[\n");
		s.append("    serialNumber=[").append(serialNumber).append("]\n");
		s.append("    origCellTrunkId=[").append(origCellTrunkId).append("]\n");
		s.append("    termCellTrunkId=[").append(termCellTrunkId).append("]\n");
		s.append("    terminationCode=[").append(terminationCode).append("]\n");
		s.append("    airtimeServiceCode=[").append(airtimeServiceCode).append("]\n");
		s.append("    airtimeFeatureCode=[").append(airtimeFeatureCode).append("]\n");
		s.append("    tollServiceCode=[").append(tollServiceCode).append("]\n");
		s.append("    tollFeatureCode=[").append(tollFeatureCode).append("]\n");
		s.append("    additionalChargeServiceCode=[").append(additionalChargeServiceCode).append("]\n");
		for (int i=0; i < featureCodes.length; i++) {
			s.append("    featureCodes[").append(i).append("]=[").append(featureCodes[i]).append("]\n");
		}
		s.append("    origRouteDescription=[").append(origRouteDescription).append("]\n");
		s.append("    termRouteDescription=[").append(termRouteDescription).append("]\n");
		s.append("    gstAmount=[").append(gstAmount).append("]\n");
		s.append("    hstAmount=[").append(hstAmount).append("]\n");
		s.append("    pstAmount=[").append(pstAmount).append("]\n");
		s.append("    taxableGSTAmount=[").append(taxableGSTAmount).append("]\n");
		s.append("    taxableHSTAmount=[").append(taxableHSTAmount).append("]\n");
		s.append("    taxablePSTAmount=[").append(taxablePSTAmount).append("]\n");
		s.append("    adjustmentAmount=[").append(adjustmentAmount).append("]\n");
		s.append("    adjustmentGSTAmount=[").append(adjustmentGSTAmount).append("]\n");
		s.append("    adjustmentHSTAmount=[").append(adjustmentHSTAmount).append("]\n");
		s.append("    adjustmentPSTAmount=[").append(adjustmentPSTAmount).append("]\n");
		s.append("    adjustmentRoamingTaxAmount=[").append(adjustmentRoamingTaxAmount).append("]\n");
		s.append("]");

		return s.toString();
	}

}
