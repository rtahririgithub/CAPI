package com.telus.cmb.common.prepaid;

import com.telus.api.ApplicationException;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.wsclient.PrepaidWirelessCustomerOrderServicePort;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.AddProductOrder;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.Create;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.CreateResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.GetCreditList;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.GetCreditListResponse;
import com.telus.tmi.xmlschema.srv.cmo.ordermgmt.prepaidwirelesscustomerorderservicerequestresponse_v1.Update;
import com.telus.tmi.xmlschema.xsd.customer.customerorder.order_types_v2.ProductOrder;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

/*
 *  This file is currently not being used as of Jan 2014 (Surepay project).
 *  This client calls the Prepaid WS that is the meant to eventually replace Prepaid API
 */
public class PrepaidWirelessCustomerOrderServiceClient extends SoaBaseSvcClient{

	protected PrepaidWirelessCustomerOrderServicePort port;
	
	public void setPort(PrepaidWirelessCustomerOrderServicePort port) {
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
	
	public void addProductOrder(final String customerId,
			final String subscriptionId,
			final String equipmentSerialNumber,
			final ProductOrder productOrder)
			throws ApplicationException {
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {
				
				AddProductOrder parameters = new AddProductOrder();
				parameters.setProductOrder(productOrder);
				parameters.setCustomerID(customerId);
				parameters.setSubscriptionID(subscriptionId);
				parameters.setEquipmentSerialNumber(equipmentSerialNumber);
				port.addProductOrder(parameters);
				return null;
			}
		});
	}
	
	
	public GetCreditListResponse getCreditList(final GetCreditList parameters) throws ApplicationException {
		return execute(new SoaCallback<GetCreditListResponse>() {
			@Override
			public GetCreditListResponse doCallback() throws Exception {
				return port.getCreditList(parameters);
			}
		});
	}
	
	public CreateResponse create(final Create parameters, final OriginatingUserType createInSoapHdr) throws ApplicationException {
		return execute(new SoaCallback<CreateResponse>() {
			@Override
			public CreateResponse doCallback() throws Exception {
				return port.create(parameters, createInSoapHdr);
			}
		});
	}
	
	public void update(final Update parameters) throws ApplicationException {
		execute(new SoaCallback<Object>() {
			@Override
			public Object doCallback() throws Exception {
				port.update(parameters);
				return null;
			}
		});
	}
}
