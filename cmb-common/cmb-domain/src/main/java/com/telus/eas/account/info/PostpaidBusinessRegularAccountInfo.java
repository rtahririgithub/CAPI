/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */


package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.BusinessCredit;
import com.telus.api.account.BusinessCreditIdentity;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.IDENPostpaidBusinessRegularAccount;
import com.telus.api.account.InvalidBillCycleChangeException;
import com.telus.api.account.PCSPostpaidBusinessRegularAccount;
import com.telus.api.account.PagerPostpaidBusinessRegularAccount;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PersonalCredit;
import com.telus.api.account.SubscribersByDataSharingGroupResult;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.BillCycle;
import com.telus.api.servicerequest.ServiceRequestHeader;


public class PostpaidBusinessRegularAccountInfo extends AccountInfo implements IDENPostpaidBusinessRegularAccount, PCSPostpaidBusinessRegularAccount, PagerPostpaidBusinessRegularAccount {
	 static final long serialVersionUID = 1L;

  public static PostpaidBusinessRegularAccountInfo newPCSInstance() {
    return new PostpaidBusinessRegularAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_PCS_REGULAR);
  }

  public static PostpaidBusinessRegularAccountInfo newIDENInstance() {
    return new PostpaidBusinessRegularAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_IDEN_REGULAR);
  }

  public static PostpaidBusinessRegularAccountInfo newPagerInstance() {
    return new PostpaidBusinessRegularAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_PAGER_REGULAR);
  }

  public static PostpaidBusinessRegularAccountInfo newAutotelInstance() {
    return new PostpaidBusinessRegularAccountInfo(ACCOUNT_TYPE_BUSINESS, ACCOUNT_SUBTYPE_AUTOTEL_REGULAR);
  }
  
  public static PostpaidBusinessRegularAccountInfo getNewInstance(char accountSubType) {
	return new PostpaidBusinessRegularAccountInfo(ACCOUNT_TYPE_BUSINESS, accountSubType);
  }

  private String legalBusinessName;
  //private String tradeNameAttention ;
//  private String firstName;
//  private String lastName;
  private BusinessCreditInfo creditInformation = new BusinessCreditInfo();
  private PaymentMethodInfo paymentMethod = new PaymentMethodInfo();
  private PersonalCreditInfo personalCreditInformation = new PersonalCreditInfo();

  protected PostpaidBusinessRegularAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }


  /**
   *  NO-OP
   */
  public BusinessCreditIdentity[] getBusinessCreditIdentities() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }


  /**
   *  NO-OP
   */
  public CreditCheckResult checkCredit(BusinessCreditIdentity identity) throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }


  /**
   *  NO-OP
   */
  public void savePaymentMethod(PaymentMethod newPaymentMethod) throws UnknownBANException, TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * NO-OP
   */
  public void savePaymentMethod(PaymentMethod newPaymentMethod, ServiceRequestHeader header) throws UnknownBANException, TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

/*
  public boolean isPostpaid() {
    return true;
  }

  public boolean isPostpaidBusinessRegular() {
    return true;
  }
*/

  public String getLegalBusinessName() {
    return legalBusinessName;
  }

  public void setLegalBusinessName(String legalBusinessName) {
    this.legalBusinessName = toUpperCase(legalBusinessName);
  }

  public String getFullName() {
    return legalBusinessName;
  }

  public void setFullName(String fullName) {
    throw new UnsupportedOperationException("Use setLegalBusinessName()");
  }

  public String getTradeNameAttention() {
    return getAdditionalLine();
  }

  public void setTradeNameAttention(String tradeNameAttention) {
    setAdditionalLine(toUpperCase(tradeNameAttention));
  }

  public String getFirstName() {
    return getContactName().getFirstName();
  }

  public String getLastName() {
    return getContactName().getLastName();
  }

  public BusinessCredit getCreditInformation() {
    return creditInformation;
  }

  public BusinessCreditInfo getCreditInformation0() {
    return creditInformation;
  }

  public PersonalCredit getPersonalCreditInformation() {
    return personalCreditInformation;
  }

  public PersonalCreditInfo getPersonalCreditInformation0() {
    return personalCreditInformation;
  }

  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public PaymentMethodInfo getPaymentMethod0() {
    return paymentMethod;
  }

  public FeeWaiver newFeeWaiver(String typeCode) {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

  public FeeWaiver[] getFeeWaivers() throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

/**
  * Note:  The methods below are duplicated in TMPostpaidConsumerAccount.  They should be moved to
  * a common super class...
  */
  /**
    * NO-OP
    *
    */
  public BillCycle[] getAvailableBillCycles() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
    * NO-OP
    *
    */
  public BillCycle getBillingCycle() {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
    * NO-OP
    *
    */
  public void changeBillCycle(BillCycle cycle) throws TelusAPIException, InvalidBillCycleChangeException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void changeBillCycle(BillCycle cycle, boolean testFirstCycleRun) throws TelusAPIException, InvalidBillCycleChangeException {
    throw new UnsupportedOperationException("Method not implemented here");
  }
  /**
    * NO-OP
    *
    */
  public void testCycleChange() throws InvalidBillCycleChangeException, TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void copyFrom(AccountInfo o) {
    if(o instanceof PostpaidBusinessRegularAccountInfo) {
      copyFrom((PostpaidBusinessRegularAccountInfo)o);
    } else {
      super.copyFrom(o);
    }
  }

  public void copyFrom(PostpaidBusinessRegularAccountInfo o) {
    super.copyFrom(o);
    setLegalBusinessName(o.legalBusinessName);
    creditInformation.copyFrom(o.creditInformation);
    paymentMethod.copyFrom(o.paymentMethod);
  }



  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidBusinessRegularAccountInfo:{\n");
    s.append(super.toString());
    s.append("    legalBusinessName=[").append(legalBusinessName).append("]\n");
    s.append("creditInformation=[").append(creditInformation).append("]\n");
    s.append("paymentMethod=[").append(paymentMethod).append("]\n");
    s.append("}");

    return s.toString();
  }

	public SubscribersByDataSharingGroupResult[] getSubscribersByDataSharingGroups(
			String[] codes,Date effectiveDate) throws LimitExceededException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}

}




