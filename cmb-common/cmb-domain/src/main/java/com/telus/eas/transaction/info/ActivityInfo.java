package com.telus.eas.transaction.info;

import java.io.Serializable;

import com.telus.api.transaction.Activity;

public class ActivityInfo implements Activity, Serializable {
	
	static final long serialVersionUID = 1L;

	private int value;

	public ActivityInfo(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}

}
