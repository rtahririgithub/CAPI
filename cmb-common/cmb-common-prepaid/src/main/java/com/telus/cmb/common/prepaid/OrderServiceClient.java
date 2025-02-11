package com.telus.cmb.common.prepaid;

import java.util.List;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.OrderServicePort;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.orderservice_v2.CreateSubscriptionProductOrder;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.orderservice_v2.CreateSubscriptionProductOrderResponse;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.CustomerOrderItem;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.ProductOrder;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

/*
 *  This file is currently not being used as of Jan 2014 (Surepay project).
 *  This client calls the Prepaid WS that is the meant to eventually replace Prepaid API
 */
public class OrderServiceClient extends SoaBaseSvcClient{
	
	protected OrderServicePort port;
	
	public void setPort(OrderServicePort port) {
		this.port = port;
	}
	
	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				// TODO: ping
				return port.ping( new Ping()).getVersion();
			}
		});
	}
	
	public List<CustomerOrderItem> createSubscriptionProductOrder(final String subscriptionId, final ProductOrder productOrder) throws ApplicationException{
		return execute(new SoaCallback<List<CustomerOrderItem>>() {
			@Override
			public List<CustomerOrderItem> doCallback() throws Exception {
				CreateSubscriptionProductOrder parameters = new CreateSubscriptionProductOrder();
				parameters.setProductOrder(productOrder);
				parameters.setSubscriptionID(subscriptionId);		 			
				CreateSubscriptionProductOrderResponse createSubscriptionProductOrderResponse = port.createSubscriptionProductOrder(parameters);
				List<CustomerOrderItem> customerOrderItems = createSubscriptionProductOrderResponse.getProductOrder().getCustomerOrderItems();
				return customerOrderItems;
			}					
		});
	}
}
