package com.telus.eas.subscriber.info;

import java.util.Date;

import com.telus.api.account.PrepaidPromotionDetail;
import com.telus.eas.framework.info.Info;

public class PrepaidPromotionDetailInfo extends Info implements PrepaidPromotionDetail {
	static final long serialVersionUID = 1L;
	
	private double allocatedBucketAmount;
	private double remainingBucketAmount;
	private String bucketMeasureType;
	private double counterCumulativeAmount;
	private double counterQualifyingMonths;
	private String counterType;
	private Date bonusExpiryDate;
	private Date bonusEffectiveDate;
	private Date counterExpiryDate;
	private Date counterEffectiveDate;
	
	public double getAllocatedBucketAmount() {
		return allocatedBucketAmount;
	}
	
	public void setAllocatedBucketAmount(double abm) {
		allocatedBucketAmount = abm;
	}
	
	public double getRemainingBucketAmount() {
		return remainingBucketAmount;
	}
	
	public void setRemainingBucketAmount(double rbm) {
		remainingBucketAmount = rbm;
	}
	
	public String getBucketMeasureType() {
		return bucketMeasureType;
	}
	
	public void setBucketMeasureType(String bmt) {
		bucketMeasureType = bmt;
	}
	
	public double getCounterCumulativeAmount() {
		return counterCumulativeAmount;
	}
	
	public void setCounterCumulativeAmount (double cca) {
		counterCumulativeAmount = cca;
	}
	
	public double getCounterQualifyingMonths() {
		return counterQualifyingMonths;
	}
	
	public void setCounterQualifyingMonths (double cqm) {
		counterQualifyingMonths = cqm;
	}
	
	public String getCounterType() {
		return counterType;
	}
	
	public void setCounterType (String ct) {
		counterType = ct;
	}
	
	public Date getBonusExpiryDate() {
		return bonusExpiryDate;
	}
	
	public void setBonusExpiryDate (Date bonusExpiry) {
		bonusExpiryDate = bonusExpiry;
	}
	
	public Date getBonusEffectiveDate() {
		return bonusEffectiveDate;
	}
	
	public void setBonusEffectiveDate(Date counterEffective) {
		counterEffectiveDate = counterEffective;
	}
	
	public Date getCounterExpiryDate() {
		return counterExpiryDate;
	}
	
	public void setCounterExpiryDate (Date counterExpiry) {
		counterExpiryDate = counterExpiry;
	}
	
	public Date getCounterEffectiveDate() {
		return counterEffectiveDate;
	}
	
	public void setCounterEffectiveDate(Date counterEffective) {
		counterEffectiveDate = counterEffective;
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer(128);

		s.append("PrepaidPromotionDetailInfo:[\n");
		s.append("    allocatedBucketAmount=[").append(allocatedBucketAmount).append("]\n");
		s.append("    remainingBucketAmount=[").append(remainingBucketAmount).append("]\n");
		s.append("    bonusEffectiveDate=[").append(bonusEffectiveDate).append("]\n");
		s.append("    bonusExpiryDate=[").append(bonusExpiryDate).append("]\n");
		s.append("    bucketMeasureType=[").append(bucketMeasureType).append("]\n");
		s.append("]");

		return s.toString();
	}
}
