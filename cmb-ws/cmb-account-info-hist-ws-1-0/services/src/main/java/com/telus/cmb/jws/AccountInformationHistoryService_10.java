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

import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.cmb.account.informationhelper.svc.AccountInformationHelper;
import com.telus.cmb.jws.mapper.AccountStatusChangeMapper;
import com.telus.cmb.jws.mapper.PaymentMethodChangeMapper;
import com.telus.eas.account.info.AddressHistoryInfo;
import com.telus.eas.account.info.PaymentMethodChangeHistoryInfo;
import com.telus.eas.account.info.StatusChangeHistoryInfo;
import com.telus.cmb.jws.AccountStatusChangeHistoryList;
import com.telus.cmb.jws.AccountStatusChangeHistoryResult;
import com.telus.cmb.jws.AddressChangeHistory;
import com.telus.cmb.jws.AddressChangeHistoryList;
import com.telus.cmb.jws.AddressChangeHistoryResult;
import com.telus.cmb.jws.GetAccountStatusChangeHistory;
import com.telus.cmb.jws.GetAccountStatusChangeHistoryResponse;
import com.telus.cmb.jws.GetAddressChangeHistory;
import com.telus.cmb.jws.GetAddressChangeHistoryResponse;
import com.telus.cmb.jws.GetPaymentMethodChangeHistory;
import com.telus.cmb.jws.GetPaymentMethodChangeHistoryResponse;
import com.telus.cmb.jws.PaymentMethodChangeHistoryList;
import com.telus.cmb.jws.PaymentMethodChangeHistoryResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v5.Address;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;


