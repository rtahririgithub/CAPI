package com.telus.api.reference;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface ServicePromotion extends Reference {

  /**
   * Returns the SOC code
   * @return String
   */
  String getServiceCode();

  /**
   * Returns an array of AdjustmentReason
   * @return AdjustmentReason[]
   */
  AdjustmentReason[] getAdjustmentReasons();

}
