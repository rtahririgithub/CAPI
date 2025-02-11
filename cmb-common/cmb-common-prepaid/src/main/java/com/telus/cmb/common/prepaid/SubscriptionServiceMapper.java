package com.telus.cmb.common.prepaid;

import java.util.HashMap;
import java.util.List;

import com.telus.api.reference.FundSource;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.TimePeriod;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.Product;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristicValue;

/*
 *  This file is currently not being used as of Jan 2014 (Surepay project).
 *  This client helps map or easily create objects when using SubscriptionService
 *  or any other PrepaidWS.
 *  
*/

public class SubscriptionServiceMapper {

	public static ServiceAgreementInfo mapProductBundleToServiceAgreementInfo(Product productBundle, ServiceAgreementInfo serviceAgreementInfo){
			
		serviceAgreementInfo.setServiceCode(productBundle.getProductSerialNumber().trim());
		TimePeriod timePeriod = productBundle.getValidFor();
		serviceAgreementInfo.setEffectiveDate(timePeriod.getStartDateTime());
		serviceAgreementInfo.setExpiryDate(timePeriod.getEndDateTime());
		
		/* 	Look for AutoRenew Value and AutoRenewFundSource. The SubscriptionService would have this under productCharacteristics
		 *  ProductCharateristics is a list of 2 things. ProductCharacKey and ProdCharacValue
		 *  Both ProductCharacKey and ProdCharacValue are lists.
		 *  The values we received are managed in the enum ProductBundleCharacteristics
		 *  
		 *  One of the possible productCharacteristics is "AutoRenew"
		 *  The list prodCharacKey contains the string id "autoRenew".
		 *  The list prodCharacValue contains the string value true or false.
		 *  
		 *  Another entry in the productCharacteristics is "renewType".
		 *  The list prodCharacKey contains the string id "renewType".
		 */
		
		List<ProductCharacteristic> productCharacteristics = productBundle.getProductCharacteristics();
		ProductCharacteristic productCharacteristic;
		
		for(int i = 0; i < productCharacteristics.size(); i++){
			productCharacteristic = productCharacteristics.get(i);
			if (productCharacteristic.getProductCharacteristicKey().getId().equalsIgnoreCase(ProductCharacteristics.AUTO_RENEW_INDICATOR.getValue())){
				String productCharacteristicValue =	getSingleProductCharacteristicValue(productCharacteristic);
				if (productCharacteristicValue != null){
					if (productCharacteristicValue.equalsIgnoreCase("true"))
						serviceAgreementInfo.setAutoRenew(true);
					else
						serviceAgreementInfo.setAutoRenew(false);
				}
			}
			if (productCharacteristic.getProductCharacteristicKey().getId().equalsIgnoreCase(ProductCharacteristics.AUTO_RENEW_FUND_SOURCE_INDICATOR.getValue())){
				String productCharacteristicValue =	getSingleProductCharacteristicValue(productCharacteristic);				
				int source = FundSource.FUND_SOURCE_NOT_DEFINED;
				
				if (productCharacteristicValue.equalsIgnoreCase(ProductCharacteristics.FUND_SOURCE_TYPE_CC.getValue()))
					source = FundSource.FUND_SOURCE_CREDIT_CARD;
				else if (productCharacteristicValue.equalsIgnoreCase(ProductCharacteristics.FUND_SOURCE_TYPE_BALANCE.getValue()))
					source = FundSource.FUND_SOURCE_BALANCE;
				else if (productCharacteristicValue.equalsIgnoreCase(ProductCharacteristics.FUND_SOURCE_TYPE_BANKCARD.getValue()))
					source = FundSource.FUND_SOURCE_BANK_CARD;
				
				serviceAgreementInfo.setAutoRenewFundSource(source);			
			}			
		}
		serviceAgreementInfo.setPurchaseFundSource(FundSource.FUND_SOURCE_NOT_DEFINED);

		//Below are default values previously set during Prepaid API mapping
		serviceAgreementInfo.setWPS(true);
		
		HashMap<String, ServiceFeatureInfo> serviceFeatures = new HashMap<String, ServiceFeatureInfo>();
		ServiceFeatureInfo sf = new ServiceFeatureInfo();
		sf.setFeatureCode(serviceAgreementInfo.getServiceCode().trim());
		// sf.setEffectiveDate(???); <-was this way during API mapping
		sf.setExpiryDate(serviceAgreementInfo.getExpiryDate());
		sf.setTransaction(ServiceFeatureInfo.NO_CHG);
		serviceFeatures.put(sf.getFeatureCode(), sf);
		serviceAgreementInfo.setFeatures(serviceFeatures);	

		return serviceAgreementInfo;
	}
	
	//for use with Product Characteristics returning single values
	private static String getSingleProductCharacteristicValue(ProductCharacteristic productCharacteristic){
		List<ProductCharacteristicValue> productCharacteristicValues = productCharacteristic.getProductCharacteristicValue();
		
		String returnVal = null;
		//should only have 1 prodCharValue
		for(int y = 0; y < productCharacteristicValues.size(); y++){
			ProductCharacteristicValue productCharacteristicValue = productCharacteristicValues.get(y);
			returnVal = productCharacteristicValue.getValue();
		}
		
		return returnVal;
	}

}
