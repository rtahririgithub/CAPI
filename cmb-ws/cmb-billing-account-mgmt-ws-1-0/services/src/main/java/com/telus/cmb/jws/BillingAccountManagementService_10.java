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
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCheckResultMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.PaymentMethodMapper;
import com.telus.eas.account.info.AccountInfo;
import com.telus.eas.account.info.CreditCheckResultDepositInfo;
import com.telus.eas.account.info.CreditCheckResultInfo;
import com.telus.eas.account.info.PaymentMethodInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCheckResult;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PaymentMethod;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(
		portName = "BillingAccountManagementServicePort", 
		serviceName = "BillingAccountManagementService_v1_0", 
		targetNamespace = "http://telus.com/wsdl/CMO/BillingAccountMgmt/BillingAccountManagementService_1", 
		wsdlLocation = "/wsdls/BillingAccountManagementService_v1_0.wsdl", 
		endpointInterface = "com.telus.cmb.jws.BillingAccountManagementServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingAccountManagementService_10 extends BaseService implements BillingAccountManagementServicePort {
	private static final String DEPOSIT_CHANGE_REASON_CODE = "103";
	private static final CreditCheckResultMapper ccrMapper = new CreditCheckResultMapper();

	
	public BillingAccountManagementService_10 () {
		super (new BillingAccountManagementExceptionTranslator());
	}
	
	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0001", errorMessage="Register Top-up Credit Card error")
	public void registerTopUpCreditCard(final String ban, final CreditCard creditCard) throws PolicyException, ServiceException {
		throw new ServicePolicyException (ServiceErrorCodes.ERROR_UNSUPPORTED_OPERATION, "This operation has been deprecated. Please migrate to BillingAccountManagementService_20");
	}

	@Override
	@ServiceBusinessOperation(errorCode="CMB_BAMS_0002", errorMessage="Update Payment Method Error")
	public void updatePaymentMethod(final String ban, final PaymentMethod paymentMethod) throws PolicyException, ServiceException {
		execute(new ServiceInvocationCallback <Object>() {

			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				PaymentMethodInfo paymentMethodInfo = new PaymentMethodMapper().mapToDomain(paymentMethod);
				if (paymentMethodInfo.getPaymentMethod().equals(PaymentMethodInfo.PAYMENT_METHOD_REGULAR)) {
					getAccountLifecycleManager(context).changePaymentMethodToRegular(Integer.parseInt(ban), context.getAccountLifeCycleManagerSessionId());
				} else{
					getAccountLifecycleFacade(context).updatePaymentMethod(Integer.parseInt(ban), null, paymentMethodInfo, false, null, context.getAccountLifeCycleFacadeSessionId());
				}					
				return null;
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
}
