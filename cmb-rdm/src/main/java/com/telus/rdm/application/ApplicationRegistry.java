/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.application;

import org.springframework.context.ApplicationContext;

import com.telus.rdm.application.service.AccountApplicationService;
import com.telus.rdm.application.service.SubscriberApplicationService;

/**
 * @author x113300
 *
 */
public class ApplicationRegistry {

	private static ApplicationContext applicationContext;
	
	public static void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationRegistry.applicationContext = applicationContext;
	}

	public static AccountApplicationService accountApplicationService() {
		return applicationContext.getBean(AccountApplicationService.class);
	}
	
	public static SubscriberApplicationService subscriberApplicationService() {
		return applicationContext.getBean(SubscriberApplicationService.class);
	}
	
}
