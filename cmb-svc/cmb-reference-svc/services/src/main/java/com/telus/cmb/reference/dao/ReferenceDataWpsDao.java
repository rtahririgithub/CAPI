/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.reference.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.reference.NetworkType;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.ProductOfferingServicePort;
import com.telus.eas.framework.info.Info;
import com.telus.eas.utility.info.FundSourceInfo;
import com.telus.eas.utility.info.PrepaidCategoryInfo;
import com.telus.eas.utility.info.RatedFeatureInfo;
import com.telus.eas.utility.info.ServiceEquipmentTypeInfo;
import com.telus.eas.utility.info.ServiceInfo;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.productofferingservice_v2.GetProductOffering;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.productofferingservice_v2.GetProductOfferingResponse;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.productofferingservice_v2.GetProductOfferings;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.product.productoffering.product_offering_types_v2.ComponentProductOfferingPrice;
import com.telus.tmi.xmlschema.xsd.product.productoffering.product_offering_types_v2.ProductOffering;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.AtomicProductSpecificationCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.CompositeProductSpecificationCharacteristicValue;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.ProductCategory;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.ProductSpecificationCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.ProductSpecificationCharacteristicValue;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.ProductType;

/**
 * @author Pavel Simonovsky
 * 
 */
public class ReferenceDataWpsDao extends SoaBaseSvcClient {

	private static final ProductType PREPAID_PRODUCT_TYPE = ProductType.REGULAR;
	private static final BigInteger PREPAID_BRAND_ID = new BigInteger("1");

	private static final String PREPAID_CHARACTERISTIC_FRENCH_DESCRIPTION = "FrenchDescription";
	private static final String PREPAID_CHARACTERISTIC_VALIDITY_PERIOD = "ValidityPeriod";
	private static final String PREPAID_CHARACTERISTIC_AUTO_RENEWABLE = "autoRenewable";
	private static final String PREPAID_CHARACTERISTIC_FORCED_AUTO_RENEWAL = "forcedAutoRenew";
	private static final String PREPAID_CHARACTERISTIC_MAX_CONSECUTIVE_DAYS = "maxConsecutiveDays";
	private static final String PREPAID_CHARACTERISTIC_PRIORITY_ORDER = "order";
	private static final String PREPAID_CHARACTERISTIC_KB_MAPPED_SOC = "SOC";
	private static final String PREPAID_CHARACTERISTIC_RENEWAL_FUND_SOURCE = "renewType";
	private static final String PREPAID_CHARACTERISTIC_PURCHASE_FUND_SOURCE = "purchaseType";
	private static final String PREPAID_CHARACTERISTIC_EQUIPMENT_TYPE = "EquipmentType";
	private static final String PREPAID_CHARACTERISTIC_NETWORK_TYPE = "NetworkType";

	private static final String PREPAID_FUND_SOURCE_BALANCE = "Balance";
	private static final String PREPAID_FUND_SOURCE_CREDIT_CARD = "CreditCard";

	private static final String PREPAID_PRICE_TYPE_RECURRING = "recurring";

	private static final String PREPAID_CATEGORY_ENGLISH_DESCRIPTION = "DISPLAY_EN";
	private static final String PREPAID_CATEGORY_FRENCH_DESCRIPTION = "DISPLAY_FR";

	private static final String PREPAID_NETWORK_TYPE_CDMA = "CDMA";
	private static final String PREPAID_NETWORK_TYPE_HSPA = "HSPA";
	private static final String LBM_PROMOTION_CATEGORY_CODE = "LBP";
	private static final int LBM_TRACKING_FEATURE_ID = 700;

	@Autowired
	private ProductOfferingServicePort productOfferingServicePort;

	public ServiceInfo[] retrieveFeaturesList() throws ApplicationException {

		return execute( new SoaCallback<ServiceInfo[]>() {
			
			/*
			 * (non-Javadoc)
			 * @see com.telus.cmb.common.dao.soa.spring.SoaCallback#doCallback()
			 */
			@Override
			public ServiceInfo[] doCallback() throws Throwable {
				List<ServiceInfo> result = new ArrayList<ServiceInfo>();
				GetProductOfferings parameters = new GetProductOfferings();
				parameters.setBrandID(PREPAID_BRAND_ID);
				parameters.setProductType(PREPAID_PRODUCT_TYPE);
				List<ProductOffering> productOfferingList = productOfferingServicePort.getProductOfferings(parameters).getProductOfferings();
				for (ProductOffering prepaidProductOffering : productOfferingList) {
					result.add(mapProductOfferingToServiceInfo(prepaidProductOffering));
				}
				return result.toArray(new ServiceInfo[result.size()]);
			}
		});
	}

