package com.telus.cmb.jws;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;

import com.sun.xml.ws.developer.SchemaValidation;
import com.telus.api.ApplicationException;
import com.telus.api.ClientAPI;
import com.telus.api.account.Account;
import com.telus.api.account.Charge;
import com.telus.api.account.Credit;
import com.telus.api.reference.ChargeType;
import com.telus.cmb.common.util.TelusConstants;
import com.telus.cmb.jws.mapper.BillingInquiryMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.AuditHeaderMapper;
import com.telus.cmb.jws.mapping.billing_inquiry.BillingInquiryMapper_10;
import com.telus.cmb.jws.mapping.customer_management_common_10.ChequeMapper;
import com.telus.cmb.jws.mapping.customer_management_common_10.CreditCardMapper;
import com.telus.cmb.jws.mapping.enterprisecommontypes_v7.AuditInfoMapper;
import com.telus.cmb.jws.mapping.reference.billing_enquiry_10.PaymentSourceTypeMapper;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditBalanceTransferInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.PaymentHistoryInfo;
import com.telus.eas.account.info.PaymentInfo;
import com.telus.eas.account.info.SearchResultsInfo;
import com.telus.eas.framework.info.ChargeAdjustmentCodeInfo;
import com.telus.eas.framework.info.ChargeAdjustmentInfo;
import com.telus.eas.framework.info.ChargeIdentifierInfo;
import com.telus.eas.framework.info.ChargeInfo;
import com.telus.eas.framework.info.CreditInfo;
import com.telus.eas.framework.info.DiscountInfo;
import com.telus.eas.subscriber.info.SubscriberInfo;
import com.telus.eas.utility.info.DiscountPlanInfo;
import com.telus.eas.utility.info.PaymentSourceTypeInfo;
import com.telus.schemas.avalon.common.v1_0.OriginatingUserType;
import com.telus.tmi.xmlschema.xsd.customer.basetypes.subscriberinformationtypes_v3.SubscriberStatus;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.Cheque;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.CreditCard;
import com.telus.tmi.xmlschema.xsd.customer.customer.customer_management_common_types_1.PaymentType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_reference_types_1_0.PaymentSourceType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.CreditCardTransaction;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.DiscountType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.Invoice;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.PaymentHistory;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billing_inquiry_types_1.TransactionType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustment;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentCode;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentCodeList;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeAndAdjustmentForSubscriber;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.ChargeIdentifier;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.CreditTransfer;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.DiscountPlanType;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.GetAllCharges;
import com.telus.tmi.xmlschema.xsd.customer.customerbill.billinginquiryreferencetypes_v3.GetAllCredits;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.AuditInfo;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.Message;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.enterprisecommontypes_v7.ResponseMessage;

/**
 * @Author Brandon Wen
 */

@SchemaValidation(handler=com.telus.cmb.jws.ServiceSchemaValidator.class)
@WebService(portName = "BillingInquiryServicePort", 
			serviceName = "BillingInquiryService_v1_4", 
			targetNamespace = "http://telus.com/wsdl/CMO/BillingInquiryMgmt/BillingInquiryService_1", 
			wsdlLocation = "/wsdls/BillingInquiryService_v1_4.wsdl", 
			endpointInterface = "com.telus.cmb.jws.BillingInquiryServicePort")

@BindingType("http://schemas.xmlsoap.org/wsdl/soap/http")

public class BillingInquiryService_14 extends BaseService  implements BillingInquiryServicePort {

	private static final int MAX_CHARGE_COUNT = 1000;
	private static final int MAX_CREDIT_COUNT = 1000;
	private static final CreditCardMapper creditCardMapper = new CreditCardMapper();
	private static final ChequeMapper chequeMapper = new ChequeMapper();
	private static final PaymentSourceTypeMapper paymentSourceTypeMapper = new PaymentSourceTypeMapper();

	// new to v 1.5
	public static final String ERROR_MSG_TYPE_ERROR = "ERROR";
	public static final String ERROR_MSG_TYPE_VALIDATION = "VALIDATION";

