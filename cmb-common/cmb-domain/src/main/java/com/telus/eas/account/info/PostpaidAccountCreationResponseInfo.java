package com.telus.eas.account.info;

import com.telus.api.account.CreditCheckResult;
import com.telus.eas.framework.info.Info;


/**
 * @author x131162 Anitha Duraisamy
 * 
 * This info class is holding the information need to be returned for post paid account creation
 * 
 */
public class PostpaidAccountCreationResponseInfo extends Info {


	private static final long serialVersionUID = 1L;

	private int ban;
	
	private CreditCheckResultInfo  creditCheckResult;
	
	private boolean isCreditCheckPerformed = false;
	
	public boolean isCreditCheckPerformed() {
		return isCreditCheckPerformed;
	}

	public void setCreditCheckPerformed(boolean isCreditCheckPerformed) {
		this.isCreditCheckPerformed = isCreditCheckPerformed;
	}

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public CreditCheckResult getCreditCheckResult() {
		return creditCheckResult;
	}
	public CreditCheckResultInfo getCreditCheckResult0() {
		return creditCheckResult;
	}

	public void setCreditCheckResult(CreditCheckResultInfo creditCheckResult) {
		this.creditCheckResult = creditCheckResult;
	}

	
}
