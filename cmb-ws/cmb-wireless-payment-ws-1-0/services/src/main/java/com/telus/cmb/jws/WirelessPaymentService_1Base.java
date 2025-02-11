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

import org.apache.commons.lang.StringUtils;

import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.cmb.jws.mapper.WirelessPaymentMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v9.AuditInfoMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreditCardTransaction;
import com.telus.cmb.jws.ProcessCreditCard;
import com.telus.cmb.jws.ProcessCreditCardResponse;
import com.telus.cmb.jws.ProcessDeposit;
import com.telus.cmb.jws.ProcessDepositResponse;
import com.telus.cmb.jws.ProcessPayment;
import com.telus.cmb.jws.ProcessPaymentResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v2.PingStats;

/**
 * @author R. Fong
 *
 */
public class WirelessPaymentService_1Base extends BaseServiceV2 implements WirelessPaymentServicePort {
	
	public WirelessPaymentService_1Base() {
		super(new WirelessPaymentExceptionTranslator());
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WPS_0001", errorMessage = "Process credit card error")
	public ProcessCreditCardResponse processCreditCard(final ProcessCreditCard parameters, final OriginatingUserType inputSoapHeader) throws ServiceException {
		
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("processCreditCard", "CMB_WPS_0001", "Process credit card error");
		
		return execute(new ServiceInvocationCallback<ProcessCreditCardResponse>() {
			
			@Override
			public ProcessCreditCardResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ProcessCreditCardResponse response = new ProcessCreditCardResponse();
				
				// Validate the schema and SOAP headers
				if (context.getSchemaValidationException() != null) {
					throw new ServicePolicyException(SCHEMA_VALIDATION_ERROR_CODE, context.getSchemaValidationException().getMessage());
				}
				if (inputSoapHeader == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				
				// Extract the parameters
				CreditCardTransaction creditCardTransaction = parameters.getCreditCardTransaction();
				
				// Validate the credit card transaction type for the educational benefit of our consumers, although we actually override this value in the AccountLifecycleFacade EJB 
				if (!StringUtils.equalsIgnoreCase(creditCardTransaction.getTransactionType(), CreditCardTransactionInfo.TYPE_CREDIT_CARD_VALIDATION)) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_CREDIT_CARD_TRANSACTION_TYPE, "Credit card transaction type [" + creditCardTransaction.getTransactionType() 
							+ "] is not supported");
				}

				// Map the credit card transaction information
				CreditCardTransactionInfo creditCardTransactionInfo = WirelessPaymentMapper.getCreditCardTransactionMapper().mapToDomain(creditCardTransaction);

				// Map the credit card information
				CreditCardInfo cardInfo = WirelessPaymentMapper.getCreditCardMapper().mapToDomain(creditCardTransaction.getCreditCard());
				if (cardInfo == null || cardInfo.hasToken() == false) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
				}
				
				// Map the audit header information
				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(inputSoapHeader);
				auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());
				creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);				

				// Make the call to the AccountLifecycleFacade EJB
				String authorizationNumber = getAccountLifecycleFacade(context).validateCreditCard(creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId()).getAuthorizationCode();
				response.setAuthorizationNumber(authorizationNumber);

