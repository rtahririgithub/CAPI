package com.telus.api.account;

/**
 * @author x119951
 *
 */
public interface CLPActivationOptionDetail {
	public int getMaxNumberOfCLPSubscribers();
	public int getMaxAdditionalCLPSubscribers();
	public int getCurrentNumberOfSubscribers();
	public String[] getResultReasonCodes();
	public boolean isCLPAccountInd();
}
