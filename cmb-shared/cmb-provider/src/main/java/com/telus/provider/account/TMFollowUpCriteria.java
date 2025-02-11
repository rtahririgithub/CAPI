package com.telus.provider.account;

import com.telus.api.reference.FollowUpCriteria;
import com.telus.eas.utility.info.FollowUpCriteriaInfo;

import java.util.Date;

public class TMFollowUpCriteria implements FollowUpCriteria {

    FollowUpCriteriaInfo delegate;

    public TMFollowUpCriteria() {
        delegate = new FollowUpCriteriaInfo();
    }

    public TMFollowUpCriteria getFollowUpCriteria() {
        return this;
    }

    public FollowUpCriteriaInfo getFollowUpCriteria0() {
        return delegate;
    }

    public String getWorkPositionId() {
        return delegate.getWorkPositionId();
    }

    public void setWorkPositionId(String value) {
        delegate.setWorkPositionId(value);
    }

    public String getFollowUpStatus() {
        return delegate.getFollowUpStatus();
    }

    public void setFollowUpStatus(String value) {
        delegate.setFollowUpStatus(value);
    }

    public String getFollowUpGroup() {
        return delegate.getFollowUpGroup();
    }

    public void setFollowUpGroup(String value) {
        delegate.setFollowUpGroup(value);
    }

    public String getFollowUpType() {
        return delegate.getFollowUpType();
    }

    public void setFollowUpType(String value) {
        delegate.setFollowUpType(value);
    }

    public Date getDueDateFrom() {
        return delegate.getDueDateFrom();
    }

    public void setDueDateFrom(Date value) {
        delegate.setDueDateFrom(value);
    }

    public Date getDueDateTo() {
        return delegate.getDueDateTo();
    }

    public void setDueDateTo(Date value) {
        delegate.setDueDateTo(value);
    }

    public String getAccountStatus() {
        return delegate.getAccountStatus();
    }

    public void setAccountStatus(String value) {
        delegate.setAccountStatus(value);
    }

    public int getBillCycleCloseDay() {
        return delegate.getBillCycleCloseDay();
    }

    public void setBillCycleCloseDay(int value) {
        delegate.setBillCycleCloseDay(value);
    }

    public double getAccountBalanceFrom() {
        return delegate.getAccountBalanceFrom();
    }

    public void setAccountBalanceFrom(double value) {
        delegate.setAccountBalanceFrom(value);
    }

    public double getAccountBalanceTo() {
        return delegate.getAccountBalanceTo();
    }

    public void setAccountBalanceTo(double value) {
        delegate.setAccountBalanceTo(value);
    }

    public void clear() {
        delegate.clear();
    }
}

