package com.telus.eas.transaction.info;

import java.io.Serializable;

import com.telus.api.transaction.Activity;
import com.telus.api.transaction.ActivityResult;

public class ActivityResultInfo implements ActivityResult, Serializable {
	
	static final long serialVersionUID = 1L;

	private Activity activity;
	private java.lang.Throwable throwable;

	public ActivityResultInfo(Activity activity, java.lang.Throwable throwable) {
		this.activity = activity;
		this.throwable = throwable;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public java.lang.Throwable getThrowable() {
		return this.throwable;
	}

	public void setThrowable(java.lang.Throwable throwable) {
		this.throwable = throwable;
	}

}
