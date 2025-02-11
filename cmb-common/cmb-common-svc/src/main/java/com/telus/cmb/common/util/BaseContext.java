package com.telus.cmb.common.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.telus.api.ApplicationException;
import com.telus.api.SystemCodes;
import com.telus.api.SystemException;
import com.telus.cmb.common.identity.ClientIdentity;
import com.telus.eas.transaction.info.AuditInfo;

/**
 * Base class for wrapping context objects required in order to execute complex business logic. For example, this class can be used to maintain the context objects required
 * when migrating context-rich Provider functionality to the EJB layer. 
 * 
 * @author R. Fong
 *
 */
public abstract class BaseContext {
	
	private AuditInfo auditInfo;
	private ClientIdentity clientIdentity;
	protected Map<Object, String> sessionIdCache = new HashMap<Object, String>();
	protected EJBController ejbController;
	
	public BaseContext(EJBController ejbController, ClientIdentity clientIdentity, AuditInfo auditInfo) {
		this.ejbController = ejbController;
		this.clientIdentity = clientIdentity;
		this.auditInfo = auditInfo;
	}
	
	public abstract void initialize() throws SystemException, ApplicationException;
	
	public abstract void refresh() throws SystemException, ApplicationException;

	public ClientIdentity getClientIdentity() {
		return clientIdentity;
	}
	
	public AuditInfo getAuditInfo() {
		return auditInfo;
	}
	
	protected String getSessionId(Object proxy) {
		
		String sessionId = sessionIdCache.get(proxy);
		if (sessionId == null) {
			try {
				Method method = proxy.getClass().getMethod("openSession", String.class, String.class, String.class);
				sessionId = (String) method.invoke(proxy, clientIdentity.getPrincipal(), clientIdentity.getCredential(), clientIdentity.getApplication());
				sessionIdCache.put(proxy, sessionId);
			} catch (Exception e) {
				throw new SystemException(SystemCodes.CMB_EJB, e.getMessage(), "", e);
			}
		}
		
		return sessionId;
	}
	
	/**
	 * Allow session ID of the creator EJB to be stored by default
	 * 
	 * @param proxy
	 * @param sessionId
	 */
	public void setSessionId(Object proxy, String sessionId) {
		if (proxy != null) {
			sessionIdCache.put(proxy, sessionId);
		}
	}
	
}