package com.telus.cmb.account.lifecyclefacade.dao.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.telus.api.ApplicationException;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.lifecyclefacade.dao.CardPaymentServiceDao;
import com.telus.cmb.common.dao.soa.spring.SoaBaseSvcClient;
import com.telus.cmb.common.dao.soa.spring.SoaCallback;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.cmb.wsclient.CardPaymentServicePort;
import com.telus.eas.account.info.AuditHeaderInfo;
import com.telus.eas.account.info.CreditCardHolderInfo;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;
import com.telus.tmi.xmlschema.srv.eo.financialmgmt.cardpaymentsvcrequestresponse_v1.CreditCardProviderResponseType;
import com.telus.tmi.xmlschema.srv.eo.financialmgmt.cardpaymentsvcrequestresponse_v1.CreditCardTransactionRequestType;
import com.telus.tmi.xmlschema.srv.eo.financialmgmt.cardpaymentsvcrequestresponse_v1.CreditCardTransactionResponseType;
import com.telus.tmi.xmlschema.srv.eo.financialmgmt.cardpaymentsvcrequestresponse_v1.ProcessTransaction;
import com.telus.tmi.xmlschema.srv.eo.financialmgmt.cardpaymentsvcrequestresponse_v1.ProcessTransactionResponse;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.ApplicationType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.ClientInfoType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.CreditCardDetailsType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.CreditCardType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.ProviderResponseType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.cardpaymentsvccommontypes_v1.TransactionType;
import com.telus.tmi.xmlschema.xsd.enterprise.basetypes.types.ping_v1.Ping;

public class CardPaymentServiceDaoImpl extends SoaBaseSvcClient implements CardPaymentServiceDao {

	private static final Logger LOGGER = Logger.getLogger(CardPaymentServiceDaoImpl.class);

	public static final String PAYMENT_SVC_TIMEOUT = "WPS_SERVICE_TIME_OUT";

	private static final String TRANSACTION_TYPE_AUTHORIZATION_ONLY = "AUTHORIZATION_ONLY";
	private static final String TRANSACTION_TYPE_PURCHASE = "PURCHASE";
	private static final String TRANSACTION_TYPE_PAYMENT_VOID = "PAYMENT_VOID";
	private static final String TRANSACTION_TYPE_REFUND = "REFUND";
	private static final String TRANSACTION_TYPE_REFUND_VOID = "REFUND_VOID";
	private static final String SUCCESS = "S";
	static final String ERROR_CODE_WPS_ERROR = "WPS40001";
	static final String ERROR_CODE_WPS_CONFIG_ERROR = "WPS40002";

	@Autowired
	private CardPaymentServicePort port;
	
	@Autowired
	private AvalonSoapHandler headerHandler;
	
	public CardPaymentServiceDaoImpl() {
		super(new CardPaymentServiceSoaExceptionHandler());
	}

	@Override
	public String ping() throws ApplicationException {
		return execute(new SoaCallback<String>() {
			@Override
			public String doCallback() throws Exception {
				return port.ping(new Ping()).getVersion();
			}
		});
	}
	
	@Override
	public CreditCardResponseInfo processCreditCard(final String termId, @Sensitive final CreditCardTransactionInfo ccTxnInfo, final AuditHeader auditHeader) throws ApplicationException {
		
		//Log request
		logProcessCreditCardReq(termId, ccTxnInfo, auditHeader);

		return execute( new SoaCallback<CreditCardResponseInfo>() {
			
			@Override
			public CreditCardResponseInfo doCallback() throws Throwable {

				//Copy the value so cardPaymentServiceAuditHeaderInfo in AvalonUserHeaderHandler has access to the AuditHeader
//				cardPaymentServiceAuditHeaderInfo.copyFrom((AuditHeaderInfo)auditHeader);
				
				headerHandler.setAuditHeader(auditHeader);
				
				ProcessTransaction parameters = new ProcessTransaction();
				parameters.setCreditCardTransactionRequest(mapCreditCardTransactionInfo(termId, ccTxnInfo));

				if (LOGGER.isDebugEnabled())
					LOGGER.debug("WPS call begin.");
				
				long startTime = System.currentTimeMillis();
				ProcessTransactionResponse response = port.processTransaction(parameters);

				if (LOGGER.isDebugEnabled())
					LOGGER.debug("WPS call end, time elapse: " + (System.currentTimeMillis() - startTime) + " ms.");

				CreditCardResponseInfo result = mapResponse(response);

				//Log response
				logProcessCreditCardRes(result);

				return result;
			}
		});
	}
	
