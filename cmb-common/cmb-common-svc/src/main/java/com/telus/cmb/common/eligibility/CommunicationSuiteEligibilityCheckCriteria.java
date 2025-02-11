package com.telus.cmb.common.eligibility;

import com.telus.api.reference.Brand;
import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;

/**
 * 
 * @author Naresh Annabathula
 *
 */
public class CommunicationSuiteEligibilityCheckCriteria extends DefaultEligibilityCheckCriteria {

	private String accountType;
	private String accountSubType;
	private int brandId = Brand.BRAND_ID_TELUS;

	@Override
	public String getAccountCombinedType() {
		return getAccountType();
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getAccountSubType() {
		return accountSubType;
	}
	public void setAccountSubType(String accountSubType) {
		this.accountSubType = accountSubType;
	}
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	@Override
	public String toString() {
		return "CommunicationSuiteEligibilityCheckCriteria [accountType="
				+ accountType + ", accountSubType=" + accountSubType
				+ ", brandId=" + brandId + "]";
	}
}
