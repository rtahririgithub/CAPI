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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.ClientAPI;
import com.telus.api.TelusAPIException;
import com.telus.api.account.Account;
import com.telus.api.account.AccountSummary;
import com.telus.api.account.FinancialHistory;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.Subscriber;
import com.telus.cmb.jws.mapper.AuditInfoMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCardMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodAuthorizationTypeMapper;
import com.telus.cmb.jws.mapping.customer_management_common_30.PaymentMethodMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.transaction.info.AuditInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreditCardValidationType;
import com.telus.cmb.jws.ProcessPayment;
import com.telus.cmb.jws.ProcessPaymentResponse;
import com.telus.cmb.jws.RegisterTopUpCreditCard;
import com.telus.cmb.jws.RegisterTopUpCreditCardResponse;
import com.telus.cmb.jws.RemoveTopUpCreditCard;
import com.telus.cmb.jws.RemoveTopUpCreditCardResponse;
import com.telus.cmb.jws.SaveCreditCheckResult;
import com.telus.cmb.jws.SaveCreditCheckResultResponse;
import com.telus.cmb.jws.UpdateCreditCheckResult;
import com.telus.cmb.jws.UpdateCreditCheckResultResponse;
import com.telus.cmb.jws.UpdatePaymentMethod;
import com.telus.cmb.jws.UpdatePaymentMethodResponse;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethod;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethodAuthorizationType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v9.ResponseMessage;








/**
 * @author Edmir
 *
 */
