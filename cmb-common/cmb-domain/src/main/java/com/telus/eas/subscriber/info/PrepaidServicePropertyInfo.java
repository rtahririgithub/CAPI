package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class PrepaidServicePropertyInfo extends Info {
	private int purchaseFundSource;
	private boolean autoRenewalInd;
	private int autoRenewalFundSource;
	
	
	public int getPurchaseFundSource() {
		return purchaseFundSource;
	}
	public void setPurchaseFundSource(int purchaseFundSource) {
		this.purchaseFundSource = purchaseFundSource;
	}
	public boolean isAutoRenewalInd() {
		return autoRenewalInd;
	}
	public void setAutoRenewalInd(boolean autoRenewalInd) {
		this.autoRenewalInd = autoRenewalInd;
	}
	public int getAutoRenewalFundSource() {
		return autoRenewalFundSource;
	}
	public void setAutoRenewalFundSource(int autoRenewalFundSource) {
		this.autoRenewalFundSource = autoRenewalFundSource;
	}
	
	
}
