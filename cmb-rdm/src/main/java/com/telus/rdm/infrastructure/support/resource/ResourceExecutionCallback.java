/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.rdm.infrastructure.support.resource;

/**
 * @author x113300
 *
 */
public interface ResourceExecutionCallback<T> {

	T doInCallback() throws Exception;
	
}
