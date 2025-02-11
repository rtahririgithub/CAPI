package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class PaymentNotificationInfo extends Info {
	
	
	private static final long serialVersionUID = -5614872394641270404L;
 
	private int ban;
	private char accountStatus;
    private boolean delinquent;
	private boolean hotlined;
	private boolean clp;
	private double amount;
	private long referenceNumber;
	private Date dueDate;
 
 
	public int getBillingAccountNumber(){
		return ban;
	}
	
	public void setBillingAccountNumber(int billingAccountNumber){
		this.ban = billingAccountNumber;
	}

	public Date getPaymentNotificationDueDate() {
		return dueDate;
	}

	public void setPaymentNotificationDueDate(Date date) {
		this.dueDate = date;
	}

 	public long getRefNum() {
		return referenceNumber;
	}

	public void setRefNum(long refNum) {
		this.referenceNumber = refNum;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public boolean isHotlined() {
		return hotlined;
	}

	public void setHotlined(boolean hotlineInd) {
		this.hotlined = hotlineInd;
	}

	public boolean isDelinquent() {
		return delinquent;
	}

	public void setDelinquent(boolean delinuentInd) {
		this.delinquent = delinuentInd;
	}

	public boolean isCLP() {
		return clp;
	}

	public void setCLP(boolean clpInd) {
		this.clp = clpInd;
	}


	public char getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(char status) {
		this.accountStatus = status;
	}
	public String toString() {
        StringBuffer s = new StringBuffer(128);
        s.append("PaymentNotificationInfo:[\n");
        s.append("    ban=[").append(ban).append("]\n");
        s.append("    accountStatus=[").append(accountStatus).append("]\n");
        s.append("    delinquent=[").append(delinquent).append("]\n");
        s.append("    hotlined=[").append(hotlined).append("]\n");
        s.append("    clp=[").append(clp).append("]\n");
        s.append("    amount=[").append(amount).append("]\n");
        s.append("    referenceNumber=[").append(referenceNumber).append("]\n");
        s.append("    dueDate=[").append(dueDate).append("]\n");
        s.append("]");
        return s.toString();
    }
}
