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

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.telus.cmb.jws.mapping.account_information_10.AccountMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.account_information_types_1.Account;

/**
 * @author Dimitry Siganevich
 * 
 */

@WebService(
		portName = "AccountLifeCycleManagementServicePort", 
		serviceName = "AccountLifeCycleManagementService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/OrderMgmt/AccountLifeCycleManagementService_1", 
		wsdlLocation = "/wsdls/AccountLifeCycleManagementService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.AccountLifeCycleManagementServicePort")
@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")
public class AccountLifeCycleManagementService_10 extends BaseService implements AccountLifeCycleManagementServicePort {

	public AccountLifeCycleManagementService_10() {
		super( new AccountLifeCycleManagementExceptionTranslator());
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_ALCMS_0001", errorMessage="Account creation error")
	public String createAccount(final Account account) throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback<String>() {

			@Override
			public String doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				AccountInfo accountInfo = new AccountMapper().mapToDomain(account);
				accountInfo.setBanId(0);
				int banNumber = getAccountLifecycleManager(context).createAccount(accountInfo, context.getAccountLifeCycleManagerSessionId());
				return String.valueOf(banNumber);
			}
		});

	}
	

}
