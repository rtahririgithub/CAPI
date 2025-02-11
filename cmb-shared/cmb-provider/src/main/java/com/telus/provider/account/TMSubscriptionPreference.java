package com.telus.provider.account;

import com.telus.api.TelusAPIException;
import com.telus.api.account.SubscriptionPreference;
import com.telus.cmb.subscriber.lifecyclemanager.svc.SubscriberLifecycleManager;
import com.telus.eas.subscriber.info.SubscriptionPreferenceInfo;
import com.telus.provider.BaseProvider;
import com.telus.provider.TMProvider;

public class TMSubscriptionPreference extends BaseProvider implements
		SubscriptionPreference {
	private static final long serialVersionUID = 1L;
	private SubscriptionPreferenceInfo delegate;

	public TMSubscriptionPreference(TMProvider provider,
			SubscriptionPreferenceInfo delegate) {
		super(provider);
		this.delegate = delegate;
	}

	public SubscriptionPreferenceInfo getDelegate() {
		return delegate;
	}

	public int getPreferenceTopicId() {
		return delegate.getPreferenceTopicId();
	}

	public String getPreferenceValueTxt() {
		return delegate.getPreferenceValueTxt();
	}

	public void setPreferenceValueTxt(String preferenceValueTxt) {
		delegate.setPreferenceValueTxt(preferenceValueTxt);
		delegate.setPreferenceValueTxtModified(true);
	}

	public void save() throws TelusAPIException {
		if (delegate.isPreferenceValueTxtModified()) {
			try {
				provider.getSubscriberLifecycleManager().saveSubscriptionPreference(
						delegate, provider.getUser());
			} catch (Throwable t) {
				provider.getExceptionHandler().handleException(t);
			}
		}
	}
}