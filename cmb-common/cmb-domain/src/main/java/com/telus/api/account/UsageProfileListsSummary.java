package com.telus.api.account;

public interface UsageProfileListsSummary {

	public UsageProfileList getUsageProfileList();

	public UsageProfileAdditionalChargesList getUsageProfileAdditionalChargesList();
	
	public void setUsageProfileList(UsageProfileList usageProfileList);
	
	public void setUsageProfileAdditionalChargesList(UsageProfileAdditionalChargesList usageProfileAdditionalChargesList);
	
}