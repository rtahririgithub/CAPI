package com.telus.cmb.common.prepaid;

import java.util.Date;

import org.apache.log4j.Logger;

import com.telus.api.reference.ServiceSummary;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.ActionType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.Quantity;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.TimePeriod;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.CustomerOrderItem;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductComponent;

public class OrderServiceMapper {

	private static final Logger LOGGER = Logger.getLogger(OrderServiceMapper.class);

	public static CustomerOrderItem mapServiceAgreementInfoToCustomerOrderItem(ServiceAgreementInfo serviceAgreementInfo){

		CustomerOrderItem customerOrderItem = new CustomerOrderItem();
		customerOrderItem.setObjectID(serviceAgreementInfo.getCode().trim());

		//Only add will apply to the current use of the service. 
		customerOrderItem.setAction(ActionType.ADD);

		/*
		 *  Quantity is set to default, ClientAPI and PP WS currently does not support
		 *  multiple quantities for one customerOrderItem. Confirmed with Prepaid team.
		 */
		//TODO: TO CHECK!
		Quantity quantity = new Quantity();
		double quantityVal = Double.parseDouble(ProductCharacteristics.QUANTITY_DEFAULT_AMT.getValue());
		quantity.setAmount(quantityVal);
		quantity.setUnits(ProductCharacteristics.QUANTITY_DEFAULT_UNITS.getValue());                         
		customerOrderItem.setQuantity(quantity);        

		ProductComponent product = mapServiceAgreementInfoToProductComponent(serviceAgreementInfo);
		customerOrderItem.setProduct(product);

		return customerOrderItem;
	}


	public static ProductComponent mapServiceAgreementInfoToProductComponent(ServiceAgreementInfo serviceAgreementInfo){

		//TODO: Add loggers to methods before this
		LOGGER.debug("Method: mapServiceAgreementInfoToProductComponent For Service Code: " + serviceAgreementInfo.getCode().trim() +
			"\n AutoRenew: " + serviceAgreementInfo.getPurchaseFundSource() +
			"\n PurchaseFundSource: " + serviceAgreementInfo.getPurchaseFundSource());

		ProductComponent product = new ProductComponent();
		product.setProductSerialNumber(serviceAgreementInfo.getCode().trim());

		//Date value is ignored by WS but is mandatory (passes dummy value)

		TimePeriod validFor = new TimePeriod(); //put into variable
		Date currentDate = new Date(); 
		validFor.setStartDateTime(currentDate);
		validFor.setEndDateTime(currentDate);
		product.setValidFor(validFor);

		ProductCharacteristic autoRenewProductCharacteristics = 
				ProductCharacteristicsHelper.createAutoRenewProductCharacteristic(serviceAgreementInfo.getAutoRenew());
		product.getProductCharacteristics().add(autoRenewProductCharacteristics);

		//if autoRenew is true, there is a autoRenewType
		if(serviceAgreementInfo.getAutoRenew()){

			//if AutoRenewFundSource is not the default value
			if(!(serviceAgreementInfo.getAutoRenewFundSource() == ServiceSummary.AUTORENEW_NOT_DEFINED)){
				ProductCharacteristic renewTypeProductCharacteristics = 
						ProductCharacteristicsHelper.createFundSourceBasedProductCharacteristic(
								ProductCharacteristics.AUTO_RENEW_FUND_SOURCE_INDICATOR.getValue(), 
								ProductCharacteristics.AUTO_RENEW_FUND_SOURCE_INDICATOR.getValue(), 
								serviceAgreementInfo.getAutoRenewFundSource());
				product.getProductCharacteristics().add(renewTypeProductCharacteristics);                                     
			}                                     
		}
		
		/*
		 *  Defect#26169 Prepaid Issue
		 *  Root cause: The retrieval process for a prepaid subscriber is incorrectly setting Purchase Fund Source
		 *  As part of the solution, we will change Purchase Type to send whatever we get from frontends. No logic
		 *  will be performed on Purchase Fund Source.
		 */
		
		ProductCharacteristic purchaseTypeProductCharacteristics = ProductCharacteristicsHelper.createFundSourceBasedProductCharacteristic(
				ProductCharacteristics.PURCHASE_TYPE_INDICATOR.getValue(), 
				ProductCharacteristics.PURCHASE_TYPE_INDICATOR.getValue(), 
				serviceAgreementInfo.getPurchaseFundSource());

		//TODO: (Optional) Log or display Product or ProductCharacteristics
		product.getProductCharacteristics().add(purchaseTypeProductCharacteristics);

		return product;
	}
}
