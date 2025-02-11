package com.telus.cmb.account.bo;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.account.AccountSummary;
import com.telus.cmb.account.bo.context.ProcessPaymentContext;
import com.telus.cmb.account.utilities.ProcessPaymentUtilities;

public class ProcessPaymentBo {

	private static final Log logger = LogFactory.getLog(ProcessPaymentBo.class);
	private final ProcessPaymentContext context;

	public ProcessPaymentBo(ProcessPaymentContext context) {
		this.context = context;
	}

	public void restoreSuspendedAccount(double paymentAmount) throws ApplicationException {

		// Note: For CLP clients, if the account is suspended due to CLM (reason code 'CLMS') and the required minimum payment is less than or equal to the payment amount,
		// then restore the account. For all other clients, if the account is suspended due to non-payment (reason codes 'SNP' and 'SNP1') or is hotlined and the payment
		// amount is greater than or equal to the past due amount, then restore the account and if required, remove the hotline indicator. Otherwise, do nothing.
				
		// Validate account is not a prepaid account
		if (context.getAccount().getAccountType() == AccountSummary.ACCOUNT_TYPE_CONSUMER && context.getAccount().getAccountSubType() == AccountSummary.ACCOUNT_SUBTYPE_PCS_PREPAID) {
			throw new ApplicationException(SystemCodes.CMB_ALF_EJB, ErrorCodes.INVALID_ACCOUNT_TYPE, "Invalid account type - cannot use restoreSuspendedAccount on prepaid account.");
		}
		
		// Validate minimum payment requirement has been met before restoring service
		if (paymentAmount >= ProcessPaymentUtilities.getRequiredMinimumPayment(context)) {
			
			if (ProcessPaymentUtilities.isSuspendedDueToCLM(context)) {
				// Restore the CLP account
				context.getAccountLifecycleFacade().restoreSuspendedAccount(context.getAccount().getBanId(), new Date(), PaymentArrangementAndNotificationConstants.CLM_RESTORE_REASON_CODE,
						"Minimum payment paid, account to be restored by CLM Rule " + context.getClientIdentity().getApplication(), false, context.getAccount().getBrandId(),
						context.getAccount().isPostpaidBusinessConnect(), context.getAccountLifecycleFacadeSessionId());
				logger.debug("Account successfully restored for CLP client.");
				
			} else {
				// Restore the non-CLP account only if it is NOT delinquent (delinquent accounts are restored by KB when payment is applied)
				if (ProcessPaymentUtilities.isSuspendedDueToNonPayment(context) && !context.getAccount().getFinancialHistory().isDelinquent()) {
					context.getAccountLifecycleFacade().restoreSuspendedAccount(context.getAccount().getBanId(), new Date(), PaymentArrangementAndNotificationConstants.PAYMENT_RECEIVED_REASON_CODE,
							"Balance paid using " + context.getClientIdentity().getApplication(), true, context.getAccount().getBrandId(), context.getAccount().isPostpaidBusinessConnect(),
							context.getAccountLifecycleFacadeSessionId());
					logger.debug("Account successfully restored for non-CLP client.");
				}

				// Remove the hotline indicator
				if (context.getAccount().getFinancialHistory().isHotlined()) {
					ProcessPaymentUtilities.removeHotlineIndicator(context);
					logger.debug("Account hotline indicator successfully removed.");
				}
			}
		}
	}
	
}