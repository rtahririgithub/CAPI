/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.account.info;

import com.telus.api.account.VoiceUsageService;
import com.telus.api.account.VoiceUsageSummary;


public class VoiceUsageSummaryInfo extends UsageSummaryInfo implements VoiceUsageSummary {

   static final long serialVersionUID = 1L;

  private VoiceUsageService[] voiceUsageServices;
  private boolean viewable;

  public VoiceUsageSummaryInfo() {
    super();
  }

  public VoiceUsageService[] getVoiceUsageServices() {
    return voiceUsageServices;
  }

  public void setVoiceUsageServices(VoiceUsageService[] voiceUsageServices){
    this.voiceUsageServices = voiceUsageServices;
  }

  public boolean isViewable() {
    return viewable;
  }

  public void setViewable(boolean viewable) {
    this.viewable = viewable;
  }

}




