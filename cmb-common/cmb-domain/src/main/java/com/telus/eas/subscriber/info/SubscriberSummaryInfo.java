/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.eas.subscriber.info;


public class SubscriberSummaryInfo {

  static final long serialVersionUID = 1L;

  private int banId;
  private String productType;
  private String subscriberId;

  public SubscriberSummaryInfo() {
  }

  public SubscriberSummaryInfo(SubscriberInfo info) {
    banId        = info.getBanId();
    productType  = info.getProductType();
    subscriberId = info.getSubscriberId();

    System.err.println(this);
  }

  public int getBanId() {
    return banId;
  }

  public void setBanId(int banId) {
    this.banId = banId;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public String getSubscriberId() {
    return subscriberId;
  }

  public void setSubscriberId(String subscriberId) {
    this.subscriberId = subscriberId;
  }

    public String toString()
    {
        StringBuffer s = new StringBuffer(128);

        s.append("SubscriberSummaryInfo:[\n");
        s.append("    banId=[").append(banId).append("]\n");
        s.append("    productType=[").append(productType).append("]\n");
        s.append("    subscriberId=[").append(subscriberId).append("]\n");
        s.append("]");

        return s.toString();
    }

}



