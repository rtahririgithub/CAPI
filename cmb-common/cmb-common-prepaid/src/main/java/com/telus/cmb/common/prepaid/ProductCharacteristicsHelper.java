package com.telus.cmb.common.prepaid;

import com.telus.api.reference.FundSource;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristicValue;
import com.telus.tmi.xmlschema.xsd.product.productspecification.product_specification_types_v2.ProductSpecificationCharacteristicKey;

public class ProductCharacteristicsHelper {

	public static ProductCharacteristic createProductCharacteristic(String productCharacteristicKeyId, String productCharacteristicKeyName,
			String productCharacteristicValue){		
		ProductCharacteristic productCharacteristic = new ProductCharacteristic();
		ProductSpecificationCharacteristicKey productCharacteristicKey = new ProductSpecificationCharacteristicKey();
		ProductCharacteristicValue productCharacteristicValue1 = new ProductCharacteristicValue();
		
		productCharacteristicKey.setId(productCharacteristicKeyId);
		productCharacteristicKey.setName(productCharacteristicKeyName);
		productCharacteristicValue1.setValue(productCharacteristicValue);
		
		productCharacteristic.setProductCharacteristicKey(productCharacteristicKey);
		//This is the way to add an item since there is no "setProductCharacteristicValue"
		productCharacteristic.getProductCharacteristicValue().add(productCharacteristicValue1);
		
		return productCharacteristic;
	}
	
	//For items which have the Key ID and Name as the same value
	public static ProductCharacteristic createProductCharacteristic(String productCharacteristicKeyIdAndName, String productCharacteristicValue){	
		ProductCharacteristic productCharacteristic = createProductCharacteristic(productCharacteristicKeyIdAndName, 
				productCharacteristicKeyIdAndName, productCharacteristicValue);
		return productCharacteristic;
		
	}
	
	public static ProductCharacteristic createAutoRenewProductCharacteristic(boolean autoRenewVal){
		
		String autoRenewValue = autoRenewVal ? "true" : "false";
		ProductCharacteristic autoRenewProductCharacteristic = createProductCharacteristic(ProductCharacteristics.AUTO_RENEW_INDICATOR.getValue(), 
				ProductCharacteristics.AUTO_RENEW_INDICATOR.getValue(), autoRenewValue);
		return autoRenewProductCharacteristic;
	}
	
	public static ProductCharacteristic createFundSourceBasedProductCharacteristic(String productCharacteristicsKeyId, 
			String productCharacteristicsKeyValue, int fundSource){
		
		String value = "";
		switch(fundSource){
		case FundSource.FUND_SOURCE_BALANCE: value = ProductCharacteristics.FUND_SOURCE_TYPE_BALANCE.getValue();
			break;
		case FundSource.FUND_SOURCE_CREDIT_CARD: value = ProductCharacteristics.FUND_SOURCE_TYPE_CC.getValue();
			break;
		case FundSource.FUND_SOURCE_BANK_CARD: value = ProductCharacteristics.FUND_SOURCE_TYPE_BANKCARD.getValue();
			break;
		case FundSource.FUND_SOURCE_NOT_DEFINED: value = ProductCharacteristics.FUND_SOURCE_TYPE_NOT_DEFINED.getValue();
			break;	
		}
		
		ProductCharacteristic productCharacteristic = createProductCharacteristic(productCharacteristicsKeyId, 
				productCharacteristicsKeyValue, value);
		return productCharacteristic;
	}
	 
}
