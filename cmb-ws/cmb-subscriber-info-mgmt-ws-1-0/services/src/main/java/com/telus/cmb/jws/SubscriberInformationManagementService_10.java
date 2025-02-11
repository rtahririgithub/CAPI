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

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.jws.DeletePhoneDirectoryEntriesRequest;
import com.telus.cmb.jws.DeletePhoneDirectoryEntriesResponse;
import com.telus.cmb.jws.GetPhoneDirectoryRequest;
import com.telus.cmb.jws.GetPhoneDirectoryResponse;
import com.telus.cmb.jws.UpdatePhoneDirectoryRequest;
import com.telus.cmb.jws.UpdatePhoneDirectoryResponse;
import com.telus.tmi.xmlschema.xsd.customer.selfmgmt.subscriber_info_management_types_1_0.PhoneDirectoryEntry;

/**
 * @author Pavel Simonovsky
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberInformationManagementServicePort", 
		serviceName = "SubscriberInformationManagementService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/SelfMgmt/SubscriberInformationManagementService_1", 
		wsdlLocation = "/wsdls/SubscriberInformationManagementService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberInformationManagementServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberInformationManagementService_10 extends BaseService implements SubscriberInformationManagementServicePort {

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.selfmgmt.subscriberinformationmanagementservice_1_0.SubscriberInformationManagementServicePortType#getPhoneDirectory(com.telus.cmb.jws.GetPhoneDirectoryRequest)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIMS_0001")
	public GetPhoneDirectoryResponse getPhoneDirectory(final GetPhoneDirectoryRequest request) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<GetPhoneDirectoryResponse>() {
			
			@Override
			public GetPhoneDirectoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetPhoneDirectoryResponse response = new GetPhoneDirectoryResponse();
				com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry[] pdEntries;				 
				pdEntries = getSubscriberLifecycleHelper(context).getPhoneDirectory(request.getSubscriptionId());					 
				convertDomainToSchema(pdEntries, response.getPhoneDirectoryEntry());
				
				return response;
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.selfmgmt.subscriberinformationmanagementservice_1_0.SubscriberInformationManagementServicePortType#updatePhoneDirectory(com.telus.cmb.jws.UpdatePhoneDirectoryRequest)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIMS_0002")
	public UpdatePhoneDirectoryResponse updatePhoneDirectory(final UpdatePhoneDirectoryRequest request) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<UpdatePhoneDirectoryResponse>() {
			
			@Override
			public UpdatePhoneDirectoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				getSubscriberLifecycleHelper(context).updatePhoneDirectory(request.getSubscriptionId(), convertSchemaToDomain (request.getPhoneDirectoryEntry()));
				return new UpdatePhoneDirectoryResponse();  
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see com.telus.wsdl.cmo.selfmgmt.subscriberinformationmanagementservice_1_0.SubscriberInformationManagementServicePortType#deletePhoneDirectoryEntries(com.telus.cmb.jws.DeletePhoneDirectoryEntriesRequest)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIMS_0003")
	public DeletePhoneDirectoryEntriesResponse deletePhoneDirectoryEntries(final DeletePhoneDirectoryEntriesRequest request) throws PolicyException, ServiceException {
		return execute( new ServiceInvocationCallback<DeletePhoneDirectoryEntriesResponse>() {
			
			@Override
			public DeletePhoneDirectoryEntriesResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				getSubscriberLifecycleHelper(context).deletePhoneDirectoryEntries(request.getSubscriptionId(), convertSchemaToDomain(request.getPhoneDirectoryEntry()));					
				return new DeletePhoneDirectoryEntriesResponse();
			}
		});
	}

	private com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry[] convertSchemaToDomain(List<PhoneDirectoryEntry> pdeList) {
		logger.debug("Converting Schema data to Domain data : " + pdeList.size() + " element(s)");
		com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry[] pdEntries = new com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry[pdeList.size()];
		
		Iterator<PhoneDirectoryEntry> iterator = pdeList.iterator();
		int i = 0;
		while(iterator.hasNext()) {
			PhoneDirectoryEntry pdEntry = iterator.next();
			pdEntries[i] = new com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry();
			pdEntries[i].setPhoneNumber(pdEntry.getPhoneNumber());
			pdEntries[i].setNickName(pdEntry.getNickName());
			if (pdEntry.getEffectiveDate() != null) {
				pdEntries[i].setEffectiveDate(pdEntry.getEffectiveDate());
			}
			i++;
		}
		
		return pdEntries;
	}

	private void convertDomainToSchema(com.telus.cmb.subscriber.lifecyclehelper.domain.PhoneDirectoryEntry[] pdEntries, List<PhoneDirectoryEntry> pdeList) throws Exception {
		logger.debug("Converting Domain data to Schema data : " + pdEntries.length + " element(s)");
		
		DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
		
		for(int i=0; i<pdEntries.length; i++) {

			PhoneDirectoryEntry pdEntry = new PhoneDirectoryEntry();
			pdEntry.setPhoneNumber(pdEntries[i].getPhoneNumber());
			pdEntry.setNickName(pdEntries[i].getNickName());
			pdEntry.setEffectiveDate(pdEntries[i].getEffectiveDate());
			
			pdeList.add(pdEntry);
		}
	}
}
