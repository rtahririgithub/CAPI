package com.telus.api.account;

import com.telus.api.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.util.*;

public interface Charge {

  void setText(String newText);
  String getText();
  void setChargeCode(String chargeCode);
  String getChargeCode();
  void setEffectiveDate(Date effectiveDate);
  Date getEffectiveDate();
  void setAmount(double amount);
  double getAmount();
  Date getPeriodCoverageStartDate();
  void setPeriodCoverageStartDate(Date startDate);
  Date getPeriodCoverageEndDate();
  void setPeriodCoverageEndDate(Date endDate);
  
  public double getId();
  public String getFeatureCode();
  public String getFeatureRevenueCode();
  public String getServiceCode();
  public boolean isBalanceImpactFlag();
  public Date getCreationDate();
  public String getReasonCode();
  public double getPSTAmount();
  public double getGSTAmount();
  public double getHSTAmount();
  public char getApprovalStatus();
  public boolean isBalanceIgnoreFlag();
  public String getSubscriberId();
  public int getOperatorId();
  public String getProductType();
  public boolean isBilled();  
  public int getBan();
  public int getBillSequenceNo();

  /**
   * Applies a charge.
   *
   * @param overrideUserLimit <CODE>true</CODE> if the user's per-transaction limit should be ignored.
   *
   * <P>This method may involve a remote method call.
   *
   * @throws LimitExceededException if the user's per-transaction limit is exceeded.
   *
   */
  public double apply(boolean overrideUserLimit) throws LimitExceededException, TelusAPIException;

  /**
   * Applies a charge, ignoring the user's per-transaction limit.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public double apply() throws TelusAPIException;
  /**
   * Adjust a charge
   *
   * @param adjustmentAmount amount of adjustment
   * @param adjustmentReasonCode reason code for adjustment
   * @param memoText text to be stored in memo
   * @param overrideUserLimit overrideUserLimit - true, if the user's per-transaction limit should be ignored.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText, boolean overrideUserLimit) throws LimitExceededException, TelusAPIException;

  /**
   * Adjust a charge, ignoring the user's per-transaction limit.
   *
   * @param adjustmentAmount amount of adjustment
   * @param adjustmentReasonCode reason code for adjustment
   * @param memoText text to be stored in memo
   *
   * <P>This method may involve a remote method call.
   *
   */
  public double adjust(double adjustmentAmount, String adjustmentReasonCode, String memoText) throws TelusAPIException;
  /**
   * Delete a charge
   *
   * @param deletionReasonCode reason code for deletion
   * @param memoText text to be stored in memo
   * @param overrideUserLimit overrideUserLimit - true, if the user's per-transaction limit should be ignored.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public void delete(String deletionReasonCode, String memoText, boolean overrideUserLimit) throws LimitExceededException, TelusAPIException;

  /**
   * Delete a charge, ignoring the user's per-transaction limit.
   *
   * @param deletionReasonCode reason code for deletion
   * @param memoText text to be stored in memo
   *
   * <P>This method may involve a remote method call.
   *
   */
  public void delete(String deletionReasonCode, String memoText) throws TelusAPIException;
  /**
   * Retrieve a list of credits related to this charge.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public Credit[] getRelatedCredits() throws TelusAPIException;
  /**
   * Retrieves roaming tax amount.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public double getRoamingTaxAmount();
  /**
   * indicates if the charge is GST exempt.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public boolean isGSTExempt();
  /**
   * indicates if the charge is PST exempt.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public boolean isPSTExempt();
  /**
   * indicates if the charge is HST exempt.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public boolean isHSTExempt();
  /**
   * indicates if the charge roaming tax exempt.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public boolean isRoamingTaxExempt();

}