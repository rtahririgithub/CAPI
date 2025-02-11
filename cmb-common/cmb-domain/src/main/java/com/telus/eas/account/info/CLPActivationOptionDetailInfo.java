package com.telus.eas.account.info;

import com.telus.api.account.CLPActivationOptionDetail;
import com.telus.eas.framework.info.Info;

/**
 * @author x119951
 *
 */
public class CLPActivationOptionDetailInfo extends Info implements CLPActivationOptionDetail {
	private static final long serialVersionUID = 2905440876025372550L;

	private int maxNumberOfCLPSubscribers;
	private int maxAdditionalCLPSubscribers;
	private int currentNumberOfSubscribers;
	private String[] resultReasonCodes;
	private boolean cLPAccountInd;
	
	public int getMaxNumberOfCLPSubscribers() {
		return maxNumberOfCLPSubscribers;
	}
	
	public int getMaxAdditionalCLPSubscribers() {
		return maxAdditionalCLPSubscribers;
	}
	
	public int getCurrentNumberOfSubscribers() {
		return currentNumberOfSubscribers;
	}
	
	public String[] getResultReasonCodes() {
		return resultReasonCodes;
	}
	
	public boolean isCLPAccountInd() {
		return cLPAccountInd;
	}

	public void setMaxNumberOfCLPSubscribers(int maxNumberOfCLPSubscribers) {
		this.maxNumberOfCLPSubscribers = maxNumberOfCLPSubscribers;
	}

	public void setMaxAdditionalCLPSubscribers(int maxAdditionalCLPSubscribers) {
		this.maxAdditionalCLPSubscribers = maxAdditionalCLPSubscribers;
	}

	public void setCurrentNumberOfSubscribers(int currentNumberOfSubscribers) {
		this.currentNumberOfSubscribers = currentNumberOfSubscribers;
	}

	public void setResultReasonCodes(String[] resultReasonCodes) {
		this.resultReasonCodes = resultReasonCodes;
	}

	public void setCLPAccountInd(boolean cLPAccountInd) {
		this.cLPAccountInd = cLPAccountInd;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("CLPActivationOptionDetailInfo:{\n");
		buffer.append("\tmaxNumberOfCLPSubscribers=[" + maxNumberOfCLPSubscribers + "]\n");
		buffer.append("\tmaxAdditionalCLPSubscribers=[" + maxAdditionalCLPSubscribers + "]\n");
		buffer.append("\tcurrentNumberOfSubscribers=[" + currentNumberOfSubscribers + "]\n");
		buffer.append("\tcLPAccountInd=[" + cLPAccountInd + "]\n");
		buffer.append("}");
		return buffer.toString();
	}
}
