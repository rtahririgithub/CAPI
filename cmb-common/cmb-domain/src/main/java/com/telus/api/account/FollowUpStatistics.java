package com.telus.api.account;

public interface FollowUpStatistics {

  boolean hasOpenFollowUps();

  boolean hasDueFollowUps();

  boolean hasOverdueFollowUps();
  
}
