package com.telus.eas.framework.eligibility;

public abstract class DefaultEligibilityCheckCriteria implements EligibilityCheckCriteria {

	public String getAccountCombinedType() {
		return null;
	}

	public int getBrandId() {
		return 1;
	}

	public String getCreditClass() {
		return "B";
	}
	
	public String getProductType() {
		return "C";
	}
	
	public String getProcessType() {
		return null;
	}
	
	public String getAccountSubType() {
		return null;
	}

}