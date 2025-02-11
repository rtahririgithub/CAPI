/*
 * $Id$ %E% %W% Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.eas.queueevent.info;

import java.util.Date;

import com.telus.api.TelusAPIException;
import com.telus.api.account.*;
import com.telus.eas.framework.info.*;

public class QueueThresholdEventInfo extends Info implements
		QueueThresholdEvent {

	static final long serialVersionUID = 1L;

	private String associatedPhoneNumber;
	private long callCentreConnectionId;
	private String callCentreQueue;
	private Date date;
	private long id;
	private int teamMemberId;
	private int waitTime;
	private boolean evaluatedForCredit;
	private String subscriberNumber;
	private long subscriptionId;

	public String getAssociatedPhoneNumber() {
		return associatedPhoneNumber;
	}

	public void setAssociatedPhoneNumber(String associatedPhoneNumber) {
		this.associatedPhoneNumber = associatedPhoneNumber;
	}

	public long getCallCentreConnectionId() {
		return callCentreConnectionId;
	}

	public void setCallCentreConnectionId(long callCentreConnectionId) {
		this.callCentreConnectionId = callCentreConnectionId;
	}

	public String getCallCentreQueue() {
		return callCentreQueue;
	}

	public void setCallCentreQueue(String callCentreQueue) {
		this.callCentreQueue = callCentreQueue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isEvaluatedForCredit() {
		return evaluatedForCredit;
	}

	public void setEvaluatedForCredit(boolean evaluatedForCredit) {
		this.evaluatedForCredit = evaluatedForCredit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriber(Subscriber sub) throws TelusAPIException{
		this.subscriberNumber = sub.getPhoneNumber();
		this.subscriptionId = sub.getSubscriptionId();
	}

	public int getTeamMemberId() {
		return teamMemberId;
	}

	public void setTeamMemberId(int teamMemberId) {
		this.teamMemberId = teamMemberId;
	}

	public int getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	public void update() throws TelusAPIException {
		throw new UnsupportedOperationException("Method not implemented here");
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("QueueThresholdEventInfo[");
		buffer.append("associatedPhoneNumber = ").append(associatedPhoneNumber);
		buffer.append(" callCentreConnectionId = ").append(
				callCentreConnectionId);
		buffer.append(" callCentreQueue = ").append(callCentreQueue);
		buffer.append(" date = ").append(date);
		buffer.append(" evaluatedForCredit = ").append(evaluatedForCredit);
		buffer.append(" id = ").append(id);
		buffer.append(" subscriberNumber = ").append(subscriberNumber);
		buffer.append(" subscriptionId = ").append(subscriptionId);
		buffer.append(" teamMemberId = ").append(teamMemberId);
		buffer.append(" waitTime = ").append(waitTime);
		buffer.append("]");
		return buffer.toString();
	}}

