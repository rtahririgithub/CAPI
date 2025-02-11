/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.cmb.common.dao.amdocs;

import javax.naming.Context;

/**
 * AmdocsTransactionContext represents Amdocs statefull bean factory during single 
 * AmdocsTemplate.execute() method.
 * 
 * <P>All beans created using {@link #createBean()} method will be released at the end of 
 * transacton by calling {@link #close()} method from AmdocsTemplate.execute() method.
 * 
 * 
 * @author Michael Liao
 *
 */
public class NonSecuredAmdocsTransactionContext extends AmdocsTransactionContext {

	/**
	 * Initialise AmdocsTransactionContext with an null AmdocsSession
	 * and configure naming context for the current thread
	 * 
	 */
	NonSecuredAmdocsTransactionContext(Context context ) {
		super( null, context);
	}
	
	@SuppressWarnings("unchecked")
	<T> T getBeanHome(Class<T> beanType, Context namingContext ) {
		return (T) AmdocsSession.lookupBeanHome(namingContext, AmdocsSession.getBeanHomeJndiName(beanType));
	}
}
