/*
 * $Id$
 * %E% %W%
 * Copyright (c) Clearnet Inc. All Rights Reserved.
 */

package com.telus.api.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.telus.api.TelusAPIException;
import com.telus.api.message.ApplicationMessage;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WCCAccountThresholdBreachException extends TelusAPIException {

	private static final long serialVersionUID = 1L;

	private List breachList;

	public WCCAccountThresholdBreachException(Throwable exception, ApplicationMessage applicationMessage) {
		super(exception, applicationMessage, 0);
		String[] breacheMessages = (applicationMessage.getText(Locale.ENGLISH.getLanguage())).split("\\|");
		breachList = new ArrayList();
		for (int i = 0; i < breacheMessages.length; i++) {
			breachList.add(parseBreachMessage(breacheMessages[i]));
		}
	}

	public BreachInfo[] getBreachErrors() {
		return (BreachInfo[]) breachList.toArray(new BreachInfo[breachList.size()]);
	}

	private BreachInfo parseBreachMessage(String breachMessage) {
		
		BreachInfo breachInfo = new BreachInfo();
		
		String[] tokens = breachMessage.split(",");
		for (int i = 0; i < tokens.length; i++) {
			String[] keyValuePair = tokens[i].split("=");
			if ("zone".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setZone(keyValuePair[1]);
			}
			if ("chargedAmount".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setChargedAmount(parseDouble(keyValuePair[1]));
			}
			if ("purchaseAmount".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setPurchaseAmount(parseDouble(keyValuePair[1]));
			}
			if ("lastConsentAmount".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setLastConsentAmount(parseDouble(keyValuePair[1]));
			}
			if ("thresholdType".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setThresholdType(keyValuePair[1]);
			}
			if ("thresholdLimitAmount".equalsIgnoreCase(keyValuePair[0])) {
				breachInfo.setThresholdLimitAmount(parseDouble(keyValuePair[1]));
			}
		}
		
		return breachInfo;		
	}

	private static double parseDouble(String value) {
		try {
			return value != null ? Double.parseDouble(value) : 0;
		} catch (NumberFormatException nfe) {
			return 0;
		}
	}

	public class BreachInfo {

		private String zone;
		private double chargedAmount;
		private double purchaseAmount;
		private double lastConsentAmount;
		private String thresholdType;
		private double thresholdLimitAmount;

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

		public String getThresholdType() {
			return thresholdType;
		}

		public void setThresholdType(String thresholdType) {
			this.thresholdType = thresholdType;
		}

		public double getThresholdLimitAmount() {
			return thresholdLimitAmount;
		}

		public void setThresholdLimitAmount(double thresholdLimitAmount) {
			this.thresholdLimitAmount = thresholdLimitAmount;
		}
	}
}