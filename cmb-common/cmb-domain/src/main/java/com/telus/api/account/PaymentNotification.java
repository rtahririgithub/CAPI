package com.telus.api.account;

import java.util.Date;

import com.telus.api.transaction.ActivityResult;

public interface PaymentNotification {
	
    Date getPaymentNotificationDueDate();  

    Memo getMemo();

    /**
     * Followup will be created if and only account is delinquent
     * 
     * <P>This method will return null If the account is not delinquent.</P>
     */

    FollowUp getFollowUp();
    
    long getId();
   
    
    /**
     * Get all the activity results for the method invocation.
     *
     * @return ActivityResult[] an array of objects that will define where in 
     * the processing steps the Payment Notification logic has failed. 
     * An empty array means that all processing steps were successful.
     */
    ActivityResult[] getActivityResults();   
 
    
}
