package com.telus.api.account;

/**
 * <p>Class ChargeTypeTax</p>
 * @version 1.0
 */

public interface ChargeTypeTax {
  String getChargeType();
  double getChargeAmount();
  double getGSTTaxAmount();
  double getPSTTaxAmount();
  double getHSTTaxAmount();
  double getRoamingTaxAmount();
  double getAdjustedTaxAmount();
}
