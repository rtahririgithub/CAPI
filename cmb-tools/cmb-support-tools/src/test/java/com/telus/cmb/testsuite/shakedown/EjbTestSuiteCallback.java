package com.telus.cmb.testsuite.shakedown;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.telus.cmb.common.shakedown.EjbShakedown;
import com.telus.cmb.shakedown.info.ServerInfo;
import com.telus.cmnb.armx.agent.shakedown.ShakedownResult;

public abstract class EjbTestSuiteCallback {
	private EjbShakedown ejbTestSuite;
	
	public EjbTestSuiteCallback (EjbShakedown ejbTestSuite) {
		this.ejbTestSuite = ejbTestSuite;
	}
	
	public List<ShakedownResult> execute(ServerInfo server) {
		Hashtable<Object, Object> env = new Hashtable<Object, Object>();
		List<ShakedownResult> shakedownResultList = new ArrayList<ShakedownResult>();
		try {
			env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
			env.put(Context.PROVIDER_URL, server.getNodeUrl(false));
			env.put(Context.SECURITY_PRINCIPAL, "ejb_user");
			env.put(Context.SECURITY_CREDENTIALS, "ejb_user");
			InitialContext cx = new InitialContext(env);
			ejbTestSuite.lookupEjb(cx);
			return testcase();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			env.clear();
		}
		
		return shakedownResultList;

	}
	public abstract List<ShakedownResult> testcase();
}
