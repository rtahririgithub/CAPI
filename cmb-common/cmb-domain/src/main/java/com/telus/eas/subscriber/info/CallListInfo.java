package com.telus.eas.subscriber.info;

import com.telus.api.account.CallList;
import com.telus.api.account.CallSummary;
import com.telus.eas.framework.info.Info;

/**
 * Title:        CallListInfo<p>
 * Description:  The CallListInfo class holds the attributes for a list of billed call summaries for a subscriber.<p>
 * Copyright:    Copyright (c) 2004<p>
 * Company:      Telus Mobility Inc<p>
 * @author R. Fong
 * @version 1.0
 */
public class CallListInfo extends Info implements CallList {

	static final long serialVersionUID = 1L;
	
	private int totalCallCount;
	private CallSummary[] callSummaries;
	
	public CallListInfo() {
	}
	
	/**
	 * @return Returns the totalCallCount.
	 */
	public int getTotalCallCount() {
		return totalCallCount;
	}
	/**
	 * @param totalCallCount The totalCallCount to set.
	 */
	public void setTotalCallCount(int totalCallCount) {
		this.totalCallCount = totalCallCount;
	}
	
	/**
	 * @return Returns the callSummaries.
	 */
	public CallSummary[] getCallSummaries() {
		return callSummaries;
	}
	/**
	 * @param callSummaries The callSummaries to set.
	 */
	public void setCallSummaries(CallSummary[] callSummaries) {
		this.callSummaries = callSummaries;
	}
	
	public String toString() {
		
		StringBuffer s = new StringBuffer(128);

		s.append("CallListInfo:[\n");
	    if (callSummaries == null) {
	        s.append("    callSummaries=[null]\n");
	    } else if (callSummaries.length == 0) {
	        s.append("    callSummaries=[]}\n");
	    } else {
	    	for (int i = 0; i < callSummaries.length; i++) {
	    		s.append("    callSummaries[" + i + "]=[").append(callSummaries[i]).append("]\n");
	    	}
	    }
		s.append("    totalCallCount=[").append(totalCallCount).append("]\n");
		s.append("]");

		return s.toString();
	}

}
