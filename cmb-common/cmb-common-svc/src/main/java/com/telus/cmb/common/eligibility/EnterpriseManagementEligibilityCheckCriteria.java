package com.telus.cmb.common.eligibility;

import com.telus.eas.framework.eligibility.DefaultEligibilityCheckCriteria;

public class EnterpriseManagementEligibilityCheckCriteria extends DefaultEligibilityCheckCriteria {
	private String accountCombinedType;
	private String productType;
	private int brandId;
	private String processType = null;
	
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
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
	@Override
	public String toString() {
		return "EnterpriseDataSyncEligibilityCheckCriteria [accountCombinedType="
				+ accountCombinedType
				+ ", productType="
				+ productType
				+ ", brandId=" + brandId + ", processType=" + processType + "]";
	}

	
	
}
