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



/**
 * Generic callback interface for code that operates on a AmdocsTransactionContext.
 *
 * @author Pavel Simonovsky
 */
public interface AmdocsTransactionCallback<T> {

	/**
	 * Gets called by <code>AmdocsTemplate.execute</code> with an active AmdocsTransactionContext
	 * Does not need to care about removing the Amdocs statefull beans allocated with the 
	 * <code>AmdocsTransactionContext.createBean()</code> method: this will all be
	 * handled by AmdocsTemplate.
	 *
	 * <p>Allows for returning a result object created within the callback, i.e.
	 * a domain object or a collection of domain objects. 
	 *
	 * @param transactionContext - active AmdocsTransactionContext
	 * @return a result object, or <code>null</code> if none
	 * @throws Exception 
	 */	
	T doInTransaction(AmdocsTransactionContext transactionContext) throws Exception;
	
}
