/*
 *  Copyright (c) 2014 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.framework.resource;

import com.telus.api.ApplicationException;

/**
 * @author Pavel Simonovsky
 *
 */
public class ResourceAccessTestClient extends ResourceAccessTemplate {

	/**
	 * 
	 */
	public ResourceAccessTestClient() {
	}

	public String sayHello(final String firstName, final String lastName) throws ApplicationException {

		return execute( new ResourceExecutionCallback<String>() {
			
			@Override
			public String doInCallback() throws Exception {
				
				if (firstName.equals("aa")) {
					throw new RuntimeException("foooo");
				}
				return "Hello " + firstName + " " + lastName;
			}
		}, "ERR_0001", "FW", "PIRIS", "WNP");
	}
	
}
