package com.telus.api.account;

import java.util.Date;

public interface SubscriberCommitment {
  String getReasonCode();
  int getMonths();
  Date getStartDate();
  Date getEndDate();
}
