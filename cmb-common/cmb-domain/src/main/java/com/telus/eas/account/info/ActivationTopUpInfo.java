package com.telus.eas.account.info;

import com.telus.api.account.ActivationTopUp;
import com.telus.eas.framework.info.Info;

public class ActivationTopUpInfo extends Info implements ActivationTopUp {

	 static final long serialVersionUID = 1L;

     private double amount;
     private double rate;
     private int rateId;
     private int days;
     private String reasonCode;

	public double getAmount() {
       return amount;
	}

	public void setAmount(double amount) {
      this.amount = amount;
	}

	public double getRate() {
      return rate;
	}
    public void setRate(double rate) {
      this.rate = rate;
	}

	public int getExpiryDays() {
       return days;
	}

	public void setExpiryDays(int days) {
        this.days = days;
    }

    public int getRateId() {        return rateId;    }

    public void setRateId(int rateId) {        this.rateId = rateId;    }

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}
}
