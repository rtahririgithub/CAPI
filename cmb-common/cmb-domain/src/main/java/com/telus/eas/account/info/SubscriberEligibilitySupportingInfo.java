package com.telus.eas.account.info;

import com.telus.api.account.Memo;
import com.telus.eas.framework.info.Info;
/**
 * @author Inbaselvan Gandhi
 *
 */
public class SubscriberEligibilitySupportingInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	private Memo[] memoList;
	private double totalPaymentAmount;
	private double totalDepositHeld;
	
	public Memo[] getMemoList() {
		return memoList;
	}
	public void setMemoList(Memo[] memoList) {
		this.memoList = memoList;
	}
	public double getTotalPaymentAmount() {
		return totalPaymentAmount;
	}
	public void setTotalPaymentAmount(double totalPaymentAmount) {
		this.totalPaymentAmount = totalPaymentAmount;
	}
	public double getTotalDepositHeld() {
		return totalDepositHeld;
	}
	public void setTotalDepositHeld(double totalDepositHeld) {
		this.totalDepositHeld = totalDepositHeld;
	}

}
