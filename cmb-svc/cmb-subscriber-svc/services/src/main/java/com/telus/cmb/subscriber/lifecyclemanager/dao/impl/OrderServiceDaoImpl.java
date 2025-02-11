package com.telus.cmb.subscriber.lifecyclemanager.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.cmb.common.prepaid.OrderServiceClient;
import com.telus.cmb.common.prepaid.OrderServiceMapper;
import com.telus.cmb.subscriber.lifecyclemanager.dao.OrderServiceDao;
import com.telus.eas.framework.info.TestPointResultInfo;
import com.telus.eas.subscriber.info.ServiceAgreementInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.base_types_2_0.InteractionStatusType;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.CustomerOrderItem;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.ProductOrder;

public class OrderServiceDaoImpl implements OrderServiceDao {

	@Autowired
	private OrderServiceClient orderServiceWSClient;
	
	private final String errorMessage = "Activate Feature from OrderService returned a status failed."; 
	private final String errorMessageFr = "";
	
	private final Logger LOGGER = Logger.getLogger(OrderServiceDaoImpl.class);
	
	@Override
	public void activateFeatureForPrepaidSubscriber(String phoneNumber,	ServiceAgreementInfo serviceAgreementInfo)
			throws ApplicationException {

		if (serviceAgreementInfo != null) {
			LOGGER.debug("ActivateFeatureForPrepaidSubscriber with PhoneNumber: " +phoneNumber + " and service code " + serviceAgreementInfo.getCode());
			ProductOrder productOrder = new ProductOrder();
			//Note: Although productOrder.getCustomerOrderItems() support a list of CustomerOrderItems, it actually support one CustomerOrderItem per transaction only.
			productOrder.getCustomerOrderItems().add(OrderServiceMapper.mapServiceAgreementInfoToCustomerOrderItem(serviceAgreementInfo));
			createSubscriptionProductOrder(phoneNumber,productOrder);
		}
	}
	
	private void createSubscriptionProductOrder(String phoneNumber, ProductOrder productOrder) throws ApplicationException{
		/*
		 *  This specific error message is thrown when a subscriber attempts to activate
		 *  a feature but we get a "failed" status code (non-application exception) from
		 *  the web service. IE: subscriber does not have enough balance.
		 */
		List<CustomerOrderItem> customerOrderItems = orderServiceWSClient.createSubscriptionProductOrder(phoneNumber, productOrder);	
		for(CustomerOrderItem o: customerOrderItems){
			if(o.getStatus().equals(InteractionStatusType.FAILED)){
				throw new ApplicationException(SystemCodes.PREPAID, errorMessage + " for Subscriber with Phone Number: " + phoneNumber, errorMessageFr);
			}
		}
	}
	
	@Override
	public TestPointResultInfo test() {
		return orderServiceWSClient.test();
	}
	
}
