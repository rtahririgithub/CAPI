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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.cmb.jws.mapper.SubscriberInformationServiceMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.AddressMapper;
import com.telus.cmb.jws.mapping.subscriber.information_types_10.SubscriberMapper_10;
import com.telus.cmb.jws.mapping.subscriber.information_types_10.SubscriptionRoleMapper;
import com.telus.eas.account.info.AddressInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.subscriber.info.SubscriptionRoleInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.NDPDirectionIndicator;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.PortOutEligibilityInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.Subscriber;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.SubscriberIdentifierList;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v1.SubscriberMemo;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "SubscriberInformationServicePort", 
		serviceName = "SubscriberInformationService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/SubscriberInformationService_1", 
		wsdlLocation = "/wsdls/SubscriberInformationService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.SubscriberInformationServicePort")
		
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class SubscriberInformationService_10 extends BaseService implements SubscriberInformationServicePort {
	private static final int MAX_SUBSCRIBER_LIST = 1000; 

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0001", errorMessage="Failed to create memo")
	public void createMemo(final String billingAccountNumber, final SubscriberMemo subscriberMemo) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				MemoInfo memoInfo = getSubscriberMemoMapper().mapToDomain(subscriberMemo);
				memoInfo.setBanId(Integer.parseInt(billingAccountNumber));
				if ( Boolean.TRUE.equals( subscriberMemo.isAsyncInd()) ) {
					getAccountLifecycleFacade(context).asyncCreateMemo(memoInfo, context.getAccountLifeCycleFacadeSessionId() );
				} else {
					getAccountLifecycleManager(context).createMemo(memoInfo, context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
		});
		
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0002", errorMessage="Failed to get subscriber identifier list by phone # and account #")
	public SubscriberIdentifierList getSubscriberIdentifierListByPhoneNumberAndAccountNumber(final String billingAccountNumber, final String phoneNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<SubscriberIdentifierList>() {

			@Override
			public SubscriberIdentifierList doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				SubscriberInfo sub = getSubscriberLifecycleHelper(context).retrieveSubscriberByPhoneNumber(Integer.valueOf(billingAccountNumber), phoneNumber);
				SubscriberIdentifierList subIdentifier = new SubscriberIdentifierList();
				subIdentifier.setSubscriberId(sub.getSubscriberId());
				subIdentifier.setSubscriptionId(sub.getSubscriptionId());
				return subIdentifier;
			}
		});
	}

 
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0003", errorMessage="Failed to get subscriber list by account #")
	public List<Subscriber> getSubscriberListByAccountNumber(final String billingAccountNumber) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByBAN(Integer.valueOf(billingAccountNumber), MAX_SUBSCRIBER_LIST);
				return mapSubscriberList(subscriberList, context);
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0004", errorMessage="Failed to get subscriber list by IMSI")
	public List<Subscriber> getSubscriberListByIMSI(final String imsi, final Boolean includeCancelledSubscribersInd) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByImsi(imsi, includeCancelledSubscribersInd.booleanValue());
				return mapSubscriberList(subscriberList, context);
			}
		});
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0005", errorMessage="Failed to get subscriber list by phone #")
	public List<Subscriber> getSubscriberListByPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<List<Subscriber>>() {

			@Override
			public List<Subscriber> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				Collection<SubscriberInfo> subscriberList = getSubscriberLifecycleHelper(context).retrieveSubscriberListByPhoneNumber(phoneNumber, MAX_SUBSCRIBER_LIST, true);
				return mapSubscriberList(subscriberList, context);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0006", errorMessage="Failed to set subscriber port in indicator")
	public void updatePortInSubscriber(final String phoneNumber) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				getSubscriberLifecycleManager(context).setSubscriberPortIndicator(phoneNumber, context.getSubscriberLifecycleManagerSessionId());
				return null;
			}
		});		
	}	 
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0007", errorMessage="Failed to snap back phone number")
	public void updateSnapbackPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
			execute(new ServiceInvocationCallback<Object>(){
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
					getSubscriberLifecycleManager(context).snapBack(phoneNumber,context.getSubscriberLifecycleManagerSessionId());
					return null;
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_SIS_0008", errorMessage="Failed to check subscriber port out eligibility")
	public PortOutEligibilityInfo checkPortOutEligibility(final String phoneNumber, final NDPDirectionIndicator ndpDirectionIndicator) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<PortOutEligibilityInfo>(){
			@Override
			public PortOutEligibilityInfo doInInvocationCallback(ServiceInvocationContext context) throws Throwable {  
				
 					com.telus.eas.portability.info.PortOutEligibilityInfo portOutEligibInfo = 
							getSubscriberLifecycleHelper(context).checkSubscriberPortOutEligibility(phoneNumber, ndpDirectionIndicator.value());				
					return getSubscriberPortOutEligibilityInfoMapper().mapToSchema(portOutEligibInfo);
			}
		});	
	}
	
	private Subscriber mapSubscriber (SubscriberInfo subscriberInfo, ServiceInvocationContext context) throws ApplicationException, Exception {
		Subscriber wsSubscriber = SubscriberMapper_10.getInstance().mapToSchema(subscriberInfo);
		
		SubscriptionRoleInfo subscriptionRoleInfo = getSubscriberLifecycleHelper(context).retrieveSubscriptionRole(subscriberInfo.getPhoneNumber());
		wsSubscriber.setSubscriptionRole(SubscriptionRoleMapper.getInstance().mapToSchema(subscriptionRoleInfo));
		
		AddressInfo addressInfo = getSubscriberLifecycleHelper(context).retrieveSubscriberAddress(subscriberInfo.getBanId(), subscriberInfo.getSubscriberId());
		wsSubscriber.setAddress(AddressMapper.getInstance().mapToSchema(addressInfo));
		
		return wsSubscriber;
	}
	
	private List<Subscriber> mapSubscriberList (Collection<SubscriberInfo> subscriberInfoList, ServiceInvocationContext context) throws ApplicationException, Exception {
		int i = 0;
		List<Subscriber> respSubscriberList = new ArrayList<Subscriber>();
		for (SubscriberInfo subscriberInfo : subscriberInfoList) {
			if (i >= MAX_SUBSCRIBER_LIST) {
				break;
			}
			respSubscriberList.add(mapSubscriber (subscriberInfo, context));
		}
		
		return respSubscriberList;
	}
	
	private SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper getSubscriberPortOutEligibilityInfoMapper() {
		return SubscriberInformationServiceMapper.SubscriberPortOutEligibilityInfoMapper();		
	}
	private SubscriberInformationServiceMapper.SubscriberMemoMapper getSubscriberMemoMapper() {
		return SubscriberInformationServiceMapper.SubscriberMemoMapper();
	}
}
