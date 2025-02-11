/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.adapter;

import java.util.Date;

import com.telus.cmb.wsclient.EnterpriseConsumerProfileRegistrationPort;
import com.telus.rdm.domain.account.AccountType;
import com.telus.rdm.domain.identityprofile.IdentityProfileRegistrationServiceAdapter;
import com.telus.rdm.domain.shared.Brand;
import com.telus.rdm.domain.shared.Language;
import com.telus.rdm.infrastructure.support.resource.NoOpExceptionTranslator;
import com.telus.rdm.infrastructure.support.resource.ResourceAdapter;
import com.telus.rdm.infrastructure.support.resource.ResourceInvocationCallback;
import com.telus.rdm.infrastructure.support.resource.ResponseMessageAsserter;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewAccount;
import com.telus.tmi.xmlschema.srv.cmo.informationmgmt.enterpriseconsumerprofileregistrationsvcrequestresponse_v1.CreateProfileForNewSubscriber;


/**
 * @author x113300
 *
 */

public class ConsumerProfileRegistrationServiceAdapterImpl extends ResourceAdapter implements IdentityProfileRegistrationServiceAdapter {

	private EnterpriseConsumerProfileRegistrationPort port;
	
	public void setPort(EnterpriseConsumerProfileRegistrationPort port) {
		this.port = port;
	}
	
	public ConsumerProfileRegistrationServiceAdapterImpl() {
		setExceptionTranslator( new NoOpExceptionTranslator());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.rdm.domain.model.consumer.ConsumerProfileRegistrationServiceAdapter#createAccountProfile(int, com.telus.cmb.rdm.domain.model.shared.Brand, com.telus.cmb.rdm.domain.model.account.AccountType, java.lang.String, java.lang.String, java.lang.String, com.telus.cmb.rdm.domain.model.shared.Language, java.util.Date, java.util.Date)
	 */
	@Override
	public void createAccountProfile(int accountId, Brand brand, AccountType type, String firstName, String lastName, String emailAddress, 
			Language language, Date activationDate, Date creationDate) {
		
		final CreateProfileForNewAccount request = new CreateProfileForNewAccount();
		
		request.setAccountNumber(Integer.toString(accountId));
		request.setBrandId(brand.getId());
		request.setAccountTypeCd(type.getPrimary());
		request.setAccountSubTypeCd(type.getSecondary());
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(emailAddress);
		request.setActivationDt(activationDate);
		request.setStartDt(creationDate);
		
		invoke( new ResourceInvocationCallback() {
			
			@Override
			public void doInCallback() throws Exception {
				ResponseMessageAsserter.assertValid(port.createProfileForNewAccount(request));
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see com.telus.cmb.rdm.domain.model.consumer.ConsumerProfileRegistrationServiceAdapter#createSubscriberProfile(int, com.telus.cmb.rdm.domain.model.shared.Brand, com.telus.cmb.rdm.domain.model.account.AccountType, java.lang.String, java.lang.String, java.lang.String, com.telus.cmb.rdm.domain.model.shared.Language, java.lang.String)
	 */
	@Override
	public void createSubscriberProfile(int accountId, Brand brand, AccountType type, String firstName, String lastName,
			String emailAddress, Language language, String phoneNumber) {
		
		final CreateProfileForNewSubscriber request = new CreateProfileForNewSubscriber();
		
		request.setAccountNumber(Integer.toString(accountId));
		request.setBrandId(brand.getId());
		request.setAccountTypeCd(type.getPrimary());
		request.setAccountSubTypeCd(type.getSecondary());
		request.setFirstName(firstName);
		request.setLastName(lastName);
		request.setEmail(emailAddress);
		request.setPhone(phoneNumber);
		
		invoke( new ResourceInvocationCallback() {
			
			@Override
			public void doInCallback() throws Exception {
				ResponseMessageAsserter.assertValid(port.createProfileForNewSubscriber(request));
			}
		});
	}
	
}
