/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.TelusAPIException;

/**
 * <CODE>PostpaidConsumerAccount</CODE>
 *
 */
public interface PostpaidConsumerAccount extends PostpaidAccount {

  /**
   * @link aggregationByValue
   */
  ConsumerName getName();

  /**
   * This method has been deprecated in favour of methods on the ConsumerName interface.
   * Additional name info may be accessed via getAdditionalLine and setAdditionalLine methods there.
   * 
   * @deprecated to be removed in July 2006
   * @see #getName
   */
  String getAdditionalName();

  /**
   * This method has been deprecated in favour of methods on the ConsumerName interface.
   * Additional name info may be accessed via getAdditionalLine and setAdditionalLine methods there.
   * 
   * @deprecated to be removed in July 2006
   * @see #getName
   */
  void setAdditionalName(String additionalName);

  
  /**
   * This method has been deprecated in favour of the getPersonalCreditInformation method
   * on the PostpaidAccount interface.
   * 
   * @deprecated to be removed in July 2006
   * @see #getPersonalCreditInformation on PostpaidAccount
   * 
   * @link aggregationByValue
   */
  PersonalCredit getCreditInformation();

  /**
   * The method has to access the summary table and read one ‘summary’ record for the ban been treated
   * and pass the amount values to the fields so Smart Desktop will be able to display the values
   * @return CLMSummary
   * @throws TelusAPIException
   */
  CLMSummary getCLMSummary() throws TelusAPIException;
  
  /**
   * 
   * @param auditHeader
   * @return CreditCheckResult
   * @throws TelusAPIException
   */
  CreditCheckResult checkCredit(AuditHeader auditHeader) throws TelusAPIException;
  
}