	public ServiceInfo retrieveFeature(final int featureId) throws ApplicationException {

		return execute( new SoaCallback<ServiceInfo>() {
			
			@Override
			public ServiceInfo doCallback() throws Throwable {
				GetProductOffering parameters = new GetProductOffering();
				parameters.setBrandID(PREPAID_BRAND_ID);
				parameters.setProductType(PREPAID_PRODUCT_TYPE);
				parameters.setId(String.valueOf(featureId));
				GetProductOfferingResponse productOfferingResponse = productOfferingServicePort.getProductOffering(parameters);
				return mapProductOfferingToServiceInfo(productOfferingResponse.getProductOffering());
			}
		});
	}

	private void populateDummyRatedFeatures(ServiceInfo serviceInfo) {
		RatedFeatureInfo[] features = new RatedFeatureInfo[1];
		features[0] = mapPrepaidFeatureToRatedFeatureInfo(serviceInfo);
		serviceInfo.setFeatures(features);
	}

	private RatedFeatureInfo mapPrepaidFeatureToRatedFeatureInfo(ServiceInfo serviceInfo) {
		RatedFeatureInfo ratedFeature = new RatedFeatureInfo();

		String code = Info.padTo(String.valueOf(serviceInfo.getCode()).trim(), ' ', 6);
		ratedFeature.setCode(code);
		ratedFeature.setDescription(serviceInfo.getDescription());
		ratedFeature.setDescriptionFrench(serviceInfo.getDescriptionFrench());
		ratedFeature.setDuplFeatureAllowed(true);
		ratedFeature.setRecurringChargeFrequency(serviceInfo.getTerm());
		ratedFeature.setRecurringCharge(serviceInfo.getRecurringCharge());
		ratedFeature.setUsageCharge(0.0);
		ratedFeature.setAdditionalNumberRequired(false);
		ratedFeature.setTelephony(true);
		ratedFeature.setDispatch(false);
		ratedFeature.setWirelessWeb(false);
		ratedFeature.setParameterRequired(false);
		ratedFeature.setSwitchCode("");
		ratedFeature.setCategoryCode("");
		ratedFeature.setPrepaidCallingCircle(false);
		ratedFeature.setWPS(true);

		return ratedFeature;
	}

	private static class NetworkMap {

		private Map<String, ServiceEquipmentTypeInfo> networkMap = new HashMap<String, ServiceEquipmentTypeInfo>();

		public void addMapping(String networkType, String serviceCode, String equipmentType) {

			ServiceEquipmentTypeInfo serviceEquipmentTypeInfo = networkMap.get(networkType);
			if (serviceEquipmentTypeInfo == null) {
				serviceEquipmentTypeInfo = new ServiceEquipmentTypeInfo();
				serviceEquipmentTypeInfo.setCode(serviceCode);
				serviceEquipmentTypeInfo.setNetworkType(networkType);
				networkMap.put(networkType, serviceEquipmentTypeInfo);
			}
			serviceEquipmentTypeInfo.addEquipmentType(equipmentType);
		}

		public Collection<ServiceEquipmentTypeInfo> values() {
			return networkMap.values();
		}
	}

