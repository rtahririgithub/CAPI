/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.Date;

import com.telus.api.*;

/**
 * <CODE>CreditCheckResult</CODE>
 *
 */
public interface CreditCheckResult {
	
	String getCreditClass();
	
	double getLimit();
	
	String getMessage();
	
	String getMessageFrench();
	
	double getDeposit();
	
	CreditCheckResultDeposit[] getDeposits();
	
	boolean isReferToCreditAnalyst();
	
	void setReferToCreditAnalyst(boolean referToCreditAnalyst);
	
	int getCreditScore();
	
	int getUniqueCode();
	
	/**
	 *  <CODE>true</CODE> if a credit check has been performed on the owning
	 *  account, otherwise <CODE>false</CODE>.
	 */
	boolean isCreditCheckPerformed();
	
	String getDepositBarCode();
		
	/**
	 *  Updates credit class, independent of Account update functions,
	 *  i.e. this method is not called when the Account is saved.
	 *  
	 *  This method invokes a remote call.
   *
	 */
	void updateCreditClass(int ban, String newCreditClass, String memoText) throws TelusAPIException;

  /**
   * updates Credit Class/limit. i.e. called when the account is saved
   * added by Roman
   */
  void updateCreditProfile(int ban, String newCreditClass, double newCreditLimit, String memoText) throws TelusAPIException;

  ActivationOption[] getActivationOptions() throws TelusAPIException;
  
  int getErrorCode();
  String getErrorMessage();
  String getBureauFile();
  String getDefaultInd();
  ConsumerName getLastCreditCheckName();
  Address getLastCreditCheckAddress();
  PersonalCredit getLastCreditCheckPersonalnformation();
  String getCreditCheckResultStatus();
  Date getCreditDate();
  String getCreditParamType();
  String getDepositChangeReasonCode();
  BusinessCreditIdentity getLastCreditCheckSelectedBusiness();
  BusinessCreditIdentity[] getLastCreditCheckListOfBusinesses();
  Date getLastCreditCheckIncorporationDate();
  String getLastCreditCheckIncorporationNumber();
  
  CLPActivationOptionDetail getCLPActivationOptionDetail();
}


