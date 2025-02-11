/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

/**
 * @author Pavel Simonovsky
 *
 */

@SuppressWarnings("rawtypes")
public class SimpleHandlerResolver implements HandlerResolver {

	private List<Handler> handlers = new ArrayList<Handler>();

	/*
	 * (non-Javadoc)
	 * @see javax.xml.ws.handler.HandlerResolver#getHandlerChain(javax.xml.ws.handler.PortInfo)
	 */
	@Override
	public List<Handler> getHandlerChain(PortInfo portInfo) {
		return handlers;
	}
	
	public void setHandlers(List<Handler> handlers) {
		this.handlers = handlers;
	}
	
}
