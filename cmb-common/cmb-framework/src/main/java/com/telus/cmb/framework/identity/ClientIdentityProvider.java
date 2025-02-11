/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.identity;

import javax.xml.ws.WebServiceContext;

/**
 * @author Pavel Simonovsky
 *
 */
public interface ClientIdentityProvider {
	
	public static final String TELUS_TID = "cmb.jws.telusTID";
	public static final String CLIENT_IDENTITY = "cmb.jws.clientIdentity";
	public static final String USE_APP_IDENTITY = "cmb.jws.clientIdentity.useAppIdentity";

	ClientIdentity getClientIdentity(WebServiceContext context);

	IdentityContext extractIdentityContext(WebServiceContext context);
}