				return response;
			}
			
		}, new ProcessCreditCardResponse(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WPS_0002", errorMessage = "Process payment error")
	public ProcessPaymentResponse processPayment(final ProcessPayment parameters, final OriginatingUserType inputSoapHeader) throws ServiceException {
		
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("processPayment", "CMB_WPS_0002", "Process payment error");
		
		return execute(new ServiceInvocationCallback <ProcessPaymentResponse>() {
			
			@Override
			public ProcessPaymentResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ProcessPaymentResponse response = new ProcessPaymentResponse();
				
				// Validate the schema and SOAP headers
				if (context.getSchemaValidationException() != null) {
					throw new ServicePolicyException(SCHEMA_VALIDATION_ERROR_CODE, context.getSchemaValidationException().getMessage());
				}
				if (inputSoapHeader == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				
				// Extract the parameters
				CreditCardTransaction creditCardTransaction = parameters.getCreditCardTransaction();
				String paymentSourceType = parameters.getPaymentSourceType(); 
				String paymentSourceID = parameters.getPaymentSourceId();
				boolean notificationSuppressionInd = parameters.isNotificationSuppressionInd();
				
				// Validate the account, payment source and transaction type
				AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(Integer.parseInt(creditCardTransaction.getBanId()));
				if (accountInfo.getAccountType() == Account.ACCOUNT_TYPE_CONSUMER && accountInfo.getAccountSubType() == Account.ACCOUNT_SUBTYPE_PCS_PREPAID) {
					throw new ServicePolicyException("VALD0001", "Can not use processPayment on Prepaid Account");
				}
				PaymentSourceTypeInfo paymentSourceTypeInfo = getReferenceDataFacade(context).getPaymentSourceType(paymentSourceType.trim() + paymentSourceID.trim());
				if (paymentSourceTypeInfo == null) {
					throw new ServicePolicyException("VALD0002", "Unknown PaymentSourceType: [sourceID=" + paymentSourceID + ", sourceType=" + paymentSourceType + "]");
				}
				
				// Validate the credit card transaction type for the educational benefit of our consumers, although we actually override this value in the AccountLifecycleFacade EJB 
				if (!StringUtils.equalsIgnoreCase(creditCardTransaction.getTransactionType(), CreditCardTransactionInfo.TYPE_CREDIT_CARD_PAYMENT)) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_CREDIT_CARD_TRANSACTION_TYPE, "Credit card transaction type ["
							+ creditCardTransaction.getTransactionType() + "] is not supported");
				}
				
				// Map the credit card transaction information
				CreditCardTransactionInfo creditCardTransactionInfo =  WirelessPaymentMapper.getCreditCardTransactionMapper().mapToDomain(creditCardTransaction);
				
				// Map the credit card information
				CreditCardInfo cardInfo = WirelessPaymentMapper.getCreditCardMapper().mapToDomain(creditCardTransaction.getCreditCard());
				if (cardInfo == null || cardInfo.hasToken() == false) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
				}

				// Map the audit header information
				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(inputSoapHeader);
				auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());
				creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);

				// Map the audit information
				AuditInfo auditInfo = new AuditInfoMapper().mapToDomain(parameters.getAuditInfo());

				// Make the call to the AccountLifecycleFacade EJB
				String authorizationNumber = getAccountLifecycleFacade(context).processPayment(Double.parseDouble(creditCardTransaction.getAmount()), accountInfo, creditCardTransactionInfo,
						paymentSourceType, paymentSourceID, notificationSuppressionInd, auditInfo, context.getAccountLifeCycleFacadeSessionId());
				response.setAuthorizationNumber(authorizationNumber);
				
				return response;
			}
			
		}, new ProcessPaymentResponse(), exceptionContext);
	}
	
	@Override
	@ServiceBusinessOperation(errorCode = "CMB_WPS_0003", errorMessage = "Process deposit error")
	public ProcessDepositResponse processDeposit(final ProcessDeposit parameters, final OriginatingUserType inputSoapHeader) throws ServiceException {
		
		// Due to new SOA guidelines, PolicyException is no longer supported. We need to pass an ExceptionTranslationContext to the ServiceInvocationContext
		// execution method in order to map PolicyException to the ResponseMessage.
		// TODO remove this once a permanent solution is in place.
		ExceptionTranslationContext exceptionContext = createExceptionTranslationContext("processDeposit", "CMB_WPS_0003", "Process deposit error");

		return execute(new ServiceInvocationCallback <ProcessDepositResponse>() {
			
			@Override
			public ProcessDepositResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ProcessDepositResponse response = new ProcessDepositResponse();

				// Validate the schema and SOAP headers
				if (context.getSchemaValidationException() != null) {
					throw new ServicePolicyException(SCHEMA_VALIDATION_ERROR_CODE, context.getSchemaValidationException().getMessage());
				}
				if (inputSoapHeader == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				
				// Extract the parameters
				CreditCardTransaction creditCardTransaction = parameters.getCreditCardTransaction();
				String paymentSourceType = parameters.getPaymentSourceType(); 
				String paymentSourceID = parameters.getPaymentSourceId();
				
				// Validate the payment source
				PaymentSourceTypeInfo paymentSourceTypeInfo = getReferenceDataFacade(context).getPaymentSourceType(paymentSourceType.trim() + paymentSourceID.trim());
				if (paymentSourceTypeInfo == null) {
					throw new ServicePolicyException("VALD0002", "Unknown PaymentSourceType: [sourceID=" + paymentSourceID + ", sourceType=" + paymentSourceType + "]");
				}
				
				// Validate the credit card transaction type for the educational benefit of our consumers, although we actually override this value in the AccountLifecycleFacade EJB 
				if (!StringUtils.equalsIgnoreCase(creditCardTransaction.getTransactionType(), CreditCardTransactionInfo.TYPE_CREDIT_CARD_PAYMENT)) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_CREDIT_CARD_TRANSACTION_TYPE, "Credit card transaction type ["
							+ creditCardTransaction.getTransactionType() + "] is not supported");
				}
				
				// Map the credit card transaction information
				CreditCardTransactionInfo creditCardTransactionInfo = WirelessPaymentMapper.getCreditCardTransactionMapper().mapToDomain(creditCardTransaction);
				
				// Map the credit card information
				CreditCardInfo cardInfo = WirelessPaymentMapper.getCreditCardMapper().mapToDomain(creditCardTransaction.getCreditCard());
				if (cardInfo == null || cardInfo.hasToken() == false) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
				}

				// Map the audit header information
				AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
				AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(inputSoapHeader);
				auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());				
				creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);

				// Make the call to the AccountLifecycleFacade EJB
				String authorizationNumber = getAccountLifecycleFacade(context).payDeposit(Double.parseDouble(creditCardTransaction.getAmount()), creditCardTransactionInfo, 
						Integer.parseInt(creditCardTransaction.getBanId()), paymentSourceType, paymentSourceID, context.getAccountLifeCycleFacadeSessionId());				
				response.setAuthorizationNumber(authorizationNumber);

				return response;
			}
			
		}, new ProcessDepositResponse(), exceptionContext);
	}
	
	//@Override
	//TODO: The ping operation on the interface is of a somewhat older style. Unfortunately this can't be used.
	public PingStats ping(String operationName, Boolean deepPing) throws ServiceException {
		PingStats pingStats = new PingStats();
		try {
			pingStats.setServiceName(super.ping());
		} catch (Exception e) {
			pingStats.setServiceName("WirelessPaymentService 1.0");
		}
		
		return pingStats;
	}
	
	@Override
	public String ping() throws PolicyException, ServiceException {
		return "WirelessPaymentService 1.0";
	}
	
	// Due to new SOA guidelines, PolicyException is no longer supported. This method creates an ExceptionTranslationContext to pass into the ServiceInvocationContext
	// execution method in order to map PolicyException to the ResponseMessage. This method only partially constructs the ExceptionTranslationContext used by the AspectJ
	// ServiceInvocationAspect code wrapping the @ServiceBusinessOperation annotation class, as the parameter names and values are missing.
	// Note: this only catches PolicyExceptions thrown within the ServiceInvocationContext. This is only a temporary solution, implemented
	// in conjunction with other workarounds (see BaseServiceV2 overloaded execute method).
	private ExceptionTranslationContext createExceptionTranslationContext(String methodName, String errorCode, String errorMessage) {
		
		ExceptionTranslationContext context = new ExceptionTranslationContext();		
		context.setErrorCode(errorCode);
		context.setErrorMessage(errorMessage);
		context.setServiceName(getClass().getName());
		context.setOperationName(methodName);
		// Note: we're missing parameter names and values.
		context.setParameterNames(new String[0]);
		
		return context;
	}
	
}
