package com.telus.cmb.utility.app;

import com.telus.cmb.common.shakedown.AbstractEjbShakedown;
import com.telus.cmb.utility.configurationmanager.svc.ConfigurationManagerTestPoint;
import com.telus.eas.framework.info.TestPointResultInfo;


public class ConfigurationManagerShakedown extends AbstractEjbShakedown<ConfigurationManagerTestPoint> implements ConfigurationManagerTestPoint {

	public ConfigurationManagerShakedown() {
		super(CONFIGURATION_MANAGER_TESTPOINT);
	}

	@Override
	public void testAmdocs(){
		//Amdocs test not required 
	}

	@Override
	public void testDataSources() {
		testEasDataSource();
		testServDataSource();
	}

	@Override
	public void testWebServices() {
	}

	@Override
	public void testOtherApi() {
	}

	@Override
	public void testPackages() {
		getLogUtilityPkgVersion();		
	}

	@Override
	public TestPointResultInfo testEasDataSource() {
		return executeTest("EAS DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testEasDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo testServDataSource() {
		return executeTest("SERV DataSource", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().testServDataSource();
			}
		});
	}

	@Override
	public TestPointResultInfo getLogUtilityPkgVersion() {
		return executeTest("Log Utility PKG", new TestPointExecutionCallback() {
			@Override
			protected TestPointResultInfo executeTestMethod() throws Throwable {
				return getEjb().getLogUtilityPkgVersion();
			}
		});
	}
}
