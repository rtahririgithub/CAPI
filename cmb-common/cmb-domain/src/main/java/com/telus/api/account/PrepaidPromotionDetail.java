package com.telus.api.account;

import java.util.Date;

public interface PrepaidPromotionDetail {
	public final static String BUCKET_MEASURE_TYPE_MONEY = "MONEY";
	public final static String BUCKET_MEASURE_TYPE_MINUTE = "MINUTE";
	public final static String COUNTER_TYPE_USAGE = "USAGE";
	public final static String COUNTER_TYPE_TOPUP = "TOPUP";
	
	double getAllocatedBucketAmount();
	double getRemainingBucketAmount();
	String getBucketMeasureType();
	double getCounterCumulativeAmount();
	double getCounterQualifyingMonths();
	String getCounterType() ;
	Date getBonusExpiryDate();
	Date getBonusEffectiveDate();
	Date getCounterExpiryDate();
	Date getCounterEffectiveDate();


}
