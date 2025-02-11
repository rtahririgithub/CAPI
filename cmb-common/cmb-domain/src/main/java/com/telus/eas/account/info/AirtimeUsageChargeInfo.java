package com.telus.eas.account.info;

import java.util.ArrayList;

import com.telus.eas.framework.info.Info;

/**
 * @author Brandon Wen
 *
 */
public class AirtimeUsageChargeInfo extends Info {
	private static final long serialVersionUID = 1L;
	private double totalChargeAmount = 0;
	private ArrayList chargeInfoList = new ArrayList();
	
	public double getTotalChargeAmount() {
		return totalChargeAmount;
	}
	
	public void setTotalChargeAmount(double totalChargeAmount) {
		this.totalChargeAmount = totalChargeAmount;
	}
	
	public ArrayList getChargeInfoList() {
		return chargeInfoList;
	}
	
	public void setChargeInfoList(ArrayList chargeInfoList) {
		this.chargeInfoList = chargeInfoList;
	}
	
	public void addChargeInfo(UsageChargeInfo chargeInfo) {
		this.chargeInfoList.add(chargeInfo);
		this.totalChargeAmount += chargeInfo.getChargeAmount();
	}
	
}
