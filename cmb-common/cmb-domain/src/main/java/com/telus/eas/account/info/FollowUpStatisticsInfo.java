package com.telus.eas.account.info;

import com.telus.api.account.FollowUpStatistics;
import com.telus.eas.framework.info.Info;

public class FollowUpStatisticsInfo extends Info implements FollowUpStatistics {

   static final long serialVersionUID = 1L;

  private boolean hasOpenFollowUps;
  private boolean hasDueFollowUps;
  private boolean hasOverdueFollowUps;

  public boolean hasOpenFollowUps() {
    return hasOpenFollowUps;
  }

  public void setHasOpenFollowUps(boolean hasOpenFollowUps) {
    this.hasOpenFollowUps = hasOpenFollowUps;
  }

  public boolean hasDueFollowUps() {
    return hasDueFollowUps;
  }

  public void setHasDueFollowUps(boolean hasDueFollowUps) {
    this.hasDueFollowUps = hasDueFollowUps;
  }

  public boolean hasOverdueFollowUps() {
    return hasOverdueFollowUps;
  }

  public void setHasOverdueFollowUps(boolean hasOverdueFollowUps) {
    this.hasOverdueFollowUps = hasOverdueFollowUps;
  }

}
