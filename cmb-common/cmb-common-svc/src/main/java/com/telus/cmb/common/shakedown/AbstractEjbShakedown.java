package com.telus.cmb.common.shakedown;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.telus.api.ApplicationException;
import com.telus.cmnb.armx.agent.shakedown.ShakedownResult;
import com.telus.eas.framework.info.TestPointResultInfo;

public abstract class AbstractEjbShakedown<T> implements EjbShakedown {
	private T ejb;
	private String testPointEjbJndi;
	private List<ShakedownResult> resultList;

	protected AbstractEjbShakedown(String testPointEjbJndi) {
		this.testPointEjbJndi = testPointEjbJndi;
	}
	
	@Override
	public List<ShakedownResult> shakedown() {
		resultList = new ArrayList<ShakedownResult>();
		testVersion();
		testAmdocs();
		testDataSources();
		testWebServices();
		testOtherApi();
		testPackages();
		return resultList;
	}
	
	@Override
	public void testVersion() {
		ShakedownResult resultInfo = createShakedownResult();
		resultInfo.setTestPointName("Version");

		try {
			String version = getVersion();
			if (version == null) {
				throw new Exception("getVersion() return NULL");
			}
			resultInfo.setResultDetail(version);
		}catch (Throwable e) {
			resultInfo.setExceptionDetail(e);
			resultInfo.setPass(false);
		}
		
		this.logResult(resultInfo);
	}

	public String getVersion() {
		String version = "n/a";
		try {
			Method method = getEjb().getClass().getMethod("getVersion");
			version = (String) method.invoke(ejb);
		}catch (Throwable t) {
			version = null;
		}
		return version;
	}
	
	@Override
	public void testAmdocs() {
		System.out.println("Testing Amdocs..");
		ShakedownResult resultInfo = createShakedownResult();
		resultInfo.setTestPointName("AMDOCS");
		try {
			String sessionId = getSessionId(getEjb());
			if (sessionId == null) {
				throw new Exception ("sessionId is null. ejb="+ejb);
			}
			resultInfo.setResultDetail("sessionId="+sessionId);
		}catch (Exception e) {
			resultInfo.setExceptionDetail(e);
			resultInfo.setPass(false);
		}
		
		logResult(resultInfo);
	}

	private String getSessionId(T proxy) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		String sessionId = null;
		if (proxy != null) {
			Method method = proxy.getClass().getMethod("openSession", String.class, String.class, String.class);
			sessionId = (String) method.invoke(proxy, KB_ID, KB_CREDENTIAL, KB_APP);
		}
		return sessionId;
	}

	private void logResult(TestPointResultInfo resultInfo) {
		ShakedownResult result = new ShakedownResult();
		result.setTestPointName(resultInfo.getTestPointName());
		result.setPass(resultInfo.isPass());
		result.setTimestamp(resultInfo.getTimestamp());
		result.setResultDetail(resultInfo.getResultDetail());
		result.setExceptionDetail(resultInfo.getExceptionDetail());
		logResult(result);
	}
	
	private void logResult(ShakedownResult result) {
		if (resultList == null) {
			resultList = new ArrayList<ShakedownResult>();
		}
		resultList.add(result);
	}
	
	protected ShakedownResult createShakedownResult() {
		ShakedownResult resultInfo = new ShakedownResult();
		resultInfo.setPass(true);
		resultInfo.setTimestamp(new Date());
		return resultInfo;
	}

	protected TestPointResultInfo executeTest(String testName, TestPointExecutionCallback callback) {
		if (callback != null) {
			return callback.execute(testName);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected T getEjb() throws NamingException {
		if (ejb == null) {
			InitialContext ctx = null;
			try { 
				ctx = new InitialContext();
				ejb = (T)ctx.lookup(testPointEjbJndi);
			} finally {
				if (ctx != null) {
					ctx.close();
				}
			}
		}
		return ejb;
	}
	
	public void lookupEjb(InitialContext ctx) throws NamingException {
		if (ejb == null) {
			try { 
				ejb = (T)ctx.lookup(testPointEjbJndi);
			} finally {
				if (ctx != null) {
					ctx.close();
				}
			}
		}
	}

	protected abstract class TestPointExecutionCallback {
		protected abstract TestPointResultInfo executeTestMethod() throws Throwable;
		
		public TestPointResultInfo execute(String testName) {
			TestPointResultInfo resultInfo = null;
			try {
				resultInfo = executeTestMethod();
			} catch (Throwable t) {
				if (resultInfo == null) {
					resultInfo = new TestPointResultInfo();
				}
				resultInfo.setPass(false);
				resultInfo.setTimestamp(new Date());
				resultInfo.setExceptionDetail(t);
			} finally {
				if (resultInfo != null) {
					if (resultInfo.getTestPointName() == null) {
						resultInfo.setTestPointName(testName);
					}
					logResult(resultInfo);
				}
			}
			return resultInfo;
		}

	}
		
	public String openSession(String userId, String password, String applicationId) throws ApplicationException {
		return null;
	}

	@Override
	public List<ShakedownResult> getResultList() {
		return resultList;
	}
	
	
	
}
