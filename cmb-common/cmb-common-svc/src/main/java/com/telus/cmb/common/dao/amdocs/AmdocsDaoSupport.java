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

import com.telus.api.ApplicationException;
import com.telus.cmb.common.identity.ClientIdentity;


/**
 * Convenient super class for Amdocs-based data access objects.
 *
 * <p>Requires a {@link com.telus.cmb.common.dao.amdocs.AmdocsSessionManager} to be set, providing a
 * {@link com.telus.cmb.common.dao.amdocs.AmdocsTemplate} based on it to
 * subclasses through the {@link #getAmdocsTemplate()} method.
 *
 * @author Pavel Simonovsky
 */

public class AmdocsDaoSupport {
	
	private AmdocsTemplate amdocsTemplate;

	/**
	 * Return the AmdocsTemplate for this DAO,
	 * pre-initialized with the AmdocsSessionManager or set explicitly.
	 */
	public AmdocsTemplate getAmdocsTemplate() {
		return amdocsTemplate;
	}

	/**
	 * Set the AmdocsTemplate for this DAO explicitly,
	 * as an alternative to specifying an AmdocsSessionManager.
	 */
	public void setAmdocsTemplate(AmdocsTemplate amdocsTemplate) {
		this.amdocsTemplate = amdocsTemplate;
	}

	/**
	 * Set the AmdocsSessionManager to be used by this DAO.
	 */
	public void setAmdocsSessionManager(AmdocsSessionManager sessionManager) {
		setAmdocsTemplate( new AmdocsTemplate(sessionManager));
	}
	
	/**
	 * Convenient method to open seesion from DAO subclasses.
	 * 
	 * @param user
	 * @param password
	 * @param application
	 * @return
	 * @throws ApplicationException 
	 */
	public String openSession(String user, String password, String application) throws ApplicationException {
		ClientIdentity identity = new ClientIdentity(user, password, application);
		return getAmdocsTemplate().getSessionManager().openSession(identity);
	}
	
	public void testConnectivity(String sessionId) throws ApplicationException {
		getAmdocsTemplate().execute(sessionId, new AmdocsTransactionCallback<Object>() {

			@Override
			public Object doInTransaction(AmdocsTransactionContext transactionContext) throws Exception {
				return null;
			}
		});
	}
	
}
