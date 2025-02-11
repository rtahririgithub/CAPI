package com.telus.api.account;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TELUS Mobility</p>
 * @author Michael Qin
 * @version 1.0
 */

public interface VoiceUsageServiceDirection {

  /**
   * Returns the direction code, such as "0", "1", or "2".
   * Refer to ServicePeriodIncludedMinutes for description of direction code.
   *
   * @return String
   */
  String getDirectionCode();

  /**
   * Returns an array of voice usage service periods
   * @return VoiceUsageServicePeriod[]
   */
  VoiceUsageServicePeriod[] getVoiceUsageServicePeriods();

  /**
   * Returns the number of odd free air calls.
   * @return int
   */
  int getOddFreeAirCalls();

  /**
   * Returns the number of odd special calls.
   * @return int
   */
  int getOddSpecialCalls();

  /**
   * Returns last call date.
   * @return Date
   */
  Date getLastCallDate();
}