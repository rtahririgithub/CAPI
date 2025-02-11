package com.telus.eas.contactevent.info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RoamingServiceInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String serviceCode;
	private String serviceCodeDescriptionEnglish;
	private String serviceCodeDescriptionFrench;
	private List<String> socFamilyTypes;
	private String coverageType;
	private String rlhInd;
	private String ppacRate;
	private String pprcRate;
	private String serviceType;
	private String duration;
	private String durationInd;
	private String BillCycleTreatmentCode;
	private Date transactionDate;
	private Date socEffDate;
	private Date socExpDate;
	private List<String> socGroups;
	
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceCodeDescriptionEnglish() {
		return serviceCodeDescriptionEnglish;
	}
	public void setServiceCodeDescriptionEnglish(
			String serviceCodeDescriptionEnglish) {
		this.serviceCodeDescriptionEnglish = serviceCodeDescriptionEnglish;
	}
	public String getServiceCodeDescriptionFrench() {
		return serviceCodeDescriptionFrench;
	}
	public void setServiceCodeDescriptionFrench(String serviceCodeDescriptionFrench) {
		this.serviceCodeDescriptionFrench = serviceCodeDescriptionFrench;
	}
	public List<String> getSocFamilyTypes() {
		return socFamilyTypes;
	}
	public void setSocFamilyTypes(List<String> socFamilyTypes) {
		this.socFamilyTypes = socFamilyTypes;
	}
	public String getCoverageType() {
		return coverageType;
	}
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	public String getPpacRate() {
		return ppacRate;
	}
	public void setPpacRate(String ppacRate) {
		this.ppacRate = ppacRate;
	}
	public String getPprcRate() {
		return pprcRate;
	}
	public void setPprcRate(String pprcRate) {
		this.pprcRate = pprcRate;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getRlhInd() {
		return rlhInd;
	}
	public void setRlhInd(String rlhInd) {
		this.rlhInd = rlhInd;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getDurationInd() {
		return durationInd;
	}
	public void setDurationInd(String durationInd) {
		this.durationInd = durationInd;
	}
	public String getBillCycleTreatmentCode() {
		return BillCycleTreatmentCode;
	}
	public void setBillCycleTreatmentCode(String billCycleTreatmentCode) {
		BillCycleTreatmentCode = billCycleTreatmentCode;
	}
	
	public Date getSocEffDate() {
		return socEffDate;
	}
	public void setSocEffDate(Date socEffDate) {
		this.socEffDate = socEffDate;
	}
	public Date getSocExpDate() {
		return socExpDate;
	}
	public void setSocExpDate(Date socExpDate) {
		this.socExpDate = socExpDate;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	public List<String> getSocGroups() {
		return socGroups;
	}
	public void setSocGroups(List<String> socGroups) {
		this.socGroups = socGroups;
	}
	
	@Override
	public String toString() {
		return "RoamingServiceInfo [serviceCode=" + serviceCode
				+ ", serviceCodeDescriptionEnglish="
				+ serviceCodeDescriptionEnglish
				+ ", serviceCodeDescriptionFrench="
				+ serviceCodeDescriptionFrench + ", socFamilyTypes="
				+ socFamilyTypes + ", coverageType=" + coverageType
				+ ", rlhInd=" + rlhInd + ", ppacRate=" + ppacRate
				+ ", pprcRate=" + pprcRate + ", serviceType=" + serviceType
				+ ", duration=" + duration + ", durationInd=" + durationInd
				+ ", BillCycleTreatmentCode=" + BillCycleTreatmentCode
				+ ", transactionDate=" + transactionDate + ", socEffDate="
				+ socEffDate + ", socExpDate=" + socExpDate + ", socGroups="
				+ socGroups + "]";
	}
}
