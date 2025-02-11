package com.telus.api.account;

import com.telus.api.TelusAPIException;



public interface SubscriptionPreference {


	/**
	 * Represents if customer wants SMS usage notifications.
	 * If call to getPreferenceValueTxt == N, customer will not receive notifications.
	 * By default, if customer has not explicitly changed, value is Y
	 */
	static final int PREFERENCE_TYPE_CODE_NOTIFICATION_TOGGLE = 1;
	/**
	 * Represents if customer wants domestic data blocking (implies capping)
	 * If call to getPreferenceValueTxt == N, customer will not be blocked and capped.
	 * By default, if customer has not explicitly changed, value is Y
	 */
	static final int PREFERENCE_TYPE_CODE_DOMESTIC_DATA_TOGGLE = 2;
	/**
	 * Represents if customer wants roaming data blocking (implies capping)
	 * If call to getPreferenceValueTxt == N, customer will not be blocked and capped.
	 * By default, if customer has not explicitly changed, value is Y
	 */
	static final int PREFERENCE_TYPE_CODE_ROAMING_DATA_TOGGLE = 3;


	int getPreferenceTopicId();



	String getPreferenceValueTxt();


	void setPreferenceValueTxt(String preferenceValueTxt);



	void save() throws TelusAPIException;	



}