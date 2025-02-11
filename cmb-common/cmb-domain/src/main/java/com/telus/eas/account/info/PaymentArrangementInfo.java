package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class PaymentArrangementInfo extends Info {
	
	private static final long serialVersionUID = 1L;

	public static final int PAYMENT_ARRANGEMENT_TRANSACTION_TYPE = 1;

	private int transactionType;
	private Date paymentArrangementDate;
	private Date paymentDueDate;
	private char accountStatus;
	private String transRefNum;
	private boolean delinquent;
	private boolean hotlined;
	private boolean clp;
	private double amount;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getPaymentArrangementDate() {
		return paymentArrangementDate;
	}

	public void setPaymentArrangementDate(Date paymentArrangementDate) {
		this.paymentArrangementDate = paymentArrangementDate;
	}

	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public char getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(char accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getTransRefNum() {
		return transRefNum;
	}

	public void setTransRefNum(String transRefNum) {
		this.transRefNum = transRefNum;
	}

	public boolean isDelinquent() {
		return delinquent;
	}

	public void setDelinquent(boolean delinquent) {
		this.delinquent = delinquent;
	}

	public boolean isHotlined() {
		return hotlined;
	}

	public void setHotlined(boolean hotlined) {
		this.hotlined = hotlined;
	}

	public boolean isClp() {
		return clp;
	}

	public void setClp(boolean clp) {
		this.clp = clp;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String toString() {
		StringBuffer s = new StringBuffer(128);
		s.append("PaymentArrangementInfo:[\n");
		s.append("    transactionType=[").append(transactionType).append("]\n");
		s.append("    paymentArrangementDate=[").append(paymentArrangementDate).append("]\n");
		s.append("    paymentDueDate=[").append(paymentDueDate).append("]\n");
		s.append("    accountStatus=[").append(accountStatus).append("]\n");
		s.append("    transRefNum=[").append(transRefNum).append("]\n");
		s.append("    hotlined=[").append(hotlined).append("]\n");
		s.append("    clp=[").append(clp).append("]\n");
		s.append("    amount=[").append(amount).append("]\n");
		s.append("]");

		return s.toString();
	}
}
