package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 2-Nov-2006
 */

public final class BrandSwapRuleInfo extends Info {
  static final long serialVersionUID = 1L;

  private long brandSwapRuleId;
  private String brandSwapRuleDescription;
  private String brandId1;
  private String brandId2;
  private String swapType;
  private String swapApp;
  private boolean inclusiveApp;
  private long messageId;

  public long getBrandSwapRuleId() {
    return brandSwapRuleId;
  }

  public void setBrandSwapRuleId(long brandSwapRuleId) {
    this.brandSwapRuleId = brandSwapRuleId;
  }

  public String getBrandSwapRuleDescription() {
    return brandSwapRuleDescription;
  }

  public void setBrandSwapRuleDescription(String brandSwapRuleDescription) {
    this.brandSwapRuleDescription = brandSwapRuleDescription;
  }

  public String getBrandId1() {
    return brandId1;
  }

  public void setBrandId1(String brandId1) {
    this.brandId1 = brandId1;
  }

  public String getBrandId2() {
    return brandId2;
  }

  public void setBrandId2(String brandId2) {
    this.brandId2 = brandId2;
  }

  public String getSwapType() {
    return swapType;
  }

  public void setSwapType(String swapType) {
    this.swapType = swapType;
  }

  public String getSwapApp() {
    return swapApp;
  }

  public void setSwapApp(String swapApp) {
    this.swapApp = swapApp;
  }

  public boolean isInclusiveApp() {
    return inclusiveApp;
  }

  public void setInclusiveApp(boolean inclusiveApp) {
    this.inclusiveApp = inclusiveApp;
  }

  public long getMessageId() {
    return messageId;
  }

  public void setMessageId(long messageId) {
    this.messageId = messageId;
  }
}
