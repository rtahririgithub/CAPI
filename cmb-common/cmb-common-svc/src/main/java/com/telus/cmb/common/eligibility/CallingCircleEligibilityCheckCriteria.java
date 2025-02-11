package com.telus.cmb.common.eligibility;

import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;

public class CallingCircleEligibilityCheckCriteria extends DefaultEligibilityCheckCriteria {
	private String accountCombinedType;
	private String productType;
	private int brandId;
	
	@Override
	public String getAccountCombinedType() {
		return accountCombinedType;
	}
	public void setAccountCombinedType(String accountCombinedType) {
		this.accountCombinedType = accountCombinedType;
	}
	
	@Override
	public String getProductType() {
		return productType;
	}
	
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	@Override
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	
	@Override
	public String toString() {
		return "CallingCircleEligibilityCheckCriteria [accountCombinedType="
				+ accountCombinedType
				+ ", productType="
				+ productType
				+ ", brandId=" + brandId +  "]";
	}
}
