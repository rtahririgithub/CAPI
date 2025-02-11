/**
 * 
 */
package com.telus.eas.account.info;

import com.telus.eas.framework.info.Info;

/**
 * @author tongts
 *
 */
public class ActivationFeaturesPurchaseArrangementInfo extends Info {
	
	static final long serialVersionUID = 1L;
	
	private String featureId;
	private String autoRenewIndicator;
	private int autoRenewFundSource;
	private int purchaseFundSource;
	
	public void setFeatureId(String fid) {
		featureId = fid;
	}
	
	public String getFeatureId() {
		return featureId;
	}
	
	public void setAutoRenewIndicator (String ind) {
		autoRenewIndicator = ind;
	}

	public String getAutoRenewIndicator() {
		return autoRenewIndicator;
	}

	public int getAutoRenewFundSource() {
		return autoRenewFundSource;
	}

	public void setAutoRenewFundSource(int autoRenewFundSource) {
		this.autoRenewFundSource = autoRenewFundSource;
	}
	
	public int getPurchaseFundSource() {
		return purchaseFundSource;
	}

	public void setPurchaseFundSource(int purchaseFundSource) {
		this.purchaseFundSource = purchaseFundSource;
	}
}
