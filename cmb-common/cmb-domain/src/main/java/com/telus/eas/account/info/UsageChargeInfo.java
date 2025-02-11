package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

/**
 * @author Brandon Wen
 *
 */
public class UsageChargeInfo extends Info  {
	private static final long serialVersionUID = 1L;

	/** 
	 * chargeRecordType has below values and corresponding meanings,
	 * 
	 * 1 - Airtime Charges
	 * 2 - Additional Charges
	 * 4 - Roaming Toll Charges
	 * 5 - Toll Charges
	 * 6 - Free minutes
	 * 7 - Minimum commitment stepped charges
	 * 8 - Pooling charges
	 * 9 - BCIC (Bill Cycle Independent Charge) carry over charges
	 */
	private String chargeRecordType;
	
	private double chargeAmount;

	public String getChargeRecordType() {
		return chargeRecordType;
	}

	public void setChargeRecordType(String chargeRecordType) {
		this.chargeRecordType = chargeRecordType;
	}

	public double getChargeAmount() {
		return chargeAmount;
	}

	public void setChargeAmount(double chargeAmount) {
		this.chargeAmount = chargeAmount;
	}

}
