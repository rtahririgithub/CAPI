/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider.account;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.QueueThresholdEvent;
import com.telus.api.account.Subscriber;
import com.telus.eas.queueevent.info.QueueThresholdEventInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;
import com.telus.provider.queueevent.TMQueueEventManager;

public class TMQueueThresholdEvent extends BaseProvider implements QueueThresholdEvent {
	
	private final QueueThresholdEventInfo delegate;

	  public TMQueueThresholdEvent(TMProvider provider, QueueThresholdEventInfo delegate) {
	    super(provider);
	    this.delegate = delegate;
	  }

	  private QueueThresholdEventInfo getDelegate() {
	    return delegate;
	  }
	
	public String getAssociatedPhoneNumber() {
		return delegate.getAssociatedPhoneNumber();
	}
	public long getCallCentreConnectionId() {
		return delegate.getCallCentreConnectionId();
	}
	public String getCallCentreQueue() {
		return delegate.getCallCentreQueue();
	}
	public Date getDate() {
		return delegate.getDate();
	}
	public long getId() {
		return delegate.getId();
	}
	public int getTeamMemberId() {
		return delegate.getTeamMemberId();
	}
	public int getWaitTime() {
		return delegate.getWaitTime();
	}
	public boolean isEvaluatedForCredit() {
		return delegate.isEvaluatedForCredit();
	}
	public void setSubscriber(Subscriber sub) throws TelusAPIException{
		delegate.setSubscriber(sub);
	}
	public void setTeamMemberId(int teamMemberId) {
		delegate.setTeamMemberId(teamMemberId);
	}
	public void update() throws TelusAPIException {
		if (delegate.getSubscriberNumber()==null)
			throw new TelusAPIException("Subscriber was not set for this event, so an update cannot be performed.");
		provider.getQueueEventManager0().updateEvent(getId(),getDelegate().getSubscriptionId(),getDelegate().getSubscriberNumber(),getDelegate().getTeamMemberId(),Integer.parseInt(provider.getUser()));
		
	}
	public String toString() {
		return delegate.toString();
	}	
	
}



