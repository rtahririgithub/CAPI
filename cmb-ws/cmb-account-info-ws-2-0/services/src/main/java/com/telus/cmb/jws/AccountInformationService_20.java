/*
 *  Copyright (c) 2010 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.api.ApplicationException;
import com.telus.cmb.jws.mapping.account_information_10.AccountMapper;
import com.telus.cmb.jws.mapping.account_information_10.InvoicePropertiesMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.ContactDetailInfo;
import com.telus.eas.account.info.InvoicePropertiesInfo;
import com.telus.eas.framework.exception.TelusException;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.Account;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.InvoiceProperties;

/**
 * @author Tsz Chung Tong
 *
 */

@WebService(
		portName = "AccountInformationServicePort", 
		serviceName = "AccountInformationService_v2_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationService_2", 
		wsdlLocation = "/wsdls/AccountInformationService_v2_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.AccountInformationServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class AccountInformationService_20 extends BaseService implements AccountInformationServicePort {

	public AccountInformationService_20() {
		super( new AccountInformationExceptionTranslator());
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0001", errorMessage="Account retrieval error")
	public Account getAccountByAccountNumber(final String ban) throws PolicyException, ServiceException {
		
		return execute( new ServiceInvocationCallback<Account>() {
			
			@Override
			public Account doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int banNumber = Integer.valueOf(ban);
				AccountInfo	accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(banNumber);
				accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(banNumber));
				return new AccountMapper().mapToSchema(accountInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0002", errorMessage="Account retrieval error")
	public Account getAccountByPhoneNumber(final String phoneNumber) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<Account>() {

			@Override
			public Account doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				AccountInfo	accountInfo = getAccountInformationHelper(context).retrieveAccountByPhoneNumber(phoneNumber);
				accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(accountInfo.getBanId()));
				return new AccountMapper().mapToSchema(accountInfo);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0003", errorMessage="Account retrieval error")
	public List<Account> getAccountsByAccountNumbers(final List<String> banNumbers) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback <List<Account>>() {

			@Override
			public List<Account> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				List<Account> accountList = new ArrayList<Account>();
				for (String banNumber : banNumbers){
					int ban = Integer.valueOf(banNumber);
					try {
						AccountInfo	accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(ban);
						accountInfo.setProductSubscriberLists(getAccountInformationHelper(context).retrieveProductSubscriberLists(ban));
						Account account = new AccountMapper().mapToSchema(accountInfo);
						accountList.add(account);
					} catch(TelusException e) {
					  if ( !e.id.equals("APP10004")) // Ban not found error will not be thrown
						throw e;
					}
					catch(ApplicationException e) {
						  if ( !e.getErrorCode().equals("APP10004")) // Ban not found error will not be thrown
							throw e;
					}
				}
				return accountList.size() == 0 ? null : accountList; 
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0004", errorMessage="Account password update error")
	public void updateAccountPassword(final String ban, final String newPassword) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
		        int banNumber = Integer.valueOf(ban);
		        if (banNumber != 0) {
					getAccountLifecycleManager(context).updateAccountPassword(banNumber, newPassword, context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
			
		});
	}

	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0006", errorMessage="Get Email Address error")
	public String getEmailAddress(final String ban) throws PolicyException, ServiceException {
		return execute (new ServiceInvocationCallback<String>() {

			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int accountNumber = Integer.valueOf(ban);
				ContactDetailInfo	contactDetailInfo = getAccountInformationHelper(context).getCustomerContactInfo(accountNumber);
				if (contactDetailInfo == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN);
				}
				return contactDetailInfo.getEmailAddress();
			}
			
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0007", errorMessage="Email Address Update error")
	public void updateEmailAddress(final String ban, final String emailAddress)
			throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				int accountNumber = Integer.valueOf(ban);
				getAccountLifecycleManager(context).updateEmailAddress(accountNumber, emailAddress, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
		});
		
	}

	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0008", errorMessage="Get Invoice Properties Error")
	public InvoiceProperties getInvoiceProperties(final String ban)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<InvoiceProperties>() {

			@Override
			public InvoiceProperties doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				InvoicePropertiesInfo invoiceProperties = getAccountInformationHelper(context).getInvoiceProperties(Integer.valueOf(ban));
				if (invoiceProperties == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_BAN, ServiceErrorCodes.ERROR_DESC_NO_DATA_FOR_BAN);
				}
				return new InvoicePropertiesMapper().mapToSchema(invoiceProperties);
			}
		});
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_AIS_0009", errorMessage="Invoice Properties Update Error")
	public void updateInvoiceProperties(final InvoiceProperties invoiceProperties) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				InvoicePropertiesInfo invProperties = new InvoicePropertiesMapper().mapToDomain(invoiceProperties);
				getAccountLifecycleManager(context).updateInvoiceProperties(invProperties.getBan(), invProperties, context.getAccountLifeCycleManagerSessionId());
				return null;
			}
		});
	}

	
	
}
