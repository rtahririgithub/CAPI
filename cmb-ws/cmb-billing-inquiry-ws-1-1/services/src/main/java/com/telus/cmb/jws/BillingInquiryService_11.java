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

import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ClientAPI;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.cmb.jws.mapper.BillingInquiryMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.BillingInquiryMapper_10;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v7.AuditInfoMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_10.PaymentSourceTypeMapper;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PaymentType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_0.PaymentSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardTransaction;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.Invoice;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.PaymentHistory;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.TransactionType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.AuditInfo;

/**
 * @author Tsz Chung Tong
 *
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "BillingInquiryServicePort", 
			serviceName = "BillingInquiryService_v1_1", 
			targetNamespace = "http://telus.com/wsdl/CMO/BillingInquiryMgmt/BillingInquiryService_1", 
			wsdlLocation = "/wsdls/BillingInquiryService_v1_1.wsdl", 
			endpointInterface = "com.telus.cmb.jws.BillingInquiryServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingInquiryService_11 extends BaseService  implements BillingInquiryServicePort {
	private static final CreditCardMapper creditCardMapper = new CreditCardMapper();
	private static final PaymentSourceTypeMapper paymentSourceTypeMapper = new PaymentSourceTypeMapper();
	
	public BillingInquiryService_11() {
		super(new BillingInquiryExceptionTranslator());
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0001", errorMessage = "Get payment history error")
	public List<PaymentHistory> getPaymentHistory(final String ban, final Date fromDate, final Date toDate) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<List<PaymentHistory>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<PaymentHistory> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				int banNumber = Integer.valueOf(ban);
				PaymentHistoryInfo[] paymentHistoryInfos = (PaymentHistoryInfo[]) getAccountInformationHelper(
						context).retrievePaymentHistory(banNumber, fromDate,
								toDate).toArray(new PaymentHistoryInfo[0]);

				return BillingInquiryMapper_10.PaymentHistoryMapper().mapToSchema(paymentHistoryInfos);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0002", errorMessage = "Process Credit Card error")
	public ProcessCreditCardResponse processCreditCard(final ProcessCreditCard parameters, final OriginatingUserType processCreditCardInSoapHdr) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<ProcessCreditCardResponse>() {
			@Override
			public ProcessCreditCardResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				CreditCardTransaction creditCardTransaction = parameters.getCreditCardTransaction();
				TransactionType transactionType = creditCardTransaction.getTransactionType();

				if (processCreditCardInSoapHdr == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_AVALON_USER_HEADER, ServiceErrorCodes.ERROR_DESC_AVALON_USER_HEADER_MISSING);
				}

				switch (transactionType) {
				case CCV:
					CreditCardInfo cardInfo = creditCardMapper.mapToDomain(creditCardTransaction.getCreditCard());
					if (cardInfo == null || cardInfo.hasToken() == false) {
						throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
					}

					AuditHeaderMapper auditHeaderMapper = new AuditHeaderMapper();
					AuditHeaderInfo auditHeaderInfo = auditHeaderMapper.mapToDomain(processCreditCardInSoapHdr);
					auditHeaderInfo.appendAppInfo(this.getClass().getSimpleName(), ClientAPI.CMDB_ID, getHostIpAddress());
					CreditCardTransactionInfo creditCardTransactionInfo = BillingInquiryMapper_10.CreditCardTransactionMapper().mapToDomain(creditCardTransaction);
					creditCardTransactionInfo.setAuditHeader(auditHeaderInfo);

					String authorizationNumber = getAccountLifecycleFacade(context).validateCreditCard(creditCardTransactionInfo, context.getAccountLifeCycleFacadeSessionId()).getAuthorizationCode();
					ProcessCreditCardResponse response = new ProcessCreditCardResponse();
					response.setAuthorizationNumber(authorizationNumber);

					return response;
					
				default:
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_CREDIT_CARD_TRANSACTION_TYPE, "Credit card transaction type [" + transactionType + "] is not supported");
				}
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0004", errorMessage = "Record Payment error")
	public void recordPayment(final String ban, final String amount, final CreditCard creditCard, final PaymentType paymentType, final PaymentSourceType paymentSourceType) throws PolicyException, ServiceException {

		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				recordPayment(context, getCreditCardPaymentInfo(creditCard), ban, amount, paymentType, paymentSourceType, Boolean.FALSE, null);
				return null;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0005", errorMessage = "Get Invoice History error")
	public List<Invoice> getInvoiceHistory(final String ban, final Date fromDate, final Date toDate) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<List<Invoice>>() {
			@Override
			public List<Invoice> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				@SuppressWarnings("unchecked")
				List<InvoiceHistoryInfo> invoiceHistoryInfoList = getAccountInformationHelper(context).retrieveInvoiceHistory(Integer.valueOf(ban), fromDate, toDate);
				return BillingInquiryMapper.InvoiceHistoryMapper().mapToSchema(invoiceHistoryInfoList);
			}
		});
	}

	private PaymentInfo getCreditCardPaymentInfo(CreditCard creditCard) throws ServicePolicyException {

		CreditCardInfo creditCardInfo = creditCardMapper.mapToDomain(creditCard);
		if (creditCardInfo == null || creditCardInfo.hasToken() == false) {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_CREDIT_CARD_TOKEN, ServiceErrorCodes.ERROR_DESC_CC_TOKEN_MISSING);
		}

		PaymentInfo paymentInfo = new PaymentInfo();
		paymentInfo.setCreditCardInfo(creditCardInfo);
		paymentInfo.setPaymentMethod(TelusConstants.PAYMENT_METHOD_CREDIT_CARD);
		
		return paymentInfo;
	}

	protected void recordPayment(ServiceInvocationContext context, PaymentInfo paymentInfo, String ban, String amount, PaymentType paymentType, PaymentSourceType paymentSourceType, 
			Boolean notificationSuppressionInd, AuditInfo auditInfo) throws Throwable {

		PaymentSourceTypeInfo paymentSourceTypeInfo = paymentSourceTypeMapper.mapToDomain(paymentSourceType);

		paymentInfo.setBan(Integer.parseInt(ban));
		paymentInfo.setAmount(Double.parseDouble(amount));
		paymentInfo.setPaymentSourceID(paymentSourceTypeInfo.getSourceID());
		paymentInfo.setPaymentSourceType(paymentSourceTypeInfo.getSourceType());
		paymentInfo.setDepositPaymentIndicator(PaymentType.D.equals(paymentType));
		paymentInfo.setDepositDate(new Date());
		paymentInfo.setAllowOverpayment(true);
		
		com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
		if (auditInfo != null) {
			auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
		}
		
		boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;

		getAccountLifecycleFacade(context).applyPaymentToAccount(null, paymentInfo, notificationSuppressionIndicator, auditInfoForDomain, context.getAccountLifeCycleManagerSessionId());
	}
}
