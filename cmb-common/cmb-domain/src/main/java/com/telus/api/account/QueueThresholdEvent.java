package com.telus.api.account;

import java.util.Date;

import com.telus.api.TelusAPIException;

/**
 * @author kparamso
 */
public interface QueueThresholdEvent {

	long getId();
	
	long getCallCentreConnectionId();
	
	String getCallCentreQueue();
	
	Date getDate();
	
	String getAssociatedPhoneNumber();
	
	int getWaitTime();
	
	boolean isEvaluatedForCredit();
	
	int getTeamMemberId();
	
	void setSubscriber(Subscriber sub) throws TelusAPIException;
	
	void setTeamMemberId(int teamMemberId);
	
	void update() throws TelusAPIException;
	
}
