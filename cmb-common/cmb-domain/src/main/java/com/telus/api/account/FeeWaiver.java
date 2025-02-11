package com.telus.api.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import com.telus.api.TelusAPIException;
import java.util.Date;

public interface FeeWaiver {

  /**
   * add
   * @throws TelusAPIException
   */
  void add() throws TelusAPIException;

  /**
   * delete
   * @throws TelusAPIException
   */
  void delete() throws TelusAPIException;

  /**
   * update
   * @throws TelusAPIException
   */
  void update() throws TelusAPIException;

  /**
   * Returns String reasonCode.
   * @return String
   */
  String getReasonCode();

  /**
   * Return String typeCode.
   * @return String
   */
  String getTypeCode();

  /**
   * Return EffectiveDate.
   * @return Date
   */
  Date getEffectiveDate();

  /**
   * Return ExpiryDate.
   * @return Date
   */
  Date getExpiryDate();

  /**
   * set EffectiveDate
   * @param effectiveDate Date
   */
  void setEffectiveDate(Date effectiveDate);

  /**
   * set ExpiryDate
   * @param expiryDate Date
   */
  void setExpiryDate(Date expiryDate);

  /**
   * set ReasonCode
   * @param reasonCode String
   */
  void setReasonCode(String reasonCode);
}
