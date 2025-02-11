package com.telus.eas.utility.info;

import java.util.List;

import com.telus.eas.framework.info.Info;

public class SapccUpdateAccountPurchaseInfo extends Info {

	private static final long serialVersionUID = 1L;

	private int ban;
	private boolean breachedInd;
	private String zone;
	private double chargedAmount;
	private double purchaseAmount;
	private double lastConsentAmount;
	private List<SapccThresholdInfo> thresholdList;

	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public boolean isBreachedInd() {
		return breachedInd;
	}

	public void setBreachedInd(boolean breachedInd) {
		this.breachedInd = breachedInd;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
	public double getChargedAmount() {
		return chargedAmount;
	}

	public void setChargedAmount(double chargedAmount) {
		this.chargedAmount = chargedAmount;
	}

	public double getPurchaseAmount() {
		return purchaseAmount;
	}

	public void setPurchaseAmount(double purchaseAmount) {
		this.purchaseAmount = purchaseAmount;
	}

	public double getLastConsentAmount() {
		return lastConsentAmount;
	}

	public void setLastConsentAmount(double lastConsentAmount) {
		this.lastConsentAmount = lastConsentAmount;
	}

	public List<SapccThresholdInfo> getThresholdList() {
		return thresholdList;
	}

	public void setThresholdList(List<SapccThresholdInfo> thresholdList) {
		this.thresholdList = thresholdList;
	}
	
	public String toString() {

		StringBuffer buffer = new StringBuffer();
		buffer.append("SapccUpdateAccountPurchaseInfo [\n");
		buffer.append("   ban=").append(ban).append("\n");
		buffer.append("   breachedInd=").append(breachedInd).append("\n");
		buffer.append("   zone=").append(zone).append("\n");
		buffer.append("   chargedAmount=").append(chargedAmount).append("\n");
		buffer.append("   purchaseAmount=").append(purchaseAmount).append("\n");
		buffer.append("   lastConsentAmount=").append(lastConsentAmount).append("\n");
		for (SapccThresholdInfo info : thresholdList) {
			buffer.append(info.toString()).append("\n");
		}		
		buffer.append("]");
		
		return buffer.toString();
	}
	
}