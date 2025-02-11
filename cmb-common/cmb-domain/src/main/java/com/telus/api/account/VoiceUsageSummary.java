/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

public interface VoiceUsageSummary extends UsageSummary {

  /**
   * Returns array of voice usage directions.
   *
   * @return VoiceUsageService[]
   */
  VoiceUsageService[] getVoiceUsageServices();


  /**
   * Returns a boolean to indicate whether the airtime usage is viewable.
   * @return boolean
   */
  boolean isViewable();

}



