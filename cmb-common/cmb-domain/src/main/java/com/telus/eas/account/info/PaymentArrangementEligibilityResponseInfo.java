package com.telus.eas.account.info;

import java.util.Date;

import com.telus.eas.framework.info.Info;

public class PaymentArrangementEligibilityResponseInfo extends Info {

	private static final long serialVersionUID = 1L;
	
	private final boolean eligible;
	private final int banId;
	private final Date nextBillDate;
	private final Date paymentArrangementDate;

	public PaymentArrangementEligibilityResponseInfo(boolean eligible, int banId, Date nextBillDate, Date paymentArrangementDate) {
		this.eligible = eligible;
		this.banId = banId;
		this.nextBillDate = nextBillDate;
		this.paymentArrangementDate = paymentArrangementDate;
	}

	public boolean isEligible() {
		return eligible;
	}

	public int getBanId() {
		return banId;
	}

	public Date getNextBillDate() {
		return nextBillDate;
	}

	public Date getPaymentArrangementDate() {
		return paymentArrangementDate;
	}

	public String toString() {
		
		StringBuffer str = new StringBuffer();
		str.append("PaymentArrangementEligibilityResponseInfo: [").append('\n');
		str.append("   eligible=[").append(eligible).append("]").append('\n');
		str.append("   banId=[").append(banId).append("]").append('\n');
		str.append("   nextBillDate=[").append(nextBillDate).append("]").append('\n');
		str.append("   paymentArrangementDate=[").append(paymentArrangementDate).append("]").append('\n');
		str.append("]");

		return str.toString();
	}
	
}