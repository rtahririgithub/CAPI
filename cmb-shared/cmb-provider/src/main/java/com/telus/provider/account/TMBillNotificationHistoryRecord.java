package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.BillNotificationHistoryRecord;
import com.telus.eas.account.info.BillNotificationHistoryRecordInfo;

public class TMBillNotificationHistoryRecord implements
		BillNotificationHistoryRecord {

	BillNotificationHistoryRecordInfo delegate;

	public TMBillNotificationHistoryRecord () {
        delegate = new BillNotificationHistoryRecordInfo();
    }

    public TMBillNotificationHistoryRecord (BillNotificationHistoryRecordInfo delegate) {
        this.delegate = delegate;
    }

    public TMBillNotificationHistoryRecord  getBillNotificationHistoryRecord() {
        return this;
    }

    public BillNotificationHistoryRecordInfo getBillNotificationHistoryRecord0() {
        return delegate;
    } 
    
	public String getActionType() {
		return delegate.getActionType();
	}

	public String getActivityReason() {
		return delegate.getActivityReason();
	}

	public String getActivityType() {
		return delegate.getActionType();
	}

	public String getBan() {
		return delegate.getBan();
	}

	public Date getEffectiveEndDate() {
		return delegate.getEffectiveEndDate();
	}

	public Date getEffectiveStartDate() {
		return delegate.getEffectiveStartDate();
	}

	public String getEmailAddress() {
		return delegate.getEmailAddress();
	}

	public boolean getMostRecentInd() {
		return delegate.getMostRecentInd();
	}

	public String getSrcReferenceId() {
		return delegate.getSrcReferenceId();
	}

	public String getActionTypeFr() {
		return delegate.getActionTypeFr();
	}

	public String getActivityReasonFr() {
		return delegate.getActivityReasonFr();
	}

	public String getActivityTypeFr() {
		return delegate.getActivityTypeFr();
	}
}