@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "BillingAccountManagementServicePort", 
		serviceName = "BillingAccountManagementService_v2_1", 
		targetNamespace = "http://telus.com/wsdl/CMO/BillingAccountMgmt/BillingAccountManagementService_2", 
		wsdlLocation = "/wsdls/BillingAccountManagementService_v2_1.wsdl", 
		endpointInterface = "com.telus.cmb.jws.BillingAccountManagementServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingAccountManagementService_21 extends BaseServiceV2 implements BillingAccountManagementServicePort {
	private static final String DEPOSIT_CHANGE_REASON_CODE = "103";
	private static final CreditCheckResultMapper ccrMapper = new CreditCheckResultMapper();
	private static final CreditCardMapper ccMapper = new CreditCardMapper();
	private static final String CLM_suspendReasonCode = "CLMS";
	private static final String CLM_restoreAccountReasonCode = "CLMR";
	
	public BillingAccountManagementService_21 () {
		super (new BillingAccountManagementExceptionTranslator());
	}
	
	/**
	 * Since AccountInformationService 3.1 
		The following operations are moved from BillingAccountManagementService_v2_0 to AIS 3.1. 
		- updatePaymentMethod 
		- registerTopUpCreditCard 
		- removeTopUpCreditCard 
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0002", errorMessage="Update Payment Method Error")
	public UpdatePaymentMethodResponse updatePaymentMethod(
			final UpdatePaymentMethod parameters, 
			final OriginatingUserType updatePaymentMethodInSoapHdr)
			throws PolicyException, ServiceException {
		return executeDeprecated(new ServiceInvocationCallback <UpdatePaymentMethodResponse>() {

			@Override
			public UpdatePaymentMethodResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				String billingAccountNumber=parameters.getBillingAccountNumber();
				PaymentMethod paymentMethod = parameters.getPaymentMethod();
				CreditCardValidationType creditCardValidationType = parameters.getPaymentMethodValidation();
				CreditCardResponseInfo creditCardResponseInfo=new CreditCardResponseInfo();
				
				if (creditCardValidationType.isValidateCreditCardInd() && updatePaymentMethodInSoapHdr == null) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(paymentMethod);
				
				if (creditCardValidationType.isValidateCreditCardInd())
				{
					CreditCardTransactionInfo creditCardTransactionInfo = createCreditCardTransactionInfo(updatePaymentMethodInSoapHdr, paymentMethodInfo.getCreditCard0(), creditCardValidationType.getBusinessRole().toString());
					creditCardResponseInfo = getAccountLifecycleFacade(context).validateCreditCard(Integer.parseInt(billingAccountNumber), creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId());
				}	

				if (paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR)) {
					getAccountLifecycleManager(context).changePaymentMethodToRegular(Integer.parseInt(billingAccountNumber), context.getAccountLifeCycleManagerSessionId());
				} else{
					getAccountLifecycleFacade(context).updatePaymentMethod(Integer.parseInt(billingAccountNumber), null, paymentMethodInfo, false, null, context.getAccountLifeCycleFacadeSessionId());
				}					
				
				UpdatePaymentMethodResponse response = new UpdatePaymentMethodResponse();
				response.setAuthorization(new PaymentMethodAuthorizationTypeMapper().mapToSchema(creditCardResponseInfo));
				return response;
			}
		});
	}

	/**
	 * Since AccountInformationService 3.1 
		The following operations are moved from BillingAccountManagementService_v2_0 to AIS 3.1. 
		- updatePaymentMethod 
		- registerTopUpCreditCard 
		- removeTopUpCreditCard 
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0001", errorMessage="Register Top-up Credit Card error")
	public RegisterTopUpCreditCardResponse registerTopUpCreditCard(
			final RegisterTopUpCreditCard parameters,
			final OriginatingUserType registerTopUpCreditCardInSoapHdr)
			throws PolicyException, ServiceException {
		return executeDeprecated(new ServiceInvocationCallback <RegisterTopUpCreditCardResponse>() {

			@Override
			public RegisterTopUpCreditCardResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				String billingAccountNumber=parameters.getBillingAccountNumber();
				CreditCard creditCard = parameters.getCreditCard();
				CreditCardValidationType creditCardValidationType = parameters.getCreditCardValidation();
				CreditCardInfo creditCardInfo = ccMapper.mapToDomain(creditCard);
				CreditCardResponseInfo creditCardResponseInfo=new CreditCardResponseInfo();
				
				if (creditCardValidationType.isValidateCreditCardInd() && registerTopUpCreditCardInSoapHdr == null) {
					throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}
				
				if (creditCardValidationType.isValidateCreditCardInd())
				{
					CreditCardTransactionInfo creditCardTransactionInfo = createCreditCardTransactionInfo(registerTopUpCreditCardInSoapHdr, creditCardInfo, creditCardValidationType.getBusinessRole().toString());		
					creditCardResponseInfo = getAccountLifecycleFacade(context).validateCreditCard(Integer.parseInt(billingAccountNumber), creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId());
				}
				
		        if (creditCardInfo == null || creditCardInfo.hasToken() == false) {
		        	throw new ServicePolicyException (ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
		        }
		        PaymentMethodInfo paymentMethodInfo = new PaymentMethodInfo();
		        paymentMethodInfo.setPaymentMethod(PaymentMethodInfo.PAYMENT_METHOD_REGULAR);
		        paymentMethodInfo.setCreditCard0(creditCardInfo);
	            paymentMethodInfo.setStatus(PaymentMethodInfo.DIRECT_DEBIT_STATUS_HOLD);
	            paymentMethodInfo.setStatusReason(PaymentMethodInfo.DIRECT_DEBIT_REASON_CHANGE_BILL_DATA);
	            
            	getAccountLifecycleFacade(context).registerTopUpCreditCard(Integer.parseInt(billingAccountNumber), creditCardInfo, context.getAccountLifeCycleFacadeSessionId());
				
				RegisterTopUpCreditCardResponse response = new RegisterTopUpCreditCardResponse();
				response.setAuthorization(new PaymentMethodAuthorizationTypeMapper().mapToSchema(creditCardResponseInfo));

				return response;
	            
			}
		});
	}	
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0003", errorMessage="Update Credit Check Result Error")
	public UpdateCreditCheckResultResponse updateCreditCheckResult(final  UpdateCreditCheckResult parameters) throws PolicyException, ServiceException {
							
		executeDeprecated(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (parameters.getCurrentCreditCheckResult() == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing currentCreditCheckResult");
				}else if (parameters.getNewCreditCheckResult() == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing newCreditCheckResult");
				}

				NumberFormat formatter = new DecimalFormat("0.00");
				int banNumber = Integer.parseInt(parameters.getBillingAccountNumber());
				CreditCheckResultInfo oldCreditCheckResultInfo = ccrMapper.mapToDomain(parameters.getCurrentCreditCheckResult());
				CreditCheckResultInfo newCreditCheckResultInfo = ccrMapper.mapToDomain(parameters.getNewCreditCheckResult());
				
				String currentCreditClass = oldCreditCheckResultInfo.getCreditClass();
				String newCreditClass = newCreditCheckResultInfo.getCreditClass();
				
				double currentLimit = oldCreditCheckResultInfo.getLimit();
		        double newLimit = newCreditCheckResultInfo.getLimit();
		        
		        CreditCheckResultDepositInfo[] currentDeposits = (CreditCheckResultDepositInfo[]) oldCreditCheckResultInfo.getDeposits();
		        CreditCheckResultDepositInfo[] newDeposits = (CreditCheckResultDepositInfo[]) newCreditCheckResultInfo.getDeposits();
		        
		        boolean isClassChanged = !newCreditClass.equals(currentCreditClass);
		        boolean isDepositChanged = isDepositResultChanged(currentDeposits, newDeposits);
		        boolean isLimitChanged = (newLimit != currentLimit);
		        
		        if (isLimitChanged) {
		        	StringBuffer msgText = new StringBuffer();
		        	msgText.append("Subscriber Eligibility Change: ");
		        	msgText.append("Credit Limit from ");
		        	msgText.append(formatter.format(currentLimit));
		        	msgText.append(" to ");
		        	msgText.append(formatter.format(newLimit));
		        	
		        	getAccountLifecycleManager(context).updateCreditProfile(banNumber,newCreditClass,newLimit,msgText.toString(), context.getAccountLifeCycleManagerSessionId());
		        }
		        
		        if (isClassChanged || isDepositChanged) {

		        	StringBuffer messageText = new StringBuffer();
		        	messageText.append("Subscriber Eligibility Change: ");

		        	if (isClassChanged) {
		        		messageText.append("Credit Class from ");
		        		messageText.append(currentCreditClass);
		        		messageText.append(" to ");
		        		messageText.append(newCreditClass);
		        		messageText.append(", ");
		        	}

		        	if (isDepositChanged) {
		        		boolean bIsIDEN = false;
		        		if (parameters.isIDENInd() != null) {
		        			bIsIDEN = parameters.isIDENInd().booleanValue();
		        		}
		        		
		        		Iterator<DepositChangeMessage> messages = depositChangeMessage(currentDeposits, newDeposits, bIsIDEN).iterator();
		        		DepositChangeMessage message = null;

		        		while (messages.hasNext()) {

		        			message = messages.next();

		        			messageText.append("Deposit Amount for Product Type ");
		        			messageText.append(message.productType);
		        			messageText.append(" from ");
		        			messageText.append(formatter.format(message.oldDepositAmount));
		        			messageText.append(" to ");
		        			messageText.append(formatter.format(message.newDepositAmount));
		        			messageText.append(", ");

		        		}
		        	}

		        		getAccountLifecycleManager(context).updateCreditCheckResult(banNumber, 
								   newCreditClass, 
								   mapCreditCheckDeposits(newDeposits),
								   DEPOSIT_CHANGE_REASON_CODE, 
								   messageText.toString(),
								   context.getAccountLifeCycleManagerSessionId());		        		
		              }
		        return null;
			}
			
		});
		// UpdateCreditCheckResultResponse ret =  null;
	    return null;
	}
	
	//@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0004", errorMessage="Save Credit Check Result error")
//	public void saveCreditCheckResult (final String ban, final CreditCheckResult creditCheckResult, final byte[] bureauFile) throws PolicyException, ServiceException {
	public SaveCreditCheckResultResponse saveCreditCheckResult (final SaveCreditCheckResult parameters) throws PolicyException, ServiceException {
		executeDeprecated(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (parameters.getCreditCheckResult() == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing creditCheckResult");
				}
				
				int pBan = Integer.parseInt(parameters.getBillingAccountNumber());
				CreditCheckResultInfo creditCheckResultInfo = ccrMapper.mapToDomain( parameters.getCreditCheckResult());
				if (parameters.getBureauFile() != null) {
					creditCheckResultInfo.setBureauFile(new String(parameters.getBureauFile()));
				}
				AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(pBan);
				if (accountInfo.isPostpaidConsumer()) {
			       	getAccountLifecycleManager(context).saveCreditCheckInfo(accountInfo, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
				} else if (accountInfo.isPostpaidBusinessRegular()) {
					getAccountLifecycleManager(context).saveCreditCheckInfoForBusiness(accountInfo, null, null, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
				}
				//SaveCreditCheckResultResponse ret = null;
				return null;
			}
			
		});
		return null;	
	}
	
	/**
	 * Since AccountInformationService 3.1 
		The following operations are moved from BillingAccountManagementService_v2_0 to AIS 3.1. 
		- updatePaymentMethod 
		- registerTopUpCreditCard 
		- removeTopUpCreditCard 
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0005", errorMessage="Remove TopUp Credit Card Error")
	public RemoveTopUpCreditCardResponse removeTopUpCreditCard(
			RemoveTopUpCreditCard parameters) throws PolicyException,
			ServiceException {
		// TODO Auto-generated method stub
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
		
	}	

	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0006", errorMessage="Process payment error")
	public ProcessPaymentResponse processPayment(final ProcessPayment parameters)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback <ProcessPaymentResponse>() {
			@Override
			public ProcessPaymentResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				ProcessPaymentResponse response = new ProcessPaymentResponse();
				
				if (context.getSchemaValidationException() != null) {
					return schemaValidationErrorToResponse(response, context.getSchemaValidationException());
				}
										
				String paymentSourceType = parameters.getPaymentSource().getSourceType();
				String paymentSourceID = parameters.getPaymentSource().getSourceId();
				
				int ban = Integer.parseInt(parameters.getBillingAccountNumber());
				double amount = Double.parseDouble(parameters.getPaymentAmount());
				
				AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(ban);
				
				if (accountInfo.getAccountType() == Account.ACCOUNT_TYPE_CONSUMER &&
						accountInfo.getAccountSubType() == Account.ACCOUNT_SUBTYPE_PCS_PREPAID) {
					return applicationErrorToResponse(response, "VALD0001", "Can not use payBill on Prepaid Account");
				}
				
				PaymentSourceTypeInfo paymentSourceTypeInfo = getReferenceDataFacade(context).getPaymentSourceType(paymentSourceType.trim() + paymentSourceID.trim());
				
				if (paymentSourceTypeInfo == null) {
					return applicationErrorToResponse(response, "VALD0002", "Unknown PaymentSourceType: [sourceID=" + paymentSourceID + ", sourceType=" + paymentSourceType + "]");
				}

				CreditCard ccws = parameters.getCreditCard();
				String ccToken = ccws.getToken();
				if (ccToken ==null || ccToken.trim().length() == 0) {
					return applicationErrorToResponse(response, "VALD0003", "CreditCard token is missing");
				}
				
				String sessionId = context.getAccountLifeCycleFacadeSessionId(); 
				CreditCardInfo cc = new CreditCardInfo(); 
				cc.setCardVerificationData(ccws.getCardVerificationData());
				
				CreditCardTransactionInfo creditCardTransactionInfo  = createCreditCardTransactionInfo(parameters.getAuditHeader(), cc, sessionId); 
				
				AuditInfo auditInfo = null;
				
				if (parameters.getAuditInfo() != null) {
					auditInfo = new AuditInfoMapper().mapToDomain(parameters.getAuditInfo());
				} 	
				String authNumber = getAccountLifecycleFacade(context).payBill(amount , creditCardTransactionInfo, ban, paymentSourceType, paymentSourceID, accountInfo, true, auditInfo, sessionId);
				
				
				PaymentMethodAuthorizationType paymentAuthorization = new PaymentMethodAuthorizationType();
				paymentAuthorization.setAuthorizationNumber(authNumber);
				paymentAuthorization.setReferenceNumber(String.valueOf(ban));
				response.setAuthorization(paymentAuthorization);
				return response;
			}
		});
	}
	
	
	private boolean isDepositResultChanged(CreditCheckResultDepositInfo[] currentDeposits, CreditCheckResultDepositInfo[] newDeposits) {
		if ((currentDeposits == null || currentDeposits.length == 0) && 
			(newDeposits == null || newDeposits.length == 0)) {
			return false;
		}

		if ((currentDeposits == null || currentDeposits.length == 0) && 
			!(newDeposits == null || newDeposits.length == 0)) {
			return true;
		}

		if (!(currentDeposits == null || currentDeposits.length == 0) && 
			(newDeposits == null || newDeposits.length == 0)) {
			return true;
		}

		for (CreditCheckResultDepositInfo newDeposit : newDeposits) {
			for (CreditCheckResultDepositInfo currentDeposit : currentDeposits) {
				if (newDeposit.getProductType().equals(currentDeposit.getProductType())) {
					if (newDeposit.getDeposit() != currentDeposit.getDeposit()) {
						return true;
					}
				}
			}
		}

		return false;
	}
	

	private class DepositChangeMessage {
		String productType;
		double oldDepositAmount;
		double newDepositAmount;
	}

	private List<DepositChangeMessage> depositChangeMessage(CreditCheckResultDepositInfo[] currentDeposits, 
															CreditCheckResultDepositInfo[] newDeposits,
															boolean isIDEN) {

		List<DepositChangeMessage> messages = new ArrayList<DepositChangeMessage>();
		DepositChangeMessage message = null;

		if (currentDeposits != null && newDeposits != null) {
			for (CreditCheckResultDepositInfo currentDeposit : currentDeposits) {
				for (CreditCheckResultDepositInfo newDeposit : newDeposits) {
					if (currentDeposit.getProductType().equals(newDeposit.getProductType())) {
						if (currentDeposit.getDeposit() != newDeposit.getDeposit()) {

							message = new DepositChangeMessage();
							String productType = SubscriberInfo.PRODUCT_TYPE_PCS;
							if (isIDEN) {
								productType = SubscriberInfo.PRODUCT_TYPE_IDEN;
							}
							message.productType = productType;
							message.oldDepositAmount = currentDeposit.getDeposit();
							message.newDepositAmount = newDeposit.getDeposit();

							messages.add(message);
						}
					}
				}
			}
		}

		return messages;
	}
	
	  private CreditCheckResultDepositInfo[] mapCreditCheckDeposits(CreditCheckResultDepositInfo[] deposits){
		    CreditCheckResultDepositInfo[] depositInfos = null;
		    if (deposits != null && deposits.length > 0){
		      depositInfos = new CreditCheckResultDepositInfo[deposits.length];
		      for (int i=0; i<deposits.length; i++ ){
		        CreditCheckResultDepositInfo depositInfo = new CreditCheckResultDepositInfo();
		        depositInfo.setDeposit(deposits[i].getDeposit());
		        depositInfo.setProductType(deposits[i].getProductType());
		        depositInfos[i] = depositInfo;
		      }
		    }

		    return depositInfos;
		  }

	  private CreditCardTransactionInfo createCreditCardTransactionInfo(OriginatingUserType updatePaymentMethodInSoapHdr ,CreditCardInfo creditCardInfo, String businessRole)
	  {
		  CreditCardTransactionInfo creditCardTransactionInfo = new CreditCardTransactionInfo();
		  AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
		  CreditCardHolderInfo creditCardHolderInfo = new CreditCardHolderInfo();
		 
		  AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(updatePaymentMethodInSoapHdr);
		  auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());
		  
		  creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);
		  creditCardTransactionInfo.setCreditCardInfo(creditCardInfo);
		  
		  creditCardHolderInfo.setBusinessRole(businessRole);
		  creditCardTransactionInfo.setCreditCardHolderInfo(creditCardHolderInfo);
		  
		  return creditCardTransactionInfo;
	  }






	
	@Override
	public com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping(
			com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping parameters)
			throws ServiceException {
		com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse ping = 
				new com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.PingResponse();
		
		String version = "2.1";
		
		try {
			version = getServiceRuntimeInfo();
		}catch (Throwable t) {
			
		}
		ping.setVersion(version);
		return ping;
	}

	private boolean isSuspendedDueToCLM(AccountInfo ai) {
		final String CLM_suspendReasonCode = "CLMS";

		if ( ai.getStatus()  == AccountSummary.STATUS_SUSPENDED && (ai.getCreditCheckResult().getCreditClass().equals("X") || ai.getCreditCheckResult().getCreditClass().equals("L"))) {
			if (CLM_suspendReasonCode.equals(ai.getStatusActivityReasonCode().trim())) {
				return true;
			}
		}

		return false;
	}
	//coming from TMPostpaidConsumerAccount.java method getCLMSummary()
	private double calculateRequiredPaymentForRestoral(AccountInfo accountInfo, ServiceInvocationContext context) throws TelusAPIException{
		Date defaultDate = new Date(100, 0, 1); //2000-01-01
		double reqMinPayment = 0.0;
		double unpaidVoiceUsage = accountInfo.getUnpaidAirtimeTotal();
		double unpaidDataUsage=0.0;
		try {
			InvoiceHistory[] invoiceHistory = accountInfo.getInvoiceHistory(defaultDate, new Date());
			Date invoiceDate = (invoiceHistory.length>0)? invoiceHistory[0].getDate(): defaultDate;
			if (invoiceHistory.length>0) {
				invoiceDate = invoiceHistory[0].getDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(invoiceDate);
				//advance last invoice date by one month, so we get current bill cycle
				cal.add(Calendar.MONTH, 1 );
				
				int billCycleYear = cal.get(Calendar.YEAR );
				int billCycleMonth = cal.get(Calendar.MONTH )+1; 

				unpaidDataUsage = context.getAccountLifecycleFacade().getTotalUnbilledDataAmount( accountInfo.getBanId(), billCycleYear, billCycleMonth, accountInfo.getBillCycle() );
			} else {
				unpaidDataUsage = context.getAccountLifecycleFacade().getTotalDataOutstandingAmount(accountInfo.getBanId(), defaultDate );
			}
			
			double billedCharges = accountInfo.getFinancialHistory().getDebtSummary().getCurrentDue()+ accountInfo.getFinancialHistory().getDebtSummary().getPastDue();
			
			double unpaidUnBilledAmount = calculateUnpaidUnBilledAmount(accountInfo);
			reqMinPayment = (unpaidVoiceUsage + unpaidDataUsage + unpaidUnBilledAmount) - (accountInfo.getCreditCheckResult().getLimit() / 2);
			if (reqMinPayment < 0.0) {
				reqMinPayment = 0.0;
			}
			reqMinPayment = reqMinPayment + billedCharges;
			if (reqMinPayment < 0.0) {
				reqMinPayment = 0.0;
			}
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		} 
		return 0.0; 
	}
	
	private double calculateUnpaidUnBilledAmount(AccountInfo accountInfo) {
		double totalAmount = 0;
		try {
			FinancialHistory financialHistory = accountInfo.getFinancialHistory();

			Date billedDueDate = financialHistory.getDebtSummary()
			.getBillDueDate();
			if(billedDueDate == null){
				Subscriber[] sub = accountInfo.getSubscribers(1);
				billedDueDate = sub[0].getStartServiceDate();
			}
			if (billedDueDate.before(new Date())) {
				InvoiceHistory[] invoiceHistories = accountInfo.getInvoiceHistory(
						billedDueDate, new Date());
				for (int i = 0; i < invoiceHistories.length; i++) {
					totalAmount += invoiceHistories[i].getAmountDue() - invoiceHistories[i].getPastDue();
				}
			}

		} catch (Throwable e) {
		
		}
		return totalAmount;
	}

	private <T extends ResponseMessage> T schemaValidationErrorToResponse(T response, Exception exception) {
		response.setErrorCode(SCHEMA_VALIDATION_ERROR_CODE);
		response.setMessageType(ERROR_MSG_TYPE_ERROR);
		Message msg = new Message();
		msg.setMessage(exception.getMessage());
		response.getMessageList().add(msg);

		return response;
	}
	
	private <T extends ResponseMessage> T applicationErrorToResponse(T response, String errorCode, String errorMessage) {
		response.setErrorCode(errorCode);
		response.setMessageType(ERROR_MSG_TYPE_VALIDATION);
		Message msg = new Message();
		msg.setMessage(errorMessage);
		response.getMessageList().add(msg);
		
		return response;
	}
}
