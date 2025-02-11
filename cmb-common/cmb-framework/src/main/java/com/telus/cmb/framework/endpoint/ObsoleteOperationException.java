/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.endpoint;


/**
 * @author Pavel Simonovsky
 *
 */
public class ObsoleteOperationException extends EndpointSystemException {

	private static final long serialVersionUID = 1L;
	
	public ObsoleteOperationException() {
		super(EndpointConstants.ERROR_OBSOLETE_OPERATION_CODE, EndpointConstants.ERROR_OBSOLETE_OPERATION_DESC);
	}
	
}
