package com.telus.api.account;

public interface NotificationToggleSubscriptionPreference extends SubscriptionPreference {

	String NOTIFICATION_TOGGLE_ENABLE = "Y";
	String NOTIFICATION_TOGGLE_DISABLE = "N";

	String getPreferenceValueTxt();

}