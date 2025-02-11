package com.telus.eas.utility.info;

import com.telus.eas.framework.info.Info;

/**
 * @author Vladimir Tsitrin
 * @version 1.0, 6-Feb-2006
 */

public final class SwapRuleInfo extends Info {
  static final long serialVersionUID = 1L;

  private long swapGroupId;
  private String swapGroupDescription;
  private String productType1;
  private String equipmentType1;
  private String productClass1;
  private String productType2;
  private String equipmentType2;
  private String productClass2;
  private String swapType;
  private String swapApp;
  private boolean inclusiveApp;
  private long messageId;

  public long getSwapGroupId() {
    return swapGroupId;
  }

  public void setSwapGroupId(long swapGroupId) {
    this.swapGroupId = swapGroupId;
  }

  public String getSwapGroupDescription() {
    return swapGroupDescription;
  }

  public void setSwapGroupDescription(String swapGroupDescription) {
    this.swapGroupDescription = swapGroupDescription;
  }

  public String getProductType1() {
    return productType1;
  }

  public void setProductType1(String productType1) {
    this.productType1 = productType1;
  }

  public String getEquipmentType1() {
    return equipmentType1;
  }

  public void setEquipmentType1(String equipmentType1) {
    this.equipmentType1 = equipmentType1;
  }

  public String getProductClass1() {
    return productClass1;
  }

  public void setProductClass1(String productClass1) {
    this.productClass1 = productClass1;
  }

  public String getProductType2() {
    return productType2;
  }

  public void setProductType2(String productType2) {
    this.productType2 = productType2;
  }

  public String getEquipmentType2() {
    return equipmentType2;
  }

  public void setEquipmentType2(String equipmentType2) {
    this.equipmentType2 = equipmentType2;
  }

  public String getProductClass2() {
    return productClass2;
  }

  public void setProductClass2(String productClass2) {
    this.productClass2 = productClass2;
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
