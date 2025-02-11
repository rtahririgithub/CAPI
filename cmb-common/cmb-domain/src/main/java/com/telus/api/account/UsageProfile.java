package com.telus.api.account;

public interface UsageProfile {

	public String getAirtimeFeatureCode();

	public String getAirtimeFeatureDescription();

	public String getDirection();

	public boolean isExtendedHomeArea();

	public String getStep();

	public int getNumberOfCalls();

	public double getTotalAirtime();

	public double getChargeAmount();

	public double getAllowedIncomingMinutes();

	public double getUsedIncomingMinutes();

	public int getFreeAirCalls();

	public int getSpecialCalls();

	public String getFreeMinuteType();

	public String getUnitOfMeasure();

	public int getNumberOfPeriods();

	public UsageProfilePeriodDetail[] getPeriodDetails();

}