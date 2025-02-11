/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.reference.customer_order_10.SalesRepMapper;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.customer_order_reference_types_1_0.SalesRep;

/**
 * @author Pavel Simonovsky
 *
 */

@WebService(
		portName = "CustomerOrderReferenceServicePort", 
		serviceName = "CustomerOrderReferenceService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/CustomerOrderReferenceService_1", 
		wsdlLocation = "/wsdls/CustomerOrderReferenceService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.CustomerOrderReferenceServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class CustomerOrderReferenceService_10 extends BaseService implements CustomerOrderReferenceServicePort {
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.ordermgmt.customerorderreferenceservice_1_0.CustomerOrderReferenceServicePortType#getDealerSalesRep(java.lang.String, java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_CORS_0001")
	public SalesRep getDealerSalesRep(final String dealerCode, final String salesRepCode) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<SalesRep>() {
			
			@Override
			public SalesRep doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				return new SalesRepMapper().mapToSchema(getCustomerOrderReferenceFacade().getDealerSalesRep(dealerCode, salesRepCode));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.jws.reference.ReferenceService#enumerateRuntimeResources(java.util.Map)
	 */
	@Override
	protected Map<String, Object> enumerateRuntimeResources(Map<String, Object> resources) {
		resources.put("CustomerOrderReferenceFacade", getCustomerOrderReferenceFacade());
		return super.enumerateRuntimeResources(resources);
	}

}
