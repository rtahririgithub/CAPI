/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.reference;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Pavel Simonovsky
 *
 */
public class PricePlanSelectionCriteria implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productType;
	private String equipmentType;
	private String provinceCode;
	private Character accountType;
	private Character accountSubType;
	private Boolean currentPlansOnly;
	private Boolean availableForActivationOnly;
	private String activityCode;
	private String activityReasonCode;
	private Integer brandId;
	private String networkType;
	private Integer term;
	private Long[] productPromoTypes;
	private Boolean initialActivation;
	private Boolean includeFeaturesAndServices;

	public PricePlanSelectionCriteria() {
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
	 * @return the accountType
	 */
	public Character getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(Character accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the accountSubType
	 */
	public Character getAccountSubType() {
		return accountSubType;
	}

	/**
	 * @param accountSubType the accountSubType to set
	 */
	public void setAccountSubType(Character accountSubType) {
		this.accountSubType = accountSubType;
	}

	/**
	 * @return the currentPlansOnly
	 */
	public Boolean getCurrentPlansOnly() {
		return currentPlansOnly;
	}

	/**
	 * @param currentPlansOnly the currentPlansOnly to set
	 */
	public void setCurrentPlansOnly(Boolean currentPlansOnly) {
		this.currentPlansOnly = currentPlansOnly;
	}

	/**
	 * @return the availableForActivationOnly
	 */
	public Boolean getAvailableForActivationOnly() {
		return availableForActivationOnly;
	}

	/**
	 * @param availableForActivationOnly the availableForActivationOnly to set
	 */
	public void setAvailableForActivationOnly(Boolean availableForActivationOnly) {
		this.availableForActivationOnly = availableForActivationOnly;
	}

	/**
	 * @return the activityCode
	 */
	public String getActivityCode() {
		return activityCode;
	}

	/**
	 * @param activityCode the activityCode to set
	 */
	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	/**
	 * @return the activityReasonCode
	 */
	public String getActivityReasonCode() {
		return activityReasonCode;
	}

	/**
	 * @param activityReasonCode the activityReasonCode to set
	 */
	public void setActivityReasonCode(String activityReasonCode) {
		this.activityReasonCode = activityReasonCode;
	}

	/**
	 * @return the brandId
	 */
	public Integer getBrandId() {
		return brandId;
	}

	/**
	 * @param brandId the brandId to set
	 */
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	/**
	 * @return the networkType
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the term
	 */
	public Integer getTerm() {
		return term;
	}

	/**
	 * @param term the term to set
	 */
	public void setTerm(Integer term) {
		this.term = term;
	}

	/**
	 * @return the productPromoTypes
	 */
	public Long[] getProductPromoTypes() {
		return productPromoTypes;
	}

	/**
	 * @param productPromoTypes the productPromoTypes to set
	 */
	public void setProductPromoTypes(Long[] productPromoTypes) {
		this.productPromoTypes = productPromoTypes;
	}

	/**
	 * @return the initialActivation
	 */
	public Boolean getInitialActivation() {
		return initialActivation;
	}

	/**
	 * @param initialActivation the initialActivation to set
	 */
	public void setInitialActivation(Boolean initialActivation) {
		this.initialActivation = initialActivation;
	}

	/**
	 * @return the includeFeaturesAndServices
	 */
	public Boolean getIncludeFeaturesAndServices() {
		return includeFeaturesAndServices;
	}

	/**
	 * @param includeFeaturesAndServices the includeFeaturesAndServices to set
	 */
	public void setIncludeFeaturesAndServices(Boolean includeFeaturesAndServices) {
		this.includeFeaturesAndServices = includeFeaturesAndServices;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("PricePlanSelectionCriteria [productType=")
		.append(productType)
		.append(", equipmentType=")
		.append(equipmentType)
		.append(", provinceCode=")
		.append(provinceCode)
		.append(", accountType=")
		.append(accountType)
		.append(", accountSubType=")
		.append(accountSubType)
		.append(", currentPlansOnly=")
		.append(currentPlansOnly)
		.append(", availableForActivationOnly=")
		.append(availableForActivationOnly)
		.append(", activityCode=")
		.append(activityCode)
		.append(", activityReasonCode=")
		.append(activityReasonCode)
		.append(", brandId=")
		.append(brandId)
		.append(", networkType=")
		.append(networkType)
		.append(", term=")
		.append(term)
		.append(", productPromoTypes=")
		.append(productPromoTypes != null ? Arrays
				.asList(productPromoTypes) : null)
				.append(", initialActivation=").append(initialActivation)
				.append(", includeFeaturesAndServices=")
				.append(includeFeaturesAndServices).append("]");
		return buffer.toString();
	}





}