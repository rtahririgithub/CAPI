package com.telus.cmb.common.prepaid;

/*
 *  This file is currently not being used as of Jan 2014 (Surepay project).
 *  This enum class is meant to hold the constant values for 
 *  ProductCharacteristics and Product Order.
 *  
 *  A lot of calls to the Prepaid WebSservices contains an object similar to a hashmap. 
 *  For example,changing an AutoRenew would contain a product would put this
 *  in the request.
 *  
 *  <productCharacteristic>
 *     <productCharacteristicKey>
 *        <id>autoRenew</id>
 *        <name>autoRenew</name>
 *     </productCharacteristicKey>
 *     <productCharacteristicValue>
 *        <value>false</value>
 *     </productCharacteristicValue>
 *  </productCharacteristic>
 *  
 */
public enum ProductCharacteristics {
    AUTO_RENEW_INDICATOR("autoRenew"),
    AUTO_RENEW_FUND_SOURCE_INDICATOR("renewType"),
    PURCHASE_TYPE_INDICATOR("purchaseType"),
    FUND_SOURCE_TYPE_CC("CreditCard"),
    FUND_SOURCE_TYPE_BALANCE("Balance"),
    FUND_SOURCE_TYPE_BANKCARD("BankCard"),
    FUND_SOURCE_TYPE_NOT_DEFINED("NotDefined"),
    DEFAULT_PREPAID_OFFER("Prepaid Offer"),
    SPECIAL_DESTINATION_LIST_SEPARATOR(","),
    SPECIAL_DESTINATION_LIST_ACTION_ADD_ID("add-SDList"),
    SPECIAL_DESTINATION_LIST_ACTION_ADD_NAME("AddSpecialDestinationList"),
    SPECIAL_DESTINATION_LIST_ACTION_REMOVE_ID("remove-SDList"),
    SPECIAL_DESTINATION_LIST_ACTION_REMOVE_NAME("RemoveSpecialDestinationList"),
    SPECIAL_DESTINATION_LIST_ACTION_UPDATE_ID("update-SDList"),
    SPECIAL_DESTINATION_LIST_ACTION_UPDATE_NAME("UpdateSpecialDestinationList"),
    SPECIAL_DESTINATION_LIST_DEFAULT_PRODUCT_SERIAL_NO("-999"),
    QUANTITY_DEFAULT_AMT("1.0"),
    QUANTITY_DEFAULT_UNITS("period"),
    FEATURE_EXPIRY_DATE("featureExpiryDate")
    ;

    private ProductCharacteristics(final String text) {
        this.text = text;
    }

    private final String text;

    public String getValue() {
        return text;
    }
    
}