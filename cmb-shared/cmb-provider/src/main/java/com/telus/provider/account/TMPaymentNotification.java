package com.telus.provider.account;

import java.util.Date;

import com.telus.api.account.FollowUp;
import com.telus.api.account.Memo;
import com.telus.api.account.PaymentNotification;
import com.telus.api.transaction.ActivityResult;

public class TMPaymentNotification implements PaymentNotification {
    
    private final long id;
    private final Memo memo;
    private final FollowUp followUp;
    private final Date dueDate;   
  

    public TMPaymentNotification(int id, Memo memo, FollowUp followUp, Date dueDate) {
        this.id = id;
        this.memo = memo;
        this.followUp = followUp;
        this.dueDate = dueDate;        
    }
    
    

    public TMPaymentNotification(int id, Memo memo, FollowUp followUp, Date dueDate,ActivityResult[] activityResultArray) {
        this.id = id;
        this.memo = memo;
        this.followUp = followUp;
        this.dueDate = dueDate;  
        this.activityResults=activityResultArray;
    }

    public Date getPaymentNotificationDueDate() {       
        return dueDate;
    }

    public Memo getMemo() {
        return memo;
    }

    public FollowUp getFollowUp() {        
        return followUp;
    }

    public long getId() {
        return id;
    }

    public String toString(){
        StringBuffer str = new StringBuffer();
        str.append("TMPaymentNotification[").append('\n').append("Id[");
        str.append(id).append("]").append('\n');
        str.append("Due date[").append(dueDate).append("]").append('\n');
        str.append(memo.toString()).append('\n');
        if(followUp != null)
            str.append(followUp.toString()).append('\n');
        str.append("]");
        
        return str.toString();
    }

   
    private ActivityResult[] activityResults;
    

    public ActivityResult[] getActivityResults() {
		return activityResults;
	}

	public void setActivityResults(ActivityResult[] activityResults) {
		this.activityResults = activityResults;
	}
	
}