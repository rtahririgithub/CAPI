package com.telus.cmb.subscriber.lifecyclehelper.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.prepaid.ProductCharacteristics;
import com.telus.cmb.common.prepaid.SubscriptionServiceClient;
import com.telus.cmb.common.prepaid.SubscriptionServiceMapper;
import com.telus.cmb.subscriber.lifecyclehelper.dao.SubscriptionServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.Product;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductBundle;

public class SubscriptionServiceDaoImpl implements SubscriptionServiceDao{

	@Autowired
	private SubscriptionServiceClient subscriptionServiceClient;

	@Override
	public ServiceAgreementInfo[] retrieveFeatures(String phoneNumber) throws ApplicationException {
		
		ServiceAgreementInfo[] wpsFeatures = new ServiceAgreementInfo[0];
		List<ProductBundle> subscribedProducts = subscriptionServiceClient.getSubscribedProducts(phoneNumber);				
		
		/*
		 * Prepaid WS returns objects as "products" which we need to map to ServiceAgreementInfo
		 * First, there is a default one called "prepaid offer".
		 * The code checks the non-default products and puts them in a list
		 * We go through the list one by one and map the product to service agreement info
		 */
		Product productBundle;
		String productSerialNumber;
		ServiceAgreementInfo serviceAgreementInfo;
		
		List<Integer> index = new ArrayList<Integer>();
		for(int i = 0; i< subscribedProducts.size(); i++){
			productBundle = subscribedProducts.get(i);
			productSerialNumber = productBundle.getProductSerialNumber();
			if (!productSerialNumber.equalsIgnoreCase(ProductCharacteristics.DEFAULT_PREPAID_OFFER.getValue())){
				index.add(i);
			}
		}				

		//Amount of ServiceAgreementInfo to be returned is dependent on non-default "products"
		wpsFeatures = new ServiceAgreementInfo[index.size()];
		Iterator<Integer> indexIteratior = index.iterator(); 
		while (indexIteratior.hasNext()) {
			int i = indexIteratior.next();	
			productBundle = subscribedProducts.get(i);
			serviceAgreementInfo = new ServiceAgreementInfo();
			wpsFeatures[i] = SubscriptionServiceMapper.mapProductBundleToServiceAgreementInfo(productBundle, serviceAgreementInfo);
		}		
						
		return wpsFeatures;
	}

	
	@Override
	public TestPointResultInfo test() {
		return subscriptionServiceClient.test();
	}

}
