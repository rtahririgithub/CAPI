package com.telus.eas.account.info;

import com.telus.api.account.*;
import com.telus.eas.framework.info.Info;
import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

public class VoiceUsageServiceDirectionInfo extends Info implements VoiceUsageServiceDirection {

   static final long serialVersionUID = 1L;

  private String directionCode;
  private VoiceUsageServicePeriod[] voiceUsageServicePeriods;
  private int oddFreeAirCalls;
  private int oddSpecialCalls;
  private Date lastCallDate;

  public String getDirectionCode() {
    return directionCode;
  }

  public void setDirectionCode(String directionCode) {
    this.directionCode = directionCode;
  }

  public VoiceUsageServicePeriod[] getVoiceUsageServicePeriods() {
    return voiceUsageServicePeriods;
  }

  public void setVoiceUsageServicePeriods(VoiceUsageServicePeriod[] voiceUsageServicePeriods) {
    this.voiceUsageServicePeriods = voiceUsageServicePeriods;
  }

  public int getOddFreeAirCalls() {
    return oddFreeAirCalls;
  }

  public void setOddFreeAirCalls(int oddFreeAirCalls){
    this.oddFreeAirCalls = oddFreeAirCalls;
  }

  public int getOddSpecialCalls() {
    return oddSpecialCalls;
  }

  public void setOddSpecialCalls(int oddSpecialCalls){
    this.oddSpecialCalls = oddSpecialCalls;
  }

  public Date getLastCallDate() {
    return lastCallDate;
  }

  public void setLastCallDate(Date lastCallDate) {
    this.lastCallDate = lastCallDate;
  }

}