package com.telus.eas.utility.info;

/**
 * @author Roman Tov
 * @version 1.0, 20-Jun-2006
 */
import com.telus.api.reference.PaymentTransferReason;

public class PaymentTransferReasonInfo extends ReferenceInfo implements PaymentTransferReason {

  public PaymentTransferReasonInfo(String code, String desc, String descFr) {
    this.code = code;
    this.description = desc;
    this.descriptionFrench = descFr;
  }
}
