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
import java.util.Iterator;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ClientAPI;
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
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.cmb.jws.CreditCardValidationType;
import com.telus.cmb.jws.RegisterTopUpCreditCard;
import com.telus.cmb.jws.RegisterTopUpCreditCardResponse;
import com.telus.cmb.jws.UpdatePaymentMethod;
import com.telus.cmb.jws.UpdatePaymentMethodResponse;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customermanagementcommontypes_v3.PaymentMethod;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "BillingAccountManagementServicePort", 
		serviceName = "BillingAccountManagementService_v2_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/BillingAccountMgmt/BillingAccountManagementService_2", 
		wsdlLocation = "/wsdls/BillingAccountManagementService_v2_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.BillingAccountManagementServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingAccountManagementService_20 extends BaseService implements BillingAccountManagementServicePort {
	private static final String DEPOSIT_CHANGE_REASON_CODE = "103";
	private static final CreditCheckResultMapper ccrMapper = new CreditCheckResultMapper();
	private static final CreditCardMapper ccMapper = new CreditCardMapper();
	
	public BillingAccountManagementService_20 () {
		super (new BillingAccountManagementExceptionTranslator());
	}
	
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0002", errorMessage="Update Payment Method Error")
	public UpdatePaymentMethodResponse updatePaymentMethod(
			final UpdatePaymentMethod parameters,
			final OriginatingUserType updatePaymentMethodInSoapHdr)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback <UpdatePaymentMethodResponse>() {

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

	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0001", errorMessage="Register Top-up Credit Card error")
	public RegisterTopUpCreditCardResponse registerTopUpCreditCard(
			final RegisterTopUpCreditCard parameters,
			final OriginatingUserType registerTopUpCreditCardInSoapHdr)
			throws PolicyException, ServiceException {
		return execute(new ServiceInvocationCallback <RegisterTopUpCreditCardResponse>() {

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
	public void updateCreditCheckResult(final String ban, 
										final Boolean isIDEN, 
										final CreditCheckResult currentCreditCheckResult, 
										final CreditCheckResult newCreditCheckResult) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (currentCreditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing currentCreditCheckResult");
				}else if (newCreditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing newCreditCheckResult");
				}

				NumberFormat formatter = new DecimalFormat("0.00");
				int banNumber = Integer.parseInt(ban);
				CreditCheckResultInfo oldCreditCheckResultInfo = ccrMapper.mapToDomain(currentCreditCheckResult);
				CreditCheckResultInfo newCreditCheckResultInfo = ccrMapper.mapToDomain(newCreditCheckResult);
				
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
		        		if (isIDEN != null) {
		        			bIsIDEN = isIDEN.booleanValue();
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
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0004", errorMessage="Save Credit Check Result error")
	public void saveCreditCheckResult (final String ban, final CreditCheckResult creditCheckResult, final byte[] bureauFile) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				if (creditCheckResult == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_SCHEMA_VALIDATION, "Missing creditCheckResult");
				}
				
				int pBan = Integer.parseInt(ban);
				CreditCheckResultInfo creditCheckResultInfo = ccrMapper.mapToDomain(creditCheckResult);
				if (bureauFile != null) {
					creditCheckResultInfo.setBureauFile(new String(bureauFile));
				}
				AccountInfo accountInfo = getAccountInformationHelper(context).retrieveAccountByBan(pBan);
				if (accountInfo.isPostpaidConsumer()) {
			       	getAccountLifecycleManager(context).saveCreditCheckInfo(accountInfo, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
				} else if (accountInfo.isPostpaidBusinessRegular()) {
					getAccountLifecycleManager(context).saveCreditCheckInfoForBusiness(accountInfo, null, null, creditCheckResultInfo, "I", context.getAccountLifeCycleManagerSessionId());
				}
				return null;
			}
			
		});
	}


	/* (non-Javadoc)
	 * @see com.telus.cmb.jws.BillingAccountManagementServicePortType#removeTopUpCreditCard(java.lang.String)
	 */
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0005", errorMessage="Remove TopUp Credit Card Error")
	public void removeTopUpCreditCard(String billingAccountNumber)
			throws PolicyException, ServiceException {
		// TODO Implementation at a later date
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation is not yet implemented.");
		
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

}
