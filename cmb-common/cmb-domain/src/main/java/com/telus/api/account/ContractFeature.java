/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import com.telus.api.*;


import com.telus.api.reference.*;

/**
 * <CODE>ContractFeature</CODE>
 *
 */
public interface ContractFeature extends Reference {

  /**
   * @link aggregation
   */
  RatedFeature getFeature();
  String getParameter();
  void setParameter(String parameter);
  java.util.Date getExpiryDate();
  void setExpiryDate(java.util.Date expiryDate);
  java.util.Date getEffectiveDate();
  void setEffectiveDate(java.util.Date effectiveDate);
  String  getAdditionalNumber();
  void  setAdditionalNumber(String additionalNumber);
  String getServiceCode();
  CallingCircleParameters getCallingCircleParameters() throws TelusAPIException;
  void setCallingCirclePhoneNumberList( String[] phoneNumbers );
  VoiceToTextOptions getVoiceToTextOptions();
  void setVoiceToTextOptions(VoiceToTextOptions option);
  
  CallingCircleCommitmentAttributeData getCallingCircleCommitmentAttributeData() throws TelusAPIException;
  
  /**
  * This attribute is null if the service is not an x-hour SOC.  If non-null, this attribute contains the time
  * components of the x-hour SOC.
  */
  DurationServiceCommitmentAttributeData getDurationServiceCommitmentAttributeData();
}




