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

import org.jvnet.jaxb2_commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.Account;
import com.telus.api.account.BasePrepaidAccount;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.PostpaidConsumerAccount;
import com.telus.api.account.PrepaidConsumerAccount;
import com.telus.api.account.Subscriber;
import com.telus.cmb.framework.endpoint.EndpointClient;
import com.telus.eas.framework.info.TestPointResultInfo;


/**
 * @author Pavel Simonovsky
 *
 */

public class IdentityProfileServiceImpl extends EndpointClient implements IdentityProfileService {
	
	private static final Logger logger = LoggerFactory.getLogger(IdentityProfileServiceImpl.class);

	@Autowired 
	private IdentityProfileRegistrationPolicy registrationPolicy; 
	
	@Autowired 
	private EnterpriseConsumerProfileRegistrationServiceAdapter serviceAdapter;
	
	public void setRegistrationPolicy(IdentityProfileRegistrationPolicy registrationPolicy) {
		this.registrationPolicy = registrationPolicy;
	}

	public void setServiceAdapter(EnterpriseConsumerProfileRegistrationServiceAdapter serviceAdapter) {
		this.serviceAdapter = serviceAdapter;
	}
	
	@Override
	public void registerConsumerProfile(Account account, IdentityProfileRegistrationOrigin origin) throws ApplicationException {
		if (registrationPolicy.isEligible(account.getAccountType(), account.getAccountSubType())) {
			
			logger.debug("Creating consumer identity profile for account [id = {}, type = {}, subtype = {}]", account.getBanId(), account.getAccountType(), account.getAccountSubType());
			
			ConsumerName name = getConsumerName(account);
			
			serviceAdapter.createAccountProfile(account.getBanId(), account.getBrandId(), account.getAccountType(), 
					account.getAccountSubType(), name.getFirstName(), name.getLastName(), 
					account.getEmail(), account.getLanguage(), null, account.getCreateDate());
		} else {
			
			logger.debug("Account [id = {}, type = {}, subtype = {}] is not eligible for consumer identity profile creation", account.getBanId(), account.getAccountType(), account.getAccountSubType());
		}
	}

	/**
	 * The Account object passing in can be from the light weight account retrieval. Ideally, this method should accept the required parameter values instaed of the whole account object.
	 */
	@Override
	public void registerConsumerProfile(Subscriber subscriber, Account account, IdentityProfileRegistrationOrigin origin) throws ApplicationException {
		char accountType = account.getAccountType();
		char accountSubType = account.getAccountSubType();
		int brandId = account.getBrandId();
		int banId = account.getBanId();
		
		if (registrationPolicy.isEligible(accountType, accountSubType)) {

			logger.debug("Creating consumer identity profile for subscriber [id = {}, account-type = {}, account-subtype = {}]", subscriber.getSubscriberId(), accountType, accountSubType);
			
			ConsumerName contactName = subscriber.getConsumerName();
	
			boolean suppressIndicator = origin == IdentityProfileRegistrationOrigin.SUBSCRIBER_MOVE || origin == IdentityProfileRegistrationOrigin.SUBSCRIBER_TOWN;
			
			String firstName = suppressIndicator ? "" :  contactName.getFirstName();
			String lastName =  suppressIndicator ? "" : contactName.getLastName();
			String emailAddress = suppressIndicator ? "" : subscriber.getEmailAddress();
					
			serviceAdapter.createSubscriberProfile(banId, brandId, accountType, accountSubType, firstName, lastName, emailAddress, subscriber.getLanguage(), subscriber.getPhoneNumber());
		} else {
			logger.debug("Subscriber [id = {}, account-type = {}, account-subtype = {}] is not eligible for consumer identity profile creation", subscriber.getSubscriberId(), accountType, accountSubType);
		}
	}

	private ConsumerName getConsumerName(Account account) {
		
		ConsumerName consumerName = account.getContactName();
		
		if (isEmptyName(consumerName)) {
			if (account instanceof PostpaidConsumerAccount ) {
				consumerName = ((PostpaidConsumerAccount) account).getName();
			} else if (account instanceof BasePrepaidAccount) {
				consumerName = ((BasePrepaidAccount) account).getName();
			}
		}
		
		return consumerName;
	}
	
	private boolean isEmptyName(ConsumerName consumerName) {
		return consumerName == null || (StringUtils.isEmpty(consumerName.getFirstName()) && StringUtils.isEmpty(consumerName.getLastName()));
	}

	@Override
	public TestPointResultInfo testEnterpriseConsumerProfileRegistrationService() {
		final TestPointResultInfo resultInfo = new TestPointResultInfo();
		resultInfo.setTimestamp(new Date());
		try {
			String pingResult = serviceAdapter.ping();
			resultInfo.setResultDetail(pingResult);
			resultInfo.setPass(true);
		} catch (Throwable t) {
			logger.error(t.getMessage(), t);
			resultInfo.setExceptionDetail(t);
			resultInfo.setPass(false);
		}
		return resultInfo;
	}
}
