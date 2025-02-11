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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.cmb.common.identity.ClientIdentityUtil;
import com.telus.cmb.common.util.AppConfiguration;

/**
 * @author Pavel Simonovsky
 *
 */
public class AmdocsSessionManager {
	
	private static final Log logger = LogFactory.getLog(AmdocsSessionManager.class);
	
	private volatile Map<String, AmdocsSession> sessions = Collections.synchronizedMap(new HashMap<String, AmdocsSession>());
		
	private ClientIdentityUtil identityUtil;
	
	public AmdocsSessionManager() {
		System.setProperty("amdocs.uams.config.resource", "res/gen/sum/client");
		
		setupSessionCacheCleanupTask();
		
		try {
			identityUtil = new ClientIdentityUtil();
		} catch (Throwable e) {
			throw new SystemException(SystemCodes.AMDOCS, "Error creating ClientIdentityUtil instance " + e.getMessage(), "", e);
		}
	}
	
	public String getConnectionUrl() {	
		System.setProperty("SEC_SRV_CONN", AppConfiguration.getUamsUrl());
		
		return AppConfiguration.getAmdocsUrl();
	}
	
	public String openSession(ClientIdentity clientIdentity) throws ApplicationException {
		return getSession(clientIdentity).getId();
	}

	/**
	 * 
	 * @param clientIdentity
	 * @return
	 * @throws ApplicationException 
	 */
	public AmdocsSession getSession(ClientIdentity clientIdentity) throws ApplicationException {
		String sessionId = getSessionId(clientIdentity);
		
		AmdocsSession session = null;
		
		session = sessions.get(sessionId);
		if (session != null) {
			session.setLastAccessTimestamp( System.currentTimeMillis() );
		}else {
			session = newSession(sessionId, clientIdentity);
		}
		
		return session;
	}

	private AmdocsSession newSession(String sessionId, ClientIdentity clientIdentity) throws ApplicationException {
		logger.debug("Creating a new Amdocs session with id = [" + sessionId + "]");

		AmdocsSession session = new AmdocsSession(sessionId, clientIdentity, getConnectionUrl());
		
		AmdocsSession verifySession = null;
		// the following is to provide synchronization without blocking the read on hashMap when connecting to APILink
		synchronized (sessions) {
			verifySession = sessions.get(sessionId); //check the map again since we don't synchronize the whole block
			if (verifySession == null) {         //this should always be true unless there's concurrency issue
				sessions.put(sessionId, session); 
			}
		}
		if (verifySession != null) { //this line is true only if there's concurrency issue. in this case, we would close the newly
			session.close();         //obtained session and return the one from hashMap to save resources
			session = verifySession;
		}
		return session;
	}
	
	public AmdocsSession getSession(String sessionId) throws ApplicationException {
		AmdocsSession session = sessions.get(sessionId);
		if (session == null) { 
			//The follow cases would cause not being able to find the session 
			//1. session being expired
			//2. session was opened in another node, but the subsequent request is rolling to this node due to the the other node is down
			//3. this node was recycled which clear out the session cache.
			//In such cases, we shall rebuild the session base on the sessionId
			
			logger.info( "Amdocs session is not found, rebuild the session for id=[" + sessionId + "]");
			
			ClientIdentity clientIdentity;
			try {
				clientIdentity = identityUtil.decrypt(sessionId);
			} catch (Throwable t) {
				throw new SystemException(SystemCodes.AMDOCS, "Error decrypting session id=[: " + sessionId + "],  " + t.getMessage(), "", t);
			}
			
			session = newSession( sessionId, clientIdentity);
			
		} else {
			session.setLastAccessTimestamp( System.currentTimeMillis() );
		}
		
		return session;
	}
	public void removeSession(AmdocsSession session) {
		sessions.remove(session.getId());
	}

	
	public ClientIdentity getClientIdentity(String sessionId) throws ApplicationException{
		AmdocsSession session = getSession(sessionId);
		return session == null ? null : session.getClientIdentity();
	}
	
	public void closeSession(ClientIdentity clientIdentity) throws ApplicationException {
		getSession(clientIdentity).close();
	}
	
	private String getSessionId(ClientIdentity clientIdentity) {
		try {
			return identityUtil.encrypt(clientIdentity);
		} catch (Throwable t) {
			throw new SystemException(SystemCodes.AMDOCS, "Error creating session id: " + t.getMessage(), "", t);
		}
	}
	
	void setupSessionCacheCleanupTask() {
		
		long idleTime = AppConfiguration.getAmdocsSessionIdle(); 
		long period = AppConfiguration.getAmdocsSessionEvictionRate();
		
		logger.info( "Schedule CacheEvictionTask to start after [" + idleTime + "]ms  at interval rate[" + period + "]ms.");
		
		new Timer().scheduleAtFixedRate( new CacheEvictionTask(), idleTime, period );
	}
	
	private class CacheEvictionTask extends java.util.TimerTask {

		@Override
		public void run() {
			logger.debug( "AmdocsSession CacheEvictionTask run." );
			
			long expirationTime = System.currentTimeMillis() - AppConfiguration.getAmdocsSessionIdle();
			
			int total = sessions.size();
			
			synchronized (sessions) {
				Iterator<Entry<String,AmdocsSession>> it = sessions.entrySet().iterator();
				while( it.hasNext() ) {
					AmdocsSession session  = it.next().getValue();
					if ( session.getLastAccessTimestamp() < expirationTime ) {
						it.remove();
						logger.debug( "evict cached item: " +  session);
					}
				}
			} 
			
			int remained = sessions.size();
			logger.debug( "cached session: total=" + total + ", evicted=" + (total-remained) );
		}
	}
	
}
