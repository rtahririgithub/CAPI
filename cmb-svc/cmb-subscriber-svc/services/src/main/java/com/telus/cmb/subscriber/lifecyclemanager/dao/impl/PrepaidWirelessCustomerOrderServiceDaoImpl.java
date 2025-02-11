package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.prepaid.PrepaidWirelessCustomerOrderServiceClient;
import com.telus.cmb.common.prepaid.ProductCharacteristicsHelper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.PrepaidWirelessCustomerOrderServiceDao;
import com.telus.eas.account.info.ActivationFeaturesPurchaseArrangementInfo;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.common_domain_types_3.ProductStatusType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.CustomerOrderItem;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.ProductOrder;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductComponent;

public class PrepaidWirelessCustomerOrderServiceDaoImpl implements PrepaidWirelessCustomerOrderServiceDao {

	@Autowired
	PrepaidWirelessCustomerOrderServiceClient pwcosClient;
	
	public void saveActivationFeaturesPurchaseArrangement(
			SubscriberInfo subscriberInfo,
			ActivationFeaturesPurchaseArrangementInfo[] featuresPurchaseList,
			String user) throws ApplicationException {
		
		ProductOrder productOrder = new ProductOrder();
		productOrder.setSalesAgentEmployeeID(user);
		ProductCharacteristic productCharacteristic = null;
		if (featuresPurchaseList != null) {
		for (int i = 0; i < featuresPurchaseList.length; i++) {
			CustomerOrderItem customerOrderItem=new CustomerOrderItem();
			ProductComponent productComponent=new ProductComponent();
			productComponent.setProductStatus(ProductStatusType.fromValue("pending"));
			productComponent.setProductSerialNumber(featuresPurchaseList[i].getFeatureId());
			productCharacteristic = ProductCharacteristicsHelper.createProductCharacteristic("AutoRenewIndicator", "AutoRenewIndicator", featuresPurchaseList[i].getAutoRenewIndicator());	
			productComponent.getProductCharacteristics().add(productCharacteristic);
			productCharacteristic = ProductCharacteristicsHelper.createProductCharacteristic("InitialPurchaseFundSouce", "InitialPurchaseFundSouce", String.valueOf(featuresPurchaseList[i].getPurchaseFundSource()));
			productComponent.getProductCharacteristics().add(productCharacteristic);
			productCharacteristic = ProductCharacteristicsHelper.createProductCharacteristic("FeatureRenewalFundSouce", "FeatureRenewalFundSouce", String.valueOf(featuresPurchaseList[i].getAutoRenewFundSource()));
			productComponent.getProductCharacteristics().add(productCharacteristic);
			customerOrderItem.setProduct(productComponent);
			productOrder.getCustomerOrderItems().add(customerOrderItem);
		}
	}	
		pwcosClient.addProductOrder(String.valueOf(subscriberInfo.getBanId()), 
				subscriberInfo.getPhoneNumber(), subscriberInfo.getSerialNumber(), productOrder);
	}
	
	@Override
	public TestPointResultInfo test() {
		return pwcosClient.test();
	}
}
