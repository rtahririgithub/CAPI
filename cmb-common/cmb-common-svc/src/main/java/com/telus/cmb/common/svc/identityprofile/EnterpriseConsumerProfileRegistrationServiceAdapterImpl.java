/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.svc.identityprofile;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import com.telus.api.ApplicationException;
import com.telus.cmb.framework.endpoint.EndpointClient;
import com.telus.cmb.framework.endpoint.ResponseMessageAsserter;
import com.telus.cmb.framework.resource.NoOpResourceExceptionTranslator;
import com.telus.cmb.framework.resource.ResourceInvocationCallback;
import com.telus.cmb.wsclient.EnterpriseConsumerProfileRegistrationPort;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewAccount;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewAccountResponse;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewSubscriber;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewSubscriberResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;

/**
 * @author Pavel Simonovsky
 *
 */
public class EnterpriseConsumerProfileRegistrationServiceAdapterImpl extends EndpointClient implements EnterpriseConsumerProfileRegistrationServiceAdapter {

	@Autowired 
	private EnterpriseConsumerProfileRegistrationPort port;
	
	public EnterpriseConsumerProfileRegistrationServiceAdapterImpl() {
		setExceptionTranslator( new NoOpResourceExceptionTranslator());
	}
	
	public void setPort(EnterpriseConsumerProfileRegistrationPort port) {
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.svc.identityprofile.EnterpriseConsumerProfileRegistrationServiceAdapter#createAccountProfile(int, int, char, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
	@Override
	public void createAccountProfile(int accountId, int brandId, char accountType, char accountSubType, 
			String firstName, String lastName, String emailAddress, String language, Date activationDate, Date creationDate) throws ApplicationException {

		final CreateProfileForNewAccount request = new CreateProfileForNewAccount();
		
		request.setAccountNumber(Integer.toString(accountId));
		request.setBrandId(brandId);
		request.setAccountTypeCd(Character.toString(accountType));
		request.setAccountSubTypeCd(Character.toString(accountSubType));
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(emailAddress);
		request.setActivationDt(null);
		request.setNotificationLanguage(language);
		request.setStartDt(creationDate);

		invoke( new ResourceInvocationCallback() {
			
			@Override
			public void doInCallback() throws Exception {
				
				CreateProfileForNewAccountResponse response = port.createProfileForNewAccount(request);
				ResponseMessageAsserter.assertValid(response);
			}
			
		}, "0001", "ECPRS Adapter", "ECPRS", "IdentityManagement");
	}

	/* (non-Javadoc)
	 * @see com.telus.cmb.common.svc.identityprofile.EnterpriseConsumerProfileRegistrationServiceAdapter#createSubscriberProfile(int, int, char, char, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createSubscriberProfile(int accountId, int brandId, char accountType, char accountSubType, 
			String firstName, String lastName, String emailAddress, String language, String phoneNumber) throws ApplicationException {

		final CreateProfileForNewSubscriber request = new CreateProfileForNewSubscriber();
		
		request.setAccountNumber(Integer.toString(accountId));
		request.setBrandId(brandId);
		request.setAccountTypeCd(Character.toString(accountType));
		request.setAccountSubTypeCd(Character.toString(accountSubType));
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(emailAddress);
		request.setPhone(phoneNumber);
		
		invoke( new ResourceInvocationCallback() {
			
			@Override
			public void doInCallback() throws Exception {
				
				CreateProfileForNewSubscriberResponse response = port.createProfileForNewSubscriber(request);
				ResponseMessageAsserter.assertValid(response);
			}
			
		}, "0002", "ECPRS Adapter", "ECPRS", "IdentityManagement");
	}
	
	public String ping() throws ApplicationException {
		invoke(new ResourceInvocationCallback() {
			@Override
			public void doInCallback() throws Exception {
				Ping parameters = null;
				PingStats pingStatus = port.ping(parameters).getPingStats();
				String serviceName = pingStatus.getServiceName();
			}
		}, "0003", "ECPRS Adapter", "ECPRS", "IdentityManagement");

		return "EnterpriseConsumerProfileRegistrationService Call is Successful";
	}
}
