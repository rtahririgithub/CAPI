package com.telus.eas.equipment.info;

public class ReservedSimProfileInfo implements java.io.Serializable {
	private String iccId;
	private int brandId;
	private String productCode;
	private String productClassCode;
	private String productGroupTypeCode;
	private String productTypeId;
	private String technologyTypeTxt;
	private static final long serialVersionUID = 1L;

	public String getIccId() {
		return iccId;
	}

	public void setIccId(String iccId) {
		this.iccId = iccId;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductClassCode() {
		return productClassCode;
	}

	public void setProductClassCode(String productClassCode) {
		this.productClassCode = productClassCode;
	}

	public String getProductGroupTypeCode() {
		return productGroupTypeCode;
	}

	public void setProductGroupTypeCode(String productGroupTypeCode) {
		this.productGroupTypeCode = productGroupTypeCode;
	}

	public String getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(String productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getTechnologyTypeTxt() {
		return technologyTypeTxt;
	}

	public void setTechnologyTypeTxt(String technologyTypeTxt) {
		this.technologyTypeTxt = technologyTypeTxt;
	}

	@Override
	public String toString() {
		return "ReservedSimProfileInfo [iccId=" + iccId + ", brandId=" + brandId + ", productCode=" + productCode + ", productClassCode=" + productClassCode + ", productGroupTypeCode=" + productGroupTypeCode + ", productTypeId="
				+ productTypeId + ", technologyTypeTxt=" + technologyTypeTxt + "]";
	}
}