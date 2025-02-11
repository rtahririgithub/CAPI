package com.telus.cmb.account.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.telus.api.ApplicationException;
import com.telus.api.account.AccountSummary;
import com.telus.cmb.account.bo.PaymentArrangementAndNotificationConstants;
import com.telus.cmb.account.bo.context.ProcessPaymentContext;
import com.telus.eas.account.info.CLMSummaryInfo;
import com.telus.eas.account.info.InvoiceHistoryInfo;

public class ProcessPaymentUtilities {
	
	public static <T extends ProcessPaymentContext> boolean isCLP(T context) {		
		return Arrays.asList(PaymentArrangementAndNotificationConstants.CLP_CREDIT_CLASSES).contains(context.getCreditCheckResult().getCreditClass());
	}
		
	public static <T extends ProcessPaymentContext> boolean isSuspendedDueToCLM(T context) {
		return context.getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED && isCLP(context) 
				&& StringUtils.equals(PaymentArrangementAndNotificationConstants.CLM_SUSPEND_REASON_CODE, context.getAccount().getStatusActivityReasonCode().trim());
	}
	
	public static <T extends ProcessPaymentContext> boolean isSuspendedDueToNonPayment(T context) {
		return context.getAccount().getStatus() == AccountSummary.STATUS_SUSPENDED 
				&& Arrays.asList(PaymentArrangementAndNotificationConstants.NON_PAY_SUSPEND_REASON_CODES).contains(context.getAccount().getStatusActivityReasonCode().trim());
	}
	
	public static <T extends ProcessPaymentContext> double getRequiredMinimumPayment(T context) throws ApplicationException {
		
		if (isCLP(context) && (isSuspendedDueToCLM(context) || isSuspendedDueToNonPayment(context) || context.getAccount().getFinancialHistory().isHotlined())) {
			// If the CLP client is suspended due to CLM or due to non-payment or is hotlined, return the required minimum payment as per the new requirement for IVR phase 3.
			return getCLMSummary(context).getRequiredMinimumPayment();

		} else if (!isCLP(context) && context.getAccount().getFinancialHistory().isHotlined() || isSuspendedDueToNonPayment(context)) {
			// If the non-CLP client is suspended due to non-payment or is hotlined, return the past due balance.
			return context.getAccount().getFinancialHistory().getDebtSummary().getPastDue();

		} else {
			// Otherwise, return 0.
			return 0;
		}
	}

	public static <T extends ProcessPaymentContext> CLMSummaryInfo getCLMSummary(T context) throws ApplicationException {

		// Note: the Provider version of this method (TMPostpaidConsumerAccount.getCLMSummary()) uses a 'default' date set to 2001-01-01 when retrieving invoice history and
		// total data outstanding amount. This has been updated to use the account start service date instead. (R. Fong, 2014.06.19)
		
		double unpaidVoiceUsage = context.getAccountInformationHelper().retrieveUnpaidAirTimeTotal(context.getAccount().getBanId());
		double unpaidDataUsage = 0.0;
		
		if (!context.getInvoiceHistory().isEmpty()) {
			Calendar billCycleCalendar = getBillCycleCalendar(context);
			int billCycleYear = billCycleCalendar.get(Calendar.YEAR);
			int billCycleMonth = billCycleCalendar.get(Calendar.MONTH) + 1;
			unpaidDataUsage = context.getAccountLifecycleFacade().getTotalUnbilledDataAmount(context.getAccount().getBanId(), billCycleYear, billCycleMonth, context.getAccount().getBillCycle());
		} else {
			unpaidDataUsage = context.getAccountLifecycleFacade().getTotalDataOutstandingAmount(context.getAccount().getBanId(), context.getAccount().getStartServiceDate());
		}

		double billedCharges = context.getAccount().getFinancialHistory().getDebtSummary().getCurrentDue() + context.getAccount().getFinancialHistory().getDebtSummary().getPastDue();
		double unpaidUnBilledAmount = getUnpaidUnbilledAmount(context);
		double requiredMinimumPayment = getRequiredMinimumPayment(unpaidVoiceUsage, unpaidDataUsage, billedCharges, unpaidUnBilledAmount, context);
		
		return new CLMSummaryInfo(billedCharges, unpaidVoiceUsage, unpaidDataUsage, requiredMinimumPayment, unpaidUnBilledAmount);
	}
	
	private static <T extends ProcessPaymentContext> Calendar getBillCycleCalendar(T context) {
		
		Date invoiceDate = context.getInvoiceHistory().get(0).getDate();
		Calendar billCycleCalendar = Calendar.getInstance();
		billCycleCalendar.setTime(invoiceDate);
		// Advance last invoice date by one month, so we get current bill cycle
		billCycleCalendar.add(Calendar.MONTH, 1);
		
		return billCycleCalendar;
	}	
	
	private static <T extends ProcessPaymentContext> double getUnpaidUnbilledAmount(T context) {
		
		// Note: if the financial history's debt summary bill due date is null, the Provider version of this method (TMAccount.unpaidUnBilledAmount()) retrieves the account's subscribers
		// and uses the start service date of the first subscriber as the bill due date. This has been updated to use the account start service date instead. (R. Fong, 2014.06.19)

		double totalAmount = 0;
		Date billDueDate = context.getAccount().getFinancialHistory().getDebtSummary().getBillDueDate() != null ? context.getAccount().getFinancialHistory().getDebtSummary().getBillDueDate() 
				: context.getAccount().getStartServiceDate();
		if (billDueDate.before(new Date())) {
			List<InvoiceHistoryInfo> history = getInvoiceHistory(billDueDate, new Date(), context);
			for (InvoiceHistoryInfo info : history) {
				totalAmount += info.getAmountDue() - info.getPastDue();
			}
		}

		return totalAmount;
	}	
	
	private static <T extends ProcessPaymentContext> List<InvoiceHistoryInfo> getInvoiceHistory(Date from, Date to, T context) {
		
		List<InvoiceHistoryInfo> list = new ArrayList<InvoiceHistoryInfo>();
		for (InvoiceHistoryInfo info : context.getInvoiceHistory()) {
			if (!(info.getDate().before(from) || info.getDate().after(to))) {
				list.add(info);
			}
		}
		
		return list;
	}
	
	private static <T extends ProcessPaymentContext> double getRequiredMinimumPayment(double unpaidVoiceUsage, double unpaidDataUsage, double billedCharges, double unpaidUnBilledAmount, T context) {
		
		double requiredMinimumPayment = (unpaidVoiceUsage + unpaidDataUsage + unpaidUnBilledAmount) - (context.getCreditCheckResult().getLimit() / 2);
		requiredMinimumPayment = requiredMinimumPayment < 0 ? billedCharges : (requiredMinimumPayment + billedCharges);
		requiredMinimumPayment = requiredMinimumPayment < 0 ? 0 : requiredMinimumPayment;
		
		return requiredMinimumPayment;
	}
	
	public static <T extends ProcessPaymentContext> void removeHotlineIndicator(T context) throws ApplicationException {
		
		// Remove the hotline indicator
		if (context.getAccount().getFinancialHistory().isHotlined()) {
			context.getAccount().getFinancialHistory0().setHotlined(false);
			context.getAccount().getAddress0().normalize();
			context.getAccount().getAlternateCreditCheckAddress0().normalize();
			context.getAccountLifecycleFacade().updateAccount(context.getAccount(), context.getAccountLifecycleFacadeSessionId());
		}	
	}
	
}