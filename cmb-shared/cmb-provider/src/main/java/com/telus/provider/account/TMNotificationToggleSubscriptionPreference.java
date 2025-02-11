package com.telus.provider.account;

import com.telus.api.account.NotificationToggleSubscriptionPreference;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.provider.TMProvider;

public class TMNotificationToggleSubscriptionPreference extends
		TMSubscriptionPreference implements
		NotificationToggleSubscriptionPreference {
	private static final long serialVersionUID = 1L;

	public TMNotificationToggleSubscriptionPreference(TMProvider provider,
			SubscriptionPreferenceInfo delegate) {
		super(provider, delegate);
	}

	public String getPreferenceValueTxt() {
		String result = super.getPreferenceValueTxt();
		if (result == null || result.trim().length() == 0) {
			result = NotificationToggleSubscriptionPreference.NOTIFICATION_TOGGLE_ENABLE;
		}
		return result;
	}
}