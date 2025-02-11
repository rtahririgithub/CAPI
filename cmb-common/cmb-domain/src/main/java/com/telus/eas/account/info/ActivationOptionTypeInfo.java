package com.telus.eas.account.info;

import com.telus.api.account.ActivationOptionType;
import com.telus.eas.framework.info.Info;

public class ActivationOptionTypeInfo extends Info implements ActivationOptionType {

	static final long serialVersionUID = 1L;
	
	private final String name;

	public ActivationOptionTypeInfo(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

}