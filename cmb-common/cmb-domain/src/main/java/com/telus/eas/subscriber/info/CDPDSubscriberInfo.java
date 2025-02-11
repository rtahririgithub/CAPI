package com.telus.eas.subscriber.info;

import com.telus.api.account.CDPDSubscriber;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 21-Feb-2006
 */

public class CDPDSubscriberInfo extends SubscriberInfo implements CDPDSubscriber {
  static final long serialVersionUID = 1L;
  
  public String toString() {
    StringBuffer sb = new StringBuffer();

    sb.append("CDPDSubscriberInfo:[\n");
    sb.append(super.toString()).append("]\n");
    sb.append("]");

    return sb.toString();
  }
}
