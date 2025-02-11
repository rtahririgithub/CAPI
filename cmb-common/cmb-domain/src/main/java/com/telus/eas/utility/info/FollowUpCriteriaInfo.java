package com.telus.eas.utility.info;

import com.telus.api.reference.FollowUpCriteria;
import com.telus.eas.framework.info.Info;

import java.util.Date;

public class FollowUpCriteriaInfo extends Info implements FollowUpCriteria {

	static final long serialVersionUID = 1L;
    private String workPositionId;
    private String followUpStatus;
    private String followUpGroup;
    private String followUpType;
    private Date dueDateFrom;
    private Date dueDateTo;
    private String accountStatus;
    private int billCycleCloseDay;
    private double accountBalanceFrom;
    private double accountBalanceTo;

    private final int dummyBillCycleCloseDay = 0;
    private final double dummyAccountBalanceFrom = -999999;
    private final double dummyAccountBalanceTo = -999999;

    public FollowUpCriteriaInfo() {
        clear();
    }

    public String getWorkPositionId() {
        return workPositionId;
    }

    public void setWorkPositionId(String workPositionId) {
        this.workPositionId = workPositionId;
    }

    public String getFollowUpStatus() {
        return followUpStatus;
    }

    public void setFollowUpStatus(String followUpStatus) {
        if (followUpStatus.equals(FOLLOW_UP_STATUS_CLOSED) || followUpStatus.equals(FOLLOW_UP_STATUS_ALL)) {
            this.followUpStatus = followUpStatus;
        }
        else {
            this.followUpStatus = FOLLOW_UP_STATUS_OPEN;
        }
    }

    public String getFollowUpGroup() {
        return followUpGroup;
    }

    public void setFollowUpGroup(String followUpGroup) {
        if (followUpGroup.equals(FOLLOW_UP_GROUP_CREATED_BY) || followUpGroup.equals(FOLLOW_UP_GROUP_BOTH)) {
            this.followUpGroup = followUpGroup;
        }
        else {
            this.followUpGroup = FOLLOW_UP_GROUP_ASSIGNED_TO;
        }
    }

    public String getFollowUpType() {
        return followUpType;
    }

    public void setFollowUpType(String followUpType) {
        this.followUpType = followUpType;
    }

    public Date getDueDateFrom() {
        return dueDateFrom;
    }

    public void setDueDateFrom(Date dueDateFrom) {
        this.dueDateFrom = dueDateFrom;
    }

    public Date getDueDateTo() {
        return dueDateTo;
    }

    public void setDueDateTo(Date dueDateTo) {
        this.dueDateTo = dueDateTo;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getBillCycleCloseDay() {
        return billCycleCloseDay;
    }

    public void setBillCycleCloseDay(int billCycleCloseDay) {
        if (billCycleCloseDay >= 1 && billCycleCloseDay <= 31) {
            this.billCycleCloseDay = billCycleCloseDay;
        }
        else {
            this.billCycleCloseDay = dummyBillCycleCloseDay;
        }
    }

    public double getAccountBalanceFrom() {
        return accountBalanceFrom;
    }

    public void setAccountBalanceFrom(double accountBalanceFrom) {
        this.accountBalanceFrom = accountBalanceFrom;
    }

    public double getAccountBalanceTo() {
        return accountBalanceTo;
    }

    public void setAccountBalanceTo(double accountBalanceTo) {
        this.accountBalanceTo = accountBalanceTo;
    }

    public void clear() {
        workPositionId = null;
        followUpStatus = FOLLOW_UP_STATUS_OPEN;
        followUpGroup = FOLLOW_UP_GROUP_ASSIGNED_TO;
        followUpType = null;
        dueDateFrom = null;
        dueDateTo = null;
        accountStatus = null;
        billCycleCloseDay = dummyBillCycleCloseDay;
        accountBalanceFrom = dummyAccountBalanceFrom;
        accountBalanceTo = dummyAccountBalanceTo;
    }

    public boolean setBillCycleCloseDay() {
        return billCycleCloseDay != dummyBillCycleCloseDay;
    }

    public boolean setAccountBalanceFrom() {
        return accountBalanceFrom != dummyAccountBalanceFrom;
    }

    public boolean setAccountBalanceTo() {
        return accountBalanceTo != dummyAccountBalanceTo;
    }
}
