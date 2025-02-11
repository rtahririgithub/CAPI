package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.reference.FundSource;
import com.telus.api.reference.ServiceSummary;
import com.telus.cmb.common.prepaid.ProductCharacteristics;
import com.telus.cmb.common.prepaid.ProductCharacteristicsHelper;
import com.telus.cmb.common.prepaid.SubscriptionServiceClient;
import com.telus.cmb.common.util.DateUtil;
import com.telus.cmb.subscriber.lifecyclemanager.dao.SubscriptionServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.eas.subscriber.info.ServiceFeatureInfo;
import com.telus.eas.utility.info.FeatureInfo;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;

public class SubscriptionServiceDaoImpl implements SubscriptionServiceDao{
	
	@Autowired
	private SubscriptionServiceClient subscriptionServiceClient;
	
	private final Logger LOGGER = Logger.getLogger(SubscriptionServiceDaoImpl.class);
	
	@Override
	public void updateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo, 
			ServiceAgreementInfo existingServiceAgreementInfo) throws ApplicationException{
		
		List<ProductCharacteristic> productCharacteristic = new ArrayList<ProductCharacteristic>();
		
		if (existingServiceAgreementInfo.getAutoRenew() ^ serviceAgreementInfo.getAutoRenew() ||
				existingServiceAgreementInfo.getAutoRenewFundSource() != serviceAgreementInfo.getAutoRenewFundSource()) {
			
			ProductCharacteristic autoRenewProductCharacteristics = createAutoRenewProductCharacteristics(serviceAgreementInfo.getAutoRenew());
			productCharacteristic.add(autoRenewProductCharacteristics);
			
			if (!(serviceAgreementInfo.getAutoRenewFundSource() == ServiceSummary.AUTORENEW_NOT_DEFINED)) {
				ProductCharacteristic renewTypeProductCharacteristics = createRenewTypeProductCharacteristics(serviceAgreementInfo.getAutoRenewFundSource());
				productCharacteristic.add(renewTypeProductCharacteristics);
			}
			if (!(serviceAgreementInfo.getPurchaseFundSource() == FundSource.FUND_SOURCE_NOT_DEFINED)) {
				ProductCharacteristic purchaseTypeProductCharacteristics = createPurchaseTypeProductCharacteristics(serviceAgreementInfo.getPurchaseFundSource());
				productCharacteristic.add(purchaseTypeProductCharacteristics);
			}
		}
		
		if (serviceAgreementInfo.getExpiryDate() != null && existingServiceAgreementInfo.getExpiryDate() != null && !existingServiceAgreementInfo.getExpiryDate().equals(serviceAgreementInfo.getExpiryDate())) {
			ProductCharacteristic featureExpiryDateProductCharacteristics = createFeatureExpiryDateProductCharacteristics(serviceAgreementInfo);
			productCharacteristic.add(featureExpiryDateProductCharacteristics);
		}		
		
		if (productCharacteristic != null && productCharacteristic.size() > 0)
			subscriptionServiceClient.changeProductCharacteristicValue(phoneNumber, serviceAgreementInfo.getServiceCode().trim(), productCharacteristic);
		else if (LOGGER.isDebugEnabled())
			LOGGER.debug("productCharacteristic is empty for " + phoneNumber + " while calling updateFeatureForPrepaidSubscriber.");
	}
	
	@Override
	public void deactivateFeatureForPrepaidSubscriber(String phoneNumber, ServiceAgreementInfo serviceAgreementInfo) throws ApplicationException {
		subscriptionServiceClient.removeSubscribedProduct(phoneNumber, serviceAgreementInfo.getServiceCode().trim());
	}
	
	public void updateCallingCircleParameters( String applicationId, String userId, String phoneNumber,
			ServiceAgreementInfo serviceAgreement, byte action) throws ApplicationException{
		
		String specialDestinationPhoneNumbers =	getSpecialDestinationNumbers(phoneNumber, serviceAgreement, action);		
		String id = "";
		String name = "";
		if ( action==ServiceAgreementInfo.ADD) {
			id = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_ADD_ID.getValue();
			name = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_ADD_NAME.getValue();
		}else if ( action==ServiceAgreementInfo.UPDATE) {
			id = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_UPDATE_ID.getValue();
			name = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_UPDATE_NAME.getValue();
		}else if ( action==ServiceAgreementInfo.DELETE) {
			id = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_REMOVE_ID.getValue();
			name = ProductCharacteristics.SPECIAL_DESTINATION_LIST_ACTION_REMOVE_NAME.getValue();
		}
		ProductCharacteristic productCharactersitic = ProductCharacteristicsHelper.createProductCharacteristic(id, name, specialDestinationPhoneNumbers.toString());
		List<ProductCharacteristic>  productCharacteristicList=new ArrayList<ProductCharacteristic>();
		productCharacteristicList.add(productCharactersitic);
		subscriptionServiceClient.changeProductCharacteristicValue(phoneNumber, ProductCharacteristics.SPECIAL_DESTINATION_LIST_DEFAULT_PRODUCT_SERIAL_NO.getValue(), productCharacteristicList);
		
	}
	
	private ProductCharacteristic createAutoRenewProductCharacteristics(boolean autoRenewValue){
		ProductCharacteristic autoRenewProductCharacteristics = ProductCharacteristicsHelper.createAutoRenewProductCharacteristic(autoRenewValue);
		return autoRenewProductCharacteristics;
	}
	
	private ProductCharacteristic createRenewTypeProductCharacteristics(int fundSource){
		ProductCharacteristic renewTypeProductCharacteristics = ProductCharacteristicsHelper.createFundSourceBasedProductCharacteristic(
						ProductCharacteristics.AUTO_RENEW_FUND_SOURCE_INDICATOR.getValue(), 
						ProductCharacteristics.AUTO_RENEW_FUND_SOURCE_INDICATOR.getValue(), 
						fundSource);
		return renewTypeProductCharacteristics;
	}
	
	private ProductCharacteristic createPurchaseTypeProductCharacteristics(int fundSource){
		ProductCharacteristic purchaseTypeProductCharacteristics = ProductCharacteristicsHelper.createFundSourceBasedProductCharacteristic(
						ProductCharacteristics.PURCHASE_TYPE_INDICATOR.getValue(), 
						ProductCharacteristics.PURCHASE_TYPE_INDICATOR.getValue(), 
						fundSource);
		return purchaseTypeProductCharacteristics;
	}
	
	private ProductCharacteristic createFeatureExpiryDateProductCharacteristics(ServiceAgreementInfo serviceAgreementInfo) {	
		String productCharacteristicValue = dateToString(serviceAgreementInfo.getExpiryDate());
		ProductCharacteristic productCharacteristics = ProductCharacteristicsHelper.createProductCharacteristic(ProductCharacteristics.FEATURE_EXPIRY_DATE.getValue(), 
				productCharacteristicValue);	
		return productCharacteristics;
	}
	
	/*
	 * Convert Service Agreement Info Expiry Date to "YYYY-MM-DD" String 
	 */
	private String dateToString(Date date){
		
		String year = DateUtil.getYear(date);	
		
		//convert 1 to "01", 2 to "02", ...
		String day = DateUtil.getDayOfMonth(date);
		int dayInInt = Integer.parseInt(day.trim());
		if(dayInInt < 10)
			day = "0" + day;		

		//Month starts at 0 for January convert to "01" for Jan and so on
		String month = DateUtil.getMonth(date);
		int monthInInt = Integer.parseInt(month.trim()) + 1;
		month = Integer.toString(monthInInt);
		if(monthInInt < 10)
			month = "0" + month;
		
		return new String(year + "-" + month + "-" + day);
	}
		
	private String getSpecialDestinationNumbers(String phoneNumber,ServiceAgreementInfo serviceAgreement, byte action){
		
		ServiceFeatureInfo[] features = serviceAgreement.getFeatures0(true);
		String[] phoneNumbersFromParam =null;
		for(int i=0; i<(features == null? 0 : features.length); i++){
			if (features[i].getSwitchCode() != null && FeatureInfo.SWITCH_CODE_CALLING_CIRCLE.equals( features[i].getSwitchCode().trim()) ) {
				phoneNumbersFromParam =features[i].getCallingCirclePhoneNumbersFromParam();
				break;
			}
			if (features[i].getSwitchCode() != null && FeatureInfo.SWITCH_CODE_CALL_HOME_FREE.equals(features[i].getSwitchCode().trim())) {
				phoneNumbersFromParam =	features[i].getCallingCirclePhoneNumbersFromParam();
				break;
			}
		}
		StringBuffer phoneNumberString=new StringBuffer("");

		for(int i=0; i<(phoneNumbersFromParam == null? 0 : phoneNumbersFromParam.length); i++){	
			if(i!=phoneNumbersFromParam.length-1)
				phoneNumberString.append(phoneNumbersFromParam[i]+",");
			else 
				phoneNumberString.append(phoneNumbersFromParam[i]);					
		}
		return phoneNumberString.toString();
		
		
		
	}

	@Override
	public TestPointResultInfo test() {
		return subscriptionServiceClient.test();
	}

}
