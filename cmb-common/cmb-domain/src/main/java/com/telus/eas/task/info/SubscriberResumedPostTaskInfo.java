package com.telus.eas.task.info;

import java.io.Serializable;

public class SubscriberResumedPostTaskInfo implements Serializable {
	static final long serialVersionUID = 1L;
	
	private boolean bRepairCommSuite = false;
	
	public SubscriberResumedPostTaskInfo () {
		
	}
	
	public SubscriberResumedPostTaskInfo repairCommunicationSuite() {
		this.bRepairCommSuite = true;
		return this;
	}

	public boolean isRepairCommSuite() {
		return bRepairCommSuite;
	}
	
	
}
