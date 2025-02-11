package com.telus.cmb.common.prepaid;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.SubscriptionServicePort;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriptionservice_v2.ChangeProductCharacteristicValue;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriptionservice_v2.GetSubscribedProducts;
import com.telus.tmi.xmlschema.srv.cmo.selfmgmt.subscriptionservice_v2.RemoveSubscribedProduct;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductBundle;
import com.telus.tmi.xmlschema.xsd.product.product.product_types_v2.ProductCharacteristic;

/*
 *  This file is currently not being used as of Jan 2014 (Surepay project).
 *  This client calls the Prepaid WS that is the meant to eventually replace Prepaid API
 *  
 *  When using changeProductCharacteristicValue method, please refer to the enum ProductBundleCharacteristics
 *  for values used.
 */
public class SubscriptionServiceClient extends SoaBaseSvcClient{

	protected SubscriptionServicePort port;
	
	public void setPort(SubscriptionServicePort port) {
		this.port = port;
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				Ping parameters = null;
				return port.ping(parameters).getVersion();
			}
		});
	}
	
	public List<ProductBundle> getSubscribedProducts(final String subscriptionID) throws ApplicationException{
		return execute(new SoaCallback<List<ProductBundle>>() {

			@Override
			public List<ProductBundle> doCallback() throws Exception {
				GetSubscribedProducts parameters = new GetSubscribedProducts();
				parameters.setSubscriptionID(subscriptionID);
				return port.getSubscribedProducts(parameters).getProducts();
			}					
		});
	}
	
	

	public void changeProductCharacteristicValue(final String subscriptionId,final String productSerialNumber,final List<ProductCharacteristic> productCharacteristic)
		throws ApplicationException{
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {
				ChangeProductCharacteristicValue parameters = new ChangeProductCharacteristicValue();
				parameters.setSubscriptionID(subscriptionId);
				parameters.setProductSerialNumber(productSerialNumber);			
				for(ProductCharacteristic o: productCharacteristic)
					parameters.getProductCharacteristic().add(o);										
				port.changeProductCharacteristicValue(parameters);
				return null;
			}
		});
		
	}
	
	
	public void removeSubscribedProduct(final String subscriptionId, final String productSerialNumber) throws ApplicationException{
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {				
				RemoveSubscribedProduct parameters = new RemoveSubscribedProduct();
				parameters.setSubscriptionID(subscriptionId);
				parameters.setProductSerialNumber(productSerialNumber);
				port.removeSubscribedProduct(parameters);
				return null;
			}
		});
	}
	
	

}
