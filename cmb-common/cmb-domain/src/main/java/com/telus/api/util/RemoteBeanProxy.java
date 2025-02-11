/*
 *  Copyright (c) 2004 TELUS Communications Inc.,
 *  All Rights Reserved.
 *
 *  This document contains proprietary information that shall be
 *  distributed or routed only within TELUS, and its authorized
 *  clients, except with written permission of TELUS.
 *
 */
package com.telus.api.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.telus.api.SystemException;

/**
 * @author Pavel Simonovsky
 *
 */
public class RemoteBeanProxy implements InvocationHandler {
	
	private Object beanInstance = null;
	
	private String jndiName = null;
	
	private String providerURL = null;
	
	private String principal = "";
	
	private String credentials = "";
	
	private Class[] constructorTypes;
	
	private Object[] constructorArguments;
	
	private String sessionId = null;

	private String sessionPrincipal = "";
	
	private String sessionCredentials = "";
	
	private String sessionApplicationCode = "";
	
	
	public RemoteBeanProxy(String jndiName, String providerURL) {
		this(jndiName, providerURL, "", "", null, null);
	}

	public RemoteBeanProxy(String jndiName, String providerURL, Class[] constructorTypes, Object[] constructorArguments) {
		this(jndiName, providerURL, "", "", constructorTypes, constructorArguments);
	}

	public RemoteBeanProxy(String jndiName, String providerURL, Class[] constructorTypes, Object[] constructorArguments,
			String sessionPrincipal, String sessionCredentials, String sessionApplicationCode) {
		this(jndiName, providerURL, "", "", constructorTypes, constructorArguments, sessionPrincipal, sessionCredentials, sessionApplicationCode);
	}
	
	public RemoteBeanProxy(String jndiName, String providerURL, String principal, String credentials, Class[] constructorTypes, Object[] constructorArguments) {
		this(jndiName, providerURL, principal, credentials, constructorTypes, constructorArguments, "", "", "");
	}

	public RemoteBeanProxy(String jndiName, String providerURL, String principal, String credentials, Class[] constructorTypes, Object[] constructorArguments, 
			String sessionPrincipal, String sessionCredentials, String sessionApplicationCode) {
		this.jndiName = jndiName;
		this.providerURL = providerURL;
		this.principal = principal;
		this.credentials = credentials;
		this.constructorTypes = constructorTypes;
		this.constructorArguments = constructorArguments;
		this.sessionPrincipal = sessionPrincipal;
		this.sessionCredentials = sessionCredentials;
		this.sessionApplicationCode = sessionApplicationCode;
	}
	
	public String getSessionId() throws Throwable {
		if (sessionId == null){
			Class[] argTypes = new Class[]{String.class, String.class, String.class};
			Method openSessionMethod = getBeanInstance().getClass().getMethod("openSession", argTypes);
			sessionId = (String)openSessionMethod.invoke(beanInstance, new Object[]{sessionPrincipal, sessionCredentials, sessionApplicationCode});
		}
		return sessionId;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		Object result = null;
		
		try {
			
			result = invokeRemoteMethod(getBeanInstance(), method, args);
			
		} catch (InvocationTargetException invocationTargetException) {
			if (invocationTargetException.getTargetException() != null){
				if (invocationTargetException.getTargetException() instanceof java.rmi.RemoteException || //Runtime exception thrown from EJB is always wrapped as RemoteException since we're using the Remote Interface 
					invocationTargetException.getTargetException() instanceof EJBException) { 
					Throwable t = invocationTargetException.getTargetException();
					if (t.getCause() != null && t.getCause() instanceof SystemException) { //currently SystemException is the only known RuntimeException we throw
						throw t.getCause();
					}else { //if the RuntimeException is some other exception, we should wrap it with SystemException. This may have to change if we have another type of SystemException equivalent later on.
						throw new SystemException ("", t.getMessage(), "", t); //wrap RemoteException as SystemException, otherwise it will be returned as java.lang.reflect.UndeclaredThrowableException
					}
				}
				throw invocationTargetException.getTargetException();
			} else {
				throw invocationTargetException;
			}
			
		} catch (NamingException namingException) {
			throw new SystemException ("", namingException.getMessage(), "", namingException); //wrap NamingException as SystemException, otherwise it will be returned as java.lang.reflect.UndeclaredThrowableException		
		} catch (Exception e) {
		
			e.printStackTrace();
			
			dispose();
		
			result = invokeRemoteMethod(getBeanInstance(), method, args);
		}

		return result;
	}
	
	private Object invokeRemoteMethod(Object bean, Method method, Object[] args) throws Throwable {
		Method targetMethod = method;
		if (!method.getDeclaringClass().isInstance(bean)) { 
			targetMethod = getBeanInstance().getClass().getMethod(method.getName(), method.getParameterTypes());
		}
		return targetMethod.invoke(bean, args);
	}
	
	private Object getBeanInstance() throws Exception {
		if (beanInstance == null) {
			beanInstance = newBeanInstance();
		}
		return beanInstance;
	}
	
	private Object newBeanInstance() throws Exception {

		Hashtable environment = new Hashtable();
		
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
		environment.put(Context.PROVIDER_URL, providerURL);
		environment.put(Context.SECURITY_PRINCIPAL, principal);
		environment.put(Context.SECURITY_CREDENTIALS, credentials);
		

		Context context = null;
		
		try {
			context = new InitialContext(environment);

			Object instance = context.lookup(jndiName);

			System.out.println("Service REF for [" + jndiName + "] = [" + instance + "]");
			
			// check if the instance is a home interface
			
			try {

				Method createMethod = instance.getClass().getMethod("create", constructorTypes);
				return createMethod.invoke(instance, constructorArguments);
				
			} catch (Exception e) {
				// home is a business interface alreasy
			}
			
			return instance;
			
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}
	
	public synchronized void dispose() {
		try {
			EJBObject ejb = (EJBObject) beanInstance;
			if (ejb != null) {
				ejb.remove();
			}
		} catch (Throwable e) {
		} finally {
			beanInstance = null;
		}
	}
	
}
