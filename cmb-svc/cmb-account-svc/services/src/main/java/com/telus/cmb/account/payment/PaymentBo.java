package com.telus.cmb.account.payment;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.AuditHeader;
import com.telus.cmb.account.lifecyclefacade.dao.CardPaymentServiceDao;
import com.telus.cmb.account.lifecyclefacade.dao.CconDao;
import com.telus.cmb.common.logging.Sensitive;
import com.telus.eas.account.info.CreditCardInfo;
import com.telus.eas.account.info.CreditCardTransactionInfo;
import com.telus.eas.framework.info.MemoInfo;
import com.telus.eas.utility.info.CreditCardResponseInfo;

public class PaymentBo {

	private CconDao cconDao;
	private CardPaymentServiceDao cardPaymentServiceDao;

	private static Logger LOGGER = Logger.getLogger(PaymentBo.class);
	private static String PAYI_MEMO_TEXT_PATTERN = "Auto Generated Memo: Credit Card Transaction Status-> [CC={0}******{1}; Token={2}],amount={3}, termId={4}; Response code->{5}, Message->{6}  {7}";
	// default response value used if response message definition is missing
	private static String TM_CREDIT_CARD_API_MSG_NUM_REJECTED_TRANSACTION = "100";

	// provide support methods that are out of scope of this Object
	public static interface PaymentProcessCallback {
		// create a memo in KB
		void createPAYIMemo(MemoInfo memoInfo) throws ApplicationException;
	}

	public PaymentBo(CconDao cconDao, CardPaymentServiceDao cardPaymentServiceDao) {
		this.cconDao = cconDao;
		this.cardPaymentServiceDao = cardPaymentServiceDao;
	}

