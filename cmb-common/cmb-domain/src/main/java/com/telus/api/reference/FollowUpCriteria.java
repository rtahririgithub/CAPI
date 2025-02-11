package com.telus.api.reference;

import java.util.Date;

public interface FollowUpCriteria {
    static final String FOLLOW_UP_STATUS_OPEN = "O";
    static final String FOLLOW_UP_STATUS_CLOSED  = "C";
    static final String FOLLOW_UP_STATUS_ALL = "A";
    static final String FOLLOW_UP_GROUP_ASSIGNED_TO = "A";
    static final String FOLLOW_UP_GROUP_CREATED_BY = "C";
    static final String FOLLOW_UP_GROUP_BOTH = "B";

    String getWorkPositionId();
    void setWorkPositionId(String workPositionId);

    String getFollowUpStatus();
    void setFollowUpStatus(String followUpStatus);

    String getFollowUpGroup();
    void setFollowUpGroup(String followUpGroup);

    String getFollowUpType();
    void setFollowUpType(String followUpType);

    Date getDueDateFrom();
    void setDueDateFrom(Date dueDateFrom);

    Date getDueDateTo();
    void setDueDateTo(Date dueDateTo);

    String getAccountStatus();
    void setAccountStatus(String accountStatus);

    int getBillCycleCloseDay();
    void setBillCycleCloseDay(int billCycleCloseDay);

    double getAccountBalanceFrom();
    void setAccountBalanceFrom(double accountBalanceFrom);

    double getAccountBalanceTo();
    void setAccountBalanceTo(double accountBalanceTo);

    void clear();
}
