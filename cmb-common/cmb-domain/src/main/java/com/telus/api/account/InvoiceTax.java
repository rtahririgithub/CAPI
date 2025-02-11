package com.telus.api.account;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface InvoiceTax {
  boolean isGSTExempt();
  boolean isPSTExempt();
  boolean isHSTExempt();
  boolean isRoamingExempt();
  ChargeTypeTax[] getChargeTypeTaxes();
}
