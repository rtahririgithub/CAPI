/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author Pavel Simonovsky
 *
 */
public class SessionUtil {

	public static String getSessionId(Object proxy) throws Throwable {
		InvocationHandler handler = Proxy.getInvocationHandler(proxy);
		if (handler instanceof RemoteBeanProxy){
			return ((RemoteBeanProxy)handler).getSessionId();
		}
		return null;
	}	
}
