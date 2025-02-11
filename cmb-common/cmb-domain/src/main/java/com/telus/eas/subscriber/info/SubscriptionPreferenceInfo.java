package com.telus.eas.subscriber.info;

import com.telus.eas.framework.info.Info;

public class SubscriptionPreferenceInfo extends Info 
{

	private static final long serialVersionUID = 1L;
	
	private long subscriptionId;
	private int subscriberPreferenceId;
	private int preferenceTopicId;
	private int subscrPrefChoiceSeqNum;
	private String preferenceValueTxt;

	private boolean isPreferenceValueTxtModified;

	public long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}


	public int getSubscriberPreferenceId() {
		return subscriberPreferenceId;
	}

	public void setSubscriberPreferenceId(int subscriberPreferenceId) {
		this.subscriberPreferenceId = subscriberPreferenceId;
	}

	public int getPreferenceTopicId() {
		return preferenceTopicId;
	}

	public void setPreferenceTopicId(int preferenceTopicId) {
		this.preferenceTopicId = preferenceTopicId;
	}

	public int getSubscrPrefChoiceSeqNum() {
		return subscrPrefChoiceSeqNum;
	}

	public void setSubscrPrefChoiceSeqNum(int subscrPrefChoiceSeqNum) {
		this.subscrPrefChoiceSeqNum = subscrPrefChoiceSeqNum;
	}

	public String getPreferenceValueTxt() {
		return preferenceValueTxt;
	}

	public void setPreferenceValueTxt(String preferenceValueTxt) {
		this.preferenceValueTxt = preferenceValueTxt;
	}

	public boolean isPreferenceValueTxtModified() {
		return isPreferenceValueTxtModified;
	}

	public void setPreferenceValueTxtModified(boolean isPreferenceValueTxtModified) {
		this.isPreferenceValueTxtModified = isPreferenceValueTxtModified;
	}
}
