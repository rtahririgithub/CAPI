package com.telus.api.account;

import com.telus.api.*;
import com.telus.api.reference.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.util.*;

public interface Discount {

  void setDiscountCode(String discountCode);

  String getDiscountCode();

  /**
   * Returns the description (meta-discount) for for this discount.
   *
   * <P>This method may involve a remote method call.
   *
   */
  DiscountPlan getDiscountPlan() throws TelusAPIException;

  void setEffectiveDate(Date effectiveDate);

  Date getEffectiveDate();

  void setExpiryDate(Date expiryDate);

  Date getExpiryDate();

  /**
   * Applies a discount.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public void apply() throws TelusAPIException;

  /**
   * Expires this discount so that it is no longer effective.  This method is
   * a short hand for setting the expiry date to today, then calling apply.
   *
   * <P>This method may involve a remote method call.
   *
   */
  public void expire() throws TelusAPIException;
  
  /**
   * Returns the Knowbility Operator ID.
   *
   */
  
  public String getKnowbilityOperatorID();
 
  /**
   * Returns the Discount By User Id.
   *
   */
  
  public String getDiscountByUserId();
  
}