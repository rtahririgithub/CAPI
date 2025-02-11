/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import java.util.Date;

import com.telus.api.LimitExceededException;
import com.telus.api.TelusAPIException;
import com.telus.api.account.AuditHeader;
import com.telus.api.account.CLMSummary;
import com.telus.api.account.ConsumerName;
import com.telus.api.account.CreditCheckNotRequiredException;
import com.telus.api.account.CreditCheckResult;
import com.telus.api.account.FeeWaiver;
import com.telus.api.account.IDENPostpaidConsumerAccount;
import com.telus.api.account.InvalidBillCycleChangeException;
import com.telus.api.account.PCSPostpaidConsumerAccount;
import com.telus.api.account.PagerPostpaidConsumerAccount;
import com.telus.api.account.PaymentMethod;
import com.telus.api.account.PersonalCredit;
import com.telus.api.account.SubscribersByDataSharingGroupResult;
import com.telus.api.account.UnknownBANException;
import com.telus.api.reference.BillCycle;
import com.telus.api.servicerequest.ServiceRequestHeader;

public class PostpaidConsumerAccountInfo extends AccountInfo implements IDENPostpaidConsumerAccount, PCSPostpaidConsumerAccount, PagerPostpaidConsumerAccount {
   static final long serialVersionUID = 1L;

  public static PostpaidConsumerAccountInfo newPCSInstance() {
    return new PostpaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PCS_REGULAR);
  }

  public static PostpaidConsumerAccountInfo newIDENInstance() {
    return new PostpaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_IDEN_REGULAR);
  }

  public static PostpaidConsumerAccountInfo newPagerInstance() {
    return new PostpaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_PAGER_REGULAR);
  }

  public static PostpaidConsumerAccountInfo newAutotelInstance() {
    return new PostpaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, ACCOUNT_SUBTYPE_AUTOTEL_REGULAR);
  }
  public static PostpaidConsumerAccountInfo getNewInstance(char accountSubType) {
	    return new PostpaidConsumerAccountInfo(ACCOUNT_TYPE_CONSUMER, accountSubType);
  }

  private ConsumerNameInfo name = new ConsumerNameInfo();
  private PersonalCreditInfo personalCreditInformation = new PersonalCreditInfo();
  private PaymentMethodInfo paymentMethod = new PaymentMethodInfo();

  protected PostpaidConsumerAccountInfo(char accountType, char accountSubType) {
    super(accountType, accountSubType);
  }

  /**
   * NO-OP
   */
  public CreditCheckResult checkCredit() throws TelusAPIException, CreditCheckNotRequiredException  {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * NO-OP
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

  public boolean isPostpaidConsumer() {
    return true;
  }
*/

  public ConsumerName getName() {
    return name;
  }

  public ConsumerNameInfo getName0() {
    return name;
  }

  public String getFullName() {
    return getName0().getFullName();
  }

  /**
   * This method has been deprecated in favour of using methods on the name object.
   *
   * @deprecated
   * @see #getName
   * @see #getName0
   */
  public void setFullName(String fullName) {
    throw new UnsupportedOperationException("Use getName().setXXX()");
  }

  /**
   * This method has been deprecated in favour of using the name object.  Additional name info may
   * be accessed via getAdditionalLine and setAdditionalLine methods there.
   *
   * @deprecated
   * @see #getName
   * @see #getName0
   */
  public String getAdditionalName() {
    return getAdditionalLine();
  }

  /**
   * This method has been deprecated in favour of using the name object.  Additional name info may
   * be accessed via getAdditionalLine and setAdditionalLine methods there.
   *
   * @deprecated
   * @see #getName
   * @see #getName0
   */
  public void setAdditionalName(String additionalName) {
    setAdditionalLine(toUpperCase(additionalName));
  }

  /**
   * For PostpaidConsumer accounts, additionalLine info is now stored in the name object.
   * The following getter method overrides the AccountInfo superclass method and provides
   * access to the name object through the getName method.
   *
   * @see #getName
   * @see #getName0
   */
  public String getAdditionalLine() {
    return getName().getAdditionalLine();
  }

  /**
   * For PostpaidConsumer accounts, additionalLine info is now stored in the name object.
   * The following setter method overrides the AccountInfo superclass method and provides
   * access to the name object through the getName method.
   *
   * @see #getName
   * @see #getName0
   */
  public void setAdditionalLine(String additionalLine) {
    getName().setAdditionalLine(toUpperCase(additionalLine));
  }


  /**
   * This method has been deprecated in favour of using the getPersonalCreditInformation method.
   *
   * @deprecated
   * @see #getPersonalCreditInformation
   * @see #getPersonalCreditInformation0
   */
  public PersonalCredit getCreditInformation() {
    return getPersonalCreditInformation();
  }

  /**
   * This method has been deprecated in favour of using the getPersonalCreditInformation method.
   *
   * @deprecated
   * @see #getPersonalCreditInformation
   * @see #getPersonalCreditInformation0
   */
  public PersonalCreditInfo getCreditInformation0() {
    return getPersonalCreditInformation0();
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
   * TODO	The methods below are duplicated in TMPostpaidBusinessRegulerAccount.  They should be moved to
   * 		a common super class...
   */

  /**
   * NO-OP
   */
  public BillCycle[] getAvailableBillCycles() throws TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * NO-OP
   */
  public BillCycle getBillingCycle() {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * NO-OP
   */
  public void changeBillCycle(BillCycle cycle) throws TelusAPIException, InvalidBillCycleChangeException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void changeBillCycle(BillCycle cycle, boolean testFirstCycleRun) throws TelusAPIException, InvalidBillCycleChangeException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  /**
   * NO-OP
   */
  public void testCycleChange() throws InvalidBillCycleChangeException, TelusAPIException {
    throw new UnsupportedOperationException("Method not implemented here");
  }

  public void copyFrom(AccountInfo o) {
    if(o instanceof PostpaidConsumerAccountInfo) {
      copyFrom((PostpaidConsumerAccountInfo)o);
    } else {
      super.copyFrom(o);
    }
  }

  public void copyFrom(PostpaidConsumerAccountInfo o) {
    super.copyFrom(o);

    name.copyFrom(o.name);
    personalCreditInformation.copyFrom(o.personalCreditInformation);
    paymentMethod.copyFrom(o.paymentMethod);
  }

  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("PostpaidConsumerAccountInfo:{\n");
    s.append(super.toString());
    s.append("    name=[").append(name).append("]\n");
    s.append("    personalCreditInformation=[").append(personalCreditInformation).append("]\n");
    s.append("    paymentMethod=[").append(paymentMethod).append("]\n");
    s.append("}");

    return s.toString();
  }

  public CLMSummary getCLMSummary() throws TelusAPIException {
    throw new java.lang.UnsupportedOperationException("Method not implemented here");
  }

	public CreditCheckResult checkCredit(AuditHeader auditHeader){
	    throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public SubscribersByDataSharingGroupResult[] getSubscribersByDataSharingGroups(
			String[] codes,Date effectiveDate) throws LimitExceededException, TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
  
}




