package com.telus.api.account;

public interface UsageProfilePeriodDetail {

	public String getPeriodDescription();

	public int getTotalCallsInPeriod();

	public double getTotalMinutesInPeriod();

	public double getTotalChargeAmountInPeriod();

	public double getAllowedIncludedMinutesInPeriod();

	public double getUsedIncludedMinutesInPeriod();

}