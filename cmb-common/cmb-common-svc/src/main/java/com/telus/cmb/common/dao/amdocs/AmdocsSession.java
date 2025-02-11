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

import java.net.InetAddress;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import amdocs.APILink.accesscontrol.APIAccessInfo;
import amdocs.APILink.accesscontrol.APIConnection;

import com.telus.api.ApplicationException;
import com.telus.api.ErrorCodes;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.identity.ClientIdentity;

/**
 * @author Pavel Simonovsky
 *
 */
public class AmdocsSession {

	private static final Log logger = LogFactory.getLog(AmdocsSession.class);
	
	private String id;
	
	private ClientIdentity clientIdentity;
	
	private String connectionUrl = null;
	
	private String ticket;
	private Date ticketCreationTime;
	
	private static String hostName;;
	
	static {
		try {
			hostName=InetAddress.getLocalHost().getHostName();
		} catch( Throwable e ) {
			hostName = "Unkonw";
		}
	}
	
	private long creationTimestamp;
	
	private long lastAccessTimestamp;
	
	private Map<String, Object> beanHomes = Collections.synchronizedMap( new HashMap<String, Object>());
	
	public AmdocsSession(String id, ClientIdentity clientIdentity, String connectionUrl) throws ApplicationException {
		this.id = id;
		this.clientIdentity = clientIdentity;
		this.connectionUrl = connectionUrl;
		getTicket();
		
		creationTimestamp = lastAccessTimestamp = System.currentTimeMillis();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ClientIdentity getClientIdentity() {
		return clientIdentity;
	}

	public void setClientIdentity(ClientIdentity clientIdentity) {
		this.clientIdentity = clientIdentity;
	}
	

	public String getConnectionUrl() {
		return connectionUrl;
	}

	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

	Object getBeanHome(Class<?> beanType, Context context) {
		
		String beanJndiName = getBeanHomeJndiName(beanType);

		Object beanHome = null; // beanHomes.get(beanJndiName);
		
		if (beanHome == null) {
			
			try {
				
				logger.debug("Obtaining Amdocs bean home inteface [" + beanJndiName + "]...");
				
				beanHome = context.lookup(beanJndiName);
				//beanHomes.put(beanJndiName, beanHome);
				
				logger.debug("Obtained Amdocs bean home inteface [" + beanHome + "]");
				
			} catch (Throwable t) {
				throw new SystemException(SystemCodes.AMDOCS, "Error obtaining Amdocs bean home interface: " + t.getMessage(), "", t);
			} 
		}
		
		return beanHome;
	}
	
	static Object lookupBeanHome(Context context, String beanJndiName ){
		Object beanHome = null;
		try {
			logger.debug("Obtaining Amdocs bean home inteface [" + beanJndiName + "]...");
			
			beanHome = context.lookup(beanJndiName);
			
			logger.debug("Obtained Amdocs bean home inteface [" + beanHome + "]");
			
		} catch (Throwable t) {
			throw new SystemException(SystemCodes.AMDOCS, "Error obtaining Amdocs bean home interface: " + t.getMessage(), "", t);
		}
		return beanHome; 
	}

	static String getBeanHomeJndiName(Class<?> beanType) {
		return "amdocsBeans." + beanType.getSimpleName() + "Home";
	}	

	public String getTicket() throws ApplicationException{
		if (ticket == null) {

			logger.debug("Acquiring a new Amdocs ticket/session for [" + getId() + "]");

			APIAccessInfo accessInfo = APIConnection.connect(clientIdentity.getPrincipal(), clientIdentity.getCredential(), connectionUrl, clientIdentity.getApplication());


			if (accessInfo == null) {
				throw new ApplicationException(SystemCodes.AMDOCS, ErrorCodes.KB_CONNECT_ERROR, "Failed to acquire Amdocs ticket", "");
//				throw new SystemException(SystemCodes.AMDOCS, "Failed to acquire Amdocs ticket", "");
			}

			if (accessInfo.getInfoType() == 'E') {
				String message = new StringBuilder(
					"Failed to acquire new Amdocs session from [").append( hostName )
					.append("]  to  [").append(connectionUrl).append("], with full credential [kbId=")
					.append(clientIdentity.getPrincipal()).append("] application=[").append(clientIdentity.getApplication()).append("] ")
					.append(" accessInfo[infoType=").append( accessInfo.getInfoType())
					.append( ",errorType=" ).append(accessInfo.getErrorType())
					.append( ", info=").append(accessInfo.getInfo() ).append("]")
					.toString();
				
				logger.error(message);
				throw new ApplicationException(SystemCodes.AMDOCS, ErrorCodes.KB_AMDOCS_SESSION_ERROR, message, "");				
//				throw new SystemException(SystemCodes.AMDOCS, message, "");
			}
			ticket = accessInfo.getTicket();
			ticketCreationTime = new Date();
			
			logger.debug("Acquired new ticket [" + ticket + "] for [" + getId() + "]");
			try {
				accessInfo.getContext().close();
			} catch (NamingException e) {
				logger.error("Error closing AMDOCS context.");
			}
		}
		return ticket;
	}
	
	/**
	 * Create a new AmdocsTransactionContext, ticket to be shared with multiple threads
	 * 
	 * Logic:
	 *  connect to APILink using existing AMDOCS ticket, 
	 *  if this fails, reconnect with full credential. if fails again, throw exception,
	 *  
	 *  If connected, return new AmdocsTransactionContext with the naming context that just acquired.
	 * 
	 * @return AmdocsTransactionContext
	 * @throws ApplicationException 
	 */
	public AmdocsTransactionContext newTransactionContext() throws ApplicationException {

		APIAccessInfo accessInfo = APIConnection.connect(connectionUrl, ticket);

		if (accessInfo == null) {
			throw new ApplicationException(SystemCodes.AMDOCS,"Failed to open Amdocs session","");			
//			throw new SystemException(SystemCodes.AMDOCS,"Failed to open Amdocs session", "");
		}

		if (accessInfo.getInfoType() == 'E') { 
			
			//connect with ticket failed
			
			StringBuilder sb = new StringBuilder( "Session[" )
					.append( getId() ).append("] Failed to open Amdocs session from [")
					.append( hostName ).append("] to [")
					.append(connectionUrl).append("], with ticket[")
					.append(ticket).append("] ticketCreationDate[")
					.append(ticketCreationTime.toString())
					.append("][kbId=").append(clientIdentity.getPrincipal()).append("] application=[").append(clientIdentity.getApplication())
					.append("]: accessInfo[infoType=").append( accessInfo.getInfoType())
					.append( ",errorType=" ).append(accessInfo.getErrorType())
					.append( ", info=").append(accessInfo.getInfo() ).append("]");
			logger.warn( sb.toString() );
			
			
			// now try to reconnect with the full credential
			logger.warn("Acquiring a new Amdocs ticket/session for [" + getId() + "]");

			accessInfo = APIConnection.connect( clientIdentity.getPrincipal(), clientIdentity.getCredential(), connectionUrl,
					clientIdentity.getApplication());

			if (accessInfo.getInfoType() == 'E') {

				StringBuilder sb2 = new StringBuilder(
					"Failed to acquire new Amdocs ticket from [").append( hostName )
					.append("]  to  [").append(connectionUrl).append("], with full credential [kbId=")
					.append(clientIdentity.getPrincipal()).append("] application=[").append(clientIdentity.getApplication()).append("] ")
					.append(" accessInfo[infoType=").append( accessInfo.getInfoType())
					.append( accessInfo.getInfoType())
					.append( ",errorType=" ).append(accessInfo.getErrorType())
					.append( ", info=").append(accessInfo.getInfo() ).append("]");
				
				logger.error( sb2.toString() );
				throw new ApplicationException(SystemCodes.AMDOCS,sb.append( "\nAND ") .append( sb2 ).toString(),"");				
//				throw new SystemException(SystemCodes.AMDOCS, sb.append( "\nAND ") .append( sb2 ).toString(), "" );
				
			}
			
			//successfully reconnected with full credential, update ticket and creation time
			ticket = accessInfo.getTicket();
			ticketCreationTime = new Date();
			
			logger.debug("Acquired new ticket [" + ticket + "] for [" + getId() + "]");
		}

		return new AmdocsTransactionContext( this, accessInfo.getContext() );

	}
	
	@SuppressWarnings("deprecation")
	public synchronized void close() {
		
		logger.debug("Closing Amdocs session [" + getId() + "]");
		
		if (ticket != null) {
			APIConnection.disconnect(connectionUrl, ticket);
			ticket = null;
			beanHomes.clear();
		}
	}

	public long getLastAccessTimestamp() {
		return lastAccessTimestamp;
	}

	
	public void setLastAccessTimestamp(long lastAccessTimestamp) {
		this.lastAccessTimestamp = lastAccessTimestamp;
	}

	public long getCreationTime() {
		return creationTimestamp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AmdocsSession [id=").append(id)
				.append(", clientIdentity=[").append(clientIdentity.getPrincipal()).append(",").append(clientIdentity.getApplication()).append("]")
				.append(", connectionUrl=").append(connectionUrl)
				.append(", ticket=").append(ticket)
				.append(", creationTime=").append( new java.util.Date (creationTimestamp))
				.append(", lastAccessTime=").append( new java.util.Date( lastAccessTimestamp ) ).append("]");
		
		return builder.toString();
	}

}
