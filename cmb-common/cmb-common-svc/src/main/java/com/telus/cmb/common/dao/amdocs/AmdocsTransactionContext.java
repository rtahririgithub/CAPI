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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBObject;
import javax.naming.Context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.ReflectionUtils;

import com.telus.api.SystemCodes;
import com.telus.api.SystemException;

/**
 * AmdocsTransactionContext represents Amdocs statefull bean factory during single 
 * AmdocsTemplate.execute() method.
 * 
 * <P>All beans created using {@link #createBean()} method will be released at the end of 
 * transacton by calling {@link #close()} method from AmdocsTemplate.execute() method.
 * 
 * 
 * @author Pavel Simonovsky
 *
 */
public class AmdocsTransactionContext {

	private static final Log logger = LogFactory.getLog(AmdocsTransactionContext.class);
	
	private AmdocsSession session;
	
	private List<EJBObject> beans = new ArrayList<EJBObject>();
	
	private Context namingContext;
	
	/**
	 * Initialise AmdocsTransactionContext with an active AmdocsSession
	 * and configure naming context for the current thread
	 * 
	 * @param session - an active AmdocsSession
	 */
	AmdocsTransactionContext(AmdocsSession session, Context context ) {
		this.session = session;
		namingContext = context;
	}
	
	/**
	 * Create a new Amdocs statefull bean using home interface cached (optionally) in active session 
	 * 
	 * @param <T>
	 * @param beanType
	 * @return
	 * @throws AmdocsException
	 */
	@SuppressWarnings("unchecked")
	public <T> T createBean(Class<T> beanType) {
		try {
			
			logger.debug("Creating a new Amdocs bean instance of type [" + beanType.getName() + "]");
			
			Object beanHome = getBeanHome(beanType, namingContext);
			
			EJBObject bean = (EJBObject) ReflectionUtils.invokeMethod(
					ReflectionUtils.findMethod(beanHome.getClass(), "create"), beanHome);
			
			beans.add(bean);
			
			logger.debug("Created Amdocs statefull bean [" + bean + "]");
			
			return (T) bean;
			
		} catch (Throwable t) {
			throw new SystemException(SystemCodes.AMDOCS, "Error creating Amdocs bean: " + t.getMessage(), "", t);
		}
	}
	
	@SuppressWarnings("unchecked")
	<T> T getBeanHome(Class<T> beanType, Context namingContext ) {
		return (T) this.session.getBeanHome(beanType, namingContext);
	}
	
	/**
	 * Close transaction context by releasing all allocated remote resources and
	 * naming context.
	 *   
	 */
	public void close() {
		logger.debug("Closing Amdocs transaction context [" + this + "]");
		
		// remove all allocated beans
		for (EJBObject bean : beans) {
			try {
				bean.remove();
			} catch (Throwable t) {
				logger.warn("Error removing EJBObject: " + t.getMessage(), t);
			}
		}

		// close naming context
		try {
			namingContext.close();
		} catch (Throwable t) {
			logger.warn("Error closing naming context: " + t.getMessage(), t);
		}
		
		logger.debug("Amdocs transaction context [" + this + "] closed successfully.");
	}
}