	public BillingInquiryService_14() {
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

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0003", errorMessage = "Record Payment error")
	public void recordPayment(final String ban, final String amount, final CreditCard creditCard, final PaymentType paymentType, final PaymentSourceType paymentSourceType, Boolean notificationSuppressionInd) 
			throws PolicyException, ServiceException {
		
		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				recordPayment(context, getCreditCardPaymentInfo(creditCard), ban, amount, paymentType, paymentSourceType, null, null);
				return null;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0004", errorMessage = "Record Payment error")
	public void recordPayment(final String ban, final String amount, final CreditCard creditCard, final PaymentType paymentType, final PaymentSourceType paymentSourceType,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo) throws PolicyException, ServiceException {

		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				recordPayment(context, getCreditCardPaymentInfo(creditCard), ban, amount, paymentType, paymentSourceType, notificationSuppressionInd, auditInfo);
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

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0006", errorMessage = "Create CreditBalance Transfer error")
	public ResponseMessage createCreditBalanceTransfer(final String sourceAccountNumber, final String targetAccountNumber, final String creditTransferReasonCode, Boolean override)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				
				if (!"TOWN".equals(creditTransferReasonCode.trim())) {
					responseMessage.setErrorCode(ServiceErrorCodes.ERROR_INVALID_CT_REASON_CODE);
					responseMessage.setMessageType(ServiceErrorCodes.ERROR_DESC_INVALID_CT_REASON_CODE);
					responseMessage.setDateTimeStamp(new Date());
					
					return responseMessage;
				}

				int sourceBan = Integer.parseInt(sourceAccountNumber);
				int targetBan = Integer.parseInt(targetAccountNumber);
				try {
					getAccountLifecycleManager(context).createCreditBalanceTransferRequest(sourceBan, targetBan, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("createCreditBalanceTransfer successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0007", errorMessage = "Create CreditBalance Transfer error")
	public ResponseMessage createCreditBalanceTransfer(final String sourceAccountNumber, final String targetAccountNumber, final String creditTransferReasonCode, Boolean override, AuditInfo auditInfo)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				if (!"TOWN".equals(creditTransferReasonCode.trim())) {
					responseMessage.setErrorCode(ServiceErrorCodes.ERROR_INVALID_CT_REASON_CODE);
					responseMessage.setMessageType(ServiceErrorCodes.ERROR_DESC_INVALID_CT_REASON_CODE);
					responseMessage.setDateTimeStamp(new Date());
					
					return responseMessage;
				}

				int sourceBan = Integer.parseInt(sourceAccountNumber);
				int targetBan = Integer.parseInt(targetAccountNumber);
				try {
					getAccountLifecycleManager(context).createCreditBalanceTransferRequest(sourceBan, targetBan, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("createCreditBalanceTransfer successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0008", errorMessage = "Get CreditBalance TransferList error")
	public List<CreditTransfer> getCreditBalanceTransferList(final String accountNumber) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<CreditTransfer>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<CreditTransfer> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				int ban = Integer.parseInt(accountNumber);
				List<CreditBalanceTransferInfo> creditBalanceTransferInfoList = (List<CreditBalanceTransferInfo>) getAccountLifecycleManager(context).getCreditBalanceTransferRequestList(ban,
								context.getAccountLifeCycleManagerSessionId());
				return BillingInquiryMapper.CreditBalanceTransferMapper().mapToSchema(creditBalanceTransferInfoList);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0009", errorMessage = "Cancel CreditBalance Transfer error")
	public ResponseMessage cancelCreditBalanceTransfer(final String creditTransferSequenceNumber, Boolean override) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				int creditTransferSequenceNum = Integer.parseInt(creditTransferSequenceNumber);
				ResponseMessage responseMessage = new ResponseMessage();
				
				try {
					getAccountLifecycleManager(context).cancelCreditBalanceTransferRequest(creditTransferSequenceNum, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("cancelCreditBalanceTransfer successful");
					responseMessage.setDateTimeStamp(new Date());
				
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0010", errorMessage = "Get All Charges error")
	public List<GetAllCharges> getAllCharges(final String accountNumber, final String subscriberId, final Date fromDate, final Date toDate, final List<String> chargeCode)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<GetAllCharges>>() {
			@Override
			public List<GetAllCharges> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				String[] chargeCodes = chargeCode.toArray(new String[chargeCode.size()]);
				char level = ChargeType.CHARGE_LEVEL_ACCOUNT;
				if (subscriberId != null) {
					level = ChargeType.CHARGE_LEVEL_SUBSCRIBER;
				}
				Charge[] charges = getAccountInformationHelper(context).retrieveCharges(Integer.parseInt(accountNumber), chargeCodes, Account.BILL_STATE_ALL, level, subscriberId, fromDate, toDate, MAX_CHARGE_COUNT);

				return BillingInquiryMapper.GetAllChargesMapper().mapToSchema(charges);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0011", errorMessage = "Apply Charges To Account error")
	public Double applyChargeToAccount(final String accountNumber, final String chargeCode, final double chargeAmount, final Date effectiveDate, final String memoText, final Boolean notificationSuppressionInd,
			final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double chargeSeqNo;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setChargeCode(chargeCode);
				chargeInfo.setAmount(chargeAmount);
				chargeInfo.setEffectiveDate(effectiveDate);
				chargeInfo.setText(memoText);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (overrideThreshold) {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccountWithOverride(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());	
				} else {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccount(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				}
				
				return chargeSeqNo;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0012", errorMessage = "Apply Charges To AccountForSubscriber error")
	public Double applyChargeToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final String chargeCode, final double chargeAmount, final Date chargeEffectiveDate,
			final String memoText, final String productType, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double chargeSeqNo;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setSubscriberId(subscriberNumber);
				chargeInfo.setChargeCode(chargeCode);
				chargeInfo.setAmount(chargeAmount);
				chargeInfo.setEffectiveDate(chargeEffectiveDate);
				chargeInfo.setText(memoText);
				chargeInfo.setProductType(productType);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (overrideThreshold) {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccountWithOverride(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				} else {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccount(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				}
				
				return chargeSeqNo;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0013", errorMessage = "Apply Charges To Account error")
	public Double applyChargeToAccount(final String accountNumber, final String chargeCode, final double chargeAmount, final Date effectiveDate, final String memoText, final Boolean notificationSuppressionInd,
			final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd, AuditInfo auditIno) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double chargeSeqNo;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setChargeCode(chargeCode);
				chargeInfo.setAmount(chargeAmount);
				chargeInfo.setEffectiveDate(effectiveDate);
				chargeInfo.setText(memoText);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (overrideThreshold) {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccountWithOverride(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				} else {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccount(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				}
				
				return chargeSeqNo;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0014", errorMessage = "Apply Charges To AccountForSubscriber error")
	public Double applyChargeToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final String chargeCode, final double chargeAmount, final Date chargeEffectiveDate,
			final String memoText, final String productType, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd, AuditInfo auditInfo)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double chargeSeqNo;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setSubscriberId(subscriberNumber);
				chargeInfo.setChargeCode(chargeCode);
				chargeInfo.setAmount(chargeAmount);
				chargeInfo.setEffectiveDate(chargeEffectiveDate);
				chargeInfo.setText(memoText);
				chargeInfo.setProductType(productType);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (overrideThreshold) {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccountWithOverride(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				} else {
					chargeSeqNo = getAccountLifecycleManager(context).applyChargeToAccount(chargeInfo, true, context.getAccountLifeCycleManagerSessionId());
				}
				
				return chargeSeqNo;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0015", errorMessage = "Adjust ChargeToAccount error")
	public Double adjustChargeToAccount(final String accountNumber, final double chargeSequenceNumber, final String adjustmentReasonCode, final String adjustmentMemoText, final double adjustmentAmount,
			final Boolean billedInd, final Long billSequenceNumber, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double adjustmentId;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setId(chargeSequenceNumber);
				boolean billedIndicator = billedInd == null ? false : Boolean.valueOf(billedInd);
				chargeInfo.setBilled(billedIndicator);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (billedIndicator && billSequenceNumber == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_BILL_SEQUENCE_NUM, ServiceErrorCodes.ERROR_DESC_BILL_SEQUENCE_NUM_MISSING);
				}
				if (billSequenceNumber != null) {
					chargeInfo.setBillSequenceNo(Integer.valueOf(String.valueOf(billSequenceNumber)));
				}

				adjustmentId = getAccountLifecycleFacade(context).adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, overrideThreshold, false, null,
						context.getAccountLifeCycleFacadeSessionId());

				return adjustmentId;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0016", errorMessage = "Adjust Charge To AccountForSubscriber error")
	public Double adjustChargeToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final double chargeSequenceNumber, final String adjustmentReasonCode, final String adjustmentMemoText,
			final double adjustmentAmount, final String productType, final Boolean billedInd, final Long billSequenceNumber, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd,
			final Boolean overrideThresholdInd) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double adjustmentId;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setId(chargeSequenceNumber);
				boolean billedIndicator = billedInd == null ? false : Boolean.valueOf(billedInd);
				chargeInfo.setBilled(billedIndicator);
				chargeInfo.setSubscriberId(subscriberNumber);
				chargeInfo.setProductType(productType);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (billedIndicator && billSequenceNumber == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_BILL_SEQUENCE_NUM, ServiceErrorCodes.ERROR_DESC_BILL_SEQUENCE_NUM_MISSING);
				}
				if (billSequenceNumber != null) {
					chargeInfo.setBillSequenceNo(Integer.valueOf(String.valueOf(billSequenceNumber)));
				}

				adjustmentId = getAccountLifecycleFacade(context).adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, overrideThreshold, false, null,
						context.getAccountLifeCycleFacadeSessionId());

				return adjustmentId;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0017", errorMessage = "Adjust ChargeToAccount error")
	public Double adjustChargeToAccount(final String accountNumber, final double chargeSequenceNumber, final String adjustmentReasonCode, final String adjustmentMemoText, final double adjustmentAmount,
			final Boolean billedInd, final Long billSequenceNumber, final Boolean notificationSuppressionInd, final AuditInfo auditInfo, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double adjustmentId;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setId(chargeSequenceNumber);
				boolean billedIndicator = billedInd == null ? false : Boolean.valueOf(billedInd);
				chargeInfo.setBilled(billedIndicator);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (billedIndicator && billSequenceNumber == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_BILL_SEQUENCE_NUM, ServiceErrorCodes.ERROR_DESC_BILL_SEQUENCE_NUM_MISSING);
				}
				if (billSequenceNumber != null) {
					chargeInfo.setBillSequenceNo(Integer.valueOf(String.valueOf(billSequenceNumber)));
				}
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				adjustmentId = getAccountLifecycleFacade(context).adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, overrideThreshold, notificationSuppressionIndicator,
						auditInfoForDomain, context.getAccountLifeCycleFacadeSessionId());

				return adjustmentId;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0018", errorMessage = "Adjust Charge To AccountForSubscriber error")
	public Double adjustChargeToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final double chargeSequenceNumber, final String adjustmentReasonCode, final String adjustmentMemoText,
			final double adjustmentAmount, final String productType, final Boolean billedInd, final Long billSequenceNumber, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd,
			final Boolean overrideThresholdInd, final AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<Double>() {
			@Override
			public Double doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				double adjustmentId;
				ChargeInfo chargeInfo = new ChargeInfo();
				chargeInfo.setBan(Integer.parseInt(accountNumber));
				chargeInfo.setId(chargeSequenceNumber);
				boolean billedIndicator = billedInd == null ? false : Boolean.valueOf(billedInd);
				chargeInfo.setBilled(billedIndicator);
				chargeInfo.setSubscriberId(subscriberNumber);
				chargeInfo.setProductType(productType);
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				if (billedIndicator && billSequenceNumber == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_BILL_SEQUENCE_NUM, ServiceErrorCodes.ERROR_DESC_BILL_SEQUENCE_NUM_MISSING);
				}
				if (billSequenceNumber != null) {
					chargeInfo.setBillSequenceNo(Integer.valueOf(String.valueOf(billSequenceNumber)));
				}
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				adjustmentId = getAccountLifecycleFacade(context).adjustCharge(chargeInfo, adjustmentAmount, adjustmentReasonCode, adjustmentMemoText, overrideThreshold, notificationSuppressionIndicator,
						auditInfoForDomain, context.getAccountLifeCycleFacadeSessionId());

				return adjustmentId;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0019", errorMessage = "Apply ChargesAndAdjustments To Account error")
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccount(final List<ChargeAndAdjustment> chargesAndAdjustmentsList) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<ChargesAndAdjustmentsId>>() {
			@Override
			public List<ChargesAndAdjustmentsId> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<ChargeAdjustmentInfo> list = BillingInquiryMapper.ChargesAndAdjustmentMapper().mapToDomain(chargesAndAdjustmentsList);
				if (isBypassAuthorization(list)) {
					setUseApplicationIdentity();
				}
				List<ChargeAdjustmentInfo> resultList = getAccountLifecycleManager(context).applyChargesAndAdjustmentsToAccount(list, context.getAccountLifeCycleManagerSessionId());
				
				return BillingInquiryMapper.ChargesAndAdjustmentIdMapper().mapToSchema(resultList);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0020", errorMessage = "Apply ChargesAndAdjustments To AccountForSubscriber error")
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccountForSubscriber(final List<ChargeAndAdjustmentForSubscriber> chargesAndAdjustmentsForSubscriberList) 
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<ChargesAndAdjustmentsId>>() {
			@Override
			public List<ChargesAndAdjustmentsId> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<ChargeAdjustmentInfo> list = BillingInquiryMapper.ChargesAndAdjustmentToSubscriberMapper().mapToDomain(chargesAndAdjustmentsForSubscriberList);
				if (isBypassAuthorization(list)) {
					setUseApplicationIdentity();
				}
				List<ChargeAdjustmentInfo> resultList = getAccountLifecycleManager(context).applyChargesAndAdjustmentsToAccountForSubscriber(list, context.getAccountLifeCycleManagerSessionId());
				
				return BillingInquiryMapper.ChargesAndAdjustmentIdMapper().mapToSchema(resultList);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0021", errorMessage = "Apply ChargesAndAdjustments To Account error")
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccount(final List<ChargeAndAdjustment> chargesAndAdjustmentsList, Boolean notificationSuppressionInd, AuditInfo auditInfo)
			throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<ChargesAndAdjustmentsId>>() {
			@Override
			public List<ChargesAndAdjustmentsId> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<ChargeAdjustmentInfo> list = BillingInquiryMapper.ChargesAndAdjustmentMapper().mapToDomain(chargesAndAdjustmentsList);
				if (isBypassAuthorization(list)) {
					setUseApplicationIdentity();
				}
				List<ChargeAdjustmentInfo> resultList = getAccountLifecycleManager(context).applyChargesAndAdjustmentsToAccount(list, context.getAccountLifeCycleManagerSessionId());
				
				return BillingInquiryMapper.ChargesAndAdjustmentIdMapper().mapToSchema(resultList);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0022", errorMessage = "Apply ChargesAndAdjustments To AccountForSubscriber error")
	public List<ChargesAndAdjustmentsId> applyChargesAndAdjustmentsToAccountForSubscriber(final List<ChargeAndAdjustmentForSubscriber> chargesAndAdjustmentsForSubscriberList, Boolean notificationSuppressionInd,
			AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<ChargesAndAdjustmentsId>>() {
			@Override
			public List<ChargesAndAdjustmentsId> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<ChargeAdjustmentInfo> list = BillingInquiryMapper.ChargesAndAdjustmentToSubscriberMapper().mapToDomain(chargesAndAdjustmentsForSubscriberList);
				if (isBypassAuthorization(list)) {
					setUseApplicationIdentity();
				}
				List<ChargeAdjustmentInfo> resultList = getAccountLifecycleManager(context).applyChargesAndAdjustmentsToAccountForSubscriber(list, context.getAccountLifeCycleManagerSessionId());
				
				return BillingInquiryMapper.ChargesAndAdjustmentIdMapper().mapToSchema(resultList);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0023", errorMessage = "Apply CreditToAccount error")
	public ResponseMessage applyCreditToAccount(final String accountNumber, final Boolean recurringInd, final Integer numberOfRecurring, final String reasonCode, final String memoText,
			final Date effectiveDate, final double amount, final String balanceImpactCode, final Boolean prepaidInd, final String taxOptionCode, final Boolean notificationSuppressionInd,
			final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				int numOfRecurring = numberOfRecurring == null ? 0 : numberOfRecurring;
				creditInfo.setNumberOfRecurring(numOfRecurring);
				creditInfo.setReasonCode(reasonCode);
				creditInfo.setText(memoText);
				creditInfo.setEffectiveDate(effectiveDate);
				creditInfo.setAmount(amount);
				creditInfo.setBalanceImpactFlag(balanceImpactCode);
				boolean prepaidIndicator = prepaidInd == null ? false : Boolean.valueOf(prepaidInd);
				creditInfo.setPrepaid(prepaidIndicator);
				creditInfo.setTaxOption(getTaxOptionCode(taxOptionCode));
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				try {
					getAccountLifecycleFacade(context).applyCreditToAccount(creditInfo, overrideThreshold, false, null, context.getAccountLifeCycleFacadeSessionId());
					responseMessage.setMessageType("applyCreditToAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0024", errorMessage = "Apply CreditToAccountForSubscriber error")
	public ResponseMessage applyCreditToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final String reasonCode, final String memoText, final Date effectiveDate, final double amount,
			final String balanceImpactCode, final Boolean recurringInd, final Integer numberOfRecurring, final String productType, final Boolean prepaidInd, final String taxOptionCode,
			final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setSubscriberId(subscriberNumber);
				creditInfo.setProductType(productType);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				int numOfRecurring = numberOfRecurring == null ? 0 : numberOfRecurring;
				creditInfo.setNumberOfRecurring(numOfRecurring);
				creditInfo.setReasonCode(reasonCode);
				creditInfo.setText(memoText);
				creditInfo.setEffectiveDate(effectiveDate);
				creditInfo.setAmount(amount);
				creditInfo.setBalanceImpactFlag(balanceImpactCode);
				boolean prepaidIndicator = prepaidInd == null ? false : Boolean.valueOf(prepaidInd);
				creditInfo.setPrepaid(prepaidIndicator);
				creditInfo.setTaxOption(getTaxOptionCode(taxOptionCode));
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				try {
					getAccountLifecycleFacade(context).applyCreditToAccount(creditInfo, overrideThreshold, false, null, context.getAccountLifeCycleFacadeSessionId());
					responseMessage.setMessageType("applyCreditToAccountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0025", errorMessage = "Apply CreditToAccount error")
	public ResponseMessage applyCreditToAccount(final String accountNumber, final Boolean recurringInd, final Integer numberOfRecurring, final String reasonCode, final String memoText,
			final Date effectiveDate, final double amount, final String balanceImpactCode, final Boolean prepaidInd, final String taxOptionCode, final Boolean notificationSuppressionInd,
			final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd, final AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {

			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				int numOfRecurring = numberOfRecurring == null ? 0 : numberOfRecurring;
				creditInfo.setNumberOfRecurring(numOfRecurring);
				creditInfo.setReasonCode(reasonCode);
				creditInfo.setText(memoText);
				creditInfo.setEffectiveDate(effectiveDate);
				creditInfo.setAmount(amount);
				creditInfo.setBalanceImpactFlag(balanceImpactCode);
				boolean prepaidIndicator = prepaidInd == null ? false : Boolean.valueOf(prepaidInd);
				creditInfo.setPrepaid(prepaidIndicator);
				creditInfo.setTaxOption(getTaxOptionCode(taxOptionCode));
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				try {
					getAccountLifecycleFacade(context).applyCreditToAccount(creditInfo, overrideThreshold, notificationSuppressionIndicator, auditInfoForDomain, context.getAccountLifeCycleFacadeSessionId());
					responseMessage.setMessageType("applyCreditToAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0026", errorMessage = "Apply CreditToAccountForSubscriber error")
	public ResponseMessage applyCreditToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final String reasonCode, final String memoText, final Date effectiveDate, final double amount,
			final String balanceImpactCode, final Boolean recurringInd, final Integer numberOfRecurring, final String productType, final Boolean prepaidInd, final String taxOptionCode,
			final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd, final AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setSubscriberId(subscriberNumber);
				creditInfo.setProductType(productType);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				int numOfRecurring = numberOfRecurring == null ? 0 : numberOfRecurring;
				creditInfo.setNumberOfRecurring(numOfRecurring);
				creditInfo.setReasonCode(reasonCode);
				creditInfo.setText(memoText);
				creditInfo.setEffectiveDate(effectiveDate);
				creditInfo.setAmount(amount);
				creditInfo.setBalanceImpactFlag(balanceImpactCode);
				boolean prepaidIndicator = prepaidInd == null ? false : Boolean.valueOf(prepaidInd);
				creditInfo.setPrepaid(prepaidIndicator);
				creditInfo.setTaxOption(getTaxOptionCode(taxOptionCode));
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				com.telus.eas.transaction.info.AuditInfo auditInfoForDomain = null;
				if (auditInfo != null) {
					auditInfoForDomain = new AuditInfoMapper().mapToDomain(auditInfo);
				}
				boolean notificationSuppressionIndicator = notificationSuppressionInd == null ? false : notificationSuppressionInd;
				try {
					getAccountLifecycleFacade(context).applyCreditToAccount(creditInfo, overrideThreshold, notificationSuppressionIndicator, auditInfoForDomain, context.getAccountLifeCycleFacadeSessionId());
					responseMessage.setMessageType("applyCreditToAccountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0027", errorMessage = "Reverse CreditToAccount error")
	public ResponseMessage reverseCreditToAccount(final String accountNumber, final double adjustmentId, final String memoText, final String reversalReasonCode, final Boolean recurringInd,
			final Boolean reverseAllRecurringInd, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd, AuditInfo auditInfo)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setId((int) adjustmentId);
				creditInfo.setText(memoText);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				if (recurringIndicator && reverseAllRecurringInd == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_ALL_RECURRING_INDICATOR, ServiceErrorCodes.ERROR_DESC_ALL_RECURRING_INDICATOR_MISSING);
				}
				boolean reverseAllRecurringIndicator = reverseAllRecurringInd == null ? false : reverseAllRecurringInd;
				creditInfo.setReverseAllRecurring(reverseAllRecurringIndicator);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				try {
					if (overrideThreshold) {
						getAccountLifecycleManager(context).reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					} else {
						getAccountLifecycleManager(context).reverseCredit(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					}
					responseMessage.setMessageType("reverseCreditToAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0028", errorMessage = "Reverse CreditToAccountForSubscriber error")
	public ResponseMessage reverseCreditToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final double adjustmentId, final String memoText, final String reversalReasonCode,
			final Boolean recurringInd, final Boolean reverseAllRecurringInd, final String productType, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd,
			final Boolean overrideThresholdInd, AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setSubscriberId(subscriberNumber);
				creditInfo.setId((int) adjustmentId);
				creditInfo.setProductType(productType);
				creditInfo.setText(memoText);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				if (recurringIndicator && reverseAllRecurringInd == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_ALL_RECURRING_INDICATOR, ServiceErrorCodes.ERROR_DESC_ALL_RECURRING_INDICATOR_MISSING);
				}
				boolean reverseAllRecurringIndicator = reverseAllRecurringInd == null ? false : reverseAllRecurringInd;
				creditInfo.setReverseAllRecurring(reverseAllRecurringIndicator);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				try {
					if (overrideThreshold) {
						getAccountLifecycleManager(context).reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					} else {
						getAccountLifecycleManager(context).reverseCredit(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					}
					responseMessage.setMessageType("reverseCreditToAccountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0029", errorMessage = "Reverse CreditToAccount error")
	public ResponseMessage reverseCreditToAccount(final String accountNumber, final double adjustmentId, final String memoText, final String reversalReasonCode, final Boolean recurringInd,
			final Boolean reverseAllRecurringInd, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd)
					throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setId((int) adjustmentId);
				creditInfo.setText(memoText);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				if (recurringIndicator && reverseAllRecurringInd == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_ALL_RECURRING_INDICATOR, ServiceErrorCodes.ERROR_DESC_ALL_RECURRING_INDICATOR_MISSING);
				}
				boolean reverseAllRecurringIndicator = reverseAllRecurringInd == null ? false : reverseAllRecurringInd;
				creditInfo.setReverseAllRecurring(reverseAllRecurringIndicator);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				try {
					if (overrideThreshold) {
						getAccountLifecycleManager(context).reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					} else {
						getAccountLifecycleManager(context).reverseCredit(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					}
					responseMessage.setMessageType("reverseCreditToAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0030", errorMessage = "Reverse CreditToAccountForSubscriber error")
	public ResponseMessage reverseCreditToAccountForSubscriber(final String accountNumber, final String subscriberNumber, final double adjustmentId, final String memoText, final String reversalReasonCode,
			final Boolean recurringInd, final Boolean reverseAllRecurringInd, final String productType, final Boolean notificationSuppressionInd, final Boolean bypassAuthorizationInd,
			final Boolean overrideThresholdInd) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				CreditInfo creditInfo = new CreditInfo();
				creditInfo.setBan(Integer.parseInt(accountNumber));
				creditInfo.setSubscriberId(subscriberNumber);
				creditInfo.setId((int) adjustmentId);
				creditInfo.setProductType(productType);
				creditInfo.setText(memoText);
				boolean recurringIndicator = recurringInd == null ? false : recurringInd;
				creditInfo.setRecurring(recurringIndicator);
				if (recurringIndicator && reverseAllRecurringInd == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_MISSING_ALL_RECURRING_INDICATOR, ServiceErrorCodes.ERROR_DESC_ALL_RECURRING_INDICATOR_MISSING);
				}
				boolean reverseAllRecurringIndicator = reverseAllRecurringInd == null ? false : reverseAllRecurringInd;
				creditInfo.setReverseAllRecurring(reverseAllRecurringIndicator);
				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				creditInfo.setBypassAuthorization(bypassAuthorizationIndicator);
				
				boolean overrideThreshold = overrideThresholdInd == null ? false : Boolean.valueOf(overrideThresholdInd);
				try {
					if (overrideThreshold) {
						getAccountLifecycleManager(context).reverseCreditWithOverride(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					} else {
						getAccountLifecycleManager(context).reverseCredit(creditInfo, reversalReasonCode, memoText, context.getAccountLifeCycleManagerSessionId());
					}
					responseMessage.setMessageType("reverseCreditToAccountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0031", errorMessage = "Get RelatedCreditList ForCharge error")
	public List<RelatedCreditsForChargeList> getRelatedCreditListForCharge(final List<ChargeIdentifier> chargeIdentifier) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<RelatedCreditsForChargeList>>() {
			@Override
			public List<RelatedCreditsForChargeList> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				List<ChargeIdentifierInfo> chargeIdentifierInfoList = BillingInquiryMapper.ChargeIdentifierMapper().mapToDomain(chargeIdentifier);
				List<List<CreditInfo>> creditInfosList = getAccountInformationHelper(context).retrieveRelatedCreditsForChargeList(chargeIdentifierInfoList);
				List<RelatedCreditsForChargeList> relatedCreditsForChargeListResponse = new ArrayList<RelatedCreditsForChargeList>();
				for (List<CreditInfo> creditInfo : creditInfosList) {
					RelatedCreditsForChargeList relatedCreditsForChargeList = new RelatedCreditsForChargeList();
					List<RelatedCreditsForCharge> relatedCreditsForCharge = BillingInquiryMapper.RelatedCreditsForChargeMapper().mapToSchema(creditInfo);
					relatedCreditsForChargeList.getRelatedCreditsList().addAll(relatedCreditsForCharge);
					relatedCreditsForChargeListResponse.add(relatedCreditsForChargeList);
				}
				
				return relatedCreditsForChargeListResponse;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0032", errorMessage = "GetAllCredits Error")
	public List<GetAllCredits> getAllCredits(final String accountNumber, final String subscriberId, final Date fromDate, final Date toDate, final String billStateCode, final String operatorId,
			final String reasonCode) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<List<GetAllCredits>>() {
			@Override
			public List<GetAllCredits> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				char level = ChargeType.CHARGE_LEVEL_ACCOUNT;
				if (subscriberId != null) {
					level = ChargeType.CHARGE_LEVEL_SUBSCRIBER;
				}
				String kboperatorId = operatorId == null ? "" : operatorId;
				SearchResultsInfo SearchResultsInfo = getAccountInformationHelper(context).retrieveCredits(Integer.parseInt(accountNumber), fromDate, toDate, getBillStateCode(billStateCode), kboperatorId,
						reasonCode, level, subscriberId, MAX_CREDIT_COUNT);
				CreditInfo[] creditInfo = (CreditInfo[]) SearchResultsInfo.getItems();
				
				return BillingInquiryMapper.GetAllCreditsMapper().mapToSchema(creditInfo);
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0033", errorMessage = "Adjust ChargeToAccountForSubscriber Return Error")
	public List<ChargesAndAdjustmentsIdAndAmount> adjustChargeToAccountForSubscriberReturn(final String accountNumber, final String subscriberNumber, final String phoneNumber, 
			final ChargeAndAdjustmentCodeList chargeAndAdjustmentCodeList, final Date searchFromDate, final Date searchToDate, final String adjustmentMemoText, final String productType,
			final Boolean bypassAuthorizationInd, final Boolean overrideThresholdInd) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<List<ChargesAndAdjustmentsIdAndAmount>>() {
			@SuppressWarnings("unchecked")
			@Override
			public List<ChargesAndAdjustmentsIdAndAmount> doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				boolean bypassAuthorizationIndicator = Boolean.TRUE.equals(bypassAuthorizationInd);
				if (bypassAuthorizationIndicator) {
					setUseApplicationIdentity();
				}
				List<ChargeAndAdjustmentCode> inputList = chargeAndAdjustmentCodeList.getChargeCodeList();
				List<ChargeAdjustmentCodeInfo> list = BillingInquiryMapper.ChargeAndAdjustmentCodeMapper().mapToDomain(inputList);
				List<ChargeAdjustmentInfo> resultList = getAccountLifecycleManager(context).adjustChargeToAccountForSubscriberReturn(Integer.valueOf(accountNumber), subscriberNumber, phoneNumber,
						list, searchFromDate, searchToDate, adjustmentMemoText, productType, bypassAuthorizationInd, overrideThresholdInd, context.getAccountLifeCycleManagerSessionId());
				
				return BillingInquiryMapper.ChargesAndAdjustmentsIdAndAmountMapper().mapToSchema(resultList);
			}
		});
	}

	private boolean isBypassAuthorization(List<ChargeAdjustmentInfo> list) {
		
		for (ChargeAdjustmentInfo chargeAdjustmentInfo : list) {
			if (chargeAdjustmentInfo.isBypassAuthorization()) {
				return true;
			}
		}
		
		return false;
	}

	private char getTaxOptionCode(String taxOptionCode) {
		
		if ("TAX_OPTION_ALL_TAXES".equals(taxOptionCode)) {
			return Credit.TAX_OPTION_ALL_TAXES;
		} else if ("TAX_OPTION_GST_ONLY".equals(taxOptionCode)) {
			return Credit.TAX_OPTION_GST_ONLY;
		} else if ("TAX_OPTION_NO_TAX".equals(taxOptionCode)) {
			return Credit.TAX_OPTION_NO_TAX;
		} else {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_TAX_OPTION_CODE, ServiceErrorCodes.ERROR_DESC_INVALID_TAX_OPTION_CODE);
		}
	}

	private String getBillStateCode(String billStateCode) {
		
		if ("BILL_STATE_BILLED".equals(billStateCode)) {
			return Account.BILL_STATE_BILLED;
		} else if ("BILL_STATE_UNBILLED".equals(billStateCode)) {
			return Account.BILL_STATE_UNBILLED;
		} else if ("BILL_STATE_ALL".equals(billStateCode)) {
			return Account.BILL_STATE_ALL;
		} else {
			throw new ServicePolicyException(ServiceErrorCodes.ERROR_INVALID_BILL_STATE_CODE, ServiceErrorCodes.ERROR_DESC_INVALID_BILL_STATE_CODE);
		}
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0034", errorMessage = "Apply discount to account error")
	public ResponseMessage applyDiscountToAccount(final String accountNumber, final DiscountType discount, Boolean notificationSuppressionInd, AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();

				DiscountInfo discountInfo = BillingInquiryMapper.DiscountTypeMapper().mapToDomain(discount);
				discountInfo.setBan(Integer.parseInt(accountNumber));

				try {
					getAccountLifecycleManager(context).applyDiscountToAccount(discountInfo, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("applyDiscountToAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	/**
	 * This operation will not work on subscriber that is on a future-dated activation (i.e. SUB_STATUS='R')
	 * 
	 * @param accountNumber
	 * @param subscriberId
	 * @param productType
	 * @param discount
	 * @param notificationSuppressionInd
	 * @param auditInfo
	 * @return
	 * @throws PolicyException
	 * @throws ServiceException
	 */
	@ServiceBusinessOperation(errorCode = "CMB_BIS_0035", errorMessage = "Apply discount to account for subscriber error")
	public ResponseMessage applyDiscountToAccountForSubscriber(final String accountNumber, final String subscriberId, final String productType, final DiscountType discount, 
			Boolean notificationSuppressionInd, AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {

				ResponseMessage responseMessage = new ResponseMessage();
				
				DiscountInfo discountInfo = BillingInquiryMapper.DiscountTypeMapper().mapToDomain(discount);
				discountInfo.setBan(Integer.parseInt(accountNumber));
				discountInfo.setSubscriberId(subscriberId);
				discountInfo.setProductType(productType);

				try {
					getAccountLifecycleManager(context).applyDiscountToAccount(discountInfo, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("applyDiscountToAccountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0036", errorMessage = "Get discount list for subscriber error")
	public void getDiscountListForSubscriber(
			Holder<String> accountNumber,
			Holder<String> subscriberId,
			Holder<String> productType,
			Holder<Date> dateTimeStamp,
			Holder<String> errorCode,
			Holder<String> messageType,
			Holder<String> transactionId,
			Holder<List<Message>> messageList,
			Holder<String> contextData,
			Holder<List<DiscountToPlanAssociation>> discountList)
			throws PolicyException, ServiceException {
		GetDiscountListForSubscriber request = new GetDiscountListForSubscriber();
		request.setAccountNumber(accountNumber.value);
		request.setProductType(productType.value);
		request.setSubscriberId(subscriberId.value);
		GetDiscountListForSubscriberResponse response = this.getDiscountListForSubscriber(request);
		accountNumber.value = response.getAccountNumber();
		subscriberId.value = response.getSubscriberId();
		productType.value = response.getProductType();
		dateTimeStamp.value = response.getDateTimeStamp();
		errorCode.value = response.getErrorCode();
		messageType.value = response.getMessageType();
		transactionId.value = response.getTransactionId();
		messageList.value = response.getMessageList();
		contextData.value = response.getContextData();
		discountList.value = response.getDiscountList();
	}
	
	private GetDiscountListForSubscriberResponse getDiscountListForSubscriber(final GetDiscountListForSubscriber parameters) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<GetDiscountListForSubscriberResponse>() {
			@Override
			public GetDiscountListForSubscriberResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				GetDiscountListForSubscriberResponse response = new GetDiscountListForSubscriberResponse();
				
				DiscountInfo[] discountInfoList = null;
				DiscountPlanInfo[] discountPlanInfoList;
				
				try {
					discountInfoList = getSubscriberLifecycleManager(context).retrieveDiscounts(Integer.parseInt(parameters.getAccountNumber()), parameters.getSubscriberId(),
							parameters.getProductType(), context.getSubscriberLifecycleManagerSessionId());
					discountPlanInfoList = new DiscountPlanInfo[discountInfoList.length];
					for (int i = 0; i < discountInfoList.length; i++) {
						discountPlanInfoList[i] = getReferenceDataFacade(context).getDiscountPlan(discountInfoList[i].getDiscountCode());
					}

					List<DiscountType> resultDiscountTypeList = BillingInquiryMapper.DiscountTypeMapper().mapToSchema(discountInfoList);
					List<DiscountPlanType> resultDiscountPlanTypeList = BillingInquiryMapper.DiscountPlanTypeMapper().mapToSchema(discountPlanInfoList);

					for (int i = 0; i < resultDiscountTypeList.size(); i++) {
						DiscountToPlanAssociation dpa = new DiscountToPlanAssociation();
						dpa.setDiscount(resultDiscountTypeList.get(i));
						dpa.setDiscountPlan(resultDiscountPlanTypeList.get(i));
						response.getDiscountList().add(dpa);
					}
					response.setAccountNumber(parameters.getAccountNumber());
					response.setSubscriberId(parameters.getSubscriberId());
					response.setProductType(parameters.getProductType());
					response.setMessageType("getDiscountListForSubscriber successful");
					response.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					response.setErrorCode(ae.getErrorCode());
					response.setMessageType(ae.getErrorMessage().replace('', ','));
					response.setContextData(ae.getStackTraceAsString().replace('', ','));
					response.setDateTimeStamp(new Date());
				}
		
				return response;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0037", errorMessage = "Get discount list for account error")
	public void getDiscountListForAccount(
			Holder<String> accountNumber,
			Holder<Date> dateTimeStamp,
			Holder<String> errorCode,
			Holder<String> messageType,
			Holder<String> transactionId,
			Holder<List<Message>> messageList,
			Holder<String> contextData,
			Holder<List<DiscountToPlanAssociation>> discountList)
			throws PolicyException, ServiceException {
		GetDiscountListForAccount request = new GetDiscountListForAccount();
		request.setAccountNumber(accountNumber.value);
		GetDiscountListForAccountResponse response = this.getDiscountListForAccount(request);
		accountNumber.value = response.getAccountNumber();
		dateTimeStamp.value = response.getDateTimeStamp();
		errorCode.value = response.getErrorCode();
		messageType.value = response.getMessageType();
		transactionId.value = response.getTransactionId();
		messageList.value = response.getMessageList();
		contextData.value = response.getContextData();
		discountList.value = response.getDiscountList();
	}
	
	private GetDiscountListForAccountResponse getDiscountListForAccount(final GetDiscountListForAccount parameters) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<GetDiscountListForAccountResponse>() {
			@SuppressWarnings("unchecked")
			@Override
			public GetDiscountListForAccountResponse doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				GetDiscountListForAccountResponse response = new GetDiscountListForAccountResponse();
				
				List<DiscountInfo> discountInfoList = null;
				DiscountPlanInfo[] discountPlanInfoList;
				
				try {
					discountInfoList = (List<DiscountInfo>) getAccountLifecycleManager(context).retrieveDiscounts(Integer.parseInt(parameters.getAccountNumber()), context.getAccountLifeCycleManagerSessionId());
					discountPlanInfoList = new DiscountPlanInfo[discountInfoList.size()];

					for (int i = 0; i < discountInfoList.size(); i++) {
						discountPlanInfoList[i] = getReferenceDataFacade(context).getDiscountPlan((discountInfoList.get(i)).getDiscountCode());
					}

					List<DiscountType> resultDiscountTypeList = BillingInquiryMapper.DiscountTypeMapper().mapToSchema(discountInfoList);
					List<DiscountPlanType> resultDiscountPlanTypeList = BillingInquiryMapper.DiscountPlanTypeMapper().mapToSchema(discountPlanInfoList);

					for (int i = 0; i < resultDiscountTypeList.size(); i++) {
						DiscountToPlanAssociation dpa = new DiscountToPlanAssociation();
						dpa.setDiscount(resultDiscountTypeList.get(i));
						dpa.setDiscountPlan(resultDiscountPlanTypeList.get(i));
						response.getDiscountList().add(dpa);
					}
					response.setAccountNumber(parameters.getAccountNumber());
					response.setMessageType("getDiscountListForAccount successful");
					response.setDateTimeStamp(new Date());

				} catch (ApplicationException ae) {
					response.setErrorCode(ae.getErrorCode());
					response.setMessageType(ae.getErrorMessage().replace('', ','));
					response.setContextData(ae.getStackTraceAsString().replace('', ','));
					response.setDateTimeStamp(new Date());
				}

				return response;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0038", errorMessage = "Expire discount for account error")
	public ResponseMessage expireDiscountForAccount(final String accountNumber, final DiscountType discount, Boolean notificationSuppressionInd, AuditInfo auditInfo) throws PolicyException, ServiceException {

		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				
				DiscountInfo discountInfo = new DiscountInfo();
				discountInfo.setBan(Integer.parseInt(accountNumber));
				discountInfo.setDiscountByUserId(discount.getDiscountByUserId());
				discountInfo.setDiscountCode(discount.getDiscountCode());
				if (discount.getDiscountSequenceNumber() != null) {
					discountInfo.setDiscountSequenceNo(discount.getDiscountSequenceNumber());
				}
				discountInfo.setEffectiveDate(discount.getEffectiveDate());
				if (discount.getExpiryDate() == null) {
					discountInfo.setExpiryDate(new Date());
				} else {
					discountInfo.setExpiryDate(discount.getExpiryDate());
				}

				try {
					getAccountLifecycleManager(context).applyDiscountToAccount(discountInfo, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("expireDiscountForAccount successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}
				
				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0039", errorMessage = "Expire discount for subscriber error")
	public ResponseMessage expireDiscountForSubscriber(final String accountNumber, final String subscriberId, final String productType, final DiscountType discount, Boolean notificationSuppressionInd,
			AuditInfo auditInfo) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();
				
				DiscountInfo discountInfo = new DiscountInfo();
				discountInfo.setBan(Integer.parseInt(accountNumber));
				discountInfo.setSubscriberId(subscriberId);
				discountInfo.setProductType(productType);
				discountInfo.setDiscountByUserId(discount.getDiscountByUserId());
				discountInfo.setDiscountCode(discount.getDiscountCode());
				if (discount.getDiscountSequenceNumber() != null) {
					discountInfo.setDiscountSequenceNo(discount.getDiscountSequenceNumber());
				}
				discountInfo.setEffectiveDate(discount.getEffectiveDate());

				if (discount.getExpiryDate() == null) {
					discountInfo.setExpiryDate(new Date());
				} else {
					discountInfo.setExpiryDate(discount.getExpiryDate());
				}

				try {
					getAccountLifecycleManager(context).applyDiscountToAccount(discountInfo, context.getAccountLifeCycleManagerSessionId());
					responseMessage.setMessageType("expireDiscountForSubscriber successful");
					responseMessage.setDateTimeStamp(new Date());
				
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0040", errorMessage = "Create Deposit error")
	public ResponseMessage createDeposit(final String billingAccountNumber, final String subscriberId, final double depositAmount, final String memoText, final String productType,
			final SubscriberStatus subscriberStatus) throws PolicyException, ServiceException {
		
		return execute(new ServiceInvocationCallback<ResponseMessage>() {
			@Override
			public ResponseMessage doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				ResponseMessage responseMessage = new ResponseMessage();

				try {
					SubscriberInfo subInfo = new SubscriberInfo();
					subInfo.setBanId(Integer.valueOf(billingAccountNumber));
					subInfo.setSubscriberId(subscriberId);
					subInfo.setProductType(productType);
					subInfo.setStatus(subscriberStatus.value().charAt(0));

					getSubscriberLifecycleManager(context).createDeposit(subInfo, depositAmount, memoText, context.getSubscriberLifecycleManagerSessionId());

					responseMessage.setMessageType("createDeposit successful");
					responseMessage.setDateTimeStamp(new Date());
					
				} catch (ApplicationException ae) {
					responseMessage.setErrorCode(ae.getErrorCode());
					responseMessage.setMessageType(ae.getErrorMessage().replace('', ','));
					responseMessage.setContextData(ae.getStackTraceAsString().replace('', ','));
					responseMessage.setDateTimeStamp(new Date());
				}

				return responseMessage;
			}
		});
	}

	@ServiceBusinessOperation(errorCode = "CMB_BIS_0041", errorMessage = "Record Payment error")
	public void recordPayment(final String ban, final String amount, final CreditCard creditCard, final Cheque cheque, final PaymentType paymentType, final PaymentSourceType paymentSourceType,
			final Boolean notificationSuppressionInd, final AuditInfo auditInfo) throws PolicyException, ServiceException {

		execute(new ServiceInvocationCallback<Object>() {
			@Override
			public Object doInInvocationCallback(ServiceInvocationContext context) throws Throwable {
				
				PaymentInfo paymentInfo;

				if (creditCard == null && cheque == null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_CODE_PAYMENT_METHOD_MISSING, ServiceErrorCodes.ERROR_DESC_PAYMENT_METHOD_MISSING);
				} else if (creditCard != null && cheque != null) {
					throw new ServicePolicyException(ServiceErrorCodes.ERROR_CODE_PAYMENT_METHOD_DUPLICATED, ServiceErrorCodes.ERROR_DESC_PAYMENT_METHOD_DUPLICATED);
				} else if (cheque != null) {
					paymentInfo = new PaymentInfo();
					paymentInfo.setChequeInfo(chequeMapper.mapToDomain(cheque));
					paymentInfo.setPaymentMethod(TelusConstants.PAYMENT_METHOD_CHEQUE);
				} else {
					paymentInfo = getCreditCardPaymentInfo(creditCard);
				}
				
				recordPayment(context, paymentInfo, ban, amount, paymentType, paymentSourceType, notificationSuppressionInd, auditInfo);

				return null;
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


