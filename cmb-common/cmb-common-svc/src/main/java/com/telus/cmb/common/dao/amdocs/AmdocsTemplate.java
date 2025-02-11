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

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import amdocs.APILink.datatypes.UsrMsgs;
import amdocs.APILink.exceptions.BackendException;
import amdocs.APILink.exceptions.ValidateException;
import amdocs.APILink.sessions.interfaces.RefDataMngr;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.identity.ClientIdentity;

/**
 * @author Pavel Simonovsky
 *
 */
public class AmdocsTemplate {
	
	private static final Log logger = LogFactory.getLog(AmdocsTemplate.class);

	private AmdocsSessionManager sessionManager;
	

	public AmdocsTemplate(AmdocsSessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public AmdocsSessionManager getSessionManager() {
		return sessionManager;
	}

	public <T> T execute(String sessionId, AmdocsTransactionCallback<T> callback) throws ApplicationException {
		AmdocsSession session = sessionManager.getSession(sessionId);
		if (session == null) {
			throw new RuntimeException("Failed to retrieve session for ID [" + sessionId + "]. Use openSession() method to create a new session");
		}
		
		return execute(session, callback);
	}
	
	public <T> T execute(ClientIdentity identity, AmdocsTransactionCallback<T> callback) throws ApplicationException {
		return execute(sessionManager.getSession(identity), callback);
	}
	
	public <T> T execute(AmdocsSession session, AmdocsTransactionCallback<T> callback) throws ApplicationException {

		logger.debug("Executing Amdocs transaction using session [" + session.getId() + "]... with ticket[" + session.getTicket() + "]");
		
		AmdocsTransactionContext transactionContext = null;

		try {
			transactionContext = session.newTransactionContext();
		} catch ( SystemException e ) {
			// the exception indicate the session is no longer valid, remove it from the cache
			logger.info("Amdocs session is no longer valid, remove it from cache: session [" + session.getId() + "]...");
			sessionManager.removeSession ( session );
			throw e;
		}
		
		try {
			return callback.doInTransaction(transactionContext);
		} catch (ApplicationException aex) {
			logger.error(getCallerInfoMsg(session));
			logger.error(aex);
			throw aex;
		} catch (BackendException be) {
			
			throw translateBackendException( transactionContext, be );
			
		} catch (ValidateException vex) {
			
			String message = getCallerInfoMsg(session) + "Amdocs validation error: [" + vex.getErrorInd() + "] - " + vex.getErrorMsg();
			logger.error(message, vex);
			
			ApplicationException exception = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(vex.getErrorInd()), vex.getErrorMsg(), null, vex);
			throw exception;
			
		} catch (Throwable t) {
		
			String message = getCallerInfoMsg(session) + "Amdocs transaction error: [" + t.getClass().getCanonicalName()+"]" + t.getMessage();
			logger.error(message, t);
			
			session.close();
			
			throw new SystemException(SystemCodes.AMDOCS, message, "", t);

		} finally {
			if (transactionContext != null) {
				transactionContext.close();
			}
		}
	}

	/**
	 * Upon catching BackendException, use RefDataMngr to retrieve the detail Tuxedo messages.
	 * @param transactionContext
	 * @param be
	 * @return
	 */
	private ApplicationException translateBackendException( AmdocsTransactionContext transactionContext, BackendException be ) {
		
		StringBuilder sb = new StringBuilder(be.getMessage()).append("\n");
		String tuxedoMessage = null;
		boolean removeMessage = true;
		
		logger.error( "BackendException occurs: ", be );
		
		try {
			logger.debug("Retrieving Tuxedo message." );
			
			RefDataMngr refDataMngr = transactionContext.createBean(RefDataMngr.class);
			
			UsrMsgs[] messages = refDataMngr.getErrorMessage(removeMessage);
			sb.append("Error Messages:\n");
			for (int i=0; i < messages.length; i++) {
				sb.append( "  Message " ).append( i+1 ).append(": ").append( messages[i].getMsgTxt())
					.append("\n");
			}
			
			messages = refDataMngr.getUserMessage(removeMessage);
			sb.append("User Messages:\n");
			for (int i=0; i < messages.length; i++) {
				sb.append( "  Message " ).append( i+1 ).append(": ").append( messages[i].getMsgTxt())
					.append("\n");
			}
			
		} catch( Throwable t) {
			logger.warn("Encounter error while retrieving Tuxedo messages (this exception will be not thrown!)" , t );
			sb.append( "\n (Retrieving Tuxedo message failed: ").append( t.getMessage() ).append(")");
		} finally {
			//log what we have so far
			tuxedoMessage = sb.toString();
			logger.error( tuxedoMessage );
		}
		
		return new ApplicationException(SystemCodes.AMDOCS, ErrorCodes.TUXEDO_FAILURE, tuxedoMessage, "", be);
	}
	
	public <T> T execute( AmdocsTransactionCallback<T> callback) throws ApplicationException {

		AmdocsTransactionContext transactionContext = null;

		try {
			Properties env = new Properties();
			env.setProperty(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
			env.setProperty(Context.PROVIDER_URL, sessionManager.getConnectionUrl());
			transactionContext = new NonSecuredAmdocsTransactionContext( new InitialContext( env ) );
			
			return callback.doInTransaction(transactionContext);
		} catch (ApplicationException aex) {
			throw aex;
		} catch (BackendException be) {
			
			throw translateBackendException( transactionContext, be );
			
		} catch (ValidateException vex) {
			
			String message = "Amdocs validation error: [" + vex.getErrorInd() + "] - " + vex.getErrorMsg();
			logger.error(message, vex);
			
			ApplicationException exception = new ApplicationException(SystemCodes.AMDOCS, String.valueOf(vex.getErrorInd()), vex.getErrorMsg(), null, vex);
			throw exception;
			
		} catch (Throwable t) {
			String message = "Amdocs transaction error: [" + t.getClass().getCanonicalName()+"]" + t.getMessage();
			logger.error(message, t);
			throw new SystemException(SystemCodes.AMDOCS, message, "", t);
		} finally {
			if (transactionContext != null) {
				transactionContext.close();
			}
		}
	} 

	protected String getCallerInfoMsg(AmdocsSession session) {
		ClientIdentity ci = session.getClientIdentity();
		return "App=[" + ci.getApplication() + "] principal=["+ci.getPrincipal()+"] ";
	}
	
}