@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "AccountInformationHistoryServicePort", serviceName = "AccountInformationHistoryService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/InformationMgmt/AccountInformationHistoryService_1", 
		wsdlLocation = "/wsdls/AccountInformationHistoryService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.AccountInformationHistoryServicePort")
		@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")                      
		public class AccountInformationHistoryService_10 extends BaseServiceV2 implements AccountInformationHistoryServicePort {

	private static final int MAX_OCCUR = 1000;

	public AccountInformationHistoryService_10() {
		super(new AccountInformationHistoryExceptionTranslator());
	}

	@ServiceBusinessOperation(errorCode = "CMB_AHS_0001", errorMessage = "Account status change retrieval error")
	@Override
	public GetAccountStatusChangeHistoryResponse getAccountStatusChangeHistory(final GetAccountStatusChangeHistory parameters) throws ServiceException {

			return execute(new ServiceInvocationCallback<GetAccountStatusChangeHistoryResponse>() {
				@Override
				public GetAccountStatusChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
					GetAccountStatusChangeHistoryResponse ret = new GetAccountStatusChangeHistoryResponse();
					if (context.getSchemaValidationException() != null) {
						return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
					}
					String ban = parameters.getBillingAccountNumber();
					Date fromDate = new Date();
					fromDate = parameters.getFromDate();
					Date to = parameters.getToDate();
					int banInteger = new Integer(ban).intValue();
					AccountInformationHelper accountInformationHelper = getAccountInformationHelper(context);

					List<StatusChangeHistoryInfo> statusChangeHistoryInfoList = accountInformationHelper.retrieveStatusChangeHistory(banInteger, fromDate, to);
					AccountStatusChangeHistoryResult accountStatusChangeHistoryResult = new AccountStatusChangeHistoryResult();

					if (statusChangeHistoryInfoList.size() > MAX_OCCUR) {
						// returns first MAX_OCCUR elements
						statusChangeHistoryInfoList = statusChangeHistoryInfoList.subList(0, MAX_OCCUR - 1);
					}

					accountStatusChangeHistoryResult.setBillingAccountNumber(String.valueOf(banInteger));

					AccountStatusChangeMapper accountStatusChangeMapper = new AccountStatusChangeMapper();
					AccountStatusChangeHistoryList accountStatusChangeHistoryList = new AccountStatusChangeHistoryList();
					accountStatusChangeHistoryList.getAccountStatusChangeHistory().addAll(accountStatusChangeMapper.mapToSchema(statusChangeHistoryInfoList));
					accountStatusChangeHistoryResult.setHistoryList(accountStatusChangeHistoryList);

					ret.setAccountStatusChangeHistoryResult(accountStatusChangeHistoryResult);
					return ret;
				}
			});
	}

	@ServiceBusinessOperation(errorCode = "CMB_AHS_0002", errorMessage = "Account payment method change retrieval error")
	@Override
	public GetPaymentMethodChangeHistoryResponse getPaymentMethodChangeHistory(GetPaymentMethodChangeHistory parameters) throws ServiceException {
		String ban = parameters.getBillingAccountNumber();
		final Date fromDate = parameters.getFromDate();
		final Date toDate = parameters.getToDate();
		final int banInteger = new Integer(ban).intValue();

		return execute(new ServiceInvocationCallback<GetPaymentMethodChangeHistoryResponse>() {
			@Override
			public GetPaymentMethodChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetPaymentMethodChangeHistoryResponse ret = new GetPaymentMethodChangeHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}

				List<PaymentMethodChangeHistoryInfo> paymentMethodChangeHistoryInfo = getAccountInformationHelper(context).retrievePaymentMethodChangeHistory(banInteger, fromDate, toDate);

				if (paymentMethodChangeHistoryInfo.size() > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					paymentMethodChangeHistoryInfo = paymentMethodChangeHistoryInfo.subList(0, MAX_OCCUR - 1);
				}

				PaymentMethodChangeHistoryResult paymentMethodChangeHistoryResult = new PaymentMethodChangeHistoryResult();
				paymentMethodChangeHistoryResult.setBillingAccountNumber(String.valueOf(banInteger));
				PaymentMethodChangeMapper paymentMethodChangeMapper = new PaymentMethodChangeMapper();
				PaymentMethodChangeHistoryList paymentMethodChangeHistoryList = new PaymentMethodChangeHistoryList();
				paymentMethodChangeHistoryList.getPaymentMethodChangeHistory().addAll(paymentMethodChangeMapper.mapToSchema(paymentMethodChangeHistoryInfo));
				paymentMethodChangeHistoryResult.setHistoryList(paymentMethodChangeHistoryList);
				// paymentMethodChangeHistoryResult.setHistoryList(mapPaymentMethodChangeHistoryList(paymentMethodChangeHistoryInfo));

				ret.setPaymentMethodChangeHistoryResult(paymentMethodChangeHistoryResult);

				return ret;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_AHS_0003", errorMessage = "Account address change history retrieval error")
	@Override
	public GetAddressChangeHistoryResponse getAddressChangeHistory(GetAddressChangeHistory parameters) throws ServiceException {
		String ban = parameters.getBillingAccountNumber();
		final Date from = parameters.getFromDate();
		final Date to = parameters.getToDate();
		final int b = new Integer(ban).intValue();
		return execute(new ServiceInvocationCallback<GetAddressChangeHistoryResponse>() {
			@Override
			public GetAddressChangeHistoryResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				GetAddressChangeHistoryResponse ret = new GetAddressChangeHistoryResponse();
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(ret, context.getSchemaValidationException());
				}
				AddressHistoryInfo[] addressHistoryInfo = getAccountInformationHelper(context).retrieveAddressHistory(b, from, to);

				AddressHistoryInfo[] addressHistoryInfo1000;

				if (addressHistoryInfo.length > MAX_OCCUR) {
					// return first MAX_OCCUR elements
					addressHistoryInfo1000 = new AddressHistoryInfo[MAX_OCCUR];
					System.arraycopy(addressHistoryInfo, 0, addressHistoryInfo1000, 0, MAX_OCCUR);
				} else {
					addressHistoryInfo1000 = addressHistoryInfo;
				}

				AddressChangeHistoryResult addressChangeHistoryResult = new AddressChangeHistoryResult();
				addressChangeHistoryResult.setBillingAccountNumber(String.valueOf(b));
				addressChangeHistoryResult.setHistoryList(mapAddressChangeHistoryList(addressHistoryInfo1000));
				
				ret.setAddressChangeHistoryResult(addressChangeHistoryResult);
				return ret;
			}
		});

	}
	/*
	@ServiceBusinessOperation(errorCode = "CMB_AHS_0004", errorMessage = "Ping error")
	@Override
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.Ping parameters) throws ServiceException {
		PingResponse pingResponse = new PingResponse();
		PingStats pingStats = new PingStats();
		pingStats.setServiceName("AccountInfoHistoryService v1.0");
		pingResponse.setPingStats(pingStats);
		return pingResponse;
	}*/
	
	@ServiceBusinessOperation(errorCode = "CMB_AHS_0004", errorMessage = "Ping error")
	@Override
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping parameters) throws ServiceException {
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse response = new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse();
		response.setVersion("AccountInformationHistoryService 1.0");
		return response;
	}


	private AddressChangeHistoryList mapAddressChangeHistoryList(AddressHistoryInfo[] addressHistoryInfo) {
		AddressChangeHistoryList addressChangeHistoryList = new AddressChangeHistoryList();

		for (AddressHistoryInfo addressHistoryInfoDomain : addressHistoryInfo) {
			AddressChangeHistory addressChangeHistory = new AddressChangeHistory();
			Address address = new Address();
			address = com.telus.cmb.jws.mapper.AddressMapper.getInstance().mapToSchema(addressHistoryInfoDomain.getAddress0());
			addressChangeHistory.setAddress(address);
			addressChangeHistory.setEffectiveDate(addressHistoryInfoDomain.getEffectiveDate());
			addressChangeHistory.setExpirationDate(addressHistoryInfoDomain.getExpirationDate());
			addressChangeHistoryList.getAddressChangeHistory().add(addressChangeHistory);
		}

		return addressChangeHistoryList;

	}

	private <T extends ResponseMessage> T schemaValidationErrorToResponse(T response, Exception exception) {
		response.setErrorCode(SCHEMA_VALIDATION_ERROR_CODE);
		response.setMessageType("ERROR");
		Message msg = new Message();
		msg.setMessage(exception.getMessage());
		response.getMessageList().add(msg);

		return response;
	}
			
}
