package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class ContractOverridingInfo extends Info {
	protected String provinceCode;
	protected String equipmentType;
	protected String productType;
	/**
	 * @return the provinceCode
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	/**
	 * @param provinceCode the provinceCode to set
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	/**
	 * @return the equipmentType
	 */
	public String getEquipmentType() {
		return equipmentType;
	}
	/**
	 * @param equipmentType the equipmentType to set
	 */
	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	/**
	 * @return the productType
	 */
	public String getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	
}