	public CreditCardResponseInfo processPayment(String applicationId, @Sensitive CreditCardTransactionInfo ccTxnInfo, AuditHeader auditHeader, PaymentProcessCallback paymentCallback)
			throws ApplicationException {

		String termId = applicationId + "_" + ccTxnInfo.getBrandId();

		String txnCtx = getTransactionInfo("processCreditCard", termId, ccTxnInfo);
		LOGGER.debug(txnCtx + ", enter.");
		LOGGER.debug("AduitHeader " + auditHeader);

		if (ccTxnInfo.getBan() != 0) {
			String clientId = ccTxnInfo.getCreditCardHolderInfo().getClientID();
			if (clientId == null || clientId.trim().isEmpty()) {
				ccTxnInfo.getCreditCardHolderInfo().setClientID("" + ccTxnInfo.getBan()); //fix for the case where BAN is only set in ccTxnInfo but not CCHolderInfo. In this case BAN is not passed to downstream
			}
		}
		
		
		try {
			CreditCardResponseInfo response = cardPaymentServiceDao.processCreditCard(termId, ccTxnInfo, auditHeader);

			if (response.isApproved()) {
				LOGGER.debug(txnCtx + ", approved, authCode:" + response.getAuthorizationCode());
				return response;
			}

			// the transaction is declined
			LOGGER.info(txnCtx + ", declined, reason:" + response.getResponseCode() + ", " + response.getResponseText());

			String msgId = TM_CREDIT_CARD_API_MSG_NUM_REJECTED_TRANSACTION;

			CCMessage messages = new MessageResolver(cconDao).resolveMessage(applicationId, ccTxnInfo.getCreditCardHolderInfo().getBusinessRole(), msgId, null);

			createPAYIMemo(paymentCallback, ccTxnInfo, termId, msgId, (String) messages.getKbMemoMessage(), "(" + response.getResponseCode() + ")");

			ApplicationException rootException = new ApplicationException(SystemCodes.WPS_GP, response.getResponseCode(), response.getResponseText(), "");

			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, msgId, messages.getEnglishMessage(), messages.getFrenchMessage(), rootException);
		} catch (ApplicationException e) {
			if (SystemCodes.WPS.equals(e.getSystemCode())) { // the CardpaymentServiceSoaExceptionHandler translate PolicyException into ApplicationException with SystemCodes = WPS
				LOGGER.error(txnCtx + " encounted error." + e.getErrorMessage(), e);
				ApplicationException newException = translateWpsPolicyException(paymentCallback, applicationId, ccTxnInfo, termId, e);
				throw newException;
			} else {
				throw e;
			}
		} 
	}

	private void createPAYIMemo(PaymentProcessCallback paymentCallback, @Sensitive CreditCardTransactionInfo ccTxnInfo, String termId, String code, String message, String subMsg) {

		int banId = 0;
		try {
			banId = Integer.parseInt(ccTxnInfo.getCreditCardHolderInfo().getClientID());
		} catch (Exception e) {
			// does not contain BAN id, no need to create memo, just return.
			return;
		}

		if (banId == 0)
			return;

		CreditCardInfo ccInfo = ccTxnInfo.getCreditCardInfo();
		String txnCtx = getTransactionInfo("create PAYI memo ", termId, ccTxnInfo);
		try {

			Object[] msgParams = new Object[] { ccInfo.getLeadingDisplayDigits(), ccInfo.getTrailingDisplayDigits(), ccInfo.getToken(), new Double(ccTxnInfo.getAmount()), termId, (String) code,
					(String) message, subMsg };

			MemoInfo memoInfo = new MemoInfo();
			memoInfo.setBanId(banId);
			memoInfo.setMemoType("PAYI");
			memoInfo.setText(MessageFormat.format(PAYI_MEMO_TEXT_PATTERN, msgParams));
			memoInfo.setSystemText("");
			memoInfo.setDate(new java.util.Date());

			LOGGER.debug(txnCtx + " begin.");
			paymentCallback.createPAYIMemo(memoInfo);
			LOGGER.debug(txnCtx + " end.");

		} catch (Throwable e) {
			LOGGER.error(txnCtx + " failed.", e);
		}
	}

	private String getTransactionInfo(String msg, String termId, @Sensitive CreditCardTransactionInfo ccTxnInfo) {
		StringBuffer sb = new StringBuffer(msg).append(" <termId=").append(termId).append(", type=").append(ccTxnInfo.getTransactionType()).append(", amount=").append(ccTxnInfo.getAmount());
		if (ccTxnInfo.getCreditCardInfo() != null)
			sb.append(", cc=").append("****").append(ccTxnInfo.getCreditCardInfo().getTrailingDisplayDigits());
		if (ccTxnInfo.getCreditCardHolderInfo() != null)
			sb.append(", banId=").append(ccTxnInfo.getCreditCardHolderInfo().getClientID());
		sb.append(">");

		String txnCtx = sb.toString();
		return txnCtx;
	}

	private boolean isErrorCodeFromAvalon(String errorCode) {
		if (errorCode != null && errorCode.startsWith("AV")) {
			return true;
		} else {
			return false;
		}
	}

	private ApplicationException translateWpsPolicyException(PaymentProcessCallback paymentCallback, String applicationId, @Sensitive CreditCardTransactionInfo ccTxnInfo, String termId,
			ApplicationException rootException) {
		String wpsErrorCode = rootException.getErrorCode();
		String wpsErrorMessage = rootException.getErrorMessage();
		MessageResolver messageResovler = new MessageResolver(cconDao);
		String msgId = messageResovler.mapMessageId(wpsErrorCode);

		if (msgId == null) {
			String contactMessage = "Please contact the WPS team for support.";

			if (isErrorCodeFromAvalon(wpsErrorCode)) {
				contactMessage = "This error code with \"AV\" prefix is an error thrown from Avalon. Please contact the Avalon team for support.";
			}

			// cannot find the response code , in this case, we just throw SWpsUnmappedPolicyException
			return new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.WPS_GENERIC_ERROR, "PolicyException was received from WPS service (errorCode=" + wpsErrorCode + ", message="
					+ wpsErrorMessage + "). " + contactMessage, "", rootException);
		}

		CCMessage messages = messageResovler.resolveMessage(applicationId, ccTxnInfo.getCreditCardHolderInfo().getBusinessRole(), msgId, wpsErrorMessage);

		createPAYIMemo(paymentCallback, ccTxnInfo, termId, msgId, (String) messages.getKbMemoMessage(), "(" + wpsErrorCode + ")");

		return new ApplicationException(SystemCodes.CMB_ALF_EJB, msgId, messages.getEnglishMessage(), messages.getFrenchMessage(), rootException);
	}

}