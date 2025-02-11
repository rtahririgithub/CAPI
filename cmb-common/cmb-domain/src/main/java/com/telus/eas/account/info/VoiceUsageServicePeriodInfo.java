/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

public class VoiceUsageServicePeriodInfo extends Info implements VoiceUsageServicePeriod {

   static final long serialVersionUID = 1L;

  private String periodCode;
  private int calls;
  private double totalUsed;
  private double included;
  private double includedUsed;
  private double free;
  private double remaining;
  private double chargeable;
  private double chargeAmount;

  public String getPeriodCode(){
    return periodCode;
  }

  public void setPeriodCode(String periodCode){
    this.periodCode = periodCode;
  }

  public int getCalls(){
    return calls;
  }

  public void setCalls(int calls){
    this.calls = calls;
  }

  public double getTotalUsed(){
    return totalUsed;
  }

  public void setTotalUsed(double totalUsed){
    this.totalUsed = totalUsed;
  }

  public double getIncluded(){
    return included;
  }

  public void setIncluded(double included){
    this.included = included;
  }

  public double getIncludedUsed(){
    return includedUsed;
  }

  public void setIncludedUsed(double includedUsed){
    this.includedUsed = includedUsed;
  }

  public double getFree(){
    return free;
  }

  public void setFree(double free){
    this.free = free;
  }

  public double getRemaining(){
    return remaining;
  }

  public void setRemaining(double remaining){
    this.remaining = remaining;
  }

  public double getChargeable(){
    return chargeable;
  }

  public void setChargeable(double chargeable){
    this.chargeable = chargeable;
  }

  public double getChargeAmount(){
    return chargeAmount;
  }

  public void setChargeAmount(double chargeAmount){
    this.chargeAmount = chargeAmount;
  }
}