	private CreditCardResponseInfo mapResponse(ProcessTransactionResponse response) {
		CreditCardResponseInfo ccResponse = new CreditCardResponseInfo();
		if (response != null) {
			CreditCardTransactionResponseType cctxResponse = response.getCreditCardTransactionResponse();
			if (cctxResponse != null) {
	  	    	CreditCardProviderResponseType bankResponse = cctxResponse.getBankResponse();
	  	    	if (SUCCESS.equals(bankResponse.getTransactionResult())) {
		  	    	ccResponse.setApproved(true);
	  	  	    	ccResponse.setAuthorizationCode( bankResponse.getApprovalCode() );
	  	  	    	ccResponse.setCardVerificationDataResult(bankResponse.getCVDResult());
	  	  	    	ccResponse.setReferenceNum(cctxResponse.getReferenceNumber());
	  	    	} else {
	  	    		//Bank declined 
		  	    	ProviderResponseType providerResponse = bankResponse.getResponse();
		  	    	ccResponse.setApproved(false);
		  	    	ccResponse.setResponseCode(providerResponse.getResponseCode());
		  	    	ccResponse.setResponseText(providerResponse.getResponseText());
	  	    	}
			}
		}
		return ccResponse;
	}

	private void logProcessCreditCardReq(String termId, CreditCardTransactionInfo ccTxnInfo, AuditHeader auditHeader) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("processCreditCard Request");
			sb.append(" | TermId: " + termId);
			if (ccTxnInfo != null) {
				sb.append(" | Amount: " + ccTxnInfo.getAmount());
				sb.append(" | BAN: " + ccTxnInfo.getBan());
			}
			if (auditHeader != null) {
				sb.append(" | AuditHeader Customer Id: " + auditHeader.getCustomerId());
				sb.append(" | AuditHeader IP Address: " + auditHeader.getUserIPAddress());
			}
			LOGGER.info(sb.toString());
		}
	}

	private void logProcessCreditCardRes(CreditCardResponseInfo response) {
		if (LOGGER.isInfoEnabled()) {
			StringBuffer sb = new StringBuffer();
			sb.append("processCreditCard Response ");
			sb.append(response.toString());
			LOGGER.info(sb.toString());
		}
	}

	private CreditCardTransactionRequestType mapCreditCardTransactionInfo(String termId, @Sensitive CreditCardTransactionInfo ccTxnInfo) throws ApplicationException {

		ApplicationType applicationInfo = new ApplicationType();
		applicationInfo.setApplicationCode(termId);

		CreditCardTransactionRequestType request = new CreditCardTransactionRequestType();
		request.setApplication(applicationInfo);
		request.setTransaction(mapPaymentTransactionInfo(ccTxnInfo));
		request.setCreditCard(mapCreditCardInfo(ccTxnInfo.getCreditCardInfo()));
		request.setClientAccount(mapClientInfo(ccTxnInfo.getCreditCardHolderInfo()));

		return request;
	}

	private TransactionType mapPaymentTransactionInfo(@Sensitive CreditCardTransactionInfo ccTxnInfo) throws ApplicationException {

		TransactionType txnType = new TransactionType();
		txnType.setAmount(new BigDecimal(new Double(ccTxnInfo.getAmount()).toString()));
		txnType.setReferenceNumber("0"); // we don't have this from our client
		txnType.setTimestamp( new Date());// TODO verify request
		txnType.setLanguage("EN"); // optional

		if (StringUtils.isNotEmpty(ccTxnInfo.getChargeAuthorizationNumber()))
			txnType.setApprovalCode(ccTxnInfo.getChargeAuthorizationNumber());
		if (CreditCardTransactionInfo.TYPE_CREDIT_CARD_VALIDATION.equals(ccTxnInfo.getTransactionType())) {
			txnType.setTransactionType(TRANSACTION_TYPE_AUTHORIZATION_ONLY);
			txnType.setAmount(new BigDecimal(0.01)); // overwrite the amount with 1 cent
		} else if (CreditCardTransactionInfo.TYPE_CREDIT_CARD_PAYMENT.equals(ccTxnInfo.getTransactionType())) {
			txnType.setTransactionType(TRANSACTION_TYPE_PURCHASE);
		} else if (CreditCardTransactionInfo.TYPE_CREDIT_CARD_VOID.equals(ccTxnInfo.getTransactionType())) {
			txnType.setTransactionType(TRANSACTION_TYPE_PAYMENT_VOID);
		} else if (CreditCardTransactionInfo.TYPE_CREDIT_CARD_REFUND.equals(ccTxnInfo.getTransactionType())) {
			txnType.setTransactionType(TRANSACTION_TYPE_REFUND);
		} else if (com.telus.eas.account.info.CreditCardTransactionInfo.TYPE_CREDIT_CARD_REFUND_VOID.equals(ccTxnInfo.getTransactionType())) {
			txnType.setTransactionType(TRANSACTION_TYPE_REFUND_VOID);
		} else {
			throw new ApplicationException(ERROR_CODE_WPS_ERROR, "Unknow credit card transactionType:" + ccTxnInfo.getTransactionType(), "");
		}
		return txnType;
	}

	private CreditCardType mapCreditCardInfo(@Sensitive CreditCardInfo ccInfo) {
		CreditCardDetailsType ccDetail = new CreditCardDetailsType();
		ccDetail.setToken(new BigInteger(ccInfo.getToken()));
		if (StringUtils.isNotEmpty(ccInfo.getLeadingDisplayDigits()))
			ccDetail.setFirst6Digits(new BigInteger(ccInfo.getLeadingDisplayDigits()));
		if (StringUtils.isNotEmpty(ccInfo.getTrailingDisplayDigits()))
			ccDetail.setLast4Digits(new BigInteger(ccInfo.getTrailingDisplayDigits()));
		if (StringUtils.isNotEmpty(ccInfo.getCardVerificationData()))
			ccDetail.setCvd(ccInfo.getCardVerificationData());
		ccDetail.setExpiryMonth(ccInfo.getExpiryMonth());
		ccDetail.setExpiryYear(ccInfo.getExpiryYear());
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardDetails(ccDetail);

		return creditCard;
	}

	private ClientInfoType mapClientInfo(CreditCardHolderInfo ccHolder) {
		ClientInfoType clientInfo = new ClientInfoType();
		clientInfo.setBan(ccHolder.getClientID()); // this field can contain empty value.
		if (StringUtils.isNotEmpty(ccHolder.getFirstName()))
			clientInfo.setFirstName(ccHolder.getFirstName());
		if (StringUtils.isNotEmpty(ccHolder.getLastName()))
			clientInfo.setLastName(ccHolder.getLastName());
		if (StringUtils.isNotEmpty(ccHolder.getCivicStreetNumber()))
			clientInfo.setStreetNum(ccHolder.getCivicStreetNumber());
		if (StringUtils.isNotEmpty(ccHolder.getStreetName()))
			clientInfo.setStreetName(ccHolder.getStreetName());
		if (StringUtils.isNotEmpty(ccHolder.getApartmentNumber()))
			clientInfo.setApartmentNum(ccHolder.getApartmentNumber());
		if (StringUtils.isNotEmpty(ccHolder.getCity()))
			clientInfo.setCity(ccHolder.getCity());
		if (StringUtils.isNotEmpty(ccHolder.getProvince()))
			clientInfo.setProvince(ccHolder.getProvince());
		// clientInfo.setCountry( "CA" ); //optional
		if (StringUtils.isNotEmpty(ccHolder.getPostalCode()))
			clientInfo.setPostalCode(ccHolder.getPostalCode());
		if (StringUtils.isNotEmpty(ccHolder.getHomePhone()))
			clientInfo.setHomePhone(ccHolder.getHomePhone()); // TODO verify request
		if (StringUtils.isNotEmpty(ccHolder.getBusinessRole()))
			clientInfo.setBusinessRole(ccHolder.getBusinessRole());// TODO verify request
		if (ccHolder.getActivationDate() != null)
			clientInfo.setActivationDate(ccHolder.getActivationDate());// TODO verify request
		if (StringUtils.isNotEmpty(ccHolder.getAccountType()))
			clientInfo.setAccountTypeCode(ccHolder.getAccountType());
		if (StringUtils.isNotEmpty(ccHolder.getAccountSubType()))
			clientInfo.setAccountSubTypeCode(ccHolder.getAccountSubType());
		return clientInfo;
	}
}
