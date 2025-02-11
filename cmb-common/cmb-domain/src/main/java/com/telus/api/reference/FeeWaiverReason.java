package com.telus.api.reference;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface FeeWaiverReason extends Reference {
  String CODE_TERMINATION_LIABILITY = "FEW";
  String CODE_LATE_PAYMENT_CHARGE = "WLPC";
  String CODE_MEDICAL = "WTLCMD";
  String CODE_NO_CONTRACT = "WTLCNC";
  String CODE_OTHER = "WTLCO";
  String CODE_RETENTION = "WTLCR";
}
