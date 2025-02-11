package com.telus.cmb.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {
	
	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		CONTEXT = context;
	}
	
	public static ApplicationContext getApplicationContext() {
		return CONTEXT;
	}
}
