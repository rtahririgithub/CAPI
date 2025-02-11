package com.telus.provider.account;

import java.util.Date;
import java.util.List;

import com.telus.api.TelusAPIException;
import com.telus.api.account.InvoiceHistory;
import com.telus.api.account.SubscriberInvoiceDetail;
import com.telus.eas.account.info.InvoiceHistoryInfo;
import com.telus.eas.account.info.SubscriberInvoiceDetailInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class TMInvoiceHistory extends BaseProvider implements InvoiceHistory {

  private InvoiceHistoryInfo delegate;

  public TMInvoiceHistory(TMProvider provider, InvoiceHistoryInfo delegate) {
    super(provider);
    this.delegate = delegate;
  }

  public Date getDate() {
    return delegate.getDate();
  }

  public boolean getMailedIndicator() {
    return delegate.getMailedIndicator();
  }

  public Date getDueDate() {
    return delegate.getDueDate();
  }

  public double getPreviousBalance() {
    return delegate.getPreviousBalance();
  }

  public double getInvoiceAmount() {
    return delegate.getInvoiceAmount();
  }

  public double getAmountDue() {
    return delegate.getAmountDue();
  }

  public double getLatePaymentCharge() {
    return delegate.getLatePaymentCharge();
  }

  public double getPastDue() {
    return delegate.getPastDue();
  }

  public double getAdjustmentAmount() {
    return delegate.getAdjustmentAmount();
  }

  public double getPaymentReceivedAmount() {
    return delegate.getPaymentReceivedAmount();
  }

  public double getCurrentCharges() {
    return delegate.getCurrentCharges();
  }

  public double getTotalTax() {
    return delegate.getTotalTax();
  }

  public int getBillSeqNo() {
    return delegate.getBillSeqNo();
  }

  public int getCycleRunYear() {
    return delegate.getCycleRunYear();
  }
  public int getCycleRunMonth() {
    return delegate.getCycleRunMonth();
  }

  public int getCycleCode() {
    return delegate.getCycleCode();
  }

  public String getStatus() {
    return delegate.getStatus();
  }

  public int getHomeCallCount() {
    return delegate.getHomeCallCount();
  }

  public int getRoamingCallCount() {
    return delegate.getRoamingCallCount();
  }

  public double getHomeCallMinutes() {
    return delegate.getHomeCallMinutes();
  }

  public double getRoamingCallMinutes() {
    return delegate.getRoamingCallMinutes();
  }

  public double getMonthlyRecurringCharge() {
    return delegate.getMonthlyRecurringCharge();
  }

  public double getLocalCallingCharges() {
    return delegate.getLocalCallingCharges();
  }

  public double getOtherCharges() {
    return delegate.getOtherCharges();
  }

  public double getZoneUsageCharges() {
    return delegate.getZoneUsageCharges();
  }

  public double getEHAUsageCharges() {
    return delegate.getEHAUsageCharges();
  }

  public SubscriberInvoiceDetail[] getSubscriberInvoiceDetails() throws TelusAPIException {
	  SubscriberInvoiceDetail[] details = null;
	  try {
		  List list = provider.getAccountInformationHelper().retrieveSubscriberInvoiceDetails(delegate.getBanId(), delegate.getBillSeqNo());
		  details=(SubscriberInvoiceDetailInfo[])list.toArray(new SubscriberInvoiceDetailInfo[list.size()]);
	  } catch (Throwable t) {
		  provider.getExceptionHandler().handleException(t);
	  }

	  return details;
  }

}
