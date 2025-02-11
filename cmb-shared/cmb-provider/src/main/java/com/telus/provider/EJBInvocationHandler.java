/*
 * $Id$
 * %E% %W%
 * Copyright (c) Telus Mobility Inc. All Rights Reserved.
 */

package com.telus.provider;


/**
 * @deprecated to be removed
 * @author tongts
 *
 */
public class EJBInvocationHandler {
//public class EJBInvocationHandler implements InvocationHandler, Serializable {
//
//	private static final Class OBJECT_CLASS = Object.class;
//
//	public static final EJBObject getInstance(boolean threadSafe, String name, Class homeInterfaceClass, Class remoteInterfaceClass) throws Throwable {
//		return getInstance(threadSafe, ProviderUrlReader.getInstance().getProviderUrl(), name, homeInterfaceClass, remoteInterfaceClass);	  
//	}
//
//	/**
//	 * Added for non-Telus-ECA EJBs
//	 * 
//	 * @param providerUrl
//	 * @param name
//	 * @param homeInterfaceClass
//	 * @param remoteInterfaceClass
//	 * @return
//	 * @throws Throwable
//	 */
//	public static final EJBObject getInstance(String providerUrl, String name, Class homeInterfaceClass, Class remoteInterfaceClass) throws Throwable {   
//		return getInstance (false, providerUrl, name, homeInterfaceClass, remoteInterfaceClass);
//	}
//
//	public static EJBObject getInstance(boolean threadSafe, String providerUrl, String name, Class homeInterfaceClass, Class remoteInterfaceClass) throws Throwable {
//		EJBInvocationHandler handler;
//
//		if (threadSafe) {
//			handler = new SynchronizedEJBInvocationHandler(providerUrl, name, homeInterfaceClass);
//		} else {
//			handler = new EJBInvocationHandler(providerUrl, name, homeInterfaceClass);
//		}
//
//		return (EJBObject) Proxy.newProxyInstance(remoteInterfaceClass.getClassLoader(), new Class[] { remoteInterfaceClass }, handler);
//	}
//
//
//	public static final EJBObject getInstance(boolean threadSafe, String name, Class homeInterfaceClass, Class remoteInterfaceClass, String user, String password, String application) throws Throwable {
//		return getInstance(threadSafe, ProviderUrlReader.getInstance().getProviderUrl(), name, homeInterfaceClass, remoteInterfaceClass, user, password, application);
//	}
//
//	public static EJBObject getInstance(boolean threadSafe, String providerURL, String name, Class homeInterfaceClass, Class remoteInterfaceClass, String user, String password, String application) throws Throwable {
//		EJBInvocationHandler handler;
//
//		if (threadSafe) {
//			handler = new SynchronizedEJBInvocationHandler(providerURL, name, homeInterfaceClass, user, password, application);
//		}
//		else {
//			handler = new EJBInvocationHandler(providerURL, name, homeInterfaceClass, user, password, application);
//		}
//
//		return (EJBObject) Proxy.newProxyInstance(remoteInterfaceClass.getClassLoader(), new Class[]{remoteInterfaceClass}, handler);
//	}
//
//	private final String name;
//	private final Class interfaceClass;
//	private final Class[] arumentClasses;
//	private final Object[] arguments;
//
//	// Added to support the Weblogic 8.1 environment - R. Fong, 2004/5/5.
//	private final String initialContextFactory;
//	private final String providerURL;
//
//	// Added the following static variables to store the principal name and credentials for the EAS
//	// user account - R. Fong, 2004/10/20.
//	public static final String securityPrincipalName = "ejb_user";
//	public static final String securityCredentials = "ejb_user";
//
//	/**
//	 *@link aggregation
//	 */
//	private EJBObject ejb;
//
//
//	protected EJBInvocationHandler(String providerUrl, String name, Class interfaceClass) {
//		this(providerUrl, name, interfaceClass, new Class[0], new Object[0]);
//	}
//
//	protected EJBInvocationHandler(String providerURL, String name, Class interfaceClass, String user, String password, String application) {
//		this(providerURL, name, interfaceClass,
//				new Class[]{String.class, String.class, String.class},
//				new Object[]{user, password, application});
//	}
//
//	private EJBInvocationHandler(String providerUrl, String name, Class interfaceClass, Class[] arumentClasses, Object[] arguments) {
//		this.name = name;
//		this.interfaceClass = interfaceClass;
//		this.arumentClasses = arumentClasses;
//		this.arguments = arguments;
//		//set the default value of InitialContextFactory
//		this.initialContextFactory = System.getProperty("com.telus.provider.initialContextFactory", "weblogic.jndi.WLInitialContextFactory");
//		this.providerURL = providerUrl;
//	}
//
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		long startTime = System.currentTimeMillis();
//		try {
//
//			try {
//				return invoke0(proxy, method, args);
//			} catch (java.rmi.NoSuchObjectException e) {
//				disconnect();
//				return invoke0(proxy, method, args);
//			} catch (java.rmi.RemoteException e) {
//				disconnect();
//				throw e;
//			} catch (Throwable e) {          
//				if (TMProvider.messageContains(e, "PR_SYS_9007")) {
//					throw new ServiceFailureException("WIN is not available", e, "WIN", ServiceFailureException.SERVICE_PREPAID_ACCOUNT_MANAGER);
//				} else if (TMProvider.messageContains(e, "PR_SYS_9999")) {
//					throw new ServiceFailureException("Prepaid API is not available", e, "Prepaid API", ServiceFailureException.SERVICE_PREPAID_ACCOUNT_MANAGER);
//				} // Catch Amdocs communication errors (ID=SYS00022) here, disconnect and try to reconnect at least once - R. Fong, 2005/01/31.
//				else if (TMProvider.messageContains(e, "SYS00022")) {
//					Logger.debug0("Knowbility communication error - disconnecting from EJB " + name + "...");
//					disconnect();
//					Logger.debug0("Reinvoking remote method call to EJB " + name + "...");
//					return invoke0(proxy, method, args);
//				} else {
//					throw e;
//				}
//			}
//		} finally {
//			long endTime = System.currentTimeMillis();
//			TMProvider.addMethodCall(method, endTime-startTime);
//		}
//	}
//
//	public final Object invoke0(Object proxy, Method method, Object[] args) throws Throwable {
//
//		if (method.getDeclaringClass() == OBJECT_CLASS)  {
//			String methodName = method.getName();
//			if (methodName.equals("hashCode"))  {
//				return proxyHashCode(proxy);
//			} else if (methodName.equals("equals")) {
//				return proxyEquals(proxy, args[0]);
//			} else if (methodName.equals("toString")) {
//				return proxyToString(proxy);
//			}
//		}
//
//		if(ejb == null) {
//			connect();
//		}
//
//		try {
//			return method.invoke(ejb, args);
//		} catch(InvocationTargetException e) {
//			throw e.getTargetException();
//		}
//	}
//
//	public synchronized void connect() throws Throwable {
//
//		//----------------------------------------------------------
//		// Since a waiting thread has already connected, just
//		// short-circuit. Yeah, yeah, double-checked locking isn't
//		// supposed to work, but it usually does.
//		//----------------------------------------------------------
//		if(ejb != null) {
//			return;
//		}
//
//		//TMProvider.debug0("Connecting to EJB " + name + "...");
//		try {
//			Hashtable env = new Hashtable();
//
//			// Added to support the Weblogic 8.1 environment - R. Fong, 2004/5/5.
//			env.put(Context.INITIAL_CONTEXT_FACTORY, initialContextFactory);
//			env.put(Context.PROVIDER_URL, providerURL);
//			//TMProvider.debug("Setting the EAS user name and credentials.");
//			env.put(Context.SECURITY_PRINCIPAL, securityPrincipalName);
//			env.put(Context.SECURITY_CREDENTIALS, securityCredentials);
//
//			Context context = new javax.naming.InitialContext(env);
//
//			try {
//				Object home   = PortableRemoteObject.narrow(context.lookup(name), interfaceClass);
//				Class  clazz  = home.getClass();
//				Method method = clazz.getMethod("create", arumentClasses);
//				this.ejb = (EJBObject)method.invoke(home, arguments);
//			} catch(InvocationTargetException e) {
//				Throwable cause = e.getTargetException();
//				if(cause != null) {
//					throw cause;
//				} else {
//					throw e;
//				}
//			} finally {
//				context.close();
//			}
//		} catch(javax.naming.NamingException e) {
//			throw new TelusAPIException("Naming server may be down; EJB=[" + name + "]", e);
//		} catch(TelusException e) {
//			if("SYS00011".equals(e.id)) {
//				throw new AuthenticationException("EJB=[" + name + "]", e);
//			} else {
//				throw new TelusAPIException("EJB=[" + name + "]", e);
//			}
//			//} catch(java.lang.reflect.UndeclaredThrowableException e) {
//			//  throw new TelusAPIException("EJB=[" + name + "]", e.getUndeclaredThrowable());
//		} catch(Throwable e) {
//			System.err.println("    ----------" + e.getClass());
//			throw new TelusAPIException("EJB=[" + name + "]", e);
//		}
//	}
//
//	public synchronized void disconnect() throws RemoteException, RemoveException {
//		try {
//			if (ejb != null) {
//				ejb.remove();
//			}
//		} catch (Throwable e) {
//			Logger.debug0("Disconnecting from EJB " + name + "...");
//			Logger.debug0(e);
//		} finally {
//			ejb = null;
//		}
//	}
//
//	protected void finalize() throws Throwable {
//		try {
//			disconnect();
//		} finally {
//			super.finalize();
//		}
//	}
//
//	protected Integer proxyHashCode(Object proxy)  {
//		return new Integer(System.identityHashCode(proxy));
//	}
//
//	protected Boolean proxyEquals(Object proxy, Object other)  {
//		return (proxy == other ? Boolean.TRUE : Boolean.FALSE);
//	}
//
//	protected String proxyToString(Object proxy)  {
//		return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
//	}
//
}