	private ServiceInfo mapProductOfferingToServiceInfo(ProductOffering prepaidProductOffering) {

		ServiceInfo serviceInfo = new ServiceInfo();
		serviceInfo.setCode(prepaidProductOffering.getProductSpecification().getProductNumber().trim());
		serviceInfo.setDescription(prepaidProductOffering.getProductSpecification().getDescription());

		ProductSpecificationCharacteristic frenchDescription = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_FRENCH_DESCRIPTION);
		ProductSpecificationCharacteristic validityPeriod = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_VALIDITY_PERIOD);
		ProductSpecificationCharacteristic autoRenew = getPrepaidProductCharacteristic(prepaidProductOffering, PREPAID_CHARACTERISTIC_AUTO_RENEWABLE);
		ProductSpecificationCharacteristic equipmentTypeMap = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_EQUIPMENT_TYPE);
		ProductSpecificationCharacteristic forcedAutoRenew = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_FORCED_AUTO_RENEWAL);
		ProductSpecificationCharacteristic kbMappedService = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_KB_MAPPED_SOC);
		ProductSpecificationCharacteristic maxConsecutiveDays = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_MAX_CONSECUTIVE_DAYS);
		ProductSpecificationCharacteristic priority = getPrepaidProductCharacteristic(prepaidProductOffering, PREPAID_CHARACTERISTIC_PRIORITY_ORDER);
		ProductSpecificationCharacteristic purchaseFundSource = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_PURCHASE_FUND_SOURCE);
		ProductSpecificationCharacteristic renewalFundSource = getPrepaidProductCharacteristic(prepaidProductOffering,
				PREPAID_CHARACTERISTIC_RENEWAL_FUND_SOURCE);
		ComponentProductOfferingPrice recurringCharge = getPrepaidProductOfferingPrice(prepaidProductOffering, PREPAID_PRICE_TYPE_RECURRING);

		serviceInfo.setDescriptionFrench(frenchDescription == null ? "" : frenchDescription.getProductSpecificationCharacteristicValues().get(0)
				.getValue());
		serviceInfo.setTermUnits(ServiceInfo.TERM_UNITS_DAYS);
		serviceInfo.setTerm(validityPeriod == null ? 0 : Integer.parseInt(validityPeriod.getProductSpecificationCharacteristicValues().get(0)
				.getValue()));
		serviceInfo.setRecurringCharge(recurringCharge == null ? 0 : (Double.valueOf(recurringCharge.getPrice().getAmount())));
		serviceInfo.setCurrent(true);
		serviceInfo.setAutoRenewalAllowed(autoRenew == null ? false : Boolean.parseBoolean(autoRenew.getProductSpecificationCharacteristicValues()
				.get(0).getValue()));
		serviceInfo.setForcedAutoRenew(forcedAutoRenew == null ? false : Boolean.parseBoolean(forcedAutoRenew
				.getProductSpecificationCharacteristicValues().get(0).getValue()));
		serviceInfo.setMaxConsActDays(maxConsecutiveDays == null ? 0 : Integer.parseInt(maxConsecutiveDays
				.getProductSpecificationCharacteristicValues().get(0).getValue()));
		serviceInfo.setWPS(true);
		serviceInfo.setPriority(priority == null ? 0 : Integer.parseInt(priority.getProductSpecificationCharacteristicValues().get(0).getValue()));
		serviceInfo.setWPSMapppedKBSocCode(kbMappedService == null ? "" : kbMappedService.getProductSpecificationCharacteristicValues().get(0)
				.getValue());

		serviceInfo.setCategory(decoratePrepaidCategory(prepaidProductOffering));

		if (renewalFundSource != null) {
			serviceInfo.setAllowedRenewalFundSourceArray(mapFundSourceList(renewalFundSource.getProductSpecificationCharacteristicValues()));
		}

		if (purchaseFundSource != null) {
			serviceInfo.setAllowedPurchaseFundSourceArray(mapFundSourceList(purchaseFundSource.getProductSpecificationCharacteristicValues()));
		}

		if (serviceInfo.getCategory() != null && serviceInfo.getCategory().getCode() != null) {
			String categoryCode = serviceInfo.getCategory().getCode().trim();

			if (LBM_PROMOTION_CATEGORY_CODE.equals(categoryCode)) {
				serviceInfo.setWPSServiceType(ServiceSummary.WPS_SERVICE_TYPE_PROMOTIONAL);
			} else if (Integer.parseInt(serviceInfo.getCode().trim()) == LBM_TRACKING_FEATURE_ID) {
				serviceInfo.setWPSServiceType(ServiceSummary.WPS_SERVICE_TYPE_TRACKING);
			} else {
				serviceInfo.setWPSServiceType("");
			}
		}

		if (equipmentTypeMap != null) {
			Collection<ServiceEquipmentTypeInfo> setInfos = mapPrepaidEquipmentType(serviceInfo.getCode(),
					equipmentTypeMap.getProductSpecificationCharacteristicValues());
			for (ServiceEquipmentTypeInfo s : setInfos) {
				serviceInfo.addServiecEquipmentTypeInfo(s);
			}
		}

		populateDummyRatedFeatures(serviceInfo);

		return serviceInfo;
	}

	private Collection<ServiceEquipmentTypeInfo> mapPrepaidEquipmentType(String serviceCode,
			List<CompositeProductSpecificationCharacteristicValue> list) {
		NetworkMap networkMap = new NetworkMap();
		for (CompositeProductSpecificationCharacteristicValue equipmentTypeChar : list) {

			List<AtomicProductSpecificationCharacteristic> prodNetworkTypeList = equipmentTypeChar.getValueSpecificationCharacteristic();
			if (prodNetworkTypeList == null || prodNetworkTypeList.size() == 0) {
				// this means means all network: CDMA and HSPA - prepaid only
				// support these two network type
				networkMap.addMapping(NetworkType.NETWORK_TYPE_CDMA, serviceCode, equipmentTypeChar.getValue());
				networkMap.addMapping(NetworkType.NETWORK_TYPE_HSPA, serviceCode, equipmentTypeChar.getValue());
			} else {
				for (AtomicProductSpecificationCharacteristic networkTypeChar : prodNetworkTypeList) {
					if (networkTypeChar.getName().equalsIgnoreCase(PREPAID_CHARACTERISTIC_NETWORK_TYPE)) {
						List<ProductSpecificationCharacteristicValue> networkTypeValueList = networkTypeChar
								.getProductSpecificationCharacteristicValues();
						for (ProductSpecificationCharacteristicValue networkTypeValue : networkTypeValueList) {
							String networkType = null;
							if (networkTypeValue.getValue().equalsIgnoreCase(PREPAID_NETWORK_TYPE_CDMA)) {
								networkType = NetworkType.NETWORK_TYPE_CDMA;
							} else if (networkTypeValue.getValue().equalsIgnoreCase(PREPAID_NETWORK_TYPE_HSPA)) {
								networkType = NetworkType.NETWORK_TYPE_HSPA;
							}
							networkMap.addMapping(networkType, serviceCode, equipmentTypeChar.getValue());
						}
					}
				}
			}
		}
		return networkMap.values();
	}

	private PrepaidCategoryInfo decoratePrepaidCategory(ProductOffering prepaidProductOffering) {

		ProductCategory categoryEnglishDecription = getPrepaidProductCategory(prepaidProductOffering, PREPAID_CATEGORY_ENGLISH_DESCRIPTION);
		ProductCategory categoryFrenchDecription = getPrepaidProductCategory(prepaidProductOffering, PREPAID_CATEGORY_FRENCH_DESCRIPTION);
		// ProductCategory categoryPriority =
		// getPrepaidProductCategory(prepaidProductOffering,
		// this.PREPAID_CATEGORY_PRIORITY_ORDER);

		PrepaidCategoryInfo prepaidCategory = new PrepaidCategoryInfo();

		prepaidCategory.setCode(categoryEnglishDecription == null ? "" : categoryEnglishDecription.getCategoryCode().trim());
		prepaidCategory.setDescription(categoryEnglishDecription == null ? "" : categoryEnglishDecription.getDescription());
		prepaidCategory.setDescriptionFrench(categoryEnglishDecription == null ? "" : categoryFrenchDecription.getDescription());
		prepaidCategory.setPrority(categoryEnglishDecription == null ? 0 : categoryEnglishDecription.getOrder().intValue());
		return prepaidCategory;
	}

	private FundSourceInfo[] mapFundSourceList(List<CompositeProductSpecificationCharacteristicValue> list) {
		FundSourceInfo[] result = null;
		if (list != null) {
			result = new FundSourceInfo[list.size()];
			for (int idx = 0; idx < list.size(); idx++) {
				result[idx] = mapFundSource(list.get(idx));
			}
		}
		return result;
	}

	private FundSourceInfo mapFundSource(CompositeProductSpecificationCharacteristicValue fundSource) {
		FundSourceInfo info = new FundSourceInfo();
		info.setDefaultIndicator(fundSource.isDefault() ? "Y" : "N");
		if (fundSource.getValue().equalsIgnoreCase(PREPAID_FUND_SOURCE_BALANCE)) {
			info.setFundSourceType(com.telus.api.reference.FundSource.FUND_SOURCE_BALANCE);
		} else if (fundSource.getValue().equalsIgnoreCase(PREPAID_FUND_SOURCE_CREDIT_CARD)) {
			info.setFundSourceType(com.telus.api.reference.FundSource.FUND_SOURCE_CREDIT_CARD);
		} else {
			info.setFundSourceType(com.telus.api.reference.FundSource.FUND_SOURCE_NOT_DEFINED);
		}
		return info;
	}

	private ProductSpecificationCharacteristic getPrepaidProductCharacteristic(ProductOffering prepaidProductOffering, String characteristicName) {
		ProductSpecificationCharacteristic psc = null;
		List<ProductSpecificationCharacteristic> charList = prepaidProductOffering.getProductSpecification().getProductSpecificationCharacteristics();

		for (int i = 0; i < charList.size(); i++) {
			psc = charList.get(i);
			if (psc.getName().equalsIgnoreCase(characteristicName)) {
				return psc;
			}
		}
		return null;
	}

	private ComponentProductOfferingPrice getPrepaidProductOfferingPrice(ProductOffering prepaidProductOffering, String type) {
		ComponentProductOfferingPrice price = null;
		List<ComponentProductOfferingPrice> priceList = prepaidProductOffering.getProductOfferingPrices();

		for (int i = 0; i < priceList.size(); i++) {
			price = priceList.get(i);
			if (price.getPriceType().value().equalsIgnoreCase(type)) {
				return price;
			}
		}
		return null;
	}

	private ProductCategory getPrepaidProductCategory(ProductOffering prepaidProductOffering, String type) {
		ProductCategory category = null;
		List<ProductCategory> categoryList = prepaidProductOffering.getProductSpecification().getProductCategories();

		for (int i = 0; i < categoryList.size(); i++) {
			category = categoryList.get(i);
			if (category.getType().equalsIgnoreCase(type)) {
				return category;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient#ping()
	 */
	@Override
	public String ping() throws ApplicationException {
		return execute( new SoaCallback<String>() {
			@Override
			public String doCallback() throws Throwable {
				return productOfferingServicePort.ping( new Ping()).getVersion();
			}
		});
	}
}
