package com.telus.api.account;

public interface UsageProfileAdditionalCharges {

	public String getAirtimeFeatureCode();

	public String getAirtimeFeatureDescription();

	public String getSecondaryFeatureCode();

	public String getSecondaryFeatureDescription();

	public String getDirection();

	public boolean isExtendedHomeArea();

	public int getNumberOfCalls();

	public double getTotalAirtime();

	public double getChargeAmount();

}